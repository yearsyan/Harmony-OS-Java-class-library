package ohos.devicesecmgr;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.sysability.samgr.SysAbilityManager;

public class DeviceSecMgr implements IDeviceSecMgr {
    private static final int DEFAULT_TIMEOUT = 6;
    private static final String DEVICESECMGR_SERVICE_DESCRIPTOR = "OHOS.Security.DeviceSecMgr";
    private static final int DEVICE_ID_MAX_LEN = 64;
    private static final int ERR_CHALLENGE_ERR = 13;
    private static final int ERR_DEFAULT = 65535;
    private static final int ERR_HUKS_ERR = 12;
    private static final int ERR_INIT_SELF_ERR = 15;
    private static final int ERR_INVALID_LEN_PARA = 2;
    private static final int ERR_INVALID_PARA = 1;
    private static final int ERR_INVALID_VERSION = 10;
    private static final int ERR_IPC_ERR = 17;
    private static final int ERR_IPC_PROXY_ERR = 20;
    private static final int ERR_IPC_REGISTER_ERR = 18;
    private static final int ERR_IPC_REMOTE_OBJ_ERR = 19;
    private static final int ERR_IPC_RET_PARCEL_ERR = 21;
    private static final int ERR_JSON_ERR = 16;
    private static final int ERR_MEMORY_ERR = 4;
    private static final int ERR_MSG_ADD_NEIGHBOR = 25;
    private static final int ERR_MSG_CREATE_WORKQUEUE = 27;
    private static final int ERR_MSG_FULL = 24;
    private static final int ERR_MSG_NEIGHBOR_FULL = 23;
    private static final int ERR_MSG_NOT_INIT = 26;
    private static final int ERR_MSG_OPEN_SESSION = 36;
    private static final int ERR_NEED_COMPATIBLE = 28;
    private static final int ERR_NOEXIST_REQUEST = 9;
    private static final int ERR_NOT_ONLINE = 14;
    private static final int ERR_NO_CHALLENGE = 5;
    private static final int ERR_NO_CRED = 6;
    private static final int ERR_NO_MEMORY = 3;
    private static final int ERR_OEM_ERR = 11;
    private static final int ERR_PERMISSION_DENIAL = 30;
    private static final int ERR_PROFILE_CONNECT_ERR = 35;
    private static final int ERR_PROXY_REMOTE_ERR = 22;
    private static final int ERR_REG_CALLBACK = 29;
    private static final int ERR_REQUEST_CODE_ERR = 31;
    private static final int ERR_SA_BUSY = 7;
    private static final int ERR_TIMEOUT = 8;
    private static final int ERR_VERIFY_MODE_CRED_ERR = 32;
    private static final int ERR_VERIFY_MODE_HUKS_ERR = 34;
    private static final int ERR_VERIFY_SIGNED_MODE_CRED_ERR = 33;
    private static final int GET_DEV_SEC_LEVEL = 1;
    private static final Object INSTANCE_LOCK = new Object();
    private static final int MAX_MSG_SIZE = 81920;
    private static final int MAX_TIMEOUT = 10;
    private static final int MIN_MSG_SIZE = 16;
    private static final int MIN_TIMEOUT = 1;
    private static final int SA_ID_DEVICE_SEC_MGR_SERVICE = 3511;
    private static final int SECURITY_DOMAIN = 218115840;
    private static final int SUCCESS = 0;
    private static final HiLogLabel TAG = new HiLogLabel(3, SECURITY_DOMAIN, "DeviceSecMgr");
    private static volatile DeviceSecMgr instance = null;
    private volatile IRemoteObject deviceSecMgrRemoteService = null;
    private final Object lock = new Object();

    static {
        try {
            System.loadLibrary("ipc_core.z");
        } catch (UnsatisfiedLinkError unused) {
            HiLog.error(TAG, "Load library libipc_core.z.so failed.", new Object[0]);
        }
    }

    private DeviceSecMgr() {
    }

