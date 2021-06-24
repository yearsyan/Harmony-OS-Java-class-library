package ohos.bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ohos.annotation.SystemApi;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;
import ohos.utils.fastjson.JSONObject;

public class FormInfo implements Sequenceable {
    private static final String EXTRA_DATA_KEY_CONFIG_ABILITY = "formConfigAbility";
    private static final String EXTRA_DATA_KEY_RELATED_BUNDLE_NAME = "relatedBundleName";
    private static final String EXTRA_DATA_KEY_VISIBLE_NOTIFY = "formVisibleNotify";
    private static final int MAX_LIMIT_SIZE = 4;
    public static final Sequenceable.Producer<FormInfo> PRODUCER = $$Lambda$FormInfo$6f4gc11tik8WEY3YufPM4Vr75Wk.INSTANCE;
    private String abilityName;
    private String bundleName;
    private String colorMode;
    private Map<String, String> customizeDatas;
    private String deepLink;
    private int defaultDimension;
    private boolean defaultFlag;
    private String description;
    private int descriptionId;
    private String formConfigAbility;
    private boolean formVisibleNotify;
    private String jsComponentName;
    private JSONObject jsonObject;
    private List<String> landscapeLayouts;
    private String moduleName;
    private String name;
    private String originalBundleName;
    private List<String> portraitLayouts;
    private String relatedBundleName;
    private String scheduledUpdateTime;
    private List<Integer> supportDimensions;
    private FormType type;
    private int updateDuration;
    private boolean updateEnabled;

    public enum FormType {
        JAVA,
        JS
    }

    static /* synthetic */ FormInfo lambda$static$0(Parcel parcel) {
        FormInfo formInfo = new FormInfo();
        formInfo.unmarshalling(parcel);
        return formInfo;
    }

    public class CustomizeData {
        String name;
        String value;

        public CustomizeData() {
        }
    }

    public FormInfo() {
        this(null);
    }

    public FormInfo(FormInfo formInfo) {
        this.bundleName = "";
        this.originalBundleName = "";
        this.relatedBundleName = "";
        this.moduleName = "";
        this.abilityName = "";
        this.name = "";
        this.description = "";
        this.descriptionId = 0;
        this.type = FormType.JAVA;
        this.jsComponentName = "";
        this.colorMode = "auto";
        this.defaultFlag = false;
        this.updateEnabled = false;
        this.formVisibleNotify = false;
        this.scheduledUpdateTime = "00:00";
        this.deepLink = "";
        this.formConfigAbility = "";
        this.updateDuration = 1;
        this.defaultDimension = 1;
        this.supportDimensions = new ArrayList();
        this.landscapeLayouts = new ArrayList();
        this.portraitLayouts = new ArrayList();
        this.customizeDatas = new HashMap();
        if (formInfo != null) {
            this.bundleName = formInfo.bundleName;
            this.originalBundleName = formInfo.originalBundleName;
            this.relatedBundleName = formInfo.relatedBundleName;
            this.moduleName = formInfo.moduleName;
            this.abilityName = formInfo.abilityName;
            this.name = formInfo.name;
            this.description = formInfo.description;
            this.type = formInfo.type;
            this.colorMode = formInfo.colorMode;
            this.defaultFlag = formInfo.defaultFlag;
            this.updateEnabled = formInfo.updateEnabled;
            this.formVisibleNotify = formInfo.formVisibleNotify;
            this.updateDuration = formInfo.updateDuration;
            this.scheduledUpdateTime = formInfo.scheduledUpdateTime;
            this.deepLink = formInfo.deepLink;
            this.formConfigAbility = formInfo.formConfigAbility;
            this.defaultDimension = formInfo.defaultDimension;
            this.supportDimensions = formInfo.supportDimensions;
            this.customizeDatas = formInfo.customizeDatas;
            if (this.type == FormType.JAVA) {
                this.landscapeLayouts = formInfo.landscapeLayouts;
                this.portraitLayouts = formInfo.portraitLayouts;
            } else {
                this.jsComponentName = formInfo.jsComponentName;
            }
            this.descriptionId = formInfo.descriptionId;
        }
    }

    public String getBundleName() {
        return this.bundleName;
    }

    public String getOriginalBundleName() {
        return this.originalBundleName;
    }

