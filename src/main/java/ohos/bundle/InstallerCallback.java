package ohos.bundle;

import ohos.annotation.SystemApi;
import ohos.appexecfwk.utils.AppLog;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public abstract class InstallerCallback extends RemoteObject implements IInstallerCallback {
    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    /* access modifiers changed from: protected */
    @SystemApi
    public void onDownloadProgress(int i, int i2) {
    }

    @Override // ohos.bundle.IInstallerCallback
    public abstract void onFinished(int i, String str);

    public InstallerCallback() {
        super(IInstallerCallback.DESCRIPTOR);
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        if (messageParcel == null || messageParcel2 == null) {
            return false;
        }
        AppLog.d("InstallerCallback::onRemoteRequest code: %{public}d", Integer.valueOf(i));
        if (!IInstallerCallback.DESCRIPTOR.equals(messageParcel.readInterfaceToken())) {
            AppLog.e("InstallerCallback::onRemoteRequest token invalid", new Object[0]);
            return false;
        }
        if (i == 0) {
            onFinished(messageParcel);
        } else if (i != 1) {
            AppLog.w("InstallerCallback::onTransact unknown, code: %{public}d", Integer.valueOf(i));
        } else {
            onDownloadProgress(messageParcel);
        }
        return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
    }

    private void onFinished(MessageParcel messageParcel) {
        int readInt = messageParcel.readInt();
        String readString = messageParcel.readString();
        AppLog.d("InstallerCallback::onfinish result: status = %{public}d, statusMessage = %{public}s", Integer.valueOf(readInt), readString);
        onFinished(readInt, readString);
    }

    private void onDownloadProgress(MessageParcel messageParcel) throws RemoteException {
        int readInt = messageParcel.readInt();
        int readInt2 = messageParcel.readInt();
        AppLog.d("InstallerCallback::onDownloadProgress result: fileSize = %{public}d, downloadedSize = %{public}d", Integer.valueOf(readInt), Integer.valueOf(readInt2));
        onDownloadProgress(readInt, readInt2);
    }
}
