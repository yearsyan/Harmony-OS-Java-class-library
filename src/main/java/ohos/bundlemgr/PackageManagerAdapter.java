package ohos.bundlemgr;

import android.app.ActivityThread;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.IBackupSessionCallback;
import android.os.RemoteException;
import android.util.SparseArray;
import com.huawei.android.content.pm.HwBundleInfo;
import com.huawei.android.content.pm.HwPackageManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import ohos.appexecfwk.utils.AppLog;
import ohos.bundle.AbilityInfo;
import ohos.bundle.BundleInfo;
import ohos.bundle.IBackupSessionCallback;
import ohos.bundle.ICleanCacheCallback;
import ohos.bundle.ProfileConstants;
import ohos.bundle.ShortcutIntent;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;
import ohos.media.image.inner.ImageDoubleFwConverter;
import ohos.security.permission.PermissionConversion;
import ohos.security.permission.PermissionDef;
import ohos.security.permission.PermissionGroupDef;
import ohos.security.permissionkitinner.PermissionKitInner;
import ohos.utils.adapter.CapabilityConstantMapper;
import ohos.utils.fastjson.JSONArray;
import ohos.utils.fastjson.JSONObject;

public class PackageManagerAdapter {
    private static final int BACKUP_SESSION_FAILED = -1;
    private static final String CERT_X_509 = "X.509";
    private static final int CHECK_PERMISSION_FAILED = -1;
    private static final int GET_PACKAGE_INFO_FLAGS = 134218240;
    private static final String OHOS_PREFIX = "ohos";
    private static final int PUBLIC_KEY_BEGIN_OFFSET = 8;
    private static final String PUBLIC_KEY_MODULUS = "modulus";
    private static final String PUBLIC_KEY_PUBLIC_EXPONENT = "publicexponent";
    private static final String PUBLIC_KEY_SPEC_CHAR = "\\s*|\t|\r|\n";
    private static final String SHELL_SUFFIX = "ShellActivity";
    private static final int SHORTCUT_EXISTENCE_EXISTS = 0;
    private static final int SHORTCUT_EXISTENCE_NOT_EXISTS = 1;
    private static final int SHORTCUT_EXISTENCE_UNKNOW = 2;
    private static final String USES_FEATRUE_ZIDANE = "zidane.software.ability";
    private static volatile PackageManagerAdapter instance = new PackageManagerAdapter();
    private volatile int cachedSafeMode = -1;
    private Context context;
    private FeatureInfo[] featureInfos = null;
    private List<String> features = new ArrayList();
    private AndroidBackupCallback mCallback = new AndroidBackupCallback();
    private final SparseArray<IBackupSessionCallback> mSessions = new SparseArray<>();
    private List<PermissionGroupDef> permissionGroupDefs = new ArrayList();
    private PackageManager pkgManager = getPackageManager();
    private ShortcutManager shortcutManager = getShortcutManager();

    public static PackageManagerAdapter getInstance() {
        return instance;
    }

    private PackageManagerAdapter() {
    }

    private ShortcutManager getShortcutManager() {
        Context context2 = this.context;
        if (context2 != null) {
            return (ShortcutManager) context2.getSystemService(ShortcutManager.class);
        }
        AppLog.e("getShortcutManager failed due to context is null", new Object[0]);
        return null;
    }

    public boolean isPackageEnabled(String str) {
        if (this.pkgManager == null) {
            AppLog.e("packageManager is null", new Object[0]);
            return false;
        } else if (str == null || str.isEmpty()) {
            AppLog.e("packageName is null or empty", new Object[0]);
            return false;
        } else {
            int applicationEnabledSetting = this.pkgManager.getApplicationEnabledSetting(str);
            if (applicationEnabledSetting == 2 || applicationEnabledSetting == 3 || applicationEnabledSetting == 4) {
                return false;
            }
            return true;
        }
    }

    public boolean isComponentEnabled(String str, String str2) {
        if (this.pkgManager == null) {
            AppLog.e("packageManager is null", new Object[0]);
            return false;
        } else if (str == null || str.isEmpty()) {
            AppLog.e("packageName is null or empty", new Object[0]);
            return false;
        } else if (str2 == null || str2.isEmpty()) {
            AppLog.e("className is null or empty", new Object[0]);
            return false;
        } else {
            int componentEnabledSetting = this.pkgManager.getComponentEnabledSetting(new ComponentName(str, str2));
            if (componentEnabledSetting == 2 || componentEnabledSetting == 3 || componentEnabledSetting == 4) {
                return false;
            }
            return true;
        }
    }

