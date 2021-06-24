package ohos.security.permission.adapter;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.app.IAppOpsService;
import java.util.Objects;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;

public class AppOpsManagerAdapter {
    private static final HiLogLabel LABEL = HiLogLabelUtil.ADAPTER.newHiLogLabel(TAG);
    public static final int MODE_ALLOWED = 0;
    public static final int MODE_DEFAULT = 3;
    public static final int MODE_ERRORED = 1;
    public static final int MODE_FOREGROUND = 4;
    public static final int MODE_IGNORED = 1;
    public static final int OP_NONE = -1;
    private static final String TAG = "AppOpsManagerAdapter";

    private AppOpsManagerAdapter() {
    }

    public static AppOpsManagerAdapter getInstance() {
        return SingletonClassHolder.INSTANCE;
    }

    public static String permissionToOp(String str) {
        return AppOpsManager.permissionToOp(str);
    }

    public static int permissionToOpCode(String str) {
        return AppOpsManager.permissionToOpCode(str);
    }

    public int checkPackage(int i, String str) {
        IAppOpsService asInterface = IAppOpsService.Stub.asInterface(ServiceManager.getService("appops"));
        if (asInterface == null) {
            HiLog.error(LABEL, "checkPackage: get appops service error", new Object[0]);
            return 1;
        }
        try {
            return asInterface.checkPackage(i, str);
        } catch (RemoteException unused) {
            HiLog.error(LABEL, "checkPackage: remoteException.", new Object[0]);
            return 1;
        }
    }

    public int noteOperation(int i, int i2, String str) {
        IAppOpsService asInterface = IAppOpsService.Stub.asInterface(ServiceManager.getService("appops"));
        if (asInterface == null) {
            HiLog.error(LABEL, "noteOperation: get appops service error", new Object[0]);
            return 1;
        }
        try {
            return asInterface.noteOperation(i, i2, str);
        } catch (RemoteException unused) {
            HiLog.error(LABEL, "noteOperation: remoteException.", new Object[0]);
            return 1;
        }
    }

    public void setUidMode(Object obj, int i, int i2, int i3) {
        if (!(obj instanceof Context)) {
            HiLog.error(LABEL, "setUidMode: contextObject type error, %{public}s", obj);
            return;
        }
        AppOpsManager appOpsManager = (AppOpsManager) ((Context) obj).getSystemService(AppOpsManager.class);
        if (Objects.isNull(appOpsManager)) {
            HiLog.error(LABEL, "setUidMode: getSystemService error, %{public}s", obj);
            return;
        }
        appOpsManager.setUidMode(i, i2, i3);
    }

    public void setUidMode(Object obj, String str, int i, int i2) {
        if (!(obj instanceof Context)) {
            HiLog.error(LABEL, "setUidMode: contextObject type error, %{public}s", obj);
            return;
        }
        AppOpsManager appOpsManager = (AppOpsManager) ((Context) obj).getSystemService(AppOpsManager.class);
        if (Objects.isNull(appOpsManager)) {
            HiLog.error(LABEL, "setUidMode: getSystemService error, %{public}s", obj);
            return;
        }
        appOpsManager.setUidMode(str, i, i2);
    }

    /* access modifiers changed from: private */
    public static final class SingletonClassHolder {
        private static final AppOpsManagerAdapter INSTANCE = new AppOpsManagerAdapter();

        private SingletonClassHolder() {
        }
    }
}
