package ohos.account;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import ohos.os.ProcessManager;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;
import ohos.sysability.samgr.SysAbilityManager;

public class AccountProxy implements IRemoteBroker {
    private static final AccountAbilityAdapter ABILITY_ADAPTER = new AccountAbilityAdapter();
    private static final AccountProxyAdapter ACCOUNT_ADAPTER = new AccountProxyAdapter();
    private static final AccountProxy ACCOUNT_PROXY_INSTANCE = new AccountProxy();
    private static final int CMD_ACTIVATE_LOCAL_ACCOUNT = 111;
    private static final int CMD_CREATE_LOCAL_ACCOUNT = 101;
    private static final int CMD_GET_ACCOUNT_ALL_CONSTRAINTS = 115;
    private static final int CMD_IS_ACCOUNT_CONSTRAINT_ENABLE = 116;
    private static final int CMD_IS_ACTIVE_STATUS_BY_ID = 120;
    private static final int CMD_IS_KING_KONG = 125;
    private static final int CMD_IS_SUPPORT_MULT_ACCOUNT = 100;
    private static final int CMD_IS_VERIFIED_STATUS = 118;
    private static final int CMD_IS_VERIFIED_STATUS_BY_ID = 119;
    private static final int CMD_QUERY_ACCOUNT_DISTRIBUTED_INFO = 2;
    private static final int CMD_QUERY_ALL_LOCAL_ACCOUNT = 108;
    private static final int CMD_QUERY_CURRENT_LOCAL_ACCOUNT = 107;
    private static final int CMD_QUERY_DVID = 10;
    private static final int CMD_QUERY_LOCAL_ACCOUNT_BY_ID = 106;
    private static final int CMD_QUERY_LOCAL_ACCOUNT_ID_FROM_UID = 105;
    private static final int CMD_QUERY_LOCAL_ID_FROM_PROCESS = 123;
    private static final int CMD_QUERY_MAX_SUPPORTED_NUMBER = 117;
    private static final int CMD_QUERY_TYPE_FROM_PROCESS = 124;
    private static final int CMD_REMOVE_LOCAL_ACCOUNT = 102;
    private static final int CMD_SET_ACCOUNT_CONSTRAINTS = 113;
    private static final int CMD_SET_ACCOUNT_LOCAL_NAME = 112;
    private static final int CMD_UPDATE_ACCOUNT_DISTRIBUTED_INFO = 1;
    private static final String DESCRIPTOR = "OHOS.AccountSA.IAccount";
    private static final String EMPTY_VALUE = "";
    private static final OsAccount FAIL_ACCOUNT = null;
    private static final DistributedInfo FAIL_DISTRIBUTED_INFO = null;
    private static final int INVALID_ACCOUNT_VALUE = -1;
    private static final int INVALID_LOCAL_ACCOUNT_ID = -1;
    private static final HiLogLabel LABEL = new HiLogLabel(3, LOG_DOMAIN, TAG);
    private static final int LOG_DOMAIN = 218110720;
    private static final MarshallInterface MARSHALL_STUB_FUNC = $$Lambda$AccountProxy$wgF8mk7FpPqyT5ViCtufND6jKw.INSTANCE;
    private static final String TAG = "AccountProxy";
    private static final int USER_ID_CONVERT_FACTOR = 100000;
    private final Object remoteLock = new Object();
    private IRemoteObject remoteObj = null;

    /* access modifiers changed from: private */
    @FunctionalInterface
    public interface MarshallInterface {
        boolean marshalling(MessageParcel messageParcel);
    }

    /* access modifiers changed from: private */
    @FunctionalInterface
    public interface UnmarshallingInterface {
        boolean unmarshalling(MessageParcel messageParcel);
    }

    static /* synthetic */ boolean lambda$static$0(MessageParcel messageParcel) {
        return true;
    }

