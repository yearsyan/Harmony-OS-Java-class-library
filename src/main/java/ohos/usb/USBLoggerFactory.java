package ohos.usb;

import ohos.hiviewdfx.HiLogLabel;

public class USBLoggerFactory {
    private static final String DEFAULT_TAG = "UsbService";
    private static final int DOMAIN = 218114560;

    private USBLoggerFactory() {
    }

    public static HiLogLabel getHilogLabel(String str) {
        if (str.isEmpty()) {
            return getHilogLabel();
        }
        return new HiLogLabel(3, DOMAIN, str);
    }

    public static HiLogLabel getHilogLabel() {
        return new HiLogLabel(3, DOMAIN, DEFAULT_TAG);
    }
}
