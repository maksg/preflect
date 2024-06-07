package com.maksg.preflect.ir

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid

class PreflectIrGenerationExtension: IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val types = mutableListOf<String>()
        moduleFragment.acceptChildrenVoid(PreflectTypesSearcher(types))
        moduleFragment.acceptChildrenVoid(PreflectTypesIrBodyGenerator(pluginContext, types))
    }
}
