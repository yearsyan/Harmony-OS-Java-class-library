package ohos.aafwk.ability;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import ohos.agp.components.ComponentProvider;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

public class ProviderFormInfo implements Sequenceable {
    private static BiFunction<Integer, Context, ComponentProvider> DEFAULT_COMPONENT_COMPONENT_BUILDER_WITH_LAYOUTID = $$Lambda$w41QkgBP4fV7giPP62RbMZpBqI.INSTANCE;
    private static Supplier<ComponentProvider> DEFAULT_COMPONENT_PROVIDER_BUILDER = $$Lambda$UxQPvovYsqBDdS9sOEPHp_Smk.INSTANCE;
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218108672, "ProviderFormInfo");
    public static final Sequenceable.Producer<ProviderFormInfo> PRODUCER = $$Lambda$ProviderFormInfo$gHXfvp4VHda8X7GnzABdZ7Z6k.INSTANCE;
    private ComponentProvider componentProvider;
    private int formDataSize;
    private boolean isJsForm;
    private FormBindingData jsBindingData;

    static /* synthetic */ ProviderFormInfo lambda$static$0(Parcel parcel) {
        ProviderFormInfo providerFormInfo = new ProviderFormInfo();
        providerFormInfo.unmarshalling(parcel);
        return providerFormInfo;
    }

    public ProviderFormInfo(int i, Context context) {
        this.isJsForm = false;
        this.formDataSize = 0;
        if (context == null) {
            HiLog.error(LABEL, "context is null", new Object[0]);
        } else {
            this.componentProvider = DEFAULT_COMPONENT_COMPONENT_BUILDER_WITH_LAYOUTID.apply(Integer.valueOf(i), context);
        }
    }

    public ProviderFormInfo() {
        this.isJsForm = false;
        this.formDataSize = 0;
        this.isJsForm = true;
        this.jsBindingData = new FormBindingData();
    }

    public static ProviderFormInfo createFromParcel(Parcel parcel) {
        ProviderFormInfo providerFormInfo = new ProviderFormInfo();
        if (parcel != null && parcel.readSequenceable(providerFormInfo) && ((providerFormInfo.isJsForm || providerFormInfo.componentProvider != null) && (!providerFormInfo.isJsForm || providerFormInfo.jsBindingData != null))) {
            return providerFormInfo;
        }
        HiLog.error(LABEL, "create formInfoProvider parcel failed, isJsForm: %{public}b.", Boolean.valueOf(providerFormInfo.isJsForm));
        return null;
    }

    public ComponentProvider getComponentProvider() {
        return this.componentProvider;
    }

    public int getFormDataSize() {
        return this.formDataSize;
    }

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        if (parcel == null) {
            HiLog.error(LABEL, "marshalling out is null", new Object[0]);
            return false;
        }
        parcel.writeBoolean(this.isJsForm);
        if (this.isJsForm) {
            if (!marshallingFormBindingData(parcel)) {
                return false;
            }
            return true;
        } else if (!marshallingComponentProvider(parcel)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean marshallingComponentProvider(Parcel parcel) {
        ComponentProvider componentProvider2 = this.componentProvider;
        if (componentProvider2 == null) {
            HiLog.error(LABEL, "componentProvider is null when marshalling.", new Object[0]);
            return false;
        }
        parcel.writeSequenceable(componentProvider2);
        return true;
    }

    private boolean marshallingFormBindingData(Parcel parcel) {
        FormBindingData formBindingData = this.jsBindingData;
        if (formBindingData == null) {
            HiLog.error(LABEL, "jsBindingData is null when marshalling.", new Object[0]);
            return false;
        }
        parcel.writeSequenceable(formBindingData);
        return true;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        if (parcel == null) {
            HiLog.error(LABEL, "unmarshalling in is null", new Object[0]);
            return false;
        }
        this.isJsForm = parcel.readBoolean();
        int readableBytes = parcel.getReadableBytes();
        if (this.isJsForm) {
            this.jsBindingData = new FormBindingData();
            if (!parcel.readSequenceable(this.jsBindingData)) {
                HiLog.error(LABEL, "read jsBindingData failed.", new Object[0]);
                return false;
            }
        } else {
            this.componentProvider = DEFAULT_COMPONENT_PROVIDER_BUILDER.get();
            if (!parcel.readSequenceable(this.componentProvider)) {
                HiLog.error(LABEL, "read componentProvider failed.", new Object[0]);
                return false;
            }
        }
        this.formDataSize = readableBytes;
        return true;
    }

    public void mergeActions(ComponentProvider componentProvider2) {
        if (componentProvider2 == null) {
            HiLog.error(LABEL, "null remote view actions for send actions.", new Object[0]);
            return;
        }
        Collection<ComponentProvider.Action> actions = componentProvider2.getActions();
        if (actions == null || actions.isEmpty()) {
            HiLog.error(LABEL, "actions is null or actions is empty", new Object[0]);
        } else {
            this.componentProvider.mergeActions(actions);
        }
    }

    public FormBindingData getJsBindingData() {
        return this.jsBindingData;
    }

    public void setJsBindingData(FormBindingData formBindingData) {
        this.jsBindingData = formBindingData;
    }

    public boolean isJsForm() {
        return this.isJsForm;
    }
}
