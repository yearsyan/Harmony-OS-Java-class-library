package ohos.global.icu.impl;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;

public abstract class ICUNotifier {
    private List<EventListener> listeners;
    private final Object notifyLock = new Object();
    private NotifyThread notifyThread;

    /* access modifiers changed from: protected */
    public abstract boolean acceptsListener(EventListener eventListener);

    /* access modifiers changed from: protected */
    public abstract void notifyListener(EventListener eventListener);

    public void addListener(EventListener eventListener) {
        if (eventListener == null) {
            throw new NullPointerException();
        } else if (acceptsListener(eventListener)) {
            synchronized (this.notifyLock) {
                if (this.listeners == null) {
                    this.listeners = new ArrayList();
                } else {
                    for (EventListener eventListener2 : this.listeners) {
                        if (eventListener2 == eventListener) {
                            return;
                        }
                    }
                }
                this.listeners.add(eventListener);
            }
        } else {
            throw new IllegalStateException("Listener invalid for this notifier.");
        }
    }

    public void removeListener(EventListener eventListener) {
        if (eventListener != null) {
            synchronized (this.notifyLock) {
                if (this.listeners != null) {
                    Iterator<EventListener> it = this.listeners.iterator();
                    while (it.hasNext()) {
                        if (it.next() == eventListener) {
                            it.remove();
                            if (this.listeners.size() == 0) {
                                this.listeners = null;
                            }
                            return;
                        }
                    }
                }
                return;
            }
        }
        throw new NullPointerException();
    }

    public void notifyChanged() {
        synchronized (this.notifyLock) {
            if (this.listeners != null) {
                if (this.notifyThread == null) {
                    this.notifyThread = new NotifyThread(this);
                    this.notifyThread.setDaemon(true);
                    this.notifyThread.start();
                }
                this.notifyThread.queue((EventListener[]) this.listeners.toArray(new EventListener[this.listeners.size()]));
            }
        }
    }

    private static class NotifyThread extends Thread {
        private final ICUNotifier notifier;
        private final List<EventListener[]> queue = new ArrayList();

        NotifyThread(ICUNotifier iCUNotifier) {
            this.notifier = iCUNotifier;
        }

        public void queue(EventListener[] eventListenerArr) {
            synchronized (this) {
                this.queue.add(eventListenerArr);
                notify();
            }
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(5:0|0|(2:10|8)|16|14) */
        /* JADX WARNING: Missing exception handler attribute for start block: B:0:0x0000 */
        /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP:0: B:0:0x0000->B:14:0x0000, LOOP_START, SYNTHETIC, Splitter:B:0:0x0000] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r4 = this;
            L_0x0000:
                monitor-enter(r4)     // Catch:{ InterruptedException -> 0x0000 }
            L_0x0001:
                java.util.List<java.util.EventListener[]> r0 = r4.queue     // Catch:{ all -> 0x0024 }
                boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x0024 }
                if (r0 == 0) goto L_0x000d
                r4.wait()     // Catch:{ all -> 0x0024 }
                goto L_0x0001
            L_0x000d:
                java.util.List<java.util.EventListener[]> r0 = r4.queue     // Catch:{ all -> 0x0024 }
                r1 = 0
                java.lang.Object r0 = r0.remove(r1)     // Catch:{ all -> 0x0024 }
                java.util.EventListener[] r0 = (java.util.EventListener[]) r0     // Catch:{ all -> 0x0024 }
                monitor-exit(r4)     // Catch:{ all -> 0x0024 }
            L_0x0017:
                int r2 = r0.length
                if (r1 >= r2) goto L_0x0000
                ohos.global.icu.impl.ICUNotifier r2 = r4.notifier
                r3 = r0[r1]
                r2.notifyListener(r3)
                int r1 = r1 + 1
                goto L_0x0017
            L_0x0024:
                r0 = move-exception
                monitor-exit(r4)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.ICUNotifier.NotifyThread.run():void");
        }
    }
}
