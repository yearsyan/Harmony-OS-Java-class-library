package ohos.bundlemgr.freeinstall.moduleinstall;

import ohos.rpc.IRemoteObject;

public interface IModuleInstallOperator {
    String queryAbility(String str, String str2);

    boolean silentInstall(String str, String str2, int i, IRemoteObject iRemoteObject);

    boolean upgradeCheck(String str, String str2, int i, IRemoteObject iRemoteObject);

    boolean upgradeInstall(String str, String str2, int i, int i2, IRemoteObject iRemoteObject);
}
