package ru.spbau.kozlov.llang.compiler.environment

import ru.spbau.kozlov.llang.types._

import scala.collection.mutable

/**
 * @author adkozlov
 */
final case class TopLevelEnvironment() {
  private val functions = mutable.HashMap[String, Set[FunctionType]]()
  private val caseClasses = mutable.HashMap[String, ClassType]()

  functions += "readLine" -> Set(FunctionType(List[Type](), StringType))
  functions += "println" -> Set(FunctionType(List[Type](AnyType), UnitType))

  def addFunction(name: String, functionType: FunctionType) = {
    def put(set: Set[FunctionType] = Set()) = {
      functions.put(name, Set(functionType) ++ set)
      true
    }

    functions.get(name) match {
      case Some(set) =>
        set.map(_.argumentTypes).contains(functionType.argumentTypes) match {
          case true => false
          case _ => put(set)
        }
      case _ => put()
    }
  }

  def getFunction(name: String): Set[FunctionType] = functions.get(name) match {
    case Some(set) => set
    case _ => Set.empty
  }

  def addCaseClass(name: String, classType: ClassType) = caseClasses.put(name, classType).isEmpty

  def getCaseClass(name: String): Option[ClassType] = caseClasses.get(name)
}