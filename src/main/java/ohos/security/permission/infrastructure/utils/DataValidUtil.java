package ohos.security.permission.infrastructure.utils;

import ohos.utils.StringUtils;

public class DataValidUtil {
    private static final int DUSER_ID_ANDROID = 126;
    private static final int DUSER_ID_HARMONY = 125;
    public static final int MAX_DEVICE_ID_LENGTH = 64;
    private static final int PER_USER_RANGE = 100000;

    public static boolean isUidValid(int i) {
        return i >= 0;
    }

    private DataValidUtil() {
    }

    public static boolean isDuid(int i) {
        int i2 = i / 100000;
        return i2 >= 125 && i2 <= 126;
    }

    public static boolean isDeviceIdValid(String str) {
        if (!StringUtils.isEmpty(str) && str.length() <= 64) {
            return true;
        }
        return false;
    }
}
