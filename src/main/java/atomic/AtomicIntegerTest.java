package atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试AtomicInteger的使用，可以看到多线程并发操作依然能保证其结果的正确性
 */
public class AtomicIntegerTest {
    private static int normalInteger = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new AddInteger());
        Thread t2 = new Thread(new AddInteger());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("普通Integer对象多线程++操作结果：" + normalInteger);
        System.out.println("AtomicInteger对象多线程累加操作结果：" + atomicInteger.intValue());
    }

    static class AddInteger implements Runnable {
        @Override
        public void run() {
            for(int i=0; i<10000; i++) {
                normalInteger++;
                atomicInteger.getAndIncrement();
            }
        }
    }
}
