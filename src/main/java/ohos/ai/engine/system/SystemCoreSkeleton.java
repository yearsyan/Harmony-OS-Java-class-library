package ohos.ai.engine.system;

import java.util.Optional;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public abstract class SystemCoreSkeleton extends RemoteObject implements ISystemCore {
    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public SystemCoreSkeleton() {
        super(ISystemCore.DESCRIPTOR);
    }

    public static Optional<ISystemCore> asInterface(IRemoteObject iRemoteObject) {
        if (iRemoteObject == null) {
            return Optional.empty();
        }
        IRemoteBroker queryLocalInterface = iRemoteObject.queryLocalInterface(ISystemCore.DESCRIPTOR);
        if (queryLocalInterface == null || !(queryLocalInterface instanceof ISystemCore)) {
            return Optional.of(new SystemCoreProxy(iRemoteObject));
        }
        return Optional.of((ISystemCore) queryLocalInterface);
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        messageParcel.readInterfaceToken();
        switch (i) {
            case 1:
                return messageParcel2.writeString(getProp(messageParcel.readString(), messageParcel.readString()).orElse(null));
            case 2:
                return messageParcel2.writeString(getUdid().orElse(null));
            case 3:
                return messageParcel2.writeString(getSerialNumber().orElse(null));
            case 4:
                return messageParcel2.writeString(getSystemVersion().orElse(null));
            case 5:
                return messageParcel2.writeString(getSystemModel().orElse(null));
            case 6:
                return messageParcel2.writeString(getDeviceBrand().orElse(null));
            default:
                return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
        }
    }
}
