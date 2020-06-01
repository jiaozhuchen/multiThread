package util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static java.lang.Thread.currentThread;

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
