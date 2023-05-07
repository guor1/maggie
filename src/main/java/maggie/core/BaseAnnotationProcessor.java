package maggie.core;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Name;
import maggie.tree.ClassDecl;
import maggie.tree.VariableDecl;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static java.util.Locale.ENGLISH;

/**
 * 提供创建属性和方法扩展
 */
public abstract class BaseAnnotationProcessor extends KeywordAnnotationProcessor {
    protected abstract Class<? extends Annotation> handleAnnotationType();

    protected void visitVar(ClassDecl classDecl, VariableDecl variableDecl) {
    }

    protected void visitMethod(ClassDecl classDecl, JCTree.JCMethodDecl methodDecl) {
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(handleAnnotationType().getName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(handleAnnotationType())) {
            if (!(element instanceof TypeElement)) {
                continue;
            }
            TypeElement typeElement = (TypeElement) element;
            JCTree.JCClassDecl jcClassDecl = trees.getTree(typeElement);
            final ClassDecl classDecl = ClassDecl.instance(jcClassDecl);
            jcClassDecl.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl tree) {
                    for (JCTree def : tree.defs) {
                        if (def instanceof JCTree.JCVariableDecl) {
                            BaseAnnotationProcessor.this.visitVar(classDecl, new VariableDecl((JCTree.JCVariableDecl) def));
                        } else if (def instanceof JCTree.JCMethodDecl) {
                            BaseAnnotationProcessor.this.visitMethod(classDecl, (JCTree.JCMethodDecl) def);
                        }
                    }
                    super.visitClassDef(tree);
                }
            });
        }
        return false;
    }

    /**
     * return this.xxx;
     */
    public JCTree.JCReturn fieldReturn(VariableDecl variableDecl) {
        return treeMaker.Return(fieldAccess(variableDecl));
    }

    /**
     * this.xxx
     */
    public JCTree.JCFieldAccess fieldAccess(VariableDecl variableDecl) {
        return treeMaker.Select(THIS, variableDecl.name());
    }

    /**
     * 生成属性的getter方法名，返回Name对象
     */
    public Name getterName(VariableDecl variableDecl) {
        String capitalizedName = capitalize(variableDecl.name().toString());
        String getterName = isBooleanType(variableDecl.vartype().type) ? "is" + capitalizedName : "get" + capitalizedName;
        return names.fromString(getterName);
    }

    /**
     * 生成属性的setter方法名，返回Name对象
     */
    public Name setterName(VariableDecl variableDecl) {
        String capitalizedName = capitalize(variableDecl.name().toString());
        return names.fromString("set" + capitalizedName);
    }

    /**
     * 是否boolean类型
     */
    static boolean isBooleanType(Type type) {
        return type instanceof Type.JCPrimitiveType && type.getKind().equals(TypeKind.BOOLEAN);
    }

    static String capitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }

    /**
     * 定义常量
     */
    public JCTree.JCExpression ofConst(Object value) {
        return treeMaker.Literal(value);
    }
}
