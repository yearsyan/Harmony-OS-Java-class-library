package ohos.usb;

public class USBEndpoint {
    private int address;
    private int attributes;
    private int direction;
    private int interval;
    private int maxPacketSize;
    private int number;
    private int type;

    public USBEndpoint(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.address = i;
        this.attributes = i2;
        this.direction = i3;
        this.number = i4;
        this.interval = i5;
        this.maxPacketSize = i6;
        this.type = i7;
    }

    public int getAddress() {
        return this.address;
    }

    public int getAttributes() {
        return this.attributes;
    }

    public int getDirection() {
        return this.direction;
    }

    public int getEndpointNumber() {
        return this.number;
    }

    public int getInterval() {
        return this.interval;
    }

    public int getMaxPacketSize() {
        return this.maxPacketSize;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return new StringBuilder("USBEndpoint[address=" + this.address + ", attributes=" + this.attributes + ", direction=" + this.direction + ", number=" + this.number + ", interval=" + this.interval + ", maxPacketSize=" + this.maxPacketSize + ", type=" + this.type + "]\n").toString();
    }
}
