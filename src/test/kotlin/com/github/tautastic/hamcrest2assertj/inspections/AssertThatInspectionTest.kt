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
        myFixture.addClass("""package org.hamcrest; public interface Matcher<T> { boolean matches(Object var1); }""")
        myFixture.addClass("""package org.hamcrest; public class Matchers { public static <T> Matcher<T> equalTo(final T op){} public static <T> Matcher<T> hasSize(final T op) {} public static <T> Matcher<T> empty() {} public static <T> Matcher<T> containsString(final T op) {} public static <T> Matcher<T> containsStringIgnoringCase(final T op) {} public static <T> Matcher<T> contains(final T op) {} public static <T> Matcher<T> allOf(final T op) {} public static <T> Matcher<T> anyOf(final T op) {} public static <T> Matcher<T> notNullValue() {} public static <T> Matcher<T> nullValue() {} }""")
        myFixture.addClass("""package org.hamcrest; public class MatcherAssert { public static <T> void assertThat(final String reason, final T actual, final Matcher<? super T> matcher) { return null; }}""")
        myFixture.addClass("""package org.assertj.core.api; public class Assertions { public static <T> void assertThat(final T actual) { }}""")
        myFixture.addClass("""package java.util; public class ArrayList<E> { public ArrayList() {} }""")
        myFixture.addClass("""package java.lang; public final class Boolean { public Boolean() {} public static final Boolean TRUE = true; public static final Boolean FALSE = false; }""")
    }

    fun testAssertThatEqualTo() {
        doIntentionTest()
    }

    fun testAssertThatHasSize() {
        doIntentionTest()
    }

    fun testAssertThatEmpty() {
        doIntentionTest()
    }

    fun testAssertThatContainsString() {
        doIntentionTest()
    }

    fun testAssertThatContainsStringIgnoringCase() {
        doIntentionTest()
    }

    fun testQualifiedAssertThatEqualTo() {
        doIntentionTest()
    }

    fun testNonHamcrestMatcher() {
        doNonIntentionTest()
    }

    fun testNonHamcrestAssertThat() {
        doNonIntentionTest()
    }

    fun testAssertThatContains() {
        doIntentionTest()
    }

    fun testAssertThatNotNull() {
        doIntentionTest()
    }

    fun testAssertThatNull() {
        doIntentionTest()
    }

    fun testAssertThatEqualToPrimitiveBool() {
        doIntentionTest()
    }

    fun testAssertThatEqualToBool() {
        doIntentionTest()
    }

    fun testAssertThatEqualToNull() {
        doIntentionTest()
    }

    fun testAssertThatHasSizeZero() {
        doIntentionTest()
    }

    private fun doIntentionTest() {
        myFixture.configureByFile(getBeforeFile())
        myFixture.enableInspections(AssertThatInspection::class.java)
        val intention = myFixture.availableIntentions.filter { it: IntentionAction ->
            it.familyName == InspectionBundle.message("inspection.hamcrest.assert.that.use.quickfix")
        }[0]
        assertNotNull("cannot find quick fix", intention)
        myFixture.launchAction(intention)
        myFixture.checkResultByFile(getResultFile())
    }

    private fun doNonIntentionTest() {
        myFixture.configureByFile(getBeforeFile())
        myFixture.enableInspections(AssertThatInspection::class.java)
        val intentions = myFixture.availableIntentions.filter { it: IntentionAction ->
            it.familyName == InspectionBundle.message("inspection.hamcrest.assert.that.use.quickfix")
        }
        assertEmpty("there should be no quick fix", intentions)
        myFixture.checkResultByFile(getResultFile())
    }

    private fun getResultFile(): String {
        return getTestName(true) + ".after.java"
    }

    private fun getBeforeFile(): String {
        return getTestName(true) + ".java"
    }

    override fun getTestDataPath() = "src/test/resources/assertThat"
}