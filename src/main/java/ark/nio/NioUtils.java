package ark.nio;

import java.nio.ByteBuffer;

public final class NioUtils {
    private NioUtils() {
    }

    public static void freeDirectBuffer(ByteBuffer byteBuffer) {
        java.nio.NioUtils.freeDirectBuffer(byteBuffer);
    }
}
