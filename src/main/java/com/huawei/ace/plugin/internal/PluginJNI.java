package com.huawei.ace.plugin.internal;

import com.huawei.ace.runtime.ALog;
import com.huawei.ace.runtime.AcePluginCallback;
import com.huawei.ace.runtime.AcePluginMessage;
import java.nio.ByteBuffer;

public class PluginJNI implements AcePluginMessage {
    private static final String TAG = "PluginJNI";
    private AcePluginCallback pluginCallback = null;

    private static native void nativeInvokePlatformMessageResponseCallback(int i, int i2, int i3, ByteBuffer byteBuffer, int i4, boolean z);

    private static native void nativeNotifyPlatformEvents(int i, int i2, int i3, ByteBuffer byteBuffer, int i4);

    private static native void nativeReplyPluginGetErrorCallback(int i, int i2, int i3, String str);

    @Override // com.huawei.ace.runtime.AcePluginMessage
    public void handlePlatformMessage(int i, String str, byte[] bArr, int i2, boolean z) {
        try {
            PluginHandlersManager.handleMessageFromJs(i, str, bArr, i2, z, this.pluginCallback);
        } catch (RuntimeException e) {
            ALog.e(TAG, "plugin/app process request runtime exception: " + e.toString());
            replyPluginGetErrorCallback(i, i2, 2, "process request runtime exception");
        } catch (Exception e2) {
            ALog.e(TAG, "plugin/app process request unchecked exception: " + e2.toString());
            replyPluginGetErrorCallback(i, i2, 2, "process request unchecked exception");
        }
    }

    public void setPluginLoadHandler(AcePluginCallback acePluginCallback) {
        this.pluginCallback = acePluginCallback;
    }

    public static void invokePlatformMessageResponseCallback(int i, int i2, int i3, ByteBuffer byteBuffer, boolean z) {
        nativeInvokePlatformMessageResponseCallback(i, i2, i3, byteBuffer, byteBuffer.position(), z);
    }

    public static void replyPluginGetErrorCallback(int i, int i2, int i3, String str) {
        nativeReplyPluginGetErrorCallback(i, i2, i3, str);
    }

    public static void notifyPlatformEvents(int i, int i2, int i3, ByteBuffer byteBuffer) {
        nativeNotifyPlatformEvents(i, i2, i3, byteBuffer, byteBuffer.position());
    }
}
