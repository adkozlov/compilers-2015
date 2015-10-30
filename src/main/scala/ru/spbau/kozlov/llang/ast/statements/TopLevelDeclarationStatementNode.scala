package ru.spbau.kozlov.llang.ast.statements

import ru.spbau.kozlov.llang.ast.expressions.BlockExpressionNode
import ru.spbau.kozlov.llang.ast.types.{ClassNode, TypeNode}
import ru.spbau.kozlov.llang.ast.{AbstractASTNode, TypedIdentifierExpressionNode, TypedIdentifierListNode}

/**
 * @author adkozlov
 */
trait TopLevelDeclarationStatementNode extends StatementNode {
  val typedIdentifierExpressionNode: TypedIdentifierExpressionNode
}

abstract class AbstractTopLevelDeclarationStatementNode(name: String,
                                                        val typedIdentifierNode: TypedIdentifierExpressionNode)
  extends AbstractASTNode(name) with TopLevelDeclarationStatementNode

final case class FunctionDeclarationStatementNode private(override val typedIdentifierExpressionNode: TypedIdentifierExpressionNode,
                                                          typedIdentifierListNode: TypedIdentifierListNode,
                                                          blockNode: BlockExpressionNode)
  extends AbstractTopLevelDeclarationStatementNode("FunctionDeclaration", typedIdentifierExpressionNode) {

  def this(functionName: String, typeNode: TypeNode,
           typedIdentifierListNode: TypedIdentifierListNode,
           blockNode: BlockExpressionNode) {
    this(new TypedIdentifierExpressionNode(functionName, typeNode), typedIdentifierListNode, blockNode)
  }
}

object FunctionDeclarationStatementNode {
  def apply(functionName: String, typeNode: TypeNode,
            typedIdentifierListNode: TypedIdentifierListNode,
            blockNode: BlockExpressionNode) = {
    new FunctionDeclarationStatementNode(functionName, typeNode, typedIdentifierListNode, blockNode)
  }
}

final case class CaseClassDeclarationStatementNode(override val typedIdentifierExpressionNode: TypedIdentifierExpressionNode,
                                                   typedIdentifierListNode: TypedIdentifierListNode)
  extends AbstractTopLevelDeclarationStatementNode("CaseClassDeclaration", typedIdentifierExpressionNode) {

  def this(className: String, typedIdentifierListNode: TypedIdentifierListNode) {
    this(new TypedIdentifierExpressionNode(className, new ClassNode(className)), typedIdentifierListNode)
  }
}

object CaseClassDeclarationStatementNode {
  def apply(className: String, node: TypedIdentifierListNode) = new CaseClassDeclarationStatementNode(className, node)
}