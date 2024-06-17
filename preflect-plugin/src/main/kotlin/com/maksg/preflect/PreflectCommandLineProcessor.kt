package com.maksg.preflect

import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

class PreflectCommandLineProcessor : CommandLineProcessor {
    companion object {
        private const val FUNCTION_OPTION_NAME = "function"
        val FUNCTION_COMPILER_KEY = CompilerConfigurationKey<String>(FUNCTION_OPTION_NAME)
    }

    override val pluginId: String = "com.maksg.preflect.plugin"

    override val pluginOptions: Collection<CliOption> = listOf(
        CliOption(
            optionName = FUNCTION_OPTION_NAME,
            valueDescription = "string",
            description = "Function to preflect",
            required = false,
        )
    )

    override fun processOption(
        option: AbstractCliOption,
        value: String,
        configuration: CompilerConfiguration
    ) {
        return when (option.optionName) {
            FUNCTION_OPTION_NAME -> configuration.put(FUNCTION_COMPILER_KEY, value)
            else -> throw IllegalArgumentException("Unexpected config option ${option.optionName}")
        }
    }
}