package ohos.security.permission.infrastructure.utils;

import ohos.idn.DeviceManager;

public class DeviceIdUtil {
    private static DeviceManager deviceManager = new DeviceManager();

    private DeviceIdUtil() {
    }

    public static String getLocalNodeId() {
        return deviceManager.getLocalNodeBasicInfo().map($$Lambda$fC9pooa3OKTyBEfmQtWaUxwcBc.INSTANCE).orElse("");
    }
}
