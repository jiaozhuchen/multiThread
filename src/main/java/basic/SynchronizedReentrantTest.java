package basic;

import java.util.concurrent.TimeUnit;

/**
 * 测试synchronized的使用及可重入性
 */
public class SynchronizedReentrantTest {
    synchronized void m1() {
        System.out.println("m1 start");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m2();
        System.out.println("m1 end");
    }

    synchronized void m2() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("m2 working");
    }

    public static void main(String[] args) {
        new SynchronizedReentrantTest().m1();
    }
}
