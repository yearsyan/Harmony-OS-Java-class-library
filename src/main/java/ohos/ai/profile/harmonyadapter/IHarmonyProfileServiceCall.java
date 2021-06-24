package ohos.ai.profile.harmonyadapter;

import java.util.List;
import ohos.ai.profile.bean.DeviceProfile;
import ohos.ai.profile.bean.DeviceProfileEx;
import ohos.ai.profile.bean.GetServiceCharacteristicsRequest;
import ohos.ai.profile.bean.ServiceCharacteristicProfile;
import ohos.ai.profile.bean.ServiceProfile;
import ohos.annotation.SystemApi;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.RemoteException;

@SystemApi
public interface IHarmonyProfileServiceCall extends IRemoteBroker {
    List<DeviceProfile> harmonyGetDevices(String str, boolean z, int i) throws RemoteException;

    List<DeviceProfile> harmonyGetDevicesById(String str, String str2, boolean z, int i) throws RemoteException;

    List<DeviceProfileEx> harmonyGetDevicesByIdEx(String str, List<String> list, boolean z, int i, String str2) throws RemoteException;

    List<DeviceProfile> harmonyGetDevicesByType(String str, String str2, boolean z, int i) throws RemoteException;

    List<DeviceProfileEx> harmonyGetDevicesByTypeEx(String str, List<String> list, boolean z, int i, String str2) throws RemoteException;

    List<DeviceProfile> harmonyGetDevicesByTypeLocal(String str, String str2, List<String> list) throws RemoteException;

    List<DeviceProfileEx> harmonyGetDevicesEx(String str, boolean z, int i, String str2) throws RemoteException;

    DeviceProfileEx harmonyGetHostDevice(String str, List<String> list, List<String> list2) throws RemoteException;

    ServiceCharacteristicProfile harmonyGetServiceCharacteristicsWithExtraInfo(GetServiceCharacteristicsRequest getServiceCharacteristicsRequest) throws RemoteException;

    List<ServiceProfile> harmonyGetServicesOfDeviceWithExtraInfo(String str, String str2, boolean z, int i, String str3) throws RemoteException;

    List<DeviceProfile> harmonyQueryDeviceList(String str, int i, String str2) throws RemoteException;
}
