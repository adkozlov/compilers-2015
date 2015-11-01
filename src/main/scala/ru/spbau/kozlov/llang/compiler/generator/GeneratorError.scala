package ru.spbau.kozlov.llang.compiler.generator

import ru.spbau.kozlov.llang.compiler.CompilationError

/**
 * @author adkozlov
 */
final case class GeneratorError(exception: Exception)
  extends CompilationError(0, 0, exception.getMessage, null)
