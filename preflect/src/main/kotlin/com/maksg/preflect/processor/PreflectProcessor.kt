package com.maksg.preflect.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.validate
import com.maksg.preflect.processor.visitor.PreflectVisitor
import com.maksg.preflect.processor.models.preflectClass

internal class PreflectProcessor(private val fileGenerator: FileWriter): SymbolProcessor {
    private val preflectVisitor = PreflectVisitor()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val preflectSymbols = resolver.getSymbolsWithAnnotation(preflectClass().qualifiedName.toString())
        if (preflectSymbols.none()) return emptyList()
        fileGenerator.createFile(preflectSymbols) {
            it.accept(preflectVisitor, Unit)
        }
        return preflectSymbols.filterNot { it.validate() }.toList()
    }
}
