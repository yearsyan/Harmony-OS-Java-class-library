package ohos.bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ohos.annotation.SystemApi;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;
import ohos.utils.fastjson.JSONArray;
import ohos.utils.fastjson.JSONObject;

public class BundleInfo implements Sequenceable {
    private static final int GET_BUNDLE_WITH_ABILITIES = 1;
    private static final int GET_BUNDLE_WITH_REQUESTED_PERMISSION = 16;
    private static final String INVALID_VERSION = "-1";
    private static final int MAX_LIMIT_SIZE = 1024;
    public static final Sequenceable.Producer<BundleInfo> PRODUCER = $$Lambda$BundleInfo$xVR71dMQry20SoSA5q2p0al0wOc.INSTANCE;
    public List<AbilityInfo> abilityInfos;
    public String appId;
    public ApplicationInfo appInfo;
    private int compatibleVersion;
    private String cpuAbi;
    public boolean debug;
    private boolean directLaunch;
    private int entryInstallationFree;
    private String entryModuleName;
    private List<HapModuleInfo> hapModuleInfos;
    public long installTime;
    private boolean isCompressNativeLibs;
    private boolean isSilentInstallation;
    public String jointUserId;
    private JSONObject jsonExtraObject;
    private boolean keepAlive;
    private int minCompatibleVersionCode;
    private List<ModuleInfo> moduleInfos;
    private boolean multiFrameworkBundle;
    public String name;
    public String originalName;
    private String process;
    public List<ReqPermissionDetail> reqPermissionDetails;
    public List<String> reqPermissions;
    private boolean supportBackUp;
    private boolean systemApp;
    private int targetVersion;
    public String type;
    public int uid;
    public long updateTime;
    private String vendor;
    private int versionCode;
    private String versionName;

    static /* synthetic */ BundleInfo lambda$static$0(Parcel parcel) {
        BundleInfo bundleInfo = new BundleInfo();
        bundleInfo.unmarshalling(parcel);
        return bundleInfo;
    }

    public BundleInfo() {
        this(null);
    }

    public BundleInfo(BundleInfo bundleInfo) {
        this.name = "";
        this.originalName = "";
        this.type = "normal";
        this.appId = "";
        this.uid = -1;
        this.debug = false;
        this.installTime = 0;
        this.updateTime = 0;
        this.appInfo = new ApplicationInfo();
        this.abilityInfos = new ArrayList();
        this.jointUserId = "";
        this.reqPermissions = new ArrayList(0);
        this.reqPermissionDetails = new ArrayList(0);
        this.vendor = "";
        this.versionCode = 0;
        this.versionName = "";
        this.compatibleVersion = 0;
        this.minCompatibleVersionCode = 0;
        this.multiFrameworkBundle = false;
        this.targetVersion = 0;
        this.process = "";
        this.keepAlive = false;
        this.directLaunch = false;
        this.supportBackUp = false;
        this.isCompressNativeLibs = true;
        this.systemApp = false;
        this.cpuAbi = "";
        this.hapModuleInfos = new ArrayList(0);
        this.moduleInfos = new ArrayList(0);
        this.entryModuleName = "";
        this.entryInstallationFree = -1;
        this.isSilentInstallation = false;
        if (bundleInfo != null) {
            this.name = bundleInfo.name;
            this.originalName = bundleInfo.originalName;
            this.vendor = bundleInfo.vendor;
            this.versionCode = bundleInfo.versionCode;
            this.versionName = bundleInfo.versionName;
            this.compatibleVersion = bundleInfo.compatibleVersion;
            this.minCompatibleVersionCode = bundleInfo.minCompatibleVersionCode;
            this.targetVersion = bundleInfo.targetVersion;
            this.process = bundleInfo.process;
            this.keepAlive = bundleInfo.keepAlive;
            this.directLaunch = bundleInfo.directLaunch;
            this.supportBackUp = bundleInfo.supportBackUp;
            this.isCompressNativeLibs = bundleInfo.isCompressNativeLibs;
            this.systemApp = bundleInfo.systemApp;
            this.cpuAbi = bundleInfo.cpuAbi;
            this.appId = bundleInfo.appId;
            this.uid = bundleInfo.uid;
            this.jointUserId = bundleInfo.jointUserId;
            this.multiFrameworkBundle = bundleInfo.multiFrameworkBundle;
            List<String> list = bundleInfo.reqPermissions;
            if (list != null) {
                this.reqPermissions.addAll(list);
            }
            List<ReqPermissionDetail> list2 = bundleInfo.reqPermissionDetails;
            if (list2 != null) {
                this.reqPermissionDetails.addAll(list2);
            }
            this.appInfo = new ApplicationInfo(bundleInfo.appInfo);
            List<HapModuleInfo> list3 = bundleInfo.hapModuleInfos;
            if (list3 != null) {
                this.hapModuleInfos.addAll(list3);
            }
            List<AbilityInfo> list4 = bundleInfo.abilityInfos;
            if (list4 != null) {
                this.abilityInfos.addAll(list4);
            }
            List<ModuleInfo> list5 = bundleInfo.moduleInfos;
            if (list5 != null) {
                this.moduleInfos.addAll(list5);
            }
            this.debug = bundleInfo.debug;
            this.entryModuleName = bundleInfo.entryModuleName;
            this.entryInstallationFree = bundleInfo.entryInstallationFree;
            this.isSilentInstallation = bundleInfo.isSilentInstallation;
            this.installTime = bundleInfo.installTime;
            this.updateTime = bundleInfo.updateTime;
            this.type = bundleInfo.type;
        }
    }

