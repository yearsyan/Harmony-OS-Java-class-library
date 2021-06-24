package ohos.ai.profile.sa.util;

import java.util.HashSet;
import java.util.Set;

public abstract class SensitiveUtil {
    private static final Set<String> SENSITIVE_EXCEPTION_SET = new HashSet();

    static {
        SENSITIVE_EXCEPTION_SET.add("FileNotFoundException");
        SENSITIVE_EXCEPTION_SET.add("JarException");
        SENSITIVE_EXCEPTION_SET.add("MissingResourceException");
        SENSITIVE_EXCEPTION_SET.add("NotOwnerException");
        SENSITIVE_EXCEPTION_SET.add("ConcurrentModificationException");
        SENSITIVE_EXCEPTION_SET.add("InsufficientResourcesException");
        SENSITIVE_EXCEPTION_SET.add("BindException");
        SENSITIVE_EXCEPTION_SET.add("OutOfMemoryError");
        SENSITIVE_EXCEPTION_SET.add("SQLException");
    }

    public static String getMessage(Throwable th) {
        if (th == null || th.getClass() == null || SENSITIVE_EXCEPTION_SET.contains(th.getClass().getSimpleName())) {
            return "";
        }
        return th.getMessage();
    }

    public static String getSimpleName(Throwable th) {
        if (th == null || th.getClass() == null) {
            return "";
        }
        String simpleName = th.getClass().getSimpleName();
        if (SENSITIVE_EXCEPTION_SET.contains(simpleName)) {
            return "";
        }
        return simpleName;
    }
}
