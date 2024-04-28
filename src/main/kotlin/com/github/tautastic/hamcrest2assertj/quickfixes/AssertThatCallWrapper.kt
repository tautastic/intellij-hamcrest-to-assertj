package com.github.tautastic.hamcrest2assertj.quickfixes

import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression

class AssertThatCallWrapper(expression: PsiMethodCallExpression) {
    val reason: String?
    val actualExp: PsiExpression
    val matcherCallExp: PsiMethodCallExpression
    val newMatcherIdentifier: String

    init {
        if (!isHamcrestAssertThatCall(expression)) {
            throw IllegalArgumentException("Expression is not a Hamcrest assertThat call")
        }

        val argumentList = expression.argumentList.expressions
        when (argumentList.size) {
            3 -> {
                this.reason = extractReasonArgument(argumentList[0])
                this.actualExp = argumentList[1]
                this.matcherCallExp = extractMatcherArgument(argumentList[2])
                this.newMatcherIdentifier = computeNewMatcherIdentifier()
            }

            2 -> {
                this.reason = null
                this.actualExp = argumentList[0]
                this.matcherCallExp = extractMatcherArgument(argumentList[1])
                this.newMatcherIdentifier = computeNewMatcherIdentifier()
            }

            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    private fun isHamcrestAssertThatCall(expression: PsiMethodCallExpression): Boolean {
        val qualifiedName = expression.methodExpression.qualifiedName

        if (qualifiedName == "MatcherAssert.assertThat" || qualifiedName == "assertThat") {
            val psiMethod = expression.methodExpression.resolve() as PsiMethod
            val qualifiedClassName = psiMethod.containingClass?.qualifiedName
            return qualifiedClassName == "org.hamcrest.MatcherAssert"
        }

        return false
    }

    private fun extractReasonArgument(exp: PsiExpression?): String {
        if (exp !is PsiLiteralExpression || exp.value !is String) {
            throw IllegalArgumentException("First argument must be a PsiLiteralExpression with a String value")
        }
        return exp.text
    }

    private fun extractMatcherArgument(exp: PsiExpression?): PsiMethodCallExpression {
        if (exp !is PsiMethodCallExpression) {
            throw IllegalArgumentException()
        }

        val qualifiedClassName = (exp.methodExpression.resolve() as PsiMethod).containingClass?.qualifiedName

        if (qualifiedClassName != "org.hamcrest.Matchers") {
            throw IllegalArgumentException()
        }

        return exp
    }

    private fun computeNewMatcherIdentifier(): String {
        val exp = this.matcherCallExp.methodExpression

        if (exp.isQualified) {
            return matcherMap[exp.qualifiedName] ?: throw IllegalStateException()
        }

        return matcherMap["Matchers.".plus(exp.qualifiedName)] ?: throw IllegalStateException()
    }

    companion object {
        @JvmStatic
        fun canConstruct(expression: PsiMethodCallExpression): Boolean {
            try {
                @Suppress("UNUSED_VARIABLE")
                val callWrapper = AssertThatCallWrapper(expression)

                return true
            } catch (e: Exception) {
                return false
            }
        }

        @JvmStatic
        private val matcherMap = mapOf(
            "Matchers.equalTo" to "isEqualTo",
            "Matchers.hasSize" to "hasSize",
            "Matchers.containsString" to "contains",
            "Matchers.containsStringIgnoringCase" to "containsIgnoringCase",
            "Matchers.empty" to "isEmpty",
            "Matchers.allOf" to "satisfiesAllOf",
            "Matchers.anyOf" to "satisfiesAnyOf",
            "Matchers.contains" to "contains",
            "Matchers.endsWith" to "endsWith",
            "Matchers.endsWithIgnoringCase" to "endsWithIgnoringCase",
            "Matchers.equalToIgnoringCase" to "isEqualToIgnoringCase",
            "Matchers.equalToIgnoringWhiteSpace" to "isEqualToIgnoringWhitespace",
            "Matchers.hasItem" to "contains",
            "Matchers.hasItems" to "containsAnyOf",
            "Matchers.instanceOf" to "isInstanceOf",
            "Matchers.isEmptyOrNullString" to "isNullOrEmpty",
            "Matchers.isEmptyString" to "isEmpty",
            "Matchers.isIn" to "isIn",
            "Matchers.notNullValue" to "isNotNull",
            "Matchers.nullValue" to "isNull",
            "Matchers.startsWith" to "startsWith",
            "Matchers.startsWithIgnoringCase" to "startsWithIgnoringCase"
        )

    }
}