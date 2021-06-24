package ohos.nfc;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.sysability.samgr.SysAbilityManager;

public class NfcCommProxy implements IRemoteBroker {
    private static final int ERR_OK = 0;
    private static final HiLogLabel LABEL = new HiLogLabel(3, NfcKitsUtils.NFC_DOMAIN_ID, "NfcCommProxy");
    private int mAbilityId;
    private final Object mLock = new Object();
    private IRemoteObject mRemoteObject;

    static {
        System.loadLibrary("ipc_core.z");
    }

    protected NfcCommProxy(int i) {
        this.mAbilityId = i;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        synchronized (this.mLock) {
            if (this.mRemoteObject != null) {
                return this.mRemoteObject;
            }
            this.mRemoteObject = SysAbilityManager.getSysAbility(this.mAbilityId);
            if (this.mRemoteObject != null) {
                this.mRemoteObject.addDeathRecipient(new NfcDeathRecipient(), 0);
                HiLog.info(LABEL, "getSysAbility %{public}d completed.", Integer.valueOf(this.mAbilityId));
            } else {
                HiLog.error(LABEL, "getSysAbility %{public}d failed.", Integer.valueOf(this.mAbilityId));
            }
            return this.mRemoteObject;
        }
    }

    /* access modifiers changed from: private */
    public class NfcDeathRecipient implements IRemoteObject.DeathRecipient {
        private NfcDeathRecipient() {
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            HiLog.warn(NfcCommProxy.LABEL, "NfcDeathRecipient::onRemoteDied.", new Object[0]);
            synchronized (NfcCommProxy.this.mLock) {
                NfcCommProxy.this.mRemoteObject = null;
            }
        }
    }

    private IRemoteObject getRemote() throws RemoteException {
        IRemoteObject asObject = asObject();
        if (asObject != null) {
            return asObject;
        }
        HiLog.error(LABEL, "Failed to get remote object of nfc sa", new Object[0]);
        throw new RemoteException("get remote failed for nfc sa");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:10|11|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002f, code lost:
        throw new ohos.rpc.RemoteException("connect with nfc sa failed via request");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0030, code lost:
        if (r5 != null) goto L_0x0032;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0032, code lost:
        r5.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0035, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0023, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0025 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.rpc.MessageParcel request(int r4, ohos.rpc.MessageParcel r5) throws ohos.rpc.RemoteException {
        /*
            r3 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r1 = new ohos.rpc.MessageOption
            r2 = 0
            r1.<init>(r2)
            ohos.rpc.IRemoteObject r3 = r3.getRemote()     // Catch:{ RemoteException -> 0x0025 }
            r3.sendRequest(r4, r5, r0, r1)     // Catch:{ RemoteException -> 0x0025 }
            int r3 = r0.readInt()     // Catch:{ RemoteException -> 0x0025 }
            if (r3 != 0) goto L_0x001d
            if (r5 == 0) goto L_0x001c
            r5.reclaim()
        L_0x001c:
            return r0
        L_0x001d:
            ohos.rpc.RemoteException r3 = new ohos.rpc.RemoteException
            r3.<init>()
            throw r3
        L_0x0023:
            r3 = move-exception
            goto L_0x0030
        L_0x0025:
            r0.reclaim()     // Catch:{ all -> 0x0023 }
            ohos.rpc.RemoteException r3 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0023 }
            java.lang.String r4 = "connect with nfc sa failed via request"
            r3.<init>(r4)     // Catch:{ all -> 0x0023 }
            throw r3     // Catch:{ all -> 0x0023 }
        L_0x0030:
            if (r5 == 0) goto L_0x0035
            r5.reclaim()
        L_0x0035:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.nfc.NfcCommProxy.request(int, ohos.rpc.MessageParcel):ohos.rpc.MessageParcel");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0024, code lost:
        if (r5 != null) goto L_0x0026;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0026, code lost:
        r5.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0029, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0017, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:?, code lost:
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0023, code lost:
        throw new ohos.rpc.RemoteException("connect with nfc sa failed via requestWithoutCheck");
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0019 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.rpc.MessageParcel requestWithoutCheck(int r4, ohos.rpc.MessageParcel r5) throws ohos.rpc.RemoteException {
        /*
            r3 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r1 = new ohos.rpc.MessageOption
            r2 = 0
            r1.<init>(r2)
            ohos.rpc.IRemoteObject r3 = r3.getRemote()     // Catch:{ RemoteException -> 0x0019 }
            r3.sendRequest(r4, r5, r0, r1)     // Catch:{ RemoteException -> 0x0019 }
            if (r5 == 0) goto L_0x0016
            r5.reclaim()
        L_0x0016:
            return r0
        L_0x0017:
            r3 = move-exception
            goto L_0x0024
        L_0x0019:
            r0.reclaim()     // Catch:{ all -> 0x0017 }
            ohos.rpc.RemoteException r3 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0017 }
            java.lang.String r4 = "connect with nfc sa failed via requestWithoutCheck"
            r3.<init>(r4)     // Catch:{ all -> 0x0017 }
            throw r3     // Catch:{ all -> 0x0017 }
        L_0x0024:
            if (r5 == 0) goto L_0x0029
            r5.reclaim()
        L_0x0029:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.nfc.NfcCommProxy.requestWithoutCheck(int, ohos.rpc.MessageParcel):ohos.rpc.MessageParcel");
    }
}
