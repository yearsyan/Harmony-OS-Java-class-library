package ohos.miscservices.download;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import java.io.File;
import ohos.abilityshell.utils.AbilityContextUtils;
import ohos.net.UriConverter;
import ohos.utils.net.Uri;

public class DownloadUtils {
    private static Handler asyncHandler;
    private static HandlerThread asyncThread;

    public static int getReasonCode(long j) {
        if ((400 <= j && j < 488) || (500 <= j && j < 600)) {
            return (int) j;
        }
        int i = (int) j;
        if (i == 1) {
            return 301;
        }
        if (i == 2) {
            return 302;
        }
        if (i == 3) {
            return 303;
        }
        if (i == 1001) {
            return 1001;
        }
        if (i == 1002) {
            return DownloadSession.ERROR_UNHANDLED_HTTP_CODE;
        }
        switch (i) {
            case 1004:
                return DownloadSession.ERROR_HTTP_DATA_ERROR;
            case 1005:
                return DownloadSession.ERROR_TOO_MANY_REDIRECTS;
            case 1006:
                return DownloadSession.ERROR_INSUFFICIENT_SPACE;
            case 1007:
                return DownloadSession.ERROR_DEVICE_NOT_FOUND;
            case 1008:
                return DownloadSession.ERROR_CANNOT_RESUME;
            case 1009:
                return DownloadSession.ERROR_FILE_ALREADY_EXISTS;
            default:
                return 304;
        }
    }

    public static Context getAPlatformContext(ohos.app.Context context) {
        return (Context) AbilityContextUtils.getAndroidContext(context);
    }

    public static Uri createDownloadPathInPrivateDir(ohos.app.Context context, String str, String str2) {
        Context aPlatformContext = getAPlatformContext(context);
        if (aPlatformContext == null) {
            return Uri.parse("");
        }
        File externalFilesDir = aPlatformContext.getExternalFilesDir(str);
        if (externalFilesDir != null) {
            if (!externalFilesDir.exists()) {
                if (!externalFilesDir.mkdirs()) {
                    throw new IllegalStateException("Failed to create directory");
                }
            } else if (!externalFilesDir.isDirectory()) {
                throw new IllegalStateException("Already exists.It is not a directory");
            }
            return UriConverter.convertToZidaneUri(createDestinationFromBase(externalFilesDir, str2));
        }
        throw new IllegalStateException("Failed to get files directory");
    }

    private static android.net.Uri createDestinationFromBase(File file, String str) {
        if (str != null) {
            return android.net.Uri.withAppendedPath(android.net.Uri.fromFile(file), str);
        }
        throw new NullPointerException("subPath cannot be null");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0041, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0042, code lost:
        if (r1 != null) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0048, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0049, code lost:
        r5.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004c, code lost:
        throw r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static ohos.utils.net.Uri createDownloadPathInPublicDir(java.lang.String r5, java.lang.String r6) {
        /*
        // Method dump skipped, instructions count: 133
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.download.DownloadUtils.createDownloadPathInPublicDir(java.lang.String, java.lang.String):ohos.utils.net.Uri");
    }

    public static synchronized Handler getAsyncHandler() {
        Handler handler;
        synchronized (DownloadUtils.class) {
            if (asyncThread == null) {
                asyncThread = new HandlerThread("asyncThread", 10);
                asyncThread.start();
                if (asyncThread.getLooper() != null) {
                    asyncHandler = new Handler(asyncThread.getLooper());
                }
            }
            handler = asyncHandler;
        }
        return handler;
    }
}
