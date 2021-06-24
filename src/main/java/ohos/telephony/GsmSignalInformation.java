package ohos.telephony;

import java.util.Objects;
import ohos.annotation.SystemApi;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

public final class GsmSignalInformation extends SignalInformation implements Sequenceable {
    private static final String SIGNALTYPENAME = GsmSignalInformation.class.getSimpleName();
    private int gsmRssi = Integer.MAX_VALUE;
    private int signalLevel = Integer.MAX_VALUE;
    private int timingAdvance;

    protected GsmSignalInformation() {
        super(1);
    }

    @SystemApi
    public int getRssi() {
        return this.gsmRssi;
    }

    @Override // ohos.telephony.SignalInformation
    @SystemApi
    public int getSignalStrength() {
        return this.gsmRssi;
    }

    @Override // ohos.telephony.SignalInformation
    public int getSignalLevel() {
        return this.signalLevel;
    }

    @Override // ohos.telephony.SignalInformation
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Integer.valueOf(this.gsmRssi), Integer.valueOf(this.signalLevel));
    }

    @Override // ohos.telephony.SignalInformation
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GsmSignalInformation)) {
            return false;
        }
        GsmSignalInformation gsmSignalInformation = (GsmSignalInformation) obj;
        return super.equals(obj) && this.gsmRssi == gsmSignalInformation.gsmRssi && this.signalLevel == gsmSignalInformation.signalLevel && this.timingAdvance == gsmSignalInformation.timingAdvance;
    }

    public String toString() {
        return SIGNALTYPENAME + "{ gsmRssi=" + this.gsmRssi + ", signalLevel=" + this.signalLevel + ", timingAdvance=" + this.timingAdvance + "}";
    }

    @Override // ohos.telephony.SignalInformation, ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        if (parcel == null) {
            return false;
        }
        super.marshalling(parcel);
        parcel.writeInt(this.gsmRssi);
        parcel.writeInt(this.signalLevel);
        parcel.writeInt(this.timingAdvance);
        return true;
    }

    @Override // ohos.telephony.SignalInformation, ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        if (parcel == null) {
            return false;
        }
        super.unmarshalling(parcel);
        this.gsmRssi = parcel.readInt();
        this.signalLevel = parcel.readInt();
        this.timingAdvance = parcel.readInt();
        return true;
    }

    @SystemApi
    public int getTimeAdvance() {
        return this.timingAdvance;
    }
}
