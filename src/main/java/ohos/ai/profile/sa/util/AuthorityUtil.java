package ohos.ai.profile.sa.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Process;

public final class AuthorityUtil {
    private static final int MAX_SYSTEM_APP_UID = 9999;
    private static final int MIN_SYSTEM_APP_UID = 0;
    private static final String TAG = "AuthorityUtil";

    private AuthorityUtil() {
    }

    public static boolean checkAuthority(Context context) {
        if (context == null) {
            LogUtil.error(TAG, "context is null", new Object[0]);
            return false;
        } else if (checkSystemApp()) {
            return true;
        } else {
            return checkSignMatch(context);
        }
    }

    private static boolean checkSystemApp() {
        int callingUid = Binder.getCallingUid();
        return callingUid <= MAX_SYSTEM_APP_UID && callingUid >= 0;
    }

    private static boolean checkSignMatch(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            LogUtil.error(TAG, "packageManager is null", new Object[0]);
            return false;
        } else if (packageManager.checkSignatures(Binder.getCallingUid(), Process.myUid()) == 0) {
            return true;
        } else {
            LogUtil.error(TAG, "the application has no platform signature", new Object[0]);
            return false;
        }
    }
}
