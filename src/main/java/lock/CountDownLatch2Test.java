package lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.currentThread;

/**
 * 另一个典型用法是将问题分为N个部分，
 *   每一部分用子线程跑，然后
 *   在锁存器上递减计数，当所有子线程都完成后，
 *   调用线程将能够继续运行。
 */
public class CountDownLatch2Test {

    public static void main(String[] args) throws InterruptedException {
        final int N = 3;
        CountDownLatch doneSignal = new CountDownLatch(1);
        ExecutorService e = Executors.newFixedThreadPool(3);
        try{
            for (int i = 0; i < N; ++i) // create and start threads
                e.submit(new WorkerRunnable(doneSignal, i));
            doneSignal.await();           // wait for all to finish
            System.out.println(currentThread().getName() + ": end");
        }finally {
            e.shutdown();
        }
    }

    static class WorkerRunnable implements Runnable {
        private final CountDownLatch doneSignal;
        private final int i;

        WorkerRunnable(CountDownLatch doneSignal, int i) {
            this.doneSignal = doneSignal;
            this.i = i;
        }

        public void run() {
            doWork(i);
            doneSignal.countDown();
        }

        void doWork(int i) {
            System.out.println(currentThread().getName() + ": " + i);
        }
    }
}

