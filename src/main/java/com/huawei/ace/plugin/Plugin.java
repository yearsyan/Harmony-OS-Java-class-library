package com.huawei.ace.plugin;

import com.huawei.ace.plugin.EventGroup;
import com.huawei.ace.plugin.ModuleGroup;
import com.huawei.ace.plugin.internal.CallbackFunction;
import com.huawei.ace.plugin.internal.DefaultFunctionCodec;
import com.huawei.ace.plugin.internal.FrontendMessageReply;
import com.huawei.ace.plugin.internal.FunctionCodec;
import com.huawei.ace.plugin.internal.PluginHandler;
import com.huawei.ace.plugin.internal.PluginHandlersManager;
import com.huawei.ace.runtime.ALog;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import sun.misc.Cleaner;

public final class Plugin {
    private static final String TAG = "Plugin#";
    private final FunctionCodec codec;
    private final String logTag;
    private final String name;

    public Plugin(String str) {
        if (str == null || "".equals(str)) {
            ALog.e(TAG, "pluhin name must not be null or empty string.");
            throw new IllegalArgumentException("plugin name is null or empty string");
        }
        this.codec = DefaultFunctionCodec.INSTANCE;
        this.logTag = TAG + str;
        this.name = str;
    }

    public void setPluginHandler(RequestHandler requestHandler) {
        setPluginHandler(requestHandler, null);
    }

    public void setPluginHandler(RequestHandler requestHandler, Integer num) {
        PluginHandlerImpl pluginHandlerImpl;
        String str = this.name;
        if (requestHandler == null) {
            pluginHandlerImpl = null;
        } else {
            pluginHandlerImpl = new PluginHandlerImpl(requestHandler);
        }
        PluginHandlersManager.setPluginHandler(str, num, pluginHandlerImpl);
    }

    public static void registerPluginHandler(String str, RequestHandler requestHandler, Integer num) {
        new Plugin(str).setPluginHandler(requestHandler, num);
    }

    public interface RequestHandler {
        default void onRequest(Result result, Object obj, Object... objArr) {
            result.notExistFunction();
        }
    }

    /* access modifiers changed from: private */
    public final class PluginHandlerImpl implements PluginHandler {
        private final RequestHandler handler;

        public PluginHandlerImpl(RequestHandler requestHandler) {
            this.handler = requestHandler;
        }

        @Override // com.huawei.ace.plugin.internal.PluginHandler
        public void onReceiveMessage(ByteBuffer byteBuffer, FrontendMessageReply frontendMessageReply, String str, int i) {
            Function decodeFunction = Plugin.this.codec.decodeFunction(byteBuffer);
            String str2 = Plugin.this.logTag;
            ALog.i(str2, "receive action message, containerId: " + i + ", replyId: " + frontendMessageReply.getReplyId());
            RequestHandler requestHandler = this.handler;
            if ((requestHandler instanceof ModuleGroup.ModuleGroupHandler) || (requestHandler instanceof EventGroup.EventGroupHandler)) {
                onSpecificRequest(decodeFunction, frontendMessageReply, i);
                return;
            }
            Object remove = decodeFunction.arguments.size() > 0 ? decodeFunction.arguments.remove(0) : null;
            boolean z = remove instanceof CallbackFunction;
            CallbackImpl callbackImpl = remove;
            if (z) {
                callbackImpl = createCallbackImpl(i, (CallbackFunction) remove);
            }
            this.handler.onRequest(new ResultImpl(Plugin.this, frontendMessageReply), callbackImpl, getInterfaceParams(decodeFunction.arguments, i));
        }

        private void onSpecificRequest(Function function, FrontendMessageReply frontendMessageReply, int i) {
            ResultImpl resultImpl = new ResultImpl(frontendMessageReply, function.name);
            if (!"subscribe".equals(function.name) || !(this.handler instanceof EventGroup.EventGroupHandler)) {
                if ("unsubscribe".equals(function.name)) {
                    RequestHandler requestHandler = this.handler;
                    if (requestHandler instanceof EventGroup.EventGroupHandler) {
                        ((EventGroup.EventGroupHandler) requestHandler).onUnsubscribe(function.arguments, resultImpl);
                        return;
                    }
                }
                RequestHandler requestHandler2 = this.handler;
                if (requestHandler2 instanceof ModuleGroup.ModuleGroupHandler) {
                    ((ModuleGroup.ModuleGroupHandler) requestHandler2).onFunctionCall(function, resultImpl);
                } else {
                    frontendMessageReply.replyPluginGetError(200, "handler is not exists or function name is unexpected in Event Group");
                }
            } else {
                ((EventGroup.EventGroupHandler) this.handler).onSubscribe(function.arguments, getEventNotifier(function.arguments, i), resultImpl);
            }
        }

        private Object[] getInterfaceParams(List<Object> list, int i) {
            int i2;
            int i3 = 0;
            if (list == null) {
                return new Object[0];
            }
            Object[] objArr = new Object[list.size()];
            for (Object obj : list) {
                if (obj instanceof CallbackFunction) {
                    i2 = i3 + 1;
                    objArr[i3] = createCallbackImpl(i, (CallbackFunction) obj);
                } else {
                    i2 = i3 + 1;
                    objArr[i3] = obj;
                }
                i3 = i2;
            }
            return objArr;
        }

