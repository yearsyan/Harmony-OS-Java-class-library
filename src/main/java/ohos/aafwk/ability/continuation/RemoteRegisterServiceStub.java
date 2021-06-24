package ohos.aafwk.ability.continuation;

import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public abstract class RemoteRegisterServiceStub extends RemoteObject implements IRemoteRegisterService {
    private static final int COMMAND_REGISTER = 1;
    private static final int COMMAND_SHOW_DEVICE_LIST = 4;
    private static final int COMMAND_UNREGISTER = 2;
    private static final int COMMAND_UPDATE_CONNECT_STATUS = 3;
    private static final String DESCRIPTOR = "com.huawei.controlcenter.featureability.sdk.IRemoteRegisterService";
    private static final int ERR_OK = 0;
    private static final int ERR_RUNTIME_EXCEPTION = -1;

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public RemoteRegisterServiceStub(String str) {
        super(str);
    }

    public static IRemoteRegisterService asInterface(IRemoteObject iRemoteObject) {
        if (iRemoteObject == null) {
            return null;
        }
        IRemoteBroker queryLocalInterface = iRemoteObject.queryLocalInterface(DESCRIPTOR);
        if (queryLocalInterface == null) {
            return new RemoteRegisterServiceProxy(iRemoteObject);
        }
        if (queryLocalInterface instanceof IRemoteRegisterService) {
            return (IRemoteRegisterService) queryLocalInterface;
        }
        return null;
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        if (!DESCRIPTOR.equals(messageParcel.readInterfaceToken())) {
            return false;
        }
        if (i == 1) {
            String readString = messageParcel.readString();
            IRemoteObject readRemoteObject = messageParcel.readRemoteObject();
            ExtraParams extraParams = new ExtraParams();
            messageParcel.readSequenceable(extraParams);
            int register = register(readString, readRemoteObject, extraParams, ConnectCallbackStub.asInterface(messageParcel.readRemoteObject()));
            messageParcel2.writeNoException();
            messageParcel2.writeInt(register);
            return true;
        } else if (i == 2) {
            boolean unregister = unregister(messageParcel.readInt());
            messageParcel2.writeNoException();
            messageParcel2.writeInt(unregister ? 1 : 0);
            return true;
        } else if (i == 3) {
            boolean updateConnectStatus = updateConnectStatus(messageParcel.readInt(), messageParcel.readString(), messageParcel.readInt());
            messageParcel2.writeNoException();
            messageParcel2.writeInt(updateConnectStatus ? 1 : 0);
            return true;
        } else if (i != 4) {
            return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
        } else {
            int readInt = messageParcel.readInt();
            ExtraParams extraParams2 = new ExtraParams();
            messageParcel.readSequenceable(extraParams2);
            boolean showDeviceList = showDeviceList(readInt, extraParams2);
            messageParcel2.writeNoException();
            messageParcel2.writeInt(showDeviceList ? 1 : 0);
            return true;
        }
    }
}
