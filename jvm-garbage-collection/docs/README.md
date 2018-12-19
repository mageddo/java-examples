### Teste 1

```java
public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println(ManagementFactory.getRuntimeMXBean().getName());
		System.out.println("type some key to continue");
		System.in.read();
		readFiles();
		System.out.println("complete, type some key to terminate");
		System.in.read();
	}

	private static void readFiles() throws IOException {
		for (int i = 0; i < 100; i++) {
			final File _200mFile = new File("/tmp/200M.tmp");
			final byte[] bytes = FileUtils.readFileToByteArray(_200mFile);
			System.out.println("file read");
			readFileBytes(bytes);
		}
	}

	private static void readFileBytes(byte[] bytes) throws IOException {
		try(final BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream("/dev/null"))) {
			for (byte b : bytes) {
				fout.write(b);
			}
		}
	}
}
```

```
java -XX:+PrintCommandLineFlags -XX:+UseParallelGC Main
```

No gnome-system-monitor ficou no final com 2.7G

![](https://i.imgur.com/jf0hqVc.png)


#### Teste 2

Mesmo codigo do teste 1

No gnome-system-monitor ficou no final com 1.3G

```
java -XX:+PrintCommandLineFlags -XX:+UseParallelGC -Xmx1500m Main
```

![](https://i.imgur.com/Ic8PCHa.png)


#### Teste 3

Mesmo codigo do teste 1

No gnome-system-monitor ficou no final com 225M

```
java -XX:+PrintCommandLineFlags -XX:+UseSerialGC -Xmx1500m
```


![](https://i.imgur.com/EThj7zV.png)

#### Teste 4

Mesmo codigo do teste 1


```
java -XX:+PrintCommandLineFlags -XX:+UseG1GC
-XX:InitialHeapSize=195134400 -XX:MaxHeapSize=3122150400 -XX:+PrintCommandLineFlags -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC 
```


![](https://i.imgur.com/0rQ1JW5.png)


#### Teste 5


Mesmo codigo do teste 1

No gnome-system-monitor ficou no final com 1.5G

```
java -XX:+PrintCommandLineFlags -XX:+UseG1GC -Xmx1500m
-XX:InitialHeapSize=195134400 -XX:MaxHeapSize=1572864000 -XX:+PrintCommandLineFlags -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC 
```

![](https://i.imgur.com/ebe8ixQ.png)


#### Teste 6

Mesmo codigo do teste 1

```
-Xmx500m -XX:+UseG1GC -XX:+PrintCommandLineFlags -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDetails
```

Java 8.162

![](https://i.imgur.com/9wYzt86.jpg)

Java 10.0.2

![](https://i.imgur.com/sybutmD.jpg)

Conclusao: Só por usar a JDK 10 ela otimizou e alocou mais de 200 megas **a menos**
