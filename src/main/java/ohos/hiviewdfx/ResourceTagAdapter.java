package ohos.hiviewdfx;

import dalvik.system.CloseGuard;

public class ResourceTagAdapter {
    private static final String COMMON_NAME = "ohos.hiviewdfx.ResourceTag/release";
    private final CloseGuard closeGuard = CloseGuard.get();

    public void markInUse(String str) {
        CloseGuard closeGuard2 = this.closeGuard;
        if (closeGuard2 != null) {
            closeGuard2.open(COMMON_NAME);
        }
    }

    public void release() {
        CloseGuard closeGuard2 = this.closeGuard;
        if (closeGuard2 != null) {
            closeGuard2.close();
        }
    }

    public void warnIfNeed() {
        CloseGuard closeGuard2 = this.closeGuard;
        if (closeGuard2 != null) {
            closeGuard2.warnIfOpen();
        }
    }
}
