package com.maksg.preflect.processor.utils

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import kotlin.reflect.KClass

internal fun KSFunctionDeclaration.annotationTypeParameter(annotationClass: KClass<*>): KSClassDeclaration {
    val annotation = annotations.first { it.shortName.asString() == annotationClass.simpleName.toString() }
    val type = annotation.arguments.first { it.name?.asString() == "type" }.value as KSType
    return type.declaration as KSClassDeclaration
}

internal fun KSFunctionDeclaration.import(): String {
    return "${packageName.asString()}.${simpleName.asString()}"
}

internal fun Sequence<KSDeclaration>.mapToName(): Sequence<String> {
    return map { it.simpleName.asString() }
}
