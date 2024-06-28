package com.maksg.preflect.ir

import org.jetbrains.kotlin.ir.symbols.IrClassifierSymbol

data class PreflectClassContainer(
    val symbol: IrClassifierSymbol,
    val name: String?,
    val members: Sequence<String>
)
