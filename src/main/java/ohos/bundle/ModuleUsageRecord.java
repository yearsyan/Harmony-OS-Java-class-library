package ohos.bundle;

import ohos.annotation.SystemApi;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

@SystemApi
public class ModuleUsageRecord implements Sequenceable {
    public static final Sequenceable.Producer<ModuleUsageRecord> PRODUCER = $$Lambda$ModuleUsageRecord$Jdkz167HiUzleyhpq5mKb0mn0c.INSTANCE;
    private int abilityDescriptionId;
    private int abilityIconId;
    private int abilityLabelId;
    private String abilityName = "";
    private int appLabelId;
    private String bundleName = "";
    private int descriptionId;
    private boolean installationFreeSupported = true;
    private boolean isRemoved;
    private int labelId;
    private long lastLaunchTime;
    private int launchedCount;
    private String name = "";

    static /* synthetic */ ModuleUsageRecord lambda$static$0(Parcel parcel) {
        ModuleUsageRecord moduleUsageRecord = new ModuleUsageRecord();
        moduleUsageRecord.unmarshalling(parcel);
        return moduleUsageRecord;
    }

    public static Sequenceable.Producer<ModuleUsageRecord> getPRODUCER() {
        return PRODUCER;
    }

    public String getBundleName() {
        return this.bundleName;
    }

    public int getAppLabelId() {
        return this.appLabelId;
    }

    public String getName() {
        return this.name;
    }

    public int getLabelId() {
        return this.labelId;
    }

    public int getDescriptionId() {
        return this.descriptionId;
    }

    public String getAbilityName() {
        return this.abilityName;
    }

    public int getAbilityLabelId() {
        return this.abilityLabelId;
    }

    public int getAbilityDescriptionId() {
        return this.abilityDescriptionId;
    }

    public int getAbilityIconId() {
        return this.abilityIconId;
    }

    public int getLaunchedCount() {
        return this.launchedCount;
    }

    public long getLastLaunchTime() {
        return this.lastLaunchTime;
    }

    public boolean isRemoved() {
        return this.isRemoved;
    }

    public boolean isInstallationFreeSupported() {
        return this.installationFreeSupported;
    }

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        if (parcel.writeString(this.bundleName) && parcel.writeInt(this.appLabelId) && parcel.writeString(this.name) && parcel.writeInt(this.labelId) && parcel.writeInt(this.descriptionId) && parcel.writeString(this.abilityName) && parcel.writeInt(this.abilityLabelId) && parcel.writeInt(this.abilityDescriptionId) && parcel.writeInt(this.abilityIconId) && parcel.writeInt(this.launchedCount) && parcel.writeLong(this.lastLaunchTime) && parcel.writeBoolean(this.isRemoved) && parcel.writeBoolean(this.installationFreeSupported)) {
            return true;
        }
        return false;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        this.bundleName = parcel.readString();
        this.appLabelId = parcel.readInt();
        this.name = parcel.readString();
        this.labelId = parcel.readInt();
        this.descriptionId = parcel.readInt();
        this.abilityName = parcel.readString();
        this.abilityLabelId = parcel.readInt();
        this.abilityDescriptionId = parcel.readInt();
        this.abilityIconId = parcel.readInt();
        this.launchedCount = parcel.readInt();
        this.lastLaunchTime = parcel.readLong();
        this.isRemoved = parcel.readBoolean();
        this.installationFreeSupported = parcel.readBoolean();
        return true;
    }
}
