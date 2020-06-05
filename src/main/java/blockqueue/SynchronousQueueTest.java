package blockqueue;

import lombok.SneakyThrows;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

/**
 * 测试SynchronousQueue队列的使用
 *  可以看到子线程中的put方法一直在阻塞，直到主线程进行take后，子线程才继续往下执行
 */
public class SynchronousQueueTest {

    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<String> synchronousQueue = new SynchronousQueue();
        Thread thread = new Thread(new Worker(synchronousQueue));
        thread.start();
        System.out.println(currentThread().getName() + ": take");
        synchronousQueue.take();
        System.out.println(currentThread().getName() + ": end");

    }

    static class Worker implements Runnable {
        private SynchronousQueue<String> synchronousQueue;

        public Worker(SynchronousQueue<String> synchronousQueue) {
            this.synchronousQueue = synchronousQueue;
        }

        @SneakyThrows
        @Override
        public void run() {
            TimeUnit.SECONDS.sleep(1L);
            synchronousQueue.put("worker put element");
            System.out.println(currentThread().getName() + ": end");
        }
    }
}
