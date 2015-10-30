package ru.spbau.kozlov.llang.compiler.parser

import org.antlr.v4.runtime.ParserRuleContext
import ru.spbau.kozlov.llang.compiler.{AbstractCompilerTest, Compiler}

/**
 * @author adkozlov
 */
abstract class AbstractParserTest[C <: ParserRuleContext] extends AbstractCompilerTest[C] {
  override protected def doTest(string: String) = {
    val context = super.doTest(string)
    Compiler.walkParseTree(ParserPhaseListener, context)
    context
  }
}
