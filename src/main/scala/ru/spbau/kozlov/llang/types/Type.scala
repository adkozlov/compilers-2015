package ru.spbau.kozlov.llang.types

/**
 * @author adkozlov
 */
trait Type {
  val name: String

  override def toString = name
}

object Type {

  implicit class RichType(val `type`: Type) extends AnyVal {
    def toBCELType = `type` match {
      case BooleanType => com.sun.org.apache.bcel.internal.generic.Type.BOOLEAN
      case IntType => com.sun.org.apache.bcel.internal.generic.Type.INT
      case UnitType => com.sun.org.apache.bcel.internal.generic.Type.VOID
      case _ => com.sun.org.apache.bcel.internal.generic.Type.OBJECT
    }
  }

}

final case class ClassType(className: String) extends Type {
  override val name = s"$className"

  var constructorArgumentsTypes: List[(String, Type)] = Nil
}

final case class TupleType(types: List[Type]) extends Type {
  override val name = s"(${types.mkString(", ")})"
}