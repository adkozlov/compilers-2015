package ru.spbau.kozlov.llang.ast

import ru.spbau.kozlov.llang.ast.statements.TopLevelDeclarationStatementNode
import ru.spbau.kozlov.llang.compiler.environment.TopLevelEnvironment

/**
 * @author adkozlov
 */
final case class ProgramNode(topLevelDeclarationStatementNodes: List[TopLevelDeclarationStatementNode] = Nil,
                             var topLevelEnvironment: Option[TopLevelEnvironment] = None)
  extends AbstractASTNode("Program")