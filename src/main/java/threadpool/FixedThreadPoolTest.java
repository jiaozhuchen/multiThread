package threadpool;

import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

/**
 * 测试newFixedThreadPool使用
 *  可以看到线程池使用了四个线程运行任务，结果可以看出，四个线程四个线程的打印出来
 *  线程都在忙碌时进入阻塞队列等待
 *
 *  源码：意思是 创建核心线程数nThreads个，最大线程数也是nThreads个，
 *      提交任务后，如果线程都在忙碌，进入LinkedBlockingQueue等待
 *  public static ExecutorService newFixedThreadPool(int nThreads) {
 *         return new ThreadPoolExecutor(nThreads, nThreads,
 *                                       0L, TimeUnit.MILLISECONDS,
 *                                       new LinkedBlockingQueue<Runnable>());
 *     }
 */
public class FixedThreadPoolTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
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
