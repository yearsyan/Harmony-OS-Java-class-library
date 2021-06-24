package ohos.aafwk.ability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

/* access modifiers changed from: package-private */
public class FormHostRecord {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218108160, "FormHostRecord");
    IHostCallback clientImpl;
    IRemoteObject clientStub;
    IRemoteObject.DeathRecipient deathRecipient;
    Map<Long, Boolean> forms = new HashMap();
    boolean isESystem;
    int myUid;
    Map<Long, Boolean> needRefresh = new HashMap();

    static FormHostRecord createRecord(boolean z, IRemoteObject iRemoteObject, int i) {
        if (iRemoteObject == null) {
            HiLog.error(LABEL, "invalid param", new Object[0]);
            return null;
        }
        FormHostRecord formHostRecord = new FormHostRecord();
        formHostRecord.isESystem = z;
        formHostRecord.myUid = i;
        formHostRecord.clientStub = iRemoteObject;
        formHostRecord.clientImpl = z ? new ESystemHostCallback(iRemoteObject) : new OhosHostCallback(iRemoteObject);
        formHostRecord.deathRecipient = new ClientDeathRecipient(formHostRecord);
        formHostRecord.clientStub.addDeathRecipient(formHostRecord.deathRecipient, 0);
        return formHostRecord;
    }

    /* access modifiers changed from: package-private */
    public void addForm(long j) {
        if (!this.forms.containsKey(Long.valueOf(j))) {
            this.forms.put(Long.valueOf(j), true);
        }
    }

    /* access modifiers changed from: package-private */
    public void delForm(long j) {
        this.forms.remove(Long.valueOf(j));
    }

    /* access modifiers changed from: package-private */
    public boolean isEmpty() {
        return this.forms.isEmpty();
    }

    /* access modifiers changed from: package-private */
    public boolean contains(long j) {
        return this.forms.containsKey(Long.valueOf(j));
    }

    /* access modifiers changed from: package-private */
    public void setEnableRefresh(long j, boolean z) {
        if (this.forms.containsKey(Long.valueOf(j))) {
            this.forms.put(Long.valueOf(j), Boolean.valueOf(z));
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isEnableRefresh(long j) {
        Boolean bool = this.forms.get(Long.valueOf(j));
        return bool != null && bool.booleanValue();
    }

    /* access modifiers changed from: package-private */
    public void setNeedRefresh(long j, boolean z) {
        this.needRefresh.put(Long.valueOf(j), Boolean.valueOf(z));
    }

    /* access modifiers changed from: package-private */
    public boolean isNeedRefresh(long j) {
        Boolean bool = this.needRefresh.get(Long.valueOf(j));
        return bool != null && bool.booleanValue();
    }

    /* access modifiers changed from: package-private */
    public void onAcquire(long j, FormRecord formRecord) {
        IHostCallback iHostCallback = this.clientImpl;
        if (iHostCallback != null) {
            iHostCallback.onAcquire(j, formRecord);
        }
    }

    /* access modifiers changed from: package-private */
    public void onUpdate(long j, FormRecord formRecord) {
        IHostCallback iHostCallback = this.clientImpl;
        if (iHostCallback != null) {
            iHostCallback.onUpdate(j, formRecord);
        }
    }

    /* access modifiers changed from: package-private */
    public void onFormUninstalled(List<Long> list) {
        IHostCallback iHostCallback = this.clientImpl;
        if (iHostCallback != null) {
            iHostCallback.onFormUninstalled(list);
        }
    }

    /* access modifiers changed from: package-private */
    public void cleanResource() {
        IRemoteObject.DeathRecipient deathRecipient2;
        IRemoteObject iRemoteObject = this.clientStub;
        if (iRemoteObject != null && (deathRecipient2 = this.deathRecipient) != null) {
            iRemoteObject.removeDeathRecipient(deathRecipient2, 0);
            this.clientStub = null;
            this.deathRecipient = null;
        }
    }

    private FormHostRecord() {
    }

    /* access modifiers changed from: private */
    public static class ClientDeathRecipient implements IRemoteObject.DeathRecipient {
        private final FormHostRecord owner;

        ClientDeathRecipient(FormHostRecord formHostRecord) {
            this.owner = formHostRecord;
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            FormAdapter.getInstance().postHostDied(this.owner.clientStub);
        }
    }
}
