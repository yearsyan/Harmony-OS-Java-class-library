package com.huawei.ace.plugin;

public final class DecodeRequestCode {
    public static String getPluginName(int i) {
        if (i < RequestCodeMapping.GEOLOCATIONSTART.getCode() || i > RequestCodeMapping.GEOLOCATIONEND.getCode()) {
            return (i < RequestCodeMapping.BLUETOOTHSTART.getCode() || i > RequestCodeMapping.BLUETOOTHEND.getCode()) ? "" : "ohos.bluetooth.ble.BleCentralManagerPlugin";
        }
        return "com.huawei.ace.systemplugin.geolocation.GeolocationPlugin";
    }
}
