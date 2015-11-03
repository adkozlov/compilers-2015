package ru.spbau.kozlov.llang.compiler.semantics

import org.junit.Assert._
import org.junit.Test
import ru.spbau.kozlov.llang.ast.expressions._
import ru.spbau.kozlov.llang.ast.types._
import ru.spbau.kozlov.llang.compiler.AbstractCompilerTest
import ru.spbau.kozlov.llang.grammar.LLangParser
import ru.spbau.kozlov.llang.grammar.LLangParser.ExpressionContext
import ru.spbau.kozlov.llang.types._

/**
 * @author adkozlov
 */
class ExpressionSemanticsTest extends AbstractSemanticsTest[ExpressionContext] {
  @Test
  @throws(classOf[Exception])
  def testArithmeticIdentityOperation() {
    val expected = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    expected.`type` = Some(IntType)

    val expressionNode = doTest("+42").node
    assertTrue(expressionNode.isInstanceOf[IntegerLiteralNode])
    val actual = expressionNode.asInstanceOf[IntegerLiteralNode]

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testArithmeticIdentityOperationIllegalOperand() {
    doTest("+true")
  }

  @Test
  @throws(classOf[Exception])
  def testArithmeticNegationOperation() {
    val operand = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    operand.`type` = Some(IntType)

    val expected = ArithmeticNegationOperationNode(operand)
    expected.`type` = Some(IntType)

    val expressionNode = doTest("-42").node
    assertTrue(expressionNode.isInstanceOf[ArithmeticNegationOperationNode])
    val actual = expressionNode.asInstanceOf[ArithmeticNegationOperationNode]

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
    assertEquals(expected.operandNode.`type`, actual.operandNode.`type`)
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testArithmeticNegationOperationIllegalOperand() {
    doTest("-true")
  }

  @Test
  @throws(classOf[Exception])
  def testBooleanNegationOperation() {
    val operand = TrueBooleanLiteralNode
    operand.`type` = Some(BooleanType)

    val expected = BooleanNegationOperationNode(operand)
    expected.`type` = Some(BooleanType)

    val expressionNode = doTest("!true").node
    assertTrue(expressionNode.isInstanceOf[BooleanNegationOperationNode])
    val actual = expressionNode.asInstanceOf[BooleanNegationOperationNode]

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
    assertEquals(expected.operandNode.`type`, actual.operandNode.`type`)
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testBooleanNegationOperationIllegalOperand() {
    doTest("!42")
  }

  @Test
  @throws(classOf[Exception])
  def testMulOperation() {
    val firstOperand = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    firstOperand.`type` = Some(IntType)

    val secondOperand = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    secondOperand.`type` = Some(IntType)

    val expected = MultiplicationOperationNode(firstOperand, secondOperand)
    expected.`type` = Some(IntType)

    val expressionNode = doTest("42 * 42").node
    assertTrue(expressionNode.isInstanceOf[MultiplicationOperationNode])
    val actual = expressionNode.asInstanceOf[MultiplicationOperationNode]

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
    assertEquals(expected.firstOperandNode.`type`, actual.firstOperandNode.`type`)
    assertEquals(expected.secondOperandNode.`type`, actual.secondOperandNode.`type`)
  }

  @Test(expected = classOf[TypeMismatchError])
  @throws(classOf[Exception])
  def testMulOperationIllegalLeftOperand() {
    doTest("true * 42")
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testMulOperationIllegalRightOperand() {
    doTest("42 * true")
  }

  @Test
  @throws(classOf[Exception])
  def testDivOperation() {
    val firstOperand = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    firstOperand.`type` = Some(IntType)

    val secondOperand = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    secondOperand.`type` = Some(IntType)

    val expected = DivisionOperationNode(firstOperand, secondOperand)
    expected.`type` = Some(IntType)

    val expressionNode = doTest("42 / 42").node
    assertTrue(expressionNode.isInstanceOf[DivisionOperationNode])
    val actual = expressionNode.asInstanceOf[DivisionOperationNode]

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
    assertEquals(expected.firstOperandNode.`type`, actual.firstOperandNode.`type`)
    assertEquals(expected.secondOperandNode.`type`, actual.secondOperandNode.`type`)
  }

  @Test(expected = classOf[TypeMismatchError])
  @throws(classOf[Exception])
  def testDivOperationIllegalLeftOperand() {
    doTest("true / 42")
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testDivOperationIllegalRightOperand() {
    doTest("42 / true")
  }

  @Test
  @throws(classOf[Exception])
  def testModOperation() {
    val firstOperand = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    firstOperand.`type` = Some(IntType)

    val secondOperand = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    secondOperand.`type` = Some(IntType)

    val expected = ModuloOperationNode(firstOperand, secondOperand)
    expected.`type` = Some(IntType)

    val expressionNode = doTest("42 % 42").node
    assertTrue(expressionNode.isInstanceOf[ModuloOperationNode])
    val actual = expressionNode.asInstanceOf[ModuloOperationNode]

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
    assertEquals(expected.firstOperandNode.`type`, actual.firstOperandNode.`type`)
    assertEquals(expected.secondOperandNode.`type`, actual.secondOperandNode.`type`)
  }

  @Test(expected = classOf[TypeMismatchError])
  @throws(classOf[Exception])
  def testModOperationIllegalLeftOperand() {
    doTest("true % 42")
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testModOperationIllegalRightOperand() {
    doTest("42 % true")
  }

  @Test
  @throws(classOf[Exception])
  def testAddOperation() {
    val firstOperand = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    firstOperand.`type` = Some(IntType)

    val secondOperand = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    secondOperand.`type` = Some(IntType)

    val expected = AdditionOperationNode(firstOperand, secondOperand)
    expected.`type` = Some(IntType)

    val expressionNode = doTest("42 + 42").node
    assertTrue(expressionNode.isInstanceOf[AdditionOperationNode])
    val actual = expressionNode.asInstanceOf[AdditionOperationNode]

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
    assertEquals(expected.firstOperandNode.`type`, actual.firstOperandNode.`type`)
    assertEquals(expected.secondOperandNode.`type`, actual.secondOperandNode.`type`)
  }

  @Test(expected = classOf[TypeMismatchError])
  @throws(classOf[Exception])
  def testAddOperationIllegalLeftOperand() {
    doTest("true + 42")
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testAddOperationIllegalRightOperand() {
    doTest("42 + true")
  }

  @Test
  @throws(classOf[Exception])
  def testAddStringLeftOperation() {
    val firstOperand = StringLiteralNode("\"\"")
    firstOperand.`type` = Some(StringType)

    val secondOperand = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    secondOperand.`type` = Some(IntType)

    val expected = AdditionOperationNode(firstOperand, secondOperand)
    expected.`type` = Some(StringType)

    val expressionNode = doTest("\"\" + 42").node
    assertTrue(expressionNode.isInstanceOf[AdditionOperationNode])
    val actual = expressionNode.asInstanceOf[AdditionOperationNode]

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
    assertEquals(expected.firstOperandNode.`type`, actual.firstOperandNode.`type`)
    assertEquals(expected.secondOperandNode.`type`, actual.secondOperandNode.`type`)
  }

  @Test
  @throws(classOf[Exception])
  def testAddStringRightOperation() {
    val firstOperand = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    firstOperand.`type` = Some(IntType)

    val secondOperand = StringLiteralNode("\"\"")
    secondOperand.`type` = Some(StringType)

    val expected = AdditionOperationNode(firstOperand, secondOperand)
    expected.`type` = Some(StringType)

    val expressionNode = doTest("42 + \"\"").node
    assertTrue(expressionNode.isInstanceOf[AdditionOperationNode])
    val actual = expressionNode.asInstanceOf[AdditionOperationNode]

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
    assertEquals(expected.firstOperandNode.`type`, actual.firstOperandNode.`type`)
    assertEquals(expected.secondOperandNode.`type`, actual.secondOperandNode.`type`)
  }

  @Test
  @throws(classOf[Exception])
  def testStringsConcatenation() {
    val firstOperand = StringLiteralNode("\"\"")
    firstOperand.`type` = Some(StringType)

    val secondOperand = StringLiteralNode("\"\"")
    secondOperand.`type` = Some(StringType)

    val expected = AdditionOperationNode(firstOperand, secondOperand)
    expected.`type` = Some(StringType)

    val expressionNode: ExpressionNode = doTest("\"\" + \"\"").node
    assertTrue(expressionNode.isInstanceOf[AdditionOperationNode])
    val actual: AdditionOperationNode = expressionNode.asInstanceOf[AdditionOperationNode]

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
    assertEquals(expected.firstOperandNode.`type`, actual.firstOperandNode.`type`)
    assertEquals(expected.secondOperandNode.`type`, actual.secondOperandNode.`type`)
  }

  @Test
  @throws(classOf[Exception])
  def testSubOperation() {
    val firstOperand = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    firstOperand.`type` = Some(IntType)

    val secondOperand = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    secondOperand.`type` = Some(IntType)

    val expected = SubtractionOperationNode(firstOperand, secondOperand)
    expected.`type` = Some(IntType)

    val expressionNode = doTest("42 - 42").node
    assertTrue(expressionNode.isInstanceOf[SubtractionOperationNode])
    val actual = expressionNode.asInstanceOf[SubtractionOperationNode]

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
    assertEquals(expected.firstOperandNode.`type`, actual.firstOperandNode.`type`)
    assertEquals(expected.secondOperandNode.`type`, actual.secondOperandNode.`type`)
  }

  @Test(expected = classOf[TypeMismatchError])
  @throws(classOf[Exception])
  def testSubOperationIllegalLeftOperand() {
    doTest("true - 42")
  }

  @Test(expected = classOf[SymbolResolutionError])
  @throws(classOf[Exception])
  def testSubOperationIllegalRightOperand() {
    doTest("42 - true")
  }

  @Test
  @throws(classOf[Exception])
  def testExpressionList() {
    val integerLiteralNode = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    integerLiteralNode.`type` = Some(IntType)

    val booleanLiteralNode = TrueBooleanLiteralNode
    booleanLiteralNode.`type` = Some(BooleanType)

    val expected = ExpressionListNode(List(integerLiteralNode, booleanLiteralNode))
    expected.`type` = Some(TupleType(List(IntType, BooleanType)))

    val actual = doTest("(42, true)").node

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
  }

  @Test
  @throws(classOf[Exception])
  def testNestedExpressionList() {
    val falseBooleanLiteralNode = FalseBooleanLiteralNode
    falseBooleanLiteralNode.`type` = Some(BooleanType)

    val trueBooleanLiteralNode = TrueBooleanLiteralNode
    trueBooleanLiteralNode.`type` = Some(BooleanType)

    val nestedExpressionListType = TupleType(List(BooleanType, BooleanType))
    val nestedExpressionListNode = ExpressionListNode(List(falseBooleanLiteralNode, trueBooleanLiteralNode))
    nestedExpressionListNode.`type` = Some(nestedExpressionListType)

    val integerLiteralNode = IntegerLiteralNode(AbstractCompilerTest.FORTY_TWO)
    integerLiteralNode.`type` = Some(IntType)

    val expected = ExpressionListNode(List(integerLiteralNode, nestedExpressionListNode))
    expected.`type` = Some(TupleType(List(IntType, nestedExpressionListType)))

    val actual = doTest("(42, (false, true))").node

    assertEquals(expected, actual)
    assertEquals(expected.`type`, actual.`type`)
  }

  override protected def parse(parser: LLangParser) = parser.expression
}
