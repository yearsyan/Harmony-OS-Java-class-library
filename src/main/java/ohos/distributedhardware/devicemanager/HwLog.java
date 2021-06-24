package ohos.distributedhardware.devicemanager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import ohos.annotation.SystemApi;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

@SystemApi
public final class HwLog {
    private static final int DOMAIN = 218120448;
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(0, DOMAIN, TAG);
    private static final int LOG_APP = 0;
    private static final String TAG = "DHMgr";

    public static void error(String str, String str2) {
        HiLog.error(LABEL_LOG, "%{public}s:%{public}s", str, str2);
    }

    public static void error(String str, String str2, Throwable th) {
        HiLog.error(LABEL_LOG, "%{public}s:%{public}s\n%{public}s", str, str2, getStackTrace(th));
    }

    public static void warn(String str, String str2) {
        HiLog.warn(LABEL_LOG, "%{public}s:%{public}s", str, str2);
    }

    public static void warn(String str, String str2, Throwable th) {
        HiLog.warn(LABEL_LOG, "%{public}s:%{public}s\n%{public}s", str, str2, getStackTrace(th));
    }

    public static void info(String str, String str2) {
        HiLog.info(LABEL_LOG, "%{public}s:%{public}s", str, str2);
    }

    public static void info(String str, String str2, Throwable th) {
        HiLog.info(LABEL_LOG, "%{public}s:%{public}s\n%{public}s", str, str2, getStackTrace(th));
    }

    public static void debug(String str, String str2) {
        HiLog.debug(LABEL_LOG, "%{public}s:%{public}s", str, str2);
    }

    public static void debug(String str, String str2, Throwable th) {
        HiLog.debug(LABEL_LOG, "%{public}s:%{public}s\n%{public}s", str, str2, getStackTrace(th));
    }

    private static String getStackTrace(Throwable th) {
        if (th == null) {
            return "";
        }
        for (Throwable th2 = th; th2 != null; th2 = th2.getCause()) {
            if (th2 instanceof UnknownHostException) {
                return "";
            }
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        printWriter.flush();
        return stringWriter.toString();
    }
}
