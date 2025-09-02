package org.silverstar.postcount.support;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class ConcurrentTestTemplate {

    private ConcurrentTestTemplate() {}

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Exception;
    }

    public static void run(int threads, int poolSize, ThrowingRunnable task) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        CountDownLatch done = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            executorService.submit(() -> {
                try {
                    task.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    done.countDown();
                }
            });
        }
        done.await();
        executorService.shutdown();
        if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }

    public static void runBurst(int threads, int poolSize, ThrowingRunnable task) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done  = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            executorService.submit(() -> {
                try {
                    start.await();
                    task.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    done.countDown();
                }
            });
        }
        start.countDown();
        done.await();
        executorService.shutdown();
        if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }

}
