package ru.spbau.kozlov.llang.compiler.parser.types

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.types.ClassNode
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.compiler.parser.{AbstractParserTest, ParserError}
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.ClazzContext

/**
 * @author adkozlov
 */
class ClassParserTest extends AbstractParserTest[ClazzContext] {
  @Test
  @throws(classOf[Exception])
  def testClass() {
    assertEquals(new ClassNode(AbstractCompilerTest.FOO_CAMEL_CASE), doTest(AbstractCompilerTest.FOO_CAMEL_CASE).node)
  }

  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testIncorrectClass() {
    doTest(Integer.toString(AbstractCompilerTest.FORTY_TWO))
  }

  override protected def parse(parser: LLangParser) = parser.clazz
}
