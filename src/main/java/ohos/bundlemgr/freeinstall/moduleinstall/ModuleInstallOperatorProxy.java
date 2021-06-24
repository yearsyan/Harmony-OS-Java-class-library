package ohos.bundlemgr.freeinstall.moduleinstall;

import android.content.Context;
import ohos.rpc.IRemoteObject;

public class ModuleInstallOperatorProxy implements IModuleInstallOperator {
    private IModuleInstallOperator moduleInstallOperator;

    public ModuleInstallOperatorProxy(Context context) {
        this.moduleInstallOperator = new ModuleDispatcherInstallOperator(context);
    }

    @Override // ohos.bundlemgr.freeinstall.moduleinstall.IModuleInstallOperator
    public boolean silentInstall(String str, String str2, int i, IRemoteObject iRemoteObject) {
        return this.moduleInstallOperator.silentInstall(str, str2, i, iRemoteObject);
    }

    @Override // ohos.bundlemgr.freeinstall.moduleinstall.IModuleInstallOperator
    public boolean upgradeCheck(String str, String str2, int i, IRemoteObject iRemoteObject) {
        return this.moduleInstallOperator.upgradeCheck(str, str2, i, iRemoteObject);
    }

    @Override // ohos.bundlemgr.freeinstall.moduleinstall.IModuleInstallOperator
    public boolean upgradeInstall(String str, String str2, int i, int i2, IRemoteObject iRemoteObject) {
        return this.moduleInstallOperator.upgradeInstall(str, str2, i, i2, iRemoteObject);
    }

    @Override // ohos.bundlemgr.freeinstall.moduleinstall.IModuleInstallOperator
    public String queryAbility(String str, String str2) {
        return this.moduleInstallOperator.queryAbility(str, str2);
    }
}
