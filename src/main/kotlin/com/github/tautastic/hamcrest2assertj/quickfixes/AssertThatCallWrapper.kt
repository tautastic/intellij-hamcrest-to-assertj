package com.github.tautastic.hamcrest2assertj.quickfixes

import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression

class AssertThatCallWrapper(expression: PsiMethodCallExpression) {
    val reason: String?
    val actualExpression: PsiExpression
    val matcherExpression: PsiMethodCallExpression

    init {
        if (!isHamcrestAssertThatCall(expression)) {
            throw IllegalArgumentException("Expression is not a Hamcrest assertThat call")
        }

        val argumentList = expression.argumentList.expressions
        when (argumentList.size) {
            3 -> {
                this.reason = extractReasonArgument(argumentList[0])
                this.actualExpression = argumentList[1]
                this.matcherExpression = extractMatcherArgument(argumentList[2])
            }

            2 -> {
                this.reason = null
                this.actualExpression = argumentList[0]
                this.matcherExpression = extractMatcherArgument(argumentList[1])
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
    }
}