package ohos.telephony;

import java.util.Optional;
import ohos.ai.engine.pluginlabel.PluginLabelConstants;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.sysability.samgr.SysAbilityManager;
import ohos.system.Parameters;

/* access modifiers changed from: package-private */
public class TelephonyProxy implements ITelephony {
    private static final int RESULT_PERMISSION_DENY = -2;
    private static final HiLogLabel TAG = new HiLogLabel(3, 218111744, "TelephonyProxy");
    private static final String TELEPHONY_SA_DESCRIPTOR = "OHOS.Telephony.ITelephony";
    private static volatile TelephonyProxy instance = null;
    private final Object lock = new Object();
    private IRemoteObject telephonyRemoteService = null;

    public enum MultiSimVariants {
        DSDS,
        DSDA,
        TSTS,
        UNKNOWN
    }

    @Override // ohos.telephony.ITelephony
    public String getRadioTechName(int i) {
        switch (i) {
            case 1:
                return "GSM";
            case 2:
                return "CDMA - 1xRTT";
            case 3:
                return "WCDMA";
            case 4:
                return "HSPA";
            case 5:
                return "HSPA+";
            case 6:
                return "TD-SCDMA";
            case 7:
                return "CDMA - EvDo";
            case 8:
                return "CDMA - eHRPD";
            case 9:
                return "LTE";
            case 10:
                return "LTE-CA";
            case 11:
                return "IWLAN";
            case 12:
                return "NR";
            default:
                return PluginLabelConstants.REMOTE_EXCEPTION_DEFAULT;
        }
    }

    static {
        try {
            System.loadLibrary("ipc_core.z");
        } catch (UnsatisfiedLinkError unused) {
            HiLog.error(TAG, "Load library libipc_core.z.so failed.", new Object[0]);
        }
    }

    private TelephonyProxy() {
    }

    public static TelephonyProxy getInstance() {
        if (instance == null) {
            synchronized (TelephonyProxy.class) {
                if (instance == null) {
                    instance = new TelephonyProxy();
                }
            }
        }
        return instance;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        synchronized (this.lock) {
            if (this.telephonyRemoteService != null) {
                return this.telephonyRemoteService;
            }
            this.telephonyRemoteService = SysAbilityManager.checkSysAbility(4001);
            if (this.telephonyRemoteService != null) {
                this.telephonyRemoteService.addDeathRecipient(new TelephonyDeathRecipient(this, null), 0);
            } else {
                HiLog.error(TAG, "getSysAbility(TelephonyService) failed.", new Object[0]);
            }
            return this.telephonyRemoteService;
        }
    }

    private IRemoteObject getTelephonySrvAbility() throws RemoteException {
        return (IRemoteObject) Optional.ofNullable(asObject()).orElseThrow($$Lambda$t9E2a5kBSvCJG3OvOwSmRDhzvos.INSTANCE);
    }

    /* access modifiers changed from: private */
    public class TelephonyDeathRecipient implements IRemoteObject.DeathRecipient {
        private TelephonyDeathRecipient() {
        }

