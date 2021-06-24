package ohos.systemrestore.bean;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.systemrestore.ISystemRestoreProgressListener;
import ohos.systemrestore.SystemRestoreException;
import ohos.systemrestore.utils.SystemRestoreStringUtil;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;

public class SystemRestoreZipFilePropBean {
    private static final int BYTE_MAX_READ_BUFFER_SIZE = 4096;
    private static final String DEFAULT_OTA_CERTS_FILE = "/system/etc/security/otacerts.zip";
    private static final int EOCD_FOOTER_LENGTH = 4;
    private static final int EOCD_OFFSET_BYTE = 22;
    private static final int INTEGER_SHIFT_1BYTES = 8;
    private static final long PUBLISH_PROGRESS_INTERVAL_MS = 500;
    private static final HiLogLabel TAG = new HiLogLabel(3, 218115072, SystemRestoreZipFilePropBean.class.getSimpleName());
    private static final int ZIP_FILE_FOOTER_SIZE = 6;
    private int commentLength;
    private byte[] eocdBytes;
    private long fileLength;
    private PKCS7 pkcs7Block;
    private RandomAccessFile randomAccessFile = null;
    private int signatureStart;

    private String getDefaultKeystoreFileName() {
        return DEFAULT_OTA_CERTS_FILE;
    }

    public SystemRestoreZipFilePropBean(RandomAccessFile randomAccessFile2) {
        this.randomAccessFile = randomAccessFile2;
    }

    public void getEOCDProperty() throws SystemRestoreException {
        try {
            this.fileLength = this.randomAccessFile.length();
            if (this.fileLength > 6) {
                this.randomAccessFile.seek(this.fileLength - 6);
                byte[] bArr = new byte[6];
                this.randomAccessFile.readFully(bArr);
                if (bArr[2] == -1 && bArr[3] == -1) {
                    setCommentSize(bArr);
                    setSignatureStart(bArr);
                    return;
                }
                throw new SystemRestoreException("no signature in file (no footer)");
            }
            HiLog.error(TAG, "file length less than 6 bytes.", new Object[0]);
            throw new SystemRestoreException("file length less than 6 bytes");
        } catch (IOException e) {
            SystemRestoreStringUtil.printException(TAG, e);
            throw new SystemRestoreException("get update file property io exception");
        }
    }

    public void setEndOfCentralDirectory() throws SystemRestoreException {
        long j = this.fileLength;
        int i = this.commentLength;
        if (j >= ((long) (i + 22))) {
            this.eocdBytes = new byte[(i + 22)];
            try {
                this.randomAccessFile.seek(j - ((long) (i + 22)));
                this.randomAccessFile.readFully(this.eocdBytes);
                byte[] bArr = this.eocdBytes;
                int i2 = 4;
                if (bArr.length < 4) {
                    HiLog.error(TAG, "bad footer length", new Object[0]);
                    throw new SystemRestoreException("bad footer length");
                } else if (bArr[0] == 80 && bArr[1] == 75 && bArr[2] == 5 && bArr[3] == 6) {
                    while (true) {
                        byte[] bArr2 = this.eocdBytes;
                        if (i2 >= bArr2.length - 3) {
                            return;
                        }
                        if (bArr2[i2] == 80 && bArr2[i2 + 1] == 75 && bArr2[i2 + 2] == 5 && bArr2[i2 + 3] == 6) {
                            HiLog.error(TAG, "EOCD marker found after start of EOCD", new Object[0]);
                            throw new SystemRestoreException("EOCD marker found after start of EOCD");
                        }
                        i2++;
                    }
                } else {
                    HiLog.error(TAG, "no signature in file (bad footer)", new Object[0]);
                    throw new SystemRestoreException("no signature in file (bad footer)");
                }
            } catch (IOException e) {
                SystemRestoreStringUtil.printException(TAG, e);
                throw new SystemRestoreException("get update file property io exception");
            }
        } else {
            HiLog.error(TAG, "file format error.", new Object[0]);
            throw new SystemRestoreException("file format error");
        }
    }

    public void verifiedCertsFile(File file) throws SystemRestoreException {
        int i = this.signatureStart;
        try {
            this.pkcs7Block = new PKCS7(new ByteArrayInputStream(this.eocdBytes, (this.commentLength + 22) - i, i));
            X509Certificate[] certificates = this.pkcs7Block.getCertificates();
            if (certificates == null || certificates.length == 0) {
                HiLog.error(TAG, "signature contains no certificates", new Object[0]);
                throw new SystemRestoreException("signature contains no certificates");
            } else {
                handleTrustedCerts(file, certificates[0].getPublicKey());
            }
        } catch (IOException e) {
            SystemRestoreStringUtil.printException(TAG, e);
            throw new SystemRestoreException("PKCS7 IOException.");
        }
    }

