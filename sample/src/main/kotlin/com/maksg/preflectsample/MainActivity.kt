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
    fun types(): Array<String> = error("Not implemented")
}

class MainActivity : AppCompatActivity() {
    @PreflectSearchTypes
    inline fun <reified T> staticTypeOf(): List<String> {
        return Preflect().types().toList()
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

        val result = staticTypeOf<Int>()[0]
        val result2 = replacedTypeOf<List<List<Boolean>>>()[1]

        setContent {
            MaterialTheme {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("[Type $result]\n[Type $result2]")
                }
            }
        }
    }
}
