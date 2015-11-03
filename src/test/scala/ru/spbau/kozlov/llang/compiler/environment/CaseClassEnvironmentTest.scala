package ru.spbau.kozlov.llang.compiler.environment

import org.junit.Test
import ru.spbau.kozlov.llang.compiler.semantics.AbstractSemanticsTest
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.CaseClassDeclarationStatementContext

/**
 * @author adkozlov
 */
class CaseClassEnvironmentTest extends AbstractSemanticsTest[CaseClassDeclarationStatementContext] {
  @Test(expected = classOf[AlreadyDefinedInScopeError])
  @throws(classOf[Exception])
  def testCaseClassParametersCollisions() {
    doTest("case class Foo(i: Int, i: Int)")
  }

  override protected def parse(parser: LLangParser) = parser.caseClassDeclarationStatement
}
