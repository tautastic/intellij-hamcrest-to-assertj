package com.github.tautastic.hamcrest2assertj.quickfixes

import com.github.tautastic.hamcrest2assertj.InspectionBundle
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiMethodCallExpression
import com.siyeh.ig.psiutils.ClassUtils
import com.siyeh.ig.psiutils.ImportUtils


private const val ASSERTJ_ASSERTIONS_IMPORT = "org.assertj.core.api.Assertions"

class AssertThatQuickFix : LocalQuickFix {

    override fun getFamilyName(): String {
        return InspectionBundle.message("inspection.hamcrest.assert.that.use.quickfix")
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val callExpression = descriptor.psiElement as PsiMethodCallExpression
        val callWrapper = AssertThatCallWrapper(callExpression)
        callWrapper.optimizeNewMatcher()

        val factory = JavaPsiFacade.getInstance(project).elementFactory
        val fixedMethodCall =
            factory.createExpressionFromText(
                "Assertions.assertThat(ACTUAL_EXPRESSION).MATCHER_EXPRESSION(MATCHER_EXPRESSION_ARGUMENT)",
                null
            ) as PsiMethodCallExpression

        replaceActualExpression(fixedMethodCall, callWrapper.actualExp)
        replaceMatcherExpression(fixedMethodCall, callWrapper, factory)

        callExpression.children[0].replace(fixedMethodCall.children[0])
        callExpression.children[1].replace(fixedMethodCall.children[1])

        handleImports(callExpression)
    }

    private fun handleImports(methodCallExpression: PsiMethodCallExpression) {
        if (ImportUtils.nameCanBeImported(ASSERTJ_ASSERTIONS_IMPORT, methodCallExpression)) {
            val psiClass = ClassUtils.findClass(ASSERTJ_ASSERTIONS_IMPORT, methodCallExpression)
            if (psiClass != null) {
                ImportUtils.addImportIfNeeded(psiClass, methodCallExpression)
            }
        }
    }

    private fun replaceActualExpression(fixedMethodCall: PsiMethodCallExpression, actualExpression: PsiExpression) {
        val methodCallExpression = fixedMethodCall.methodExpression.firstChild as PsiMethodCallExpression
        methodCallExpression.argumentList.expressions[0].replace(actualExpression)
    }

    private fun replaceMatcherExpression(
        fixedMethodCall: PsiMethodCallExpression,
        callWrapper: AssertThatCallWrapper,
        factory: PsiElementFactory
    ) {

        fixedMethodCall.firstChild.lastChild.replace(factory.createIdentifier(callWrapper.newMatcherIdentifier))
        val argumentList = callWrapper.matcherCallArgs
        if (argumentList.isEmpty || callWrapper.ignoreMatcherCallArgsFlag) {
            fixedMethodCall.argumentList.expressions[0].delete()
        } else {
            fixedMethodCall.argumentList.replace(argumentList)
        }
    }
}