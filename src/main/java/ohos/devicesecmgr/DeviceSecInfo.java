package ohos.devicesecmgr;

public class DeviceSecInfo {
    private static final int DEFAULT_LEVEL = 0;
    private static final int DEFAULT_RESULT = 65535;
    private static final int DEFAULT_SOFTBUS_TYPE = 0;
    private static final int MAGIC = -1412623820;
    private String extra;
    private int level;
    private int magicNum;
    private int result;
    private int softbusType;

    DeviceSecInfo() {
        this(65535, 0, 0, "");
    }

    public DeviceSecInfo(int i, int i2, int i3, String str) {
        this.magicNum = MAGIC;
        this.result = i;
        this.level = i2;
        this.softbusType = i3;
        this.extra = str;
    }

    public int getResult() {
        return this.result;
    }

    public int getLevel() {
        return this.level;
    }

    public int getSoftbusType() {
        return this.softbusType;
    }

    public String getExtra() {
        return this.extra;
    }
}
