package ru.spbau.kozlov.llang.compiler

import java.io.IOException

import org.antlr.v4.runtime._
import org.antlr.v4.runtime.tree.ParseTreeWalker
import ru.spbau.kozlov.llang.compiler.parser._
import ru.spbau.kozlov.llang.grammar.{LLangLexer, LLangListener, LLangParser}

import scala.collection.immutable.SortedSet

/**
 * @author adkozlov
 */
case object Compiler {
  def main(args: Array[String]) {
    args.foreach(compile)
  }

  def createParser(charStream: CharStream, errorListener: ParserErrorListener) = {
    val parser = new LLangParser(CompilerTokenStream(charStream))
    parser.removeErrorListener(ConsoleErrorListener.INSTANCE)
    parser.addErrorListener(errorListener)
    parser
  }

  def walkParseTree(listener: LLangListener, parserRuleContext: ParserRuleContext) {
    ParseTreeWalker.DEFAULT.walk(listener, parserRuleContext)
  }

  private def compile(path: String) {
    def printErrors(errors: Seq[Throwable]) {
      println(s"File: $path, errors: ${errors.size}")
      errors.foreach(e => println(s"\t${e.getMessage}"))
    }

    def printCompilationErrors[E <: CompilationError](compilationErrors: SortedSet[E]) {
      printErrors(compilationErrors.toSeq)
    }

    try {
      parserPhase(path) match {
        case ParserPhaseSucceededResult(parserContext) =>
        case ParserPhaseFailedResult(parsingErrors) => printCompilationErrors(parsingErrors)
      }
    } catch {
      case e: IOException => printErrors(e :: e.getSuppressed.toList)
    }
  }

  private def parserPhase(path: String): ParserPhaseResult = {
    val errorListener = new ParserErrorListener
    val parser = createParser(new ANTLRFileStream(path), errorListener)
    errorListener.result(parser.program) match {
      case result@ParserPhaseSucceededResult(programContext) =>
        walkParseTree(ParserPhaseListener, programContext)
        result
      case result => result
    }
  }
}

final case class CompilerTokenStream(charStream: CharStream) extends CommonTokenStream(new LLangLexer(charStream)) {
  def getFetchedEOF = fetchedEOF
}
