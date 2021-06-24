package ohos.bundle;

import java.util.Objects;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

public class BackupBundleInfo implements Sequenceable {
    public static final Sequenceable.Producer<BackupBundleInfo> PRODUCER = $$Lambda$BackupBundleInfo$AxFdxJA0AOKHUBobBHEYSNo6kSY.INSTANCE;
    private String bundleName;
    private int versionCode;

    @Override // ohos.utils.Sequenceable
    public boolean hasFileDescriptor() {
        return false;
    }

    static /* synthetic */ BackupBundleInfo lambda$static$0(Parcel parcel) {
        BackupBundleInfo backupBundleInfo = new BackupBundleInfo();
        backupBundleInfo.unmarshalling(parcel);
        return backupBundleInfo;
    }

    public BackupBundleInfo() {
        this("", 0);
    }

    public BackupBundleInfo(String str, int i) {
        this.bundleName = str;
        this.versionCode = i;
    }

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        if (parcel.writeString(this.bundleName) && parcel.writeInt(this.versionCode)) {
            return true;
        }
        return false;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        this.bundleName = parcel.readString();
        this.versionCode = parcel.readInt();
        return true;
    }

    public String getBundleName() {
        return this.bundleName;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public String toString() {
        return "BackupBundleInfo[" + this.bundleName + "/" + this.versionCode + "]";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BackupBundleInfo)) {
            return false;
        }
        BackupBundleInfo backupBundleInfo = (BackupBundleInfo) obj;
        return this.versionCode == backupBundleInfo.versionCode && Objects.equals(this.bundleName, backupBundleInfo.bundleName);
    }

    public int hashCode() {
        return Objects.hash(this.bundleName, Integer.valueOf(this.versionCode));
    }
}
