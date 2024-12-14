package com.hanhan.javautil.threadpool;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GwallThreadPool extends ThreadPoolExecutor {
    private final String poolName;

    public GwallThreadPool(String poolName, int corePoolSize, int maximumPoolSize, int queueSize) {
        super(corePoolSize, maximumPoolSize, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize), new NamedThreadFactory(poolName), new BlockRejectHandler());
        this.poolName = poolName;
    }


    @Override
    public void execute(Runnable command) {
        super.execute(new CopyContextRunnable(command));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(new CopyContextRunnable(task));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(new CopyContextCallable<>(task));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return super.submit(new CopyContextRunnable(task), result);
    }

    private static class CopyContextCallable<V> implements Callable<V> {
        private final Map<String, Object> context;
        private final Callable<V> job;

        public CopyContextCallable(Callable<V> job) {
            this.context = ThreadContext.copy();
            this.job = job;
        }

        @Override
        public V call() throws Exception {
            ThreadContext.restore(context);
            return job.call();
        }
    }

    private static class CopyContextRunnable implements Runnable {
        private final Map<String, Object> context;
        private final Runnable job;

        public CopyContextRunnable(Runnable job) {
            this.context = ThreadContext.copy();
            this.job = job;
        }

        @Override
        public void run() {
            ThreadContext.restore(context);
            job.run();
        }
    }

    private static class NamedThreadFactory implements ThreadFactory {
        /**
         * 线程池计数器
         */
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        /**
         * 线程数计数器
         */
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        /**
         * 线程名前缀
         */
        private final String namePrefix;

        NamedThreadFactory(String poolName) {
            this.namePrefix = poolNumber.incrementAndGet() + "-" + poolName + "-";
        }

        public Thread newThread(Runnable r) {
            return new Thread(r, this.namePrefix + this.threadNumber.getAndIncrement());
        }
    }

    private static class BlockRejectHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (!executor.isShutdown()) {
                try {
                    executor.getQueue().put(r);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    public String getPoolName() {
        return poolName;
    }
}
