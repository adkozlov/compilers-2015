package ru.spbau.kozlov.llang.compiler.parser.types

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.types._
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.compiler.parser.{AbstractParserTest, ParserError}
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.BuiltInTypeContext

/**
 * @author adkozlov
 */
class BuiltInTypeParserTest extends AbstractParserTest[BuiltInTypeContext] {
  @Test
  @throws(classOf[Exception])
  def testAnyType() {
    assertEquals(AnyTypeNode, doTest("Any").node)
  }

  @Test
  @throws(classOf[Exception])
  def testAnyValType() {
    assertEquals(AnyValTypeNode, doTest("AnyVal").node)
  }

  @Test
  @throws(classOf[Exception])
  def testIntType() {
    assertEquals(IntTypeNode, doTest("Int").node)
  }

  @Test
  @throws(classOf[Exception])
  def testBooleanType() {
    assertEquals(BooleanTypeNode, doTest("Boolean").node)
  }

  @Test
  @throws(classOf[Exception])
  def testStringType() {
    assertEquals(StringTypeNode, doTest("String").node)
  }

  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testIncorrectType() {
    doTest(AbstractCompilerTest.FOO_CAMEL_CASE)
  }

  override protected def parse(parser: LLangParser) = parser.builtInType
}
