package ru.spbau.kozlov.llang.compiler.semantics

import org.antlr.v4.runtime.tree.TerminalNode
import ru.spbau.kozlov.llang.ast.expressions.{ExpressionNode, IfElseExpressionNode}
import ru.spbau.kozlov.llang.ast.{IdentifierAccessNode, IdentifierNode, IndexAccessNode}
import ru.spbau.kozlov.llang.compiler.CompilationPhaseErrorListener
import ru.spbau.kozlov.llang.compiler.environment.EnvironmentStack
import ru.spbau.kozlov.llang.grammar.LLangBaseListener
import ru.spbau.kozlov.llang.grammar.LLangParser._
import ru.spbau.kozlov.llang.types._

import scala.collection.JavaConversions
import scala.collection.immutable.SortedSet

/**
 * @author adkozlov
 */
final case class SemanticsPhaseListener()
  extends LLangBaseListener with CompilationPhaseErrorListener[SemanticsPhaseResult, SemanticsError] {

  private val stack = EnvironmentStack()

  override def caseSucceeded(programContext: ProgramContext) = SemanticsPhaseSucceededResult(programContext)

  override def caseFailed(semanticsErrors: SortedSet[SemanticsError]) = SemanticsPhaseFailedResult(semanticsErrors)

  override def enterProgram(programContext: ProgramContext) {
    stack.topLevelEnvironment = programContext.node.topLevelEnvironment
  }

  override def exitFunctionDeclarationStatement(statementContext: FunctionDeclarationStatementContext) {
    val expectedType = statementContext.node.typedIdentifierExpressionNode.typeNode.toType
    expectedType match {
      case UnitType =>
      case _ =>
        val maybeActualType = statementContext.node.blockNode.`type`
        maybeActualType match {
          case Some(actualType) => actualType.equals(expectedType) match {
            case false => addCompilationError(TypeConfirmationError(actualType, expectedType, statementContext.stop))
            case _ =>
          }
          case _ => addCompilationError(TypeInferenceError(statementContext.stop))
        }
    }
  }

  override def exitExpression(expressionContext: ExpressionContext) {
    val expressionNode = expressionContext.node
    if (expressionContext.primaryExpression != null) {
      expressionNode.`type` = expressionContext.primaryExpression.node.`type`
      return
    }

    if (expressionContext.variableDeclarationExpression != null) {
      expressionNode.`type` = expressionContext.variableDeclarationExpression.node.`type`
      return
    }

    if (expressionContext.blockExpression != null) {
      expressionNode.`type` = expressionContext.blockExpression.node.`type`
      return
    }

    if (expressionContext.ifExpression != null) {
      expressionNode.`type` = expressionContext.ifExpression.node.`type`
      return
    }

    if (expressionContext.whileExpression != null) {
      expressionNode.`type` = expressionContext.whileExpression.node.`type`
      return
    }

    if (expressionContext.returnExpression != null) {
      expressionNode.`type` = expressionContext.returnExpression.node.`type`
      return
    }

    JavaConversions.asScalaBuffer(expressionContext.expression).map(_.node.`type`).toList match {
      case maybeType :: Nil =>
        val identifierContext = expressionContext.expression(0)
        if (expressionContext.expressionList != null) {

          val identifierNode = identifierContext.node.asInstanceOf[IdentifierNode]
          val name = identifierNode.id
          val argumentsTypes = expressionContext.expressionList.node.expressionNodes.map(_.`type`).map(_.orNull)

          def addArgumentsTypeMismatchError(expectedTypes: List[Type]) {
            argumentsTypes.contains(null) match {
              case true => addTypeInferenceError(identifierContext)
              case _ => addCompilationError(ArgumentsTypesMismatch(argumentsTypes, expectedTypes, identifierContext.start))
            }
          }

          expressionNode.`type` = Option(expressionContext.NEW) match {
            case Some(_) =>
              val maybeClassType = stack.getCaseClass(name)
              maybeClassType.find(_.constructorArgumentsTypes.map(_._2).equals(argumentsTypes)) match {
                case Some(constructorArgumentsTypes) => maybeClassType
                case _ =>
                  maybeClassType.map(_.constructorArgumentsTypes).map(_.map(_._2)).foreach(addArgumentsTypeMismatchError)
                  None
              }
            case _ =>
              val functions = stack.getFunction(name)
              functions.find(_.argumentTypes.equals(argumentsTypes)) match {
                case Some(functionType) => Some(functionType.resultType)
                case _ =>
                  functions.map(_.argumentTypes).foreach(addArgumentsTypeMismatchError)
                  None
              }
          }
          return
        }

        if (expressionContext.innerAccess != null) {
          def addSymbolResolutionError() = {
            val accessContext = expressionContext.innerAccess
            addCompilationError(SymbolResolutionError(Option(accessContext.integerLiteral) match {
              case Some(integerLiteralContext) => integerLiteralContext.start
              case _ => accessContext.identifier.start
            }))
          }

          expressionNode.`type` = maybeType match {
            case Some(identifierType) => expressionContext.innerAccess.node match {
              case identifierAccessNode: IdentifierAccessNode =>
                maybeType match {
                  case Some(classType: ClassType) =>
                    stack.getCaseClass(classType.className).orNull.constructorArgumentsTypes.find {
                      case (id, _) => id.equals(identifierAccessNode.id)
                    } match {
                      case result@Some(_) => result.map(_._2)
                      case _ =>
                        addSymbolResolutionError()
                        None
                    }
                  case _ =>
                    addSymbolResolutionError()
                    None
                }
              case indexAccessNode: IndexAccessNode =>
                maybeType match {
                  case Some(tupleType: TupleType) =>
                    val tupleTypes = tupleType.types
                    val index = indexAccessNode.index - 1
                    index >= 0 && index < tupleTypes.size match {
                      case true => Some(tupleTypes(index))
                      case _ =>
                        addSymbolResolutionError()
                        None
                    }
                  case _ =>
                    addSymbolResolutionError()
                    None
                }
              case _ => None
            }
            case _ =>
              addTypeInferenceError(identifierContext)
              None
          }
          return
        }

        if (expressionContext.ADD_OP != null) {
          checkSymbolResolution(maybeType, IntType, expressionContext.ADD_OP)
          expressionNode.`type` = Some(IntType)
          return
        }

        if (expressionContext.SUB_OP != null) {
          checkSymbolResolution(maybeType, IntType, expressionContext.SUB_OP)
          expressionNode.`type` = Some(IntType)
          return
        }

        if (expressionContext.NOT_OP != null) {
          checkSymbolResolution(maybeType, BooleanType, expressionContext.NOT_OP)
          expressionNode.`type` = Some(BooleanType)
          return
        }
      case maybeFirstType :: maybeSecondType :: Nil =>
        val suffixContext = expressionContext.expression(1)
        def checkEqual(terminalNode: TerminalNode, maybeExpectedType: Option[Type] = None) {
          maybeExpectedType match {
            case Some(expectedType) => maybeFirstType match {
              case Some(_) =>
                checkSymbolResolution(maybeSecondType, expectedType, terminalNode)
                checkTypeMatch(expressionContext.expression(0), expectedType)
                checkTypeMatch(suffixContext, expectedType)
              case _ => addTypeInferenceError(expressionContext)
            }
            case _ => maybeFirstType match {
              case Some(actualType) => checkSymbolResolution(maybeSecondType, actualType, terminalNode)
              case _ => addTypeInferenceError(expressionContext)
            }
          }
        }

        def checkEqualToInt(terminalNode: TerminalNode) = {
          checkEqual(terminalNode, Some(IntType))
          Some(IntType)
        }
        if (expressionContext.MUL_OP != null) {
          expressionNode.`type` = checkEqualToInt(expressionContext.MUL_OP)
          return
        }

        if (expressionContext.DIV_OP != null) {
          expressionNode.`type` = checkEqualToInt(expressionContext.DIV_OP)
          return
        }

        if (expressionContext.MOD_OP != null) {
          expressionNode.`type` = checkEqualToInt(expressionContext.MOD_OP)
          return
        }

        if (expressionContext.ADD_OP != null) {
          expressionNode.`type` = maybeFirstType match {
            case Some(_: IntType.type) =>
              maybeSecondType match {
                case Some(_: IntType.type) => Some(IntType)
                case Some(_: StringType.type) => Some(StringType)
                case result =>
                  checkSymbolResolution(maybeSecondType, IntType, expressionContext.ADD_OP)
                  checkTypeMatch(suffixContext, IntType)
                  Some(IntType)
              }
            case Some(_: StringType.type) => Some(StringType)
            case _ =>
              checkTypeMatch(suffixContext, StringType)
              Some(StringType)
          }
          return
        }

        if (expressionContext.SUB_OP != null) {
          expressionNode.`type` = checkEqualToInt(expressionContext.SUB_OP)
          return
        }

        if (expressionContext.LE != null) {
          checkEqual(expressionContext.LE)
          expressionNode.`type` = Some(BooleanType)
          return
        }

        if (expressionContext.LEQ != null) {
          checkEqual(expressionContext.LEQ)
          expressionNode.`type` = Some(BooleanType)
          return
        }

        if (expressionContext.GT != null) {
          checkEqual(expressionContext.GT)
          expressionNode.`type` = Some(BooleanType)
          return
        }

        if (expressionContext.GEQ != null) {
          checkEqual(expressionContext.GEQ)
          expressionNode.`type` = Some(BooleanType)
          return
        }

        if (expressionContext.EQ != null) {
          checkEqual(expressionContext.EQ)
          expressionNode.`type` = Some(BooleanType)
          return
        }

        if (expressionContext.NEQ != null) {
          checkEqual(expressionContext.NEQ)
          expressionNode.`type` = Some(BooleanType)
          return
        }

        def checkEqualToBoolean(terminalNode: TerminalNode) = {
          checkEqual(terminalNode, Some(BooleanType))
          Some(BooleanType)
        }
        if (expressionContext.AND_OP != null) {
          expressionNode.`type` = checkEqualToBoolean(expressionContext.AND_OP)
          return
        }

        if (expressionContext.XOR_OP != null) {
          expressionNode.`type` = checkEqualToBoolean(expressionContext.XOR_OP)
          return
        }

        if (expressionContext.OR_OP != null) {
          expressionNode.`type` = checkEqualToBoolean(expressionContext.OR_OP)
          return
        }

        if (expressionContext.ASSIGN != null) {
          checkTypeConfirmation(suffixContext, maybeFirstType)
          expressionNode.`type` = Some(UnitType)
          return
        }
      case _ =>
    }
  }

  override def exitVariableDeclarationExpression(expressionContext: VariableDeclarationExpressionContext) {
    val expressionNode = expressionContext.node
    checkTypeConfirmation(expressionContext.expression,
      Some(expressionNode.typedIdentifierExpressionNode.typeNode.toType))
    expressionNode.`type` = Some(UnitType)
  }

  override def enterBlockExpression(expressionContext: BlockExpressionContext) {
    stack.pushVariables(expressionContext.node.variablesEnvironment)
  }

  override def exitBlockExpression(expressionContext: BlockExpressionContext) {
    stack.popVariables()
    expressionContext.node.`type` = JavaConversions.asScalaBuffer(expressionContext.statement).reverse.map(_.node)
      .find(_.isInstanceOf[ExpressionNode]) match {
      case Some(expressionNode: ExpressionNode) => expressionNode.`type`
      case _ => Some(UnitType)
    }
  }

  override def exitIfExpression(expressionContext: IfExpressionContext) {
    val ifExpressionNode = expressionContext.node
    checkTypeMatch(expressionContext.expression, BooleanType)
    ifExpressionNode.`type` = Option(ifExpressionNode match {
      case ifElseExpressionNode: IfElseExpressionNode => ifElseExpressionNode.ifBlockNode.`type` match {
        case Some(firstType) => ifElseExpressionNode.elseBlockNode.`type` match {
          case Some(secondType) => firstType.equals(secondType) match {
            case true => firstType
            case _ => AnyType
          }
          case _ => AnyType
        }
        case _ => AnyType
      }
      case _ => UnitType
    })
  }

  override def exitWhileExpression(expressionContext: WhileExpressionContext) {
    checkTypeMatch(expressionContext.expression, BooleanType)
    expressionContext.node.`type` = Some(UnitType)
  }

  override def exitReturnExpression(expressionContext: ReturnExpressionContext) {
    expressionContext.node.`type` = Option(expressionContext.expression) match {
      case Some(resultExpressionContext) => resultExpressionContext.node.`type`
      case _ => Some(UnitType)
    }
  }

  override def exitPrimaryExpression(expressionContext: PrimaryExpressionContext) {
    expressionContext.node.`type` = JavaConversions.asScalaBuffer(expressionContext.expression)
      .map(_.node.`type`).toList match {
      case Nil => Option(expressionContext.identifier) match {
        case Some(identifierContext) => stack.getVariable(identifierContext.name)
        case _ => Option(expressionContext.booleanLiteral) match {
          case Some(_) => Some(BooleanType)
          case _ => Option(expressionContext.integerLiteral) match {
            case Some(_) => Some(IntType)
            case _ => Option(expressionContext.stringLiteral) match {
              case Some(_) => Some(StringType)
              case _ => None
            }
          }
        }
      }
      case expressionType :: Nil => expressionType
      case result => result.exists(_.isEmpty) match {
        case false => Some(TupleType(result.map(_.orNull)))
        case _ => None
      }
    }
  }

  override def exitTypedIdentifier(identifierContext: TypedIdentifierContext) {
    identifierContext.node.`type` = Some(identifierContext.node.typeNode.toType)
  }

  private def checkTypeMatch(context: ExpressionContext, expectedType: Type) {
    context.node.`type` match {
      case Some(actualType) => actualType.equals(expectedType) match {
        case false => addCompilationError(TypeMismatchError(actualType, expectedType, context.start))
        case _ =>
      }
      case _ => addTypeInferenceError(context)
    }
  }

  private def checkTypeConfirmation(context: ExpressionContext, maybeExpectedType: Option[Type]) {
    maybeExpectedType match {
      case Some(expectedType) => context.node.`type` match {
        case Some(actualType) => actualType.equals(expectedType) match {
          case false => addCompilationError(TypeConfirmationError(actualType, expectedType, context.start))
          case _ =>
        }
        case _ => addTypeInferenceError(context)
      }
      case _ => addTypeInferenceError(context)
    }
  }

  private def checkSymbolResolution(maybeActualType: Option[Type], expectedType: Type, terminalNode: TerminalNode) {
    def addSymbolResolutionError() = addCompilationError(SymbolResolutionError(terminalNode.getSymbol))

    maybeActualType match {
      case Some(actualType) => actualType.equals(expectedType) match {
        case false => addSymbolResolutionError()
        case _ =>
      }
      case _ => addSymbolResolutionError()
    }
  }

  private def addTypeInferenceError(context: ExpressionContext) {
    addCompilationError(TypeInferenceError(context.start))
  }
}