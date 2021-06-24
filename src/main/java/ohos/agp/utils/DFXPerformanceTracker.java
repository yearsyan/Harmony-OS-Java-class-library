package ohos.agp.utils;

public class DFXPerformanceTracker {
    private static DFXPerformanceTracker instance = new DFXPerformanceTracker();

    private native void nativeDfxBegin(String str);

    private native void nativeDfxEnd(String str);

    public static DFXPerformanceTracker getInstance() {
        return instance;
    }

    public void dfxBegin(String str) {
        nativeDfxBegin(str);
    }

    public void dfxEnd(String str) {
        nativeDfxEnd(str);
    }
}
