package ohos.accessibility.adapter;

public class AccessibilityNativeAceMethods {
    public static native AccessibilityViewInfo getAccessibilityViewInfoById(int i);

    public static native AccessibilityViewInfo getAccessibilityViewInfoByIdAndKey(int i, String str);

    public static native boolean performAction(int i, int i2);

    public static native boolean performActionByKey(int i, int i2, String str);

    static {
        System.loadLibrary("barrierfree_native_ace.z");
    }

    private AccessibilityNativeAceMethods() {
    }
}
