package ohos.global.resource.utils;

import ohos.hiviewdfx.HiLogLabel;

public class ZipFileUtils {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218111488, "ZipFileUtils");
    private static final int MAX_FILE_LENGTH = 10485760;

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0060, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0061, code lost:
        $closeResource(r1, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0064, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0067, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0068, code lost:
        if (r5 != null) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x006a, code lost:
        $closeResource(r6, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x006d, code lost:
        throw r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<java.lang.String> getContentFromZip(java.util.zip.ZipFile r5, java.lang.String r6) throws java.lang.IllegalStateException {
        /*
        // Method dump skipped, instructions count: 129
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.resource.utils.ZipFileUtils.getContentFromZip(java.util.zip.ZipFile, java.lang.String):java.util.Optional");
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
}
