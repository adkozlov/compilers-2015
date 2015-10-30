package ru.spbau.kozlov.llang.compiler.parser.expressions

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.expressions.VarDeclarationExpressionNode
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.compiler.parser.{AbstractParserTest, ParserError}
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.VariableDeclarationExpressionContext

/**
 * @author adkozlov
 */
class VariableDeclarationStatementParserTest extends AbstractParserTest[VariableDeclarationExpressionContext] {
  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testIncorrectValDeclarationStatement() {
    doTest("val foo: Int")
  }

  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testIncorrectVarDeclarationStatement() {
    doTest("var foo: Int")
  }

  @Test
  @throws(classOf[Exception])
  def testInitializedValDeclarationStatement() {
    assertEquals(AbstractCompilerTest.FOO_INT_FORTY_TWO, doTest("val foo: Int = 42").node)
  }

  @Test
  @throws(classOf[Exception])
  def testInitializedVarDeclarationStatement() {
    assertEquals(VarDeclarationExpressionNode(AbstractCompilerTest.FOO_INT, AbstractCompilerTest.FORTY_TWO_LITERAL),
      doTest("var foo: Int = 42").node)
  }

  override protected def parse(parser: LLangParser) = parser.variableDeclarationExpression
}
