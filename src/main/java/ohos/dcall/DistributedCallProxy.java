package ohos.dcall;

import android.content.res.Resources;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.sysability.samgr.SysAbilityManager;
import ohos.utils.PacMap;
import ohos.utils.ParcelException;

class DistributedCallProxy implements IDistributedCall {
    private static final String DCALL_SA_DESCRIPTOR = "OHOS.DistributedCall.IDistributedCall";
    private static final int RESULT_PERMISSION_DENY = -2;
    private static final HiLogLabel TAG = new HiLogLabel(3, 218111744, "DistributedCallProxy");
    private static final String TELEPHONY_SA_DESCRIPTOR = "OHOS.Telephony.ITelephony";
    private static final int VAL_ARRAY_LIST = 18;
    private static final int VAL_BOOLEAN = 6;
    private static final int VAL_BOOLEAN_ARRAY = 15;
    private static final int VAL_BYTE = 0;
    private static final int VAL_BYTE_ARRAY = 9;
    private static final int VAL_CHAR = 7;
    private static final int VAL_CHAR_ARRAY = 16;
    private static final int VAL_DOUBLE = 5;
    private static final int VAL_DOUBLE_ARRAY = 14;
    private static final int VAL_FLOAT = 4;
    private static final int VAL_FLOAT_ARRAY = 13;
    private static final int VAL_INTEGER = 2;
    private static final int VAL_INTEGER_ARRAY = 11;
    private static final int VAL_LONG = 3;
    private static final int VAL_LONG_ARRAY = 12;
    private static final int VAL_NULL = -1;
    private static final int VAL_SHORT = 1;
    private static final int VAL_SHORT_ARRAY = 10;
    private static final int VAL_STRING = 8;
    private static final int VAL_STRING_ARRAY = 17;
    private static volatile DistributedCallProxy sInstance = null;
    private IRemoteObject mDistributedCallRemoteService = null;
    private final Object mLock = new Object();
    private IRemoteObject mTelephonyRemoteService = null;

    static {
        try {
            System.loadLibrary("ipc_core.z");
        } catch (UnsatisfiedLinkError unused) {
            HiLog.error(TAG, "Load library libipc_core.z.so failed.", new Object[0]);
        }
    }

    private DistributedCallProxy() {
    }

    public static DistributedCallProxy getInstance() {
        if (sInstance == null) {
            synchronized (DistributedCallProxy.class) {
                if (sInstance == null) {
                    sInstance = new DistributedCallProxy();
                }
            }
        }
        return sInstance;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        synchronized (this.mLock) {
            if (this.mDistributedCallRemoteService != null) {
                return this.mDistributedCallRemoteService;
            }
            this.mDistributedCallRemoteService = SysAbilityManager.getSysAbility(4002);
            if (this.mDistributedCallRemoteService != null) {
                this.mDistributedCallRemoteService.addDeathRecipient(new DistributedCallDeathRecipient(), 0);
            } else {
                HiLog.error(TAG, "getSysAbility(DistributedCallService) failed.", new Object[0]);
            }
            return this.mDistributedCallRemoteService;
        }
    }

    private IRemoteObject getDistributedCallSrvAbility() throws RemoteException {
        return (IRemoteObject) Optional.ofNullable(asObject()).orElseThrow($$Lambda$t9E2a5kBSvCJG3OvOwSmRDhzvos.INSTANCE);
    }

