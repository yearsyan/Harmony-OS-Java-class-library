package ohos.abilityshell.support;

import java.util.List;
import ohos.aafwk.content.Intent;
import ohos.abilityshell.BundleMgrBridge;
import ohos.abilityshell.utils.AbilityShellConverterUtils;
import ohos.bundle.AbilityInfo;
import ohos.bundle.InstallerCallback;
import ohos.bundle.ShellInfo;

public class AbilityUtilsHelper {
    private static final AbilityUtilsImpl SERVICE = new AbilityUtilsImpl();
    private static BundleMgrBridge bundleMgrBridge = new BundleMgrBridge();

    public static IAbilityUtils getService() {
        return SERVICE;
    }

    private AbilityUtilsHelper() {
    }

    public static ShellInfo getAbilityShellInfo(String str, String str2) throws IllegalArgumentException {
        return AbilityShellConverterUtils.convertToShellInfo(bundleMgrBridge.getAbilityInfo(str, str2));
    }

    public static List<AbilityInfo> queryAbilityByIntent(Intent intent) {
        return bundleMgrBridge.queryAbilityByIntent(intent);
    }

    public static boolean silentInstall(Intent intent, InstallerCallback installerCallback) {
        return bundleMgrBridge.silentInstall(intent, installerCallback);
    }

    public static int getModuleUpgradeFlag(String str, String str2) {
        return bundleMgrBridge.getModuleUpgradeFlag(str, str2);
    }

    public static boolean upgradeCheck(Intent intent, InstallerCallback installerCallback) {
        return bundleMgrBridge.upgradeCheck(intent, installerCallback);
    }

    public static boolean upgradeInstall(Intent intent, int i, InstallerCallback installerCallback) {
        return bundleMgrBridge.upgradeInstall(intent, i, installerCallback);
    }
}
