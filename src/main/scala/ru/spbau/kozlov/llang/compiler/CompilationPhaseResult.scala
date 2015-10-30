package ru.spbau.kozlov.llang.compiler

import ru.spbau.kozlov.llang.grammar.LLangParser.ProgramContext

import scala.collection.immutable.SortedSet

/**
 * @author adkozlov
 */
trait CompilationPhaseResult

abstract class CompilationPhaseSucceededResult(val programContext: ProgramContext) extends CompilationPhaseResult

abstract class CompilationPhaseFailedResult[E <: CompilationError](val compilationErrors: SortedSet[E])
  extends CompilationPhaseResult
