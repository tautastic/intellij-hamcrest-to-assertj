package com.github.tautastic.hamcrest2assertj.inspections

import com.github.tautastic.hamcrest2assertj.InspectionBundle
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase


@TestDataPath("\$CONTENT_ROOT/src/test/resources")
class AssertThatInspectionTest : LightJavaCodeInsightFixtureTestCase() {
    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(AssertThatInspection())
        myFixture.addClass("""package org.hamcrest; public interface Matcher<T> { boolean matches(Object var1); void describeMismatch(Object var1, Object var2); }""")
        myFixture.addClass("""package org.hamcrest; public class Matchers { public Matchers() {} public static <T> Matcher<T> equalTo(final T operand) { return IsEqual.equalTo(operand); } }""")
        myFixture.addClass("""package org.hamcrest; public class MatcherAssert { public static <T> void assertThat(final String reason, final T actual, final Matcher<? super T> matcher) { }}""")
        myFixture.addClass("""package org.assertj.core.api; public class Assertions { public static <T> void assertThat(final T actual) { }}""")
    }

    fun testAssertThat() {
        doTest()
    }

    fun testQualifiedAssertThat() {
        doTest()
    }

    private fun doTest(toSelect: Int) {
        myFixture.configureByFile(getBeforeFile())
        myFixture.enableInspections(AssertThatInspection::class.java)
        val intention = intentionAction(toSelect)
        assertNotNull("cannot find quick fix", intention)
        myFixture.launchAction(intention)
        myFixture.checkResultByFile(getResultFile())
    }

    private fun intentionAction(toSelect: Int): IntentionAction {
        return myFixture.availableIntentions.filter { it: IntentionAction ->
            it.familyName == InspectionBundle.message("inspection.hamcrest.assert.that.use.quickfix")
        }[toSelect]
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