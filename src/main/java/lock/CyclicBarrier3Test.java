package lock;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

/**
 * 测试CyclicBarrier.reset使用，以下例子中
 *  如果某线程异常后调用了cyclicBarrier.reset，和该线程本来应该是一组"被释放通过"的线程，不能在执行await后的方法了，且会抛出BrokenBarrierException异常
 *  如果异常后没有调用cyclicBarrier.reset（即将reset代码注释掉），会发现本来应该在一组被释放通过的线程和其他线程组成一组释放了
 */
public class CyclicBarrier3Test {

    private final static int THRESHOLD = 3;
    private final static CyclicBarrier cyclicBarrier = new CyclicBarrier(THRESHOLD);

    static class Worker implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println(currentThread().getName() + ": start");
                int randomValue = new Random().nextInt(4);
                if(randomValue >= THRESHOLD) {
                    throw new InterruptedException();
                }
                cyclicBarrier.await();
                System.out.println(currentThread().getName() + ": end");
            } catch (InterruptedException e) {
                System.out.println(currentThread().getName() + ": exception" );
                cyclicBarrier.reset();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int N = 10;
        for (int i = 0; i < N; ++i) {
            TimeUnit.SECONDS.sleep(2);
            new Thread(new Worker()).start();
        }
        System.out.println("main end");
    }

}
