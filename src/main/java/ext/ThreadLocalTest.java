package ext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试ThreadLocal的使用
 *  线程间数据不会串
 */
public class ThreadLocalTest {

    public static void main(String[] args) {
        testThreadLocal();
    }

    public static void testThreadLocal() {
        ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
        System.out.println(Thread.currentThread().getName() + ".set: " + -1);
        threadLocal.set(-1);

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i=1; i< 5; i++) {
            final Integer setValue = i;
            executorService.submit(() -> {
                System.out.println(Thread.currentThread().getName() + ".set: " + setValue);
                threadLocal.set(setValue);
                System.out.println(Thread.currentThread().getName() + ".get: " + threadLocal.get());
                threadLocal.remove();
            });
        }
        System.out.println(Thread.currentThread().getName() + ".get: " +  threadLocal.get());
        threadLocal.remove();
    }
}
