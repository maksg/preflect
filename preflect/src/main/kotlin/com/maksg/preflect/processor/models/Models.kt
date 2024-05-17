package com.maksg.preflect.processor.models

import com.maksg.preflect.annotation.Preflect

internal data class ProcessedFunction(
    val import: String,
    val type: String,
    val properties: Sequence<String>,
    val functions: Sequence<String>,
)

internal fun kspPackage() = "com.maksg.preflect"
internal fun fileName() = "GeneratedPreflections"
internal fun preflectClass() = Preflect::class
