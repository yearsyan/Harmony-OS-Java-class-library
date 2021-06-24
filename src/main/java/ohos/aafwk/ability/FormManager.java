package ohos.aafwk.ability;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ohos.aafwk.ability.FormException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.ComponentProvider;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.sysability.samgr.SysAbilityManager;

public class FormManager {
    private static final int ADD_FORM = 2;
    private static final int BUNDLEMGR_SERVICE_ID = 401;
    private static final int CAST_TEMP_FORM = 10;
    private static final int CHECK_AND_DELETE_INVALID_FORMS = 12;
    private static final int DEATH_RECIPIENT_FLAG = 0;
    private static final String DESCRIPTOR = "OHOS.AppExecFwk.IFormMgr";
    private static final int EVENT_NOTIFY = 11;
    private static final int FLAG_HAS_OBJECT = 1;
    private static final int FLAG_NO_OBJECT = 0;
    private static final int FORMMGR_SERVICE_ID = 403;
    private static final Object INSTANCE_LOCK = new Object();
    static final int IN_RECOVERING = 2;
    private static final boolean JAVA_FORM = false;
    private static final boolean JS_FORM = true;
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 218108160, "FormManager");
    private static final int MAX_VISIBLE_NOTIFY_LIST = 32;
    static final int NOT_IN_RECOVERY = 0;
    static final int RECOVER_FAIL = 1;
    private static final int REQUEST_FORM = 7;
    private static final int UPDATE_FORM = 4;
    private static List<DeathCallback> callbacks = new ArrayList();
    private static IRemoteObject.DeathRecipient deathRecipient;
    private static volatile FormManager instance;
    private static volatile int recoverStatus = 0;
    private static volatile boolean resetFlag = false;
    private IRemoteObject remote;

    /* access modifiers changed from: package-private */
    public interface DeathCallback {
        void onDeathReceived();
    }

    public static FormManager getInstance() {
        if (instance == null) {
            synchronized (INSTANCE_LOCK) {
                if (instance == null) {
                    IRemoteObject sysAbility = SysAbilityManager.getSysAbility(403);
                    if (sysAbility == null) {
                        HiLog.warn(LABEL_LOG, "FormManager getInstance failed, remote is null", new Object[0]);
                        return null;
                    }
                    deathRecipient = new FormManagerDeathRecipient();
                    if (!sysAbility.addDeathRecipient(deathRecipient, 0)) {
                        HiLog.debug(LABEL_LOG, "FormManager register FormManagerDeathRecipient failed", new Object[0]);
                    }
                    instance = new FormManager(sysAbility);
                }
            }
        }
        return instance;
    }

    public FormManager(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    public Form addForm(Intent intent, IRemoteObject iRemoteObject) throws FormException {
        HiLog.info(LABEL_LOG, "addForm.", new Object[0]);
        if (intent == null || iRemoteObject == null) {
            throw new FormException(FormException.FormError.INPUT_PARAM_INVALID, "intent or clientStub can not be null");
        }
        if (resetFlag) {
            resetRemoteObject();
        }
        if (this.remote != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            MessageOption messageOption = new MessageOption();
            try {
                if (!obtain.writeInterfaceToken(DESCRIPTOR)) {
                    reclaimParcel(obtain, obtain2);
                    return null;
                }
                IntentParams param = intent.getParam(AbilitySlice.PARAM_FORM_CUSTOMIZE_KEY);
                intent.removeParam(AbilitySlice.PARAM_FORM_CUSTOMIZE_KEY);
                if (param == null) {
                    param = new IntentParams();
                }
                for (Map.Entry<String, Object> entry : param.getParams().entrySet()) {
                    if (!isTypeValid(entry.getValue())) {
                        throw new IllegalArgumentException("the type or contained type is not support to transport when acquireForm : " + entry.getValue().getClass());
                    }
                }
                obtain.writeSequenceable(intent);
                obtain.writeRemoteObject(iRemoteObject);
                obtain.writeSequenceable(param);
                if (this.remote.sendRequest(2, obtain, obtain2, messageOption)) {
                    int readInt = obtain2.readInt();
                    if (readInt != 0) {
                        throw new FormException(getErrFromResult(readInt));
                    } else if (obtain2.readInt() != 1) {
                        reclaimParcel(obtain, obtain2);
                        return null;
                    } else {
                        Form createFromParcel = Form.createFromParcel(obtain2);
                        reclaimParcel(obtain, obtain2);
                        return createFromParcel;
                    }
                } else {
                    HiLog.error(LABEL_LOG, "FormManager::addForm sendRequest failed", new Object[0]);
                    throw new FormException(FormException.FormError.SEND_FMS_MSG_ERROR);
                }
            } catch (RemoteException e) {
                HiLog.error(LABEL_LOG, "FormManager::addForm, send request to fms failed", new Object[0]);
                FormException.FormError formError = FormException.FormError.SEND_FMS_MSG_ERROR;
                throw new FormException(formError, "send request to fms failed " + e.getMessage());
            } catch (Throwable th) {
                reclaimParcel(obtain, obtain2);
                throw th;
            }
        } else {
            throw new FormException(FormException.FormError.FMS_RPC_ERROR);
        }
    }

    private FormException.FormError getErrFromResult(int i) {
        FormException.FormError fromErrCode = FormException.FormError.fromErrCode(i);
        return fromErrCode == null ? FormException.FormError.INTERNAL_ERROR : fromErrCode;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setRemote(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private IRemoteObject getRemote() {
        return this.remote;
    }

    public boolean deleteForm(long j, IRemoteObject iRemoteObject, int i) throws FormException {
        HiLog.info(LABEL_LOG, "deleteForm.", new Object[0]);
        if (j <= 0 || iRemoteObject == null) {
            throw new FormException(FormException.FormError.INTERNAL_ERROR, "form has no client");
        }
        if (resetFlag) {
            resetRemoteObject();
        }
        if (this.remote != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            MessageOption messageOption = new MessageOption();
            try {
                if (!obtain.writeInterfaceToken(DESCRIPTOR)) {
                    reclaimParcel(obtain, obtain2);
                    return false;
                } else if (!obtain.writeLong(j)) {
                    reclaimParcel(obtain, obtain2);
                    return false;
                } else {
                    obtain.writeRemoteObject(iRemoteObject);
                    if (this.remote.sendRequest(i, obtain, obtain2, messageOption)) {
                        int readInt = obtain2.readInt();
                        if (readInt != 0) {
                            FormException.FormError errFromResult = getErrFromResult(readInt);
                            if (errFromResult == FormException.FormError.FORM_NOT_SELF_OWNED) {
                                HiLog.info(LABEL_LOG, "FORM_NOT_SELF_OWNED, need clear cache", new Object[0]);
                                reclaimParcel(obtain, obtain2);
                                return true;
                            }
                            HiLog.error(LABEL_LOG, "delete form error, code %{public}d", Integer.valueOf(readInt));
                            throw new FormException(errFromResult);
                        }
                        reclaimParcel(obtain, obtain2);
                        return true;
                    }
                    HiLog.error(LABEL_LOG, "FormManager::deleteForm sendRequest failed", new Object[0]);
                    throw new FormException(FormException.FormError.SEND_FMS_MSG_ERROR);
                }
            } catch (RemoteException e) {
                HiLog.error(LABEL_LOG, "deleteForm exception %{public}s", e.getMessage());
                FormException.FormError formError = FormException.FormError.SEND_FMS_MSG_ERROR;
                throw new FormException(formError, "delete form occurs error " + e.getMessage());
            } catch (Throwable th) {
                reclaimParcel(obtain, obtain2);
                throw th;
            }
        } else {
            throw new FormException(FormException.FormError.FMS_RPC_ERROR);
        }
    }

    public boolean requestForm(long j, IRemoteObject iRemoteObject, Intent intent) throws FormException {
        HiLog.info(LABEL_LOG, "requestForm.", new Object[0]);
        if (j <= 0 || iRemoteObject == null) {
            throw new FormException(FormException.FormError.INPUT_PARAM_INVALID, "request formId or clientStub is invalid");
        }
        if (resetFlag) {
            resetRemoteObject();
        }
        if (this.remote != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            MessageOption messageOption = new MessageOption();
            try {
                if (!obtain.writeInterfaceToken(DESCRIPTOR)) {
                    reclaimParcel(obtain, obtain2);
                    return false;
                } else if (!obtain.writeLong(j)) {
                    reclaimParcel(obtain, obtain2);
                    return false;
                } else {
                    obtain.writeSequenceable(intent);
                    obtain.writeRemoteObject(iRemoteObject);
                    if (this.remote.sendRequest(7, obtain, obtain2, messageOption)) {
                        int readInt = obtain2.readInt();
                        if (readInt == 0) {
                            reclaimParcel(obtain, obtain2);
                            return true;
                        }
                        throw new FormException(getErrFromResult(readInt));
                    }
                    throw new FormException(FormException.FormError.SEND_FMS_MSG_ERROR, "FormManager::requestForm sendRequest failed");
                }
            } catch (RemoteException e) {
                HiLog.error(LABEL_LOG, "requestForm exception %{public}s", e.getMessage());
                FormException.FormError formError = FormException.FormError.SEND_FMS_MSG_ERROR;
                throw new FormException(formError, "request form occurs error " + e.getMessage());
            } catch (Throwable th) {
                reclaimParcel(obtain, obtain2);
                throw th;
            }
        } else {
            throw new FormException(FormException.FormError.FMS_RPC_ERROR);
        }
    }

    public boolean lifecycleUpdate(List<Long> list, IRemoteObject iRemoteObject, int i) throws FormException {
        HiLog.info(LABEL_LOG, "lifecycleUpdate.", new Object[0]);
        if (list == null || list.isEmpty() || iRemoteObject == null) {
            throw new FormException(FormException.FormError.INPUT_PARAM_INVALID, "formIds or clientStub is invalid");
        }
        if (resetFlag) {
            resetRemoteObject();
        }
        if (this.remote != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            MessageOption messageOption = new MessageOption();
            try {
                if (!obtain.writeInterfaceToken(DESCRIPTOR)) {
                    reclaimParcel(obtain, obtain2);
                    return false;
                } else if (!obtain.writeInt(list.size())) {
                    reclaimParcel(obtain, obtain2);
                    return false;
                } else {
                    for (Long l : list) {
                        if (!obtain.writeLong(l.longValue())) {
                            reclaimParcel(obtain, obtain2);
                            return false;
                        }
                    }
                    obtain.writeRemoteObject(iRemoteObject);
                    if (this.remote.sendRequest(i, obtain, obtain2, messageOption)) {
                        int readInt = obtain2.readInt();
                        if (readInt == 0) {
                            reclaimParcel(obtain, obtain2);
                            return true;
                        }
                        throw new FormException(getErrFromResult(readInt));
                    }
                    throw new FormException(FormException.FormError.SEND_FMS_MSG_ERROR, "FormManager::lifecycle sendRequest failed");
                }
            } catch (RemoteException e) {
                HiLog.error(LABEL_LOG, "requestForm exception %{public}s", e.getMessage());
                FormException.FormError formError = FormException.FormError.SEND_FMS_MSG_ERROR;
                throw new FormException(formError, "lifecycle update form error " + e.getMessage());
            } catch (Throwable th) {
                reclaimParcel(obtain, obtain2);
                throw th;
            }
        } else {
            throw new FormException(FormException.FormError.FMS_RPC_ERROR);
        }
    }

    public boolean updateForm(long j, String str, ComponentProvider componentProvider) throws FormException {
        HiLog.info(LABEL_LOG, "updateForm.", new Object[0]);
        if (j <= 0 || str == null || str.isEmpty() || componentProvider == null) {
            throw new FormException(FormException.FormError.INPUT_PARAM_INVALID);
        }
        if (resetFlag) {
            resetRemoteObject();
        }
        if (this.remote != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            int applyType = componentProvider.getApplyType();
            if (componentProvider.setApplyType(1)) {
                try {
                    if (!obtain.writeInterfaceToken(DESCRIPTOR)) {
                        reclaimParcel(obtain, obtain2);
                        return false;
                    } else if (!obtain.writeLong(j)) {
                        reclaimParcel(obtain, obtain2);
                        return false;
                    } else if (!obtain.writeString(str)) {
                        reclaimParcel(obtain, obtain2);
                        return false;
                    } else if (!obtain.writeBoolean(false)) {
                        reclaimParcel(obtain, obtain2);
                        return false;
                    } else {
                        obtain.writeSequenceable(componentProvider);
                        if (this.remote.sendRequest(4, obtain, obtain2, new MessageOption())) {
                            int readInt = obtain2.readInt();
                            if (readInt != 0) {
                                throw new FormException(getErrFromResult(readInt));
                            } else if (componentProvider.setApplyType(applyType)) {
                                reclaimParcel(obtain, obtain2);
                                return true;
                            } else {
                                throw new FormException(FormException.FormError.INTERNAL_ERROR, "updateForm set apply type error after marshal.");
                            }
                        } else {
                            throw new FormException(FormException.FormError.SEND_FMS_MSG_ERROR, "FormManager::updateForm sendRequest failed");
                        }
                    }
                } catch (RemoteException e) {
                    HiLog.error(LABEL_LOG, "updateForm exception %{public}s", e.getMessage());
                    FormException.FormError formError = FormException.FormError.SEND_FMS_MSG_ERROR;
                    throw new FormException(formError, "updateForm error " + e.getMessage());
                } catch (Throwable th) {
                    reclaimParcel(obtain, obtain2);
                    throw th;
                }
            } else {
                throw new FormException(FormException.FormError.INTERNAL_ERROR, "updateForm set apply type error before marshal.");
            }
        } else {
            throw new FormException(FormException.FormError.FMS_RPC_ERROR);
        }
    }

    public boolean updateForm(long j, String str, FormBindingData formBindingData) throws FormException {
        int i;
        if (j <= 0 || str == null || str.isEmpty() || formBindingData == null) {
            throw new FormException(FormException.FormError.INPUT_PARAM_INVALID);
        }
        String dataString = formBindingData.getDataString();
        HiLogLabel hiLogLabel = LABEL_LOG;
        Object[] objArr = new Object[3];
        objArr[0] = "formId is " + j;
        StringBuilder sb = new StringBuilder();
        sb.append("data size is");
        if (dataString == null) {
            i = 0;
        } else {
            i = dataString.length();
        }
        sb.append(i);
        objArr[1] = sb.toString();
        objArr[2] = "data content is " + dataString;
        HiLog.info(hiLogLabel, "update js form. %{public}s,  %{public}s, %{private}s", objArr);
        if (resetFlag) {
            resetRemoteObject();
        }
        if (this.remote != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            try {
                if (!obtain.writeInterfaceToken(DESCRIPTOR)) {
                    reclaimParcel(obtain, obtain2);
                    return false;
                } else if (!obtain.writeLong(j)) {
                    reclaimParcel(obtain, obtain2);
                    return false;
                } else if (!obtain.writeString(str)) {
                    reclaimParcel(obtain, obtain2);
                    return false;
                } else if (!obtain.writeBoolean(true)) {
                    reclaimParcel(obtain, obtain2);
                    return false;
                } else {
                    obtain.writeSequenceable(formBindingData);
                    if (this.remote.sendRequest(4, obtain, obtain2, new MessageOption())) {
                        int readInt = obtain2.readInt();
                        if (readInt == 0) {
                            reclaimParcel(obtain, obtain2);
                            return true;
                        }
                        throw new FormException(getErrFromResult(readInt));
                    }
                    HiLog.error(LABEL_LOG, "FormManager::updateForm sendRequest failed", new Object[0]);
                    throw new FormException(FormException.FormError.SEND_FMS_MSG_ERROR, "FormManager::updateForm sendRequest failed");
                }
            } catch (RemoteException e) {
                HiLog.error(LABEL_LOG, "updateForm exception %{public}s", e.getMessage());
                throw new FormException(FormException.FormError.SEND_FMS_MSG_ERROR, "updateForm error " + e.getMessage());
            } catch (Throwable th) {
                reclaimParcel(obtain, obtain2);
                throw th;
            }
        } else {
            throw new FormException(FormException.FormError.FMS_RPC_ERROR);
        }
    }

    public boolean castTempForm(long j, IRemoteObject iRemoteObject) throws FormException {
        HiLog.info(LABEL_LOG, "castTempForm.", new Object[0]);
        if (j <= 0 || iRemoteObject == null) {
            throw new FormException(FormException.FormError.INTERNAL_ERROR, "form has no client");
        }
        if (resetFlag) {
            resetRemoteObject();
        }
        if (this.remote != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            MessageOption messageOption = new MessageOption();
            try {
                if (!obtain.writeInterfaceToken(DESCRIPTOR)) {
                    reclaimParcel(obtain, obtain2);
                    return false;
                } else if (!obtain.writeLong(j)) {
                    reclaimParcel(obtain, obtain2);
                    return false;
                } else {
                    obtain.writeRemoteObject(iRemoteObject);
                    if (this.remote.sendRequest(10, obtain, obtain2, messageOption)) {
                        int readInt = obtain2.readInt();
                        if (readInt == 0) {
                            reclaimParcel(obtain, obtain2);
                            return true;
                        }
                        HiLog.error(LABEL_LOG, "cast temp form error, code %{public}d", Integer.valueOf(readInt));
                        throw new FormException(getErrFromResult(readInt));
                    }
                    HiLog.error(LABEL_LOG, "FormManager::castTempForm sendRequest failed", new Object[0]);
                    throw new FormException(FormException.FormError.SEND_FMS_MSG_ERROR);
                }
            } catch (RemoteException e) {
                HiLog.error(LABEL_LOG, "castTempForm exception %{public}s", e.getMessage());
                FormException.FormError formError = FormException.FormError.SEND_FMS_MSG_ERROR;
                throw new FormException(formError, "cast temp form occurs error " + e.getMessage());
            } catch (Throwable th) {
                reclaimParcel(obtain, obtain2);
                throw th;
            }
        } else {
            throw new FormException(FormException.FormError.FMS_RPC_ERROR);
        }
    }

    public boolean notifyWhetherVisibleForms(List<Long> list, int i, IRemoteObject iRemoteObject) throws FormException {
        if (list == null || list.size() == 0 || list.size() > 32 || iRemoteObject == null) {
            HiLog.error(LABEL_LOG, "input param is not correct.", new Object[0]);
            throw new FormException(FormException.FormError.INTERNAL_ERROR, "input param is not correct");
        }
        if (resetFlag) {
            resetRemoteObject();
        }
        if (this.remote != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            try {
                if (!obtain.writeInterfaceToken(DESCRIPTOR)) {
                    reclaimParcel(obtain, obtain2);
                    return false;
                } else if (!obtain.writeInt(i)) {
                    reclaimParcel(obtain, obtain2);
                    return false;
                } else if (!obtain.writeInt(list.size())) {
                    reclaimParcel(obtain, obtain2);
                    return false;
                } else {
                    for (Long l : list) {
                        if (!obtain.writeLong(l.longValue())) {
                            reclaimParcel(obtain, obtain2);
                            return false;
                        }
                    }
                    obtain.writeRemoteObject(iRemoteObject);
                    if (this.remote.sendRequest(11, obtain, obtain2, new MessageOption())) {
                        int readInt = obtain2.readInt();
                        if (readInt == 0) {
                            reclaimParcel(obtain, obtain2);
                            return true;
                        }
                        HiLog.error(LABEL_LOG, "notifyWhetherVisibleForm error, code %{public}d", Integer.valueOf(readInt));
                        throw new FormException(getErrFromResult(readInt));
                    }
                    HiLog.error(LABEL_LOG, "FormManager::notifyWhetherVisibleForm sendRequest failed", new Object[0]);
                    throw new FormException(FormException.FormError.SEND_FMS_MSG_ERROR);
                }
            } catch (RemoteException e) {
                HiLog.error(LABEL_LOG, "notifyWhetherVisibleForm occurs exception %{public}s", e.getMessage());
                FormException.FormError formError = FormException.FormError.SEND_FMS_MSG_ERROR;
                throw new FormException(formError, "notifyWhetherVisibleForm occurs error " + e.getMessage());
            } catch (Throwable th) {
                reclaimParcel(obtain, obtain2);
                throw th;
            }
        } else {
            HiLog.error(LABEL_LOG, "remote is error.", new Object[0]);
            throw new FormException(FormException.FormError.FMS_RPC_ERROR);
        }
    }

    public int checkAndDeleteInvalidForms(List<Long> list, IRemoteObject iRemoteObject) throws FormException {
        HiLog.info(LABEL_LOG, "checkAndDeleteInvalidForms.", new Object[0]);
        if (list == null || iRemoteObject == null) {
            throw new FormException(FormException.FormError.INPUT_PARAM_INVALID, "persistedIds or clientStub is invalid");
        }
        if (resetFlag) {
            resetRemoteObject();
        }
        if (this.remote != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            MessageOption messageOption = new MessageOption();
            try {
                if (!writeCheckValidFormsRequest(obtain, list, iRemoteObject)) {
                    throw new FormException(FormException.FormError.INPUT_PARAM_INVALID, "write request info failed");
                } else if (this.remote.sendRequest(12, obtain, obtain2, messageOption)) {
                    int readInt = obtain2.readInt();
                    if (readInt == 0) {
                        int readInt2 = obtain2.readInt();
                        HiLog.info(LABEL_LOG, "checkAndDeleteInvalidForms success. invalidForms size: %{public}d", Integer.valueOf(readInt2));
                        reclaimParcel(obtain, obtain2);
                        return readInt2;
                    }
                    FormException.FormError errFromResult = getErrFromResult(readInt);
                    HiLog.error(LABEL_LOG, "checkAndDeleteInvalidForms error: %{public}s", errFromResult);
                    throw new FormException(errFromResult);
                } else {
                    HiLog.error(LABEL_LOG, "FormManager::checkAndDeleteInvalidForms sendRequest failed", new Object[0]);
                    throw new FormException(FormException.FormError.SEND_FMS_MSG_ERROR);
                }
            } catch (RemoteException e) {
                HiLog.error(LABEL_LOG, "checkAndDeleteInvalidForms exception %{public}s", e.getMessage());
                FormException.FormError formError = FormException.FormError.SEND_FMS_MSG_ERROR;
                throw new FormException(formError, "checkAndDeleteInvalidForms occurs error " + e.getMessage());
            } catch (Throwable th) {
                reclaimParcel(obtain, obtain2);
                throw th;
            }
        } else {
            throw new FormException(FormException.FormError.FMS_RPC_ERROR);
        }
    }

    private boolean writeCheckValidFormsRequest(MessageParcel messageParcel, List<Long> list, IRemoteObject iRemoteObject) {
        if (!messageParcel.writeInterfaceToken(DESCRIPTOR)) {
            HiLog.error(LABEL_LOG, "writeCheckValidFormsRequest write interface token failed", new Object[0]);
            return false;
        } else if (!messageParcel.writeInt(list.size())) {
            HiLog.error(LABEL_LOG, "writeCheckValidFormsRequest write persistedIds size failed", new Object[0]);
            return false;
        } else {
            for (Long l : list) {
                if (!messageParcel.writeLong(l.longValue())) {
                    HiLog.error(LABEL_LOG, "writeCheckValidFormsRequest write persistedIds failed", new Object[0]);
                    return false;
                }
            }
            messageParcel.writeRemoteObject(iRemoteObject);
            return true;
        }
    }

    static void registerDeathCallback(DeathCallback deathCallback) {
        synchronized (INSTANCE_LOCK) {
            callbacks.add(deathCallback);
        }
    }

    static void unregisterDeathCallback(DeathCallback deathCallback) {
        synchronized (INSTANCE_LOCK) {
            callbacks.remove(deathCallback);
        }
    }

    static int getRecoverStatus() {
        int i;
        synchronized (INSTANCE_LOCK) {
            i = recoverStatus;
        }
        return i;
    }

    /* access modifiers changed from: package-private */
    public void resetRemoteObject() {
        synchronized (INSTANCE_LOCK) {
            if (resetFlag) {
                IRemoteObject sysAbility = SysAbilityManager.getSysAbility(403);
                if (sysAbility == null) {
                    HiLog.info(LABEL_LOG, "BundleManager reset remoteObject failed, remote is null", new Object[0]);
                    return;
                }
                deathRecipient = new FormManagerDeathRecipient();
                if (!sysAbility.addDeathRecipient(deathRecipient, 0)) {
                    HiLog.info(LABEL_LOG, "BundleManager register BundleManagerDeathRecipient failed", new Object[0]);
                }
                this.remote = sysAbility;
                resetFlag = false;
            }
        }
    }

    private void reclaimParcel(MessageParcel messageParcel, MessageParcel messageParcel2) {
        messageParcel.reclaim();
        messageParcel2.reclaim();
    }

    protected static void resetFlag() {
        resetFlag = true;
    }

    /* access modifiers changed from: private */
    public static class FormManagerDeathRecipient implements IRemoteObject.DeathRecipient {
        private static final int MAX_RETRY_TIME = 30;
        private static final int SLEEP_TIME = 1000;

        private FormManagerDeathRecipient() {
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            ArrayList<DeathCallback> arrayList;
            synchronized (FormManager.INSTANCE_LOCK) {
                HiLog.debug(FormManager.LABEL_LOG, "remote died, start to process onRemoteDied", new Object[0]);
                while (FormManager.recoverStatus == 2) {
                    try {
                        HiLog.debug(FormManager.LABEL_LOG, "remote died, wait for previous recover finish", new Object[0]);
                        FormManager.INSTANCE_LOCK.wait();
                    } catch (InterruptedException unused) {
                        HiLog.error(FormManager.LABEL_LOG, "wait lock occurs interrupt exception", new Object[0]);
                        return;
                    }
                }
                int unused2 = FormManager.recoverStatus = 2;
                if (!(FormManager.instance.getRemote() == null || FormManager.deathRecipient == null)) {
                    FormManager.instance.getRemote().removeDeathRecipient(FormManager.deathRecipient, 0);
                }
                FormManager.instance.setRemote(null);
                arrayList = new ArrayList(FormManager.callbacks);
            }
            if (!reconnect()) {
                HiLog.error(FormManager.LABEL_LOG, "remote died, try to reconnect to bms and fms failed", new Object[0]);
                synchronized (FormManager.INSTANCE_LOCK) {
                    int unused3 = FormManager.recoverStatus = 1;
                    FormManager.INSTANCE_LOCK.notifyAll();
                    FormManager.resetFlag();
                }
                return;
            }
            try {
                for (DeathCallback deathCallback : arrayList) {
                    deathCallback.onDeathReceived();
                }
                synchronized (FormManager.INSTANCE_LOCK) {
                    int unused4 = FormManager.recoverStatus = 0;
                    FormManager.INSTANCE_LOCK.notifyAll();
                }
            } catch (Throwable th) {
                synchronized (FormManager.INSTANCE_LOCK) {
                    int unused5 = FormManager.recoverStatus = 0;
                    FormManager.INSTANCE_LOCK.notifyAll();
                    throw th;
                }
            }
        }

        private boolean reconnect() {
            for (int i = 0; i < 30; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException unused) {
                    HiLog.warn(FormManager.LABEL_LOG, "reconnect occurs InterruptedException", new Object[0]);
                }
                if (SysAbilityManager.getSysAbility(401) == null) {
                    HiLog.warn(FormManager.LABEL_LOG, "get bms proxy fail, try again", new Object[0]);
                } else if (getFmsProxy() == null) {
                    HiLog.warn(FormManager.LABEL_LOG, "get fms proxy fail, try again", new Object[0]);
                } else {
                    HiLog.info(FormManager.LABEL_LOG, "get bms and fms proxy success", new Object[0]);
                    return true;
                }
            }
            return false;
        }

        private IRemoteObject getFmsProxy() {
            if (FormManager.instance.getRemote() != null) {
                return FormManager.instance.getRemote();
            }
            IRemoteObject sysAbility = SysAbilityManager.getSysAbility(403);
            if (sysAbility == null) {
                return null;
            }
            IRemoteObject.DeathRecipient unused = FormManager.deathRecipient = new FormManagerDeathRecipient();
            if (!sysAbility.addDeathRecipient(FormManager.deathRecipient, 0)) {
                HiLog.warn(FormManager.LABEL_LOG, "FormManager register FormManagerDeathRecipient failed", new Object[0]);
                return null;
            }
            HiLog.debug(FormManager.LABEL_LOG, "FormManager addDeathRecipient success, is dead %{public}b", Boolean.valueOf(sysAbility.isObjectDead()));
            FormManager.instance.setRemote(sysAbility);
            return sysAbility;
        }
    }

    private boolean isTypeValid(Object obj) {
        if (obj == null || (obj instanceof Boolean) || (obj instanceof Byte) || (obj instanceof Character) || (obj instanceof Short) || (obj instanceof Integer) || (obj instanceof Long) || (obj instanceof Float) || (obj instanceof Double) || (obj instanceof String) || (obj instanceof CharSequence)) {
            return true;
        }
        if (obj instanceof List) {
            return isTypeListValid((List) obj);
        }
        if (!(obj instanceof boolean[]) && !(obj instanceof byte[]) && !(obj instanceof char[]) && !(obj instanceof short[]) && !(obj instanceof int[]) && !(obj instanceof long[]) && !(obj instanceof float[]) && !(obj instanceof double[]) && !(obj instanceof String[]) && !(obj instanceof CharSequence[])) {
            return false;
        }
        return true;
    }

    private boolean isTypeListValid(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            for (Object obj : list) {
                if (!isTypeValid(obj)) {
                    return false;
                }
            }
        }
        return true;
    }
}
