package ext;

import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

/**
 * 测试软引用的使用
 *  注意：将运行的JVM最大内存参数调到20M -Xmx20m
 *
 *  软引用垃圾回收时，不回收，但是如果垃圾回收后内存还是不够，就会回收软引用内存（否则就内存溢出了嘛）  适用于缓存
 */
public class SoftReferenceTest {

    public static void main(String[] args) throws InterruptedException {
        //M对象被回收时，会放大QUEUE中
        SoftReference<byte []> softReference = new SoftReference<>(new byte[1024 * 1024 * 10]);
        System.out.println(softReference.get());
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        //可以看到这里仍然可以获得
        System.out.println(softReference.get());

        byte [] a = new byte[1024 * 1024 * 12];
        //有一个强引用分配了，堆放不下，这时进行垃圾回收，回收后，如果还不够，会把软引用回收
        System.out.println(softReference.get());
    }
}
