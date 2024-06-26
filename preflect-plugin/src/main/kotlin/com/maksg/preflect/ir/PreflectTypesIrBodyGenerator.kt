package com.maksg.preflect.ir

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.createTmpVariable
import org.jetbrains.kotlin.ir.builders.irBlock
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irBranch
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irElseBranch
import org.jetbrains.kotlin.ir.builders.irEquals
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.builders.irVararg
import org.jetbrains.kotlin.ir.builders.irWhen
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.IrClassifierSymbol
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.toIrConst
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.name.Name

class PreflectTypesIrBodyGenerator(
    context: IrPluginContext,
    private val functionNames: List<Name>,
    private val types: Map<IrClassifierSymbol, Sequence<String>>
) : IrElementVisitorVoid {
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
        val arrayType = irBuiltIns.arrayClass.typeWith(elementType)
        val typeParameterT = function.typeParameters.first()
        val irBuilder = irBuiltIns.createIrBuilder(function.symbol)
        return irBuilder.irBlockBody {
            +irReturn(
                irBlock {
                    val tmp = createTmpVariable(kClassReference(typeParameterT.defaultType.classifier))
                    val branches = types.map { type ->
                        val elements = type.value.map { it.toIrConst(elementType) }.toList()
                        irBranch(
                            irEquals(irGet(tmp), kClassReference(type.key)),
                            irVararg(elementType, elements)
                        )
                    } + listOf(
                        irElseBranch(
                            irCall(irBuiltIns.findFunctions(Name.identifier("error")).first()).apply {
                                putValueArgument(0, irString("This type wasn't preflected"))
                            }
                        )
                    )
                    +irWhen(arrayType, branches)
                }
            )
        }
    }
}

private fun IrBuilderWithScope.kClassReference(classSymbol: IrClassifierSymbol): IrClassReferenceImpl =
    IrClassReferenceImpl(UNDEFINED_OFFSET, UNDEFINED_OFFSET, context.irBuiltIns.kClassClass.typeWith(classSymbol.defaultType), classSymbol, classSymbol.defaultType)
