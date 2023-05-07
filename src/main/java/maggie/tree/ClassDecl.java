package maggie.tree;

import com.sun.tools.javac.tree.JCTree;

import java.util.HashMap;
import java.util.Map;

public class ClassDecl {
    private JCTree.JCClassDecl tree;
    /**
     * 成员变量，唯一
     */
    private final Map<String, JCTree> defs = new HashMap<>();

    public static ClassDecl instance(JCTree.JCClassDecl jcClassDecl) {
        ClassDecl classDecl = new ClassDecl();
        classDecl.tree = jcClassDecl;

        for (JCTree def : jcClassDecl.defs) {
            String uniqueKey = keyOf(def);
            if (uniqueKey != null) {
                classDecl.defs.put(uniqueKey, def);
            }
        }
        return classDecl;
    }

    private boolean containDef(JCTree decl) {
        return defs.containsKey(keyOf(decl));
    }

    /**
     * 基于成员变量、成员方法计算key，考虑方法的重载
     */
    static String keyOf(JCTree decl) {
        if (decl instanceof JCTree.JCVariableDecl) {
            return ((JCTree.JCVariableDecl) decl).getName().toString();
        }
        //方法重载，方法名+参数列表类型唯一
        if (decl instanceof JCTree.JCMethodDecl) {
            JCTree.JCMethodDecl methodDecl = (JCTree.JCMethodDecl) decl;
            StringBuilder sb = new StringBuilder();
            sb.append(methodDecl.name.toString());
            for (JCTree.JCVariableDecl param : methodDecl.params) {
                sb.append(param.vartype.toString());
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * 添加成员变量或者成员方法
     */
    public void tryAppend(JCTree def) {
        if (!containDef(def)) {
            tree.defs = tree.defs.append(def);
        }
    }
}
