package lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class TwinsLock implements Lock {

    static final class Sync extends AbstractQueuedSynchronizer {
        Sync(int count) {
            if(count <= 0) {
                throw new IllegalArgumentException("count must large than zero");
            }
            setState(count);
        }

        protected int tryAcquireShared(int arg) {
            while (true) {
                int current = getState();
                int newCount = current - arg;
                if(newCount < 0 || compareAndSetState(current, newCount)) {
                    return newCount;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            while (true) {
                int current = getState();
                int newCount = current + arg;
                if(compareAndSetState(current, newCount)) {
                    return true;
                }
            }
        }

        public Condition newCondition() {
            return new ConditionObject();
        }
    }

    private Sync sync = new Sync(2);

    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquireShared(1) >= 0;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
