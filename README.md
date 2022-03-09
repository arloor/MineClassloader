## 类加载隔离实现

classloader模块是自定义的classloader

InnerModule模块中的类，在package时会将`.class`重命名为`.classd`,保证默认的类加载器加载不到，具体看InnerModule模块的pom文件。

测试代码：

新起一个项目（在本项目中测试，会被ide干扰）

```xml
<dependencies>
    <dependency>
        <groupId>com.arloor</groupId>
        <artifactId>classloader</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

```java
import com.arloor.classloader.ShadedClassLoader;

import java.io.File;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws Exception{
        final ShadedClassLoader shadedClassLoader = new ShadedClassLoader(ClassLoader.getSystemClassLoader());
        final Class<?> CatClass = shadedClassLoader.loadClass("com.arloor.inner.Cat");
        // 获取resource
        final InputStream in = CatClass.getClassLoader().getResourceAsStream("text");
        final byte[] bytes = in.readAllBytes();
        System.out.println(new String(bytes));
        // 确认classloader
        System.out.println(CatClass.getClassLoader());
        final Method say = CatClass.getMethod("say");
        say.invoke(CatClass);
    }
}
```

输出：

```shell
text
com.arloor.classloader.ShadedClassLoader@1c9b0314
cat
```

## 参考

参考的是es的apm-agent实现的：

https://github.com/elastic/apm-agent-java/blob/acdafe604ea3bc5944b4db04c4605f52a6d9f273/elastic-apm-agent/src/main/java/co/elastic/apm/agent/premain/ShadedClassLoader.java#L49

https://github.com/elastic/apm-agent-java/blob/15f967dc113cabc77c917ad1906c495e8fb2a9c7/apm-agent-bootstrap/pom.xml