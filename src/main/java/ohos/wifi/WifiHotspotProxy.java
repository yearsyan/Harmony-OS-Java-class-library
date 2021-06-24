package ohos.wifi;

import java.util.ArrayList;
import java.util.List;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.utils.system.safwk.java.SystemAbilityDefinition;

/* access modifiers changed from: package-private */
public class WifiHotspotProxy extends WifiCommProxy implements IWifiHotspot {
    private static final int COMMAND_ENABLE_HOTSPOT = 1;
    private static final int COMMAND_GET_HOTSPOT_CONFIG = 4;
    private static final int COMMAND_GET_STATION_LIST = 6;
    private static final int COMMAND_IS_HOTSPOT_ACTIVE = 5;
    private static final int COMMAND_IS_HOTSPOT_DUAL_BAND_SUPPORTED = 3;
    private static final int COMMAND_SET_HOTSPOT_CONFIG = 2;
    private static final HiLogLabel LABEL = new HiLogLabel(3, InnerUtils.LOG_ID_HOTSPOT, "WifiHotspotProxy");
    private static final int MIN_TRANSACTION_ID = 1;
    private static final Object PROXY_LOCK = new Object();
    private static volatile WifiHotspotProxy sInstance = null;

    private WifiHotspotProxy(int i) {
        super(i);
    }

    public static WifiHotspotProxy getInstance() {
        if (sInstance == null) {
            synchronized (PROXY_LOCK) {
                if (sInstance == null) {
                    sInstance = new WifiHotspotProxy(SystemAbilityDefinition.WIFI_HOTSPOT_SYS_ABILITY_ID);
                }
            }
        }
        return sInstance;
    }

    @Override // ohos.wifi.IWifiHotspot
    public boolean enableHotspot(boolean z) throws RemoteException {
        MessageParcel create = MessageParcel.create();
        create.writeString(InnerUtils.HOTSPOT_INTERFACE_TOKEN);
        create.writeInt(z ? 1 : 0);
        boolean z2 = true;
        MessageParcel request = request(1, create);
        if (request.readInt() != 1) {
            z2 = false;
        }
        request.reclaim();
        return z2;
    }

    @Override // ohos.wifi.IWifiHotspot
    public boolean setHotspotConfig(HotspotConfig hotspotConfig, String str) throws RemoteException {
        MessageParcel create = MessageParcel.create();
        create.writeString(InnerUtils.HOTSPOT_INTERFACE_TOKEN);
        create.writeSequenceable(hotspotConfig);
        create.writeString(str);
        MessageParcel request = request(2, create);
        boolean z = true;
        if (request.readInt() != 1) {
            z = false;
        }
        request.reclaim();
        return z;
    }

    @Override // ohos.wifi.IWifiHotspot
    public boolean isHotspotDualBandSupported() throws RemoteException {
        MessageParcel create = MessageParcel.create();
        create.writeString(InnerUtils.HOTSPOT_INTERFACE_TOKEN);
        MessageParcel request = request(3, create);
        boolean z = true;
        if (request.readInt() != 1) {
            z = false;
        }
        request.reclaim();
        return z;
    }

    @Override // ohos.wifi.IWifiHotspot
    public HotspotConfig getHotspotConfig() throws RemoteException {
        MessageParcel create = MessageParcel.create();
        create.writeString(InnerUtils.HOTSPOT_INTERFACE_TOKEN);
        MessageParcel request = request(4, create);
        HotspotConfig hotspotConfig = new HotspotConfig();
        request.readSequenceable(hotspotConfig);
        request.reclaim();
        return hotspotConfig;
    }

    @Override // ohos.wifi.IWifiHotspot
    public boolean isHotspotActive() throws RemoteException {
        MessageParcel create = MessageParcel.create();
        create.writeString(InnerUtils.HOTSPOT_INTERFACE_TOKEN);
        MessageParcel request = request(5, create);
        boolean z = true;
        if (request.readInt() != 1) {
            z = false;
        }
        request.reclaim();
        return z;
    }

    @Override // ohos.wifi.IWifiHotspot
    public List<StationInfo> getStationList() throws RemoteException {
        MessageParcel create = MessageParcel.create();
        create.writeString(InnerUtils.HOTSPOT_INTERFACE_TOKEN);
        MessageParcel request = request(6, create);
        ArrayList arrayList = new ArrayList();
        int readInt = request.readInt();
        for (int i = 0; i < readInt; i++) {
            if (request.getReadableBytes() <= 0) {
                HiLog.warn(LABEL, "read parcel failed in proxy, Hotspot station list Size = %{public}d", Integer.valueOf(readInt));
                request.reclaim();
                return new ArrayList();
            }
            StationInfo stationInfo = new StationInfo();
            request.readSequenceable(stationInfo);
            arrayList.add(stationInfo);
        }
        request.reclaim();
        return arrayList;
    }
}
