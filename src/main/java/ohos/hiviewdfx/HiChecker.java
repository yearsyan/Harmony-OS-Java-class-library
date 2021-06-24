package ohos.hiviewdfx;

import java.util.EnumSet;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import ohos.hiviewdfx.HiChecker;

public class HiChecker {
    private static final EnumSet<Rule> ALL_THREAD_CAUTION_RULES = EnumSet.complementOf(ALL_VM_RULES);
    private static final EnumSet<Rule> ALL_THREAD_RULES = EnumSet.range(Rule.CHECK_FILE_READ, Rule.CHECK_SLOW_PROCESS);
    private static final EnumSet<Rule> ALL_VM_CAUTION_RULES = EnumSet.complementOf(ALL_THREAD_RULES);
    private static final EnumSet<Rule> ALL_VM_RULES = EnumSet.of(Rule.CHECK_VM_RELEASE_MISS);
    private static final Object LOCK_OBJ = new Object();
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, 218115339, "HiChecker");
    private static final ThreadLocal<EnumSet<Rule>> THREAD_LOCAL_RULE = new ThreadLocal<EnumSet<Rule>>() {
        /* class ohos.hiviewdfx.HiChecker.AnonymousClass1 */

        /* access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public EnumSet<Rule> initialValue() {
            return EnumSet.noneOf(Rule.class);
        }
    };
    private static volatile Executor cautionExecutor = null;
    private static volatile OnCautionListener cautionListener = null;
    private static IHiCheckerPlatformAdapter platformAdapter = new PlatformPolicyAdapter();
    private static volatile EnumSet<Rule> sVmRule = EnumSet.noneOf(Rule.class);

    public interface OnCautionListener {
        void onCautionFound(Caution caution);
    }

    public enum Rule {
        CAUTION_BY_LOG,
        CAUTION_BY_CRASH,
        CHECK_FILE_READ,
        CHECK_FILE_WRITE,
        CHECK_NETWORK_ACCESS,
        CHECK_SLOW_PROCESS,
        CHECK_VM_RELEASE_MISS
    }

    private HiChecker() {
    }

    public static void addRule(EnumSet<Rule> enumSet) {
        if (enumSet == null) {
            HiLog.warn(LOG_LABEL, "rules is null to addRule, return.", new Object[0]);
            return;
        }
        synchronized (LOCK_OBJ) {
            EnumSet<Rule> enumSet2 = THREAD_LOCAL_RULE.get();
            enumSet2.addAll(enumSet);
            enumSet2.retainAll(ALL_THREAD_CAUTION_RULES);
            platformAdapter.updateThreadCheckRule(enumSet2);
            sVmRule.addAll(enumSet);
            sVmRule.retainAll(ALL_VM_CAUTION_RULES);
            if (enumSet.contains(Rule.CHECK_VM_RELEASE_MISS)) {
                platformAdapter.setResourceTagEnabled(true);
            }
        }
    }

    public static void addRule(Rule rule) {
        if (rule == null) {
            HiLog.warn(LOG_LABEL, "rule is null to addRule, return.", new Object[0]);
        } else {
            addRule(EnumSet.of(rule));
        }
    }

    public static void addDefaultRules() {
        EnumSet noneOf = EnumSet.noneOf(Rule.class);
        noneOf.addAll(ALL_THREAD_RULES);
        noneOf.addAll(ALL_VM_RULES);
        noneOf.add(Rule.CAUTION_BY_LOG);
        addRule(noneOf);
    }

    public static void addAllRules() {
        addRule(EnumSet.allOf(Rule.class));
    }

    public static void removeRule(EnumSet<Rule> enumSet) {
        if (enumSet == null) {
            HiLog.warn(LOG_LABEL, "rules is null to removeRule, return.", new Object[0]);
            return;
        }
        synchronized (LOCK_OBJ) {
            EnumSet<Rule> enumSet2 = THREAD_LOCAL_RULE.get();
            enumSet2.removeAll(enumSet);
            platformAdapter.updateThreadCheckRule(enumSet2);
            sVmRule.removeAll(enumSet);
            if (enumSet.contains(Rule.CHECK_VM_RELEASE_MISS)) {
                platformAdapter.setResourceTagEnabled(false);
            }
        }
    }

    public static void removeRule(Rule rule) {
        if (rule == null) {
            HiLog.warn(LOG_LABEL, "rule is null to removeRule, return.", new Object[0]);
        } else {
            removeRule(EnumSet.of(rule));
        }
    }

    public static void removeAllRules() {
        synchronized (LOCK_OBJ) {
            removeRule(EnumSet.allOf(Rule.class));
        }
    }

    public static EnumSet<Rule> getRule() {
        EnumSet<Rule> noneOf;
        synchronized (LOCK_OBJ) {
            noneOf = EnumSet.noneOf(Rule.class);
            noneOf.addAll(THREAD_LOCAL_RULE.get());
            noneOf.addAll(sVmRule);
        }
        return noneOf;
    }

    public static boolean contains(Rule rule) {
        boolean z;
        synchronized (LOCK_OBJ) {
            if (!sVmRule.contains(rule)) {
                if (!THREAD_LOCAL_RULE.get().contains(rule)) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    public static EnumSet<Rule> removeCheckFileWriteRule() {
        EnumSet<Rule> rule;
        synchronized (LOCK_OBJ) {
            rule = getRule();
            removeRule(Rule.CHECK_FILE_WRITE);
        }
        return rule;
    }

    public static EnumSet<Rule> removeCheckFileReadRule() {
        EnumSet<Rule> rule;
        synchronized (LOCK_OBJ) {
            rule = getRule();
            removeRule(Rule.CHECK_FILE_READ);
        }
        return rule;
    }

    public static void notifySlowProcess(String str) {
        synchronized (LOCK_OBJ) {
            platformAdapter.notifySlowProcess(str);
        }
    }

    public static void setCautionListener(Executor executor, OnCautionListener onCautionListener) {
        if (executor == null) {
            throw new NullPointerException("executor must not be null");
        } else if (onCautionListener != null) {
            synchronized (LOCK_OBJ) {
                cautionExecutor = executor;
                cautionListener = onCautionListener;
            }
        } else {
            throw new NullPointerException("listener must not be null");
        }
    }

    protected static void notifyCustomListener(Caution caution) {
        Executor executor = cautionExecutor;
        OnCautionListener onCautionListener = cautionListener;
        if (executor != null && onCautionListener != null) {
            try {
                executor.execute(new Runnable(caution) {
                    /* class ohos.hiviewdfx.$$Lambda$HiChecker$Cbx6ZdDaNydFkx9HtcsrQ2rtuCc */
                    private final /* synthetic */ Caution f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void run() {
                        HiChecker.lambda$notifyCustomListener$0(HiChecker.OnCautionListener.this, this.f$1);
                    }
                });
            } catch (RejectedExecutionException unused) {
                HiLog.info(LOG_LABEL, "HiChecker notification failed", new Object[0]);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000e, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.info(ohos.hiviewdfx.HiChecker.LOG_LABEL, "HiChecker notification failed", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001c, code lost:
        addRule(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001f, code lost:
        throw r2;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0010 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static /* synthetic */ void lambda$notifyCustomListener$0(ohos.hiviewdfx.HiChecker.OnCautionListener r2, ohos.hiviewdfx.Caution r3) {
        /*
            java.util.EnumSet r0 = getRule()
            removeAllRules()
            r2.onCautionFound(r3)     // Catch:{ NullPointerException -> 0x0010 }
        L_0x000a:
            addRule(r0)
            goto L_0x001b
        L_0x000e:
            r2 = move-exception
            goto L_0x001c
        L_0x0010:
            ohos.hiviewdfx.HiLogLabel r2 = ohos.hiviewdfx.HiChecker.LOG_LABEL     // Catch:{ all -> 0x000e }
            java.lang.String r3 = "HiChecker notification failed"
            r1 = 0
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x000e }
            ohos.hiviewdfx.HiLog.info(r2, r3, r1)     // Catch:{ all -> 0x000e }
            goto L_0x000a
        L_0x001b:
            return
        L_0x001c:
            addRule(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.hiviewdfx.HiChecker.lambda$notifyCustomListener$0(ohos.hiviewdfx.HiChecker$OnCautionListener, ohos.hiviewdfx.Caution):void");
    }
}
