package ohos.usb;

import java.util.concurrent.TimeoutException;

public class USBDevicePipe {
    public final USBDevice device;
    private USBDevicePipeAdapter devicePipeAdapter = USBDevicePipeAdapter.getInstance();

    public USBDevicePipe(USBDevice uSBDevice) {
        this.device = uSBDevice;
    }

    public int bulkTransfer(USBEndpoint uSBEndpoint, byte[] bArr, int i, int i2) {
        return bulkTransfer(uSBEndpoint, bArr, 0, i, i2);
    }

    public int bulkTransfer(USBEndpoint uSBEndpoint, byte[] bArr, int i, int i2, int i3) {
        return this.devicePipeAdapter.bulkTransfer(uSBEndpoint, bArr, i, i2, i3);
    }

    public boolean claimInterface(USBInterface uSBInterface, boolean z) {
        return this.devicePipeAdapter.claimInterface(uSBInterface, z);
    }

    public void close() {
        this.devicePipeAdapter.close();
    }

    public int controlTransfer(int i, int i2, int i3, int i4, byte[] bArr, int i5, int i6, int i7) {
        return this.devicePipeAdapter.controlTransfer(i, i2, i3, i4, bArr, i5, i6, i7);
    }

    public int controlTransfer(int i, int i2, int i3, int i4, byte[] bArr, int i5, int i6) {
        return controlTransfer(i, i2, i3, i4, bArr, 0, i5, i6);
    }

    public int getFileDescriptor() {
        return this.devicePipeAdapter.getFileDescriptor();
    }

    public byte[] getRawDescriptors() {
        return this.devicePipeAdapter.getRawDescriptors();
    }

    public String getSerial() {
        return this.devicePipeAdapter.getSerial();
    }

    public boolean releaseInterface(USBInterface uSBInterface) {
        return this.devicePipeAdapter.releaseInterface(uSBInterface);
    }

    public USBRequest requestWait(long j) throws TimeoutException {
        return this.devicePipeAdapter.requestWait(j);
    }

    public USBRequest requestWait() {
        return this.devicePipeAdapter.requestWait();
    }

    public boolean setConfiguration(USBConfig uSBConfig) {
        return this.devicePipeAdapter.setConfiguration(uSBConfig);
    }

    public boolean setInterface(USBInterface uSBInterface) {
        return this.devicePipeAdapter.setInterface(uSBInterface);
    }

    public boolean openByInt(String str, int i) {
        return this.devicePipeAdapter.openByInt(str, i);
    }
}
