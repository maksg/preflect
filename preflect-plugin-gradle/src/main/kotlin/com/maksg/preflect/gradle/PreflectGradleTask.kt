package com.maksg.preflect.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

abstract class PreflectGradleTask : DefaultTask() {
    @get:Optional
    @get:Input
    abstract val function: Property<String>

    @TaskAction
    fun preflect() {
        val functionName = function.orNull
        println("pReflect $functionName")
    }
}
