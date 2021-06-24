package ohos.appexecfwk.utils;

import ohos.aafwk.content.Intent;
import ohos.devtools.JLog;
import ohos.global.icu.text.DateFormat;

public class JLogUtil {
    public static void debugLog(int i, String str, String str2, long j) {
        long currentTimeMillis = System.currentTimeMillis();
        JLog.debug(i, str + "/" + str2 + " cost: " + (currentTimeMillis - j) + DateFormat.MINUTE_SECOND);
    }

    public static void printStartAbilityInfo(Intent intent, long j, int i) {
        JLog.printStartAbilityInfo(intent, j, i);
    }
}
