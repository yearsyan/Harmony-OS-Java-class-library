package ohos.security.fileprotect;

import com.huawei.fileprotect.HwSfpPolicyManager;
import java.io.IOException;
import ohos.annotation.SystemApi;
import ohos.app.Context;
import ohos.sysability.samgr.SysAbilityManager;

public class SfpPolicyManager {
    public static final int ERROR_CODE_CE_DE_CONTEXT = 3;
    public static final int ERROR_CODE_FAILED = 4;
    public static final int ERROR_CODE_INVALID_PARAM = 1;
    public static final int ERROR_CODE_LABEL_HAS_BEEN_SET = 2;
    public static final int ERROR_CODE_OK = 0;
    public static final int FLAG_FILE_PROTECTION_COMPLETE = 0;
    public static final int FLAG_FILE_PROTECTION_COMPLETE_UNLESS_OPEN = 1;
    public static final String LABEL_NAME_SECURITY_LEVEL = "SecurityLevel";
    public static final String LABEL_VALUE_S0 = "S0";
    public static final String LABEL_VALUE_S1 = "S1";
    public static final String LABEL_VALUE_S2 = "S2";
    public static final String LABEL_VALUE_S3 = "S3";
    public static final String LABEL_VALUE_S4 = "S4";
    private static final int SA_ID_FILEPROTECT_SERVICE = 3599;
    private static SfpPolicyManagerSingleton sSfpPolicyManager;

    private SfpPolicyManager() {
    }

    public static synchronized ISfpPolicyManager getInstance() {
        SfpPolicyManagerSingleton sfpPolicyManagerSingleton;
        synchronized (SfpPolicyManager.class) {
            if (sSfpPolicyManager == null) {
                sSfpPolicyManager = new SfpPolicyManagerSingleton();
            }
            sfpPolicyManagerSingleton = sSfpPolicyManager;
        }
        return sfpPolicyManagerSingleton;
    }

    @SystemApi
    public static final boolean isSupportIudf() {
        return HwSfpPolicyManager.isSupportIudf();
    }

    private static class SfpPolicyManagerSingleton implements ISfpPolicyManager {
        private ISfpPolicyProxy mProxy = new SfpPolicyProxy(SysAbilityManager.getSysAbility(SfpPolicyManager.SA_ID_FILEPROTECT_SERVICE));

        SfpPolicyManagerSingleton() {
        }

        @Override // ohos.security.fileprotect.ISfpPolicyManager
        public void setEcePolicy(Context context, String str) throws IllegalArgumentException, IllegalStateException, IllegalAccessException, IOException {
            this.mProxy.setEcePolicy(context, str);
        }

        @Override // ohos.security.fileprotect.ISfpPolicyManager
        public void setSecePolicy(Context context, String str) throws IllegalArgumentException, IllegalStateException, IllegalAccessException, IOException {
            this.mProxy.setSecePolicy(context, str);
        }

        @Override // ohos.security.fileprotect.ISfpPolicyManager
        public int getPolicyProtectType(Context context, String str) throws IllegalArgumentException, IllegalStateException, IllegalAccessException {
            return this.mProxy.getPolicyProtectType(context, str);
        }

        @Override // ohos.security.fileprotect.ISfpPolicyManager
        public int setLabel(Context context, String str, String str2, String str3, int i) {
            return this.mProxy.setLabel(context, str, str2, str3, i);
        }

        @Override // ohos.security.fileprotect.ISfpPolicyManager
        public String getLabel(Context context, String str, String str2) {
            return this.mProxy.getLabel(context, str, str2);
        }

        @Override // ohos.security.fileprotect.ISfpPolicyManager
        public int getFlag(Context context, String str, String str2) {
            return this.mProxy.getFlag(context, str, str2);
        }
    }
}
