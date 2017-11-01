```
> mvn archetype:generate -DinteractiveMode=false -DarchetypeGroupId=org.openjdk.jmh -DarchetypeArtifactId=jmh-java-benchmark-archetype -DgroupId=org.zdxue.util -DartifactId=jmh_benchmark -Dversion=1.0
> cd jmh_benchmark
> mvn clean install
> java -jar ./target/benchmarks.jar MyBenchmark -wi 5 -i 5 -f 5
```

### Samples as follow: 
http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/