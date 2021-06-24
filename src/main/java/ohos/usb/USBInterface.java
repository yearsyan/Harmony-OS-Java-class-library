package ohos.usb;

public class USBInterface {
    private final int alternateSetting;
    private final int classType;
    private USBEndpoint[] endpoints;
    private final int id;
    private final String name;
    private final int protocol;
    private final int subclass;

    public USBInterface(int i, int i2, String str, int i3, int i4, int i5) {
        this.id = i;
        this.alternateSetting = i2;
        this.name = str;
        this.classType = i3;
        this.subclass = i4;
        this.protocol = i5;
    }

    public int getAlternateSetting() {
        return this.alternateSetting;
    }

    public int getId() {
        return this.id;
    }

    public int getInterfaceClass() {
        return this.classType;
    }

    public int getInterfaceProtocol() {
        return this.protocol;
    }

    public int getInterfaceSubclass() {
        return this.subclass;
    }

    public String getName() {
        return this.name;
    }

    public USBEndpoint getEndpoint(int i) {
        USBEndpoint[] uSBEndpointArr = this.endpoints;
        if (uSBEndpointArr == null || i < 0 || i >= uSBEndpointArr.length) {
            return null;
        }
        return uSBEndpointArr[i];
    }

    public int getEndpointCount() {
        USBEndpoint[] uSBEndpointArr = this.endpoints;
        if (uSBEndpointArr == null) {
            return 0;
        }
        return uSBEndpointArr.length;
    }

    public void setEndpoints(USBEndpoint[] uSBEndpointArr) {
        this.endpoints = uSBEndpointArr;
    }

    public String toString() {
        return new StringBuilder("USBInterface[id=" + this.id + ", alternateSetting=" + this.alternateSetting + ", name=" + this.name + ", class=" + this.classType + ", subclass=" + this.subclass + ", protocol=" + this.protocol + "]\n").toString();
    }
}
