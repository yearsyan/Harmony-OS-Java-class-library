package ohos.distributedhardware.devicemanager;

import java.util.List;

public interface SelectDeviceListListener {
    void onGetDeviceList(List<DeviceInfo> list, int i);
}
