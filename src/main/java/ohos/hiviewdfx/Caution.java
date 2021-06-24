package ohos.hiviewdfx;

import ohos.hiviewdfx.HiChecker;

public final class Caution extends Throwable {
    private static final long serialVersionUID = -6765376978657331746L;
    private final String customMessage;
    private HiChecker.Rule triggerRule;

    protected Caution(HiChecker.Rule rule, String str) {
        this(rule, str, null);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected Caution(ohos.hiviewdfx.HiChecker.Rule r4, java.lang.String r5, java.lang.Throwable r6) {
        /*
            r3 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "trigger:Rule."
            r0.append(r1)
            r0.append(r4)
            if (r5 != 0) goto L_0x0012
            java.lang.String r1 = ""
            goto L_0x0023
        L_0x0012:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = ", msg:"
            r1.append(r2)
            r1.append(r5)
            java.lang.String r1 = r1.toString()
        L_0x0023:
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r3.<init>(r0)
            r0 = 0
            r3.triggerRule = r0
            r3.triggerRule = r4
            r3.customMessage = r5
            r3.initCause(r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.hiviewdfx.Caution.<init>(ohos.hiviewdfx.HiChecker$Rule, java.lang.String, java.lang.Throwable):void");
    }

    public HiChecker.Rule getTriggerRule() {
        return this.triggerRule;
    }

    public String getCustomMessage() {
        return this.customMessage;
    }
}
