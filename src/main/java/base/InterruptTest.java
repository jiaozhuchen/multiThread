package base;

import java.util.concurrent.TimeUnit;

/**
 * 该类用于测试Interrupt方法
 * NormalInterrupt测试表明调用线程的interrupt方法，不会中断正在运行的线程，该方法仅仅是设置中断状态，是否响应中断还要看具体代码
 *      当线程未调用Thread.currentThread().isInterrupted()时，子线程和主线程中的i结果都是999999999，说明完全执行结束，未中断线程
 *
 * SleepInterrupt测试表明当线程在sleep时，或者在wait，join时，
 *      调用线程的interrupt会使线程抛出异常
 *      且线程的interrupt状态会被清除
 */
public class InterruptTest {
    public static int i = 0;
    static int length = 100_000_000_0;
    public static void main(String[] args) throws InterruptedException {
        testNotInterruptIfSubThreadNotCatchInterruptState();
        testWillThrowExceptionWhenSleep();

    }

    private static void testNotInterruptIfSubThreadNotCatchInterruptState() throws InterruptedException {
        Thread t = new Thread(new NormalInterrupt());
        t.start();
        TimeUnit.SECONDS.sleep(1L);
        t.interrupt();
        System.out.println(Thread.currentThread().getName() + ": " + i);
    }

    static class NormalInterrupt implements Runnable {
        @Override
        public void run() {
            //在其他线程中发出中断信号，是否中断还要看线程内部方法怎么写的，没有判断isInterrupted()并不会中断哦
            //可以去掉for循环中的   !Thread.currentThread().isInterrupted()条件
            for(int j = 0; j< length && !Thread.currentThread().isInterrupted(); j++) {
                i = j;
            }
            System.out.println(Thread.currentThread().getName() + ": " + i);
        }
    }

    private static void testWillThrowExceptionWhenSleep() throws InterruptedException {
        Thread t = new Thread(new SleepInterrupt());
        t.start();
        TimeUnit.NANOSECONDS.sleep(1L);
        t.interrupt();
        System.out.println(Thread.currentThread().getName() + ": end");
    }

    static class SleepInterrupt implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": start");
            try {
                TimeUnit.SECONDS.sleep(2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().isInterrupted());//异常处理后，中断状态将被清除，所以结果是false
            System.out.println(Thread.currentThread().getName() + ": end");
        }
    }

}
