package blockqueue;

import java.util.concurrent.LinkedBlockingDeque;

import static java.lang.Thread.currentThread;

/**
 * 测试LinkedBlockingDeque使用
 *  LinkedBlockingDeque 可以实现FIFO队列和LIFO队列
 */
public class LinkedBlockingDequeTest {

    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingDeque<String> blockingDeque = new LinkedBlockingDeque();
        int N = 3;
        for (int i = 0; i < N; i++) {
            blockingDeque.putFirst(currentThread().getName() + ": " + i);
        }

        while (blockingDeque.size() > 0) {
            System.out.println(blockingDeque.takeFirst());
        }
    }
}
