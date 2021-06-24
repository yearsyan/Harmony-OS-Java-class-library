package ohos.systemrestore.interfaces.impl;

import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteObject;
import ohos.systemrestore.interfaces.ISystemRestoreSystemAbility;
import ohos.systemrestore.utils.SystemRestoreConstant;
import ohos.systemrestore.utils.SystemRestoreStringUtil;

public abstract class SystemRestoreSystemAbilityStub extends RemoteObject implements ISystemRestoreSystemAbility {
    private static final HiLogLabel TAG = new HiLogLabel(3, 218115072, SystemRestoreSystemAbilityStub.class.getSimpleName());
    private static SystemRestoreSystemAbilityProxy abilityProxy;

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public SystemRestoreSystemAbilityStub() {
        super(SystemRestoreConstant.SYSTEMABILITY_DESCRIPTOR);
    }

    public static ISystemRestoreSystemAbility asInterface() {
        SystemRestoreSystemAbilityProxy systemRestoreSystemAbilityProxy;
        SystemRestoreStringUtil.printDebug(TAG, "asInterface input obj is null.");
        synchronized (SystemRestoreSystemAbilityStub.class) {
            if (abilityProxy == null) {
                SystemRestoreStringUtil.printDebug(TAG, "asInterface abilityProxy is null.");
                abilityProxy = new SystemRestoreSystemAbilityProxy();
            }
            systemRestoreSystemAbilityProxy = abilityProxy;
        }
        return systemRestoreSystemAbilityProxy;
    }
}
