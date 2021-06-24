package ohos.bluetooth.ble;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

public final class BleScanResult implements Sequenceable {
    private int mAdvertiseFlag;
    private BlePeripheralDevice mBlePeripheralDevice;
    private byte[] mBytes;
    private boolean mIsConnectable;
    private Map<Integer, byte[]> mManufacturerDatas;
    private int mRssi;
    private Map<UUID, byte[]> mServiceDatas;
    private List<UUID> mServiceUuids;
    private long mTime;

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        return true;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        return true;
    }

    public BleScanResult(BlePeripheralDevice blePeripheralDevice, Map<Integer, byte[]> map, Map<UUID, byte[]> map2, List<UUID> list, int i, long j) {
        this.mBlePeripheralDevice = blePeripheralDevice;
        this.mManufacturerDatas = map;
        this.mServiceDatas = map2;
        this.mServiceUuids = list;
        this.mAdvertiseFlag = i;
        this.mTime = j;
    }

    /* access modifiers changed from: package-private */
    public void setRssi(int i) {
        this.mRssi = i;
    }

    /* access modifiers changed from: package-private */
    public void setTime(long j) {
        this.mTime = j;
    }

    /* access modifiers changed from: package-private */
    public void setIsConnectable(boolean z) {
        this.mIsConnectable = z;
    }

    /* access modifiers changed from: package-private */
    public void setRawData(byte[] bArr) {
        this.mBytes = bArr;
    }

    public BlePeripheralDevice getPeripheralDevice() {
        return this.mBlePeripheralDevice;
    }

    public int getRssi() {
        return this.mRssi;
    }

    public boolean isConnectable() {
        return this.mIsConnectable;
    }

    public Map<Integer, byte[]> getManufacturerData() {
        return this.mManufacturerDatas;
    }

    public Map<UUID, byte[]> getServiceData() {
        return this.mServiceDatas;
    }

    public List<UUID> getServiceUuids() {
        return this.mServiceUuids;
    }

    public int getAdvertiseFlag() {
        return this.mAdvertiseFlag;
    }

    public long getTime() {
        return this.mTime;
    }

    public byte[] getRawData() {
        return this.mBytes;
    }
}
