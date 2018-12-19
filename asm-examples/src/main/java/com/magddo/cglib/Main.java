package com.magddo.cglib;

import com.magddo.asm.Person;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class Main {
	public static void main(String[] args) {
		final Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Person.class);
		enhancer.setCallback(new MethodInterceptor() {
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				if(method.getName().equals("getName")){
					System.out.println("proxied");
					return String.valueOf(System.nanoTime());
				}
				System.out.println("not proxied");
				return proxy.invokeSuper(obj, args);
			}
		});

		final Person proxy = (Person) enhancer.create(new Class[]{String.class}, new Object[]{"Elvis"});
		final String res = proxy.getName();
		proxy.setName("John");
		System.out.println(res);
	}
}
