package ohos.data.dataability.impl;

import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

public class ResultSetRemoteTransportDescriptor implements Sequenceable {
    private String[] columnNames;
    private int count;
    private IResultSetRemoteTransport remoteTransport;

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        if (!(parcel instanceof MessageParcel)) {
            return false;
        }
        MessageParcel messageParcel = (MessageParcel) parcel;
        messageParcel.writeRemoteObject(this.remoteTransport.asObject());
        messageParcel.writeStringArray(this.columnNames);
        messageParcel.writeInt(this.count);
        return true;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        if (!(parcel instanceof MessageParcel)) {
            return false;
        }
        MessageParcel messageParcel = (MessageParcel) parcel;
        IRemoteObject readRemoteObject = messageParcel.readRemoteObject();
        if (readRemoteObject != null) {
            this.remoteTransport = new ResultSetRemoteTransportProxy(readRemoteObject);
        }
        this.columnNames = messageParcel.readStringArray();
        this.count = messageParcel.readInt();
        return true;
    }

    public IResultSetRemoteTransport getRemoteTransport() {
        return this.remoteTransport;
    }

    public void setRemoteTransport(IResultSetRemoteTransport iResultSetRemoteTransport) {
        this.remoteTransport = iResultSetRemoteTransport;
    }

    public String[] getColumnNames() {
        return this.columnNames;
    }

    public void setColumnNames(String[] strArr) {
        this.columnNames = strArr;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i;
    }
}
