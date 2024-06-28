package com.maksg.preflect.ir

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.name.Name

class PreflectIrGenerationExtension(private val gradleFunctions: List<Name>, private val shouldReplaceImplementation: Boolean): IrGenerationExtension {
    companion object {
        private val NAME_ID = Name.identifier("name")
        private val MEMBERS_ID = Name.identifier("members")
    }

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val functions = mutableSetOf<Name>()
        functions.addAll(gradleFunctions)
        moduleFragment.acceptChildrenVoid(PreflectAnnotationsSearcher(functions))

        val containers = mutableListOf<PreflectClassContainer>()
        moduleFragment.acceptChildrenVoid(PreflectTypesSearcher(functions, containers))
        val nameFunctionNames = mutableListOf(NAME_ID)
        val membersFunctionNames = mutableListOf(MEMBERS_ID)
        if (shouldReplaceImplementation) {
            membersFunctionNames.addAll(gradleFunctions)
        }
        moduleFragment.acceptChildrenVoid(PreflectTypesIrBodyGenerator(pluginContext, nameFunctionNames, membersFunctionNames, containers))
    }
}
