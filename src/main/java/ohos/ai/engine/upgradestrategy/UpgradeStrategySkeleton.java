package ohos.ai.engine.upgradestrategy;

import java.util.Optional;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public abstract class UpgradeStrategySkeleton extends RemoteObject implements IUpgradeStrategy {
    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public UpgradeStrategySkeleton() {
        super(IUpgradeStrategy.DESCRIPTOR);
    }

    public static Optional<IUpgradeStrategy> asInterface(IRemoteObject iRemoteObject) {
        if (iRemoteObject == null) {
            return Optional.empty();
        }
        IRemoteBroker queryLocalInterface = iRemoteObject.queryLocalInterface(IUpgradeStrategy.DESCRIPTOR);
        if (queryLocalInterface == null || !(queryLocalInterface instanceof IUpgradeStrategy)) {
            return Optional.of(new UpgradeStrategyProxy(iRemoteObject));
        }
        return Optional.of((IUpgradeStrategy) queryLocalInterface);
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        messageParcel.readInterfaceToken();
        if (i == 3) {
            checkHiAiAppUpdate(UpgradeIndicatorSkeleton.asInterface(messageParcel.readRemoteObject()).orElse(null));
            messageParcel2.writeInt(0);
            return true;
        } else if (i != 4) {
            return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
        } else {
            updateHiAiApp();
            messageParcel2.writeInt(0);
            return true;
        }
    }
}
