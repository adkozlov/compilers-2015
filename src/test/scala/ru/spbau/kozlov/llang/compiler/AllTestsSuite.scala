package ru.spbau.kozlov.llang.compiler

import org.junit.runner.RunWith
import org.junit.runners.Suite
import ru.spbau.kozlov.llang.compiler.environment.EnvironmentTestsSuite
import ru.spbau.kozlov.llang.compiler.parser.ParserTestsSuite
import ru.spbau.kozlov.llang.compiler.semantics.SemanticsTestsSuite

/**
 * @author adkozlov
 */
@RunWith(classOf[Suite])
@Suite.SuiteClasses(Array(
  classOf[ParserTestsSuite],
  classOf[EnvironmentTestsSuite],
  classOf[SemanticsTestsSuite]
))
class AllTestsSuite
