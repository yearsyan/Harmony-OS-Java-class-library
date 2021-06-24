package com.huawei.ace.runtime;

public final class LibraryLoader {
    private static final String ANDROID_LIB_NAME = "ace_android.z";
    private static final Object JNI_LOAD_LOCK = new Object();
    private static final String LIB_NAME = "ace.z";
    private static final String LIB_NAME_DEBUG = "ace_debug.z";
    private static final String LOG_TAG = "LibraryLoader";
    private static volatile boolean jniLoaded = false;
    private static volatile boolean loadDebugSo = false;
    private static volatile boolean useWatchLib = false;

    private LibraryLoader() {
    }

    public static void setUseWatchLib() {
        synchronized (JNI_LOAD_LOCK) {
            useWatchLib = true;
        }
    }

    public static void setLoadDebugSo() {
        synchronized (JNI_LOAD_LOCK) {
            loadDebugSo = true;
        }
    }

    private static void loadAndroidJniLibrary() {
        try {
            ALog.i(LOG_TAG, "Load android ace lib");
            System.loadLibrary(ANDROID_LIB_NAME);
            jniLoaded = true;
        } catch (UnsatisfiedLinkError e) {
            ALog.e(LOG_TAG, "Could not load android ace lib. Exception: " + e.getMessage());
        }
        ALog.i(LOG_TAG, "Load android ace lib ok");
    }

    static boolean loadJniLibrary() {
        if (jniLoaded) {
            ALog.i(LOG_TAG, "Has loaded ace lib");
            return true;
        }
        synchronized (JNI_LOAD_LOCK) {
            if (jniLoaded) {
                ALog.i(LOG_TAG, "Has loaded ace lib");
                return true;
            }
            try {
                ALog.i(LOG_TAG, "Load ace lib");
                if (!loadDebugSo || !useWatchLib) {
                    System.loadLibrary(LIB_NAME);
                } else {
                    System.loadLibrary(LIB_NAME_DEBUG);
                }
                jniLoaded = true;
            } catch (UnsatisfiedLinkError e) {
                ALog.w(LOG_TAG, "Could not load ace lib. Exception: " + e.getMessage());
                loadAndroidJniLibrary();
            }
            ALog.i(LOG_TAG, "Load ace lib ok");
            return jniLoaded;
        }
    }
}
