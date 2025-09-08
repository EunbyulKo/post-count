package org.silverstar.postcount.support;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public final class ConcurrentTestTemplate {

    private ConcurrentTestTemplate() {}

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Exception;
    }

    public static List<Future<?>>  run(int threads, int poolSize, ThrowingRunnable task) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        CountDownLatch done = new CountDownLatch(threads);

        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            Future<?> future = executorService.submit(() -> {
                try {
                    task.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    done.countDown();
                }
            });
            futures.add(future);
        }
        done.await();
        executorService.shutdown();
        if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }

        return futures;
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
