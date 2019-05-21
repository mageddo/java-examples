package com.mageddo.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class JMHSample_02_BenchmarkModes {

    /*
     * JMH generates lots of synthetic code for the benchmarks for you during
     * the benchmark compilation. JMH can measure the benchmark methods in lots
     * of modes. Users may select the default benchmark mode with a special
     * annotation, or select/override the mode via the runtime options.
     *
     * With this scenario, we start to measure something useful. Note that our
     * payload code potentially throws exceptions, and we can just declare them
     * to be thrown. If the code throws the actual exception, the benchmark
     * execution will stop with an error.
     *
     * When you are puzzled with some particular behavior, it usually helps to
     * look into the generated code. You might see the code is doing not
     * something you intend it to do. Good experiments always follow up on the
     * experimental setup, and cross-checking the generated code is an important
     * part of that follow up.
     *
     * The generated code for this particular sample is somewhere at
     * target/generated-sources/annotations/.../JMHSample_02_BenchmarkModes.java
     */

    /*
     * Mode.Throughput, as stated in its Javadoc, measures the raw throughput by
     * continuously calling the benchmark method in a time-bound iteration, and
     * counting how many times we executed the method.
     *
     * We are using the special annotation to select the units to measure in,
     * although you can use the default.
     */

    @Benchmark()
    @BenchmarkMode(Mode.SampleTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void measureThroughput() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(new Random().nextInt(150));
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHSample_02_BenchmarkModes.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }

}
