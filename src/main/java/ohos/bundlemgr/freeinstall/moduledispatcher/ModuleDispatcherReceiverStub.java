package ohos.bundlemgr.freeinstall.moduledispatcher;

import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public abstract class ModuleDispatcherReceiverStub extends RemoteObject implements IModuleDispatcherReceiver {
    private static final int COMMAND_ON_DOWNLOAD_PROGRESS = 2;
    private static final int COMMAND_ON_FINISHED = 1;

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public ModuleDispatcherReceiverStub(String str) {
        super(str);
    }

    public static IModuleDispatcherReceiver asInterface(IRemoteObject iRemoteObject) {
        if (iRemoteObject == null) {
            return null;
        }
        IRemoteBroker queryLocalInterface = iRemoteObject.queryLocalInterface(IModuleDispatcherReceiver.DESCRIPTOR);
        if (queryLocalInterface == null) {
            return new ModuleDispatcherReceiverProxy(iRemoteObject);
        }
        if (queryLocalInterface instanceof IModuleDispatcherReceiver) {
            return (IModuleDispatcherReceiver) queryLocalInterface;
        }
        return null;
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        if (!IModuleDispatcherReceiver.DESCRIPTOR.equals(messageParcel.readInterfaceToken())) {
            return false;
        }
        if (i == 1) {
            return onFinished(messageParcel, messageParcel2);
        }
        if (i != 2) {
            return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
        }
        return onDownloadProgress(messageParcel, messageParcel2);
    }

    private boolean onFinished(MessageParcel messageParcel, MessageParcel messageParcel2) throws RemoteException {
        onFinished(messageParcel.readInt(), messageParcel.readInt(), messageParcel.readString());
        messageParcel2.writeNoException();
        return true;
    }

    private boolean onDownloadProgress(MessageParcel messageParcel, MessageParcel messageParcel2) throws RemoteException {
        onDownloadProgress(messageParcel.readInt(), messageParcel.readInt(), messageParcel.readInt());
        messageParcel2.writeNoException();
        return true;
    }
}
