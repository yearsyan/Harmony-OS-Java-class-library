package ohos.sysability.samgr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

/* access modifiers changed from: package-private */
public class SysAbilityRegistry implements ISysAbilityRegistry {
    private static final int DEATH_RECIPIENT_FLAG = 0;
    private static final String DEFAULT_CAPABILITY = "";
    private static final boolean DEFAULT_DISTRIBUTED_FLAG = false;
    private static final int DEFAULT_DUMP_FLAG = 1;
    private static final int FLATTEN_ERROR = 1;
    private static final Object INSTANCE_LOCK = new Object();
    private static final String SAMGR_INTERFACE_TOKEN = "ohos.samgr.accessToken";
    private static final HiLogLabel TAG = new HiLogLabel(3, 218109952, "SysAbilityRegistry");
    private static volatile SysAbilityRegistry registryInstance;
    private volatile IRemoteObject iRemoteObject;
    private final Object infoMapLock = new Object();
    private Map<Integer, SAInfo> saInfoMap = new HashMap();

    private SysAbilityRegistry(IRemoteObject iRemoteObject2) {
        this.iRemoteObject = iRemoteObject2;
    }

    static ISysAbilityRegistry getRegistry() {
        return getSystemAbilityManagerRegistry();
    }

    static ISysAbilityRegistry getSystemAbilityManagerRegistry() {
        if (registryInstance == null) {
            synchronized (INSTANCE_LOCK) {
                if (registryInstance == null) {
                    IRemoteObject systemAbilityManagerObject = SystemAbilityManagerClient.getSystemAbilityManagerObject();
                    if (systemAbilityManagerObject == null) {
                        HiLog.debug(TAG, "getSystemAbilityManagerObject failed", new Object[0]);
                    } else if (!systemAbilityManagerObject.addDeathRecipient(new SysAbilityManagerDeathRecipient(), 0)) {
                        HiLog.debug(TAG, "SystemAbilityManagerObject addDeathRecipient failed", new Object[0]);
                    }
                    registryInstance = new SysAbilityRegistry(systemAbilityManagerObject);
                }
            }
        }
        return registryInstance;
    }

    @Override // ohos.sysability.samgr.ISysAbilityRegistry
    public IRemoteObject getSysAbility(int i) throws RemoteException {
        return checkSysAbility(i);
    }

    @Override // ohos.sysability.samgr.ISysAbilityRegistry
    public int addSysAbility(int i, IRemoteObject iRemoteObject2) throws RemoteException {
        return addSysAbility(i, iRemoteObject2, false, 1, "");
    }

    @Override // ohos.sysability.samgr.ISysAbilityRegistry
    public int addSysAbility(int i, IRemoteObject iRemoteObject2, boolean z, int i2) throws RemoteException {
        return addSysAbility(i, iRemoteObject2, z, i2, "");
    }

    @Override // ohos.sysability.samgr.ISysAbilityRegistry
    public int removeSysAbility(int i) throws RemoteException {
        if (this.iRemoteObject != null) {
            synchronized (this.infoMapLock) {
                this.saInfoMap.remove(Integer.valueOf(i));
            }
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            try {
                if (obtain.writeInterfaceToken(SAMGR_INTERFACE_TOKEN)) {
                    if (!obtain.writeInt(i)) {
                        HiLog.error(TAG, "removeSysAbility: write systemAbilityId error!", new Object[0]);
                    } else {
                        if (!this.iRemoteObject.sendRequest(4, obtain, obtain2, new MessageOption())) {
                            HiLog.error(TAG, "removeSysAbility: sendRequest failed!", new Object[0]);
                        } else {
                            int readInt = obtain2.readInt();
                            obtain2.reclaim();
                            obtain.reclaim();
                            return readInt;
                        }
                    }
                }
                return 1;
            } finally {
                obtain2.reclaim();
                obtain.reclaim();
            }
        } else {
            HiLog.error(TAG, "Remote Exception: native server is not ready!", new Object[0]);
            throw new RemoteException();
        }
    }