    public String getName() {
        return this.name;
    }

    public String getOriginalName() {
        return this.originalName;
    }

    public String getType() {
        return this.type;
    }

    public List<String> getPermissions() {
        ApplicationInfo applicationInfo = this.appInfo;
        if (applicationInfo == null) {
            return Collections.emptyList();
        }
        return applicationInfo.getPermissions();
    }

    public List<String> getHapModuleNames() {
        ArrayList arrayList = new ArrayList();
        for (HapModuleInfo hapModuleInfo : this.hapModuleInfos) {
            arrayList.add(hapModuleInfo.getName());
        }
        return arrayList;
    }

    public List<String> getModuleNames() {
        ArrayList arrayList = new ArrayList();
        for (ModuleInfo moduleInfo : this.moduleInfos) {
            arrayList.add(moduleInfo.getModuleName());
        }
        return arrayList;
    }

    public List<String> getModulePublicDirs() {
        ArrayList arrayList = new ArrayList();
        for (ModuleInfo moduleInfo : this.moduleInfos) {
            String moduleSourceDir = moduleInfo.getModuleSourceDir();
            int lastIndexOf = moduleSourceDir.lastIndexOf("/");
            if (lastIndexOf >= 0 && moduleSourceDir.substring(lastIndexOf).contains(".hap")) {
                moduleSourceDir = moduleSourceDir.substring(0, lastIndexOf);
            }
            arrayList.add(moduleSourceDir);
        }
        return arrayList;
    }

    public List<String> getModuleDirs() {
        ArrayList arrayList = new ArrayList();
        for (ModuleInfo moduleInfo : this.moduleInfos) {
            arrayList.add(moduleInfo.getModuleSourceDir());
        }
        return arrayList;
    }

