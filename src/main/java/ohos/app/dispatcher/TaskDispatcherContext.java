package ohos.app.dispatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicReferenceArray;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.app.dispatcher.threading.WorkerPoolConfig;
import ohos.appexecfwk.utils.AppLog;

public class TaskDispatcherContext {
    private static final int DEFAULT_PRIORITY_INDEX = 1;
    private static final int HIGH_PRIORITY_INDEX = 0;
    private static final int LOW_PRIORITY_INDEX = 2;
    private final WorkerPoolConfig config;
    private final TaskExecutor executor;
    private final AtomicReferenceArray<TaskDispatcher> globalDispatchers;
    private final Object instanceLock;
    private final Map<SerialTaskDispatcher, String> serialDispatchers;

    public TaskDispatcherContext() {
        this.globalDispatchers = new AtomicReferenceArray<>(TaskPriority.values().length);
        this.serialDispatchers = new WeakHashMap();
        this.config = new DefaultWorkerPoolConfig();
        this.instanceLock = new Object();
        this.executor = new TaskExecutor(this.config);
    }

    public TaskDispatcherContext(TaskExecutor taskExecutor) {
        this.globalDispatchers = new AtomicReferenceArray<>(TaskPriority.values().length);
        this.serialDispatchers = new WeakHashMap();
        this.config = new DefaultWorkerPoolConfig();
        this.instanceLock = new Object();
        this.executor = taskExecutor;
    }

    public WorkerPoolConfig getWorkerPoolConfig() {
        return this.config;
    }

    public Map<String, Long> getWorkerThreadsInfo() {
        TaskExecutor taskExecutor = this.executor;
        if (taskExecutor != null) {
            return taskExecutor.getWorkerThreadsInfo();
        }
        return new HashMap();
    }

    public Map<SerialTaskDispatcher, String> getSerialDispatchers() {
        return this.serialDispatchers;
    }

    public int getWaitingTasksCount() {
        TaskExecutor taskExecutor = this.executor;
        if (taskExecutor != null) {
            return taskExecutor.getPendingTasksSize();
        }
        return 0;
    }

    public long getTaskCounter() {
        return this.executor.getTaskCounter();
    }

    public SerialTaskDispatcher createSerialDispatcher(String str, TaskPriority taskPriority) {
        SerialTaskDispatcher serialTaskDispatcher = new SerialTaskDispatcher(str, taskPriority, this.executor);
        this.serialDispatchers.put(serialTaskDispatcher, str);
        return serialTaskDispatcher;
    }

    public ParallelTaskDispatcher createParallelDispatcher(String str, TaskPriority taskPriority) {
        return new ParallelTaskDispatcher(str, taskPriority, this.executor);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.app.dispatcher.TaskDispatcherContext$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$app$dispatcher$task$TaskPriority = new int[TaskPriority.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                ohos.app.dispatcher.task.TaskPriority[] r0 = ohos.app.dispatcher.task.TaskPriority.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.app.dispatcher.TaskDispatcherContext.AnonymousClass1.$SwitchMap$ohos$app$dispatcher$task$TaskPriority = r0
                int[] r0 = ohos.app.dispatcher.TaskDispatcherContext.AnonymousClass1.$SwitchMap$ohos$app$dispatcher$task$TaskPriority     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.app.dispatcher.task.TaskPriority r1 = ohos.app.dispatcher.task.TaskPriority.HIGH     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.app.dispatcher.TaskDispatcherContext.AnonymousClass1.$SwitchMap$ohos$app$dispatcher$task$TaskPriority     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.app.dispatcher.task.TaskPriority r1 = ohos.app.dispatcher.task.TaskPriority.DEFAULT     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.app.dispatcher.TaskDispatcherContext.AnonymousClass1.$SwitchMap$ohos$app$dispatcher$task$TaskPriority     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.app.dispatcher.task.TaskPriority r1 = ohos.app.dispatcher.task.TaskPriority.LOW     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.app.dispatcher.TaskDispatcherContext.AnonymousClass1.<clinit>():void");
        }
    }

    private static int mapPriorityIndex(TaskPriority taskPriority) {
        int i = AnonymousClass1.$SwitchMap$ohos$app$dispatcher$task$TaskPriority[taskPriority.ordinal()];
        if (i == 1) {
            return 0;
        }
        if (i == 2) {
            return 1;
        }
        if (i == 3) {
            return 2;
        }
        AppLog.w("TaskDispatcherContext.mapPriorityIndex unhandled priority: %{public}s", taskPriority);
        return 1;
    }

    public TaskDispatcher getGlobalTaskDispatcher(TaskPriority taskPriority) {
        int mapPriorityIndex = mapPriorityIndex(taskPriority);
        TaskDispatcher taskDispatcher = this.globalDispatchers.get(mapPriorityIndex);
        if (taskDispatcher != null) {
            return taskDispatcher;
        }
        GlobalTaskDispatcher globalTaskDispatcher = new GlobalTaskDispatcher(taskPriority, this.executor);
        return !this.globalDispatchers.compareAndSet(mapPriorityIndex, null, globalTaskDispatcher) ? this.globalDispatchers.get(mapPriorityIndex) : globalTaskDispatcher;
    }

    public void shutdown(boolean z) {
        this.executor.terminate(z);
    }
}
