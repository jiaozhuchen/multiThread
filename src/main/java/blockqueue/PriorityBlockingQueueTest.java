package blockqueue;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * 测试PriorityBlockingQueue优先级阻塞队列的使用
 *  1. 测试类中如果Person类没有实现Comparable接口，添加Person对象会报错
 *  2. 添加的元素对象会被排序
 *  3.无界队列并不是真的无界，最大元素数 Integer.MAX_VALUE - 8(因为使用数组的原因，底层控制了最大长度)（可以看到比LinkedBlockingQueue最大量还小一些）
 *  4. 内部头肩的是一个小顶堆
 */
public class PriorityBlockingQueueTest {

    public static void main(String[] args) {
        testPriorityBlockingQueueSorted();
    }

    //Person类必须实现Comparable 接口
    //Person类插入后是有序的
    private static void testPriorityBlockingQueueSorted() {
        PriorityBlockingQueue<Person> pbq = new PriorityBlockingQueue();
        //PriorityBlockingQueue添加的元素必须实现了Comparable接口不然会抛出异常
        pbq.offer(new Person("dd", 10));
        pbq.offer(new Person("ee", 30));
        pbq.offer(new Person("ff", 40));
        pbq.offer(new Person("gg", 20));
        while (pbq.size() > 0) {
            System.out.println(pbq.poll());
        }
    }
}
