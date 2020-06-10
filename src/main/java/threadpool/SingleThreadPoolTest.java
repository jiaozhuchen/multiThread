package threadpool;

import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

/**
 * 测试newSingleThreadExecutor使用
 *  可以看到线程池中只有一个线程在运行, 在前面的线程执行成功之前，后面的任务只能进入阻塞队列等待
 *
 *  源码：
 *  public static ExecutorService newSingleThreadExecutor() {
 *         return new FinalizableDelegatedExecutorService
 *             (new ThreadPoolExecutor(1, 1,
 *                                     0L, TimeUnit.MILLISECONDS,
 *                                     new LinkedBlockingQueue<Runnable>()));
 *     }
 */
public class SingleThreadPoolTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
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
