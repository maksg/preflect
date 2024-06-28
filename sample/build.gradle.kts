plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.preflect.plugin)
}

android {
    namespace = "com.maksg.preflectsample"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.maksg.preflectsample"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }
}

repositories {
    google()
    mavenCentral()
}

preflect {
    function = "replacedMembersOf"
    replace = true
}

dependencies {
    implementation(dependencies.platform(libs.compose.bom))
    implementation(libs.androidx.appcompat)
    implementation(libs.bundles.compose)
    implementation(libs.kotlin.reflect)
    implementation(projects.preflectRuntime)
}
