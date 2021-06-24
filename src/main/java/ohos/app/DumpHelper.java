package ohos.app;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import ohos.app.dispatcher.SerialTaskDispatcher;
import ohos.app.dispatcher.TaskDispatcherContext;
import ohos.bundle.AbilityInfo;
import ohos.bundle.ApplicationInfo;

public class DumpHelper {
    private final ConcurrentMap<Object, Context> abilityRecords;
    private Date appCreateTime;
    private final ApplicationInfo applicationInfo;
    private final TaskDispatcherContext dispatcherContext;
    private final ProcessInfo processInfo;

    public DumpHelper(Application application) {
        this.applicationInfo = application.getApplicationInfo();
        this.processInfo = application.getProcessInfo();
        this.dispatcherContext = application.getTaskDispatcherContext();
        this.appCreateTime = application.getAppCreateTime();
        this.abilityRecords = application.getAbilityRecord();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002f, code lost:
        if (r9.equals("-h") != false) goto L_0x003d;
     */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0058  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dump(java.lang.String r6, java.io.FileDescriptor r7, java.io.PrintWriter r8, java.lang.String[] r9) {
        /*
            r5 = this;
            if (r9 == 0) goto L_0x005c
            int r7 = r9.length
            r0 = 1
            if (r7 == r0) goto L_0x0007
            goto L_0x005c
        L_0x0007:
            r7 = 0
            r9 = r9[r7]
            r1 = -1
            int r2 = r9.hashCode()
            r3 = -1226959101(0xffffffffb6de1703, float:-6.6187945E-6)
            r4 = 2
            if (r2 == r3) goto L_0x0032
            r3 = 1499(0x5db, float:2.1E-42)
            if (r2 == r3) goto L_0x0029
            r7 = 1455280(0x1634b0, float:2.039282E-39)
            if (r2 == r7) goto L_0x001f
            goto L_0x003c
        L_0x001f:
            java.lang.String r7 = "-tdm"
            boolean r7 = r9.equals(r7)
            if (r7 == 0) goto L_0x003c
            r7 = r4
            goto L_0x003d
        L_0x0029:
            java.lang.String r2 = "-h"
            boolean r9 = r9.equals(r2)
            if (r9 == 0) goto L_0x003c
            goto L_0x003d
        L_0x0032:
            java.lang.String r7 = "-application"
            boolean r7 = r9.equals(r7)
            if (r7 == 0) goto L_0x003c
            r7 = r0
            goto L_0x003d
        L_0x003c:
            r7 = r1
        L_0x003d:
            if (r7 == 0) goto L_0x0058
            if (r7 == r0) goto L_0x0051
            if (r7 == r4) goto L_0x0047
            r5.showIllegalInformation(r6, r8)
            goto L_0x005b
        L_0x0047:
            r5.showProcessInfo(r6, r8)
            r5.showWorkerPool(r6, r8)
            r5.showTaskQueue(r6, r8)
            goto L_0x005b
        L_0x0051:
            r5.showApplicationInfo(r6, r8)
            r5.showAbilityInfo(r6, r8)
            goto L_0x005b
        L_0x0058:
            r5.showHelp(r6, r8)
        L_0x005b:
            return
        L_0x005c:
            r5.showIllegalInformation(r6, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.app.DumpHelper.dump(java.lang.String, java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
    }

    public void showHelp(String str, PrintWriter printWriter) {
        printWriter.println("Usage:");
        printWriter.println("  -ability: dump information of ability.");
        printWriter.println("  -application: dump information of user application.");
        printWriter.println("  -tdm: dump information of TDM threading model.");
    }

    public void showIllegalInformation(String str, PrintWriter printWriter) {
        printWriter.println("The arguments are illegal and you can enter '-h' for help.");
    }

    public void showApplicationInfo(String str, PrintWriter printWriter) {
        printWriter.println("application information: ");
        if (this.applicationInfo != null) {
            printWriter.print("  name: " + this.applicationInfo.getName());
            printWriter.print("  process: " + this.applicationInfo.getProcess());
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        printWriter.println("  create time: " + simpleDateFormat.format(this.appCreateTime));
        printWriter.println("  running time: " + getRunningTime(this.appCreateTime));
    }

    public void showProcessInfo(String str, PrintWriter printWriter) {
        if (this.processInfo != null) {
            printWriter.println("process information: ");
            printWriter.print("  process name: " + this.processInfo.getProcessName());
            printWriter.println("  process id: " + this.processInfo.getPid());
        }
    }

    public void showAbilityInfo(String str, PrintWriter printWriter) {
        printWriter.println("ability information: ");
        for (Map.Entry<Object, Context> entry : this.abilityRecords.entrySet()) {
            AbilityInfo abilityInfo = entry.getValue().getAbilityInfo();
            if (abilityInfo != null) {
                printWriter.print("  name: " + abilityInfo.getClassName());
                printWriter.print("  bundle name: " + abilityInfo.getBundleName());
                printWriter.print("  process: " + abilityInfo.getProcess());
                printWriter.print("  targetAbility: " + abilityInfo.getTargetAbility());
                printWriter.print("  type: " + abilityInfo.getType());
                printWriter.print("  orientation: " + abilityInfo.getOrientation());
                printWriter.print("  launchMode: " + abilityInfo.getLaunchMode());
                printWriter.println("  description: " + abilityInfo.getDescription());
            }
        }
    }

    public void showWorkerPool(String str, PrintWriter printWriter) {
        printWriter.println("WorkerPool information: ");
        printWriter.print("  max thread count: " + this.dispatcherContext.getWorkerPoolConfig().getMaxThreadCount());
        printWriter.print("  core thread count: " + this.dispatcherContext.getWorkerPoolConfig().getCoreThreadCount());
        printWriter.println("  keep alive time: " + this.dispatcherContext.getWorkerPoolConfig().getKeepAliveTime());
        Map<String, Long> workerThreadsInfo = this.dispatcherContext.getWorkerThreadsInfo();
        if (!workerThreadsInfo.isEmpty()) {
            for (Map.Entry<String, Long> entry : workerThreadsInfo.entrySet()) {
                printWriter.println("  thread name: " + entry.getKey() + "  finished tasks count: " + entry.getValue());
            }
        }
    }

    public void showTaskQueue(String str, PrintWriter printWriter) {
        printWriter.println("waiting tasks information: ");
        printWriter.println("  sum of finished tasks: " + this.dispatcherContext.getTaskCounter());
        printWriter.println("  waiting tasks count of TaskExecutor: " + this.dispatcherContext.getWaitingTasksCount());
        for (Map.Entry<SerialTaskDispatcher, String> entry : this.dispatcherContext.getSerialDispatchers().entrySet()) {
            SerialTaskDispatcher key = entry.getKey();
            if (key != null) {
                printWriter.println("  serial dispatcher name: " + key.getDispatcherName() + "  waiting tasks count: " + key.getWorkingTasksSize());
            }
        }
    }

    public String getRunningTime(Date date) {
        long time = new Date().getTime() - date.getTime();
        return (time / 60000) + " minutes " + ((time % 60000) / 1000) + "seconds";
    }
}
