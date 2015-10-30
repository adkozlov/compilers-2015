package ru.spbau.kozlov.llang.compiler.environment

import org.antlr.v4.runtime.Token
import ru.spbau.kozlov.llang.grammar.LLangParser._
import ru.spbau.kozlov.llang.types.{ClassType, FunctionType}

import scala.collection.JavaConversions

/**
 * @author adkozlov
 */
final case class EnvironmentFirstPhaseListener() extends EnvironmentPhaseListener {

  override def exitProgram(programContext: ProgramContext) {
    programContext.node.topLevelEnvironment = stack.topLevelEnvironment
  }

  override def exitCaseClassDeclarationStatement(statementContext: CaseClassDeclarationStatementContext) = stack.popVariables()

  override def enterFunctionDeclarationStatement(statementContext: FunctionDeclarationStatementContext) {
    val statementNode = statementContext.node
    val identifierNode = statementNode.typedIdentifierNode
    val name = identifierNode.id
    checkedAddEnvironmentError(stack.addFunction(name, FunctionType(
      statementNode.typedIdentifierListNode.typedIdentifierNodes.map(_.typeNode.toType),
      identifierNode.typeNode.toType)), statementContext, name)

    pushTopVariables()
  }

  override def enterCaseClassDeclarationStatement(statementContext: CaseClassDeclarationStatementContext) {
    val statementNode = statementContext.node
    val className = statementNode.typedIdentifierNode.id
    val classType = ClassType(className)
    classType.constructorArgumentsTypes = statementNode.typedIdentifierListNode.typedIdentifierNodes
      .map(node => (node.id, node.typeNode.toType))
    checkedAddEnvironmentError(stack.addCaseClass(className, classType), statementContext, className)

    pushTopVariables()
  }

  override def exitFunctionDeclarationStatement(statementContext: FunctionDeclarationStatementContext) = stack.popVariables()

  override def exitVariableDeclarationExpression(expressionContext: VariableDeclarationExpressionContext) {
    addVariable(expressionContext.typedIdentifier)
  }

  override def enterBlockExpression(expressionContext: BlockExpressionContext) = pushTopVariables()

  override def exitBlockExpression(expressionContext: BlockExpressionContext) {
    expressionContext.node.variablesEnvironment = stack.popVariables()
  }

  override def exitTypedIdentifierList(listContext: TypedIdentifierListContext) {
    JavaConversions.asScalaBuffer(listContext.typedIdentifier).foreach(addVariable)
  }

  override protected def createEnvironmentError(name: String, token: Token) = AlreadyDefinedInScopeError(name, token)

  private def addVariable(identifierContext: TypedIdentifierContext) {
    val expressionNode = identifierContext.node
    val name = expressionNode.id
    checkedAddEnvironmentError(stack.addVariable(name, expressionNode.typeNode.toType), identifierContext, name)
  }

  private def pushTopVariables() = stack.pushVariables(stack.topVariables())
}
