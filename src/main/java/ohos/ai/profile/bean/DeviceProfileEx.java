package ohos.ai.profile.bean;

import java.util.ArrayList;
import java.util.List;
import ohos.ai.engine.utils.Constants;
import ohos.annotation.SystemApi;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

@SystemApi
public class DeviceProfileEx extends DeviceProfile {
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, Constants.DOMAIN_ID, "DeviceProfileEx");
    public static final Sequenceable.Producer<DeviceProfileEx> PRODUCER = $$Lambda$DeviceProfileEx$H1nusH6TMiYWqb1jDbSWYjB6Iys.INSTANCE;
    private List<ServiceProfileEx> services = new ArrayList();

    @Override // ohos.ai.profile.bean.DeviceProfile, ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        return false;
    }

    static /* synthetic */ DeviceProfileEx lambda$static$0(Parcel parcel) {
        DeviceProfileEx deviceProfileEx = new DeviceProfileEx();
        if (parcel.readSequenceable(deviceProfileEx)) {
            return deviceProfileEx;
        }
        return null;
    }

    public void setServices(List<ServiceProfileEx> list) {
        this.services = list;
    }

    public List<ServiceProfileEx> getServices() {
        return this.services;
    }

    @Override // ohos.ai.profile.bean.DeviceProfile, ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        if (!super.unmarshalling(parcel)) {
            return false;
        }
        int readInt = parcel.readInt();
        if (readInt <= 0 || readInt > 2500) {
            HiLog.error(LOG_LABEL, "Service's size is out of range.", new Object[0]);
            return false;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < readInt; i++) {
            arrayList.add(ServiceProfileEx.PRODUCER.createFromParcel(parcel));
        }
        setServices(arrayList);
        return true;
    }

    @Override // ohos.ai.profile.bean.DeviceProfile
    public String toString() {
        return super.toString() + ", services: " + this.services + ']';
    }
}
