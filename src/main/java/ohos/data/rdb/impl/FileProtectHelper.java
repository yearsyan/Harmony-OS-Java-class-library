package ohos.data.rdb.impl;

import java.io.IOException;
import ohos.app.Context;
import ohos.data.DatabaseFileSecurityLevel;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.security.fileprotect.ISfpPolicyManager;
import ohos.security.fileprotect.SfpPolicyManager;

/* access modifiers changed from: package-private */
public class FileProtectHelper {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218109520, "FileProtectHelper");

    FileProtectHelper() {
    }

    static boolean isValid(Context context, DatabaseFileSecurityLevel databaseFileSecurityLevel) {
        boolean z;
        boolean z2 = true;
        if (databaseFileSecurityLevel == null || databaseFileSecurityLevel == DatabaseFileSecurityLevel.NO_LEVEL) {
            return true;
        }
        if (context.isCredentialEncryptedStorage()) {
            HiLog.debug(LABEL, "Checking for level %{public}s with ce mode.", databaseFileSecurityLevel);
            z = (databaseFileSecurityLevel == DatabaseFileSecurityLevel.S4) | false | (databaseFileSecurityLevel == DatabaseFileSecurityLevel.S3) | (databaseFileSecurityLevel == DatabaseFileSecurityLevel.S2);
        } else {
            z = false;
        }
        if (!context.isDeviceEncryptedStorage()) {
            return z;
        }
        HiLog.debug(LABEL, "Checking for level %{public}s with de mode.", databaseFileSecurityLevel);
        boolean z3 = (databaseFileSecurityLevel == DatabaseFileSecurityLevel.S1) | z;
        if (databaseFileSecurityLevel != DatabaseFileSecurityLevel.S0) {
            z2 = false;
        }
        return z3 | z2;
    }

    static void setPolicy(Context context, String str, DatabaseFileSecurityLevel databaseFileSecurityLevel) {
        if (databaseFileSecurityLevel != null && databaseFileSecurityLevel != DatabaseFileSecurityLevel.NO_LEVEL) {
            if (databaseFileSecurityLevel == DatabaseFileSecurityLevel.S4 || databaseFileSecurityLevel == DatabaseFileSecurityLevel.S3) {
                try {
                    SfpPolicyManager.getInstance().setSecePolicy(context, getDataUserPath(str));
                } catch (IOException | IllegalAccessException unused) {
                    throw new IllegalStateException("The security level does not take effect for the database directory.");
                }
            }
        }
    }

    private static String getDataUserPath(String str) {
        if (!str.startsWith("/data/data")) {
            return str;
        }
        return str.replaceFirst("/data/data", "/data/user/0");
    }

    private static int getLabelFlag(DatabaseFileSecurityLevel databaseFileSecurityLevel) {
        return databaseFileSecurityLevel == DatabaseFileSecurityLevel.S3 ? 1 : 0;
    }

    static void setFileLabel(Context context, String str, DatabaseFileSecurityLevel databaseFileSecurityLevel) {
        if (databaseFileSecurityLevel != null && databaseFileSecurityLevel != DatabaseFileSecurityLevel.NO_LEVEL) {
            String labelValue = getLabelValue(databaseFileSecurityLevel);
            String dataUserPath = getDataUserPath(str);
            ISfpPolicyManager instance = SfpPolicyManager.getInstance();
            if (!instance.getLabel(context, dataUserPath, SfpPolicyManager.LABEL_NAME_SECURITY_LEVEL).equals(labelValue)) {
                int labelFlag = getLabelFlag(databaseFileSecurityLevel);
                HiLog.debug(LABEL, "Set label with [dataUserPath=%{private}s, labelName=%{public}s, labelValue=%{public}s].", dataUserPath, SfpPolicyManager.LABEL_NAME_SECURITY_LEVEL, labelValue);
                if (instance.setLabel(context, dataUserPath, SfpPolicyManager.LABEL_NAME_SECURITY_LEVEL, labelValue, labelFlag) != 0) {
                    throw new IllegalArgumentException("The security level does not take effect for the database file.");
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.data.rdb.impl.FileProtectHelper$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$data$DatabaseFileSecurityLevel = new int[DatabaseFileSecurityLevel.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            /*
                ohos.data.DatabaseFileSecurityLevel[] r0 = ohos.data.DatabaseFileSecurityLevel.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.data.rdb.impl.FileProtectHelper.AnonymousClass1.$SwitchMap$ohos$data$DatabaseFileSecurityLevel = r0
                int[] r0 = ohos.data.rdb.impl.FileProtectHelper.AnonymousClass1.$SwitchMap$ohos$data$DatabaseFileSecurityLevel     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.data.DatabaseFileSecurityLevel r1 = ohos.data.DatabaseFileSecurityLevel.S4     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.data.rdb.impl.FileProtectHelper.AnonymousClass1.$SwitchMap$ohos$data$DatabaseFileSecurityLevel     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.data.DatabaseFileSecurityLevel r1 = ohos.data.DatabaseFileSecurityLevel.S3     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.data.rdb.impl.FileProtectHelper.AnonymousClass1.$SwitchMap$ohos$data$DatabaseFileSecurityLevel     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.data.DatabaseFileSecurityLevel r1 = ohos.data.DatabaseFileSecurityLevel.S2     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.data.rdb.impl.FileProtectHelper.AnonymousClass1.$SwitchMap$ohos$data$DatabaseFileSecurityLevel     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.data.DatabaseFileSecurityLevel r1 = ohos.data.DatabaseFileSecurityLevel.S1     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = ohos.data.rdb.impl.FileProtectHelper.AnonymousClass1.$SwitchMap$ohos$data$DatabaseFileSecurityLevel     // Catch:{ NoSuchFieldError -> 0x0040 }
                ohos.data.DatabaseFileSecurityLevel r1 = ohos.data.DatabaseFileSecurityLevel.S0     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.data.rdb.impl.FileProtectHelper.AnonymousClass1.<clinit>():void");
        }
    }

    private static String getLabelValue(DatabaseFileSecurityLevel databaseFileSecurityLevel) {
        int i = AnonymousClass1.$SwitchMap$ohos$data$DatabaseFileSecurityLevel[databaseFileSecurityLevel.ordinal()];
        if (i == 1) {
            return SfpPolicyManager.LABEL_VALUE_S4;
        }
        if (i == 2) {
            return SfpPolicyManager.LABEL_VALUE_S3;
        }
        if (i == 3) {
            return SfpPolicyManager.LABEL_VALUE_S2;
        }
        if (i == 4) {
            return SfpPolicyManager.LABEL_VALUE_S1;
        }
        if (i == 5) {
            return SfpPolicyManager.LABEL_VALUE_S0;
        }
        throw new IllegalStateException("setDbFileLabel unSupport db risk level.");
    }

    static String getPrefix(DatabaseFileSecurityLevel databaseFileSecurityLevel) {
        return (databaseFileSecurityLevel == DatabaseFileSecurityLevel.S4 || databaseFileSecurityLevel == DatabaseFileSecurityLevel.S3) ? "sece_" : "";
    }
}
