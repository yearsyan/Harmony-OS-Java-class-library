package ohos.bundle;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import ohos.app.Context;
import ohos.appexecfwk.utils.AppLog;

public class ParseProfile {
    private static final String CONFIG_JSON = "config.json";
    private static final String HAP_SUFFIX = ".hap";

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0063, code lost:
        r7 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0065, code lost:
        r7 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0066, code lost:
        r1 = r2;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0065 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:17:0x0043] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static ohos.bundle.BundleInfo parse(ohos.app.Context r5, java.lang.String r6, int r7) {
        /*
        // Method dump skipped, instructions count: 173
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bundle.ParseProfile.parse(ohos.app.Context, java.lang.String, int):ohos.bundle.BundleInfo");
    }

    private static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                AppLog.e("ParseProfile::closeStream io close exception: %{public}s", e.getMessage());
            }
        }
    }

    private static String getFormattedPath(Context context, String str) {
        IOException e;
        String str2;
        if (context == null) {
            AppLog.e("ParseProfile::getFormattedPath context is null", new Object[0]);
            return "";
        } else if (str == null || str.isEmpty()) {
            AppLog.e("ParseProfile::getFormattedPath path is null or empty", new Object[0]);
            return "";
        } else {
            File file = new File(str);
            if (!isValidHapFile(file)) {
                AppLog.e("ParseProfile::getFormattedPath is not valid hap", new Object[0]);
                return "";
            }
            try {
                str2 = file.getCanonicalPath();
                try {
                    File dataDir = context.getDataDir();
                    if (dataDir == null) {
                        return "";
                    }
                    String canonicalPath = dataDir.getCanonicalPath();
                    AppLog.d("ParseProfile::getFormattedPath appPath = %{private}s", canonicalPath);
                    if (str2.startsWith(canonicalPath)) {
                        return str2;
                    }
                    return canonicalPath + str2;
                } catch (IOException e2) {
                    e = e2;
                    AppLog.e("ParseProfile::getFormattedPath exception: %{public}s", e.getMessage());
                    return str2;
                }
            } catch (IOException e3) {
                e = e3;
                str2 = "";
                AppLog.e("ParseProfile::getFormattedPath exception: %{public}s", e.getMessage());
                return str2;
            }
        }
    }

    private static boolean isValidHapFile(File file) {
        if (file.exists() && file.isFile() && file.canRead() && file.length() > 0 && file.getName().endsWith(HAP_SUFFIX)) {
            return true;
        }
        return false;
    }
}
