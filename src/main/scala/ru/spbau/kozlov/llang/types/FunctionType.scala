package ru.spbau.kozlov.llang.types

/**
 * @author adkozlov
 */
final case class FunctionType(argumentTypes: List[Type], resultType: Type) extends Type {
  override val name = s"(${argumentTypes.mkString(" => ")} => $resultType)"
}
