package ru.spbau.kozlov.llang.compiler.generator

import java.io.{File, IOException}

import com.sun.org.apache.bcel.internal.Constants
import com.sun.org.apache.bcel.internal.generic._
import ru.spbau.kozlov.llang.compiler.CompilationPhaseErrorListener
import ru.spbau.kozlov.llang.grammar.LLangBaseListener
import ru.spbau.kozlov.llang.grammar.LLangParser.{CaseClassDeclarationStatementContext, ProgramContext}

import scala.collection.immutable.SortedSet

/**
 * @author adkozlov
 */
final case class GeneratorPhaseListener(outputFolder: File) extends LLangBaseListener
with CompilationPhaseErrorListener[GeneratorPhaseResult, GeneratorError] {
  override def caseSucceeded(programContext: ProgramContext) = GeneratorPhaseSucceededResult(programContext)

  override def caseFailed(generatorErrors: SortedSet[GeneratorError]) = GeneratorPhaseFailedResult(generatorErrors)

  override def exitCaseClassDeclarationStatement(statementContext: CaseClassDeclarationStatementContext) {
    val statementNode = statementContext.node
    val className = statementNode.typedIdentifierExpressionNode.id
    val arguments = statementNode.typedIdentifierListNode.typedIdentifierNodes
      .map(node => (node.id, node.typeNode.toType.toBCELType)).toArray

    val classGen = new ClassGen(className, "java.lang.Object", "<generated>", Constants.ACC_PUBLIC | Constants.ACC_SUPER, null)
    val constantPoolGen = classGen.getConstantPool
    val constructorInstructionList = new InstructionList
    val instructionFactory = new InstructionFactory(classGen)

    constructorInstructionList.append(InstructionConstants.THIS)
    constructorInstructionList.append(new INVOKESPECIAL(constantPoolGen.addMethodref("java.lang.Object", "<init>", "()V")))

    arguments.zipWithIndex.foreach {
      case ((fieldName, fieldType), i) =>
        constructorInstructionList.append(InstructionConstants.THIS)
        constructorInstructionList.append(fieldType match {
          case Type.OBJECT => new ALOAD(i + 1)
          case _ => new ILOAD(i + 1)
        })
        constructorInstructionList.append(instructionFactory.createPutField(className, fieldName, fieldType))
    }

    constructorInstructionList.append(InstructionConstants.RETURN)

    val constructorGen = new MethodGen(Constants.ACC_PUBLIC, Type.VOID,
      arguments.map(_._2), arguments.map(_._1),
      "<init>", className, constructorInstructionList, constantPoolGen)
    constructorGen.setMaxLocals()
    constructorGen.setMaxStack()

    classGen.addMethod(constructorGen.getMethod)
    constructorInstructionList.dispose()

    arguments.foreach {
      case (fieldName, fieldType) =>
        classGen.addField(new FieldGen(Constants.ACC_PRIVATE | Constants.ACC_FINAL, fieldType, fieldName, constantPoolGen).getField)

        val instructionList = new InstructionList()
        instructionList.append(InstructionConstants.THIS)
        instructionList.append(instructionFactory.createGetField(className, fieldName, fieldType))
        instructionList.append(fieldType match {
          case Type.OBJECT => InstructionConstants.ARETURN
          case _ => InstructionConstants.IRETURN
        })

        val methodGen = new MethodGen(Constants.ACC_PUBLIC, fieldType,
          Type.NO_ARGS, Array.empty,
          fieldName, className, instructionList, constantPoolGen)
        methodGen.setMaxLocals()
        methodGen.setMaxStack()
        classGen.addMethod(methodGen.getMethod)
    }

    try {
      classGen.getJavaClass.dump(s"$className.class")
    }
    catch {
      case e: IOException => addCompilationError(GeneratorError(e))
    }
  }
}