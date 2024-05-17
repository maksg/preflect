package com.maksg.preflect.processor.provider

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.maksg.preflect.processor.FileContentGenerator
import com.maksg.preflect.processor.FileWriter
import com.maksg.preflect.processor.PreflectProcessor

class PreflectProcessorProvider: SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val fileWriter = FileWriter(FileContentGenerator(), environment.codeGenerator)
        return PreflectProcessor(fileWriter)
    }
}