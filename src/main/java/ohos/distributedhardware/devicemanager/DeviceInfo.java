package ohos.distributedhardware.devicemanager;

import java.util.HashMap;
import java.util.Map;

public class DeviceInfo {
    private String mDeviceName;
    private DeviceType mDeviceType;
    private String mNetworkId;

    public DeviceInfo() {
        this.mDeviceType = DeviceType.UNKNOWN_TYPE;
    }

    public DeviceInfo(String str, String str2, DeviceType deviceType) {
        this.mNetworkId = str;
        this.mDeviceName = str2;
        this.mDeviceType = deviceType;
    }

    public String getNetworkId() {
        return this.mNetworkId;
    }

    public DeviceType getDeviceType() {
        return this.mDeviceType;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public enum DeviceType {
        UNKNOWN_TYPE(0),
        SPEAKER(10),
        PHONE(14),
        TABLET(17),
        WEARABLE(109),
        CAR(131),
        TV(156);
        
        private static final Map<Integer, DeviceType> DEVICE_TYPE_MAP = new HashMap();
        private final int mVal;

        static {
            DeviceType[] values = values();
            for (DeviceType deviceType : values) {
                DEVICE_TYPE_MAP.put(Integer.valueOf(deviceType.value()), deviceType);
            }
        }

        private DeviceType(int i) {
            this.mVal = i;
        }

        public int value() {
            return this.mVal;
        }

        public static DeviceType valueOf(int i) {
            return DEVICE_TYPE_MAP.getOrDefault(Integer.valueOf(i), UNKNOWN_TYPE);
        }
    }
}
