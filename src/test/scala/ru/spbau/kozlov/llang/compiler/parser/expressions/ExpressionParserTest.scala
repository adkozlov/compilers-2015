package ru.spbau.kozlov.llang.compiler.parser.expressions

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.expressions._
import ru.spbau.kozlov.llang.ast.types.{FalseBooleanLiteralNode, TrueBooleanLiteralNode}
import ru.spbau.kozlov.llang.ast.{ExpressionInnerAccessNode, IdentifierAccessNode, IndexAccessNode}
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.compiler.parser.{AbstractParserTest, ParserError}
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.ExpressionContext

/**
 * @author adkozlov
 */
class ExpressionParserTest extends AbstractParserTest[ExpressionContext] {
  @Test
  @throws(classOf[Exception])
  def testInnerAccess() {
    assertEquals(ExpressionInnerAccessNode(AbstractCompilerTest.FOO_IDENTIFIER,
      IdentifierAccessNode(AbstractCompilerTest.FOO_LOWER_CASE)),
      doTest("foo.foo").node)
    assertEquals(ExpressionInnerAccessNode(AbstractCompilerTest.FOO_IDENTIFIER, IndexAccessNode(1)),
      doTest("foo._1").node)
  }

  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testIncorrectIdentifierAccessNode() {
    doTest("foo.1")
  }

  @Test
  @throws(classOf[Exception])
  def testExpressionList() {
    assertEquals(FunctionInvocationNode(AbstractCompilerTest.FOO_IDENTIFIER, ExpressionListNode(List(
      AbstractCompilerTest.FORTY_TWO_LITERAL, AbstractCompilerTest.FORTY_TWO_LITERAL))),
      doTest("foo(42, 42)").node)
  }

  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testIncorrectExpressionList() {
    doTest("foo(42 42)")
  }

  @Test
  @throws(classOf[Exception])
  def testPrefixOperations() {
    assertEquals(AbstractCompilerTest.FORTY_TWO_LITERAL, doTest("+42").node)
    assertEquals(ArithmeticNegationOperationNode(AbstractCompilerTest.FORTY_TWO_LITERAL), doTest("-42").node)
    assertEquals(BooleanNegationOperationNode(TrueBooleanLiteralNode), doTest("!true").node)
  }

  @Test
  @throws(classOf[Exception])
  def testBooleanOperations() {
    assertEquals(OrOperationNode(TrueBooleanLiteralNode, FalseBooleanLiteralNode), doTest("true || false").node)
    assertEquals(XorOperationNode(TrueBooleanLiteralNode, FalseBooleanLiteralNode), doTest("true ^ false").node)
    assertEquals(AndOperationNode(TrueBooleanLiteralNode, FalseBooleanLiteralNode), doTest("true && false").node)
  }

  @Test
  @throws(classOf[Exception])
  def testComparisonOperations() {
    assertEquals(EqOperationNode(AbstractCompilerTest.FORTY_TWO_LITERAL, AbstractCompilerTest.FORTY_TWO_LITERAL),
      doTest("42 == 42").node)
    assertEquals(NEqOperationNode(AbstractCompilerTest.FORTY_TWO_LITERAL, AbstractCompilerTest.FORTY_TWO_LITERAL),
      doTest("42 != 42").node)
    assertEquals(LeOperationNode(AbstractCompilerTest.FORTY_TWO_LITERAL, AbstractCompilerTest.FORTY_TWO_LITERAL),
      doTest("42 < 42").node)
    assertEquals(LEqOperationNode(AbstractCompilerTest.FORTY_TWO_LITERAL, AbstractCompilerTest.FORTY_TWO_LITERAL),
      doTest("42 <= 42").node)
    assertEquals(GtOperationNode(AbstractCompilerTest.FORTY_TWO_LITERAL, AbstractCompilerTest.FORTY_TWO_LITERAL),
      doTest("42 > 42").node)
    assertEquals(GEqOperationNode(AbstractCompilerTest.FORTY_TWO_LITERAL, AbstractCompilerTest.FORTY_TWO_LITERAL),
      doTest("42 >= 42").node)
  }

  @Test
  @throws(classOf[Exception])
  def testArithmeticOperations() {
    assertEquals(AdditionOperationNode(AbstractCompilerTest.FORTY_TWO_LITERAL, AbstractCompilerTest.FORTY_TWO_LITERAL),
      doTest("42 + 42").node)
    assertEquals(SubtractionOperationNode(AbstractCompilerTest.FORTY_TWO_LITERAL, AbstractCompilerTest.FORTY_TWO_LITERAL),
      doTest("42 - 42").node)
    assertEquals(MultiplicationOperationNode(AbstractCompilerTest.FORTY_TWO_LITERAL, AbstractCompilerTest.FORTY_TWO_LITERAL),
      doTest("42 * 42").node)
    assertEquals(DivisionOperationNode(AbstractCompilerTest.FORTY_TWO_LITERAL, AbstractCompilerTest.FORTY_TWO_LITERAL),
      doTest("42 / 42").node)
    assertEquals(ModuloOperationNode(AbstractCompilerTest.FORTY_TWO_LITERAL, AbstractCompilerTest.FORTY_TWO_LITERAL),
      doTest("42 % 42").node)
    assertEquals(AdditionOperationNode(AbstractCompilerTest.FOO_LITERAL, AbstractCompilerTest.FOO_LITERAL),
      doTest("\"foo\" + \"foo\"").node)
  }

  @Test
  @throws(classOf[Exception])
  def testAssignment() {
    assertEquals(AssignmentOperationNode(AbstractCompilerTest.FOO_IDENTIFIER, AbstractCompilerTest.FORTY_TWO_LITERAL),
      doTest("foo = 42").node)
  }

  override protected def parse(parser: LLangParser) = parser.expression
}
