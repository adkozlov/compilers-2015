package ru.spbau.kozlov.llang.compiler.parser

import ru.spbau.kozlov.llang.ast._
import ru.spbau.kozlov.llang.ast.expressions._
import ru.spbau.kozlov.llang.ast.statements._
import ru.spbau.kozlov.llang.ast.types._
import ru.spbau.kozlov.llang.grammar.LLangBaseListener
import ru.spbau.kozlov.llang.grammar.LLangParser._

import scala.collection.JavaConversions

/**
 * @author adkozlov
 */
object ParserPhaseListener extends LLangBaseListener {
  override def exitProgram(programContext: ProgramContext) {
    programContext.node = ProgramNode(JavaConversions.asScalaBuffer(programContext.topLevelDeclarationStatement)
      .map(_.node).toList)
  }

  override def exitTopLevelDeclarationStatement(statementContext: TopLevelDeclarationStatementContext) {
    statementContext.node = Option(statementContext.functionDeclarationStatement) match {
      case Some(functionDeclarationStatementContext) => functionDeclarationStatementContext.node
      case _ => Option(statementContext.caseClassDeclarationStatement) match {
        case Some(caseClassDeclarationStatementContext) => caseClassDeclarationStatementContext.node
        case _ => null
      }
    }
  }

  override def exitFunctionDeclarationStatement(statementContext: FunctionDeclarationStatementContext) {
    statementContext.node = FunctionDeclarationStatementNode(statementContext.identifier.name,
      statementContext.`type`.node,
      statementContext.typedIdentifierList.node,
      statementContext.blockExpression.node)
  }

  override def exitCaseClassDeclarationStatement(statementContext: CaseClassDeclarationStatementContext) {
    statementContext.node = CaseClassDeclarationStatementNode(statementContext.identifier.name,
      statementContext.typedIdentifierList.node)
  }

  override def exitStatement(statementContext: StatementContext) {
    statementContext.node = Option(statementContext.topLevelDeclarationStatement) match {
      case Some(topLevelDeclarationStatementContext) => topLevelDeclarationStatementContext.node
      case _ => Option(statementContext.expression) match {
        case Some(expressionContext) => expressionContext.node
        case _ => null
      }
    }
  }

