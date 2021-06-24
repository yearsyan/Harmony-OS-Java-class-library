package ohos.ai.profile.util;

import ohos.annotation.SystemApi;

@SystemApi
public abstract class TextUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
