package ohos.data.distributed.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class DistFile extends File {
    private static final int BUFFER_LENGTH = 8192;
    private static final long serialVersionUID = -249758070224197024L;
    private final String path;

    public DistFile(String str) {
        super(str);
        this.path = str;
    }

    public boolean isDistFile() {
        String str = this.path;
        return str != null && str.startsWith(DistFileSystem.ROOT_DIST_PATH);
    }

    public String getDevice() {
        return isDistFile() ? DistFileSystem.getXattr(this.path, "location") : "";
    }

    public String getGroup() {
        return isDistFile() ? DistFileSystem.getXattr(this.path, "group") : "";
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0047, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0048, code lost:
        $closeResource(r5, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x004b, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x004e, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x004f, code lost:
        $closeResource(r4, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0052, code lost:
        throw r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean copyTo(ohos.data.distributed.file.DistFile r5) throws java.io.IOException {
        /*
            r4 = this;
            if (r5 == 0) goto L_0x005b
            boolean r0 = r4.isDirectory()
            if (r0 != 0) goto L_0x0053
            boolean r0 = r5.isDirectory()
            if (r0 != 0) goto L_0x0053
            boolean r0 = r4.exists()
            r1 = 0
            if (r0 != 0) goto L_0x0016
            return r1
        L_0x0016:
            boolean r0 = r5.exists()
            if (r0 != 0) goto L_0x0023
            boolean r0 = r5.createNewFile()
            if (r0 != 0) goto L_0x0023
            return r1
        L_0x0023:
            r0 = 8192(0x2000, float:1.14794E-41)
            byte[] r2 = new byte[r0]
            java.io.FileInputStream r3 = new java.io.FileInputStream
            r3.<init>(r4)
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ all -> 0x004c }
            r4.<init>(r5)     // Catch:{ all -> 0x004c }
        L_0x0031:
            int r5 = r3.read(r2, r1, r0)     // Catch:{ all -> 0x0045 }
            if (r5 <= 0) goto L_0x003c
            r4.write(r2, r1, r5)     // Catch:{ all -> 0x0045 }
            int r1 = r1 + r5
            goto L_0x0031
        L_0x003c:
            r5 = 1
            r0 = 0
            $closeResource(r0, r4)
            $closeResource(r0, r3)
            return r5
        L_0x0045:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x0047 }
        L_0x0047:
            r0 = move-exception
            $closeResource(r5, r4)
            throw r0
        L_0x004c:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x004e }
        L_0x004e:
            r5 = move-exception
            $closeResource(r4, r3)
            throw r5
        L_0x0053:
            java.io.IOException r4 = new java.io.IOException
            java.lang.String r5 = "Doesn't support to copy directory"
            r4.<init>(r5)
            throw r4
        L_0x005b:
            java.io.IOException r4 = new java.io.IOException
            java.lang.String r5 = "Invalid argument"
            r4.<init>(r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.distributed.file.DistFile.copyTo(ohos.data.distributed.file.DistFile):boolean");
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

    public DistFile getParentFile() {
        return fileToDistFile(super.getParentFile());
    }

    public DistFile[] listFiles() {
        return filesToDistFiles(super.listFiles());
    }

    @Override // java.io.File
    public DistFile[] listFiles(FileFilter fileFilter) {
        return filesToDistFiles(super.listFiles(fileFilter));
    }

    @Override // java.io.File
    public DistFile[] listFiles(FilenameFilter filenameFilter) {
        return filesToDistFiles(super.listFiles(filenameFilter));
    }

    private DistFile fileToDistFile(File file) {
        if (file == null) {
            return null;
        }
        return new DistFile(file.getPath());
    }

    private DistFile[] filesToDistFiles(File[] fileArr) {
        if (fileArr == null) {
            return new DistFile[0];
        }
        int length = fileArr.length;
        DistFile[] distFileArr = new DistFile[length];
        for (int i = 0; i < length; i++) {
            distFileArr[i] = fileToDistFile(fileArr[i]);
        }
        return distFileArr;
    }
}
