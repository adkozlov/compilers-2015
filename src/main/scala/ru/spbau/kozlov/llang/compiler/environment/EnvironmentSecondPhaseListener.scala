package ru.spbau.kozlov.llang.compiler.environment

import org.antlr.v4.runtime.Token
import ru.spbau.kozlov.llang.ast.IdentifierNode
import ru.spbau.kozlov.llang.ast.expressions.{ConstructorInvocationNode, FunctionInvocationNode}
import ru.spbau.kozlov.llang.grammar.LLangParser._

/**
 * @author adkozlov
 */
final case class EnvironmentSecondPhaseListener() extends EnvironmentPhaseListener {

  override def enterProgram(programContext: ProgramContext) {
    stack.topLevelEnvironment = programContext.node.topLevelEnvironment
  }

  override def enterBlockExpression(expressionContext: BlockExpressionContext) = {
    stack.pushVariables(expressionContext.node.variablesEnvironment)
  }

  override def exitBlockExpression(expressionContext: BlockExpressionContext) {
    stack.popVariables()
  }

  override def exitExpression(expressionContext: ExpressionContext) {
    Option(expressionContext.expressionList) match {
      case Some(expressionListContext) =>
        val identifierContext = expressionContext.expression(0)
        val name = identifierContext.node.asInstanceOf[IdentifierNode].id
        val argumentsCount = expressionListContext.node.expressionNodes.size

        def addReferenceResolutionError() = addCompilationError(ReferenceResolutionError(name, identifierContext.start))

        Option(expressionContext.NEW) match {
          case Some(_) =>
            stack.getCaseClass(name) match {
              case Some(classTypes) => classTypes.constructorArgumentsTypes.size == argumentsCount match {
                case false => addReferenceResolutionError()
                case _ =>
              }
              case _ => checkedAddEnvironmentError(flag = false, expressionContext, name)
            }
          case _ => stack.getFunction(name).toList match {
            case Nil => checkedAddEnvironmentError(flag = false, expressionContext, name)
            case functionTypes => functionTypes.find(_.argumentTypes.size == argumentsCount) match {
              case None => addReferenceResolutionError()
              case _ =>
            }
          }
        }
      case _ =>
    }
  }

  override def exitPrimaryExpression(expressionContext: PrimaryExpressionContext) {
    Option(expressionContext.identifier) match {
      case Some(identifierContext) =>
        val name = identifierContext.name
        val parentContext = expressionContext.getParent.getParent

        def addSymbolResolutionError: Boolean => Unit = checkedAddEnvironmentError(_, identifierContext, name)

        variableIsDefined(name) match {
          case false => functionIsDefined(name) match {
            case false => classIsDefined(name) match {
              case false => addSymbolResolutionError(false)
              case _ => addSymbolResolutionError(parentContext.isInstanceOf[ExpressionContext] &&
                parentContext.asInstanceOf[ExpressionContext].node.isInstanceOf[ConstructorInvocationNode])
            }
            case _ => addSymbolResolutionError(parentContext.isInstanceOf[ExpressionContext] &&
              parentContext.asInstanceOf[ExpressionContext].node.isInstanceOf[FunctionInvocationNode])
          }
          case _ =>
        }
      case _ =>
    }
  }

  override def exitClazz(clazzContext: ClazzContext) {
    val identifierContext = clazzContext.identifier
    val name = identifierContext.name
    checkedAddEnvironmentError(stack.getCaseClass(name).isDefined, identifierContext, name)
  }

  override protected def createEnvironmentError(name: String, token: Token) = SymbolResolutionError(name, token)

  private def variableIsDefined: String => Boolean = stack.getVariable(_).isDefined

  private def functionIsDefined: String => Boolean = stack.getFunction(_).nonEmpty

  private def classIsDefined: String => Boolean = stack.getCaseClass(_).isDefined
}
