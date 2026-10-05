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
        myFixture.addClass(
            """
            package org.hamcrest;
            public class Matchers {
                public static <T> Matcher<T> equalTo(T op) { return null; }
                public static <T> Matcher<T> is(T op) { return null; }
                public static <T> Matcher<T> is(Matcher<T> matcher) { return null; }
                public static <T> Matcher<T> greaterThan(T op) { return null; }
                public static <T> Matcher<T> hasSize(int size) { return null; }
                public static <T> Matcher<T> hasSize(Matcher<Integer> matcher) { return null; }
                public static <T> Matcher<T> empty() { return null; }
                public static <T> Matcher<T> containsString(String op) { return null; }
                public static <T> Matcher<T> containsStringIgnoringCase(String op) { return null; }
                public static <T> Matcher<T> contains(Object... items) { return null; }
                public static <T> Matcher<T> allOf(Matcher<?>... matchers) { return null; }
                public static <T> Matcher<T> anyOf(Matcher<?>... matchers) { return null; }
                public static <T> Matcher<T> notNullValue() { return null; }
                public static <T> Matcher<T> nullValue() { return null; }
                public static <T> Matcher<T> hasItem(Object item) { return null; }
                public static <T> Matcher<T> hasItems(Object... items) { return null; }
            }
            """.trimIndent()
        )
        myFixture.addClass(
            """
            package org.hamcrest;
            public class MatcherAssert {
                public static <T> void assertThat(String reason, T actual, Matcher<? super T> matcher) { }
                public static <T> void assertThat(T actual, Matcher<? super T> matcher) { }
                public static void assertThat(String reason, boolean assertion) { }
            }
            """.trimIndent()
        )
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

    fun testAssertThatHasItem() {
        doIntentionTest()
    }

    fun testAssertThatHasItems() {
        doIntentionTest()
    }

    fun testAssertThatAllOf() {
        doIntentionTest()
    }

    fun testAssertThatAnyOf() {
        doIntentionTest()
    }

    fun testAssertThatNestedAllOf() {
        doIntentionTest()
    }

    fun testAssertThatNoReason() {
        doIntentionTest()
    }

    fun testAssertThatIsValue() {
        doIntentionTest()
    }

    fun testAssertThatIsNested() {
        doIntentionTest()
    }

    fun testAssertThatHasSizeMatcher() {
        doNonIntentionTest()
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