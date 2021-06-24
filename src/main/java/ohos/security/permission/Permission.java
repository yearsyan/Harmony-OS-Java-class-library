package ohos.security.permission;

import com.huawei.security.dpermission.DistributedPermissionManager;
import ohos.annotation.SystemApi;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.security.SystemPermission;
import ohos.security.permission.adapter.AppContextAdapter;
import ohos.security.permission.adapter.PermissionReasonAdapter;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;

public class Permission {
    private static final HiLogLabel LABEL = HiLogLabelUtil.KIT.newHiLogLabel(TAG);
    private static final String TAG = "Permission";

    public interface OnRequestPermissionsResult {
        void onCancel(String str, String[] strArr);

        void onResult(String str, String[] strArr, int[] iArr);

        void onTimeOut(String str, String[] strArr);
    }

    private Permission() {
    }

    @SystemApi
    @NeedsPermission(SystemPermission.GRANT_SENSITIVE_PERMISSIONS)
    public static void grantSensitivePermission(Object obj, String str, String str2, int i) {
        PermissionInner.grantSensitivePermission(getHostContext(obj), str, str2, i);
    }

    @SystemApi
    @NeedsPermission(SystemPermission.REVOKE_SENSITIVE_PERMISSIONS)
    public static void revokeSensitivePermission(Object obj, String str, String str2, int i) {
        PermissionInner.revokeSensitivePermission(getHostContext(obj), str, str2, i);
    }

    @SystemApi
    @NeedsPermission(any = {SystemPermission.GRANT_SENSITIVE_PERMISSIONS, SystemPermission.REVOKE_SENSITIVE_PERMISSIONS, SystemPermission.GET_SENSITIVE_PERMISSIONS})
    public static int getPermissionStatus(Object obj, String str, String str2, int i) {
        return PermissionInner.getPermissionStatus(getHostContext(obj), str, str2, i);
    }

    @SystemApi
    @NeedsPermission(any = {SystemPermission.GRANT_SENSITIVE_PERMISSIONS, SystemPermission.REVOKE_SENSITIVE_PERMISSIONS})
    public static void updatePermissionStatus(Object obj, String str, String str2, int i, int i2, int i3) {
        PermissionInner.updatePermissionStatus(getHostContext(obj), str, str2, i, i2, i3);
    }

    @SystemApi
    public static boolean isPermissionDiscarded(PermissionDef permissionDef) {
        return PermissionInner.isPermissionDiscarded(permissionDef);
    }

    @SystemApi
    @NeedsPermission(SystemPermission.MANAGE_APP_POLICY)
    public static void setPermissionState(Object obj, String str, int i, int i2) {
        PermissionInner.setPermissionState(getHostContext(obj), str, i, i2);
    }

    public static boolean canRequestPermissionFromRemote(String str, String str2) {
        return DistributedPermissionManager.getDefault().canRequestPermissionFromRemote(PermissionConversion.getAosPermissionNameIfPossible(str), str2);
    }

    public static void requestPermissionsFromRemote(final String[] strArr, final OnRequestPermissionsResult onRequestPermissionsResult, String str, String str2, int i) {
        if (onRequestPermissionsResult == null) {
            HiLog.error(LABEL, "requestPermissionsFromRemote is null!", new Object[0]);
            return;
        }
        AnonymousClass1 r2 = new DistributedPermissionManager.IRequestPermissionsResult() {
            /* class ohos.security.permission.Permission.AnonymousClass1 */

            public void onResult(String str, String[] strArr, int[] iArr) {
                OnRequestPermissionsResult.this.onResult(str, strArr, iArr);
            }

            public void onCancel(String str, String[] strArr) {
                OnRequestPermissionsResult.this.onCancel(str, strArr);
            }

            public void onTimeOut(String str, String[] strArr) {
                OnRequestPermissionsResult.this.onTimeOut(str, strArr);
            }
        };
        DistributedPermissionManager.getDefault().requestPermissionsFromRemote(getAosPermissions(strArr), r2, str, str2, i);
    }

    @SystemApi
    @NeedsPermission(SystemPermission.MANAGE_DISTRIBUTED_PERMISSION)
    public static void grantSensitivePermissionToRemoteApp(String str, String str2, int i) {
        DistributedPermissionManager.getDefault().grantSensitivePermissionToRemoteApp(PermissionConversion.getAosPermissionNameIfPossible(str), str2, i);
    }

    public static int verifyPermissionFromRemote(String str, String str2, String str3) {
        return DistributedPermissionManager.getDefault().verifyPermissionFromRemote(PermissionConversion.getAosPermissionNameIfPossible(str), str2, str3);
    }

    public static int verifySelfPermissionFromRemote(String str, String str2) {
        return DistributedPermissionManager.getDefault().verifySelfPermissionFromRemote(PermissionConversion.getAosPermissionNameIfPossible(str), str2);
    }

    @SystemApi
    public static int verifyPermissionAndState(Object obj, String str, String str2) {
        return PermissionInner.verifyPermissionAndState(getHostContext(obj), str, str2);
    }

    private static Object getHostContext(Object obj) {
        if (obj == null) {
            HiLog.error(LABEL, "the param in should not be null!", new Object[0]);
            return null;
        } else if (!(obj instanceof Context)) {
            HiLog.error(LABEL, "wrong context param input!", new Object[0]);
            return null;
        } else {
            Object hostContext = ((Context) obj).getHostContext();
            if (hostContext != null) {
                return hostContext;
            }
            HiLog.error(LABEL, "get context failed", new Object[0]);
            return null;
        }
    }

    private static String[] getAosPermissions(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            HiLog.error(LABEL, "permissions is empty", new Object[0]);
            return strArr;
        }
        String[] strArr2 = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            strArr2[i] = PermissionConversion.getAosPermissionNameIfPossible(strArr[i]);
        }
        return strArr2;
    }

    @SystemApi
    public static String getPermissionUsagesInfo(Context context, String str, String[] strArr) {
        if (!(context instanceof Context)) {
            HiLog.error(LABEL, "applicationContext param error", new Object[0]);
            return null;
        }
        AppContextAdapter.getInstance().init(getHostContext(context));
        return new PermissionReasonAdapter().getPermissionUsagesInfo(str, strArr).orElse(null);
    }
}
