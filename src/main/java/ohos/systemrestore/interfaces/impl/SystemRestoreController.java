package ohos.systemrestore.interfaces.impl;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.systemrestore.SystemRestoreException;
import ohos.systemrestore.interfaces.ISystemRestoreController;
import ohos.systemrestore.interfaces.ISystemRestoreSystemAbility;
import ohos.systemrestore.utils.SystemRestoreStringUtil;

public class SystemRestoreController implements ISystemRestoreController {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final HiLogLabel TAG = new HiLogLabel(3, 218115072, SystemRestoreController.class.getSimpleName());
    private static ISystemRestoreController instance;
    private ISystemRestoreSystemAbility sysRestoreSystemAbility = SystemRestoreSystemAbilityStub.asInterface();

    private SystemRestoreController() {
    }

    public static ISystemRestoreController getInstance() {
        ISystemRestoreController iSystemRestoreController;
        synchronized (SystemRestoreController.class) {
            if (instance == null) {
                instance = new SystemRestoreController();
            }
            iSystemRestoreController = instance;
        }
        return iSystemRestoreController;
    }

    @Override // ohos.systemrestore.interfaces.ISystemRestoreController
    public boolean rebootRestoreCache(Context context) throws SystemRestoreException {
        abilityCanUse(context);
        this.sysRestoreSystemAbility.passedAuthenticationRestoreCache(context);
        SystemRestoreStringUtil.printDebug(TAG, "rebootRestoreCache passed authentication and then get commands.");
        Optional<String> rebootRestoreCacheCommands = getRebootRestoreCacheCommands(context);
        if (!rebootRestoreCacheCommands.isPresent()) {
            return false;
        }
        return this.sysRestoreSystemAbility.rebootRestoreCache(rebootRestoreCacheCommands.get());
    }

