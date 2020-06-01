package util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static java.lang.Thread.currentThread;

/**
 * 测试LockSupport类park unpark的使用
 *  LockSupport类用于代替Object 的suspend和resume方法（这两个方法因为会造成死锁，已被废弃）
 */
public class LockSupportTest {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(()-> {
            System.out.println(currentThread() + ": is parking");
            LockSupport.park();
            System.out.println(currentThread() + ": unpark");
        });
        thread.start();
        TimeUnit.SECONDS.sleep(2L);
        LockSupport.unpark(thread);
    }
}
