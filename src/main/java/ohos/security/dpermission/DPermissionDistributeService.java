package ohos.security.dpermission;

import android.content.pm.IPackageManager;
import android.content.pm.PermissionInfo;
import android.os.ServiceManager;
import com.huawei.security.dpermission.DistributedPermissionManager;
import ohos.bundle.BundleManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.security.dpermission.IDPermissionDistributeService;
import ohos.security.permission.adapter.PermissionReasonAdapter;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;
import ohos.sysability.samgr.SysAbilityManager;

public class DPermissionDistributeService extends IDPermissionDistributeService.Stub {
    private static final int GENERAL_ERROR = -1;
    private static final HiLogLabel LABEL = HiLogLabelUtil.SERVICE.newHiLogLabel(TAG);
    private static final Object LOCK = new Object();
    private static final int SA_ID = 3502;
    private static final String TAG = "DPermissionDistributeService";
    private volatile boolean hasSuccessfullyAddedSystemAbility;

    private DPermissionDistributeService() {
        this.hasSuccessfullyAddedSystemAbility = false;
    }

    public static DPermissionDistributeService getInstance() {
        return SingletonClassHolder.INSTANCE;
    }

    public void initialize() {
        HiLogLabel hiLogLabel = LABEL;
        boolean z = true;
        Integer valueOf = Integer.valueOf((int) SA_ID);
        HiLog.info(hiLogLabel, "addSysAbility %{public}d start!", valueOf);
        synchronized (LOCK) {
            if (this.hasSuccessfullyAddedSystemAbility) {
                HiLog.info(LABEL, "addSysAbility %{public}d has been added!", valueOf);
            } else if (SysAbilityManager.getSysAbility(SA_ID) != null) {
                this.hasSuccessfullyAddedSystemAbility = true;
                HiLog.info(LABEL, "addSysAbility %{public}d has been added, we can get!", valueOf);
            } else {
                int addSysAbility = SysAbilityManager.addSysAbility(SA_ID, asObject(), true, 1);
                HiLog.info(LABEL, "addSysAbility done, result: %{public}d!", Integer.valueOf(addSysAbility));
                if (addSysAbility != 0) {
                    z = false;
                }
                this.hasSuccessfullyAddedSystemAbility = z;
            }
        }
    }

    @Override // ohos.security.dpermission.IDPermissionDistributeService
    public String executeRemoteCommand(String str, String str2) {
        HiLog.info(LABEL, "executeRemoteCommand: called %{public}s, %{private}s", str, str2);
        if (str != null && str2 != null) {
            return DistributedPermissionManager.getDefault().processZ2aMessage(str, str2);
        }
        HiLog.error(LABEL, "executeRemoteCommand: invalid param", new Object[0]);
        return "";
    }

    @Override // ohos.security.dpermission.IDPermissionDistributeService
    public String executeRemoteCommandBacktrack(String str, String str2, String str3) {
        HiLog.info(LABEL, "executeRemoteCommandBacktrack: called %{private}s, %{public}s, %{private}s", str, str2, str3);
        if (str == null || str2 == null || str3 == null) {
            HiLog.error(LABEL, "executeRemoteCommandBacktrack: invalid param", new Object[0]);
            return "";
        }
        IRemoteObject sysAbility = SysAbilityManager.getSysAbility(SA_ID, str);
        if (sysAbility == null) {
            HiLog.error(LABEL, "executeRemoteCommandBacktrack: sa3502 got null, return empty string", new Object[0]);
            return "";
        }
        try {
            return IDPermissionDistributeService.Stub.asInterface(sysAbility).executeRemoteCommand(str2, str3);
        } catch (RemoteException e) {
            HiLog.error(LABEL, "executeRemoteCommandBacktrack: error, %{private}s", e.getMessage());
            return "";
        }
    }

    @Override // ohos.security.dpermission.IDPermissionDistributeService
    public boolean canRequestPermission(String str, String str2, int i) {
        HiLog.debug(LABEL, "canRequestPermission: called %{public}s, %{public}s, %{public}s", str, str2, Integer.valueOf(i));
        try {
            IPackageManager asInterface = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
            if (asInterface == null) {
                HiLog.error(LABEL, "canRequestPermission packageManager is null", new Object[0]);
                return false;
            }
            PermissionInfo permissionInfo = asInterface.getPermissionInfo(str, str2, 0);
            return permissionInfo != null && permissionInfo.isRuntime() && asInterface.checkPermission(str, str2, i) != 0 && (asInterface.getPermissionFlags(str, str2, i) & 22) == 0;
        } catch (android.os.RemoteException unused) {
            HiLog.error(LABEL, "failed to canRequestPermission RemoteException", new Object[0]);
            return false;
        }
    }

    @Override // ohos.security.dpermission.IDPermissionDistributeService
    public int notifyUidPermissionChanged(int i) {
        HiLog.info(LABEL, "notifyUidPermissionChanged: called %{public}d", Integer.valueOf(i));
        try {
            BundleManager instance = BundleManager.getInstance();
            if (instance == null) {
                return -1;
            }
            instance.notifyPermissionsChanged(i);
            return 0;
        } catch (RemoteException e) {
            HiLog.error(LABEL, "notifyUidPermissionChanged: error, %{private}s", e.getMessage());
            return -1;
        }
    }

    @Override // ohos.security.dpermission.IDPermissionDistributeService
    public String getPermissionUsagesInfo(String str, String[] strArr) {
        return new PermissionReasonAdapter().getPermissionUsagesInfo(str, strArr).orElse(null);
    }

    /* access modifiers changed from: private */
    public static final class SingletonClassHolder {
        private static final DPermissionDistributeService INSTANCE = new DPermissionDistributeService();

        private SingletonClassHolder() {
        }
    }
}
