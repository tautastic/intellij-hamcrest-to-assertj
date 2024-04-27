package com.github.tautastic.hamcrest2assertj.quickfixes

import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiLiteralExpression

class AssertThatArgumentWrapper(argumentList: Array<PsiExpression>) {
    val reason: PsiExpression?
    val actualExpression: PsiExpression
    val matcherExpression: PsiExpression

    init {
        when (argumentList.size) {
            3 -> {
                val arg0 = argumentList[0]
                if (arg0 !is PsiLiteralExpression || arg0.value !is String) {
                    throw IllegalArgumentException("First argument must be a PsiLiteralExpression with a String value")
                }
                this.reason = arg0
                this.actualExpression = argumentList[1]
                this.matcherExpression = argumentList[2]
            }

            2 -> {
                val arg0 = argumentList[0]
                if (arg0 is PsiLiteralExpression && arg0.value is String) {
                    throw IllegalArgumentException("First argument can NOT be a PsiLiteralExpression with a String value")
                }
                this.reason = null
                this.actualExpression = arg0
                this.matcherExpression = argumentList[1]
            }

            else -> {
                throw IllegalArgumentException()
            }
        }
    }
}