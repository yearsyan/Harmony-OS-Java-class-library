package ohos.dcall;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;
import ohos.telephony.TelephonyUtils;

public abstract class CallStateObserverSkeleton extends RemoteObject implements ICallStateObserver {
    private static final String DESCRIPTOR = "ohos.telephony.ICallStateObserver";
    private static final HiLogLabel TAG = new HiLogLabel(3, 218111744, "CallStateObserverSkeleton");
    private static final int TRANSACTION_ON_CALL_FORWARDING_INDICATOR_CHANGED = 4;
    private static final int TRANSACTION_ON_CALL_STATE_CHANGED = 6;
    private static final int TRANSACTION_ON_MESSAGE_WAITING_INDICATOR_CHANGED = 3;

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public CallStateObserverSkeleton() {
        super(DESCRIPTOR);
    }

    private boolean enforceInterface(MessageParcel messageParcel) {
        return TelephonyUtils.SRC_DESCRIPTOR.equals(messageParcel.readInterfaceToken());
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        boolean z = false;
        if (i == 3) {
            if (messageParcel != null && enforceInterface(messageParcel)) {
                if (messageParcel.readInt() != 0) {
                    z = true;
                }
                onVoiceMailMsgIndicatorUpdated(z);
            }
            return true;
        } else if (i == 4) {
            if (messageParcel != null && enforceInterface(messageParcel)) {
                if (messageParcel.readInt() != 0) {
                    z = true;
                }
                onCfuIndicatorUpdated(z);
            }
            return true;
        } else if (i != 6) {
            HiLog.warn(TAG, "call back is not implemented on code: %{public}d", Integer.valueOf(i));
            return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
        } else {
            if (messageParcel != null && enforceInterface(messageParcel)) {
                onCallStateUpdated(messageParcel.readInt(), messageParcel.readString());
            }
            return true;
        }
    }
}
