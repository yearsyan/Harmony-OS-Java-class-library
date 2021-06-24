package ohos.data.dataability.impl;

import java.lang.ref.WeakReference;
import java.util.Objects;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public class RemoteResultSetObserverStub extends RemoteObject implements IRemoteResultSetObserver {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218109536, "RemoteResultSetObserverStub");
    private WeakReference<RemoteResultSet> resultSet;

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public RemoteResultSetObserverStub(String str, WeakReference<RemoteResultSet> weakReference) {
        super(str);
        this.resultSet = weakReference;
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        if (i != 2) {
            return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
        }
        onChange();
        return true;
    }

    @Override // ohos.data.dataability.impl.IRemoteResultSetObserver
    public void onChange() {
        HiLog.info(LABEL, "notifyChange", new Object[0]);
        ((RemoteResultSet) Objects.requireNonNull(this.resultSet.get())).notifyChange();
    }
}
