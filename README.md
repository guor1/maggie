### 介绍

关于Lombok的原理，解释起来就是一句话，即在编译期通过改变抽象语法树来修改java代码。

由于Java抽象语法树的API是在 tools.jar 中，没有文档介绍，本项目是记录关键API的用法，后期方便查阅，也可以根据需要进行扩展。

### 使用步骤

- 引入依赖

```xml

<dependency>
    <groupId>maggie</groupId>
    <artifactId>maggie</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

- 创建实体bean

```java
import maggie.annotation.PropAccess;

@PropAccess
public class UserBean {
    private String name;
}
```
