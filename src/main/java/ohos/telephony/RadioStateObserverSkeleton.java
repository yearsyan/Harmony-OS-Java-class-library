package ohos.telephony;

import java.util.ArrayList;
import java.util.List;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public abstract class RadioStateObserverSkeleton extends RemoteObject implements IRadioStateObserver {
    private static final String DESCRIPTOR = "ohos.telephony.IRadioStateObserver";
    private static final HiLogLabel TAG = new HiLogLabel(3, 218111744, "RadioStateObserverSkeleton");
    private static final int TRANSACTION_ONCELLINFOCHANGED = 12;
    private static final int TRANSACTION_ONSERVICESTATECHANGED = 1;
    private static final int TRANSACTION_ONSIGNALINFOCHANGED = 9;

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public RadioStateObserverSkeleton() {
        super(DESCRIPTOR);
    }

    private boolean enforceInterface(MessageParcel messageParcel) {
        return TelephonyUtils.SRC_DESCRIPTOR.equals(messageParcel.readInterfaceToken());
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        List<SignalInformation> list;
        NetworkState networkState = null;
        if (i != 1) {
            if (i != 9) {
                if (i != 12) {
                    HiLog.warn(TAG, "call back is not implemented on code: %{public}d", Integer.valueOf(i));
                    return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
                } else if (!enforceInterface(messageParcel)) {
                    return true;
                } else {
                    onCellInfoUpdated(RadioInfoManager.getInstance(null).createCellInfoFromObserverParcel(messageParcel));
                    return true;
                }
            } else if (!enforceInterface(messageParcel)) {
                return true;
            } else {
                if (messageParcel.readInt() != 0) {
                    list = RadioInfoManager.getInstance(null).createSignalInfoFromObserverParcel(messageParcel);
                } else {
                    list = new ArrayList<>();
                }
                onSignalInfoUpdated(list);
                return true;
            }
        } else if (!enforceInterface(messageParcel)) {
            return true;
        } else {
            if (messageParcel.readInt() > 0) {
                networkState = RadioInfoManager.getInstance(null).createNetworkStateFromObserverParcel(messageParcel);
            }
            onNetworkStateUpdated(networkState);
            return true;
        }
    }
}
