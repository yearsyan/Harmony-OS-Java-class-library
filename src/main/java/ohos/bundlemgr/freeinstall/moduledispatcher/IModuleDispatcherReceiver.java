package ohos.bundlemgr.freeinstall.moduledispatcher;

import ohos.rpc.IRemoteBroker;
import ohos.rpc.RemoteException;

public interface IModuleDispatcherReceiver extends IRemoteBroker {
    public static final String DESCRIPTOR = "com.huawei.ohos.abilitydispatcher.openapi.install.IFreeInstallCallback";

    void onDownloadProgress(int i, int i2, int i3) throws RemoteException;

    void onFinished(int i, int i2, String str) throws RemoteException;
}
