package ru.spbau.kozlov.llang.compiler.environment

import ru.spbau.kozlov.llang.types.Type

import scala.collection.mutable

/**
 * @author adkozlov
 */
final case class VariablesEnvironment(private val variables: mutable.Map[String, (Boolean, Type)]) {

  def this() {
    this(mutable.HashMap[String, (Boolean, Type)]())
  }

  def addVariable(name: String, `type`: Type): Boolean = variables.put(name, (false, `type`)) match {
    case Some((false, _)) => false
    case _ => true
  }

  def getVariable(name: String): Option[Type] = variables.get(name).map {
    case (_, variableType) => variableType
  }
}

object VariablesEnvironment {
  def apply() = new VariablesEnvironment()

  def apply(variablesEnvironment: VariablesEnvironment): VariablesEnvironment = {
    VariablesEnvironment(mutable.HashMap[String, (Boolean, Type)]() ++ variablesEnvironment.variables.map {
      case (name, (_, variableType)) => (name, (true, variableType))
    })
  }
}
