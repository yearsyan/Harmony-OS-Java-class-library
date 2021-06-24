package ohos.wifi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ohos.annotation.SystemApi;
import ohos.app.Context;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.RemoteException;

public final class WifiDevice {
    private static final String DEFAULT_CALLER = "ohos";
    private static final Object DEVICE_LOCK = new Object();
    private static final int FEATURE_ID_ENHANCED_OPEN = 0;
    private static final int FEATURE_ID_WPA3_SAE = 1;
    private static final int FEATURE_ID_WPA3_SUITE_B = 2;
    private static final HiLogLabel LABEL = new HiLogLabel(3, InnerUtils.LOG_ID_WIFI, "WifiDevice");
    private static volatile WifiDevice sInstance = null;
    private final Context mContext;
    private final WifiHotspotProxy mHotspotProxy = WifiHotspotProxy.getInstance();
    private final WifiDeviceProxy mWifiProxy = WifiDeviceProxy.getInstance();

    private WifiDevice(Context context) {
        this.mContext = context;
    }

    public static WifiDevice getInstance(Context context) throws IllegalArgumentException {
        if (sInstance == null) {
            synchronized (DEVICE_LOCK) {
                if (sInstance == null) {
                    if (context != null) {
                        sInstance = new WifiDevice(context.getApplicationContext());
                    } else {
                        throw new IllegalArgumentException("NullContext");
                    }
                }
            }
        }
        return sInstance;
    }

    @SystemApi
    public static WifiDevice getInstance() {
        if (sInstance == null) {
            synchronized (DEVICE_LOCK) {
                if (sInstance == null) {
                    sInstance = new WifiDevice(null);
                }
            }
        }
        return sInstance;
    }

    @SystemApi
    public boolean enableWifi() {
        try {
            return this.mWifiProxy.setWifiPowerState(InnerUtils.getCaller(this.mContext), true);
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to enableWifi", new Object[0]);
            return false;
        }
    }

    @SystemApi
    public boolean disableWifi() {
        try {
            return this.mWifiProxy.setWifiPowerState(InnerUtils.getCaller(this.mContext), false);
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to disableWifi", new Object[0]);
            return false;
        }
    }

    private int getWifiPowerState() {
        try {
            return this.mWifiProxy.getWifiPowerState();
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to getWifiPowerState", new Object[0]);
            return 4;
        }
    }

    public boolean isWifiActive() {
        return getWifiPowerState() == 3;
    }

    public boolean scan() {
        try {
            return this.mWifiProxy.scan(InnerUtils.getCaller(this.mContext));
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to scan", new Object[0]);
            return false;
        }
    }

    public List<WifiScanInfo> getScanInfoList() {
        try {
            return this.mWifiProxy.getScanInfoList(InnerUtils.getCaller(this.mContext));
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to getScanInfoList", new Object[0]);
            return new ArrayList();
        }
    }

    @SystemApi
    public int addDeviceConfig(WifiDeviceConfig wifiDeviceConfig) {
        try {
            return this.mWifiProxy.addDeviceConfig(wifiDeviceConfig, InnerUtils.getCaller(this.mContext));
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to addDeviceConfig", new Object[0]);
            return -1;
        }
    }

    @SystemApi
    public boolean removeDevice(int i) {
        try {
            return this.mWifiProxy.removeDevice(i, InnerUtils.getCaller(this.mContext));
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to removeDevice", new Object[0]);
            return false;
        }
    }

    @SystemApi
    public boolean connectToDevice(WifiDeviceConfig wifiDeviceConfig) {
        int addDeviceConfig = addDeviceConfig(wifiDeviceConfig);
        if (addDeviceConfig == -1) {
            return false;
        }
        return connectToDevice(addDeviceConfig);
    }

    @SystemApi
    public boolean connectToDevice(int i) {
        try {
            return this.mWifiProxy.connectToDevice(i, InnerUtils.getCaller(this.mContext));
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to connectToDevice", new Object[0]);
            return false;
        }
    }

