package ohos.agp.render.opengl;

import java.nio.Buffer;

public class ETC1 {
    private static native int nativeEtc1GetEncodedDataSize(int i, int i2);

    private static native int nativeEtc1PkmGetHeight(Buffer buffer);

    private static native int nativeEtc1PkmGetWidth(Buffer buffer);

    private static native boolean nativeIsValid(Buffer buffer);

    public static boolean isValid(Buffer buffer) {
        return nativeIsValid(buffer);
    }

    public static int etc1PkmGetWidth(Buffer buffer) {
        return nativeEtc1PkmGetWidth(buffer);
    }

    public static int etc1PkmGetHeight(Buffer buffer) {
        return nativeEtc1PkmGetHeight(buffer);
    }

    public static int etc1GetEncodedDataSize(int i, int i2) {
        return nativeEtc1GetEncodedDataSize(i, i2);
    }
}
