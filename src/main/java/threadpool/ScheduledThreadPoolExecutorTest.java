package threadpool;

import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

/**
 * 测试ScheduledThreadPoolExecutor的使用
 *  1. scheduleAtFixedRate 方法是每隔一段时间执行一次
 *      如例子中每2秒执行一次，虽然子线程方法需要1秒的执行时间，但还是每2秒打印一次
 *      那么如果子线程执行一次需要4秒呢？那子线程执行完后会接着执行，即每4秒打印一次
 *  2. scheduleWithFixedDelay 方法是子线程执行后，再隔一段时间
 *      如例子子线程执行一次需要1秒，再隔2秒， 就是每3秒打印1次
 *      那么如果子线程执行一次需要4秒呢？那子线程会再等2秒，即6秒打印一次
 *  3. 测试可以多线程并发执行定时调度任务
 */
public class ScheduledThreadPoolExecutorTest {

    public static void main(String[] args) throws InterruptedException {
        testFixedRate();
//        testFixedDelay();
//        testFixedRateMulti();
    }

    private static void testFixedDelay() throws InterruptedException {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleWithFixedDelay(new Worker("task"), 1L, 2L, TimeUnit.SECONDS);
        TimeUnit.MINUTES.sleep(1L);
        scheduledThreadPool.shutdown();
    }
    private static void testFixedRate() throws InterruptedException {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleAtFixedRate(new Worker("task"), 1L, 2L, TimeUnit.SECONDS);
        TimeUnit.MINUTES.sleep(1L);
        scheduledThreadPool.shutdown();
    }

    private static void testFixedRateMulti() throws InterruptedException {
        int N = 3;
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(N);
        for (int i = 0; i < N; i++) {
            scheduledThreadPool.scheduleAtFixedRate(new Worker("task" + i), 1L, 2L, TimeUnit.SECONDS);
        }
        TimeUnit.MINUTES.sleep(1L);
        scheduledThreadPool.shutdown();
    }

    static class Worker implements Runnable {
        private String name;

        public Worker(String name) {
            this.name = name;
        }

        @SneakyThrows
        @Override
        public void run() {
            int n = new Random().nextInt(4);
            TimeUnit.SECONDS.sleep(n);
            System.out.println(currentThread().getName() + ": " + System.currentTimeMillis() + ": " + name + " working");
        }
    }
}
