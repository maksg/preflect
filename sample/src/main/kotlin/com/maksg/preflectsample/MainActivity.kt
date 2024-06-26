package com.maksg.preflectsample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.maksg.preflect.runtime.annotation.PreflectSearchTypes

class Preflect {
    inline fun <reified T> members(): Array<String> = when (T::class) {
        Double::class -> arrayOf("kotlin.Double", "kotlin.Double2")
        Example::class -> arrayOf("kotlin.Example", "kotlin.Example2")
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

class MainActivity : AppCompatActivity() {
    @PreflectSearchTypes
    inline fun <reified T> staticTypeOf(): List<String> {
        return Preflect().members<T>().toList()
    }

    private inline fun <reified T> replacedTypeOf(): Array<String> {
        return arrayOf("Empty", "Not Implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()
        WindowInsetsControllerCompat(
            window,
            findViewById(android.R.id.content)
        ).isAppearanceLightStatusBars = true

        val doubleMembers = staticTypeOf<Double>()
        val exampleMembers = replacedTypeOf<Example>().toList()
        val doubleReflect = Double::class.java.kotlin.members.map { it.name }
        val exampleReflect = Example::class.java.kotlin.members.map { it.name }

        setContent {
            MaterialTheme {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Double pReflect\n$doubleMembers\nDouble Reflect\n$doubleReflect\n\nExample pReflect\n$exampleMembers\nExample Reflect\n$exampleReflect")
                }
            }
        }
    }
}
