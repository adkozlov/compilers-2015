package ru.spbau.kozlov.llang.compiler.parser.types

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.types.{IntTypeNode, TupleNode}
import ru.spbau.kozlov.llang.compiler.parser.{AbstractParserTest, ParserError}
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.TupleContext

/**
 * @author adkozlov
 */
class TupleParserTest extends AbstractParserTest[TupleContext] {
  @Test
  @throws(classOf[Exception])
  def testTuple() {
    assertEquals(TupleNode(List(
      IntTypeNode,
      TupleNode(List(IntTypeNode, IntTypeNode)))),
      doTest("(Int, (Int, Int))").node)
  }

  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testEmptyTuple() {
    doTest("()")
  }

  @Test(expected = classOf[ParserError])
  @throws(classOf[Exception])
  def testIncorrectTuple() {
    doTest("Int")
  }

  override protected def parse(parser: LLangParser) = parser.tuple
}
