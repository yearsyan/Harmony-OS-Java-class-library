package ohos.bundlemgr.freeinstall.moduleinstall;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.huawei.ohos.interwork.AbilityUtils;
import java.util.concurrent.atomic.AtomicInteger;
import ohos.app.BinderAdapter;
import ohos.appexecfwk.utils.AppLog;
import ohos.bundle.BundleManager;
import ohos.bundle.InstallerCallbackProxy;
import ohos.bundlemgr.freeinstall.moduledispatcher.IModuleDispatcher;
import ohos.bundlemgr.freeinstall.moduledispatcher.IModuleDispatcherReceiver;
import ohos.bundlemgr.freeinstall.moduledispatcher.ModuleDispatcherProxy;
import ohos.bundlemgr.freeinstall.moduledispatcher.ModuleDispatcherReceiverStub;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IPCAdapter;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;

public class ModuleDispatcherInstallOperator implements IModuleInstallOperator {
    private static final HiLogLabel BMS_ADAPTER_LABEL = new HiLogLabel(3, 218108160, "ModuleDispatcherInstallOperator");
    private static final int CONNECT_THRESHOLD = 60000;
    private static final int DISCONNECT_DELAY = 20000;
    private static final int DISCONNECT_FA_DISPATCHER = 2;
    private static final int ERROR_OK = 0;
    private static final String FA_DISPATCHER_CLASS_NAME = "com.huawei.ohos.abilitydispatcher.HapInstallServiceAbility";
    private static final String FA_DISPATCHER_PACKAGE_NAME = "com.huawei.ohos.famanager";
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_DISCONNECT = 0;
    private final Object connectLock = new Object();
    private int connectState = 0;
    private ServiceConnection connection = new ServiceConnection() {
        /* class ohos.bundlemgr.freeinstall.moduleinstall.ModuleDispatcherInstallOperator.AnonymousClass1 */

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IRemoteObject iRemoteObject;
            AppLog.i(ModuleDispatcherInstallOperator.BMS_ADAPTER_LABEL, "onServiceConnected", new Object[0]);
            synchronized (ModuleDispatcherInstallOperator.this.connectLock) {
                if (iBinder instanceof BinderAdapter) {
                    iRemoteObject = ((BinderAdapter) iBinder).getLocalRemoteObject();
                } else {
                    iRemoteObject = IPCAdapter.translateToIRemoteObject(iBinder).get();
                }
                if (iRemoteObject == null) {
                    AppLog.e("onServiceConnected remote is null", new Object[0]);
                    return;
                }
                ModuleDispatcherInstallOperator.this.faDispatcherProxy = new ModuleDispatcherProxy(iRemoteObject);
                ModuleDispatcherInstallOperator.this.connectState = 2;
                ModuleDispatcherInstallOperator.this.connectLock.notifyAll();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            AppLog.i(ModuleDispatcherInstallOperator.BMS_ADAPTER_LABEL, "onServiceDisconnected", new Object[0]);
            synchronized (ModuleDispatcherInstallOperator.this.connectLock) {
                ModuleDispatcherInstallOperator.this.connectState = 0;
                ModuleDispatcherInstallOperator.this.faDispatcherProxy = null;
            }
        }
    };
    private Context context;
    private EventHandler eventHandler;
    private IModuleDispatcher faDispatcherProxy;
    private AtomicInteger taskNumber = new AtomicInteger(0);

    public ModuleDispatcherInstallOperator(Context context2) {
        this.context = context2;
        this.eventHandler = new TimeoutHandler(EventRunner.create(true));
    }

    @Override // ohos.bundlemgr.freeinstall.moduleinstall.IModuleInstallOperator
    public boolean silentInstall(String str, String str2, int i, IRemoteObject iRemoteObject) {
        if (!connectAbility()) {
            AppLog.e(BMS_ADAPTER_LABEL, "silentInstall connectAbility fail", new Object[0]);
            return false;
        }
        try {
            incrementConnectNum();
            FaDispatcherReceiverStubImpl createFaDispatcherReceiverStub = createFaDispatcherReceiverStub(iRemoteObject);
            AppLog.i(BMS_ADAPTER_LABEL, "silentInstall bundleName:%{public}s, abilityName:%{public}s, flags:%{public}d", str, str2, Integer.valueOf(i));
            int silentInstall = this.faDispatcherProxy.silentInstall(0, str, str2, i, createFaDispatcherReceiverStub);
            if (silentInstall == 0) {
                return true;
            }
            AppLog.e(BMS_ADAPTER_LABEL, "faDispatcherProxy silentInstall fail errorCode:%{public}d", Integer.valueOf(silentInstall));
            decrementConnectNum();
            return false;
        } catch (RemoteException e) {
            AppLog.e(BMS_ADAPTER_LABEL, "silentInstall Exception,eMsg:%{public}s", e.getMessage());
        }
    }

