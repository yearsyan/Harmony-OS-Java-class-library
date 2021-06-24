package ark.system;

import java.lang.reflect.InvocationTargetException;

public class HotFixClassLoader {
    private static final String EXTENDED_CLASSLOADER_PATH = "com.huawei.ark.classloader.ExtendedClassLoaderHelper";
    private static final String MAPLE_RUNTIME_PROPERTY = "MAPLE_RUNTIME";

    public static boolean applyPatch(ClassLoader classLoader, String str) throws UnsupportedOperationException {
        if (System.getenv(MAPLE_RUNTIME_PROPERTY) != null) {
            try {
                Object invoke = Class.forName(EXTENDED_CLASSLOADER_PATH).getDeclaredMethod("applyPatch", ClassLoader.class, String.class).invoke(null, classLoader, str);
                Boolean bool = false;
                if (invoke instanceof Boolean) {
                    bool = (Boolean) invoke;
                }
                return bool.booleanValue();
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException unused) {
                throw new UnsupportedOperationException("Current runtime environment is not ark runtime, can't use HotFixClassLoader");
            }
        } else {
            throw new UnsupportedOperationException("Current runtime environment is not ark runtime, can't use HotFixClassLoader");
        }
    }
}
