package lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.currentThread;

public class ConditionTest {

    public static void main(String[] args) throws InterruptedException {
        testUseCondition();
    }

    private static void testUseCondition() throws InterruptedException {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Thread t0 = new Thread(() -> {
            lock.lock();
            System.out.println(currentThread().getName() + " 线程加锁运行");
            try {
                System.out.println(currentThread().getName() + " 线程进入等待，释放锁资源");
                condition.await();
                System.out.println(currentThread().getName() + " 线程被唤醒，继续运行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        });
        t0.start();

        TimeUnit.SECONDS.sleep(2L);

        Thread t1 = new Thread(() -> {
            lock.lock();
            System.out.println(currentThread().getName() + " 线程加锁运行");
            System.out.println(currentThread().getName() + " 线程唤醒condition等待");
            condition.signal();
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(currentThread().getName() + " 运行完成");
            lock.unlock();
        });
        t1.start();
    }
}
