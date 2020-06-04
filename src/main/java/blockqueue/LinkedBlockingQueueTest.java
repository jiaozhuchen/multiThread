package blockqueue;

import lombok.SneakyThrows;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

/**
 * 测试LinkedBlockingQueue类的使用
 *  1.LinkedBlockingQueue是用链表实现的有界阻塞队列（默认支持Integer.MAX_VALUE个元素）
 *  2.入队方法：put方法队列满时等待， offer方法队列满时直接返回或者有指定时间的等待， add方法队列满时抛出异常
 *  3.出队方法：take方法队列空时等待， poll方法队列空时返回null或者有指定时间的等待， remove方法队列为空时抛出异常
 *  4.该队列时先进先出的队列
 *  5.源码可以看到LinkedBlockingQueue入队和出队各是一把锁，通常多线程并发下性能比ArrayBlockingQueue稍好
 */
public class LinkedBlockingQueueTest {

    private static final int CAPACITY = 10;
    private static final LinkedBlockingQueue<String> blockingQueue = new LinkedBlockingQueue(CAPACITY);

    public static void main(String[] args) throws InterruptedException {
        for(int i=0; i< 2; i++) {
            new Thread(new Worker()).start();
        }
        TimeUnit.MILLISECONDS.sleep(100);
        for(int i=0; i<CAPACITY * 2; i++) {
            System.out.println("main take: " + blockingQueue.take());
        }
    }

    static class Worker implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            for(int i=0; i<CAPACITY; i++) {
                blockingQueue.put(currentThread().getName() + ": " + i);
                System.out.println(currentThread().getName() + " put : " + i + ", 当前队列中的元素个数：" + blockingQueue.size());
            }
        }
    }
}
