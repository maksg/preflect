enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "preflect"

include(":sample")
include(":preflect-runtime")
includeBuild("preflect-plugin")
includeBuild("preflect-plugin-gradle")