    @SystemApi
    public boolean disconnect() {
        try {
            return this.mWifiProxy.disconnect(InnerUtils.getCaller(this.mContext));
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to disconnect", new Object[0]);
            return false;
        }
    }

    public boolean isConnected() {
        try {
            return this.mWifiProxy.isConnected();
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to isConnected", new Object[0]);
            return false;
        }
    }

    public Optional<WifiLinkedInfo> getLinkedInfo() {
        try {
            return Optional.ofNullable(this.mWifiProxy.getLinkedInfo(InnerUtils.getCaller(this.mContext)));
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to getLinkedInfo", new Object[0]);
            return Optional.empty();
        }
    }

    public Optional<IpInfo> getIpInfo() {
        try {
            return Optional.ofNullable(this.mWifiProxy.getIpInfo());
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to getIpInfo", new Object[0]);
            return Optional.empty();
        }
    }

    @SystemApi
    public boolean enableHotspot() {
        try {
            return this.mHotspotProxy.enableHotspot(true);
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to enableHotspot", new Object[0]);
            return false;
        }
    }

    @SystemApi
    public boolean disableHotspot() {
        try {
            return this.mHotspotProxy.enableHotspot(false);
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to disableHotspot", new Object[0]);
            return false;
        }
    }

    @SystemApi
    public boolean setHotspotConfig(HotspotConfig hotspotConfig) {
        try {
            return this.mHotspotProxy.setHotspotConfig(hotspotConfig, InnerUtils.getCaller(this.mContext));
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to setHotspotConfig", new Object[0]);
            return false;
        }
    }

    @SystemApi
    public boolean isHotspotDualBandSupported() {
        try {
            return this.mHotspotProxy.isHotspotDualBandSupported();
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to isHotspotDualBandSupported", new Object[0]);
            return false;
        }
    }

    @SystemApi
    public Optional<HotspotConfig> getHotspotConfig() {
        try {
            return Optional.ofNullable(this.mHotspotProxy.getHotspotConfig());
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to getHotspotConfig", new Object[0]);
            return Optional.empty();
        }
    }

    @SystemApi
    public boolean isHotspotActive() {
        try {
            return this.mHotspotProxy.isHotspotActive();
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to isHotspotActive", new Object[0]);
            return false;
        }
    }

    @SystemApi
    public List<StationInfo> getStationList() {
        try {
            return this.mHotspotProxy.getStationList();
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to getStationList", new Object[0]);
            return new ArrayList();
        }
    }

    @SystemApi
    public List<WifiDeviceConfig> getDeviceConfigs() {
        try {
            return this.mWifiProxy.getDeviceConfigs(InnerUtils.getCaller(this.mContext));
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to getDeviceConfigs", new Object[0]);
            return new ArrayList();
        }
    }

    public int getSignalLevel(int i, int i2) {
        return InnerUtils.getSignalLevel(i, i2);
    }

    @SystemApi
    public String[] getDeviceMacAddress() {
        try {
            return this.mWifiProxy.getDeviceMacAddress();
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to getDeviceMacAddress", new Object[0]);
            return new String[0];
        }
    }

    @SystemApi
    public boolean reassociate() {
        try {
            return this.mWifiProxy.reassociate(InnerUtils.getCaller(this.mContext));
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to reassociate", new Object[0]);
            return false;
        }
    }

    @SystemApi
    public boolean reconnect() {
        try {
            return this.mWifiProxy.reconnect(InnerUtils.getCaller(this.mContext));
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to reconnect", new Object[0]);
            return false;
        }
    }

    @SystemApi
    public boolean disableNetwork(int i) {
        try {
            return this.mWifiProxy.disableNetwork(InnerUtils.getCaller(this.mContext), i);
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to disableNetwork", new Object[0]);
            return false;
        }
    }

    @SystemApi
    public void factoryReset() {
        try {
            this.mWifiProxy.factoryReset(InnerUtils.getCaller(this.mContext));
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to factoryReset", new Object[0]);
        }
    }

