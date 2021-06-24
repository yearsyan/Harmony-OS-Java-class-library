package ohos.bundlemgr;

import android.app.ActivityManager;
import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.PowerManager;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import ohos.appexecfwk.utils.AppLog;
import ohos.event.notification.NotificationRequest;
import ohos.hiviewdfx.HiLogLabel;
import ohos.system.Parameters;

public class SilentAppMrgAdapter {
    private static final String BMS_SERVICE_NAME = "BundleMgrService";
    private static final String FOREGROUNG_TIME = "foregroungTime";
    private static final Uri IAWARE_APPUSAGE_URI = Uri.parse("content://iaware.app.usage/appusage");
    private static final long ONE_MB = 1048576;
    private static final String PKG_NAME = "pkgName";
    private static final HiLogLabel SAM_ADAPTER_LABEL = new HiLogLabel(3, 218108160, TAG);
    private static final int SILENT_INSTALLED_MAX_MB = 1048576;
    private static final long SILENT_INSTALLED_MAX_SIZE = (((long) Parameters.getInt(SYSTEM_PARAM_SILENT_INSTALLED_MAX_SIZE, 1048576)) * 1048576);
    private static final String SYSTEM_PARAM_SILENT_INSTALLED_MAX_SIZE = "persist.sys.bms.silentapp.size";
    private static final String TAG = "SilentAppMrgAdapter";
    private static final String USER_ID = "userID";
    private Context silentMgrContext = null;

    public SilentAppMrgAdapter(Context context) {
        this.silentMgrContext = context;
    }

    private boolean isScreenOn() {
        Context context = this.silentMgrContext;
        if (context == null) {
            AppLog.e(SAM_ADAPTER_LABEL, "SilentAppMrgAdapter, context is null", new Object[0]);
            return true;
        }
        Object systemService = context.getSystemService("power");
        if (systemService == null) {
            AppLog.e(SAM_ADAPTER_LABEL, "isScreenOn failed, object is null", new Object[0]);
            return true;
        } else if (systemService instanceof PowerManager) {
            return ((PowerManager) systemService).isInteractive();
        } else {
            AppLog.e(SAM_ADAPTER_LABEL, "isScreenOn failed, object is not PowerManager", new Object[0]);
            return true;
        }
    }

    private boolean isCharging() {
        if (this.silentMgrContext == null) {
            AppLog.e(SAM_ADAPTER_LABEL, "SilentAppMrgAdapter, context is null", new Object[0]);
            return false;
        }
        int intExtra = this.silentMgrContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra(NotificationRequest.CLASSIFICATION_STATUS, -1);
        if (intExtra == 2 || intExtra == 5) {
            return true;
        }
        return false;
    }

