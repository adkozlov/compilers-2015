package ru.spbau.kozlov.llang.compiler

import org.junit.runner.RunWith
import org.junit.runners.Suite
import ru.spbau.kozlov.llang.compiler.parser.ParserTestsSuite

/**
 * @author adkozlov
 */
@RunWith(classOf[Suite])
@Suite.SuiteClasses(Array(classOf[ParserTestsSuite]))
class AllTestsSuite
