package ohos.security.permission.adapter;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.security.permission.infrastructure.utils.DataValidUtil;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;

public class ActivityManagerAdapter {
    public static final int DENIED = -1;
    public static final int FAILURE = -1;
    public static final int GRANTED = 0;
    public static final int IMPORTANCE_FOREGROUND_SERVICE = 125;
    private static final HiLogLabel LABEL = HiLogLabelUtil.ADAPTER.newHiLogLabel(TAG);
    public static final int SUCCESS = 0;
    private static final String TAG = "ActivityManagerAdapter";

    private ActivityManagerAdapter() {
    }

    /* access modifiers changed from: private */
    public static final class SingletonClassHolder {
        private static final ActivityManagerAdapter INSTANCE = new ActivityManagerAdapter();

        private SingletonClassHolder() {
        }
    }

    public static ActivityManagerAdapter getInstance() {
        return SingletonClassHolder.INSTANCE;
    }

    public int checkPermission(String str, int i, int i2) {
        IActivityManager asInterface = IActivityManager.Stub.asInterface(ServiceManager.getService("activity"));
        if (asInterface == null) {
            HiLog.error(LABEL, "checkPermissionByAms: get null activityManager.", new Object[0]);
            return -1;
        }
        try {
            return asInterface.checkPermission(str, i, i2);
        } catch (RemoteException unused) {
            HiLog.error(LABEL, "checkPermissionByAms: remoteException.", new Object[0]);
            return -1;
        }
    }

    public int getUidImportance(Object obj, int i) {
        if (!(obj instanceof Context)) {
            HiLog.error(LABEL, "getUidImportance: contextObject type error, %{public}s", obj);
            return -1;
        } else if (!DataValidUtil.isUidValid(i)) {
            HiLog.error(LABEL, "getUidImportance: invalid uid %{public}d ", Integer.valueOf(i));
            return -1;
        } else {
            Object systemService = ((Context) obj).getSystemService("activity");
            if (systemService instanceof ActivityManager) {
                return ((ActivityManager) systemService).getUidImportance(i);
            }
            HiLog.error(LABEL, "getUidImportance: getSystemService for activity error", new Object[0]);
            return -1;
        }
    }
}
