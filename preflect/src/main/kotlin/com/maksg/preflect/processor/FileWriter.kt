package com.maksg.preflect.processor

import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFile
import com.maksg.preflect.processor.models.ProcessedFunction
import com.maksg.preflect.processor.models.fileName
import com.maksg.preflect.processor.models.kspPackage
import java.io.OutputStream

internal class FileWriter(private val fileContentGenerator: FileContentGenerator, private val codeGenerator: CodeGenerator) {
    fun createFile(symbols: Sequence<KSAnnotated>, visitorTransform: (KSAnnotated) -> ProcessedFunction?) {
        if (symbols.none()) return

        val content = fileContentGenerator.generateContent(symbols, visitorTransform)
        val files = symbols.mapNotNull { it.containingFile }

        val file: OutputStream = createFile(files)
        file.write(content.toByteArray())
        file.close()
    }

    private fun createFile(files: Sequence<KSFile>) = codeGenerator.createNewFile(
        Dependencies(
            false,
            *files.toList().toTypedArray(),
        ),
        kspPackage(),
        fileName()
    )
}