    @Override // ohos.sysability.samgr.ISysAbilityRegistry
    public IRemoteObject checkSysAbility(int i) throws RemoteException {
        if (this.iRemoteObject != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            try {
                if (!obtain.writeInt(i)) {
                    HiLog.error(TAG, "checkSysAbility: write systemAbilityId error!", new Object[0]);
                } else {
                    if (!this.iRemoteObject.sendRequest(1, obtain, obtain2, new MessageOption())) {
                        HiLog.error(TAG, "checkSysAbility: sendRequest failed!", new Object[0]);
                    } else {
                        IRemoteObject readRemoteObject = obtain2.readRemoteObject();
                        obtain2.reclaim();
                        obtain.reclaim();
                        return readRemoteObject;
                    }
                }
                return null;
            } finally {
                obtain2.reclaim();
                obtain.reclaim();
            }
        } else {
            throw new RemoteException();
        }
    }

    @Override // ohos.sysability.samgr.ISysAbilityRegistry
    public String[] listSysAbilities(int i) throws RemoteException {
        if (this.iRemoteObject != null) {
            ArrayList<String> listAbilitiesListInner = listAbilitiesListInner(i);
            HiLog.debug(TAG, "listSysAbilities: get services size %{public}d!", Integer.valueOf(listAbilitiesListInner.size()));
            String[] strArr = new String[listAbilitiesListInner.size()];
            listAbilitiesListInner.toArray(strArr);
            return strArr;
        }
        HiLog.error(TAG, "Remote Exception: native server is not ready!", new Object[0]);
        throw new RemoteException();
    }

    @Override // ohos.sysability.samgr.ISysAbilityRegistry
    public IRemoteObject getSysAbility(int i, String str) throws RemoteException {
        if (this.iRemoteObject != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            try {
                if (obtain.writeInterfaceToken(SAMGR_INTERFACE_TOKEN)) {
                    if (!obtain.writeInt(i)) {
                        HiLog.error(TAG, "getRemoteSysAbility: write systemAbilityId failed!", new Object[0]);
                    } else if (!obtain.writeString(str)) {
                        HiLog.error(TAG, "getRemoteSysAbility: write deviceId failed!", new Object[0]);
                    } else {
                        if (!this.iRemoteObject.sendRequest(16, obtain, obtain2, new MessageOption())) {
                            HiLog.error(TAG, "getRemoteSysAbility: sendRequest failed!", new Object[0]);
                        } else {
                            IRemoteObject readRemoteObject = obtain2.readRemoteObject();
                            obtain2.reclaim();
                            obtain.reclaim();
                            return readRemoteObject;
                        }
                    }
                }
                return null;
            } finally {
                obtain2.reclaim();
                obtain.reclaim();
            }
        } else {
            HiLog.error(TAG, "getRemoteSysAbility: iRemoteObject is null", new Object[0]);
            throw new RemoteException();
        }
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.iRemoteObject;
    }

    @Override // ohos.sysability.samgr.ISysAbilityRegistry
    public int addSysAbility(int i, IRemoteObject iRemoteObject2, boolean z, int i2, String str) throws RemoteException {
        if (this.iRemoteObject != null) {
            SAInfo sAInfo = new SAInfo(i, iRemoteObject2, z, i2, str);
            synchronized (this.infoMapLock) {
                this.saInfoMap.put(Integer.valueOf(i), sAInfo);
            }
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            try {
                if (obtain.writeInterfaceToken(SAMGR_INTERFACE_TOKEN)) {
                    if (!parcelSAInfo(obtain, sAInfo)) {
                        HiLog.error(TAG, "addSysAbility: parcelSAInfo error!", new Object[0]);
                    } else {
                        if (!this.iRemoteObject.sendRequest(3, obtain, obtain2, new MessageOption())) {
                            HiLog.error(TAG, "addSysAbility: sendRequest error!", new Object[0]);
                        } else {
                            int readInt = obtain2.readInt();
                            obtain2.reclaim();
                            obtain.reclaim();
                            return readInt;
                        }
                    }
                }
                return 1;
            } finally {
                obtain2.reclaim();
                obtain.reclaim();
            }
        } else {
            HiLog.error(TAG, "Remote Exception: native server is not ready!", new Object[0]);
            throw new RemoteException();
        }
    }

