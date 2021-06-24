package ohos.security.permission.infrastructure.utils;

import ohos.hiviewdfx.HiLogLabel;

public final class GzipUtil {
    private static final HiLogLabel LABEL = HiLogLabelUtil.INFRA.newHiLogLabel(TAG);
    private static final String TAG = "GzipUtil";

    private GzipUtil() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0025, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0026, code lost:
        $closeResource(r4, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0029, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x002c, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x002d, code lost:
        $closeResource(r4, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0030, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] compress(byte[] r4) {
        /*
            r0 = 0
            if (r4 == 0) goto L_0x003d
            int r1 = r4.length
            if (r1 != 0) goto L_0x0007
            goto L_0x003d
        L_0x0007:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0031 }
            r1.<init>()     // Catch:{ IOException -> 0x0031 }
            java.util.zip.GZIPOutputStream r2 = new java.util.zip.GZIPOutputStream     // Catch:{ all -> 0x002a }
            r2.<init>(r1)     // Catch:{ all -> 0x002a }
            r2.write(r4)     // Catch:{ all -> 0x0023 }
            r2.finish()     // Catch:{ all -> 0x0023 }
            byte[] r4 = r1.toByteArray()     // Catch:{ all -> 0x0023 }
            r3 = 0
            $closeResource(r3, r2)
            $closeResource(r3, r1)
            return r4
        L_0x0023:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0025 }
        L_0x0025:
            r3 = move-exception
            $closeResource(r4, r2)
            throw r3
        L_0x002a:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x002c }
        L_0x002c:
            r2 = move-exception
            $closeResource(r4, r1)
            throw r2
        L_0x0031:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.security.permission.infrastructure.utils.GzipUtil.LABEL
            java.lang.Object[] r1 = new java.lang.Object[r0]
            java.lang.String r2 = "compress: fail"
            ohos.hiviewdfx.HiLog.error(r4, r2, r1)
            byte[] r4 = new byte[r0]
            return r4
        L_0x003d:
            byte[] r4 = new byte[r0]
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.security.permission.infrastructure.utils.GzipUtil.compress(byte[]):byte[]");
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

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0035, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0036, code lost:
        $closeResource(r3, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0039, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x003c, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x003d, code lost:
        $closeResource(r5, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0040, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0043, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0044, code lost:
        $closeResource(r5, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0047, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] uncompress(byte[] r5) {
        /*
            r0 = 0
            if (r5 == 0) goto L_0x0054
            int r1 = r5.length
            if (r1 != 0) goto L_0x0007
            goto L_0x0054
        L_0x0007:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0048 }
            r1.<init>()     // Catch:{ IOException -> 0x0048 }
            java.io.ByteArrayInputStream r2 = new java.io.ByteArrayInputStream     // Catch:{ all -> 0x0041 }
            r2.<init>(r5)     // Catch:{ all -> 0x0041 }
            java.util.zip.GZIPInputStream r5 = new java.util.zip.GZIPInputStream     // Catch:{ all -> 0x003a }
            r5.<init>(r2)     // Catch:{ all -> 0x003a }
            r3 = 8192(0x2000, float:1.14794E-41)
            byte[] r3 = new byte[r3]     // Catch:{ all -> 0x0033 }
        L_0x001a:
            int r4 = r5.read(r3)     // Catch:{ all -> 0x0033 }
            if (r4 < 0) goto L_0x0024
            r1.write(r3, r0, r4)     // Catch:{ all -> 0x0033 }
            goto L_0x001a
        L_0x0024:
            byte[] r3 = r1.toByteArray()     // Catch:{ all -> 0x0033 }
            r4 = 0
            $closeResource(r4, r5)
            $closeResource(r4, r2)
            $closeResource(r4, r1)
            return r3
        L_0x0033:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x0035 }
        L_0x0035:
            r4 = move-exception
            $closeResource(r3, r5)
            throw r4
        L_0x003a:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x003c }
        L_0x003c:
            r3 = move-exception
            $closeResource(r5, r2)
            throw r3
        L_0x0041:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x0043 }
        L_0x0043:
            r2 = move-exception
            $closeResource(r5, r1)
            throw r2
        L_0x0048:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.security.permission.infrastructure.utils.GzipUtil.LABEL
            java.lang.Object[] r1 = new java.lang.Object[r0]
            java.lang.String r2 = "uncompress: fail"
            ohos.hiviewdfx.HiLog.error(r5, r2, r1)
            byte[] r5 = new byte[r0]
            return r5
        L_0x0054:
            byte[] r5 = new byte[r0]
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.security.permission.infrastructure.utils.GzipUtil.uncompress(byte[]):byte[]");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0035, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0036, code lost:
        $closeResource(r3, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0039, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x003c, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x003d, code lost:
        $closeResource(r5, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0040, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0043, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0044, code lost:
        $closeResource(r5, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0047, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] inflater(byte[] r5) {
        /*
            r0 = 0
            if (r5 == 0) goto L_0x0054
            int r1 = r5.length
            if (r1 != 0) goto L_0x0007
            goto L_0x0054
        L_0x0007:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0048 }
            r1.<init>()     // Catch:{ IOException -> 0x0048 }
            java.io.ByteArrayInputStream r2 = new java.io.ByteArrayInputStream     // Catch:{ all -> 0x0041 }
            r2.<init>(r5)     // Catch:{ all -> 0x0041 }
            java.util.zip.InflaterInputStream r5 = new java.util.zip.InflaterInputStream     // Catch:{ all -> 0x003a }
            r5.<init>(r2)     // Catch:{ all -> 0x003a }
            r3 = 8192(0x2000, float:1.14794E-41)
            byte[] r3 = new byte[r3]     // Catch:{ all -> 0x0033 }
        L_0x001a:
            int r4 = r5.read(r3)     // Catch:{ all -> 0x0033 }
            if (r4 < 0) goto L_0x0024
            r1.write(r3, r0, r4)     // Catch:{ all -> 0x0033 }
            goto L_0x001a
        L_0x0024:
            byte[] r3 = r1.toByteArray()     // Catch:{ all -> 0x0033 }
            r4 = 0
            $closeResource(r4, r5)
            $closeResource(r4, r2)
            $closeResource(r4, r1)
            return r3
        L_0x0033:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x0035 }
        L_0x0035:
            r4 = move-exception
            $closeResource(r3, r5)
            throw r4
        L_0x003a:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x003c }
        L_0x003c:
            r3 = move-exception
            $closeResource(r5, r2)
            throw r3
        L_0x0041:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x0043 }
        L_0x0043:
            r2 = move-exception
            $closeResource(r5, r1)
            throw r2
        L_0x0048:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.security.permission.infrastructure.utils.GzipUtil.LABEL
            java.lang.Object[] r1 = new java.lang.Object[r0]
            java.lang.String r2 = "inflater: fail"
            ohos.hiviewdfx.HiLog.error(r5, r2, r1)
            byte[] r5 = new byte[r0]
            return r5
        L_0x0054:
            byte[] r5 = new byte[r0]
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.security.permission.infrastructure.utils.GzipUtil.inflater(byte[]):byte[]");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0025, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0026, code lost:
        $closeResource(r4, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0029, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x002c, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x002d, code lost:
        $closeResource(r4, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0030, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] deflater(byte[] r4) {
        /*
            r0 = 0
            if (r4 == 0) goto L_0x003d
            int r1 = r4.length
            if (r1 != 0) goto L_0x0007
            goto L_0x003d
        L_0x0007:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0031 }
            r1.<init>()     // Catch:{ IOException -> 0x0031 }
            java.util.zip.DeflaterOutputStream r2 = new java.util.zip.DeflaterOutputStream     // Catch:{ all -> 0x002a }
            r2.<init>(r1)     // Catch:{ all -> 0x002a }
            r2.write(r4)     // Catch:{ all -> 0x0023 }
            r2.finish()     // Catch:{ all -> 0x0023 }
            byte[] r4 = r1.toByteArray()     // Catch:{ all -> 0x0023 }
            r3 = 0
            $closeResource(r3, r2)
            $closeResource(r3, r1)
            return r4
        L_0x0023:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0025 }
        L_0x0025:
            r3 = move-exception
            $closeResource(r4, r2)
            throw r3
        L_0x002a:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x002c }
        L_0x002c:
            r2 = move-exception
            $closeResource(r4, r1)
            throw r2
        L_0x0031:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.security.permission.infrastructure.utils.GzipUtil.LABEL
            java.lang.Object[] r1 = new java.lang.Object[r0]
            java.lang.String r2 = "deflater: fail"
            ohos.hiviewdfx.HiLog.error(r4, r2, r1)
            byte[] r4 = new byte[r0]
            return r4
        L_0x003d:
            byte[] r4 = new byte[r0]
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.security.permission.infrastructure.utils.GzipUtil.deflater(byte[]):byte[]");
    }
}
