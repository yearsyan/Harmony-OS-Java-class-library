package ohos.usb;

public class USBDeviceAdapter {
    private static final Object LOCK = new Object();
    private static volatile USBDeviceAdapter instance;
    private USBConfig[] configs;
    private USBInterface[] interfaces;

    public static int getDeviceId(String str) {
        return str == null ? -1 : 0;
    }

    public static USBDeviceAdapter getInstance() {
        USBDeviceAdapter uSBDeviceAdapter;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new USBDeviceAdapter();
            }
            uSBDeviceAdapter = instance;
        }
        return uSBDeviceAdapter;
    }

    private USBInterface[] getAllInterfaces() {
        if (this.interfaces == null) {
            USBConfig[] uSBConfigArr = this.configs;
            if (uSBConfigArr == null) {
                return new USBInterface[0];
            }
            int length = uSBConfigArr.length;
            int i = 0;
            for (int i2 = 0; i2 < length; i2++) {
                i += this.configs[i2].getInterfaceCount();
            }
            this.interfaces = new USBInterface[i];
            int i3 = 0;
            int i4 = 0;
            while (i3 < length) {
                USBConfig uSBConfig = this.configs[i3];
                int interfaceCount = uSBConfig.getInterfaceCount();
                int i5 = i4;
                int i6 = 0;
                while (i6 < interfaceCount) {
                    this.interfaces[i5] = uSBConfig.getInterface(i6);
                    i6++;
                    i5++;
                }
                i3++;
                i4 = i5;
            }
        }
        return this.interfaces;
    }

    public USBConfig getConfiguration(int i) {
        if (checkConfigIndexValid(i)) {
            return this.configs[i];
        }
        return null;
    }

    public int getConfigurationCount() {
        USBConfig[] uSBConfigArr = this.configs;
        if (uSBConfigArr == null) {
            return 0;
        }
        return uSBConfigArr.length;
    }

    public USBInterface getInterface(int i) {
        if (i >= getInterfaceCount() || i < 0) {
            return null;
        }
        this.interfaces = getAllInterfaces();
        return this.interfaces[i];
    }

    public int getInterfaceCount() {
        return getAllInterfaces().length;
    }

    private boolean checkConfigIndexValid(int i) {
        int configurationCount;
        if (this.configs != null && (configurationCount = getConfigurationCount()) != 0 && i < configurationCount && i >= 0) {
            return true;
        }
        return false;
    }

    private boolean checkInterfaceIndexValid(int i) {
        int interfaceCount;
        if (this.interfaces != null && (interfaceCount = getInterfaceCount()) != 0 && i < interfaceCount && i >= 0) {
            return true;
        }
        return false;
    }

    public int getId(int i) {
        if (!checkConfigIndexValid(i)) {
            return 0;
        }
        return this.configs[i].getId();
    }

    public int getMaxPower(int i) {
        if (!checkConfigIndexValid(i)) {
            return 0;
        }
        return this.configs[i].getMaxPower();
    }

    public String getName(int i) {
        if (!checkConfigIndexValid(i)) {
            return new String();
        }
        return this.configs[i].getName();
    }

    public boolean isRemoteWakeup(int i) {
        if (!checkConfigIndexValid(i)) {
            return false;
        }
        return this.configs[i].isRemoteWakeup();
    }

    public boolean isSelfPowered(int i) {
        if (!checkConfigIndexValid(i)) {
            return false;
        }
        return this.configs[i].isSelfPowered();
    }

    public void setConfigurations(USBConfig[] uSBConfigArr) {
        this.configs = uSBConfigArr;
    }
}
