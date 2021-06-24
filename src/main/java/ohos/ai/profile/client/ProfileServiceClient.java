package ohos.ai.profile.client;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import ohos.ai.profile.bean.DeviceProfile;
import ohos.ai.profile.bean.DeviceProfileEx;
import ohos.ai.profile.bean.GetServiceCharacteristicsRequest;
import ohos.ai.profile.bean.ServiceCharacteristicProfile;
import ohos.ai.profile.bean.ServiceProfile;
import ohos.ai.profile.harmonyadapter.IHarmonyProfileServiceCall;
import ohos.ai.profile.util.LogUtil;
import ohos.ai.profile.util.SensitiveUtil;
import ohos.ai.profile.util.TextUtil;
import ohos.annotation.SystemApi;
import ohos.rpc.RemoteException;

@SystemApi
public class ProfileServiceClient implements ProfileClient {
    private static final String CLOUD_LOCAL_DOMAIN = "cloud_local_domain";
    private static final int DATA_SOURCE_CLOUD = 1;
    private static final int DATA_SOURCE_DEVICE = 2;
    private static final int DATA_SOURCE_INVALID = -1;
    private static final int DATA_SOURCE_P2P = 4;
    private static final String DEFAULT_TRUST_DOMAIN = "default_trust_domain";
    private static final int DEVICE_LIST_TYPE_NUM = 4;
    private static final String P2P_TRUST_DOMAIN = "p2p_trust_domain";
    private static final String TAG = "ProfileServiceClient";
    private String callingPkgName;
    private ProfileServiceConnection connection;

    private boolean hasP2pDataSource(int i) {
        return (i & 4) != 0;
    }

    private boolean isValidListType(int i) {
        if (i <= 0) {
            return false;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < 4; i3++) {
            if (((1 << i3) & i) > 0) {
                i2++;
            }
        }
        return i2 == 1;
    }

    @Override // ohos.ai.profile.client.ProfileClient
    public boolean isAvailable() {
        return true;
    }

    public ProfileServiceClient(String str, ProfileServiceConnection profileServiceConnection) {
        this.callingPkgName = str;
        this.connection = profileServiceConnection;
    }

    @Override // ohos.ai.profile.client.ProfileClient
    public List<DeviceProfile> getDevices(boolean z, List<String> list) {
        markCallInterface("getDevices", this.callingPkgName);
        int dataSource = getDataSource(list);
        if (dataSource == -1) {
            LogUtil.error(TAG, "GetDevices failed for data source is invalid.", new Object[0]);
            return null;
        } else if (hasP2pSync(z, dataSource)) {
            LogUtil.error(TAG, "GetDevices don't support p2p synchronization.", new Object[0]);
            return null;
        } else {
            Optional<IHarmonyProfileServiceCall> profileService = this.connection.getProfileService();
            if (!profileService.isPresent()) {
                LogUtil.error(TAG, "GetDevices failed for profileService is null.", new Object[0]);
                return null;
            }
            try {
                List<DeviceProfile> harmonyGetDevices = profileService.get().harmonyGetDevices(this.callingPkgName, z, dataSource);
                LogUtil.info(TAG, "GetDevices size is " + size(harmonyGetDevices), new Object[0]);
                return harmonyGetDevices;
            } catch (RemoteException e) {
                LogUtil.error(TAG, "GetDevices failed for " + SensitiveUtil.getMessage(e), new Object[0]);
                return null;
            }
        }
    }

