package ru.spbau.kozlov.llang.ast.expressions

/**
 * @author adkozlov
 */
final case class WhileExpressionNode(conditionNode: ExpressionNode, blockNode: BlockExpressionNode)
  extends AbstractExpressionNode("While")
