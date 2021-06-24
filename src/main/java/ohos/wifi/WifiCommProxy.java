package ohos.wifi;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.sysability.samgr.SysAbilityManager;

public class WifiCommProxy implements IRemoteBroker {
    private static final int ERR_OK = 0;
    private static final HiLogLabel LABEL = new HiLogLabel(3, InnerUtils.LOG_ID_WIFI, "WifiCommProxy");
    private int mAbilityId;
    private final Object mLock = new Object();
    private IRemoteObject mRemoteAbility;

    static {
        try {
            System.loadLibrary("ipc_core.z");
        } catch (UnsatisfiedLinkError unused) {
            HiLog.error(LABEL, "load ipc_core.z failed!", new Object[0]);
        }
    }

    protected WifiCommProxy(int i) {
        this.mAbilityId = i;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        synchronized (this.mLock) {
            if (this.mRemoteAbility != null) {
                return this.mRemoteAbility;
            }
            this.mRemoteAbility = SysAbilityManager.getSysAbility(this.mAbilityId);
            if (this.mRemoteAbility != null) {
                this.mRemoteAbility.addDeathRecipient(new WifiDeathRecipient(), 0);
            } else {
                HiLog.error(LABEL, "getSysAbility %{public}d failed.", Integer.valueOf(this.mAbilityId));
            }
            return this.mRemoteAbility;
        }
    }

    /* access modifiers changed from: private */
    public class WifiDeathRecipient implements IRemoteObject.DeathRecipient {
        private WifiDeathRecipient() {
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            HiLog.warn(WifiCommProxy.LABEL, "WifiDeathRecipient::onRemoteDied.", new Object[0]);
            synchronized (WifiCommProxy.this.mLock) {
                WifiCommProxy.this.mRemoteAbility = null;
            }
        }
    }

    private IRemoteObject getRemote() throws RemoteException {
        IRemoteObject asObject = asObject();
        if (asObject != null) {
            return asObject;
        }
        HiLog.error(LABEL, "Failed to get remote object", new Object[0]);
        throw new RemoteException();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:14|15|16) */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0032, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003c, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003d, code lost:
        if (r5 != null) goto L_0x003f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003f, code lost:
        r5.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0042, code lost:
        throw r3;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0034 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.rpc.MessageParcel request(int r4, ohos.rpc.MessageParcel r5) throws ohos.rpc.RemoteException {
        /*
            r3 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.create()
            ohos.rpc.MessageOption r1 = new ohos.rpc.MessageOption
            r2 = 0
            r1.<init>(r2)
            ohos.rpc.IRemoteObject r3 = r3.getRemote()     // Catch:{ RemoteException -> 0x0034 }
            r3.sendRequest(r4, r5, r0, r1)     // Catch:{ RemoteException -> 0x0034 }
            int r3 = r0.readInt()     // Catch:{ RemoteException -> 0x0034 }
            r4 = 73465857(0x4610001, float:2.6448625E-36)
            if (r3 == r4) goto L_0x002a
            if (r3 != 0) goto L_0x0022
            if (r5 == 0) goto L_0x0021
            r5.reclaim()
        L_0x0021:
            return r0
        L_0x0022:
            ohos.rpc.RemoteException r3 = new ohos.rpc.RemoteException
            java.lang.String r4 = "ReplyError"
            r3.<init>(r4)
            throw r3
        L_0x002a:
            java.lang.SecurityException r3 = new java.lang.SecurityException
            java.lang.String r4 = "Permission denied"
            r3.<init>(r4)
            throw r3
        L_0x0032:
            r3 = move-exception
            goto L_0x003d
        L_0x0034:
            r0.reclaim()     // Catch:{ all -> 0x0032 }
            ohos.rpc.RemoteException r3 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0032 }
            r3.<init>()     // Catch:{ all -> 0x0032 }
            throw r3     // Catch:{ all -> 0x0032 }
        L_0x003d:
            if (r5 == 0) goto L_0x0042
            r5.reclaim()
        L_0x0042:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.wifi.WifiCommProxy.request(int, ohos.rpc.MessageParcel):ohos.rpc.MessageParcel");
    }
}
