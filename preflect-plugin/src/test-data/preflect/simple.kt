package foo.bar

import com.maksg.preflect.runtime.annotation.PreflectSearchTypes

class Preflect {
    fun types(): Array<String> = error("Not implemented")
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
    return Preflect().types().toList()
}

fun box(): String {
    val excpectedString = "foo.bar.Example, number, text, list, count, info, equals, hashCode, toString"
    println(staticTypeOf<Double>()[0])
    val types = staticTypeOf<Example>()
    val type = types[1]
    return if (type == excpectedString) { "OK" } else { "Fail: actual = ${type} excpected = ${excpectedString}" }
}