    public void setComponentEnabled(String str, String str2, boolean z) {
        if (this.pkgManager == null) {
            AppLog.e("packageManager is null", new Object[0]);
        } else if (str == null || str.isEmpty()) {
            AppLog.e("packageName is null or empty", new Object[0]);
        } else if (str2 == null || str2.isEmpty()) {
            AppLog.e("className is null or empty", new Object[0]);
        } else {
            this.pkgManager.setComponentEnabledSetting(new ComponentName(str, str2), z ? 1 : 2, 0);
        }
    }

    public void setApplicationEnabled(String str, boolean z) {
        if (this.pkgManager == null) {
            AppLog.e("packageManager is null", new Object[0]);
        } else if (str == null || str.isEmpty()) {
            AppLog.e("packageName is null or empty", new Object[0]);
        } else {
            this.pkgManager.setApplicationEnabledSetting(str, z ? 1 : 2, 0);
        }
    }

    public List<String> getSystemAvailableFeatures() {
        if (!this.features.isEmpty()) {
            return this.features;
        }
        PackageManager packageManager = this.pkgManager;
        int i = 0;
        if (packageManager == null) {
            AppLog.e("packageManager is null", new Object[0]);
            return this.features;
        }
        if (this.featureInfos == null) {
            this.featureInfos = packageManager.getSystemAvailableFeatures();
        }
        if (this.featureInfos == null) {
            AppLog.e("get featureInfos from pms is null", new Object[0]);
            return this.features;
        }
        while (true) {
            FeatureInfo[] featureInfoArr = this.featureInfos;
            if (i >= featureInfoArr.length) {
                return this.features;
            }
            String str = featureInfoArr[i].name;
            if (str != null && !str.isEmpty()) {
                if (str.contains(OHOS_PREFIX)) {
                    this.features.add(str);
                } else {
                    Optional<String> convertToCapability = CapabilityConstantMapper.convertToCapability(str);
                    if (convertToCapability.isPresent() && !convertToCapability.get().isEmpty()) {
                        this.features.add(convertToCapability.get());
                    }
                }
            }
            i++;
        }
    }

    public boolean hasSystemFeature(String str) {
        if (this.features.isEmpty()) {
            this.features = getSystemAvailableFeatures();
        }
        return this.features.contains(str);
    }

    public int[] getPackageGids(String str) {
        int[] iArr = new int[0];
        PackageManager packageManager = this.pkgManager;
        if (packageManager == null) {
            AppLog.e("packageManager is null", new Object[0]);
            return iArr;
        }
        try {
            return packageManager.getPackageGids(str);
        } catch (PackageManager.NameNotFoundException e) {
            AppLog.w("getPackageGids failed, error : %{public}s", e.getMessage());
            return iArr;
        }
    }

    public boolean isSafeMode() {
        PackageManager packageManager;
        if (this.cachedSafeMode < 0 && (packageManager = this.pkgManager) != null) {
            this.cachedSafeMode = packageManager.isSafeMode() ? 1 : 0;
        }
        return this.cachedSafeMode != 0;
    }

    public PermissionDef getPermissionInfo(String str) {
        if (this.pkgManager == null) {
            AppLog.e("packageManager is null", new Object[0]);
            return null;
        }
        String aosPermissionNameIfPossible = PermissionConversion.getAosPermissionNameIfPossible(str);
        if (aosPermissionNameIfPossible == null || aosPermissionNameIfPossible.isEmpty()) {
            AppLog.e("get translated permission name is null", new Object[0]);
            return null;
        }
        try {
            PermissionInfo permissionInfo = this.pkgManager.getPermissionInfo(aosPermissionNameIfPossible, 0);
            if (permissionInfo != null) {
                return PermissionKitInner.getInstance().translatePermission(permissionInfo, str).orElse(null);
            }
            AppLog.e("get permission from pms is null", new Object[0]);
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            AppLog.e("permissionName not found, %{public}s", e.getMessage());
            AppLog.e("getPermissionInfo from pms failed", new Object[0]);
            return null;
        }
    }

    public int checkPermission(String str, String str2) {
        if (this.pkgManager == null) {
            AppLog.e("packageManager is null", new Object[0]);
            return -1;
        }
        String aosPermissionNameIfPossible = PermissionConversion.getAosPermissionNameIfPossible(str);
        if (aosPermissionNameIfPossible != null && !aosPermissionNameIfPossible.isEmpty()) {
            return this.pkgManager.checkPermission(aosPermissionNameIfPossible, str2);
        }
        AppLog.e("get translated permission name is null", new Object[0]);
        return -1;
    }

