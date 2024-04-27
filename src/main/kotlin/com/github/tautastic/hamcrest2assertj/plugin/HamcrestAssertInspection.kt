package com.github.tautastic.hamcrest2assertj.plugin

import com.github.tautastic.hamcrest2assertj.InspectionBundle
import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.InspectionsBundle
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiMethodCallExpression
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NotNull

private const val HAMCREST_QUALIFIED_ASSERT_THAT = "MatcherAssert.assertThat"
private const val HAMCREST_UNQUALIFIED_ASSERT_THAT = "assertThat"
private const val HAMCREST_ASSERT_THAT_CLASS = "PsiClass:MatcherAssert"

class HamcrestAssertInspection : AbstractBaseJavaLocalInspectionTool() {

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
                val qualifiedName = expression.methodExpression.qualifiedName
                val parentText = expression.resolveMethod()?.parent.toString()
                val isQualifiedName = qualifiedName == HAMCREST_QUALIFIED_ASSERT_THAT
                val isUnqualifiedName = qualifiedName == HAMCREST_UNQUALIFIED_ASSERT_THAT
                        && parentText == HAMCREST_ASSERT_THAT_CLASS

                if (isQualifiedName || isUnqualifiedName) {
                    holder.registerProblem(
                        expression,
                        InspectionBundle.message("inspection.hamcrest.assert.that.problem.descriptor")
                    )
                }
            }
        }
    }
}
