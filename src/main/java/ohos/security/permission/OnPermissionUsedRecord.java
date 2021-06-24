package ohos.security.permission;

import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;

public abstract class OnPermissionUsedRecord extends RemoteObject implements IRemoteBroker {
    private static final String DESCRIPTOR = "ohos.security.permission.OnPermissionUsedRecord";
    private static final HiLogLabel LABEL = HiLogLabelUtil.INNER_KIT.newHiLogLabel(TAG);
    private static final int ON_QUERIED = 0;
    private static final String TAG = "OnPermissionUsedRecord";

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public abstract void onQueried(int i, QueryPermissionUsedResult queryPermissionUsedResult) throws RemoteException;

    @Override // ohos.rpc.RemoteObject
    @Deprecated
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        return false;
    }

    public OnPermissionUsedRecord() {
        super(DESCRIPTOR);
    }
}
