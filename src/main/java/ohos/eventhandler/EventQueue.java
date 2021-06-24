package ohos.eventhandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.PriorityBlockingQueue;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.interwork.eventhandler.ICourierEx;

public final class EventQueue {
    private static final int DEFAULT_MAX_HANDLED_COUNT = 5;
    private static final int INITIAL_EVENT_QUEUE_NUM = 10;
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, ICourierEx.DOMAIN, "JavaEventQueue");
    private static final int SUB_EVENT_QUEUE_NUM = (EventHandler.Priority.IDLE.ordinal() + 1);
    private PriorityBlockingQueue<InnerEvent> idleEvents = new PriorityBlockingQueue<>(10, new Comparator<InnerEvent>() {
        /* class ohos.eventhandler.EventQueue.AnonymousClass1 */

        public int compare(InnerEvent innerEvent, InnerEvent innerEvent2) {
            if (innerEvent == null || innerEvent2 == null || innerEvent.handleTime == innerEvent2.handleTime) {
                return 0;
            }
            return innerEvent.handleTime > innerEvent2.handleTime ? 1 : -1;
        }
    });
    private long idleTimeStamp = EventHandlerUtils.fromNanoSecondsToMills(System.nanoTime());
    private boolean inIdle = true;
    boolean inPlatformRunner = false;
    private volatile boolean isFinished = true;
    final Object lock = new Object();
    Optional<InnerEvent> nextInnerEvent = Optional.empty();
    WeakReference<EventRunner.EventInnerRunner> platformRunner = null;
    private List<SubEventQueue> subEventQueues = new ArrayList(SUB_EVENT_QUEUE_NUM);
    private long wakeUpTime = Long.MAX_VALUE;

    /* access modifiers changed from: private */
    public static class SubEventQueue {
        int handledCount;
        int maxHandledCount;
        PriorityBlockingQueue<InnerEvent> queue;

        private SubEventQueue() {
            this.queue = new PriorityBlockingQueue<>(10, new Comparator<InnerEvent>() {
                /* class ohos.eventhandler.EventQueue.SubEventQueue.AnonymousClass1 */

                public int compare(InnerEvent innerEvent, InnerEvent innerEvent2) {
                    if (innerEvent == null || innerEvent2 == null) {
                        return 0;
                    }
                    if ((innerEvent.handleTime != innerEvent2.handleTime || innerEvent.currentTime < innerEvent2.currentTime) && innerEvent.handleTime <= innerEvent2.handleTime) {
                        return -1;
                    }
                    return 1;
                }
            });
            this.handledCount = 0;
            this.maxHandledCount = 5;
        }
    }

    /* access modifiers changed from: package-private */
    public void prepare() {
        synchronized (this.lock) {
            this.isFinished = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void finish() {
        synchronized (this.lock) {
            this.isFinished = true;
            this.lock.notifyAll();
        }
    }

    /* access modifiers changed from: package-private */
    public void insert(InnerEvent innerEvent, EventHandler.Priority priority) {
        boolean z = false;
        if (innerEvent == null) {
            HiLog.error(LOG_LABEL, "Could not insert an invalid event", new Object[0]);
        } else if (this.subEventQueues.size() == SUB_EVENT_QUEUE_NUM) {
            synchronized (this.lock) {
                int i = AnonymousClass9.$SwitchMap$ohos$eventhandler$EventHandler$Priority[priority.ordinal()];
                if (i == 1 || i == 2 || i == 3) {
                    if (innerEvent.handleTime < this.wakeUpTime) {
                        z = true;
                    }
                    this.subEventQueues.get(priority.ordinal()).queue.add(innerEvent);
                } else if (i == 4) {
                    this.subEventQueues.get(priority.ordinal()).queue.add(innerEvent);
                }
                if (!this.inPlatformRunner) {
                    if (z) {
                        this.lock.notifyAll();
                    }
                    return;
                }
                if (this.platformRunner != null) {
                    if (this.platformRunner.get() != null) {
                        if (z) {
                            this.platformRunner.get().wake(innerEvent.handleTime - EventHandlerUtils.fromNanoSecondsToMills(System.nanoTime()));
                            innerEvent.isPosted = true;
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.eventhandler.EventQueue$9  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass9 {
        static final /* synthetic */ int[] $SwitchMap$ohos$eventhandler$EventHandler$Priority = new int[EventHandler.Priority.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                ohos.eventhandler.EventHandler$Priority[] r0 = ohos.eventhandler.EventHandler.Priority.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.eventhandler.EventQueue.AnonymousClass9.$SwitchMap$ohos$eventhandler$EventHandler$Priority = r0
                int[] r0 = ohos.eventhandler.EventQueue.AnonymousClass9.$SwitchMap$ohos$eventhandler$EventHandler$Priority     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.eventhandler.EventHandler$Priority r1 = ohos.eventhandler.EventHandler.Priority.IMMEDIATE     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.eventhandler.EventQueue.AnonymousClass9.$SwitchMap$ohos$eventhandler$EventHandler$Priority     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.eventhandler.EventHandler$Priority r1 = ohos.eventhandler.EventHandler.Priority.HIGH     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.eventhandler.EventQueue.AnonymousClass9.$SwitchMap$ohos$eventhandler$EventHandler$Priority     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.eventhandler.EventHandler$Priority r1 = ohos.eventhandler.EventHandler.Priority.LOW     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.eventhandler.EventQueue.AnonymousClass9.$SwitchMap$ohos$eventhandler$EventHandler$Priority     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.eventhandler.EventHandler$Priority r1 = ohos.eventhandler.EventHandler.Priority.IDLE     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.eventhandler.EventQueue.AnonymousClass9.<clinit>():void");
        }
    }

    /* access modifiers changed from: package-private */
    public void dump(Dumper dumper) {
        synchronized (this.lock) {
            if (dumper == null) {
                HiLog.info(LOG_LABEL, "parameter dumper is null, please ensure method input is correct!", new Object[0]);
                return;
            }
            String tag = dumper.getTag() == null ? "" : dumper.getTag();
            int i = 0;
            for (int i2 = 0; i2 < SUB_EVENT_QUEUE_NUM; i2++) {
                Iterator<InnerEvent> it = this.subEventQueues.get(i2).queue.iterator();
                while (it.hasNext()) {
                    dumper.dump(tag + " Message " + i + " : " + it.next().toString());
                    i++;
                }
            }
            dumper.dump(tag + " " + this + " @ " + System.nanoTime() + ", (total InnerEvents: " + i + ", inPlatformRunner = " + this.inPlatformRunner + ", isFinished = " + this.isFinished + ")");
        }
    }

    public boolean isQueueEmpty() {
        List<SubEventQueue> list = this.subEventQueues;
        if (list == null || list.size() != SUB_EVENT_QUEUE_NUM) {
            return false;
        }
        for (int i = 0; i < SUB_EVENT_QUEUE_NUM; i++) {
            if (!(this.subEventQueues.get(i) == null || this.subEventQueues.get(i).queue == null || this.subEventQueues.get(i).queue.isEmpty())) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void remove(final EventHandler eventHandler) {
        if (eventHandler == null) {
            HiLog.error(LOG_LABEL, "Empty owner", new Object[0]);
        } else {
            remove(new EventHandler.IEventFilter() {
                /* class ohos.eventhandler.EventQueue.AnonymousClass2 */

                @Override // ohos.eventhandler.EventHandler.IEventFilter
                public boolean match(InnerEvent innerEvent) {
                    return (innerEvent == null || innerEvent.owner == null || innerEvent.owner.get() != eventHandler) ? false : true;
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public void remove(final EventHandler eventHandler, final int i) {
        if (eventHandler == null) {
            HiLog.error(LOG_LABEL, "Empty owner", new Object[0]);
        } else {
            remove(new EventHandler.IEventFilter() {
                /* class ohos.eventhandler.EventQueue.AnonymousClass3 */

                @Override // ohos.eventhandler.EventHandler.IEventFilter
                public boolean match(InnerEvent innerEvent) {
                    return innerEvent != null && innerEvent.eventId == i && innerEvent.owner != null && innerEvent.owner.get() == eventHandler;
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public void remove(final EventHandler eventHandler, final int i, final long j) {
        if (eventHandler == null) {
            HiLog.error(LOG_LABEL, "Empty owner", new Object[0]);
        } else {
            remove(new EventHandler.IEventFilter() {
                /* class ohos.eventhandler.EventQueue.AnonymousClass4 */

                @Override // ohos.eventhandler.EventHandler.IEventFilter
                public boolean match(InnerEvent innerEvent) {
                    return innerEvent != null && innerEvent.eventId == i && innerEvent.param == j && innerEvent.owner != null && innerEvent.owner.get() == eventHandler;
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public void remove(final EventHandler eventHandler, final int i, final Object obj) {
        if (eventHandler == null) {
            HiLog.error(LOG_LABEL, "Empty owner", new Object[0]);
        } else {
            remove(new EventHandler.IEventFilter() {
                /* class ohos.eventhandler.EventQueue.AnonymousClass5 */

                @Override // ohos.eventhandler.EventHandler.IEventFilter
                public boolean match(InnerEvent innerEvent) {
                    return innerEvent != null && innerEvent.eventId == i && innerEvent.object == obj && innerEvent.owner != null && innerEvent.owner.get() == eventHandler;
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public void remove(final EventHandler eventHandler, final int i, final long j, final Object obj) {
        if (eventHandler == null) {
            HiLog.error(LOG_LABEL, "Empty owner", new Object[0]);
        } else {
            remove(new EventHandler.IEventFilter() {
                /* class ohos.eventhandler.EventQueue.AnonymousClass6 */

                @Override // ohos.eventhandler.EventHandler.IEventFilter
                public boolean match(InnerEvent innerEvent) {
                    return innerEvent != null && innerEvent.eventId == i && innerEvent.param == j && innerEvent.object == obj && innerEvent.owner != null && innerEvent.owner.get() == eventHandler;
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public void remove(final EventHandler eventHandler, final Runnable runnable) {
        if (eventHandler == null) {
            HiLog.error(LOG_LABEL, "Empty owner", new Object[0]);
        } else {
            remove(new EventHandler.IEventFilter() {
                /* class ohos.eventhandler.EventQueue.AnonymousClass7 */

                @Override // ohos.eventhandler.EventHandler.IEventFilter
                public boolean match(InnerEvent innerEvent) {
                    return innerEvent != null && innerEvent.task == runnable && innerEvent.owner != null && innerEvent.owner.get() == eventHandler;
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public void remove(final EventHandler eventHandler, final Runnable runnable, final Object obj) {
        if (eventHandler == null) {
            HiLog.error(LOG_LABEL, "Empty owner", new Object[0]);
        } else {
            remove(new EventHandler.IEventFilter() {
                /* class ohos.eventhandler.EventQueue.AnonymousClass8 */

                @Override // ohos.eventhandler.EventHandler.IEventFilter
                public boolean match(InnerEvent innerEvent) {
                    return innerEvent != null && innerEvent.task == runnable && innerEvent.object == obj && innerEvent.owner != null && innerEvent.owner.get() == eventHandler;
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0055  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0062 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x001a A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasInnerEvent(ohos.eventhandler.EventHandler r10, long r11, java.lang.Object r13, java.lang.Runnable r14) {
        /*
        // Method dump skipped, instructions count: 108
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.eventhandler.EventQueue.hasInnerEvent(ohos.eventhandler.EventHandler, long, java.lang.Object, java.lang.Runnable):boolean");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x004d A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x001a A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasInnerEvent(ohos.eventhandler.EventHandler r9, int r10, java.lang.Object r11) {
        /*
            r8 = this;
            r0 = 0
            if (r9 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.lang.Object r1 = r8.lock
            monitor-enter(r1)
            r2 = r0
        L_0x0008:
            int r3 = ohos.eventhandler.EventQueue.SUB_EVENT_QUEUE_NUM     // Catch:{ all -> 0x0054 }
            if (r2 >= r3) goto L_0x0052
            java.util.List<ohos.eventhandler.EventQueue$SubEventQueue> r3 = r8.subEventQueues     // Catch:{ all -> 0x0054 }
            java.lang.Object r3 = r3.get(r2)     // Catch:{ all -> 0x0054 }
            ohos.eventhandler.EventQueue$SubEventQueue r3 = (ohos.eventhandler.EventQueue.SubEventQueue) r3     // Catch:{ all -> 0x0054 }
            java.util.concurrent.PriorityBlockingQueue<ohos.eventhandler.InnerEvent> r3 = r3.queue     // Catch:{ all -> 0x0054 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x0054 }
        L_0x001a:
            boolean r4 = r3.hasNext()     // Catch:{ all -> 0x0054 }
            if (r4 == 0) goto L_0x004f
            java.lang.Object r4 = r3.next()     // Catch:{ all -> 0x0054 }
            ohos.eventhandler.InnerEvent r4 = (ohos.eventhandler.InnerEvent) r4     // Catch:{ all -> 0x0054 }
            int r5 = r4.eventId     // Catch:{ all -> 0x0054 }
            r6 = 1
            if (r10 != r5) goto L_0x002d
            r5 = r6
            goto L_0x002e
        L_0x002d:
            r5 = r0
        L_0x002e:
            if (r11 == 0) goto L_0x0037
            java.lang.Object r7 = r4.object     // Catch:{ all -> 0x0054 }
            if (r7 != r11) goto L_0x0035
            goto L_0x0037
        L_0x0035:
            r7 = r0
            goto L_0x0038
        L_0x0037:
            r7 = r6
        L_0x0038:
            if (r5 == 0) goto L_0x001a
            if (r7 == 0) goto L_0x001a
            java.lang.ref.WeakReference<ohos.eventhandler.EventHandler> r5 = r4.owner     // Catch:{ all -> 0x0054 }
            if (r5 == 0) goto L_0x004a
            java.lang.ref.WeakReference<ohos.eventhandler.EventHandler> r4 = r4.owner     // Catch:{ all -> 0x0054 }
            java.lang.Object r4 = r4.get()     // Catch:{ all -> 0x0054 }
            if (r4 != r9) goto L_0x004a
            r4 = r6
            goto L_0x004b
        L_0x004a:
            r4 = r0
        L_0x004b:
            if (r4 == 0) goto L_0x001a
            monitor-exit(r1)     // Catch:{ all -> 0x0054 }
            return r6
        L_0x004f:
            int r2 = r2 + 1
            goto L_0x0008
        L_0x0052:
            monitor-exit(r1)     // Catch:{ all -> 0x0054 }
            return r0
        L_0x0054:
            r8 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0054 }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.eventhandler.EventQueue.hasInnerEvent(ohos.eventhandler.EventHandler, int, java.lang.Object):boolean");
    }

    /* access modifiers changed from: package-private */
    public Optional<InnerEvent> getEvent() {
        synchronized (this.lock) {
            while (!this.isFinished) {
                Optional<InnerEvent> expiredEventLocked = getExpiredEventLocked();
                if (expiredEventLocked.isPresent()) {
                    return expiredEventLocked;
                }
                waitUntilLocked(this.wakeUpTime);
            }
            HiLog.error(LOG_LABEL, "getEvent: Break out", new Object[0]);
            return Optional.empty();
        }
    }

    /* access modifiers changed from: package-private */
    public Optional<InnerEvent> getExpiredEvent() {
        Optional<InnerEvent> expiredEventLocked;
        synchronized (this.lock) {
            expiredEventLocked = getExpiredEventLocked();
        }
        return expiredEventLocked;
    }

    /* access modifiers changed from: package-private */
    public void setPosted(InnerEvent innerEvent, boolean z) {
        synchronized (this.lock) {
            innerEvent.isPosted = z;
        }
    }

    /* access modifiers changed from: package-private */
    public void prepareSubEventQueue() {
        for (int i = 0; i < SUB_EVENT_QUEUE_NUM; i++) {
            this.subEventQueues.add(new SubEventQueue());
        }
    }

    private Optional<InnerEvent> getExpiredEventLocked() {
        InnerEvent peek;
        long fromNanoSecondsToMills = EventHandlerUtils.fromNanoSecondsToMills(System.nanoTime());
        this.wakeUpTime = Long.MAX_VALUE;
        this.nextInnerEvent = Optional.empty();
        Optional<InnerEvent> pickEventLocked = pickEventLocked(fromNanoSecondsToMills);
        if (pickEventLocked.isPresent()) {
            this.inIdle = false;
            return pickEventLocked;
        }
        if (!this.inIdle) {
            this.idleTimeStamp = fromNanoSecondsToMills;
            this.inIdle = true;
        }
        if (this.idleEvents.isEmpty() || (peek = this.idleEvents.peek()) == null || peek.sendTime > this.idleTimeStamp || peek.handleTime > fromNanoSecondsToMills) {
            return Optional.empty();
        }
        return popFrontEventFromListLocked(this.idleEvents);
    }

    private void remove(EventHandler.IEventFilter iEventFilter) {
        synchronized (this.lock) {
            for (int i = 0; i < SUB_EVENT_QUEUE_NUM; i++) {
                if (this.subEventQueues.get(i).queue != null) {
                    if (!this.subEventQueues.get(i).queue.isEmpty()) {
                        Iterator<InnerEvent> it = this.subEventQueues.get(i).queue.iterator();
                        while (it.hasNext()) {
                            if (iEventFilter.match(it.next())) {
                                it.remove();
                            }
                        }
                    }
                }
            }
            Iterator<InnerEvent> it2 = this.idleEvents.iterator();
            while (it2.hasNext()) {
                if (iEventFilter.match(it2.next())) {
                    it2.remove();
                }
            }
        }
    }

    private Optional<InnerEvent> pickEventLocked(long j) {
        int i = SUB_EVENT_QUEUE_NUM;
        int i2 = 0;
        while (true) {
            if (i2 >= SUB_EVENT_QUEUE_NUM) {
                break;
            }
            if (!(this.subEventQueues.get(i2).queue == null || this.subEventQueues.get(i2).queue.isEmpty() || this.subEventQueues.get(i2).queue.peek() == null)) {
                long j2 = this.subEventQueues.get(i2).queue.peek().handleTime;
                if (j2 >= this.wakeUpTime) {
                    continue;
                } else {
                    this.wakeUpTime = j2;
                    this.nextInnerEvent = Optional.of(this.subEventQueues.get(i2).queue.peek());
                    if (j2 <= j) {
                        if (i < SUB_EVENT_QUEUE_NUM && this.subEventQueues.get(i).handledCount < this.subEventQueues.get(i).maxHandledCount) {
                            this.subEventQueues.get(i).handledCount++;
                            break;
                        }
                        i = i2;
                    } else {
                        continue;
                    }
                }
            }
            i2++;
        }
        if (i >= SUB_EVENT_QUEUE_NUM) {
            return Optional.empty();
        }
        for (int i3 = 0; i3 < i; i3++) {
            this.subEventQueues.get(i3).handledCount = 0;
        }
        return popFrontEventFromListLocked(this.subEventQueues.get(i).queue);
    }

    private void waitUntilLocked(long j) {
        try {
            long fromNanoSecondsToMills = j - EventHandlerUtils.fromNanoSecondsToMills(System.nanoTime());
            if (fromNanoSecondsToMills >= 2147483647L) {
                this.lock.wait(2147483647L);
            } else if (fromNanoSecondsToMills > 0) {
                this.lock.wait(fromNanoSecondsToMills);
            } else {
                HiLog.debug(LOG_LABEL, "waitUntilLocked: not need to wait", new Object[0]);
            }
        } catch (InterruptedException unused) {
            HiLog.error(LOG_LABEL, "waitUntilLocked: Break out", new Object[0]);
        }
    }

    private Optional<InnerEvent> popFrontEventFromListLocked(PriorityBlockingQueue<InnerEvent> priorityBlockingQueue) {
        return Optional.of(priorityBlockingQueue.poll());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EventQueue:{");
        int i = 0;
        for (int i2 = 0; i2 < SUB_EVENT_QUEUE_NUM; i2++) {
            if (!(this.subEventQueues.get(i2) == null || this.subEventQueues.get(i2).queue == null)) {
                Iterator<InnerEvent> it = this.subEventQueues.get(i2).queue.iterator();
                while (it.hasNext()) {
                    sb.append("{InnerEvent Message " + i + ": " + it.next().toString() + "}, ");
                    i++;
                }
            }
        }
        sb.append("inPlatformRunner=");
        sb.append(this.inPlatformRunner);
        sb.append(", ");
        sb.append("isFinished=");
        sb.append(this.isFinished);
        sb.append("}");
        return sb.toString();
    }
}
