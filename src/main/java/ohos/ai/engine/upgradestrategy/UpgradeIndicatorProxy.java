package ohos.ai.engine.upgradestrategy;

import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class UpgradeIndicatorProxy implements IUpgradeIndicator {
    private final IRemoteObject remoteObject;

    UpgradeIndicatorProxy(IRemoteObject iRemoteObject) {
        this.remoteObject = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remoteObject;
    }

    @Override // ohos.ai.engine.upgradestrategy.IUpgradeIndicator
    public void onUpdate(boolean z) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            obtain.writeInterfaceToken(IUpgradeIndicator.DESCRIPTOR);
            obtain.writeInt(z ? 1 : 0);
            this.remoteObject.sendRequest(1, obtain, obtain2, messageOption);
            obtain2.readInt();
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }
}
