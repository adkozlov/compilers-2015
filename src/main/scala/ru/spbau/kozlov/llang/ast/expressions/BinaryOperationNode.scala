package ru.spbau.kozlov.llang.ast.expressions

/**
 * @author adkozlov
 */
trait BinaryOperationNode extends ExpressionNode {
  val firstOperandNode: ExpressionNode
  val secondOperandNode: ExpressionNode
}

abstract class AbstractBinaryOperationNode(name: String,
                                           val firstOperandNode: ExpressionNode,
                                           val secondOperandNode: ExpressionNode)
  extends AbstractExpressionNode(name) with BinaryOperationNode

// arithmetic expressions
final case class MultiplicationOperationNode(override val firstOperandNode: ExpressionNode,
                                             override val secondOperandNode: ExpressionNode)
  extends AbstractBinaryOperationNode("MultiplicationOperation", firstOperandNode, secondOperandNode)

final case class DivisionOperationNode(override val firstOperandNode: ExpressionNode,
                                       override val secondOperandNode: ExpressionNode)
  extends AbstractBinaryOperationNode("DivisionOperation", firstOperandNode, secondOperandNode)

final case class ModuloOperationNode(override val firstOperandNode: ExpressionNode,
                                     override val secondOperandNode: ExpressionNode)
  extends AbstractBinaryOperationNode("ModuloOperation", firstOperandNode, secondOperandNode)

final case class AdditionOperationNode(override val firstOperandNode: ExpressionNode,
                                       override val secondOperandNode: ExpressionNode)
  extends AbstractBinaryOperationNode("AdditionOperation", firstOperandNode, secondOperandNode)

final case class SubtractionOperationNode(override val firstOperandNode: ExpressionNode,
                                          override val secondOperandNode: ExpressionNode)
  extends AbstractBinaryOperationNode("SubtractionOperation", firstOperandNode, secondOperandNode)

// comparison expressions
final case class LeOperationNode(override val firstOperandNode: ExpressionNode,
                                 override val secondOperandNode: ExpressionNode)
  extends AbstractBinaryOperationNode("LeOperation", firstOperandNode, secondOperandNode)

final case class LEqOperationNode(override val firstOperandNode: ExpressionNode,
                                  override val secondOperandNode: ExpressionNode)
  extends AbstractBinaryOperationNode("LEqOperation", firstOperandNode, secondOperandNode)

final case class GtOperationNode(override val firstOperandNode: ExpressionNode,
                                 override val secondOperandNode: ExpressionNode)
  extends AbstractBinaryOperationNode("GtOperation", firstOperandNode, secondOperandNode)

final case class GEqOperationNode(override val firstOperandNode: ExpressionNode,
                                  override val secondOperandNode: ExpressionNode)
  extends AbstractBinaryOperationNode("GeqOperation", firstOperandNode, secondOperandNode)

final case class EqOperationNode(override val firstOperandNode: ExpressionNode,
                                 override val secondOperandNode: ExpressionNode)
  extends AbstractBinaryOperationNode("EqOperation", firstOperandNode, secondOperandNode)

final case class NEqOperationNode(override val firstOperandNode: ExpressionNode,
                                  override val secondOperandNode: ExpressionNode)
  extends AbstractBinaryOperationNode("NEqOperation", firstOperandNode, secondOperandNode)

// boolean expressions
final case class AndOperationNode(override val firstOperandNode: ExpressionNode,
                                  override val secondOperandNode: ExpressionNode)
  extends AbstractBinaryOperationNode("AndOperation", firstOperandNode, secondOperandNode)

final case class XorOperationNode(override val firstOperandNode: ExpressionNode,
                                  override val secondOperandNode: ExpressionNode)
  extends AbstractBinaryOperationNode("XorOperation", firstOperandNode, secondOperandNode)

final case class OrOperationNode(override val firstOperandNode: ExpressionNode,
                                 override val secondOperandNode: ExpressionNode)
  extends AbstractBinaryOperationNode("OrOperation", firstOperandNode, secondOperandNode)

// assignment
final case class AssignmentOperationNode(override val firstOperandNode: ExpressionNode,
                                         override val secondOperandNode: ExpressionNode)
  extends AbstractBinaryOperationNode("AssignmentOperation", firstOperandNode, secondOperandNode)