package com.github.tautastic.hamcrest2assertj.quickfixes

import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression

class AssertThatCallWrapper(expression: PsiMethodCallExpression) {
    val reason: PsiExpression?
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
                throw IllegalArgumentException("Expression is not a Hamcrest assertThat call")
            }
        }
    }

    private fun extractMatcherArgument(arg1: PsiExpression?): PsiMethodCallExpression {
        if (arg1 !is PsiMethodCallExpression) {
            throw IllegalArgumentException("Second argument must be a PsiMethodCallExpression")
        }
        return arg1
    }

    private fun extractReasonArgument(arg0: PsiExpression?): PsiExpression {
        if (arg0 !is PsiLiteralExpression || arg0.value !is String) {
            throw IllegalArgumentException("First argument must be a PsiLiteralExpression with a String value")
        }
        return arg0
    }

    companion object {
        @JvmStatic
        fun isHamcrestAssertThatCall(expression: PsiMethodCallExpression): Boolean {
            try {
                val qualifiedName = expression.methodExpression.qualifiedName

                if (qualifiedName == "MatcherAssert.assertThat" || qualifiedName == "assertThat") {
                    val psiMethod = expression.methodExpression.resolve() as PsiMethod
                    val qualifiedClassName = psiMethod.containingClass?.qualifiedName
                    return qualifiedClassName == "org.hamcrest.MatcherAssert"
                }

                return false
            } catch (e: Exception) {
                return false
            }
        }
    }
}