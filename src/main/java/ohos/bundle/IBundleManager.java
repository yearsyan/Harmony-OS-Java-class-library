package ohos.bundle;

import java.util.List;
import java.util.Optional;
import ohos.aafwk.content.Intent;
import ohos.annotation.SystemApi;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.security.permission.PermissionDef;
import ohos.security.permission.PermissionGroupDef;

public interface IBundleManager extends IRemoteBroker {
    public static final String DESCRIPTOR = "OHOS.AppExecFwk.IBundleMgr";
    public static final int ERROR_CODE_DOWNLOAD_FAILED = 2;
    public static final int ERROR_CODE_INSTALL_FAILED = 3;
    public static final int ERROR_CODE_QUERY_FAILED = 1;
    public static final int FLAG_MODULE_NOT_USED_BY_FORM = 0;
    public static final int FLAG_MODULE_NOT_USED_BY_SHORTCUT = 2;
    public static final int FLAG_MODULE_UPGRADE_CHECK = 0;
    public static final int FLAG_MODULE_UPGRADE_INSTALL = 1;
    public static final int FLAG_MODULE_UPGRADE_INSTALL_WITH_CONFIG_WINDOWS = 2;
    public static final int FLAG_MODULE_USED_BY_FORM = 1;
    public static final int FLAG_MODULE_USED_BY_SHORTCUT = 3;
    public static final int GET_ABILITY_INFO_WITH_APPLICATION = 4;
    public static final int GET_ABILITY_INFO_WITH_PERMISSION = 2;
    public static final int GET_ALL_APPLICATION_INFO = -65536;
    public static final int GET_APPLICATION_INFO_WITH_PERMISSION = 8;
    public static final int GET_BUNDLE_DEFAULT = 0;
    public static final int GET_BUNDLE_WITH_ABILITIES = 1;
    public static final int GET_BUNDLE_WITH_REQUESTED_PERMISSION = 16;
    public static final int PERMISSION_DENIED = -1;
    public static final int PERMISSION_GRANTED = 0;
    public static final int QUERY_SHORTCUT_HOME = 0;
    public static final int SHORTCUT_EXISTENCE_EXISTS = 0;
    public static final int SHORTCUT_EXISTENCE_NOT_EXISTS = 1;
    public static final int SHORTCUT_EXISTENCE_UNKNOW = 2;
    public static final int SIGNATURE_MATCHED = 0;
    public static final int SIGNATURE_NOT_MATCHED = 1;
    public static final int SIGNATURE_UNKNOWN_BUNDLE = 2;

    boolean addHomeShortcut(ShortcutInfo shortcutInfo) throws IllegalArgumentException, IllegalStateException;

    @SystemApi
    boolean addTargetBundleList(String str, List<String> list) throws RemoteException;

    @SystemApi
    boolean addUsageRecord(String str, String str2) throws RemoteException;

    BundleInfo attachApplication(String str, IRemoteObject iRemoteObject) throws RemoteException;

    boolean cancelDownload(AbilityInfo abilityInfo) throws RemoteException;

    int checkPermission(String str, String str2);

    int checkPublicKeys(String str, String str2) throws RemoteException;

    void cleanBundleCacheFiles(String str, ICleanCacheCallback iCleanCacheCallback);

    void disableHomeShortcuts(List<String> list) throws IllegalArgumentException;

    boolean downloadAndInstall(AbilityInfo abilityInfo, boolean z, InstallerCallback installerCallback) throws RemoteException;

    void enableHomeShortcuts(List<String> list) throws IllegalArgumentException;

    int executeBackupTask(int i, String str);

    int finishBackupSession(int i);

    AbilityInfo getAbilityByShell(ShellInfo shellInfo);

    PixelMap getAbilityIcon(String str, String str2) throws RemoteException;

    AbilityInfo getAbilityInfo(String str, String str2) throws RemoteException;

    String getAbilityLabel(String str, String str2) throws RemoteException;

    List<FormInfo> getAllFormsInfo() throws RemoteException;

    Optional<List<PermissionGroupDef>> getAllPermissionGroupDefs();

    String getAppType(String str) throws RemoteException;

    ApplicationInfo getApplicationInfo(String str, int i, int i2) throws RemoteException;

    List<ApplicationInfo> getApplicationInfos(int i, int i2) throws RemoteException;

    List<String> getAppsGrantedPermissions(String[] strArr) throws RemoteException;

    BundleInfo getBundleArchiveInfo(String str, int i);

    int[] getBundleGids(String str);

    BundleInfo getBundleInfo(String str, int i) throws RemoteException;

    BundleInfo getBundleInfo(String str, String str2, int i) throws RemoteException;

    List<BundleInfo> getBundleInfos(int i) throws RemoteException;

