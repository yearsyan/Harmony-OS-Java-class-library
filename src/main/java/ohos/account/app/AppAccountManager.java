package ohos.account.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import ohos.app.Context;
import ohos.data.distributed.common.Entry;
import ohos.data.distributed.common.KvManager;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvManagerFactory;
import ohos.data.distributed.common.KvStoreException;
import ohos.data.distributed.common.KvStoreType;
import ohos.data.distributed.common.Options;
import ohos.data.distributed.user.SingleKvStore;
import ohos.event.commonevent.CommonEventData;
import ohos.event.commonevent.CommonEventManager;
import ohos.event.commonevent.CommonEventSubscribeInfo;
import ohos.event.commonevent.CommonEventSubscriber;
import ohos.event.commonevent.CommonEventSupport;
import ohos.event.commonevent.MatchingSkills;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.sysability.samgr.SysAbilityManager;

public class AppAccountManager implements IRemoteBroker {
    private static final AppAccountManagerAdapter APP_ACCOUNT_ADAPTER = new AppAccountManagerAdapter();
    private static final String DB_NAME = "app_account_data_ex";
    private static final String EMPTY_VALUE = "";
    private static final String INFIX_LINE = "_";
    private static final HiLogLabel LABEL = new HiLogLabel(3, LOG_DOMAIN, TAG);
    private static final int LOG_DOMAIN = 218110720;
    private static final String PREFIX_ASSOCIATED = "AccountAssociated:";
    private static final String PREFIX_EXTRA = "AccountExtra:";
    private static final String PREFIX_SYNC = "AccountSync:";
    private static final String TAG = "AppAccountManager";
    private String accountOwner;
    private final Set<IAppAccountSubscriber> accountsChangedSubscribers;
    private final Map<IAppAccountSubscriber, Set<String>> accountsChangedSubscribersTypes;
    private Context context;
    private KvManager dataManager;
    private final Object eventLock;
    private boolean isKvStoreOpened;
    private SingleKvStore kvStore;
    private final Object remoteLock;
    private IRemoteObject remoteObj;
    private AccountChangeEventSubscriber subscriber;

    @FunctionalInterface
    private interface MarshallInterface {
        boolean marshalling(MessageParcel messageParcel);
    }

    @FunctionalInterface
    private interface UnmarshallingInterface {
        boolean unmarshalling(MessageParcel messageParcel);
    }

    private boolean isInvalidAccountParam(String str) {
        return str == null;
    }

    private AppAccountManager() {
        this.eventLock = new Object();
        this.accountsChangedSubscribers = new HashSet();
        this.accountsChangedSubscribersTypes = new HashMap();
        this.context = null;
        this.isKvStoreOpened = false;
        this.dataManager = null;
        this.kvStore = null;
        this.remoteObj = null;
        this.remoteLock = new Object();
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        synchronized (this.remoteLock) {
            if (this.remoteObj == null) {
                this.remoteObj = SysAbilityManager.getSysAbility(200);
                if (this.remoteObj == null) {
                    HiLog.error(LABEL, "getSysAbility account failed", new Object[0]);
                    return this.remoteObj;
                }
                this.remoteObj.addDeathRecipient(new AppAccountManagerDeathRecipient(), 0);
                HiLog.debug(LABEL, "get remote object completed", new Object[0]);
            }
            return this.remoteObj;
        }
    }

    /* access modifiers changed from: private */
    public static class AppAccountMgrSingleTon {
        private static AppAccountManager singleTon = new AppAccountManager();

        private AppAccountMgrSingleTon() {
        }
    }

    public static AppAccountManager getInstance() {
        return AppAccountMgrSingleTon.singleTon;
    }

    public void setAccountOwner(Context context2) {
        if (context2 != null) {
            this.accountOwner = context2.getBundleName();
            this.context = context2;
            openKvStore();
        }
    }

    private boolean addSubscribeEvent() {
        HiLog.debug(LABEL, "addSubscribeEvent start", new Object[0]);
        MatchingSkills matchingSkills = new MatchingSkills();
        matchingSkills.addEvent(CommonEventSupport.COMMON_EVENT_VISIBLE_ACCOUNTS_UPDATED);
        this.subscriber = new AccountChangeEventSubscriber(new CommonEventSubscribeInfo(matchingSkills));
        try {
            CommonEventManager.subscribeCommonEvent(this.subscriber);
            HiLog.debug(LABEL, "CommonEventManager has subscribed CommonEvent", new Object[0]);
            HiLog.debug(LABEL, "addSubscribeEvent end", new Object[0]);
            return true;
        } catch (RemoteException unused) {
            HiLog.error(LABEL, "subscribeCommonEvent occur exception", new Object[0]);
            return false;
        }
    }

