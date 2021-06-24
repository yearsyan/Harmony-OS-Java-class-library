package com.huawei.ace.plugin.internal;

import com.huawei.ace.plugin.Function;
import java.nio.ByteBuffer;

public interface FunctionCodec {
    Function decodeFunction(ByteBuffer byteBuffer);

    ByteBuffer encodeJsonReply(String str);

    ByteBuffer encodeReply(Object obj);

    ByteBuffer encodeReplyMap(int i, Object obj);
}
