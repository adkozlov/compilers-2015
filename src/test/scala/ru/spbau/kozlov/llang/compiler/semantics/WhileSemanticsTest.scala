package ru.spbau.kozlov.llang.compiler.semantics

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.expressions.{BlockExpressionNode, WhileExpressionNode}
import ru.spbau.kozlov.llang.ast.types.TrueBooleanLiteralNode
import ru.spbau.kozlov.llang.compiler.environment.VariablesEnvironment
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.WhileExpressionContext
import ru.spbau.kozlov.llang.types.UnitType

/**
 * @author adkozlov
 */
class WhileSemanticsTest extends AbstractSemanticsTest[WhileExpressionContext] {
  @Test
  @throws(classOf[Exception])
  def testWhile() {
    val blockNode = BlockExpressionNode(variablesEnvironment = Some(VariablesEnvironment()))
    blockNode.`type` = Some(UnitType)

    val expected = WhileExpressionNode(TrueBooleanLiteralNode, blockNode)
    expected.`type` = Some(UnitType)

    val actual = doTest("while (true) {}").node

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
    assertEquals(expected.conditionNode.`type`, actual.conditionNode.`type`)
    assertEquals(expected.blockNode.`type`, actual.blockNode.`type`)
  }

  @Test(expected = classOf[TypeMismatchError])
  @throws(classOf[Exception])
  def testConditionType() {
    doTest("while (42) {}")
  }

  override protected def parse(parser: LLangParser) = parser.whileExpression
}
