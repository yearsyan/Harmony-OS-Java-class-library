package ohos.aafwk.ability.continuation;

import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public abstract class ConnectCallbackStub extends RemoteObject implements IConnectCallback {
    private static final int COMMAND_CONNECT = 1;
    private static final int COMMAND_DISCONNECT = 2;
    private static final String DESCRIPTOR = "com.huawei.controlcenter.featureability.sdk.IConnectCallback";
    private static final int ERR_OK = 0;
    private static final int ERR_RUNTIME_EXCEPTION = -1;

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public ConnectCallbackStub(String str) {
        super(str);
    }

    public static IConnectCallback asInterface(IRemoteObject iRemoteObject) {
        if (iRemoteObject == null) {
            return null;
        }
        IRemoteBroker queryLocalInterface = iRemoteObject.queryLocalInterface(DESCRIPTOR);
        if (queryLocalInterface == null) {
            return new ConnectCallbackProxy(iRemoteObject);
        }
        if (queryLocalInterface instanceof IConnectCallback) {
            return (IConnectCallback) queryLocalInterface;
        }
        return null;
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        if (!DESCRIPTOR.equals(messageParcel.readInterfaceToken())) {
            return false;
        }
        if (i == 1) {
            connect(messageParcel.readString(), messageParcel.readString());
            messageParcel2.writeNoException();
            return true;
        } else if (i != 2) {
            return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
        } else {
            disconnect(messageParcel.readString());
            messageParcel2.writeNoException();
            return true;
        }
    }
}
