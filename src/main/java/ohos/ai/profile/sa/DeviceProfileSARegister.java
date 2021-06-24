package ohos.ai.profile.sa;

import android.content.Context;
import ohos.sysability.samgr.SysAbilityManager;

public abstract class DeviceProfileSARegister {
    private static final int SYSTEM_ABILITY_ID = 301;

    public static void init(Context context) {
        if (context != null) {
            SysAbilityManager.addSysAbility(301, DeviceProfileSystemAbility.getInstance(context).asObject());
        }
    }
}
