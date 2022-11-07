package Agent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomThreadPoolExecutor extends ThreadPoolExecutor {


    private boolean isPaused;
    private ReentrantLock pauseLock = new ReentrantLock();
    private Condition unpaused = pauseLock.newCondition();
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        try {
            while (isPaused) {
                unpaused.await();
            }
        } catch (InterruptedException ie) {
            t.interrupt();
        } finally {
            pauseLock.unlock();
        }
    }

    public void pause() {
        pauseLock.lock();
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    public void resume() {
        pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }
    public void shutDownThreadPool() {
        this.shutdownNow();
        //this.resume();
    }
    private static final String THREAD_NAME_PATTERN = "%s%d";
    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                    long keepAliveTime, TimeUnit unit,
                                    BlockingQueue<Runnable> workQueue, AbortPolicy abortPolicy) {
        //super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                new ThreadFactory() {

                    private final AtomicInteger counter = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        final String threadName = String.format(THREAD_NAME_PATTERN, "Agent ", counter.incrementAndGet());
                        return new Thread(r, threadName);
                    }
                });

    }
}
