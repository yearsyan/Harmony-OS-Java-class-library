package ohos.abilityshell;

import java.util.Collections;
import java.util.List;
import ohos.aafwk.content.Intent;
import ohos.appexecfwk.utils.AppLog;
import ohos.bundle.AbilityInfo;
import ohos.bundle.ApplicationInfo;
import ohos.bundle.BundleInfo;
import ohos.bundle.BundleManager;
import ohos.bundle.IBundleManager;
import ohos.bundle.InstallerCallback;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.sysability.samgr.SysAbilityManager;

public class BundleMgrBridge {
    private static final HiLogLabel SHELL_LABEL = new HiLogLabel(3, 218108160, "AbilityShell");
    private static IBundleManager bundleMgrProxy = null;
    private static SysAbilityFactory sysAbilityFactory = new DefaultSysAbilityFactory();

    public static void setSysAbilityFactory(SysAbilityFactory sysAbilityFactory2) {
        sysAbilityFactory = sysAbilityFactory2;
    }

    public static void setBundleMgrProxy(IBundleManager iBundleManager) {
        bundleMgrProxy = iBundleManager;
    }

    public static SysAbilityFactory getSysAbilityFactory() {
        SysAbilityFactory sysAbilityFactory2 = sysAbilityFactory;
        return sysAbilityFactory2 == null ? new DefaultSysAbilityFactory() : sysAbilityFactory2;
    }

    private boolean initBundleMgrProxy() {
        synchronized (BundleMgrBridge.class) {
            if (bundleMgrProxy == null) {
                bundleMgrProxy = BundleManager.getInstance();
                if (bundleMgrProxy == null) {
                    return false;
                }
            }
            return true;
        }
    }

