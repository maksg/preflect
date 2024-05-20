package com.maksg.preflectsample

import com.maksg.preflect.annotation.Preflect
import kotlin.reflect.KProperty

@Preflect(Test::class)
fun preflectModule() = 0

object Test {
    val hello: String = "hello"
    val one: Int = 1
    val x by EventDispatcher()
    val list: List<List<Int>> = listOf(listOf(0))
    fun play() {}
}

class Event

class EventDispatcher {
    operator fun getValue(thisRef: Any, property: KProperty<*>): Event {
        return Event()
    }
}