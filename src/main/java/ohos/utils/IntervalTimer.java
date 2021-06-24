package ohos.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class IntervalTimer {
    private final long duration;
    private final long interval;
    private volatile long remain;
    private volatile ScheduledExecutorService timer;

    public abstract void onFinish();

    public abstract void onInterval(long j);

    public IntervalTimer(long j, long j2) {
        this.duration = j;
        this.interval = j2;
    }

    public final void schedule() {
        long j = this.interval;
        if (j > 0) {
            long j2 = this.duration;
            if (j <= j2) {
                this.remain = j2;
                startTimer(j, j, j2);
                return;
            }
            throw new IllegalArgumentException("interval cannot be larger than duration");
        }
        throw new IllegalArgumentException("interval cannot be less than or equal to 0");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void startTimer(long j, long j2, long j3) {
        if (this.timer != null) {
            this.timer.shutdownNow();
        }
        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.timer.scheduleAtFixedRate(new IntervalTask(j2), j, j2, TimeUnit.MILLISECONDS);
        this.timer.schedule(new FinishTask(), j3, TimeUnit.MILLISECONDS);
    }

    public final void cancel() {
        if (this.timer != null) {
            this.timer.shutdownNow();
            this.timer = null;
        }
    }

    /* access modifiers changed from: private */
    public class IntervalTask implements Runnable {
        private static final int FAULT_TOLERANT_INTERVAL = 10;
        private final Object lock;
        private volatile long taskInterval;

        private IntervalTask(long j) {
            this.lock = new Object();
            this.taskInterval = j;
        }

        public void run() {
            synchronized (this.lock) {
                IntervalTimer.this.remain -= this.taskInterval;
                long currentTimeMillis = System.currentTimeMillis();
                IntervalTimer.this.onInterval(IntervalTimer.this.remain);
                if (this.taskInterval >= 10) {
                    long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                    if (currentTimeMillis2 > this.taskInterval) {
                        long j = this.taskInterval - currentTimeMillis2;
                        while (j < 0) {
                            j += this.taskInterval;
                        }
                        long j2 = IntervalTimer.this.remain - currentTimeMillis2;
                        if (j2 > 0) {
                            this.taskInterval = ((currentTimeMillis2 / IntervalTimer.this.interval) + 1) * IntervalTimer.this.interval;
                            IntervalTimer.this.startTimer(j, this.taskInterval, j2);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public class FinishTask implements Runnable {
        private FinishTask() {
        }

        public void run() {
            if (IntervalTimer.this.timer != null) {
                IntervalTimer.this.timer.shutdown();
                IntervalTimer.this.timer = null;
            }
            IntervalTimer.this.onFinish();
        }
    }
}
