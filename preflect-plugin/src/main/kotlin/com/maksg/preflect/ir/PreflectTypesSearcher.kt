package com.maksg.preflect.ir

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.backend.js.utils.typeArguments
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.symbols.IrClassifierSymbol
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.name.Name

class PreflectTypesSearcher(private val functions: Set<Name>, private var types: MutableMap<IrClassifierSymbol, Sequence<String>>) : IrElementVisitorVoid {
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
        expression.acceptChildrenVoid(this)
        if (!functions.contains(expression.symbol.owner.name)) return

        val callTypes = expression.typeArguments.mapNotNull { type ->
            val classSymbol = type?.classOrNull ?: return@mapNotNull null
            val typeClass = type.getClass()!!
            val members = (typeClass.properties + typeClass.functions).map { it.name.asString() }
            classSymbol to members
        }.toMap()
        types.putAll(callTypes)
    }
}
