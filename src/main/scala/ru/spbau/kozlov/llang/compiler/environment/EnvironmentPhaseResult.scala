package ru.spbau.kozlov.llang.compiler.environment

import ru.spbau.kozlov.llang.compiler.{CompilationPhaseFailedResult, CompilationPhaseResult, CompilationPhaseSucceededResult}
import ru.spbau.kozlov.llang.grammar.LLangParser.ProgramContext

import scala.collection.immutable.SortedSet

/**
 * @author adkozlov
 */
trait EnvironmentPhaseResult extends CompilationPhaseResult

final case class EnvironmentPhaseSucceededResult(override val programContext: ProgramContext)
  extends CompilationPhaseSucceededResult(programContext) with EnvironmentPhaseResult

final case class EnvironmentPhaseFailedResult(override val compilationErrors: SortedSet[EnvironmentError])
  extends CompilationPhaseFailedResult(compilationErrors) with EnvironmentPhaseResult
