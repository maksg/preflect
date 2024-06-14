package com.maksg.preflect

import org.jetbrains.kotlin.generators.generateTestGroupSuiteWithJUnit5
import com.maksg.preflect.runners.AbstractPreflectTestRunner

fun main() {
    generateTestGroupSuiteWithJUnit5 {
        testGroup("src/test-gen", "src/test-data") {
            testClass<AbstractPreflectTestRunner> {
                model("preflect")
            }
        }
    }
}
