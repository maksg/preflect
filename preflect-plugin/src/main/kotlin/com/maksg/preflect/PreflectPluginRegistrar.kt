package com.maksg.preflect

import com.maksg.preflect.ir.PreflectIrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration

class PreflectPluginRegistrar: CompilerPluginRegistrar() {
    override val supportsK2: Boolean
        get() = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        register(this, configuration)
    }

    companion object {
        fun register(storage: ExtensionStorage, configuration: CompilerConfiguration) = with(storage) {
            IrGenerationExtension.registerExtension(PreflectIrGenerationExtension())
        }
    }
}
