package ru.spbau.kozlov.llang.types

/**
 * @author adkozlov
 */
trait BuiltInType extends Type

case object AnyType extends BuiltInType {
  override val name = "Any"
}

case object AnyValType extends BuiltInType {
  override val name = "AnyVal"
}

case object BooleanType extends BuiltInType {
  override val name = "Boolean"
}

case object IntType extends BuiltInType {
  override val name = "Int"
}

case object StringType extends BuiltInType {
  override val name = "String"
}

case object UnitType extends BuiltInType {
  override val name = "Unit"
}
