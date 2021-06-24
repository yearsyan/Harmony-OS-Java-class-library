package ohos.systemrestore.adapter;

import java.io.File;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.systemrestore.SystemRestoreException;
import ohos.systemrestore.interfaces.impl.SystemRestoreSystemAbilityStub;
import ohos.systemrestore.utils.SystemRestoreStringUtil;

public class SystemRestoreSystemAbility extends SystemRestoreSystemAbilityStub {
    private static final SystemRestoreManagerAdapter ADAPTER = SystemRestoreManagerAdapter.getInstance();
    private static final File LOG_FILE = new File(SYSTEM_RESTORE_DIR, "log");
    private static final File SYSTEM_RESTORE_DIR = new File(SystemRestoreStringUtil.getSystemRestoreDir());
    private static final HiLogLabel TAG = new HiLogLabel(3, 218115072, SystemRestoreSystemAbility.class.getSimpleName());
    private static volatile SystemRestoreSystemAbility abilityInstance;

    private SystemRestoreSystemAbility() {
    }

    public static SystemRestoreSystemAbility getInstance() {
        if (abilityInstance == null) {
            synchronized (SystemRestoreSystemAbility.class) {
                if (abilityInstance == null) {
                    abilityInstance = new SystemRestoreSystemAbility();
                }
            }
        }
        return abilityInstance;
    }

    @Override // ohos.systemrestore.interfaces.ISystemRestoreSystemAbility
    public boolean rebootRestoreCache(String str) throws SystemRestoreException {
        SystemRestoreStringUtil.printDebug(TAG, "rebootRestoreCache before exec in system ability");
        verifyCommand(str);
        deleteLogFile();
        return ADAPTER.rebootRestoreCache(str);
    }

    @Override // ohos.systemrestore.interfaces.ISystemRestoreSystemAbility
    public boolean passedAuthenticationRestoreCache(Context context) throws SystemRestoreException {
        return ADAPTER.passedAuthenticationRestoreCache(context);
    }

    @Override // ohos.systemrestore.interfaces.ISystemRestoreSystemAbility
    public boolean rebootRestoreUserData(String str) throws SystemRestoreException {
        SystemRestoreStringUtil.printDebug(TAG, "rebootRestoreUserData before exec in system ability");
        verifyCommand(str);
        deleteLogFile();
        return ADAPTER.rebootRestoreUserData(str);
    }

    @Override // ohos.systemrestore.interfaces.ISystemRestoreSystemAbility
    public boolean passedAuthenticationRestoreUserData(Context context) throws SystemRestoreException {
        return ADAPTER.passedAuthenticationRestoreUserData(context);
    }

    @Override // ohos.systemrestore.interfaces.ISystemRestoreSystemAbility
    public void sendRestoreUserDataBroadcast(Context context) throws SystemRestoreException {
        ADAPTER.sendRestoreUserDataBroadcast(context);
    }

    @Override // ohos.systemrestore.interfaces.ISystemRestoreSystemAbility
    public void passedAuthenticationInstallPackage(Context context) throws SystemRestoreException {
        ADAPTER.passedAuthenticationInstallPackage(context);
    }

    @Override // ohos.systemrestore.interfaces.ISystemRestoreSystemAbility
    public void installUpdatePackageCommands(String str) throws SystemRestoreException {
        SystemRestoreStringUtil.printDebug(TAG, "installUpdatePackageCommands before exec in system ability");
        verifyCommand(str);
        deleteLogFile();
        ADAPTER.installUpdateCommands(str);
    }

    private void deleteLogFile() {
        if (!LOG_FILE.delete()) {
            SystemRestoreStringUtil.printDebug(TAG, "deleteLogFile fail!");
        }
    }

    private void verifyCommand(String str) throws SystemRestoreException {
        if (SystemRestoreStringUtil.isEmpty(str)) {
            HiLog.error(TAG, "verifiCommand verification commands is null.", new Object[0]);
            throw new SystemRestoreException("command is null");
        }
    }
}