    public boolean subscribeAccountEvent(IAppAccountSubscriber iAppAccountSubscriber, List<String> list) {
        HiLog.debug(LABEL, "subscribe app account event start", new Object[0]);
        if (iAppAccountSubscriber == null || this.accountOwner == null || list == null) {
            HiLog.error(LABEL, "subscribe param null", new Object[0]);
            return false;
        }
        synchronized (this.eventLock) {
            if (this.accountsChangedSubscribers.contains(iAppAccountSubscriber)) {
                HiLog.error(LABEL, "this listener is already added", new Object[0]);
                return false;
            } else if (!this.accountsChangedSubscribers.isEmpty() || addSubscribeEvent()) {
                this.accountsChangedSubscribers.add(iAppAccountSubscriber);
                this.accountsChangedSubscribersTypes.put(iAppAccountSubscriber, new HashSet(list));
                return APP_ACCOUNT_ADAPTER.subscribeAccountEvent(list, this.accountOwner);
            } else {
                HiLog.error(LABEL, "subscribe event fail", new Object[0]);
                return false;
            }
        }
    }

    public boolean unsubscribeAccountEvent(IAppAccountSubscriber iAppAccountSubscriber) {
        HiLog.debug(LABEL, "unsubscribe app account event", new Object[0]);
        if (iAppAccountSubscriber == null) {
            HiLog.error(LABEL, "this event is null", new Object[0]);
            return false;
        }
        synchronized (this.eventLock) {
            if (!this.accountsChangedSubscribers.contains(iAppAccountSubscriber)) {
                HiLog.error(LABEL, "event was not previously added", new Object[0]);
                return false;
            }
            Set<String> set = this.accountsChangedSubscribersTypes.get(iAppAccountSubscriber);
            this.accountsChangedSubscribers.remove(iAppAccountSubscriber);
            this.accountsChangedSubscribersTypes.remove(iAppAccountSubscriber);
            if (this.accountsChangedSubscribers.isEmpty()) {
                try {
                    CommonEventManager.unsubscribeCommonEvent(this.subscriber);
                    HiLog.debug(LABEL, "CommonEventManager has unsubscribed CommonEvent", new Object[0]);
                } catch (RemoteException unused) {
                    HiLog.error(LABEL, "unsubscribeCommonEvent occur exception", new Object[0]);
                    return false;
                }
            }
            return APP_ACCOUNT_ADAPTER.unsubscribeAccountEvent(set, this.accountOwner);
        }
    }

    public List<AppAccount> getAllAccounts(String str) {
        List<AppAccount> allKvAccounts;
        HiLog.debug(LABEL, "get all accounts in", new Object[0]);
        String str2 = this.accountOwner;
        if (str2 == null || str == null) {
            return new ArrayList();
        }
        if (!str2.equals(str) || (allKvAccounts = getAllKvAccounts(str)) == null || allKvAccounts.isEmpty()) {
            return APP_ACCOUNT_ADAPTER.getAllAccounts(str, this.accountOwner);
        }
        return allKvAccounts;
    }

    public boolean addAccount(String str, String str2) {
        HiLog.debug(LABEL, "addAccount in", new Object[0]);
        if (this.accountOwner == null || isInvalidAccountParam(str)) {
            return false;
        }
        if (!getAppAccountSync(str)) {
            return APP_ACCOUNT_ADAPTER.addAccount(str, this.accountOwner, str2);
        }
        return setKvString(PREFIX_EXTRA + str, str2);
    }

    public boolean deleteAccount(String str) {
        HiLog.debug(LABEL, "deleteAccount in", new Object[0]);
        if (this.accountOwner == null || isInvalidAccountParam(str)) {
            return false;
        }
        if (getAppAccountSync(str)) {
            return deleteKvAccount(str);
        }
        return APP_ACCOUNT_ADAPTER.deleteAccount(str, this.accountOwner);
    }

    public List<AppAccount> getAllAccessibleAccounts() {
        HiLog.debug(LABEL, "get all accessible accounts in", new Object[0]);
        String str = this.accountOwner;
        if (str == null) {
            return new ArrayList();
        }
        return APP_ACCOUNT_ADAPTER.getAllAccounts(null, str);
    }

