package ohos.ai.profile.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ohos.annotation.SystemApi;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;
import ohos.utils.fastjson.JSONObject;
import ohos.utils.fastjson.TypeReference;
import ohos.utils.fastjson.parser.Feature;

@SystemApi
public class DeviceProfile implements Sequenceable {
    private static final String BOUND_SOURCE_LIST = "__boundSources__";
    public static final Sequenceable.Producer<DeviceProfile> PRODUCER = $$Lambda$DeviceProfile$u7EwwuFTu_LOXHhP3i6a6l5IsbM.INSTANCE;
    private String deviceId = "";
    private Map<String, Object> deviceProfileMap = new HashMap();
    private String deviceType = "";
    private boolean isNeedCloud;
    private boolean isNeedNearField;

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        return false;
    }

    static /* synthetic */ DeviceProfile lambda$static$0(Parcel parcel) {
        DeviceProfile deviceProfile = new DeviceProfile();
        if (parcel.readSequenceable(deviceProfile)) {
            return deviceProfile;
        }
        return null;
    }

    public Map<String, Object> getProfile() {
        return this.deviceProfileMap;
    }

    public boolean addEntityInfo(String str, Object obj) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        this.deviceProfileMap.put(str, obj);
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
        this.deviceProfileMap.putAll(hashMap);
        return true;
    }

    public boolean setId(String str) {
        if (str == null || str.isEmpty() || str.contains("/")) {
            return false;
        }
        this.deviceId = str;
        addEntityInfo(ProfileConstants.DEVICE_ID, str);
        return true;
    }

    public String getId() {
        return this.deviceId;
    }

    public boolean setType(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        this.deviceType = str;
        addEntityInfo(ProfileConstants.DEV_TYPE, str);
        return true;
    }

    public String getType() {
        return this.deviceType;
    }

    public void setBoundSource(String str) {
        if (str != null && !str.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(str);
            this.deviceProfileMap.put(BOUND_SOURCE_LIST, arrayList);
        }
    }

    public List<String> getBoundSourceList() {
        Object obj = this.deviceProfileMap.get(BOUND_SOURCE_LIST);
        if (obj instanceof List) {
            return (List) obj;
        }
        return Collections.emptyList();
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
        setIsNeedCloud(parcel.readInt() != 0);
        setIsNeedNearField(parcel.readInt() != 0);
        addEntities((Map) JSONObject.parseObject(parcel.readString(), new TypeReference<Map<String, Object>>() {
            /* class ohos.ai.profile.bean.DeviceProfile.AnonymousClass1 */
        }, new Feature[0]));
        return true;
    }

    public String toString() {
        return "device type is " + this.deviceType + " ,isNeedCloud is " + this.isNeedCloud + " ,isNeedNearField is " + this.isNeedNearField;
    }
}
