package ohos.bundlemgr.webability;

import android.content.ContentResolver;
import android.content.Context;
import ohos.appexecfwk.utils.AppLog;
import ohos.hiviewdfx.HiLogLabel;

public class WebPackagesResolver {
    private static final String APP_DATA_PATH_COLUMN_NAME = "app_load_cache_path";
    private static final String APP_ICON_COLUMN_NAME = "app_icon";
    private static final String APP_NAME_COLUMN_NAME = "app_name";
    private static final String APP_TYPE_COLUMN_NAME = "app_type";
    private static final String PACKAGE_NAME_COLUMN_NAME = "app_package_name";
    private static final String PACKAGE_SELECTION_CLAUSE = "app_package_name=?";
    private static final String[] PROJECTION = {PACKAGE_NAME_COLUMN_NAME, APP_NAME_COLUMN_NAME, APP_ICON_COLUMN_NAME, "app_type", APP_DATA_PATH_COLUMN_NAME};
    private static final String TAG = "WebPackagesResolver";
    private static final String WEB_ABILITY_CONTENT_URI = "content://com.huawei.fastapp.provider/installed_app_info/";
    private static final HiLogLabel WEB_RESOLVER_LABEL = new HiLogLabel(3, 218108160, TAG);
    private ContentResolver contentResolver = null;

    public WebPackagesResolver(Context context) {
        if (context != null) {
            this.contentResolver = context.getContentResolver();
        } else {
            AppLog.e(WEB_RESOLVER_LABEL, "Context is null", new Object[0]);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0093, code lost:
        if (r3 != null) goto L_0x00aa;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00a8, code lost:
        if (0 == 0) goto L_0x00ad;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00aa, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x00ad, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.bundlemgr.webability.WebPackageInfo> getPackages(java.lang.String r13) {
        /*
        // Method dump skipped, instructions count: 180
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bundlemgr.webability.WebPackagesResolver.getPackages(java.lang.String):java.util.List");
    }
}
