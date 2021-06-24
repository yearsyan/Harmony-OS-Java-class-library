package ohos.security.permission.infrastructure.utils;

import ohos.hiviewdfx.HiLogLabel;

public enum HiLogLabelUtil {
    KIT("KIT"),
    INNER_KIT("INNER_KIT"),
    ADAPTER("ADAPTER"),
    SERVICE("SERVICE"),
    INFRA("INFRA");
    
    public static final String MODULE_NAME = "DPMS";
    public static final int OPEN_DISTRIBUTED_PERMISSION_MANAGER_SERVICE_DOMAIN_ID = 218115841;
    private final String partName;

    private HiLogLabelUtil(String str) {
        this.partName = str;
    }

    public HiLogLabel newHiLogLabel(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("DPMS_");
        sb.append(this.partName);
        sb.append("_");
        String str2 = "";
        if (str != null) {
            str2 = str.replaceAll("[a-z]", str2);
        }
        sb.append(str2);
        return new HiLogLabel(3, OPEN_DISTRIBUTED_PERMISSION_MANAGER_SERVICE_DOMAIN_ID, sb.toString());
    }
}
