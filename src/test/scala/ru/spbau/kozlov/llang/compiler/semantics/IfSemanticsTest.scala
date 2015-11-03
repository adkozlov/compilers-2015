package ru.spbau.kozlov.llang.compiler.semantics

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.expressions.{BaseIfExpressionNode, BlockExpressionNode, IfElseExpressionNode}
import ru.spbau.kozlov.llang.ast.types.{IntegerLiteralNode, TrueBooleanLiteralNode}
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.compiler.environment.VariablesEnvironment
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.IfExpressionContext
import ru.spbau.kozlov.llang.types.{IntType, UnitType}

/**
 * @author adkozlov
 */
class IfSemanticsTest extends AbstractSemanticsTest[IfExpressionContext] {
  @Test
  @throws(classOf[Exception])
  def testIf() {
    val ifBlockNode = BlockExpressionNode(variablesEnvironment = Some(VariablesEnvironment()))
    ifBlockNode.`type` = Some(UnitType)

    val expected = BaseIfExpressionNode(TrueBooleanLiteralNode, ifBlockNode)
    expected.`type` = Some(UnitType)

    val expressionNode = doTest("if (true) {}").node
    assertTrue(expressionNode.isInstanceOf[BaseIfExpressionNode])
    val actual = expressionNode.asInstanceOf[BaseIfExpressionNode]

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
    assertEquals(expected.conditionNode.`type`, actual.conditionNode.`type`)
    assertEquals(expected.ifBlockNode.`type`, actual.ifBlockNode.`type`)
  }

  @Test
  @throws(classOf[Exception])
  def testIfElse() {
    val firstLiteralNode: IntegerLiteralNode = new IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    firstLiteralNode.`type` = Some(IntType)

    val ifBlockNode = BlockExpressionNode(List(firstLiteralNode), Some(VariablesEnvironment()))
    ifBlockNode.`type` = Some(IntType)

    val secondLiteralNode = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    secondLiteralNode.`type` = Some(IntType)

    val elseBlockNode = BlockExpressionNode(List(secondLiteralNode), Some(VariablesEnvironment()))
    elseBlockNode.`type` = Some(IntType)

    val expected = IfElseExpressionNode(TrueBooleanLiteralNode, ifBlockNode, elseBlockNode)
    expected.`type` = Some(IntType)

    val expressionNode = doTest("if (true) {42} else {42}").node
    assertTrue(expressionNode.isInstanceOf[IfElseExpressionNode])
    val actual = expressionNode.asInstanceOf[IfElseExpressionNode]

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
    assertEquals(expected.conditionNode.`type`, actual.conditionNode.`type`)
    assertEquals(expected.ifBlockNode.`type`, actual.ifBlockNode.`type`)
    assertEquals(expected.elseBlockNode.`type`, actual.elseBlockNode.`type`)
  }

  @Test(expected = classOf[TypeMismatchError])
  @throws(classOf[Exception])
  def testConditionType() {
    doTest("if (42) {}")
  }

  override protected def parse(parser: LLangParser) = parser.ifExpression
}
