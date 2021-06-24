package ohos.bundle;

import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class InstallerCallbackProxy implements IInstallerCallback {
    private final IRemoteObject remote;

    public InstallerCallbackProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    @Override // ohos.bundle.IInstallerCallback
    public void onFinished(int i, String str) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken(IInstallerCallback.DESCRIPTOR);
        obtain.writeInt(i);
        obtain.writeString(str);
        try {
            this.remote.sendRequest(0, obtain, obtain2, messageOption);
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    public void onDownloadProgress(int i, int i2) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken(IInstallerCallback.DESCRIPTOR);
        obtain.writeInt(i);
        obtain.writeInt(i2);
        try {
            this.remote.sendRequest(1, obtain, obtain2, messageOption);
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }
}
