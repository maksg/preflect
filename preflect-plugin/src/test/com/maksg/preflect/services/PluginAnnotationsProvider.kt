package com.maksg.preflect.services

import com.maksg.preflect.PreflectPluginRegistrar
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoot
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.test.model.TestModule
import org.jetbrains.kotlin.test.services.EnvironmentConfigurator
import org.jetbrains.kotlin.test.services.TestServices
import org.jetbrains.kotlin.utils.PathUtil
import java.io.File

class PluginAnnotationsProvider(testServices: TestServices) : EnvironmentConfigurator(testServices) {
    override fun CompilerPluginRegistrar.ExtensionStorage.registerCompilerExtensions(
        module: TestModule,
        configuration: CompilerConfiguration
    ) {
        PreflectPluginRegistrar.register(this, configuration)
    }

    override fun configureCompilerConfiguration(configuration: CompilerConfiguration, module: TestModule) {
        getPreflectRuntimeJarFile()?.let { configuration.addJvmClasspathRoot(it) }
    }

    private fun getPreflectRuntimeJarFile(): File? {
        try {
            val clazz = Class.forName("com.maksg.preflect.runtime.annotation.PreflectSearchTypes")
            return PathUtil.getResourcePathForClass(clazz)
        } catch (e: ClassNotFoundException) {
            assert(false) { "pReflect runtime jar not found!" }
        }
        return null
    }
}
