import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "com.maksg"
version = libs.versions.preflect

repositories {
    mavenCentral()
}

sourceSets {
    main {
        kotlin.setSrcDirs(listOf("src/main/kotlin"))
        java.setSrcDirs(listOf("src/main/java"))
        resources.setSrcDirs(listOf("src/main/resources"))
    }
    test {
        kotlin.setSrcDirs(listOf("src/test", "src/test-gen"))
        java.setSrcDirs(listOf("src/test", "src/test-gen"))
    }
}

dependencies {
    libs.kotlin.compiler.let {
        compileOnly(it)
        testImplementation(it)
    }

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.compiler.internal.test.framework)
    testImplementation(libs.kotlin.test)
    testImplementation(projects.preflectRuntimePlugin)

    testRuntimeOnly(libs.kotlin.annotations.jvm)
}

tasks.test {
    useJUnitPlatform()
    doFirst {
        setLibraryProperty(libs.kotlin.annotations.jvm)
        setLibraryProperty(libs.kotlin.script.runtime)
        setLibraryProperty(libs.kotlin.stdlib)
        setLibraryProperty(libs.kotlin.test)
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
        optIn.add("org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI")
    }
}

fun Test.setLibraryProperty(library: Provider<MinimalExternalModuleDependency>) {
    val module = library.get().module
    val jarName = module.name
    val propName = "${module.group}.test.${jarName}"
    setLibraryProperty(propName, jarName)
}

fun Test.setLibraryProperty(propName: String, jarName: String) {
    val path = project.configurations
        .testRuntimeClasspath.get()
        .files
        .find { """$jarName-\d.*jar""".toRegex().matches(it.name) }
        ?.absolutePath
        ?: return
    systemProperty(propName, path)
}