    public String getVendor() {
        return this.vendor;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public int getMinSdkVersion() {
        return this.compatibleVersion;
    }

    public int getMaxSdkVersion() {
        return this.targetVersion;
    }

    public int getCompatibleVersion() {
        return this.compatibleVersion;
    }

    public int getMinCompatibleVersionCode() {
        return this.minCompatibleVersionCode;
    }

    public boolean isMultiFrameworkBundle() {
        return this.multiFrameworkBundle;
    }

    public int getTargetVersion() {
        return this.targetVersion;
    }

    public ApplicationInfo getAppInfo() {
        return this.appInfo;
    }

    public List<AbilityInfo> getAbilityInfos() {
        return this.abilityInfos;
    }

    public AbilityInfo getAbilityInfoByName(String str) {
        for (AbilityInfo abilityInfo : this.abilityInfos) {
            if (abilityInfo.getClassName().equals(str)) {
                return abilityInfo;
            }
        }
        return null;
    }

    public AbilityInfo getAbilityInfoByOriginalName(String str) {
        for (AbilityInfo abilityInfo : this.abilityInfos) {
            if (str.startsWith((abilityInfo.getOriginalClassName() == null || abilityInfo.getOriginalClassName().isEmpty()) ? abilityInfo.getClassName() : abilityInfo.getOriginalClassName())) {
                return abilityInfo;
            }
        }
        return null;
    }

    public String getAppId() {
        return this.appId;
    }

    public int getUid() {
        return this.uid;
    }

    public String getJointUserId() {
        return this.jointUserId;
    }

    public boolean isDifferentName() {
        return !this.originalName.isEmpty() && !this.name.equals(this.originalName);
    }

    public String getCpuAbi() {
        return this.cpuAbi;
    }

    public boolean getCompressNativeLibs() {
        return this.isCompressNativeLibs;
    }

    public String getEntryModuleName() {
        return this.entryModuleName;
    }

    /* JADX WARNING: Removed duplicated region for block: B:77:0x0110  */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x015c  */
    @Override // ohos.utils.Sequenceable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean marshalling(ohos.utils.Parcel r5) {
        /*
        // Method dump skipped, instructions count: 455
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bundle.BundleInfo.marshalling(ohos.utils.Parcel):boolean");
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        int readInt;
        this.name = parcel.readString();
        this.originalName = parcel.readString();
        this.vendor = parcel.readString();
        this.versionName = parcel.readString();
        this.versionCode = parcel.readInt();
        this.compatibleVersion = parcel.readInt();
        this.targetVersion = parcel.readInt();
        this.minCompatibleVersionCode = parcel.readInt();
        this.multiFrameworkBundle = parcel.readBoolean();
        this.process = parcel.readString();
        this.keepAlive = parcel.readBoolean();
        this.directLaunch = parcel.readBoolean();
        this.supportBackUp = parcel.readBoolean();
        this.isCompressNativeLibs = parcel.readBoolean();
        this.systemApp = parcel.readBoolean();
        this.cpuAbi = parcel.readString();
        this.appId = parcel.readString();
        this.uid = parcel.readInt();
        this.jointUserId = parcel.readString();
        if (!parcel.readSequenceable(this.appInfo) || (readInt = parcel.readInt()) > 1024) {
            return false;
        }
        for (int i = 0; i < readInt; i++) {
            HapModuleInfo hapModuleInfo = new HapModuleInfo();
            if (!parcel.readSequenceable(hapModuleInfo)) {
                return false;
            }
            this.hapModuleInfos.add(hapModuleInfo);
        }
        int readInt2 = parcel.readInt();
        if (readInt2 > 1024) {
            return false;
        }
        for (int i2 = 0; i2 < readInt2; i2++) {
            AbilityInfo abilityInfo = new AbilityInfo();
            if (!parcel.readSequenceable(abilityInfo)) {
                return false;
            }
            this.abilityInfos.add(abilityInfo);
        }
        int readInt3 = parcel.readInt();
        if (readInt3 > 1024) {
            return false;
        }
        for (int i3 = 0; i3 < readInt3; i3++) {
            this.moduleInfos.add(new ModuleInfo(parcel.readString(), parcel.readString()));
        }
        this.debug = parcel.readBoolean();
        this.entryModuleName = parcel.readString();
        this.entryInstallationFree = parcel.readInt();
        this.reqPermissions = parcel.readStringList();
        int readInt4 = parcel.readInt();
        if (readInt4 > 1024) {
            return false;
        }
        for (int i4 = 0; i4 < readInt4; i4++) {
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            String readString3 = parcel.readString();
            int readInt5 = parcel.readInt();
            String[] strArr = new String[readInt5];
            for (int i5 = 0; i5 < readInt5; i5++) {
                strArr[i5] = parcel.readString();
            }
            String readString4 = parcel.readString();
            ReqPermissionDetail reqPermissionDetail = new ReqPermissionDetail(readString, readString2, readString3);
            reqPermissionDetail.setUsedScene(new ReqPermissionDetail.UsedScene(readString4, strArr));
            this.reqPermissionDetails.add(reqPermissionDetail);
        }
        this.isSilentInstallation = parcel.readBoolean();
        this.installTime = parcel.readLong();
        this.updateTime = parcel.readLong();
        this.type = parcel.readString();
        parcel.readString();
        return true;
    }

    public void parseBundle(String str, int i) {
        JSONObject parseObject = JSONObject.parseObject(str);
        if (parseObject != null && parseObject.containsKey(ProfileConstants.APP) && parseObject.containsKey(ProfileConstants.DEVICE_CONFIG) && parseObject.containsKey(ProfileConstants.MODULE)) {
            JSONObject jSONObject = parseObject.getJSONObject(ProfileConstants.APP);
            this.name = ProfileConstants.getJsonString(jSONObject, ProfileConstants.BUNDLE_NAME);
            this.type = ProfileConstants.getJsonString(jSONObject, "type");
            this.vendor = ProfileConstants.getJsonString(jSONObject, ProfileConstants.VENDOR);
            JSONObject jSONObject2 = jSONObject.getJSONObject("version");
            this.versionName = ProfileConstants.getJsonString(jSONObject2, "name");
            this.versionCode = ProfileConstants.getJsonInt(jSONObject2, "code");
            this.minCompatibleVersionCode = ProfileConstants.getJsonInt(jSONObject2, ProfileConstants.MIN_COMPATIBLE);
            this.multiFrameworkBundle = ProfileConstants.getJsonBoolean(jSONObject, ProfileConstants.MULTI_FRAMEWORK, false);
            JSONObject jSONObject3 = jSONObject.getJSONObject(ProfileConstants.API_VERSION);
            this.compatibleVersion = ProfileConstants.getJsonInt(jSONObject3, ProfileConstants.SDK_COMPATIBLE);
            this.targetVersion = ProfileConstants.getJsonInt(jSONObject3, ProfileConstants.SDK_TARGET);
            JSONObject jSONObject4 = parseObject.getJSONObject(ProfileConstants.DEVICE_CONFIG);
            if (jSONObject4.containsKey("default")) {
                JSONObject jSONObject5 = jSONObject4.getJSONObject("default");
                this.jointUserId = ProfileConstants.getJsonString(jSONObject5, ProfileConstants.JOINT_USER_ID);
                this.directLaunch = ProfileConstants.getJsonBoolean(jSONObject5, "directLaunch", false);
                this.keepAlive = ProfileConstants.getJsonBoolean(jSONObject5, "keepAlive", false);
                this.process = ProfileConstants.getJsonString(jSONObject5, "process");
                this.supportBackUp = ProfileConstants.getJsonBoolean(jSONObject5, "supportBackup", false);
            }
            JSONObject jSONObject6 = parseObject.getJSONObject(ProfileConstants.MODULE);
            HapModuleInfo parseHapModuleInfo = new HapModuleInfo().parseHapModuleInfo(jSONObject6);
            String packageNameForModule = parseHapModuleInfo.getPackageNameForModule(jSONObject6);
            if ((i & 1) == 1) {
                this.abilityInfos = parseAbility(jSONObject6, this.name, packageNameForModule, parseHapModuleInfo);
            }
            this.appInfo = new ApplicationInfo().parseApplication(parseHapModuleInfo, packageNameForModule);
            this.hapModuleInfos.add(parseHapModuleInfo);
        }
    }

    private List<AbilityInfo> parseAbility(JSONObject jSONObject, String str, String str2, HapModuleInfo hapModuleInfo) {
        ArrayList arrayList = new ArrayList();
        if (jSONObject.containsKey(ProfileConstants.ABILITIES)) {
            JSONArray jSONArray = jSONObject.getJSONArray(ProfileConstants.ABILITIES);
            int size = jSONArray.size();
            for (int i = 0; i < size; i++) {
                arrayList.add(new AbilityInfo().parseAbility(jSONArray.getJSONObject(i), str, str2, hapModuleInfo));
            }
        }
        return arrayList;
    }

    public String getModuleDir(String str) {
        for (ModuleInfo moduleInfo : this.moduleInfos) {
            if (moduleInfo.getModuleName().equals(str)) {
                return moduleInfo.getModuleSourceDir();
            }
        }
        return "";
    }

    @SystemApi
    public HapModuleInfo getHapModuleInfo(String str) {
        for (HapModuleInfo hapModuleInfo : this.hapModuleInfos) {
            if (hapModuleInfo.getModuleName().equals(str)) {
                return hapModuleInfo;
            }
        }
        return null;
    }

    public List<String> getReqPermissions() {
        return this.reqPermissions;
    }

    public List<ReqPermissionDetail> getReqPermissionDetail() {
        return this.reqPermissionDetails;
    }

    public Boolean isSilentInstallation() {
        return Boolean.valueOf(this.isSilentInstallation);
    }

    public long getInstallTime() {
        return this.installTime;
    }

    public long getUpdateTime() {
        return this.updateTime;
    }

    public class ReqPermissionDetail {
        String name;
        String reason;
        String reasonId;
        UsedScene usedScene;

        public ReqPermissionDetail(String str, String str2) {
            this.name = str;
            this.reason = str2;
        }

        private ReqPermissionDetail(String str, String str2, String str3) {
            this.name = str;
            this.reason = str2;
            this.reasonId = str3;
        }

        public class UsedScene {
            String[] abilities;
            String when;

            public UsedScene(String str, String[] strArr) {
                this.when = str;
                this.abilities = strArr;
            }

            public String[] getAbilities() {
                return this.abilities;
            }

            public String getWhen() {
                return this.when;
            }
        }

        public String getName() {
            return this.name;
        }

        public String getReason() {
            return this.reason;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private String getReasonIds() {
            return this.reasonId;
        }

        @SystemApi
        public int getReasonId() {
            try {
                return Integer.parseInt(this.reasonId);
            } catch (NumberFormatException unused) {
                return -1;
            }
        }

        public UsedScene getUsedScene() {
            return this.usedScene;
        }

        public void setUsedScene(UsedScene usedScene2) {
            this.usedScene = usedScene2;
        }
    }
}
