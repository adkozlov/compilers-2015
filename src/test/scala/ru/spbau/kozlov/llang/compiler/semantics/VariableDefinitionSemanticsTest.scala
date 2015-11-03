package ru.spbau.kozlov.llang.compiler.semantics

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.TypedIdentifierExpressionNode
import ru.spbau.kozlov.llang.ast.expressions.{ValDeclarationExpressionNode, VarDeclarationExpressionNode, VariableDeclarationExpressionNode}
import ru.spbau.kozlov.llang.ast.types.{IntTypeNode, IntegerLiteralNode}
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.VariableDeclarationExpressionContext
import ru.spbau.kozlov.llang.types.{IntType, UnitType}

/**
 * @author adkozlov
 */
class VariableDefinitionSemanticsTest extends AbstractSemanticsTest[VariableDeclarationExpressionContext] {
  @Test
  @throws(classOf[Exception])
  def testValType() {
    val typedIdentifierExpressionNode = TypedIdentifierExpressionNode(AbstractCompilerTest.FOO_LOWER_CASE, IntTypeNode)
    typedIdentifierExpressionNode.`type` = Some(IntType)

    val literalNode = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    literalNode.`type` = Some(IntType)

    val expected = ValDeclarationExpressionNode(typedIdentifierExpressionNode, literalNode)
    expected.`type` = Some(UnitType)

    val actual = doTest("val foo: Int = 42").node

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
    assertEquals(expected.typedIdentifierExpressionNode.`type`, actual.typedIdentifierExpressionNode.`type`)
    assertEquals(expected.expressionNode.`type`, actual.expressionNode.`type`)
  }

  @Test
  @throws(classOf[Exception])
  def testVarType() {
    val typedIdentifierExpressionNode = TypedIdentifierExpressionNode(AbstractCompilerTest.FOO_LOWER_CASE, IntTypeNode)
    typedIdentifierExpressionNode.`type` = Some(IntType)

    val literalNode: IntegerLiteralNode = new IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    literalNode.`type` = Some(IntType)

    val expected = VarDeclarationExpressionNode(typedIdentifierExpressionNode, literalNode)
    expected.`type` = Some(UnitType)

    val actual: VariableDeclarationExpressionNode = doTest("var foo: Int = 42").node

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
    assertEquals(expected.typedIdentifierExpressionNode.`type`, actual.typedIdentifierExpressionNode.`type`)
    assertEquals(expected.expressionNode.`type`, actual.expressionNode.`type`)
  }

  @Test(expected = classOf[TypeConfirmationError])
  @throws(classOf[Exception])
  def testTypeConfirmationError() {
    doTest("val foo: Int = \"\"")
  }

  override protected def parse(parser: LLangParser) = parser.variableDeclarationExpression
}
