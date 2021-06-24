package ohos.hiviewdfx;

public class FreezeDetectorUtils {
    public static final HiLogLabel LOG_TAG = new HiLogLabel(3, 218115333, "FreezeDetector_Java");
    private static volatile FreezeDetectorUtils freezeDetectorUtils = null;

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0011 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean loadJniLibrary() {
        /*
            ohos.hiviewdfx.FreezeDetectorUtils r0 = ohos.hiviewdfx.FreezeDetectorUtils.freezeDetectorUtils
            r1 = 1
            if (r0 != 0) goto L_0x001f
            java.lang.Class<ohos.hiviewdfx.FreezeDetectorUtils> r0 = ohos.hiviewdfx.FreezeDetectorUtils.class
            monitor-enter(r0)
            r2 = 0
            java.lang.String r3 = "freezedetector_jni.z"
            java.lang.System.loadLibrary(r3)     // Catch:{ UnsatisfiedLinkError -> 0x0011 }
            goto L_0x001b
        L_0x000f:
            r1 = move-exception
            goto L_0x001d
        L_0x0011:
            ohos.hiviewdfx.HiLogLabel r1 = ohos.hiviewdfx.FreezeDetectorUtils.LOG_TAG     // Catch:{ all -> 0x000f }
            java.lang.String r3 = "Could not load libfreezedetector_jni.z.so"
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ all -> 0x000f }
            ohos.hiviewdfx.HiLog.error(r1, r3, r4)     // Catch:{ all -> 0x000f }
            r1 = r2
        L_0x001b:
            monitor-exit(r0)     // Catch:{ all -> 0x000f }
            goto L_0x001f
        L_0x001d:
            monitor-exit(r0)     // Catch:{ all -> 0x000f }
            throw r1
        L_0x001f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.hiviewdfx.FreezeDetectorUtils.loadJniLibrary():boolean");
    }
}
