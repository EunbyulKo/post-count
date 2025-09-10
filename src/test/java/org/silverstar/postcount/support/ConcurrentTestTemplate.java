package org.silverstar.postcount.support;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public final class ConcurrentTestTemplate {

    private ConcurrentTestTemplate() {}

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Exception;
    }

    public static List<Future<?>> run(int threads, int poolSize, ThrowingRunnable task) throws InterruptedException {
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

    public static void runTwoThreads(PlatformTransactionManager transactionManager, ThrowingRunnable task1, ThrowingRunnable task2) throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        Callable<Void> aToB = () -> {
            var tx = new TransactionTemplate(transactionManager);
            return tx.execute(status -> {
                try {
                    ready.countDown();
                    start.await();
                    task1.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
        };
        Callable<Void> bToA = () -> {
            var tx = new TransactionTemplate(transactionManager);
            return tx.execute(status -> {
                try {
                    ready.countDown();
                    start.await();
                    task2.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
        };

        Future<Void> f1 = executorService.submit(aToB);
        Future<Void> f2 = executorService.submit(bToA);

        ready.await(3, TimeUnit.SECONDS);
        start.countDown();

        // 데드락/락 타임아웃 없이 완료되어야 함
        f1.get(10, TimeUnit.SECONDS);
        f2.get(10, TimeUnit.SECONDS);

        executorService.shutdownNow();

    }

}
