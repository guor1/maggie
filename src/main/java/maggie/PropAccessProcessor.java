package maggie;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import maggie.annotation.PropAccess;
import maggie.core.BaseAnnotationProcessor;
import maggie.tree.ClassDecl;
import maggie.tree.MethodDecl;
import maggie.tree.VariableDecl;

import javax.annotation.processing.Processor;
import java.lang.annotation.Annotation;

/**
 * 为成员变量生成getter、setter方法
 */
@AutoService({Processor.class})
public class PropAccessProcessor extends BaseAnnotationProcessor {

    @Override
    public Class<? extends Annotation> handleAnnotationType() {
        return PropAccess.class;
    }

    @Override
    public void visitVar(ClassDecl tree, VariableDecl variableDecl) {
        //生成getter方法
        JCTree.JCBlock body1 = treeMaker.Block(0, List.of(fieldReturn(variableDecl)));
        new MethodDecl.Builder(treeMaker).modifiers(PUBLIC).name(getterName(variableDecl)).retType(variableDecl.vartype()).body(body1).build().appendTo(tree);

        //生成setter方法
        VariableDecl parameter = new VariableDecl.Builder(treeMaker).modifiers(PARAMETER).name(variableDecl.name()).varType(variableDecl.vartype()).build();
        JCTree.JCBlock body2 = treeMaker.Block(0, List.of(treeMaker.Exec(treeMaker.Assign(fieldAccess(variableDecl), treeMaker.Ident(parameter.name())))));
        new MethodDecl.Builder(treeMaker).modifiers(PUBLIC).name(setterName(variableDecl)).retType(TYPE_VOID).params(parameter.getReal()).body(body2).build().appendTo(tree);
    }
}