    @Override // ohos.bundlemgr.freeinstall.moduleinstall.IModuleInstallOperator
    public boolean upgradeCheck(String str, String str2, int i, IRemoteObject iRemoteObject) {
        if (!connectAbility()) {
            AppLog.e(BMS_ADAPTER_LABEL, "upgradeCheck connectAbility fail", new Object[0]);
            return false;
        }
        try {
            incrementConnectNum();
            FaDispatcherReceiverStubImpl createFaDispatcherReceiverStub = createFaDispatcherReceiverStub(iRemoteObject);
            AppLog.i(BMS_ADAPTER_LABEL, "upgradeCheck bundleName:%{public}s, moduleName:%{public}s", str, str2);
            int upgradeCheck = this.faDispatcherProxy.upgradeCheck(0, str, str2, i, createFaDispatcherReceiverStub);
            if (upgradeCheck == 0) {
                return true;
            }
            AppLog.e(BMS_ADAPTER_LABEL, "faDispatcherProxy upgradeCheck fail errorCode:%{public}d", Integer.valueOf(upgradeCheck));
            decrementConnectNum();
            return false;
        } catch (RemoteException e) {
            AppLog.e(BMS_ADAPTER_LABEL, "upgradeCheck Exception,eMsg:%{public}s", e.getMessage());
        }
    }

    @Override // ohos.bundlemgr.freeinstall.moduleinstall.IModuleInstallOperator
    public boolean upgradeInstall(String str, String str2, int i, int i2, IRemoteObject iRemoteObject) {
        if (!connectAbility()) {
            AppLog.e(BMS_ADAPTER_LABEL, "upgradeInstall connectAbility fail", new Object[0]);
            return false;
        }
        try {
            incrementConnectNum();
            FaDispatcherReceiverStubImpl createFaDispatcherReceiverStub = createFaDispatcherReceiverStub(iRemoteObject);
            AppLog.i(BMS_ADAPTER_LABEL, "upgradeInstall bundleName:%{public}s, moduleName:%{public}s, flags:%{public}d", str, str2, Integer.valueOf(i));
            int upgradeInstall = this.faDispatcherProxy.upgradeInstall(0, str, str2, i, i2, createFaDispatcherReceiverStub);
            if (upgradeInstall == 0) {
                BundleManager instance = BundleManager.getInstance();
                if (instance == null) {
                    AppLog.e(BMS_ADAPTER_LABEL, "upgradeInstall bundleMgr is null", new Object[0]);
                    return false;
                }
                instance.updateModuleUpgradeFlag(str, str2, 0);
                return true;
            }
            AppLog.e(BMS_ADAPTER_LABEL, "faDispatcherProxy upgradeInstall fail errorCode:%{public}d", Integer.valueOf(upgradeInstall));
            decrementConnectNum();
            return false;
        } catch (RemoteException e) {
            AppLog.e(BMS_ADAPTER_LABEL, "upgradeInstall Exception,eMsg:%{public}s", e.getMessage());
        }
    }

    @Override // ohos.bundlemgr.freeinstall.moduleinstall.IModuleInstallOperator
    public String queryAbility(String str, String str2) {
        if (!connectAbility()) {
            AppLog.e(BMS_ADAPTER_LABEL, "queryAbility connectAbility fail", new Object[0]);
            return "";
        }
        try {
            incrementConnectNum();
            AppLog.i(BMS_ADAPTER_LABEL, "queryAbility bundleName:%{public}s, abilityName:%{public}s", str, str2);
            String queryAbility = this.faDispatcherProxy.queryAbility(str, str2);
            AppLog.d(BMS_ADAPTER_LABEL, "queryAbility result:%{public}s", queryAbility);
            return queryAbility;
        } catch (RemoteException e) {
            AppLog.e(BMS_ADAPTER_LABEL, "queryAbility Exception,eMsg:%{public}s", e.getMessage());
            return "";
        } finally {
            decrementConnectNum();
        }
    }

