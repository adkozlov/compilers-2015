package ru.spbau.kozlov.llang.ast.expressions

/**
 * @author adkozlov
 */
trait IfExpressionNode extends ExpressionNode {
  val conditionNode: ExpressionNode
  val ifBlockNode: BlockExpressionNode
}

final case class BaseIfExpressionNode(override val conditionNode: ExpressionNode,
                                      override val ifBlockNode: BlockExpressionNode)
  extends AbstractExpressionNode("If") with IfExpressionNode

final case class IfElseExpressionNode(override val conditionNode: ExpressionNode,
                                      override val ifBlockNode: BlockExpressionNode,
                                      elseBlockNode: BlockExpressionNode)
  extends AbstractExpressionNode("IfElse") with IfExpressionNode
