package ohos.bundlemgr.freeinstall.moduledispatcher;

import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public abstract class ModuleDispatcherStub extends RemoteObject implements IModuleDispatcher {
    private static final int COMMAND_QUERY_ABILITY = 4;
    private static final int COMMAND_SILENT_INSTALL = 1;
    private static final int COMMAND_UPGRADE_CHECK = 2;
    private static final int COMMAND_UPGRADE_INSTALL = 3;
    private static final String DESCRIPTOR = "com.huawei.ohos.abilitydispatcher.openapi.install.FaDispatcher";
    private static final int ERR_OK = 0;
    private static final int ERR_RUNTIME_EXCEPTION = -1;

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public ModuleDispatcherStub(String str) {
        super(str);
    }

    public static IModuleDispatcher asInterface(IRemoteObject iRemoteObject) {
        if (iRemoteObject == null) {
            return null;
        }
        IRemoteBroker queryLocalInterface = iRemoteObject.queryLocalInterface(DESCRIPTOR);
        if (queryLocalInterface == null) {
            return new ModuleDispatcherProxy(iRemoteObject);
        }
        if (queryLocalInterface instanceof IModuleDispatcher) {
            return (IModuleDispatcher) queryLocalInterface;
        }
        return null;
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        if (!DESCRIPTOR.equals(messageParcel.readInterfaceToken())) {
            return false;
        }
        if (i == 1) {
            return handleSilentInstall(messageParcel, messageParcel2);
        }
        if (i == 2) {
            return handleUpgradeCheck(messageParcel, messageParcel2);
        }
        if (i == 3) {
            return handleUpgradeInstall(messageParcel, messageParcel2);
        }
        if (i != 4) {
            return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
        }
        return handleQueryAbility(messageParcel, messageParcel2);
    }

    private boolean handleQueryAbility(MessageParcel messageParcel, MessageParcel messageParcel2) throws RemoteException {
        String queryAbility = queryAbility(messageParcel.readString(), messageParcel.readString());
        messageParcel2.writeNoException();
        messageParcel2.writeString(queryAbility);
        return true;
    }

    private boolean handleUpgradeInstall(MessageParcel messageParcel, MessageParcel messageParcel2) throws RemoteException {
        int upgradeInstall = upgradeInstall(messageParcel.readInt(), messageParcel.readString(), messageParcel.readString(), messageParcel.readInt(), messageParcel.readInt(), ModuleDispatcherReceiverStub.asInterface(messageParcel.readRemoteObject()));
        messageParcel2.writeNoException();
        messageParcel2.writeInt(upgradeInstall);
        return true;
    }

    private boolean handleUpgradeCheck(MessageParcel messageParcel, MessageParcel messageParcel2) throws RemoteException {
        int upgradeCheck = upgradeCheck(messageParcel.readInt(), messageParcel.readString(), messageParcel.readString(), messageParcel.readInt(), ModuleDispatcherReceiverStub.asInterface(messageParcel.readRemoteObject()));
        messageParcel2.writeNoException();
        messageParcel2.writeInt(upgradeCheck);
        return true;
    }

    private boolean handleSilentInstall(MessageParcel messageParcel, MessageParcel messageParcel2) throws RemoteException {
        int silentInstall = silentInstall(messageParcel.readInt(), messageParcel.readString(), messageParcel.readString(), messageParcel.readInt(), ModuleDispatcherReceiverStub.asInterface(messageParcel.readRemoteObject()));
        messageParcel2.writeNoException();
        messageParcel2.writeInt(silentInstall);
        return true;
    }
}
