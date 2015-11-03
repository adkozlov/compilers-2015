package ru.spbau.kozlov.llang.compiler.environment

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.expressions.BlockExpressionNode
import ru.spbau.kozlov.llang.ast.statements.{CaseClassDeclarationStatementNode, FunctionDeclarationStatementNode}
import ru.spbau.kozlov.llang.ast.types.UnitTypeNode
import ru.spbau.kozlov.llang.ast.{ProgramNode, TypedIdentifierExpressionNode, TypedIdentifierListNode}
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.ProgramContext
import ru.spbau.kozlov.llang.types._

/**
 * @author adkozlov
 */
class ProgramEnvironmentTest extends AbstractEnvironmentTest[ProgramContext] {
  @Test(expected = classOf[AlreadyDefinedInScopeError])
  @throws(classOf[Exception])
  def testFunctionsCollision() {
    doTest("def foo(i: Int): Int = {};" + "def foo(i: Int): Boolean = {}")
  }

  @Test
  @throws(classOf[Exception])
  def testParameterPolymorphism() {
    val topLevelEnvironment = TopLevelEnvironment()
    topLevelEnvironment.addFunction(AbstractCompilerTest.FOO_LOWER_CASE, FunctionType(Nil, UnitType))
    topLevelEnvironment.addFunction(AbstractCompilerTest.FOO_LOWER_CASE, FunctionType(List(IntType), UnitType))

    val variablesEnvironment = VariablesEnvironment()
    variablesEnvironment.addVariable(AbstractCompilerTest.FOO_LOWER_CASE, IntType)

    val expected = ProgramNode(List(
      FunctionDeclarationStatementNode(AbstractCompilerTest.FOO_LOWER_CASE, UnitTypeNode,
        TypedIdentifierListNode(),
        BlockExpressionNode(variablesEnvironment = Some(VariablesEnvironment()))),
      FunctionDeclarationStatementNode(AbstractCompilerTest.FOO_LOWER_CASE, UnitTypeNode,
        TypedIdentifierListNode(List(AbstractCompilerTest.FOO_INT)),
        BlockExpressionNode(variablesEnvironment = Some(VariablesEnvironment(variablesEnvironment))))),
      Some(topLevelEnvironment))

    assertEquals(expected, doTest("def foo(): Unit = {};" + "def foo(foo: Int): Unit = {}").node)
  }

  @Test(expected = classOf[AlreadyDefinedInScopeError])
  @throws(classOf[Exception])
  def testCaseClassesCollision() {
    doTest("case class Foo();" + "case class Foo()")
  }