    public boolean setAccountExtraInfo(String str, String str2) {
        HiLog.debug(LABEL, "set account certification in", new Object[0]);
        if (this.accountOwner == null || isInvalidAccountParam(str)) {
            HiLog.error(LABEL, "set account certification error", new Object[0]);
            return false;
        } else if (!getAppAccountSync(str)) {
            return APP_ACCOUNT_ADAPTER.setAccountExtraInfo(str, this.accountOwner, str2);
        } else {
            return setKvString(PREFIX_EXTRA + str, str2);
        }
    }

    public String getAccountExtraInfo(String str) {
        HiLog.debug(LABEL, "get account certification in", new Object[0]);
        if (this.accountOwner == null || isInvalidAccountParam(str)) {
            HiLog.error(LABEL, "get account certification error", new Object[0]);
            return AppAccountConst.INVALID_ACCOUNT_INFO;
        } else if (!getAppAccountSync(str)) {
            return APP_ACCOUNT_ADAPTER.getAccountExtraInfo(str, this.accountOwner);
        } else {
            return getKvString(PREFIX_EXTRA + str);
        }
    }

    public boolean setAccountCredential(String str, String str2, String str3) {
        HiLog.debug(LABEL, "set credential in", new Object[0]);
        if (this.accountOwner != null && !isInvalidAccountParam(str) && str2 != null && !str2.isEmpty() && str3 != null && !str3.isEmpty()) {
            return APP_ACCOUNT_ADAPTER.setAccountCredential(str, this.accountOwner, str2, str3);
        }
        HiLog.error(LABEL, "set credential param error", new Object[0]);
        return false;
    }

    public String getAccountCredential(String str, String str2) {
        HiLog.debug(LABEL, "get credential in", new Object[0]);
        if (this.accountOwner != null && !isInvalidAccountParam(str) && str2 != null && !str2.isEmpty()) {
            return APP_ACCOUNT_ADAPTER.getAccountCredential(str, this.accountOwner, str2);
        }
        HiLog.error(LABEL, "get credential param error", new Object[0]);
        return AppAccountConst.INVALID_ACCOUNT_INFO;
    }

    public boolean setAccountAccess(String str, String str2, boolean z) {
        HiLog.debug(LABEL, "set account access in", new Object[0]);
        if (this.accountOwner != null && !isInvalidAccountParam(str) && str2 != null && !str2.isEmpty()) {
            return APP_ACCOUNT_ADAPTER.setAccountAccess(str, this.accountOwner, str2, z);
        }
        HiLog.error(LABEL, "get account access error", new Object[0]);
        return false;
    }

    public boolean setAssociatedData(String str, String str2, String str3) {
        HiLog.debug(LABEL, "set associated in", new Object[0]);
        if (this.accountOwner == null || isInvalidAccountParam(str) || str2 == null || str2.isEmpty() || str3 == null || str3.isEmpty()) {
            HiLog.error(LABEL, "set associated param error", new Object[0]);
            return false;
        } else if (!getAppAccountSync(str)) {
            return APP_ACCOUNT_ADAPTER.setAssociatedData(str, this.accountOwner, str2, str3);
        } else {
            return setKvString(PREFIX_ASSOCIATED + str + "_" + str2, str3);
        }
    }

    public String getAssociatedData(String str, String str2) {
        HiLog.debug(LABEL, "get associated in", new Object[0]);
        if (this.accountOwner == null || isInvalidAccountParam(str) || str2 == null || str2.isEmpty()) {
            HiLog.error(LABEL, "get associated param error", new Object[0]);
            return AppAccountConst.INVALID_ACCOUNT_INFO;
        } else if (!getAppAccountSync(str)) {
            return APP_ACCOUNT_ADAPTER.getAssociatedData(str, this.accountOwner, str2);
        } else {
            return getKvString(PREFIX_ASSOCIATED + str + "_" + str2);
        }
    }

