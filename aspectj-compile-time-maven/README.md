```bash
$ mvn clean compile
$ java -cp "target/classes:${HOME}/.m2/repository/org/aspectj/aspectjrt/1.8.13/aspectjrt-1.8.13.jar" com.huongdanjava.aspectj.Application
before
Hello
after
```

At compile time HelloWorld class was transpiled to the code bellow

```
package com.huongdanjava.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.runtime.reflect.Factory;

public class HelloWorld {
    public HelloWorld() {
    }

    public void hello() {
        JoinPoint var1 = Factory.makeJP(ajc$tjp_0, this, this);
        hello_aroundBody1$advice(this, var1, ApplicationAspect.aspectOf(), (ProceedingJoinPoint)var1);
    }

    static {
        ajc$preClinit();
    }
}
```