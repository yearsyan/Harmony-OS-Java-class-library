package ohos.eventhandler;

import java.lang.Thread;
import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.interwork.eventhandler.ICourierEx;

public final class EventRunner {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, ICourierEx.DOMAIN, "JavaEventRunner");
    private static ThreadLocal<WeakReference<EventRunner>> currentEventRunner = new ThreadLocal<>();
    private static WeakReference<EventRunner> mainRunner;
    private boolean deposit = true;
    EventInnerRunner eventInnerRunner;
    EventQueue eventQueue;
    private Logger logger;
    private AtomicBoolean running = new AtomicBoolean(false);

    /* access modifiers changed from: package-private */
    public static class EventInnerRunner implements Runnable {
        private static final int STATE_FAILED = -1;
        private static final int STATE_INIT = 0;
        private static final int STATE_RUNNING = 1;
        boolean deposit;
        EventQueue eventQueue;
        final Object lock = new Object();
        WeakReference<EventRunner> owner = null;
        int state = 0;
        Thread thread;
        long threadId = -1;

        /* access modifiers changed from: package-private */
        public void wake(long j) {
        }

        EventInnerRunner(EventRunner eventRunner, boolean z) {
            this.owner = new WeakReference<>(eventRunner);
            this.deposit = z;
            this.eventQueue = new EventQueue();
            this.eventQueue.prepareSubEventQueue();
        }

        public void run() {
            String name = Thread.currentThread().getName();
            HiLog.info(EventRunner.LOG_LABEL, "Thread %{public}s start running", name);
            if (!startToRun()) {
                HiLog.error(EventRunner.LOG_LABEL, "Failed to start event runner", new Object[0]);
                notifyRunning(false);
            }
            HiLog.info(EventRunner.LOG_LABEL, "Thread %{public}s stop running", name);
        }

        /* access modifiers changed from: package-private */
        public void setOwner(WeakReference<EventRunner> weakReference) {
            this.owner = weakReference;
        }

        /* access modifiers changed from: package-private */
        public boolean startToRun() {
            this.eventQueue.prepare();
            this.thread = Thread.currentThread();
            this.threadId = this.thread.getId();
            WeakReference<EventRunner> weakReference = this.owner;
            if (weakReference == null || weakReference.get() == null) {
                notifyRunning(false);
                return false;
            } else if (EventRunner.currentEventRunner == null) {
                notifyRunning(false);
                return false;
            } else {
                notifyRunning(true);
                WeakReference weakReference2 = (WeakReference) EventRunner.currentEventRunner.get();
                EventRunner.currentEventRunner.set(this.owner);
                Optional<InnerEvent> event = this.eventQueue.getEvent();
                while (event.isPresent()) {
                    if (event.get().owner != null) {
                        EventHandler eventHandler = event.get().owner.get();
                        if (eventHandler != null) {
                            eventHandler.distributeEvent(event.get());
                        }
                        event.get().drop();
                    }
                    event = this.eventQueue.getEvent();
                }
                EventRunner.currentEventRunner.set(weakReference2);
                return true;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean stop() {
            this.eventQueue.finish();
            this.thread = null;
            this.threadId = -1;
            return true;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
            ohos.hiviewdfx.HiLog.warn(ohos.eventhandler.EventRunner.LOG_LABEL, "Interrupted while waiting event runner running", new java.lang.Object[0]);
         */
        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0010 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean waitUntilStarted() {
            /*
                r5 = this;
                java.lang.Object r0 = r5.lock
                monitor-enter(r0)
            L_0x0003:
                r1 = 0
                int r2 = r5.state     // Catch:{ InterruptedException -> 0x0010 }
                if (r2 != 0) goto L_0x001b
                java.lang.Object r2 = r5.lock     // Catch:{ InterruptedException -> 0x0010 }
                r2.wait()     // Catch:{ InterruptedException -> 0x0010 }
                goto L_0x0003
            L_0x000e:
                r5 = move-exception
                goto L_0x003a
            L_0x0010:
                ohos.hiviewdfx.HiLogLabel r2 = ohos.eventhandler.EventRunner.access$000()     // Catch:{ all -> 0x000e }
                java.lang.String r3 = "Interrupted while waiting event runner running"
                java.lang.Object[] r4 = new java.lang.Object[r1]     // Catch:{ all -> 0x000e }
                ohos.hiviewdfx.HiLog.warn(r2, r3, r4)     // Catch:{ all -> 0x000e }
            L_0x001b:
                int r5 = r5.state     // Catch:{ all -> 0x000e }
                r2 = 1
                if (r5 == r2) goto L_0x002d
                ohos.hiviewdfx.HiLogLabel r5 = ohos.eventhandler.EventRunner.access$000()     // Catch:{ all -> 0x000e }
                java.lang.String r2 = "Event runner is notified not running"
                java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ all -> 0x000e }
                ohos.hiviewdfx.HiLog.warn(r5, r2, r3)     // Catch:{ all -> 0x000e }
                monitor-exit(r0)     // Catch:{ all -> 0x000e }
                return r1
            L_0x002d:
                ohos.hiviewdfx.HiLogLabel r5 = ohos.eventhandler.EventRunner.access$000()     // Catch:{ all -> 0x000e }
                java.lang.String r3 = "Event runner is notified running"
                java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x000e }
                ohos.hiviewdfx.HiLog.info(r5, r3, r1)     // Catch:{ all -> 0x000e }
                monitor-exit(r0)     // Catch:{ all -> 0x000e }
                return r2
            L_0x003a:
                monitor-exit(r0)     // Catch:{ all -> 0x000e }
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.eventhandler.EventRunner.EventInnerRunner.waitUntilStarted():boolean");
        }

        /* access modifiers changed from: package-private */
        public void notifyRunning(boolean z) {
            synchronized (this.lock) {
                this.state = z ? 1 : -1;
                this.lock.notifyAll();
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isCurrentRunnerThread() {
            return Thread.currentThread() == this.thread;
        }

        /* access modifiers changed from: package-private */
        public long getThreadId() {
            return this.threadId;
        }
    }

    /* access modifiers changed from: private */
    public static class StopCallback implements Runnable {
        private final EventInnerRunner eventInnerRunner;

        StopCallback(EventInnerRunner eventInnerRunner2) {
            this.eventInnerRunner = eventInnerRunner2;
        }

        public void run() {
            this.eventInnerRunner.stop();
        }
    }

    /* access modifiers changed from: private */
    public static class ExceptionHandler implements Thread.UncaughtExceptionHandler {
        private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

        ExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler2) {
            this.uncaughtExceptionHandler = uncaughtExceptionHandler2;
        }

        public void uncaughtException(Thread thread, Throwable th) {
            String str = "<Unknown>";
            String name = thread != null ? thread.getName() : str;
            if (th != null) {
                str = th.getMessage();
            }
            HiLog.error(EventRunner.LOG_LABEL, "Uncaught exception, %{public}s: %{public}s", name, str);
            Thread.UncaughtExceptionHandler uncaughtExceptionHandler2 = this.uncaughtExceptionHandler;
            if (uncaughtExceptionHandler2 != null && thread != null && th != null) {
                uncaughtExceptionHandler2.uncaughtException(thread, th);
            }
        }
    }

    EventRunner(boolean z) {
        this.deposit = z;
    }

    private EventRunner(EventInnerRunner eventInnerRunner2) {
        this.eventInnerRunner = eventInnerRunner2;
        if (this.eventInnerRunner.deposit) {
            EventHandlerUtils.trackObject(this, new StopCallback(this.eventInnerRunner));
        }
    }

    public static EventRunner current() {
        ThreadLocal<WeakReference<EventRunner>> threadLocal = currentEventRunner;
        if (threadLocal != null && threadLocal.get() != null) {
            return currentEventRunner.get().get();
        }
        if (!PlatformEventRunner.checkCurrent()) {
            return null;
        }
        Optional<EventRunner> eventRunner = PlatformEventRunner.getEventRunner();
        if (!eventRunner.isPresent()) {
            return null;
        }
        currentEventRunner.set(new WeakReference<>(eventRunner.get()));
        return eventRunner.get();
    }

    private static EventRunner create(boolean z, String str) {
        EventRunner eventRunner = new EventRunner(z);
        EventInnerRunner eventInnerRunner2 = new EventInnerRunner(eventRunner, z);
        eventRunner.setEventInnerRunner(eventInnerRunner2);
        eventRunner.eventQueue = eventInnerRunner2.eventQueue;
        if (z) {
            if (str == null || "".equals(str)) {
                str = String.format(Locale.ENGLISH, "JavaEventRunner#%d", Integer.valueOf(ID_GENERATOR.getAndIncrement()));
            }
            Thread thread = new Thread(eventInnerRunner2, str);
            thread.setUncaughtExceptionHandler(new ExceptionHandler(thread.getUncaughtExceptionHandler()));
            thread.start();
            eventInnerRunner2.waitUntilStarted();
        }
        return eventRunner;
    }

    public static EventRunner create() {
        return create(true);
    }

    public static EventRunner create(boolean z) {
        return create(z, null);
    }

    public static EventRunner create(String str) {
        return create(true, str);
    }

    /* access modifiers changed from: package-private */
    public void setEventInnerRunner(EventInnerRunner eventInnerRunner2) {
        this.eventInnerRunner = eventInnerRunner2;
        if (eventInnerRunner2.deposit) {
            EventHandlerUtils.trackObject(this, new StopCallback(eventInnerRunner2));
        }
    }

    public static void setMainEventRunner() {
        EventRunner current = current();
        synchronized (EventRunner.class) {
            if (mainRunner == null) {
                mainRunner = new WeakReference<>(current);
            } else {
                throw new IllegalStateException("The main runner has been set up.");
            }
        }
    }

    public void setLogger(Logger logger2) {
        this.logger = logger2;
    }

    public void dump(Dumper dumper) {
        if (dumper == null) {
            HiLog.info(LOG_LABEL, "parameter dumper is null, please ensure method input is correct!", new Object[0]);
            return;
        }
        String tag = dumper.getTag() == null ? "" : dumper.getTag();
        dumper.dump(tag + " " + this + " @ " + System.nanoTime());
        EventQueue eventQueue2 = this.eventQueue;
        if (eventQueue2 == null) {
            dumper.dump(tag + " runner uninitialized");
            return;
        }
        eventQueue2.dump(dumper);
    }

    public static EventRunner getMainEventRunner() {
        EventRunner eventRunner;
        synchronized (EventRunner.class) {
            eventRunner = mainRunner.get();
        }
        return eventRunner;
    }

    public long getThreadId() {
        EventInnerRunner eventInnerRunner2 = this.eventInnerRunner;
        if (eventInnerRunner2 == null) {
            return -1;
        }
        return eventInnerRunner2.getThreadId();
    }

    public boolean isCurrentRunnerThread() {
        EventInnerRunner eventInnerRunner2 = this.eventInnerRunner;
        if (eventInnerRunner2 == null) {
            return false;
        }
        return eventInnerRunner2.isCurrentRunnerThread();
    }

    public EventQueue getEventQueue() {
        return this.eventQueue;
    }

    public static EventQueue getCurrentEventQueue() {
        EventRunner eventRunner;
        ThreadLocal<WeakReference<EventRunner>> threadLocal = currentEventRunner;
        WeakReference<EventRunner> weakReference = threadLocal == null ? null : threadLocal.get();
        if (weakReference == null || (eventRunner = weakReference.get()) == null) {
            return null;
        }
        return eventRunner.eventQueue;
    }

    public boolean run() throws IllegalStateException {
        if (this.eventInnerRunner.deposit) {
            HiLog.warn(LOG_LABEL, "Failed to call 'run', if it is deposited", new Object[0]);
            throw new IllegalStateException("Failed to call 'run', if it is deposited");
        } else if (this.running.getAndSet(true)) {
            HiLog.warn(LOG_LABEL, "Failed to call 'run', already running", new Object[0]);
            return false;
        } else if (!this.eventInnerRunner.startToRun()) {
            HiLog.warn(LOG_LABEL, "Failed to call 'run', startToRun failed.", new Object[0]);
            return false;
        } else {
            this.running.lazySet(false);
            return true;
        }
    }

    public boolean stop() throws IllegalStateException {
        if (!this.eventInnerRunner.deposit) {
            return this.eventInnerRunner.stop();
        }
        HiLog.warn(LOG_LABEL, "Failed to call 'stop', if it is deposited", new Object[0]);
        throw new IllegalStateException("Failed to call 'stop', if it is deposited");
    }
}