    private boolean parcelSAInfo(MessageParcel messageParcel, SAInfo sAInfo) {
        if (messageParcel == null || sAInfo == null) {
            HiLog.error(TAG, "parcelSAInfo: input null params!", new Object[0]);
            return false;
        } else if (!messageParcel.writeInt(sAInfo.getSaId())) {
            HiLog.error(TAG, "parcelSAInfo: write systemAbilityId error!", new Object[0]);
            return false;
        } else if (!messageParcel.writeRemoteObject(sAInfo.getService())) {
            HiLog.error(TAG, "parcelSAInfo: write object error!", new Object[0]);
            return false;
        } else if (!messageParcel.writeBoolean(sAInfo.isDistributed())) {
            HiLog.error(TAG, "parcelSAInfo: write distributed error!", new Object[0]);
            return false;
        } else if (!messageParcel.writeInt(sAInfo.getDumpFlags())) {
            HiLog.error(TAG, "parcelSAInfo: write flag error!", new Object[0]);
            return false;
        } else if (messageParcel.writeString(sAInfo.getCapability())) {
            return true;
        } else {
            HiLog.error(TAG, "parcelSAInfo: write capability error!", new Object[0]);
            return false;
        }
    }

    @Override // ohos.sysability.samgr.ISysAbilityRegistry
    public boolean reRegisterSysAbility() {
        this.iRemoteObject = SystemAbilityManagerClient.getSystemAbilityManagerObject();
        if (this.iRemoteObject == null) {
            HiLog.debug(TAG, "SystemAbilityManagerClient getSystemAbilityManagerObject is null", new Object[0]);
            return false;
        }
        synchronized (this.infoMapLock) {
            for (Map.Entry<Integer, SAInfo> entry : this.saInfoMap.entrySet()) {
                HiLog.debug(TAG, "ReRegisterSysAbility: addSA name: %{public}d", entry.getKey());
                SAInfo value = entry.getValue();
                try {
                    int addSysAbility = addSysAbility(value.getSaId(), value.getService(), value.isDistributed(), value.getDumpFlags(), value.getCapability());
                    HiLog.debug(TAG, "ReRegisterSysAbility: addSA result: %{public}d", Integer.valueOf(addSysAbility));
                } catch (RemoteException unused) {
                    HiLog.debug(TAG, "ReRegisterSysAbility add exception", new Object[0]);
                }
            }
        }
        return true;
    }

    @Override // ohos.sysability.samgr.ISysAbilityRegistry
    public int registerSystemReadyCallback(IRemoteObject iRemoteObject2) throws RemoteException {
        if (iRemoteObject2 == null) {
            throw new RemoteException();
        } else if (this.iRemoteObject != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            try {
                if (obtain.writeInterfaceToken(SAMGR_INTERFACE_TOKEN)) {
                    if (!obtain.writeRemoteObject(iRemoteObject2)) {
                        HiLog.error(TAG, "registerSystemReadyCallback: write object error!", new Object[0]);
                    } else {
                        if (!this.iRemoteObject.sendRequest(24, obtain, obtain2, new MessageOption())) {
                            HiLog.error(TAG, "registerSystemReadyCallback: sendRequest failed!", new Object[0]);
                        } else {
                            int readInt = obtain2.readInt();
                            obtain2.reclaim();
                            obtain.reclaim();
                            return readInt;
                        }
                    }
                }
                return 1;
            } finally {
                obtain2.reclaim();
                obtain.reclaim();
            }
        } else {
            HiLog.error(TAG, "registerSystemReadyCallback: iRemoteObject is null", new Object[0]);
            throw new RemoteException();
        }
    }

