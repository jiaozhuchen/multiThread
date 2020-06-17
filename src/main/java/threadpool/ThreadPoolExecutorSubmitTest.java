package threadpool;

import java.util.concurrent.*;

import static java.lang.Thread.currentThread;

/**
 * 比较ThreadPoolExecutor execute方法和submit方法
 * 1. execute方法不能有返回值（只接收Runnable对象）， submit方法可以有返回值（接收Runnable或者Callable对象）
 * 2. execute方法遇到异常直接抛出，submit方法捕获了异常，如果不调用submit方法是不会抛出异常的
 *      如例子中所示，调用testSubmitWhenException  如果没有调用submit.get是没有异常抛出的
 */
public class ThreadPoolExecutorSubmitTest {

    public static void main(String[] args) throws InterruptedException {
        testExecuteWhenException();
//        testSubmitWhenException();
    }

    private static void testSubmitWhenException() throws InterruptedException {
        int corePoolSize = 1;
        int maximumPoolSize = 2;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 100L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>());
        for (int i = 0; i < maximumPoolSize; i++) {
            Future<String> submit = threadPoolExecutor.submit(new Worker(), "执行成功");
//            try {
//                String result = submit.get();
//                System.out.println(result);
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
        }
        TimeUnit.SECONDS.sleep(1L);
        threadPoolExecutor.shutdown();
    }


    private static void testExecuteWhenException() throws InterruptedException {
        int corePoolSize = 1;
        int maximumPoolSize = 2;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 100L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>());
        for (int i = 0; i < maximumPoolSize; i++) {
            threadPoolExecutor.execute(new Worker());
        }
        TimeUnit.SECONDS.sleep(1L);
        threadPoolExecutor.shutdown();
    }

    static class Worker implements Runnable {
        //构造一个除0异常，看看execute方法和submit方法的不同表现
        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(currentThread().getName() + " working");
            int a = 1;
            int b = 0;
            int c = a / b;
            System.out.println(c);
        }
    }
}
