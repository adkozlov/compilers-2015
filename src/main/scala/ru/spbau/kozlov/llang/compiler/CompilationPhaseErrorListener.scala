package ru.spbau.kozlov.llang.compiler

import ru.spbau.kozlov.llang.grammar.LLangParser.ProgramContext

import scala.collection.immutable.{SortedSet, TreeSet}

/**
 * @author adkozlov
 */
trait CompilationPhaseErrorListener[R <: CompilationPhaseResult, E <: CompilationError] {
  private var compilationErrors: SortedSet[E] = new TreeSet()

  def caseSucceeded(programContext: ProgramContext): R

  def caseFailed(compilationErrors: SortedSet[E]): R

  def result(programContext: ProgramContext): R = compilationErrors.isEmpty match {
    case true => caseSucceeded(programContext)
    case _ => caseFailed(compilationErrors)
  }

  def addCompilationError(e: E) = compilationErrors = compilationErrors + e
}
