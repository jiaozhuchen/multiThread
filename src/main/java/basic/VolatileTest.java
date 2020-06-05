package basic;

import java.util.concurrent.TimeUnit;

/**
 * volatile测试
 *  volatile关键字我们知道有两个作用，1. 保证内存可见性  2. 禁止指令重排序
 *  下面的例子可以看到 虽然主线程将flag设为1但子线程还是无法停止，解决方法可以用下面3种方式的一种
 *  1. 放开volatile的注释,这个可以我们很容易理解，因为volatile关键字JVM会保证线程间变量的可见性
 *  2. 放开System.out.println注释，为什么这个可以呢？
 *      我们可以看println的源码，它里面用了synchronized，又因为在while循环里面，虚拟机有锁粗化的优化，锁会加到while循环外，所以能保证变量的可见性
 *  3. 放开子线程中m循环的注释，为什么这个可以呢？这个就更复杂一些。
 *      我们知道加了volatile关键字，JVM会保证内存的可见性，但没有volatile hotsport 虚拟机也会尽量保证内存的一致性，虚拟机会在cpu空闲时，同步主内存的值
 *      前面加了一个while循环，然后给cpu一个喘息时间，再进行i的while循环
 *      有点难理解哈，其实我们可以看到什么注释都没方法，直接运行的程序虽然卡死，但主线程打印出来的main i:的值不为0，为一个1000000左右的数，如果jvm真的不保证可见性
 *      主线程中的i值为0才对，说明主线程中获取i时，是同步了主内存值的，至于flag设为1为什么卡死，只能说while循环太多次后，jvm会优化，充分压榨CPU，让它一直处于繁忙状态，
 */
public class VolatileTest extends Thread{

    /*volatile*/ int flag = 0;
    int i = 0;
    public static void main(String[] args) throws InterruptedException {
        VolatileTest volatileTest = new VolatileTest();
        volatileTest.start();
        TimeUnit.MILLISECONDS.sleep(3);
        volatileTest.flag = 1;
        System.out.println("main i: " + volatileTest.i);
    }

    @Override
    public void run() {

        System.out.println("worker start, flag: " + flag);
//        int m=0;
//        while (m < 100_000_000) {
//            m++;
//        }
        while (flag == 0) {
            i++;
//                System.out.println();
        }
        System.out.println("worker i: " + i);
    }
}
