package lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.Thread.currentThread;

/**
 * 测试ReentrantReadWriteLock的使用
 *  1. 写锁互斥,加了写锁以后就其他线程不能再加读锁或写锁了，读锁不互斥，加了读锁以后自己或其他线程还能加读锁
 *  2. 支持锁降级， 加了写锁以后，自己的线程还能加写锁或者读锁
 *  3. 不支持锁升级，加了读锁以后，自己的线程也不能加写锁（因为读锁是共享的，自己加了别人也可能加）
 */
public class ReentrantReadWriteLockTest {
    private final Map<String, String> cacheMap = new HashMap<String, String>();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock readLock = rwl.readLock();
    private final Lock writeLock = rwl.writeLock();

    public String get(String key) {
        readLock.lock();
        try {
            return cacheMap.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public String put(String key, String value) {
        writeLock.lock();
        try {
            return cacheMap.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public void testUseReadWriteLock() {
        int N = 3;
        for (int i=0; i<N; i++) {
            Thread thread = new Thread(() -> {
                for(int j=0; j<100; j++) {
                    put(currentThread().getName()+j, currentThread().getName()+j);
                    System.out.println(get(currentThread().getName()+j));
                }
            });
            thread.start();
        }
    }

    //测试当加了读锁以后，写锁无法加锁成功，即使是自己加的读锁也不行
    //该方法会产生死锁， 程序无法结束
    public void testCannotWriteWhenReadLock() {
        readLock.lock();
        try {
            System.out.println("can read");
            writeLock.lock();
            try {
                System.out.println("test write");
            } finally {
                writeLock.unlock();
            }
        } finally {
            readLock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantReadWriteLockTest test = new ReentrantReadWriteLockTest();
        test.testUseReadWriteLock();
//        test.testCannotWriteWhenReadLock();
    }

}
