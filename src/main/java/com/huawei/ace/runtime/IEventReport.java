package com.huawei.ace.runtime;

public interface IEventReport {
    void sendAppStartException(int i);

    void sendEventException(int i);

    void sendInternationalException(int i);

    void sendPluginException(int i);

    void setBundleName(String str);

    void setPid(int i);
}