    @Override // ohos.systemrestore.interfaces.ISystemRestoreController
    public boolean rebootRestoreUserData(Context context) throws SystemRestoreException {
        abilityCanUse(context);
        this.sysRestoreSystemAbility.passedAuthenticationRestoreUserData(context);
        SystemRestoreStringUtil.printDebug(TAG, "rebootRestoreUserData passed authentication and then get commands.");
        Optional<String> rebootRestoreUserDataCommands = getRebootRestoreUserDataCommands(context);
        if (!rebootRestoreUserDataCommands.isPresent()) {
            return false;
        }
        this.sysRestoreSystemAbility.sendRestoreUserDataBroadcast(context);
        return this.sysRestoreSystemAbility.rebootRestoreUserData(rebootRestoreUserDataCommands.get());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0020, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0025, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0026, code lost:
        r2.addSuppressed(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0029, code lost:
        throw r3;
     */
    @Override // ohos.systemrestore.interfaces.ISystemRestoreController
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void verifyUpdatePackage(java.io.File r2, ohos.systemrestore.ISystemRestoreProgressListener r3, java.io.File r4, boolean r5) throws ohos.systemrestore.SystemRestoreException {
        /*
            r1 = this;
            if (r2 == 0) goto L_0x0048
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch:{ FileNotFoundException -> 0x003a, IOException -> 0x002a }
            java.lang.String r0 = "r"
            r1.<init>(r2, r0)     // Catch:{ FileNotFoundException -> 0x003a, IOException -> 0x002a }
            ohos.systemrestore.bean.SystemRestoreZipFilePropBean r2 = new ohos.systemrestore.bean.SystemRestoreZipFilePropBean     // Catch:{ all -> 0x001e }
            r2.<init>(r1)     // Catch:{ all -> 0x001e }
            r2.getEOCDProperty()     // Catch:{ all -> 0x001e }
            r2.setEndOfCentralDirectory()     // Catch:{ all -> 0x001e }
            r2.verifiedCertsFile(r4)     // Catch:{ all -> 0x001e }
            r2.readAndVerifyFile(r3, r5)     // Catch:{ all -> 0x001e }
            r1.close()
            return
        L_0x001e:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x0020 }
        L_0x0020:
            r3 = move-exception
            r1.close()     // Catch:{ all -> 0x0025 }
            goto L_0x0029
        L_0x0025:
            r1 = move-exception
            r2.addSuppressed(r1)
        L_0x0029:
            throw r3
        L_0x002a:
            r1 = move-exception
            ohos.hiviewdfx.HiLogLabel r2 = ohos.systemrestore.interfaces.impl.SystemRestoreController.TAG
            ohos.systemrestore.utils.SystemRestoreStringUtil.printException(r2, r1)
            ohos.systemrestore.SystemRestoreException r2 = new ohos.systemrestore.SystemRestoreException
            java.lang.String r1 = r1.getMessage()
            r2.<init>(r1)
            throw r2
        L_0x003a:
            r1 = move-exception
            ohos.hiviewdfx.HiLogLabel r2 = ohos.systemrestore.interfaces.impl.SystemRestoreController.TAG
            ohos.systemrestore.utils.SystemRestoreStringUtil.printException(r2, r1)
            ohos.systemrestore.SystemRestoreException r1 = new ohos.systemrestore.SystemRestoreException
            java.lang.String r2 = "The upgrade file to be verified not found."
            r1.<init>(r2)
            throw r1
        L_0x0048:
            ohos.hiviewdfx.HiLogLabel r1 = ohos.systemrestore.interfaces.impl.SystemRestoreController.TAG
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.String r3 = "verifyUpdatePackage update file is null."
            ohos.hiviewdfx.HiLog.error(r1, r3, r2)
            ohos.systemrestore.SystemRestoreException r1 = new ohos.systemrestore.SystemRestoreException
            java.lang.String r2 = "update file is null."
            r1.<init>(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.systemrestore.interfaces.impl.SystemRestoreController.verifyUpdatePackage(java.io.File, ohos.systemrestore.ISystemRestoreProgressListener, java.io.File, boolean):void");
    }

    @Override // ohos.systemrestore.interfaces.ISystemRestoreController
    public void rebootAndInstallUpdatePackage(Context context, File file) throws SystemRestoreException {
        if (file != null) {
            abilityCanUse(context);
            this.sysRestoreSystemAbility.passedAuthenticationInstallPackage(context);
            SystemRestoreStringUtil.printDebug(TAG, "installUpdatePackage passed authentication and then get commands.");
            Optional<String> installUpdatePackageCommands = getInstallUpdatePackageCommands(file);
            if (installUpdatePackageCommands.isPresent()) {
                this.sysRestoreSystemAbility.installUpdatePackageCommands(installUpdatePackageCommands.get());
                return;
            }
            return;
        }
        HiLog.error(TAG, "update package is null.", new Object[0]);
        throw new SystemRestoreException("update package is null.");
    }

    private void abilityCanUse(Context context) throws SystemRestoreException {
        if (context != null) {
            ISystemRestoreSystemAbility iSystemRestoreSystemAbility = this.sysRestoreSystemAbility;
            if (iSystemRestoreSystemAbility != null) {
                SystemRestoreStringUtil.printDebug(TAG, "before exec in controller", iSystemRestoreSystemAbility.toString());
            } else {
                HiLog.error(TAG, "get mIMSAbility is null.", new Object[0]);
                throw new SystemRestoreException("get ability is null.");
            }
        } else {
            HiLog.error(TAG, "get context is null.", new Object[0]);
            throw new SystemRestoreException("get context is null.");
        }
    }

    private Optional<String> getRebootRestoreCacheCommands(Context context) throws SystemRestoreException {
        return getRebootRestoreCommands(context, "wipe_cache");
    }

    private Optional<String> getRebootRestoreUserDataCommands(Context context) throws SystemRestoreException {
        return getRebootRestoreCommands(context, "wipe_data");
    }

    private Optional<String> getRebootRestoreCommands(Context context, String str) throws SystemRestoreException {
        if (SystemRestoreStringUtil.isEmpty(str)) {
            return Optional.empty();
        }
        SystemRestoreStringUtil.printDebug(TAG, "getRebootRestoreCommands wipeType begin", str);
        if (context.getApplicationInfo() == null) {
            HiLog.error(TAG, "get applicationInfo is null.", new Object[0]);
            throw new SystemRestoreException("get applicationInfo is null.");
        } else if (!SystemRestoreStringUtil.isEmpty(context.getApplicationInfo().getName())) {
            SystemRestoreStringUtil.printDebug(TAG, "merge string before in controller");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("--reason=");
            stringBuffer.append(context.getApplicationInfo().getName());
            stringBuffer.append(LINE_SEPARATOR);
            stringBuffer.append("--");
            stringBuffer.append(str);
            stringBuffer.append(LINE_SEPARATOR);
            stringBuffer.append("--locale=");
            stringBuffer.append(Locale.getDefault().toLanguageTag());
            stringBuffer.append(LINE_SEPARATOR);
            SystemRestoreStringUtil.printDebug(TAG, "rebootRestoreCache merge string in controller", stringBuffer.toString().replace(LINE_SEPARATOR, "  "));
            return Optional.of(stringBuffer.toString());
        } else {
            HiLog.error(TAG, "get application bundleName is null.", new Object[0]);
            throw new SystemRestoreException("get application bundleName is null.");
        }
    }

    private Optional<String> getInstallUpdatePackageCommands(File file) throws SystemRestoreException {
        if (file != null) {
            SystemRestoreStringUtil.printDebug(TAG, "getInstallUpdatePackageCommands begin.");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("--update_package=");
            try {
                stringBuffer.append(file.getCanonicalPath());
                stringBuffer.append(LINE_SEPARATOR);
                stringBuffer.append("--locale=");
                stringBuffer.append(Locale.getDefault().toLanguageTag());
                stringBuffer.append(LINE_SEPARATOR);
                stringBuffer.append("--security");
                stringBuffer.append(LINE_SEPARATOR);
                SystemRestoreStringUtil.printDebug(TAG, "getInstallUpdatePackageCommands merge string in controller", stringBuffer.toString().replace(LINE_SEPARATOR, "  "));
                return Optional.of(stringBuffer.toString());
            } catch (IOException e) {
                SystemRestoreStringUtil.printException(TAG, e);
                throw new SystemRestoreException("update file io exception");
            }
        } else {
            HiLog.error(TAG, "update file is null.", new Object[0]);
            throw new SystemRestoreException("update file is null");
        }
    }
}
