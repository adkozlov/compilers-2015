package ru.spbau.kozlov.llang.ast

/**
 * @author adkozlov
 */
trait ASTNode {
  val name: String
}

abstract class AbstractASTNode(val name: String) extends ASTNode
