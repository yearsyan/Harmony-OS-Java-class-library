package ohos.distributedhardware.devicemanager;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;

public class DeviceBasicInfo implements Parcelable {
    public static final Parcelable.Creator<DeviceBasicInfo> CREATOR = new Parcelable.Creator<DeviceBasicInfo>() {
        /* class ohos.distributedhardware.devicemanager.DeviceBasicInfo.AnonymousClass1 */

        @Override // android.os.Parcelable.Creator
        public DeviceBasicInfo createFromParcel(Parcel parcel) {
            return new DeviceBasicInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public DeviceBasicInfo[] newArray(int i) {
            return new DeviceBasicInfo[i];
        }
    };
    public static final int FLAG_GET_ALL_DEVICE = 0;
    public static final int FLAG_GET_OFFLINE_DEVICE = 2;
    public static final int FLAG_GET_ONLINE_DEVICE = 1;
    private static final String TAG = "DM_DeviceBasicInfo";
    private String mDeviceId;
    private String mDeviceName;
    private DeviceState mDeviceState;
    private DeviceType mDeviceType;

    public enum DeviceState {
        UNKNOWN,
        ONLINE,
        OFFLINE
    }

    public int describeContents() {
        return 0;
    }

    public DeviceBasicInfo(String str, String str2) {
        this.mDeviceId = str;
        this.mDeviceName = str2;
        this.mDeviceType = DeviceType.PHONE;
        this.mDeviceState = DeviceState.OFFLINE;
    }

    private DeviceBasicInfo(Parcel parcel) {
        if (parcel != null) {
            parcel.enforceInterface(TAG);
            this.mDeviceId = parcel.readString();
            this.mDeviceName = parcel.readString();
            int readInt = parcel.readInt();
            this.mDeviceType = DeviceType.values()[(readInt < 0 || readInt >= DeviceType.values().length) ? 0 : readInt];
            int readInt2 = parcel.readInt();
            this.mDeviceState = DeviceState.values()[(readInt2 < 0 || readInt2 >= DeviceState.values().length) ? 0 : readInt2];
        }
    }

    public void setDeviceInfo(String str, String str2) {
        this.mDeviceId = str;
        this.mDeviceName = str2;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.mDeviceType = deviceType;
    }

    public void setDeviceState(DeviceState deviceState) {
        this.mDeviceState = deviceState;
    }

    public String getDeviceId() {
        return this.mDeviceId;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public DeviceType getDeviceType() {
        return this.mDeviceType;
    }

    public DeviceState getDeviceState() {
        return this.mDeviceState;
    }

    public boolean isDeviceOnline() {
        return this.mDeviceState == DeviceState.ONLINE;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (parcel != null) {
            parcel.writeInterfaceToken(TAG);
            parcel.writeString(this.mDeviceId);
            parcel.writeString(this.mDeviceName);
            parcel.writeInt(this.mDeviceType.ordinal());
            parcel.writeInt(this.mDeviceState.ordinal());
        }
    }

    public enum DeviceType {
        UNKNOWN_TYPE(0),
        AUDIO(10),
        PHONE(14),
        PAD(17),
        WATCH(109),
        CAR(131),
        TELEVISION(156);
        
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

    public String toString() {
        return "deviceId:" + this.mDeviceId + ", deviceName:" + this.mDeviceName + ", deviceType:" + this.mDeviceType + ", deviceState:" + this.mDeviceState;
    }
}
