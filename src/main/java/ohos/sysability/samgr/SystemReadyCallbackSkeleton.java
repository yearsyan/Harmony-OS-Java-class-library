package ohos.sysability.samgr;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public abstract class SystemReadyCallbackSkeleton extends RemoteObject implements ISystemReadyCallback {
    private static final int COMMAND_ON_SYSTEM_READY_NOTIFY = 1;
    private static final String DESCRIPTOR = "ohos.systemReadyCallback.accessToken";
    private static final int ERR_OK = 0;
    private static final int ERR_RUNTIME_EXCEPTION = -1;
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218109952, "SA");

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public SystemReadyCallbackSkeleton() {
        super(DESCRIPTOR);
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        if (messageParcel == null || messageParcel2 == null) {
            HiLog.error(LABEL, "SystemReadyCallbackSkeleton::onRemoteRequest param invalid", new Object[0]);
            return false;
        } else if (!DESCRIPTOR.equals(messageParcel.readInterfaceToken())) {
            HiLog.error(LABEL, "SystemReadyCallbackSkeleton::onRemoteRequest token is invalid.", new Object[0]);
            return false;
        } else if (i != 1) {
            return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
        } else {
            onSystemReadyNotify();
            messageParcel2.writeInt(0);
            return true;
        }
    }
}
