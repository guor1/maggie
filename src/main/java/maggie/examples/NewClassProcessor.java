package maggie.examples;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import maggie.annotation.NewClass;
import maggie.core.BaseAnnotationProcessor;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 基于javapoet生成一个新的class
 */
@AutoService(Processor.class)
public class NewClassProcessor extends BaseAnnotationProcessor {
    @Override
    public Class<? extends Annotation> handleAnnotationType() {
        return NewClass.class;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(NewClass.class).forEach(item -> {
            try {
                String name = ((TypeElement) item).getQualifiedName().toString();
                int ch = name.lastIndexOf('.');
                String packageName = name.substring(0, ch);
                createJavaFile(packageName, "HelloWorld").writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return false;
    }

    /**
     * $T 类占位符，用于替换代码中的类
     * $L 姑且叫它变量占位符吧，用法和String.format中的%s差不多，按照顺序依次替换里面的变量值
     * $S 字符串占位符，当我们需要在代码中使用字符串时，用这个替换
     * $N 名称占位符，比方说需要在一个方法里使用另一个方法，可以用这个替换
     */
    public static JavaFile createJavaFile(String packageName, String className) {
        FieldSpec fieldSpec = FieldSpec.builder(String.class, "name", Modifier.PRIVATE).build();
        MethodSpec methodSpec = MethodSpec.methodBuilder("getName").addModifiers(Modifier.PUBLIC).returns(String.class).addStatement("return this.name").build();
        MethodSpec main = MethodSpec.methodBuilder("main") //方法名
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC) //修饰符
                .returns(void.class)//返回值
                .addParameter(String[].class, "args")//参数
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")//内容
                .build();
        TypeSpec typeSpec = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC).addField(fieldSpec).addMethod(methodSpec).addMethod(main).build();
        return JavaFile.builder(packageName, typeSpec).build();
    }
}
