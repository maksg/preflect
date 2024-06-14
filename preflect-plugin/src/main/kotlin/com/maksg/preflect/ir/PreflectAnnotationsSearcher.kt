package com.maksg.preflect.ir

import org.jetbrains.kotlin.backend.jvm.codegen.AnnotationCodegen.Companion.annotationClass
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.name.Name

class PreflectAnnotationsSearcher(private var functions: MutableSet<Name>) : IrElementVisitorVoid {
    companion object {
        val ANNOTATION_NAME = Name.identifier("PreflectSearchTypes")
    }

    override fun visitFile(declaration: IrFile) {
        declaration.acceptChildrenVoid(this)
    }

    override fun visitDeclaration(declaration: IrDeclarationBase) {
        declaration.acceptChildrenVoid(this)
    }

    override fun visitElement(element: IrElement) {
        element.acceptChildrenVoid(this)
    }

    override fun visitSimpleFunction(declaration: IrSimpleFunction) {
        declaration.acceptChildrenVoid(this)
        val names = declaration.annotations.map { it.annotationClass.name }
        if (!names.contains(ANNOTATION_NAME)) return
        functions.add(declaration.name)
    }
}
