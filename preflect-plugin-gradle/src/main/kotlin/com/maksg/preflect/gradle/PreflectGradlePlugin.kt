package com.maksg.preflect.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class PreflectGradlePlugin : KotlinCompilerPluginSupportPlugin {
    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project
        val extension = project.extensions.getByType(PreflectGradleExtension::class.java)
        return project.provider {
            listOf(
                SubpluginOption("function", extension.function ?: ""),
                SubpluginOption("replace", (extension.replace ?: false).toString())
            )
        }
    }

    override fun apply(target: Project) {
        val preflectName = "preflect"
        val extension = target.extensions.create(preflectName, PreflectGradleExtension::class.java)
        val task = target.tasks.register(preflectName, PreflectGradleTask::class.java)
        target.afterEvaluate {
            task.configure { target ->
                target.function.set(extension.function)
                target.replace.set(extension.replace)
            }
        }
    }

    override fun getCompilerPluginId(): String = "com.maksg.preflect.plugin"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact("com.maksg", "preflect-plugin")

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true
}