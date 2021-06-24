package ohos.softnet.connect;

import com.huawei.softnet.connect.ConnectionCallback;
import com.huawei.softnet.connect.DataCallback;
import com.huawei.softnet.connect.DataPayload;
import com.huawei.softnet.connect.DeviceDesc;
import com.huawei.softnet.connect.DiscoveryCallback;
import com.huawei.softnet.connect.NetRole;
import com.huawei.softnet.connect.PowerPolicy;
import com.huawei.softnet.connect.ServiceDesc;
import com.huawei.softnet.connect.ServiceFilter;
import com.huawei.softnet.connect.Strategy;
import java.util.ArrayList;
import java.util.List;
import ohos.softnet.connect.DataPayload;
import ohos.softnet.connect.DeviceDesc;

public class ConnectDataUtils {
    private static final String TAG = "ConnectDataUtils";

    public static Strategy getStrategy(Strategy strategy) {
        if (strategy == null) {
            LogUtils.error(TAG, "getStrategy", "Obligatory Strategy is null", new Object[0]);
            return null;
        }
        int i = AnonymousClass4.$SwitchMap$ohos$softnet$connect$Strategy[strategy.ordinal()];
        if (i == 1) {
            return Strategy.BLE;
        }
        if (i == 2) {
            return Strategy.P2P;
        }
        if (i == 3) {
            return Strategy.USB;
        }
        if (i == 4) {
            return Strategy.COAP;
        }
        if (i == 5) {
            return Strategy.WIFI;
        }
        LogUtils.error(TAG, "getStrategy", "unknown Strategy", new Object[0]);
        return null;
    }

    public static List<ServiceDesc> getServiceDesc(List<ServiceDesc> list) {
        ArrayList arrayList = new ArrayList();
        if (list == null) {
            LogUtils.info(TAG, "getServiceDesc", "Optional ServiceDesc is null", new Object[0]);
            return arrayList;
        }
        for (ServiceDesc serviceDesc : list) {
            arrayList.add(new ServiceDesc.Builder().serviceId(serviceDesc.getServiceId()).serviceName(serviceDesc.getServiceName()).serviceData(serviceDesc.getServiceData()).build());
        }
        return arrayList;
    }

    public static List<ServiceFilter> getServiceFilter(List<ServiceFilter> list) {
        ArrayList arrayList = new ArrayList();
        if (list == null) {
            LogUtils.error(TAG, "getServiceFilter", "Obligatory ServiceFilter is null", new Object[0]);
            return arrayList;
        }
        for (ServiceFilter serviceFilter : list) {
            arrayList.add(new ServiceFilter.Builder().serviceId(serviceFilter.getServiceId()).filterData(serviceFilter.getFilterData()).filterMask(serviceFilter.getFilterMask()).build());
        }
        return arrayList;
    }

    public static PowerPolicy getPowerPolicy(PowerPolicy powerPolicy) {
        if (powerPolicy == null) {
            LogUtils.info(TAG, "getPowerPolicy", "Optional PowerPolicy is null", new Object[0]);
            return null;
        }
        int i = AnonymousClass4.$SwitchMap$ohos$softnet$connect$PowerPolicy[powerPolicy.ordinal()];
        if (i == 1) {
            return PowerPolicy.High;
        }
        if (i == 2) {
            return PowerPolicy.Middle;
        }
        if (i == 3) {
            return PowerPolicy.Low;
        }
        if (i == 4) {
            return PowerPolicy.Very_Low;
        }
        LogUtils.error(TAG, "getPowerPolicy", "unknown PowerPolicy", new Object[0]);
        return null;
    }

    public static DeviceDesc getServiceDesc(DeviceDesc deviceDesc) {
        if (deviceDesc != null) {
            return new DeviceDesc.Builder().deviceName(deviceDesc.getDeviceName()).deviceId(deviceDesc.getDeviceId()).deviceType(deviceDesc.getDeviceType()).wifiMac(deviceDesc.getWifiMac()).btMac(deviceDesc.getBtMac()).ipv4(deviceDesc.getIpv4()).ipv6(deviceDesc.getIpv6()).port(deviceDesc.getPort()).capabilityBitmap(deviceDesc.getCapabilityBitmap()).reservedInfo(deviceDesc.getReservedInfo()).build();
        }
        LogUtils.error(TAG, "getServiceDesc", "DeviceDesc is null", new Object[0]);
        return null;
    }

    public static DiscoveryCallback getDiscoveryCallback(final DiscoveryCallback discoveryCallback) {
        return new DiscoveryCallback() {
            /* class ohos.softnet.connect.ConnectDataUtils.AnonymousClass1 */
            DiscoveryCallback cb = DiscoveryCallback.this;

            public void onDeviceFound(com.huawei.softnet.connect.DeviceDesc deviceDesc) {
                this.cb.onDeviceFound(ConnectDataUtils.getServiceDesc(deviceDesc));
            }

            public void onDeviceLost(com.huawei.softnet.connect.DeviceDesc deviceDesc) {
                this.cb.onDeviceLost(ConnectDataUtils.getServiceDesc(deviceDesc));
            }
        };
    }

