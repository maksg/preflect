package com.maksg.preflect.ir

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.backend.js.utils.typeArguments
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.name.Name

class PreflectTypesSearcher(private var types: MutableList<String>) : IrElementVisitorVoid {
    companion object {
        val FUNCTION_NAME = Name.identifier("staticTypeOf")
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

    override fun visitCall(expression: IrCall) {
        if (expression.symbol.owner.name != FUNCTION_NAME) return
        val callTypes = expression.typeArguments.mapNotNull { type ->
            val typeClass = type?.getClass()
            val properties = typeClass?.properties?.map { it.name.asString() } ?: emptySequence()
            val functions = typeClass?.functions?.map { it.name.asString() } ?: emptySequence()
            (sequenceOf(type?.classFqName?.asString() ?: "") + properties + functions).joinToString()
        }
        types.addAll(callTypes)
        expression.acceptChildrenVoid(this)
    }
}
