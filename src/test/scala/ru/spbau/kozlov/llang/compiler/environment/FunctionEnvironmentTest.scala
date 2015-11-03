package ru.spbau.kozlov.llang.compiler.environment

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.TypedIdentifierListNode
import ru.spbau.kozlov.llang.ast.expressions.BlockExpressionNode
import ru.spbau.kozlov.llang.ast.statements.FunctionDeclarationStatementNode
import ru.spbau.kozlov.llang.ast.types.UnitTypeNode
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.FunctionDeclarationStatementContext
import ru.spbau.kozlov.llang.types.IntType

/**
 * @author adkozlov
 */
class FunctionEnvironmentTest extends AbstractEnvironmentTest[FunctionDeclarationStatementContext] {
  @Test(expected = classOf[AlreadyDefinedInScopeError])
  @throws(classOf[Exception])
  def testFunctionParametersCollisions() {
    doTest("def foo(i: Int, i: Int): Unit = {}")
  }

  @Test
  @throws(classOf[Exception])
  def testVariableShadowing() {
    val variablesEnvironment = VariablesEnvironment()
    variablesEnvironment.addVariable(AbstractCompilerTest.FOO_LOWER_CASE, IntType)

    val expected = FunctionDeclarationStatementNode(AbstractCompilerTest.FOO_LOWER_CASE, UnitTypeNode,
      TypedIdentifierListNode(List(AbstractCompilerTest.FOO_INT)),
      BlockExpressionNode(List(AbstractCompilerTest.FOO_INT_FORTY_TWO), Some(variablesEnvironment)))

    assertEquals(expected, doTest("def foo(foo: Int): Unit = {" + "val foo: Int = 42" + "}").node)
  }

  @Test(expected = classOf[AlreadyDefinedInScopeError])
  @throws(classOf[Exception])
  def testBlockVariableShadowing() {
    doTest("def foo(): Unit = {" + "val i: Int = 42;" + "val i: Int = 42" + "}")
  }

  override protected def parse(parser: LLangParser) = parser.functionDeclarationStatement
}
