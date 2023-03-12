By some reason native-image won't generate arm64 binary because of `jna-platform` dependency along with quarkus, while
it works great on x64.

Issue opened on quarkus project: https://github.com/quarkusio/quarkus/issues/31750

```bash
$ docker run --rm --privileged multiarch/qemu-user-static --reset -p yes
$ docker-compose build -progress=plain
```

<details>
<summary>log</summary>

```
docker-compose build --progress=plain
#1 [internal] load build definition from Dockerfile.builder.linux-aarch64
#1 transferring dockerfile: 484B done
#1 DONE 0.0s

#2 [internal] load .dockerignore
#2 transferring context: 34B done
#2 DONE 0.0s

#3 [internal] load metadata for ghcr.io/graalvm/graalvm-ce:22.3.1@sha256:74154179185b623f8a707449a123022299dfd97fce584eb53fcf5b145116818e
#3 DONE 0.0s

#4 [1/5] FROM ghcr.io/graalvm/graalvm-ce:22.3.1@sha256:74154179185b623f8a707449a123022299dfd97fce584eb53fcf5b145116818e
#4 CACHED

#5 [internal] load build context
#5 transferring context: 10.28kB 0.0s done
#5 DONE 0.1s

#6 [2/5] RUN gu install native-image
#6 0.686 Downloading: Component catalog from www.graalvm.org
#6 1.799 Processing Component: Native Image
#6 1.821 Downloading: Component native-image: Native Image from github.com
#6 2.923 Installing new component: Native Image (org.graalvm.native-image, version 22.3.1)
#6 11.60 Refreshed alternative links in /usr/bin/
#6 DONE 11.7s

#7 [3/5] COPY ./ /app
#7 DONE 0.3s

#8 [4/5] WORKDIR /app
#8 DONE 0.1s

#9 [5/5] RUN uname -m && ./gradlew build -Dquarkus.package.type=native     -Dquarkus.native.additional-build-args="-Djdk.lang.Process.launchMechanism=fork","-J-Djdk.lang.Process.launchMechanism=fork"
#9 0.350 aarch64
#9 0.807 Picked up JAVA_TOOL_OPTIONS: -Djdk.lang.Process.launchMechanism=fork
#9 2.267 Downloading https://services.gradle.org/distributions/gradle-7.5.1-bin.zip
#9 5.926 ...........10%............20%...........30%............40%...........50%............60%...........70%............80%...........90%............100%
#9 17.68 
#9 17.68 Welcome to Gradle 7.5.1!
#9 17.68 
#9 17.68 Here are the highlights of this release:
#9 17.68  - Support for Java 18
#9 17.68  - Support for building with Groovy 4
#9 17.68  - Much more responsive continuous builds
#9 17.68  - Improved diagnostics for dependency resolution
#9 17.68 
#9 17.68 For more details see https://docs.gradle.org/7.5.1/release-notes.html
#9 17.68 
#9 18.67 Starting a Gradle Daemon (subsequent builds will be faster)
#9 121.6 > Task :processResources
#9 151.7 > Task :quarkusGenerateCode
#9 174.1 > Task :quarkusGenerateCodeDev
#9 189.5 > Task :compileJava
#9 189.6 > Task :classes
#9 190.0 > Task :jar
#9 207.4 > Task :quarkusGenerateCodeTests
#9 224.5 > Task :compileTestJava
#9 224.8 > Task :processTestResources NO-SOURCE
#9 224.8 > Task :testClasses
#9 235.4 
#9 235.4 > Task :test
#9 235.4 Picked up JAVA_TOOL_OPTIONS: -Djdk.lang.Process.launchMechanism=fork
#9 317.1 
#9 317.1 > Task :quarkusBuild
#9 338.7 
#9 338.7 ========================================================================================================================
#9 338.7 GraalVM Native Image: Generating 'quarkus-example-1.0.0-runner' (executable)...
#9 338.7 ========================================================================================================================
#9 374.3 [1/7] Initializing...                                                                                   (56.3s @ 0.21GB)
#9 374.3  Version info: 'GraalVM 22.3.1 Java 17 CE'
#9 374.3  Java version info: '17.0.6+10-jvmci-22.3-b13'
#9 374.3  C compiler: gcc (redhat, aarch64, 11.3.1)
#9 374.3  Garbage collector: Serial GC
#9 374.3  3 user-specific feature(s)
#9 374.3  - io.quarkus.runner.Feature: Auto-generated class by Quarkus from the existing extensions
#9 374.3  - io.quarkus.runtime.graal.DisableLoggingFeature: Disables INFO logging during the analysis phase for the [org.jboss.threads] categories
#9 374.3  - io.quarkus.runtime.graal.ResourcesFeature: Register each line in META-INF/quarkus-native-resources.txt as a resource on Substrate VM
^A^A
#9 761.0 [2/7] Performing analysis...  [*]                                                                      (384.0s @ 0.93GB)
#9 761.0   10,802 (87.93%) of 12,285 classes reachable
#9 761.0   14,839 (56.78%) of 26,134 fields reachable
#9 761.1   47,685 (50.42%) of 94,575 methods reachable
#9 761.1      541 classes,   125 fields, and 3,033 methods registered for reflection
#9 761.1 
#9 761.2 Error: Classes that should be initialized at run time got initialized during image building:
#9 761.2  com.sun.jna.Native the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.Native got initialized use --trace-class-initialization=com.sun.jna.Native
#9 761.2 com.sun.jna.NativeLibrary the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.NativeLibrary got initialized use --trace-class-initialization=com.sun.jna.NativeLibrary
#9 761.2 com.sun.jna.win32.W32APITypeMapper the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.win32.W32APITypeMapper got initialized use --trace-class-initialization=com.sun.jna.win32.W32APITypeMapper
#9 761.2 com.sun.jna.Callback the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.Callback got initialized use --trace-class-initialization=com.sun.jna.Callback
#9 761.2 com.sun.jna.CallbackReference$AttachOptions the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.CallbackReference$AttachOptions got initialized use --trace-class-initialization=com.sun.jna.CallbackReference$AttachOptions
#9 761.2 com.sun.jna.Structure the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.Structure got initialized use --trace-class-initialization=com.sun.jna.Structure
#9 761.2 com.sun.jna.CallbackReference the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.CallbackReference got initialized use --trace-class-initialization=com.sun.jna.CallbackReference
#9 761.2 com.sun.jna.platform.unix.Resource$Rlimit the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.platform.unix.Resource$Rlimit got initialized use --trace-class-initialization=com.sun.jna.platform.unix.Resource$Rlimit
#9 761.2 com.sun.jna.platform.unix.LibC the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.platform.unix.LibC got initialized use --trace-class-initialization=com.sun.jna.platform.unix.LibC
#9 761.2 com.sun.jna.platform.unix.LibCAPI$size_t the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.platform.unix.LibCAPI$size_t got initialized use --trace-class-initialization=com.sun.jna.platform.unix.LibCAPI$size_t
#9 761.2 com.sun.jna.win32.W32APIOptions the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.win32.W32APIOptions got initialized use --trace-class-initialization=com.sun.jna.win32.W32APIOptions
#9 761.2 To see how the classes got initialized, use --trace-class-initialization=com.sun.jna.Native,com.sun.jna.NativeLibrary,com.sun.jna.win32.W32APITypeMapper,com.sun.jna.Callback,com.sun.jna.CallbackReference$AttachOptions,com.sun.jna.Structure,com.sun.jna.CallbackReference,com.sun.jna.platform.unix.Resource$Rlimit,com.sun.jna.platform.unix.LibC,com.sun.jna.platform.unix.LibCAPI$size_t,com.sun.jna.win32.W32APIOptions
#9 761.2 com.oracle.svm.core.util.UserError$UserException: Classes that should be initialized at run time got initialized during image building:
#9 761.2  com.sun.jna.Native the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.Native got initialized use --trace-class-initialization=com.sun.jna.Native
#9 761.2 com.sun.jna.NativeLibrary the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.NativeLibrary got initialized use --trace-class-initialization=com.sun.jna.NativeLibrary
#9 761.2 com.sun.jna.win32.W32APITypeMapper the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.win32.W32APITypeMapper got initialized use --trace-class-initialization=com.sun.jna.win32.W32APITypeMapper
#9 761.2 com.sun.jna.Callback the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.Callback got initialized use --trace-class-initialization=com.sun.jna.Callback
#9 761.2 com.sun.jna.CallbackReference$AttachOptions the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.CallbackReference$AttachOptions got initialized use --trace-class-initialization=com.sun.jna.CallbackReference$AttachOptions
#9 761.2 com.sun.jna.Structure the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.Structure got initialized use --trace-class-initialization=com.sun.jna.Structure
#9 761.2 com.sun.jna.CallbackReference the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.CallbackReference got initialized use --trace-class-initialization=com.sun.jna.CallbackReference
#9 761.2 com.sun.jna.platform.unix.Resource$Rlimit the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.platform.unix.Resource$Rlimit got initialized use --trace-class-initialization=com.sun.jna.platform.unix.Resource$Rlimit
#9 761.2 com.sun.jna.platform.unix.LibC the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.platform.unix.LibC got initialized use --trace-class-initialization=com.sun.jna.platform.unix.LibC
#9 761.2 com.sun.jna.platform.unix.LibCAPI$size_t the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.platform.unix.LibCAPI$size_t got initialized use --trace-class-initialization=com.sun.jna.platform.unix.LibCAPI$size_t
#9 761.2 com.sun.jna.win32.W32APIOptions the class was requested to be initialized at run time (from 'META-INF/native-image/jni-jna/native-image.properties' in 'file:///app/build/quarkus-example-1.0.0-native-image-source-jar/quarkus-example-1.0.0-runner.jar' with 'com.sun.jna'). To see why com.sun.jna.win32.W32APIOptions got initialized use --trace-class-initialization=com.sun.jna.win32.W32APIOptions
#9 761.2 To see how the classes got initialized, use --trace-class-initialization=com.sun.jna.Native,com.sun.jna.NativeLibrary,com.sun.jna.win32.W32APITypeMapper,com.sun.jna.Callback,com.sun.jna.CallbackReference$AttachOptions,com.sun.jna.Structure,com.sun.jna.CallbackReference,com.sun.jna.platform.unix.Resource$Rlimit,com.sun.jna.platform.unix.LibC,com.sun.jna.platform.unix.LibCAPI$size_t,com.sun.jna.win32.W32APIOptions
#9 761.2        at org.graalvm.nativeimage.builder/com.oracle.svm.core.util.UserError.abort(UserError.java:73)
#9 761.2        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.classinitialization.ProvenSafeClassInitializationSupport.checkDelayedInitialization(ProvenSafeClassInitializationSupport.java:273)
#9 761.2        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.classinitialization.ClassInitializationFeature.duringAnalysis(ClassInitializationFeature.java:164)
#9 761.2        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGenerator.lambda$runPointsToAnalysis$10(NativeImageGenerator.java:748)
#9 761.2 ------------------------------------------------------------------------------------------------------------------------
#9 761.2        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.FeatureHandler.forEachFeature(FeatureHandler.java:85)
#9 761.2        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGenerator.lambda$runPointsToAnalysis$11(NativeImageGenerator.java:748)
#9 761.2        at org.graalvm.nativeimage.pointsto/com.oracle.graal.pointsto.AbstractAnalysisEngine.runAnalysis(AbstractAnalysisEngine.java:162)
#9 761.2        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGenerator.runPointsToAnalysis(NativeImageGenerator.java:745)
#9 761.2        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGenerator.doRun(NativeImageGenerator.java:578)
#9 761.2        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGenerator.run(NativeImageGenerator.java:535)
#9 761.2        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGeneratorRunner.buildImage(NativeImageGeneratorRunner.java:403)
#9 761.2        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGeneratorRunner.build(NativeImageGeneratorRunner.java:580)
#9 761.2        at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGeneratorRunner.main(NativeImageGeneratorRunner.java:128)
#9 761.2                         9.5s (2.1% of total time) in 42 GCs | Peak RSS: 2.66GB | CPU load: 5.16
#9 761.2 ========================================================================================================================
#9 761.2 Failed generating 'quarkus-example-1.0.0-runner' after 7m 23s.
#9 761.4 Error: Image build request failed with exit status 1
#9 761.8 
#9 761.8 > Task :quarkusBuild FAILED
#9 761.9 
#9 761.9 FAILURE: Build failed with an exception.
#9 761.9 
#9 761.9 * What went wrong:
#9 761.9 Execution failed for task ':quarkusBuild'.
#9 761.9 > io.quarkus.builder.BuildException: Build failure: Build failed due to errors
#9 761.9        [error]: Build step io.quarkus.deployment.pkg.steps.NativeImageBuildStep#build threw an exception: io.quarkus.deployment.pkg.steps.NativeImageBuildStep$ImageGenerationFailureException: Image generation failed. Exit code: 1
#9 761.9        at io.quarkus.deployment.pkg.steps.NativeImageBuildStep.imageGenerationFailed(NativeImageBuildStep.java:421)
#9 761.9        at io.quarkus.deployment.pkg.steps.NativeImageBuildStep.build(NativeImageBuildStep.java:262)
#9 761.9        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
#9 761.9        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
#9 761.9        at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
#9 761.9        at java.base/java.lang.reflect.Method.invoke(Method.java:568)
#9 761.9        at io.quarkus.deployment.ExtensionLoader$3.execute(ExtensionLoader.java:909)
#9 761.9        at io.quarkus.builder.BuildContext.run(BuildContext.java:281)
#9 761.9        at org.jboss.threads.ContextHandler$1.runWith(ContextHandler.java:18)
#9 761.9        at org.jboss.threads.EnhancedQueueExecutor$Task.run(EnhancedQueueExecutor.java:2449)
#9 761.9        at org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run(EnhancedQueueExecutor.java:1478)
#9 761.9        at java.base/java.lang.Thread.run(Thread.java:833)
#9 761.9        at org.jboss.threads.JBossThread.run(JBossThread.java:501)
#9 761.9 
#9 761.9 
#9 761.9 * Try:
#9 761.9 > Run with --stacktrace option to get the stack trace.
#9 761.9 > Run with --info or --debug option to get more log output.
#9 761.9 > Run with --scan to get full insights.
#9 761.9 
#9 761.9 * Get more help at https://help.gradle.org
#9 761.9 
#9 761.9 BUILD FAILED in 12m 40s
#9 761.9 9 actionable tasks: 9 executed
#9 ERROR: executor failed running [/bin/sh -c uname -m && ./gradlew build -Dquarkus.package.type=native     -Dquarkus.native.additional-build-args="-Djdk.lang.Process.launchMechanism=fork","-J-Djdk.lang.Process.launchMechanism=fork"]: exit code: 1
------
 > [5/5] RUN uname -m && ./gradlew build -Dquarkus.package.type=native     -Dquarkus.native.additional-build-args="-Djdk.lang.Process.launchMechanism=fork","-J-Djdk.lang.Process.launchMechanism=fork":
#9 761.9 
#9 761.9 * Try:
#9 761.9 > Run with --stacktrace option to get the stack trace.
#9 761.9 > Run with --info or --debug option to get more log output.
#9 761.9 > Run with --scan to get full insights.
#9 761.9 
#9 761.9 * Get more help at https://help.gradle.org
#9 761.9 
#9 761.9 BUILD FAILED in 12m 40s
#9 761.9 9 actionable tasks: 9 executed
------
failed to solve: executor failed running [/bin/sh -c uname -m && ./gradlew build -Dquarkus.package.type=native     -Dquarkus.native.additional-build-args="-Djdk.lang.Process.launchMechanism=fork","-J-Djdk.lang.Process.launchMechanism=fork"]: exit code: 1

```

</details>

But on Linux x64 works great
```bash
$ uname -sm
Linux x86_64

$ ./gradlew build -Dquarkus.package.type=native
Finished generating 'quarkus-example-1.0.0-runner' in 1m 9s.
```

