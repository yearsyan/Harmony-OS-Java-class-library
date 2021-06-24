package ohos.bundlemgr.freeinstall.moduledispatcher;

import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class ModuleDispatcherReceiverProxy implements IModuleDispatcherReceiver {
    private static final int COMMAND_ON_DOWNLOAD_PROGRESS = 2;
    private static final int COMMAND_ON_FINISHED = 1;
    private final IRemoteObject remote;

    public ModuleDispatcherReceiverProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    @Override // ohos.bundlemgr.freeinstall.moduledispatcher.IModuleDispatcherReceiver
    public void onFinished(int i, int i2, String str) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken(IModuleDispatcherReceiver.DESCRIPTOR);
        obtain.writeInt(i);
        obtain.writeInt(i2);
        obtain.writeString(str);
        try {
            this.remote.sendRequest(1, obtain, obtain2, messageOption);
            obtain2.readException();
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.bundlemgr.freeinstall.moduledispatcher.IModuleDispatcherReceiver
    public void onDownloadProgress(int i, int i2, int i3) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken(IModuleDispatcherReceiver.DESCRIPTOR);
        obtain.writeInt(i);
        obtain.writeInt(i2);
        obtain.writeInt(i3);
        try {
            this.remote.sendRequest(2, obtain, obtain2, messageOption);
            obtain2.readException();
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }
}
