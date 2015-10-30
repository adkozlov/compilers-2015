package ru.spbau.kozlov.llang.compiler.parser.expressions

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.expressions.{BaseIfExpressionNode, BlockExpressionNode, IfElseExpressionNode}
import ru.spbau.kozlov.llang.ast.types.TrueBooleanLiteralNode
import ru.spbau.kozlov.llang.compiler.parser.{AbstractParserTest, ParserError}
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.IfExpressionContext

/**
 * @author adkozlov
 */
class IfExpressionParserTest extends AbstractParserTest[IfExpressionContext] {
  @Test
  @throws(classOf[Exception])
  def testIf() {
    assertEquals(BaseIfExpressionNode(TrueBooleanLiteralNode, BlockExpressionNode()), doTest("if (true) {}").node)
  }

  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testIncorrectIf() {
    doTest("if true {}")
  }

  @Test
  @throws(classOf[Exception])
  def testIfElse() {
    assertEquals(IfElseExpressionNode(TrueBooleanLiteralNode, BlockExpressionNode(), BlockExpressionNode()),
      doTest("if (true) {} else {}").node)
  }

  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testIncorrectIfElse() {
    doTest("if (true) else {}")
  }

  override protected def parse(parser: LLangParser) = parser.ifExpression
}
