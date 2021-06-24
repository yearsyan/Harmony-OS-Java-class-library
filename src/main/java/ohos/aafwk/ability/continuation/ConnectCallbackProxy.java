package ohos.aafwk.ability.continuation;

import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class ConnectCallbackProxy implements IConnectCallback {
    private static final int COMMAND_CONNECT = 1;
    private static final int COMMAND_DISCONNECT = 2;
    private static final String DESCRIPTOR = "com.huawei.controlcenter.featureability.sdk.IConnectCallback";
    private static final int ERR_OK = 0;
    private final IRemoteObject remote;

    public ConnectCallbackProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    @Override // ohos.aafwk.ability.continuation.IConnectCallback
    public void connect(String str, String str2) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeString(str);
        obtain.writeString(str2);
        try {
            this.remote.sendRequest(1, obtain, obtain2, messageOption);
            obtain2.readException();
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.aafwk.ability.continuation.IConnectCallback
    public void disconnect(String str) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeString(str);
        try {
            this.remote.sendRequest(2, obtain, obtain2, messageOption);
            obtain2.readException();
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }
}
