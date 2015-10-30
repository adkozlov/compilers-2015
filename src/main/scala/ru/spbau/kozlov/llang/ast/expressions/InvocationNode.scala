package ru.spbau.kozlov.llang.ast.expressions

/**
 * @author adkozlov
 */
trait InvocationNode extends ExpressionNode {
  val callableNode: ExpressionNode
  val expressionListNode: ExpressionListNode
}

abstract class AbstractInvocationNode(name: String,
                                      val callableNode: ExpressionNode,
                                      val expressionListNode: ExpressionListNode)
  extends AbstractExpressionNode(name) with InvocationNode

final case class FunctionInvocationNode(override val callableNode: ExpressionNode,
                                        override val expressionListNode: ExpressionListNode)
  extends AbstractInvocationNode("FunctionInvocation", callableNode, expressionListNode)

final case class ConstructorInvocationNode(override val callableNode: ExpressionNode,
                                           override val expressionListNode: ExpressionListNode)
  extends AbstractInvocationNode("ConstructorInvocation", callableNode, expressionListNode)
