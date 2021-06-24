package ohos.abilityshell.support;

import android.app.Activity;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.UserHandle;
import com.huawei.android.app.ActivityManagerEx;
import com.huawei.android.content.ContextEx;
import com.huawei.android.os.UserHandleEx;
import java.lang.Thread;
import java.util.List;
import java.util.Optional;
import ohos.abilityshell.AbilityShellData;
import ohos.abilityshell.DistributedImpl;
import ohos.abilityshell.IDistributedManager;
import ohos.abilityshell.utils.AbilityShellConverterUtils;
import ohos.abilityshell.utils.IntentConverter;
import ohos.appexecfwk.utils.AppLog;
import ohos.bundle.AbilityInfo;
import ohos.bundle.InstallerCallback;
import ohos.bundle.ShellInfo;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class AbilityUtilsImpl implements IAbilityUtils {
    private static final IDistributedManager DISTRIBUTED_IMPL = new DistributedImpl();
    private static final int FLAG_MODULE_UPGRADE_INSTALL_WITH_CONFIG_WINDOWS = 2;
    private static final int FLAG_NEED_MODULE_UPGRADE = 1;
    private static final int FREE_INSTALL_ERROR_CODE_FA_DISPATCHER_FAILED = 5;
    private static final String INTERFACE_DESCRIPTOR = "OHOS.AppExecFwk.IStartAbilityCallback";
    private static final int INVALID_REQUEST_CODE = -1;
    private static final int MAX_REQUEST_CODE = 65535;
    private static final int MIN_REQUEST_CODE = 0;
    private static final String PARAM_KEY_INSTALL_ON_DEMAND = "ohos.extra.param.key.INSTALL_ON_DEMAND";
    private static final String PARAM_KEY_INSTALL_WITH_BACKGROUND = "ohos.extra.param.key.INSTALL_WITH_BACKGROUND";
    private static final int REMOTE_CALLBACK_CODE = 1;
    private static final HiLogLabel SUPPORT_LABEL = new HiLogLabel(3, 218108160, "AZSupport");

    private static boolean isFlagExists(int i, int i2) {
        return (i2 & i) == i;
    }

    private boolean isRequestCodeValid(int i) {
        return i >= 0 && i <= 65535;
    }

    @Override // ohos.abilityshell.support.IAbilityUtils
    public void startAbility(Context context, Intent intent) {
        startAbilityForResult(context, intent, false, -1, AbilityInfo.AbilityType.UNKNOWN);
    }

    @Override // ohos.abilityshell.support.IAbilityUtils
    public void startForegroundAbility(Context context, Intent intent) {
        startAbilityForResult(context, intent, true, -1, AbilityInfo.AbilityType.SERVICE);
    }

    @Override // ohos.abilityshell.support.IAbilityUtils
    public void startAbilityForResult(Context context, Intent intent, int i) {
        startAbilityForResult(context, intent, false, i, AbilityInfo.AbilityType.PAGE);
    }

    @Override // ohos.abilityshell.support.IAbilityUtils
    public boolean freeInstallWithCallback(Context context, ohos.aafwk.content.Intent intent, IRemoteObject iRemoteObject) {
        if (context == null || intent == null || iRemoteObject == null) {
            AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::freeInstallWithCallback with invalid param", new Object[0]);
            return false;
        } else if (intent.getElement() == null || intent.getElement().getAbilityName() == null || intent.getElement().getBundleName() == null) {
            AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::freeInstallWithCallback with invalid intent param", new Object[0]);
            return false;
        } else if (!isFlagExists(2048, intent.getFlags())) {
            AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::freeInstallWithCallback with flag invalid", new Object[0]);
            return false;
        } else {
            AppLog.d(SUPPORT_LABEL, "AbilityUtilsImpl::freeInstallWithCallback start", new Object[0]);
            intent.addFlags(268435456);
            startFreeInstallAbility(context, intent, false, -1, iRemoteObject);
            return true;
        }
    }

    private void startAbilityForResult(Context context, Intent intent, boolean z, int i, AbilityInfo.AbilityType abilityType) {
        AppLog.d(SUPPORT_LABEL, "AbilityUtilsImpl::startAbility", new Object[0]);
        if (context == null || intent == null) {
            AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::startAbility intent is null!", new Object[0]);
            throw new IllegalArgumentException("context or intent is null, can't start ability");
        }
        Optional<ohos.aafwk.content.Intent> createZidaneIntent = IntentConverter.createZidaneIntent(intent, null);
        if (!createZidaneIntent.isPresent()) {
            AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::startAbility createZidaneIntent failed", new Object[0]);
        } else if (createZidaneIntent.get().getBooleanParam(PARAM_KEY_INSTALL_ON_DEMAND, false)) {
            AppLog.d(SUPPORT_LABEL, "AbilityUtilsImpl::startAbility with free install", new Object[0]);
            startFreeInstallAbility(context, createZidaneIntent.get(), z, i, null);
        } else {
            AbilityShellData selectAbility = selectAbility(createZidaneIntent.get());
            if (selectAbility == null || !selectAbility.getLocal() || selectAbility.getShellInfo() == null || selectAbility.getAbilityInfo() == null) {
                AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::startAbility selectAbility failed", new Object[0]);
                throw new SecurityException("ability can not be found, can't start ability");
            }
            checkAbilityType(abilityType, selectAbility.getAbilityInfo().getType());
            startLocalAbility(context, selectAbility, createZidaneIntent.get(), z, i);
        }
    }

    private void startFreeInstallAbility(final Context context, final ohos.aafwk.content.Intent intent, final boolean z, final int i, final IRemoteObject iRemoteObject) {
        intent.addFlags(2048);
        if (intent.getBooleanParam(PARAM_KEY_INSTALL_WITH_BACKGROUND, false)) {
            intent.addFlags(Integer.MIN_VALUE);
        }
        Thread thread = new Thread(new Runnable() {
            /* class ohos.abilityshell.support.AbilityUtilsImpl.AnonymousClass1 */

            public void run() {
                AbilityUtilsImpl.this.freeInstallByFaDispatcher(intent, context, z, i, iRemoteObject);
            }
        });
        thread.setUncaughtExceptionHandler(new ExceptionHandler());
        thread.start();
    }

    /* access modifiers changed from: private */
    public static class ExceptionHandler implements Thread.UncaughtExceptionHandler {
        private ExceptionHandler() {
        }

        public void uncaughtException(Thread thread, Throwable th) {
            String str = "<Unknown>";
            String name = thread != null ? thread.getName() : str;
            if (th != null) {
                str = th.getMessage();
            }
            AppLog.e(AbilityUtilsImpl.SUPPORT_LABEL, "Uncaught exception in %{public}s : %{public}s", name, str);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void freeInstallByFaDispatcher(ohos.aafwk.content.Intent intent, Context context, boolean z, int i, IRemoteObject iRemoteObject) {
        AppLog.d(SUPPORT_LABEL, "begin startFreeInstallAbility by fa dispatcher", new Object[0]);
        int queryLocalAbilitySize = queryLocalAbilitySize(intent);
        AppLog.d(SUPPORT_LABEL, "startFreeInstallAbility info size: %{public}d", Integer.valueOf(queryLocalAbilitySize));
        if (queryLocalAbilitySize > 1) {
            AppLog.e(SUPPORT_LABEL, "startFreeInstallAbility selectAbility failed, more than one ability match", new Object[0]);
            dealRemoteCallback(iRemoteObject, 5);
        } else if (queryLocalAbilitySize == 1) {
            upgradeCheckAndInstallPageAbility(context, intent, z, i, iRemoteObject);
        } else {
            silentInstallPageAbility(context, intent, z, i, iRemoteObject);
        }
    }

    private void silentInstallPageAbility(final Context context, final ohos.aafwk.content.Intent intent, final boolean z, final int i, final IRemoteObject iRemoteObject) {
        if (!silentInstall(intent, new InstallerCallback() {
            /* class ohos.abilityshell.support.AbilityUtilsImpl.AnonymousClass2 */

            @Override // ohos.bundle.IInstallerCallback, ohos.bundle.InstallerCallback
            public void onFinished(int i, String str) {
                if (i != 0) {
                    AppLog.e(AbilityUtilsImpl.SUPPORT_LABEL, "silentInstall onFinished, resultCode: %{public}d, msg: %{public}s", Integer.valueOf(i), str);
                    AbilityUtilsImpl.this.dealRemoteCallback(iRemoteObject, i);
                    return;
                }
                AbilityUtilsImpl abilityUtilsImpl = AbilityUtilsImpl.this;
                abilityUtilsImpl.startLocalAbility(context, abilityUtilsImpl.getAbilityShellDataLocal(intent), intent, z, i);
                AbilityUtilsImpl.this.dealRemoteCallback(iRemoteObject, i);
            }
        })) {
            AppLog.e(SUPPORT_LABEL, "silentInstallPageAbility silentInstall failed", new Object[0]);
            dealRemoteCallback(iRemoteObject, 5);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void dealRemoteCallback(IRemoteObject iRemoteObject, int i) {
        if (iRemoteObject != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            MessageOption messageOption = new MessageOption();
            if (!obtain.writeInterfaceToken(INTERFACE_DESCRIPTOR)) {
                AppLog.w("AbilityUtilsImpl::dealRemoteCallback writeInterfaceToken failed", new Object[0]);
            } else if (!obtain.writeInt(i)) {
                AppLog.w("AbilityUtilsImpl::dealRemoteCallback writeInt failed", new Object[0]);
            } else {
                try {
                    if (!iRemoteObject.sendRequest(1, obtain, obtain2, messageOption)) {
                        AppLog.w("AbilityUtilsImpl::dealRemoteCallback sendRequest failed", new Object[0]);
                        obtain.reclaim();
                        obtain2.reclaim();
                        return;
                    }
                } catch (RemoteException e) {
                    AppLog.w("AbilityUtilsImpl::dealRemoteCallback sendRequest failed %{public}s", e.getMessage());
                } catch (Throwable th) {
                    obtain.reclaim();
                    obtain2.reclaim();
                    throw th;
                }
                obtain.reclaim();
                obtain2.reclaim();
            }
        }
    }

    private void upgradeCheckAndInstallPageAbility(final Context context, final ohos.aafwk.content.Intent intent, final boolean z, final int i, final IRemoteObject iRemoteObject) {
        int moduleUpgradeFlag = getModuleUpgradeFlag(intent.getElement().getBundleName(), intent.getElement().getAbilityName());
        AppLog.i(SUPPORT_LABEL, "getModuleUpgradeFlag upgradeFlag: %{public}d ", Integer.valueOf(moduleUpgradeFlag));
        if (moduleUpgradeFlag == 1 || moduleUpgradeFlag == 2) {
            AnonymousClass3 r11 = new InstallerCallback() {
                /* class ohos.abilityshell.support.AbilityUtilsImpl.AnonymousClass3 */

                @Override // ohos.bundle.IInstallerCallback, ohos.bundle.InstallerCallback
                public void onFinished(int i, String str) {
                    if (i != 0) {
                        AppLog.e(AbilityUtilsImpl.SUPPORT_LABEL, "upgradeInstall onFinished, resultCode: %{public}d, msg: %{public}s", Integer.valueOf(i), str);
                    }
                    AbilityUtilsImpl abilityUtilsImpl = AbilityUtilsImpl.this;
                    abilityUtilsImpl.startLocalAbility(context, abilityUtilsImpl.getAbilityShellDataLocal(intent), intent, z, i);
                    AbilityUtilsImpl.this.dealRemoteCallback(iRemoteObject, 0);
                }
            };
            if (!upgradeInstall(intent, moduleUpgradeFlag, r11)) {
                AppLog.e(SUPPORT_LABEL, "upgradeCheckAndInstallPageAbility upgradeInstall fail", new Object[0]);
                r11.onFinished(5, "");
                return;
            }
            return;
        }
        startLocalAbility(context, getAbilityShellDataLocal(intent), intent, z, i);
        upgradeCheck(intent, iRemoteObject);
    }

    private void upgradeCheck(ohos.aafwk.content.Intent intent, final IRemoteObject iRemoteObject) {
        AnonymousClass4 r0 = new InstallerCallback() {
            /* class ohos.abilityshell.support.AbilityUtilsImpl.AnonymousClass4 */

            @Override // ohos.bundle.IInstallerCallback, ohos.bundle.InstallerCallback
            public void onFinished(int i, String str) {
                AbilityUtilsImpl.this.dealRemoteCallback(iRemoteObject, 0);
                if (i != 0) {
                    AppLog.e(AbilityUtilsImpl.SUPPORT_LABEL, "upgradeCheck onFinished, resultCode: %{public}d, msg: %{public}s", Integer.valueOf(i), str);
                }
            }
        };
        if (!upgradeCheck(intent, (InstallerCallback) r0)) {
            AppLog.e(SUPPORT_LABEL, "upgradeCheckAndInstallPageAbility upgradeCheck fail", new Object[0]);
            r0.onFinished(5, "");
        }
    }

    private List<AbilityInfo> queryLocalAbility(ohos.aafwk.content.Intent intent) {
        return AbilityUtilsHelper.queryAbilityByIntent(intent);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int queryLocalAbilitySize(ohos.aafwk.content.Intent intent) {
        List<AbilityInfo> queryAbilityByIntent = AbilityUtilsHelper.queryAbilityByIntent(intent);
        if (queryAbilityByIntent == null) {
            return 0;
        }
        return queryAbilityByIntent.size();
    }

    private boolean silentInstall(ohos.aafwk.content.Intent intent, InstallerCallback installerCallback) {
        return AbilityUtilsHelper.silentInstall(intent, installerCallback);
    }

    private int getModuleUpgradeFlag(String str, String str2) {
        return AbilityUtilsHelper.getModuleUpgradeFlag(str, str2);
    }

    private boolean upgradeCheck(ohos.aafwk.content.Intent intent, InstallerCallback installerCallback) {
        return AbilityUtilsHelper.upgradeCheck(intent, installerCallback);
    }

    private boolean upgradeInstall(ohos.aafwk.content.Intent intent, int i, InstallerCallback installerCallback) {
        return AbilityUtilsHelper.upgradeInstall(intent, i, installerCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private AbilityShellData getAbilityShellDataLocal(ohos.aafwk.content.Intent intent) {
        if (isFlagExists(32, intent.getFlags())) {
            return selectFormAbility(intent, ShellInfo.ShellType.SERVICE);
        }
        intent.getElement().getBundleName();
        List<AbilityInfo> queryAbilityByIntent = AbilityUtilsHelper.queryAbilityByIntent(intent);
        if (queryAbilityByIntent == null || queryAbilityByIntent.size() != 1) {
            AppLog.e("getAbilityShellDataLocal failed, invalid abilityInfos", new Object[0]);
            return null;
        }
        return new AbilityShellData(true, queryAbilityByIntent.get(0), AbilityShellConverterUtils.convertToShellInfo(queryAbilityByIntent.get(0)));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private ShellInfo getAbilityShellInfoLocal(ohos.aafwk.content.Intent intent) {
        AbilityShellData abilityShellDataLocal = getAbilityShellDataLocal(intent);
        if (abilityShellDataLocal != null && abilityShellDataLocal.getShellInfo() != null) {
            return abilityShellDataLocal.getShellInfo();
        }
        AppLog.e("getAbilityShellInfoLocal failed, invalid abilityInfos", new Object[0]);
        return null;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void startLocalAbility(Context context, AbilityShellData abilityShellData, ohos.aafwk.content.Intent intent, boolean z, int i) {
        if (abilityShellData != null) {
            ShellInfo shellInfo = abilityShellData.getShellInfo();
            AbilityInfo abilityInfo = abilityShellData.getAbilityInfo();
            Optional<Intent> createAndroidIntent = IntentConverter.createAndroidIntent(intent, shellInfo);
            if (!createAndroidIntent.isPresent()) {
                AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::startLocalAbility createAndroidIntent", new Object[0]);
                return;
            }
            handleForwardFlag(intent, createAndroidIntent.get());
            if (context instanceof Service) {
                createAndroidIntent.get().addFlags(268435456);
            }
            if (abilityInfo.getType() == AbilityInfo.AbilityType.PAGE) {
                if (!isRequestCodeValid(i)) {
                    try {
                        context.startActivity(createAndroidIntent.get());
                    } catch (ActivityNotFoundException unused) {
                        throw new SecurityException("ability can not be found, can't start ability");
                    }
                } else if (context instanceof Activity) {
                    try {
                        ((Activity) context).startActivityForResult(createAndroidIntent.get(), i);
                    } catch (ActivityNotFoundException unused2) {
                        throw new SecurityException("ability can not be found, can't start ability");
                    }
                } else {
                    AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::startLocalAbility only Activity support", new Object[0]);
                }
            } else if (abilityInfo.getType() != AbilityInfo.AbilityType.SERVICE) {
                AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::startLocalAbility not page or service ability", new Object[0]);
            } else if (z) {
                try {
                    context.startForegroundService(createAndroidIntent.get());
                } catch (SecurityException unused3) {
                    throw new SecurityException("ability can not be found, can't start ability");
                } catch (IllegalStateException unused4) {
                    throw new IllegalStateException("caller is wrong state, can't start ability");
                }
            } else {
                context.startService(createAndroidIntent.get());
            }
        }
    }

    private void checkAbilityType(AbilityInfo.AbilityType abilityType, AbilityInfo.AbilityType abilityType2) {
        if (abilityType != AbilityInfo.AbilityType.UNKNOWN && abilityType != abilityType2) {
            throw new IllegalStateException("request ability type is wrong, start ability failed");
        }
    }

    private boolean stopLocalAbility(Context context, AbilityShellData abilityShellData, ohos.aafwk.content.Intent intent) {
        AppLog.d(SUPPORT_LABEL, "AbilityUtilsImpl::stopLocalAbility called", new Object[0]);
        ShellInfo shellInfo = abilityShellData.getShellInfo();
        if (shellInfo == null) {
            AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::stopLocalAbility shellInfo is null", new Object[0]);
            return false;
        } else if (shellInfo.getType() == ShellInfo.ShellType.SERVICE) {
            Optional<Intent> createAndroidIntent = IntentConverter.createAndroidIntent(intent, shellInfo);
            if (!createAndroidIntent.isPresent()) {
                AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::stopLocalAbility createAndroidIntent failed", new Object[0]);
                return false;
            }
            try {
                return context.stopService(createAndroidIntent.get());
            } catch (SecurityException unused) {
                AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::stopLocalAbility ability not found", new Object[0]);
                throw new SecurityException("ability can not be found, can't stop ability");
            } catch (IllegalStateException unused2) {
                AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::stopLocalAbility caller is wrong state.", new Object[0]);
                throw new IllegalStateException("caller is wrong state, can't stop ability");
            }
        } else {
            AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::stopLocalAbility ShellType not SERVICE", new Object[0]);
            throw new IllegalStateException("request ability is not a service, stop ability failed");
        }
    }

    private void handleForwardFlag(ohos.aafwk.content.Intent intent, Intent intent2) {
        if ((intent.getFlags() & 4) != 0) {
            AppLog.d(SUPPORT_LABEL, "AbilityUtilsImpl::handleForwardFlag have FLAG_ABILITY_FORWARD_RESULT", new Object[0]);
            intent2.setFlags(intent2.getFlags() | 33554432);
        }
    }

    @Override // ohos.abilityshell.support.IAbilityUtils
    public boolean stopAbility(Context context, Intent intent) {
        AppLog.d(SUPPORT_LABEL, "AbilityUtilsImpl::stopAbility", new Object[0]);
        if (context == null || intent == null) {
            AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::stopAbility intent is null!", new Object[0]);
            throw new IllegalArgumentException("context or intent is null, can't stop ability");
        }
        Optional<ohos.aafwk.content.Intent> createZidaneIntent = IntentConverter.createZidaneIntent(intent, null);
        if (!createZidaneIntent.isPresent()) {
            AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::stopAbility createZidaneIntent failed", new Object[0]);
            return false;
        }
        AbilityShellData selectAbility = selectAbility(createZidaneIntent.get());
        if (selectAbility != null && selectAbility.getLocal()) {
            return stopLocalAbility(context, selectAbility, createZidaneIntent.get());
        }
        AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::stopAbility selectAbility failed", new Object[0]);
        return false;
    }

    @Override // ohos.abilityshell.support.IAbilityUtils
    public int connectAbility(Context context, Intent intent, ServiceConnection serviceConnection) {
        AbilityShellData abilityShellData;
        AppLog.d(SUPPORT_LABEL, "AbilityUtilsImpl::connectAbility", new Object[0]);
        if (context == null || intent == null) {
            AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::connectAbility intent is null!", new Object[0]);
            throw new IllegalArgumentException("context or intent is null, can't connect ability");
        }
        Optional<ohos.aafwk.content.Intent> createZidaneIntent = IntentConverter.createZidaneIntent(intent, null);
        if (!createZidaneIntent.isPresent()) {
            AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::connectAbility createZidaneIntent failed", new Object[0]);
            return -1;
        } else if (createZidaneIntent.get().getBooleanParam(PARAM_KEY_INSTALL_ON_DEMAND, false)) {
            AppLog.d(SUPPORT_LABEL, "AbilityUtilsImpl::connectAbility with free install", new Object[0]);
            if (!connectFreeInstallAbility(context, createZidaneIntent.get(), serviceConnection)) {
                return -1;
            }
            return 0;
        } else {
            if (isFlagExists(32, createZidaneIntent.get().getFlags())) {
                abilityShellData = selectFormAbility(createZidaneIntent.get(), ShellInfo.ShellType.SERVICE);
            } else {
                abilityShellData = selectAbility(createZidaneIntent.get());
            }
            if (abilityShellData == null) {
                AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::connectAbility selectAbility failed", new Object[0]);
                return -1;
            }
            ShellInfo shellInfo = abilityShellData.getShellInfo();
            if (shellInfo == null || shellInfo.getType() != ShellInfo.ShellType.SERVICE) {
                AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::connectLocalAbility ShellType not SERVICE", new Object[0]);
                throw new IllegalStateException("request ability is not a service, connect ability failed");
            } else if (abilityShellData.getLocal()) {
                return connectLocalAbility(context, shellInfo, createZidaneIntent.get(), serviceConnection);
            } else {
                AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::unsupport connect remote ability", new Object[0]);
                return -1;
            }
        }
    }

    private boolean connectFreeInstallAbility(final Context context, final ohos.aafwk.content.Intent intent, final ServiceConnection serviceConnection) {
        intent.addFlags(2048);
        if (intent.getBooleanParam(PARAM_KEY_INSTALL_WITH_BACKGROUND, false)) {
            intent.addFlags(Integer.MIN_VALUE);
        }
        Thread thread = new Thread(new Runnable() {
            /* class ohos.abilityshell.support.AbilityUtilsImpl.AnonymousClass5 */

            public void run() {
                AppLog.d(AbilityUtilsImpl.SUPPORT_LABEL, "begin connectFreeInstallAbility by fa dispatcher", new Object[0]);
                int queryLocalAbilitySize = AbilityUtilsImpl.this.queryLocalAbilitySize(intent);
                AppLog.d(AbilityUtilsImpl.SUPPORT_LABEL, "connectFreeInstallAbility info size: %{public}d", Integer.valueOf(queryLocalAbilitySize));
                if (queryLocalAbilitySize > 1) {
                    AppLog.e(AbilityUtilsImpl.SUPPORT_LABEL, "connectFreeInstallAbility, more than one ability match", new Object[0]);
                } else if (queryLocalAbilitySize == 1) {
                    AbilityUtilsImpl abilityUtilsImpl = AbilityUtilsImpl.this;
                    Context context = context;
                    ohos.aafwk.content.Intent intent = intent;
                    abilityUtilsImpl.upgradeCheckAndInstallServiceAbility(context, intent, serviceConnection, abilityUtilsImpl.getAbilityShellInfoLocal(intent));
                } else {
                    AbilityUtilsImpl.this.silentInstallServiceAbility(context, intent, serviceConnection);
                }
            }
        });
        thread.setUncaughtExceptionHandler(new ExceptionHandler());
        thread.start();
        return true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void upgradeCheckAndInstallServiceAbility(Context context, ohos.aafwk.content.Intent intent, ServiceConnection serviceConnection, ShellInfo shellInfo) {
        int moduleUpgradeFlag = getModuleUpgradeFlag(intent.getElement().getBundleName(), intent.getElement().getAbilityName());
        AppLog.i(SUPPORT_LABEL, "getModuleUpgradeFlag upgradeFlag: %{public}d ", Integer.valueOf(moduleUpgradeFlag));
        if (moduleUpgradeFlag == 1 || moduleUpgradeFlag == 2) {
            upgradeInstallServiceAbility(context, intent, serviceConnection, moduleUpgradeFlag);
            return;
        }
        connectLocalAbility(context, shellInfo, intent, serviceConnection);
        upgradeCheck(intent, (InstallerCallback) null);
    }

    private void upgradeInstallServiceAbility(final Context context, final ohos.aafwk.content.Intent intent, final ServiceConnection serviceConnection, int i) {
        AnonymousClass6 r0 = new InstallerCallback() {
            /* class ohos.abilityshell.support.AbilityUtilsImpl.AnonymousClass6 */

            @Override // ohos.bundle.IInstallerCallback, ohos.bundle.InstallerCallback
            public void onFinished(int i, String str) {
                if (i != 0) {
                    AppLog.e(AbilityUtilsImpl.SUPPORT_LABEL, "upgradeInstall onFinished, resultCode: %{public}d, msg: %{public}s", Integer.valueOf(i), str);
                }
                if (!AbilityUtilsImpl.this.connectInnerAbility(context, intent, serviceConnection)) {
                    AppLog.e(AbilityUtilsImpl.SUPPORT_LABEL, "upgradeInstall onFinished connectInnerAbility failed!", new Object[0]);
                }
            }
        };
        if (!upgradeInstall(intent, i, r0)) {
            AppLog.e(SUPPORT_LABEL, "upgradeCheckAndInstallPageAbility upgradeInstall fail", new Object[0]);
            r0.onFinished(5, "");
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void silentInstallServiceAbility(final Context context, final ohos.aafwk.content.Intent intent, final ServiceConnection serviceConnection) {
        if (!silentInstall(intent, new InstallerCallback() {
            /* class ohos.abilityshell.support.AbilityUtilsImpl.AnonymousClass7 */

            @Override // ohos.bundle.IInstallerCallback, ohos.bundle.InstallerCallback
            public void onFinished(int i, String str) {
                if (i != 0) {
                    AppLog.e(AbilityUtilsImpl.SUPPORT_LABEL, "silentInstall onFinished, resultCode: %{public}d, msg: %{public}s", Integer.valueOf(i), str);
                } else if (!AbilityUtilsImpl.this.connectInnerAbility(context, intent, serviceConnection)) {
                    AppLog.e(AbilityUtilsImpl.SUPPORT_LABEL, "silentInstall onFinished connectInnerAbility failed!", new Object[0]);
                }
            }
        })) {
            AppLog.e(SUPPORT_LABEL, "silentInstallServiceAbility silentInstall failed", new Object[0]);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean connectInnerAbility(Context context, ohos.aafwk.content.Intent intent, ServiceConnection serviceConnection) {
        ShellInfo abilityShellInfoLocal = getAbilityShellInfoLocal(intent);
        if (abilityShellInfoLocal == null) {
            AppLog.e(SUPPORT_LABEL, "AbilityProxy::connectAbility selectAbility failed", new Object[0]);
            throw new IllegalArgumentException("abilility can't be found, can't connect ability");
        } else if (abilityShellInfoLocal.getType() != ShellInfo.ShellType.SERVICE) {
            AppLog.e(SUPPORT_LABEL, "AbilityProxy::connectLocalAbility not SERVICE", new Object[0]);
            throw new IllegalStateException("request ability is not a service, connect ability failed");
        } else if (connectLocalAbility(context, abilityShellInfoLocal, intent, serviceConnection) != 0) {
            return true;
        } else {
            return false;
        }
    }

    private int connectLocalAbility(Context context, ShellInfo shellInfo, ohos.aafwk.content.Intent intent, ServiceConnection serviceConnection) {
        AppLog.d(SUPPORT_LABEL, "AbilityUtilsImpl::connectLocalAbility called", new Object[0]);
        Optional<Intent> createAndroidIntent = IntentConverter.createAndroidIntent(intent, shellInfo);
        if (!createAndroidIntent.isPresent()) {
            AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::connectLocalAbility createAndroidIntent failed", new Object[0]);
            return -1;
        }
        try {
            int currentUser = ActivityManagerEx.getCurrentUser();
            AppLog.d(SUPPORT_LABEL, "currentUser is %{public}d", Integer.valueOf(currentUser));
            UserHandle userHandle = UserHandleEx.getUserHandle(currentUser);
            if (currentUser != 0) {
                AppLog.d(SUPPORT_LABEL, "bindServiceAsUser", new Object[0]);
                if (ContextEx.bindServiceAsUser(context, createAndroidIntent.get(), serviceConnection, 1, userHandle)) {
                    return 0;
                }
                return -1;
            } else if (context.bindService(createAndroidIntent.get(), serviceConnection, 1)) {
                return 0;
            } else {
                return -1;
            }
        } catch (SecurityException e) {
            AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::connectLocalAbility with exception: %{public}s", e.toString());
            throw new SecurityException("ability can not be found, can't connect ability");
        }
    }

    @Override // ohos.abilityshell.support.IAbilityUtils
    public void disconnectAbility(Context context, ServiceConnection serviceConnection) {
        AppLog.d(SUPPORT_LABEL, "AbilityUtilsImpl::disconnectAbility", new Object[0]);
        if (context != null) {
            context.unbindService(serviceConnection);
        } else {
            AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::disconnectAbility context is null!", new Object[0]);
            throw new IllegalArgumentException("context is null, can't disconnect ability");
        }
    }

    private AbilityShellData selectAbility(ohos.aafwk.content.Intent intent) {
        try {
            return DISTRIBUTED_IMPL.selectAbility(intent);
        } catch (RemoteException e) {
            AppLog.e(SUPPORT_LABEL, "AbilityUtilsImpl::selectAbility RemoteException: %{public}s", e.getMessage());
            return null;
        }
    }

    private AbilityShellData selectFormAbility(ohos.aafwk.content.Intent intent, ShellInfo.ShellType shellType) {
        List<AbilityInfo> queryAbilityByIntent = AbilityUtilsHelper.queryAbilityByIntent(intent);
        if (queryAbilityByIntent == null || queryAbilityByIntent.isEmpty()) {
            AppLog.e("AbilityUtilsImpl::selectFormAbility failed", new Object[0]);
            return null;
        }
        AbilityInfo abilityInfo = queryAbilityByIntent.get(0);
        ShellInfo convertToFormShellInfo = AbilityShellConverterUtils.convertToFormShellInfo(abilityInfo, shellType);
        if (convertToFormShellInfo != null) {
            return new AbilityShellData(true, abilityInfo, convertToFormShellInfo);
        }
        AppLog.e("AbilityUtilsImpl::selectFormAbility failed", new Object[0]);
        return null;
    }
}