    private AccountProxy() {
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        synchronized (this.remoteLock) {
            if (this.remoteObj != null) {
                return this.remoteObj;
            }
            this.remoteObj = SysAbilityManager.getSysAbility(200);
            if (this.remoteObj == null) {
                HiLog.error(LABEL, "getSysAbility account failed", new Object[0]);
                return this.remoteObj;
            }
            this.remoteObj.addDeathRecipient(new AccountProxyDeathRecipient(), 0);
            HiLog.info(LABEL, "get remote object completed", new Object[0]);
            return this.remoteObj;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:28|29|30|31) */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x008e, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.account.AccountProxy.LABEL, "send request remote exception", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x009a, code lost:
        r2.reclaim();
        r3.reclaim();
        ohos.hiviewdfx.HiLog.debug(ohos.account.AccountProxy.LABEL, "send request end", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00a7, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00a8, code lost:
        r2.reclaim();
        r3.reclaim();
        ohos.hiviewdfx.HiLog.debug(ohos.account.AccountProxy.LABEL, "send request end", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00b5, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:28:0x0090 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean sendAccountRequest(int r6, ohos.account.AccountProxy.MarshallInterface r7, ohos.account.AccountProxy.UnmarshallingInterface r8) {
        /*
        // Method dump skipped, instructions count: 182
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.account.AccountProxy.sendAccountRequest(int, ohos.account.AccountProxy$MarshallInterface, ohos.account.AccountProxy$UnmarshallingInterface):boolean");
    }

    private boolean isInvalidUnmarshallLength(int i, MessageParcel messageParcel) {
        return i < 0 || i > messageParcel.getSize() - messageParcel.getReadPosition();
    }

    public DistributedInfo queryOsAccountDistributedInfo() {
        HiLog.debug(LABEL, "queryOsAccountDistributedInfo begin", new Object[0]);
        DistributedInfo distributedInfo = new DistributedInfo();
        if (!sendAccountRequest(2, MARSHALL_STUB_FUNC, new UnmarshallingInterface() {
            /* class ohos.account.$$Lambda$AccountProxy$7wxtZLLl_epEHjxh4QUKjPaJ8 */

            @Override // ohos.account.AccountProxy.UnmarshallingInterface
            public final boolean unmarshalling(MessageParcel messageParcel) {
                return DistributedInfo.this.unmarshalling(messageParcel);
            }
        })) {
            HiLog.error(LABEL, "queryOsAccountDistributedInfo sendAccountRequest fail", new Object[0]);
            return FAIL_DISTRIBUTED_INFO;
        }
        HiLog.debug(LABEL, "queryOsAccountDistributedInfo end", new Object[0]);
        return distributedInfo;
    }

    public boolean updateOsAccountDistributedInfo(String str, String str2, String str3) {
        HiLog.debug(LABEL, "updateOsAccountDistributedInfo begin", new Object[0]);
        if (str == null || str.isEmpty()) {
            HiLog.error(LABEL, "updateOsAccountDistributedInfo accountName is null", new Object[0]);
            return false;
        } else if (str2 == null || str2.isEmpty()) {
            HiLog.error(LABEL, "updateOsAccountDistributedInfo uid is null", new Object[0]);
            return false;
        } else if (str3 == null || str3.isEmpty()) {
            HiLog.error(LABEL, "updateOsAccountDistributedInfo eventStr is null", new Object[0]);
            return false;
        } else {
            boolean sendAccountRequest = sendAccountRequest(1, new MarshallInterface(str, str2, str3) {
                /* class ohos.account.$$Lambda$AccountProxy$YS9OEP5VmRVeZEdUJfOV9nLkXc */
                private final /* synthetic */ String f$0;
                private final /* synthetic */ String f$1;
                private final /* synthetic */ String f$2;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // ohos.account.AccountProxy.MarshallInterface
                public final boolean marshalling(MessageParcel messageParcel) {
                    return AccountProxy.lambda$updateOsAccountDistributedInfo$2(this.f$0, this.f$1, this.f$2, messageParcel);
                }
            }, $$Lambda$AccountProxy$5OwveoCxuY48I1Yap7rqtYJ1MsM.INSTANCE);
            if (!sendAccountRequest) {
                HiLog.error(LABEL, "updateOsAccountDistributedInfo sendAccountRequest fail", new Object[0]);
                return false;
            }
            HiLog.debug(LABEL, "updateOsAccountDistributedInfo end", new Object[0]);
            return sendAccountRequest;
        }
    }

    static /* synthetic */ boolean lambda$updateOsAccountDistributedInfo$2(String str, String str2, String str3, MessageParcel messageParcel) {
        if (messageParcel.writeString(str) && messageParcel.writeString(str2) && messageParcel.writeString(str3)) {
            return true;
        }
        HiLog.error(LABEL, "updateOsAccountDistributedInfo write data fail", new Object[0]);
        return false;
    }

    static /* synthetic */ boolean lambda$updateOsAccountDistributedInfo$3(MessageParcel messageParcel) {
        return messageParcel.readInt() == 0;
    }

    public String getDistributedVirtualDeviceId() {
        HiLog.debug(LABEL, "getDistributedVirtualDeviceId begin", new Object[0]);
        ArrayList arrayList = new ArrayList();
        if (!sendAccountRequest(10, MARSHALL_STUB_FUNC, new UnmarshallingInterface(arrayList) {
            /* class ohos.account.$$Lambda$AccountProxy$u9ZSEcxolrVz5r6iziwY8_yoWM */
            private final /* synthetic */ List f$0;

            {
                this.f$0 = r1;
            }

            @Override // ohos.account.AccountProxy.UnmarshallingInterface
            public final boolean unmarshalling(MessageParcel messageParcel) {
                return this.f$0.add(messageParcel.readString());
            }
        })) {
            HiLog.error(LABEL, "getDistributedVirtualDeviceId sendAccountRequest fail", new Object[0]);
            return "";
        }
        HiLog.debug(LABEL, "getDistributedVirtualDeviceId end", new Object[0]);
        if (arrayList.isEmpty()) {
            return "";
        }
        return (String) arrayList.get(0);
    }

