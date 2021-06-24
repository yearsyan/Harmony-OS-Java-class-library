package ohos.ai.engine.pluginservice;

import java.util.Optional;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public abstract class LoadPluginCallbackSkeleton extends RemoteObject implements ILoadPluginCallback {
    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public LoadPluginCallbackSkeleton() {
        super(ILoadPluginCallback.DESCRIPTOR);
    }

    public static Optional<ILoadPluginCallback> asInterface(IRemoteObject iRemoteObject) {
        if (iRemoteObject == null) {
            return Optional.empty();
        }
        IRemoteBroker queryLocalInterface = iRemoteObject.queryLocalInterface(ILoadPluginCallback.DESCRIPTOR);
        if (queryLocalInterface == null || !(queryLocalInterface instanceof ILoadPluginCallback)) {
            return Optional.of(new LoadPluginCallbackProxy(iRemoteObject));
        }
        return Optional.of((ILoadPluginCallback) queryLocalInterface);
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        messageParcel.readInterfaceToken();
        if (i == 1) {
            onResult(messageParcel.readInt());
            messageParcel2.writeInt(0);
            return true;
        } else if (i != 2) {
            return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
        } else {
            onProgress(messageParcel.readInt());
            messageParcel2.writeInt(0);
            return true;
        }
    }
}