    @Override // ohos.ai.profile.client.ProfileClient
    public List<DeviceProfile> getDevicesById(String str, boolean z, List<String> list) {
        markCallInterface("getDevicesById", this.callingPkgName);
        if (TextUtil.isEmpty(str)) {
            LogUtil.error(TAG, "GetDevicesById failed for invalid parameter.", new Object[0]);
            return null;
        }
        int dataSource = getDataSource(list);
        if (dataSource == -1) {
            LogUtil.error(TAG, "GetDevicesById failed for data source is invalid.", new Object[0]);
            return null;
        } else if (hasP2pSync(z, dataSource)) {
            LogUtil.error(TAG, "GetDevicesById don't support p2p synchronization.", new Object[0]);
            return null;
        } else {
            Optional<IHarmonyProfileServiceCall> profileService = this.connection.getProfileService();
            if (!profileService.isPresent()) {
                LogUtil.error(TAG, "GetDevicesById failed for profileService is null.", new Object[0]);
                return null;
            }
            try {
                List<DeviceProfile> harmonyGetDevicesById = profileService.get().harmonyGetDevicesById(this.callingPkgName, str, z, dataSource);
                LogUtil.info(TAG, "GetDevicesById size is " + size(harmonyGetDevicesById), new Object[0]);
                return harmonyGetDevicesById;
            } catch (RemoteException e) {
                LogUtil.error(TAG, "GetDevicesById failed for " + SensitiveUtil.getMessage(e), new Object[0]);
                return null;
            }
        }
    }

    @Override // ohos.ai.profile.client.ProfileClient
    public List<DeviceProfile> getDevicesByType(String str, boolean z, List<String> list) {
        markCallInterface("getDevicesByType", this.callingPkgName);
        if (TextUtil.isEmpty(str)) {
            LogUtil.error(TAG, "GetDevicesByType failed for invalid parameter.", new Object[0]);
            return null;
        }
        int dataSource = getDataSource(list);
        if (dataSource == -1) {
            LogUtil.error(TAG, "GetDevicesByType failed for data source is invalid.", new Object[0]);
            return null;
        } else if (hasP2pSync(z, dataSource)) {
            LogUtil.error(TAG, "GetDevicesByType don't support p2p synchronization.", new Object[0]);
            return null;
        } else {
            Optional<IHarmonyProfileServiceCall> profileService = this.connection.getProfileService();
            if (!profileService.isPresent()) {
                LogUtil.error(TAG, "GetDevicesByType failed for profileService is null.", new Object[0]);
                return null;
            }
            try {
                List<DeviceProfile> harmonyGetDevicesByType = profileService.get().harmonyGetDevicesByType(this.callingPkgName, str, z, dataSource);
                LogUtil.info(TAG, "GetDevicesByType size is " + size(harmonyGetDevicesByType), new Object[0]);
                return harmonyGetDevicesByType;
            } catch (RemoteException e) {
                LogUtil.error(TAG, "GetDevicesByType failed for " + SensitiveUtil.getMessage(e), new Object[0]);
                return null;
            }
        }
    }

    @Override // ohos.ai.profile.client.ProfileClient
    public List<ServiceProfile> getServicesOfDevice(String str, boolean z, List<String> list, String str2) {
        markCallInterface("getServicesOfDevice with extra info", this.callingPkgName);
        if (TextUtil.isEmpty(str) || TextUtil.isEmpty(str2)) {
            LogUtil.error(TAG, "GetServicesOfDevice failed for invalid parameter.", new Object[0]);
            return null;
        }
        int dataSource = getDataSource(list);
        if (dataSource == -1) {
            LogUtil.error(TAG, "GetServicesOfDevice failed for data source is invalid.", new Object[0]);
            return null;
        } else if (hasP2pSync(z, dataSource)) {
            LogUtil.error(TAG, "GetServicesOfDevice don't support p2p synchronization.", new Object[0]);
            return null;
        } else {
            Optional<IHarmonyProfileServiceCall> profileService = this.connection.getProfileService();
            if (!profileService.isPresent()) {
                LogUtil.error(TAG, "GetServicesOfDevice failed for profileService is null.", new Object[0]);
                return null;
            }
            try {
                List<ServiceProfile> harmonyGetServicesOfDeviceWithExtraInfo = profileService.get().harmonyGetServicesOfDeviceWithExtraInfo(this.callingPkgName, str, z, dataSource, str2);
                LogUtil.info(TAG, "GetServicesOfDevice size is " + size(harmonyGetServicesOfDeviceWithExtraInfo), new Object[0]);
                return harmonyGetServicesOfDeviceWithExtraInfo;
            } catch (RemoteException e) {
                LogUtil.error(TAG, "GetServicesOfDevice failed for " + SensitiveUtil.getMessage(e), new Object[0]);
                return null;
            }
        }
    }

