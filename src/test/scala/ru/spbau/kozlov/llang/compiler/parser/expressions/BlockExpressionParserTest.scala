package ru.spbau.kozlov.llang.compiler.parser.expressions

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.TypedIdentifierExpressionNode
import ru.spbau.kozlov.llang.ast.expressions.{BlockExpressionNode, ValDeclarationExpressionNode}
import ru.spbau.kozlov.llang.ast.types.IntTypeNode
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.compiler.parser.{AbstractParserTest, ParserError}
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.BlockExpressionContext

/**
 * @author adkozlov
 */
class BlockExpressionParserTest extends AbstractParserTest[BlockExpressionContext] {
  @Test
  @throws(classOf[Exception])
  def testEmptyBlock() {
    assertEquals(BlockExpressionNode(), doTest("{}").node)
  }

  @Test
  @throws(classOf[Exception])
  def testOnlySemicolons() {
    assertEquals(BlockExpressionNode(), doTest("{; ; ;}").node)
  }

  @Test
  @throws(classOf[Exception])
  def testNotEmptyBlock() {
    assertEquals(BlockExpressionNode(List(AbstractCompilerTest.FOO_INT_FORTY_TWO)), doTest("{val foo: Int = 42}").node)
  }

  @Test
  @throws(classOf[Exception])
  def testSemicolons() {
    assertEquals(BlockExpressionNode(List(AbstractCompilerTest.FOO_INT_FORTY_TWO)), doTest("{; ; ;val foo: Int = 42 ; ; ;}").node)
  }

  @Test
  @throws(classOf[Exception])
  def testMultiStatementBlock() {
    assertEquals(BlockExpressionNode(List(AbstractCompilerTest.FOO_INT_FORTY_TWO,
      ValDeclarationExpressionNode(TypedIdentifierExpressionNode(
        AbstractCompilerTest.BAR_LOWER_CASE, IntTypeNode),
        AbstractCompilerTest.FORTY_TWO_LITERAL))),
      doTest("{" + "val foo: Int = 42;" + "val bar: Int = 42" + "}").node)
  }

  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testIncorrectBlock() {
    doTest("{")
  }

  override protected def parse(parser: LLangParser) = parser.blockExpression
}
