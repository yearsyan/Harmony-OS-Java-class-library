package ohos.ai.engine.cloudstrategy.grs;

import java.util.Optional;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public abstract class GrsCallbackSkeleton extends RemoteObject implements IGrsCallback {
    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public GrsCallbackSkeleton() {
        super(IGrsCallback.DESCRIPTOR);
    }

    public static Optional<IGrsCallback> asInterface(IRemoteObject iRemoteObject) {
        if (iRemoteObject == null) {
            return Optional.empty();
        }
        IRemoteBroker queryLocalInterface = iRemoteObject.queryLocalInterface(IGrsCallback.DESCRIPTOR);
        if (queryLocalInterface == null || !(queryLocalInterface instanceof IGrsCallback)) {
            return Optional.of(new GrsCallbackProxy(iRemoteObject));
        }
        return Optional.of((IGrsCallback) queryLocalInterface);
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        messageParcel.readInterfaceToken();
        if (i != 1) {
            return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
        }
        onGrsResult(messageParcel.readInt(), messageParcel.readString());
        return true;
    }
}
