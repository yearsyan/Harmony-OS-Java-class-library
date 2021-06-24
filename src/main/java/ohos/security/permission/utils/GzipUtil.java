package ohos.security.permission.utils;

import ohos.hiviewdfx.HiLogLabel;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;

@Deprecated
public final class GzipUtil {
    private static final HiLogLabel LABEL = HiLogLabelUtil.INNER_KIT.newHiLogLabel(TAG);
    private static final String TAG = "GzipUtil";

    private GzipUtil() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
        $closeResource(r4, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0032, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0035, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0036, code lost:
        $closeResource(r4, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0039, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] compress(java.lang.String r4) {
        /*
            r0 = 0
            if (r4 == 0) goto L_0x0046
            boolean r1 = r4.isEmpty()
            if (r1 == 0) goto L_0x000a
            goto L_0x0046
        L_0x000a:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x003a }
            r1.<init>()     // Catch:{ IOException -> 0x003a }
            java.util.zip.GZIPOutputStream r2 = new java.util.zip.GZIPOutputStream     // Catch:{ all -> 0x0033 }
            r2.<init>(r1)     // Catch:{ all -> 0x0033 }
            java.nio.charset.Charset r3 = java.nio.charset.StandardCharsets.UTF_8     // Catch:{ all -> 0x002c }
            byte[] r4 = r4.getBytes(r3)     // Catch:{ all -> 0x002c }
            r2.write(r4)     // Catch:{ all -> 0x002c }
            r2.finish()     // Catch:{ all -> 0x002c }
            byte[] r4 = r1.toByteArray()     // Catch:{ all -> 0x002c }
            r3 = 0
            $closeResource(r3, r2)
            $closeResource(r3, r1)
            return r4
        L_0x002c:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x002e }
        L_0x002e:
            r3 = move-exception
            $closeResource(r4, r2)
            throw r3
        L_0x0033:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0035 }
        L_0x0035:
            r2 = move-exception
            $closeResource(r4, r1)
            throw r2
        L_0x003a:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.security.permission.utils.GzipUtil.LABEL
            java.lang.Object[] r1 = new java.lang.Object[r0]
            java.lang.String r2 = "compress: fail"
            ohos.hiviewdfx.HiLog.error(r4, r2, r1)
            byte[] r4 = new byte[r0]
            return r4
        L_0x0046:
            byte[] r4 = new byte[r0]
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.security.permission.utils.GzipUtil.compress(java.lang.String):byte[]");
    }

    private static /* synthetic */ void $closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        } else {
            autoCloseable.close();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003d, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x003e, code lost:
        $closeResource(r4, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0041, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0044, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0045, code lost:
        $closeResource(r6, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0048, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x004b, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x004c, code lost:
        $closeResource(r6, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x004f, code lost:
        throw r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String uncompress(byte[] r6) {
        /*
            java.lang.String r0 = ""
            if (r6 == 0) goto L_0x0059
            int r1 = r6.length
            if (r1 != 0) goto L_0x0008
            goto L_0x0059
        L_0x0008:
            r1 = 0
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0050 }
            r2.<init>()     // Catch:{ IOException -> 0x0050 }
            java.io.ByteArrayInputStream r3 = new java.io.ByteArrayInputStream     // Catch:{ all -> 0x0049 }
            r3.<init>(r6)     // Catch:{ all -> 0x0049 }
            java.util.zip.GZIPInputStream r6 = new java.util.zip.GZIPInputStream     // Catch:{ all -> 0x0042 }
            r6.<init>(r3)     // Catch:{ all -> 0x0042 }
            r4 = 8192(0x2000, float:1.14794E-41)
            byte[] r4 = new byte[r4]     // Catch:{ all -> 0x003b }
        L_0x001c:
            int r5 = r6.read(r4)     // Catch:{ all -> 0x003b }
            if (r5 < 0) goto L_0x0026
            r2.write(r4, r1, r5)     // Catch:{ all -> 0x003b }
            goto L_0x001c
        L_0x0026:
            java.nio.charset.Charset r4 = java.nio.charset.StandardCharsets.UTF_8     // Catch:{ all -> 0x003b }
            java.lang.String r4 = r4.name()     // Catch:{ all -> 0x003b }
            java.lang.String r4 = r2.toString(r4)     // Catch:{ all -> 0x003b }
            r5 = 0
            $closeResource(r5, r6)
            $closeResource(r5, r3)
            $closeResource(r5, r2)
            return r4
        L_0x003b:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x003d }
        L_0x003d:
            r5 = move-exception
            $closeResource(r4, r6)
            throw r5
        L_0x0042:
            r6 = move-exception
            throw r6     // Catch:{ all -> 0x0044 }
        L_0x0044:
            r4 = move-exception
            $closeResource(r6, r3)
            throw r4
        L_0x0049:
            r6 = move-exception
            throw r6     // Catch:{ all -> 0x004b }
        L_0x004b:
            r3 = move-exception
            $closeResource(r6, r2)
            throw r3
        L_0x0050:
            ohos.hiviewdfx.HiLogLabel r6 = ohos.security.permission.utils.GzipUtil.LABEL
            java.lang.Object[] r1 = new java.lang.Object[r1]
            java.lang.String r2 = "uncompress: fail"
            ohos.hiviewdfx.HiLog.error(r6, r2, r1)
        L_0x0059:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.security.permission.utils.GzipUtil.uncompress(byte[]):java.lang.String");
    }
}