    public OsAccount createOsAccount(String str, OsAccountType osAccountType) {
        HiLog.debug(LABEL, "createOsAccount begin", new Object[0]);
        if (str == null || str.isEmpty()) {
            HiLog.error(LABEL, "create account local name error", new Object[0]);
            return FAIL_ACCOUNT;
        } else if (osAccountType == OsAccountType.INVALID) {
            HiLog.error(LABEL, "create account type error", new Object[0]);
            return FAIL_ACCOUNT;
        } else {
            OsAccount osAccount = new OsAccount(str, osAccountType);
            if (!sendAccountRequest(101, new MarshallInterface() {
                /* class ohos.account.$$Lambda$AccountProxy$avjKi7UBvJwWXP2Sed9whYXKg */

                @Override // ohos.account.AccountProxy.MarshallInterface
                public final boolean marshalling(MessageParcel messageParcel) {
                    return OsAccount.this.marshalling(messageParcel);
                }
            }, new UnmarshallingInterface() {
                /* class ohos.account.$$Lambda$AccountProxy$nFoQJPH_7EWzhFnNiKQeK2uwg2s */

                @Override // ohos.account.AccountProxy.UnmarshallingInterface
                public final boolean unmarshalling(MessageParcel messageParcel) {
                    return AccountProxy.lambda$createOsAccount$6(OsAccount.this, messageParcel);
                }
            })) {
                HiLog.error(LABEL, "createOsAccount sendAccountRequest fail", new Object[0]);
                return FAIL_ACCOUNT;
            }
            HiLog.debug(LABEL, "createOsAccount end", new Object[0]);
            return osAccount;
        }
    }

    static /* synthetic */ boolean lambda$createOsAccount$6(OsAccount osAccount, MessageParcel messageParcel) {
        if (messageParcel.readInt() != 0) {
            return false;
        }
        return osAccount.unmarshalling(messageParcel);
    }

    public boolean removeOsAccount(int i) {
        HiLog.debug(LABEL, "removeOsAccount begin", new Object[0]);
        if (i < 0) {
            HiLog.error(LABEL, "removeOsAccount param error", new Object[0]);
            return false;
        }
        boolean sendAccountRequest = sendAccountRequest(102, new MarshallInterface(i) {
            /* class ohos.account.$$Lambda$AccountProxy$rG84riockrbFoiBPf_9ypOVAM34 */
            private final /* synthetic */ int f$0;

            {
                this.f$0 = r1;
            }

            @Override // ohos.account.AccountProxy.MarshallInterface
            public final boolean marshalling(MessageParcel messageParcel) {
                return messageParcel.writeInt(this.f$0);
            }
        }, $$Lambda$AccountProxy$_QRP5yihISUes6tOTemVfo_uI.INSTANCE);
        if (!sendAccountRequest) {
            HiLog.error(LABEL, "removeOsAccount sendAccountRequest fail", new Object[0]);
            return false;
        }
        HiLog.debug(LABEL, "removeOsAccount end", new Object[0]);
        return sendAccountRequest;
    }

    static /* synthetic */ boolean lambda$removeOsAccount$8(MessageParcel messageParcel) {
        return messageParcel.readInt() == 0;
    }

    public boolean isMultiOSAccountEnable() {
        HiLog.debug(LABEL, "isMultiOSAccountEnable begin", new Object[0]);
        ArrayList arrayList = new ArrayList();
        boolean sendAccountRequest = sendAccountRequest(100, MARSHALL_STUB_FUNC, new UnmarshallingInterface(arrayList) {
            /* class ohos.account.$$Lambda$AccountProxy$GSMfC9L_mWQyyGGrKQkGcIVsy0 */
            private final /* synthetic */ List f$0;

            {
                this.f$0 = r1;
            }

            @Override // ohos.account.AccountProxy.UnmarshallingInterface
            public final boolean unmarshalling(MessageParcel messageParcel) {
                return this.f$0.add(Boolean.valueOf(messageParcel.readBoolean()));
            }
        });
        if (!sendAccountRequest) {
            HiLog.error(LABEL, "sendAccountRequest fail", new Object[0]);
            return sendAccountRequest;
        }
        HiLog.debug(LABEL, "isMultiOSAccountEnable end", new Object[0]);
        if (arrayList.isEmpty()) {
            return false;
        }
        return ((Boolean) arrayList.get(0)).booleanValue();
    }

    public int queryMaxOsAccountNumber() {
        HiLog.debug(LABEL, "queryMaxOsAccountNumber begin", new Object[0]);
        ArrayList arrayList = new ArrayList();
        if (!sendAccountRequest(117, MARSHALL_STUB_FUNC, new UnmarshallingInterface(arrayList) {
            /* class ohos.account.$$Lambda$AccountProxy$wxfi3zXhDd5x8NPS4v65MoSM56s */
            private final /* synthetic */ List f$0;

            {
                this.f$0 = r1;
            }

            @Override // ohos.account.AccountProxy.UnmarshallingInterface
            public final boolean unmarshalling(MessageParcel messageParcel) {
                return this.f$0.add(Integer.valueOf(messageParcel.readInt()));
            }
        })) {
            HiLog.error(LABEL, "sendAccountRequest fail", new Object[0]);
            return -1;
        }
        HiLog.debug(LABEL, "queryMaxOsAccountNumber end", new Object[0]);
        if (arrayList.isEmpty()) {
            return -1;
        }
        return ((Integer) arrayList.get(0)).intValue();
    }

