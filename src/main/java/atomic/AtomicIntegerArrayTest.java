package atomic;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 测试AtomicIntegerArray的使用
 */
public class AtomicIntegerArrayTest {
    static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[]{0, 0});
    static int[] array = new int[] {0, 0};
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new IntegerArrayItemAdd());
        Thread t2 = new Thread(new IntegerArrayItemAdd());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("普通Integer数组元素多线程++操作结果：" + Arrays.toString(array));
        System.out.println("AtomicIntegerArray数组元素多线程累加操作结果：" + atomicIntegerArray.toString());
    }

    static class IntegerArrayItemAdd implements Runnable {
        @Override
        public void run() {
            for(int i=0; i<10000; i++) {
                for(int j=0; j<array.length; j++) {
                    atomicIntegerArray.incrementAndGet(j);
                    array[j]++;
                }
            }

        }
    }
}