    @SystemApi
    public String getRelatedBundleName() {
        return this.relatedBundleName;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public String getAbilityName() {
        return this.abilityName;
    }

    public String getFormName() {
        return this.name;
    }

    public boolean getUpdateEnabled() {
        return this.updateEnabled;
    }

    public int getUpdateDuration() {
        return this.updateDuration;
    }

    public String getScheduledUpdateTime() {
        return this.scheduledUpdateTime;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean getFormVisibleNotify() {
        return this.formVisibleNotify;
    }

    public FormType getType() {
        return this.type;
    }

    public String getColorMode() {
        return this.colorMode;
    }

    public String getJsComponentName() {
        return this.jsComponentName;
    }

    @Deprecated
    public String getDeepLink() {
        return this.deepLink;
    }

    public String getFormConfigAbility() {
        return this.formConfigAbility;
    }

    public boolean isDefaultForm() {
        return this.defaultFlag;
    }

    public int getDefaultDimension() {
        return this.defaultDimension;
    }

    public List<Integer> getSupportDimensions() {
        return this.supportDimensions;
    }

    public Map<String, String> getCustomizeDatas() {
        return this.customizeDatas;
    }

    /* access modifiers changed from: package-private */
    public int getDescriptionId() {
        return this.descriptionId;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0028  */
    @Override // ohos.utils.Sequenceable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean marshalling(ohos.utils.Parcel r5) {
        /*
        // Method dump skipped, instructions count: 138
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bundle.FormInfo.marshalling(ohos.utils.Parcel):boolean");
    }

    private boolean marshallingFirst(Parcel parcel) {
        if (!(parcel.writeString(this.bundleName) && parcel.writeString(this.originalBundleName) && parcel.writeString(this.relatedBundleName) && parcel.writeString(this.moduleName) && parcel.writeString(this.abilityName) && parcel.writeString(this.name) && parcel.writeString(this.description) && parcel.writeString(this.type.toString()) && parcel.writeString(this.colorMode) && parcel.writeBoolean(this.defaultFlag) && parcel.writeBoolean(this.updateEnabled) && parcel.writeBoolean(this.formVisibleNotify) && parcel.writeInt(this.updateDuration) && parcel.writeString(this.scheduledUpdateTime) && parcel.writeString(this.deepLink) && parcel.writeString(this.formConfigAbility) && parcel.writeInt(this.defaultDimension) && parcel.writeInt(this.supportDimensions.size()))) {
            return false;
        }
        for (Integer num : this.supportDimensions) {
            parcel.writeInt(num.intValue());
        }
        return true;
    }

    private boolean writeInfoToParcel(List<String> list, Parcel parcel) {
        if (!parcel.writeInt(list.size())) {
            return false;
        }
        for (String str : list) {
            parcel.writeString(str);
        }
        return true;
    }

    private boolean readInfoToList(Parcel parcel, List<String> list) {
        int readInt = parcel.readInt();
        if (readInt > 4) {
            return false;
        }
        for (int i = 0; i < readInt; i++) {
            list.add(parcel.readString());
        }
        return true;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        if (parcel == null) {
            return false;
        }
        this.bundleName = parcel.readString();
        this.originalBundleName = parcel.readString();
        this.moduleName = parcel.readString();
        this.abilityName = parcel.readString();
        this.name = parcel.readString();
        this.description = parcel.readString();
        if ("JS".equalsIgnoreCase(parcel.readString())) {
            this.type = FormType.JS;
        }
        this.colorMode = parcel.readString();
        this.defaultFlag = parcel.readBoolean();
        this.updateEnabled = parcel.readBoolean();
        this.updateDuration = parcel.readInt();
        this.scheduledUpdateTime = parcel.readString();
        this.deepLink = parcel.readString();
        this.defaultDimension = parcel.readInt();
        int readInt = parcel.readInt();
        if (readInt > 4) {
            return false;
        }
        for (int i = 0; i < readInt; i++) {
            this.supportDimensions.add(Integer.valueOf(parcel.readInt()));
        }
        int readInt2 = parcel.readInt();
        for (int i2 = 0; i2 < readInt2; i2++) {
            this.customizeDatas.put(parcel.readString(), parcel.readString());
        }
        if (this.type != FormType.JAVA) {
            this.jsComponentName = parcel.readString();
        } else if (!readInfoToList(parcel, this.landscapeLayouts) || !readInfoToList(parcel, this.portraitLayouts)) {
            return false;
        }
        this.descriptionId = parcel.readInt();
        parcel.readInt();
        unmarshalExtraData(parcel);
        return true;
    }

    private void unmarshalExtraData(Parcel parcel) {
        JSONObject parseObject;
        String readString = parcel.readString();
        if (readString != null && !readString.isEmpty() && (parseObject = JSONObject.parseObject(readString)) != null) {
            this.jsonObject = parseObject;
            parseExtraDataJson();
        }
    }

    private void parseExtraDataJson() {
        JSONObject jSONObject = this.jsonObject;
        if (jSONObject != null) {
            if (jSONObject.containsKey(EXTRA_DATA_KEY_CONFIG_ABILITY)) {
                this.formConfigAbility = ProfileConstants.getJsonString(this.jsonObject, EXTRA_DATA_KEY_CONFIG_ABILITY);
            }
            if (this.jsonObject.containsKey(EXTRA_DATA_KEY_VISIBLE_NOTIFY)) {
                this.formVisibleNotify = ProfileConstants.getJsonBoolean(this.jsonObject, EXTRA_DATA_KEY_VISIBLE_NOTIFY, false);
            }
            if (this.jsonObject.containsKey(EXTRA_DATA_KEY_RELATED_BUNDLE_NAME)) {
                this.relatedBundleName = ProfileConstants.getJsonString(this.jsonObject, EXTRA_DATA_KEY_RELATED_BUNDLE_NAME);
            }
        }
    }
}