    @Override // ohos.sysability.samgr.ISysAbilityRegistry
    public int addSystemCapability(String str) throws RemoteException {
        if (this.iRemoteObject != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            try {
                if (obtain.writeInterfaceToken(SAMGR_INTERFACE_TOKEN)) {
                    if (!obtain.writeString(str)) {
                        HiLog.error(TAG, "addSystemCapability: write object error!", new Object[0]);
                    } else {
                        if (!this.iRemoteObject.sendRequest(26, obtain, obtain2, new MessageOption())) {
                            HiLog.error(TAG, "addSystemCapability: sendRequest failed!", new Object[0]);
                        } else {
                            int readInt = obtain2.readInt();
                            obtain2.reclaim();
                            obtain.reclaim();
                            return readInt;
                        }
                    }
                }
                return 1;
            } finally {
                obtain2.reclaim();
                obtain.reclaim();
            }
        } else {
            HiLog.error(TAG, "addSystemCapability: iRemoteObject is null", new Object[0]);
            throw new RemoteException();
        }
    }

    @Override // ohos.sysability.samgr.ISysAbilityRegistry
    public boolean hasSystemCapability(String str) throws RemoteException {
        if (this.iRemoteObject != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            try {
                if (obtain.writeInterfaceToken(SAMGR_INTERFACE_TOKEN)) {
                    if (!obtain.writeString(str)) {
                        HiLog.error(TAG, "hasSystemCapability: write object error!", new Object[0]);
                    } else {
                        if (!this.iRemoteObject.sendRequest(27, obtain, obtain2, new MessageOption())) {
                            HiLog.error(TAG, "hasSystemCapability: sendRequest failed!", new Object[0]);
                        } else {
                            boolean readBoolean = obtain2.readBoolean();
                            obtain2.reclaim();
                            obtain.reclaim();
                            return readBoolean;
                        }
                    }
                }
                return false;
            } finally {
                obtain2.reclaim();
                obtain.reclaim();
            }
        } else {
            HiLog.error(TAG, "hasSystemCapability: iRemoteObject is null", new Object[0]);
            throw new RemoteException();
        }
    }

    @Override // ohos.sysability.samgr.ISysAbilityRegistry
    public List<String> getSystemAvailableCapabilities() throws RemoteException {
        List<String> readStringList;
        if (this.iRemoteObject != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            try {
                if (!obtain.writeInterfaceToken(SAMGR_INTERFACE_TOKEN)) {
                    readStringList = Collections.emptyList();
                } else {
                    if (!this.iRemoteObject.sendRequest(28, obtain, obtain2, new MessageOption())) {
                        HiLog.error(TAG, "getSystemAvailableCapabilities: sendRequest failed!", new Object[0]);
                        readStringList = Collections.emptyList();
                    } else {
                        readStringList = obtain2.readStringList();
                    }
                }
                return readStringList;
            } finally {
                obtain2.reclaim();
                obtain.reclaim();
            }
        } else {
            HiLog.error(TAG, "getSystemAvailableCapabilities: iRemoteObject is null", new Object[0]);
            throw new RemoteException();
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:23|24) */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0085, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.sysability.samgr.SysAbilityRegistry.TAG, "listAbilitiesListInner: runtime exception!", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0097, code lost:
        r3.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x009d, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0087 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.ArrayList<java.lang.String> listAbilitiesListInner(int r8) {
        /*
        // Method dump skipped, instructions count: 158
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.sysability.samgr.SysAbilityRegistry.listAbilitiesListInner(int):java.util.ArrayList");
    }

    /* access modifiers changed from: private */
    public static class SAInfo {
        private String capability;
        private int dumpFlags;
        private boolean isDistributed;
        private IRemoteObject service;
        private int systemAbilityId;

        public SAInfo(int i, IRemoteObject iRemoteObject, boolean z, int i2, String str) {
            this.systemAbilityId = i;
            this.service = iRemoteObject;
            this.isDistributed = z;
            this.dumpFlags = i2;
            this.capability = str;
        }

        public int getSaId() {
            return this.systemAbilityId;
        }

        public IRemoteObject getService() {
            return this.service;
        }

        public boolean isDistributed() {
            return this.isDistributed;
        }

        public int getDumpFlags() {
            return this.dumpFlags;
        }

        public String getCapability() {
            return this.capability;
        }
    }
}
