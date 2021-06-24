package ohos.abilityshell;

import ohos.rpc.IRemoteBroker;
import ohos.rpc.RemoteException;

public interface IRemoteFreeInstallCallback extends IRemoteBroker {
    void onResult(int i) throws RemoteException;
}