    @Override // ohos.ai.profile.client.ProfileClient
    public ServiceCharacteristicProfile getServiceCharacteristics(String str, String str2, boolean z, List<String> list, String str3) {
        markCallInterface("getServiceCharacteristics with extra info", this.callingPkgName);
        if (TextUtil.isEmpty(str) || TextUtil.isEmpty(str2)) {
            LogUtil.error(TAG, "GetServiceCharacteristics failed for invalid parameter.", new Object[0]);
            return null;
        }
        int dataSource = getDataSource(list);
        if (dataSource == -1) {
            LogUtil.error(TAG, "GetServiceCharacteristics failed for data source is invalid.", new Object[0]);
            return null;
        } else if (hasP2pSync(z, dataSource)) {
            LogUtil.error(TAG, "GetServiceCharacteristics don't support p2p synchronization.", new Object[0]);
            return null;
        } else {
            Optional<IHarmonyProfileServiceCall> profileService = this.connection.getProfileService();
            if (!profileService.isPresent()) {
                LogUtil.error(TAG, "GetServiceCharacteristics failed for profileService is null.", new Object[0]);
                return null;
            }
            try {
                GetServiceCharacteristicsRequest getServiceCharacteristicsRequest = new GetServiceCharacteristicsRequest();
                getServiceCharacteristicsRequest.setPkgName(this.callingPkgName);
                getServiceCharacteristicsRequest.setDevId(str);
                getServiceCharacteristicsRequest.setSid(str2);
                getServiceCharacteristicsRequest.setSync(z);
                getServiceCharacteristicsRequest.setDataSourceType(dataSource);
                getServiceCharacteristicsRequest.setExtraInfo(str3);
                ServiceCharacteristicProfile harmonyGetServiceCharacteristicsWithExtraInfo = profileService.get().harmonyGetServiceCharacteristicsWithExtraInfo(getServiceCharacteristicsRequest);
                int size = (harmonyGetServiceCharacteristicsWithExtraInfo == null || harmonyGetServiceCharacteristicsWithExtraInfo.getProfile() == null) ? 0 : harmonyGetServiceCharacteristicsWithExtraInfo.getProfile().size();
                LogUtil.info(TAG, "GetServiceCharacteristics size is " + size, new Object[0]);
                return harmonyGetServiceCharacteristicsWithExtraInfo;
            } catch (RemoteException e) {
                LogUtil.error(TAG, "GetServiceCharacteristics failed for " + SensitiveUtil.getMessage(e), new Object[0]);
                return null;
            }
        }
    }

    @Override // ohos.ai.profile.client.ProfileClient
    public DeviceProfileEx getHostDevice(List<String> list, List<String> list2) {
        markCallInterface("getHostDevice", this.callingPkgName);
        Optional<IHarmonyProfileServiceCall> profileService = this.connection.getProfileService();
        if (!profileService.isPresent()) {
            LogUtil.error(TAG, "GetHostDevice failed for profileService is null.", new Object[0]);
            return null;
        }
        try {
            DeviceProfileEx harmonyGetHostDevice = profileService.get().harmonyGetHostDevice(this.callingPkgName, list, list2);
            StringBuilder sb = new StringBuilder();
            sb.append("GetHostDevice result is ");
            sb.append(harmonyGetHostDevice != null);
            LogUtil.info(TAG, sb.toString(), new Object[0]);
            return harmonyGetHostDevice;
        } catch (RemoteException e) {
            LogUtil.error(TAG, "GetHostDevice failed for " + SensitiveUtil.getMessage(e), new Object[0]);
            return null;
        }
    }

