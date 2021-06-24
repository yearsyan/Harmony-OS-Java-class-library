package ohos.ai.profile.bean;

import java.util.HashMap;
import java.util.Map;
import ohos.annotation.SystemApi;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;
import ohos.utils.fastjson.JSONObject;
import ohos.utils.fastjson.TypeReference;
import ohos.utils.fastjson.parser.Feature;

@SystemApi
public class ServiceCharacteristicProfile implements Sequenceable {
    public static final Sequenceable.Producer<ServiceCharacteristicProfile> PRODUCER = $$Lambda$ServiceCharacteristicProfile$dbOpOiniRWyr0AFksUOK06bBPss.INSTANCE;
    private Map<String, Object> characteristicProfileMap = new HashMap();
    private String deviceId = "";
    private boolean isNeedCloud;
    private boolean isNeedNearField;
    private String serviceCharacterType = "";
    private String serviceId = "";

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        return false;
    }

    static /* synthetic */ ServiceCharacteristicProfile lambda$static$0(Parcel parcel) {
        ServiceCharacteristicProfile serviceCharacteristicProfile = new ServiceCharacteristicProfile();
        if (parcel.readSequenceable(serviceCharacteristicProfile)) {
            return serviceCharacteristicProfile;
        }
        return null;
    }

    public Map<String, Object> getProfile() {
        return this.characteristicProfileMap;
    }

    public boolean addEntityInfo(String str, Object obj) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        this.characteristicProfileMap.put(str, obj);
        return true;
    }

    public boolean addEntities(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return false;
        }
        HashMap hashMap = new HashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey() != null && !entry.getKey().isEmpty()) {
                hashMap.put(entry.getKey(), entry.getValue());
            }
        }
        if (hashMap.isEmpty()) {
            return false;
        }
        this.characteristicProfileMap.putAll(hashMap);
        return true;
    }

    public boolean setId(String str) {
        if (str == null || str.isEmpty() || str.contains("/")) {
            return false;
        }
        this.serviceCharacterType = str;
        return true;
    }

    public String getId() {
        return this.serviceCharacterType;
    }

    public void setIsNeedCloud(boolean z) {
        this.isNeedCloud = z;
    }

    public boolean getIsNeedCloud() {
        return this.isNeedCloud;
    }

    public void setIsNeedNearField(boolean z) {
        this.isNeedNearField = z;
    }

    public boolean getIsNeedNearField() {
        return this.isNeedNearField;
    }

    public boolean setDeviceId(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        this.deviceId = str;
        return true;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public boolean setServiceId(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        this.serviceId = str;
        return true;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        String readString = parcel.readString();
        if ("".equals(readString)) {
            readString = null;
        }
        setId(readString);
        String readString2 = parcel.readString();
        if ("".equals(readString2)) {
            readString2 = null;
        }
        setDeviceId(readString2);
        String readString3 = parcel.readString();
        if ("".equals(readString3)) {
            readString3 = null;
        }
        setServiceId(readString3);
        setIsNeedCloud(parcel.readInt() != 0);
        setIsNeedNearField(parcel.readInt() != 0);
        addEntities((Map) JSONObject.parseObject(parcel.readString(), new TypeReference<Map<String, Object>>() {
            /* class ohos.ai.profile.bean.ServiceCharacteristicProfile.AnonymousClass1 */
        }, new Feature[0]));
        return true;
    }

    public String toString() {
        return "serviceCharacterType is " + this.serviceCharacterType + " ,isNeedCloud is " + this.isNeedCloud + " ,isNeedNearField is " + this.isNeedNearField;
    }
}