    private int getUIDByPackageName(String str) {
        Context context = this.silentMgrContext;
        if (context == null) {
            AppLog.e(SAM_ADAPTER_LABEL, "SilentAppMrgAdapter, context is null", new Object[0]);
            return -1;
        }
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            AppLog.e(SAM_ADAPTER_LABEL, "getUIDByPackageName failed, pm is null", new Object[0]);
            return -1;
        }
        try {
            ApplicationInfo applicationInfoAsUser = packageManager.getApplicationInfoAsUser(str, 0, getCurrentUserID());
            if (applicationInfoAsUser != null) {
                return applicationInfoAsUser.uid;
            }
            return -1;
        } catch (PackageManager.NameNotFoundException unused) {
            AppLog.e(SAM_ADAPTER_LABEL, "getUIDByPackageName failed!", new Object[0]);
            return -1;
        }
    }

    private int getCurrentUserID() {
        return ActivityManager.getCurrentUser();
    }

    public boolean isRunning(String str) {
        if (this.silentMgrContext == null) {
            AppLog.e(SAM_ADAPTER_LABEL, "SilentAppMrgAdapter, context is null", new Object[0]);
            return false;
        }
        int uIDByPackageName = getUIDByPackageName(str);
        if (uIDByPackageName < 0) {
            AppLog.e(SAM_ADAPTER_LABEL, "isRunning failed, uid -1", new Object[0]);
            return false;
        }
        Object systemService = this.silentMgrContext.getSystemService("activity");
        if (systemService instanceof ActivityManager) {
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) systemService).getRunningAppProcesses();
            if (runningAppProcesses == null || runningAppProcesses.isEmpty()) {
                AppLog.d(SAM_ADAPTER_LABEL, "SilentAppMrgAdapter no running app!", new Object[0]);
                return false;
            }
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.uid == uIDByPackageName) {
                    return true;
                }
            }
            return false;
        }
        AppLog.e(SAM_ADAPTER_LABEL, "SilentAppMrgAdapter, get activityManager failed!", new Object[0]);
        return false;
    }

    public long getAppTotalSize(String str) {
        long j = 0;
        if (this.silentMgrContext == null) {
            AppLog.e(SAM_ADAPTER_LABEL, "SilentAppMrgAdapter, context is null", new Object[0]);
            return 0;
        }
        int uIDByPackageName = getUIDByPackageName(str);
        if (uIDByPackageName < 0) {
            AppLog.e(SAM_ADAPTER_LABEL, "getAppTotalSize failed, uid -1", new Object[0]);
            return 0;
        }
        Object systemService = this.silentMgrContext.getSystemService("storagestats");
        if (systemService == null || !(systemService instanceof StorageStatsManager)) {
            AppLog.e(SAM_ADAPTER_LABEL, "getAppTotalSize failed, object is not StorageStatsManager", new Object[0]);
            return 0;
        }
        StorageStatsManager storageStatsManager = (StorageStatsManager) systemService;
        Object systemService2 = this.silentMgrContext.getSystemService("storage");
        if (systemService2 == null || !(systemService2 instanceof StorageManager)) {
            AppLog.e(SAM_ADAPTER_LABEL, "getAppTotalSize failed, object is not StorageManager", new Object[0]);
            return 0;
        }
        for (StorageVolume storageVolume : ((StorageManager) systemService2).getStorageVolumes()) {
            String uuid = storageVolume.getUuid();
            UUID uuid2 = StorageManager.UUID_DEFAULT;
            if (uuid != null) {
                uuid2 = UUID.fromString(uuid);
            }
            StorageStats storageStats = null;
            try {
                storageStats = storageStatsManager.queryStatsForUid(uuid2, uIDByPackageName);
            } catch (IOException unused) {
                AppLog.e(SAM_ADAPTER_LABEL, "getAppTotalSize failed", new Object[0]);
            }
            if (storageStats != null) {
                j = storageStats.getDataBytes() + storageStats.getAppBytes();
                AppLog.d(SAM_ADAPTER_LABEL, "getAppTotalSize uid:%{public}d cachesize:%{public}d,datasize:%{public}d, appsize:%{public}d", Integer.valueOf(uIDByPackageName), Long.valueOf(storageStats.getCacheBytes()), Long.valueOf(storageStats.getDataBytes()), Long.valueOf(storageStats.getAppBytes()));
            }
        }
        return j;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00e9, code lost:
        if (0 == 0) goto L_0x00ec;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x00d2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String[] uninstallByAppUsage(java.util.Map<java.lang.String, java.lang.Long> r18, long r19) {
        /*
        // Method dump skipped, instructions count: 252
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bundlemgr.SilentAppMrgAdapter.uninstallByAppUsage(java.util.Map, long):java.lang.String[]");
    }

    public String[] checkAndUninstall(String[] strArr) {
        AppLog.d(SAM_ADAPTER_LABEL, "uninstallSilentInstalledApps begin %{public}d", Long.valueOf(SILENT_INSTALLED_MAX_SIZE));
        if (isScreenOn() || !isCharging()) {
            AppLog.d(SAM_ADAPTER_LABEL, "uninstallSilentInstalledApps device is screen on or not charging!", new Object[0]);
            return new String[0];
        }
        AppLog.d(SAM_ADAPTER_LABEL, "uninstallSilentInstalledApps get silent app", new Object[0]);
        if (strArr == null || strArr.length == 0) {
            AppLog.d(SAM_ADAPTER_LABEL, "uninstallSilentInstalledApps has no silent intalled app!", new Object[0]);
            return new String[0];
        }
        HashMap hashMap = new HashMap();
        long j = 0;
        for (int i = 0; i < strArr.length; i++) {
            long appTotalSize = getAppTotalSize(strArr[i]);
            hashMap.put(strArr[i], Long.valueOf(appTotalSize));
            j += appTotalSize;
            AppLog.d(SAM_ADAPTER_LABEL, "checkAndUninstall total name:%{public}s size:%{public}d", strArr[i], Long.valueOf(appTotalSize));
        }
        AppLog.d(SAM_ADAPTER_LABEL, "uninstallSilentInstalledApps total size %{public}d", Long.valueOf(j));
        if (j > SILENT_INSTALLED_MAX_SIZE) {
            return uninstallByAppUsage(hashMap, j);
        }
        AppLog.d(SAM_ADAPTER_LABEL, "uninstallSilentInstalledApps total size not exceed max value!", new Object[0]);
        return new String[0];
    }

    public int getBatteryPercentage() {
        Context context = this.silentMgrContext;
        if (context == null) {
            AppLog.e(SAM_ADAPTER_LABEL, "SilentAppMrgAdapter, context is null", new Object[0]);
            return -1;
        }
        Object systemService = context.getSystemService("batterymanager");
        if (systemService instanceof BatteryManager) {
            return ((BatteryManager) systemService).getIntProperty(4);
        }
        AppLog.e(SAM_ADAPTER_LABEL, "SilentAppMrgAdapter, failed to get battery service", new Object[0]);
        return -1;
    }
}
