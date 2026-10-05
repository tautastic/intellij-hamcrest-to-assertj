package com.github.tautastic.hamcrest2assertj.quickfixes

import com.github.tautastic.hamcrest2assertj.InspectionBundle
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.siyeh.ig.psiutils.ClassUtils
import com.siyeh.ig.psiutils.ImportUtils


private const val ASSERTJ_ASSERTIONS_CLASS = "org.assertj.core.api.Assertions"
private const val ASSERTJ_ASSERTIONS_SHORT_NAME = "Assertions"

class AssertThatQuickFix : LocalQuickFix {

    override fun getFamilyName(): String {
        return InspectionBundle.message("inspection.hamcrest.assert.that.use.quickfix")
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val callExpression = descriptor.psiElement as? PsiMethodCallExpression ?: return
        val callWrapper = AssertThatCallWrapper.from(callExpression) ?: return

        // Fall back to the qualified name if another class named 'Assertions' is already visible here
        val useImport = ImportUtils.nameCanBeImported(ASSERTJ_ASSERTIONS_CLASS, callExpression)
        val assertionsName = if (useImport) ASSERTJ_ASSERTIONS_SHORT_NAME else ASSERTJ_ASSERTIONS_CLASS
        val lambdaParameter =
            JavaCodeStyleManager.getInstance(project).suggestUniqueVariableName("actual", callExpression, true)

        val assertJText = callWrapper.render(assertionsName, lambdaParameter) ?: return
        val factory = JavaPsiFacade.getInstance(project).elementFactory
        val replaced = callExpression.replace(factory.createExpressionFromText(assertJText, callExpression))

        if (useImport) {
            ClassUtils.findClass(ASSERTJ_ASSERTIONS_CLASS, replaced)?.let { ImportUtils.addImportIfNeeded(it, replaced) }
        }
    }
}