        /* synthetic */ TelephonyDeathRecipient(TelephonyProxy telephonyProxy, AnonymousClass1 r2) {
            this();
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            HiLog.warn(TelephonyProxy.TAG, "TelephonyDeathRecipient::onRemoteDied.", new Object[0]);
            synchronized (TelephonyProxy.this.lock) {
                TelephonyProxy.this.telephonyRemoteService = null;
            }
        }
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002c */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getRadioTech(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002c }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002c }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002c }
            r6 = 1
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002c }
            int r1 = r2.readInt()     // Catch:{ RemoteException -> 0x002c }
            goto L_0x0035
        L_0x002a:
            r5 = move-exception
            goto L_0x003c
        L_0x002c:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002a }
            java.lang.String r6 = "Failed to getRadioTech"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ all -> 0x002a }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002a }
        L_0x0035:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003c:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getRadioTech(int):int");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002c */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getPsRadioTech(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002c }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002c }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002c }
            r6 = 2
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002c }
            int r1 = r2.readInt()     // Catch:{ RemoteException -> 0x002c }
            goto L_0x0035
        L_0x002a:
            r5 = move-exception
            goto L_0x003c
        L_0x002c:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002a }
            java.lang.String r6 = "Failed to getPsRadioTech"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ all -> 0x002a }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002a }
        L_0x0035:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003c:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getPsRadioTech(int):int");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002c */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getCsRadioTech(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002c }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002c }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002c }
            r6 = 3
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002c }
            int r1 = r2.readInt()     // Catch:{ RemoteException -> 0x002c }
            goto L_0x0035
        L_0x002a:
            r5 = move-exception
            goto L_0x003c
        L_0x002c:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002a }
            java.lang.String r6 = "Failed to getCsRadioTech"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ all -> 0x002a }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002a }
        L_0x0035:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003c:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getCsRadioTech(int):int");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002d */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getIsoCountryCodeForNetwork(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0009
            return r1
        L_0x0009:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002d }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002d }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002d }
            r6 = 4
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002d }
            java.lang.String r1 = r2.readString()     // Catch:{ RemoteException -> 0x002d }
            goto L_0x0037
        L_0x002b:
            r5 = move-exception
            goto L_0x003e
        L_0x002d:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002b }
            java.lang.String r6 = "Failed to getIsoCountryCodeForNetwork"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x002b }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002b }
        L_0x0037:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003e:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getIsoCountryCodeForNetwork(int):java.lang.String");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:3|4|5|7|8|9|10) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0040, code lost:
        r2.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0046, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x002d, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002f */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.telephony.NetworkState getNetworkState(int r6) {
        /*
            r5 = this;
            ohos.telephony.NetworkState r0 = new ohos.telephony.NetworkState
            r0.<init>()
            boolean r1 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            if (r1 != 0) goto L_0x000c
            return r0
        L_0x000c:
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r1.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002f }
            r1.writeInt(r6)     // Catch:{ RemoteException -> 0x002f }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002f }
            r6 = 5
            r5.sendRequest(r6, r1, r2, r3)     // Catch:{ RemoteException -> 0x002f }
            r2.readSequenceable(r0)     // Catch:{ RemoteException -> 0x002f }
            goto L_0x0039
        L_0x002d:
            r5 = move-exception
            goto L_0x0040
        L_0x002f:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002d }
            java.lang.String r6 = "Failed to getServiceState"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x002d }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002d }
        L_0x0039:
            r2.reclaim()
            r1.reclaim()
            return r0
        L_0x0040:
            r2.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getNetworkState(int):ohos.telephony.NetworkState");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x004e, code lost:
        throw r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x002b, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to getCellInfoList", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0048, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002d */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.telephony.CellInformation> getCellInfoList() {
        /*
            r6 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r4 = 0
            java.lang.String r5 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r5)     // Catch:{ RemoteException -> 0x002d }
            ohos.rpc.IRemoteObject r6 = r6.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002d }
            r5 = 6
            r6.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002d }
            java.util.List r3 = ohos.telephony.CellInformation.createCellInfoListFromParcel(r1)     // Catch:{ RemoteException -> 0x002d }
        L_0x0024:
            r1.reclaim()
            r0.reclaim()
            goto L_0x0037
        L_0x002b:
            r6 = move-exception
            goto L_0x0048
        L_0x002d:
            ohos.hiviewdfx.HiLogLabel r6 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002b }
            java.lang.String r2 = "Failed to getCellInfoList"
            java.lang.Object[] r5 = new java.lang.Object[r4]     // Catch:{ all -> 0x002b }
            ohos.hiviewdfx.HiLog.error(r6, r2, r5)     // Catch:{ all -> 0x002b }
            goto L_0x0024
        L_0x0037:
            ohos.hiviewdfx.HiLogLabel r6 = ohos.telephony.TelephonyProxy.TAG
            r0 = 1
            java.lang.Object[] r0 = new java.lang.Object[r0]
            java.lang.String r1 = r3.toString()
            r0[r4] = r1
            java.lang.String r1 = "getCellInfoList is: %{private}s"
            ohos.hiviewdfx.HiLog.debug(r6, r1, r0)
            return r3
        L_0x0048:
            r1.reclaim()
            r0.reclaim()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getCellInfoList():java.util.List");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0031 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.telephony.SignalInformation> getSignalInfoList(int r7) {
        /*
            r6 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            boolean r1 = ohos.telephony.TelephonyUtils.isValidSlotId(r7)
            if (r1 != 0) goto L_0x000c
            return r0
        L_0x000c:
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            r4 = 0
            java.lang.String r5 = "OHOS.Telephony.ITelephony"
            r1.writeInterfaceToken(r5)     // Catch:{ RemoteException -> 0x0031 }
            r1.writeInt(r7)     // Catch:{ RemoteException -> 0x0031 }
            ohos.rpc.IRemoteObject r6 = r6.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0031 }
            r7 = 7
            r6.sendRequest(r7, r1, r2, r3)     // Catch:{ RemoteException -> 0x0031 }
            java.util.List r0 = ohos.telephony.SignalInformation.createSignalInfoListFromParcel(r2)     // Catch:{ RemoteException -> 0x0031 }
            goto L_0x003a
        L_0x002f:
            r6 = move-exception
            goto L_0x0051
        L_0x0031:
            ohos.hiviewdfx.HiLogLabel r6 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002f }
            java.lang.String r7 = "Failed to getSignalInfoList"
            java.lang.Object[] r3 = new java.lang.Object[r4]     // Catch:{ all -> 0x002f }
            ohos.hiviewdfx.HiLog.error(r6, r7, r3)     // Catch:{ all -> 0x002f }
        L_0x003a:
            r2.reclaim()
            r1.reclaim()
            ohos.hiviewdfx.HiLogLabel r6 = ohos.telephony.TelephonyProxy.TAG
            r7 = 1
            java.lang.Object[] r7 = new java.lang.Object[r7]
            java.lang.String r1 = r0.toString()
            r7[r4] = r1
            java.lang.String r1 = "getSignalInfoList is: %{private}s"
            ohos.hiviewdfx.HiLog.debug(r6, r1, r7)
            return r0
        L_0x0051:
            r2.reclaim()
            r1.reclaim()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getSignalInfoList(int):java.util.List");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002e */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getUniqueDeviceId(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0009
            return r1
        L_0x0009:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002e }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002e }
            r6 = 8
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002e }
            java.lang.String r1 = r2.readString()     // Catch:{ RemoteException -> 0x002e }
            goto L_0x0038
        L_0x002c:
            r5 = move-exception
            goto L_0x003f
        L_0x002e:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002c }
            java.lang.String r6 = "Failed to getUniqueDeviceId"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x002c }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002c }
        L_0x0038:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003f:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getUniqueDeviceId(int):java.lang.String");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002e */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getImei(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0009
            return r1
        L_0x0009:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002e }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002e }
            r6 = 9
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002e }
            java.lang.String r1 = r2.readString()     // Catch:{ RemoteException -> 0x002e }
            goto L_0x0038
        L_0x002c:
            r5 = move-exception
            goto L_0x003f
        L_0x002e:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002c }
            java.lang.String r6 = "Failed to getImei"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x002c }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002c }
        L_0x0038:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003f:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getImei(int):java.lang.String");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002e */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getImeiSv(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0009
            return r1
        L_0x0009:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002e }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002e }
            r6 = 10
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002e }
            java.lang.String r1 = r2.readString()     // Catch:{ RemoteException -> 0x002e }
            goto L_0x0038
        L_0x002c:
            r5 = move-exception
            goto L_0x003f
        L_0x002e:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002c }
            java.lang.String r6 = "Failed to getImeiSv"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x002c }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002c }
        L_0x0038:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003f:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getImeiSv(int):java.lang.String");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002e */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getTypeAllocationCode(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0009
            return r1
        L_0x0009:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002e }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002e }
            r6 = 11
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002e }
            java.lang.String r1 = r2.readString()     // Catch:{ RemoteException -> 0x002e }
            goto L_0x0038
        L_0x002c:
            r5 = move-exception
            goto L_0x003f
        L_0x002e:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002c }
            java.lang.String r6 = "Failed to getTypeAllocationCode"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x002c }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002c }
        L_0x0038:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003f:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getTypeAllocationCode(int):java.lang.String");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002e */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getMeid(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0009
            return r1
        L_0x0009:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002e }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002e }
            r6 = 12
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002e }
            java.lang.String r1 = r2.readString()     // Catch:{ RemoteException -> 0x002e }
            goto L_0x0038
        L_0x002c:
            r5 = move-exception
            goto L_0x003f
        L_0x002e:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002c }
            java.lang.String r6 = "Failed to getTypeAllocationCode"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x002c }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002c }
        L_0x0038:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003f:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getMeid(int):java.lang.String");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002e */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getManufacturerCode(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0009
            return r1
        L_0x0009:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002e }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002e }
            r6 = 13
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002e }
            java.lang.String r1 = r2.readString()     // Catch:{ RemoteException -> 0x002e }
            goto L_0x0038
        L_0x002c:
            r5 = move-exception
            goto L_0x003f
        L_0x002e:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002c }
            java.lang.String r6 = "Failed to getManufacturerCode"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x002c }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002c }
        L_0x0038:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003f:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getManufacturerCode(int):java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0027, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to getPrimarySlotId", new java.lang.Object[r3]);
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
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getPrimarySlotId() {
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
            r4 = 14
            r5.sendRequest(r4, r0, r1, r2)     // Catch:{ RemoteException -> 0x0029 }
            int r3 = r1.readInt()     // Catch:{ RemoteException -> 0x0029 }
        L_0x0020:
            r1.reclaim()
            r0.reclaim()
            goto L_0x0033
        L_0x0027:
            r5 = move-exception
            goto L_0x0034
        L_0x0029:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0027 }
            java.lang.String r2 = "Failed to getPrimarySlotId"
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getPrimarySlotId():int");
    }

    public boolean isNrSupported() {
        String telephonyProperty = TelephonyUtils.getTelephonyProperty(0, TelephonyUtils.PROPERTY_MODEM_CAPABILITY, "false");
        if (telephonyProperty.length() < 8 || (Integer.parseInt(String.valueOf(telephonyProperty.charAt(7)), 16) & 4) == 0) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0027, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to getNrOptionMode", new java.lang.Object[r3]);
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
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getNrOptionMode() {
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
            r4 = 17
            r5.sendRequest(r4, r0, r1, r2)     // Catch:{ RemoteException -> 0x0029 }
            int r3 = r1.readInt()     // Catch:{ RemoteException -> 0x0029 }
        L_0x0020:
            r1.reclaim()
            r0.reclaim()
            goto L_0x0033
        L_0x0027:
            r5 = move-exception
            goto L_0x0034
        L_0x0029:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0027 }
            java.lang.String r2 = "Failed to getNrOptionMode"
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getNrOptionMode():int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:6|7) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0037, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x003d, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0025, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to isNsaState", new java.lang.Object[0]);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x0027 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isNsaState() {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            r3 = 0
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0027 }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0027 }
            r4 = 18
            r5.sendRequest(r4, r0, r1, r2)     // Catch:{ RemoteException -> 0x0027 }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0027 }
            if (r5 == 0) goto L_0x0030
            r5 = 1
            r3 = r5
            goto L_0x0030
        L_0x0025:
            r5 = move-exception
            goto L_0x0037
        L_0x0027:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0025 }
            java.lang.String r2 = "Failed to isNsaState"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x0025 }
            ohos.hiviewdfx.HiLog.error(r5, r2, r4)     // Catch:{ all -> 0x0025 }
        L_0x0030:
            r1.reclaim()
            r0.reclaim()
            return r3
        L_0x0037:
            r1.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.isNsaState():boolean");
    }

    public String getPlmnNumeric(int i) {
        if (!TelephonyUtils.isValidSlotId(i)) {
            return "";
        }
        String telephonyProperty = TelephonyUtils.getTelephonyProperty(i, TelephonyUtils.PROPERTY_OPERATOR_PLMN, "");
        HiLog.debug(TAG, "getPlmnNumeric, plmn = %s, slotId = %d", telephonyProperty, Integer.valueOf(i));
        return telephonyProperty;
    }

    public String getOperatorName(int i) {
        if (!TelephonyUtils.isValidSlotId(i)) {
            return "";
        }
        String telephonyProperty = TelephonyUtils.getTelephonyProperty(i, TelephonyUtils.PROPERTY_OPERATOR_NAME, "");
        HiLog.debug(TAG, "getPlmnName, plmnName = %s, slotId = %d", telephonyProperty, Integer.valueOf(i));
        return telephonyProperty;
    }

    public boolean isRoaming(int i) {
        if (!TelephonyUtils.isValidSlotId(i)) {
            return false;
        }
        boolean parseBoolean = Boolean.parseBoolean(TelephonyUtils.getTelephonyProperty(i, TelephonyUtils.PROPERTY_OPERATOR_ISROAMING, "false"));
        HiLog.debug(TAG, "isRoaming, isRoaming = %{public}s, slotId = %d", String.valueOf(parseBoolean), Integer.valueOf(i));
        return parseBoolean;
    }

    public boolean isSupportMultiSim() {
        String str = Parameters.get(TelephonyUtils.PROPERTY_MULTI_SIM_CONFIG, "");
        HiLog.debug(TAG, "isSupportMultiSim, multiSimProp = %s", str);
        return "dsds".equals(str) || "dsda".equals(str) || "tsts".equals(str);
    }

    public MultiSimVariants getMultiSimConfiguration() {
        String str = Parameters.get(TelephonyUtils.PROPERTY_MULTI_SIM_CONFIG, "");
        if ("dsda".equals(str)) {
            return MultiSimVariants.DSDA;
        }
        if ("dsds".equals(str)) {
            return MultiSimVariants.DSDS;
        }
        if ("tsts".equals(str)) {
            return MultiSimVariants.TSTS;
        }
        return MultiSimVariants.UNKNOWN;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.telephony.TelephonyProxy$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$telephony$TelephonyProxy$MultiSimVariants = new int[MultiSimVariants.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                ohos.telephony.TelephonyProxy$MultiSimVariants[] r0 = ohos.telephony.TelephonyProxy.MultiSimVariants.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.telephony.TelephonyProxy.AnonymousClass1.$SwitchMap$ohos$telephony$TelephonyProxy$MultiSimVariants = r0
                int[] r0 = ohos.telephony.TelephonyProxy.AnonymousClass1.$SwitchMap$ohos$telephony$TelephonyProxy$MultiSimVariants     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.telephony.TelephonyProxy$MultiSimVariants r1 = ohos.telephony.TelephonyProxy.MultiSimVariants.DSDS     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.telephony.TelephonyProxy.AnonymousClass1.$SwitchMap$ohos$telephony$TelephonyProxy$MultiSimVariants     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.telephony.TelephonyProxy$MultiSimVariants r1 = ohos.telephony.TelephonyProxy.MultiSimVariants.DSDA     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.telephony.TelephonyProxy.AnonymousClass1.$SwitchMap$ohos$telephony$TelephonyProxy$MultiSimVariants     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.telephony.TelephonyProxy$MultiSimVariants r1 = ohos.telephony.TelephonyProxy.MultiSimVariants.TSTS     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.AnonymousClass1.<clinit>():void");
        }
    }

    public int getMaxSimCount() {
        int i = AnonymousClass1.$SwitchMap$ohos$telephony$TelephonyProxy$MultiSimVariants[getMultiSimConfiguration().ordinal()];
        if (i == 1 || i == 2) {
            return 2;
        }
        return i != 3 ? 1 : 3;
    }

    public String getIsoCountryCodeForSim(int i) {
        if (!TelephonyUtils.isValidSlotId(i)) {
            return "";
        }
        String telephonyProperty = TelephonyUtils.getTelephonyProperty(i, TelephonyUtils.PROPERTY_SIM_ISO_COUNTRY, "");
        HiLog.debug(TAG, "getIsoCountryCodeForSim, SimIsoCountryCode = %s, slotId = %d", telephonyProperty, Integer.valueOf(i));
        return telephonyProperty;
    }

    public String getSimOperatorNumeric(int i) {
        if (!TelephonyUtils.isValidSlotId(i)) {
            return "";
        }
        String telephonyProperty = TelephonyUtils.getTelephonyProperty(i, TelephonyUtils.PROPERTY_SIM_PLMN_NUMERIC, "");
        HiLog.debug(TAG, "getSimOperatorNumeric, SimOperatorNumeric = %s, slotId = %d", telephonyProperty, Integer.valueOf(i));
        return telephonyProperty;
    }

    public String getSimSpnName(int i) {
        if (!TelephonyUtils.isValidSlotId(i)) {
            return "";
        }
        String telephonyProperty = TelephonyUtils.getTelephonyProperty(i, TelephonyUtils.PROPERTY_SIM_SERVICE_PROVIDER_NAME, "");
        HiLog.debug(TAG, "getSimSpnName, spn = %s, slotId = %d", telephonyProperty, Integer.valueOf(i));
        return telephonyProperty;
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002e */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getSimIccId(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0009
            return r1
        L_0x0009:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002e }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002e }
            r6 = 1002(0x3ea, float:1.404E-42)
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002e }
            java.lang.String r1 = r2.readString()     // Catch:{ RemoteException -> 0x002e }
            goto L_0x0038
        L_0x002c:
            r5 = move-exception
            goto L_0x003f
        L_0x002e:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002c }
            java.lang.String r6 = "Failed to getSimIccId"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x002c }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002c }
        L_0x0038:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003f:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getSimIccId(int):java.lang.String");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002e */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getSimTelephoneNumber(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0009
            return r1
        L_0x0009:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002e }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002e }
            r6 = 1003(0x3eb, float:1.406E-42)
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002e }
            java.lang.String r1 = r2.readString()     // Catch:{ RemoteException -> 0x002e }
            goto L_0x0038
        L_0x002c:
            r5 = move-exception
            goto L_0x003f
        L_0x002e:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002c }
            java.lang.String r6 = "Failed to getTelephoneNumber"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x002c }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002c }
        L_0x0038:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003f:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getSimTelephoneNumber(int):java.lang.String");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002e */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getSimGid1(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0009
            return r1
        L_0x0009:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002e }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002e }
            r6 = 1004(0x3ec, float:1.407E-42)
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002e }
            java.lang.String r1 = r2.readString()     // Catch:{ RemoteException -> 0x002e }
            goto L_0x0038
        L_0x002c:
            r5 = move-exception
            goto L_0x003f
        L_0x002e:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002c }
            java.lang.String r6 = "Failed to getGid1"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x002c }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002c }
        L_0x0038:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003f:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getSimGid1(int):java.lang.String");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002d */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getSimState(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002d }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002d }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002d }
            r6 = 1005(0x3ed, float:1.408E-42)
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002d }
            int r1 = r2.readInt()     // Catch:{ RemoteException -> 0x002d }
            goto L_0x0036
        L_0x002b:
            r5 = move-exception
            goto L_0x003d
        L_0x002d:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002b }
            java.lang.String r6 = "Failed to getSimState"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ all -> 0x002b }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002b }
        L_0x0036:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003d:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getSimState(int):int");
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:4:0x0017 */
    /* JADX DEBUG: Multi-variable search result rejected for r1v2, resolved type: int */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v3, types: [boolean] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002d */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasSimCard(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002d }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002d }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002d }
            r6 = 1006(0x3ee, float:1.41E-42)
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002d }
            boolean r1 = r2.readBoolean()     // Catch:{ RemoteException -> 0x002d }
            goto L_0x0036
        L_0x002b:
            r5 = move-exception
            goto L_0x003d
        L_0x002d:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002b }
            java.lang.String r6 = "Failed to hasSimCard"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ all -> 0x002b }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002b }
        L_0x0036:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003d:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.hasSimCard(int):boolean");
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:4:0x0013 */
    /* JADX DEBUG: Multi-variable search result rejected for r0v2, resolved type: int */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v3, types: [boolean] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0029 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isEmergencyPhoneNumber(java.lang.String r6) {
        /*
            r5 = this;
            r0 = 0
            if (r6 != 0) goto L_0x0004
            return r0
        L_0x0004:
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r1.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0029 }
            r1.writeString(r6)     // Catch:{ RemoteException -> 0x0029 }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0029 }
            r6 = 2008(0x7d8, float:2.814E-42)
            r5.sendRequest(r6, r1, r2, r3)     // Catch:{ RemoteException -> 0x0029 }
            boolean r0 = r2.readBoolean()     // Catch:{ RemoteException -> 0x0029 }
            goto L_0x0032
        L_0x0027:
            r5 = move-exception
            goto L_0x0039
        L_0x0029:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0027 }
            java.lang.String r6 = "Failed to check emergency phone number"
            java.lang.Object[] r3 = new java.lang.Object[r0]     // Catch:{ all -> 0x0027 }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x0027 }
        L_0x0032:
            r2.reclaim()
            r1.reclaim()
            return r0
        L_0x0039:
            r2.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.isEmergencyPhoneNumber(java.lang.String):boolean");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002e */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getVoiceMailNumber(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0009
            return r1
        L_0x0009:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002e }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002e }
            r6 = 1007(0x3ef, float:1.411E-42)
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002e }
            java.lang.String r1 = r2.readString()     // Catch:{ RemoteException -> 0x002e }
            goto L_0x0038
        L_0x002c:
            r5 = move-exception
            goto L_0x003f
        L_0x002e:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002c }
            java.lang.String r6 = "Failed to get voice mail number"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x002c }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002c }
        L_0x0038:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003f:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getVoiceMailNumber(int):java.lang.String");
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:4:0x0017 */
    /* JADX DEBUG: Multi-variable search result rejected for r1v2, resolved type: int */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v3, types: [boolean] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002d */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setDefaultSmsSlotId(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002d }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002d }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002d }
            r6 = 3002(0xbba, float:4.207E-42)
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002d }
            boolean r1 = r2.readBoolean()     // Catch:{ RemoteException -> 0x002d }
            goto L_0x0036
        L_0x002b:
            r5 = move-exception
            goto L_0x003d
        L_0x002d:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002b }
            java.lang.String r6 = "Failed to setDefaultSmsSlotId"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ all -> 0x002b }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002b }
        L_0x0036:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003d:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.setDefaultSmsSlotId(int):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0027, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to getDefaultSmsSlotId", new java.lang.Object[r3]);
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
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getDefaultSmsSlotId() {
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
            r4 = 3003(0xbbb, float:4.208E-42)
            r5.sendRequest(r4, r0, r1, r2)     // Catch:{ RemoteException -> 0x0029 }
            int r3 = r1.readInt()     // Catch:{ RemoteException -> 0x0029 }
        L_0x0020:
            r1.reclaim()
            r0.reclaim()
            goto L_0x0033
        L_0x0027:
            r5 = move-exception
            goto L_0x0034
        L_0x0029:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0027 }
            java.lang.String r2 = "Failed to getDefaultSmsSlotId"
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getDefaultSmsSlotId():int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:5|6|7|10) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return "unknown";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0026, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to getImsShortMessageFormat", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0032, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003b, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0041, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0028 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getImsShortMessageFormat() {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x0028 }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0028 }
            r3 = 3005(0xbbd, float:4.211E-42)
            r4.sendRequest(r3, r0, r1, r2)     // Catch:{ RemoteException -> 0x0028 }
            java.lang.String r4 = r1.readString()     // Catch:{ RemoteException -> 0x0028 }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003a
        L_0x0026:
            r4 = move-exception
            goto L_0x003b
        L_0x0028:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0026 }
            java.lang.String r2 = "Failed to getImsShortMessageFormat"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x0026 }
            ohos.hiviewdfx.HiLog.error(r4, r2, r3)     // Catch:{ all -> 0x0026 }
            r1.reclaim()
            r0.reclaim()
            java.lang.String r4 = "unknown"
        L_0x003a:
            return r4
        L_0x003b:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getImsShortMessageFormat():java.lang.String");
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
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to isImsSmsSupported", new java.lang.Object[r3]);
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
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isImsSmsSupported() {
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
            r4 = 3006(0xbbe, float:4.212E-42)
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
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0027 }
            java.lang.String r2 = "Failed to isImsSmsSupported"
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.isImsSmsSupported():boolean");
    }

    @Override // ohos.telephony.ITelephony
    public void sendMessage(Context context, int i, String str, String str2, String str3, ISendShortMessageCallback iSendShortMessageCallback, IDeliveryShortMessageCallback iDeliveryShortMessageCallback) {
        TelephonyAdapt.sendSmsMessage(i, str, str2, str3, context, iSendShortMessageCallback, iDeliveryShortMessageCallback);
    }

    @Override // ohos.telephony.ITelephony
    public void sendMessage(Context context, int i, String str, String str2, short s, byte[] bArr, ISendShortMessageCallback iSendShortMessageCallback, IDeliveryShortMessageCallback iDeliveryShortMessageCallback) {
        TelephonyAdapt.sendDataMessage(i, str, str2, s, bArr, context, iSendShortMessageCallback, iDeliveryShortMessageCallback);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:11|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0051, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to addObserver", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0064, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x006a, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0053 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addObserver(int r5, ohos.telephony.IRadioStateObserver r6, java.lang.String r7, int r8) {
        /*
        // Method dump skipped, instructions count: 107
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.addObserver(int, ohos.telephony.IRadioStateObserver, java.lang.String, int):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:3|4|5|7|8|9|10) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0043, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0049, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0030, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0032 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void removeObserver(int r5, ohos.telephony.IRadioStateObserver r6, java.lang.String r7) {
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
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x0032 }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x0032 }
            ohos.rpc.IRemoteObject r5 = r6.asObject()     // Catch:{ RemoteException -> 0x0032 }
            r0.writeRemoteObject(r5)     // Catch:{ RemoteException -> 0x0032 }
            r0.writeString(r7)     // Catch:{ RemoteException -> 0x0032 }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0032 }
            r5 = 6001(0x1771, float:8.409E-42)
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0032 }
            goto L_0x003c
        L_0x0030:
            r4 = move-exception
            goto L_0x0043
        L_0x0032:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0030 }
            java.lang.String r5 = "Failed to removeObserver"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x0030 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r6)     // Catch:{ all -> 0x0030 }
        L_0x003c:
            r1.reclaim()
            r0.reclaim()
            return
        L_0x0043:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.removeObserver(int, ohos.telephony.IRadioStateObserver, java.lang.String):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:11|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to create service state from observer parcel", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0033, code lost:
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0036, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0022, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0025 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.telephony.NetworkState createNetworkStateFromObserverParcel(ohos.rpc.MessageParcel r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r1 = new ohos.rpc.MessageOption
            r1.<init>()
            r2 = 0
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0024 }
            r3 = 6004(0x1774, float:8.413E-42)
            r4.sendRequest(r3, r5, r0, r1)     // Catch:{ RemoteException -> 0x0024 }
            ohos.telephony.NetworkState r4 = new ohos.telephony.NetworkState     // Catch:{ RemoteException -> 0x0024 }
            r4.<init>()     // Catch:{ RemoteException -> 0x0024 }
            boolean r5 = r0.readSequenceable(r4)     // Catch:{ RemoteException -> 0x0025 }
            if (r5 != 0) goto L_0x002f
            r0.reclaim()
            return r2
        L_0x0022:
            r4 = move-exception
            goto L_0x0033
        L_0x0024:
            r4 = r2
        L_0x0025:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0022 }
            java.lang.String r1 = "Failed to create service state from observer parcel"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0022 }
            ohos.hiviewdfx.HiLog.error(r5, r1, r2)     // Catch:{ all -> 0x0022 }
        L_0x002f:
            r0.reclaim()
            return r4
        L_0x0033:
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.createNetworkStateFromObserverParcel(ohos.rpc.MessageParcel):ohos.telephony.NetworkState");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x001f, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to createSignalInfoFromObserverParcel", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002d, code lost:
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0030, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0021 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.telephony.SignalInformation> createSignalInfoFromObserverParcel(ohos.rpc.MessageParcel r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r1 = new ohos.rpc.MessageOption
            r1.<init>()
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0021 }
            r3 = 6002(0x1772, float:8.41E-42)
            r4.sendRequest(r3, r5, r0, r1)     // Catch:{ RemoteException -> 0x0021 }
            java.util.List r2 = ohos.telephony.SignalInformation.createSignalInfoListFromParcel(r0)     // Catch:{ RemoteException -> 0x0021 }
        L_0x001b:
            r0.reclaim()
            goto L_0x002c
        L_0x001f:
            r4 = move-exception
            goto L_0x002d
        L_0x0021:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x001f }
            java.lang.String r5 = "Failed to createSignalInfoFromObserverParcel"
            r1 = 0
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x001f }
            ohos.hiviewdfx.HiLog.error(r4, r5, r1)     // Catch:{ all -> 0x001f }
            goto L_0x001b
        L_0x002c:
            return r2
        L_0x002d:
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.createSignalInfoFromObserverParcel(ohos.rpc.MessageParcel):java.util.List");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x001f, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to createCellInfoFromObserverParcel", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002d, code lost:
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0030, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0021 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.telephony.CellInformation> createCellInfoFromObserverParcel(ohos.rpc.MessageParcel r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r1 = new ohos.rpc.MessageOption
            r1.<init>()
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0021 }
            r3 = 6003(0x1773, float:8.412E-42)
            r4.sendRequest(r3, r5, r0, r1)     // Catch:{ RemoteException -> 0x0021 }
            java.util.List r2 = ohos.telephony.CellInformation.createCellInfoListFromParcel(r0)     // Catch:{ RemoteException -> 0x0021 }
        L_0x001b:
            r0.reclaim()
            goto L_0x002c
        L_0x001f:
            r4 = move-exception
            goto L_0x002d
        L_0x0021:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x001f }
            java.lang.String r5 = "Failed to createCellInfoFromObserverParcel"
            r1 = 0
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x001f }
            ohos.hiviewdfx.HiLog.error(r4, r5, r1)     // Catch:{ all -> 0x001f }
            goto L_0x001b
        L_0x002c:
            return r2
        L_0x002d:
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.createCellInfoFromObserverParcel(ohos.rpc.MessageParcel):java.util.List");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(9:0|1|2|3|4|8|9|10|11) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0044, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x004a, code lost:
        throw r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0031, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0034 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getCellularDataFlowType() {
        /*
            r7 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            r3 = 0
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0033 }
            ohos.rpc.IRemoteObject r7 = r7.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0033 }
            r4 = 4001(0xfa1, float:5.607E-42)
            r7.sendRequest(r4, r0, r1, r2)     // Catch:{ RemoteException -> 0x0033 }
            int r7 = r1.readInt()     // Catch:{ RemoteException -> 0x0033 }
            ohos.hiviewdfx.HiLogLabel r2 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ RemoteException -> 0x0034 }
            java.lang.String r4 = "getCellularDataFlowType, dataFlow = %d"
            r5 = 1
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ RemoteException -> 0x0034 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r7)     // Catch:{ RemoteException -> 0x0034 }
            r5[r3] = r6     // Catch:{ RemoteException -> 0x0034 }
            ohos.hiviewdfx.HiLog.debug(r2, r4, r5)     // Catch:{ RemoteException -> 0x0034 }
            goto L_0x003d
        L_0x0031:
            r7 = move-exception
            goto L_0x0044
        L_0x0033:
            r7 = r3
        L_0x0034:
            ohos.hiviewdfx.HiLogLabel r2 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0031 }
            java.lang.String r4 = "Failed to getCellularDataFlowType"
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x0031 }
            ohos.hiviewdfx.HiLog.error(r2, r4, r3)     // Catch:{ all -> 0x0031 }
        L_0x003d:
            r1.reclaim()
            r0.reclaim()
            return r7
        L_0x0044:
            r1.reclaim()
            r0.reclaim()
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getCellularDataFlowType():int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to isCellularDataEnabled", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0041, code lost:
        r2.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0047, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002f, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0031 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isCellularDataEnabled(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0031 }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x0031 }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0031 }
            r6 = 4002(0xfa2, float:5.608E-42)
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x0031 }
            int r5 = r2.readInt()     // Catch:{ RemoteException -> 0x0031 }
            if (r5 == 0) goto L_0x003a
            r5 = 1
            r1 = r5
            goto L_0x003a
        L_0x002f:
            r5 = move-exception
            goto L_0x0041
        L_0x0031:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002f }
            java.lang.String r6 = "Failed to isCellularDataEnabled"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ all -> 0x002f }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002f }
        L_0x003a:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x0041:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.isCellularDataEnabled(int):boolean");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002b */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void enableCellularData(int r5, boolean r6) {
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
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002b }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002b }
            r0.writeBoolean(r6)     // Catch:{ RemoteException -> 0x002b }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002b }
            r5 = 4003(0xfa3, float:5.61E-42)
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002b }
            goto L_0x0035
        L_0x0029:
            r4 = move-exception
            goto L_0x003c
        L_0x002b:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0029 }
            java.lang.String r5 = "Failed to enableCellularData"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x0029 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r6)     // Catch:{ all -> 0x0029 }
        L_0x0035:
            r1.reclaim()
            r0.reclaim()
            return
        L_0x003c:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.enableCellularData(int, boolean):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to isCellularDataRoamingEnabled", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0041, code lost:
        r2.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0047, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002f, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0031 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isCellularDataRoamingEnabled(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0031 }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x0031 }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0031 }
            r6 = 4004(0xfa4, float:5.611E-42)
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x0031 }
            int r5 = r2.readInt()     // Catch:{ RemoteException -> 0x0031 }
            if (r5 == 0) goto L_0x003a
            r5 = 1
            r1 = r5
            goto L_0x003a
        L_0x002f:
            r5 = move-exception
            goto L_0x0041
        L_0x0031:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002f }
            java.lang.String r6 = "Failed to isCellularDataRoamingEnabled"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ all -> 0x002f }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002f }
        L_0x003a:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x0041:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.isCellularDataRoamingEnabled(int):boolean");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002b */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void enableCellularDataRoaming(int r5, boolean r6) {
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
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002b }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002b }
            r0.writeBoolean(r6)     // Catch:{ RemoteException -> 0x002b }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002b }
            r5 = 4005(0xfa5, float:5.612E-42)
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002b }
            goto L_0x0035
        L_0x0029:
            r4 = move-exception
            goto L_0x003c
        L_0x002b:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0029 }
            java.lang.String r5 = "Failed to enableCellularDataRoaming"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x0029 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r6)     // Catch:{ all -> 0x0029 }
        L_0x0035:
            r1.reclaim()
            r0.reclaim()
            return
        L_0x003c:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.enableCellularDataRoaming(int, boolean):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:0|1|2|4|5|6|7) */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0024, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0036, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x003c, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0026 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getRadioTechnologyType(int r6) {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            r3 = 0
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0026 }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x0026 }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0026 }
            r6 = 4006(0xfa6, float:5.614E-42)
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x0026 }
            int r3 = r1.readInt()     // Catch:{ RemoteException -> 0x0026 }
            goto L_0x002f
        L_0x0024:
            r5 = move-exception
            goto L_0x0036
        L_0x0026:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0024 }
            java.lang.String r6 = "Failed to getRadioTechnologyType"
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch:{ all -> 0x0024 }
            ohos.hiviewdfx.HiLog.error(r5, r6, r2)     // Catch:{ all -> 0x0024 }
        L_0x002f:
            r1.reclaim()
            r0.reclaim()
            return r3
        L_0x0036:
            r1.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getRadioTechnologyType(int):int");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002d */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getCellularDataState(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002d }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002d }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002d }
            r6 = 4007(0xfa7, float:5.615E-42)
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002d }
            int r1 = r2.readInt()     // Catch:{ RemoteException -> 0x002d }
            goto L_0x0036
        L_0x002b:
            r5 = move-exception
            goto L_0x003d
        L_0x002d:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002b }
            java.lang.String r6 = "Failed to getCellularDataState"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ all -> 0x002b }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002b }
        L_0x0036:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003d:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getCellularDataState(int):int");
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
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to getDefaultCellularDataSlotId", new java.lang.Object[0]);
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
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getDefaultCellularDataSlotId() {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x0028 }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0028 }
            r3 = 4008(0xfa8, float:5.616E-42)
            r4.sendRequest(r3, r0, r1, r2)     // Catch:{ RemoteException -> 0x0028 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0028 }
            r1.reclaim()
            r0.reclaim()
            goto L_0x0039
        L_0x0026:
            r4 = move-exception
            goto L_0x003a
        L_0x0028:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0026 }
            java.lang.String r2 = "Failed to getDefaultCellularDataSlotId"
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getDefaultCellularDataSlotId():int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x003a, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0040, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x002c, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to setDefaultCellularDataSlotId", new java.lang.Object[0]);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x002e */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setDefaultCellularDataSlotId(int r5) {
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
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002e }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002e }
            r5 = 4009(0xfa9, float:5.618E-42)
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002e }
        L_0x0025:
            r1.reclaim()
            r0.reclaim()
            goto L_0x0039
        L_0x002c:
            r4 = move-exception
            goto L_0x003a
        L_0x002e:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002c }
            java.lang.String r5 = "Failed to setDefaultCellularDataSlotId"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x002c }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x002c }
            goto L_0x0025
        L_0x0039:
            return
        L_0x003a:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.setDefaultCellularDataSlotId(int):void");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0031 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addObserver(int r5, ohos.rpc.IRemoteObject r6, java.lang.String r7, int r8) {
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
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x0031 }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x0031 }
            r0.writeRemoteObject(r6)     // Catch:{ RemoteException -> 0x0031 }
            r0.writeString(r7)     // Catch:{ RemoteException -> 0x0031 }
            r0.writeInt(r8)     // Catch:{ RemoteException -> 0x0031 }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0031 }
            r5 = 4010(0xfaa, float:5.619E-42)
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0031 }
            goto L_0x003b
        L_0x002f:
            r4 = move-exception
            goto L_0x0042
        L_0x0031:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002f }
            java.lang.String r5 = "Failed to addObserver"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x002f }
            ohos.hiviewdfx.HiLog.error(r4, r5, r6)     // Catch:{ all -> 0x002f }
        L_0x003b:
            r1.reclaim()
            r0.reclaim()
            return
        L_0x0042:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.addObserver(int, ohos.rpc.IRemoteObject, java.lang.String, int):void");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002e */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void removeObserver(int r5, ohos.rpc.IRemoteObject r6, java.lang.String r7) {
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
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002e }
            r0.writeRemoteObject(r6)     // Catch:{ RemoteException -> 0x002e }
            r0.writeString(r7)     // Catch:{ RemoteException -> 0x002e }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002e }
            r5 = 4011(0xfab, float:5.62E-42)
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002e }
            goto L_0x0038
        L_0x002c:
            r4 = move-exception
            goto L_0x003f
        L_0x002e:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002c }
            java.lang.String r5 = "Failed to removeObserver"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x002c }
            ohos.hiviewdfx.HiLog.error(r4, r5, r6)     // Catch:{ all -> 0x002c }
        L_0x0038:
            r1.reclaim()
            r0.reclaim()
            return
        L_0x003f:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.removeObserver(int, ohos.rpc.IRemoteObject, java.lang.String):void");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002e */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getSimTeleNumberIdentifier(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0009
            return r1
        L_0x0009:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002e }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002e }
            r6 = 1008(0x3f0, float:1.413E-42)
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002e }
            java.lang.String r1 = r2.readString()     // Catch:{ RemoteException -> 0x002e }
            goto L_0x0038
        L_0x002c:
            r5 = move-exception
            goto L_0x003f
        L_0x002e:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002c }
            java.lang.String r6 = "Failed to getSimTeleNumberIdentifier"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x002c }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002c }
        L_0x0038:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003f:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getSimTeleNumberIdentifier(int):java.lang.String");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:10|11) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to getVoiceMailCount", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0042, code lost:
        r2.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0048, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0030, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0032 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getVoiceMailCount(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0032 }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x0032 }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0032 }
            r6 = 1009(0x3f1, float:1.414E-42)
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x0032 }
            int r5 = r2.readInt()     // Catch:{ RemoteException -> 0x0032 }
            r6 = -1
            if (r5 != r6) goto L_0x002e
            goto L_0x003b
        L_0x002e:
            r1 = r5
            goto L_0x003b
        L_0x0030:
            r5 = move-exception
            goto L_0x0042
        L_0x0032:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0030 }
            java.lang.String r6 = "Failed to getVoiceMailCount"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ all -> 0x0030 }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x0030 }
        L_0x003b:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x0042:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getVoiceMailCount(int):int");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002e */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getVoiceMailIdentifier(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0009
            return r1
        L_0x0009:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002e }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002e }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002e }
            r6 = 1010(0x3f2, float:1.415E-42)
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002e }
            java.lang.String r1 = r2.readString()     // Catch:{ RemoteException -> 0x002e }
            goto L_0x0038
        L_0x002c:
            r5 = move-exception
            goto L_0x003f
        L_0x002e:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002c }
            java.lang.String r6 = "Failed to getVoiceMailIdentifier"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x002c }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002c }
        L_0x0038:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003f:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getVoiceMailIdentifier(int):java.lang.String");
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:4:0x0017 */
    /* JADX DEBUG: Multi-variable search result rejected for r1v2, resolved type: int */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v3, types: [boolean] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002d */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isSimActive(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002d }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002d }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002d }
            r6 = 1011(0x3f3, float:1.417E-42)
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002d }
            boolean r1 = r2.readBoolean()     // Catch:{ RemoteException -> 0x002d }
            goto L_0x0036
        L_0x002b:
            r5 = move-exception
            goto L_0x003d
        L_0x002d:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002b }
            java.lang.String r6 = "Failed to isSimActive"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ all -> 0x002b }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002b }
        L_0x0036:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003d:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.isSimActive(int):boolean");
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
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to getDefaultVoiceSlotId", new java.lang.Object[0]);
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
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getDefaultVoiceSlotId() {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x0028 }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0028 }
            r3 = 1012(0x3f4, float:1.418E-42)
            r4.sendRequest(r3, r0, r1, r2)     // Catch:{ RemoteException -> 0x0028 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0028 }
            r1.reclaim()
            r0.reclaim()
            goto L_0x0039
        L_0x0026:
            r4 = move-exception
            goto L_0x003a
        L_0x0028:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0026 }
            java.lang.String r2 = "Failed to getDefaultVoiceSlotId"
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getDefaultVoiceSlotId():int");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002d */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getNetworkSelectionMode(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            r1 = -1
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002d }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002d }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002d }
            r6 = 19
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002d }
            int r1 = r2.readInt()     // Catch:{ RemoteException -> 0x002d }
            goto L_0x0037
        L_0x002b:
            r5 = move-exception
            goto L_0x003e
        L_0x002d:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002b }
            java.lang.String r6 = "Failed to getNetworkSelectionMode"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x002b }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002b }
        L_0x0037:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003e:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getNetworkSelectionMode(int):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x008c, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to getNetworkSearchInformation", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x009e, code lost:
        r2.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00a4, code lost:
        throw r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        return null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:22:0x008e */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.telephony.NetworkSearchResult getNetworkSearchInformation(int r12) {
        /*
        // Method dump skipped, instructions count: 165
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getNetworkSearchInformation(int):ohos.telephony.NetworkSearchResult");
    }

    @Override // ohos.telephony.ITelephony
    public boolean setNetworkSelectionMode(int i, int i2, NetworkInformation networkInformation, boolean z) {
        if (!TelephonyUtils.isValidSlotId(i)) {
            return false;
        }
        if (i2 == 0) {
            return setNetworkSelectionModeAuto(i);
        }
        if (i2 != 1) {
            HiLog.error(TAG, "unknown select mode.", new Object[0]);
            return false;
        } else if (networkInformation != null) {
            return setNetworkSelectionModeManual(i, networkInformation, z);
        } else {
            HiLog.error(TAG, "networkInformation is null.", new Object[0]);
            return false;
        }
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:1:0x0010 */
    /* JADX DEBUG: Multi-variable search result rejected for r3v2, resolved type: int */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v3, types: [boolean] */
    /* JADX WARNING: Can't wrap try/catch for region: R(7:0|1|2|4|5|6|7) */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0024, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0036, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x003c, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0026 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean setNetworkSelectionModeAuto(int r6) {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            r3 = 0
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0026 }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x0026 }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0026 }
            r6 = 21
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x0026 }
            boolean r3 = r1.readBoolean()     // Catch:{ RemoteException -> 0x0026 }
            goto L_0x002f
        L_0x0024:
            r5 = move-exception
            goto L_0x0036
        L_0x0026:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0024 }
            java.lang.String r6 = "Failed to setNetworkSelectionModeAuto"
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch:{ all -> 0x0024 }
            ohos.hiviewdfx.HiLog.error(r5, r6, r2)     // Catch:{ all -> 0x0024 }
        L_0x002f:
            r1.reclaim()
            r0.reclaim()
            return r3
        L_0x0036:
            r1.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.setNetworkSelectionModeAuto(int):boolean");
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:1:0x0010 */
    /* JADX DEBUG: Multi-variable search result rejected for r3v2, resolved type: int */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v3, types: [boolean] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x002c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean setNetworkSelectionModeManual(int r6, ohos.telephony.NetworkInformation r7, boolean r8) {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            r3 = 0
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002c }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002c }
            r5.marshallingNetworkInformation(r7, r0)     // Catch:{ RemoteException -> 0x002c }
            r0.writeBoolean(r8)     // Catch:{ RemoteException -> 0x002c }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002c }
            r6 = 22
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x002c }
            boolean r3 = r1.readBoolean()     // Catch:{ RemoteException -> 0x002c }
            goto L_0x0035
        L_0x002a:
            r5 = move-exception
            goto L_0x003c
        L_0x002c:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002a }
            java.lang.String r6 = "Failed to setNetworkSelectionModeManual"
            java.lang.Object[] r7 = new java.lang.Object[r3]     // Catch:{ all -> 0x002a }
            ohos.hiviewdfx.HiLog.error(r5, r6, r7)     // Catch:{ all -> 0x002a }
        L_0x0035:
            r1.reclaim()
            r0.reclaim()
            return r3
        L_0x003c:
            r1.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.setNetworkSelectionModeManual(int, ohos.telephony.NetworkInformation, boolean):boolean");
    }

    private void marshallingNetworkInformation(NetworkInformation networkInformation, MessageParcel messageParcel) {
        messageParcel.writeString(networkInformation.getOperatorName());
        messageParcel.writeString(networkInformation.getOperatorName());
        messageParcel.writeString(networkInformation.getOperatorNumeric());
        messageParcel.writeInt(networkInformation.getNetworkState());
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:4:0x0017 */
    /* JADX DEBUG: Multi-variable search result rejected for r1v2, resolved type: int */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v3, types: [boolean] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002d */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasOperatorPrivileges(int r6) {
        /*
            r5 = this;
            boolean r0 = ohos.telephony.TelephonyUtils.isValidSlotId(r6)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>()
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x002d }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x002d }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002d }
            r6 = 1013(0x3f5, float:1.42E-42)
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x002d }
            boolean r1 = r2.readBoolean()     // Catch:{ RemoteException -> 0x002d }
            goto L_0x0036
        L_0x002b:
            r5 = move-exception
            goto L_0x003d
        L_0x002d:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x002b }
            java.lang.String r6 = "Failed to hasOperatorPrivileges"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ all -> 0x002b }
            ohos.hiviewdfx.HiLog.error(r5, r6, r3)     // Catch:{ all -> 0x002b }
        L_0x0036:
            r2.reclaim()
            r0.reclaim()
            return r1
        L_0x003d:
            r2.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.hasOperatorPrivileges(int):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:5|6) */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0022, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to sendUpdateCellLocationRequest", new java.lang.Object[0]);
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
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sendUpdateCellLocationRequest() {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x0024 }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0024 }
            r3 = 23
            r4.sendRequest(r3, r0, r1, r2)     // Catch:{ RemoteException -> 0x0024 }
        L_0x001b:
            r1.reclaim()
            r0.reclaim()
            goto L_0x002f
        L_0x0022:
            r4 = move-exception
            goto L_0x0030
        L_0x0024:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0022 }
            java.lang.String r2 = "Failed to sendUpdateCellLocationRequest"
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.sendUpdateCellLocationRequest():void");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0029, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to getPesn", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0035, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003e, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0044, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002b */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getPesn(int r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002b }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002b }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002b }
            r5 = 24
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002b }
            java.lang.String r4 = r1.readString()     // Catch:{ RemoteException -> 0x002b }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003d
        L_0x0029:
            r4 = move-exception
            goto L_0x003e
        L_0x002b:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0029 }
            java.lang.String r5 = "Failed to getPesn"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0029 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0029 }
            r1.reclaim()
            r0.reclaim()
            java.lang.String r4 = ""
        L_0x003d:
            return r4
        L_0x003e:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getPesn(int):java.lang.String");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0029, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to getLine1NumberFromImpu", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0035, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003e, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0044, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002b */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getLine1NumberFromImpu(int r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002b }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002b }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002b }
            r5 = 1014(0x3f6, float:1.421E-42)
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002b }
            java.lang.String r4 = r1.readString()     // Catch:{ RemoteException -> 0x002b }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003d
        L_0x0029:
            r4 = move-exception
            goto L_0x003e
        L_0x002b:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0029 }
            java.lang.String r5 = "Failed to getLine1NumberFromImpu"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0029 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0029 }
            r1.reclaim()
            r0.reclaim()
            java.lang.String r4 = ""
        L_0x003d:
            return r4
        L_0x003e:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getLine1NumberFromImpu(int):java.lang.String");
    }

    @Override // ohos.telephony.ITelephony
    public int getCardType(int i) {
        if (i == 0) {
            return Parameters.getInt("gsm.sim1.type", -1);
        }
        if (i == 1) {
            return Parameters.getInt("gsm.sim2.type", -1);
        }
        return -1;
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:1:0x0010 */
    /* JADX DEBUG: Multi-variable search result rejected for r3v2, resolved type: int */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v3, types: [boolean] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0029 */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setSmscAddr(int r6, java.lang.String r7) {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            r3 = 0
            java.lang.String r4 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0029 }
            r0.writeInt(r6)     // Catch:{ RemoteException -> 0x0029 }
            r0.writeString(r7)     // Catch:{ RemoteException -> 0x0029 }
            ohos.rpc.IRemoteObject r5 = r5.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x0029 }
            r6 = 3008(0xbc0, float:4.215E-42)
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x0029 }
            boolean r3 = r1.readBoolean()     // Catch:{ RemoteException -> 0x0029 }
            goto L_0x0032
        L_0x0027:
            r5 = move-exception
            goto L_0x0039
        L_0x0029:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0027 }
            java.lang.String r6 = "Failed to setSmscAddr"
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.setSmscAddr(int, java.lang.String):boolean");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0029, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.telephony.TelephonyProxy.TAG, "Failed to getSmscAddr", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0035, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003e, code lost:
        r1.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0044, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002b */
    @Override // ohos.telephony.ITelephony
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getSmscAddr(int r5) {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r2.<init>()
            java.lang.String r3 = "OHOS.Telephony.ITelephony"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x002b }
            r0.writeInt(r5)     // Catch:{ RemoteException -> 0x002b }
            ohos.rpc.IRemoteObject r4 = r4.getTelephonySrvAbility()     // Catch:{ RemoteException -> 0x002b }
            r5 = 3007(0xbbf, float:4.214E-42)
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x002b }
            java.lang.String r4 = r1.readString()     // Catch:{ RemoteException -> 0x002b }
            r1.reclaim()
            r0.reclaim()
            goto L_0x003d
        L_0x0029:
            r4 = move-exception
            goto L_0x003e
        L_0x002b:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.telephony.TelephonyProxy.TAG     // Catch:{ all -> 0x0029 }
            java.lang.String r5 = "Failed to getSmscAddr"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0029 }
            ohos.hiviewdfx.HiLog.error(r4, r5, r2)     // Catch:{ all -> 0x0029 }
            r1.reclaim()
            r0.reclaim()
            java.lang.String r4 = ""
        L_0x003d:
            return r4
        L_0x003e:
            r1.reclaim()
            r0.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.TelephonyProxy.getSmscAddr(int):java.lang.String");
    }
}