    public List<OsAccount> queryAllCreatedOsAccounts() {
        HiLog.debug(LABEL, "queryAllCreatedOsAccounts begin", new Object[0]);
        ArrayList arrayList = new ArrayList();
        if (!sendAccountRequest(108, MARSHALL_STUB_FUNC, new UnmarshallingInterface(arrayList) {
            /* class ohos.account.$$Lambda$AccountProxy$yM6wwNyf1gY6TdSAgRPZENkTio */
            private final /* synthetic */ List f$1;

            {
                this.f$1 = r2;
            }

            @Override // ohos.account.AccountProxy.UnmarshallingInterface
            public final boolean unmarshalling(MessageParcel messageParcel) {
                return AccountProxy.this.lambda$queryAllCreatedOsAccounts$11$AccountProxy(this.f$1, messageParcel);
            }
        })) {
            HiLog.error(LABEL, "sendAccountRequest fail", new Object[0]);
            return new ArrayList();
        }
        HiLog.debug(LABEL, "queryAllCreatedOsAccounts end", new Object[0]);
        return arrayList;
    }

    public /* synthetic */ boolean lambda$queryAllCreatedOsAccounts$11$AccountProxy(List list, MessageParcel messageParcel) {
        int readInt = messageParcel.readInt();
        if (isInvalidUnmarshallLength(readInt, messageParcel)) {
            HiLog.error(LABEL, "get number error %{public}d", Integer.valueOf(readInt));
            return false;
        }
        HiLog.info(LABEL, "queryAllCreatedOsAccounts num %{public}d", Integer.valueOf(readInt));
        for (int i = 0; i < readInt; i++) {
            OsAccount osAccount = new OsAccount();
            if (!osAccount.unmarshalling(messageParcel)) {
                HiLog.error(LABEL, "unmarshalling LocalAccount from accounts failed", new Object[0]);
                return false;
            }
            list.add(osAccount);
        }
        return true;
    }

    public OsAccount queryOsAccountById(int i) {
        HiLog.debug(LABEL, "queryOsAccountById begin", new Object[0]);
        OsAccount osAccount = new OsAccount();
        if (i < 0) {
            HiLog.error(LABEL, "local id error", new Object[0]);
            return FAIL_ACCOUNT;
        } else if (!sendAccountRequest(106, new MarshallInterface(i) {
            /* class ohos.account.$$Lambda$AccountProxy$2p01IfnPfJdHYB3bB011Hd5Us */
            private final /* synthetic */ int f$0;

            {
                this.f$0 = r1;
            }

            @Override // ohos.account.AccountProxy.MarshallInterface
            public final boolean marshalling(MessageParcel messageParcel) {
                return AccountProxy.lambda$queryOsAccountById$12(this.f$0, messageParcel);
            }
        }, new UnmarshallingInterface() {
            /* class ohos.account.$$Lambda$AccountProxy$cnApZjuhmQbEW5EVKoMcrWJx_Hw */

            @Override // ohos.account.AccountProxy.UnmarshallingInterface
            public final boolean unmarshalling(MessageParcel messageParcel) {
                return OsAccount.this.unmarshalling(messageParcel);
            }
        })) {
            HiLog.error(LABEL, "sendAccountRequest fail", new Object[0]);
            return FAIL_ACCOUNT;
        } else {
            HiLog.debug(LABEL, "queryOsAccountById end", new Object[0]);
            return osAccount;
        }
    }

    static /* synthetic */ boolean lambda$queryOsAccountById$12(int i, MessageParcel messageParcel) {
        if (messageParcel.writeInt(i)) {
            return true;
        }
        HiLog.error(LABEL, "write data fail", new Object[0]);
        return false;
    }

    public int getOsAccountLocalIdFromUid(int i) {
        if (i >= 0) {
            return i / 100000;
        }
        HiLog.error(LABEL, "param uid error", new Object[0]);
        return -1;
    }

    public boolean activateOsAccount(int i) {
        HiLog.debug(LABEL, "activateOsAccount %{private}d begin", Integer.valueOf(i));
        if (i < 0) {
            HiLog.error(LABEL, "activate account param error", new Object[0]);
            return false;
        }
        boolean sendAccountRequest = sendAccountRequest(111, new MarshallInterface(i) {
            /* class ohos.account.$$Lambda$AccountProxy$iuRk1xRfWoQELSGeYpzVu8Y_qEc */
            private final /* synthetic */ int f$0;

            {
                this.f$0 = r1;
            }

            @Override // ohos.account.AccountProxy.MarshallInterface
            public final boolean marshalling(MessageParcel messageParcel) {
                return AccountProxy.lambda$activateOsAccount$14(this.f$0, messageParcel);
            }
        }, $$Lambda$AccountProxy$0OlSVnXF6ktAUiBA5GhpwUPtRrU.INSTANCE);
        if (!sendAccountRequest) {
            HiLog.error(LABEL, "sendAccountRequest fail", new Object[0]);
            return sendAccountRequest;
        }
        HiLog.debug(LABEL, "activateOsAccount end", new Object[0]);
        return sendAccountRequest;
    }

