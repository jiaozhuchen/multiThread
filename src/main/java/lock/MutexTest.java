package lock;

import java.util.concurrent.TimeUnit;

public class MutexTest {

    private static Mutex lock = new Mutex();
    static class Worker extends Thread {
        public void run() {
//            testNonfairLock();
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

                //测试重入死锁, 当线程执行代码再次对其加锁时，线程发生死锁，卡死
//                lockAgain();
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        private void lockAgain() {
            lock.lock();
            System.out.println(System.currentTimeMillis());
            lock.unlock();
        }

        /**
         * 当线程存在循环竞争锁时，虽然线程A让出了锁，但线程A再次竞争到锁的概率很大，
         * 所以ReentrantLock默认是不公平的锁
         */
        private void testNonfairLock() {
            while(true) {
                testLockAndUnlock();
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
