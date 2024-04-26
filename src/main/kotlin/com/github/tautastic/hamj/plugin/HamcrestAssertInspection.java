package com.github.tautastic.hamj.plugin;

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.InspectionsBundle;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

final class HamcrestAssertInspection extends AbstractBaseJavaLocalInspectionTool {

    @Nls
    @NotNull
    @Override
    public String getGroupDisplayName() {
        return InspectionsBundle.message("group.names.potentially.confusing.code.constructs");
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @NotNull
    @Override
    public String getShortName() {
        return "Hamcrest2AssertJ";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(@NotNull final PsiMethodCallExpression expression) {
                super.visitMethodCallExpression(expression);
                // Get the method expression
                PsiReferenceExpression methodExpression = expression.getMethodExpression();

                // Check if the method expression is for assertThat method from Hamcrest MatcherAssert class
                if ("assertThat".equals(methodExpression.getReferenceName())
                        && methodExpression.getQualifier() != null && methodExpression.getQualifier().getText().equals("MatcherAssert")) {
                    holder.registerProblem(expression, InspectionBundle.message("inspection.hamcrest.assert.that.problem.descriptor"));
                }
            }
        };
    }

}