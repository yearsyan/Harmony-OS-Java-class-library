package ohos.aafwk.ability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import ohos.aafwk.ability.IFormHost;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/* access modifiers changed from: package-private */
public class FormHostClient extends IFormHost.FormHostStub {
    private static final Object CLIENT_LOCK = new Object();
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218108160, "FormHostClient");
    private static volatile FormHostClient client;
    private final WeakHashMap<AbilitySlice, HostForms> slicesRecord = new WeakHashMap<>();

    static FormHostClient getInstance() {
        if (client == null) {
            synchronized (CLIENT_LOCK) {
                if (client == null) {
                    client = new FormHostClient();
                }
            }
        }
        return client;
    }

    FormHostClient() {
    }

    /* access modifiers changed from: package-private */
    public void addForm(AbilitySlice abilitySlice, long j) {
        if (j > 0 && abilitySlice != null) {
            synchronized (CLIENT_LOCK) {
                this.slicesRecord.computeIfAbsent(abilitySlice, $$Lambda$FormHostClient$L2ZYdYPsMzFlficcxWRAoI1qos.INSTANCE).addForm(j);
            }
        }
    }

    static /* synthetic */ HostForms lambda$addForm$0(AbilitySlice abilitySlice) {
        return new HostForms();
    }

    /* access modifiers changed from: package-private */
    public void removeForm(AbilitySlice abilitySlice, long j) {
        if (j > 0 && abilitySlice != null) {
            synchronized (CLIENT_LOCK) {
                HostForms hostForms = this.slicesRecord.get(abilitySlice);
                if (hostForms != null) {
                    if (!hostForms.isEmpty()) {
                        hostForms.delForm(j);
                        if (hostForms.isEmpty()) {
                            this.slicesRecord.remove(abilitySlice);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean containsForm(long j) {
        synchronized (CLIENT_LOCK) {
            for (HostForms hostForms : this.slicesRecord.values()) {
                if (hostForms.contains(j)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override // ohos.aafwk.ability.IFormHost
    public void onAcquired(Form form) {
        if (form == null || form.formId < 0) {
            HiLog.error(LABEL, "on acquired ability form error, form is empty", new Object[0]);
            return;
        }
        long j = form.formId;
        AbilitySlice findTargetSlice = findTargetSlice(j);
        if (findTargetSlice == null) {
            HiLog.error(LABEL, "can't find target slice of form %{public}d on acquire", Long.valueOf(j));
            return;
        }
        findTargetSlice.processFormUpdate(form);
    }

    @Override // ohos.aafwk.ability.IFormHost
    public void onUpdate(Form form) {
        if (form == null || form.formId < 0) {
            HiLog.error(LABEL, "on update ability form error, form is empty", new Object[0]);
            return;
        }
        long j = form.formId;
        AbilitySlice findTargetSlice = findTargetSlice(j);
        if (findTargetSlice == null) {
            HiLog.error(LABEL, "can't find target slice of form %{public}d on update", Long.valueOf(j));
            return;
        }
        findTargetSlice.processFormUpdate(form);
    }

    @Override // ohos.aafwk.ability.IFormHost
    public void onFormUninstalled(List<Long> list) {
        if (list == null || list.isEmpty()) {
            HiLog.error(LABEL, "onFormUninstalled, no form id need to be processed", new Object[0]);
            return;
        }
        for (Long l : list) {
            long longValue = l.longValue();
            AbilitySlice findTargetSlice = findTargetSlice(longValue);
            if (findTargetSlice == null) {
                HiLog.error(LABEL, "can't find target slice of form %{public}d on uninstall", Long.valueOf(longValue));
            } else {
                findTargetSlice.processFormUninstall(longValue);
            }
        }
    }

    private AbilitySlice findTargetSlice(long j) {
        synchronized (CLIENT_LOCK) {
            for (Map.Entry<AbilitySlice, HostForms> entry : this.slicesRecord.entrySet()) {
                if (entry.getValue().contains(j)) {
                    return entry.getKey();
                }
            }
            return null;
        }
    }

    /* access modifiers changed from: private */
    public static class HostForms {
        Map<Long, Boolean> forms;

        private HostForms() {
            this.forms = new HashMap();
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
    }
}
