package ohos.app.dispatcher.task;

import java.io.Serializable;
import java.util.Comparator;
import ohos.appexecfwk.utils.AppLog;

public class TaskPriorityComparator implements Comparator<Task>, Serializable {
    private static final int DEFAULT_PRIORITY_WEIGHT = 1;
    private static final int HIGH_PRIORITY_WEIGHT = 2;
    private static final int LOW_PRIORITY_WEIGHT = 0;
    private static final long serialVersionUID = -4352745781101560609L;

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.app.dispatcher.task.TaskPriorityComparator$1  reason: invalid class name */
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
                ohos.app.dispatcher.task.TaskPriorityComparator.AnonymousClass1.$SwitchMap$ohos$app$dispatcher$task$TaskPriority = r0
                int[] r0 = ohos.app.dispatcher.task.TaskPriorityComparator.AnonymousClass1.$SwitchMap$ohos$app$dispatcher$task$TaskPriority     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.app.dispatcher.task.TaskPriority r1 = ohos.app.dispatcher.task.TaskPriority.HIGH     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.app.dispatcher.task.TaskPriorityComparator.AnonymousClass1.$SwitchMap$ohos$app$dispatcher$task$TaskPriority     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.app.dispatcher.task.TaskPriority r1 = ohos.app.dispatcher.task.TaskPriority.DEFAULT     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.app.dispatcher.task.TaskPriorityComparator.AnonymousClass1.$SwitchMap$ohos$app$dispatcher$task$TaskPriority     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.app.dispatcher.task.TaskPriority r1 = ohos.app.dispatcher.task.TaskPriority.LOW     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.app.dispatcher.task.TaskPriorityComparator.AnonymousClass1.<clinit>():void");
        }
    }

    private static int getPriorityWeight(TaskPriority taskPriority) {
        int i = AnonymousClass1.$SwitchMap$ohos$app$dispatcher$task$TaskPriority[taskPriority.ordinal()];
        if (i == 1) {
            return 2;
        }
        if (i == 2) {
            return 1;
        }
        if (i == 3) {
            return 0;
        }
        AppLog.w("TaskPriorityComparator.getPriorityWeight unhandled priority: %{public}s", taskPriority);
        return 1;
    }

    public int compare(Task task, Task task2) {
        if (task == null || task2 == null) {
            throw new NullPointerException("Comparable is null");
        } else if (task == task2) {
            return 0;
        } else {
            int priorityWeight = getPriorityWeight(task.getPriority());
            int priorityWeight2 = getPriorityWeight(task2.getPriority());
            if (priorityWeight == priorityWeight2) {
                return Long.compare(task.getSequence(), task2.getSequence());
            }
            return Integer.compare(priorityWeight2, priorityWeight);
        }
    }
}