    /* access modifiers changed from: private */
    public class DistributedCallDeathRecipient implements IRemoteObject.DeathRecipient {
        private DistributedCallDeathRecipient() {
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            HiLog.warn(DistributedCallProxy.TAG, "DistributedCallDeathRecipient::onRemoteDied.", new Object[0]);
            synchronized (DistributedCallProxy.this.mLock) {
                DistributedCallProxy.this.mDistributedCallRemoteService = null;
            }
        }
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x002b, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to answerCall", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0037, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003f, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0045, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002d */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int answerCall(int r5, int r6) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002d }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002d }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002d }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x002d }
            r5 = 1
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002d }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002d }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003e
        L_0x002b:
            r4 = move-exception
            goto L_0x003f
        L_0x002d:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x002b }
            java.lang.String r5 = "Failed to answerCall"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x002b }
            ohos.hiviewdfx.HiLog.error(r4, r5, r6)     // Catch:{ all -> 0x002b }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x003e:
            return r4
        L_0x003f:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.answerCall(int, int):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0028, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to disconnect", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0034, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003c, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0042, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002a */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int disconnect(int r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002a }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002a }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x002a }
            r5 = 2
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002a }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002a }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003b
        L_0x0028:
            r4 = move-exception
            goto L_0x003c
        L_0x002a:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0028 }
            java.lang.String r5 = "Failed to disconnect"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0028 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0028 }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x003b:
            return r4
        L_0x003c:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.disconnect(int):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x002b, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to playDtmfTone", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0037, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003f, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0045, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002d */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int startDtmfTone(int r5, char r6) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002d }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002d }
            r0.writeChar(r6)     // Catch:{ RemoteException -> 0x002d }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x002d }
            r5 = 3
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002d }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002d }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003e
        L_0x002b:
            r4 = move-exception
            goto L_0x003f
        L_0x002d:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x002b }
            java.lang.String r5 = "Failed to playDtmfTone"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x002b }
            ohos.hiviewdfx.HiLog.error(r4, r5, r6)     // Catch:{ all -> 0x002b }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x003e:
            return r4
        L_0x003f:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.startDtmfTone(int, char):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0028, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to disconnect", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0034, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003c, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0042, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002a */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int stopDtmfTone(int r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002a }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002a }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x002a }
            r5 = 4
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002a }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002a }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003b
        L_0x0028:
            r4 = move-exception
            goto L_0x003c
        L_0x002a:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0028 }
            java.lang.String r5 = "Failed to disconnect"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0028 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0028 }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x003b:
            return r4
        L_0x003c:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.stopDtmfTone(int):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x002b, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to disconnect", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0037, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003f, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0045, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002d */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int postDialDtmfContinue(int r5, boolean r6) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002d }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002d }
            r0.writeBoolean(r6)     // Catch:{ RemoteException -> 0x002d }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x002d }
            r5 = 5
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002d }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002d }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003e
        L_0x002b:
            r4 = move-exception
            goto L_0x003f
        L_0x002d:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x002b }
            java.lang.String r5 = "Failed to disconnect"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x002b }
            ohos.hiviewdfx.HiLog.error(r4, r5, r6)     // Catch:{ all -> 0x002b }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x003e:
            return r4
        L_0x003f:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.postDialDtmfContinue(int, boolean):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:5|6|7|10) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x002e, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to reject", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x003a, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0042, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0048, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0030 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int reject(int r5, boolean r6, java.lang.String r7) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x0030 }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x0030 }
            r0.writeBoolean(r6)     // Catch:{ RemoteException -> 0x0030 }
            r0.writeString(r7)     // Catch:{ RemoteException -> 0x0030 }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x0030 }
            r5 = 6
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0030 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0030 }
            r1.reclaim()
            r0.reclaim()
            goto L_0x0041
        L_0x002e:
            r4 = move-exception
            goto L_0x0042
        L_0x0030:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x002e }
            java.lang.String r5 = "Failed to reject"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x002e }
            ohos.hiviewdfx.HiLog.error(r4, r5, r6)     // Catch:{ all -> 0x002e }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x0041:
            return r4
        L_0x0042:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.reject(int, boolean, java.lang.String):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x002f, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to distributeCallEvent", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x003b, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0043, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0049, code lost:
        throw r3;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0031 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int distributeCallEvent(int r4, java.lang.String r5, ohos.utils.PacMap r6) {
        /*
            r3 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            java.lang.String r2 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r2)     // Catch:{ RemoteException -> 0x0031 }
            r0.writeInt(r4)     // Catch:{ RemoteException -> 0x0031 }
            r0.writeString(r5)     // Catch:{ RemoteException -> 0x0031 }
            r3.writePacMapParcel(r6, r0)     // Catch:{ RemoteException -> 0x0031 }
            ohos.rpc.IRemoteObject r3 = r3.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x0031 }
            r4 = 13
            ohos.rpc.MessageOption r5 = new ohos.rpc.MessageOption     // Catch:{ RemoteException -> 0x0031 }
            r5.<init>()     // Catch:{ RemoteException -> 0x0031 }
            r3.sendRequest(r4, r0, r1, r5)     // Catch:{ RemoteException -> 0x0031 }
            int r3 = r1.readInt()     // Catch:{ RemoteException -> 0x0031 }
            r1.reclaim()
            r0.reclaim()
            goto L_0x0042
        L_0x002f:
            r3 = move-exception
            goto L_0x0043
        L_0x0031:
            ohos.hiviewdfx.HiLogLabel r3 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x002f }
            java.lang.String r4 = "Failed to distributeCallEvent"
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x002f }
            ohos.hiviewdfx.HiLog.error(r3, r4, r5)     // Catch:{ all -> 0x002f }
            r1.reclaim()
            r0.reclaim()
            r3 = -1
        L_0x0042:
            return r3
        L_0x0043:
            r1.reclaim()
            r0.reclaim()
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.distributeCallEvent(int, java.lang.String, ohos.utils.PacMap):int");
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:1:0x0010 */
    /* JADX DEBUG: Multi-variable search result rejected for r3v1, resolved type: int */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v3, types: [boolean] */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:5|6) */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0026, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to isNewCallAllowed", new java.lang.Object[r3]);
        r3 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0033, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0039, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0028 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isNewCallAllowed() {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            r3 = 0
            java.lang.String r4 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0028 }
            ohos.rpc.IRemoteObject r5 = r5.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x0028 }
            r4 = 7
            r5.sendRequest(r4, r0, r1, r2)     // Catch:{ RemoteException -> 0x0028 }
            boolean r3 = r1.readBoolean()     // Catch:{ RemoteException -> 0x0028 }
        L_0x001f:
            r1.reclaim()
            r0.reclaim()
            goto L_0x0032
        L_0x0026:
            r5 = move-exception
            goto L_0x0033
        L_0x0028:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0026 }
            java.lang.String r2 = "Failed to isNewCallAllowed"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x0026 }
            ohos.hiviewdfx.HiLog.error(r5, r2, r4)     // Catch:{ all -> 0x0026 }
            goto L_0x001f
        L_0x0032:
            return r3
        L_0x0033:
            r1.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.isNewCallAllowed():boolean");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0029, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to setMuted", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0035, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003d, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0043, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002b */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int setMuted(boolean r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002b }
            r0.writeBoolean(r5)     // Catch:{ RemoteException -> 0x002b }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x002b }
            r5 = 8
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002b }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002b }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003c
        L_0x0029:
            r4 = move-exception
            goto L_0x003d
        L_0x002b:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0029 }
            java.lang.String r5 = "Failed to setMuted"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0029 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0029 }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x003c:
            return r4
        L_0x003d:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.setMuted(boolean):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0029, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to setAudioDevice", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0035, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003d, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0043, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002b */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int setAudioDevice(int r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002b }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002b }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x002b }
            r5 = 9
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002b }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002b }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003c
        L_0x0029:
            r4 = move-exception
            goto L_0x003d
        L_0x002b:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0029 }
            java.lang.String r5 = "Failed to setAudioDevice"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0029 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0029 }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x003c:
            return r4
        L_0x003d:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.setAudioDevice(int):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0029, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to hold call", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0035, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003d, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0043, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002b */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int hold(int r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002b }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002b }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x002b }
            r5 = 10
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002b }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002b }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003c
        L_0x0029:
            r4 = move-exception
            goto L_0x003d
        L_0x002b:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0029 }
            java.lang.String r5 = "Failed to hold call"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0029 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0029 }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x003c:
            return r4
        L_0x003d:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.hold(int):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0029, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to unhold call", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0035, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003d, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0043, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002b */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int unhold(int r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002b }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002b }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x002b }
            r5 = 11
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002b }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002b }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003c
        L_0x0029:
            r4 = move-exception
            goto L_0x003d
        L_0x002b:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0029 }
            java.lang.String r5 = "Failed to unhold call"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0029 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0029 }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x003c:
            return r4
        L_0x003d:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.unhold(int):int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0042, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to getPredefinedRejectMessages", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0054, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005a, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0044 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<java.lang.String> getPredefinedRejectMessages(int r7) {
        /*
            r6 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r4 = 0
            java.lang.String r5 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r5)     // Catch:{ RemoteException -> 0x0044 }
            r0.writeInt(r7)     // Catch:{ RemoteException -> 0x0044 }
            ohos.rpc.IRemoteObject r6 = r6.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x0044 }
            r7 = 12
            r6.sendRequest(r7, r0, r1, r2)     // Catch:{ RemoteException -> 0x0044 }
            int r6 = r1.readInt()     // Catch:{ RemoteException -> 0x0044 }
            if (r6 <= 0) goto L_0x003b
            r7 = 4
            if (r6 <= r7) goto L_0x002e
            goto L_0x003b
        L_0x002e:
            r7 = r4
        L_0x002f:
            if (r7 >= r6) goto L_0x004d
            java.lang.String r2 = r1.readString()     // Catch:{ RemoteException -> 0x0044 }
            r3.add(r2)     // Catch:{ RemoteException -> 0x0044 }
            int r7 = r7 + 1
            goto L_0x002f
        L_0x003b:
            r1.reclaim()
            r0.reclaim()
            return r3
        L_0x0042:
            r6 = move-exception
            goto L_0x0054
        L_0x0044:
            ohos.hiviewdfx.HiLogLabel r6 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0042 }
            java.lang.String r7 = "Failed to getPredefinedRejectMessages"
            java.lang.Object[] r2 = new java.lang.Object[r4]     // Catch:{ all -> 0x0042 }
            ohos.hiviewdfx.HiLog.error(r6, r7, r2)     // Catch:{ all -> 0x0042 }
        L_0x004d:
            r1.reclaim()
            r0.reclaim()
            return r3
        L_0x0054:
            r1.reclaim()
            r0.reclaim()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.getPredefinedRejectMessages(int):java.util.List");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x002c, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to joinConference.", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0038, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0040, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0046, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002e */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int joinConference(int r5, int r6) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002e }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x002e }
            r5 = 16
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002e }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002e }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003f
        L_0x002c:
            r4 = move-exception
            goto L_0x0040
        L_0x002e:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x002c }
            java.lang.String r5 = "Failed to joinConference."
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x002c }
            ohos.hiviewdfx.HiLog.error(r4, r5, r6)     // Catch:{ all -> 0x002c }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x003f:
            return r4
        L_0x0040:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.joinConference(int, int):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0029, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to combineConference.", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0035, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003d, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0043, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002b */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int combineConference(int r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002b }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002b }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x002b }
            r5 = 17
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002b }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002b }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003c
        L_0x0029:
            r4 = move-exception
            goto L_0x003d
        L_0x002b:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0029 }
            java.lang.String r5 = "Failed to combineConference."
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0029 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0029 }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x003c:
            return r4
        L_0x003d:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.combineConference(int):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0029, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to separateConference.", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0035, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003d, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0043, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002b */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int separateConference(int r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002b }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002b }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x002b }
            r5 = 18
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002b }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002b }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003c
        L_0x0029:
            r4 = move-exception
            goto L_0x003d
        L_0x002b:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0029 }
            java.lang.String r5 = "Failed to separateConference."
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0029 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0029 }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x003c:
            return r4
        L_0x003d:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.separateConference(int):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0029, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to switchCdmaConference.", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0035, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003d, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0043, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002b */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int switchCdmaConference(int r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002b }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002b }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x002b }
            r5 = 19
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002b }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002b }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003c
        L_0x0029:
            r4 = move-exception
            goto L_0x003d
        L_0x002b:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0029 }
            java.lang.String r5 = "Failed to switchCdmaConference."
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0029 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0029 }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x003c:
            return r4
        L_0x003d:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.switchCdmaConference(int):int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0043, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to getSubCallIdList.", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0055, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005b, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0045 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<java.lang.String> getSubCallIdList(int r7) {
        /*
            r6 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r4 = 0
            java.lang.String r5 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r5)     // Catch:{ RemoteException -> 0x0045 }
            r0.writeInt(r7)     // Catch:{ RemoteException -> 0x0045 }
            ohos.rpc.IRemoteObject r6 = r6.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x0045 }
            r7 = 20
            r6.sendRequest(r7, r0, r1, r2)     // Catch:{ RemoteException -> 0x0045 }
            int r6 = r1.readInt()     // Catch:{ RemoteException -> 0x0045 }
            if (r6 <= 0) goto L_0x003c
            r7 = 32
            if (r6 <= r7) goto L_0x002f
            goto L_0x003c
        L_0x002f:
            r7 = r4
        L_0x0030:
            if (r7 >= r6) goto L_0x004e
            java.lang.String r2 = r1.readString()     // Catch:{ RemoteException -> 0x0045 }
            r3.add(r2)     // Catch:{ RemoteException -> 0x0045 }
            int r7 = r7 + 1
            goto L_0x0030
        L_0x003c:
            r1.reclaim()
            r0.reclaim()
            return r3
        L_0x0043:
            r6 = move-exception
            goto L_0x0055
        L_0x0045:
            ohos.hiviewdfx.HiLogLabel r6 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0043 }
            java.lang.String r7 = "Failed to getSubCallIdList."
            java.lang.Object[] r2 = new java.lang.Object[r4]     // Catch:{ all -> 0x0043 }
            ohos.hiviewdfx.HiLog.error(r6, r7, r2)     // Catch:{ all -> 0x0043 }
        L_0x004e:
            r1.reclaim()
            r0.reclaim()
            return r3
        L_0x0055:
            r1.reclaim()
            r0.reclaim()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.getSubCallIdList(int):java.util.List");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0043, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to getCallIdListForConference.", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0055, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005b, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0045 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<java.lang.String> getCallIdListForConference(int r7) {
        /*
            r6 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r4 = 0
            java.lang.String r5 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r5)     // Catch:{ RemoteException -> 0x0045 }
            r0.writeInt(r7)     // Catch:{ RemoteException -> 0x0045 }
            ohos.rpc.IRemoteObject r6 = r6.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x0045 }
            r7 = 21
            r6.sendRequest(r7, r0, r1, r2)     // Catch:{ RemoteException -> 0x0045 }
            int r6 = r1.readInt()     // Catch:{ RemoteException -> 0x0045 }
            if (r6 <= 0) goto L_0x003c
            r7 = 32
            if (r6 <= r7) goto L_0x002f
            goto L_0x003c
        L_0x002f:
            r7 = r4
        L_0x0030:
            if (r7 >= r6) goto L_0x004e
            java.lang.String r2 = r1.readString()     // Catch:{ RemoteException -> 0x0045 }
            r3.add(r2)     // Catch:{ RemoteException -> 0x0045 }
            int r7 = r7 + 1
            goto L_0x0030
        L_0x003c:
            r1.reclaim()
            r0.reclaim()
            return r3
        L_0x0043:
            r6 = move-exception
            goto L_0x0055
        L_0x0045:
            ohos.hiviewdfx.HiLogLabel r6 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0043 }
            java.lang.String r7 = "Failed to getCallIdListForConference."
            java.lang.Object[] r2 = new java.lang.Object[r4]     // Catch:{ all -> 0x0043 }
            ohos.hiviewdfx.HiLog.error(r6, r7, r2)     // Catch:{ all -> 0x0043 }
        L_0x004e:
            r1.reclaim()
            r0.reclaim()
            return r3
        L_0x0055:
            r1.reclaim()
            r0.reclaim()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.getCallIdListForConference(int):java.util.List");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0029, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to getMainCallId.", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0035, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003d, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0043, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002b */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getMainCallId(int r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002b }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002b }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x002b }
            r5 = 22
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002b }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002b }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003c
        L_0x0029:
            r4 = move-exception
            goto L_0x003d
        L_0x002b:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0029 }
            java.lang.String r5 = "Failed to getMainCallId."
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0029 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0029 }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x003c:
            return r4
        L_0x003d:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.getMainCallId(int):int");
    }

    private IRemoteObject asTelephonyObject() {
        synchronized (this.mLock) {
            if (this.mTelephonyRemoteService != null) {
                return this.mTelephonyRemoteService;
            }
            this.mTelephonyRemoteService = SysAbilityManager.checkSysAbility(4001);
            if (this.mTelephonyRemoteService != null) {
                this.mTelephonyRemoteService.addDeathRecipient(new TelephonyDeathRecipient(), 0);
            } else {
                HiLog.error(TAG, "getSysAbility(TelephonyService) failed.", new Object[0]);
            }
            return this.mTelephonyRemoteService;
        }
    }

    private IRemoteObject getTelephonySrvAbility() throws RemoteException {
        return (IRemoteObject) Optional.ofNullable(asTelephonyObject()).orElseThrow($$Lambda$t9E2a5kBSvCJG3OvOwSmRDhzvos.INSTANCE);
    }

    /* access modifiers changed from: private */
    public class TelephonyDeathRecipient implements IRemoteObject.DeathRecipient {
        private TelephonyDeathRecipient() {
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            HiLog.warn(DistributedCallProxy.TAG, "TelephonyDeathRecipient::onRemoteDied.", new Object[0]);
            synchronized (DistributedCallProxy.this.mLock) {
                DistributedCallProxy.this.mTelephonyRemoteService = null;
            }
        }
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:1:0x0010 */
    /* JADX DEBUG: Multi-variable search result rejected for r3v1, resolved type: int */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v3, types: [boolean] */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0027, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to hasCall", new java.lang.Object[r3]);
        r3 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0034, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x003a, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0029 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasCall() {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            r3 = 0
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0029 }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0029 }
            r4 = 2001(0x7d1, float:2.804E-42)
            r5.sendRequest(r4, r0, r1, r2)     // Catch:{ RemoteException -> 0x0029 }
            boolean r3 = r1.readBoolean()     // Catch:{ RemoteException -> 0x0029 }
        L_0x0020:
            r1.reclaim()
            r0.reclaim()
            goto L_0x0033
        L_0x0027:
            r5 = move-exception
            goto L_0x0034
        L_0x0029:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0027 }
            java.lang.String r2 = "Failed to hasCall"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x0027 }
            ohos.hiviewdfx.HiLog.error(r5, r2, r4)     // Catch:{ all -> 0x0027 }
            goto L_0x0020
        L_0x0033:
            return r3
        L_0x0034:
            r1.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.hasCall():boolean");
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:1:0x0010 */
    /* JADX DEBUG: Multi-variable search result rejected for r3v2, resolved type: int */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v3, types: [boolean] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0029 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean dial(java.lang.String r6, boolean r7) {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            r3 = 0
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0029 }
            r0.writeString(r6)     // Catch:{ RemoteException -> 0x0029 }
            r0.writeBoolean(r7)     // Catch:{ RemoteException -> 0x0029 }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0029 }
            r6 = 2002(0x7d2, float:2.805E-42)
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x0029 }
            boolean r3 = r1.readBoolean()     // Catch:{ RemoteException -> 0x0029 }
            goto L_0x0032
        L_0x0027:
            r5 = move-exception
            goto L_0x0039
        L_0x0029:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0027 }
            java.lang.String r6 = "Failed to dial"
            java.lang.Object[] r7 = new java.lang.Object[r3]     // Catch:{ all -> 0x0027 }
            ohos.hiviewdfx.HiLog.error(r5, r6, r7)     // Catch:{ all -> 0x0027 }
        L_0x0032:
            r1.reclaim()
            r0.reclaim()
            return r3
        L_0x0039:
            r1.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.dial(java.lang.String, boolean):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:5|6) */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0025, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to displayCallScreen", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0033, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0039, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0027 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void displayCallScreen(boolean r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x0027 }
            r0.writeBoolean(r5)     // Catch:{ RemoteException -> 0x0027 }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0027 }
            r5 = 2003(0x7d3, float:2.807E-42)
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0027 }
        L_0x001e:
            r1.reclaim()
            r0.reclaim()
            goto L_0x0032
        L_0x0025:
            r4 = move-exception
            goto L_0x0033
        L_0x0027:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0025 }
            java.lang.String r5 = "Failed to displayCallScreen"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0025 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0025 }
            goto L_0x001e
        L_0x0032:
            return
        L_0x0033:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.displayCallScreen(boolean):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:5|6) */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0022, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to muteRinger", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0030, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0036, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0024 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void muteRinger() {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x0024 }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0024 }
            r3 = 2004(0x7d4, float:2.808E-42)
            r4.sendRequest(r3, r0, r1, r2)     // Catch:{ RemoteException -> 0x0024 }
        L_0x001b:
            r1.reclaim()
            r0.reclaim()
            goto L_0x002f
        L_0x0022:
            r4 = move-exception
            goto L_0x0030
        L_0x0024:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0022 }
            java.lang.String r2 = "Failed to muteRinger"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x0022 }
            ohos.hiviewdfx.HiLog.error(r4, r2, r3)     // Catch:{ all -> 0x0022 }
            goto L_0x001b
        L_0x002f:
            return
        L_0x0030:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.muteRinger():void");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:5|6|7|10) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0026, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to getCallState", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0032, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003a, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0040, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0028 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getCallState() {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x0028 }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0028 }
            r3 = 2005(0x7d5, float:2.81E-42)
            r4.sendRequest(r3, r0, r1, r2)     // Catch:{ RemoteException -> 0x0028 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0028 }
            r1.reclaim()
            r0.reclaim()
            goto L_0x0039
        L_0x0026:
            r4 = move-exception
            goto L_0x003a
        L_0x0028:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0026 }
            java.lang.String r2 = "Failed to getCallState"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x0026 }
            ohos.hiviewdfx.HiLog.error(r4, r2, r3)     // Catch:{ all -> 0x0026 }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x0039:
            return r4
        L_0x003a:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.getCallState():int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:13|14) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0044, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to addObserver", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0057, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005d, code lost:
        throw r3;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0046 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addCallObserver(int r4, ohos.dcall.ICallStateObserver r5, int r6) {
        /*
            r3 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r4)
            if (r0 != 0) goto L_0x000c
            r0 = 2147483647(0x7fffffff, float:NaN)
            if (r4 == r0) goto L_0x000c
            return
        L_0x000c:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            java.lang.String r2 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r2)     // Catch:{ RemoteException -> 0x0046 }
            r0.writeInt(r4)     // Catch:{ RemoteException -> 0x0046 }
            ohos.rpc.IRemoteObject r4 = r5.asObject()     // Catch:{ RemoteException -> 0x0046 }
            r0.writeRemoteObject(r4)     // Catch:{ RemoteException -> 0x0046 }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x0046 }
            ohos.rpc.IRemoteObject r3 = r3.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0046 }
            r4 = 2006(0x7d6, float:2.811E-42)
            ohos.rpc.MessageOption r5 = new ohos.rpc.MessageOption     // Catch:{ RemoteException -> 0x0046 }
            r5.<init>()     // Catch:{ RemoteException -> 0x0046 }
            r3.sendRequest(r4, r0, r1, r5)     // Catch:{ RemoteException -> 0x0046 }
            int r3 = r1.readInt()     // Catch:{ RemoteException -> 0x0046 }
            r4 = -2
            if (r3 == r4) goto L_0x003c
            goto L_0x0050
        L_0x003c:
            java.lang.SecurityException r3 = new java.lang.SecurityException     // Catch:{ RemoteException -> 0x0046 }
            java.lang.String r4 = "Failed to add observer, permission denied!"
            r3.<init>(r4)     // Catch:{ RemoteException -> 0x0046 }
            throw r3     // Catch:{ RemoteException -> 0x0046 }
        L_0x0044:
            r3 = move-exception
            goto L_0x0057
        L_0x0046:
            ohos.hiviewdfx.HiLogLabel r3 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0044 }
            java.lang.String r4 = "Failed to addObserver"
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x0044 }
            ohos.hiviewdfx.HiLog.error(r3, r4, r5)     // Catch:{ all -> 0x0044 }
        L_0x0050:
            r1.reclaim()
            r0.reclaim()
            return
        L_0x0057:
            r1.reclaim()
            r0.reclaim()
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.addCallObserver(int, ohos.dcall.ICallStateObserver, int):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:3|4|5|7|8|9|10) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0040, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0046, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x002d, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002f */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void removeCallObserver(int r5, ohos.dcall.ICallStateObserver r6) {
        /*
            r4 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r5)
            if (r0 != 0) goto L_0x0007
            return
        L_0x0007:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002f }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002f }
            ohos.rpc.IRemoteObject r5 = r6.asObject()     // Catch:{ RemoteException -> 0x002f }
            r0.writeRemoteObject(r5)     // Catch:{ RemoteException -> 0x002f }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002f }
            r5 = 2007(0x7d7, float:2.812E-42)
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002f }
            goto L_0x0039
        L_0x002d:
            r4 = move-exception
            goto L_0x0040
        L_0x002f:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x002d }
            java.lang.String r5 = "Failed to removeObserver"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x002d }
            ohos.hiviewdfx.HiLog.error(r4, r5, r6)     // Catch:{ all -> 0x002d }
        L_0x0039:
            r1.reclaim()
            r0.reclaim()
            return
        L_0x0040:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.removeCallObserver(int, ohos.dcall.ICallStateObserver):void");
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:1:0x0010 */
    /* JADX DEBUG: Multi-variable search result rejected for r3v1, resolved type: int */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v3, types: [boolean] */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0027, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to check video calling enabled", new java.lang.Object[r3]);
        r3 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0034, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x003a, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0029 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isVideoCallingEnabled() {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            r3 = 0
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0029 }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0029 }
            r4 = 2009(0x7d9, float:2.815E-42)
            r5.sendRequest(r4, r0, r1, r2)     // Catch:{ RemoteException -> 0x0029 }
            boolean r3 = r1.readBoolean()     // Catch:{ RemoteException -> 0x0029 }
        L_0x0020:
            r1.reclaim()
            r0.reclaim()
            goto L_0x0033
        L_0x0027:
            r5 = move-exception
            goto L_0x0034
        L_0x0029:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0027 }
            java.lang.String r2 = "Failed to check video calling enabled"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x0027 }
            ohos.hiviewdfx.HiLog.error(r5, r2, r4)     // Catch:{ all -> 0x0027 }
            goto L_0x0020
        L_0x0033:
            return r3
        L_0x0034:
            r1.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.isVideoCallingEnabled():boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:5|6) */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0025, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to inputDialerSpecialCode", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0033, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0039, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0027 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void inputDialerSpecialCode(java.lang.String r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x0027 }
            r0.writeString(r5)     // Catch:{ RemoteException -> 0x0027 }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0027 }
            r5 = 2010(0x7da, float:2.817E-42)
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0027 }
        L_0x001e:
            r1.reclaim()
            r0.reclaim()
            goto L_0x0032
        L_0x0025:
            r4 = move-exception
            goto L_0x0033
        L_0x0027:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0025 }
            java.lang.String r5 = "Failed to inputDialerSpecialCode"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0025 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0025 }
            goto L_0x001e
        L_0x0032:
            return
        L_0x0033:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.inputDialerSpecialCode(java.lang.String):void");
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:1:0x0010 */
    /* JADX DEBUG: Multi-variable search result rejected for r3v1, resolved type: int */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v3, types: [boolean] */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0027, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to isHacEnabled", new java.lang.Object[r3]);
        r3 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0034, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x003a, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0029 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isHacEnabled() {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            r3 = 0
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0029 }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0029 }
            r4 = 2011(0x7db, float:2.818E-42)
            r5.sendRequest(r4, r0, r1, r2)     // Catch:{ RemoteException -> 0x0029 }
            boolean r3 = r1.readBoolean()     // Catch:{ RemoteException -> 0x0029 }
        L_0x0020:
            r1.reclaim()
            r0.reclaim()
            goto L_0x0033
        L_0x0027:
            r5 = move-exception
            goto L_0x0034
        L_0x0029:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0027 }
            java.lang.String r2 = "Failed to isHacEnabled"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x0027 }
            ohos.hiviewdfx.HiLog.error(r5, r2, r4)     // Catch:{ all -> 0x0027 }
            goto L_0x0020
        L_0x0033:
            return r3
        L_0x0034:
            r1.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.isHacEnabled():boolean");
    }

    @Override // ohos.dcall.IDistributedCall
    public boolean hasVoiceCapability() {
        Resources system = Resources.getSystem();
        if (system != null) {
            return system.getBoolean(17891576);
        }
        return true;
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:8|9|10|13) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x003f, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0047, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004d, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0033, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to dial with extras", new java.lang.Object[0]);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0035 */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int dial(ohos.utils.net.Uri r5, ohos.utils.PacMap r6) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x0035 }
            r3 = 0
            if (r5 == 0) goto L_0x0019
            java.lang.String r3 = r5.toString()     // Catch:{ RemoteException -> 0x0035 }
        L_0x0019:
            r0.writeString(r3)     // Catch:{ RemoteException -> 0x0035 }
            r4.writePacMapParcel(r6, r0)     // Catch:{ RemoteException -> 0x0035 }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x0035 }
            r5 = 14
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0035 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0035 }
            r1.reclaim()
            r0.reclaim()
            goto L_0x0046
        L_0x0033:
            r4 = move-exception
            goto L_0x0047
        L_0x0035:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0033 }
            java.lang.String r5 = "Failed to dial with extras"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x0033 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r6)     // Catch:{ all -> 0x0033 }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x0046:
            return r4
        L_0x0047:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.dial(ohos.utils.net.Uri, ohos.utils.PacMap):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0029, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.dcall.DistributedCallProxy.TAG, "Failed to initDialEnv", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0035, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003d, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0043, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002b */
    @Override // ohos.dcall.IDistributedCall
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int initDialEnv(ohos.utils.PacMap r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.DistributedCall.IDistributedCall"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002b }
            r4.writePacMapParcel(r5, r0)     // Catch:{ RemoteException -> 0x002b }
            ohos.rpc.IRemoteObject r4 = r4.getDistributedCallSrvAbility()     // Catch:{ RemoteException -> 0x002b }
            r5 = 15
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002b }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002b }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003c
        L_0x0029:
            r4 = move-exception
            goto L_0x003d
        L_0x002b:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.dcall.DistributedCallProxy.TAG     // Catch:{ all -> 0x0029 }
            java.lang.String r5 = "Failed to initDialEnv"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0029 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0029 }
            r1.reclaim()
            r0.reclaim()
            r4 = -1
        L_0x003c:
            return r4
        L_0x003d:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.DistributedCallProxy.initDialEnv(ohos.utils.PacMap):int");
    }

    private void writePacMapParcel(PacMap pacMap, MessageParcel messageParcel) {
        if (pacMap == null || pacMap.isEmpty()) {
            HiLog.error(TAG, "writePacMapParcel: pacMap is null or empty", new Object[0]);
            messageParcel.writeInt(0);
            return;
        }
        Map<String, Object> all = pacMap.getAll();
        if (all == null) {
            HiLog.error(TAG, "writePacMapParcel: map is null.", new Object[0]);
            messageParcel.writeInt(0);
            return;
        }
        messageParcel.writeInt(all.size());
        for (String str : all.keySet()) {
            if (!TextUtils.isEmpty(str)) {
                messageParcel.writeString(str);
                writeValueIntoParcel(all.get(str), messageParcel);
            }
        }
    }

    private void writeValueIntoParcel(Object obj, MessageParcel messageParcel) {
        if (obj == null) {
            messageParcel.writeInt(-1);
        } else if (obj instanceof Byte) {
            messageParcel.writeInt(0);
            messageParcel.writeByte(((Byte) obj).byteValue());
        } else if (obj instanceof Short) {
            messageParcel.writeInt(1);
            messageParcel.writeShort(((Short) obj).shortValue());
        } else if (obj instanceof Integer) {
            messageParcel.writeInt(2);
            messageParcel.writeInt(((Integer) obj).intValue());
        } else if (obj instanceof Long) {
            messageParcel.writeInt(3);
            messageParcel.writeLong(((Long) obj).longValue());
        } else if (obj instanceof Float) {
            messageParcel.writeInt(4);
            messageParcel.writeFloat(((Float) obj).floatValue());
        } else if (obj instanceof Double) {
            messageParcel.writeInt(5);
            messageParcel.writeDouble(((Double) obj).doubleValue());
        } else if (obj instanceof Boolean) {
            messageParcel.writeInt(6);
            messageParcel.writeBoolean(((Boolean) obj).booleanValue());
        } else if (obj instanceof Character) {
            messageParcel.writeInt(7);
            messageParcel.writeChar(((Character) obj).charValue());
        } else if (obj instanceof String) {
            messageParcel.writeInt(8);
            messageParcel.writeString((String) obj);
        } else {
            writeArrayValueIntoParcel(obj, messageParcel);
        }
    }

    private void writeArrayValueIntoParcel(Object obj, MessageParcel messageParcel) {
        if (obj instanceof byte[]) {
            messageParcel.writeInt(9);
            writeByteArray((byte[]) obj, messageParcel);
        } else if (obj instanceof short[]) {
            messageParcel.writeInt(10);
            writeShortArray((short[]) obj, messageParcel);
        } else if (obj instanceof int[]) {
            messageParcel.writeInt(11);
            writeIntArray((int[]) obj, messageParcel);
        } else if (obj instanceof long[]) {
            messageParcel.writeInt(12);
            writeLongArray((long[]) obj, messageParcel);
        } else if (obj instanceof float[]) {
            messageParcel.writeInt(13);
            writeFloatArray((float[]) obj, messageParcel);
        } else if (obj instanceof double[]) {
            messageParcel.writeInt(14);
            writeDoubleArray((double[]) obj, messageParcel);
        } else if (obj instanceof boolean[]) {
            messageParcel.writeInt(15);
            writeBooleanArray((boolean[]) obj, messageParcel);
        } else if (obj instanceof char[]) {
            messageParcel.writeInt(16);
            writeCharArray((char[]) obj, messageParcel);
        } else if (obj instanceof String[]) {
            messageParcel.writeInt(17);
            writeStringArray((String[]) obj, messageParcel);
        } else if (obj instanceof ArrayList) {
            messageParcel.writeInt(18);
            writeArrayList((ArrayList) obj, messageParcel);
        } else {
            throw new ParcelException("Unsupported type in PacMap.");
        }
    }

    private void writeByteArray(byte[] bArr, MessageParcel messageParcel) {
        if (bArr == null || bArr.length <= 0) {
            messageParcel.writeInt(0);
            return;
        }
        int length = bArr.length;
        messageParcel.writeInt(length);
        for (byte b : bArr) {
            messageParcel.writeInt(0);
            messageParcel.writeByte(b);
        }
    }

    private void writeShortArray(short[] sArr, MessageParcel messageParcel) {
        if (sArr == null || sArr.length <= 0) {
            messageParcel.writeInt(0);
            return;
        }
        int length = sArr.length;
        messageParcel.writeInt(length);
        for (short s : sArr) {
            messageParcel.writeInt(1);
            messageParcel.writeShort(s);
        }
    }

    private void writeIntArray(int[] iArr, MessageParcel messageParcel) {
        if (iArr == null || iArr.length <= 0) {
            messageParcel.writeInt(0);
            return;
        }
        int length = iArr.length;
        messageParcel.writeInt(length);
        for (int i : iArr) {
            messageParcel.writeInt(2);
            messageParcel.writeInt(i);
        }
    }

    private void writeLongArray(long[] jArr, MessageParcel messageParcel) {
        if (jArr == null || jArr.length <= 0) {
            messageParcel.writeInt(0);
            return;
        }
        int length = jArr.length;
        messageParcel.writeInt(length);
        for (long j : jArr) {
            messageParcel.writeInt(3);
            messageParcel.writeLong(j);
        }
    }

    private void writeFloatArray(float[] fArr, MessageParcel messageParcel) {
        if (fArr == null || fArr.length <= 0) {
            messageParcel.writeInt(0);
            return;
        }
        int length = fArr.length;
        messageParcel.writeInt(length);
        for (float f : fArr) {
            messageParcel.writeInt(4);
            messageParcel.writeFloat(f);
        }
    }

    private void writeDoubleArray(double[] dArr, MessageParcel messageParcel) {
        if (dArr == null || dArr.length <= 0) {
            messageParcel.writeInt(0);
            return;
        }
        int length = dArr.length;
        messageParcel.writeInt(length);
        for (double d : dArr) {
            messageParcel.writeInt(5);
            messageParcel.writeDouble(d);
        }
    }

    private void writeBooleanArray(boolean[] zArr, MessageParcel messageParcel) {
        if (zArr == null || zArr.length <= 0) {
            messageParcel.writeInt(0);
            return;
        }
        int length = zArr.length;
        messageParcel.writeInt(length);
        for (boolean z : zArr) {
            messageParcel.writeInt(6);
            messageParcel.writeBoolean(z);
        }
    }

    private void writeCharArray(char[] cArr, MessageParcel messageParcel) {
        if (cArr == null || cArr.length <= 0) {
            messageParcel.writeInt(0);
            return;
        }
        int length = cArr.length;
        messageParcel.writeInt(length);
        for (char c : cArr) {
            messageParcel.writeInt(7);
            messageParcel.writeChar(c);
        }
    }

    private void writeStringArray(String[] strArr, MessageParcel messageParcel) {
        if (strArr == null || strArr.length <= 0) {
            messageParcel.writeInt(0);
            return;
        }
        int length = strArr.length;
        messageParcel.writeInt(length);
        for (String str : strArr) {
            messageParcel.writeInt(8);
            messageParcel.writeString(str);
        }
    }

    private void writeArrayList(ArrayList<?> arrayList, MessageParcel messageParcel) {
        if (arrayList == null) {
            messageParcel.writeInt(0);
            return;
        }
        messageParcel.writeInt(arrayList.size());
        Iterator<?> it = arrayList.iterator();
        while (it.hasNext()) {
            writeValueIntoParcel(it.next(), messageParcel);
        }
    }
}
