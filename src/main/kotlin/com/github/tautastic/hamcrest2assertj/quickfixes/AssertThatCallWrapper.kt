package com.github.tautastic.hamcrest2assertj.quickfixes

import com.intellij.psi.*

/**
 * A Hamcrest `assertThat` call whose matcher can be expressed as an AssertJ assertion chain.
 * Use [from] to obtain one; it returns `null` for anything that cannot be converted faithfully.
 */
class AssertThatCallWrapper private constructor(
    private val reason: PsiExpression?,
    private val actual: PsiExpression,
    private val matcher: PsiMethodCallExpression,
) {

    /**
     * Renders the equivalent AssertJ expression, or `null` if the matcher (or one of its nested matchers) is not
     * supported.
     *
     * @param assertionsName how `org.assertj.core.api.Assertions` should be referenced in the generated code
     * @param lambdaParameter name of the lambda parameter used for `allOf`/`anyOf`; must not clash with a variable in scope
     */
    fun render(assertionsName: String, lambdaParameter: String): String? {
        val chain = renderMatcher(matcher, assertionsName, lambdaParameter) ?: return null
        val description = if (reason != null) ".as(${reason.text})" else ""
        return "$assertionsName.assertThat(${actual.text})$description$chain"
    }

    private fun renderMatcher(call: PsiMethodCallExpression, assertions: String, lambdaParameter: String): String? {
        val name = hamcrestMatcherName(call) ?: return null
        val args = call.argumentList.expressions

        return when (name) {
            "equalTo" -> args.singleOrNull()?.let(::renderEqualTo)

            "is" -> args.singleOrNull()?.let { arg ->
                if (hamcrestMatcherName(arg) != null) renderMatcher(arg as PsiMethodCallExpression, assertions, lambdaParameter)
                else renderEqualTo(arg)
            }

            "hasSize" -> args.singleOrNull()?.takeIf(::isPlainValue)?.let { arg ->
                if ((arg as? PsiLiteralExpression)?.value == 0) ".isEmpty()" else ".hasSize(${arg.text})"
            }

            "containsString" -> plainArguments(args, 1)?.let { ".contains($it)" }
            "containsStringIgnoringCase" -> plainArguments(args, 1)?.let { ".containsIgnoringCase($it)" }
            "hasItem" -> plainArguments(args, 1)?.let { ".contains($it)" }
            "hasItems" -> plainArguments(args)?.let { ".contains($it)" }
            // Hamcrest's contains() matches the exact items in the exact order
            "contains" -> plainArguments(args)?.let { ".containsExactly($it)" }

            "empty" -> ".isEmpty()".takeIf { args.isEmpty() }
            "notNullValue" -> ".isNotNull()".takeIf { args.isEmpty() }
            "nullValue" -> ".isNull()".takeIf { args.isEmpty() }

            "allOf" -> renderComposite("satisfies", args, assertions, lambdaParameter)
            "anyOf" -> renderComposite("satisfiesAnyOf", args, assertions, lambdaParameter)

            else -> null
        }
    }

    private fun renderEqualTo(arg: PsiExpression): String? {
        if (!isPlainValue(arg)) return null

        if (arg is PsiLiteralExpression) {
            val value = arg.value
            if (value is Boolean) return if (value) ".isTrue()" else ".isFalse()"
            if (arg.type == PsiTypes.nullType()) return ".isNull()"
        }

        if (arg is PsiReferenceExpression) {
            val field = arg.resolve() as? PsiField
            if (field?.containingClass?.qualifiedName == "java.lang.Boolean") {
                if (field.name == "TRUE") return ".isTrue()"
                if (field.name == "FALSE") return ".isFalse()"
            }
        }

        return ".isEqualTo(${arg.text})"
    }

    private fun renderComposite(
        assertjMethod: String,
        args: Array<PsiExpression>,
        assertions: String,
        lambdaParameter: String,
    ): String? {
        if (args.isEmpty()) return null

        // nested lambdas must not reuse the name of the enclosing lambda's parameter
        val nestedParameter = lambdaParameter + "1"
        val consumers = args.map { arg ->
            val nested = arg as? PsiMethodCallExpression ?: return null
            val chain = renderMatcher(nested, assertions, nestedParameter) ?: return null
            "$lambdaParameter -> $assertions.assertThat($lambdaParameter)$chain"
        }

        return ".$assertjMethod(${consumers.joinToString(", ")})"
    }

    private fun plainArguments(args: Array<PsiExpression>, expectedCount: Int? = null): String? {
        if (args.isEmpty() || (expectedCount != null && args.size != expectedCount)) return null
        if (!args.all(::isPlainValue)) return null
        return args.joinToString(", ") { it.text }
    }

    /** `false` for nested Hamcrest matchers, which cannot be passed on to AssertJ as plain values. */
    private fun isPlainValue(exp: PsiExpression) = hamcrestMatcherName(exp) == null

    companion object {
        private const val HAMCREST_ASSERT_CLASS = "org.hamcrest.MatcherAssert"
        private const val HAMCREST_PACKAGE = "org.hamcrest."

        @JvmStatic
        fun from(expression: PsiMethodCallExpression): AssertThatCallWrapper? {
            if (expression.methodExpression.referenceName != "assertThat") return null
            val assertThatMethods = resolveMethods(expression)
            if (assertThatMethods.isEmpty() || assertThatMethods.any { it.containingClass?.qualifiedName != HAMCREST_ASSERT_CLASS }) {
                return null
            }

            val args = expression.argumentList.expressions
            val matcher = args.lastOrNull() as? PsiMethodCallExpression ?: return null
            if (hamcrestMatcherName(matcher) == null) return null

            return when (args.size) {
                3 -> AssertThatCallWrapper(args[0], args[1], matcher)
                2 -> AssertThatCallWrapper(null, args[0], matcher)
                else -> null
            }
        }

        /** Whether the call can be converted, using placeholder names since the result is discarded. */
        @JvmStatic
        fun canConstruct(expression: PsiMethodCallExpression): Boolean =
            from(expression)?.render("Assertions", "actual") != null

        /**
         * All methods the call may refer to. Overloaded or not-yet-inferable calls have several candidates, which
         * `resolve()` reports as `null`, but they all share the same name and containing class.
         */
        private fun resolveMethods(call: PsiMethodCallExpression): List<PsiMethod> =
            call.methodExpression.multiResolve(true).mapNotNull { it.element as? PsiMethod }

        /** The name of the Hamcrest matcher factory method `exp` calls, or `null` if it is not one. */
        private fun hamcrestMatcherName(exp: PsiExpression): String? {
            val call = exp as? PsiMethodCallExpression ?: return null
            val methods = resolveMethods(call)
            if (methods.isEmpty() || !methods.all { it.containingClass?.qualifiedName?.startsWith(HAMCREST_PACKAGE) == true }) {
                return null
            }
            return methods.first().name
        }
    }
}