    public List<AbilityInfo> queryAbilityByIntent(Intent intent) {
        List<AbilityInfo> emptyList = Collections.emptyList();
        if (intent == null) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::queryAbilityByIntent intent param invalid", new Object[0]);
            return emptyList;
        } else if (!initBundleMgrProxy()) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::queryAbilityByIntent get proxy failed", new Object[0]);
            return emptyList;
        } else {
            try {
                return bundleMgrProxy.queryAbilityByIntent(intent);
            } catch (RemoteException e) {
                AppLog.e(SHELL_LABEL, "BundleMgrBridge::queryAbilityByIntent get data failed:%{public}s", e.getMessage());
                return emptyList;
            }
        }
    }

    public BundleInfo getBundleInfo(String str, int i) {
        if (!initBundleMgrProxy()) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::getBundleInfo get proxy failed", new Object[0]);
            return null;
        }
        try {
            AppLog.i(SHELL_LABEL, "start to get bundle info from bms.", new Object[0]);
            return bundleMgrProxy.getBundleInfo(str, i);
        } catch (RemoteException e) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::getBundleInfo get data failed:%{public}s", e.getMessage());
            return null;
        } catch (SecurityException e2) {
            AppLog.i(SHELL_LABEL, "BundleMgrBridge::getBundleInfo failed: %{public}s", e2.getMessage());
            return null;
        }
    }

    public boolean silentInstall(Intent intent, InstallerCallback installerCallback) {
        if (!initBundleMgrProxy()) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::silentInstall get proxy failed", new Object[0]);
            return false;
        }
        try {
            AppLog.i(SHELL_LABEL, "start to silent install bundle from bms.", new Object[0]);
            return bundleMgrProxy.silentInstall(intent, installerCallback);
        } catch (RemoteException e) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::silentInstall get data failed:%{public}s", e.getMessage());
            return false;
        } catch (SecurityException e2) {
            AppLog.i(SHELL_LABEL, "BundleMgrBridge::silentInstall failed: %{public}s", e2.getMessage());
            return false;
        }
    }

    public int getModuleUpgradeFlag(String str, String str2) {
        if (!initBundleMgrProxy()) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::getModuleUpgradeFlag get proxy failed", new Object[0]);
            return -1;
        }
        try {
            AppLog.i(SHELL_LABEL, "start to get module upgrade flag from bms.", new Object[0]);
            return bundleMgrProxy.getModuleUpgradeFlag(str, str2);
        } catch (RemoteException e) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::getModuleUpgradeFlag failed:%{public}s", e.getMessage());
            return -1;
        } catch (SecurityException e2) {
            AppLog.i(SHELL_LABEL, "BundleMgrBridge::getModuleUpgradeFlag failed: %{public}s", e2.getMessage());
            return -1;
        }
    }

    public boolean upgradeCheck(Intent intent, InstallerCallback installerCallback) {
        if (!initBundleMgrProxy()) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::upgradeCheck get proxy failed", new Object[0]);
            return false;
        }
        try {
            AppLog.i(SHELL_LABEL, "start to do upgrade check from bms.", new Object[0]);
            return bundleMgrProxy.upgradeCheck(intent, installerCallback);
        } catch (RemoteException e) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::upgradeCheck failed:%{public}s", e.getMessage());
            return false;
        } catch (SecurityException e2) {
            AppLog.i(SHELL_LABEL, "BundleMgrBridge::upgradeCheck failed: %{public}s", e2.getMessage());
            return false;
        }
    }

    public boolean upgradeInstall(Intent intent, int i, InstallerCallback installerCallback) {
        if (!initBundleMgrProxy()) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::upgradeInstall get proxy failed", new Object[0]);
            return false;
        }
        try {
            AppLog.i(SHELL_LABEL, "start to do upgrade install bundle from bms.", new Object[0]);
            return bundleMgrProxy.upgradeInstall(intent, i, installerCallback);
        } catch (RemoteException e) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::upgradeInstall failed:%{public}s", e.getMessage());
            return false;
        } catch (SecurityException e2) {
            AppLog.i(SHELL_LABEL, "BundleMgrBridge::upgradeInstall failed: %{public}s", e2.getMessage());
            return false;
        }
    }

    public List<String> getModuleSourceDirs(String str, int i) {
        BundleInfo bundleInfo = getBundleInfo(str, i);
        if (bundleInfo == null) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::getModuleSourceDirs failed, bundleInfo is null", new Object[0]);
            return Collections.emptyList();
        }
        ApplicationInfo appInfo = bundleInfo.getAppInfo();
        if (appInfo != null) {
            return appInfo.getModuleSourceDirs();
        }
        AppLog.e(SHELL_LABEL, "HarmonyApplication::setResources failed, applicationInfo is null", new Object[0]);
        return Collections.emptyList();
    }

    public AbilityInfo getAbilityInfo(String str, String str2) {
        if (str == null || str2 == null) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::getAbilityInfo param invalid", new Object[0]);
            return null;
        } else if (!initBundleMgrProxy()) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::getAbilityInfo get proxy failed", new Object[0]);
            return null;
        } else {
            try {
                AbilityInfo abilityInfo = bundleMgrProxy.getAbilityInfo(str, str2);
                if (abilityInfo != null) {
                    AppLog.i(SHELL_LABEL, "BundleMgrBridge::getAbilityInfo bundleName: %{public}s, className: %{public}s", abilityInfo.getBundleName(), abilityInfo.getClassName());
                }
                return abilityInfo;
            } catch (RemoteException e) {
                AppLog.e(SHELL_LABEL, "BundleMgrBridge::getAbilityInfo get data failed:%{public}s", e.getMessage());
                return null;
            }
        }
    }

    public ApplicationInfo getApplicationInfo(String str, int i, int i2) {
        if (str == null) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::getApplicationInfo param invalid", new Object[0]);
            return null;
        } else if (!initBundleMgrProxy()) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::getApplicationInfo get proxy failed", new Object[0]);
            return null;
        } else {
            try {
                return bundleMgrProxy.getApplicationInfo(str, i, i2);
            } catch (RemoteException unused) {
                AppLog.e(SHELL_LABEL, "BundleMgrBridge::getAbilityInfo get data failed", new Object[0]);
                return null;
            }
        }
    }

    public BundleInfo attachApplication(String str, IRemoteObject iRemoteObject) {
        if (str == null || str.isEmpty()) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::attachApplication param invalid", new Object[0]);
            return null;
        } else if (!initBundleMgrProxy()) {
            AppLog.e(SHELL_LABEL, "BundleMgrBridge::attachApplication get proxy failed", new Object[0]);
            return null;
        } else {
            try {
                return bundleMgrProxy.attachApplication(str, iRemoteObject);
            } catch (RemoteException unused) {
                AppLog.e(SHELL_LABEL, "BundleMgrBridge::attachApplication get data failed", new Object[0]);
                return null;
            }
        }
    }

    private static class DefaultSysAbilityFactory implements SysAbilityFactory {
        private DefaultSysAbilityFactory() {
        }

        @Override // ohos.abilityshell.SysAbilityFactory
        public IRemoteObject getSysAbility(int i) {
            return SysAbilityManager.getSysAbility(i);
        }
    }
}