    public Optional<PermissionGroupDef> getPermissionGroupInfo(String str) {
        if (this.pkgManager == null) {
            AppLog.e("permission group get packageManager is null", new Object[0]);
            return Optional.empty();
        }
        String androidPermGroupName = PermissionKitInner.getInstance().getAndroidPermGroupName(str);
        if (androidPermGroupName == null || androidPermGroupName.isEmpty()) {
            AppLog.e("get permission group, permission group name is invalid", new Object[0]);
            return Optional.empty();
        }
        try {
            PermissionGroupInfo permissionGroupInfo = this.pkgManager.getPermissionGroupInfo(androidPermGroupName, 0);
            if (permissionGroupInfo != null) {
                return PermissionKitInner.getInstance().translatePermissionGroup(permissionGroupInfo, str);
            }
            AppLog.e("get permission group from pms is null", new Object[0]);
            return Optional.empty();
        } catch (PackageManager.NameNotFoundException unused) {
            AppLog.e("getPermissionGroupInfo failed GroupName not found, %{public}s", str);
            return Optional.empty();
        }
    }

    public Optional<List<PermissionDef>> getPermissionInfoByGroup(String str) {
        if (this.pkgManager == null) {
            AppLog.e("sub permission info get by group packageManager is null", new Object[0]);
            return Optional.empty();
        }
        String androidPermGroupName = PermissionKitInner.getInstance().getAndroidPermGroupName(str);
        if (androidPermGroupName == null || androidPermGroupName.isEmpty()) {
            AppLog.e("get sub permission by group, group name is invalid", new Object[0]);
            return Optional.empty();
        }
        try {
            ArrayList arrayList = new ArrayList();
            List<PermissionInfo> queryPermissionsByGroup = this.pkgManager.queryPermissionsByGroup(androidPermGroupName, 0);
            if (queryPermissionsByGroup == null) {
                AppLog.e("get sub permission by permission group from pms is null", new Object[0]);
                return Optional.empty();
            }
            for (PermissionInfo permissionInfo : queryPermissionsByGroup) {
                if (permissionInfo != null) {
                    Optional<PermissionDef> translatePermission = PermissionKitInner.getInstance().translatePermission(permissionInfo, permissionInfo.name);
                    if (translatePermission.isPresent()) {
                        arrayList.add(translatePermission.get());
                    }
                }
            }
            return Optional.of(arrayList);
        } catch (PackageManager.NameNotFoundException unused) {
            AppLog.e("get sub permission info failed GroupName not found, %{public}s", str);
            return Optional.empty();
        }
    }

    public void cleanBundleCacheFiles(String str, final ICleanCacheCallback iCleanCacheCallback) {
        if (this.pkgManager == null) {
            AppLog.e("clean bundle cache files get packageManager is null", new Object[0]);
            return;
        }
        this.pkgManager.deleteApplicationCacheFiles(str, new IPackageDataObserver.Stub() {
            /* class ohos.bundlemgr.PackageManagerAdapter.AnonymousClass1 */

            public void onRemoveCompleted(String str, boolean z) throws RemoteException {
                iCleanCacheCallback.onCleanCacheFinished(z);
            }
        });
    }

    private PackageManager getPackageManager() {
        Context applicationContext;
        Application currentApplication = ActivityThread.currentApplication();
        if (currentApplication == null || (applicationContext = currentApplication.getApplicationContext()) == null) {
            return null;
        }
        this.context = applicationContext;
        return applicationContext.getPackageManager();
    }

    public int startBackupSession(IBackupSessionCallback iBackupSessionCallback) {
        if (iBackupSessionCallback == null) {
            return -1;
        }
        int startBackupSession = HwPackageManager.startBackupSession(this.mCallback);
        if (startBackupSession > 0) {
            synchronized (this.mSessions) {
                this.mSessions.put(startBackupSession, iBackupSessionCallback);
            }
        }
        return startBackupSession;
    }

    public int executeBackupTask(int i, String str) {
        return HwPackageManager.executeBackupTask(i, str);
    }

    public int finishBackupSession(int i) {
        int finishBackupSession = HwPackageManager.finishBackupSession(i);
        synchronized (this.mSessions) {
            this.mSessions.remove(i);
        }
        return finishBackupSession;
    }

    /* access modifiers changed from: private */
    public final class AndroidBackupCallback extends IBackupSessionCallback.Stub {
        private AndroidBackupCallback() {
        }

        public void onTaskStatusChanged(int i, int i2, int i3, String str) {
            synchronized (PackageManagerAdapter.this.mSessions) {
                ohos.bundle.IBackupSessionCallback iBackupSessionCallback = (ohos.bundle.IBackupSessionCallback) PackageManagerAdapter.this.mSessions.get(i);
                if (iBackupSessionCallback == null) {
                    AppLog.i("no callback set for session:%{public}d", Integer.valueOf(i));
                    return;
                }
                iBackupSessionCallback.onTaskStatusChanged(i, i2, i3, str);
            }
        }
    }

