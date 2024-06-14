dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

include(":preflect-runtime-plugin")
project(":preflect-runtime-plugin").projectDir = file("../preflect-runtime")
