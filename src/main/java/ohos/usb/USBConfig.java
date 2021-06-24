package ohos.usb;

public class USBConfig {
    private int id;
    private USBInterface[] interfaces;
    private boolean isRemoteWakeup;
    private boolean isSelfPowered;
    private int maxPower;
    private String name;

    public USBConfig(int i, int i2, String str, boolean z, boolean z2) {
        this.id = i;
        this.maxPower = i2;
        this.name = str;
        this.isRemoteWakeup = z;
        this.isSelfPowered = z2;
    }

    public USBInterface getInterface(int i) {
        USBInterface[] uSBInterfaceArr = this.interfaces;
        if (uSBInterfaceArr == null || i < 0 || i >= uSBInterfaceArr.length) {
            return null;
        }
        return uSBInterfaceArr[i];
    }

    public void setInterfaces(USBInterface[] uSBInterfaceArr) {
        this.interfaces = uSBInterfaceArr;
    }

    public int getInterfaceCount() {
        USBInterface[] uSBInterfaceArr = this.interfaces;
        if (uSBInterfaceArr == null) {
            return 0;
        }
        return uSBInterfaceArr.length;
    }

    public int getId() {
        return this.id;
    }

    public int getMaxPower() {
        return this.maxPower;
    }

    public String getName() {
        return this.name;
    }

    public boolean isRemoteWakeup() {
        return this.isRemoteWakeup;
    }

    public boolean isSelfPowered() {
        return this.isSelfPowered;
    }
}
