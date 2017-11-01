package org.zdxue.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.time.FastDateFormat;
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
public class DateFormatBenchmark {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Benchmark
    public void jdkSimpleDateFormat() {
        SimpleDateFormat fmt = new SimpleDateFormat(DATE_PATTERN);
        fmt.format(new Date());
    }

    @Benchmark
    public void jdkLocalDateTime() {
        Instant instant = Instant.ofEpochMilli(new Date().getTime());
        LocalDateTime datetime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        datetime.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    @Benchmark
    public void fastDateFormat() {
        FastDateFormat fmt = FastDateFormat.getInstance(DATE_PATTERN);
        fmt.format(new Date());
    }

    @Benchmark
    public void cachingDateFormatter() {
        CachingDateFormatter fmt = new CachingDateFormatter(DATE_PATTERN);
        fmt.format(new Date());
    }

    static class CachingDateFormatter {
        private final FastDateFormat fastDateFormat;
        private AtomicReference<CacheTime> cache;

        public CachingDateFormatter(String pattern) {
            this.fastDateFormat = FastDateFormat.getInstance(pattern);

            long currentTime = System.currentTimeMillis();
            String formatted = fastDateFormat.format(currentTime);
            this.cache = new AtomicReference<CacheTime>(new CacheTime(currentTime, formatted));
        }

        public String format(Date date) {
            CacheTime cacheTime = cache.get();

            long timestamp = date.getTime();
            if (cacheTime.timestamp != timestamp) {
                String formatted = fastDateFormat.format(date);
                CacheTime newCacheTime = new CacheTime(timestamp, formatted);
                cache.compareAndSet(cacheTime, newCacheTime);
                return formatted;
            }

            return cacheTime.formatted;
        }

        static final class CacheTime {
            long timestamp;
            String formatted;

            public CacheTime(long timestamp, String formatted) {
                this.timestamp = timestamp;
                this.formatted = formatted;
            }
        }
    }

}
