package ru.spbau.kozlov.llang.ast.expressions

/**
 * @author adkozlov
 */
trait UnaryOperationNode extends ExpressionNode {
  val operandNode: ExpressionNode
}

abstract class AbstractUnaryOperationNode(name: String, val operandNode: ExpressionNode)
  extends AbstractExpressionNode(name) with UnaryOperationNode

final case class ArithmeticNegationOperationNode(override val operandNode: ExpressionNode)
  extends AbstractUnaryOperationNode("ArithmeticNegationOperation", operandNode)

final case class BooleanNegationOperationNode(override val operandNode: ExpressionNode)
  extends AbstractUnaryOperationNode("BooleanNegationOperation", operandNode)