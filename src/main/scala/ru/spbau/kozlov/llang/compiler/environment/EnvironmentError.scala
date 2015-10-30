package ru.spbau.kozlov.llang.compiler.environment

import org.antlr.v4.runtime.Token
import ru.spbau.kozlov.llang.compiler.CompilationError

/**
 * @author adkozlov
 */
abstract class EnvironmentError(val token: Token, message: String)
  extends CompilationError(token.getLine, token.getCharPositionInLine, message, null)

final case class AlreadyDefinedInScopeError(private val text: String, override val token: Token)
  extends EnvironmentError(token, s"$text is already defined in the scope")

final case class SymbolResolutionError(private val text: String, override val token: Token)
  extends EnvironmentError(token, s"Cannot resolve symbol $text")

final case class ReferenceResolutionError(private val text: String, override val token: Token)
  extends EnvironmentError(token, s"Cannot resolve reference $text with such signature")
