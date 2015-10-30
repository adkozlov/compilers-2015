package ru.spbau.kozlov.llang.compiler.environment

import ru.spbau.kozlov.llang.types.{ClassType, FunctionType, Type}

import scala.collection.mutable

/**
 * @author adkozlov
 */
final case class EnvironmentStack() {
  var topLevelEnvironment: Option[TopLevelEnvironment] = Some(TopLevelEnvironment())

  private val stack = mutable.Stack[VariablesEnvironment](VariablesEnvironment())

  def pushVariables(variablesEnvironment: Option[VariablesEnvironment]) = variablesEnvironment match {
    case Some(environment) => stack.push(environment)
    case _ =>
  }

  def topVariables() = Some(VariablesEnvironment(stack.top))

  def popVariables() = Some(stack.pop())

  def addFunction(name: String, functionType: FunctionType) = topLevelEnvironment match {
    case Some(environment) => environment.addFunction(name, functionType)
    case _ => false
  }

  def getFunction(name: String) = topLevelEnvironment match {
    case Some(environment) => environment.getFunction(name)
    case _ => Set.empty
  }

  def addCaseClass(name: String, classType: ClassType) = topLevelEnvironment match {
    case Some(environment) => environment.addCaseClass(name, classType)
    case _ => false
  }

  def getCaseClass(name: String): Option[ClassType] = topLevelEnvironment match {
    case Some(environment) => environment.getCaseClass(name)
    case _ => None
  }

  def addVariable(name: String, `type`: Type) = stack.top.addVariable(name, `type`)

  def getVariable(name: String): Option[Type] = stack.top.getVariable(name)
}
