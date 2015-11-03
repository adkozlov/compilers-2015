package ru.spbau.kozlov.llang.compiler.semantics

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * @author adkozlov
 */
@RunWith(classOf[Suite])
@Suite.SuiteClasses(Array(
  classOf[ProgramSemanticsTest],
  classOf[FunctionSemanticsTest],
  classOf[BlockSemanticsTest],
  classOf[ExpressionSemanticsTest],
  classOf[VariableDefinitionSemanticsTest],
  classOf[IfSemanticsTest],
  classOf[WhileSemanticsTest],
  classOf[ReturnSemanticsTest]))
class SemanticsTestsSuite
