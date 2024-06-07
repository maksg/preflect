plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
}

group = "com.maksg"
version = libs.versions.preflect

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("PreflectGradlePlugin") {
            id = "com.maksg.preflect.plugin"
            displayName = "PreflectGradlePlugin"
            description = "pReflect: An IR-based plugin that can make reflections during compilation instead of run-time."
            implementationClass = "com.maksg.preflect.gradle.PreflectGradlePlugin"
        }
    }
}

dependencies {
    implementation(libs.kotlin.gradle.plugin.api)
}
