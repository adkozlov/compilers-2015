package ru.spbau.kozlov.llang.compiler.semantics

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.expressions.ReturnExpressionNode
import ru.spbau.kozlov.llang.ast.types.IntegerLiteralNode
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.ReturnExpressionContext
import ru.spbau.kozlov.llang.types.{IntType, UnitType}

/**
 * @author adkozlov
 */
class ReturnSemanticsTest extends AbstractSemanticsTest[ReturnExpressionContext] {
  @Test
  @throws(classOf[Exception])
  def tesEmptyReturn() {
    val expected = ReturnExpressionNode()
    expected.`type` = Some(UnitType)

    val actual = doTest("return").node

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
  }

  @Test
  @throws(classOf[Exception])
  def testNonEmptyReturn() {
    val literalNode = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    literalNode.`type` = Some(IntType)

    val expected = ReturnExpressionNode(Some(literalNode))
    expected.`type` = Some(IntType)

    val actual = doTest("return 42").node

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
  }

  override protected def parse(parser: LLangParser) = parser.returnExpression
}
