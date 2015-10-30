package ru.spbau.kozlov.llang.compiler.parser.expressions

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.expressions.{ExpressionListNode, ParenExpressionNode}
import ru.spbau.kozlov.llang.ast.types.{FalseBooleanLiteralNode, TrueBooleanLiteralNode}
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.compiler.parser.{AbstractParserTest, ParserError}
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.PrimaryExpressionContext

/**
 * @author adkozlov
 */
class PrimaryExpressionParserTest extends AbstractParserTest[PrimaryExpressionContext] {
  @Test
  @throws(classOf[Exception])
  def testIdentifier() {
    assertEquals(AbstractCompilerTest.FOO_IDENTIFIER, doTest(AbstractCompilerTest.FOO_LOWER_CASE).node)
  }

  @Test
  @throws(classOf[Exception])
  def testIntegerLiteral() {
    assertEquals(AbstractCompilerTest.FORTY_TWO_LITERAL, doTest("42").node)
  }

  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testIncorrectIntegerLiteral() {
    doTest("42.")
  }

  @Test
  @throws(classOf[Exception])
  def testBooleanLiterals() {
    assertEquals(TrueBooleanLiteralNode, doTest("true").node)
    assertEquals(FalseBooleanLiteralNode, doTest("false").node)
  }

  @Test
  @throws(classOf[Exception])
  def testStringLiteral() {
    assertEquals(AbstractCompilerTest.FOO_LITERAL, doTest("\"foo\"").node)
  }

  @Test
  @throws(classOf[Exception])
  def testParenthesisExpression() {
    assertEquals(ParenExpressionNode(AbstractCompilerTest.FORTY_TWO_LITERAL), doTest("(42)").node)
  }

  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testEmptyParenthesisExpression() {
    doTest("()")
  }

  @Test
  @throws(classOf[Exception])
  def testTuple() {
    assertEquals(ExpressionListNode(List(
      AbstractCompilerTest.FORTY_TWO_LITERAL,
      AbstractCompilerTest.FORTY_TWO_LITERAL)),
      doTest("(42, 42)").node)
  }

  override protected def parse(parser: LLangParser) = parser.primaryExpression
}
