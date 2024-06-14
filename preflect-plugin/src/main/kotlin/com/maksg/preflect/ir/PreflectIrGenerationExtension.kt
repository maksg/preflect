package com.maksg.preflect.ir

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.name.Name

class PreflectIrGenerationExtension: IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val functions = mutableSetOf<Name>()
        moduleFragment.acceptChildrenVoid(PreflectAnnotationsSearcher(functions))

        val types = mutableListOf<String>()
        moduleFragment.acceptChildrenVoid(PreflectTypesSearcher(functions, types))
        moduleFragment.acceptChildrenVoid(PreflectTypesIrBodyGenerator(pluginContext, types))
    }
}
