package ohos.ace.plugin.featureability.proxy;

import com.huawei.ace.plugin.Callback;
import com.huawei.ace.plugin.Result;
import com.huawei.ace.runtime.ALog;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public class CallbackProxy<T> extends RemoteObject implements IRemoteBroker {
    private static final String TAG = CallbackProxy.class.getSimpleName();
    private T callback;
    private boolean zidl;

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public CallbackProxy(T t, boolean z) {
        super(TAG);
        this.callback = t;
        this.zidl = z;
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        T t = this.callback;
        if (t instanceof Result) {
            t.replyJsonSuccess(messageParcel.readString());
            return true;
        } else if (t instanceof Callback) {
            call(t, i, messageParcel);
            return true;
        } else {
            ALog.w(TAG, "callback handler already be released");
            return false;
        }
    }

    private void call(Callback callback2, int i, MessageParcel messageParcel) {
        if (this.zidl) {
            String readInterfaceToken = messageParcel.readInterfaceToken();
            String readString = messageParcel.readString();
            callback2.replyJson("[\"" + readInterfaceToken + "\"," + i + "," + readString + "]");
            return;
        }
        callback2.replyJson("{\"code\":" + i + ",\"data\":" + messageParcel.readString() + "}");
    }
}
