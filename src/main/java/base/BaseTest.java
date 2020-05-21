package base;

import java.util.ArrayList;
import java.util.List;

/**
 * 看完MultiThreadBase类和InterruptTest类后我们做个小练习吧
 */
public class BaseTest {

    /**
     * 看看这个类的写法是否有问题，如果有问题，哪里有问题
     */
    static class MultiStack {
        private List<String> list = new ArrayList<String>();
        public synchronized void push(String value) {
            list.add(value);
            notify();
        }
        public synchronized String pop() throws InterruptedException {
            if (list.size() <= 0) {
                wait();
            }
            return list.remove(list.size() - 1);
        }
    }

    static class MyThread extends Thread {
        private int index = 0;
        private final MultiStack stack;
        private final int push_frequency = 5; //增加出错的可能性
        MyThread(MultiStack stack) {
            this.stack = stack;
        }
        public void run() {
            while(true) {
                if(index++ % push_frequency == 0) {
                    stack.push(String.valueOf(index));
                }else {
                    try {
                        System.out.println(stack.pop());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 如无意外，多尝试两次应该会发生数组越界异常
     * 分析原因：可能的情形有多种但原理类似，分析其中一种
     *  并发场景下，多线程对stack对象进行争用，假设有A B C 三个线程
     *  线程A竞争到了锁，然后进行pop，当list的length为0时，会进行等待同时释放stack对象的锁
     *  线程B竞争得到了锁，然后进行push，会对其加锁，调用list.add,这时如果线程C进行pop则C进入阻塞状态
     *  当B添加完成调用notify后，B被唤醒，如果B只是被唤醒没有竞争到锁，锁被C获取了，则C pop成功，释放锁，这时B继续处理就会产生数组越界
     */
    private static void testMultiStack() {
        MultiStack stack = new MultiStack();
        for(int i=0; i<10; i++) {
            MyThread t = new MyThread(stack);
            t.start();
        }
    }

    public static void main(String[] args) {
//        testMultiStack();
        testInterrupt();
    }

    public static void testInterrupt() {
        GeneralInterrupt si = new GeneralInterrupt();
        Thread t = new Thread(si);
        t.start();
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException x) {
            System.out.println("------- main InterruptedException--------------");
        }
        System.out.println("in main() - interrupt other thread");
        t.interrupt();
        System.out.println("in main() - leaving");
    }

    static class GeneralInterrupt extends Object
            implements Runnable {
        public void run() {
            try {
                System.out.println("in run() - about to work()");
                work();
                System.out.println("in run() - back from  work()");
            }catch (InterruptedException x) {
                System.out.println("in run() - isInterrupted()=" + Thread.currentThread().isInterrupted());
                return;
            }
            System.out.println("in run() - leaving normally");
        }
        public void work() throws InterruptedException {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("work method before sleep isInterrupted()="+ Thread.currentThread().isInterrupted());
                    Thread.sleep(2000);
                    System.out.println("work method after sleep isInterrupted()="+ Thread.currentThread().isInterrupted());
                }
            }
        }
    }

}