    public static ConnectionCallback getConnectionCallback(final ConnectionCallback connectionCallback) {
        return new ConnectionCallback() {
            /* class ohos.softnet.connect.ConnectDataUtils.AnonymousClass2 */
            ConnectionCallback cb = ConnectionCallback.this;

            public void onConnectionInit(String str, String str2, String str3) {
                this.cb.onConnectionInit(str, str2, str3);
            }

            public void onConnectionStateUpdate(String str, String str2, int i, String str3) {
                this.cb.onConnectionStateUpdate(str, str2, i, str3);
            }
        };
    }

    public static NetRole getNetRole(NetRole netRole) {
        if (netRole == null) {
            LogUtils.error(TAG, "getNetRole", "netRole is null", new Object[0]);
            return null;
        }
        int i = AnonymousClass4.$SwitchMap$ohos$softnet$connect$NetRole[netRole.ordinal()];
        if (i == 1) {
            return NetRole.P2P_GO;
        }
        if (i == 2) {
            return NetRole.P2P_GC;
        }
        if (i == 3) {
            return NetRole.HOTSPOT_AP;
        }
        if (i == 4) {
            return NetRole.HOTSPOT_STA;
        }
        LogUtils.error(TAG, "getNetRole", "unknown NetRole", new Object[0]);
        return null;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.softnet.connect.ConnectDataUtils$4  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$ohos$softnet$connect$NetRole = new int[NetRole.values().length];
        static final /* synthetic */ int[] $SwitchMap$ohos$softnet$connect$PowerPolicy = new int[PowerPolicy.values().length];
        static final /* synthetic */ int[] $SwitchMap$ohos$softnet$connect$Strategy = new int[Strategy.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|17|18|19|20|21|22|(2:23|24)|25|27|28|29|30|31|32|33|34|(3:35|36|38)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(29:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|27|28|29|30|31|32|33|34|(3:35|36|38)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(32:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|27|28|29|30|31|32|33|34|35|36|38) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0048 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0052 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x005c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0079 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0083 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x008d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x0097 */
        static {
            /*
            // Method dump skipped, instructions count: 163
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.softnet.connect.ConnectDataUtils.AnonymousClass4.<clinit>():void");
        }
    }

    public static DataPayload getDataPayload(DataPayload dataPayload) {
        if (dataPayload.getInStream() != null) {
            return new DataPayload.Builder().id(dataPayload.getId()).type(dataPayload.getType()).bytes(dataPayload.getInBytes()).file(null).stream(null).build();
        }
        LogUtils.error(TAG, "getDataPayload", "DataPayload is invalid, Stream is null", new Object[0]);
        return null;
    }

    public static com.huawei.softnet.connect.DataPayload getDataPayload(DataPayload dataPayload) {
        if (dataPayload.getInStream() != null) {
            return new DataPayload.Builder().id(dataPayload.getId()).type(dataPayload.getType()).bytes(dataPayload.getInBytes()).file((DataPayload.File) null).stream((DataPayload.Stream) null).build();
        }
        LogUtils.error(TAG, "getDataPayload", "DataPayload is invalid, Stream is null", new Object[0]);
        return null;
    }

    public static DataCallback getDataCallback(final DataCallback dataCallback) {
        return new DataCallback() {
            /* class ohos.softnet.connect.ConnectDataUtils.AnonymousClass3 */
            DataCallback cb = DataCallback.this;

            public int onByteReceive(String str, String str2, byte[] bArr, int i, String str3) {
                return this.cb.onByteReceive(str, str2, bArr, i, str3);
            }

            public int onBlockReceive(String str, String str2, byte[] bArr, int i, String str3) {
                return this.cb.onBlockReceive(str, str2, bArr, i, str3);
            }

            public int onFileReceive(String str, String str2, String str3, String str4) {
                return this.cb.onFileReceive(str, str2, str3, str4);
            }

            public int onStreamReceive(String str, String str2, com.huawei.softnet.connect.DataPayload dataPayload, String str3) {
                if (dataPayload != null) {
                    return this.cb.onStreamReceive(str, str2, ConnectDataUtils.getDataPayload(dataPayload), str3);
                }
                LogUtils.error(ConnectDataUtils.TAG, "onStreamReceive", "DataPayload is null", new Object[0]);
                return -1;
            }

            public int onSendFileStateUpdate(String str, String str2, int i, String str3) {
                return this.cb.onSendFileStateUpdate(str, str2, i, str3);
            }

            public String onCommonUpdate(String str) {
                return this.cb.onCommonUpdate(str);
            }
        };
    }
}
