package ohos.bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import ohos.annotation.SystemApi;
import ohos.bundle.AbilityInfo;
import ohos.data.search.schema.ContactItem;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;
import ohos.utils.fastjson.JSONArray;
import ohos.utils.fastjson.JSONObject;

public class HapModuleInfo implements Sequenceable {
    public static final int AUTO_MODE = -1;
    public static final int DARK_MODE = 0;
    private static final String DRIVE_MODE = "drive";
    public static final int LIGHT_MODE = 1;
    private static final int MAX_DEVICETYPE_SIZE = 50;
    private static final int MAX_LIMIT_SIZE = 1024;
    public static final Sequenceable.Producer<HapModuleInfo> PRODUCER = $$Lambda$HapModuleInfo$LeUbJfPlG7M0kKQgX29UNvGEChc.INSTANCE;
    private static final int VALUE_FALSE = 0;
    private static final int VALUE_TRUE = 1;
    private static final int VALUE_UNSPECIFIED = -1;
    private List<AbilityInfo> abilityInfos;
    private boolean allowClassMap;
    private String backgroundImg;
    private int backgroundImgId;
    private String bundleName;
    private int colorMode;
    private String description;
    private int descriptionId;
    private List<String> deviceTypes;
    private int iconId;
    private String iconPath;
    private int installationFree;
    private String label;
    private int labelId;
    private String mainAbilityName;
    private String moduleName;
    private String name;
    private List<String> reqCapabilities;
    private int supportedModes;
    private String theme;
    private int themeId;
    private String versionName;

    static /* synthetic */ HapModuleInfo lambda$static$0(Parcel parcel) {
        HapModuleInfo hapModuleInfo = new HapModuleInfo();
        hapModuleInfo.unmarshalling(parcel);
        return hapModuleInfo;
    }

    public HapModuleInfo() {
        this(null);
    }

