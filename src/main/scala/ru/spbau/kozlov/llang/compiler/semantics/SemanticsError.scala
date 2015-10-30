package ru.spbau.kozlov.llang.compiler.semantics

import org.antlr.v4.runtime.Token
import ru.spbau.kozlov.llang.compiler.CompilationError
import ru.spbau.kozlov.llang.types.{Type, UnitType}

/**
 * @author adkozlov
 */
abstract class SemanticsError(val expectedType: Type, val token: Token, message: String)
  extends CompilationError(token.getLine, token.getCharPositionInLine, message, null)

final case class TypeMismatchError(actualType: Type,
                                   override val expectedType: Type,
                                   override val token: Token)
  extends SemanticsError(expectedType, token, s"Type mismatch, expected: $expectedType, actual: $actualType")

final case class TypeConfirmationError(actualType: Type,
                                       override val expectedType: Type,
                                       override val token: Token)
  extends SemanticsError(expectedType, token, s"Expression of type $actualType doesn't conform to expected type $expectedType")

final case class SymbolResolutionError(override val token: Token)
  extends SemanticsError(null, token, s"Cannot resolve symbol ${token.getText}")

final case class TypeInferenceError(override val token: Token)
  extends SemanticsError(null, token, "Type inference error")

final case class ArgumentsTypesMismatch(actualTypeList: List[Type],
                                        expectedTypeList: List[Type],
                                        override val token: Token)
  extends SemanticsError(null, token, s"Arguments types mismatch, expected: ${
    expectedTypeList match {
      case Nil => UnitType.name
      case list => list.mkString(", ")
    }
  }, actual: ${
    actualTypeList match {
      case Nil => UnitType.name
      case list => list.mkString(", ")
    }
  }")