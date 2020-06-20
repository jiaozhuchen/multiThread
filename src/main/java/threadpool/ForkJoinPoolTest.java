package threadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * 测试ForkJoinPool的使用
 */
public class ForkJoinPoolTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //显然使用IntStream.parallel().sum()可以方便得到结果
        // 且parallel也是使用的ForkJoinPool，这是后话，我们本例就是测试ForkJoinTask的分解
        int[] numbers = IntStream.rangeClosed(0, 1_000_000).toArray();
        long begin = System.currentTimeMillis();
        ForkJoinTask<Integer> submit = forkJoinPool.submit(new SumTask(numbers, 0, numbers.length - 1));
        System.out.println("累加结果为：" + submit.get());
        System.out.println("运算耗时：" + (System.currentTimeMillis() - begin));
    }

    private static class SumTask extends RecursiveTask<Integer> {
        private int[] numbers;
        private int from;
        private int to;

        public SumTask(int[] numbers, int from, int to) {
            this.numbers = numbers;
            this.from = from;
            this.to = to;
        }

        @Override
        protected Integer compute() {
            if (to - from <= 2) {
                int total = 0;
                for (int i = from; i <= to; i++) {
                    total += numbers[i];
                }
                return total;
            } else {
                int middle = (from + to) / 2;
                SumTask taskLeft = new SumTask(numbers, from, middle);
                SumTask taskRight = new SumTask(numbers, middle + 1, to);
                taskLeft.fork();
                taskRight.fork();
                return taskRight.join() + taskLeft.join();
//                return taskLeft.join() + taskRight.join() ;
            }
        }
    }
}
