package ru.spbau.kozlov.llang.compiler

import org.antlr.v4.runtime.{ANTLRInputStream, ParserRuleContext}
import org.junit.Assert._
import ru.spbau.kozlov.llang.ast.expressions.ValDeclarationExpressionNode
import ru.spbau.kozlov.llang.ast.types._
import ru.spbau.kozlov.llang.ast.{IdentifierExpressionNode, TypedIdentifierExpressionNode}
import ru.spbau.kozlov.llang.compiler.parser.{ParserError, ParserErrorListener}
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.types.ClassType

/**
 * @author adkozlov
 */
abstract class AbstractCompilerTest[C <: ParserRuleContext] {
  protected def parse(parser: LLangParser): C

  protected def doTest(string: String) = {
    val parserErrorListener = ParserErrorListener()
    val parser = AbstractCompilerTest.createParser(string, parserErrorListener)
    val context = parse(parser)

    if (!parser.getTokenStream.asInstanceOf[CompilerTokenStream].getFetchedEOF) {
      val currentToken = parser.getCurrentToken
      throw new ParserError(currentToken.getLine, currentToken.getCharPositionInLine, "Lexer has not hit EOF", null)
    }
    AbstractCompilerTest.checkResult(parserErrorListener)

    assertNotNull(context)
    context
  }
}

object AbstractCompilerTest {
  private def createParser(string: String, parserErrorListener: ParserErrorListener) = {
    Compiler.createParser(new ANTLRInputStream(string), parserErrorListener)
  }

  @throws(classOf[CompilationError])
  def checkResult(compilationPhaseListener: CompilationPhaseErrorListener[_ <: CompilationPhaseResult, _ <: CompilationError]) {
    compilationPhaseListener.result(null) match {
      case result: CompilationPhaseFailedResult[_] => throw result.compilationErrors.toList.head
      case _ =>
    }
  }

  val FOO_LOWER_CASE = "foo"

  val QUOTED_FOO = "\"" + FOO_LOWER_CASE + "\""

  val FOO_CAMEL_CASE = "Foo"

  val BAR_LOWER_CASE = "bar"

  val BAR_CAMEL_CASE = "Bar"

  val FORTY_TWO = 42

  val FOO_IDENTIFIER = IdentifierExpressionNode(FOO_LOWER_CASE)

  val FOO_INT = TypedIdentifierExpressionNode(FOO_LOWER_CASE, IntTypeNode)

  val FORTY_TWO_LITERAL = IntegerLiteralNode(FORTY_TWO)

  val FOO_INT_FORTY_TWO = ValDeclarationExpressionNode(FOO_INT, FORTY_TWO_LITERAL)

  val FOO_LITERAL = StringLiteralNode(QUOTED_FOO)

  val FOO_CLASS_NODE = ClassNode(FOO_CAMEL_CASE)

  val FOO_CLASS_TYPE = ClassType(FOO_CAMEL_CASE)
}
