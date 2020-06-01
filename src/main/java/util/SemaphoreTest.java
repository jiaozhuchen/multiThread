package util;

import java.util.concurrent.Semaphore;

import static java.lang.Thread.currentThread;

/**
 * 测试Semaphore的使用
 * 可以使用Semaphore做流量控制，对有限的公共资源做控制，比如数据库连接。
 * 假设有一个需求，要读取几万个文件的数据，因为都是IO密集型任务，我们可以启动几十个线程并发的读取，但如果处理后需要保存到数据库，
 * 而数据库的连接数只有十个，我们必须控制只有十个线程同时获取数据库链接，否则可能会报错无法获取数据库连接。这时就可以用Semaphore
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        final int N = 30;
        final int SEMAPHORE_COUNT = 1;
        Semaphore semaphore = new Semaphore(SEMAPHORE_COUNT);

        for (int i = 0; i < N; ++i)
            new Thread(new Worker(semaphore)).start();
        System.out.println(currentThread().getName() + ": end");
    }

    static class Worker implements Runnable {
        private final Semaphore semaphore;

        public Worker(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                doWork();
                semaphore.acquire();
                System.out.println(currentThread().getName() + ": save db");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                semaphore.release();
            }

        }

        private void doWork() {
            System.out.println(currentThread().getName() + ": is working");
        }
    }
}
