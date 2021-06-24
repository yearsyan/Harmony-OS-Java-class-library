package ohos.ai.profile.harmonyadapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import ohos.ai.profile.bean.DeviceProfile;
import ohos.ai.profile.bean.DeviceProfileEx;
import ohos.ai.profile.bean.GetServiceCharacteristicsRequest;
import ohos.ai.profile.bean.ServiceCharacteristicProfile;
import ohos.ai.profile.bean.ServiceProfile;
import ohos.annotation.SystemApi;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.utils.Sequenceable;

@SystemApi
public class HarmonyProfileServiceCallProxy implements IHarmonyProfileServiceCall {
    private static final int COMMAND_HARMONY_GET_DEVICES = 4;
    private static final int COMMAND_HARMONY_GET_DEVICES_BY_ID = 5;
    private static final int COMMAND_HARMONY_GET_DEVICES_BY_ID_EX = 17;
    private static final int COMMAND_HARMONY_GET_DEVICES_BY_TYPE = 6;
    private static final int COMMAND_HARMONY_GET_DEVICES_BY_TYPE_EX = 21;
    private static final int COMMAND_HARMONY_GET_DEVICES_BY_TYPE_LOCAL = 22;
    private static final int COMMAND_HARMONY_GET_DEVICES_EX = 20;
    private static final int COMMAND_HARMONY_GET_HOST_DEVICE = 19;
    private static final int COMMAND_HARMONY_GET_SERVICES_OF_DEVICE_WITH_EXTRA_INFO = 14;
    private static final int COMMAND_HARMONY_GET_SERVICE_CHARACTERISTICS_WITH_EXTRA_INFO = 15;
    private static final int COMMAND_HARMONY_QUERY_DEVICE_LIST = 18;
    private static final String DESCRIPTOR = "com.huawei.profile.harmonyadapter.IHarmonyProfileServiceCall";
    private static final int NO_DATA = -1;
    private final IRemoteObject remote;

    private HarmonyProfileServiceCallProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    @Override // ohos.ai.profile.harmonyadapter.IHarmonyProfileServiceCall
    public List<DeviceProfile> harmonyGetDevices(String str, boolean z, int i) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeString(str);
        obtain.writeInt(z ? 1 : 0);
        obtain.writeInt(i);
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            this.remote.sendRequest(4, obtain, obtain2, new MessageOption(0));
            obtain2.readException();
            return createTypedArrayList(obtain2, DeviceProfile.PRODUCER);
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.ai.profile.harmonyadapter.IHarmonyProfileServiceCall
    public List<DeviceProfile> harmonyGetDevicesById(String str, String str2, boolean z, int i) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeString(str);
        obtain.writeString(str2);
        obtain.writeInt(z ? 1 : 0);
        obtain.writeInt(i);
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            this.remote.sendRequest(5, obtain, obtain2, new MessageOption(0));
            obtain2.readException();
            return createTypedArrayList(obtain2, DeviceProfile.PRODUCER);
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.ai.profile.harmonyadapter.IHarmonyProfileServiceCall
    public List<DeviceProfile> harmonyGetDevicesByType(String str, String str2, boolean z, int i) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeString(str);
        obtain.writeString(str2);
        obtain.writeInt(z ? 1 : 0);
        obtain.writeInt(i);
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            this.remote.sendRequest(6, obtain, obtain2, new MessageOption(0));
            obtain2.readException();
            return createTypedArrayList(obtain2, DeviceProfile.PRODUCER);
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.ai.profile.harmonyadapter.IHarmonyProfileServiceCall
    public List<ServiceProfile> harmonyGetServicesOfDeviceWithExtraInfo(String str, String str2, boolean z, int i, String str3) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeString(str);
        obtain.writeString(str2);
        obtain.writeInt(z ? 1 : 0);
        obtain.writeInt(i);
        obtain.writeString(str3);
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            this.remote.sendRequest(14, obtain, obtain2, new MessageOption(0));
            obtain2.readException();
            return createTypedArrayList(obtain2, ServiceProfile.PRODUCER);
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.ai.profile.harmonyadapter.IHarmonyProfileServiceCall
    public ServiceCharacteristicProfile harmonyGetServiceCharacteristicsWithExtraInfo(GetServiceCharacteristicsRequest getServiceCharacteristicsRequest) throws RemoteException {
        ServiceCharacteristicProfile serviceCharacteristicProfile;
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeString(getServiceCharacteristicsRequest.getPkgName());
        obtain.writeString(getServiceCharacteristicsRequest.getDevId());
        obtain.writeString(getServiceCharacteristicsRequest.getSid());
        obtain.writeInt(getServiceCharacteristicsRequest.isSync() ? 1 : 0);
        obtain.writeInt(getServiceCharacteristicsRequest.getDataSourceType());
        obtain.writeString(getServiceCharacteristicsRequest.getExtraInfo());
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            this.remote.sendRequest(15, obtain, obtain2, new MessageOption(0));
            obtain2.readException();
            if (obtain2.readInt() == 1) {
                serviceCharacteristicProfile = ServiceCharacteristicProfile.PRODUCER.createFromParcel(obtain2);
            } else {
                serviceCharacteristicProfile = null;
            }
            return serviceCharacteristicProfile;
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.ai.profile.harmonyadapter.IHarmonyProfileServiceCall
    public List<DeviceProfileEx> harmonyGetDevicesByIdEx(String str, List<String> list, boolean z, int i, String str2) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeString(str);
        writeStringList(list, obtain);
        obtain.writeInt(z ? 1 : 0);
        obtain.writeInt(i);
        obtain.writeString(str2);
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            this.remote.sendRequest(17, obtain, obtain2, new MessageOption(0));
            obtain2.readException();
            return createTypedArrayList(obtain2, DeviceProfileEx.PRODUCER);
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.ai.profile.harmonyadapter.IHarmonyProfileServiceCall
    public List<DeviceProfile> harmonyQueryDeviceList(String str, int i, String str2) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeString(str);
        obtain.writeInt(i);
        obtain.writeString(str2);
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            this.remote.sendRequest(18, obtain, obtain2, new MessageOption(0));
            obtain2.readException();
            return createTypedArrayList(obtain2, DeviceProfile.PRODUCER);
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.ai.profile.harmonyadapter.IHarmonyProfileServiceCall
    public DeviceProfileEx harmonyGetHostDevice(String str, List<String> list, List<String> list2) throws RemoteException {
        DeviceProfileEx deviceProfileEx;
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeString(str);
        writeStringList(list, obtain);
        writeStringList(list2, obtain);
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            this.remote.sendRequest(19, obtain, obtain2, new MessageOption(0));
            obtain2.readException();
            if (obtain2.readInt() == 1) {
                deviceProfileEx = DeviceProfileEx.PRODUCER.createFromParcel(obtain2);
            } else {
                deviceProfileEx = null;
            }
            return deviceProfileEx;
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.ai.profile.harmonyadapter.IHarmonyProfileServiceCall
    public List<DeviceProfileEx> harmonyGetDevicesEx(String str, boolean z, int i, String str2) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeString(str);
        obtain.writeInt(z ? 1 : 0);
        obtain.writeInt(i);
        obtain.writeString(str2);
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            this.remote.sendRequest(20, obtain, obtain2, new MessageOption(0));
            obtain2.readException();
            return createTypedArrayList(obtain2, DeviceProfileEx.PRODUCER);
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.ai.profile.harmonyadapter.IHarmonyProfileServiceCall
    public List<DeviceProfileEx> harmonyGetDevicesByTypeEx(String str, List<String> list, boolean z, int i, String str2) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeString(str);
        writeStringList(list, obtain);
        obtain.writeInt(z ? 1 : 0);
        obtain.writeInt(i);
        obtain.writeString(str2);
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            this.remote.sendRequest(21, obtain, obtain2, new MessageOption(0));
            obtain2.readException();
            return createTypedArrayList(obtain2, DeviceProfileEx.PRODUCER);
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.ai.profile.harmonyadapter.IHarmonyProfileServiceCall
    public List<DeviceProfile> harmonyGetDevicesByTypeLocal(String str, String str2, List<String> list) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeInterfaceToken(DESCRIPTOR);
        obtain.writeString(str);
        obtain.writeString(str2);
        writeStringList(list, obtain);
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            this.remote.sendRequest(22, obtain, obtain2, new MessageOption(0));
            obtain2.readException();
            return createTypedArrayList(obtain2, DeviceProfile.PRODUCER);
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    public static Optional<IHarmonyProfileServiceCall> asInterface(IRemoteObject iRemoteObject) {
        if (iRemoteObject == null) {
            return Optional.empty();
        }
        Object obj = null;
        IRemoteBroker queryLocalInterface = iRemoteObject.queryLocalInterface(DESCRIPTOR);
        if (queryLocalInterface == null) {
            obj = new HarmonyProfileServiceCallProxy(iRemoteObject);
        } else if (queryLocalInterface instanceof IHarmonyProfileServiceCall) {
            obj = (IHarmonyProfileServiceCall) queryLocalInterface;
        }
        return Optional.ofNullable(obj);
    }

    private void writeStringList(List<String> list, MessageParcel messageParcel) {
        if (list == null) {
            messageParcel.writeInt(-1);
            return;
        }
        messageParcel.writeInt(list.size());
        list.forEach(new Consumer() {
            /* class ohos.ai.profile.harmonyadapter.$$Lambda$m1eStHMaLNBGlSbSzcUrIpMfCAI */

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                MessageParcel.this.writeString((String) obj);
            }
        });
    }

    private <T> ArrayList<T> createTypedArrayList(MessageParcel messageParcel, Sequenceable.Producer<T> producer) {
        int readInt = messageParcel.readInt();
        if (readInt == -1) {
            return null;
        }
        ArrayList<T> arrayList = new ArrayList<>(readInt);
        for (int i = 0; i < readInt; i++) {
            if (messageParcel.readInt() != 0) {
                arrayList.add(producer.createFromParcel(messageParcel));
            } else {
                arrayList.add(null);
            }
        }
        return arrayList;
    }
}
