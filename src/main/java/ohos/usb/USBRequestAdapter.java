package ohos.usb;

import android.annotation.UnsupportedAppUsage;
import android.hardware.usb.UsbEndpoint;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class USBRequestAdapter {
    private static final String HILOG_TAG = "USBRequestAdapter";
    private static final HiLogLabel LABEL = USBLoggerFactory.getHilogLabel(HILOG_TAG);
    private static final Object LOCK = new Object();
    static final int MAX_USBFS_BUFFER_SIZE = 16384;
    private static volatile USBRequestAdapter instance;
    @UnsupportedAppUsage
    private ByteBuffer byteArray;
    @UnsupportedAppUsage
    private int byteArrayLen;
    private Object clientData;
    @UnsupportedAppUsage
    private long context;
    private USBDevicePipe devicePipe;
    private UsbEndpoint endpoint;
    private boolean isNewQueue;
    private ByteBuffer localByteArray;

    private native boolean localCancel();

    private native void localClose();

    private native int localDequeueArray(byte[] bArr, int i, boolean z);

    private native int localDequeueDirect();

    private native boolean localInit(USBDevicePipe uSBDevicePipe, int i, int i2, int i3, int i4);

    private native boolean localQueue(ByteBuffer byteBuffer, int i, int i2);

    private native boolean localQueueArray(byte[] bArr, int i, boolean z);

    private native boolean localQueueDirect(ByteBuffer byteBuffer, int i, boolean z);

    public static USBRequestAdapter getInstance() {
        USBRequestAdapter uSBRequestAdapter;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new USBRequestAdapter();
            }
            uSBRequestAdapter = instance;
        }
        return uSBRequestAdapter;
    }

    public void abort() {
        localCancel();
    }

    public void free() {
        this.endpoint = null;
        this.devicePipe = null;
        localClose();
    }

    public boolean initialize(USBDevicePipe uSBDevicePipe, USBEndpoint uSBEndpoint) {
        if (uSBDevicePipe == null) {
            return false;
        }
        this.devicePipe = uSBDevicePipe;
        return localInit(uSBDevicePipe, uSBEndpoint.getAddress(), uSBEndpoint.getAttributes(), uSBEndpoint.getMaxPacketSize(), uSBEndpoint.getInterval());
    }

    public boolean queue(ByteBuffer byteBuffer) {
        boolean z;
        boolean z2 = this.endpoint.getDirection() == 0;
        synchronized (LOCK) {
            this.byteArray = byteBuffer;
            if (byteBuffer == null) {
                this.isNewQueue = true;
                z = localQueue(null, 0, 0);
            } else {
                if (!byteBuffer.isDirect()) {
                    this.localByteArray = ByteBuffer.allocateDirect(this.byteArray.remaining());
                    if (z2) {
                        this.byteArray.mark();
                        this.localByteArray.put(this.byteArray);
                        this.localByteArray.flip();
                        this.byteArray.reset();
                    }
                    byteBuffer = this.localByteArray;
                }
                this.isNewQueue = true;
                z = localQueue(byteBuffer, byteBuffer.position(), byteBuffer.remaining());
            }
            if (!z) {
                this.isNewQueue = false;
                this.localByteArray = null;
                this.byteArray = null;
            }
        }
        return z;
    }

    public boolean queue(ByteBuffer byteBuffer, int i) {
        boolean z;
        if (byteBuffer == null) {
            return false;
        }
        boolean z2 = this.endpoint.getDirection() == 0;
        synchronized (LOCK) {
            this.byteArray = byteBuffer;
            this.byteArrayLen = i;
            if (byteBuffer.isDirect()) {
                z = localQueueDirect(byteBuffer, i, z2);
            } else if (byteBuffer.hasArray()) {
                z = localQueueArray(byteBuffer.array(), i, z2);
            } else {
                throw new IllegalArgumentException("buffer is not direct and has no array");
            }
            if (!z) {
                this.byteArray = null;
                this.byteArrayLen = 0;
            }
        }
        return z;
    }

    public void dequeue(boolean z) {
        int i;
        boolean z2 = this.endpoint.getDirection() == 0;
        synchronized (LOCK) {
            if (this.isNewQueue) {
                int localDequeueDirect = localDequeueDirect();
                this.isNewQueue = false;
                if (this.byteArray == null) {
                    HiLog.info(LABEL, "byteArray == null", new Object[0]);
                } else if (this.localByteArray == null) {
                    this.byteArray.position(this.byteArray.position() + localDequeueDirect);
                } else {
                    this.localByteArray.limit(localDequeueDirect);
                    if (z2) {
                        try {
                            this.byteArray.position(this.byteArray.position() + localDequeueDirect);
                        } catch (Throwable th) {
                            this.localByteArray = null;
                            throw th;
                        }
                    } else {
                        this.byteArray.put(this.localByteArray);
                    }
                    this.localByteArray = null;
                }
            } else {
                if (this.byteArray.isDirect()) {
                    i = localDequeueDirect();
                } else {
                    i = localDequeueArray(this.byteArray.array(), this.byteArrayLen, z2);
                }
                if (i >= 0) {
                    if (i >= this.byteArrayLen) {
                        i = this.byteArrayLen;
                    }
                    try {
                        this.byteArray.position(i);
                    } catch (IllegalArgumentException e) {
                        if (z) {
                            throw new BufferOverflowException();
                        }
                        throw e;
                    }
                }
            }
            this.byteArray = null;
            this.byteArrayLen = 0;
        }
    }
}
