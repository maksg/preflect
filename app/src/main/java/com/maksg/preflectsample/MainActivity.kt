package com.maksg.preflectsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.maksg.preflect.Reflekt
import com.maksg.preflect.reflektMembers
import kotlin.reflect.typeOf
import kotlin.time.measureTimedValue

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val (members, timeTaken) = measureTimedValue {
            val myClass = List::class
            myClass.members
        }

        typeOf<List<Int>>().arguments.first().type

        val (reflektMemebers2, timeTakenReflekt) = measureTimedValue {
            Reflekt.objects().WithSuperType<View>()
        }

        val (preflectMemebers, timeTakenPreflect) = measureTimedValue {
            reflektMembers
        }

        findViewById<TextView>(R.id.text).text = "$timeTaken\n$timeTakenReflekt\n$timeTakenPreflect\n$preflectMemebers"
    }
}
