package lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

/**
 * 简单测试CyclicBarrier的使用，可以看到当运行cyclicBarrier.await方法后进入等待，当等待量达到阈值3，大家一起向后执行
 */
public class CyclicBarrierTest {
    private final static CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

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
    }
}
