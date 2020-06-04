package blockqueue;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

/**
 * 测试LinkedTransferQueue使用
 *  1. LinkedTransferQueue是先进先出队列
 *  2. put方法和transfer方法区别，put方法如果没有消费就直接进入等待队列，方法返回，transfer方法会等到数据被消费
 *  3. linkedTransferQueue是无界队列
 */
public class LinkedTransferQueueTest {

    public static void main(String[] args) throws InterruptedException {
        int N = 3;
        LinkedTransferQueue<String> linkedTransferQueue = new LinkedTransferQueue();
        for (int i = 0; i < N; i++) {
            linkedTransferQueue.put(currentThread().getName() + ": " + i);
        }

        Thread thread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1L);
                while (linkedTransferQueue.size() > 0) {
                    System.out.println(linkedTransferQueue.take());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        System.out.println(currentThread().getName() + ": " + "transfer start");
        linkedTransferQueue.transfer(currentThread().getName() + ": " + N);
        System.out.println(currentThread().getName() + ": " + "transfer end");

    }
}