        private EventNotifier getEventNotifier(List<Object> list, int i) {
            if (list == null) {
                return null;
            }
            Iterator<Object> it = list.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (next instanceof CallbackFunction) {
                    CallbackImpl createCallbackImpl = createCallbackImpl(i, (CallbackFunction) next);
                    it.remove();
                    return createCallbackImpl;
                }
            }
            return null;
        }

        private CallbackImpl createCallbackImpl(int i, CallbackFunction callbackFunction) {
            CallbackImpl callbackImpl = new CallbackImpl(i, callbackFunction.getId());
            Cleaner.create(callbackImpl, new CallbackCleaner(Plugin.this.codec, i, callbackFunction.getId()));
            return callbackImpl;
        }
    }

    /* access modifiers changed from: private */
    public static final class CallbackCleaner implements Runnable {
        private final FunctionCodec codec;
        private final int containerId;
        private final int functionId;

        CallbackCleaner(FunctionCodec functionCodec, int i, int i2) {
            this.codec = functionCodec;
            this.containerId = i;
            this.functionId = i2;
        }

        public void run() {
            ALog.i("Plugin#Cleaner", "release Callback, callbackId: " + this.functionId);
            PluginHandlersManager.notify(this.containerId, this.functionId, 3, this.codec.encodeReplyMap(3, "callback destroy"));
        }
    }

    /* access modifiers changed from: private */
    public final class CallbackImpl implements Callback, EventNotifier {
        private final int callbackId;
        private final int containerId;

        public CallbackImpl(int i, int i2) {
            this.containerId = i;
            this.callbackId = i2;
        }

        @Override // com.huawei.ace.plugin.Callback
        public void reply(Object obj) {
            PluginHandlersManager.notify(this.containerId, this.callbackId, 0, Plugin.this.codec.encodeReply(obj));
        }

        @Override // com.huawei.ace.plugin.Callback
        public void replyJson(String str) {
            PluginHandlersManager.notify(this.containerId, this.callbackId, 0, Plugin.this.codec.encodeJsonReply(str));
        }

        @Override // com.huawei.ace.plugin.EventNotifier
        public void success(Object obj) {
            success(0, obj);
        }

        @Override // com.huawei.ace.plugin.EventNotifier
        public void success(int i, Object obj) {
            PluginHandlersManager.notify(this.containerId, this.callbackId, 0, Plugin.this.codec.encodeReplyMap(i, obj));
        }

        @Override // com.huawei.ace.plugin.EventNotifier
        public void error(int i, Object obj) {
            PluginHandlersManager.notify(this.containerId, this.callbackId, 1, Plugin.this.codec.encodeReplyMap(i, obj));
        }

        @Override // com.huawei.ace.plugin.EventNotifier
        public void endOfNotify() {
            PluginHandlersManager.notify(this.containerId, this.callbackId, 0, Plugin.this.codec.encodeReplyMap(0, "endOfNotify"));
        }
    }

    /* access modifiers changed from: private */
    public final class ResultImpl implements Result {
        private final FrontendMessageReply reply;
        private final String specificFuncName;

        public ResultImpl(Plugin plugin, FrontendMessageReply frontendMessageReply) {
            this(frontendMessageReply, null);
        }

        public ResultImpl(FrontendMessageReply frontendMessageReply, String str) {
            this.reply = frontendMessageReply;
            this.specificFuncName = str;
        }

        private int matchErrorCode(int i) {
            if (i == 2006) {
                if ("subscribe".equals(this.specificFuncName)) {
                    return 2007;
                }
                if ("unsubscribe".equals(this.specificFuncName)) {
                    return 2008;
                }
            }
            return i;
        }

        @Override // com.huawei.ace.plugin.Result
        public void replySuccess(Object obj) {
            this.reply.reply(0, Plugin.this.codec.encodeReply(obj));
        }

        @Override // com.huawei.ace.plugin.Result
        public void replyError(Object obj) {
            this.reply.reply(1, Plugin.this.codec.encodeReply(obj));
        }

        @Override // com.huawei.ace.plugin.Result
        public void replyJsonSuccess(String str) {
            this.reply.reply(0, Plugin.this.codec.encodeJsonReply(str));
        }

        @Override // com.huawei.ace.plugin.Result
        public void replyJsonError(String str) {
            this.reply.reply(1, Plugin.this.codec.encodeJsonReply(str));
        }

        @Override // com.huawei.ace.plugin.Result
        public void replyError(int i, Object obj) {
            this.reply.reply(1, Plugin.this.codec.encodeReplyMap(matchErrorCode(i), obj));
        }

        @Override // com.huawei.ace.plugin.Result
        public void notExistFunction() {
            this.reply.replyPluginGetError(104, "function not defined in platform side");
        }

        @Override // com.huawei.ace.plugin.Result
        public void success(Object obj) {
            this.reply.reply(0, Plugin.this.codec.encodeReplyMap(0, obj));
        }

        @Override // com.huawei.ace.plugin.Result
        public void error(int i, Object obj) {
            replyError(i, obj);
        }

        @Override // com.huawei.ace.plugin.Result
        public void successWithRawString(String str) {
            replyJsonSuccess(str);
        }
    }
}
