package ru.spbau.kozlov.llang.compiler.parser

import org.antlr.v4.runtime.{BaseErrorListener, RecognitionException, Recognizer}
import ru.spbau.kozlov.llang.compiler.{CompilationError, CompilationPhaseErrorListener}
import ru.spbau.kozlov.llang.grammar.LLangParser.ProgramContext

import scala.collection.immutable.SortedSet

/**
 * @author adkozlov
 */
final case class ParserError(override val line: Int, override val charPositionInLine: Int,
                             override val message: String, cause: Throwable)
  extends CompilationError(line, charPositionInLine, message, cause)

final case class ParserErrorListener()
  extends BaseErrorListener with CompilationPhaseErrorListener[ParserPhaseResult, ParserError] {

  override def caseSucceeded(programContext: ProgramContext) = ParserPhaseSucceededResult(programContext)

  override def caseFailed(parserErrors: SortedSet[ParserError]) = ParserPhaseFailedResult(parserErrors)

  override def result(programContext: ProgramContext) = super.result(programContext)

  override def syntaxError(recognizer: Recognizer[_, _],
                           offendingSymbol: scala.Any,
                           line: Int,
                           charPositionInLine: Int,
                           message: String,
                           recognitionException: RecognitionException) {
    addCompilationError(ParserError(line, charPositionInLine, message, recognitionException))
  }
}
