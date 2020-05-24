package lock;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static java.lang.Thread.currentThread;
import static java.util.Collections.reverse;

/**
 * 测试非公平重入锁和公平重入锁的使用
 *  非公平重入锁是默认的重入锁，并发性能较好，但可以造成饥饿现象
 *  公平重入锁并发性能差，但不会造成饥饿现象，大家可以按需使用
 */
public class ReentrantLockTest {

    public static void main(String[] args) {
//        testNonFairLock();//通过结果可以看到非公平锁，线程不是按顺序调用的，更偏向于再次获取锁，这就可能造成线程饥饿
        testFairLock(); //通过结果可以看到公平锁，线程按顺序调用，一次调用成功以后就交给下一个等待节点了
    }

    private static void testNonFairLock() {
        ReentrantLock2 lock = new ReentrantLock2(); //构建不公平的锁
        for(int i=0; i<5; i++) {
            new Thread(new MyRunnable(lock)).start();
        }
    }

    private static void testFairLock() {
        ReentrantLock2 lock = new ReentrantLock2(true);//构建公平锁
        for(int i=0; i<5; i++) {
            new Thread(new MyRunnable(lock)).start();
        }
    }

    /**
     * 1.测试ReentrantLock的使用是可重入的
     * 2.通过连续十次锁定释放锁，看打印的结果是公平调用锁还是非公平的调用
     * 3.测试ReentrantLock的锁和synchronized加锁不冲突
     */
    static class MyRunnable implements Runnable {
        private final ReentrantLock2 lock;

        public MyRunnable(ReentrantLock2 lock) {
            this.lock = lock;
        }
        @Override
        public void run() {
            for(int i=0; i<10; i++) {
                lock.lock();
                try {
                    testCanReentrant();
                }finally {
                    lock.unlock();
                }
            }
        }

        private synchronized void testCanReentrant() { //synchronized 在这里仅仅用来测试lock和synchronized锁定不冲突
            lock.lock();//外层函数加锁以后，可以再次加锁说明是可以重入的
            try {
                System.out.println("locked by " + currentThread().getId() + ", waiting by " +  lock.getQueuedThreadIds());
            }finally {
                lock.unlock();
            }
        }
    }


    /**
     * 使用ReentrantLock2继承ReentrantLock，为了调用父类的getQueuedThreads方法观察队列等待情况
     */
    private static class ReentrantLock2 extends ReentrantLock {
        public ReentrantLock2() {
            super();
        }
        public ReentrantLock2(boolean fair) {
            super(fair);
        }

        /**
         * super.getQueuedThreads()获取等待线程队列
         * 看源码可以知道，等待队列是从尾节点获取的，为了方便观察先将队列反转
         * 再得到线程ID输出，观察等待队列的情况
         * @return
         */
        public String getQueuedThreadIds() {
            ArrayList<Thread> threads = new ArrayList<>(super.getQueuedThreads());
            reverse(threads);
            //使用Java8流式操作
            return threads.stream().map(Thread::getId).map(String::valueOf).collect(Collectors.joining(","));
        }
    }
}
