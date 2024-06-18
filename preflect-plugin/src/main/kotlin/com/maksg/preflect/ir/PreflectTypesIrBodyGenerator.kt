package com.maksg.preflect.ir

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.toIrConst
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.name.Name

class PreflectTypesIrBodyGenerator(context: IrPluginContext, private val functionNames: List<Name>, private val types: List<String>) : IrElementVisitorVoid {
    private val irFactory = context.irFactory
    private val irBuiltIns = context.irBuiltIns

    override fun visitFile(declaration: IrFile) {
        declaration.acceptChildrenVoid(this)
    }

    override fun visitDeclaration(declaration: IrDeclarationBase) {
        declaration.acceptChildrenVoid(this)
    }

    override fun visitSimpleFunction(declaration: IrSimpleFunction) {
        if (!functionNames.contains(declaration.name)) return
        declaration.body = generateBodyForFunction(declaration)
    }

    private fun generateBodyForFunction(function: IrSimpleFunction): IrBody {
        val elementType = irBuiltIns.stringType
        val elements = types.map { it.toIrConst(elementType) }
        val arrayType = irBuiltIns.arrayClass.typeWith(elementType)
        val const = IrVarargImpl(UNDEFINED_OFFSET, UNDEFINED_OFFSET, arrayType, elementType, elements)
        val returnStatement = IrReturnImpl(UNDEFINED_OFFSET, UNDEFINED_OFFSET, arrayType, function.symbol, const)
        return irFactory.createBlockBody(UNDEFINED_OFFSET, UNDEFINED_OFFSET, listOf(returnStatement))
    }
}