    public HapModuleInfo(HapModuleInfo hapModuleInfo) {
        this.name = "";
        this.description = "";
        this.descriptionId = 0;
        this.iconPath = "";
        this.iconId = 0;
        this.label = "";
        this.labelId = 0;
        this.backgroundImg = "";
        this.backgroundImgId = 0;
        this.supportedModes = 0;
        this.reqCapabilities = new ArrayList();
        this.deviceTypes = new ArrayList();
        this.abilityInfos = new ArrayList();
        this.moduleName = "";
        this.allowClassMap = false;
        this.theme = "";
        this.themeId = -1;
        this.colorMode = -1;
        this.mainAbilityName = "";
        this.installationFree = -1;
        this.bundleName = "";
        this.versionName = "";
        if (hapModuleInfo != null) {
            this.name = hapModuleInfo.name;
            this.description = hapModuleInfo.description;
            this.descriptionId = hapModuleInfo.descriptionId;
            this.iconPath = hapModuleInfo.iconPath;
            this.iconId = hapModuleInfo.iconId;
            this.label = hapModuleInfo.label;
            this.labelId = hapModuleInfo.labelId;
            this.backgroundImg = hapModuleInfo.backgroundImg;
            this.backgroundImgId = hapModuleInfo.backgroundImgId;
            this.supportedModes = hapModuleInfo.supportedModes;
            this.reqCapabilities = hapModuleInfo.reqCapabilities;
            this.deviceTypes = hapModuleInfo.deviceTypes;
            this.abilityInfos = hapModuleInfo.abilityInfos;
            this.moduleName = hapModuleInfo.moduleName;
            this.allowClassMap = hapModuleInfo.allowClassMap;
            this.theme = hapModuleInfo.theme;
            this.themeId = hapModuleInfo.themeId;
            this.colorMode = hapModuleInfo.colorMode;
            this.mainAbilityName = hapModuleInfo.mainAbilityName;
            this.installationFree = hapModuleInfo.installationFree;
            this.bundleName = hapModuleInfo.bundleName;
            this.versionName = hapModuleInfo.versionName;
        }
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getDescriptionId() {
        return this.descriptionId;
    }

    public String getIconPath() {
        return this.iconPath;
    }

    public int getIconId() {
        return this.iconId;
    }

    public String getLabel() {
        return this.label;
    }

    public int getLabelId() {
        return this.labelId;
    }

    public String getBackgroundImg() {
        return this.backgroundImg;
    }

    public int getBackgroundImgId() {
        return this.backgroundImgId;
    }

    public int getSupportedModes() {
        return this.supportedModes;
    }

    public List<String> getReqCapabilities() {
        return this.reqCapabilities;
    }

    public List<String> getDeviceTypes() {
        return this.deviceTypes;
    }

    public List<AbilityInfo> getAbilityInfos() {
        return this.abilityInfos;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        if (!(parcel.writeString(this.name) && parcel.writeString(this.description) && parcel.writeInt(this.descriptionId) && parcel.writeString(this.iconPath) && parcel.writeInt(this.iconId) && parcel.writeString(this.label) && parcel.writeInt(this.labelId) && parcel.writeString(this.backgroundImg) && parcel.writeInt(this.backgroundImgId) && parcel.writeInt(this.supportedModes) && parcel.writeInt(this.reqCapabilities.size()))) {
            return false;
        }
        for (String str : this.reqCapabilities) {
            if (!parcel.writeString(str)) {
                return false;
            }
        }
        if (!parcel.writeInt(this.deviceTypes.size())) {
            return false;
        }
        for (String str2 : this.deviceTypes) {
            if (!parcel.writeString(str2)) {
                return false;
            }
        }
        if (!parcel.writeInt(this.abilityInfos.size())) {
            return false;
        }
        for (AbilityInfo abilityInfo : this.abilityInfos) {
            parcel.writeSequenceable(abilityInfo);
        }
        if (parcel.writeString(this.moduleName) && parcel.writeBoolean(this.allowClassMap) && parcel.writeString(this.theme) && parcel.writeInt(this.themeId) && parcel.writeInt(this.colorMode) && parcel.writeString(this.mainAbilityName) && parcel.writeInt(this.installationFree) && parcel.writeString(this.bundleName) && parcel.writeString(this.versionName)) {
            return true;
        }
        return false;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        this.name = parcel.readString();
        this.description = parcel.readString();
        this.descriptionId = parcel.readInt();
        this.iconPath = parcel.readString();
        this.iconId = parcel.readInt();
        this.label = parcel.readString();
        this.labelId = parcel.readInt();
        this.backgroundImg = parcel.readString();
        this.backgroundImgId = parcel.readInt();
        this.supportedModes = parcel.readInt();
        int readInt = parcel.readInt();
        if (readInt > 1024) {
            return false;
        }
        for (int i = 0; i < readInt; i++) {
            this.reqCapabilities.add(parcel.readString());
        }
        int readInt2 = parcel.readInt();
        if (readInt2 > 50) {
            return false;
        }
        for (int i2 = 0; i2 < readInt2; i2++) {
            this.deviceTypes.add(parcel.readString());
        }
        int readInt3 = parcel.readInt();
        if (readInt3 > 1024) {
            return false;
        }
        for (int i3 = 0; i3 < readInt3; i3++) {
            AbilityInfo abilityInfo = new AbilityInfo();
            if (!parcel.readSequenceable(abilityInfo)) {
                return false;
            }
            this.abilityInfos.add(abilityInfo);
        }
        this.moduleName = parcel.readString();
        this.allowClassMap = parcel.readBoolean();
        this.theme = parcel.readString();
        this.themeId = parcel.readInt();
        this.colorMode = parcel.readInt();
        this.mainAbilityName = parcel.readString();
        this.installationFree = parcel.readInt();
        this.bundleName = parcel.readString();
        this.versionName = parcel.readString();
        parcel.readInt();
        parcel.readInt();
        return true;
    }

    public HapModuleInfo parseHapModuleInfo(JSONObject jSONObject) {
        this.name = ProfileConstants.getJsonString(jSONObject, "name");
        this.description = ProfileConstants.getJsonString(jSONObject, "description");
        this.iconPath = ProfileConstants.getJsonString(jSONObject, "iconPath");
        this.label = ProfileConstants.getJsonString(jSONObject, "label");
        if (jSONObject.containsKey("distro")) {
            JSONObject jSONObject2 = jSONObject.getJSONObject("distro");
            this.moduleName = ProfileConstants.getJsonString(jSONObject2, "moduleName");
            if (jSONObject2.containsKey("installationFree")) {
                this.installationFree = ProfileConstants.getJsonBoolean(jSONObject2, "installationFree", true) ? 1 : 0;
            } else {
                this.installationFree = -1;
            }
        }
        if (jSONObject.containsKey("supportedModes")) {
            Iterator it = JSONObject.parseArray(ProfileConstants.getJsonString(jSONObject, "supportedModes"), String.class).iterator();
            while (true) {
                if (it.hasNext()) {
                    if (DRIVE_MODE.equals((String) it.next())) {
                        this.supportedModes = 1;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        if (jSONObject.containsKey("reqCapabilities")) {
            this.reqCapabilities = JSONArray.parseArray(ProfileConstants.getJsonString(jSONObject, "reqCapabilities"), String.class);
        }
        if (jSONObject.containsKey("deviceType")) {
            this.deviceTypes = JSONArray.parseArray(ProfileConstants.getJsonString(jSONObject, "deviceType"), String.class);
            for (String str : this.deviceTypes) {
                if (ContactItem.PHONE.equals(str)) {
                    Collections.replaceAll(this.deviceTypes, ContactItem.PHONE, "default");
                }
            }
        }
        this.mainAbilityName = ProfileConstants.getJsonString(jSONObject, ProfileConstants.MAIN_ABILITY_NAME);
        return this;
    }

    public String getPackageNameForModule(JSONObject jSONObject) {
        return jSONObject.containsKey("package") ? ProfileConstants.getJsonString(jSONObject, "package") : "";
    }

    public boolean isAllowClassMap() {
        return this.allowClassMap;
    }

    public int getThemeId() {
        return this.themeId;
    }

    public void setThemeId(int i) {
        this.themeId = i;
    }

    public int getColorMode() {
        return this.colorMode;
    }

    public void setColorMode(int i) {
        this.colorMode = i;
    }

    @SystemApi
    public String getMainAbilityName() {
        return this.mainAbilityName;
    }

    public AbilityInfo getMainAbility() {
        for (AbilityInfo abilityInfo : this.abilityInfos) {
            if (abilityInfo.getType() == AbilityInfo.AbilityType.PAGE && (this.mainAbilityName.isEmpty() || this.mainAbilityName.equals(abilityInfo.getName()))) {
                return abilityInfo;
            }
        }
        return null;
    }

    public boolean isInstallationFreeSupported() {
        int i = this.installationFree;
        return i == -1 || i != 0;
    }

    public String getBundleName() {
        return this.bundleName;
    }

    public String getVersionName() {
        return this.versionName;
    }
}
