package ru.spbau.kozlov.llang.compiler.environment

import org.antlr.v4.runtime.{ParserRuleContext, Token}
import ru.spbau.kozlov.llang.compiler.CompilationPhaseErrorListener
import ru.spbau.kozlov.llang.grammar.LLangBaseListener
import ru.spbau.kozlov.llang.grammar.LLangParser._

import scala.collection.immutable.SortedSet

/**
 * @author adkozlov
 */
trait EnvironmentPhaseListener extends LLangBaseListener
with CompilationPhaseErrorListener[EnvironmentPhaseResult, EnvironmentError] {
  protected val stack = EnvironmentStack()

  override def caseSucceeded(programContext: ProgramContext) = EnvironmentPhaseSucceededResult(programContext)

  override def caseFailed(environmentErrors: SortedSet[EnvironmentError]) = EnvironmentPhaseFailedResult(environmentErrors)

  protected def checkedAddEnvironmentError(flag: Boolean, context: ParserRuleContext, name: String) {
    flag match {
      case false => addCompilationError(createEnvironmentError(name, context.getStart))
      case _ =>
    }
  }

  protected def createEnvironmentError(name: String, token: Token): EnvironmentError
}
