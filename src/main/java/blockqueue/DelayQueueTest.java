package blockqueue;

import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * 测试DelayQueue的使用
 *  1. DelayQueue是一个延迟队列，可以定义延迟多久后元素可以取出
 *  2. DelayQueue中的元素必须是Delayed的子类
 *  3. DelayQueue 元素是有序的，根据重写的compareTo方法排序
 */
public class DelayQueueTest {

    public static void main(String[] args) throws InterruptedException {
        DelayQueue delayQueue = new DelayQueue();
        for(int i=0; i<10; i++) {
            TimeUnit.MILLISECONDS.sleep(1);
            delayQueue.put(new DelayItem(new Random().nextInt(100)));
        }

        while (delayQueue.size() > 0) {
            System.out.println(delayQueue.take());
        }
    }
}
