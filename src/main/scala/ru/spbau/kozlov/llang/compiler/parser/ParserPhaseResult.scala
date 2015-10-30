package ru.spbau.kozlov.llang.compiler.parser

import ru.spbau.kozlov.llang.compiler.{CompilationPhaseFailedResult, CompilationPhaseResult, CompilationPhaseSucceededResult}
import ru.spbau.kozlov.llang.grammar.LLangParser.ProgramContext

import scala.collection.immutable.SortedSet

/**
 * @author adkozlov
 */
trait ParserPhaseResult extends CompilationPhaseResult

final case class ParserPhaseSucceededResult(override val programContext: ProgramContext)
  extends CompilationPhaseSucceededResult(programContext) with ParserPhaseResult

final case class ParserPhaseFailedResult(override val compilationErrors: SortedSet[ParserError])
  extends CompilationPhaseFailedResult(compilationErrors) with ParserPhaseResult