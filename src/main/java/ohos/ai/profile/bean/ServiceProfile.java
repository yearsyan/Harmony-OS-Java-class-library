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
public class ServiceProfile implements Sequenceable {
    public static final Sequenceable.Producer<ServiceProfile> PRODUCER = $$Lambda$ServiceProfile$aX0FIRkYp6O7YMvMyYbCpryd_w.INSTANCE;
    private String deviceId = "";
    private boolean isNeedCloud;
    private boolean isNeedNearField;
    private String serviceId = "";
    private Map<String, Object> serviceProfileMap = new HashMap();
    private String serviceType = "";

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        return false;
    }

    static /* synthetic */ ServiceProfile lambda$static$0(Parcel parcel) {
        ServiceProfile serviceProfile = new ServiceProfile();
        if (parcel.readSequenceable(serviceProfile)) {
            return serviceProfile;
        }
        return null;
    }

    public Map<String, Object> getProfile() {
        return this.serviceProfileMap;
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

    public boolean addEntityInfo(String str, Object obj) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        this.serviceProfileMap.put(str, obj);
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
        this.serviceProfileMap.putAll(hashMap);
        return true;
    }

    public boolean setId(String str) {
        if (str == null || str.isEmpty() || str.contains("/")) {
            return false;
        }
        this.serviceId = str;
        addEntityInfo(ProfileConstants.SERVICE_ID, str);
        return true;
    }

    public String getId() {
        return this.serviceId;
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

    public boolean setType(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        this.serviceType = str;
        addEntityInfo("type", str);
        return true;
    }

    public String getType() {
        return this.serviceType;
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
        setType(readString2);
        String readString3 = parcel.readString();
        if ("".equals(readString3)) {
            readString3 = null;
        }
        setDeviceId(readString3);
        setIsNeedCloud(parcel.readInt() != 0);
        setIsNeedNearField(parcel.readInt() != 0);
        addEntities((Map) JSONObject.parseObject(parcel.readString(), new TypeReference<Map<String, Object>>() {
            /* class ohos.ai.profile.bean.ServiceProfile.AnonymousClass1 */
        }, new Feature[0]));
        return true;
    }

    public String toString() {
        return "serviceId is " + this.serviceId + " ,service type is " + this.serviceType + " ,isNeedCloud is " + this.isNeedCloud + " ,isNeedNearField is " + this.isNeedNearField + " ,serviceProfile is " + this.serviceProfileMap.toString();
    }
}