    private boolean openKvStore() {
        if (this.isKvStoreOpened) {
            HiLog.debug(LABEL, "Kv store is already opened", new Object[0]);
            return true;
        }
        KvManagerConfig kvManagerConfig = new KvManagerConfig(this.context);
        Options options = new Options();
        options.setCreateIfMissing(true).setEncrypt(false).setKvStoreType(KvStoreType.SINGLE_VERSION);
        options.setAutoSync(true);
        try {
            this.dataManager = KvManagerFactory.getInstance().createKvManager(kvManagerConfig);
            if (this.dataManager == null) {
                HiLog.error(LABEL, "Invalid dataManager", new Object[0]);
                return false;
            }
            this.kvStore = (SingleKvStore) this.dataManager.getKvStore(options, DB_NAME);
            if (this.kvStore == null) {
                HiLog.error(LABEL, "Invalid kv store", new Object[0]);
                return false;
            }
            HiLog.info(LABEL, "Open kv store success", new Object[0]);
            this.isKvStoreOpened = true;
            return true;
        } catch (KvStoreException e) {
            HiLog.error(LABEL, "Open kv store failed, error: %{public}s", e.getKvStoreErrorCode().getErrorMsg());
            return false;
        }
    }

    private List<AppAccount> getAllKvAccounts(String str) {
        ArrayList arrayList = new ArrayList();
        if (!openKvStore()) {
            HiLog.error(LABEL, "Open kv store failed", new Object[0]);
            return arrayList;
        }
        try {
            for (Entry entry : this.kvStore.getEntries(PREFIX_SYNC)) {
                if (getKvBoolean(entry.getKey())) {
                    arrayList.add(new AppAccount(entry.getKey().substring(12), str));
                }
            }
            HiLog.debug(LABEL, "Get kv account success", new Object[0]);
        } catch (KvStoreException e) {
            HiLog.error(LABEL, "Get kv account failed, error: %{public}s", e.getKvStoreErrorCode().getErrorMsg());
        }
        return arrayList;
    }

    private String getKvString(String str) {
        String str2 = "";
        if (!openKvStore()) {
            HiLog.error(LABEL, "Open kv store failed", new Object[0]);
            return str2;
        }
        try {
            str2 = this.kvStore.getString(str);
            HiLog.debug(LABEL, "Get kv string success", new Object[0]);
            return str2;
        } catch (KvStoreException e) {
            HiLog.error(LABEL, "Get kv string failed, error: %{public}s", e.getKvStoreErrorCode().getErrorMsg());
            return str2;
        }
    }

    private boolean setKvString(String str, String str2) {
        boolean z;
        KvStoreException e;
        if (!openKvStore()) {
            HiLog.error(LABEL, "Open kv store failed", new Object[0]);
            return false;
        }
        try {
            this.kvStore.putString(str, str2);
            try {
                HiLog.debug(LABEL, "Set kv string success", new Object[0]);
                return true;
            } catch (KvStoreException e2) {
                e = e2;
                z = true;
                HiLog.error(LABEL, "Set kv string failed, error: %{public}s", e.getKvStoreErrorCode().getErrorMsg());
                return z;
            }
        } catch (KvStoreException e3) {
            e = e3;
            z = false;
            HiLog.error(LABEL, "Set kv string failed, error: %{public}s", e.getKvStoreErrorCode().getErrorMsg());
            return z;
        }
    }

    private boolean getKvBoolean(String str) {
        boolean z;
        KvStoreException e;
        if (!openKvStore()) {
            HiLog.error(LABEL, "Open kv store failed", new Object[0]);
            return false;
        }
        try {
            z = this.kvStore.getBoolean(str);
            try {
                HiLog.debug(LABEL, "Get kv boolean success", new Object[0]);
            } catch (KvStoreException e2) {
                e = e2;
            }
        } catch (KvStoreException e3) {
            e = e3;
            z = false;
            HiLog.error(LABEL, "Get kv boolean failed, error: %{public}s", e.getKvStoreErrorCode().getErrorMsg());
            return z;
        }
        return z;
    }

    private boolean setKvBoolean(String str, boolean z) {
        boolean z2;
        KvStoreException e;
        if (!openKvStore()) {
            HiLog.error(LABEL, "Open kv store failed", new Object[0]);
            return false;
        }
        try {
            this.kvStore.putBoolean(str, z);
            try {
                HiLog.debug(LABEL, "Set kv boolean success", new Object[0]);
                return true;
            } catch (KvStoreException e2) {
                e = e2;
                z2 = true;
                HiLog.error(LABEL, "Set kv boolean failed, error: %{public}s", e.getKvStoreErrorCode().getErrorMsg());
                return z2;
            }
        } catch (KvStoreException e3) {
            e = e3;
            z2 = false;
            HiLog.error(LABEL, "Set kv boolean failed, error: %{public}s", e.getKvStoreErrorCode().getErrorMsg());
            return z2;
        }
    }

