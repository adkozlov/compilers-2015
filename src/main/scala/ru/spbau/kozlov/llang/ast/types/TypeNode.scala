package ru.spbau.kozlov.llang.ast.types

import ru.spbau.kozlov.llang.ast.{ASTNode, AbstractASTNode}
import ru.spbau.kozlov.llang.types.{ClassType, TupleType, Type}

/**
 * @author adkozlov
 */
trait TypeNode extends ASTNode {
  val typeName: String

  def toType: Type
}

final case class ClassNode(typeName: String) extends AbstractASTNode(typeName) with TypeNode {
  override def toType = ClassType(typeName)
}

final case class TupleNode(typeNodes: List[TypeNode]) extends AbstractASTNode("Tuple") with TypeNode {
  override val typeName = s"(${typeNodes.mkString(", ")})"

  override def toType = TupleType(typeNodes.map(_.toType))
}