    public static DeviceSecMgr getInstance() {
        if (instance == null) {
            synchronized (INSTANCE_LOCK) {
                if (instance == null) {
                    instance = new DeviceSecMgr();
                }
            }
        }
        return instance;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        synchronized (this.lock) {
            if (this.deviceSecMgrRemoteService != null) {
                return this.deviceSecMgrRemoteService;
            }
            this.deviceSecMgrRemoteService = SysAbilityManager.checkSysAbility(SA_ID_DEVICE_SEC_MGR_SERVICE);
            if (this.deviceSecMgrRemoteService != null) {
                this.deviceSecMgrRemoteService.addDeathRecipient(new DeviceSecMgrDeathRecipient(), 0);
            } else {
                HiLog.error(TAG, "getSysAbility(deviceSecMgrRemoteService) failed.", new Object[0]);
            }
            return this.deviceSecMgrRemoteService;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x005f, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.devicesecmgr.DeviceSecMgr.TAG, "Failed to requestDeviceSecInfo", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0071, code lost:
        r2.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0077, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0061 */
    @Override // ohos.devicesecmgr.IDeviceSecMgr
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.devicesecmgr.DeviceSecInfo requestDeviceSecInfo(java.lang.String r7, ohos.devicesecmgr.RequestOption r8) {
        /*
        // Method dump skipped, instructions count: 120
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.devicesecmgr.DeviceSecMgr.requestDeviceSecInfo(java.lang.String, ohos.devicesecmgr.RequestOption):ohos.devicesecmgr.DeviceSecInfo");
    }

    private IRemoteObject getDeviceSecMgrAbility() throws RemoteException {
        return (IRemoteObject) Optional.ofNullable(asObject()).orElseThrow($$Lambda$t9E2a5kBSvCJG3OvOwSmRDhzvos.INSTANCE);
    }

    /* access modifiers changed from: private */
    public class DeviceSecMgrDeathRecipient implements IRemoteObject.DeathRecipient {
        private DeviceSecMgrDeathRecipient() {
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            HiLog.warn(DeviceSecMgr.TAG, "DeviceSecMgrDeathRecipient::onRemoteDied.", new Object[0]);
            synchronized (DeviceSecMgr.this.lock) {
                DeviceSecMgr.this.deviceSecMgrRemoteService = null;
            }
        }
    }

    private int packMsg(String str, RequestOption requestOption, MessageParcel messageParcel) {
        HiLog.info(TAG, "packMsg start!", new Object[0]);
        if (str.length() > 64) {
            HiLog.error(TAG, "The length of identify is not right!", new Object[0]);
            return 2;
        }
        messageParcel.writeByteArray(str.getBytes(StandardCharsets.UTF_8));
        int length = 64 - str.length();
        for (int i = 0; i < length; i++) {
            messageParcel.writeByte((byte) 0);
        }
        messageParcel.writeLong(requestOption.getChallenge());
        messageParcel.writeInt(requestOption.getTimeout());
        messageParcel.writeInt(requestOption.getExtra());
        return 0;
    }

    private DeviceSecInfo parseMsg(MessageParcel messageParcel) {
        HiLog.info(TAG, "parseMsg start!", new Object[0]);
        int readableBytes = messageParcel.getReadableBytes();
        if (readableBytes < 16 || readableBytes > MAX_MSG_SIZE) {
            HiLog.error(TAG, "parseMsg failed, invalid param!", new Object[0]);
            return new DeviceSecInfo();
        }
        int readInt = messageParcel.readInt();
        int readInt2 = messageParcel.readInt();
        int readInt3 = messageParcel.readInt();
        String str = messageParcel.getReadableBytes() + -32 > 0 ? new String(messageParcel.readByteArray()) : "";
        if (readInt == 28) {
            readInt2 = Legacy.legacyTrans(readInt3);
        }
        return new DeviceSecInfo(readInt, readInt2, readInt3, str);
    }
}