    private boolean deleteKvAccount(String str) {
        boolean z;
        KvStoreException e;
        if (!openKvStore()) {
            HiLog.error(LABEL, "Open kv store failed", new Object[0]);
            return false;
        }
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.add(PREFIX_SYNC + str);
            arrayList.add(PREFIX_EXTRA + str);
            for (Entry entry : this.kvStore.getEntries(PREFIX_ASSOCIATED + str + "_")) {
                arrayList.add(entry.getKey());
            }
            this.kvStore.deleteBatch(arrayList);
            try {
                HiLog.debug(LABEL, "Delete kv account success", new Object[0]);
                return true;
            } catch (KvStoreException e2) {
                e = e2;
                z = true;
                HiLog.error(LABEL, "Delete kv account failed, error: %{public}s", e.getKvStoreErrorCode().getErrorMsg());
                return z;
            }
        } catch (KvStoreException e3) {
            e = e3;
            z = false;
            HiLog.error(LABEL, "Delete kv account failed, error: %{public}s", e.getKvStoreErrorCode().getErrorMsg());
            return z;
        }
    }

    public boolean setAppAccountSync(String str, boolean z) {
        HiLog.debug(LABEL, "setAppAccountSync in", new Object[0]);
        if (this.accountOwner == null || str == null || str.isEmpty()) {
            HiLog.error(LABEL, "accountOwner or name invalid", new Object[0]);
            return false;
        } else if (!z) {
            return deleteKvAccount(str);
        } else {
            return setKvBoolean(PREFIX_SYNC + str, z);
        }
    }

    public boolean getAppAccountSync(String str) {
        HiLog.debug(LABEL, "getAppAccountSync in", new Object[0]);
        if (this.accountOwner == null || str == null || str.isEmpty()) {
            HiLog.error(LABEL, "accountOwner or name invalid", new Object[0]);
            return false;
        }
        return getKvBoolean(PREFIX_SYNC + str);
    }

    /* access modifiers changed from: private */
    public class AccountChangeEventSubscriber extends CommonEventSubscriber {
        AccountChangeEventSubscriber(CommonEventSubscribeInfo commonEventSubscribeInfo) {
            super(commonEventSubscribeInfo);
        }

        @Override // ohos.event.commonevent.CommonEventSubscriber
        public void onReceiveEvent(CommonEventData commonEventData) {
            HiLog.debug(AppAccountManager.LABEL, "onReceiveEvent in", new Object[0]);
            if (AppAccountManager.this.accountOwner == null || AppAccountManager.this.accountOwner.isEmpty()) {
                HiLog.error(AppAccountManager.LABEL, "can't get calling bundle name", new Object[0]);
                return;
            }
            List<AppAccount> allAccessibleAccounts = AppAccountManager.this.getAllAccessibleAccounts();
            synchronized (AppAccountManager.this.eventLock) {
                for (IAppAccountSubscriber iAppAccountSubscriber : AppAccountManager.this.accountsChangedSubscribers) {
                    HiLog.debug(AppAccountManager.LABEL, "before listener onAccountsChanged", new Object[0]);
                    iAppAccountSubscriber.onAccountsChanged((List) allAccessibleAccounts.stream().filter(new Predicate((Set) AppAccountManager.this.accountsChangedSubscribersTypes.get(iAppAccountSubscriber)) {
                        /* class ohos.account.app.$$Lambda$AppAccountManager$AccountChangeEventSubscriber$fwi4WhJpSAK8uGmTNDaqH_zyErI */
                        private final /* synthetic */ Set f$0;

                        {
                            this.f$0 = r1;
                        }

                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            return this.f$0.contains(((AppAccount) obj).getOwner());
                        }
                    }).collect(Collectors.toList()));
                    HiLog.debug(AppAccountManager.LABEL, "after listener onAccountsChanged", new Object[0]);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setRemoteObject(IRemoteObject iRemoteObject) {
        synchronized (this.remoteLock) {
            getInstance().remoteObj = iRemoteObject;
        }
    }

    private static class AppAccountManagerDeathRecipient implements IRemoteObject.DeathRecipient {
        private AppAccountManagerDeathRecipient() {
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            HiLog.warn(AppAccountManager.LABEL, "AppAccountManagerDeathRecipient::onRemoteDied", new Object[0]);
            AppAccountManager.getInstance().setRemoteObject(null);
        }
    }
}
