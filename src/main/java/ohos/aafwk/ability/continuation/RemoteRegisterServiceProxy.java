package ohos.aafwk.ability.continuation;

import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class RemoteRegisterServiceProxy implements IRemoteRegisterService {
    private static final int COMMAND_REGISTER = 1;
    private static final int COMMAND_SHOW_DEVICE_LIST = 4;
    private static final int COMMAND_UNREGISTER = 2;
    private static final int COMMAND_UPDATE_CONNECT_STATUS = 3;
    private static final String DESCRIPTOR = "com.huawei.controlcenter.featureability.sdk.IRemoteRegisterService";
    private static final int ERR_OK = 0;
    private final IRemoteObject remote;

    public RemoteRegisterServiceProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    @Override // ohos.aafwk.ability.continuation.IRemoteRegisterService
    public int register(String str, IRemoteObject iRemoteObject, ExtraParams extraParams, IConnectCallback iConnectCallback) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeString(str);
        obtain.writeRemoteObject(iRemoteObject);
        obtain.writeSequenceable(extraParams);
        obtain.writeRemoteObject(iConnectCallback.asObject());
        try {
            this.remote.sendRequest(1, obtain, obtain2, messageOption);
            obtain2.readException();
            return obtain2.readInt();
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.aafwk.ability.continuation.IRemoteRegisterService
    public boolean unregister(int i) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeInt(i);
        try {
            this.remote.sendRequest(2, obtain, obtain2, messageOption);
            obtain2.readException();
            boolean z = true;
            if (obtain2.readInt() != 1) {
                z = false;
            }
            return z;
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.aafwk.ability.continuation.IRemoteRegisterService
    public boolean updateConnectStatus(int i, String str, int i2) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeInt(i);
        obtain.writeString(str);
        obtain.writeInt(i2);
        try {
            this.remote.sendRequest(3, obtain, obtain2, messageOption);
            obtain2.readException();
            boolean z = true;
            if (obtain2.readInt() != 1) {
                z = false;
            }
            return z;
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.aafwk.ability.continuation.IRemoteRegisterService
    public boolean showDeviceList(int i, ExtraParams extraParams) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeInt(i);
        obtain.writeSequenceable(extraParams);
        try {
            this.remote.sendRequest(4, obtain, obtain2, messageOption);
            obtain2.readException();
            boolean z = true;
            if (obtain2.readInt() != 1) {
                z = false;
            }
            return z;
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }
}
