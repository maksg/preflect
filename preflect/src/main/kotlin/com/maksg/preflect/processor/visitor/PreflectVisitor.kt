package com.maksg.preflect.processor.visitor

import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import com.maksg.preflect.processor.models.ProcessedFunction
import com.maksg.preflect.processor.models.preflectClass
import com.maksg.preflect.processor.utils.annotationTypeParameter
import com.maksg.preflect.processor.utils.import
import com.maksg.preflect.processor.utils.mapToSignature

internal class PreflectVisitor: KSEmptyVisitor<Unit, ProcessedFunction?>() {
    override fun defaultHandler(node: KSNode, data: Unit): ProcessedFunction? {
        return null
    }

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit): ProcessedFunction {
        val type = function.annotationTypeParameter(preflectClass())
        (type.getAllFunctions().first() as KSDeclaration).simpleName
        return ProcessedFunction(
            function.import(),
            type.qualifiedName?.asString() ?: "",
            type.getAllProperties().mapToSignature(),
            type.getAllFunctions().mapToSignature()
        )
    }
}