package com.maksg.preflect.ir

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.name.Name

class PreflectIrGenerationExtension(private val gradleMemberFunctions: List<Name>, private val shouldReplaceImplementation: Boolean): IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val functions = mutableSetOf<Name>()
        functions.addAll(gradleMemberFunctions)
        moduleFragment.acceptChildrenVoid(PreflectAnnotationsSearcher(functions))

        val containers = mutableListOf<PreflectClassContainer>()
        moduleFragment.acceptChildrenVoid(PreflectTypesSearcher(functions, containers))
        moduleFragment.acceptChildrenVoid(PreflectTypesIrBodyGenerator(pluginContext, emptyList(), gradleMemberFunctions, containers))
    }
}