    public void readAndVerifyFile(ISystemRestoreProgressListener iSystemRestoreProgressListener, boolean z) throws SystemRestoreException {
        SignerInfo signerInfo;
        long currentTimeMillis = System.currentTimeMillis();
        if (iSystemRestoreProgressListener != null) {
            iSystemRestoreProgressListener.onProgressChanged(0);
        }
        try {
            this.randomAccessFile.seek(0);
            SignerInfo[] signerInfos = this.pkcs7Block.getSignerInfos();
            if (signerInfos == null || signerInfos.length == 0) {
                HiLog.error(TAG, "signature contains no signedData", new Object[0]);
                throw new SystemRestoreException("signature contains no signedData");
            }
            SignerInfo signerInfo2 = signerInfos[0];
            if (z) {
                try {
                    signerInfo = this.pkcs7Block.verify(signerInfo2, toByteArray(new SystemRestoreInputStream(currentTimeMillis, iSystemRestoreProgressListener)));
                } catch (NoSuchAlgorithmException | SignatureException e) {
                    SystemRestoreStringUtil.printException(TAG, e);
                    throw new SystemRestoreException("Byte signature digest verification exception");
                }
            } else {
                try {
                    signerInfo = this.pkcs7Block.verify(signerInfo2, new SystemRestoreInputStream(currentTimeMillis, iSystemRestoreProgressListener));
                } catch (IOException | NoSuchAlgorithmException | SignatureException e2) {
                    SystemRestoreStringUtil.printException(TAG, e2);
                    throw new SystemRestoreException("InputStream signature digest verification exception");
                }
            }
            boolean interrupted = Thread.interrupted();
            if (iSystemRestoreProgressListener != null) {
                iSystemRestoreProgressListener.onProgressChanged(100);
            }
            if (interrupted) {
                throw new SystemRestoreException("verification was interrupted");
            } else if (signerInfo == null) {
                throw new SystemRestoreException("signature digest verification failed");
            }
        } catch (IOException e3) {
            SystemRestoreStringUtil.printException(TAG, e3);
            throw new SystemRestoreException("randomAccessFile exception");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002b, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002c, code lost:
        $closeResource(r5, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002f, code lost:
        throw r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void handleTrustedCerts(java.io.File r6, java.security.PublicKey r7) throws ohos.systemrestore.SystemRestoreException {
        /*
            r5 = this;
            if (r6 != 0) goto L_0x0006
            java.io.File r6 = r5.getDefaultKeystoreFile()
        L_0x0006:
            r0 = 0
            java.util.zip.ZipFile r1 = new java.util.zip.ZipFile
            r1.<init>(r6)
            r6 = 0
            java.lang.String r2 = "X.509"
            java.security.cert.CertificateFactory r2 = java.security.cert.CertificateFactory.getInstance(r2)     // Catch:{ all -> 0x0029 }
            java.util.Enumeration r3 = r1.entries()     // Catch:{ all -> 0x0029 }
        L_0x0017:
            boolean r4 = r3.hasMoreElements()     // Catch:{ all -> 0x0029 }
            if (r4 == 0) goto L_0x0025
            boolean r4 = r5.isX509CertificateVerified(r3, r7, r1, r2)     // Catch:{ all -> 0x0029 }
            if (r4 == 0) goto L_0x0017
            r5 = 1
            r0 = r5
        L_0x0025:
            $closeResource(r6, r1)     // Catch:{ IOException | CertificateException -> 0x0030 }
            goto L_0x0036
        L_0x0029:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x002b }
        L_0x002b:
            r6 = move-exception
            $closeResource(r5, r1)
            throw r6
        L_0x0030:
            r5 = move-exception
            ohos.hiviewdfx.HiLogLabel r6 = ohos.systemrestore.bean.SystemRestoreZipFilePropBean.TAG
            ohos.systemrestore.utils.SystemRestoreStringUtil.printException(r6, r5)
        L_0x0036:
            if (r0 == 0) goto L_0x0039
            return
        L_0x0039:
            ohos.systemrestore.SystemRestoreException r5 = new ohos.systemrestore.SystemRestoreException
            java.lang.String r6 = "signature doesn't match any trusted key"
            r5.<init>(r6)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.systemrestore.bean.SystemRestoreZipFilePropBean.handleTrustedCerts(java.io.File, java.security.PublicKey):void");
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

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002e, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002f, code lost:
        if (r0 != null) goto L_0x0031;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0031, code lost:
        $closeResource(r1, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0034, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isX509CertificateVerified(java.util.Enumeration<? extends java.util.zip.ZipEntry> r1, java.security.PublicKey r2, java.util.zip.ZipFile r3, java.security.cert.CertificateFactory r4) {
        /*
            r0 = this;
            java.lang.Object r0 = r1.nextElement()
            java.util.zip.ZipEntry r0 = (java.util.zip.ZipEntry) r0
            java.io.InputStream r0 = r3.getInputStream(r0)
            r1 = 0
            java.security.cert.Certificate r3 = r4.generateCertificate(r0)     // Catch:{ all -> 0x002c }
            boolean r4 = r3 instanceof java.security.cert.X509Certificate     // Catch:{ all -> 0x002c }
            if (r4 == 0) goto L_0x0026
            java.security.cert.X509Certificate r3 = (java.security.cert.X509Certificate) r3     // Catch:{ all -> 0x002c }
            java.security.PublicKey r3 = r3.getPublicKey()     // Catch:{ all -> 0x002c }
            boolean r2 = r3.equals(r2)     // Catch:{ all -> 0x002c }
            if (r2 == 0) goto L_0x0026
            r2 = 1
            if (r0 == 0) goto L_0x0025
            $closeResource(r1, r0)     // Catch:{ IOException | CertificateException -> 0x0035 }
        L_0x0025:
            return r2
        L_0x0026:
            if (r0 == 0) goto L_0x003b
            $closeResource(r1, r0)     // Catch:{ IOException | CertificateException -> 0x0035 }
            goto L_0x003b
        L_0x002c:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x002e }
        L_0x002e:
            r2 = move-exception
            if (r0 == 0) goto L_0x0034
            $closeResource(r1, r0)
        L_0x0034:
            throw r2
        L_0x0035:
            r0 = move-exception
            ohos.hiviewdfx.HiLogLabel r1 = ohos.systemrestore.bean.SystemRestoreZipFilePropBean.TAG
            ohos.systemrestore.utils.SystemRestoreStringUtil.printException(r1, r0)
        L_0x003b:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.systemrestore.bean.SystemRestoreZipFilePropBean.isX509CertificateVerified(java.util.Enumeration, java.security.PublicKey, java.util.zip.ZipFile, java.security.cert.CertificateFactory):boolean");
    }

    public long getFileLength() {
        return this.fileLength;
    }

    public int getCommentLength() {
        return this.commentLength;
    }

    private void setCommentSize(byte[] bArr) {
        this.commentLength = ((bArr[5] & 255) << 8) | (bArr[4] & 255);
    }

    public int getSignatureStart() {
        return this.signatureStart;
    }

    private void setSignatureStart(byte[] bArr) {
        this.signatureStart = ((bArr[1] & 255) << 8) | (bArr[0] & 255);
    }

    public RandomAccessFile getRandomAccessFile() {
        return this.randomAccessFile;
    }

    private File getDefaultKeystoreFile() {
        return new File(getDefaultKeystoreFileName());
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0042 A[SYNTHETIC, Splitter:B:22:0x0042] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x004f A[SYNTHETIC, Splitter:B:28:0x004f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] toByteArray(java.io.InputStream r5) {
        /*
            r4 = this;
            r0 = 0
            r1 = 0
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0038 }
            r2.<init>()     // Catch:{ IOException -> 0x0038 }
            int r4 = r4.copy(r5, r2)     // Catch:{ IOException -> 0x0032, all -> 0x0030 }
            boolean r5 = ohos.hiviewdfx.HiLog.isDebuggable()     // Catch:{ IOException -> 0x0032, all -> 0x0030 }
            if (r5 == 0) goto L_0x0021
            ohos.hiviewdfx.HiLogLabel r5 = ohos.systemrestore.bean.SystemRestoreZipFilePropBean.TAG     // Catch:{ IOException -> 0x0032, all -> 0x0030 }
            java.lang.String r1 = "toByteArray is %{public}d."
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ IOException -> 0x0032, all -> 0x0030 }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ IOException -> 0x0032, all -> 0x0030 }
            r3[r0] = r4     // Catch:{ IOException -> 0x0032, all -> 0x0030 }
            ohos.hiviewdfx.HiLog.debug(r5, r1, r3)     // Catch:{ IOException -> 0x0032, all -> 0x0030 }
        L_0x0021:
            byte[] r4 = r2.toByteArray()     // Catch:{ IOException -> 0x0032, all -> 0x0030 }
            r2.close()     // Catch:{ IOException -> 0x0029 }
            goto L_0x002f
        L_0x0029:
            r5 = move-exception
            ohos.hiviewdfx.HiLogLabel r0 = ohos.systemrestore.bean.SystemRestoreZipFilePropBean.TAG
            ohos.systemrestore.utils.SystemRestoreStringUtil.printException(r0, r5)
        L_0x002f:
            return r4
        L_0x0030:
            r4 = move-exception
            goto L_0x004d
        L_0x0032:
            r4 = move-exception
            r1 = r2
            goto L_0x0039
        L_0x0035:
            r4 = move-exception
            r2 = r1
            goto L_0x004d
        L_0x0038:
            r4 = move-exception
        L_0x0039:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.systemrestore.bean.SystemRestoreZipFilePropBean.TAG     // Catch:{ all -> 0x0035 }
            ohos.systemrestore.utils.SystemRestoreStringUtil.printException(r5, r4)     // Catch:{ all -> 0x0035 }
            byte[] r4 = new byte[r0]     // Catch:{ all -> 0x0035 }
            if (r1 == 0) goto L_0x004c
            r1.close()     // Catch:{ IOException -> 0x0046 }
            goto L_0x004c
        L_0x0046:
            r5 = move-exception
            ohos.hiviewdfx.HiLogLabel r0 = ohos.systemrestore.bean.SystemRestoreZipFilePropBean.TAG
            ohos.systemrestore.utils.SystemRestoreStringUtil.printException(r0, r5)
        L_0x004c:
            return r4
        L_0x004d:
            if (r2 == 0) goto L_0x0059
            r2.close()     // Catch:{ IOException -> 0x0053 }
            goto L_0x0059
        L_0x0053:
            r5 = move-exception
            ohos.hiviewdfx.HiLogLabel r0 = ohos.systemrestore.bean.SystemRestoreZipFilePropBean.TAG
            ohos.systemrestore.utils.SystemRestoreStringUtil.printException(r0, r5)
        L_0x0059:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.systemrestore.bean.SystemRestoreZipFilePropBean.toByteArray(java.io.InputStream):byte[]");
    }

    private int copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        long copyLarge = copyLarge(inputStream, outputStream);
        if (copyLarge > 2147483647L) {
            return -1;
        }
        return (int) copyLarge;
    }

    private long copyLarge(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[4096];
        int read = inputStream.read(bArr);
        long j = 0;
        while (read != -1) {
            outputStream.write(bArr, 0, read);
            j += (long) read;
            read = inputStream.read(bArr);
        }
        return j;
    }

    /* access modifiers changed from: private */
    public class SystemRestoreInputStream extends InputStream {
        private int lastPercent = 0;
        private long lastPublishTime = 0;
        private ISystemRestoreProgressListener listener;
        private long soFars = 0;
        private long toRead = ((SystemRestoreZipFilePropBean.this.fileLength - ((long) SystemRestoreZipFilePropBean.this.commentLength)) - 2);

        public SystemRestoreInputStream(long j, ISystemRestoreProgressListener iSystemRestoreProgressListener) {
            this.lastPublishTime = j;
            this.listener = iSystemRestoreProgressListener;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i, int i2) throws IOException {
            if (this.soFars >= this.toRead || Thread.interrupted()) {
                return -1;
            }
            long j = this.soFars;
            long j2 = this.toRead;
            if (((long) i2) + j > j2) {
                i2 = (int) (j2 - j);
            }
            int read = SystemRestoreZipFilePropBean.this.randomAccessFile.read(bArr, i, i2);
            this.soFars += (long) read;
            if (this.listener != null) {
                long currentTimeMillis = System.currentTimeMillis();
                int i3 = (int) ((this.soFars * 100) / this.toRead);
                if (i3 > this.lastPercent && currentTimeMillis - this.lastPublishTime > SystemRestoreZipFilePropBean.PUBLISH_PROGRESS_INTERVAL_MS) {
                    this.lastPercent = i3;
                    this.lastPublishTime = currentTimeMillis;
                    this.listener.onProgressChanged(this.lastPercent);
                }
            }
            return read;
        }
    }
}
