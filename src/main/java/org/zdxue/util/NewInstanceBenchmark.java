package org.zdxue.util;

import java.util.concurrent.TimeUnit;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.All)
public class NewInstanceBenchmark {
    private static final Objenesis objenesis = new ObjenesisStd(true);
    
    @Benchmark
    public void jdkNewInstance() {
        new ForNewInstance();
    }

    @Benchmark
    public void jdkReflectNewInstance() {
        try {
            ForNewInstance.class.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void objenesisNewInstance() {
        objenesis.newInstance(ForNewInstance.class);
    }
    
    /*
    public static void main(String[] args) throws Exception {
        ForNewInstance obj1 = new ForNewInstance();
        ForNewInstance obj2 = ForNewInstance.class.newInstance();
        ForNewInstance obj3 = objenesis.newInstance(ForNewInstance.class);
        System.out.println(obj1);
        System.out.println(obj2);
        System.out.println(obj3);
    }
    */
    
}

class ForNewInstance {
    
}
