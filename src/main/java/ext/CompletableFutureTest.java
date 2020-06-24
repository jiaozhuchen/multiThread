package ext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture测试
 *   CompletableFuture实现了Future和 CompletionStage，所以既有Future的get/ isDown等功能，又可以实现异步的处理
 *      包括thenApply，thenAccept等异步回调方法，thenCompose，thenCombine等组合CompletableFuture方法
 */
public class CompletableFutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        testUseAsyncMethodAndDealException();
//        testCombineTwoFutures();
    }

    /**
     * 测试组合两个Future
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void testCombineTwoFutures() throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> priceFuture = CompletableFuture.supplyAsync(() -> {
            sleepOneSecond();
            return 30;
        });

        CompletableFuture<Double> weightFuture = CompletableFuture.supplyAsync(() -> {
            sleepOneSecond();
            return 23.5;
        });

        CompletableFuture<Double> orderFuture = weightFuture.thenCombine(priceFuture, (weight, price) -> {
            return weight * price;
        });

        System.out.println("订单金额为：" + orderFuture.get());
    }

    private static void sleepOneSecond() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 测试CompletableFuture异步回调方法的使用及异常处理
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void testUseAsyncMethodAndDealException() throws InterruptedException, ExecutionException {
        CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
            sleepOneSecond();
            return "CC";
        }).thenApply(name -> {
            return "Hello " + name;
        }).thenApply(greeting -> {
            return greeting + ", Welcome to the World";
        }).exceptionally(ex -> {
            System.out.println("exception: " + ex.getMessage());
            return "exception!";
        });
        System.out.println(result.get());
    }
}
