package com.maksg.preflect

object Reflekt {
    fun objects() = Objects()
    class Objects {
        inline fun <reified T : Any> WithSuperType(): List<String> {
            val myClass = T::class
            val members = myClass.members
            return members.map { it.name }
        }
    }
}