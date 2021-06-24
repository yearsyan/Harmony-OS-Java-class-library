package com.huawei.ace.plugin.internal;

import java.nio.ByteBuffer;

public interface FrontendMessageReply {
    int getReplyId();

    void reply(int i, ByteBuffer byteBuffer);

    void replyPluginGetError(int i, String str);
}
