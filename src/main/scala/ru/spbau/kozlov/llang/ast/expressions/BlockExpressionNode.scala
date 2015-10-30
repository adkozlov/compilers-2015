package ru.spbau.kozlov.llang.ast.expressions

import ru.spbau.kozlov.llang.ast.statements.StatementNode
import ru.spbau.kozlov.llang.compiler.environment.VariablesEnvironment

import scala.collection.JavaConversions

/**
 * @author adkozlov
 */
final case class BlockExpressionNode(statementNodes: List[StatementNode] = Nil,
                                     var variablesEnvironment: Option[VariablesEnvironment] = None)
  extends AbstractExpressionNode("Block") {

  def this(nodes: java.util.List[StatementNode]) {
    this(JavaConversions.asScalaBuffer(nodes).toList)
  }
}
