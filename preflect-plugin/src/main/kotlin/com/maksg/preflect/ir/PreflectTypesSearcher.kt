package com.maksg.preflect.ir

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.backend.js.utils.typeArguments
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.types.typeOrNull
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.name.Name

class PreflectTypesSearcher(private val functions: Set<Name>, private var containers: MutableList<PreflectClassContainer>) : IrElementVisitorVoid {
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
        containers.addAll(
            expression.typeArguments.mapNotNull { type ->
                val typeClass = type?.getClass() ?: return@mapNotNull null
                val members = (typeClass.properties + typeClass.functions).map { it.name.asString() }
                PreflectClassContainer(typeClass.symbol, type.fullName(), members)
            }
        )
    }
}

private fun IrType.fullName(): String? {
    val name = classFqName?.asString() ?: return null
    val argumentNames = (this as? IrSimpleType)?.arguments?.mapNotNull { it.typeOrNull?.fullName() } ?: emptyList()
    if (argumentNames.isNotEmpty()) {
        return "${name}<${argumentNames.joinToString()}>"
    } else {
        return name
    }
}

