package ru.spbau.kozlov.llang.compiler.parser.statements

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.expressions.BlockExpressionNode
import ru.spbau.kozlov.llang.ast.statements.{CaseClassDeclarationStatementNode, FunctionDeclarationStatementNode}
import ru.spbau.kozlov.llang.ast.types.{BooleanTypeNode, IntTypeNode}
import ru.spbau.kozlov.llang.ast.{TypedIdentifierExpressionNode, TypedIdentifierListNode}
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.compiler.parser.{AbstractParserTest, ParserError}
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.TopLevelDeclarationStatementContext

/**
 * @author adkozlov
 */
class TopLevelDeclarationStatementParserTest extends AbstractParserTest[TopLevelDeclarationStatementContext] {
  @Test
  @throws(classOf[Exception])
  def testFunctionDeclaration() {
    assertEquals(
      FunctionDeclarationStatementNode(AbstractCompilerTest.FOO_LOWER_CASE, IntTypeNode,
        TypedIdentifierListNode(List(AbstractCompilerTest.FOO_INT)),
        BlockExpressionNode()),
      doTest("def foo(foo: Int): Int = {}").node)
  }

  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testIncorrectFunctionDeclaration() {
    doTest("foo()")
  }

  @Test
  @throws(classOf[Exception])
  def testCaseClassDeclaration() {
    assertEquals(CaseClassDeclarationStatementNode(AbstractCompilerTest.FOO_CAMEL_CASE,
      TypedIdentifierListNode(List(
        AbstractCompilerTest.FOO_INT,
        TypedIdentifierExpressionNode(AbstractCompilerTest.BAR_LOWER_CASE, BooleanTypeNode)))),
      doTest("case class Foo(foo: Int, bar: Boolean)").node)
  }

  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testIncorrectCaseClassDeclaration() {
    doTest("case class Foo")
  }

  override protected def parse(parser: LLangParser) = parser.topLevelDeclarationStatement
}
