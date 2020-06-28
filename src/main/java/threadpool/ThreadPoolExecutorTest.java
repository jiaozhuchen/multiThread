package threadpool;

import lombok.SneakyThrows;

import java.util.concurrent.*;

import static java.lang.Thread.currentThread;

/**
 * 1. 测试ThreadPoolExecutor构造方法的使用
 * 2. 测试shutdown方法: shutdown以后不会再接受新任务，但会把已经在队列中的任务执行完
 * 3. 测试allowCoreThreadTimeOut字段的作用：
 *      默认线程增加以后，当线程数量小于等于核心线程数时，不回收
 *      这个字段设为true后，核心线程也会回收
 */
public class ThreadPoolExecutorTest {

    public static void main(String[] args) throws InterruptedException {
//        testAllowCoreThreadTimeOut(true);
//        testShutdown();
        testShutdownNow();
    }

    //测试shutdownNow方法的使用，线程池shutdownNow以后不会再接受新任务，已经在队列中的任务,还没开始执行的任务会被移除掉
    private static void testShutdownNow() throws InterruptedException {
        int corePoolSize = 5;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, corePoolSize, 100L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        for (int i = 0; i < corePoolSize * 2; i++) {
            threadPoolExecutor.execute(new Worker());
        }
        TimeUnit.MILLISECONDS.sleep(1L);
        threadPoolExecutor.shutdownNow();
    }

    //测试shutdown方法的使用，线程池shutdown以后不会再接受新任务，但会把已经在队列中的任务执行完
    private static void testShutdown() throws InterruptedException {
        int corePoolSize = 5;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, corePoolSize, 100L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        for (int i = 0; i < corePoolSize * 2; i++) {
            threadPoolExecutor.execute(new Worker());
        }
        TimeUnit.MILLISECONDS.sleep(1L);
        threadPoolExecutor.shutdown();
    }

    /**
     * 测试允许核心线程超时的设置
     * 如果allowCoreThreadTimeOut 传true 会看到核心线程到超时时间后也会被回收
     * 默认是维持核心线程数的
     *
     * @param allowCoreThreadTimeOut
     * @throws InterruptedException
     */
    private static void testAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) throws InterruptedException {
        int corePoolSize = 5;
        int maximumPoolSize = 10;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 100L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>());
        threadPoolExecutor.allowCoreThreadTimeOut(allowCoreThreadTimeOut);
        for (int i = 0; i < maximumPoolSize; i++) {
            threadPoolExecutor.execute(new Worker());
        }
        System.out.println("工作线程数量：" + threadPoolExecutor.getPoolSize());
        TimeUnit.SECONDS.sleep(1L);
        System.out.println("工作线程数量：" + threadPoolExecutor.getPoolSize());
        threadPoolExecutor.shutdown();
    }

    static class Worker implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            //对比shutDown和shutDownNow的区别
            int m =0;
            for(int i=0; i<100_000_000; i++) {
                m = i;
            }
            System.out.println(currentThread().getName() + " working " + m);
        }
    }
}