    static /* synthetic */ boolean lambda$activateOsAccount$14(int i, MessageParcel messageParcel) {
        if (messageParcel.writeInt(i)) {
            return true;
        }
        HiLog.error(LABEL, "activate account write data fail", new Object[0]);
        return false;
    }

    static /* synthetic */ boolean lambda$activateOsAccount$15(MessageParcel messageParcel) {
        return messageParcel.readInt() == 0;
    }

    public OsAccount queryCurrentOsAccount() {
        HiLog.debug(LABEL, "queryCurrentOsAccount begin", new Object[0]);
        OsAccount osAccount = new OsAccount();
        if (!sendAccountRequest(107, MARSHALL_STUB_FUNC, new UnmarshallingInterface() {
            /* class ohos.account.$$Lambda$AccountProxy$elsLdcN_17VKjmIABPyCegeMabA */

            @Override // ohos.account.AccountProxy.UnmarshallingInterface
            public final boolean unmarshalling(MessageParcel messageParcel) {
                return OsAccount.this.unmarshalling(messageParcel);
            }
        })) {
            HiLog.error(LABEL, "sendAccountRequest fail", new Object[0]);
            return FAIL_ACCOUNT;
        }
        HiLog.debug(LABEL, "queryCurrentOsAccount end", new Object[0]);
        return osAccount;
    }

