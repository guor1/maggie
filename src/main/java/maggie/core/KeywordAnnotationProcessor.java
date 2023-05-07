package maggie.core;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;

/**
 * java关键字处理，可以扩展生成
 */
public abstract class KeywordAnnotationProcessor extends AbstractProcessor {
    /**
     * 语法树对象
     */
    protected JavacTrees trees;
    /**
     * 语法树工厂，可以创建所有节点
     */
    protected TreeMaker treeMaker;
    /**
     * 命名相关的工厂
     */
    protected Names names;
    /**
     * 访问修饰符
     */
    public JCTree.JCModifiers PUBLIC;
    public JCTree.JCModifiers PRIVATE;
    public JCTree.JCModifiers PARAMETER;

    /**
     * 基础字段类型（包括常用的String）
     */
    public JCTree.JCExpression TYPE_INT;
    public JCTree.JCExpression TYPE_VOID;
    public JCTree.JCExpression TYPE_STRING;
    /**
     * this关键字
     */
    public JCTree.JCExpression THIS;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);

        this.PUBLIC = treeMaker.Modifiers(Flags.PUBLIC);
        this.PRIVATE = treeMaker.Modifiers(Flags.PRIVATE);
        this.PARAMETER = treeMaker.Modifiers(Flags.PARAMETER);

        this.TYPE_INT = treeMaker.TypeIdent(TypeTag.INT);
        this.TYPE_VOID = treeMaker.TypeIdent(TypeTag.VOID);
        this.TYPE_STRING = treeMaker.Ident(names.fromString("String"));
        this.THIS = treeMaker.Ident(names._this);
    }
}
