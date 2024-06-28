package foo.bar

import com.maksg.preflect.runtime.annotation.PreflectSearchTypes

class Preflect {
    inline fun <reified T> name(): String = when (T::class) {
        Double::class -> "kotlin.Double"
        else -> error("Not implemented")
    }

    inline fun <reified T> members(): Array<String> = when (T::class) {
        Double::class -> arrayOf("kotlin.Double", "kotlin.Double2")
        else -> error("Not implemented")
    }
}

class Example {
    var number: Double = 2.0
    var text: String = "abc"
    var list: List<List<Int>> = listOf()
    fun count(): Double { return number }
    fun info(): String { return "test" }
}

@PreflectSearchTypes
inline fun <reified T> staticNameOf(): String {
    return Preflect().name<T>()
}

@PreflectSearchTypes
inline fun <reified T> staticMembersOf(): List<String> {
    return Preflect().members<T>().toList()
}

fun box(): String {
    val expectedName = "kotlin.collections.Map<kotlin.Double, kotlin.collections.List<foo.bar.Example>>"
    val expectedMembers = listOf("number", "text", "list", "count", "info", "equals", "hashCode", "toString")
    println(staticMembersOf<Double>()[0])
    val name = staticNameOf<Map<Double, List<Example>>>()
    val members = staticMembersOf<Example>()

    if (name == expectedName) {
        if (members == expectedMembers) {
            return "OK"
        } else {
            return "Fail members: actual = ${members} expected = ${expectedMembers}"
        }
    } else {
        return "Fail name: actual = ${name} expected = ${expectedName}"
    }
}