  override def exitExpression(expressionContext: ExpressionContext) {
    expressionContext.node = Option(expressionContext.primaryExpression) match {
      case Some(primaryExpressionContext) => primaryExpressionContext.node
      case _ => Option(expressionContext.variableDeclarationExpression) match {
        case Some(variableDeclarationExpressionContext) => variableDeclarationExpressionContext.node
        case _ => Option(expressionContext.blockExpression) match {
          case Some(blockExpressionContext) => blockExpressionContext.node
          case _ => Option(expressionContext.ifExpression) match {
            case Some(ifExpressionContext) => ifExpressionContext.node
            case _ => Option(expressionContext.whileExpression) match {
              case Some(whileExpressionContext) => whileExpressionContext.node
              case _ => Option(expressionContext.returnExpression) match {
                case Some(returnExpressionContext) => returnExpressionContext.node
                case _ =>
                  JavaConversions.asScalaBuffer(expressionContext.expression).map(_.node).toList match {
                    case expressionNode :: Nil => Option(expressionContext.expressionList) match {
                      case Some(expressionListContext) =>
                        val expressionListNode = expressionListContext.node
                        Option(expressionContext.NEW) match {
                          case Some(_) => ConstructorInvocationNode(expressionNode, expressionListNode)
                          case _ => FunctionInvocationNode(expressionNode, expressionListNode)
                        }
                      case _ => Option(expressionContext.innerAccess) match {
                        case Some(innerAccessContext) => ExpressionInnerAccessNode(expressionNode, innerAccessContext.node)
                        case _ => Option(expressionContext.ADD_OP) match {
                          case Some(_) => expressionNode
                          case _ => Option(expressionContext.SUB_OP) match {
                            case Some(_) => ArithmeticNegationOperationNode(expressionNode)
                            case _ => Option(expressionContext.NOT_OP) match {
                              case Some(_) => BooleanNegationOperationNode(expressionNode)
                              case _ => null
                            }
                          }
                        }
                      }
                    }
                    case firstOperandNode :: secondOperandNode :: Nil =>
                      Option(expressionContext.MUL_OP) match {
                        case Some(_) => MultiplicationOperationNode(firstOperandNode, secondOperandNode)
                        case _ => Option(expressionContext.DIV_OP) match {
                          case Some(_) => DivisionOperationNode(firstOperandNode, secondOperandNode)
                          case _ => Option(expressionContext.MOD_OP) match {
                            case Some(_) => ModuloOperationNode(firstOperandNode, secondOperandNode)
                            case _ => Option(expressionContext.ADD_OP) match {
                              case Some(_) => AdditionOperationNode(firstOperandNode, secondOperandNode)
                              case _ => Option(expressionContext.SUB_OP) match {
                                case Some(_) => SubtractionOperationNode(firstOperandNode, secondOperandNode)
                                case _ => Option(expressionContext.LE) match {
                                  case Some(_) => LeOperationNode(firstOperandNode, secondOperandNode)
                                  case _ => Option(expressionContext.LEQ) match {
                                    case Some(_) => LEqOperationNode(firstOperandNode, secondOperandNode)
                                    case _ => Option(expressionContext.GT) match {
                                      case Some(_) => GtOperationNode(firstOperandNode, secondOperandNode)
                                      case _ => Option(expressionContext.GEQ) match {
                                        case Some(_) => GEqOperationNode(firstOperandNode, secondOperandNode)
                                        case _ => Option(expressionContext.EQ) match {
                                          case Some(_) => EqOperationNode(firstOperandNode, secondOperandNode)
                                          case _ => Option(expressionContext.NEQ) match {
                                            case Some(_) => NEqOperationNode(firstOperandNode, secondOperandNode)
                                            case _ => Option(expressionContext.AND_OP) match {
                                              case Some(_) => AndOperationNode(firstOperandNode, secondOperandNode)
                                              case _ => Option(expressionContext.XOR_OP) match {
                                                case Some(_) => XorOperationNode(firstOperandNode, secondOperandNode)
                                                case _ => Option(expressionContext.OR_OP) match {
                                                  case Some(_) => OrOperationNode(firstOperandNode, secondOperandNode)
                                                  case _ => Option(expressionContext.ASSIGN) match {
                                                    case Some(_) => AssignmentOperationNode(firstOperandNode, secondOperandNode)
                                                    case _ => null
                                                  }
                                                }
                                              }
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    case _ => null
                  }
              }
            }
          }
        }
      }
    }
  }

  override def exitVariableDeclarationExpression(expressionContext: VariableDeclarationExpressionContext) {
    val typedIdentifierExpressionNode = expressionContext.typedIdentifier.node
    val expressionNode = expressionContext.expression.node

    expressionContext.node = Option(expressionContext.VAL) match {
      case Some(_) => ValDeclarationExpressionNode(typedIdentifierExpressionNode, expressionNode)
      case _ => Option(expressionContext.VAR) match {
        case Some(_) => VarDeclarationExpressionNode(typedIdentifierExpressionNode, expressionNode)
        case _ => null
      }
    }
  }

  override def exitBlockExpression(expressionContext: BlockExpressionContext) {
    expressionContext.node = BlockExpressionNode(JavaConversions.asScalaBuffer(expressionContext.statement).map(_.node).toList)
  }

  override def exitIfExpression(expressionContext: IfExpressionContext) {
    val expressionNode = expressionContext.expression.node
    expressionContext.node = JavaConversions.asScalaBuffer(expressionContext.blockExpression).map(_.node).toList match {
      case blockStatementNode :: Nil => BaseIfExpressionNode(expressionNode, blockStatementNode)
      case blockStatementNode :: elseBlockStatementNode :: Nil =>
        IfElseExpressionNode(expressionNode, blockStatementNode, elseBlockStatementNode)
      case _ => null
    }
  }

  override def exitWhileExpression(expressionContext: WhileExpressionContext) {
    expressionContext.node = WhileExpressionNode(expressionContext.expression.node, expressionContext.blockExpression.node)
  }

  override def exitReturnExpression(expressionContext: ReturnExpressionContext) {
    expressionContext.node = ReturnExpressionNode(Option(expressionContext.expression).map(_.node))
  }

  override def exitInnerAccess(accessContext: InnerAccessContext) {
    accessContext.node = Option(accessContext.identifier) match {
      case Some(identifierContext) => IdentifierAccessNode(identifierContext.name)
      case _ => Option(accessContext.integerLiteral) match {
        case Some(integerLiteralContext) => IndexAccessNode(integerLiteralContext.value)
        case _ => null
      }
    }
  }

  override def exitExpressionList(listContext: ExpressionListContext) {
    listContext.node = ExpressionListNode(JavaConversions.asScalaBuffer(listContext.expression)
      .map(_.node).toList)
  }

  override def exitPrimaryExpression(expressionContext: PrimaryExpressionContext) {
    expressionContext.node = JavaConversions.asScalaBuffer(expressionContext.expression).map(_.node).toList match {
      case Nil => Option(expressionContext.identifier) match {
        case Some(identifierContext) => IdentifierExpressionNode(identifierContext.name)
        case _ => Option(expressionContext.booleanLiteral) match {
          case Some(booleanLiteralContext) => booleanLiteralContext.value match {
            case true => TrueBooleanLiteralNode
            case _ => FalseBooleanLiteralNode
          }
          case _ => Option(expressionContext.integerLiteral) match {
            case Some(integerLiteralContext) => IntegerLiteralNode(integerLiteralContext.value)
            case _ => Option(expressionContext.stringLiteral) match {
              case Some(stringLiteralContext) => StringLiteralNode(stringLiteralContext.value)
              case _ => null
            }
          }
        }
      }
      case expressionNode :: Nil => ParenExpressionNode(expressionNode)
      case expressionNodes => ExpressionListNode(expressionNodes)
    }
  }

  override def exitBooleanLiteral(literalContext: BooleanLiteralContext) {
    literalContext.value = literalContext.getText == "true"
  }

  override def exitIntegerLiteral(literalContext: IntegerLiteralContext) {
    literalContext.value = literalContext.getText.toInt
  }

  override def exitStringLiteral(literalContext: StringLiteralContext) {
    literalContext.value = literalContext.getText
  }

  override def exitTypedIdentifier(identifierContext: TypedIdentifierContext) {
    identifierContext.node = TypedIdentifierExpressionNode(identifierContext.identifier.name,
      identifierContext.`type`.node)
  }

  override def exitTypedIdentifierList(listContext: TypedIdentifierListContext) {
    listContext.node = TypedIdentifierListNode(JavaConversions.asScalaBuffer(listContext.typedIdentifier)
      .map(_.node).toList)
  }

  override def exitIdentifier(identifierContext: IdentifierContext) {
    identifierContext.name = identifierContext.id.getText
  }

  override def exitType(typeContext: TypeContext) {
    typeContext.node = Option(typeContext.builtInType) match {
      case Some(builtInTypeContext) => builtInTypeContext.node
      case _ => Option(typeContext.clazz) match {
        case Some(classContext) => classContext.node
        case _ => Option(typeContext.tuple) match {
          case Some(tupleContext) => tupleContext.node
          case _ => null
        }
      }
    }
  }

  override def exitBuiltInType(typeContext: BuiltInTypeContext) {
    typeContext.node = Option(typeContext.ANY) match {
      case Some(_) => AnyTypeNode
      case _ => Option(typeContext.ANY_VAL) match {
        case Some(_) => AnyValTypeNode
        case _ => Option(typeContext.INT) match {
          case Some(_) => IntTypeNode
          case _ => Option(typeContext.BOOLEAN) match {
            case Some(_) => BooleanTypeNode
            case _ => Option(typeContext.STRING) match {
              case Some(_) => StringTypeNode
              case _ => Option(typeContext.UNIT) match {
                case Some(_) => UnitTypeNode
                case _ => null
              }
            }
          }
        }
      }
    }
  }

  override def exitClazz(classContext: ClazzContext) {
    classContext.node = ClassNode(classContext.identifier.name)
  }

  override def exitTuple(tupleContext: TupleContext) {
    tupleContext.node = TupleNode(JavaConversions.asScalaBuffer(tupleContext.`type`()).map(_.node).toList)
  }
}
