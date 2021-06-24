package ohos.backgroundtaskmgr;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class BackgroundTaskManagerInner {
    private static final int BGTAK_WORK_EXEMPTION_MAX = 1;
    public static final int BGTASK_WORK_EXEMPTION_BATTERY = 1;
    private static final int LOG_DOMAIN = 218109696;
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, LOG_DOMAIN, TAG);
    private static final String TAG = "BackgroundTaskManagerInner";

    private static native boolean nativeIsWorkScheduleAllowed(int i, String str, int i2);

    private static native void nativeNoteWorkStart(int i, String str);

    private static native void nativeNoteWorkStop(int i, String str);

    static {
        try {
            System.loadLibrary("bgtaskmgrclient_jni.z");
            HiLog.info(LOG_LABEL, "Load jni library", new Object[0]);
        } catch (NullPointerException | UnsatisfiedLinkError unused) {
            HiLog.error(LOG_LABEL, "Failed to load jni library", new Object[0]);
        }
    }

    private BackgroundTaskManagerInner() {
    }

    public static boolean isWorkScheduleAllowed(int i, String str, int i2) {
        if (str == null || str.isEmpty() || i2 > 1) {
            return false;
        }
        return nativeIsWorkScheduleAllowed(i, str, i2);
    }

    public static void noteWorkStart(int i, String str) {
        if (str != null && !str.isEmpty()) {
            nativeNoteWorkStart(i, str);
        }
    }

    public static void noteWorkStop(int i, String str) {
        if (str != null && !str.isEmpty()) {
            nativeNoteWorkStop(i, str);
        }
    }
}
