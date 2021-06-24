package ohos.usb;

public class USBInterfaceAdapter {
    private static final Object LOCK = new Object();
    private static volatile USBInterfaceAdapter instance;
    private USBEndpoint[] endpoints;

    public void setEndpoints(USBEndpoint[] uSBEndpointArr) {
    }

    public static USBInterfaceAdapter getInstance() {
        USBInterfaceAdapter uSBInterfaceAdapter;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new USBInterfaceAdapter();
            }
            uSBInterfaceAdapter = instance;
        }
        return uSBInterfaceAdapter;
    }

    private boolean checkBounds(int i) {
        if (this.endpoints != null && i >= 0 && i < getEndpointCount()) {
            return true;
        }
        return false;
    }

    public USBEndpoint getEndpoint(int i) {
        if (!checkBounds(i)) {
            return null;
        }
        return this.endpoints[i];
    }

    public int getEndpointAddress(int i) {
        if (!checkBounds(i)) {
            return 0;
        }
        return this.endpoints[i].getAddress();
    }

    public int getEndpointCount() {
        USBEndpoint[] uSBEndpointArr = this.endpoints;
        if (uSBEndpointArr == null) {
            return 0;
        }
        return uSBEndpointArr.length;
    }

    public int getEndpointAttributes(int i) {
        if (!checkBounds(i)) {
            return 0;
        }
        return this.endpoints[i].getAttributes();
    }

    public int getEndpointDirection(int i) {
        if (!checkBounds(i)) {
            return 0;
        }
        return this.endpoints[i].getDirection();
    }

    public int getEndpointNumber(int i) {
        if (!checkBounds(i)) {
            return 0;
        }
        return this.endpoints[i].getEndpointNumber();
    }

    public int getEndpointInterval(int i) {
        if (!checkBounds(i)) {
            return 0;
        }
        return this.endpoints[i].getInterval();
    }

    public int getEndpointMaxPacketSize(int i) {
        if (!checkBounds(i)) {
            return 0;
        }
        return this.endpoints[i].getMaxPacketSize();
    }

    public int getEndpointType(int i) {
        if (!checkBounds(i)) {
            return 0;
        }
        return this.endpoints[i].getType();
    }
}