    @Override // ohos.ai.profile.client.ProfileClient
    public List<DeviceProfileEx> getDevicesEx(boolean z, List<String> list, String str) {
        markCallInterface("getDevicesEx", this.callingPkgName);
        if (z) {
            LogUtil.info(TAG, "GetDevicesEx don't support terminal-to-terminal and terminal-to-cloud and p2p synchronization.", new Object[0]);
            return null;
        } else if (TextUtil.isEmpty(str)) {
            LogUtil.info(TAG, "GetDevicesEx failed for extra Info is null or empty.", new Object[0]);
            return null;
        } else {
            int dataSource = getDataSource(list);
            if (dataSource == -1) {
                LogUtil.error(TAG, "GetDevicesEx failed for data source is invalid.", new Object[0]);
                return null;
            }
            Optional<IHarmonyProfileServiceCall> profileService = this.connection.getProfileService();
            if (!profileService.isPresent()) {
                LogUtil.error(TAG, "GetDevicesEx failed for profileService is null.", new Object[0]);
                return null;
            }
            try {
                List<DeviceProfileEx> harmonyGetDevicesEx = profileService.get().harmonyGetDevicesEx(this.callingPkgName, false, dataSource, str);
                LogUtil.info(TAG, "GetDevicesEx size is " + size(harmonyGetDevicesEx), new Object[0]);
                return harmonyGetDevicesEx;
            } catch (RemoteException e) {
                LogUtil.error(TAG, "GetDevicesEx failed for " + SensitiveUtil.getMessage(e), new Object[0]);
                return null;
            }
        }
    }

    @Override // ohos.ai.profile.client.ProfileClient
    public List<DeviceProfileEx> getDevicesByTypeEx(List<String> list, boolean z, List<String> list2, String str) {
        markCallInterface("getDevicesByTypeEx", this.callingPkgName);
        if (list == null || list.isEmpty()) {
            LogUtil.error(TAG, "Can't get device without device type.", new Object[0]);
            return null;
        }
        int dataSource = getDataSource(list2);
        if (dataSource == -1) {
            LogUtil.error(TAG, "GetDevicesByTypeEx failed: error: data source is invalid.", new Object[0]);
            return null;
        } else if (z) {
            LogUtil.error(TAG, "Can't get getDevicesByTypeEx by isSync is true.", new Object[0]);
            return null;
        } else {
            Optional<IHarmonyProfileServiceCall> profileService = this.connection.getProfileService();
            if (!profileService.isPresent()) {
                LogUtil.error(TAG, "GetDevicesByTypeEx failed for profileService is null.", new Object[0]);
                return null;
            }
            try {
                List<DeviceProfileEx> harmonyGetDevicesByTypeEx = profileService.get().harmonyGetDevicesByTypeEx(this.callingPkgName, list, z, dataSource, str);
                LogUtil.info(TAG, "GetDevicesByTypeEx size is " + size(harmonyGetDevicesByTypeEx), new Object[0]);
                return harmonyGetDevicesByTypeEx;
            } catch (RemoteException e) {
                LogUtil.error(TAG, "GetDevicesByTypeEx failed for " + SensitiveUtil.getMessage(e), new Object[0]);
                return null;
            }
        }
    }

    @Override // ohos.ai.profile.client.ProfileClient
    public List<DeviceProfile> getDevicesByTypeLocal(String str, List<String> list) {
        markCallInterface("getDevicesByTypeLocal", this.callingPkgName);
        if (TextUtil.isEmpty(str)) {
            LogUtil.error(TAG, "DeviceType is null when get devices in local.", new Object[0]);
            return null;
        } else if (list == null || list.isEmpty()) {
            LogUtil.error(TAG, "Binding source list is empty when get devices in local.", new Object[0]);
            return null;
        } else {
            Optional<IHarmonyProfileServiceCall> profileService = this.connection.getProfileService();
            if (!profileService.isPresent()) {
                LogUtil.error(TAG, "GetDevicesByTypeLocal failed for profileService is null.", new Object[0]);
                return null;
            }
            try {
                List<DeviceProfile> harmonyGetDevicesByTypeLocal = profileService.get().harmonyGetDevicesByTypeLocal(this.callingPkgName, str, list);
                LogUtil.info(TAG, "Get devices by devType in local size is " + size(harmonyGetDevicesByTypeLocal), new Object[0]);
                return harmonyGetDevicesByTypeLocal;
            } catch (RemoteException e) {
                LogUtil.error(TAG, "GetDevicesByTypeLocal failed for " + SensitiveUtil.getMessage(e), new Object[0]);
                return null;
            }
        }
    }

