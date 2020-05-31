package lock;

import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.currentThread;

/**
 * 测试countDownLatch的一种使用方式
 * 设置两个信号，一个是启动信号，当控制线程发出信号以后，所有线程才开始工作
 * 一个是全部完成的信号，当所有工作线程完成后，控制线程才继续工作
 * 比如：一个excel中有多个sheet需要处理，当全部处理完成以后返回处理成功
 */
public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        final int N = 3;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(N);

        for (int i = 0; i < N; ++i)
            new Thread(new Worker(startSignal, doneSignal)).start();
        doSomethingElse();            // don't let run yet
        startSignal.countDown();      // let all threads proceed
        doSomethingElse();
        doneSignal.await();
        System.out.println(currentThread().getName() + ": end");
    }

    private static void doSomethingElse() {
        System.out.println(currentThread().getName() + ": do something");
    }

    static class Worker implements Runnable {
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;

        public Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        @Override
        public void run() {
            try {
                startSignal.await();
                doWork();
                doneSignal.countDown();
                System.out.println(currentThread().getName() + ": finish work");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        private void doWork() {
            System.out.println(currentThread().getName() + ": is working");
        }
    }
}
