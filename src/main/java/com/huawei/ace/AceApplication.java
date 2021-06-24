package com.huawei.ace;

import android.app.Application;
import com.huawei.ace.runtime.ALog;

public class AceApplication extends Application {
    private static final String LOG_TAG = "AceApplication";
    private static Thread loadSoThread;

    public void onCreate() {
        ALog.setLogger(new Logger());
        ALog.i(LOG_TAG, "AceApplication::onCreate called");
        super.onCreate();
    }
}
