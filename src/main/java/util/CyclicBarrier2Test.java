package util;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

/**
 * 测试CyclicBarrier的第二个功能，当到达的线程量达到阈值时，其中一个线程先执行barrierAction，再一起执行后续代码
 */
public class CyclicBarrier2Test {

    private final static CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> {
        System.out.println(currentThread().getName() + ": 我先说两句");
    });

    static class Worker implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println(currentThread().getName() + ": start");
                cyclicBarrier.await();
                System.out.println(currentThread().getName() + ": end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int N = 30;
        for (int i = 0; i < N; ++i) {
            TimeUnit.SECONDS.sleep(1);
            new Thread(new Worker()).start();
        }
        System.out.println("main end");
    }
}
