package ohos.data.orm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import ohos.hiviewdfx.HiLogLabel;

public class Blob implements java.sql.Blob {
    static final HiLogLabel LABEL = new HiLogLabel(3, 218109520, "Blob");
    private byte[] binaryData;
    private boolean isClosed;
    private final Object lock;

    public Blob() {
        this.binaryData = new byte[0];
        this.isClosed = false;
        this.lock = new Object();
    }

    public Blob(byte[] bArr) {
        this.binaryData = new byte[0];
        this.isClosed = false;
        this.lock = new Object();
        if (bArr != null) {
            this.binaryData = Arrays.copyOf(bArr, bArr.length);
        }
    }

    private byte[] getBinaryData() {
        byte[] bArr;
        synchronized (this.lock) {
            bArr = this.binaryData;
        }
        return bArr;
    }

    private void checkClosed() {
        synchronized (this.lock) {
            if (this.isClosed) {
                throw new IllegalStateException("The blob is not initialized");
            }
        }
    }

    @Override // java.sql.Blob
    public long length() {
        checkClosed();
        return (long) getBinaryData().length;
    }

    @Override // java.sql.Blob
    public byte[] getBytes(long j, int i) {
        checkClosed();
        if (j >= 1) {
            int i2 = ((int) j) - 1;
            byte[] bArr = this.binaryData;
            if (i2 > bArr.length) {
                throw new IllegalArgumentException();
            } else if (i2 + i <= bArr.length) {
                byte[] bArr2 = new byte[i];
                System.arraycopy(getBinaryData(), i2, bArr2, 0, i);
                return bArr2;
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override // java.sql.Blob
    public InputStream getBinaryStream() {
        checkClosed();
        return new ByteArrayInputStream(getBinaryData());
    }

    @Override // java.sql.Blob
    public long position(byte[] bArr, long j) {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Blob
    public long position(java.sql.Blob blob, long j) {
        throw new UnsupportedOperationException();
    }

    @Override // java.sql.Blob
    public int setBytes(long j, byte[] bArr) {
        checkClosed();
        return setBytes(j, bArr, 0, bArr.length);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.data.orm.Blob.LABEL, "An error occurred when write to Blob use an outputStream", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0037, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.data.orm.Blob.LABEL, "An error occurred when close an outputStream in Blob", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003e, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0026, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0028 */
    @Override // java.sql.Blob
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int setBytes(long r2, byte[] r4, int r5, int r6) {
        /*
            r1 = this;
            java.lang.String r0 = "An error occurred when close an outputStream in Blob"
            r1.checkClosed()
            java.io.OutputStream r2 = r1.setBinaryStream(r2)
            r3 = 0
            r2.write(r4, r5, r6)     // Catch:{ IOException -> 0x0028 }
            boolean r4 = r2 instanceof java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0028 }
            if (r4 == 0) goto L_0x001a
            r4 = r2
            java.io.ByteArrayOutputStream r4 = (java.io.ByteArrayOutputStream) r4     // Catch:{ IOException -> 0x0028 }
            byte[] r4 = r4.toByteArray()     // Catch:{ IOException -> 0x0028 }
            r1.binaryData = r4     // Catch:{ IOException -> 0x0028 }
        L_0x001a:
            r2.close()     // Catch:{ IOException -> 0x001e }
            goto L_0x0032
        L_0x001e:
            ohos.hiviewdfx.HiLogLabel r1 = ohos.data.orm.Blob.LABEL
            java.lang.Object[] r2 = new java.lang.Object[r3]
            ohos.hiviewdfx.HiLog.error(r1, r0, r2)
            goto L_0x0032
        L_0x0026:
            r1 = move-exception
            goto L_0x0033
        L_0x0028:
            ohos.hiviewdfx.HiLogLabel r1 = ohos.data.orm.Blob.LABEL     // Catch:{ all -> 0x0026 }
            java.lang.String r4 = "An error occurred when write to Blob use an outputStream"
            java.lang.Object[] r5 = new java.lang.Object[r3]     // Catch:{ all -> 0x0026 }
            ohos.hiviewdfx.HiLog.error(r1, r4, r5)     // Catch:{ all -> 0x0026 }
            goto L_0x001a
        L_0x0032:
            return r6
        L_0x0033:
            r2.close()     // Catch:{ IOException -> 0x0037 }
            goto L_0x003e
        L_0x0037:
            ohos.hiviewdfx.HiLogLabel r2 = ohos.data.orm.Blob.LABEL
            java.lang.Object[] r3 = new java.lang.Object[r3]
            ohos.hiviewdfx.HiLog.error(r2, r0, r3)
        L_0x003e:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.orm.Blob.setBytes(long, byte[], int, int):int");
    }

    @Override // java.sql.Blob
    public OutputStream setBinaryStream(long j) {
        checkClosed();
        if (j >= 1) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(this.binaryData, 0, (int) (j - 1));
            return byteArrayOutputStream;
        }
        throw new IllegalArgumentException();
    }

    @Override // java.sql.Blob
    public void truncate(long j) {
        checkClosed();
        if (j < 0) {
            throw new IllegalArgumentException();
        } else if (j <= ((long) this.binaryData.length)) {
            int i = (int) j;
            byte[] bArr = new byte[i];
            System.arraycopy(getBinaryData(), 0, bArr, 0, i);
            this.binaryData = bArr;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override // java.sql.Blob
    public void free() {
        synchronized (this.lock) {
            this.binaryData = null;
            this.isClosed = true;
        }
    }

    @Override // java.sql.Blob
    public InputStream getBinaryStream(long j, long j2) {
        checkClosed();
        if (j >= 1) {
            int i = ((int) j) - 1;
            byte[] bArr = this.binaryData;
            if (i > bArr.length) {
                throw new IllegalArgumentException();
            } else if (((long) i) + j2 <= ((long) bArr.length)) {
                return new ByteArrayInputStream(getBinaryData(), i, (int) j2);
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Arrays.equals(getBinaryData(), ((Blob) obj).getBinaryData());
    }

    public int hashCode() {
        return Arrays.hashCode(this.binaryData);
    }
}
