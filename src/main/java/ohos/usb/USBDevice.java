package ohos.usb;

public class USBDevice {
    private final int classId;
    private USBConfig[] configs;
    private USBInterface[] interfaces;
    private final String manufacturerName;
    private final String name;
    private final int productId;
    private final String productName;
    private final int protocol;
    private final String serialNumber;
    private final int subclass;
    private final int vendorId;
    private final String version;

    public static String getDeviceName(int i) {
        return "default_dev";
    }

    public USBDevice(String str, int i, int i2, int i3, int i4, int i5, String str2, String str3, String str4, String str5) {
        this.name = str;
        this.vendorId = i;
        this.productId = i2;
        this.classId = i3;
        this.subclass = i4;
        this.protocol = i5;
        this.manufacturerName = str2;
        this.productName = str3;
        this.version = str4;
        this.serialNumber = str5;
    }

    public int getDeviceClass() {
        return this.classId;
    }

    public int getDeviceId() {
        return USBDeviceAdapter.getDeviceId(this.name);
    }

    public static int getDeviceId(String str) {
        return USBDeviceAdapter.getDeviceId(str);
    }

    public String getDeviceName() {
        return this.name;
    }

    public int getDeviceProtocol() {
        return this.protocol;
    }

    public int getDeviceSubclass() {
        return this.subclass;
    }

    public void setInterfaces(USBInterface[] uSBInterfaceArr) {
        this.interfaces = uSBInterfaceArr;
    }

    public USBInterface getInterface(int i) {
        USBInterface[] uSBInterfaceArr = this.interfaces;
        if (uSBInterfaceArr == null || i < 0 || i >= uSBInterfaceArr.length) {
            return null;
        }
        return uSBInterfaceArr[i];
    }

    public int getInterfaceCount() {
        USBInterface[] uSBInterfaceArr = this.interfaces;
        if (uSBInterfaceArr == null) {
            return 0;
        }
        return uSBInterfaceArr.length;
    }

    public String getManufacturerName() {
        return this.manufacturerName;
    }

    public int getProductId() {
        return this.productId;
    }

    public String getProductName() {
        return this.productName;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public int getVendorId() {
        return this.vendorId;
    }

    public String getVersion() {
        return this.version;
    }

    public USBConfig getConfiguration(int i) {
        USBConfig[] uSBConfigArr = this.configs;
        if (uSBConfigArr == null || i < 0 || i >= uSBConfigArr.length) {
            return null;
        }
        return uSBConfigArr[i];
    }

    public int getConfigurationCount() {
        USBConfig[] uSBConfigArr = this.configs;
        if (uSBConfigArr == null) {
            return 0;
        }
        return uSBConfigArr.length;
    }

    public void setConfigurations(USBConfig[] uSBConfigArr) {
        this.configs = uSBConfigArr;
    }
}
