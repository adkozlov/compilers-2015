package ru.spbau.kozlov.llang.ast.expressions

import ru.spbau.kozlov.llang.ast.{AbstractASTNode, TypedIdentifierExpressionNode}

/**
 * @author adkozlov
 */
trait VariableDeclarationExpressionNode extends ExpressionNode {
  val typedIdentifierExpressionNode: TypedIdentifierExpressionNode
  val expressionNode: ExpressionNode
}

abstract class AbstractVariableDeclarationExpressionNode(name: String,
                                                         val typedIdentifierExpressionNode: TypedIdentifierExpressionNode,
                                                         val expressionNode: ExpressionNode)
  extends AbstractASTNode(name) with VariableDeclarationExpressionNode

final case class ValDeclarationExpressionNode(override val typedIdentifierExpressionNode: TypedIdentifierExpressionNode,
                                              override val expressionNode: ExpressionNode)
  extends AbstractVariableDeclarationExpressionNode("ValDeclaration", typedIdentifierExpressionNode, expressionNode)

final case class VarDeclarationExpressionNode(override val typedIdentifierExpressionNode: TypedIdentifierExpressionNode,
                                              override val expressionNode: ExpressionNode)
  extends AbstractVariableDeclarationExpressionNode("VarDeclaration", typedIdentifierExpressionNode, expressionNode)
