package com.github.tautastic.hamcrest2assertj.plugin

import com.github.tautastic.hamcrest2assertj.InspectionBundle
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase


@TestDataPath("\$CONTENT_ROOT/src/test/resources")
class HamcrestAssertInspectionTest : LightJavaCodeInsightFixtureTestCase() {
    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(HamcrestAssertInspection())
        myFixture.addClass("""package mock.hamcrest; public class MatcherAssert { public static <T> void assertThat(final String reason, final T actual, final T matcher) { }}""")
        myFixture.addClass("""package mock.assertj.core.api; public class Assertions { public static <T> void assertThat(final T actual) { }}""")
    }

    fun testAssertThat() {
        doTest()
    }

    fun testQualifiedAssertThat() {
        doTest()
    }

    private fun doTest(toSelect: Int) {
        myFixture.configureByFile(getBeforeFile())
        myFixture.enableInspections(HamcrestAssertInspection::class.java)
        val highlightInfo =
            myFixture.doHighlighting()
                .filter(this::filterHighlightInfos)[toSelect]
        assertNotNull("cannot find highlight", highlightInfo)
    }

    private fun filterHighlightInfos(it: HighlightInfo?): Boolean {
        return it?.inspectionToolId == "Hamcrest2AssertJ" && it.description == InspectionBundle.message(
            "inspection.hamcrest.assert.that.problem.descriptor"
        )
    }

    private fun doTest() {
        doTest(0)
    }

    private fun getResultFile(): String {
        return getTestName(true) + ".after.java"
    }

    private fun getBeforeFile(): String {
        return getTestName(true) + ".java"
    }

    override fun getTestDataPath() = "src/test/resources/assertThat"
}