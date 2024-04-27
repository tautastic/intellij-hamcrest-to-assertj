package com.github.tautastic.hamcrest2assertj.quickfixes

import com.github.tautastic.hamcrest2assertj.InspectionBundle
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiMethodCallExpression

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

        println(argumentWrapper.reason)
        println(argumentWrapper.actualExpression)
        println(argumentWrapper.matcherExpression)
    }
}