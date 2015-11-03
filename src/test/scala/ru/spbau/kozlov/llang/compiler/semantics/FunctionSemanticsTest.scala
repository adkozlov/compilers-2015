package ru.spbau.kozlov.llang.compiler.semantics

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.TypedIdentifierListNode
import ru.spbau.kozlov.llang.ast.expressions.BlockExpressionNode
import ru.spbau.kozlov.llang.ast.statements.FunctionDeclarationStatementNode
import ru.spbau.kozlov.llang.ast.types.{IntTypeNode, IntegerLiteralNode, UnitTypeNode}
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.compiler.environment.VariablesEnvironment
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.FunctionDeclarationStatementContext
import ru.spbau.kozlov.llang.types.IntType

/**
 * @author adkozlov
 */
class FunctionSemanticsTest extends AbstractSemanticsTest[FunctionDeclarationStatementContext] {
  @Test
  @throws(classOf[Exception])
  def testFunctionResultType() {
    val literalNode = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    literalNode.`type` = Some(IntType)

    val blockNode = BlockExpressionNode(List(literalNode), Some(VariablesEnvironment()))
    blockNode.`type` = Some(IntType)

    val expected = FunctionDeclarationStatementNode(AbstractCompilerTest.FOO_LOWER_CASE, IntTypeNode,
      TypedIdentifierListNode(), blockNode)

    val actual = doTest("def foo(): Int = { 42 }").node

    assertEquals(expected, actual)
    assertEquals(expected.blockNode.`type`, actual.blockNode.`type`)
  }

  @Test(expected = classOf[TypeConfirmationError])
  @throws(classOf[Exception])
  def testResultTypeConformationError() {
    doTest("def foo(): Int = {}")
  }

  @Test
  @throws(classOf[Exception])
  def testResultUnitTypeConformation() {
    val literalNode = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    literalNode.`type` = Some(IntType)

    val blockNode = BlockExpressionNode(List(literalNode), Some(VariablesEnvironment()))
    blockNode.`type` = Some(IntType)

    val expected = FunctionDeclarationStatementNode(AbstractCompilerTest.FOO_LOWER_CASE, UnitTypeNode,
      TypedIdentifierListNode(), blockNode)

    val actual = doTest("def foo(): Unit = { 42 }").node

    assertEquals(expected, actual)
    assertEquals(expected.blockNode.`type`, actual.blockNode.`type`)
  }

  override protected def parse(parser: LLangParser) = parser.functionDeclarationStatement
}
