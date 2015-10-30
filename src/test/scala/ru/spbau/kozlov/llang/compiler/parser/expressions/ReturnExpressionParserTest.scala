package ru.spbau.kozlov.llang.compiler.parser.expressions

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.expressions.ReturnExpressionNode
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.compiler.parser.AbstractParserTest
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.ReturnExpressionContext

/**
 * @author adkozlov
 */
class ReturnExpressionParserTest extends AbstractParserTest[ReturnExpressionContext] {
  @Test
  @throws(classOf[Exception])
  def testReturn() {
    assertEquals(ReturnExpressionNode(Some(AbstractCompilerTest.FORTY_TWO_LITERAL)), doTest("return 42").node)
  }

  @Test
  @throws(classOf[Exception])
  def testIncorrectReturn() {
    assertEquals(ReturnExpressionNode(), doTest("return").node)
  }

  override protected def parse(parser: LLangParser) = parser.returnExpression
}
