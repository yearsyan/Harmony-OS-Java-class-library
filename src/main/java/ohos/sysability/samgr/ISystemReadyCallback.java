package ohos.sysability.samgr;

import ohos.rpc.IRemoteBroker;
import ohos.rpc.RemoteException;

public interface ISystemReadyCallback extends IRemoteBroker {
    void onSystemReadyNotify() throws RemoteException;
}