    public String getCountryCode() {
        try {
            return this.mWifiProxy.getCountryCode();
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to getCountryCode", new Object[0]);
            return "";
        }
    }

    @SystemApi
    public long getSupportedFeatures() {
        try {
            return this.mWifiProxy.getSupportedFeatures();
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to getCountryCode", new Object[0]);
            return -1;
        }
    }

    public boolean isFeatureSupported(long j) {
        return (getSupportedFeatures() & j) == j;
    }

    @SystemApi
    public int updateNetwork(WifiDeviceConfig wifiDeviceConfig) {
        int i;
        if (wifiDeviceConfig != null && wifiDeviceConfig.getNetId() >= 0) {
            return addDeviceConfig(wifiDeviceConfig);
        }
        HiLogLabel hiLogLabel = LABEL;
        Object[] objArr = new Object[1];
        if (wifiDeviceConfig == null) {
            i = -1;
        } else {
            i = wifiDeviceConfig.getNetId();
        }
        objArr[0] = Integer.valueOf(i);
        HiLog.warn(hiLogLabel, "UpdateNetwork has invalid config, net id is %{public}d", objArr);
        return -1;
    }

    @SystemApi
    public boolean removeAllNetwork() {
        List<WifiDeviceConfig> deviceConfigs = getDeviceConfigs();
        if (deviceConfigs == null) {
            HiLog.warn(LABEL, "No available device configs", new Object[0]);
            return false;
        }
        boolean z = true;
        for (WifiDeviceConfig wifiDeviceConfig : deviceConfigs) {
            if (!removeDevice(wifiDeviceConfig.getNetId())) {
                HiLog.warn(LABEL, "Failed to remove wifi device config, SSID is %{public}s", InnerUtils.safeDisplaySsid(wifiDeviceConfig.getSsid()));
                z = false;
            }
        }
        return z;
    }

    @SystemApi
    public void addStreamListener(StreamListener streamListener, EventRunner eventRunner) {
        HiLog.info(LABEL, "addStreamListener!", new Object[0]);
        if (streamListener == null) {
            HiLog.warn(LABEL, "listener is null!! Add Stream listener failed!", new Object[0]);
            return;
        }
        try {
            this.mWifiProxy.addStreamListener(streamListener, eventRunner, InnerUtils.getCaller(this.mContext));
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to addStreamListener", new Object[0]);
        }
    }

    @SystemApi
    public void removeStreamListener(StreamListener streamListener) {
        HiLog.info(LABEL, "removeStreamListener!", new Object[0]);
        if (streamListener == null) {
            HiLog.warn(LABEL, "listener is null!! Remove Stream listener failed!", new Object[0]);
            return;
        }
        try {
            this.mWifiProxy.removeStreamListener(streamListener);
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to removeStreamListener", new Object[0]);
        }
    }

    public boolean addUntrustedConfig(WifiDeviceConfig wifiDeviceConfig) {
        HiLog.info(LABEL, "addUntrustedConfig!", new Object[0]);
        String str = DEFAULT_CALLER;
        try {
            if (this.mContext != null) {
                str = this.mContext.getBundleName();
            }
            return this.mWifiProxy.addUntrustedConfig(wifiDeviceConfig, str);
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to addUntrustedConfig", new Object[0]);
            return false;
        }
    }

    public boolean removeUntrustedConfig(WifiDeviceConfig wifiDeviceConfig) {
        HiLog.info(LABEL, "removeUntrustedConfig!", new Object[0]);
        String str = DEFAULT_CALLER;
        try {
            if (this.mContext != null) {
                str = this.mContext.getBundleName();
            }
            return this.mWifiProxy.removeUntrustedConfig(wifiDeviceConfig, str);
        } catch (RemoteException unused) {
            HiLog.warn(LABEL, "Exception to removeUntrustedConfig", new Object[0]);
            return false;
        }
    }
}
