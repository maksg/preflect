package com.maksg.preflect.processor

import com.google.devtools.ksp.symbol.KSAnnotated
import com.maksg.preflect.processor.models.ProcessedFunction
import com.maksg.preflect.processor.models.kspPackage

internal class FileContentGenerator {
    fun generateContent(symbols: Sequence<KSAnnotated>, visitorTransform: (KSAnnotated) -> ProcessedFunction?): String {
        return buildString {
            append("package ${kspPackage()}\n")

            if (symbols.none()) return@buildString

            val processedFunctions = symbols.mapNotNull(visitorTransform)

            val imports = processedFunctions.joinToString("\n", "\n", "\n") { "import ${it.import}" }
            append(imports)

            val functionTexts = processedFunctions.joinToString(", ", "\nval reflektMembers = listOf(", ")\n") {
                (sequenceOf(it.type) + it.properties + it.functions).joinToString { "\"${it}\""}
            }
            append(functionTexts)
        }
    }
}