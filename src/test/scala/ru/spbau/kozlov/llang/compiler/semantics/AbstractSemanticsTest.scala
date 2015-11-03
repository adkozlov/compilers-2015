package ru.spbau.kozlov.llang.compiler.semantics

import org.antlr.v4.runtime.ParserRuleContext
import ru.spbau.kozlov.llang.compiler.environment.AbstractEnvironmentTest
import ru.spbau.kozlov.llang.compiler.{AbstractCompilerTest, CompilationError, Compiler}

/**
 * @author adkozlov
 */
abstract class AbstractSemanticsTest[C <: ParserRuleContext] extends AbstractEnvironmentTest[C] {
  @throws(classOf[CompilationError])
  override protected def doTest(string: String) = {
    val context = super.doTest(string)
    val semanticsPhaseListener = SemanticsPhaseListener()

    Compiler.walkParseTree(semanticsPhaseListener, context)
    AbstractCompilerTest.checkResult(semanticsPhaseListener)

    context
  }
}
