package com.github.tautastic.hamcrest2assertj.quickfixes

import com.intellij.psi.*

class AssertThatCallWrapper(expression: PsiMethodCallExpression) {
    val reason: String?
    val actualExp: PsiExpression
    val matcherCallArgs: PsiExpressionList
    var newMatcherIdentifier: String
    var ignoreMatcherCallArgsFlag: Boolean = false

    init {
        if (!isHamcrestAssertThatCall(expression)) {
            throw IllegalArgumentException("Expression is not a Hamcrest assertThat call")
        }

        val argumentList = expression.argumentList.expressions
        when (argumentList.size) {
            3 -> {
                this.reason = extractReasonArgument(argumentList[0])
                this.actualExp = argumentList[1]
                this.matcherCallArgs = extractMatcherArguments(argumentList[2])
                this.newMatcherIdentifier = computeNewMatcherIdentifier(argumentList[2])
            }

            2 -> {
                this.reason = null
                this.actualExp = argumentList[0]
                this.matcherCallArgs = extractMatcherArguments(argumentList[1])
                this.newMatcherIdentifier = computeNewMatcherIdentifier(argumentList[1])
            }

            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    fun optimizeNewMatcher() {
        val args = this.matcherCallArgs.expressions
        if (args.size == 1) {
            val arg0 = args[0]
            when (newMatcherIdentifier) {
                "isEqualTo" -> {
                    when (arg0) {
                        is PsiLiteralExpression -> {
                            val value = arg0.value
                            if (value is Boolean) {
                                if (value == true) {
                                    this.newMatcherIdentifier = "isTrue"
                                } else {
                                    this.newMatcherIdentifier = "isFalse"
                                }
                                this.ignoreMatcherCallArgsFlag = true
                            }
                            if (value == null) {
                                this.newMatcherIdentifier = "isNull"
                                this.ignoreMatcherCallArgsFlag = true
                            }
                        }

                        is PsiReferenceExpression -> {
                            val psiMember = arg0.resolve() as? PsiMember
                            val qualifiedMemberName = psiMember?.containingClass?.qualifiedName

                            if (qualifiedMemberName == "java.lang.Boolean") {
                                val qualifiedName = arg0.qualifiedName
                                if (qualifiedName == "Boolean.TRUE" || qualifiedName == "TRUE") {
                                    this.newMatcherIdentifier = "isTrue"
                                    this.ignoreMatcherCallArgsFlag = true
                                }
                                if (qualifiedName == "Boolean.FALSE" || qualifiedName == "FALSE") {
                                    this.newMatcherIdentifier = "isFalse"
                                    this.ignoreMatcherCallArgsFlag = true
                                }
                            }
                        }
                    }
                }

                "hasSize" -> {
                    val value = (arg0 as? PsiLiteralExpression)?.value as? Int
                    if (value == 0) {
                        this.newMatcherIdentifier = "isEmpty"
                        this.ignoreMatcherCallArgsFlag = true
                    }
                }
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

    private fun extractMatcherArguments(exp: PsiExpression?): PsiExpressionList {
        if (exp !is PsiMethodCallExpression) {
            throw IllegalArgumentException()
        }

        val qualifiedClassName = (exp.methodExpression.resolve() as PsiMethod).containingClass?.qualifiedName

        if (qualifiedClassName?.contains("org.hamcrest") == false) {
            throw IllegalArgumentException()
        }

        return exp.argumentList
    }

    private fun computeNewMatcherIdentifier(exp: PsiExpression?): String {
        if (exp == null || exp !is PsiMethodCallExpression) {
            throw IllegalArgumentException()
        }

        val callExp = exp.methodExpression

        if (callExp.isQualified) {
            val unqualifiedName = callExp.qualifiedName.split(".").last()
            return matcherMap[unqualifiedName] ?: throw IllegalStateException()
        }

        return matcherMap[callExp.qualifiedName] ?: throw IllegalStateException()
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
            "equalTo" to "isEqualTo",
            "is" to "isEqualTo",
            "hasSize" to "hasSize",
            "containsString" to "contains",
            "containsStringIgnoringCase" to "containsIgnoringCase",
            "empty" to "isEmpty",
            "contains" to "contains",
            "notNullValue" to "isNotNull",
            "nullValue" to "isNull"
        )

    }
}