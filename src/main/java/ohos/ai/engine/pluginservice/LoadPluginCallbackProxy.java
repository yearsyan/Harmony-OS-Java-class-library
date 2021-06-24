package ohos.ai.engine.pluginservice;

import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class LoadPluginCallbackProxy implements ILoadPluginCallback {
    private final IRemoteObject remoteObject;

    LoadPluginCallbackProxy(IRemoteObject iRemoteObject) {
        this.remoteObject = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remoteObject;
    }

    @Override // ohos.ai.engine.pluginservice.ILoadPluginCallback
    public void onResult(int i) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            obtain.writeInterfaceToken(ILoadPluginCallback.DESCRIPTOR);
            obtain.writeInt(i);
            this.remoteObject.sendRequest(1, obtain, obtain2, messageOption);
            obtain2.readInt();
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    @Override // ohos.ai.engine.pluginservice.ILoadPluginCallback
    public void onProgress(int i) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            obtain.writeInterfaceToken(ILoadPluginCallback.DESCRIPTOR);
            obtain.writeInt(i);
            this.remoteObject.sendRequest(2, obtain, obtain2, messageOption);
            obtain2.readInt();
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }
}