    public BundleInfo getBundleInfoFromPms(String str, boolean z) {
        AppLog.d("packageManager getBundleInfoFromPms is called", new Object[0]);
        if (this.pkgManager == null) {
            return null;
        }
        int i = GET_PACKAGE_INFO_FLAGS;
        if (z) {
            i = 134218253;
        }
        try {
            return convertPackageInfoToBundleInfo(this.pkgManager.getPackageInfo(str, i));
        } catch (PackageManager.NameNotFoundException unused) {
            AppLog.e("packageManager getBundleInfoFromPms occur exception.", new Object[0]);
            return null;
        }
    }

    public List<BundleInfo> getBundleInfosFromPms(boolean z) {
        BundleInfo convertPackageInfoToBundleInfo;
        AppLog.d("packageManager getBundleInfosFromPms is called", new Object[0]);
        if (this.pkgManager == null) {
            return new ArrayList();
        }
        int i = GET_PACKAGE_INFO_FLAGS;
        if (z) {
            i = 134218253;
        }
        List<PackageInfo> installedPackages = this.pkgManager.getInstalledPackages(i);
        if (installedPackages == null || installedPackages.isEmpty()) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        Iterator<PackageInfo> it = installedPackages.iterator();
        while (true) {
            boolean z2 = true;
            if (it.hasNext()) {
                PackageInfo next = it.next();
                if (next != null) {
                    if (next.reqFeatures != null) {
                        FeatureInfo[] featureInfoArr = next.reqFeatures;
                        int length = featureInfoArr.length;
                        int i2 = 0;
                        while (true) {
                            if (i2 >= length) {
                                break;
                            } else if (USES_FEATRUE_ZIDANE.equals(featureInfoArr[i2].name)) {
                                break;
                            } else {
                                i2++;
                            }
                        }
                    }
                    z2 = false;
                    if (!z2 && (convertPackageInfoToBundleInfo = convertPackageInfoToBundleInfo(next)) != null) {
                        arrayList.add(convertPackageInfoToBundleInfo);
                    }
                }
            } else {
                AppLog.d("packageManager getBundleInfosFromPms bundleInfos size:%{public}d", Integer.valueOf(arrayList.size()));
                return arrayList;
            }
        }
    }

    public int isPinnedShortcutExist(String str, int i) {
        if (str == null || str.isEmpty()) {
            AppLog.e("isPinnedShortcutExist::invalid shortcutId", new Object[0]);
            return 1;
        }
        ShortcutManager shortcutManager2 = this.shortcutManager;
        if (shortcutManager2 == null) {
            AppLog.e("isPinnedShortcutExist::shortcutManager is null", new Object[0]);
            return 2;
        }
        try {
            List<ShortcutInfo> pinnedShortcuts = shortcutManager2.getPinnedShortcuts();
            if (pinnedShortcuts == null) {
                AppLog.d("isPinnedShortcutExist::infos is null", new Object[0]);
                return 1;
            }
            for (ShortcutInfo shortcutInfo : pinnedShortcuts) {
                if (str.equals(shortcutInfo.getId())) {
                    AppLog.d("isPinnedShortcutExist::shortcut id: %{private}s exists", str);
                    return 0;
                }
            }
            AppLog.d("isPinnedShortcutExist::shortcut id: %{private}s not exists", str);
            return 1;
        } catch (IllegalStateException unused) {
            AppLog.w("isPinnedShortcutExist::Device currently may be locked", new Object[0]);
        }
    }

    public boolean isHomeShortcutExist(String str) {
        if (str == null || str.isEmpty()) {
            AppLog.e("isHomeShortcutExist::invalid shortcutId", new Object[0]);
            return false;
        }
        ShortcutManager shortcutManager2 = this.shortcutManager;
        if (shortcutManager2 == null) {
            AppLog.e("isHomeShortcutExist::shortcutManager is null", new Object[0]);
            return false;
        }
        try {
            List<ShortcutInfo> pinnedShortcuts = shortcutManager2.getPinnedShortcuts();
            if (pinnedShortcuts == null) {
                return false;
            }
            for (ShortcutInfo shortcutInfo : pinnedShortcuts) {
                if (str.equals(shortcutInfo.getId())) {
                    return true;
                }
            }
            return false;
        } catch (IllegalStateException unused) {
            AppLog.w("isHomeShortcutExist::Device currently may be locked", new Object[0]);
        }
    }

