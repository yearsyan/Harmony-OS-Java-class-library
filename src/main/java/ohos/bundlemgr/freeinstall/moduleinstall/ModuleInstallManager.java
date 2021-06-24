package ohos.bundlemgr.freeinstall.moduleinstall;

import android.content.Context;
import com.huawei.ohos.interwork.AbilityUtils;
import ohos.aafwk.content.Intent;
import ohos.appexecfwk.utils.AppLog;
import ohos.bundle.ProfileConstants;
import ohos.bundlemgr.BundleMgrAdapter;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;
import ohos.utils.fastjson.JSONObject;

public class ModuleInstallManager {
    private static final HiLogLabel BMS_ADAPTER_LABEL = new HiLogLabel(3, 218108160, "ModuleInstallManager");
    private static final int FREE_INSTALL_ERROR_CODE_FA_DISPATCHER_FAILED = 5;
    private static volatile ModuleInstallManager instance;
    private Context mContext;
    private IModuleInstallOperator moduleInstallOperatorProxy;

    private ModuleInstallManager(Context context) {
        this.mContext = context;
        this.moduleInstallOperatorProxy = new ModuleInstallOperatorProxy(context);
    }

    public static ModuleInstallManager getInstance(Context context) {
        if (instance == null) {
            synchronized (ModuleInstallManager.class) {
                if (instance == null) {
                    instance = new ModuleInstallManager(context);
                }
            }
        }
        return instance;
    }

    public boolean callFaDispatcher(String str) {
        JSONObject parseObject = JSONObject.parseObject(str);
        if (parseObject == null) {
            return false;
        }
        String string = parseObject.getString("method");
        char c = 65535;
        int hashCode = string.hashCode();
        if (hashCode != -1363129268) {
            if (hashCode != 495226758) {
                if (hashCode == 1213058015 && string.equals("upgradeInstall")) {
                    c = 1;
                }
            } else if (string.equals("silentInstall")) {
                c = 0;
            }
        } else if (string.equals("upgradeCheck")) {
            c = 2;
        }
        if (c == 0) {
            return silentInstall(parseObject);
        }
        if (c == 1) {
            return upgradeInstall(parseObject);
        }
        if (c != 2) {
            return false;
        }
        return upgradeCheck(parseObject);
    }

    public boolean freeInstallAbility(String str) {
        JSONObject parseObject = JSONObject.parseObject(str);
        if (parseObject == null) {
            return false;
        }
        MessageParcel obtain = MessageParcel.obtain(parseObject.getLong("freeInstallParam").longValue());
        try {
            Intent intent = new Intent();
            if (!obtain.readSequenceable(intent)) {
                AppLog.e(BMS_ADAPTER_LABEL, "readSequenceable intent failed", new Object[0]);
                return false;
            }
            boolean freeInstallWithCallback = AbilityUtils.freeInstallWithCallback(this.mContext, intent, obtain.readRemoteObject());
            obtain.reclaim();
            return freeInstallWithCallback;
        } finally {
            obtain.reclaim();
        }
    }

    private boolean silentInstall(JSONObject jSONObject) {
        AppLog.d(BMS_ADAPTER_LABEL, "silentInstall start", new Object[0]);
        String string = jSONObject.getString(ProfileConstants.BUNDLE_NAME);
        String string2 = jSONObject.getString("abilityName");
        Integer integer = jSONObject.getInteger("flags");
        MessageParcel obtain = MessageParcel.obtain(jSONObject.getLong("statusReceiver").longValue());
        try {
            IRemoteObject readRemoteObject = obtain.readRemoteObject();
            if (!(string == null || string2 == null || integer == null)) {
                if (readRemoteObject != null) {
                    boolean silentInstall = this.moduleInstallOperatorProxy.silentInstall(string, string2, integer.intValue(), readRemoteObject);
                    if (!silentInstall) {
                        BundleMgrAdapter.showErrorMessage(5);
                    }
                    obtain.reclaim();
                    return silentInstall;
                }
            }
            AppLog.e(BMS_ADAPTER_LABEL, "silentInstall param error", new Object[0]);
            return false;
        } finally {
            obtain.reclaim();
        }
    }

    private boolean upgradeInstall(JSONObject jSONObject) {
        AppLog.d(BMS_ADAPTER_LABEL, "upgradeInstall start", new Object[0]);
        String string = jSONObject.getString(ProfileConstants.BUNDLE_NAME);
        String string2 = jSONObject.getString("moduleName");
        Integer integer = jSONObject.getInteger("flags");
        Integer integer2 = jSONObject.getInteger("reasonFlag");
        MessageParcel obtain = MessageParcel.obtain(jSONObject.getLong("statusReceiver").longValue());
        try {
            IRemoteObject readRemoteObject = obtain.readRemoteObject();
            if (!(string == null || string2 == null || integer == null || integer2 == null)) {
                if (readRemoteObject != null) {
                    boolean upgradeInstall = this.moduleInstallOperatorProxy.upgradeInstall(string, string2, integer.intValue(), integer2.intValue(), readRemoteObject);
                    obtain.reclaim();
                    return upgradeInstall;
                }
            }
            AppLog.e(BMS_ADAPTER_LABEL, "upgradeInstall param error", new Object[0]);
            return false;
        } finally {
            obtain.reclaim();
        }
    }

    private boolean upgradeCheck(JSONObject jSONObject) {
        AppLog.d(BMS_ADAPTER_LABEL, "upgradeCheck start", new Object[0]);
        String string = jSONObject.getString(ProfileConstants.BUNDLE_NAME);
        String string2 = jSONObject.getString("moduleName");
        Integer integer = jSONObject.getInteger("flags");
        MessageParcel obtain = MessageParcel.obtain(jSONObject.getLong("statusReceiver").longValue());
        try {
            IRemoteObject readRemoteObject = obtain.readRemoteObject();
            if (!(string == null || string2 == null || integer == null)) {
                if (readRemoteObject != null) {
                    boolean upgradeCheck = this.moduleInstallOperatorProxy.upgradeCheck(string, string2, integer.intValue(), readRemoteObject);
                    obtain.reclaim();
                    return upgradeCheck;
                }
            }
            AppLog.e(BMS_ADAPTER_LABEL, "upgradeCheck param error", new Object[0]);
            return false;
        } finally {
            obtain.reclaim();
        }
    }

    public String queryAbility(String str, String str2) {
        if (str != null && !str.isEmpty() && str2 != null && !str2.isEmpty()) {
            return this.moduleInstallOperatorProxy.queryAbility(str, str2);
        }
        AppLog.e(BMS_ADAPTER_LABEL, "queryAbility param empty", new Object[0]);
        return "";
    }
}
