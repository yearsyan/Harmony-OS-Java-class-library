package ohos.aafwk.ability;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.os.IDeviceIdentifiersPolicyService;
import android.os.ServiceManager;
import android.os.UserHandle;
import com.android.internal.content.PackageMonitor;
import com.huawei.ohos.interwork.AbilityUtils;
import com.huawei.pgmng.log.LogPower;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import ohos.aafwk.ability.IAbilityFormProvider;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.ComponentProvider;
import ohos.appexecfwk.utils.HiViewUtil;
import ohos.appexecfwk.utils.StringUtils;
import ohos.bundle.BundleInfo;
import ohos.bundle.BundleManager;
import ohos.bundle.BundlePackInfo;
import ohos.bundle.FormInfo;
import ohos.bundle.ModuleInfo;
import ohos.event.commonevent.ActionMapper;
import ohos.event.commonevent.CommonEventSupport;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.miscservices.timeutility.Time;
import ohos.powermanager.PowerManager;
import ohos.rpc.IPCAdapter;
import ohos.rpc.IPCSkeleton;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.system.Parameters;
import ohos.utils.Pair;

public class FormAdapter {
    private static final int CODE_FORM_OFFSET = 8585216;
    private static final int DELETE_TYPE_DELETE_FORM = 3;
    private static final int DELETE_TYPE_RELEASE_CACHED_FORM = 9;
    private static final int DELETE_TYPE_RELEASE_FORM = 8;
    private static final String DEVICE_ID_SERVICE = "device_identifiers";
    private static final int ERR_BIND_SUPPLIER_FAILED = 8585226;
    private static final int ERR_CFG_NOT_MATCH_ID = 8585224;
    private static final int ERR_CODE_COMMON = 8585217;
    private static final int ERR_CODE_OK = 0;
    private static final int ERR_FORM_INVALID_PARAM = 8585223;
    private static final int ERR_MAX_RECORDS_PER_APP = 8585231;
    private static final int ERR_MAX_SYSTEM_FORMS = 8585227;
    private static final int ERR_MAX_SYSTEM_TEMP_FORMS = 8585232;
    private static final int ERR_NOT_EXIST_ID = 8585225;
    private static final int ERR_OPERATION_FORM_NOT_SELF = 8585229;
    private static final int ERR_SUPPLIER_DEL_FAIL = 8585230;
    private static final int FORM_INVISIBLE = 2;
    private static final String FORM_SUFFIX = "ShellServiceForm";
    private static final int FORM_VISIBLE = 1;
    private static final String[] FOUNDATION_PKG_NAME = {"com.huawei.harmonyos.foundation"};
    private static final int IMAGE_DATA_STATE_ADDED = 1;
    private static final long INVALID_UDID_HASH = 0;
    private static final String JS_MESSAGE_KEY = "ohos.extra.param.key.message";
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 218108160, "FormAdapter");
    private static final int MAX_CONFIG_DURATION = 336;
    private static final int MAX_FORMS = 512;
    private static final int MAX_FORM_DATA_SIZE = 1024;
    private static final int MAX_HOUR = 23;
    private static final int MAX_INSTANCE_PER_FORM = 32;
    private static final int MAX_MININUTE = 59;
    private static final long MAX_PERIOD;
    private static final int MAX_RECORD_PER_APP = 256;
    private static final int MAX_TEMP_FORMS = 256;
    private static final int MIN_CONFIG_DURATION = 1;
    private static final long MIN_PERIOD;
    private static final int MIN_TIME = 0;
    private static final String PARAM_FORM_CUSTOMIZE_KEY = "ohos.extra.param.key.form_customize";
    private static final String PARAM_KEY_INSTALL_ON_DEMAND = "ohos.extra.param.key.INSTALL_ON_DEMAND";
    private static final String PARAM_KEY_INSTALL_WITH_BACKGROUND = "ohos.extra.param.key.INSTALL_WITH_BACKGROUND";
    private static final String PG_BIND_SERVICE = "bindservice";
    private static final String PG_DEFAULT_PID = "0";
    private static final String RECREATE_FORM_KEY = "ohos.extra.param.key.recreate";
    private static final String SCHEME_PACKAGE = "package";
    static final String SYSTEM_PARAM_FORM_UPDATE_TIME = "persist.sys.fms.form.update.time";
    private static final int TIMER_CONFIG_UNIT = 30;
    private static final long TIME_CONVERSION = ((((long) Parameters.getInt(SYSTEM_PARAM_FORM_UPDATE_TIME, 30)) * 60) * 1000);
    private static final String TIME_DELIMETER = ":";
    private static final String TYPE_JAVA = "Java";
    private static final String TYPE_JS = "JS";
    private static final String UDID_EXCEPTION = "AndroidRuntimeException";
    private static final String UNKNOWN = "unknown";
    private static final int UPDATE_AT_CONFIG_COUNT = 2;
    private static Context aContext = null;
    private static IDeviceIdentifiersPolicyService deviceSvc = null;
    private static long udidHash = 0;
    private final Object BINDER_LOCK;
    private final Object FORM_LOCK;
    private HashMap<String, BinderInfo> binderRecords;
    private BundleDataReceiver bundleDataReceiver;
    private BundleMonitor bundleMonitor;
    private List<FormHostRecord> clientRecords;
    private EventHandler dbEventHandler;
    private EventHandler eventHandler;
    private boolean formDBInfoInited;
    private HashMap<Long, FormDBRecord> formDBRecords;
    private HashMap<Long, FormRecord> formRecords;
    private PowerManager powerManager;
    private SupplierReceiver supplierReceiver;
    private List<Long> tempForms;
    private UserReceiver userReceiver;

    /* access modifiers changed from: package-private */
    public enum UpdateType {
        TYPE_INTERVAL_CHANGE,
        TYPE_ATTIME_CHANGE,
        TYPE_INTERVAL_TO_ATTIME,
        TYPE_ATTIME_TO_INTERVAL
    }

    private boolean isDeleteValid(long j, IRemoteObject iRemoteObject, int i) {
        if (j <= 0 || iRemoteObject == null) {
            return false;
        }
        return i == 3 || i == 8 || i == 9;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native boolean nativeBatchDeleteForms(HashSet<Long> hashSet);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native boolean nativeDeleteFormInfo(long j);

    private native ArrayList<FormDBRecord> nativeGetAllFormInfo();

    private native FormResource nativeGetFormResource(FormIdKey formIdKey);

    private native void nativeInit();

    private native boolean nativeQueryFormInfo(long j);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native boolean nativeSaveFormInfo(FormDBRecord formDBRecord);

    static {
        long j = TIME_CONVERSION;
        MIN_PERIOD = 1 * j;
        MAX_PERIOD = j * 336;
        try {
            HiLog.info(LABEL_LOG, "Load form mgr jni so", new Object[0]);
            System.loadLibrary("formmgr_jni.z");
        } catch (UnsatisfiedLinkError unused) {
            HiLog.warn(LABEL_LOG, "ERROR: Could not load formmgr_jni.z.so ", new Object[0]);
        }
    }

    /* access modifiers changed from: private */
    public static class Holder {
        private static final FormAdapter INSTANCE = new FormAdapter();

        private Holder() {
        }
    }

    public static FormAdapter getInstance() {
        return Holder.INSTANCE;
    }

    private FormAdapter() {
        this.FORM_LOCK = new Object();
        this.BINDER_LOCK = new Object();
        this.formDBInfoInited = false;
        this.formDBRecords = new HashMap<>();
        this.formRecords = new HashMap<>();
        this.clientRecords = new ArrayList();
        this.powerManager = new PowerManager();
        this.bundleMonitor = null;
        this.supplierReceiver = null;
        this.userReceiver = null;
        this.bundleDataReceiver = null;
        this.eventHandler = null;
        this.dbEventHandler = null;
        this.tempForms = new ArrayList();
        this.binderRecords = new HashMap<>();
    }

    public void init(Context context) {
        HiLog.info(LABEL_LOG, "FormAdapter init begin", new Object[0]);
        if (context == null) {
            HiLog.error(LABEL_LOG, "init failed, context is null", new Object[0]);
            return;
        }
        aContext = context;
        initReceiver(context);
        if (this.eventHandler == null) {
            this.eventHandler = new EventHandler(EventRunner.create("form_adapter"));
        }
        if (this.dbEventHandler == null) {
            this.dbEventHandler = new EventHandler(EventRunner.create("form_adapter_db"));
        }
        generateUdidHash();
        nativeInit();
    }

    public int addForm(FormItemInfo formItemInfo, long j, long j2) {
        if (formItemInfo == null || !formItemInfo.isValidItem()) {
            HiLog.error(LABEL_LOG, "input param itemInfo is invalid", new Object[0]);
            return ERR_FORM_INVALID_PARAM;
        }
        HiLog.info(LABEL_LOG, "addForm begin here,bundle:%{public}s,abilityName:%{public}s,formName:%{public}s", formItemInfo.getBundleName(), formItemInfo.getAbilityName(), formItemInfo.getFormName());
        MessageParcel create = MessageParcel.create(j);
        MessageParcel create2 = MessageParcel.create(j2);
        try {
            IRemoteObject readRemoteObject = create.readRemoteObject();
            IntentParams intentParams = new IntentParams();
            create.readSequenceable(intentParams);
            intentParams.getParams();
            if (!isAddValid(formItemInfo, readRemoteObject)) {
                HiLog.error(LABEL_LOG, "addForm invalid param", new Object[0]);
                create2.writeInt(ERR_FORM_INVALID_PARAM);
                return ERR_FORM_INVALID_PARAM;
            }
            int handleAddForm = handleAddForm(formItemInfo, readRemoteObject, create2, intentParams);
            create.reclaim();
            create2.reclaim();
            return handleAddForm;
        } finally {
            create.reclaim();
            create2.reclaim();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x009c, code lost:
        if (r8 == false) goto L_0x00a5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x009e, code lost:
        ohos.aafwk.ability.FormTimerManager.getInstance().deleteFormTimer(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00a5, code lost:
        return 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int deleteForm(long r15, int r17, long r18) {
        /*
        // Method dump skipped, instructions count: 169
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.FormAdapter.deleteForm(long, int, long):int");
    }

    public int updateForm(long j, String str, long j2) {
        ComponentProvider componentProvider;
        FormBindingData formBindingData;
        HiLog.info(LABEL_LOG, "updateForm begin here, formId:%{public}d", Long.valueOf(j));
        if (!isUpdateValid(j, str)) {
            HiLog.error(LABEL_LOG, "update form formId or bundleName is invalid", new Object[0]);
            return ERR_FORM_INVALID_PARAM;
        }
        long findMatchedFormId = findMatchedFormId(j);
        MessageParcel create = MessageParcel.create(j2);
        try {
            boolean readBoolean = create.readBoolean();
            int readableBytes = create.getReadableBytes();
            if (readBoolean) {
                FormBindingData formBindingData2 = new FormBindingData();
                if (!create.readSequenceable(formBindingData2)) {
                    HiLog.error(LABEL_LOG, "update form bindingData is invalid", new Object[0]);
                    return ERR_FORM_INVALID_PARAM;
                }
                componentProvider = null;
                formBindingData = formBindingData2;
            } else {
                ComponentProvider componentProvider2 = new ComponentProvider();
                if (!create.readSequenceable(componentProvider2)) {
                    HiLog.error(LABEL_LOG, "updateform remoteComponent is invalid", new Object[0]);
                    create.reclaim();
                    return ERR_FORM_INVALID_PARAM;
                }
                formBindingData = null;
                componentProvider = componentProvider2;
            }
            create.reclaim();
            synchronized (this.FORM_LOCK) {
                FormRecord formRecord = this.formRecords.get(Long.valueOf(findMatchedFormId));
                if (formRecord == null) {
                    HiLog.error(LABEL_LOG, "updateform, not exist such form:%{public}d", Long.valueOf(findMatchedFormId));
                    return ERR_NOT_EXIST_ID;
                } else if (!isFormOperationUnderCurrentUserLocked(findMatchedFormId)) {
                    HiLog.error(LABEL_LOG, "updateForm not under current user, formId:%{public}d", Long.valueOf(findMatchedFormId));
                    return ERR_NOT_EXIST_ID;
                } else if (!str.equals(formRecord.bundleName)) {
                    HiLog.error(LABEL_LOG, "updateform, not match bundleName:%{public}s", str);
                    return ERR_FORM_INVALID_PARAM;
                } else {
                    return updateFormCommonProcessLocked(findMatchedFormId, readBoolean, formBindingData, componentProvider, readableBytes);
                }
            }
        } finally {
            create.reclaim();
        }
    }

    private int updateFormCommonProcessLocked(long j, boolean z, FormBindingData formBindingData, ComponentProvider componentProvider, int i) {
        FormRecord formRecord = this.formRecords.get(Long.valueOf(j));
        if (formRecord == null) {
            HiLog.error(LABEL_LOG, "updateform, not exist such form:%{public}d", Long.valueOf(j));
            return ERR_FORM_INVALID_PARAM;
        } else if (formRecord.isJsForm != z) {
            HiLog.error(LABEL_LOG, "updateform, not match js form flag:%{public}d", Boolean.valueOf(z));
            return ERR_FORM_INVALID_PARAM;
        } else {
            if (!z) {
                formRecord.formView = componentProvider;
            } else if (formRecord.versionUpgrade) {
                formRecord.instantProvider.setFormBindingData(formBindingData);
                formRecord.instantProvider.setUpgradeFlag(true);
            } else {
                formRecord.instantProvider.mergeFormBindingData(formBindingData);
            }
            formRecord.isInited = true;
            formRecord.needRefresh = false;
            if (formRecord.needFreeInstall) {
                formRecord.needFreeInstall = false;
            }
            for (FormHostRecord formHostRecord : this.clientRecords) {
                if (formHostRecord.contains(j)) {
                    formHostRecord.setNeedRefresh(j, true);
                }
            }
            doUpdateFormCommonProcessLocked(j, formRecord, z, i);
            return 0;
        }
    }

    private void doUpdateFormCommonProcessLocked(long j, FormRecord formRecord, boolean z, int i) {
        HiLog.debug(LABEL_LOG, "the form number is %{private}d when update, the formHost number is %{private}d when update.", Integer.valueOf(this.formRecords.size()), Integer.valueOf(this.clientRecords.size()));
        if (!isScreenOn()) {
            HiLog.debug(LABEL_LOG, "screen off, do not initiative refresh", new Object[0]);
        } else {
            for (FormHostRecord formHostRecord : this.clientRecords) {
                if (formHostRecord.isEnableRefresh(j)) {
                    formHostRecord.onUpdate(j, formRecord);
                    formHostRecord.setNeedRefresh(j, false);
                    formRecord.versionUpgrade = false;
                } else {
                    HiLog.debug(LABEL_LOG, "enableRefresh is false, not need to update, formId:%{public}d", Long.valueOf(j));
                }
            }
        }
        if (z) {
            if (!formRecord.versionUpgrade) {
                formRecord.instantProvider.setUpgradeFlag(false);
            }
            FormBindingData formBindingData = formRecord.instantProvider.getFormBindingData();
            if (i > 1024 || (formBindingData != null && formBindingData.getImageDataState() == 1)) {
                HiLog.info(LABEL_LOG, "updateJsForm, data is over 1k, do not cache data", new Object[0]);
                formRecord.instantProvider.resetFormBindingData();
            }
        } else if (i > 1024) {
            HiLog.info(LABEL_LOG, "updateJavaForm, data is over 1k, do not cache data", new Object[0]);
            formRecord.formView = null;
        }
    }

    public int enableUpdateForm(List<Long> list, long j) {
        HiLog.info(LABEL_LOG, "enableUpdateForm", new Object[0]);
        return handleUpdateFormFlag(list, j, true);
    }

    public int disableUpdateForm(List<Long> list, long j) {
        HiLog.info(LABEL_LOG, "disableUpdateForm", new Object[0]);
        return handleUpdateFormFlag(list, j, false);
    }

    public int requestForm(long j, long j2) {
        HiLog.info(LABEL_LOG, "requestForm", new Object[0]);
        MessageParcel create = MessageParcel.create(j2);
        Intent intent = new Intent();
        try {
            if (!create.readSequenceable(intent)) {
                HiLog.error(LABEL_LOG, "requestForm read intent failed", new Object[0]);
                return ERR_FORM_INVALID_PARAM;
            }
            IRemoteObject readRemoteObject = create.readRemoteObject();
            if (j <= 0 || readRemoteObject == null) {
                HiLog.error(LABEL_LOG, "requestForm invalid param", new Object[0]);
                create.reclaim();
                return ERR_FORM_INVALID_PARAM;
            }
            create.reclaim();
            long findMatchedFormId = findMatchedFormId(j);
            synchronized (this.FORM_LOCK) {
                if (!this.formRecords.containsKey(Long.valueOf(findMatchedFormId))) {
                    HiLog.error(LABEL_LOG, "requestForm not exist such form:%{public}d", Long.valueOf(findMatchedFormId));
                    return ERR_NOT_EXIST_ID;
                }
                for (FormHostRecord formHostRecord : this.clientRecords) {
                    if (readRemoteObject.equals(formHostRecord.clientStub)) {
                        if (!formHostRecord.contains(findMatchedFormId)) {
                            HiLog.error(LABEL_LOG, "requestForm form is not self-owned", new Object[0]);
                            return ERR_OPERATION_FORM_NOT_SELF;
                        }
                        HiLog.debug(LABEL_LOG, "requestForm find target client", new Object[0]);
                        return getInstance().refreshForm(findMatchedFormId, intent);
                    }
                }
                HiLog.debug(LABEL_LOG, "requestForm cannot find target client", new Object[0]);
                return ERR_FORM_INVALID_PARAM;
            }
        } finally {
            create.reclaim();
        }
    }

    public int castTempForm(long j, long j2) {
        HiLog.info(LABEL_LOG, "castTempForm begin here, formId:%{public}d", Long.valueOf(j));
        MessageParcel create = MessageParcel.create(j2);
        IRemoteObject readRemoteObject = create.readRemoteObject();
        create.reclaim();
        if (j <= 0 || readRemoteObject == null) {
            HiLog.error(LABEL_LOG, "castTempForm invalid param", new Object[0]);
            return ERR_FORM_INVALID_PARAM;
        }
        long findMatchedFormId = findMatchedFormId(j);
        int callingUid = IPCSkeleton.getCallingUid();
        synchronized (this.FORM_LOCK) {
            if (this.formRecords.containsKey(Long.valueOf(findMatchedFormId))) {
                if (this.tempForms.contains(Long.valueOf(findMatchedFormId))) {
                    FormHostRecord matchedHostClientLocked = getMatchedHostClientLocked(readRemoteObject);
                    if (matchedHostClientLocked != null) {
                        if (matchedHostClientLocked.contains(findMatchedFormId)) {
                            int checkEnoughForm = checkEnoughForm(callingUid);
                            if (checkEnoughForm != 0) {
                                HiLog.error(LABEL_LOG, "cast temp form:%{public}d faild,because if too mush forms", Long.valueOf(findMatchedFormId));
                                return checkEnoughForm;
                            }
                            FormRecord formRecord = this.formRecords.get(Long.valueOf(findMatchedFormId));
                            int handleCastTempForm = handleCastTempForm(findMatchedFormId, formRecord);
                            if (handleCastTempForm != 0) {
                                HiLog.error(LABEL_LOG, "cast temp form bindSupplier failed", new Object[0]);
                                return handleCastTempForm;
                            }
                            this.tempForms.remove(Long.valueOf(findMatchedFormId));
                            formRecord.tempFormFlag = false;
                            formRecord.formUserUids.add(Integer.valueOf(callingUid));
                            savePersistentInfo(new FormDBRecord(findMatchedFormId, formRecord.userId, formRecord.bundleName, formRecord.moduleName, formRecord.originalBundleName, formRecord.abilityName, formRecord.formUserUids));
                            return 0;
                        }
                    }
                    return ERR_OPERATION_FORM_NOT_SELF;
                }
            }
            HiLog.error(LABEL_LOG, "castTempForm not exist such temp form:%{public}d", Long.valueOf(findMatchedFormId));
            return ERR_NOT_EXIST_ID;
        }
    }

    public int eventNotify(List<Long> list, int i, long j) {
        HiLog.info(LABEL_LOG, "eventNotify begin here, event type is %{public}d", Integer.valueOf(i));
        if (i != 1 && i != 2) {
            HiLog.error(LABEL_LOG, "eventNotify type error", new Object[0]);
            return ERR_FORM_INVALID_PARAM;
        } else if (list == null || list.isEmpty()) {
            HiLog.error(LABEL_LOG, "eventNotify formIds is empty", new Object[0]);
            return ERR_FORM_INVALID_PARAM;
        } else {
            MessageParcel create = MessageParcel.create(j);
            IRemoteObject readRemoteObject = create.readRemoteObject();
            create.reclaim();
            if (readRemoteObject == null) {
                HiLog.error(LABEL_LOG, "eventNotify callback is null", new Object[0]);
                return ERR_FORM_INVALID_PARAM;
            }
            HashMap hashMap = new HashMap();
            synchronized (this.FORM_LOCK) {
                for (Long l : list) {
                    long findMatchedFormId = findMatchedFormId(l.longValue());
                    FormRecord formRecord = this.formRecords.get(Long.valueOf(findMatchedFormId));
                    if (formRecord == null) {
                        HiLog.warn(LABEL_LOG, "formRecord is null, formId %{public}d", Long.valueOf(findMatchedFormId));
                    } else if (!formRecord.formVisibleNotify) {
                        HiLog.warn(LABEL_LOG, "formVisibleNotify is false, formId %{public}d", Long.valueOf(findMatchedFormId));
                    } else {
                        FormHostRecord matchedHostClientLocked = getMatchedHostClientLocked(readRemoteObject);
                        if (matchedHostClientLocked != null) {
                            if (matchedHostClientLocked.contains(findMatchedFormId)) {
                                formRecord.formVisibleNotifyState = i;
                                String str = formRecord.originalBundleName + "::" + formRecord.abilityName;
                                List list2 = (List) hashMap.get(str);
                                if (list2 == null) {
                                    list2 = new ArrayList();
                                }
                                list2.add(Long.valueOf(findMatchedFormId));
                                hashMap.put(str, list2);
                            }
                        }
                        HiLog.warn(LABEL_LOG, "form is not belong to self, formId %{public}d ", Long.valueOf(findMatchedFormId));
                    }
                }
            }
            for (Map.Entry entry : hashMap.entrySet()) {
                if (handleEventNotify((List) entry.getValue(), (String) entry.getKey()) != 0) {
                    HiLog.warn(LABEL_LOG, "handleEventNotify error, key is %{public}s", entry.getKey());
                }
            }
            return 0;
        }
    }

    public int checkAndDeleteInvalidForms(List<Long> list, long j, long j2) {
        HiLog.info(LABEL_LOG, "CheckAndDeleteInvalidForms", new Object[0]);
        MessageParcel create = MessageParcel.create(j);
        MessageParcel create2 = MessageParcel.create(j2);
        try {
            IRemoteObject readRemoteObject = create.readRemoteObject();
            if (list != null) {
                if (readRemoteObject != null) {
                    synchronized (this.FORM_LOCK) {
                        ensureFormIdSetInitialed();
                        HashSet hashSet = new HashSet();
                        for (Long l : list) {
                            hashSet.add(Long.valueOf(findMatchedFormId(l.longValue())));
                        }
                        int checkProviderFormsValidLocked = checkProviderFormsValidLocked(IPCSkeleton.getCallingUid(), hashSet);
                        create2.writeInt(0);
                        create2.writeInt(checkProviderFormsValidLocked);
                        HiLog.info(LABEL_LOG, "CheckAndDeleteInvalidForms inValidFormIdsNum:%{public}d", Integer.valueOf(checkProviderFormsValidLocked));
                    }
                    create.reclaim();
                    create2.reclaim();
                    return 0;
                }
            }
            HiLog.error(LABEL_LOG, "CheckAndDeleteInvalidForms invalid param", new Object[0]);
            create2.writeInt(ERR_FORM_INVALID_PARAM);
            return ERR_FORM_INVALID_PARAM;
        } finally {
            create.reclaim();
            create2.reclaim();
        }
    }

    private int checkProviderFormsValidLocked(int i, Set<Long> set) {
        HiLog.debug(LABEL_LOG, "checkProviderFormsValidLocked uid=%{public}d", Integer.valueOf(i));
        HashMap hashMap = new HashMap();
        int userId = UserHandle.getUserId(i);
        if (this.formDBRecords.size() > 0) {
            checkFormDBRecordsValidLocked(userId, i, set, hashMap);
        }
        checkTempFormRecordsValidLocked(userId, i, set, hashMap);
        if (hashMap.isEmpty()) {
            HiLog.debug(LABEL_LOG, "checkProviderFormsValidLocked foundFormsMap empty.", new Object[0]);
            return 0;
        }
        Iterator<FormHostRecord> it = this.clientRecords.iterator();
        while (it.hasNext()) {
            FormHostRecord next = it.next();
            if (next.myUid == i) {
                for (Long l : hashMap.keySet()) {
                    long longValue = l.longValue();
                    if (next.contains(longValue)) {
                        next.delForm(longValue);
                    }
                }
                if (next.isEmpty()) {
                    next.cleanResource();
                    it.remove();
                }
            }
        }
        deleteFormsTimer(hashMap);
        return hashMap.size();
    }

    private void checkFormDBRecordsValidLocked(int i, int i2, Set<Long> set, Map<Long, Boolean> map) {
        HashMap hashMap = new HashMap();
        for (Map.Entry<Long, FormDBRecord> entry : this.formDBRecords.entrySet()) {
            FormDBRecord value = entry.getValue();
            if (value.userId == i && value.formUserUids != null && value.formUserUids.contains(Integer.valueOf(i2))) {
                long longValue = entry.getKey().longValue();
                if (!set.contains(Long.valueOf(longValue))) {
                    checkEachDBRecord(i2, longValue, value, map, hashMap);
                }
            }
        }
        HiLog.debug(LABEL_LOG, "checkFormDBRecordsValidLocked noHostDBFormsMap size:%{public}d", Integer.valueOf(hashMap.size()));
        if (hashMap.size() > 0) {
            batchDeleteNoHostDBForms(i, i2, hashMap, map);
        }
    }

    private void checkEachDBRecord(int i, long j, FormDBRecord formDBRecord, Map<Long, Boolean> map, Map<FormIdKey, HashSet<Long>> map2) {
        formDBRecord.formUserUids.remove(Integer.valueOf(i));
        if (formDBRecord.formUserUids.isEmpty()) {
            FormIdKey formIdKey = new FormIdKey();
            formIdKey.bundleName = formDBRecord.originalBundleName;
            formIdKey.abilityName = formDBRecord.abilityName;
            HashSet<Long> hashSet = map2.get(formIdKey);
            if (hashSet == null) {
                hashSet = new HashSet<>();
                map2.put(formIdKey, hashSet);
            }
            hashSet.add(Long.valueOf(j));
            return;
        }
        map.put(Long.valueOf(j), false);
        savePersistentInfo(formDBRecord);
        FormRecord formRecord = this.formRecords.get(Long.valueOf(j));
        if (formRecord != null) {
            formRecord.formUserUids.remove(Integer.valueOf(i));
        }
    }

    private void batchDeleteNoHostDBForms(int i, int i2, Map<FormIdKey, HashSet<Long>> map, Map<Long, Boolean> map2) {
        HashSet<Long> hashSet = new HashSet<>();
        HashSet<FormIdKey> hashSet2 = new HashSet();
        for (Map.Entry<FormIdKey, HashSet<Long>> entry : map.entrySet()) {
            HashSet<Long> value = entry.getValue();
            FormIdKey key = entry.getKey();
            String str = key.bundleName;
            String str2 = key.abilityName;
            if (notifyProviderFormsBatchDelete(str, str2, value) != 0) {
                HiLog.error(LABEL_LOG, "batchDeleteNoHostDBForms: notifyProviderFormsBatchDelete failed! bundleName:%{public}s, abilityName:%{public}s", str, str2);
                Iterator<Long> it = value.iterator();
                while (it.hasNext()) {
                    FormDBRecord formDBRecord = this.formDBRecords.get(Long.valueOf(it.next().longValue()));
                    if (formDBRecord != null) {
                        formDBRecord.formUserUids.add(Integer.valueOf(i2));
                    }
                }
            } else {
                Iterator<Long> it2 = value.iterator();
                while (it2.hasNext()) {
                    long longValue = it2.next().longValue();
                    map2.put(Long.valueOf(longValue), true);
                    FormDBRecord formDBRecord2 = this.formDBRecords.get(Long.valueOf(longValue));
                    if (formDBRecord2 != null) {
                        FormIdKey formIdKey = new FormIdKey();
                        formIdKey.bundleName = formDBRecord2.bundleName;
                        formIdKey.moduleName = formDBRecord2.moduleName;
                        hashSet2.add(formIdKey);
                        this.formDBRecords.remove(Long.valueOf(longValue));
                    }
                    this.formRecords.remove(Long.valueOf(longValue));
                }
                hashSet.addAll(value);
            }
        }
        handleNativeBatchDeleteFormDBInfos(hashSet);
        for (FormIdKey formIdKey2 : hashSet2) {
            notifyModuleRemovable(formIdKey2.bundleName, formIdKey2.moduleName, i);
        }
    }

    private void checkTempFormRecordsValidLocked(int i, int i2, Set<Long> set, Map<Long, Boolean> map) {
        HashMap hashMap = new HashMap();
        for (Map.Entry<Long, FormRecord> entry : this.formRecords.entrySet()) {
            FormRecord value = entry.getValue();
            if (value.userId == i && value.tempFormFlag && value.formUserUids.contains(Integer.valueOf(i2))) {
                long longValue = entry.getKey().longValue();
                if (!set.contains(Long.valueOf(longValue))) {
                    checkEachTempFormRecord(i2, longValue, value, map, hashMap);
                }
            }
        }
        HiLog.debug(LABEL_LOG, "checkTempFormRecordsValidLocked noHostTempFormsMap size:%{public}d", Integer.valueOf(hashMap.size()));
        if (hashMap.size() > 0) {
            batchDeleteNoHostTempForms(i2, hashMap, map);
        }
    }

    private void checkEachTempFormRecord(int i, long j, FormRecord formRecord, Map<Long, Boolean> map, Map<FormIdKey, HashSet<Long>> map2) {
        formRecord.formUserUids.remove(Integer.valueOf(i));
        if (formRecord.formUserUids.isEmpty()) {
            FormIdKey formIdKey = new FormIdKey();
            formIdKey.bundleName = formRecord.originalBundleName;
            formIdKey.abilityName = formRecord.abilityName;
            HashSet<Long> hashSet = map2.get(formIdKey);
            if (hashSet == null) {
                hashSet = new HashSet<>();
                map2.put(formIdKey, hashSet);
            }
            hashSet.add(Long.valueOf(j));
            return;
        }
        map.put(Long.valueOf(j), false);
    }

    private void batchDeleteNoHostTempForms(int i, Map<FormIdKey, HashSet<Long>> map, Map<Long, Boolean> map2) {
        for (Map.Entry<FormIdKey, HashSet<Long>> entry : map.entrySet()) {
            HashSet<Long> value = entry.getValue();
            FormIdKey key = entry.getKey();
            String str = key.bundleName;
            String str2 = key.abilityName;
            if (notifyProviderFormsBatchDelete(str, str2, value) != 0) {
                HiLog.error(LABEL_LOG, "batchDeleteNoHostTempForms: notifyProviderFormsBatchDelete failed! bundleName:%{public}s, abilityName:%{public}s", str, str2);
                Iterator<Long> it = value.iterator();
                while (it.hasNext()) {
                    FormRecord formRecord = this.formRecords.get(Long.valueOf(it.next().longValue()));
                    if (formRecord != null) {
                        formRecord.formUserUids.add(Integer.valueOf(i));
                    }
                }
            } else {
                Iterator<Long> it2 = value.iterator();
                while (it2.hasNext()) {
                    long longValue = it2.next().longValue();
                    map2.put(Long.valueOf(longValue), true);
                    this.formRecords.remove(Long.valueOf(longValue));
                    this.tempForms.remove(Long.valueOf(longValue));
                }
            }
        }
    }

    private void deleteFormsTimer(Map<Long, Boolean> map) {
        for (Map.Entry<Long, Boolean> entry : map.entrySet()) {
            if (entry.getValue().booleanValue()) {
                FormTimerManager.getInstance().deleteFormTimer(entry.getKey().longValue());
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0063, code lost:
        r4 = new ohos.aafwk.ability.FormAdapter.FreeInstallConnection(null);
        r7 = new android.content.Intent();
        r7.setComponent(new android.content.ComponentName(r5, r6));
        r7.setFlags(32);
        r7.putExtra(ohos.aafwk.ability.FormAdapter.PARAM_KEY_INSTALL_ON_DEMAND, true);
        r7.putExtra(ohos.aafwk.ability.FormAdapter.PARAM_KEY_INSTALL_WITH_BACKGROUND, true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x008e, code lost:
        if (com.huawei.ohos.interwork.AbilityUtils.connectAbility(ohos.aafwk.ability.FormAdapter.aContext, r7, r4) != false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0090, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.aafwk.ability.FormAdapter.LABEL_LOG, "freeInstallForm connectAbility failed", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x009a, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.aafwk.ability.FormAdapter.LABEL_LOG, "freeInstallForm connectAbility  error", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void freeInstallForm(java.lang.String r5, java.lang.String r6, long r7) {
        /*
        // Method dump skipped, instructions count: 172
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.FormAdapter.freeInstallForm(java.lang.String, java.lang.String, long):void");
    }

    private int handleCastTempForm(long j, FormRecord formRecord) {
        HiLog.debug(LABEL_LOG, "cast temp form to normal form, notify supplier, package:%{public}s, class:%{public}s", formRecord.originalBundleName, formRecord.abilityName);
        String str = formRecord.originalBundleName;
        ComponentName componentName = new ComponentName(str, formRecord.abilityName + FORM_SUFFIX);
        CastTempConnection castTempConnection = new CastTempConnection(j, componentName);
        android.content.Intent intent = new android.content.Intent();
        intent.addFlags(32);
        if (formRecord.needFreeInstall) {
            intent.setComponent(new ComponentName(formRecord.bundleName, formRecord.abilityName));
            intent.putExtra(PARAM_KEY_INSTALL_ON_DEMAND, true);
            intent.putExtra(PARAM_KEY_INSTALL_WITH_BACKGROUND, true);
            castTempConnection.isFreeInstall = true;
            castTempConnection.bundleName = formRecord.bundleName;
            try {
                if (!AbilityUtils.connectAbility(aContext, intent, castTempConnection)) {
                    return ERR_SUPPLIER_DEL_FAIL;
                }
                return 0;
            } catch (IllegalArgumentException | IllegalStateException | SecurityException unused) {
                HiLog.error(LABEL_LOG, "handleCastTempForm connectAbility bind error", new Object[0]);
                return ERR_SUPPLIER_DEL_FAIL;
            }
        } else {
            BinderInfo binder = getBinder(componentName, castTempConnection.userId);
            if (binder != null) {
                postTask(new AsyncRunnable(castTempConnection, componentName, binder.binder));
                return 0;
            }
            intent.setComponent(componentName);
            return bindService(intent, castTempConnection);
        }
    }

    private int handleEventNotify(List<Long> list, String str) {
        HiLog.debug(LABEL_LOG, "handle event notify come, intentInfo:%{public}s", str);
        String str2 = str.split("::")[0];
        ComponentName componentName = new ComponentName(str2, str.split("::")[1] + FORM_SUFFIX);
        EventNotifyConnection eventNotifyConnection = new EventNotifyConnection(list, componentName);
        BinderInfo binder = getBinder(componentName, eventNotifyConnection.userId);
        if (binder != null) {
            postTask(new AsyncRunnable(eventNotifyConnection, componentName, binder.binder));
            return 0;
        }
        android.content.Intent intent = new android.content.Intent();
        intent.setComponent(componentName);
        intent.setFlags(32);
        return bindService(intent, eventNotifyConnection);
    }

    /* access modifiers changed from: package-private */
    public Context getContext() {
        return aContext;
    }

    /* access modifiers changed from: package-private */
    public boolean isScreenOn() {
        return this.powerManager.isScreenOn();
    }

    /* access modifiers changed from: package-private */
    public void postTask(Runnable runnable) {
        EventHandler eventHandler2 = this.eventHandler;
        if (eventHandler2 != null) {
            eventHandler2.postTask(runnable);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean postBinderTask(FormRecord formRecord, BaseConnection baseConnection) {
        String str = formRecord.originalBundleName;
        ComponentName componentName = new ComponentName(str, formRecord.abilityName + FORM_SUFFIX);
        BinderInfo binder = getBinder(componentName, baseConnection.userId);
        if (binder == null) {
            return false;
        }
        postTask(new AsyncRunnable(baseConnection, componentName, binder.binder));
        return true;
    }

    /* access modifiers changed from: package-private */
    public int putBinder(ComponentName componentName, IBinder iBinder, BaseConnection baseConnection) {
        if (!ServiceNotResponseHandle.getInstance().removeRequest(baseConnection)) {
            return -1;
        }
        String str = componentName.getPackageName() + "::" + componentName.getClassName() + "::" + baseConnection.userId;
        synchronized (this.BINDER_LOCK) {
            if (this.binderRecords.get(str) != null) {
                HiLog.debug(LABEL_LOG, "putBinder come, binder is not null", new Object[0]);
                return -1;
            }
            BinderInfo binderInfo = new BinderInfo();
            binderInfo.connection = baseConnection;
            binderInfo.binder = iBinder;
            this.binderRecords.put(str, binderInfo);
            HiLog.debug(LABEL_LOG, "putBinder come, supply is %{public}s, count is 1", str);
            return 0;
        }
    }

    /* access modifiers changed from: package-private */
    public BinderInfo getBinder(ComponentName componentName, int i) {
        String str = componentName.getPackageName() + "::" + componentName.getClassName() + "::" + i;
        synchronized (this.BINDER_LOCK) {
            BinderInfo binderInfo = this.binderRecords.get(str);
            if (binderInfo == null) {
                HiLog.debug(LABEL_LOG, "getBinder come, supply is %{public}s, count is null, connectService", str);
                return null;
            }
            binderInfo.count++;
            HiLog.debug(LABEL_LOG, "getBinder come, supply is %{public}s, count is %{public}d", str, Integer.valueOf(binderInfo.count));
            return binderInfo;
        }
    }

    /* access modifiers changed from: package-private */
    public void deleteBinder(String str) {
        synchronized (this.BINDER_LOCK) {
            BinderInfo binderInfo = this.binderRecords.get(str);
            if (binderInfo == null) {
                HiLog.warn(LABEL_LOG, "deleteBinder binderInfo is null, supply is %{public}s", str);
                return;
            }
            binderInfo.count--;
            HiLog.debug(LABEL_LOG, "deleteBinder come supply is %{public}s, count is %{public}d", str, Integer.valueOf(binderInfo.count));
            if (binderInfo.count == 0) {
                this.binderRecords.remove(str);
                disconnectAbility(getContext(), binderInfo.connection);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void cleanBinder(String str) {
        HiLog.debug(LABEL_LOG, "cleanBinder come supply is %{public}s", str);
        synchronized (this.BINDER_LOCK) {
            BinderInfo binderInfo = this.binderRecords.get(str);
            if (binderInfo != null) {
                this.binderRecords.remove(str);
                disconnectAbility(getContext(), binderInfo.connection);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void handleAcquireBack(long j, ProviderFormInfo providerFormInfo) {
        HiLog.debug(LABEL_LOG, "handleAcquireBack, formId is %{public}d", Long.valueOf(j));
        if (providerFormInfo == null) {
            HiLog.error(LABEL_LOG, "handleAcquireBack come, providerFormInfo is null", new Object[0]);
            return;
        }
        synchronized (this.FORM_LOCK) {
            FormRecord formRecord = this.formRecords.get(Long.valueOf(j));
            if (formRecord == null) {
                HiLog.error(LABEL_LOG, "handleAcquireBack, not exist such form:%{public}d", Long.valueOf(j));
                return;
            }
            FormHostRecord formHostRecord = null;
            for (FormHostRecord formHostRecord2 : this.clientRecords) {
                if (formHostRecord2.contains(j)) {
                    formHostRecord = formHostRecord2;
                }
            }
            if (formHostRecord == null) {
                HiLog.error(LABEL_LOG, "handleAcquireBack, clientHost is null:%{public}d", Long.valueOf(j));
            } else if (formRecord.isInited) {
                HiLog.info(LABEL_LOG, "handleAcquireBack, isInited is true, not refresh again:%{public}d", Long.valueOf(j));
                if (!isComponentCached(formRecord)) {
                    refreshForm(j, new Intent());
                } else if (formHostRecord.contains(j)) {
                    formHostRecord.onAcquire(j, formRecord);
                }
            } else {
                formRecord.isInited = true;
                if (!providerFormInfo.isJsForm()) {
                    formRecord.formView = providerFormInfo.getComponentProvider();
                } else if (formRecord.instantProvider == null) {
                    HiLog.error(LABEL_LOG, "handleAcquireBack error: js instant provider is null", new Object[0]);
                    return;
                } else {
                    formRecord.instantProvider.setFormBindingData(providerFormInfo.getJsBindingData());
                }
                formRecord.needRefresh = false;
                if (formHostRecord.contains(j)) {
                    formHostRecord.onAcquire(j, formRecord);
                }
                int formDataSize = providerFormInfo.getFormDataSize();
                if (providerFormInfo.isJsForm()) {
                    FormBindingData formBindingData = formRecord.instantProvider.getFormBindingData();
                    if (formDataSize > 1024 || (formBindingData != null && formBindingData.getImageDataState() == 1)) {
                        HiLog.warn(LABEL_LOG, "acquire js card, not cache the card, dataSize:%{public}d", Integer.valueOf(formDataSize));
                        formRecord.instantProvider.resetFormBindingData();
                    }
                } else if (formDataSize > 1024) {
                    HiLog.warn(LABEL_LOG, "acquire java card, data is over 1k, do not cache data", new Object[0]);
                    formRecord.formView = null;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void handleAcquireAndUpdateForm(long j, ProviderFormInfo providerFormInfo) {
        HiLog.debug(LABEL_LOG, "handleAcquireAndUpdateForm, formId is %{public}d", Long.valueOf(j));
        if (providerFormInfo == null) {
            HiLog.error(LABEL_LOG, "providerFormInfo is null", new Object[0]);
            return;
        }
        synchronized (this.FORM_LOCK) {
            updateFormCommonProcessLocked(j, providerFormInfo.isJsForm(), providerFormInfo.getJsBindingData(), providerFormInfo.getComponentProvider(), providerFormInfo.getFormDataSize());
        }
    }

    /* access modifiers changed from: package-private */
    public void postHostDied(final IRemoteObject iRemoteObject) {
        EventHandler eventHandler2 = this.eventHandler;
        if (eventHandler2 != null) {
            eventHandler2.postTask(new Runnable() {
                /* class ohos.aafwk.ability.FormAdapter.AnonymousClass1 */

                public void run() {
                    FormAdapter.this.handleHostDied(iRemoteObject);
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public void handleHostDied(IRemoteObject iRemoteObject) {
        HiLog.info(LABEL_LOG, "remote client died", new Object[0]);
        if (iRemoteObject == null) {
            HiLog.info(LABEL_LOG, "remote client died, invalid param", new Object[0]);
            return;
        }
        List<Long> arrayList = new ArrayList<>();
        ArrayList arrayList2 = new ArrayList();
        synchronized (this.FORM_LOCK) {
            Iterator<FormHostRecord> it = this.clientRecords.iterator();
            while (it.hasNext()) {
                FormHostRecord next = it.next();
                if (iRemoteObject.equals(next.clientStub)) {
                    recordDiedTempForms(next, arrayList);
                    HiLog.info(LABEL_LOG, "find died client, remove it", new Object[0]);
                    next.cleanResource();
                    it.remove();
                }
            }
            Iterator<Map.Entry<Long, FormRecord>> it2 = this.formRecords.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<Long, FormRecord> next2 = it2.next();
                long longValue = next2.getKey().longValue();
                if (!hasHostLocked(longValue)) {
                    HiLog.info(LABEL_LOG, "no host need this form, remove it from timer", new Object[0]);
                    arrayList2.add(Long.valueOf(longValue));
                }
                if (arrayList.contains(Long.valueOf(longValue))) {
                    it2.remove();
                    notifyProviderFormDelete(longValue, next2.getValue());
                }
            }
        }
        arrayList.removeAll(arrayList2);
        arrayList.addAll(arrayList2);
        for (Long l : arrayList) {
            FormTimerManager.getInstance().deleteFormTimer(l.longValue());
        }
    }

    private void recordDiedTempForms(FormHostRecord formHostRecord, List<Long> list) {
        Iterator<Long> it = this.tempForms.iterator();
        while (it.hasNext()) {
            Long next = it.next();
            if (formHostRecord.contains(next.longValue())) {
                list.add(next);
                it.remove();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void handleSupplierRemoved(String str, int i) {
        HashSet<Long> hashSet = new HashSet();
        synchronized (this.FORM_LOCK) {
            ensureFormIdSetInitialed();
            int userId = UserHandle.getUserId(i);
            handleDBRecordForSupplierRemovedLocked(str, userId, hashSet);
            handleTempRecordForSupplierRemovedLocked(str, userId, hashSet);
            handleHostRecordForSupplierRemovedLocked(hashSet);
        }
        for (Long l : hashSet) {
            FormTimerManager.getInstance().deleteFormTimer(l.longValue());
        }
        handleFormHostDataCleared(i);
    }

    /* access modifiers changed from: package-private */
    public void handleSupplierUpdated(String str, int i) {
        boolean z;
        BundlePackInfo bundlePackInfo;
        BundleInfo bundleInfo;
        int userId = UserHandle.getUserId(i);
        HashSet<Long> hashSet = new HashSet();
        HashSet<Long> hashSet2 = new HashSet();
        synchronized (this.FORM_LOCK) {
            Iterator<Map.Entry<Long, FormRecord>> it = this.formRecords.entrySet().iterator();
            List<FormInfo> list = null;
            BundleInfo bundleInfo2 = null;
            BundlePackInfo bundlePackInfo2 = null;
            boolean z2 = false;
            while (it.hasNext()) {
                Map.Entry<Long, FormRecord> next = it.next();
                long longValue = next.getKey().longValue();
                FormRecord value = next.getValue();
                if (str.equals(value.bundleName)) {
                    if (userId == value.userId) {
                        List<FormInfo> list2 = list;
                        HiLog.debug(LABEL_LOG, "supplier update, formName:%{public}s", value.formName);
                        if (!z2) {
                            bundlePackInfo = getBundlePackInfo(value.bundleName);
                            bundleInfo = getBundleInfo(value.bundleName);
                            list2 = getFormsInfoByHap(value.bundleName);
                            z = true;
                        } else {
                            z = z2;
                            bundleInfo = bundleInfo2;
                            bundlePackInfo = bundlePackInfo2;
                        }
                        if (handleSupplierFormUpdated(longValue, value, bundleInfo, list2, bundlePackInfo)) {
                            hashSet2.add(Long.valueOf(longValue));
                        } else {
                            HiLog.info(LABEL_LOG, "update, no such form anymore,delete it:%{public}s", value.formName);
                            if (!value.tempFormFlag) {
                                delPersistentInfo(longValue);
                            } else {
                                this.tempForms.remove(Long.valueOf(longValue));
                            }
                            hashSet.add(Long.valueOf(longValue));
                            it.remove();
                        }
                        bundlePackInfo2 = bundlePackInfo;
                        userId = userId;
                        list = list2;
                        z2 = z;
                        bundleInfo2 = bundleInfo;
                    }
                }
                userId = userId;
                list = list;
            }
            if (!hashSet.isEmpty()) {
                cleanRemovedForms(hashSet);
            }
        }
        for (Long l : hashSet) {
            FormTimerManager.getInstance().deleteFormTimer(l.longValue());
        }
        Intent intent = new Intent();
        for (Long l2 : hashSet2) {
            refreshForm(l2.longValue(), intent);
        }
    }

    private boolean handleSupplierFormUpdated(long j, FormRecord formRecord, BundleInfo bundleInfo, List<FormInfo> list, BundlePackInfo bundlePackInfo) {
        if (formRecord == null) {
            HiLog.error(LABEL_LOG, "handleSupplierFormUpdated come, record is null,formId:%{public}d", Long.valueOf(j));
            return false;
        }
        UpdatedForm updatedForm = getUpdatedForm(formRecord, bundleInfo, list);
        if (updatedForm == null || updatedForm.formInfo == null) {
            BundlePackInfo.AbilityFormInfo packageForm = getPackageForm(formRecord, bundlePackInfo);
            if (packageForm == null) {
                return false;
            }
            HiLog.info(LABEL_LOG, "supplier update, form is still in packinfo,form:%{public}s", formRecord.formName);
            formRecord.needFreeInstall = true;
            handleTimerUpdate(j, formRecord, getTimerCfg(packageForm.updateEnabled, packageForm.updateDuration, packageForm.scheduledUpateTime));
            formRecord.versionUpgrade = true;
            return true;
        }
        HiLog.info(LABEL_LOG, "supplier update, form is still exist,form:%{public}s", formRecord.formName);
        handleResourceUpdate(formRecord, bundleInfo, updatedForm);
        notifyModuleNotRemovable(formRecord.bundleName, formRecord.moduleName, formRecord.userId);
        handleTimerUpdate(j, formRecord, getTimerCfg(updatedForm.formInfo.getUpdateEnabled(), updatedForm.formInfo.getUpdateDuration(), updatedForm.formInfo.getScheduledUpdateTime()));
        formRecord.versionUpgrade = true;
        return true;
    }

    /* access modifiers changed from: package-private */
    public void handleFormFreeInstalled(long j, String str) {
        BundleInfo bundleInfo = getBundleInfo(str);
        List<FormInfo> formsInfoByHap = getFormsInfoByHap(str);
        synchronized (this.FORM_LOCK) {
            FormRecord formRecord = this.formRecords.get(Long.valueOf(j));
            if (formRecord == null) {
                HiLog.error(LABEL_LOG, "handleFormFreeInstalled, not exist such form:%{public}d", Long.valueOf(j));
                return;
            }
            formRecord.needFreeInstall = false;
            UpdatedForm updatedForm = getUpdatedForm(formRecord, bundleInfo, formsInfoByHap);
            if (updatedForm != null) {
                if (updatedForm.formInfo != null) {
                    handleResourceUpdate(formRecord, bundleInfo, updatedForm);
                    notifyModuleNotRemovable(formRecord.bundleName, formRecord.moduleName, formRecord.userId);
                    return;
                }
            }
            this.formRecords.remove(Long.valueOf(j));
            HashSet hashSet = new HashSet(1);
            hashSet.add(Long.valueOf(j));
            cleanRemovedForms(hashSet);
        }
    }

    private BundlePackInfo getBundlePackInfo(String str) {
        List<BundlePackInfo.AbilityFormInfo> list;
        HiLog.info(LABEL_LOG, "getBundlePackInfo bundleName:%{public}s.", str);
        BundleManager instance = BundleManager.getInstance();
        BundlePackInfo bundlePackInfo = null;
        if (instance == null) {
            HiLog.error(LABEL_LOG, "getBundlePackInfo bms is null.", new Object[0]);
            return null;
        }
        try {
            bundlePackInfo = instance.getBundlePackInfo(str, 4);
        } catch (RemoteException unused) {
            HiLog.error(LABEL_LOG, "getBundleInfo bms getBundleInfo failed.", new Object[0]);
        }
        if (bundlePackInfo == null || bundlePackInfo.summary == null) {
            HiLog.info(LABEL_LOG, "getBundlePackInfo packInfo is invalid.", new Object[0]);
            return bundlePackInfo;
        }
        for (BundlePackInfo.ModuleConfigInfo moduleConfigInfo : bundlePackInfo.summary.modules) {
            if (!(moduleConfigInfo.distro == null || moduleConfigInfo.distro.moduleName == null)) {
                List<BundlePackInfo.ModuleAbilityInfo> list2 = moduleConfigInfo.abilities;
                if (list2 == null || list2.isEmpty()) {
                    HiLog.debug(LABEL_LOG, "no abilities in module:%{public}s.", moduleConfigInfo.distro.moduleName);
                } else {
                    for (BundlePackInfo.ModuleAbilityInfo moduleAbilityInfo : list2) {
                        if (moduleAbilityInfo.name != null && ((list = moduleAbilityInfo.forms) == null || list.isEmpty())) {
                            HiLog.debug(LABEL_LOG, "tra no forms in ability:%{public}s.", moduleAbilityInfo.name);
                        }
                    }
                }
            }
        }
        return bundlePackInfo;
    }

    private BundleInfo getBundleInfo(String str) {
        BundleManager instance = BundleManager.getInstance();
        if (instance == null) {
            HiLog.error(LABEL_LOG, "getBundleInfo bms is null.", new Object[0]);
            return null;
        }
        try {
            return instance.getBundleInfo(str, 1);
        } catch (RemoteException unused) {
            HiLog.error(LABEL_LOG, "getBundleInfo bms getBundleInfo failed.", new Object[0]);
            return null;
        }
    }

    private List<FormInfo> getFormsInfoByHap(String str) {
        BundleManager instance = BundleManager.getInstance();
        if (instance == null) {
            return Collections.emptyList();
        }
        try {
            return instance.getFormsInfoByApp(str);
        } catch (RemoteException unused) {
            return Collections.emptyList();
        }
    }

    private UpdatedForm getUpdatedForm(FormRecord formRecord, BundleInfo bundleInfo, List<FormInfo> list) {
        UpdatedForm updatedForm = null;
        if (formRecord == null || bundleInfo == null || list == null || list.isEmpty()) {
            HiLog.error(LABEL_LOG, "getUpdatedForm invalid.", new Object[0]);
            return null;
        }
        Iterator<FormInfo> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            FormInfo next = it.next();
            if (isSameForm(formRecord, next)) {
                if (formRecord.isJsForm) {
                    UpdatedForm updatedForm2 = new UpdatedForm();
                    updatedForm2.formInfo = next;
                    updatedForm = updatedForm2;
                } else {
                    FormIdKey formIdKey = new FormIdKey();
                    formIdKey.bundleName = formRecord.bundleName;
                    formIdKey.moduleName = formRecord.moduleName;
                    formIdKey.abilityName = formRecord.abilityName;
                    formIdKey.formName = formRecord.formName;
                    formIdKey.specificationId = formRecord.specification;
                    formIdKey.orientation = formRecord.orientation;
                    FormResource nativeGetFormResource = nativeGetFormResource(formIdKey);
                    if (nativeGetFormResource != null && nativeGetFormResource.isValid()) {
                        UpdatedForm updatedForm3 = new UpdatedForm();
                        updatedForm3.formInfo = next;
                        updatedForm3.formResource = nativeGetFormResource;
                        updatedForm = updatedForm3;
                    }
                }
                HiLog.debug(LABEL_LOG, "find matched form.", new Object[0]);
            }
        }
        return updatedForm;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00b4, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private ohos.bundle.BundlePackInfo.AbilityFormInfo getPackageForm(ohos.aafwk.ability.FormRecord r8, ohos.bundle.BundlePackInfo r9) {
        /*
        // Method dump skipped, instructions count: 191
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.FormAdapter.getPackageForm(ohos.aafwk.ability.FormRecord, ohos.bundle.BundlePackInfo):ohos.bundle.BundlePackInfo$AbilityFormInfo");
    }

    private boolean isSameForm(FormRecord formRecord, FormInfo formInfo) {
        List<Integer> supportDimensions;
        if (formRecord == null || formInfo == null || !isEqual(formRecord.bundleName, formInfo.getBundleName()) || !isEqual(formRecord.originalBundleName, formInfo.getOriginalBundleName()) || !isEqual(formRecord.moduleName, formInfo.getModuleName()) || !isEqual(formRecord.abilityName, formInfo.getAbilityName()) || !isEqual(formRecord.formName, formInfo.getFormName()) || !isSameType(formRecord.isJsForm, formInfo.getType()) || (supportDimensions = formInfo.getSupportDimensions()) == null || !supportDimensions.contains(Integer.valueOf(formRecord.specification))) {
            return false;
        }
        return true;
    }

    private boolean isSameForm(FormRecord formRecord, BundlePackInfo.AbilityFormInfo abilityFormInfo) {
        if (formRecord == null || abilityFormInfo == null || !isEqual(formRecord.formName, abilityFormInfo.name) || !isSameType(formRecord.isJsForm, abilityFormInfo.type) || abilityFormInfo.supportDimensions == null || !abilityFormInfo.supportDimensions.contains(Integer.valueOf(formRecord.specification))) {
            return false;
        }
        return true;
    }

    private boolean isEqual(String str, String str2) {
        return str != null && str.equals(str2);
    }

    private boolean isSameType(boolean z, FormInfo.FormType formType) {
        if (formType == FormInfo.FormType.JAVA && !z) {
            return true;
        }
        if (formType != FormInfo.FormType.JS || !z) {
            return false;
        }
        return true;
    }

    private boolean isSameType(boolean z, String str) {
        return (z && TYPE_JS.equals(str)) || (!z && TYPE_JAVA.equals(str));
    }

    private void handleResourceUpdate(FormRecord formRecord, BundleInfo bundleInfo, UpdatedForm updatedForm) {
        if (formRecord != null && bundleInfo != null && updatedForm != null) {
            if (formRecord.isJsForm) {
                handleJsFormResourceUpdate(formRecord, bundleInfo, updatedForm.formInfo);
            } else {
                hanleJavaFormResourceUpdate(formRecord, bundleInfo, updatedForm);
            }
        }
    }

    private void handleJsFormResourceUpdate(FormRecord formRecord, BundleInfo bundleInfo, FormInfo formInfo) {
        String str;
        List<ModuleInfo> moduleInfos = bundleInfo.appInfo.getModuleInfos();
        if (moduleInfos != null) {
            Iterator<ModuleInfo> it = moduleInfos.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ModuleInfo next = it.next();
                if (formRecord.moduleName.equals(next.getModuleName())) {
                    str = next.getModuleSourceDir();
                    break;
                }
            }
            formRecord.instantProvider = new InstantProvider(formInfo.getJsComponentName(), str);
            formRecord.instantProvider.resetFormBindingData();
            formRecord.needRefresh = true;
        }
        str = "";
        formRecord.instantProvider = new InstantProvider(formInfo.getJsComponentName(), str);
        formRecord.instantProvider.resetFormBindingData();
        formRecord.needRefresh = true;
    }

    private void hanleJavaFormResourceUpdate(FormRecord formRecord, BundleInfo bundleInfo, UpdatedForm updatedForm) {
        formRecord.formView = null;
        formRecord.needRefresh = true;
        if (updatedForm.formInfo != null && updatedForm.formResource != null && updatedForm.formResource.isValid()) {
            List<ModuleInfo> moduleInfos = bundleInfo.appInfo.getModuleInfos();
            if (moduleInfos != null) {
                Iterator<ModuleInfo> it = moduleInfos.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ModuleInfo next = it.next();
                    if (formRecord.moduleName.equals(next.getModuleName())) {
                        formRecord.hapSourceDirs = new String[]{next.getModuleSourceDir()};
                        break;
                    }
                }
            }
            if (!ComponentUtils.initLayout(aContext, formRecord, updatedForm.formResource)) {
                HiLog.error(LABEL_LOG, "hanleJavaFormResourceUpdate initLayout failed", new Object[0]);
                return;
            }
            formRecord.previewLayoutId = updatedForm.formResource.previewLayoutId;
            formRecord.eSystemPreviewLayoutId = updatedForm.formResource.eSystemPreviewLayoutId;
        }
    }

    private void handleTimerUpdate(long j, FormRecord formRecord, FormTimerCfg formTimerCfg) {
        UpdateType updateType;
        RefreshRunnable refreshRunnable;
        if (!formRecord.isEnableUpdate && !formTimerCfg.enableUpdate) {
            return;
        }
        if (formRecord.isEnableUpdate && !formTimerCfg.enableUpdate) {
            formRecord.isEnableUpdate = false;
            FormTimerManager.getInstance().deleteFormTimer(j);
        } else if (formRecord.isEnableUpdate || !formTimerCfg.enableUpdate) {
            if (formRecord.updateDuration > 0) {
                if (formTimerCfg.updateDuration <= 0) {
                    updateType = UpdateType.TYPE_INTERVAL_TO_ATTIME;
                } else if (formRecord.updateDuration != formTimerCfg.updateDuration) {
                    updateType = UpdateType.TYPE_INTERVAL_CHANGE;
                } else {
                    return;
                }
            } else if (formTimerCfg.updateDuration > 0) {
                updateType = UpdateType.TYPE_ATTIME_TO_INTERVAL;
            } else if (formRecord.updateAtHour != formTimerCfg.updateAtHour || formRecord.updateAtMin != formTimerCfg.updateAtMin) {
                updateType = UpdateType.TYPE_ATTIME_CHANGE;
            } else {
                return;
            }
            formRecord.updateDuration = formTimerCfg.updateDuration;
            formRecord.updateAtHour = formTimerCfg.updateAtHour;
            formRecord.updateAtMin = formTimerCfg.updateAtMin;
            FormTimerManager.getInstance().updateFormTimer(j, updateType, formTimerCfg);
        } else {
            formRecord.isEnableUpdate = true;
            formRecord.updateDuration = formTimerCfg.updateDuration;
            formRecord.updateAtHour = formTimerCfg.updateAtHour;
            formRecord.updateAtMin = formTimerCfg.updateAtMin;
            if (formTimerCfg.updateDuration > 0) {
                refreshRunnable = new RefreshRunnable(j, formTimerCfg.updateDuration);
            } else {
                refreshRunnable = new RefreshRunnable(j, formRecord.updateAtHour, formRecord.updateAtMin);
            }
            FormTimerManager.getInstance().addFormTimer(refreshRunnable);
        }
    }

    private FormTimerCfg getTimerCfg(boolean z, int i, String str) {
        FormTimerCfg formTimerCfg = new FormTimerCfg();
        if (!z) {
            return formTimerCfg;
        }
        HiLog.debug(LABEL_LOG, "getTimerCfg updateDuration:%{public}d", Integer.valueOf(i));
        if (i > 0) {
            if (i <= 1) {
                formTimerCfg.updateDuration = MIN_PERIOD;
            } else if (i >= 336) {
                formTimerCfg.updateDuration = MAX_PERIOD;
            } else {
                formTimerCfg.updateDuration = ((long) i) * TIME_CONVERSION;
            }
            formTimerCfg.enableUpdate = true;
            return formTimerCfg;
        }
        if (str != null && !str.isEmpty()) {
            HiLog.debug(LABEL_LOG, "getTimerCfg updateAt:%{public}s", str);
            String[] split = str.split(TIME_DELIMETER);
            if (split.length != 2) {
                HiLog.error(LABEL_LOG, "getTimerCfg invalid config", new Object[0]);
                return formTimerCfg;
            }
            try {
                int parseInt = Integer.parseInt(split[0]);
                int parseInt2 = Integer.parseInt(split[1]);
                if (parseInt < 0 || parseInt > 23 || parseInt2 < 0 || parseInt2 > 59) {
                    HiLog.error(LABEL_LOG, "getTimerCfg time is invalid", new Object[0]);
                    return formTimerCfg;
                }
                formTimerCfg.updateAtHour = parseInt;
                formTimerCfg.updateAtMin = parseInt2;
                formTimerCfg.enableUpdate = true;
                return formTimerCfg;
            } catch (NumberFormatException unused) {
                HiLog.error(LABEL_LOG, "getTimerCfg invalid hour or min", new Object[0]);
            }
        }
        return formTimerCfg;
    }

    private void cleanRemovedForms(Set<Long> set) {
        for (FormHostRecord formHostRecord : this.clientRecords) {
            ArrayList arrayList = new ArrayList();
            for (Long l : set) {
                HiLog.info(LABEL_LOG, "FormAdaptera, cleanRemovedForms tra formId:%{public}d", l);
                if (formHostRecord.contains(l.longValue())) {
                    HiLog.info(LABEL_LOG, "FormAdaptera, client match formId:%{public}d", l);
                    arrayList.add(l);
                    formHostRecord.delForm(l.longValue());
                }
            }
            if (!arrayList.isEmpty()) {
                formHostRecord.onFormUninstalled(arrayList);
            }
        }
    }

    private void handleDBRecordForSupplierRemovedLocked(String str, int i, Set<Long> set) {
        HashSet<Long> hashSet = new HashSet<>();
        Iterator<Map.Entry<Long, FormDBRecord>> it = this.formDBRecords.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, FormDBRecord> next = it.next();
            FormDBRecord value = next.getValue();
            if (str.equals(value.bundleName) && i == value.userId) {
                HiLog.debug(LABEL_LOG, "handleDBRecordForSupplierRemovedLocked find matched FormDBRecord", new Object[0]);
                long longValue = next.getKey().longValue();
                hashSet.add(Long.valueOf(longValue));
                this.formRecords.remove(Long.valueOf(longValue));
                it.remove();
                notifyModuleRemovable(value.bundleName, value.moduleName, value.userId);
            }
        }
        HiLog.debug(LABEL_LOG, "handleDBRecordForSupplierRemovedLocked: userId=%{public}d, foundFormsSet size:%{public}d", Integer.valueOf(i), Integer.valueOf(hashSet.size()));
        if (!hashSet.isEmpty()) {
            handleNativeBatchDeleteFormDBInfos(hashSet);
            set.addAll(hashSet);
        }
    }

    private void handleTempRecordForSupplierRemovedLocked(String str, int i, Set<Long> set) {
        HashSet hashSet = new HashSet();
        Iterator<Map.Entry<Long, FormRecord>> it = this.formRecords.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, FormRecord> next = it.next();
            FormRecord value = next.getValue();
            if (value.tempFormFlag && str.equals(value.bundleName) && i == value.userId) {
                HiLog.debug(LABEL_LOG, "handleTempRecordForSupplierRemovedLocked find matched temp FormRecord", new Object[0]);
                long longValue = next.getKey().longValue();
                hashSet.add(Long.valueOf(longValue));
                this.tempForms.remove(Long.valueOf(longValue));
                it.remove();
            }
        }
        HiLog.debug(LABEL_LOG, "handleTempRecordForSupplierRemovedLocked: userId=%{public}d, foundFormsSet size:%{public}d", Integer.valueOf(i), Integer.valueOf(hashSet.size()));
        if (!hashSet.isEmpty()) {
            set.addAll(hashSet);
        }
    }

    private void handleHostRecordForSupplierRemovedLocked(Set<Long> set) {
        for (FormHostRecord formHostRecord : this.clientRecords) {
            ArrayList arrayList = new ArrayList();
            for (Long l : set) {
                long longValue = l.longValue();
                if (formHostRecord.contains(longValue)) {
                    arrayList.add(Long.valueOf(longValue));
                    formHostRecord.delForm(longValue);
                }
            }
            if (!arrayList.isEmpty()) {
                formHostRecord.onFormUninstalled(arrayList);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00c0, code lost:
        r12 = r0.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00c8, code lost:
        if (r12.hasNext() == false) goto L_0x00dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00ca, code lost:
        ohos.aafwk.ability.FormTimerManager.getInstance().deleteFormTimer(((java.lang.Long) r12.next()).longValue());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00dc, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleUserRemoved(int r13) {
        /*
        // Method dump skipped, instructions count: 224
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.FormAdapter.handleUserRemoved(int):void");
    }

    /* access modifiers changed from: package-private */
    public void handleBundleDataCleared(String str, int i) {
        HiLog.debug(LABEL_LOG, "handleBundleDataCleared in, bundleName:%{public}s, uid:%{public}d", str, Integer.valueOf(i));
        if (str == null || str.isEmpty()) {
            HiLog.warn(LABEL_LOG, "handleBundleDataCleared bundleName invalid", new Object[0]);
            return;
        }
        handleFormProviderDataCleared(str, i);
        handleFormHostDataCleared(i);
    }

    /* access modifiers changed from: package-private */
    public void handleConfigChanged() {
        ArrayList<Long> arrayList;
        HiLog.debug(LABEL_LOG, "handleConfigChanged begin, refresh forms", new Object[0]);
        synchronized (this.FORM_LOCK) {
            arrayList = new ArrayList(this.formRecords.keySet());
        }
        for (Long l : arrayList) {
            refreshForm(l.longValue(), new Intent());
        }
    }

    /* access modifiers changed from: package-private */
    public void handleFormProviderDataCleared(String str, int i) {
        HiLog.debug(LABEL_LOG, "handleFormProviderDataCleared in, uid:%{public}d, packageName:%{public}s", Integer.valueOf(i), str);
        HashSet<Long> hashSet = new HashSet();
        synchronized (this.FORM_LOCK) {
            for (Map.Entry<Long, FormRecord> entry : this.formRecords.entrySet()) {
                long longValue = entry.getKey().longValue();
                FormRecord value = entry.getValue();
                if (value.originalBundleName.equals(str) && value.userId == UserHandle.getUserId(i)) {
                    hashSet.add(Long.valueOf(longValue));
                }
            }
        }
        if (!hashSet.isEmpty()) {
            for (Long l : hashSet) {
                reCreateForm(l.longValue());
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x006f, code lost:
        r0 = buildCreateFormIntent(r6, r3.formName, r3.specification, r3.tempFormFlag);
        r0.setParam(ohos.aafwk.ability.FormAdapter.RECREATE_FORM_KEY, true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0081, code lost:
        if (r3.customParams == null) goto L_0x008b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0083, code lost:
        r0.setParam("ohos.extra.param.key.form_customize", r3.customParams);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x008b, code lost:
        reBindSupplier(r6, r3, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x008e, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void reCreateForm(long r6) {
        /*
        // Method dump skipped, instructions count: 146
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.FormAdapter.reCreateForm(long):void");
    }

    /* access modifiers changed from: package-private */
    public Map<Long, Integer> buildNotifyEvents(List<Long> list) {
        HiLog.debug(LABEL_LOG, "buildNotifyEvents come", new Object[0]);
        HashMap hashMap = new HashMap();
        synchronized (this.FORM_LOCK) {
            for (Long l : list) {
                long longValue = l.longValue();
                FormRecord formRecord = this.formRecords.get(Long.valueOf(longValue));
                if (formRecord != null) {
                    hashMap.put(Long.valueOf(longValue), Integer.valueOf(formRecord.formVisibleNotifyState));
                }
            }
        }
        HiLog.debug(LABEL_LOG, "buildNotifyEvents end", new Object[0]);
        return hashMap;
    }

    /* access modifiers changed from: package-private */
    public void handleFormHostDataCleared(int i) {
        HiLog.debug(LABEL_LOG, "handleFormHostDataCleared, uid:%{public}d", Integer.valueOf(i));
        HashMap hashMap = new HashMap();
        synchronized (this.FORM_LOCK) {
            ensureFormIdSetInitialed();
            int userId = UserHandle.getUserId(i);
            clearFormDBRecordDataLocked(userId, i, hashMap);
            clearTempFormRecordDataLocked(userId, i, hashMap);
            clearHostDataLocked(i);
        }
        deleteFormsTimer(hashMap);
    }

    private void clearHostDataLocked(int i) {
        Iterator<FormHostRecord> it = this.clientRecords.iterator();
        while (it.hasNext()) {
            FormHostRecord next = it.next();
            if (next.myUid == i) {
                HiLog.debug(LABEL_LOG, "find matched FormHostRecord, uid:%{public}d", Integer.valueOf(i));
                next.cleanResource();
                it.remove();
            }
        }
    }

    private void clearFormDBRecordDataLocked(int i, int i2, Map<Long, Boolean> map) {
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        for (Map.Entry<Long, FormDBRecord> entry : this.formDBRecords.entrySet()) {
            FormDBRecord value = entry.getValue();
            if (value.userId == i && value.formUserUids != null && value.formUserUids.contains(Integer.valueOf(i2))) {
                checkEachDBRecord(i2, entry.getKey().longValue(), value, hashMap, hashMap2);
            }
        }
        HiLog.debug(LABEL_LOG, "clearFormDBRecordDataLocked noHostDBFormsMap size:%{public}d", Integer.valueOf(hashMap2.size()));
        if (hashMap2.size() > 0) {
            batchDeleteNoHostDBForms(i, i2, hashMap2, hashMap);
        }
        if (!hashMap.isEmpty()) {
            map.putAll(hashMap);
        }
    }

    private void clearTempFormRecordDataLocked(int i, int i2, Map<Long, Boolean> map) {
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        for (Map.Entry<Long, FormRecord> entry : this.formRecords.entrySet()) {
            FormRecord value = entry.getValue();
            if (i == value.userId && value.tempFormFlag && value.formUserUids.contains(Integer.valueOf(i2))) {
                checkEachTempFormRecord(i2, entry.getKey().longValue(), value, hashMap, hashMap2);
            }
        }
        HiLog.debug(LABEL_LOG, "clearTempFormRecordDataLocked noHostTempFormsMap size:%{public}d", Integer.valueOf(hashMap2.size()));
        if (hashMap2.size() > 0) {
            batchDeleteNoHostTempForms(i2, hashMap2, hashMap);
        }
        if (!hashMap.isEmpty()) {
            map.putAll(hashMap);
        }
    }

    private void ensureFormIdSetInitialed() {
        if (!this.formDBInfoInited) {
            ArrayList<FormDBRecord> nativeGetAllFormInfo = nativeGetAllFormInfo();
            if (nativeGetAllFormInfo != null) {
                Iterator<FormDBRecord> it = nativeGetAllFormInfo.iterator();
                while (it.hasNext()) {
                    FormDBRecord next = it.next();
                    if (next.formId > 0) {
                        HiLog.debug(LABEL_LOG, "ensureFormIdSetInitialed, formId:%{public}d, userId:%{private}d", Long.valueOf(next.formId), Integer.valueOf(next.userId));
                        if ((next.formId & -4294967296L) == 0) {
                            next.formId = udidHash | next.formId;
                            this.formDBRecords.putIfAbsent(Long.valueOf(next.formId), next);
                        } else {
                            this.formDBRecords.put(Long.valueOf(next.formId), next);
                        }
                    }
                }
            }
            this.formDBInfoInited = true;
        }
    }

    private int handleUpdateFormFlag(List<Long> list, long j, boolean z) {
        MessageParcel create = MessageParcel.create(j);
        IRemoteObject readRemoteObject = create.readRemoteObject();
        create.reclaim();
        if (list == null || list.isEmpty() || readRemoteObject == null) {
            HiLog.error(LABEL_LOG, "handleUpdateFormFlag invalid param", new Object[0]);
            return ERR_FORM_INVALID_PARAM;
        }
        synchronized (this.FORM_LOCK) {
            FormHostRecord matchedHostClientLocked = getMatchedHostClientLocked(readRemoteObject);
            if (matchedHostClientLocked == null) {
                HiLog.error(LABEL_LOG, "handleUpdateFormFlag can't find target client", new Object[0]);
                return ERR_FORM_INVALID_PARAM;
            }
            HiLog.debug(LABEL_LOG, "handleUpdateFormFlag find target client", new Object[0]);
            for (Long l : list) {
                handleItemUpdateFormFlagLocked(matchedHostClientLocked, findMatchedFormId(l.longValue()), z);
            }
            return 0;
        }
    }

    private FormHostRecord getMatchedHostClientLocked(IRemoteObject iRemoteObject) {
        for (FormHostRecord formHostRecord : this.clientRecords) {
            if (iRemoteObject.equals(formHostRecord.clientStub)) {
                return formHostRecord;
            }
        }
        return null;
    }

    private void handleItemUpdateFormFlagLocked(FormHostRecord formHostRecord, long j, boolean z) {
        if (!formHostRecord.contains(j)) {
            HiLog.warn(LABEL_LOG, "form %{public}d is not owned by this client, don't need to update flag", Long.valueOf(j));
            return;
        }
        formHostRecord.setEnableRefresh(j, z);
        if (z) {
            FormRecord formRecord = this.formRecords.get(Long.valueOf(j));
            if (formRecord == null) {
                HiLog.warn(LABEL_LOG, "handleItemUpdateFormFlagLocked, not exist such form:%{public}d", Long.valueOf(j));
            } else if (formRecord.needRefresh) {
                HiLog.info(LABEL_LOG, "handleItemUpdateFormFlagLocked, formRecord need refresh", new Object[0]);
                getInstance().refreshForm(j, new Intent());
            } else if (formHostRecord.isNeedRefresh(j)) {
                if (isComponentCached(formRecord)) {
                    formHostRecord.onUpdate(j, formRecord);
                    formHostRecord.setNeedRefresh(j, false);
                    return;
                }
                getInstance().refreshForm(j, new Intent());
            }
        }
    }

    private boolean isComponentCached(FormRecord formRecord) {
        if (!formRecord.isJsForm) {
            return formRecord.formView != null;
        }
        if (formRecord.versionUpgrade) {
            return false;
        }
        return formRecord.instantProvider != null && formRecord.instantProvider.isInitializedData();
    }

    private boolean hasHostLocked(long j) {
        for (FormHostRecord formHostRecord : this.clientRecords) {
            if (formHostRecord.contains(j)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAddValid(FormItemInfo formItemInfo, IRemoteObject iRemoteObject) {
        if (iRemoteObject == null) {
            return false;
        }
        if (formItemInfo.isJsForm() || ComponentUtils.initLayout(aContext, formItemInfo)) {
            return true;
        }
        HiLog.error(LABEL_LOG, "isAddValid initLayout failed", new Object[0]);
        return false;
    }

    private int handleAddForm(FormItemInfo formItemInfo, IRemoteObject iRemoteObject, MessageParcel messageParcel, IntentParams intentParams) {
        synchronized (this.FORM_LOCK) {
            if (!generateUdidHash()) {
                messageParcel.writeInt(ERR_CODE_COMMON);
                HiLog.error(LABEL_LOG, "generate udid hash failed", new Object[0]);
                return ERR_CODE_COMMON;
            }
            ensureFormIdSetInitialed();
            if (formItemInfo.getFormId() > 0) {
                HiLog.debug(LABEL_LOG, "addForm formId > 0", new Object[0]);
                return handleAddFormById(formItemInfo, iRemoteObject, messageParcel, intentParams);
            }
            return handleAddFormByInfo(formItemInfo, iRemoteObject, messageParcel, intentParams);
        }
    }

    private FormHostRecord findOrNewFormHostRecord(FormItemInfo formItemInfo, IRemoteObject iRemoteObject, int i) {
        for (FormHostRecord formHostRecord : this.clientRecords) {
            if (iRemoteObject.equals(formHostRecord.clientStub)) {
                return formHostRecord;
            }
        }
        FormHostRecord createRecord = FormHostRecord.createRecord(formItemInfo.isESystem(), iRemoteObject, i);
        if (createRecord != null) {
            this.clientRecords.add(createRecord);
        }
        return createRecord;
    }

    private int handleAddFormById(FormItemInfo formItemInfo, IRemoteObject iRemoteObject, MessageParcel messageParcel, IntentParams intentParams) {
        HiLog.info(LABEL_LOG, "addForm handleAddFormById: %{public}d", Long.valueOf(formItemInfo.getFormId()));
        long paddingUDIDHash = paddingUDIDHash(formItemInfo.getFormId());
        int currentUser = ActivityManager.getCurrentUser();
        FormRecord formRecord = this.formRecords.get(Long.valueOf(paddingUDIDHash));
        if (formRecord != null && formRecord.tempFormFlag) {
            HiLog.error(LABEL_LOG, "addForm can not acquire temp form when select form id", new Object[0]);
            return ERR_CODE_COMMON;
        } else if (formRecord == null || !isCallingUidValid(formRecord.userId, formRecord.formUserUids)) {
            FormDBRecord formDBRecord = this.formDBRecords.get(Long.valueOf(paddingUDIDHash));
            if (formDBRecord == null || (formDBRecord.userId != -1 && !isCallingUidValid(formDBRecord.userId, formDBRecord.formUserUids))) {
                messageParcel.writeInt(ERR_NOT_EXIST_ID);
                HiLog.error(LABEL_LOG, "addForm no such form %{public}d", Long.valueOf(paddingUDIDHash));
                if ((formRecord != null && formRecord.userId != currentUser) || (formDBRecord != null && formDBRecord.userId != currentUser)) {
                    return ERR_NOT_EXIST_ID;
                }
                FormRecord formRecord2 = new FormRecord();
                formRecord2.bundleName = formItemInfo.getBundleName();
                formRecord2.originalBundleName = formItemInfo.getOriginalBundleName();
                formRecord2.abilityName = formItemInfo.getAbilityName();
                notifyProviderFormDelete(paddingUDIDHash, formRecord2);
                return ERR_NOT_EXIST_ID;
            }
            int callingUid = IPCSkeleton.getCallingUid();
            int checkEnoughForm = checkEnoughForm(callingUid);
            if (checkEnoughForm != 0) {
                messageParcel.writeInt(checkEnoughForm);
                HiLog.error(LABEL_LOG, "too mush forms in system", new Object[0]);
                return checkEnoughForm;
            }
            HiLog.debug(LABEL_LOG, "addForm form id in db but not in cache", new Object[0]);
            return handleAddNewFormRecord(formItemInfo, iRemoteObject, messageParcel, paddingUDIDHash, callingUid, intentParams);
        } else if (formItemInfo.isMatch(formRecord)) {
            return handleAddExistFormRecord(formItemInfo, iRemoteObject, messageParcel, formRecord, paddingUDIDHash, intentParams);
        } else {
            messageParcel.writeInt(ERR_CFG_NOT_MATCH_ID);
            HiLog.error(LABEL_LOG, "formId and item info not match:%{public}d", Long.valueOf(paddingUDIDHash));
            return ERR_CFG_NOT_MATCH_ID;
        }
    }

    private long paddingUDIDHash(long j) {
        return (-4294967296L & j) == 0 ? udidHash | j : j;
    }

    private long findMatchedFormId(long j) {
        if ((-4294967296L & j) != 0) {
            return j;
        }
        synchronized (this.FORM_LOCK) {
            for (Long l : this.formRecords.keySet()) {
                long longValue = l.longValue();
                if ((longValue & 4294967295L) == (4294967295L & j)) {
                    return longValue;
                }
            }
            return j;
        }
    }

    private int handleAddFormByInfo(FormItemInfo formItemInfo, IRemoteObject iRemoteObject, MessageParcel messageParcel, IntentParams intentParams) {
        int i;
        HiLog.info(LABEL_LOG, "addForm handleAddFormByInfo", new Object[0]);
        int callingUid = IPCSkeleton.getCallingUid();
        if (formItemInfo.isTemporaryForm()) {
            i = checkTempEnoughForm();
        } else {
            i = checkEnoughForm(callingUid);
        }
        if (i != 0) {
            messageParcel.writeInt(i);
            HiLog.error(LABEL_LOG, "too much forms in system", new Object[0]);
            return i;
        }
        long generateFormId = generateFormId();
        if (generateFormId >= 0) {
            return handleAddNewFormRecord(formItemInfo, iRemoteObject, messageParcel, generateFormId, callingUid, intentParams);
        }
        messageParcel.writeInt(ERR_CODE_COMMON);
        HiLog.error(LABEL_LOG, "addForm generateFormId no invalid formId", new Object[0]);
        return ERR_CODE_COMMON;
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0060  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int handleAddExistFormRecord(ohos.aafwk.ability.FormItemInfo r5, ohos.rpc.IRemoteObject r6, ohos.rpc.MessageParcel r7, ohos.aafwk.ability.FormRecord r8, long r9, ohos.aafwk.content.IntentParams r11) {
        /*
        // Method dump skipped, instructions count: 169
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.FormAdapter.handleAddExistFormRecord(ohos.aafwk.ability.FormItemInfo, ohos.rpc.IRemoteObject, ohos.rpc.MessageParcel, ohos.aafwk.ability.FormRecord, long, ohos.aafwk.content.IntentParams):int");
    }

    private int handleAddNewFormRecord(FormItemInfo formItemInfo, IRemoteObject iRemoteObject, MessageParcel messageParcel, long j, int i, IntentParams intentParams) {
        FormHostRecord findOrNewFormHostRecord = findOrNewFormHostRecord(formItemInfo, iRemoteObject, i);
        if (findOrNewFormHostRecord == null) {
            messageParcel.writeInt(ERR_CODE_COMMON);
            HiLog.error(LABEL_LOG, "addForm findOrNewFormHostRecord record failed when no matched formRecord", new Object[0]);
            return ERR_CODE_COMMON;
        }
        FormRecord formRecord = new FormRecord();
        fillBasicFormRecord(formRecord, formItemInfo);
        formRecord.customParams = intentParams;
        if (bindSupplier(j, formItemInfo, intentParams) != 0) {
            messageParcel.writeInt(ERR_BIND_SUPPLIER_FAILED);
            HiLog.error(LABEL_LOG, "addForm bindSupplier failed", new Object[0]);
            return ERR_BIND_SUPPLIER_FAILED;
        }
        findOrNewFormHostRecord.addForm(j);
        this.formRecords.put(Long.valueOf(j), formRecord);
        if (formItemInfo.isTemporaryForm()) {
            this.tempForms.add(Long.valueOf(j));
        }
        formRecord.formUserUids.add(Integer.valueOf(i));
        if (formItemInfo.isESystem()) {
            ESystemHostCallback.replyFormRecord(j, messageParcel, formRecord);
        } else {
            OhosHostCallback.replyFormRecord(j, messageParcel, formRecord);
        }
        if (!formItemInfo.isTemporaryForm()) {
            updateDBRecord(j, i, formRecord);
        }
        if (formRecord.isEnableUpdate) {
            if (formRecord.updateDuration > 0) {
                FormTimerManager.getInstance().addFormTimer(new RefreshRunnable(j, formRecord.updateDuration));
            } else {
                FormTimerManager.getInstance().addFormTimer(new RefreshRunnable(j, formRecord.updateAtHour, formRecord.updateAtMin));
            }
        }
        return 0;
    }

    private void fillBasicFormRecord(FormRecord formRecord, FormItemInfo formItemInfo) {
        formRecord.packageName = formItemInfo.getPackageName();
        formRecord.bundleName = formItemInfo.getBundleName();
        formRecord.originalBundleName = formItemInfo.getOriginalBundleName();
        formRecord.relatedBundleName = formItemInfo.getRelatedBundleName();
        formRecord.moduleName = formItemInfo.getModuleName();
        formRecord.abilityName = formItemInfo.getAbilityName();
        formRecord.formName = formItemInfo.getFormName();
        formRecord.orientation = formItemInfo.getOrientation();
        formRecord.specification = formItemInfo.getSpecificationId();
        formRecord.isEnableUpdate = formItemInfo.isEnableUpdateFlag();
        formRecord.tempFormFlag = formItemInfo.isTemporaryForm();
        formRecord.formVisibleNotify = formItemInfo.isFormVisibleNotify();
        if (formRecord.isEnableUpdate) {
            parseUpdateConfig(formRecord, formItemInfo);
        }
        formRecord.formView = null;
        formRecord.isJsForm = formItemInfo.isJsForm();
        if (formRecord.isJsForm) {
            formRecord.instantProvider = new InstantProvider(formItemInfo.getJsComponentName(), formItemInfo.getHapSourceByModuleName(formItemInfo.getAbilityModuleName()));
        }
        formRecord.hapSourceDirs = formItemInfo.getHapSourceDirs();
        formRecord.previewLayoutId = formItemInfo.previewLayoutId;
        formRecord.eSystemPreviewLayoutId = formItemInfo.eSystemPreviewLayoutId;
        formRecord.userId = ActivityManager.getCurrentUser();
    }

    private Pair<Integer, Boolean> handleDeleteFormLocked(int i, long j, boolean z, IRemoteObject iRemoteObject) {
        FormDBRecord formDBRecord = this.formDBRecords.get(Long.valueOf(j));
        FormRecord formRecord = this.formRecords.get(Long.valueOf(j));
        if (!((i == 3 && (formDBRecord != null || z)) || !(i == 3 || formRecord == null))) {
            HiLog.error(LABEL_LOG, "handleDeleteFormLocked: not exist such db or temp form:%{public}d", Long.valueOf(j));
            return new Pair<>(Integer.valueOf((int) ERR_NOT_EXIST_ID), false);
        }
        int callingUid = IPCSkeleton.getCallingUid();
        int userId = UserHandle.getUserId(callingUid);
        boolean z2 = formDBRecord != null && formDBRecord.userId == userId && formDBRecord.formUserUids != null && formDBRecord.formUserUids.contains(Integer.valueOf(callingUid));
        boolean z3 = formRecord != null && formRecord.userId == userId && formRecord.tempFormFlag && formRecord.formUserUids.contains(Integer.valueOf(callingUid));
        FormHostRecord matchedHostClientLocked = getMatchedHostClientLocked(iRemoteObject);
        if (!((i == 3 && (z2 || z3)) || !(i == 3 || matchedHostClientLocked == null || !matchedHostClientLocked.contains(j)))) {
            HiLog.error(LABEL_LOG, "handleDeleteFormLocked not self form:%{public}d", Long.valueOf(j));
            return new Pair<>(Integer.valueOf((int) ERR_OPERATION_FORM_NOT_SELF), false);
        } else if (i != 3 && i != 9) {
            HiLog.debug(LABEL_LOG, "handleDeleteFormLocked not handle current type: %{public}d", Integer.valueOf(i));
            return new Pair<>(0, false);
        } else if (!z2) {
            return handleDeleteTempFormLocked(formRecord, callingUid, j, i);
        } else {
            if (i == 3) {
                return handleDeleteDbFormLocked(formDBRecord, formRecord, callingUid, j);
            }
            return handleReleaseFormRecordLocked(formRecord, callingUid, j);
        }
    }

    private Pair<Integer, Boolean> handleDeleteDbFormLocked(FormDBRecord formDBRecord, FormRecord formRecord, int i, long j) {
        HiLog.debug(LABEL_LOG, "handleDeleteDbFormLocked: delete formDBRecords, formId: %{public}d", Long.valueOf(j));
        formDBRecord.formUserUids.remove(Integer.valueOf(i));
        if (formDBRecord.formUserUids.isEmpty()) {
            int notifyProviderFormDelete = notifyProviderFormDelete(j, getFormRecordByDBRecord(formDBRecord));
            if (notifyProviderFormDelete != 0) {
                HiLog.error(LABEL_LOG, "handleDeleteDbFormLocked failed!", new Object[0]);
                formDBRecord.formUserUids.add(Integer.valueOf(i));
                return new Pair<>(Integer.valueOf(notifyProviderFormDelete), false);
            }
            this.formRecords.remove(Long.valueOf(j));
            handleNativeDeleteFormDBInfo(j);
            this.formDBRecords.remove(Long.valueOf(j));
            notifyModuleRemovable(formDBRecord.bundleName, formDBRecord.moduleName, formDBRecord.userId);
            return new Pair<>(0, false);
        }
        HiLog.debug(LABEL_LOG, "handleDeleteDbFormLocked dbRecord.formUserUids size: %{public}d", Integer.valueOf(formDBRecord.formUserUids.size()));
        savePersistentInfo(formDBRecord);
        return handleReleaseFormRecordLocked(formRecord, i, j);
    }

    private Pair<Integer, Boolean> handleReleaseFormRecordLocked(FormRecord formRecord, int i, long j) {
        boolean z = true;
        if (formRecord != null) {
            HiLog.debug(LABEL_LOG, "handleReleaseFormRecordLocked: release formRecords, formId: %{public}d", Long.valueOf(j));
            formRecord.formUserUids.remove(Integer.valueOf(i));
            if (formRecord.formUserUids.isEmpty()) {
                this.formRecords.remove(Long.valueOf(j));
            }
            return new Pair<>(0, Boolean.valueOf(z));
        }
        z = false;
        return new Pair<>(0, Boolean.valueOf(z));
    }

    private Pair<Integer, Boolean> handleDeleteTempFormLocked(FormRecord formRecord, int i, long j, int i2) {
        HiLog.debug(LABEL_LOG, "handleDeleteTempFormLocked start, type: %{public}d", Integer.valueOf(i2));
        if (formRecord == null) {
            HiLog.debug(LABEL_LOG, "handleDeleteTempFormLocked record is null", new Object[0]);
            return new Pair<>(Integer.valueOf((int) ERR_NOT_EXIST_ID), false);
        }
        formRecord.formUserUids.remove(Integer.valueOf(i));
        if (formRecord.formUserUids.isEmpty()) {
            if (i2 == 3) {
                int notifyProviderFormDelete = notifyProviderFormDelete(j, formRecord);
                if (notifyProviderFormDelete != 0) {
                    HiLog.error(LABEL_LOG, "handleDeleteTempFormLocked failed!", new Object[0]);
                    formRecord.formUserUids.add(Integer.valueOf(i));
                    return new Pair<>(Integer.valueOf(notifyProviderFormDelete), false);
                }
                this.tempForms.remove(Long.valueOf(j));
            }
            this.formRecords.remove(Long.valueOf(j));
            return new Pair<>(0, false);
        }
        HiLog.debug(LABEL_LOG, "handleDeleteTempFormLocked record.formUserUids size: %{public}d", Integer.valueOf(formRecord.formUserUids.size()));
        return new Pair<>(0, true);
    }

    private boolean isUpdateValid(long j, String str) {
        return j > 0 && str != null && !str.isEmpty();
    }

    private int notifyProviderFormDelete(long j, FormRecord formRecord) {
        if (formRecord == null) {
            HiLog.error(LABEL_LOG, "formRecord is null.", new Object[0]);
            return ERR_CODE_COMMON;
        } else if (formRecord.abilityName == null || formRecord.abilityName.isEmpty()) {
            HiLog.error(LABEL_LOG, "formRecord.abilityName is null or empty.", new Object[0]);
            return ERR_CODE_COMMON;
        } else if (formRecord.originalBundleName == null || formRecord.originalBundleName.isEmpty() || formRecord.bundleName == null || formRecord.bundleName.isEmpty()) {
            HiLog.error(LABEL_LOG, "formRecord.originalBundleName is null or empty.", new Object[0]);
            return ERR_CODE_COMMON;
        } else {
            HiLog.debug(LABEL_LOG, "notifyProviderFormDelete connectAbility,bundleName:%{public}s, abilityName:%{public}s", formRecord.bundleName, formRecord.abilityName);
            android.content.Intent intent = new android.content.Intent();
            intent.setFlags(32);
            String str = formRecord.originalBundleName;
            ComponentName componentName = new ComponentName(str, formRecord.abilityName + FORM_SUFFIX);
            DeletionConnection deletionConnection = new DeletionConnection(j, componentName);
            if (formRecord.needFreeInstall) {
                intent.setComponent(new ComponentName(formRecord.bundleName, formRecord.abilityName));
                intent.putExtra(PARAM_KEY_INSTALL_ON_DEMAND, true);
                intent.putExtra(PARAM_KEY_INSTALL_WITH_BACKGROUND, true);
                deletionConnection.isFreeInstall = true;
                deletionConnection.bundleName = formRecord.bundleName;
                try {
                    if (!AbilityUtils.connectAbility(aContext, intent, deletionConnection)) {
                        return ERR_SUPPLIER_DEL_FAIL;
                    }
                    return 0;
                } catch (IllegalArgumentException | IllegalStateException | SecurityException unused) {
                    HiLog.error(LABEL_LOG, "notifyProviderFormDelete connectAbility bind error", new Object[0]);
                    return ERR_SUPPLIER_DEL_FAIL;
                }
            } else if (postBinderTask(formRecord, deletionConnection)) {
                return 0;
            } else {
                intent.setComponent(componentName);
                return bindService(intent, deletionConnection);
            }
        }
    }

    private int notifyProviderFormsBatchDelete(String str, String str2, Set<Long> set) {
        if (str2 == null || str2.isEmpty()) {
            HiLog.error(LABEL_LOG, "notifyProviderFormsBatchDelete abilityName is null or empty.", new Object[0]);
            return ERR_CODE_COMMON;
        } else if (str == null || str.isEmpty()) {
            HiLog.error(LABEL_LOG, "notifyProviderFormsBatchDelete bundleName is null or empty.", new Object[0]);
            return ERR_CODE_COMMON;
        } else {
            HiLogLabel hiLogLabel = LABEL_LOG;
            HiLog.debug(hiLogLabel, "notifyProviderFormsBatchDelete bindService,package:%{public}s, class:%{public}s", str, str2 + FORM_SUFFIX);
            ComponentName componentName = new ComponentName(str, str2 + FORM_SUFFIX);
            BatchDeleteFormsConnection batchDeleteFormsConnection = new BatchDeleteFormsConnection(set, componentName);
            BinderInfo binder = getBinder(componentName, batchDeleteFormsConnection.userId);
            if (binder != null) {
                postTask(new AsyncRunnable(batchDeleteFormsConnection, componentName, binder.binder));
                return 0;
            }
            android.content.Intent intent = new android.content.Intent();
            intent.setComponent(componentName);
            intent.setFlags(32);
            try {
                if (aContext.bindServiceAsUser(intent, batchDeleteFormsConnection, 1, UserHandle.CURRENT)) {
                    return 0;
                }
                HiLog.error(LABEL_LOG, "notifyProviderFormsBatchDelete bind service failed.", new Object[0]);
                return ERR_SUPPLIER_DEL_FAIL;
            } catch (SecurityException unused) {
                HiLog.error(LABEL_LOG, "notifyProviderFormsBatchDelete bind service exception", new Object[0]);
                return ERR_SUPPLIER_DEL_FAIL;
            }
        }
    }

    private int checkEnoughForm(int i) {
        int i2 = 0;
        int i3 = 0;
        for (FormRecord formRecord : this.formRecords.values()) {
            if (isCallingUidValid(formRecord.userId, formRecord.formUserUids) && !formRecord.tempFormFlag) {
                i2++;
                if (i2 >= 512) {
                    HiLog.warn(LABEL_LOG, "already exist %{public}d forms in system", 512);
                    HiViewUtil.sendAddFormExceedLimitEvent();
                    return ERR_MAX_SYSTEM_FORMS;
                } else if (formRecord.formUserUids.contains(Integer.valueOf(i)) && (i3 = i3 + 1) >= 256) {
                    HiLog.warn(LABEL_LOG, "already use %{public}d forms", 256);
                    return ERR_MAX_RECORDS_PER_APP;
                }
            }
        }
        return 0;
    }

    private int checkTempEnoughForm() {
        if (this.tempForms.size() < 256) {
            return 0;
        }
        HiLog.warn(LABEL_LOG, "already exist %{public}d temp forms in system", 256);
        HiViewUtil.sendAddFormExceedLimitEvent();
        return ERR_MAX_SYSTEM_TEMP_FORMS;
    }

    private boolean isFormOperationUnderCurrentUserLocked(long j) {
        FormRecord formRecord = this.formRecords.get(Long.valueOf(j));
        if (formRecord != null) {
            return isCallingUidValid(formRecord.userId, formRecord.formUserUids);
        }
        HiLog.error(LABEL_LOG, "form record not found, formId:%{public}d", Long.valueOf(j));
        return false;
    }

    private void parseUpdateConfig(FormRecord formRecord, FormItemInfo formItemInfo) {
        int updateDuration = formItemInfo.getUpdateDuration();
        if (updateDuration > 0) {
            parseAsUpdateInterval(formRecord, updateDuration);
        } else {
            parseAsUpdateAt(formRecord, formItemInfo);
        }
    }

    private void parseAsUpdateAt(FormRecord formRecord, FormItemInfo formItemInfo) {
        formRecord.isEnableUpdate = false;
        formRecord.updateDuration = 0;
        String scheduledUpdateTime = formItemInfo.getScheduledUpdateTime();
        HiLog.info(LABEL_LOG, "parseAsUpdateAt updateAt:%{public}s", scheduledUpdateTime);
        if (!scheduledUpdateTime.isEmpty()) {
            String[] split = scheduledUpdateTime.split(TIME_DELIMETER);
            if (split.length != 2) {
                HiLog.error(LABEL_LOG, "parseAsUpdateAt invalid config", new Object[0]);
                return;
            }
            try {
                int parseInt = Integer.parseInt(split[0]);
                int parseInt2 = Integer.parseInt(split[1]);
                if (parseInt < 0 || parseInt > 23 || parseInt2 < 0 || parseInt2 > 59) {
                    HiLog.error(LABEL_LOG, "parseAsUpdateAt time is invalid", new Object[0]);
                    return;
                }
                formRecord.updateAtHour = parseInt;
                formRecord.updateAtMin = parseInt2;
                formRecord.isEnableUpdate = true;
            } catch (NumberFormatException unused) {
                HiLog.error(LABEL_LOG, "parseAsUpdateAt invalid hour or min", new Object[0]);
            }
        }
    }

    private void parseAsUpdateInterval(FormRecord formRecord, int i) {
        if (i <= 1) {
            formRecord.updateDuration = MIN_PERIOD;
        } else if (i >= 336) {
            formRecord.updateDuration = MAX_PERIOD;
        } else {
            formRecord.updateDuration = ((long) i) * TIME_CONVERSION;
        }
    }

    private long generateFormId() {
        return udidHash | (((long) String.valueOf(Long.valueOf(Time.getRealTimeNs())).hashCode()) & 2147483647L);
    }

    private void saveFormDBInfo(final FormDBRecord formDBRecord) {
        final long j = formDBRecord.formId;
        final boolean z = this.formDBRecords.get(Long.valueOf(j)) != null && this.formDBRecords.get(Long.valueOf(j)).userId == -1;
        this.formDBRecords.put(Long.valueOf(j), formDBRecord);
        HiLog.debug(LABEL_LOG, "saveFormDBInfo formDBRecord.formUserUids size: %{public}d, formId:%{public}d", Integer.valueOf(formDBRecord.formUserUids.size()), Long.valueOf(j));
        EventHandler eventHandler2 = this.dbEventHandler;
        if (eventHandler2 != null) {
            eventHandler2.postTask(new Runnable() {
                /* class ohos.aafwk.ability.FormAdapter.AnonymousClass2 */

                public void run() {
                    if (z) {
                        FormAdapter.this.nativeDeleteFormInfo((long) ((int) j));
                    }
                    FormAdapter.this.nativeSaveFormInfo(formDBRecord);
                }
            });
        }
    }

    private void handleNativeDeleteFormDBInfo(final long j) {
        EventHandler eventHandler2 = this.dbEventHandler;
        if (eventHandler2 != null) {
            eventHandler2.postTask(new Runnable() {
                /* class ohos.aafwk.ability.FormAdapter.AnonymousClass3 */

                public void run() {
                    FormAdapter.this.nativeDeleteFormInfo(j);
                }
            });
        }
    }

    private void handleNativeBatchDeleteFormDBInfos(final HashSet<Long> hashSet) {
        EventHandler eventHandler2 = this.dbEventHandler;
        if (eventHandler2 != null) {
            eventHandler2.postTask(new Runnable() {
                /* class ohos.aafwk.ability.FormAdapter.AnonymousClass4 */

                public void run() {
                    FormAdapter.this.nativeBatchDeleteForms(hashSet);
                }
            });
        }
    }

    private void notifyModuleNotRemovable(String str, String str2, int i) {
        String generateModuleKey = generateModuleKey(str, str2, i);
        HiLog.info(LABEL_LOG, "begin to notify %{public}s not removable", generateModuleKey);
        BundleManager instance = BundleManager.getInstance();
        if (instance == null) {
            HiLog.error(LABEL_LOG, "cannot get bms proxy when notify %{public}s not removable", generateModuleKey);
            return;
        }
        String resetCallingIdentity = IPCSkeleton.resetCallingIdentity();
        try {
            if (!instance.updateModuleRemovableFlag(str, str2, 1)) {
                HiLog.error(LABEL_LOG, "notify %{public}s not removable failed", generateModuleKey);
                IPCSkeleton.setCallingIdentity(resetCallingIdentity);
                return;
            }
        } catch (SecurityException | RemoteException e) {
            HiLog.error(LABEL_LOG, "notify %{public}s not removable, error %{public}s", generateModuleKey, e.getMessage());
        } catch (IllegalArgumentException e2) {
            HiLog.debug(LABEL_LOG, "%{public}s is not silence installed, %{public}s", generateModuleKey, e2.getMessage());
        } catch (Throwable th) {
            IPCSkeleton.setCallingIdentity(resetCallingIdentity);
            throw th;
        }
        IPCSkeleton.setCallingIdentity(resetCallingIdentity);
    }

    private String generateModuleKey(String str, String str2, int i) {
        return str + "#" + str2 + "#" + i;
    }

    private void savePersistentInfo(FormDBRecord formDBRecord) {
        saveFormDBInfo(formDBRecord);
        notifyModuleNotRemovable(formDBRecord.bundleName, formDBRecord.moduleName, formDBRecord.userId);
    }

    private void deleteFormDBInfo(long j) {
        if (!this.tempForms.contains(Long.valueOf(j))) {
            this.formDBRecords.remove(Long.valueOf(j));
            handleNativeDeleteFormDBInfo(j);
        }
    }

    private void notifyModuleRemovable(String str, String str2, int i) {
        HiLog.info(LABEL_LOG, "notifyModuleRemovable start, bundleName:%{public}s, moduleName:%{public}s", str, str2);
        if (!(StringUtils.isEmpty(str) || StringUtils.isEmpty(str2))) {
            int i2 = 0;
            for (FormDBRecord formDBRecord : this.formDBRecords.values()) {
                if (str.equals(formDBRecord.bundleName) && str2.equals(formDBRecord.moduleName) && i == formDBRecord.userId) {
                    i2++;
                }
            }
            if (i2 == 0) {
                String generateModuleKey = generateModuleKey(str, str2, i);
                HiLog.info(LABEL_LOG, "begin to notify %{public}s removable", generateModuleKey);
                BundleManager instance = BundleManager.getInstance();
                if (instance == null) {
                    HiLog.error(LABEL_LOG, "cannot get bms proxy when notify %{public}s removable", generateModuleKey);
                    return;
                }
                String resetCallingIdentity = IPCSkeleton.resetCallingIdentity();
                try {
                    if (!instance.updateModuleRemovableFlag(str, str2, 0)) {
                        HiLog.error(LABEL_LOG, "notify %{public}s removable failed", generateModuleKey);
                        IPCSkeleton.setCallingIdentity(resetCallingIdentity);
                        return;
                    }
                } catch (SecurityException | RemoteException e) {
                    HiLog.error(LABEL_LOG, "notify %{public}s removable, error %{public}s", generateModuleKey, e.getMessage());
                } catch (IllegalArgumentException e2) {
                    HiLog.debug(LABEL_LOG, "%{public}s is not silence installed, %{public}s", generateModuleKey, e2.getMessage());
                } catch (Throwable th) {
                    IPCSkeleton.setCallingIdentity(resetCallingIdentity);
                    throw th;
                }
                IPCSkeleton.setCallingIdentity(resetCallingIdentity);
            }
        }
    }

    private void delPersistentInfo(long j) {
        FormDBRecord formDBRecord = this.formDBRecords.get(Long.valueOf(j));
        deleteFormDBInfo(j);
        if (formDBRecord != null) {
            notifyModuleRemovable(formDBRecord.bundleName, formDBRecord.moduleName, formDBRecord.userId);
        }
    }

    private int bindSupplier(long j, FormItemInfo formItemInfo, IntentParams intentParams) {
        HiLog.info(LABEL_LOG, "addForm connectAbility,bundle:%{public}s, abilityName:%{public}s, original:%{public}s", formItemInfo.getBundleName(), formItemInfo.getAbilityName(), formItemInfo.getOriginalBundleName());
        String originalBundleName = formItemInfo.getOriginalBundleName();
        ComponentName componentName = new ComponentName(originalBundleName, formItemInfo.getAbilityName() + FORM_SUFFIX);
        FormConnection formConnection = new FormConnection(j, formItemInfo, intentParams, componentName);
        android.content.Intent intent = new android.content.Intent();
        intent.setComponent(componentName);
        intent.addFlags(32);
        BinderInfo binder = getBinder(componentName, formConnection.userId);
        if (binder == null) {
            return bindService(intent, formConnection);
        }
        postTask(new AsyncRunnable(formConnection, componentName, binder.binder));
        return 0;
    }

    private int reBindSupplier(long j, FormRecord formRecord, Intent intent) {
        if (formRecord == null) {
            HiLog.error(LABEL_LOG, "reBind supplier failed, record is null!", new Object[0]);
            return ERR_CODE_COMMON;
        }
        HiLog.debug(LABEL_LOG, "refresh connectability,bundleName:%{public}s, abilityName:%{public}s", formRecord.bundleName, formRecord.abilityName);
        String str = formRecord.originalBundleName;
        ComponentName componentName = new ComponentName(str, formRecord.abilityName + FORM_SUFFIX);
        RefreshConnection refreshConnection = new RefreshConnection(j, intent, componentName);
        android.content.Intent intent2 = new android.content.Intent();
        intent2.addFlags(32);
        if (formRecord.needFreeInstall) {
            intent2.setComponent(new ComponentName(formRecord.bundleName, formRecord.abilityName));
            intent2.putExtra(PARAM_KEY_INSTALL_ON_DEMAND, true);
            intent2.putExtra(PARAM_KEY_INSTALL_WITH_BACKGROUND, true);
            refreshConnection.isFreeInstall = true;
            refreshConnection.bundleName = formRecord.bundleName;
            try {
                if (!AbilityUtils.connectAbility(aContext, intent2, refreshConnection)) {
                    return ERR_SUPPLIER_DEL_FAIL;
                }
                return 0;
            } catch (IllegalArgumentException | IllegalStateException | SecurityException unused) {
                HiLog.error(LABEL_LOG, "reBindSupplier connectAbility bind error", new Object[0]);
                return ERR_SUPPLIER_DEL_FAIL;
            }
        } else if (postBinderTask(formRecord, refreshConnection)) {
            return 0;
        } else {
            intent2.setComponent(componentName);
            return bindService(intent2, refreshConnection);
        }
    }

    /* access modifiers changed from: package-private */
    public int refreshForm(long j, Intent intent) {
        boolean z;
        HiLog.debug(LABEL_LOG, "refresh form,formId:%{public}d", Long.valueOf(j));
        boolean isScreenOn = isScreenOn();
        synchronized (this.FORM_LOCK) {
            FormRecord formRecord = this.formRecords.get(Long.valueOf(j));
            if (formRecord == null) {
                HiLog.error(LABEL_LOG, "not exist such form:%{public}d", Long.valueOf(j));
                return ERR_NOT_EXIST_ID;
            } else if (formRecord.userId != ActivityManager.getCurrentUser()) {
                formRecord.needRefresh = true;
                HiLog.debug(LABEL_LOG, "not current user, set refresh flag, do not refresh", new Object[0]);
                return 0;
            } else if (!isScreenOn) {
                formRecord.needRefresh = true;
                HiLog.debug(LABEL_LOG, "screen off, set refresh flag, do not refresh now", new Object[0]);
                return 0;
            } else {
                Iterator<FormHostRecord> it = this.clientRecords.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (it.next().isEnableRefresh(j)) {
                            z = true;
                            break;
                        }
                    } else {
                        z = false;
                        break;
                    }
                }
                if (!z) {
                    formRecord.needRefresh = true;
                    HiLog.debug(LABEL_LOG, "no one needReresh, set refresh flag, do not refresh now", new Object[0]);
                    return 0;
                }
                HiLog.debug(LABEL_LOG, "the form number is %{private}d before refreshed, the formHost number is %{private}d before refreshed.", Integer.valueOf(this.formRecords.size()), Integer.valueOf(this.clientRecords.size()));
                FormRecord formAbilityInfo = getFormAbilityInfo(formRecord);
                formAbilityInfo.isInited = formRecord.isInited;
                formAbilityInfo.needFreeInstall = formRecord.needFreeInstall;
                formAbilityInfo.versionUpgrade = formRecord.versionUpgrade;
                return reBindSupplier(j, formAbilityInfo, intent);
            }
        }
    }

    private void initReceiver(Context context) {
        this.bundleMonitor = new BundleMonitor();
        this.bundleMonitor.register(context, null, UserHandle.ALL, true);
        this.supplierReceiver = new SupplierReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonEventSupport.COMMON_EVENT_ABILITY_UPDATED);
        intentFilter.addAction(CommonEventSupport.COMMON_EVENT_ABILITY_REMOVED);
        context.registerReceiverAsUser(this.supplierReceiver, UserHandle.ALL, intentFilter, null, null);
        this.userReceiver = new UserReceiver();
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(ActionMapper.ACTION_USER_REMOVED);
        context.registerReceiver(this.userReceiver, intentFilter2);
        this.bundleDataReceiver = new BundleDataReceiver();
        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("android.intent.action.PACKAGE_DATA_CLEARED");
        intentFilter3.addDataScheme(SCHEME_PACKAGE);
        context.registerReceiverAsUser(this.bundleDataReceiver, UserHandle.ALL, intentFilter3, null, null);
        IntentFilter intentFilter4 = new IntentFilter();
        intentFilter4.addAction("android.intent.action.CONFIGURATION_CHANGED");
        context.registerReceiverAsUser(this.bundleDataReceiver, UserHandle.ALL, intentFilter4, null, null);
    }

    /* access modifiers changed from: package-private */
    public static class RefreshRunnable implements Runnable {
        final long formId;
        int hour;
        boolean isUpdateAt;
        int min;
        long period;
        long refreshTime = Long.MAX_VALUE;

        RefreshRunnable(long j, long j2) {
            this.formId = j;
            this.period = j2;
            this.hour = -1;
            this.min = -1;
            this.isUpdateAt = false;
        }

        RefreshRunnable(long j, int i, int i2) {
            this.formId = j;
            this.hour = i;
            this.min = i2;
            this.period = -1;
            this.isUpdateAt = true;
        }

        public void run() {
            FormAdapter.getInstance().refreshForm(this.formId, new Intent());
        }
    }

    /* access modifiers changed from: private */
    public static class RefreshConnection extends BaseConnection {
        private Intent intent;

        RefreshConnection(long j, Intent intent2, ComponentName componentName) {
            super(componentName);
            this.formId = j;
            this.intent = intent2;
        }

        @Override // ohos.aafwk.ability.FormAdapter.BaseConnection
        public void handleOnServiceConnected(ComponentName componentName, IBinder iBinder) {
            HiLog.debug(FormAdapter.LABEL_LOG, "refresh handleOnServiceConnected. name is %{public}s", componentName.toString());
            super.onServiceConnected(componentName, iBinder);
            IRemoteObject orElse = IPCAdapter.translateToIRemoteObject(iBinder).orElse(null);
            if (orElse == null) {
                HiLog.error(FormAdapter.LABEL_LOG, "refresh onServiceConnected failed to get supplier remote object.", new Object[0]);
                disconnectSupply(false);
                return;
            }
            SupplyHostClient.getInstance().addConnection(this);
            IAbilityFormProvider asProxy = IAbilityFormProvider.FormProviderStub.asProxy(orElse);
            try {
                if (this.intent.hasParameter(FormAdapter.JS_MESSAGE_KEY)) {
                    asProxy.fireFormEvent(this.formId, this.intent.getStringParam(FormAdapter.JS_MESSAGE_KEY), FormAdapter.buildDefaultIntent(null, this), SupplyHostClient.getInstance());
                } else if (this.intent.hasParameter(FormAdapter.RECREATE_FORM_KEY)) {
                    this.intent.removeParam(FormAdapter.RECREATE_FORM_KEY);
                    this.intent.setParam(ISupplyHost.ACQUIRE_TYPE, 2);
                    FormAdapter.buildDefaultIntent(this.intent, this);
                    asProxy.acquireProviderFormInfo(this.intent, SupplyHostClient.getInstance());
                } else {
                    asProxy.notifyFormUpdate(this.formId, FormAdapter.buildDefaultIntent(null, this), SupplyHostClient.getInstance());
                }
            } catch (RemoteException unused) {
                HiLog.error(FormAdapter.LABEL_LOG, "onServiceConnected acquireAbilityForm exception.", new Object[0]);
                disconnectSupply(true);
            }
        }

        @Override // ohos.aafwk.ability.FormAdapter.BaseConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            HiLog.debug(FormAdapter.LABEL_LOG, "refresh onServiceConnected. name is %{public}s", componentName.toString());
            this.connectId = (long) FormAdapter.getInstance().putBinder(componentName, iBinder, this);
            FormAdapter.getInstance().postTask(new AsyncRunnable(this, componentName, iBinder));
        }
    }

    /* access modifiers changed from: private */
    public static class DeletionConnection extends BaseConnection {
        DeletionConnection(long j, ComponentName componentName) {
            super(componentName);
            this.formId = j;
        }

        @Override // ohos.aafwk.ability.FormAdapter.BaseConnection
        public void handleOnServiceConnected(ComponentName componentName, IBinder iBinder) {
            HiLog.debug(FormAdapter.LABEL_LOG, "delForm onServiceConnected.", new Object[0]);
            super.onServiceConnected(componentName, iBinder);
            IRemoteObject orElse = IPCAdapter.translateToIRemoteObject(iBinder).orElse(null);
            if (orElse == null) {
                HiLog.error(FormAdapter.LABEL_LOG, "delForm onServiceConnected failed to get supplier remote object.", new Object[0]);
                disconnectSupply(false);
                return;
            }
            IAbilityFormProvider asProxy = IAbilityFormProvider.FormProviderStub.asProxy(orElse);
            SupplyHostClient.getInstance().addConnection(this);
            try {
                asProxy.notifyFormDelete(this.formId, FormAdapter.buildDefaultIntent(null, this), SupplyHostClient.getInstance());
            } catch (RemoteException unused) {
                HiLog.error(FormAdapter.LABEL_LOG, "onServiceConnected notifyDeleteAbilityForm exception.", new Object[0]);
                disconnectSupply(true);
            }
        }

        @Override // ohos.aafwk.ability.FormAdapter.BaseConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            this.connectId = (long) FormAdapter.getInstance().putBinder(componentName, iBinder, this);
            FormAdapter.getInstance().postTask(new AsyncRunnable(this, componentName, iBinder));
        }
    }

    /* access modifiers changed from: private */
    public static class BatchDeleteFormsConnection extends BaseConnection {
        private Set<Long> formIds;

        BatchDeleteFormsConnection(Set<Long> set, ComponentName componentName) {
            super(componentName);
            this.formIds = set;
        }

        @Override // ohos.aafwk.ability.FormAdapter.BaseConnection
        public void handleOnServiceConnected(ComponentName componentName, IBinder iBinder) {
            HiLog.debug(FormAdapter.LABEL_LOG, "batch delForms handleOnServiceConnected.", new Object[0]);
            super.onServiceConnected(componentName, iBinder);
            IRemoteObject orElse = IPCAdapter.translateToIRemoteObject(iBinder).orElse(null);
            if (orElse == null) {
                HiLog.error(FormAdapter.LABEL_LOG, "batch delForms onServiceConnected failed to get supplier remote object.", new Object[0]);
                disconnectSupply(false);
                return;
            }
            IAbilityFormProvider asProxy = IAbilityFormProvider.FormProviderStub.asProxy(orElse);
            SupplyHostClient.getInstance().addConnection(this);
            try {
                asProxy.notifyFormsDelete(this.formIds, FormAdapter.buildDefaultIntent(null, this), SupplyHostClient.getInstance());
            } catch (RemoteException unused) {
                HiLog.error(FormAdapter.LABEL_LOG, "onServiceConnected BatchDeleteFormsConnection exception.", new Object[0]);
                disconnectSupply(true);
            }
        }

        @Override // ohos.aafwk.ability.FormAdapter.BaseConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            HiLog.debug(FormAdapter.LABEL_LOG, "BatchDeleteFormsConnection onServiceConnected %{public}s", componentName.getPackageName());
            this.connectId = (long) FormAdapter.getInstance().putBinder(componentName, iBinder, this);
            FormAdapter.getInstance().postTask(new AsyncRunnable(this, componentName, iBinder));
        }
    }

    /* access modifiers changed from: private */
    public static class FormConnection extends BaseConnection {
        private FormItemInfo info;
        private IntentParams intentParams;

        public FormConnection(long j, FormItemInfo formItemInfo, IntentParams intentParams2, ComponentName componentName) {
            super(componentName);
            this.formId = j;
            this.info = formItemInfo;
            this.intentParams = intentParams2;
        }

        @Override // ohos.aafwk.ability.FormAdapter.BaseConnection
        public void handleOnServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            HiLog.info(FormAdapter.LABEL_LOG, "addForm handleOnServiceConnected, bundle is %{public}s, class is %{public}s", componentName.getPackageName(), componentName.getClassName());
            IRemoteObject orElse = IPCAdapter.translateToIRemoteObject(iBinder).orElse(null);
            if (orElse == null) {
                HiLog.error(FormAdapter.LABEL_LOG, "addForm onServiceConnected failed to get supplier remote object.", new Object[0]);
                disconnectSupply(false);
                return;
            }
            SupplyHostClient.getInstance().addConnection(this);
            IAbilityFormProvider asProxy = IAbilityFormProvider.FormProviderStub.asProxy(orElse);
            try {
                Intent buildCreateFormIntent = FormAdapter.buildCreateFormIntent(this.formId, this.info.getFormName(), this.info.getSpecificationId(), this.info.isTemporaryForm());
                buildCreateFormIntent.setParam("ohos.extra.param.key.form_customize", this.intentParams);
                buildCreateFormIntent.setParam(ISupplyHost.ACQUIRE_TYPE, 1);
                FormAdapter.buildDefaultIntent(buildCreateFormIntent, this);
                asProxy.acquireProviderFormInfo(buildCreateFormIntent, SupplyHostClient.getInstance());
            } catch (RemoteException unused) {
                HiLog.error(FormAdapter.LABEL_LOG, "onServiceConnected acquireProviderFormInfo exception.", new Object[0]);
                disconnectSupply(true);
            }
        }

        @Override // ohos.aafwk.ability.FormAdapter.BaseConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            HiLog.debug(FormAdapter.LABEL_LOG, "addForm onServiceConnected %{public}s", componentName.getPackageName());
            this.connectId = (long) FormAdapter.getInstance().putBinder(componentName, iBinder, this);
            FormAdapter.getInstance().postTask(new AsyncRunnable(this, componentName, iBinder));
        }
    }

    /* access modifiers changed from: private */
    public static Intent buildCreateFormIntent(long j, String str, int i, boolean z) {
        Intent intent = new Intent();
        intent.setParam(AbilitySlice.PARAM_FORM_ID_KEY, (int) j);
        intent.setParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, j);
        intent.setParam(AbilitySlice.PARAM_FORM_NAME_KEY, str);
        intent.setParam(AbilitySlice.PARAM_FORM_DIMENSION_KEY, i);
        intent.setParam(AbilitySlice.PARAM_FORM_TEMPORARY_KEY, z);
        return intent;
    }

    /* access modifiers changed from: private */
    public static Intent buildDefaultIntent(Intent intent, BaseConnection baseConnection) {
        if (intent == null) {
            intent = new Intent();
        }
        intent.setParam(ISupplyHost.FORM_CONNECT_ID, baseConnection.connectId);
        intent.setParam(ISupplyHost.FORM_SUPPLY_INFO, baseConnection.getSupplyInfo());
        return intent;
    }

    /* access modifiers changed from: private */
    public static class CastTempConnection extends BaseConnection {
        CastTempConnection(long j, ComponentName componentName) {
            super(componentName);
            this.formId = j;
        }

        @Override // ohos.aafwk.ability.FormAdapter.BaseConnection
        public void handleOnServiceConnected(ComponentName componentName, IBinder iBinder) {
            HiLog.debug(FormAdapter.LABEL_LOG, "notify cast form onServiceConnected.", new Object[0]);
            super.onServiceConnected(componentName, iBinder);
            IRemoteObject orElse = IPCAdapter.translateToIRemoteObject(iBinder).orElse(null);
            if (orElse == null) {
                HiLog.error(FormAdapter.LABEL_LOG, "notify cast form onServiceConnected failed to get supplier remote object.", new Object[0]);
                disconnectSupply(false);
                return;
            }
            IAbilityFormProvider asProxy = IAbilityFormProvider.FormProviderStub.asProxy(orElse);
            SupplyHostClient.getInstance().addConnection(this);
            try {
                asProxy.notifyFormCastTempForm(this.formId, FormAdapter.buildDefaultIntent(null, this), SupplyHostClient.getInstance());
            } catch (RemoteException unused) {
                HiLog.error(FormAdapter.LABEL_LOG, "onServiceConnected notifyDeleteAbilityForm exception.", new Object[0]);
                disconnectSupply(true);
            }
        }

        @Override // ohos.aafwk.ability.FormAdapter.BaseConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            this.connectId = (long) FormAdapter.getInstance().putBinder(componentName, iBinder, this);
            FormAdapter.getInstance().postTask(new AsyncRunnable(this, componentName, iBinder));
        }
    }

    /* access modifiers changed from: private */
    public static class EventNotifyConnection extends BaseConnection {
        private List<Long> formIds;

        EventNotifyConnection(List<Long> list, ComponentName componentName) {
            super(componentName);
            this.formIds = list;
        }

        @Override // ohos.aafwk.ability.FormAdapter.BaseConnection
        public void handleOnServiceConnected(ComponentName componentName, IBinder iBinder) {
            HiLog.debug(FormAdapter.LABEL_LOG, "event notify connection handleOnServiceConnected: %{public}s", componentName.toString());
            super.onServiceConnected(componentName, iBinder);
            IRemoteObject orElse = IPCAdapter.translateToIRemoteObject(iBinder).orElse(null);
            if (orElse == null) {
                HiLog.error(FormAdapter.LABEL_LOG, "get remote error in EventNotifyConnection's onServiceConnected.", new Object[0]);
                disconnectSupply(false);
                return;
            }
            IAbilityFormProvider asProxy = IAbilityFormProvider.FormProviderStub.asProxy(orElse);
            SupplyHostClient.getInstance().addConnection(this);
            try {
                HiLog.debug(FormAdapter.LABEL_LOG, "event notify connection onServiceConnected.", new Object[0]);
                asProxy.eventNotify(FormAdapter.getInstance().buildNotifyEvents(this.formIds), FormAdapter.buildDefaultIntent(null, this), SupplyHostClient.getInstance());
            } catch (Exception e) {
                HiLog.error(FormAdapter.LABEL_LOG, "eventNotify error in handleOnServiceConnected. %{public}", e.getMessage());
                disconnectSupply(true);
            }
        }

        @Override // ohos.aafwk.ability.FormAdapter.BaseConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            String str;
            HiLogLabel hiLogLabel = FormAdapter.LABEL_LOG;
            Object[] objArr = new Object[1];
            if (componentName == null) {
                str = "";
            } else {
                str = componentName.toString();
            }
            objArr[0] = str;
            HiLog.debug(hiLogLabel, "event notify connection onServiceConnected: %{public}s", objArr);
            this.connectId = (long) FormAdapter.getInstance().putBinder(componentName, iBinder, this);
            FormAdapter.getInstance().postTask(new AsyncRunnable(this, componentName, iBinder));
        }
    }

    /* access modifiers changed from: package-private */
    public static abstract class BaseConnection implements ServiceConnection {
        String bundleName;
        long connectId = 0;
        long createTime = System.currentTimeMillis();
        long formId;
        boolean isFreeInstall = false;
        ComponentName name;
        int userId = ActivityManager.getCurrentUser();

        public abstract void handleOnServiceConnected(ComponentName componentName, IBinder iBinder);

        public BaseConnection(ComponentName componentName) {
            this.name = componentName;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            this.name = componentName;
            if (this.isFreeInstall) {
                HiLog.debug(FormAdapter.LABEL_LOG, "it is free install this time.", new Object[0]);
                FormAdapter.getInstance().handleFormFreeInstalled(this.formId, this.bundleName);
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            HiLog.debug(FormAdapter.LABEL_LOG, "onServiceDisconnected come, name is %{public}s", componentName.toString());
            if (this.connectId > 0) {
                SupplyHostClient.getInstance().delConnectAbility(this.connectId, getSupplyInfo());
            } else {
                FormAdapter.getInstance().cleanBinder(getSupplyInfo());
            }
        }

        public void onNullBinding(ComponentName componentName) {
            HiLog.debug(FormAdapter.LABEL_LOG, "onNullBinding come, name is %{public}s", componentName.toString());
            if (this.connectId > 0) {
                SupplyHostClient.getInstance().delConnectAbility(this.connectId, getSupplyInfo());
            } else {
                FormAdapter.getInstance().cleanBinder(getSupplyInfo());
            }
        }

        public void onBindingDied(ComponentName componentName) {
            HiLog.debug(FormAdapter.LABEL_LOG, "onBindingDied come, name is %{public}s", componentName.toString());
            if (this.connectId > 0) {
                SupplyHostClient.getInstance().delConnectAbility(this.connectId, getSupplyInfo());
            } else {
                FormAdapter.getInstance().cleanBinder(getSupplyInfo());
            }
        }

        public void disconnectSupply(boolean z) {
            if (z) {
                SupplyHostClient.getInstance().cleanConnection(this.connectId);
            }
            if (this.connectId != 0) {
                FormAdapter.disconnectAbility(FormAdapter.getInstance().getContext(), this);
            } else {
                FormAdapter.getInstance().deleteBinder(getSupplyInfo());
            }
        }

        public String getSupplyInfo() {
            if (this.name == null) {
                HiLog.warn(FormAdapter.LABEL_LOG, "getSupplyInfo error, ComponentName is null", new Object[0]);
                return null;
            }
            return this.name.getPackageName() + "::" + this.name.getClassName() + "::" + this.userId;
        }
    }

    /* access modifiers changed from: private */
    public static class AsyncRunnable implements Runnable {
        private IBinder myBinder;
        private BaseConnection myConnection;
        private ComponentName myName;

        AsyncRunnable(BaseConnection baseConnection, ComponentName componentName, IBinder iBinder) {
            this.myConnection = baseConnection;
            this.myName = componentName;
            this.myBinder = iBinder;
        }

        public void run() {
            BaseConnection baseConnection = this.myConnection;
            if (baseConnection != null) {
                baseConnection.handleOnServiceConnected(this.myName, this.myBinder);
            }
        }
    }

    private static class FreeInstallConnection implements ServiceConnection {
        private FreeInstallConnection() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            HiLog.debug(FormAdapter.LABEL_LOG, "free install success.", new Object[0]);
            FormAdapter.disconnectAbility(FormAdapter.getInstance().getContext(), this);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            HiLog.debug(FormAdapter.LABEL_LOG, "free install onServiceDisconnected come.", new Object[0]);
        }
    }

    /* access modifiers changed from: private */
    public static class BundleMonitor extends PackageMonitor {
        private BundleMonitor() {
        }

        public void onPackageRemoved(String str, int i) {
            HiLog.info(FormAdapter.LABEL_LOG, "onPackageRemoved,packageName:%{public}s, uid=%{public}d", str, Integer.valueOf(i));
            if (str != null && !str.isEmpty() && i > 0) {
                FormAdapter.getInstance().handleFormHostDataCleared(i);
            }
        }
    }

    /* access modifiers changed from: private */
    public static class SupplierReceiver extends BroadcastReceiver {
        private static final String KEY_BUNDLE_NAME = "bundleName";
        private static final String KEY_UID = "uid";

        private SupplierReceiver() {
        }

        public void onReceive(Context context, android.content.Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                String stringExtra = intent.getStringExtra("bundleName");
                int intExtra = intent.getIntExtra(KEY_UID, 0);
                if (action != null && !action.isEmpty() && stringExtra != null && !stringExtra.isEmpty() && intExtra > 0) {
                    if (CommonEventSupport.COMMON_EVENT_ABILITY_REMOVED.equals(action)) {
                        HiLog.debug(FormAdapter.LABEL_LOG, "bundle removed, bundleName: %{public}s, uid:%{public}d", stringExtra, Integer.valueOf(intExtra));
                        FormAdapter.getInstance().handleSupplierRemoved(stringExtra, intExtra);
                    } else if (CommonEventSupport.COMMON_EVENT_ABILITY_UPDATED.equals(action)) {
                        HiLog.debug(FormAdapter.LABEL_LOG, "bundle updated, bundleName: %{public}s, uid:%{public}d", stringExtra, Integer.valueOf(intExtra));
                        FormAdapter.getInstance().handleSupplierUpdated(stringExtra, intExtra);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static class UserReceiver extends BroadcastReceiver {
        private UserReceiver() {
        }

        public void onReceive(Context context, android.content.Intent intent) {
            if (intent != null && ActionMapper.ACTION_USER_REMOVED.equals(intent.getAction())) {
                int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -1);
                if (intExtra == -1) {
                    HiLog.error(FormAdapter.LABEL_LOG, "userId get failed!", new Object[0]);
                } else {
                    FormAdapter.getInstance().handleUserRemoved(intExtra);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static class BundleDataReceiver extends BroadcastReceiver {
        private Locale locale = Locale.getDefault();

        BundleDataReceiver() {
        }

        public void onReceive(Context context, android.content.Intent intent) {
            Uri data;
            String schemeSpecificPart;
            if (intent != null) {
                String action = intent.getAction();
                char c = 65535;
                int hashCode = action.hashCode();
                if (hashCode != 158859398) {
                    if (hashCode == 267468725 && action.equals("android.intent.action.PACKAGE_DATA_CLEARED")) {
                        c = 1;
                    }
                } else if (action.equals("android.intent.action.CONFIGURATION_CHANGED")) {
                    c = 0;
                }
                if (c != 0) {
                    if (c == 1 && (data = intent.getData()) != null && (schemeSpecificPart = data.getSchemeSpecificPart()) != null && !schemeSpecificPart.isEmpty()) {
                        FormAdapter.getInstance().handleBundleDataCleared(schemeSpecificPart, intent.getIntExtra("android.intent.extra.UID", 0));
                    }
                } else if (checkConfigChanged()) {
                    FormAdapter.getInstance().handleConfigChanged();
                }
            }
        }

        private boolean checkConfigChanged() {
            Locale locale2 = Locale.getDefault();
            if (locale2 == null || !locale2.equals(this.locale)) {
                HiLog.info(FormAdapter.LABEL_LOG, "locale changed", new Object[0]);
                this.locale = locale2;
                return true;
            }
            HiLog.debug(FormAdapter.LABEL_LOG, "don't need to refresh form when unsupported config changed", new Object[0]);
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public static class UpdatedForm {
        FormInfo formInfo;
        FormResource formResource;

        UpdatedForm() {
        }
    }

    /* access modifiers changed from: package-private */
    public static class FormTimerCfg {
        boolean enableUpdate = false;
        int updateAtHour = -1;
        int updateAtMin = -1;
        long updateDuration = 0;

        FormTimerCfg() {
        }
    }

    private boolean generateUdidHash() {
        if (udidHash != 0) {
            return true;
        }
        Optional<String> udid = getUdid();
        if (!udid.isPresent()) {
            return false;
        }
        udidHash = (((long) udid.get().hashCode()) & 16777215) << 32;
        HiLog.info(LABEL_LOG, "FormAdapter generate hash %{public}d", Long.valueOf(udidHash));
        return true;
    }

    private Optional<String> getUdid() {
        if (deviceSvc == null) {
            deviceSvc = IDeviceIdentifiersPolicyService.Stub.asInterface(ServiceManager.getService(DEVICE_ID_SERVICE));
            if (deviceSvc == null) {
                HiLog.error(LABEL_LOG, "remote service get failed.", new Object[0]);
                return Optional.empty();
            }
        }
        try {
            String udid = deviceSvc.getUDID();
            if (udid == null) {
                HiLog.warn(LABEL_LOG, "udid is not exist, use sn instead.", new Object[0]);
                String serial = deviceSvc.getSerial();
                if (serial != null) {
                    if (!UNKNOWN.equals(serial)) {
                        return Optional.of(serial);
                    }
                }
                HiLog.warn(LABEL_LOG, "get sn info failed.", new Object[0]);
                return Optional.empty();
            } else if (!UDID_EXCEPTION.equals(udid)) {
                return Optional.of(udid);
            } else {
                HiLog.warn(LABEL_LOG, "get udid failed.", new Object[0]);
                return Optional.empty();
            }
        } catch (android.os.RemoteException e) {
            HiLog.warn(LABEL_LOG, "RemoteException happens in getUDID! %{public}s", e.getMessage());
            return Optional.empty();
        } catch (SecurityException e2) {
            HiLog.warn(LABEL_LOG, "SecurityException happens in getUDID! %{public}s", e2.getMessage());
            return Optional.empty();
        }
    }

    private FormRecord getFormAbilityInfo(FormRecord formRecord) {
        FormRecord formRecord2 = new FormRecord();
        formRecord2.bundleName = formRecord.bundleName;
        formRecord2.originalBundleName = formRecord.originalBundleName;
        formRecord2.abilityName = formRecord.abilityName;
        return formRecord2;
    }

    private FormRecord getFormRecordByDBRecord(FormDBRecord formDBRecord) {
        FormRecord formRecord = new FormRecord();
        formRecord.bundleName = formDBRecord.bundleName;
        formRecord.originalBundleName = formDBRecord.originalBundleName;
        formRecord.abilityName = formDBRecord.abilityName;
        return formRecord;
    }

    private void updateDBRecord(long j, int i, FormRecord formRecord) {
        FormDBRecord formDBRecord;
        FormDBRecord formDBRecord2 = this.formDBRecords.get(Long.valueOf(j));
        if (formDBRecord2 != null) {
            if (formDBRecord2.formUserUids == null) {
                formDBRecord2.formUserUids = new HashSet<>();
            }
            formDBRecord2.formUserUids.add(Integer.valueOf(i));
            formDBRecord2.moduleName = formRecord.moduleName;
            formDBRecord2.originalBundleName = formRecord.originalBundleName;
            formDBRecord2.abilityName = formRecord.abilityName;
            formDBRecord = formDBRecord2;
        } else {
            formDBRecord = new FormDBRecord(j, formRecord.userId, formRecord.bundleName, formRecord.moduleName, formRecord.originalBundleName, formRecord.abilityName, formRecord.formUserUids);
        }
        savePersistentInfo(formDBRecord);
    }

    private boolean isCallingUidValid(int i, HashSet<Integer> hashSet) {
        if (i == ActivityManager.getCurrentUser()) {
            return true;
        }
        if (hashSet == null || !hashSet.contains(Integer.valueOf(IPCSkeleton.getCallingUid()))) {
            return false;
        }
        return true;
    }

    static void disconnectAbility(Context context, ServiceConnection serviceConnection) {
        try {
            AbilityUtils.disconnectAbility(context, serviceConnection);
        } catch (IllegalArgumentException e) {
            HiLog.warn(LABEL_LOG, "disconnectAbility fail: %{public}s", e.getMessage());
        }
    }

    private int bindService(android.content.Intent intent, BaseConnection baseConnection) {
        LogPower.push(148, PG_BIND_SERVICE, intent.getComponent().getPackageName(), "0", FOUNDATION_PKG_NAME);
        if (ServiceNotResponseHandle.getInstance().addRequest(baseConnection) != 0) {
            return ERR_BIND_SUPPLIER_FAILED;
        }
        try {
            if (aContext.bindServiceAsUser(intent, baseConnection, 1, UserHandle.CURRENT)) {
                return 0;
            }
            HiLog.error(LABEL_LOG, "bindServiceAsUser failed, app: %{public}s", baseConnection.getSupplyInfo());
            ServiceNotResponseHandle.getInstance().removeRequest(baseConnection);
            return ERR_BIND_SUPPLIER_FAILED;
        } catch (SecurityException e) {
            HiLog.error(LABEL_LOG, "bindServiceAsUser ex %{public}s", e.getMessage());
            ServiceNotResponseHandle.getInstance().removeRequest(baseConnection);
            return ERR_BIND_SUPPLIER_FAILED;
        }
    }

    /* access modifiers changed from: private */
    public class BinderInfo {
        IBinder binder;
        BaseConnection connection;
        int count;

        private BinderInfo() {
            this.binder = null;
            this.connection = null;
            this.count = 1;
        }
    }
}
