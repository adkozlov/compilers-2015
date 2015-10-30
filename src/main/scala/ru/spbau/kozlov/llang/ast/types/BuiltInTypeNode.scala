package ru.spbau.kozlov.llang.ast.types

import ru.spbau.kozlov.llang.ast.AbstractASTNode
import ru.spbau.kozlov.llang.ast.expressions.{AbstractExpressionNode, ExpressionNode}
import ru.spbau.kozlov.llang.types._

/**
 * @author adkozlov
 */
trait BuiltInTypeNode extends TypeNode

abstract class AbstractBuiltInTypeNode(name: String) extends AbstractASTNode(name) with BuiltInTypeNode {
  val typeName = name
}

case object AnyTypeNode extends AbstractBuiltInTypeNode(AnyType.name) {
  override def toType = AnyType
}

case object AnyValTypeNode extends AbstractBuiltInTypeNode(AnyValType.name) {
  override def toType = AnyValType
}

case object BooleanTypeNode extends AbstractBuiltInTypeNode(BooleanType.name) {
  override def toType = BooleanType
}

trait BooleanLiteralNode extends ExpressionNode {
  val value: Boolean
}

case object TrueBooleanLiteralNode extends AbstractExpressionNode("TrueBooleanLiteral") with BooleanLiteralNode {
  override val value = true
}

case object FalseBooleanLiteralNode extends AbstractExpressionNode("FalseBooleanLiteral") with BooleanLiteralNode {
  override val value = false
}

case object IntTypeNode extends AbstractBuiltInTypeNode(IntType.name) {
  override def toType = IntType
}

case class IntegerLiteralNode(value: Int) extends AbstractExpressionNode("IntegerLiteral")

case object StringTypeNode extends AbstractBuiltInTypeNode(StringType.name) {
  override def toType = StringType
}

case class StringLiteralNode(value: String) extends AbstractExpressionNode("StringLiteral")

case object UnitTypeNode extends AbstractBuiltInTypeNode(UnitType.name) {
  override def toType = UnitType
}