    public boolean requestPinShortcut(ohos.bundle.ShortcutInfo shortcutInfo, ResourceManager resourceManager) {
        AppLog.d("requestPinShortcut is called", new Object[0]);
        if (shortcutInfo == null || resourceManager == null) {
            AppLog.e("parameters invalid", new Object[0]);
            return false;
        } else if (this.shortcutManager == null) {
            AppLog.e("shortcutManager is null", new Object[0]);
            return false;
        } else {
            ShortcutInfo convertShortcut = convertShortcut(shortcutInfo, resourceManager);
            if (convertShortcut == null) {
                AppLog.e("shortcutInfo is invalid", new Object[0]);
                return false;
            }
            try {
                return this.shortcutManager.requestPinShortcut(convertShortcut, null);
            } catch (IllegalArgumentException unused) {
                throw new IllegalArgumentException("shortcut may be disabled");
            } catch (IllegalStateException unused2) {
                throw new IllegalStateException("device may be locked or invalid caller");
            }
        }
    }

    public boolean updateShortcuts(List<ohos.bundle.ShortcutInfo> list, ResourceManager resourceManager) {
        if (list == null || list.isEmpty() || resourceManager == null) {
            AppLog.e("parameters invalid", new Object[0]);
            return false;
        } else if (this.shortcutManager == null) {
            AppLog.e("shortcutManager is null", new Object[0]);
            return false;
        } else {
            List<ShortcutInfo> convertToAShortcuts = convertToAShortcuts(list, resourceManager);
            if (convertToAShortcuts.isEmpty()) {
                AppLog.e("updateShortcuts shorcutInfos is invalid", new Object[0]);
                return false;
            }
            try {
                return this.shortcutManager.updateShortcuts(convertToAShortcuts);
            } catch (IllegalArgumentException unused) {
                AppLog.e("updateShortcuts shorcutInfos is invalid", new Object[0]);
                return false;
            } catch (IllegalStateException unused2) {
                AppLog.e("device currently may be locked or invalid host ability", new Object[0]);
                return false;
            }
        }
    }

    public boolean isRequestPinShortcutSupported() {
        ShortcutManager shortcutManager2 = this.shortcutManager;
        if (shortcutManager2 != null) {
            return shortcutManager2.isRequestPinShortcutSupported();
        }
        AppLog.e("shortcutManager is null", new Object[0]);
        return false;
    }

    public void disablePinShortcuts(List<String> list) {
        ShortcutManager shortcutManager2 = this.shortcutManager;
        if (shortcutManager2 == null) {
            AppLog.e("shortcutManager is null", new Object[0]);
            return;
        }
        try {
            shortcutManager2.disableShortcuts(list);
        } catch (IllegalArgumentException unused) {
            throw new IllegalArgumentException("shortcut may be immutable");
        } catch (IllegalStateException unused2) {
            AppLog.e("user may be locked", new Object[0]);
        }
    }

    public void enablePinShortcuts(List<String> list) {
        ShortcutManager shortcutManager2 = this.shortcutManager;
        if (shortcutManager2 == null) {
            AppLog.e("shortcutManager is null", new Object[0]);
            return;
        }
        try {
            shortcutManager2.enableShortcuts(list);
        } catch (IllegalArgumentException unused) {
            throw new IllegalArgumentException("invalid shortcut");
        } catch (IllegalStateException unused2) {
            AppLog.e("user may be locked", new Object[0]);
        }
    }

    public List<ohos.bundle.ShortcutInfo> getHomeShortcutInfos() {
        ShortcutManager shortcutManager2 = this.shortcutManager;
        if (shortcutManager2 == null) {
            AppLog.e("shortcutManager is null", new Object[0]);
            return new ArrayList();
        }
        try {
            return convertShortcutParams(shortcutManager2.getPinnedShortcuts());
        } catch (IllegalStateException unused) {
            AppLog.e("user may be locked", new Object[0]);
            return new ArrayList();
        }
    }

    private List<ohos.bundle.ShortcutInfo> convertShortcutParams(List<ShortcutInfo> list) {
        ArrayList arrayList = new ArrayList();
        if (list != null && !list.isEmpty()) {
            for (ShortcutInfo shortcutInfo : list) {
                if (shortcutInfo != null) {
                    arrayList.add(convertShortcutParam(shortcutInfo));
                }
            }
        }
        return arrayList;
    }

