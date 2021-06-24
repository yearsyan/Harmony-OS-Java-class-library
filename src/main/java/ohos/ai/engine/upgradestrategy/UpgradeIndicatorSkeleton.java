package ohos.ai.engine.upgradestrategy;

import java.util.Optional;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public abstract class UpgradeIndicatorSkeleton extends RemoteObject implements IUpgradeIndicator {
    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public UpgradeIndicatorSkeleton() {
        super(IUpgradeIndicator.DESCRIPTOR);
    }

    public static Optional<IUpgradeIndicator> asInterface(IRemoteObject iRemoteObject) {
        if (iRemoteObject == null) {
            return Optional.empty();
        }
        IRemoteBroker queryLocalInterface = iRemoteObject.queryLocalInterface(IUpgradeIndicator.DESCRIPTOR);
        if (queryLocalInterface == null || !(queryLocalInterface instanceof IUpgradeIndicator)) {
            return Optional.of(new UpgradeIndicatorProxy(iRemoteObject));
        }
        return Optional.of((IUpgradeIndicator) queryLocalInterface);
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        messageParcel.readInterfaceToken();
        if (i != 1) {
            return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
        }
        onUpdate(messageParcel.readInt() != 0);
        messageParcel2.writeInt(0);
        return true;
    }
}
