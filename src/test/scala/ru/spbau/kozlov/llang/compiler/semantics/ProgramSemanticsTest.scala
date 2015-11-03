package ru.spbau.kozlov.llang.compiler.semantics

import org.junit.Test
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.ProgramContext

/**
 * @author adkozlov
 */
class ProgramSemanticsTest extends AbstractSemanticsTest[ProgramContext] {
  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testIncorrectClassFieldsAccess() {
    doTest("case class Foo(foo: Int); def bar(): Unit = { val foo: Foo = new Foo(0); foo.bar }")
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testIncorrectClassIndexAccess() {
    doTest("case class Foo(foo: Int); def bar(): Unit = { val foo: Foo = new Foo(0); foo._1 }")
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testIncorrectClassTupleAccess() {
    doTest("def foo(): Unit = { val foo: (Boolean, Boolean) = (false, false); foo.bar }")
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testIncorrectTupleZeroIndexAccess() {
    doTest("def foo(): Unit = { val foo: (Boolean, Boolean) = (false, false); foo._0 }")
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testIncorrectTupleIndexAccess() {
    doTest("def foo(): Unit = { val foo: (Boolean, Boolean) = (false, false); foo._3 }")
  }

  override protected def parse(parser: LLangParser) = parser.program
}