    @Override // ohos.ai.profile.client.ProfileClient
    public List<DeviceProfile> getDevicesByTypeLocal(String str) {
        markCallInterface("getDevicesByTypeLocal", this.callingPkgName);
        if (TextUtil.isEmpty(str)) {
            LogUtil.error(TAG, "DeviceType is null when get devices in local.", new Object[0]);
            return null;
        }
        Optional<IHarmonyProfileServiceCall> profileService = this.connection.getProfileService();
        if (!profileService.isPresent()) {
            LogUtil.error(TAG, "GetDevicesByTypeLocal failed for profileService is null.", new Object[0]);
            return null;
        }
        try {
            List<DeviceProfile> harmonyGetDevicesByTypeLocal = profileService.get().harmonyGetDevicesByTypeLocal(this.callingPkgName, str, null);
            LogUtil.info(TAG, "Get devices by devType in local size is " + size(harmonyGetDevicesByTypeLocal), new Object[0]);
            return harmonyGetDevicesByTypeLocal;
        } catch (RemoteException e) {
            LogUtil.error(TAG, "GetDevicesByTypeLocal failed for " + SensitiveUtil.getMessage(e), new Object[0]);
            return null;
        }
    }

    @Override // ohos.ai.profile.client.ProfileClient
    public List<DeviceProfileEx> getDevicesByIdEx(List<String> list, boolean z, List<String> list2, String str) {
        markCallInterface("getDevicesByIdEx", this.callingPkgName);
        if (list == null || list.isEmpty()) {
            LogUtil.error(TAG, "Cant get device without device id.", new Object[0]);
            return null;
        }
        int dataSource = getDataSource(list2);
        if (dataSource == -1) {
            LogUtil.error(TAG, "GetDevicesByIdEx failed for data source is invalid.", new Object[0]);
            return null;
        }
        Optional<IHarmonyProfileServiceCall> profileService = this.connection.getProfileService();
        if (!profileService.isPresent()) {
            LogUtil.error(TAG, "GetDevicesByIdEx failed for profileService is null.", new Object[0]);
            return null;
        }
        try {
            List<DeviceProfileEx> harmonyGetDevicesByIdEx = profileService.get().harmonyGetDevicesByIdEx(this.callingPkgName, list, z, dataSource, str);
            LogUtil.info(TAG, "GetDevicesByIdEx size is " + size(harmonyGetDevicesByIdEx), new Object[0]);
            return harmonyGetDevicesByIdEx;
        } catch (RemoteException e) {
            LogUtil.error(TAG, "GetDevicesByIdEx failed for " + SensitiveUtil.getMessage(e), new Object[0]);
            return null;
        }
    }

    @Override // ohos.ai.profile.client.ProfileClient
    public List<DeviceProfile> queryDeviceList(int i, String str) {
        markCallInterface("queryDeviceList, type:" + i, this.callingPkgName);
        if (!isValidListType(i)) {
            LogUtil.error(TAG, "Please assign the correct type for querying.", new Object[0]);
            return null;
        }
        Optional<IHarmonyProfileServiceCall> profileService = this.connection.getProfileService();
        if (!profileService.isPresent()) {
            LogUtil.error(TAG, "QueryDeviceList failed for profileService is null.", new Object[0]);
            return null;
        }
        try {
            List<DeviceProfile> harmonyQueryDeviceList = profileService.get().harmonyQueryDeviceList(this.callingPkgName, i, str);
            LogUtil.info(TAG, "QueryDeviceList size is " + size(harmonyQueryDeviceList), new Object[0]);
            return harmonyQueryDeviceList;
        } catch (RemoteException e) {
            LogUtil.error(TAG, "Query device list failed for " + e.getMessage(), new Object[0]);
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0055  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0060  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getDataSource(java.util.List<java.lang.String> r8) {
        /*
        // Method dump skipped, instructions count: 125
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.ai.profile.client.ProfileServiceClient.getDataSource(java.util.List):int");
    }

    private boolean hasP2pSync(boolean z, int i) {
        return z && hasP2pDataSource(i);
    }

    private int size(Collection<?> collection) {
        if (collection == null) {
            return 0;
        }
        return collection.size();
    }

    private void markCallInterface(String str, String str2) {
        LogUtil.info(TAG, String.format(Locale.ENGLISH, "Start to call %s function, caller is %s", str, str2), new Object[0]);
    }
}
