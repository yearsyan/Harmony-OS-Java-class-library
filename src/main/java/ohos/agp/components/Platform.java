package ohos.agp.components;

public class Platform {
    public static final int PLATFORM_TYPE_ANDROID = 0;
    public static final int PLATFORM_TYPE_DARWIN = 3;
    public static final int PLATFORM_TYPE_LINUX = 2;
    public static final int PLATFORM_TYPE_WINDOWS = 1;

    private static native int nativeGetPlatformType();

    public static int getPlatformType() {
        return nativeGetPlatformType();
    }

    public static boolean isAndroid() {
        return getPlatformType() == 0;
    }

    public static boolean isWindows() {
        return getPlatformType() == 1;
    }

    public static boolean isLinux() {
        return getPlatformType() == 2;
    }

    public static boolean isDarwin() {
        return getPlatformType() == 3;
    }
}
