package ohos.hiviewdfx;

import dalvik.system.BlockGuard;
import dalvik.system.CloseGuard;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiChecker;
import ohos.hiviewdfx.PlatformPolicyAdapter;
import ohos.miscservices.timeutility.Time;

public final class PlatformPolicyAdapter implements IHiCheckerPlatformAdapter {
    private static final ThreadLocal<EventHandler> EVENT_HANDLER = new ThreadLocal<EventHandler>() {
        /* class ohos.hiviewdfx.PlatformPolicyAdapter.AnonymousClass2 */

        /* access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public EventHandler initialValue() {
            return new EventHandler(EventRunner.current());
        }
    };
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, 218115339, "HiChecker");
    private static final int MAX_PENDING_TASKS = 10;
    private static final long MIN_CAUTION_INTERVAL_MS = 3000;
    private static final int STACK_TRACE_BUFFER = 300;
    private static final ThreadLocal<ThreadRuleChecker> THREAD_CHECKER = new ThreadLocal<ThreadRuleChecker>() {
        /* class ohos.hiviewdfx.PlatformPolicyAdapter.AnonymousClass1 */

        /* access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public ThreadRuleChecker initialValue() {
            return new ThreadRuleChecker(EnumSet.noneOf(HiChecker.Rule.class));
        }
    };
    private static final HashMap<Integer, Long> VM_CAUTION_TIME_RECORDS = new HashMap<>();

    protected PlatformPolicyAdapter() {
    }

    /* access modifiers changed from: private */
    public static final class CautionDetail {
        private Caution caution;
        private EnumSet<HiChecker.Rule> ruleSet;
        private String stackTrace;

        CautionDetail(Caution caution2, EnumSet<HiChecker.Rule> enumSet) {
            this.caution = caution2;
            this.ruleSet = enumSet;
        }

        /* access modifiers changed from: package-private */
        public String getStackTrace() {
            if (this.stackTrace == null) {
                StringWriter stringWriter = new StringWriter(300);
                PrintWriter printWriter = new PrintWriter(stringWriter);
                this.caution.printStackTrace(printWriter);
                printWriter.flush();
                printWriter.close();
                this.stackTrace = stringWriter.toString();
            }
            return this.stackTrace;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof CautionDetail) {
                return getStackTrace().equals(((CautionDetail) obj).getStackTrace());
            }
            return false;
        }

        public int hashCode() {
            return getStackTrace().hashCode();
        }

        /* access modifiers changed from: package-private */
        public boolean cautionEnabled(HiChecker.Rule rule) {
            return this.ruleSet.contains(rule);
        }
    }

    /* access modifiers changed from: private */
    public static class ThreadRuleChecker implements BlockGuard.Policy {
        private HashMap<Integer, Long> cautionTimeRecords = new HashMap<>();
        private EnumSet<HiChecker.Rule> checkRuleSet;
        private ArrayList<CautionDetail> pendingTask = new ArrayList<>();

        public int getPolicyMask() {
            return 0;
        }

        public void onExplicitGc() {
        }

        public void onUnbufferedIO() {
        }

        public ThreadRuleChecker(EnumSet<HiChecker.Rule> enumSet) {
            this.checkRuleSet = enumSet;
        }

        public void onWriteToDisk() {
            if (this.checkRuleSet.contains(HiChecker.Rule.CHECK_FILE_WRITE)) {
                handleThreadCaution(new Caution(HiChecker.Rule.CHECK_FILE_WRITE, null));
            }
        }

        public void onReadFromDisk() {
            if (this.checkRuleSet.contains(HiChecker.Rule.CHECK_FILE_READ)) {
                handleThreadCaution(new Caution(HiChecker.Rule.CHECK_FILE_READ, null));
            }
        }

        public void onNetwork() {
            if (this.checkRuleSet.contains(HiChecker.Rule.CHECK_NETWORK_ACCESS)) {
                handleThreadCaution(new Caution(HiChecker.Rule.CHECK_NETWORK_ACCESS, null));
            }
        }

