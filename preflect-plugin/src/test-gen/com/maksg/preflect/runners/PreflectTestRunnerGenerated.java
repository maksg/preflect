

package com.maksg.preflect.runners;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link com.maksg.preflect.GenerateTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("src/test-data/preflect")
@TestDataPath("$PROJECT_ROOT")
public class PreflectTestRunnerGenerated extends AbstractPreflectTestRunner {
  @Test
  public void testAllFilesPresentInPreflect() {
    KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("src/test-data/preflect"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
  }

  @Test
  @TestMetadata("simple.kt")
  public void testSimple() {
    runTest("src/test-data/preflect/simple.kt");
  }
}