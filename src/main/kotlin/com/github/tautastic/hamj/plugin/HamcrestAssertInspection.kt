package com.github.tautastic.hamj.plugin

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.InspectionsBundle
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiMethodCallExpression
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NotNull

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
                // Get the method expression
                val methodExpression = expression.methodExpression

                // Check if the method expression is for assertThat method from Hamcrest MatcherAssert class
                if ("assertThat" == methodExpression.referenceName
                    && methodExpression.qualifier != null && methodExpression.qualifier!!.text == "MatcherAssert"
                ) {
                    holder.registerProblem(
                        expression,
                        InspectionBundle.message("inspection.hamcrest.assert.that.problem.descriptor")
                    )
                }
            }
        }
    }
}
