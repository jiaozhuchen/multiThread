package basic;

import java.util.concurrent.TimeUnit;

public class MultiThreadBase {

    private static Object res = new Object();
    public static void main(String[] args) throws InterruptedException {
        final MultiThreadBase multiThreadBase = new MultiThreadBase();
        testWaitAndNotify(multiThreadBase);
        testJoin(multiThreadBase);
    }

    private static void testJoin(MultiThreadBase multiThreadBase) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " testJoin start");
        Thread t = new Thread(()-> {
            System.out.println(Thread.currentThread().getName() + " start");
            try {
                TimeUnit.SECONDS.sleep(2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " end");
        });
        t.start();
        t.join();
        System.out.println(Thread.currentThread().getName() + " testJoin end");
    }

    private static void testWaitAndNotify(MultiThreadBase multiThreadBase) throws InterruptedException {
        for(int i=0; i< 5; i++) {
            new Thread(()-> {
                multiThreadBase.testWait();
            }).start();
        }
        TimeUnit.SECONDS.sleep(1L);
        System.out.println("---多线程已启动---");
        synchronized (res) {
            res.notify();
        }
        TimeUnit.SECONDS.sleep(1L);
        System.out.println("---一个线程已被唤醒---");
        synchronized (res) {
            res.notifyAll();
        }
        TimeUnit.SECONDS.sleep(1L);
        System.out.println("---全部等待线程已被唤醒---");
    }

    private void testWait() {
        synchronized (res) {
            System.out.println(Thread.currentThread().getName() + ": start");
            try {
                res.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ": end");
        }
    }


}
