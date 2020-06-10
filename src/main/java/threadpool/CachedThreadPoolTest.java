package threadpool;

import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

/**
 * 测试CachedThreadPool使用
 *  可以看到线程池使用了20个线程运行任务
 *    因为主线程for循环20次提交任务
 *    但每个线程执行都休眠了1s，执行很慢，每次提交新任务时，线程池的线程都在忙碌
 *    所以新建线程执行任务，最终新建了20个
 *
 *    如果将子线程中的sleep 1s 注释掉，执行任务的线程会有重复
 *
 *    一起看下源码：
 *      意思是核心线程数是0个，最大线程数是Integer.MAX_VALUE个，
 *      等待队列是SynchronousQueue不存储等待线程，线程池里有空闲线程就使用，没有就新建
 *    public static ExecutorService newCachedThreadPool() {
 *         return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
 *                                       60L, TimeUnit.SECONDS,
 *                                       new SynchronousQueue<Runnable>());
 *     }
 */
public class CachedThreadPoolTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i=0; i<20; i++) {
            executorService.execute(new Worker());
        }
        executorService.shutdown();
    }

    static class Worker implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            TimeUnit.SECONDS.sleep(1L);
            System.out.println(currentThread().getName() + " working");
        }
    }
}
