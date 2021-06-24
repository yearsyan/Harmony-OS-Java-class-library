package ohos.wifi.utils;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import ohos.aafwk.content.IntentParams;
import ohos.aafwk.content.Skills;
import ohos.event.commonevent.CommonEventBaseConverter;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.wifi.WifiDevice;
import ohos.wifi.WifiEvents;

public class WifiEventsConverter extends CommonEventBaseConverter {
    private static final String EVENT_MPLINK_STATE = "com.android.server.hidata.arbitration.HwArbitrationStateMachine";
    private static final String EVENT_WIFI_AP_STA_JOIN = "com.huawei.net.wifi.WIFI_AP_STA_JOIN";
    private static final String EVENT_WIFI_AP_STA_LEAVE = "com.huawei.net.wifi.WIFI_AP_STA_LEAVE";
    private static final String EXTRA_CURRENT_TIME = "currentTime";
    private static final String EXTRA_MPLINK_STATE = "MPLinkSuccessNetworkKey";
    private static final String EXTRA_STA_COUNT = "staCount";
    private static final String EXTRA_STA_INFO = "macInfo";
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218109280, "CommonEventBaseConverter");
    private static final int LOG_ID_WIFI = 218109280;
    private static final Map<String, ZosAction> MAP_ACTION_CONVERTER;
    private static final Map<String, String> MAP_EXTRAS_BIND_MPLINK;
    private static final Map<String, String> MAP_EXTRAS_CONNECTION;
    private static final Map<String, String> MAP_EXTRAS_HOTSPOT_STATE;
    private static final Map<String, String> MAP_EXTRAS_P2P_CONNECTION;
    private static final Map<String, String> MAP_EXTRAS_P2P_CURRENT_DEVICE;
    private static final Map<String, String> MAP_EXTRAS_P2P_DISCOVERY;
    private static final Map<String, String> MAP_EXTRAS_P2P_PEERS;
    private static final Map<String, String> MAP_EXTRAS_P2P_STATE;
    private static final Map<String, String> MAP_EXTRAS_RSSI;
    private static final Map<String, String> MAP_EXTRAS_SCAN;
    private static final Map<String, String> MAP_EXTRAS_STATION;
    private static final Map<String, String> MAP_EXTRAS_WIFI_STATE;

    private int convertWifiApState(int i) {
        switch (i) {
            case 10:
                return 3;
            case 11:
                return 0;
            case 12:
                return 2;
            case 13:
                return 1;
            default:
                return 0;
        }
    }

    private int convertWifiState(int i) {
        if (i == 0) {
            return 3;
        }
        if (i == 1) {
            return 0;
        }
        if (i != 2) {
            return i != 3 ? 0 : 1;
        }
        return 2;
    }

    @Override // ohos.event.commonevent.CommonEventBaseConverter
    public void convertSkillsToAospIntentFilter(Skills skills, IntentFilter intentFilter) {
    }

    static {
        HashMap hashMap = new HashMap();
        hashMap.put("wifi_state", WifiEvents.PARAM_ACTIVE_STATE);
        MAP_EXTRAS_WIFI_STATE = Collections.unmodifiableMap(hashMap);
        HashMap hashMap2 = new HashMap();
        hashMap2.put(EXTRA_MPLINK_STATE, WifiEvents.PARAM_MPLINK_STATE);
        MAP_EXTRAS_BIND_MPLINK = Collections.unmodifiableMap(hashMap2);
        HashMap hashMap3 = new HashMap();
        hashMap3.put("resultsUpdated", WifiEvents.PARAM_SCAN_STATE);
        MAP_EXTRAS_SCAN = Collections.unmodifiableMap(hashMap3);
        HashMap hashMap4 = new HashMap();
        hashMap4.put("newRssi", WifiEvents.PARAM_RSSI_VALUE);
        MAP_EXTRAS_RSSI = Collections.unmodifiableMap(hashMap4);
        HashMap hashMap5 = new HashMap();
        hashMap5.put("networkInfo", WifiEvents.PARAM_CONN_STATE);
        MAP_EXTRAS_CONNECTION = Collections.unmodifiableMap(hashMap5);
        HashMap hashMap6 = new HashMap();
        hashMap6.put("wifi_state", WifiEvents.PARAM_HOTSPOT_STATE);
        MAP_EXTRAS_HOTSPOT_STATE = Collections.unmodifiableMap(hashMap6);
        HashMap hashMap7 = new HashMap();
        hashMap7.put("macInfo", "macInfo");
        hashMap7.put("currentTime", "currentTime");
        hashMap7.put("staCount", "staCount");
        MAP_EXTRAS_STATION = Collections.unmodifiableMap(hashMap7);
        HashMap hashMap8 = new HashMap();
        hashMap8.put("wifi_p2p_state", WifiEvents.PARAM_P2P_STATE_CHANGED);
        MAP_EXTRAS_P2P_STATE = Collections.unmodifiableMap(hashMap8);
        HashMap hashMap9 = new HashMap();
        hashMap9.put("wifiP2pInfo", WifiEvents.PARAM_P2P_CONN_LINKED_INFO);
        hashMap9.put("networkInfo", WifiEvents.PARAM_P2P_CONN_NETWORK_INFO);
        hashMap9.put("p2pGroupInfo", WifiEvents.PARAM_P2P_CONN_GROUP_INFO);
        MAP_EXTRAS_P2P_CONNECTION = Collections.unmodifiableMap(hashMap9);
        HashMap hashMap10 = new HashMap();
        hashMap10.put("wifiP2pDeviceList", WifiEvents.PARAM_P2P_DEVICE_LIST);
        MAP_EXTRAS_P2P_PEERS = Collections.unmodifiableMap(hashMap10);
        HashMap hashMap11 = new HashMap();
        hashMap11.put("discoveryState", WifiEvents.PARAM_P2P_DISCOVERY);
        MAP_EXTRAS_P2P_DISCOVERY = Collections.unmodifiableMap(hashMap11);
        HashMap hashMap12 = new HashMap();
        hashMap12.put("wifiP2pDevice", WifiEvents.PARAM_P2P_CURRENT_DEVICE);
        MAP_EXTRAS_P2P_CURRENT_DEVICE = Collections.unmodifiableMap(hashMap12);
        HashMap hashMap13 = new HashMap();
        hashMap13.put("android.net.wifi.WIFI_STATE_CHANGED", new ZosAction("usual.event.wifi.POWER_STATE", MAP_EXTRAS_WIFI_STATE));
        hashMap13.put("com.android.server.hidata.arbitration.HwArbitrationStateMachine", new ZosAction("usual.event.wifi.mplink.STATE_CHANGE", MAP_EXTRAS_BIND_MPLINK));
        hashMap13.put("android.net.wifi.SCAN_RESULTS", new ZosAction("usual.event.wifi.SCAN_FINISHED", MAP_EXTRAS_SCAN));
        hashMap13.put("android.net.wifi.RSSI_CHANGED", new ZosAction("usual.event.wifi.RSSI_VALUE", MAP_EXTRAS_RSSI));
        hashMap13.put("android.net.wifi.STATE_CHANGE", new ZosAction("usual.event.wifi.CONN_STATE", MAP_EXTRAS_CONNECTION));
        hashMap13.put("android.net.wifi.WIFI_AP_STATE_CHANGED", new ZosAction("usual.event.wifi.HOTSPOT_STATE", MAP_EXTRAS_HOTSPOT_STATE));
        hashMap13.put("com.huawei.net.wifi.WIFI_AP_STA_JOIN", new ZosAction("usual.event.wifi.WIFI_HS_STA_JOIN", MAP_EXTRAS_STATION));
        hashMap13.put("com.huawei.net.wifi.WIFI_AP_STA_LEAVE", new ZosAction("usual.event.wifi.WIFI_HS_STA_LEAVE", MAP_EXTRAS_STATION));
        hashMap13.put("android.net.wifi.p2p.STATE_CHANGED", new ZosAction("usual.event.wifi.p2p.STATE_CHANGE", MAP_EXTRAS_P2P_STATE));
        hashMap13.put("android.net.wifi.p2p.CONNECTION_STATE_CHANGE", new ZosAction("usual.event.wifi.p2p.CONN_STATE_CHANGE", MAP_EXTRAS_P2P_CONNECTION));
        hashMap13.put("android.net.wifi.p2p.PEERS_CHANGED", new ZosAction("usual.event.wifi.p2p.DEVICES_CHANGE", MAP_EXTRAS_P2P_PEERS));
        hashMap13.put("android.net.wifi.p2p.DISCOVERY_STATE_CHANGE", new ZosAction("usual.event.wifi.p2p.PEER_DISCOVERY_STATE_CHANGE", MAP_EXTRAS_P2P_DISCOVERY));
        hashMap13.put("android.net.wifi.p2p.THIS_DEVICE_CHANGED", new ZosAction("usual.event.wifi.p2p.CURRENT_DEVICE_CHANGE", MAP_EXTRAS_P2P_CURRENT_DEVICE));
        hashMap13.put("android.net.wifi.p2p.PERSISTENT_GROUPS_CHANGED", new ZosAction("usual.event.wifi.p2p.GROUP_STATE_CHANGED", new HashMap()));
        MAP_ACTION_CONVERTER = Collections.unmodifiableMap(hashMap13);
    }

    private static final class ZosAction {
        public String action;
        public Map<String, String> extrasConverter;

        public ZosAction(String str, Map<String, String> map) {
            this.action = str;
            this.extrasConverter = map;
        }
    }

    private Object convertExtraValue(String str, String str2, Object obj) {
        if ("com.android.server.hidata.arbitration.HwArbitrationStateMachine".equals(str)) {
            if (EXTRA_MPLINK_STATE.equals(str2)) {
                if (((Integer) obj).intValue() == 801) {
                    return 1;
                }
                return 0;
            }
        } else if ("android.net.wifi.STATE_CHANGE".equals(str)) {
            if ("networkInfo".equals(str2)) {
                WifiDevice instance = WifiDevice.getInstance();
                if (instance == null) {
                    return 0;
                }
                return Integer.valueOf(instance.isConnected() ? 1 : 0);
            }
        } else if ("android.net.wifi.WIFI_STATE_CHANGED".equals(str)) {
            if ("wifi_state".equals(str2) && obj != null) {
                return Integer.valueOf(convertWifiState(((Integer) obj).intValue()));
            }
        } else if ("android.net.wifi.SCAN_RESULTS".equals(str)) {
            if ("resultsUpdated".equals(str2) && obj != null) {
                return Integer.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
            }
        } else if (!"android.net.wifi.WIFI_AP_STATE_CHANGED".equals(str)) {
            HiLog.info(LABEL, "convertP2pExtraValue aAction is %{public}s need to convert", str);
            return convertP2pExtraValue(str, str2, obj);
        } else if ("wifi_state".equals(str2) && obj != null) {
            return Integer.valueOf(convertWifiApState(((Integer) obj).intValue()));
        }
        return obj;
    }

    private Object convertP2pExtraValue(String str, String str2, Object obj) {
        int i = 2;
        if ("android.net.wifi.p2p.STATE_CHANGED".equals(str)) {
            if ("wifi_p2p_state".equals(str2) && obj != null) {
                if (((Integer) obj).intValue() != 2) {
                    i = 1;
                }
                return Integer.valueOf(i);
            }
        } else if ("android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(str)) {
            if ("wifiP2pInfo".equals(str2) || "networkInfo".equals(str2) || "p2pGroupInfo".equals(str2)) {
                return 1;
            }
        } else if ("android.net.wifi.p2p.PEERS_CHANGED".equals(str)) {
            if ("wifiP2pDeviceList".equals(str2)) {
                return 1;
            }
        } else if ("android.net.wifi.p2p.DISCOVERY_STATE_CHANGE".equals(str)) {
            if ("discoveryState".equals(str2) && obj != null) {
                if (((Integer) obj).intValue() != 2) {
                    i = 1;
                }
                return Integer.valueOf(i);
            }
        } else if (!"android.net.wifi.p2p.THIS_DEVICE_CHANGED".equals(str)) {
            HiLog.info(LABEL, "convertAospIntentToIntent aAction is %{public}s no need to convert", str);
        } else if ("wifiP2pDevice".equals(str2)) {
            return 1;
        }
        return obj;
    }

    @Override // ohos.event.commonevent.CommonEventBaseConverter
    public Optional<Intent> convertIntentToAospIntent(ohos.aafwk.content.Intent intent) {
        return Optional.ofNullable(new Intent());
    }

    @Override // ohos.event.commonevent.CommonEventBaseConverter
    public Optional<ohos.aafwk.content.Intent> convertAospIntentToIntent(Intent intent) {
        if (intent == null) {
            HiLog.warn(LABEL, "convertAospIntentToIntent aIntent is null!", new Object[0]);
            return Optional.ofNullable(new ohos.aafwk.content.Intent());
        }
        String action = intent.getAction();
        HiLog.info(LABEL, "convertAospIntentToIntent aAction is %{public}s", action);
        ZosAction zosAction = MAP_ACTION_CONVERTER.get(action);
        if (zosAction == null) {
            HiLog.warn(LABEL, "zAction is null", new Object[0]);
            return Optional.empty();
        }
        HiLog.warn(LABEL, "convertAospIntentToIntent zAction is %{public}s", zosAction.action);
        ohos.aafwk.content.Intent intent2 = new ohos.aafwk.content.Intent();
        intent2.setAction(zosAction.action);
        Bundle extras = intent.getExtras();
        HiLog.info(LABEL, "a action: %{public}s, z action: %{public}s", action, zosAction.action);
        if (extras == null) {
            HiLog.warn(LABEL, "get extra is null", new Object[0]);
            return Optional.ofNullable(intent2);
        }
        Set<String> keySet = extras.keySet();
        IntentParams intentParams = new IntentParams();
        for (String str : keySet) {
            if (str instanceof String) {
                String str2 = str;
                String str3 = zosAction.extrasConverter.get(str2);
                HiLog.info(LABEL, "aExtraKey is %{public}s, zExtraKey is %{public}s", str2, str3);
                if (str3 != null) {
                    intentParams.setParam(str3, convertExtraValue(action, str2, extras.get(str2)));
                } else {
                    HiLog.warn(LABEL, "Cannot get z key from a key: %{public}s", str2);
                }
            } else {
                HiLog.warn(LABEL, "aExtraKey is null", new Object[0]);
            }
        }
        intent2.setParams(intentParams);
        return Optional.ofNullable(intent2);
    }
}
