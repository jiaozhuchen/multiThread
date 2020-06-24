package ext;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 测试虚引用的使用
 *  注意：将运行的JVM最大内存参数调到20M -Xmx20m
 *
 *  虚引用不管有没有进行垃圾回收，都get不到，一般被用于管理直接内存，在垃圾回收时，会放到QUEUE中，起到通知的作用， JVM监听到QUEUE中有对象，进行一些特殊处理，回收直接内存
 */
public class PhantomReferenceTest {
    private static final List<Object> LIST = new LinkedList<>();
    private static final ReferenceQueue<Person> QUEUE = new ReferenceQueue<>();

    public static void main(String[] args) {
        //M对象被回收时，会放大QUEUE中
        PhantomReference<Person> phantomReference = new PhantomReference<>(new Person(), QUEUE);
        //可以看到这里get不到
        System.out.println(phantomReference.get());

        new Thread(() -> {
            while(true) {
                LIST.add(new byte[1024 * 1024]);
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(phantomReference.get());
            }
        }).start();

        new Thread(() -> {
            while(true) {
                Reference<? extends Person> poll = QUEUE.poll();
                if(poll != null) {
                    System.out.println("虚引用对象被垃圾回收了： " + poll);
                }

            }
        }).start();
    }
}