        public void onSlowProcess(String str) {
            if (this.checkRuleSet.contains(HiChecker.Rule.CHECK_SLOW_PROCESS)) {
                handleThreadCaution(new Caution(HiChecker.Rule.CHECK_SLOW_PROCESS, str));
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void updateCheckRuleSet(EnumSet<HiChecker.Rule> enumSet) {
            this.checkRuleSet = enumSet;
        }

        private void handleThreadCaution(Caution caution) {
            CautionDetail cautionDetail = new CautionDetail(caution, this.checkRuleSet);
            PlatformPolicyAdapter.removeOldRecords(this.cautionTimeRecords);
            Integer valueOf = Integer.valueOf(cautionDetail.hashCode());
            if (!this.cautionTimeRecords.containsKey(valueOf)) {
                this.cautionTimeRecords.put(valueOf, Long.valueOf(Time.getRealActiveTime()));
                if (EventRunner.current() == null) {
                    onThreadCautionFound(cautionDetail);
                    return;
                }
                this.pendingTask.add(cautionDetail);
                if (this.pendingTask.size() <= 1) {
                    ((EventHandler) PlatformPolicyAdapter.EVENT_HANDLER.get()).postTask((Runnable) new Runnable() {
                        /* class ohos.hiviewdfx.$$Lambda$PlatformPolicyAdapter$ThreadRuleChecker$Rw_ie78K2AExxxrdymPlGouw8Mg */

                        public final void run() {
                            PlatformPolicyAdapter.ThreadRuleChecker.this.lambda$handleThreadCaution$0$PlatformPolicyAdapter$ThreadRuleChecker();
                        }
                    }, EventHandler.Priority.IMMEDIATE);
                }
            }
        }

        public /* synthetic */ void lambda$handleThreadCaution$0$PlatformPolicyAdapter$ThreadRuleChecker() {
            int size = this.pendingTask.size();
            if (this.pendingTask.size() > 10) {
                HiLog.info(PlatformPolicyAdapter.LOG_LABEL, "too many cautions, ignore %{public}d caution(s)", Integer.valueOf(this.pendingTask.size() - 10));
                size = 10;
            }
            for (int i = 0; i < size; i++) {
                onThreadCautionFound(this.pendingTask.get(i));
            }
            this.pendingTask.clear();
        }

        private void onThreadCautionFound(CautionDetail cautionDetail) {
            if (cautionDetail.cautionEnabled(HiChecker.Rule.CAUTION_BY_LOG)) {
                HiLog.info(PlatformPolicyAdapter.LOG_LABEL, "%{public}s %{public}s", "HiChecker,", cautionDetail.getStackTrace());
            }
            HiChecker.notifyCustomListener(cautionDetail.caution);
            if (cautionDetail.cautionEnabled(HiChecker.Rule.CAUTION_BY_CRASH)) {
                throw new CautionDeathException("HiChecker Caution", cautionDetail.caution);
            }
        }
    }

    private static class ResourceTagReporter implements CloseGuard.Reporter {
        private ResourceTagReporter() {
        }

        public void report(String str, Throwable th) {
            PlatformPolicyAdapter.onVmCautionFound(new CautionDetail(new Caution(HiChecker.Rule.CHECK_VM_RELEASE_MISS, null, th), HiChecker.getRule()));
        }
    }

    @Override // ohos.hiviewdfx.IHiCheckerPlatformAdapter
    public void updateThreadCheckRule(EnumSet<HiChecker.Rule> enumSet) {
        ThreadRuleChecker threadRuleChecker;
        if (enumSet == null || enumSet.size() == 0) {
            BlockGuard.setThreadPolicy(BlockGuard.LAX_POLICY);
            return;
        }
        EnumSet<HiChecker.Rule> clone = enumSet.clone();
        if (!hasCautionRule(clone)) {
            clone.add(HiChecker.Rule.CAUTION_BY_LOG);
        }
        ThreadRuleChecker threadPolicy = BlockGuard.getThreadPolicy();
        if (threadPolicy instanceof ThreadRuleChecker) {
            threadRuleChecker = threadPolicy;
        } else {
            threadRuleChecker = THREAD_CHECKER.get();
            BlockGuard.setThreadPolicy(threadRuleChecker);
        }
        threadRuleChecker.updateCheckRuleSet(clone);
    }

    @Override // ohos.hiviewdfx.IHiCheckerPlatformAdapter
    public void notifySlowProcess(String str) {
        ThreadRuleChecker threadPolicy = BlockGuard.getThreadPolicy();
        if (threadPolicy instanceof ThreadRuleChecker) {
            threadPolicy.onSlowProcess(str);
        }
    }

    @Override // ohos.hiviewdfx.IHiCheckerPlatformAdapter
    public void setResourceTagEnabled(boolean z) {
        if (z && !(CloseGuard.getReporter() instanceof ResourceTagReporter)) {
            CloseGuard.setReporter(new ResourceTagReporter());
        }
        CloseGuard.setEnabled(z);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0030, code lost:
        if (hasCautionRule(r5.ruleSet) != false) goto L_0x003b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0032, code lost:
        r5.ruleSet.add(ohos.hiviewdfx.HiChecker.Rule.CAUTION_BY_LOG);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0042, code lost:
        if (r5.cautionEnabled(ohos.hiviewdfx.HiChecker.Rule.CAUTION_BY_LOG) == false) goto L_0x0059;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0044, code lost:
        ohos.hiviewdfx.HiLog.info(ohos.hiviewdfx.PlatformPolicyAdapter.LOG_LABEL, "%{public}s %{public}s", "HiChecker,", r5.getStackTrace());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0059, code lost:
        ohos.hiviewdfx.HiChecker.notifyCustomListener(r5.caution);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0066, code lost:
        if (r5.cautionEnabled(ohos.hiviewdfx.HiChecker.Rule.CAUTION_BY_CRASH) == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0068, code lost:
        ohos.hiviewdfx.HiLog.info(ohos.hiviewdfx.PlatformPolicyAdapter.LOG_LABEL, "HiChecker caution with RULE_CAUTION_CRASH; exit.", new java.lang.Object[0]);
        ohos.os.ProcessManager.kill(ohos.os.ProcessManager.getPid());
        java.lang.System.exit(10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void onVmCautionFound(ohos.hiviewdfx.PlatformPolicyAdapter.CautionDetail r5) {
        /*
        // Method dump skipped, instructions count: 129
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.hiviewdfx.PlatformPolicyAdapter.onVmCautionFound(ohos.hiviewdfx.PlatformPolicyAdapter$CautionDetail):void");
    }

    /* access modifiers changed from: private */
    public static void removeOldRecords(HashMap<Integer, Long> hashMap) {
        if (hashMap != null) {
            long realActiveTime = Time.getRealActiveTime();
            Iterator<Map.Entry<Integer, Long>> it = hashMap.entrySet().iterator();
            while (it.hasNext()) {
                if (realActiveTime - it.next().getValue().longValue() > MIN_CAUTION_INTERVAL_MS) {
                    it.remove();
                }
            }
        }
    }

    private static boolean hasCautionRule(EnumSet<HiChecker.Rule> enumSet) {
        return enumSet.contains(HiChecker.Rule.CAUTION_BY_CRASH) || enumSet.contains(HiChecker.Rule.CAUTION_BY_LOG);
    }

    /* access modifiers changed from: private */
    public static class CautionDeathException extends RuntimeException {
        private static final long serialVersionUID = -5394718584743796391L;

        CautionDeathException(String str, Throwable th) {
            super(str, th);
        }
    }
}
