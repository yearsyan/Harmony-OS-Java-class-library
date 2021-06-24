package ohos.app.dispatcher;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Consumer;
import ohos.app.dispatcher.task.Task;
import ohos.app.dispatcher.task.TaskExecuteInterceptor;
import ohos.app.dispatcher.task.TaskListener;
import ohos.app.dispatcher.task.TaskStage;
import ohos.appexecfwk.utils.AppLog;

/* access modifiers changed from: package-private */
public class BarrierHandler implements TaskExecuteInterceptor {
    private final Object barrierLock = new Object();
    private final LinkedList<BarrierPair> barrierQueue = new LinkedList<>();
    private final TaskExecutor executor;

    /* access modifiers changed from: private */
    public static class BarrierPair {
        Task barrier;
        Set<Task> tasks;

        BarrierPair(Set<Task> set, Task task) {
            this.tasks = set;
            this.barrier = task;
        }
    }

    public BarrierHandler(TaskExecutor taskExecutor) {
        this.executor = taskExecutor;
    }

    @Override // ohos.app.dispatcher.task.TaskExecuteInterceptor
    public boolean intercept(Task task) {
        listenToTask(task);
        boolean addTaskAfterBarrier = addTaskAfterBarrier(task);
        if (addTaskAfterBarrier) {
            AppLog.d("Barrier.intercept intercepted a task.", new Object[0]);
        }
        return addTaskAfterBarrier;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0024  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addBarrier(ohos.app.dispatcher.task.Task r8) {
        /*
            r7 = this;
            r7.listenToTask(r8)
            java.lang.Object r0 = r7.barrierLock
            monitor-enter(r0)
            java.util.LinkedList<ohos.app.dispatcher.BarrierHandler$BarrierPair> r1 = r7.barrierQueue     // Catch:{ all -> 0x004d }
            java.lang.Object r1 = r1.peekLast()     // Catch:{ all -> 0x004d }
            ohos.app.dispatcher.BarrierHandler$BarrierPair r1 = (ohos.app.dispatcher.BarrierHandler.BarrierPair) r1     // Catch:{ all -> 0x004d }
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0021
            java.util.Set<ohos.app.dispatcher.task.Task> r4 = r1.tasks     // Catch:{ all -> 0x004d }
            boolean r4 = r7.hasTask(r4)     // Catch:{ all -> 0x004d }
            if (r4 != 0) goto L_0x001f
            ohos.app.dispatcher.task.Task r4 = r1.barrier     // Catch:{ all -> 0x004d }
            if (r4 != 0) goto L_0x001f
            goto L_0x0021
        L_0x001f:
            r4 = r3
            goto L_0x0022
        L_0x0021:
            r4 = r2
        L_0x0022:
            if (r1 == 0) goto L_0x002c
            ohos.app.dispatcher.task.Task r5 = r1.barrier     // Catch:{ all -> 0x004d }
            if (r5 == 0) goto L_0x0029
            goto L_0x002c
        L_0x0029:
            r1.barrier = r8     // Catch:{ all -> 0x004d }
            goto L_0x0037
        L_0x002c:
            java.util.LinkedList<ohos.app.dispatcher.BarrierHandler$BarrierPair> r1 = r7.barrierQueue     // Catch:{ all -> 0x004d }
            ohos.app.dispatcher.BarrierHandler$BarrierPair r5 = new ohos.app.dispatcher.BarrierHandler$BarrierPair     // Catch:{ all -> 0x004d }
            r6 = 0
            r5.<init>(r6, r8)     // Catch:{ all -> 0x004d }
            r1.offerLast(r5)     // Catch:{ all -> 0x004d }
        L_0x0037:
            monitor-exit(r0)     // Catch:{ all -> 0x004d }
            java.lang.Object[] r0 = new java.lang.Object[r2]
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r4)
            r0[r3] = r1
            java.lang.String r1 = "Barrier.addBarrier need execute now: %{public}b"
            ohos.appexecfwk.utils.AppLog.d(r1, r0)
            if (r4 == 0) goto L_0x004c
            ohos.app.dispatcher.TaskExecutor r7 = r7.executor
            r7.execute(r8)
        L_0x004c:
            return
        L_0x004d:
            r7 = move-exception
            monitor-exit(r0)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.app.dispatcher.BarrierHandler.addBarrier(ohos.app.dispatcher.task.Task):void");
    }

    private void listenToTask(final Task task) {
        task.addTaskListener(new TaskListener() {
            /* class ohos.app.dispatcher.BarrierHandler.AnonymousClass1 */

            @Override // ohos.app.dispatcher.task.TaskListener
            public void onChanged(TaskStage taskStage) {
                if (taskStage.isDone()) {
                    BarrierHandler.this.onTaskDone(task);
                }
            }
        });
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x003c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean addTaskAfterBarrier(ohos.app.dispatcher.task.Task r5) {
        /*
            r4 = this;
            java.lang.Object r0 = r4.barrierLock
            monitor-enter(r0)
            java.util.LinkedList<ohos.app.dispatcher.BarrierHandler$BarrierPair> r1 = r4.barrierQueue     // Catch:{ all -> 0x003f }
            java.lang.Object r1 = r1.peekLast()     // Catch:{ all -> 0x003f }
            ohos.app.dispatcher.BarrierHandler$BarrierPair r1 = (ohos.app.dispatcher.BarrierHandler.BarrierPair) r1     // Catch:{ all -> 0x003f }
            if (r1 == 0) goto L_0x0023
            ohos.app.dispatcher.task.Task r2 = r1.barrier     // Catch:{ all -> 0x003f }
            if (r2 == 0) goto L_0x0012
            goto L_0x0023
        L_0x0012:
            java.util.Set<ohos.app.dispatcher.task.Task> r2 = r1.tasks     // Catch:{ all -> 0x003f }
            if (r2 != 0) goto L_0x001d
            java.util.Set r5 = r4.createTaskSet(r5)     // Catch:{ all -> 0x003f }
            r1.tasks = r5     // Catch:{ all -> 0x003f }
            goto L_0x0032
        L_0x001d:
            java.util.Set<ohos.app.dispatcher.task.Task> r1 = r1.tasks     // Catch:{ all -> 0x003f }
            r1.add(r5)     // Catch:{ all -> 0x003f }
            goto L_0x0032
        L_0x0023:
            java.util.LinkedList<ohos.app.dispatcher.BarrierHandler$BarrierPair> r1 = r4.barrierQueue     // Catch:{ all -> 0x003f }
            ohos.app.dispatcher.BarrierHandler$BarrierPair r2 = new ohos.app.dispatcher.BarrierHandler$BarrierPair     // Catch:{ all -> 0x003f }
            java.util.Set r5 = r4.createTaskSet(r5)     // Catch:{ all -> 0x003f }
            r3 = 0
            r2.<init>(r5, r3)     // Catch:{ all -> 0x003f }
            r1.offerLast(r2)     // Catch:{ all -> 0x003f }
        L_0x0032:
            java.util.LinkedList<ohos.app.dispatcher.BarrierHandler$BarrierPair> r4 = r4.barrierQueue     // Catch:{ all -> 0x003f }
            int r4 = r4.size()     // Catch:{ all -> 0x003f }
            r5 = 1
            if (r4 <= r5) goto L_0x003c
            goto L_0x003d
        L_0x003c:
            r5 = 0
        L_0x003d:
            monitor-exit(r0)     // Catch:{ all -> 0x003f }
            return r5
        L_0x003f:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x003f }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.app.dispatcher.BarrierHandler.addTaskAfterBarrier(ohos.app.dispatcher.task.Task):boolean");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void onTaskDone(Task task) {
        boolean z;
        synchronized (this.barrierLock) {
            BarrierPair peekFirst = this.barrierQueue.peekFirst();
            z = true;
            if (peekFirst != null) {
                if (hasTask(peekFirst.tasks)) {
                    z = peekFirst.tasks.remove(task);
                    if (peekFirst.tasks.isEmpty() && peekFirst.barrier != null) {
                        AppLog.d("Barrier.onTaskDone execute barrier task after task done.", new Object[0]);
                        this.executor.execute(peekFirst.barrier);
                    }
                } else if (task.equals(peekFirst.barrier)) {
                    AppLog.d("Barrier.onTaskDone remove a barrier.", new Object[0]);
                    peekFirst.barrier = null;
                    if (this.barrierQueue.size() > 1) {
                        this.barrierQueue.pollFirst();
                        BarrierPair peekFirst2 = this.barrierQueue.peekFirst();
                        if (hasTask(peekFirst2.tasks)) {
                            peekFirst2.tasks.forEach(new Consumer() {
                                /* class ohos.app.dispatcher.$$Lambda$BarrierHandler$BiXQbGmM1CKYY2mzwEK3984nj4 */

                                @Override // java.util.function.Consumer
                                public final void accept(Object obj) {
                                    BarrierHandler.this.lambda$onTaskDone$0$BarrierHandler((Task) obj);
                                }
                            });
                        } else if (peekFirst2.barrier != null) {
                            AppLog.d("Barrier.onTaskDone execute barrier task after barrier done.", new Object[0]);
                            this.executor.execute(peekFirst2.barrier);
                        } else {
                            AppLog.w("Barrier.onTaskDone: Detected an empty node.", new Object[0]);
                        }
                    }
                }
            }
            z = false;
        }
        if (!z) {
            AppLog.w("Barrier.onTaskDone: Task remove failed.", new Object[0]);
        }
    }

    public /* synthetic */ void lambda$onTaskDone$0$BarrierHandler(Task task) {
        this.executor.execute(task);
    }

    private boolean hasTask(Set<Task> set) {
        return set != null && !set.isEmpty();
    }

    private Set<Task> createTaskSet(Task task) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        linkedHashSet.add(task);
        return linkedHashSet;
    }
}