    IBundleInstaller getBundleInstaller() throws RemoteException;

    @SystemApi
    List<String> getBundleNames() throws RemoteException;

    @SystemApi
    BundlePackInfo getBundlePackInfo(String str, int i) throws RemoteException, IllegalArgumentException;

    List<String> getBundlesForUid(int i) throws RemoteException;

    boolean getClearUserDataFlag(String str, String str2);

    @SystemApi
    String getFormDescription(FormInfo formInfo) throws IllegalArgumentException;

    List<FormInfo> getFormsInfoByApp(String str) throws RemoteException;

    List<FormInfo> getFormsInfoByModule(String str, String str2) throws RemoteException;

    @SystemApi
    List<HapModuleInfo> getHapModuleInfos(int i, int i2) throws RemoteException;

    List<ShortcutInfo> getHomeShortcutInfos();

    Intent getLaunchIntentForBundle(String str) throws RemoteException;

    AbilityInfo getModuleMainAbility(String str, String str2) throws RemoteException;

    int getModuleUpgradeFlag(String str, String str2) throws RemoteException;

    @SystemApi
    List<ModuleUsageRecord> getModuleUsageRecords(int i) throws RemoteException, IllegalArgumentException;

    String getNameForUid(int i) throws RemoteException;

    PermissionDef getPermissionDef(String str);

    Optional<List<PermissionDef>> getPermissionDefByGroup(String str);

    Optional<PermissionGroupDef> getPermissionGroupDef(String str);

    @SystemApi
    PixelMap getPixelMapByResId(String str, int i);

    List<ShortcutInfo> getShortcutInfos(String str) throws RemoteException;

    @SystemApi
    String getStringByResId(String str, int i);

    List<String> getSystemAvailableCapabilities();

    int getUidByBundleName(String str, int i) throws RemoteException;

    boolean hasSystemCapability(String str);

    boolean isAbilityEnabled(AbilityInfo abilityInfo) throws IllegalArgumentException;

    boolean isApplicationEnabled(String str) throws IllegalArgumentException;

    boolean isHomeShortcutSupported();

    @SystemApi
    boolean isModuleRemovable(String str, String str2) throws RemoteException, SecurityException, IllegalArgumentException;

    boolean isSafeMode();

    int isShortcutExist(String str, int i);

    boolean isShortcutExist(String str);

    void notifyPermissionsChanged(int i) throws RemoteException;

    List<AbilityInfo> queryAbilityByIntent(Intent intent) throws RemoteException;

    List<AbilityInfo> queryAbilityByIntent(Intent intent, int i, int i2) throws RemoteException;

    List<CommonEventInfo> queryCommonEventInfos() throws RemoteException;

    List<CommonEventInfo> queryCommonEventInfos(int i) throws RemoteException;

    void registerAllPermissionsChanged(IRemoteObject iRemoteObject) throws RemoteException;

    void registerPermissionsChanged(int[] iArr, IRemoteObject iRemoteObject) throws RemoteException;

    @SystemApi
    boolean removeTargetBundleList(String str, List<String> list) throws RemoteException;

    void setAbilityEnabled(AbilityInfo abilityInfo, boolean z) throws IllegalArgumentException;

    void setApplicationEnabled(String str, boolean z) throws IllegalArgumentException;

    boolean setClearUserDataFlag(String str, boolean z);

    void setContext(Context context);

    void showErrorMessage(String str, int i) throws RemoteException;

    boolean silentInstall(Intent intent, InstallerCallback installerCallback) throws RemoteException;

    int startBackupSession(IBackupSessionCallback iBackupSessionCallback);

    void startShortcut(String str, String str2) throws RemoteException;

    void unregisterPermissionsChanged(IRemoteObject iRemoteObject) throws RemoteException;

    @SystemApi
    boolean updateModuleRemovableFlag(String str, String str2, int i) throws RemoteException, SecurityException, IllegalArgumentException;

    @SystemApi
    boolean updateModuleUpgradeFlag(String str, String str2, int i) throws SecurityException;

    boolean updateShortcuts(List<ShortcutInfo> list);

    boolean upgradeCheck(Intent intent, InstallerCallback installerCallback) throws RemoteException;

    boolean upgradeInstall(Intent intent, int i, InstallerCallback installerCallback) throws RemoteException;

    @Deprecated
    default List<FormInfo> getAllForms() throws RemoteException {
        return getAllFormsInfo();
    }

    @Deprecated
    default List<FormInfo> getFormsByApp(String str) throws RemoteException {
        return getFormsInfoByApp(str);
    }

    @Deprecated
    default List<FormInfo> getFormsByModule(String str, String str2) throws RemoteException {
        return getFormsInfoByModule(str, str2);
    }
}