    public boolean setOsAccountName(int i, String str) {
        HiLog.debug(LABEL, "setOsAccountName begin", new Object[0]);
        if (i < 0) {
            HiLog.error(LABEL, "local id error", new Object[0]);
            return false;
        } else if (str == null || str.isEmpty()) {
            HiLog.error(LABEL, "local name is null", new Object[0]);
            return false;
        } else {
            boolean sendAccountRequest = sendAccountRequest(112, new MarshallInterface(i, str) {
                /* class ohos.account.$$Lambda$AccountProxy$Exef25atmYJafIWy91zfxEzZJuM */
                private final /* synthetic */ int f$0;
                private final /* synthetic */ String f$1;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                }

                @Override // ohos.account.AccountProxy.MarshallInterface
                public final boolean marshalling(MessageParcel messageParcel) {
                    return AccountProxy.lambda$setOsAccountName$17(this.f$0, this.f$1, messageParcel);
                }
            }, $$Lambda$AccountProxy$SjafWP0dQP_XBKtdLV0myUjJzhE.INSTANCE);
            if (!sendAccountRequest) {
                HiLog.error(LABEL, "sendAccountRequest fail", new Object[0]);
                return sendAccountRequest;
            }
            HiLog.debug(LABEL, "setOsAccountName end", new Object[0]);
            return sendAccountRequest;
        }
    }

    static /* synthetic */ boolean lambda$setOsAccountName$17(int i, String str, MessageParcel messageParcel) {
        if (!messageParcel.writeInt(i)) {
            HiLog.error(LABEL, "write local id fail", new Object[0]);
            return false;
        } else if (messageParcel.writeString(str)) {
            return true;
        } else {
            HiLog.error(LABEL, "write local name fail", new Object[0]);
            return false;
        }
    }

    static /* synthetic */ boolean lambda$setOsAccountName$18(MessageParcel messageParcel) {
        return messageParcel.readInt() == 0;
    }

    public boolean setOsAccountConstraints(int i, List<String> list, boolean z) {
        HiLog.debug(LABEL, "setOsAccountConstraints in", new Object[0]);
        if (i < 0) {
            HiLog.error(LABEL, "local id error", new Object[0]);
            return false;
        } else if (list == null || list.isEmpty()) {
            HiLog.error(LABEL, "constraints error", new Object[0]);
            return false;
        } else {
            boolean sendAccountRequest = sendAccountRequest(113, new MarshallInterface(i, list, z) {
                /* class ohos.account.$$Lambda$AccountProxy$snWtHTLdMIKzuji3MUFyunK7jA */
                private final /* synthetic */ int f$0;
                private final /* synthetic */ List f$1;
                private final /* synthetic */ boolean f$2;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // ohos.account.AccountProxy.MarshallInterface
                public final boolean marshalling(MessageParcel messageParcel) {
                    return AccountProxy.lambda$setOsAccountConstraints$19(this.f$0, this.f$1, this.f$2, messageParcel);
                }
            }, $$Lambda$AccountProxy$ppNELoRf8Dao8iygYTWSvCHjQ90.INSTANCE);
            if (!sendAccountRequest) {
                HiLog.error(LABEL, "sendAccountRequest fail", new Object[0]);
                return sendAccountRequest;
            }
            HiLog.debug(LABEL, "setOsAccountConstraints end", new Object[0]);
            return sendAccountRequest;
        }
    }

    static /* synthetic */ boolean lambda$setOsAccountConstraints$19(int i, List list, boolean z, MessageParcel messageParcel) {
        if (!messageParcel.writeInt(i)) {
            HiLog.error(LABEL, "write local id fail", new Object[0]);
            return false;
        } else if (!messageParcel.writeInt(list.size())) {
            HiLog.error(LABEL, "constraints size fail", new Object[0]);
            return false;
        } else {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                if (!messageParcel.writeString((String) it.next())) {
                    HiLog.error(LABEL, "write constraints fail", new Object[0]);
                    return false;
                }
            }
            if (messageParcel.writeBoolean(z)) {
                return true;
            }
            HiLog.error(LABEL, "write enable fail", new Object[0]);
            return false;
        }
    }

    static /* synthetic */ boolean lambda$setOsAccountConstraints$20(MessageParcel messageParcel) {
        return messageParcel.readInt() == 0;
    }

    public boolean isOsAccountConstraintEnable(int i, String str) {
        HiLog.debug(LABEL, "isOsAccountConstraintEnable begin", new Object[0]);
        ArrayList arrayList = new ArrayList();
        if (i < 0) {
            HiLog.error(LABEL, "local id error", new Object[0]);
            return false;
        } else if (str == null || str.isEmpty()) {
            HiLog.error(LABEL, "constraint is null", new Object[0]);
            return false;
        } else {
            boolean sendAccountRequest = sendAccountRequest(116, new MarshallInterface(i, str) {
                /* class ohos.account.$$Lambda$AccountProxy$NP_DIvIP0RDbkPZRgzMs0je0n9Q */
                private final /* synthetic */ int f$0;
                private final /* synthetic */ String f$1;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                }

                @Override // ohos.account.AccountProxy.MarshallInterface
                public final boolean marshalling(MessageParcel messageParcel) {
                    return AccountProxy.lambda$isOsAccountConstraintEnable$21(this.f$0, this.f$1, messageParcel);
                }
            }, new UnmarshallingInterface(arrayList) {
                /* class ohos.account.$$Lambda$AccountProxy$xtrLzy5ldWgnCB_gpObQoS18sg */
                private final /* synthetic */ List f$0;

                {
                    this.f$0 = r1;
                }

                @Override // ohos.account.AccountProxy.UnmarshallingInterface
                public final boolean unmarshalling(MessageParcel messageParcel) {
                    return this.f$0.add(Boolean.valueOf(messageParcel.readBoolean()));
                }
            });
            if (!sendAccountRequest) {
                HiLog.error(LABEL, "sendAccountRequest fail", new Object[0]);
                return sendAccountRequest;
            }
            HiLog.debug(LABEL, "isOsAccountConstraintEnable end", new Object[0]);
            if (arrayList.isEmpty()) {
                return false;
            }
            return ((Boolean) arrayList.get(0)).booleanValue();
        }
    }

    static /* synthetic */ boolean lambda$isOsAccountConstraintEnable$21(int i, String str, MessageParcel messageParcel) {
        if (!messageParcel.writeInt(i)) {
            HiLog.error(LABEL, "write id fail", new Object[0]);
            return false;
        } else if (messageParcel.writeString(str)) {
            return true;
        } else {
            HiLog.error(LABEL, "write constraint fail", new Object[0]);
            return false;
        }
    }

    public List<String> getOsAccountAllConstraints(int i) {
        HiLog.debug(LABEL, "getOsAccountAllConstraints begin", new Object[0]);
        ArrayList arrayList = new ArrayList();
        if (i < 0) {
            HiLog.error(LABEL, "local id error", new Object[0]);
            return new ArrayList();
        } else if (!sendAccountRequest(115, new MarshallInterface(i) {
            /* class ohos.account.$$Lambda$AccountProxy$sRy1lOGxyYKAc4eu4un4mApy3iU */
            private final /* synthetic */ int f$0;

            {
                this.f$0 = r1;
            }

            @Override // ohos.account.AccountProxy.MarshallInterface
            public final boolean marshalling(MessageParcel messageParcel) {
                return AccountProxy.lambda$getOsAccountAllConstraints$23(this.f$0, messageParcel);
            }
        }, new UnmarshallingInterface(arrayList) {
            /* class ohos.account.$$Lambda$AccountProxy$vbXiuYlgMKFukMZH2As7NKjC_Z0 */
            private final /* synthetic */ List f$1;

            {
                this.f$1 = r2;
            }

            @Override // ohos.account.AccountProxy.UnmarshallingInterface
            public final boolean unmarshalling(MessageParcel messageParcel) {
                return AccountProxy.this.lambda$getOsAccountAllConstraints$24$AccountProxy(this.f$1, messageParcel);
            }
        })) {
            HiLog.error(LABEL, "sendAccountRequest fail", new Object[0]);
            return new ArrayList();
        } else {
            HiLog.debug(LABEL, "getOsAccountAllConstraints end", new Object[0]);
            return arrayList;
        }
    }

    static /* synthetic */ boolean lambda$getOsAccountAllConstraints$23(int i, MessageParcel messageParcel) {
        if (messageParcel.writeInt(i)) {
            return true;
        }
        HiLog.error(LABEL, "write id fail", new Object[0]);
        return false;
    }

    public /* synthetic */ boolean lambda$getOsAccountAllConstraints$24$AccountProxy(List list, MessageParcel messageParcel) {
        int readInt = messageParcel.readInt();
        if (isInvalidUnmarshallLength(readInt, messageParcel)) {
            HiLog.error(LABEL, "get constraints number error %{public}d", Integer.valueOf(readInt));
            return false;
        }
        HiLog.info(LABEL, "getOsAccountAllConstraints num %{public}d", Integer.valueOf(readInt));
        for (int i = 0; i < readInt; i++) {
            list.add(messageParcel.readString());
        }
        return true;
    }

    public int getAllCreatedOsAccounts() {
        HiLog.debug(LABEL, "getAllCreatedOsAccounts begin", new Object[0]);
        List<OsAccount> queryAllCreatedOsAccounts = queryAllCreatedOsAccounts();
        HiLog.debug(LABEL, "getAllCreatedOsAccounts end", new Object[0]);
        return queryAllCreatedOsAccounts.size();
    }

    public int getOsAccountLocalIdFromProcess() {
        HiLog.debug(LABEL, "getOsAccountLocalIdFromProcess begin", new Object[0]);
        int osAccountLocalIdFromUid = getOsAccountLocalIdFromUid(ProcessManager.getUid());
        HiLog.debug(LABEL, "getOsAccountIdFromProcess end", new Object[0]);
        return osAccountLocalIdFromUid;
    }

    public boolean isOsAccountVerified() {
        HiLog.debug(LABEL, "isOsAccountVerified begin", new Object[0]);
        ArrayList arrayList = new ArrayList();
        boolean sendAccountRequest = sendAccountRequest(118, MARSHALL_STUB_FUNC, new UnmarshallingInterface(arrayList) {
            /* class ohos.account.$$Lambda$AccountProxy$1lKG1FQzGsdYiDbBlNw5NAc8bew */
            private final /* synthetic */ List f$0;

            {
                this.f$0 = r1;
            }

            @Override // ohos.account.AccountProxy.UnmarshallingInterface
            public final boolean unmarshalling(MessageParcel messageParcel) {
                return this.f$0.add(Boolean.valueOf(messageParcel.readBoolean()));
            }
        });
        if (!sendAccountRequest) {
            HiLog.error(LABEL, "sendAccountRequest fail", new Object[0]);
            return sendAccountRequest;
        }
        HiLog.debug(LABEL, "isOsAccountVerified end", new Object[0]);
        if (arrayList.isEmpty()) {
            return false;
        }
        return ((Boolean) arrayList.get(0)).booleanValue();
    }

    public boolean isOsAccountVerified(int i) {
        HiLog.debug(LABEL, "isOsAccountVerified with id begin", new Object[0]);
        if (i < 0) {
            HiLog.error(LABEL, "local id error", new Object[0]);
            return false;
        }
        ArrayList arrayList = new ArrayList();
        boolean sendAccountRequest = sendAccountRequest(119, new MarshallInterface(i) {
            /* class ohos.account.$$Lambda$AccountProxy$DFlGBwVGOqZ6kGntOhOXdSfk0 */
            private final /* synthetic */ int f$0;

            {
                this.f$0 = r1;
            }

            @Override // ohos.account.AccountProxy.MarshallInterface
            public final boolean marshalling(MessageParcel messageParcel) {
                return AccountProxy.lambda$isOsAccountVerified$26(this.f$0, messageParcel);
            }
        }, new UnmarshallingInterface(arrayList) {
            /* class ohos.account.$$Lambda$AccountProxy$tOqHqFWk0zdZHBgv9daOFG99E */
            private final /* synthetic */ List f$0;

            {
                this.f$0 = r1;
            }

            @Override // ohos.account.AccountProxy.UnmarshallingInterface
            public final boolean unmarshalling(MessageParcel messageParcel) {
                return this.f$0.add(Boolean.valueOf(messageParcel.readBoolean()));
            }
        });
        if (!sendAccountRequest) {
            HiLog.error(LABEL, "sendAccountRequest fail", new Object[0]);
            return sendAccountRequest;
        }
        HiLog.debug(LABEL, "isOsAccountVerified with id end", new Object[0]);
        if (arrayList.isEmpty()) {
            return false;
        }
        return ((Boolean) arrayList.get(0)).booleanValue();
    }

    static /* synthetic */ boolean lambda$isOsAccountVerified$26(int i, MessageParcel messageParcel) {
        if (messageParcel.writeInt(i)) {
            return true;
        }
        HiLog.error(LABEL, "write data fail", new Object[0]);
        return false;
    }

    public boolean isOsAccountKingKong() {
        HiLog.debug(LABEL, "isOsAccountKingKong begin", new Object[0]);
        ArrayList arrayList = new ArrayList();
        boolean sendAccountRequest = sendAccountRequest(125, MARSHALL_STUB_FUNC, new UnmarshallingInterface(arrayList) {
            /* class ohos.account.$$Lambda$AccountProxy$wB43tlRVFM5kHi6_RZoO9OZy9hw */
            private final /* synthetic */ List f$0;

            {
                this.f$0 = r1;
            }

            @Override // ohos.account.AccountProxy.UnmarshallingInterface
            public final boolean unmarshalling(MessageParcel messageParcel) {
                return this.f$0.add(Boolean.valueOf(messageParcel.readBoolean()));
            }
        });
        if (!sendAccountRequest) {
            HiLog.error(LABEL, "send request fail", new Object[0]);
            return sendAccountRequest;
        }
        HiLog.debug(LABEL, "isOsAccountKingKong end", new Object[0]);
        if (arrayList.isEmpty()) {
            return false;
        }
        return ((Boolean) arrayList.get(0)).booleanValue();
    }

    public boolean isOsAccountActive(int i) {
        HiLog.debug(LABEL, "isOsAccountActive id begin", new Object[0]);
        if (i < 0) {
            HiLog.error(LABEL, "local id error", new Object[0]);
            return false;
        }
        ArrayList arrayList = new ArrayList();
        boolean sendAccountRequest = sendAccountRequest(120, new MarshallInterface(i) {
            /* class ohos.account.$$Lambda$AccountProxy$hEMDulapj4cB_mJyWLfdRWQAg2A */
            private final /* synthetic */ int f$0;

            {
                this.f$0 = r1;
            }

            @Override // ohos.account.AccountProxy.MarshallInterface
            public final boolean marshalling(MessageParcel messageParcel) {
                return AccountProxy.lambda$isOsAccountActive$29(this.f$0, messageParcel);
            }
        }, new UnmarshallingInterface(arrayList) {
            /* class ohos.account.$$Lambda$AccountProxy$VLUZYImCor0WTRDMXpuMPGkq5Q4 */
            private final /* synthetic */ List f$0;

            {
                this.f$0 = r1;
            }

            @Override // ohos.account.AccountProxy.UnmarshallingInterface
            public final boolean unmarshalling(MessageParcel messageParcel) {
                return this.f$0.add(Boolean.valueOf(messageParcel.readBoolean()));
            }
        });
        if (!sendAccountRequest) {
            HiLog.error(LABEL, "sendAccountRequest fail", new Object[0]);
            return sendAccountRequest;
        }
        HiLog.debug(LABEL, "isOsAccountActive end", new Object[0]);
        if (arrayList.isEmpty()) {
            return false;
        }
        return ((Boolean) arrayList.get(0)).booleanValue();
    }

    static /* synthetic */ boolean lambda$isOsAccountActive$29(int i, MessageParcel messageParcel) {
        if (messageParcel.writeInt(i)) {
            return true;
        }
        HiLog.error(LABEL, "write data fail", new Object[0]);
        return false;
    }

    public OsAccountType getOsAccountTypeFromProcess() {
        HiLog.debug(LABEL, "get os account type begin", new Object[0]);
        ArrayList arrayList = new ArrayList();
        if (!sendAccountRequest(124, MARSHALL_STUB_FUNC, new UnmarshallingInterface(arrayList) {
            /* class ohos.account.$$Lambda$AccountProxy$IhGADQGQti99oDmva_wwhNuNB2w */
            private final /* synthetic */ List f$0;

            {
                this.f$0 = r1;
            }

            @Override // ohos.account.AccountProxy.UnmarshallingInterface
            public final boolean unmarshalling(MessageParcel messageParcel) {
                return this.f$0.add(Integer.valueOf(messageParcel.readInt()));
            }
        })) {
            HiLog.error(LABEL, "send request fail", new Object[0]);
            return OsAccountType.INVALID;
        }
        HiLog.debug(LABEL, "get os account type end", new Object[0]);
        return arrayList.isEmpty() ? OsAccountType.INVALID : OsAccountType.getType(((Integer) arrayList.get(0)).intValue());
    }

    public PixelMap getOsAccountProfilePhoto(int i) {
        HiLog.debug(LABEL, "getOsAccountProfilePhoto in", new Object[0]);
        return ACCOUNT_ADAPTER.getOsAccountProfilePhoto(i);
    }

    public boolean setOsAccountProfilePhoto(int i, PixelMap pixelMap) {
        HiLog.debug(LABEL, "setOsAccountProfilePhoto in", new Object[0]);
        return ACCOUNT_ADAPTER.setOsAccountProfilePhoto(i, pixelMap);
    }

    public void subscribeOsAccountEvent(IOsAccountSubscriber iOsAccountSubscriber, String str) {
        HiLog.info(LABEL, "subscribeOsAccountEvent", new Object[0]);
        ABILITY_ADAPTER.subscribeOsAccountEvent(iOsAccountSubscriber, str);
    }

    public static AccountProxy getAccountProxy() {
        return ACCOUNT_PROXY_INSTANCE;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setRemoteObject(IRemoteObject iRemoteObject) {
        synchronized (this.remoteLock) {
            getAccountProxy().remoteObj = iRemoteObject;
        }
    }

    /* access modifiers changed from: private */
    public static class AccountProxyDeathRecipient implements IRemoteObject.DeathRecipient {
        private AccountProxyDeathRecipient() {
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            HiLog.warn(AccountProxy.LABEL, "AccountProxyDeathRecipient::onRemoteDied", new Object[0]);
            AccountProxy.getAccountProxy().setRemoteObject(null);
        }
    }
}
