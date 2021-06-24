package ohos.bundle;

import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

public class InstallParam implements Sequenceable {
    public static final int INSTALL_FLAG_DEFAULT = 0;
    public static final int INSTALL_FLAG_REPLACE_EXISTING = 1;
    public static final Sequenceable.Producer<InstallParam> PRODUCER = $$Lambda$InstallParam$ALum7jPDTR1RtvccDZ_0TVDf4n4.INSTANCE;
    public static final int UNSPECIFIED_USER_ID = -2;
    private int installFlag;
    private int installLocation;
    private boolean isKeepData;
    private int userId;

    static /* synthetic */ InstallParam lambda$static$0(Parcel parcel) {
        InstallParam installParam = new InstallParam();
        installParam.unmarshalling(parcel);
        return installParam;
    }

    public InstallParam() {
        this(-2, 0);
    }

    public InstallParam(int i, int i2) {
        this.isKeepData = false;
        this.installLocation = 1;
        this.userId = i;
        this.installFlag = i2;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int i) {
        this.userId = i;
    }

    public int getInstallFlag() {
        return this.installFlag;
    }

    public void setInstallFlag(int i) {
        this.installFlag = i;
    }

    public boolean isKeepData() {
        return this.isKeepData;
    }

    public void setKeepData(boolean z) {
        this.isKeepData = z;
    }

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        if (parcel.writeInt(this.userId) && parcel.writeInt(this.installFlag) && parcel.writeBoolean(this.isKeepData) && parcel.writeInt(this.installLocation)) {
            return true;
        }
        return false;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        this.userId = parcel.readInt();
        this.installFlag = parcel.readInt();
        this.isKeepData = parcel.readBoolean();
        this.installLocation = parcel.readInt();
        return true;
    }
}
