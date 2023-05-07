package maggie.tree;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;


public class VariableDecl {
    private final JCTree.JCVariableDecl variableDecl;

    public VariableDecl(JCTree.JCVariableDecl variableDecl) {
        this.variableDecl = variableDecl;
    }

    public JCTree.JCVariableDecl getReal() {
        return variableDecl;
    }

    public Name name() {
        return variableDecl.name;
    }

    public JCTree.JCExpression vartype() {
        return variableDecl.vartype;
    }

    public void appendTo(ClassDecl classDecl) {
        classDecl.tryAppend(variableDecl);
    }

    public static class Builder {
        final TreeMaker treeMaker;
        private JCTree.JCModifiers modifiers;//访问修饰符
        private Name name;//字段名称
        private JCTree.JCExpression varType;//字段类型
        private JCTree.JCExpression init;//初始值

        public Builder(TreeMaker treeMaker) {
            this.treeMaker = treeMaker;
        }

        public VariableDecl.Builder modifiers(JCTree.JCModifiers modifiers) {
            this.modifiers = modifiers;
            return this;
        }

        public VariableDecl.Builder name(Name name) {
            this.name = name;
            return this;
        }

        public VariableDecl.Builder varType(JCTree.JCExpression varType) {
            this.varType = varType;
            return this;
        }

        public VariableDecl.Builder init(JCTree.JCExpression init) {
            this.init = init;
            return this;
        }

        public VariableDecl build() {
            return new VariableDecl(treeMaker.VarDef(modifiers, name, varType, init));
        }
    }
}
