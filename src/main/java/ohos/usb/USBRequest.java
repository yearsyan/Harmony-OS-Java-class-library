package ohos.usb;

import java.nio.ByteBuffer;

public class USBRequest {
    private Object clientData;
    private USBRequestAdapter requestAdapter = USBRequestAdapter.getInstance();

    public void abort() {
        this.requestAdapter.abort();
    }

    public void free() {
        this.requestAdapter.free();
    }

    public boolean initialize(USBDevicePipe uSBDevicePipe, USBEndpoint uSBEndpoint) {
        return this.requestAdapter.initialize(uSBDevicePipe, uSBEndpoint);
    }

    public boolean queue(ByteBuffer byteBuffer) {
        return this.requestAdapter.queue(byteBuffer);
    }

    public boolean queue(ByteBuffer byteBuffer, int i) {
        return this.requestAdapter.queue(byteBuffer, i);
    }

    public void dequeue(boolean z) {
        this.requestAdapter.dequeue(z);
    }

    public void setClientData(Object obj) {
        this.clientData = obj;
    }
}
