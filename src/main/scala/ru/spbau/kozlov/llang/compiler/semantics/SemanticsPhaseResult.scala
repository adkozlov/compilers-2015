package ru.spbau.kozlov.llang.compiler.semantics

import ru.spbau.kozlov.llang.compiler.{CompilationPhaseFailedResult, CompilationPhaseResult, CompilationPhaseSucceededResult}
import ru.spbau.kozlov.llang.grammar.LLangParser.ProgramContext

import scala.collection.immutable.SortedSet

/**
 * @author adkozlov
 */
trait SemanticsPhaseResult extends CompilationPhaseResult

final case class SemanticsPhaseSucceededResult(override val programContext: ProgramContext)
  extends CompilationPhaseSucceededResult(programContext) with SemanticsPhaseResult

final case class SemanticsPhaseFailedResult(override val compilationErrors: SortedSet[SemanticsError])
  extends CompilationPhaseFailedResult(compilationErrors) with SemanticsPhaseResult