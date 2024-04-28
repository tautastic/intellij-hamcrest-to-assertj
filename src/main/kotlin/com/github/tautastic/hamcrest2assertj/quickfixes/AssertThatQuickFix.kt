package com.github.tautastic.hamcrest2assertj.quickfixes

import com.github.tautastic.hamcrest2assertj.InspectionBundle
import com.intellij.codeInsight.actions.OptimizeImportsProcessor
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

        val factory = JavaPsiFacade.getInstance(project).elementFactory
        val fixedMethodCall =
            factory.createExpressionFromText(
                "Assertions.assertThat(ACTUAL_EXPRESSION).MATCHER_EXPRESSION(MATCHER_EXPRESSION_ARGUMENT)",
                null
            ) as PsiMethodCallExpression

        replaceActualExpression(fixedMethodCall, callWrapper.actualExpression)
        replaceMatcherExpression(factory, fixedMethodCall, callWrapper.matcherExpression)

        callExpression.children[0].replace(fixedMethodCall.children[0])
        callExpression.children[1].replace(fixedMethodCall.children[1])

        handleImports(callExpression)
    }

    private fun handleImports(methodCallExpression: PsiMethodCallExpression) {
        if (ImportUtils.nameCanBeImported(ASSERTJ_ASSERTIONS_IMPORT, methodCallExpression)) {
            val psiClass = ClassUtils.findClass(ASSERTJ_ASSERTIONS_IMPORT, methodCallExpression)
            if (psiClass != null) {
                ImportUtils.addImportIfNeeded(psiClass, methodCallExpression)
                OptimizeImportsProcessor(
                    methodCallExpression.project,
                    methodCallExpression.containingFile
                ).runWithoutProgress()
            }
        }
    }

    private fun replaceActualExpression(fixedMethodCall: PsiMethodCallExpression, actualExpression: PsiExpression) {
        val methodCallExpression = fixedMethodCall.methodExpression.firstChild as PsiMethodCallExpression
        methodCallExpression.argumentList.expressions[0].replace(actualExpression)
    }

    private fun replaceMatcherExpression(
        factory: PsiElementFactory,
        fixedMethodCall: PsiMethodCallExpression,
        matcherCall: PsiMethodCallExpression
    ) {
        val qualifiedName = matcherCall.methodExpression.qualifiedName
        when (qualifiedName) {
            "Matchers.equalTo", "equalTo" -> {
                fixedMethodCall.firstChild.lastChild.replace(factory.createIdentifier("isEqualTo"))
                fixedMethodCall.argumentList.expressions[0].replace(matcherCall.argumentList.expressions[0])
            }

            "Matchers.hasSize", "hasSize" -> {
                fixedMethodCall.firstChild.lastChild.replace(factory.createIdentifier("hasSize"))
                fixedMethodCall.argumentList.expressions[0].replace(matcherCall.argumentList.expressions[0])
            }

            "Matchers.containsString", "containsString" -> {
                fixedMethodCall.firstChild.lastChild.replace(factory.createIdentifier("contains"))
                fixedMethodCall.argumentList.expressions[0].replace(matcherCall.argumentList.expressions[0])
            }

            "Matchers.containsStringIgnoringCase", "containsStringIgnoringCase" -> {
                fixedMethodCall.firstChild.lastChild.replace(factory.createIdentifier("containsIgnoringCase"))
                fixedMethodCall.argumentList.expressions[0].replace(matcherCall.argumentList.expressions[0])
            }

            "Matchers.empty", "empty" -> {
                fixedMethodCall.firstChild.lastChild.replace(factory.createIdentifier("isEmpty"))
                fixedMethodCall.argumentList.expressions[0].delete()
            }

            else -> {
                throw IllegalStateException()
            }
        }
    }
}