package ru.spbau.kozlov.llang.compiler.semantics

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.expressions.BlockExpressionNode
import ru.spbau.kozlov.llang.ast.types.{IntegerLiteralNode, TrueBooleanLiteralNode}
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.compiler.environment.VariablesEnvironment
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.BlockExpressionContext
import ru.spbau.kozlov.llang.types.{IntType, UnitType}

/**
 * @author adkozlov
 */
class BlockSemanticsTest extends AbstractSemanticsTest[BlockExpressionContext] {
  @Test
  @throws(classOf[Exception])
  def testEmptyBlock() {
    val expected = BlockExpressionNode(variablesEnvironment = Some(VariablesEnvironment()))
    expected.`type` = Some(UnitType)

    val actual = doTest("{}").node

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
  }

  @Test
  @throws(classOf[Exception])
  def testNonEmptyBlock() {
    val expected = BlockExpressionNode(List(TrueBooleanLiteralNode,
      IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)), Some(VariablesEnvironment()))
    expected.`type` = Some(IntType)

    val actual = doTest("{true; 42}").node

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
  }

  @Test(expected = classOf[TypeConfirmationError])
  @throws(classOf[Exception])
  def testAssignmentTypeConfirmationError() {
    doTest("{var foo: Int = 42; foo = \"\"}")
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testResolveSymbolError() {
    doTest("{val foo: Int = 42; foo * \"\"}")
  }

  override protected def parse(parser: LLangParser) = parser.blockExpression
}
