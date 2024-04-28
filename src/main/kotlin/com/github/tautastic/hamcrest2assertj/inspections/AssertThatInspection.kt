package com.github.tautastic.hamcrest2assertj.inspections

import com.github.tautastic.hamcrest2assertj.InspectionBundle
import com.github.tautastic.hamcrest2assertj.quickfixes.AssertThatQuickFix
import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.InspectionsBundle
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.impl.source.PsiJavaFileImpl
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NotNull

class AssertThatInspection : AbstractBaseJavaLocalInspectionTool() {
    private val logger = Logger.getInstance(AssertThatInspection::class.java)

    private val myQuickFix = AssertThatQuickFix()

    @Nls
    @NotNull
    override fun getGroupDisplayName(): String {
        return InspectionsBundle.message("group.names.potentially.confusing.code.constructs")
    }

    override fun isEnabledByDefault(): Boolean {
        return true
    }

    @NotNull
    override fun getShortName(): String {
        return "Hamcrest2AssertJ"
    }

    @NotNull
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : JavaElementVisitor() {
            override fun visitMethodCallExpression(expression: PsiMethodCallExpression) {
                super.visitMethodCallExpression(expression)
                try {
                    if (isHamcrestAssertThatCall(expression)) {
                        holder.registerProblem(
                            expression,
                            InspectionBundle.message("inspection.hamcrest.assert.that.problem.descriptor"),
                            myQuickFix
                        )
                    }
                } catch (e: Exception) {
                    logger.debug(e)
                }
            }
        }
    }

    private fun isHamcrestAssertThatCall(expression: PsiMethodCallExpression): Boolean {
        val qualifiedName = expression.methodExpression.qualifiedName
        val packageName = (expression.resolveMethod()?.containingClass?.containingFile as PsiJavaFileImpl).packageName

        return (qualifiedName == "MatcherAssert.assertThat" || qualifiedName == "assertThat")
                && packageName == "org.hamcrest"
    }
}
