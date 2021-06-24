package ohos.ai.profile.client;

import java.util.List;
import ohos.ai.profile.bean.DeviceProfile;
import ohos.ai.profile.bean.DeviceProfileEx;
import ohos.ai.profile.bean.ServiceCharacteristicProfile;
import ohos.ai.profile.bean.ServiceProfile;
import ohos.annotation.SystemApi;

@SystemApi
public interface ProfileClient {
    List<DeviceProfile> getDevices(boolean z, List<String> list);

    List<DeviceProfile> getDevicesById(String str, boolean z, List<String> list);

    List<DeviceProfileEx> getDevicesByIdEx(List<String> list, boolean z, List<String> list2, String str);

    List<DeviceProfile> getDevicesByType(String str, boolean z, List<String> list);

    List<DeviceProfileEx> getDevicesByTypeEx(List<String> list, boolean z, List<String> list2, String str);

    List<DeviceProfile> getDevicesByTypeLocal(String str);

    List<DeviceProfile> getDevicesByTypeLocal(String str, List<String> list);

    List<DeviceProfileEx> getDevicesEx(boolean z, List<String> list, String str);

    DeviceProfileEx getHostDevice(List<String> list, List<String> list2);

    ServiceCharacteristicProfile getServiceCharacteristics(String str, String str2, boolean z, List<String> list, String str3);

    List<ServiceProfile> getServicesOfDevice(String str, boolean z, List<String> list, String str2);

    boolean isAvailable();

    List<DeviceProfile> queryDeviceList(int i, String str);
}
