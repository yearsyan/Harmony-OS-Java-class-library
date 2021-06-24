package ohos.aafwk.ability.continuation;

import ohos.rpc.IRemoteBroker;
import ohos.rpc.RemoteException;

public interface IConnectCallback extends IRemoteBroker {
    void connect(String str, String str2) throws RemoteException;

    void disconnect(String str) throws RemoteException;
}
