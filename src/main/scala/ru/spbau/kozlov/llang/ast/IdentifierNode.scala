package ru.spbau.kozlov.llang.ast

import ru.spbau.kozlov.llang.ast.expressions.{AbstractExpressionNode, ExpressionNode}
import ru.spbau.kozlov.llang.ast.types.TypeNode

/**
 * @author adkozlov
 */
trait IdentifierNode extends ExpressionNode {
  val id: String
}

abstract class AbstractIdentifierNode(name: String) extends AbstractExpressionNode(name) with IdentifierNode

final case class IdentifierExpressionNode(id: String) extends AbstractIdentifierNode("IdentifierExpression")

final case class TypedIdentifierExpressionNode(id: String, typeNode: TypeNode)
  extends AbstractIdentifierNode("TypedIdentifier")

final case class TypedIdentifierListNode(typedIdentifierNodes: List[TypedIdentifierExpressionNode] = Nil)
  extends AbstractExpressionNode("TypedIdentifierList")

trait InnerAccessNode extends IdentifierNode

abstract class AbstractInnerAccessNode(name: String) extends AbstractIdentifierNode(name) with InnerAccessNode

final case class IdentifierAccessNode(id: String) extends AbstractInnerAccessNode("IdentifierAccess")

final case class IndexAccessNode(index: Int) extends AbstractInnerAccessNode("IndexAccess") {
  override val id = s"_$index"
}

final case class ExpressionInnerAccessNode(expressionNode: ExpressionNode, innerAccessNode: InnerAccessNode)
  extends AbstractExpressionNode("ExpressionInnerAccess") with IdentifierNode {
  override val id: String = s"${expressionNode.name}.${innerAccessNode.id}"
}
