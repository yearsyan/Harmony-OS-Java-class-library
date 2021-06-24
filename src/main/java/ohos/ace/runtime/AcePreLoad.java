package ohos.ace.runtime;

import com.huawei.ace.runtime.ALog;

public final class AcePreLoad {
    private static final String ACE_CORE_LIB_NAME = "ace.z";
    private static final String TAG = "AcePreLoad";

    static {
        if (!isRunningInArk()) {
            try {
                System.loadLibrary(ACE_CORE_LIB_NAME);
            } catch (UnsatisfiedLinkError unused) {
                ALog.w(TAG, "Failed to preload ACE core library");
            }
        }
    }

    private AcePreLoad() {
    }

    private static boolean isRunningInArk() {
        return System.getenv("MAPLE_RUNTIME") != null;
    }
}
