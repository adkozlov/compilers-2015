package ru.spbau.kozlov.llang.compiler.parser

import org.junit.runner.RunWith
import org.junit.runners.Suite
import ru.spbau.kozlov.llang.compiler.parser.expressions._
import ru.spbau.kozlov.llang.compiler.parser.statements.TopLevelDeclarationStatementParserTest
import ru.spbau.kozlov.llang.compiler.parser.types.{BuiltInTypeParserTest, ClassParserTest, TupleParserTest}

/**
 * @author adkozlov
 */
@RunWith(classOf[Suite])
@Suite.SuiteClasses(Array(
  classOf[TopLevelDeclarationStatementParserTest],
  classOf[VariableDeclarationStatementParserTest],
  classOf[BlockExpressionParserTest],
  classOf[ExpressionParserTest],
  classOf[IfExpressionParserTest],
  classOf[WhileExpressionParserTest],
  classOf[ReturnExpressionParserTest],
  classOf[PrimaryExpressionParserTest],
  classOf[BuiltInTypeParserTest],
  classOf[ClassParserTest],
  classOf[TupleParserTest]))
class ParserTestsSuite
