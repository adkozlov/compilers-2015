package ru.spbau.kozlov.llang.compiler

/**
 * @author adkozlov
 */
abstract class CompilationError(val line: Int, val charPositionInLine: Int, val message: String, cause: Throwable)
  extends Exception(s"Line $line:${charPositionInLine + 1}: $message", cause)

object CompilationError {
  implicit def ordering[E <: CompilationError]: Ordering[E] = new Ordering[E] {
    override def compare(x: E, y: E): Int = {
      x.line.compareTo(y.line) match {
        case 0 => x.charPositionInLine.compareTo(y.charPositionInLine) match {
          case 0 => x.message.compareTo(y.message)
          case result => result
        }
        case result => result
      }
    }
  }
}



