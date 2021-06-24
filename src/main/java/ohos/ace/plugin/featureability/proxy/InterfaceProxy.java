package ohos.ace.plugin.featureability.proxy;

import com.huawei.ace.plugin.Callback;
import com.huawei.ace.plugin.Result;
import com.huawei.ace.runtime.ALog;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import ohos.ace.ability.AceAbility;
import ohos.ace.plugin.featureability.dto.InterfaceElement;
import ohos.ace.plugin.featureability.dto.RequestHeader;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.tools.C0000Bytrace;
import ohos.utils.fastjson.JSONObject;

public abstract class InterfaceProxy<T> {
    private static final String TAG = InterfaceProxy.class.getSimpleName();
    protected final AceAbility aceAbility;
    protected final InterfaceElement interfaceElement;
    protected T interfaceInstance;
    protected final byte[] lock = new byte[0];
    protected final WeakReference<InterfaceProxyManager> proxyManager;

    /* access modifiers changed from: protected */
    public boolean checkConnect() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void releaseConnect() {
    }

    public InterfaceProxy(InterfaceProxyManager interfaceProxyManager, AceAbility aceAbility2, InterfaceElement interfaceElement2) {
        this.proxyManager = new WeakReference<>(interfaceProxyManager);
        this.aceAbility = aceAbility2;
        this.interfaceElement = interfaceElement2;
    }

    /* access modifiers changed from: protected */
    public void process(Result result, RequestHeader requestHeader, Object... objArr) {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(!requestHeader.isSync());
        try {
            if (!writeParam(obtain, obtain2, requestHeader, objArr)) {
                ALog.e(TAG, "write request data failed");
                result.replyError(2006, "write request data failed");
            } else if (requestHeader.isSync() || writeReply(obtain, obtain2, new CallbackProxy(result, requestHeader.isRequestZidl()))) {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ACE, "callAppCode");
                if (!sendRequest(requestHeader.getCode().intValue(), obtain, obtain2, messageOption)) {
                    ALog.e(TAG, "interface return false");
                    result.replyError(2006, "request failed");
                } else {
                    C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ACE, "callAppCode");
                    processResult(requestHeader, result, obtain2);
                    obtain2.reclaim();
                    obtain.reclaim();
                    return;
                }
            } else {
                ALog.e(TAG, "write result callback failed");
                result.replyError(2006, "write request callback failed");
            }
            obtain2.reclaim();
            obtain.reclaim();
        } catch (RemoteException e) {
            String str = TAG;
            ALog.e(str, "call interface exception: " + e.toString());
            result.replyError(2010, "request RemoteException");
        } catch (Throwable th) {
            obtain2.reclaim();
            obtain.reclaim();
            throw th;
        }
    }

    private void processResult(RequestHeader requestHeader, Result result, MessageParcel messageParcel) {
        if (requestHeader.isSync()) {
            if (requestHeader.isRequestZidl()) {
                messageParcel.readException();
            }
            result.replyJsonSuccess(messageParcel.readString());
        }
    }

    public final void processRequest(Result result, RequestHeader requestHeader, Object... objArr) {
        String str = TAG;
        ALog.d(str, "process interface request, requestHeader: " + requestHeader.toString());
        if (!checkConnect()) {
            this.proxyManager.get().removeInterfaceProxy(this.interfaceElement);
            result.replyError(2003, "connect interface failed");
            return;
        }
        process(result, requestHeader, objArr);
    }

    /* access modifiers changed from: protected */
    public boolean writeParam(MessageParcel messageParcel, MessageParcel messageParcel2, RequestHeader requestHeader, Object... objArr) {
        boolean isRequestZidl = requestHeader.isRequestZidl();
        if (isRequestZidl && !messageParcel.writeInterfaceToken(requestHeader.getToken())) {
            return false;
        }
        LinkedList linkedList = new LinkedList();
        for (Object obj : objArr) {
            if (obj instanceof Callback) {
                if (!messageParcel.writeRemoteObject(new CallbackProxy((Callback) obj, isRequestZidl))) {
                    return false;
                }
            } else if (isRequestZidl) {
                linkedList.add(obj);
            } else if (!writeStringValue(messageParcel, obj)) {
                return false;
            }
        }
        if (!isRequestZidl || linkedList.size() <= 0 || writeValue(messageParcel, linkedList)) {
            return true;
        }
        return false;
    }

    private boolean writeStringValue(MessageParcel messageParcel, Object obj) {
        if (obj instanceof String) {
            return messageParcel.writeString((String) obj);
        }
        return messageParcel.writeString(JSONObject.toJSONString(obj));
    }

    private boolean writeValue(MessageParcel messageParcel, Object obj) {
        try {
            byte[] convertToBytes = convertToBytes(obj);
            if (messageParcel.writeInt(convertToBytes.length)) {
                return messageParcel.writeRawData(convertToBytes, convertToBytes.length);
            }
            return false;
        } catch (IOException e) {
            String str = TAG;
            ALog.e(str, "change params to byte exception: " + e.toString());
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001b, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001c, code lost:
        $closeResource(r3, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001f, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0022, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0023, code lost:
        $closeResource(r3, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0026, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] convertToBytes(java.lang.Object r3) throws java.io.IOException {
        /*
            r2 = this;
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream
            r2.<init>()
            java.io.ObjectOutputStream r0 = new java.io.ObjectOutputStream     // Catch:{ all -> 0x0020 }
            r0.<init>(r2)     // Catch:{ all -> 0x0020 }
            r0.writeObject(r3)     // Catch:{ all -> 0x0019 }
            byte[] r3 = r2.toByteArray()     // Catch:{ all -> 0x0019 }
            r1 = 0
            $closeResource(r1, r0)
            $closeResource(r1, r2)
            return r3
        L_0x0019:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x001b }
        L_0x001b:
            r1 = move-exception
            $closeResource(r3, r0)
            throw r1
        L_0x0020:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x0022 }
        L_0x0022:
            r0 = move-exception
            $closeResource(r3, r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.ace.plugin.featureability.proxy.InterfaceProxy.convertToBytes(java.lang.Object):byte[]");
    }

    private static /* synthetic */ void $closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        } else {
            autoCloseable.close();
        }
    }

    /* access modifiers changed from: protected */
    public boolean writeReply(MessageParcel messageParcel, MessageParcel messageParcel2, IRemoteObject iRemoteObject) {
        return messageParcel.writeRemoteObject(iRemoteObject);
    }

    /* access modifiers changed from: protected */
    public boolean sendRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        throw new RemoteException("the method 'callRemoteMethod' is not impl");
    }
}
