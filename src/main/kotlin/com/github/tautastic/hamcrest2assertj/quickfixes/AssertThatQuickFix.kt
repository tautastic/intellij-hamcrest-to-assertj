package com.github.tautastic.hamcrest2assertj.quickfixes

import com.github.tautastic.hamcrest2assertj.InspectionBundle
import com.intellij.codeInsight.actions.OptimizeImportsProcessor
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiMethodCallExpression
import com.siyeh.ig.psiutils.ClassUtils
import com.siyeh.ig.psiutils.ImportUtils


private const val ASSERTJ_ASSERTIONS_IMPORT = "org.assertj.core.api.Assertions"

class AssertThatQuickFix : LocalQuickFix {
    /**
     * @return text to appear in "Apply Fix" popup when multiple Quick Fixes exist (in the results of batch code inspection). For example,
     * if the name of the quickfix is "Create template &lt;filename&gt", the return value of getFamilyName() should be "Create template".
     * If the name of the quickfix does not depend on a specific element, simply return [.getName].
     */
    override fun getFamilyName(): String {
        return InspectionBundle.message("inspection.hamcrest.assert.that.use.quickfix")
    }

    /**
     * Called to apply the fix.
     *
     *
     * Please call [com.intellij.profile.codeInspection.ProjectInspectionProfileManager.fireProfileChanged] if inspection profile is changed as result of fix.
     *
     * @param project    [Project]
     * @param descriptor problem reported by the tool which provided this quick fix action
     */
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val methodCallExpression = descriptor.psiElement as PsiMethodCallExpression
        val argumentWrapper = AssertThatArgumentWrapper(methodCallExpression.argumentList.expressions)

        val factory = JavaPsiFacade.getInstance(project).elementFactory
        val fixedMethodCall =
            factory.createExpressionFromText(
                "Assertions.assertThat(ACTUAL_EXPRESSION).isEqualTo(MATCHER_EXPRESSION)",
                null
            ) as PsiMethodCallExpression

        replaceActualExpression(fixedMethodCall, argumentWrapper.actualExpression)
        replaceMatcherExpression(fixedMethodCall, argumentWrapper.matcherExpression)

        methodCallExpression.children[0].replace(fixedMethodCall.children[0])
        methodCallExpression.children[1].replace(fixedMethodCall.children[1])

        handleImports(methodCallExpression)
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

    private fun replaceMatcherExpression(fixedMethodCall: PsiMethodCallExpression, matcherExpression: PsiExpression) {
        val matcherPsiExpression = (matcherExpression as PsiMethodCallExpression).argumentList.expressions[0]
        fixedMethodCall.argumentList.expressions[0].replace(matcherPsiExpression)
    }
}