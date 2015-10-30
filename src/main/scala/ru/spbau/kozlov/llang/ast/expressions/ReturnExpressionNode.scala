package ru.spbau.kozlov.llang.ast.expressions

/**
 * @author adkozlov
 */
final case class ReturnExpressionNode(expressionNode: Option[ExpressionNode] = None)
  extends AbstractExpressionNode("Return")