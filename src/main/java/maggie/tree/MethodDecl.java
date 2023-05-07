package maggie.tree;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

public class MethodDecl {
    private final JCTree.JCMethodDecl methodDecl;

    public MethodDecl(JCTree.JCMethodDecl methodDecl) {
        this.methodDecl = methodDecl;
    }

    public void appendTo(ClassDecl classDecl) {
        classDecl.tryAppend(methodDecl);
    }

    public static class Builder {
        final TreeMaker treeMaker;
        private JCTree.JCModifiers modifiers;//访问修饰符
        private Name name;//方法名称
        private JCTree.JCExpression retType;//方法返回类型
        private JCTree.JCBlock body;
        private List<JCTree.JCVariableDecl> params = List.nil();

        public Builder(TreeMaker treeMaker) {
            this.treeMaker = treeMaker;
        }

        public Builder modifiers(JCTree.JCModifiers modifiers) {
            this.modifiers = modifiers;
            return this;
        }

        public Builder name(Name name) {
            this.name = name;
            return this;
        }

        public Builder retType(JCTree.JCExpression retType) {
            this.retType = retType;
            return this;
        }

        public Builder body(JCTree.JCBlock body) {
            this.body = body;
            return this;
        }

        public Builder params(JCTree.JCVariableDecl parameter) {
            this.params = this.params.append(parameter);
            return this;
        }

        public MethodDecl build() {
            return new MethodDecl(treeMaker.MethodDef(modifiers, name, retType, List.nil(), params, List.nil(), body, null));
        }
    }
}
