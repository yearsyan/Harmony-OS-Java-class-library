package ohos.usb;

import java.io.FileDescriptor;
import java.util.concurrent.TimeoutException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class USBDevicePipeAdapter {
    private static final String HILOG_TAG = "USBDevicePipeAdapter";
    private static final HiLogLabel LABEL = USBLoggerFactory.getHilogLabel(HILOG_TAG);
    private static final Object LOCK = new Object();
    private static volatile USBDevicePipeAdapter instance;
    private long mNativeContext;

    private native int localBulkRequest(int i, byte[] bArr, int i2, int i3, int i4);

    private native boolean localClaimInterface(int i, boolean z);

    private native void localClose();

    private native int localControlRequest(int i, int i2, int i3, int i4, byte[] bArr, int i5, int i6, int i7);

    private native byte[] localGetDesc();

    private native int localGetFd();

    private native String localGetSerial();

    private native boolean localOpen(String str, FileDescriptor fileDescriptor);

    private native boolean localOpenByInt(String str, int i);

    private native boolean localReleaseInterface(int i);

    private native USBRequest localRequestWait(long j) throws TimeoutException;

    private native boolean localResetDevice();

    private native boolean localSetConfiguration(int i);

    private native boolean localSetInterface(int i, int i2);

    private USBDevicePipeAdapter() {
    }

    public static USBDevicePipeAdapter getInstance() {
        USBDevicePipeAdapter uSBDevicePipeAdapter;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new USBDevicePipeAdapter();
            }
            uSBDevicePipeAdapter = instance;
        }
        return uSBDevicePipeAdapter;
    }

    public boolean open(String str, FileDescriptor fileDescriptor) {
        return localOpen(str, fileDescriptor);
    }

    public boolean openByInt(String str, int i) {
        return localOpenByInt(str, i);
    }

    private static void checkControlTransferBounds(int i, int i2, int i3, int i4, byte[] bArr, int i5, int i6, int i7) {
        int length = bArr != null ? bArr.length : 0;
        if (i6 < 0 || i6 > length) {
            throw new IllegalArgumentException("Buffer start or length out of bounds.");
        } else if (i < 0 || i2 < 0 || i3 < 0 || i4 < 0 || i7 < 0 || i5 < 0) {
            throw new IllegalArgumentException("requestType or other parameter out of bounds.");
        }
    }

    public int bulkTransfer(USBEndpoint uSBEndpoint, byte[] bArr, int i, int i2) {
        return bulkTransfer(uSBEndpoint, bArr, 0, i, i2);
    }

    public int bulkTransfer(USBEndpoint uSBEndpoint, byte[] bArr, int i, int i2, int i3) {
        return localBulkRequest(uSBEndpoint.getAddress(), bArr, i, i2, i3);
    }

    public boolean claimInterface(USBInterface uSBInterface, boolean z) {
        if (uSBInterface == null) {
            return false;
        }
        return localClaimInterface(uSBInterface.getId(), z);
    }

    public void close() {
        if (this.mNativeContext != 0) {
            localClose();
        }
    }

    public int controlTransfer(int i, int i2, int i3, int i4, byte[] bArr, int i5, int i6, int i7) {
        checkControlTransferBounds(i, i2, i3, i4, bArr, i5, i6, i7);
        return localControlRequest(i, i2, i3, i4, bArr, i5, i6, i7);
    }

    public int controlTransfer(int i, int i2, int i3, int i4, byte[] bArr, int i5, int i6) {
        return controlTransfer(i, i2, i3, i4, bArr, 0, i5, i6);
    }

    public int getFileDescriptor() {
        return localGetFd();
    }

    public byte[] getRawDescriptors() {
        return localGetDesc();
    }

    public String getSerial() {
        return localGetSerial();
    }

    public boolean releaseInterface(USBInterface uSBInterface) {
        return localReleaseInterface(uSBInterface.getId());
    }

    public USBRequest requestWait(long j) throws TimeoutException {
        USBRequest localRequestWait = localRequestWait(j);
        if (localRequestWait != null) {
            localRequestWait.dequeue(true);
        }
        return localRequestWait;
    }

    public USBRequest requestWait() {
        USBRequest uSBRequest;
        try {
            uSBRequest = localRequestWait(-1);
        } catch (TimeoutException unused) {
            HiLog.info(LABEL, "requestWait error!", new Object[0]);
            uSBRequest = null;
        }
        if (uSBRequest != null) {
            uSBRequest.dequeue(true);
        }
        return uSBRequest;
    }

    public boolean setConfiguration(USBConfig uSBConfig) {
        return localSetConfiguration(uSBConfig.getId());
    }

    public boolean setInterface(USBInterface uSBInterface) {
        return localSetInterface(uSBInterface.getId(), uSBInterface.getAlternateSetting());
    }
}
