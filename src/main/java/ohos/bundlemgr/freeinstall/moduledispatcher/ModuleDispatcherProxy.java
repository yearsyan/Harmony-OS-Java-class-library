package ohos.bundlemgr.freeinstall.moduledispatcher;

import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class ModuleDispatcherProxy implements IModuleDispatcher {
    private static final int COMMAND_QUERY_ABILITY = 4;
    private static final int COMMAND_SILENT_INSTALL = 1;
    private static final int COMMAND_UPGRADE_CHECK = 2;
    private static final int COMMAND_UPGRADE_INSTALL = 3;
    private static final String DESCRIPTOR = "com.huawei.ohos.abilitydispatcher.openapi.install.FaDispatcher";
    private final IRemoteObject remote;

    public ModuleDispatcherProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    @Override // ohos.bundlemgr.freeinstall.moduledispatcher.IModuleDispatcher
    public int silentInstall(int i, String str, String str2, int i2, IModuleDispatcherReceiver iModuleDispatcherReceiver) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeInt(i);
        obtain.writeString(str);
        obtain.writeString(str2);
        obtain.writeInt(i2);
        obtain.writeRemoteObject(iModuleDispatcherReceiver.asObject());
        try {
            this.remote.sendRequest(1, obtain, obtain2, messageOption);
            obtain2.readException();
            return obtain2.readInt();
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.bundlemgr.freeinstall.moduledispatcher.IModuleDispatcher
    public int upgradeCheck(int i, String str, String str2, int i2, IModuleDispatcherReceiver iModuleDispatcherReceiver) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeInt(i);
        obtain.writeString(str);
        obtain.writeString(str2);
        obtain.writeInt(i2);
        obtain.writeRemoteObject(iModuleDispatcherReceiver.asObject());
        try {
            this.remote.sendRequest(2, obtain, obtain2, messageOption);
            obtain2.readException();
            return obtain2.readInt();
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.bundlemgr.freeinstall.moduledispatcher.IModuleDispatcher
    public int upgradeInstall(int i, String str, String str2, int i2, int i3, IModuleDispatcherReceiver iModuleDispatcherReceiver) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeInt(i);
        obtain.writeString(str);
        obtain.writeString(str2);
        obtain.writeInt(i2);
        obtain.writeInt(i3);
        obtain.writeRemoteObject(iModuleDispatcherReceiver.asObject());
        try {
            this.remote.sendRequest(3, obtain, obtain2, messageOption);
            obtain2.readException();
            return obtain2.readInt();
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.bundlemgr.freeinstall.moduledispatcher.IModuleDispatcher
    public String queryAbility(String str, String str2) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeString(str);
        obtain.writeString(str2);
        try {
            this.remote.sendRequest(4, obtain, obtain2, messageOption);
            obtain2.readException();
            return obtain2.readString();
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }
}
