package com.maksg.preflect.services

import com.maksg.preflect.PreflectCommandLineProcessor
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.test.model.TestModule
import org.jetbrains.kotlin.test.services.EnvironmentConfigurator
import org.jetbrains.kotlin.test.services.TestServices
import com.maksg.preflect.ir.PreflectIrGenerationExtension
import org.jetbrains.kotlin.name.Name

class ExtensionRegistrarConfigurator(testServices: TestServices) : EnvironmentConfigurator(testServices) {
    override fun CompilerPluginRegistrar.ExtensionStorage.registerCompilerExtensions(
        module: TestModule,
        configuration: CompilerConfiguration
    ) {
        val function = configuration.get(PreflectCommandLineProcessor.FUNCTION_COMPILER_KEY, "")
        val gradleFunctions = listOf(Name.identifier(function))
        IrGenerationExtension.registerExtension(PreflectIrGenerationExtension(gradleFunctions))
    }
}
