package ohos.data;

import ohos.security.fileprotect.SfpPolicyManager;

public enum DatabaseFileSecurityLevel {
    S4(SfpPolicyManager.LABEL_VALUE_S4),
    S3(SfpPolicyManager.LABEL_VALUE_S3),
    S2(SfpPolicyManager.LABEL_VALUE_S2),
    S1(SfpPolicyManager.LABEL_VALUE_S1),
    S0(SfpPolicyManager.LABEL_VALUE_S0),
    NO_LEVEL("NO_LEVEL");
    
    private String value;

    private DatabaseFileSecurityLevel(String str) {
        this.value = str;
    }

    public String getValue() {
        return this.value;
    }
}
