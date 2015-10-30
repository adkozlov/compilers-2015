package ru.spbau.kozlov.llang.compiler.parser.expressions

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.expressions.{BlockExpressionNode, WhileExpressionNode}
import ru.spbau.kozlov.llang.ast.types.TrueBooleanLiteralNode
import ru.spbau.kozlov.llang.compiler.parser.{AbstractParserTest, ParserError}
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.WhileExpressionContext

/**
 * @author adkozlov
 */
class WhileExpressionParserTest extends AbstractParserTest[WhileExpressionContext] {
  @Test
  @throws(classOf[Exception])
  def testWhile() {
    assertEquals(WhileExpressionNode(TrueBooleanLiteralNode, BlockExpressionNode()),
      doTest("while (true) {}").node)
  }

  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testIncorrectWhile() {
    doTest("while true {}")
  }

  override protected def parse(parser: LLangParser) = parser.whileExpression
}
