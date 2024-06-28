package com.maksg.preflect.ir

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.IrBlockBuilder
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
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.IrClassifierSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.toIrConst
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.name.Name

class PreflectTypesIrBodyGenerator(
    context: IrPluginContext,
    private val nameFunctionNames: List<Name>,
    private val membersFunctionNames: List<Name>,
    private val containers: List<PreflectClassContainer>
) : IrElementVisitorVoid {
    companion object {
        private val PREFLECT_CLASS_ID = Name.identifier("Preflect")
        private val NAME_FUNCTION_ID = Name.identifier("name")
        private val MEMBERS_FUNCTION_ID = Name.identifier("members")
    }

    private val irBuiltIns = context.irBuiltIns

    override fun visitFile(declaration: IrFile) {
        declaration.acceptChildrenVoid(this)
    }

    override fun visitDeclaration(declaration: IrDeclarationBase) {
        declaration.acceptChildrenVoid(this)
    }

    override fun visitSimpleFunction(declaration: IrSimpleFunction) {
        val isPreflectClassMember = declaration.parent.kotlinFqName.shortName() == PREFLECT_CLASS_ID
        if ((nameFunctionNames.contains(declaration.name) && !isPreflectClassMember) || (declaration.name == NAME_FUNCTION_ID && isPreflectClassMember)) {
            declaration.body = generateNameBody(declaration)
        } else if ((membersFunctionNames.contains(declaration.name) && !isPreflectClassMember) || (declaration.name == MEMBERS_FUNCTION_ID && isPreflectClassMember)) {
            declaration.body = generateMembersBody(declaration)
        }
    }

    private fun generateBody(function: IrSimpleFunction, type: IrType, valueTransformer: IrBlockBuilder.(PreflectClassContainer) -> IrExpression): IrBody {
        val typeParameterT = function.typeParameters.first()
        val irBuilder = irBuiltIns.createIrBuilder(function.symbol)
        return irBuilder.irBlockBody {
            +irReturn(
                irBlock {
                    val tmp = createTmpVariable(kClassReference(typeParameterT.defaultType.classifier))
                    val branches = containers.map { container ->
                        irBranch(
                            irEquals(irGet(tmp), kClassReference(container.symbol)),
                            valueTransformer(container)
                        )
                    } + listOf(
                        irElseBranch(
                            irCall(irBuiltIns.findFunctions(Name.identifier("error")).first()).apply {
                                putValueArgument(0, irString("This type wasn't preflected"))
                            }
                        )
                    )
                    +irWhen(type, branches)
                }
            )
        }
    }

    private fun generateNameBody(function: IrSimpleFunction): IrBody {
        val type = irBuiltIns.stringType
        return generateBody(function, type) { container ->
            irString(container.name ?: "")
        }
    }

    private fun generateMembersBody(function: IrSimpleFunction): IrBody {
        val elementType = irBuiltIns.stringType
        val arrayType = irBuiltIns.arrayClass.typeWith(elementType)
        return generateBody(function, arrayType) { container ->
            val elements = container.members.map { it.toIrConst(elementType) }.toList()
            irVararg(elementType, elements)
        }
    }
}

private fun IrBuilderWithScope.kClassReference(classSymbol: IrClassifierSymbol): IrClassReferenceImpl =
    IrClassReferenceImpl(UNDEFINED_OFFSET, UNDEFINED_OFFSET, context.irBuiltIns.kClassClass.typeWith(classSymbol.defaultType), classSymbol, classSymbol.defaultType)
