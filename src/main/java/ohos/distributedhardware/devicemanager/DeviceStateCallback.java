package ohos.distributedhardware.devicemanager;

public interface DeviceStateCallback {
    void onDeviceOffline(DeviceInfo deviceInfo);

    void onDeviceOnline(DeviceInfo deviceInfo);
}
