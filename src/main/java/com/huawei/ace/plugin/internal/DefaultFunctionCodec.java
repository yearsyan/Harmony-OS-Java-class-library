package com.huawei.ace.plugin.internal;

import com.huawei.ace.plugin.Function;
import com.huawei.ace.plugin.internal.DefaultMessageCodec;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.LinkedList;
import ohos.utils.fastjson.JSON;
import ohos.utils.fastjson.JSONObject;
import ohos.utils.fastjson.serializer.SerializerFeature;

public final class DefaultFunctionCodec implements FunctionCodec {
    private static final byte ERROR_REPLY = 1;
    public static final DefaultFunctionCodec INSTANCE = new DefaultFunctionCodec(DefaultMessageCodec.INSTANCE);
    private static final byte SUCCESS_REPLY = 0;
    private static final String TAG = "DefaultFunctionCodec";
    private final DefaultMessageCodec messageCodec;

    private DefaultFunctionCodec(DefaultMessageCodec defaultMessageCodec) {
        this.messageCodec = defaultMessageCodec;
    }

    @Override // com.huawei.ace.plugin.internal.FunctionCodec
    public Function decodeFunction(ByteBuffer byteBuffer) {
        byteBuffer.order(ByteOrder.nativeOrder());
        Object readValue = this.messageCodec.readValue(byteBuffer);
        byte readByteSize = DefaultMessageCodec.readByteSize(byteBuffer);
        LinkedList linkedList = new LinkedList();
        for (int i = 0; i < readByteSize; i++) {
            linkedList.add(this.messageCodec.readValue(byteBuffer));
        }
        if ((readValue instanceof String) && !byteBuffer.hasRemaining()) {
            return new Function((String) readValue, linkedList);
        }
        throw new IllegalArgumentException("Decode function failed");
    }

    @Override // com.huawei.ace.plugin.internal.FunctionCodec
    public ByteBuffer encodeReply(Object obj) {
        DefaultMessageCodec.ExposedByteArrayOutputStream exposedByteArrayOutputStream = new DefaultMessageCodec.ExposedByteArrayOutputStream();
        this.messageCodec.writeValue(exposedByteArrayOutputStream, JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue));
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(exposedByteArrayOutputStream.size());
        allocateDirect.put(exposedByteArrayOutputStream.buffer(), 0, exposedByteArrayOutputStream.size());
        return allocateDirect;
    }

    @Override // com.huawei.ace.plugin.internal.FunctionCodec
    public ByteBuffer encodeJsonReply(String str) {
        DefaultMessageCodec.ExposedByteArrayOutputStream exposedByteArrayOutputStream = new DefaultMessageCodec.ExposedByteArrayOutputStream();
        this.messageCodec.writeValue(exposedByteArrayOutputStream, str);
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(exposedByteArrayOutputStream.size());
        allocateDirect.put(exposedByteArrayOutputStream.buffer(), 0, exposedByteArrayOutputStream.size());
        return allocateDirect;
    }

    @Override // com.huawei.ace.plugin.internal.FunctionCodec
    public ByteBuffer encodeReplyMap(int i, Object obj) {
        HashMap hashMap = new HashMap(2, 1.0f);
        hashMap.put("code", Integer.valueOf(i));
        hashMap.put("data", obj);
        DefaultMessageCodec.ExposedByteArrayOutputStream exposedByteArrayOutputStream = new DefaultMessageCodec.ExposedByteArrayOutputStream();
        this.messageCodec.writeValue(exposedByteArrayOutputStream, JSONObject.toJSONString(hashMap, SerializerFeature.WriteMapNullValue));
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(exposedByteArrayOutputStream.size());
        allocateDirect.put(exposedByteArrayOutputStream.buffer(), 0, exposedByteArrayOutputStream.size());
        return allocateDirect;
    }
}
