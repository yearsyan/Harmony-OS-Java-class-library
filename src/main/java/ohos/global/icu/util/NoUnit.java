package ohos.global.icu.util;

import ohos.com.sun.org.apache.xalan.internal.templates.Constants;
import ohos.usb.USBCore;

public class NoUnit extends MeasureUnit {
    public static final NoUnit BASE = ((NoUnit) MeasureUnit.internalGetInstance(USBCore.USB_FUNC_NONE, "base"));
    public static final NoUnit PERCENT = ((NoUnit) MeasureUnit.internalGetInstance(USBCore.USB_FUNC_NONE, Constants.ATTRNAME_PERCENT));
    public static final NoUnit PERMILLE = ((NoUnit) MeasureUnit.internalGetInstance(USBCore.USB_FUNC_NONE, "permille"));
    private static final long serialVersionUID = 2467174286237024095L;

    NoUnit(String str) {
        super(USBCore.USB_FUNC_NONE, str);
    }
}
