package maggie.examples;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import maggie.annotation.LoopFor;
import maggie.core.BaseAnnotationProcessor;
import maggie.tree.ClassDecl;
import maggie.tree.VariableDecl;

import javax.annotation.processing.Processor;
import java.lang.annotation.Annotation;

@AutoService(Processor.class)
public class LoopForProcessor extends BaseAnnotationProcessor {
    @Override
    protected Class<? extends Annotation> handleAnnotationType() {
        return LoopFor.class;
    }

    @Override
    protected void visitVar(ClassDecl tree, VariableDecl variableDecl) {
        //生成for循环
        JCTree.JCVariableDecl aVar = treeMaker.VarDef(PARAMETER, names.fromString("a"), TYPE_INT, treeMaker.Literal(1));
        List<JCTree.JCStatement> init = List.of(aVar);//int a = 1;
        JCTree.JCExpression cond = treeMaker.Binary(JCTree.Tag.LE, treeMaker.Ident(aVar.name), treeMaker.Literal(10));//a <= 10
        List<JCTree.JCExpressionStatement> step = List.of(treeMaker.Exec(treeMaker.Unary(JCTree.Tag.PREINC, treeMaker.Ident(aVar.name))));//++a;
        JCTree.JCStatement loopAddBody = treeMaker.Exec(treeMaker.Assignop(JCTree.Tag.PLUS_ASG, treeMaker.Ident(aVar.name), treeMaker.Literal(2)));//a+=2;
        JCTree.JCForLoop loop = treeMaker.ForLoop(init, cond, step, loopAddBody);
        JCTree.JCBlock loopBody = treeMaker.Block(0, List.of(loop));
        JCTree.JCMethodDecl methodDecl3 = treeMaker.MethodDef(PUBLIC, names.fromString("loopAdd"), TYPE_VOID, List.nil(), List.nil(), List.nil(), loopBody, null);
        tree.tryAppend(methodDecl3);
    }
}
