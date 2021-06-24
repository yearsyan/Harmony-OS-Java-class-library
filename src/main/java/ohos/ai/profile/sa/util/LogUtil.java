package ohos.ai.profile.sa.util;

import ohos.ai.engine.utils.Constants;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public abstract class LogUtil {
    private static final String LOG_FORMAT = "%{public}s: %{public}s";
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, Constants.DOMAIN_ID, "DeviceProfileSA");

    public static void debug(String str, String str2, Object... objArr) {
        HiLog.debug(LOG_LABEL, LOG_FORMAT, str, str2, objArr);
    }

    public static void info(String str, String str2, Object... objArr) {
        HiLog.info(LOG_LABEL, LOG_FORMAT, str, str2, objArr);
    }

    public static void warn(String str, String str2, Object... objArr) {
        HiLog.warn(LOG_LABEL, LOG_FORMAT, str, str2, objArr);
    }

    public static void error(String str, String str2, Object... objArr) {
        HiLog.error(LOG_LABEL, LOG_FORMAT, str, str2, objArr);
    }
}
