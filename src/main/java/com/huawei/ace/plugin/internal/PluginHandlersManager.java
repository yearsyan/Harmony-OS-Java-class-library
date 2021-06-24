package com.huawei.ace.plugin.internal;

import com.huawei.ace.runtime.ALog;
import com.huawei.ace.runtime.AcePluginCallback;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public abstract class PluginHandlersManager {
    private static final int DEFAULT_CONTAINER_ID = Integer.MIN_VALUE;
    private static final int DEFAULT_PLUGINMAP_CONTAINER_CAPICITY = 4;
    private static final int DEFAULT_PLUGINMAP_CONTAINER_LPUGIN_CAPICITY = 16;
    private static final Map<String, Map<Integer, PluginHandler>> MESSAGE_HANDLERS = new HashMap(4);
    private static final String TAG = "PluginHandlersManager";

    public static void setPluginHandler(String str, Integer num, PluginHandler pluginHandler) {
        int intValue = num == null ? Integer.MIN_VALUE : num.intValue();
        if (pluginHandler == null) {
            ALog.i(TAG, "Removing platform handler for abilityId '" + num + "' and group '" + str + "'");
            deletePluginHandler(str, intValue);
            return;
        }
        ALog.i(TAG, "Setting handler for abilityId '" + num + "' and group '" + str + "'");
        addPluginHandler(str, intValue, pluginHandler);
    }

    private static void addPluginHandler(String str, int i, PluginHandler pluginHandler) {
        if (MESSAGE_HANDLERS.containsKey(str)) {
            MESSAGE_HANDLERS.get(str).put(Integer.valueOf(i), pluginHandler);
            return;
        }
        HashMap hashMap = new HashMap(16);
        hashMap.put(Integer.valueOf(i), pluginHandler);
        MESSAGE_HANDLERS.put(str, hashMap);
    }

    private static void deletePluginHandler(String str, int i) {
        if (MESSAGE_HANDLERS.containsKey(str)) {
            Map<Integer, PluginHandler> map = MESSAGE_HANDLERS.get(str);
            map.remove(Integer.valueOf(i));
            if (map.isEmpty()) {
                MESSAGE_HANDLERS.remove(str);
            }
        }
    }

    public static Optional<PluginHandler> getPluginHandler(String str, int i) {
        return Optional.ofNullable(MESSAGE_HANDLERS.get(str)).map(new Function(i) {
            /* class com.huawei.ace.plugin.internal.$$Lambda$PluginHandlersManager$VmFB_aKp2pflC1N4OsoZo9LVC0 */
            private final /* synthetic */ int f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return PluginHandlersManager.lambda$getPluginHandler$0(this.f$0, (Map) obj);
            }
        });
    }

    static /* synthetic */ PluginHandler lambda$getPluginHandler$0(int i, Map map) {
        if (map.containsKey(Integer.valueOf(i))) {
            return (PluginHandler) map.get(Integer.valueOf(i));
        }
        return (PluginHandler) map.get(Integer.MIN_VALUE);
    }

    public static void notify(int i, int i2, int i3, ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            ALog.e(TAG, "event is NULL");
        } else {
            PluginJNI.notifyPlatformEvents(i, i2, i3, byteBuffer);
        }
    }

    public static void handleMessageFromJs(int i, String str, byte[] bArr, int i2, boolean z, AcePluginCallback acePluginCallback) {
        if (str == null || bArr == null) {
            ALog.e(TAG, "the param 'group' or 'message' from Js is null");
            PluginJNI.replyPluginGetErrorCallback(i, i2, 200, "group or message from Js is null");
            return;
        }
        if (acePluginCallback != null) {
            acePluginCallback.onLoadPlugin(str, i);
        }
        Optional<PluginHandler> pluginHandler = getPluginHandler(str, i);
        if (pluginHandler.isPresent()) {
            try {
                pluginHandler.get().onReceiveMessage(ByteBuffer.wrap(bArr), new Reply(i2, i, z), str, i);
            } catch (IllegalArgumentException e) {
                ALog.e(TAG, "Decode message error when dispatch error:" + e.getMessage());
                PluginJNI.replyPluginGetErrorCallback(i, i2, 2, "throw exception:" + e.getMessage());
            }
        } else {
            ALog.e(TAG, "No registered handler named '" + str + "', and reply an empty message.");
            PluginJNI.replyPluginGetErrorCallback(i, i2, 103, "group name is not registered in platform side");
        }
    }

    /* access modifiers changed from: private */
    public static class Reply implements FrontendMessageReply {
        private final int containerId;
        private final AtomicBoolean done = new AtomicBoolean(false);
        private final int replyId;
        private final boolean replyToComponent;

        Reply(int i, int i2, boolean z) {
            this.replyId = i;
            this.containerId = i2;
            this.replyToComponent = z;
        }

        @Override // com.huawei.ace.plugin.internal.FrontendMessageReply
        public void reply(int i, ByteBuffer byteBuffer) {
            if (this.done.getAndSet(true)) {
                ALog.i(PluginHandlersManager.TAG, "Reply already submitted.");
            } else if (byteBuffer == null) {
                ALog.e(PluginHandlersManager.TAG, "System error: reply message is null");
                PluginJNI.replyPluginGetErrorCallback(this.containerId, this.replyId, 200, "reply message is null");
            } else {
                ALog.i(PluginHandlersManager.TAG, "reply data to js, containerId: " + this.containerId + ", callbackId: " + this.replyId);
                PluginJNI.invokePlatformMessageResponseCallback(this.containerId, this.replyId, i, byteBuffer, this.replyToComponent);
            }
        }

        @Override // com.huawei.ace.plugin.internal.FrontendMessageReply
        public void replyPluginGetError(int i, String str) {
            if (this.done.getAndSet(true)) {
                ALog.i(PluginHandlersManager.TAG, "Reply already submitted.");
            } else {
                PluginJNI.replyPluginGetErrorCallback(this.containerId, this.replyId, i, str);
            }
        }

        @Override // com.huawei.ace.plugin.internal.FrontendMessageReply
        public int getReplyId() {
            return this.replyId;
        }
    }
}
