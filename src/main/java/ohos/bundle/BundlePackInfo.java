package ohos.bundle;

import java.util.ArrayList;
import java.util.List;
import ohos.annotation.SystemApi;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

@SystemApi
public class BundlePackInfo implements Sequenceable {
    public static final int GET_BUNDLE_SUMMARY = 2;
    public static final int GET_MODULE_SUMMARY = 4;
    public static final int GET_PACKAGES = 1;
    private static final int MAX_DEVICETYPE_SIZE = 50;
    private static final int MAX_DIMENSION_SIZE = 10;
    private static final int MAX_LIMIT_SIZE = 100;
    public List<PackageConfig> packages = new ArrayList();
    public PackageSummary summary = new PackageSummary();

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        int readInt = parcel.readInt();
        if (readInt > 100) {
            return false;
        }
        for (int i = 0; i < readInt; i++) {
            PackageConfig packageConfig = new PackageConfig();
            parcel.readSequenceable(packageConfig);
            this.packages.add(packageConfig);
        }
        parcel.readSequenceable(this.summary);
        return true;
    }

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        parcel.writeInt(this.packages.size());
        for (PackageConfig packageConfig : this.packages) {
            parcel.writeSequenceable(packageConfig);
        }
        parcel.writeSequenceable(this.summary);
        return true;
    }

    public class PackageConfig implements Sequenceable {
        public boolean deliveryWithInstall;
        public List<String> deviceType = new ArrayList();
        public String moduleType;
        public String name;

        PackageConfig() {
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            int readInt = parcel.readInt();
            if (readInt > 50) {
                return false;
            }
            for (int i = 0; i < readInt; i++) {
                this.deviceType.add(parcel.readString());
            }
            this.name = parcel.readString();
            this.moduleType = parcel.readString();
            this.deliveryWithInstall = parcel.readBoolean();
            return true;
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            parcel.writeInt(this.deviceType.size());
            for (String str : this.deviceType) {
                parcel.writeString(str);
            }
            parcel.writeString(this.name);
            parcel.writeString(this.moduleType);
            parcel.writeBoolean(this.deliveryWithInstall);
            return true;
        }
    }

    public class PackageSummary implements Sequenceable {
        public BundleConfigInfo app;
        public List<ModuleConfigInfo> modules = new ArrayList();

        PackageSummary() {
            this.app = new BundleConfigInfo();
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            parcel.readSequenceable(this.app);
            int readInt = parcel.readInt();
            if (readInt > 100) {
                return false;
            }
            for (int i = 0; i < readInt; i++) {
                ModuleConfigInfo moduleConfigInfo = new ModuleConfigInfo();
                parcel.readSequenceable(moduleConfigInfo);
                this.modules.add(moduleConfigInfo);
            }
            return true;
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            parcel.writeSequenceable(this.app);
            parcel.writeInt(this.modules.size());
            for (ModuleConfigInfo moduleConfigInfo : this.modules) {
                parcel.writeSequenceable(moduleConfigInfo);
            }
            return true;
        }
    }

    public class BundleConfigInfo implements Sequenceable {
        public String bundleName;
        public Version version;

        BundleConfigInfo() {
            this.version = new Version();
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            this.bundleName = parcel.readString();
            parcel.readSequenceable(this.version);
            return true;
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            parcel.writeString(this.bundleName);
            parcel.writeSequenceable(this.version);
            return true;
        }
    }

    public class ModuleConfigInfo implements Sequenceable {
        public List<ModuleAbilityInfo> abilities;
        public ApiVersion apiVersion;
        public List<String> deviceType = new ArrayList();
        public ModuleDistroInfo distro;

        ModuleConfigInfo() {
            this.apiVersion = new ApiVersion();
            this.distro = new ModuleDistroInfo();
            this.abilities = new ArrayList();
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            parcel.readSequenceable(this.apiVersion);
            int readInt = parcel.readInt();
            if (readInt > 100) {
                return false;
            }
            for (int i = 0; i < readInt; i++) {
                this.deviceType.add(parcel.readString());
            }
            parcel.readSequenceable(this.distro);
            int readInt2 = parcel.readInt();
            if (readInt2 > 100) {
                return false;
            }
            for (int i2 = 0; i2 < readInt2; i2++) {
                ModuleAbilityInfo moduleAbilityInfo = new ModuleAbilityInfo();
                parcel.readSequenceable(moduleAbilityInfo);
                this.abilities.add(moduleAbilityInfo);
            }
            return true;
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            parcel.writeSequenceable(this.apiVersion);
            parcel.writeInt(this.deviceType.size());
            for (String str : this.deviceType) {
                parcel.writeString(str);
            }
            parcel.writeSequenceable(this.distro);
            parcel.writeInt(this.abilities.size());
            for (ModuleAbilityInfo moduleAbilityInfo : this.abilities) {
                parcel.writeSequenceable(moduleAbilityInfo);
            }
            return true;
        }
    }

    public class ModuleDistroInfo implements Sequenceable {
        public boolean deliveryWithInstall;
        public boolean installationFree;
        public String mainAbility;
        public String moduleName;
        public String moduleType;

        ModuleDistroInfo() {
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            this.deliveryWithInstall = parcel.readBoolean();
            this.installationFree = parcel.readBoolean();
            this.moduleName = parcel.readString();
            this.moduleType = parcel.readString();
            this.mainAbility = parcel.readString();
            return true;
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            parcel.writeBoolean(this.deliveryWithInstall);
            parcel.writeBoolean(this.installationFree);
            parcel.writeString(this.moduleName);
            parcel.writeString(this.moduleType);
            parcel.writeString(this.mainAbility);
            return true;
        }
    }

    public class ModuleAbilityInfo implements Sequenceable {
        public List<AbilityFormInfo> forms = new ArrayList();
        public String label;
        public String name;
        public boolean visible;

        ModuleAbilityInfo() {
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            this.name = parcel.readString();
            this.label = parcel.readString();
            this.visible = parcel.readBoolean();
            int readInt = parcel.readInt();
            if (readInt > 100) {
                return false;
            }
            for (int i = 0; i < readInt; i++) {
                AbilityFormInfo abilityFormInfo = new AbilityFormInfo();
                parcel.readSequenceable(abilityFormInfo);
                this.forms.add(abilityFormInfo);
            }
            return true;
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            parcel.writeString(this.name);
            parcel.writeString(this.label);
            parcel.writeBoolean(this.visible);
            parcel.writeInt(this.forms.size());
            for (AbilityFormInfo abilityFormInfo : this.forms) {
                parcel.writeSequenceable(abilityFormInfo);
            }
            return true;
        }
    }

    public class AbilityFormInfo implements Sequenceable {
        public int defaultDimension;
        public String name;
        public String scheduledUpateTime;
        public List<Integer> supportDimensions = new ArrayList();
        public String type;
        public int updateDuration;
        public boolean updateEnabled;

        AbilityFormInfo() {
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            this.name = parcel.readString();
            this.type = parcel.readString();
            this.updateEnabled = parcel.readBoolean();
            this.scheduledUpateTime = parcel.readString();
            this.updateDuration = parcel.readInt();
            int readInt = parcel.readInt();
            if (readInt > 10) {
                return false;
            }
            for (int i = 0; i < readInt; i++) {
                this.supportDimensions.add(Integer.valueOf(parcel.readInt()));
            }
            this.defaultDimension = parcel.readInt();
            return true;
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            parcel.writeString(this.name);
            parcel.writeString(this.type);
            parcel.writeBoolean(this.updateEnabled);
            parcel.writeString(this.scheduledUpateTime);
            parcel.writeInt(this.updateDuration);
            parcel.writeInt(this.supportDimensions.size());
            for (Integer num : this.supportDimensions) {
                parcel.writeInt(num.intValue());
            }
            parcel.writeInt(this.defaultDimension);
            return true;
        }
    }

    public class Version implements Sequenceable {
        public int code;
        public int minCompatibleVersionCode;
        public String name;

        Version() {
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            this.minCompatibleVersionCode = parcel.readInt();
            this.name = parcel.readString();
            this.code = parcel.readInt();
            return true;
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            parcel.writeInt(this.minCompatibleVersionCode);
            parcel.writeString(this.name);
            parcel.writeInt(this.code);
            return true;
        }
    }

    public class ApiVersion implements Sequenceable {
        public int compatible;
        public String releaseType;
        public int target;

        ApiVersion() {
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            this.releaseType = parcel.readString();
            this.compatible = parcel.readInt();
            this.target = parcel.readInt();
            return true;
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            parcel.writeString(this.releaseType);
            parcel.writeInt(this.compatible);
            parcel.writeInt(this.target);
            return true;
        }
    }
}
