package ohos.aafwk.ability.continuation;

import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;

public interface IRemoteRegisterService extends IRemoteBroker {
    int register(String str, IRemoteObject iRemoteObject, ExtraParams extraParams, IConnectCallback iConnectCallback) throws RemoteException;

    boolean showDeviceList(int i, ExtraParams extraParams) throws RemoteException;

    boolean unregister(int i) throws RemoteException;

    boolean updateConnectStatus(int i, String str, int i2) throws RemoteException;
}
