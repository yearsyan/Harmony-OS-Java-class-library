package ohos.ai.profile.bean;

import ohos.annotation.SystemApi;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

@SystemApi
public class ServiceProfileEx extends ServiceProfile {
    public static final Sequenceable.Producer<ServiceProfileEx> PRODUCER = $$Lambda$ServiceProfileEx$BawBUR9roQpewV5OnmuOOpdcxk4.INSTANCE;
    private ServiceCharacteristicProfile characters;

    @Override // ohos.ai.profile.bean.ServiceProfile, ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        return false;
    }

    static /* synthetic */ ServiceProfileEx lambda$static$0(Parcel parcel) {
        ServiceProfileEx serviceProfileEx = new ServiceProfileEx();
        if (parcel.readSequenceable(serviceProfileEx)) {
            return serviceProfileEx;
        }
        return null;
    }

    public ServiceCharacteristicProfile getCharacters() {
        return this.characters;
    }

    public void setCharacters(ServiceCharacteristicProfile serviceCharacteristicProfile) {
        this.characters = serviceCharacteristicProfile;
    }

    @Override // ohos.ai.profile.bean.ServiceProfile, ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        if (!super.unmarshalling(parcel)) {
            return false;
        }
        setCharacters(ServiceCharacteristicProfile.PRODUCER.createFromParcel(parcel));
        return true;
    }

    @Override // ohos.ai.profile.bean.ServiceProfile
    public String toString() {
        return super.toString() + ", characters: " + this.characters;
    }
}
