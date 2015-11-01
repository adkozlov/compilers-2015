package ru.spbau.kozlov.llang.compiler.generator

import ru.spbau.kozlov.llang.compiler.{CompilationPhaseFailedResult, CompilationPhaseResult, CompilationPhaseSucceededResult}
import ru.spbau.kozlov.llang.grammar.LLangParser.ProgramContext

import scala.collection.immutable.SortedSet

/**
 * @author adkozlov
 */
trait GeneratorPhaseResult extends CompilationPhaseResult

final case class GeneratorPhaseSucceededResult(override val programContext: ProgramContext)
  extends CompilationPhaseSucceededResult(programContext) with GeneratorPhaseResult

final case class GeneratorPhaseFailedResult(override val compilationErrors: SortedSet[GeneratorError])
  extends CompilationPhaseFailedResult(compilationErrors) with GeneratorPhaseResult
