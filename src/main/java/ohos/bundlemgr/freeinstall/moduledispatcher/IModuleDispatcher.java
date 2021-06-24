package ohos.bundlemgr.freeinstall.moduledispatcher;

import ohos.rpc.IRemoteBroker;
import ohos.rpc.RemoteException;

public interface IModuleDispatcher extends IRemoteBroker {
    String queryAbility(String str, String str2) throws RemoteException;

    int silentInstall(int i, String str, String str2, int i2, IModuleDispatcherReceiver iModuleDispatcherReceiver) throws RemoteException;

    int upgradeCheck(int i, String str, String str2, int i2, IModuleDispatcherReceiver iModuleDispatcherReceiver) throws RemoteException;

    int upgradeInstall(int i, String str, String str2, int i2, int i3, IModuleDispatcherReceiver iModuleDispatcherReceiver) throws RemoteException;
}
