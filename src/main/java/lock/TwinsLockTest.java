package lock;

import java.util.concurrent.TimeUnit;

public class TwinsLockTest {

    private static TwinsLock lock = new TwinsLock();
    static class Worker extends Thread {
        public void run() {
            testLockAndUnlock();
        }

        /**
         * 测试同一时刻只能有一个线程加锁成功
         */
        private void testLockAndUnlock() {
            lock.lock();
            try {
                TimeUnit.SECONDS.sleep(1L);
                System.out.println(Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        for(int i=0; i<10; i++) {
            Worker w = new Worker();
            w.start();
        }
    }
}
