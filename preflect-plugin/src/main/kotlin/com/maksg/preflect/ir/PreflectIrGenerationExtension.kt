package com.maksg.preflect.ir

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.name.Name

class PreflectIrGenerationExtension(private val gradleFunctions: List<Name>, private val shouldReplaceImplementation: Boolean): IrGenerationExtension {
    companion object {
        private val TYPES_ID = Name.identifier("types")
    }

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val functions = mutableSetOf<Name>()
        functions.addAll(gradleFunctions)
        moduleFragment.acceptChildrenVoid(PreflectAnnotationsSearcher(functions))

        val types = mutableListOf<String>()
        moduleFragment.acceptChildrenVoid(PreflectTypesSearcher(functions, types))
        val functionNames = mutableListOf(TYPES_ID)
        if (shouldReplaceImplementation) {
            functionNames.addAll(gradleFunctions)
        }
        moduleFragment.acceptChildrenVoid(PreflectTypesIrBodyGenerator(pluginContext, functionNames, types))
    }
}
