package com.github.tautastic.hamcrest2assertj.plugin

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
import junit.framework.TestCase
import org.jetbrains.annotations.NotNull

@TestDataPath("\$CONTENT_ROOT/src/test/testData")
class HamcrestAssertInspectionTest : LightJavaCodeInsightFixtureTestCase() {
    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(HamcrestAssertInspection())
    }

    fun testQualifiedAssertThat() {
        doTest("QualifiedAssertThat")
    }

    private fun doTest(@NotNull testName: String) {
        myFixture.configureByFile("$testName.java")
        val highlightInfos: List<HighlightInfo> = myFixture.doHighlighting()
        TestCase.assertFalse(highlightInfos.isEmpty())
    }

    override fun getTestDataPath() = "src/test/testData/assertThat"
}