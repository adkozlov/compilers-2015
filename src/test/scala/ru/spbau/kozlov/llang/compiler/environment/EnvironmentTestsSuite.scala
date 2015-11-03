package ru.spbau.kozlov.llang.compiler.environment

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * @author adkozlov
 */
@RunWith(classOf[Suite])
@Suite.SuiteClasses(Array(
  classOf[ProgramEnvironmentTest],
  classOf[FunctionEnvironmentTest],
  classOf[CaseClassEnvironmentTest]))
class EnvironmentTestsSuite