  @Test
  @throws(classOf[Exception])
  def testNotBuiltInParameterType() {
    val topLevelEnvironment = TopLevelEnvironment()
    topLevelEnvironment.addCaseClass(AbstractCompilerTest.FOO_CAMEL_CASE, AbstractCompilerTest.FOO_CLASS_TYPE)
    topLevelEnvironment.addFunction(AbstractCompilerTest.FOO_LOWER_CASE, FunctionType(List(
      AbstractCompilerTest.FOO_CLASS_TYPE),
      UnitType))

    val variablesEnvironment = VariablesEnvironment()
    variablesEnvironment.addVariable(AbstractCompilerTest.FOO_LOWER_CASE, AbstractCompilerTest.FOO_CLASS_TYPE)

    val expected = ProgramNode(List(
      CaseClassDeclarationStatementNode(AbstractCompilerTest.FOO_CAMEL_CASE, TypedIdentifierListNode()),
      FunctionDeclarationStatementNode(AbstractCompilerTest.FOO_LOWER_CASE, UnitTypeNode,
        TypedIdentifierListNode(List(
          TypedIdentifierExpressionNode(AbstractCompilerTest.FOO_LOWER_CASE, AbstractCompilerTest.FOO_CLASS_NODE))),
        BlockExpressionNode(variablesEnvironment = Some(VariablesEnvironment(variablesEnvironment))))),
      Some(topLevelEnvironment))

    assertEquals(expected, doTest("case class Foo();" + "def foo(foo: Foo): Unit = {}").node)
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testNotBuiltInParameterTypeFailure() {
    doTest("def foo(foo: Foo): Unit = {}")
  }

  @Test
  @throws(classOf[Exception])
  def testNotBuiltInReturnType() {
    val topLevelEnvironment = TopLevelEnvironment()
    topLevelEnvironment.addCaseClass(AbstractCompilerTest.FOO_CAMEL_CASE, AbstractCompilerTest.FOO_CLASS_TYPE)
    topLevelEnvironment.addFunction(AbstractCompilerTest.FOO_LOWER_CASE,
      FunctionType(Nil, AbstractCompilerTest.FOO_CLASS_TYPE))

    val expected = ProgramNode(List(
      CaseClassDeclarationStatementNode(AbstractCompilerTest.FOO_CAMEL_CASE, TypedIdentifierListNode()),
      FunctionDeclarationStatementNode(AbstractCompilerTest.FOO_LOWER_CASE, AbstractCompilerTest.FOO_CLASS_NODE,
        TypedIdentifierListNode(), BlockExpressionNode(variablesEnvironment = Some(VariablesEnvironment())))),
      Some(topLevelEnvironment))

    assertEquals(expected, doTest("case class Foo();" + "def foo(): Foo = {}").node)
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testNotBuiltInReturnTypeFailure() {
    doTest("def foo(): Foo = {}")
  }

  @Test
  @throws(classOf[Exception])
  def testNotBuiltInFieldType() {
    val topLevelEnvironment = TopLevelEnvironment()
    topLevelEnvironment.addCaseClass(AbstractCompilerTest.FOO_CAMEL_CASE, AbstractCompilerTest.FOO_CLASS_TYPE)
    topLevelEnvironment.addCaseClass(AbstractCompilerTest.BAR_CAMEL_CASE, ClassType(AbstractCompilerTest.BAR_CAMEL_CASE))

    val expected = ProgramNode(List(
      CaseClassDeclarationStatementNode(AbstractCompilerTest.FOO_CAMEL_CASE, TypedIdentifierListNode()),
      CaseClassDeclarationStatementNode(AbstractCompilerTest.BAR_CAMEL_CASE, TypedIdentifierListNode(List(
        TypedIdentifierExpressionNode(AbstractCompilerTest.FOO_LOWER_CASE, AbstractCompilerTest.FOO_CLASS_NODE))))),
      Some(topLevelEnvironment))

    assertEquals(expected, doTest("case class Foo();" + "case class Bar(foo: Foo)").node)
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testNotBuiltInFieldTypeFailure() {
    doTest("case class Foo(bar: Bar)")
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testUnknownReference() {
    doTest("def foo(): Unit = { bar }")
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testIncorrectFunctionReference() {
    doTest("def foo(): Unit = {}; def bar(): Unit = { foo }")
  }

  @Test(expected = classOf[ReferenceResolutionError])
  @throws(classOf[Exception])
  def testNotEnoughFunctionArgumentsCount() {
    doTest("def foo(foo: Int): Unit = {}; def bar(): Unit = { foo() }")
  }

  @Test(expected = classOf[ReferenceResolutionError])
  @throws(classOf[Exception])
  def testTooManyFunctionArgumentsCount() {
    doTest("def foo(): Unit = {}; def bar(): Unit = { foo(0) }")
  }

  @Test(expected = classOf[ReferenceResolutionError])
  @throws(classOf[Exception])
  def testNotEnoughConstructorArgumentsCount() {
    doTest("case class Foo(foo: Int); def bar(): Unit = { new Foo() }")
  }

  @Test(expected = classOf[ReferenceResolutionError])
  @throws(classOf[Exception])
  def testTooManyConstructorArgumentsCount() {
    doTest("case class Foo(); def foo(): Unit = { new Foo(0) }")
  }

  override protected def parse(parser: LLangParser) = parser.program
}