    private ohos.bundle.ShortcutInfo convertShortcutParam(ShortcutInfo shortcutInfo) {
        ohos.bundle.ShortcutInfo shortcutInfo2 = new ohos.bundle.ShortcutInfo();
        shortcutInfo2.setId(shortcutInfo.getId());
        String str = shortcutInfo.getPackage();
        if (str != null) {
            shortcutInfo2.setBundleName(str.toString());
        }
        CharSequence shortLabel = shortcutInfo.getShortLabel();
        if (shortLabel != null) {
            shortcutInfo2.setLabel(shortLabel.toString());
        }
        CharSequence disabledMessage = shortcutInfo.getDisabledMessage();
        if (disabledMessage != null) {
            shortcutInfo2.setDisableMessage(disabledMessage.toString());
        }
        ComponentName activity = shortcutInfo.getActivity();
        if (activity != null) {
            String className = activity.getClassName();
            int lastIndexOf = className.lastIndexOf("ShellActivity");
            if (lastIndexOf == className.length() - 13) {
                className = className.substring(0, lastIndexOf);
            }
            shortcutInfo2.setHostAbilityName(className);
        }
        Intent[] intents = shortcutInfo.getIntents();
        if (intents != null && intents.length > 0) {
            shortcutInfo2.setIntents(createShortcutIntents(intents));
        }
        shortcutInfo2.addFlags(4);
        if (!shortcutInfo.isEnabled()) {
            shortcutInfo2.addFlags(2);
        }
        shortcutInfo2.clearFlags(1);
        return shortcutInfo2;
    }

    private ShortcutInfo convertShortcut(ohos.bundle.ShortcutInfo shortcutInfo, ResourceManager resourceManager) throws IllegalArgumentException {
        if (shortcutInfo.getId() == null) {
            throw new IllegalArgumentException("shortcut's id can't be null");
        } else if (shortcutInfo.getLabel() != null) {
            ShortcutInfo.Builder shortLabel = new ShortcutInfo.Builder(this.context, shortcutInfo.getId()).setShortLabel(shortcutInfo.getLabel());
            String bundleName = shortcutInfo.getBundleName();
            shortLabel.setActivity(new ComponentName(bundleName, shortcutInfo.getHostAbilityName() + "ShellActivity"));
            Intent[] createIntents = createIntents(shortcutInfo);
            if (createIntents.length != 0) {
                shortLabel.setIntents(createIntents);
                Icon createIcon = createIcon(shortcutInfo, resourceManager);
                if (createIcon != null) {
                    shortLabel.setIcon(createIcon);
                }
                String disableMessage = shortcutInfo.getDisableMessage();
                if (disableMessage != null && !disableMessage.isEmpty()) {
                    shortLabel.setDisabledMessage(disableMessage);
                }
                return shortLabel.build();
            }
            throw new IllegalArgumentException("shortcut's intents can't be null or empty");
        } else {
            throw new IllegalArgumentException("shortcut's label can't be null");
        }
    }

    private List<ShortcutInfo> convertToAShortcuts(List<ohos.bundle.ShortcutInfo> list, ResourceManager resourceManager) {
        ArrayList arrayList = new ArrayList();
        for (ohos.bundle.ShortcutInfo shortcutInfo : list) {
            arrayList.add(convertShortcut(shortcutInfo, resourceManager));
        }
        return arrayList;
    }

    private Icon createIcon(ohos.bundle.ShortcutInfo shortcutInfo, ResourceManager resourceManager) {
        int shortcutIconId = shortcutInfo.getShortcutIconId();
        if (shortcutIconId <= 0) {
            InputStream iconStream = shortcutInfo.getIconStream();
            if (iconStream == null) {
                AppLog.w("createIcon failed due to resId is invalid and iconStream is null", new Object[0]);
                return null;
            }
            Bitmap decodeStream = BitmapFactory.decodeStream(iconStream);
            if (decodeStream != null) {
                return Icon.createWithBitmap(decodeStream);
            }
            AppLog.e("decodeStream failed", new Object[0]);
            return null;
        }
        try {
            PixelMap createPixelMap = createPixelMap(resourceManager.getResource(shortcutIconId));
            if (createPixelMap == null) {
                AppLog.e("createPixelMap failed", new Object[0]);
                return null;
            }
            Bitmap createShadowBitmap = ImageDoubleFwConverter.createShadowBitmap(createPixelMap);
            if (createShadowBitmap != null) {
                return Icon.createWithBitmap(createShadowBitmap);
            }
            AppLog.e("createShadowBitmap failed", new Object[0]);
            return null;
        } catch (IOException | NotExistException e) {
            AppLog.e("getResource failed, exception: %{public}s", e.getMessage());
            return null;
        }
    }

    private PixelMap createPixelMap(Resource resource) {
        ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
        sourceOptions.formatHint = "image/png";
        ImageSource create = ImageSource.create(resource, sourceOptions);
        if (create == null) {
            AppLog.e("create imageSource failed", new Object[0]);
            return null;
        }
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        decodingOptions.desiredSize = new Size(0, 0);
        decodingOptions.desiredRegion = new Rect(0, 0, 0, 0);
        decodingOptions.desiredPixelFormat = PixelFormat.RGBA_8888;
        return create.createPixelmap(decodingOptions);
    }

