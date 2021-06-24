package ohos.ace.ability;

import com.huawei.ace.runtime.ALog;
import com.huawei.ace.runtime.EventInfo;
import com.huawei.ace.runtime.IEventReport;
import ohos.hiviewdfx.HiEvent;
import ohos.hiviewdfx.HiView;

public class AceEventReport implements IEventReport {
    private static final String EVENT_KEY_ERROR_TYPE = "ERROR_TYPE";
    private static final String EVENT_KEY_PACKAGE_NAME = "PACKAGE_NAME";
    private static final String EVENT_KEY_PID = "PID";
    public static final int EXCEPTION_EVENT = 951004007;
    public static final int EXCEPTION_FRAMEWORK_APP_START = 951004000;
    public static final int EXCEPTION_INTERNATIONALIZATION = 951004009;
    public static final int EXCEPTION_PLUGIN = 951004008;
    private static final String LOG_TAG = "EventReport";
    private static final int MAX_PACKAGE_NAME_LENGTH = 128;
    private String bundleName = "";
    private int pid = 0;

    private void sendEvent(EventInfo eventInfo) {
        if (eventInfo == null) {
            ALog.e(LOG_TAG, "EventReport::sendEvent failed, EventInfo is null");
        } else {
            HiView.report(new HiEvent(eventInfo.getEventType()).putInt(EVENT_KEY_PID, this.pid).putString(EVENT_KEY_PACKAGE_NAME, this.bundleName).putInt(EVENT_KEY_ERROR_TYPE, eventInfo.getErrType()));
        }
    }

    @Override // com.huawei.ace.runtime.IEventReport
    public void setPid(int i) {
        this.pid = i;
    }

    @Override // com.huawei.ace.runtime.IEventReport
    public void setBundleName(String str) {
        this.bundleName = str;
        if (this.bundleName.length() > 128) {
            this.bundleName = this.bundleName.substring(0, 128);
        }
    }

    @Override // com.huawei.ace.runtime.IEventReport
    public void sendAppStartException(int i) {
        EventInfo eventInfo = new EventInfo(EXCEPTION_FRAMEWORK_APP_START);
        eventInfo.setErrType(i);
        sendEvent(eventInfo);
    }

    @Override // com.huawei.ace.runtime.IEventReport
    public void sendEventException(int i) {
        EventInfo eventInfo = new EventInfo(EXCEPTION_EVENT);
        eventInfo.setErrType(i);
        sendEvent(eventInfo);
    }

    @Override // com.huawei.ace.runtime.IEventReport
    public void sendPluginException(int i) {
        EventInfo eventInfo = new EventInfo(EXCEPTION_PLUGIN);
        eventInfo.setErrType(i);
        sendEvent(eventInfo);
    }

    @Override // com.huawei.ace.runtime.IEventReport
    public void sendInternationalException(int i) {
        EventInfo eventInfo = new EventInfo(EXCEPTION_INTERNATIONALIZATION);
        eventInfo.setErrType(i);
        sendEvent(eventInfo);
    }
}
