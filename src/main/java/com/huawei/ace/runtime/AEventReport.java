package com.huawei.ace.runtime;

public class AEventReport {
    public static final int CHANGE_LOCALE_ERR = 0;
    public static final int CONTEXT_NULL_ERR = 0;
    public static final int CREATE_CONTAINER_ERR = 1;
    public static final int FILE_CACHE_PATH_INIT_ERR = 7;
    public static final int GET_OFFSET_ERR = 0;
    public static final int GET_PACKAGE_PATH_ERR = 4;
    public static final int IMAGE_CACHE_PATH_INIT_ERR = 8;
    public static final int LOAD_LIBRARY_ERR = 9;
    public static final int OHOS_SYSTEM_PLUGIN_LOAD_ERR = 2;
    public static final int RESOURCE_MANAGER_ERR = 6;
    public static final int TEXT_INPUT_PLUGIN_ERR = 1;
    public static final int VIDEO_INIT_ERR = 0;
    private static IEventReport eventReport;

    private AEventReport() {
    }

    public static void setEventReport(IEventReport iEventReport) {
        eventReport = iEventReport;
    }

    public static void setPid(int i) {
        IEventReport iEventReport = eventReport;
        if (iEventReport != null) {
            iEventReport.setPid(i);
        }
    }

    public static void setBundleName(String str) {
        IEventReport iEventReport = eventReport;
        if (iEventReport != null) {
            iEventReport.setBundleName(str);
        }
    }

    public static void sendAppStartException(int i) {
        IEventReport iEventReport = eventReport;
        if (iEventReport != null) {
            iEventReport.sendAppStartException(i);
        }
    }

    public static void sendEventException(int i) {
        IEventReport iEventReport = eventReport;
        if (iEventReport != null) {
            iEventReport.sendEventException(i);
        }
    }

    public static void sendPluginException(int i) {
        IEventReport iEventReport = eventReport;
        if (iEventReport != null) {
            iEventReport.sendPluginException(i);
        }
    }

    public static void sendInternationalException(int i) {
        IEventReport iEventReport = eventReport;
        if (iEventReport != null) {
            iEventReport.sendInternationalException(i);
        }
    }
}
