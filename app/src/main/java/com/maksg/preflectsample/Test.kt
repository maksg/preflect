package com.maksg.preflectsample

import com.maksg.preflect.annotation.Preflect

@Preflect(Test::class)
fun preflectModule() = 0

object Test {
    val hello: String = "hello"
    val one: Int = 1
}