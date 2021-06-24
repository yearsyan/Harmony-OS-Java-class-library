package com.huawei.ace.plugin;

public enum RequestCodeMapping {
    GEOLOCATIONSTART(10),
    GEOLOCATIONEND(19),
    BLUETOOTHSTART(20),
    BLUETOOTHEND(29);
    
    private final int code;

    private RequestCodeMapping(int i) {
        this.code = i;
    }

    public int getCode() {
        return this.code;
    }
}
