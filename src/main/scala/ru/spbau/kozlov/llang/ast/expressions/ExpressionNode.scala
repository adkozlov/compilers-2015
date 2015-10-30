package ru.spbau.kozlov.llang.ast.expressions

import ru.spbau.kozlov.llang.ast.AbstractASTNode
import ru.spbau.kozlov.llang.ast.statements.StatementNode
import ru.spbau.kozlov.llang.types.Type

/**
 * @author adkozlov
 */
trait ExpressionNode extends StatementNode {
  var `type`: Option[Type] = None
}

abstract class AbstractExpressionNode(name: String) extends AbstractASTNode(name) with ExpressionNode

final case class ParenExpressionNode(expressionNode: ExpressionNode) extends AbstractExpressionNode("ParenExpression")

final case class ExpressionListNode(expressionNodes: List[ExpressionNode]) extends AbstractExpressionNode("ExpressionList")
