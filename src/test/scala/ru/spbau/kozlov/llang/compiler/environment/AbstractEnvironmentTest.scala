package ru.spbau.kozlov.llang.compiler.environment

import org.antlr.v4.runtime.ParserRuleContext
import ru.spbau.kozlov.llang.compiler.parser.AbstractParserTest
import ru.spbau.kozlov.llang.compiler.{AbstractCompilerTest, CompilationError, Compiler}

/**
 * @author adkozlov
 */
abstract class AbstractEnvironmentTest[C <: ParserRuleContext] extends AbstractParserTest[C] {
  @throws(classOf[CompilationError])
  override protected def doTest(string: String) = {
    val context = super.doTest(string)

    val environmentFirstPhaseListener = EnvironmentFirstPhaseListener()
    Compiler.walkParseTree(environmentFirstPhaseListener, context)
    AbstractCompilerTest.checkResult(environmentFirstPhaseListener)

    val environmentSecondPhaseListener = EnvironmentSecondPhaseListener()
    Compiler.walkParseTree(environmentSecondPhaseListener, context)
    AbstractCompilerTest.checkResult(environmentSecondPhaseListener)

    context
  }
}