    private void incrementConnectNum() {
        AppLog.d(BMS_ADAPTER_LABEL, "incrementConnectNum", new Object[0]);
        this.taskNumber.incrementAndGet();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void decrementConnectNum() {
        AppLog.d(BMS_ADAPTER_LABEL, "decrementConnectNum", new Object[0]);
        if (this.taskNumber.decrementAndGet() == 0) {
            this.eventHandler.sendEvent(InnerEvent.get(2), 20000);
        }
    }

    private FaDispatcherReceiverStubImpl createFaDispatcherReceiverStub(IRemoteObject iRemoteObject) {
        return new FaDispatcherReceiverStubImpl(new InstallerCallbackProxy(iRemoteObject));
    }

    private boolean connectAbility() {
        synchronized (this.connectLock) {
            this.eventHandler.removeEvent(2);
            long nanoTime = System.nanoTime();
            AppLog.i(BMS_ADAPTER_LABEL, "connectAbility connectState:%{public}d", Integer.valueOf(this.connectState));
            if (this.connectState == 1) {
                connectWait();
                AppLog.i(BMS_ADAPTER_LABEL, "connectAbility await end STATE_CONNECTING", new Object[0]);
            } else if (this.connectState == 0) {
                this.connectState = 1;
                Intent intent = new Intent();
                intent.setClassName(FA_DISPATCHER_PACKAGE_NAME, FA_DISPATCHER_CLASS_NAME);
                boolean connectAbility = AbilityUtils.connectAbility(this.context, intent, this.connection);
                if (!connectAbility) {
                    AppLog.e(BMS_ADAPTER_LABEL, "connectAbility result = %{public}s", Boolean.valueOf(connectAbility));
                } else if (this.connectState != 2) {
                    connectWait();
                    AppLog.i(BMS_ADAPTER_LABEL, "connectAbility await end STATE_DISCONNECT", new Object[0]);
                }
                this.connectLock.notifyAll();
            }
            AppLog.i(BMS_ADAPTER_LABEL, "connectAbility got state, cost: %{public}d", Long.valueOf(System.nanoTime() - nanoTime));
            if (this.connectState == 2) {
                return true;
            }
            this.connectState = 0;
            return false;
        }
    }

    private void connectWait() {
        try {
            this.connectLock.wait(60000);
        } catch (InterruptedException unused) {
            AppLog.i(BMS_ADAPTER_LABEL, "connectAbility InterruptedException", new Object[0]);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void disconnectAbility() {
        synchronized (this.connectLock) {
            AppLog.i(BMS_ADAPTER_LABEL, "disconnectAbility", new Object[0]);
            this.faDispatcherProxy = null;
            this.connectState = 0;
            AbilityUtils.disconnectAbility(this.context, this.connection);
        }
    }

    private class TimeoutHandler extends EventHandler {
        public TimeoutHandler(EventRunner eventRunner) throws IllegalArgumentException {
            super(eventRunner);
        }

        @Override // ohos.eventhandler.EventHandler
        public void processEvent(InnerEvent innerEvent) {
            int i = innerEvent.eventId;
            AppLog.i(ModuleDispatcherInstallOperator.BMS_ADAPTER_LABEL, "processEvent eventType %{public}d", Integer.valueOf(i));
            if (i == 2) {
                ModuleDispatcherInstallOperator.this.disconnectAbility();
            }
        }
    }

    /* access modifiers changed from: private */
    public class FaDispatcherReceiverStubImpl extends ModuleDispatcherReceiverStub {
        private InstallerCallbackProxy installerCallbackProxy;
        private boolean isCallback = false;
        private final Object lock = new Object();
        private final long startTime = System.nanoTime();

        public FaDispatcherReceiverStubImpl(InstallerCallbackProxy installerCallbackProxy2) {
            super(IModuleDispatcherReceiver.DESCRIPTOR);
            this.installerCallbackProxy = installerCallbackProxy2;
        }

        @Override // ohos.bundlemgr.freeinstall.moduledispatcher.IModuleDispatcherReceiver
        public void onFinished(int i, int i2, String str) throws RemoteException {
            long nanoTime = System.nanoTime() - this.startTime;
            AppLog.i(ModuleDispatcherInstallOperator.BMS_ADAPTER_LABEL, "onFinished, result: %{public}d, msg: %{public}s, cost: %{public}d", Integer.valueOf(i2), str, Long.valueOf(nanoTime));
            synchronized (this.lock) {
                if (this.isCallback) {
                    AppLog.e(ModuleDispatcherInstallOperator.BMS_ADAPTER_LABEL, "onFinished has called", new Object[0]);
                    return;
                }
                this.isCallback = true;
                this.installerCallbackProxy.onFinished(i2, str);
                ModuleDispatcherInstallOperator.this.decrementConnectNum();
            }
        }

        @Override // ohos.bundlemgr.freeinstall.moduledispatcher.IModuleDispatcherReceiver
        public void onDownloadProgress(int i, int i2, int i3) throws RemoteException {
            AppLog.d(ModuleDispatcherInstallOperator.BMS_ADAPTER_LABEL, "onDownloadProgress, transactId: %{public}d, fileSize: %{public}d, downloadedSize: %{public}d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
            this.installerCallbackProxy.onDownloadProgress(i2, i3);
        }
    }
}