    private Intent[] createIntents(ohos.bundle.ShortcutInfo shortcutInfo) {
        if (shortcutInfo == null || shortcutInfo.getIntents() == null) {
            AppLog.e("createIntents failed due to invalid parameter", new Object[0]);
            return new Intent[0];
        }
        int size = shortcutInfo.getIntents().size();
        Intent[] intentArr = new Intent[size];
        for (int i = 0; i < size; i++) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            ShortcutIntent shortcutIntent = shortcutInfo.getIntents().get(i);
            if (shortcutIntent == null) {
                AppLog.w("createIntents intent is null", new Object[0]);
            } else {
                String targetBundle = shortcutIntent.getTargetBundle();
                String targetClass = shortcutIntent.getTargetClass();
                if (targetBundle == null || targetClass == null) {
                    AppLog.w("createIntents bundleName or className is null", new Object[0]);
                } else {
                    if (targetClass.startsWith(".")) {
                        targetClass = targetBundle + shortcutIntent.getTargetClass();
                    }
                    intent.setComponent(new ComponentName(targetBundle, targetClass + "ShellActivity"));
                    for (Map.Entry<String, String> entry : shortcutIntent.getParams().entrySet()) {
                        intent.putExtra(entry.getKey(), entry.getValue());
                    }
                    intentArr[i] = intent;
                }
            }
        }
        return intentArr;
    }

    private List<ShortcutIntent> createShortcutIntents(Intent[] intentArr) {
        int lastIndexOf;
        ArrayList arrayList = new ArrayList();
        for (Intent intent : intentArr) {
            ComponentName component = intent.getComponent();
            if (component != null) {
                String packageName = component.getPackageName();
                String className = component.getClassName();
                if (!(packageName == null || className == null || (lastIndexOf = className.lastIndexOf("ShellActivity")) != className.length() - 13)) {
                    arrayList.add(new ShortcutIntent(packageName, className.substring(0, lastIndexOf)));
                }
            }
        }
        return arrayList;
    }

    static String generateAppId(PackageInfo packageInfo) {
        Signature[] apkContentsSigners = packageInfo.signingInfo.getApkContentsSigners();
        if (apkContentsSigners == null || apkContentsSigners.length <= 0) {
            return "";
        }
        String publicKey = getPublicKey(apkContentsSigners[0]);
        if (!publicKey.isEmpty()) {
            return packageInfo.packageName + "_" + publicKey;
        }
        AppLog.w("generateAppId failed, public key is null", new Object[0]);
        return "";
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:16:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getPublicKey(android.content.pm.Signature r4) {
        /*
        // Method dump skipped, instructions count: 125
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bundlemgr.PackageManagerAdapter.getPublicKey(android.content.pm.Signature):java.lang.String");
    }

    private static BundleInfo convertPackageInfoToBundleInfo(PackageInfo packageInfo) {
        if (packageInfo == null || packageInfo.applicationInfo == null) {
            return null;
        }
        BundleInfo bundleInfo = new BundleInfo();
        bundleInfo.name = packageInfo.packageName;
        bundleInfo.appId = generateAppId(packageInfo);
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        bundleInfo.uid = applicationInfo.uid;
        bundleInfo.jointUserId = packageInfo.sharedUserId;
        bundleInfo.appInfo.name = packageInfo.packageName;
        bundleInfo.appInfo.systemApp = applicationInfo.isSystemApp();
        bundleInfo.appInfo.enabled = applicationInfo.enabled;
        bundleInfo.installTime = packageInfo.firstInstallTime;
        bundleInfo.updateTime = packageInfo.lastUpdateTime;
        ArrayList arrayList = new ArrayList();
        ActivityInfo[] activityInfoArr = packageInfo.activities;
        if (activityInfoArr != null && activityInfoArr.length > 0) {
            convertComponentInfoToAbilityInfo(Arrays.asList(activityInfoArr), arrayList);
        }
        ServiceInfo[] serviceInfoArr = packageInfo.services;
        if (serviceInfoArr != null && serviceInfoArr.length > 0) {
            convertComponentInfoToAbilityInfo(Arrays.asList(serviceInfoArr), arrayList);
        }
        ProviderInfo[] providerInfoArr = packageInfo.providers;
        if (providerInfoArr != null && providerInfoArr.length > 0) {
            convertComponentInfoToAbilityInfo(Arrays.asList(providerInfoArr), arrayList);
        }
        if (!arrayList.isEmpty()) {
            bundleInfo.abilityInfos.addAll(arrayList);
        }
        return bundleInfo;
    }

    private static void convertComponentInfoToAbilityInfo(List<? extends ComponentInfo> list, List<AbilityInfo> list2) {
        if (!(list == null || list2 == null)) {
            for (ComponentInfo componentInfo : list) {
                if (componentInfo != null) {
                    AbilityInfo abilityInfo = new AbilityInfo();
                    abilityInfo.bundleName = componentInfo.packageName;
                    abilityInfo.className = new ComponentName(componentInfo.packageName, componentInfo.name).getClassName();
                    abilityInfo.enabled = componentInfo.enabled;
                    list2.add(abilityInfo);
                }
            }
        }
    }

    public Optional<List<PermissionGroupDef>> getAllPermissionGroupDefs() {
        if (!this.permissionGroupDefs.isEmpty()) {
            return Optional.of(this.permissionGroupDefs);
        }
        PackageManager packageManager = this.pkgManager;
        if (packageManager == null) {
            AppLog.e("getAllPermissionGroups packageManager is null", new Object[0]);
            return Optional.empty();
        }
        List<PermissionGroupInfo> allPermissionGroups = packageManager.getAllPermissionGroups(0);
        if (allPermissionGroups == null) {
            AppLog.e("getAllPermissionGroups from pms is null", new Object[0]);
            return Optional.empty();
        }
        for (PermissionGroupInfo permissionGroupInfo : allPermissionGroups) {
            this.permissionGroupDefs.add(PermissionKitInner.getInstance().translatePermissionGroup(permissionGroupInfo, PermissionKitInner.getInstance().getHarmonyosPermGroupName(permissionGroupInfo.name)).get());
        }
        return Optional.of(this.permissionGroupDefs);
    }

    public List<String> getAppsGrantedPermissions(String[] strArr) {
        ArrayList arrayList = new ArrayList();
        if (strArr == null) {
            AppLog.e("getAppsGrantedPermissions permissions is null", new Object[0]);
            return arrayList;
        } else if (this.pkgManager == null) {
            AppLog.e("getAppsGrantedPermissions packageManager is null", new Object[0]);
            return arrayList;
        } else {
            int length = strArr.length;
            String[] strArr2 = new String[length];
            for (int i = 0; i < length; i++) {
                strArr2[i] = PermissionConversion.getAosPermissionNameIfPossible(strArr[i]);
                AppLog.d("getAppsGrantedPermissions aPermission = %{private}s", strArr2[i]);
            }
            List<PackageInfo> packagesHoldingPermissions = this.pkgManager.getPackagesHoldingPermissions(strArr2, 0);
            if (packagesHoldingPermissions == null) {
                AppLog.e("getAppsGrantedPermissions packageInfos is null", new Object[0]);
                return arrayList;
            }
            for (PackageInfo packageInfo : packagesHoldingPermissions) {
                arrayList.add(packageInfo.packageName);
            }
            return arrayList;
        }
    }

    public static void notifyTargetBundleList(String str, String[] strArr, int i) {
        if (str != null && strArr != null) {
            List asList = Arrays.asList(strArr);
            AppLog.d("notifyTargetBundleList bundleName=%{public}s, size=%{public}d, option=%{public}d", str, Integer.valueOf(asList.size()), Integer.valueOf(i));
            HwPackageManager.updateTargetBundleList(str, asList, i);
        }
    }

    public static void updateSharedLibMapList(List<String> list, List<String> list2, List<String> list3) {
        HwPackageManager.updateSharedLibMapList(list, list2, list3);
    }

    public static void notifyBundleInfo(String str, int i) {
        if (str == null || str.isEmpty()) {
            AppLog.e("notifyBundleInfo failed, param is null or empty", new Object[0]);
            return;
        }
        JSONObject parseObject = JSONObject.parseObject(str);
        if (parseObject == null) {
            AppLog.i("notifyBundleInfo: jsonObject is null", new Object[0]);
            return;
        }
        String string = parseObject.getString(ProfileConstants.BUNDLE_NAME);
        AppLog.i("notifyBundleInfo: %{public}s, option: %{public}d", string, Integer.valueOf(i));
        String string2 = parseObject.getString("smartWindowSize");
        List list = null;
        if (parseObject.containsKey("smartWindowDeviceType")) {
            list = JSONArray.parseArray(parseObject.getString("smartWindowDeviceType").toString(), String.class);
        }
        HwBundleInfo hwBundleInfo = new HwBundleInfo();
        hwBundleInfo.setPackageName(string);
        hwBundleInfo.setSmartWindowSize(string2);
        hwBundleInfo.setSmartWindowDeviceType(list);
        HwPackageManager.updateBundleInfo(string, hwBundleInfo, i);
    }
}
