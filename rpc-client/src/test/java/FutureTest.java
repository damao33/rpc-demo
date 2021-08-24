import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author zhanggeng
 * @date 2021/8/19 16:16
 */
public class FutureTest {
    @Test
    public void test() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = (CompletableFuture<String>) getFuture();
        System.out.println(future.get());
    }
    private Object getFuture() throws InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        Thread.sleep(100);
        System.out.println("wake");
        return future;
    }
}
