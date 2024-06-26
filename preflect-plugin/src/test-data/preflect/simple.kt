package foo.bar

import com.maksg.preflect.runtime.annotation.PreflectSearchTypes

class Preflect {
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
inline fun <reified T> staticTypeOf(): List<String> {
    return Preflect().members<T>().toList()
}

fun box(): String {
    val expectedList = listOf("number", "text", "list", "count", "info", "equals", "hashCode", "toString")
    println(staticTypeOf<Double>()[0])
    val members = staticTypeOf<Example>()
    return if (members == expectedList) { "OK" } else { "Fail: actual = ${members} expected = ${expectedList}" }
}
