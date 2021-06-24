package ohos.appexecfwk.utils;

import huawei.hiview.HiEvent;
import huawei.hiview.HiView;

public class HiViewUtil {
    public static final int ABILITY_TYPE_DATA = 1;
    public static final int ABILITY_TYPE_PAGE = 1;
    public static final int ABILITY_TYPE_SERVICE = 1;
    public static final int ABILITY_TYPE_UNKNOW = 0;
    public static final int ADD_FORM_EXCEEDS_LIMIT = 0;
    public static final int DEFAULT_ERROR_TYPE = 0;
    public static final int DEFAULT_EXCEPTION_ID = 0;
    private static final int EVENT_ID_ABILITY_STARTUP = 951000118;
    private static final int EVENT_ID_ABILITY_START_RESULT = 951000119;
    private static final int EVENT_ID_BMS_INSTALL_RESULT = 951000207;
    private static final int EVENT_ID_BMS_INSTALL_STATE = 951000206;
    public static final int EVENT_ID_BMS_INTERFACE_FAILED = 951000102;
    public static final int EVENT_ID_BMS_SERVICE_FAILED = 951000101;
    private static final int EVENT_ID_CONNECT_ABILITY_FAILED = 951000002;
    public static final int EVENT_ID_DISTRIBUTED_DATAMGR_INTERFACE_FAILED = 951000104;
    public static final int EVENT_ID_DMS_CALLBACK_FAILED = 950004203;
    public static final int EVENT_ID_DMS_INTERFACE_FAILED = 950004202;
    public static final int EVENT_ID_DMS_SERVICE_FAILED = 950004201;
    private static final int EVENT_ID_FMS_ADD_FORM_FAILED = 951000022;
    public static final int EVENT_ID_GLOBAL_INTERFACE_FAILED = 951000103;
    public static final int EVENT_ID_INIT_ENVIRONMENT_FAILED = 951000105;
    private static final int EVENT_ID_LOAD_ABILITY_FAILED = 951000001;
    public static final int FREEINSTALL_ERRORTYPE_OTHERS = 1;
    public static final int FREEINSTALL_ERRORTYPE_SUCCESS = 0;
    public static final int FREEINSTALL_STATE_BEGIN = 0;
    public static final int FREEINSTALL_STATE_CANCEL = 1;
    private static final String HIEVENT_KEY_ABILITY_NAME = "ABILITY_NAME";
    private static final String HIEVENT_KEY_ABILITY_TYPE = "ABILITY_TYPE";
    private static final String HIEVENT_KEY_BUNDLE_NAME = "BUNDLE_NAME";
    private static final String HIEVENT_KEY_CALLBACK_NAME = "CALLBACK_NAME";
    private static final String HIEVENT_KEY_CLASS_NAME = "CLASS_NAME";
    private static final String HIEVENT_KEY_ERROR_TYPE = "ERROR_TYPE";
    private static final String HIEVENT_KEY_EXCEPTION_ID = "EXCEPTION_ID";
    private static final String HIEVENT_KEY_INTERFACE_NAME = "INTERFACE_NAME";
    private static final String HIEVENT_KEY_PACKAGE_NAME = "PACKAGE_NAME";
    private static final String HIEVENT_KEY_SERVICE_ID = "SERVICE_ID";
    private static final String HIEVENT_KEY_SERVICE_NAME = "SERVICE_NAME";
    private static final String HIEVENT_KEY_START_TYPE = "START_TYPE";
    private static final String HIEVENT_KEY_TYPE = "TYPE";
    public static final int START_ERRORTYPE_NOTFOUND = 1;
    public static final int START_ERRORTYPE_OTHERS = 3;
    public static final int START_ERRORTYPE_PERMISSIONDENEIED = 2;
    public static final int START_ERRORTYPE_SUCCESS = 0;
    public static final int START_TYPE_FREEINSTALL = 1;
    public static final int START_TYPE_UNKNOW = 0;

    public static void sendAppEnvironmentEvent() {
        ExceptionInfo exceptionInfo = new ExceptionInfo(EVENT_ID_INIT_ENVIRONMENT_FAILED);
        exceptionInfo.setExceptionId(0);
        sendEvent(exceptionInfo);
    }

    public static void sendBmsInterfaceEvent(String str, String str2, String str3, int i) {
        ExceptionInfo exceptionInfo = new ExceptionInfo((int) EVENT_ID_BMS_INTERFACE_FAILED, 401);
        exceptionInfo.setInterfaceName(str);
        exceptionInfo.setPackageName(str2);
        exceptionInfo.setClassName(str3);
        exceptionInfo.setErrorType(i);
        sendEvent(exceptionInfo);
    }

    public static void sendBmsEvent() {
        sendEvent(new ExceptionInfo((int) EVENT_ID_BMS_SERVICE_FAILED, 401));
    }

    public static void sendDmsEvent() {
        sendEvent(new ExceptionInfo((int) EVENT_ID_DMS_SERVICE_FAILED, 1401));
    }

    public static void sendDmsInterfaceEvent(String str, int i) {
        ExceptionInfo exceptionInfo = new ExceptionInfo((int) EVENT_ID_DMS_INTERFACE_FAILED, 1401);
        exceptionInfo.setInterfaceName(str);
        exceptionInfo.setErrorType(i);
        sendEvent(exceptionInfo);
    }

    public static void sendGlobalEvent(String str, String str2, int i) {
        ExceptionInfo exceptionInfo = new ExceptionInfo(EVENT_ID_GLOBAL_INTERFACE_FAILED);
        exceptionInfo.setServiceName(AppConstants.GLOBAL_SERVICE_NAME);
        exceptionInfo.setInterfaceName(str);
        exceptionInfo.setAbilityName(str2);
        exceptionInfo.setErrorType(i);
        sendEvent(exceptionInfo);
    }

    public static void sendDistributedDataEvent(String str, int i) {
        ExceptionInfo exceptionInfo = new ExceptionInfo(EVENT_ID_DISTRIBUTED_DATAMGR_INTERFACE_FAILED);
        exceptionInfo.setServiceName(AppConstants.DISTRIBUTE_DATA_SERVICE_NAME);
        exceptionInfo.setInterfaceName(str);
        exceptionInfo.setErrorType(i);
        sendEvent(exceptionInfo);
    }

    public static void sendAbilityEvent(String str, String str2, int i) {
        HiEvent hiEvent = new HiEvent((int) EVENT_ID_LOAD_ABILITY_FAILED);
        if (str != null) {
            hiEvent.putString(HIEVENT_KEY_PACKAGE_NAME, str);
        }
        if (str2 != null) {
            hiEvent.putString(HIEVENT_KEY_ABILITY_NAME, str2);
        }
        hiEvent.putInt(HIEVENT_KEY_ERROR_TYPE, i);
        HiView.report(hiEvent);
    }

    public static void sendConnectAbilityEvent(String str, String str2, int i) {
        HiEvent hiEvent = new HiEvent(951000002);
        if (str != null) {
            hiEvent.putString(HIEVENT_KEY_PACKAGE_NAME, str);
        }
        if (str2 != null) {
            hiEvent.putString(HIEVENT_KEY_ABILITY_NAME, str2);
        }
        hiEvent.putInt(HIEVENT_KEY_ERROR_TYPE, i);
        HiView.report(hiEvent);
    }

    public static void sendAbilityStartup(String str, String str2, int i) {
        HiEvent hiEvent = new HiEvent((int) EVENT_ID_ABILITY_STARTUP);
        if (str != null) {
            hiEvent.putString(HIEVENT_KEY_BUNDLE_NAME, str);
        }
        if (str2 != null) {
            hiEvent.putString(HIEVENT_KEY_ABILITY_NAME, str2);
        }
        hiEvent.putInt(HIEVENT_KEY_START_TYPE, i);
        hiEvent.putInt(HIEVENT_KEY_ABILITY_TYPE, 0);
        HiView.report(hiEvent);
    }

    public static void sendAbilityStartResult(String str, String str2, int i, int i2, int i3) {
        HiEvent hiEvent = new HiEvent((int) EVENT_ID_ABILITY_START_RESULT);
        if (str != null) {
            hiEvent.putString(HIEVENT_KEY_BUNDLE_NAME, str);
        }
        if (str2 != null) {
            hiEvent.putString(HIEVENT_KEY_ABILITY_NAME, str2);
        }
        hiEvent.putInt(HIEVENT_KEY_ABILITY_TYPE, i);
        hiEvent.putInt(HIEVENT_KEY_START_TYPE, i2);
        hiEvent.putInt(HIEVENT_KEY_ERROR_TYPE, i3);
        HiView.report(hiEvent);
    }

    public static void sendDownloadAndInstallState(String str, int i) {
        HiEvent hiEvent = new HiEvent((int) EVENT_ID_BMS_INSTALL_STATE);
        if (str != null) {
            hiEvent.putString(HIEVENT_KEY_BUNDLE_NAME, str);
        }
        hiEvent.putInt(HIEVENT_KEY_TYPE, i);
        HiView.report(hiEvent);
    }

    public static void sendDownloadAndInstallResult(String str, int i) {
        HiEvent hiEvent = new HiEvent((int) EVENT_ID_BMS_INSTALL_RESULT);
        if (str != null) {
            hiEvent.putString(HIEVENT_KEY_BUNDLE_NAME, str);
        }
        hiEvent.putInt(HIEVENT_KEY_ERROR_TYPE, i);
        HiView.report(hiEvent);
    }

    public static void sendAddFormExceedLimitEvent() {
        HiEvent hiEvent = new HiEvent((int) EVENT_ID_FMS_ADD_FORM_FAILED);
        hiEvent.putInt(HIEVENT_KEY_ERROR_TYPE, 0);
        HiView.report(hiEvent);
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0033  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void sendEvent(ohos.appexecfwk.utils.ExceptionInfo r2) {
        /*
            if (r2 != 0) goto L_0x000b
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.String r0 = "HiViewUtil::sendEvent failed, exceptionInfo is null"
            ohos.appexecfwk.utils.AppLog.e(r0, r2)
            return
        L_0x000b:
            int r0 = r2.getEventId()
            huawei.hiview.HiEvent r1 = new huawei.hiview.HiEvent
            r1.<init>(r0)
            switch(r0) {
                case 950004201: goto L_0x0033;
                case 950004202: goto L_0x002f;
                case 950004203: goto L_0x002b;
                default: goto L_0x0017;
            }
        L_0x0017:
            switch(r0) {
                case 951000101: goto L_0x0033;
                case 951000102: goto L_0x0027;
                case 951000103: goto L_0x0023;
                case 951000104: goto L_0x001f;
                case 951000105: goto L_0x001b;
                default: goto L_0x001a;
            }
        L_0x001a:
            goto L_0x0036
        L_0x001b:
            setEvenironmentEventInfo(r1, r2)
            goto L_0x0036
        L_0x001f:
            setDistribtePathEventInfo(r1, r2)
            goto L_0x0036
        L_0x0023:
            setGlobalInterfaceEventInfo(r1, r2)
            goto L_0x0036
        L_0x0027:
            setBmsInterfaceEventInfo(r1, r2)
            goto L_0x0036
        L_0x002b:
            setDmsCallbackEventInfo(r1, r2)
            goto L_0x0036
        L_0x002f:
            setDmsInterfaceEventInfo(r1, r2)
            goto L_0x0036
        L_0x0033:
            setServiceEventInfo(r1, r2)
        L_0x0036:
            huawei.hiview.HiView.report(r1)
            return
            switch-data {950004201->0x0033, 950004202->0x002f, 950004203->0x002b, }
            switch-data {951000101->0x0033, 951000102->0x0027, 951000103->0x0023, 951000104->0x001f, 951000105->0x001b, }
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.appexecfwk.utils.HiViewUtil.sendEvent(ohos.appexecfwk.utils.ExceptionInfo):void");
    }

    private static void setServiceEventInfo(HiEvent hiEvent, ExceptionInfo exceptionInfo) {
        hiEvent.putInt(HIEVENT_KEY_SERVICE_ID, exceptionInfo.getServiceId());
    }

    private static void setDmsInterfaceEventInfo(HiEvent hiEvent, ExceptionInfo exceptionInfo) {
        hiEvent.putInt(HIEVENT_KEY_SERVICE_ID, exceptionInfo.getServiceId());
        hiEvent.putString(HIEVENT_KEY_INTERFACE_NAME, exceptionInfo.getInterfaceName());
        hiEvent.putInt(HIEVENT_KEY_ERROR_TYPE, exceptionInfo.getErrorType());
    }

    private static void setDmsCallbackEventInfo(HiEvent hiEvent, ExceptionInfo exceptionInfo) {
        hiEvent.putInt(HIEVENT_KEY_SERVICE_ID, exceptionInfo.getServiceId());
        hiEvent.putString(HIEVENT_KEY_INTERFACE_NAME, exceptionInfo.getInterfaceName());
        hiEvent.putString(HIEVENT_KEY_CALLBACK_NAME, exceptionInfo.getCallbackName());
        hiEvent.putInt(HIEVENT_KEY_ERROR_TYPE, exceptionInfo.getErrorType());
    }

    private static void setBmsInterfaceEventInfo(HiEvent hiEvent, ExceptionInfo exceptionInfo) {
        hiEvent.putInt(HIEVENT_KEY_SERVICE_ID, exceptionInfo.getServiceId());
        hiEvent.putString(HIEVENT_KEY_INTERFACE_NAME, exceptionInfo.getInterfaceName());
        hiEvent.putString(HIEVENT_KEY_PACKAGE_NAME, exceptionInfo.getPackageName());
        hiEvent.putString(HIEVENT_KEY_CLASS_NAME, exceptionInfo.getClassName());
        hiEvent.putInt(HIEVENT_KEY_ERROR_TYPE, exceptionInfo.getErrorType());
    }

    private static void setGlobalInterfaceEventInfo(HiEvent hiEvent, ExceptionInfo exceptionInfo) {
        hiEvent.putString(HIEVENT_KEY_SERVICE_NAME, exceptionInfo.getServiceName());
        hiEvent.putString(HIEVENT_KEY_INTERFACE_NAME, exceptionInfo.getInterfaceName());
        hiEvent.putString(HIEVENT_KEY_ABILITY_NAME, exceptionInfo.getAbilityName());
        hiEvent.putInt(HIEVENT_KEY_ERROR_TYPE, exceptionInfo.getErrorType());
    }

    private static void setDistribtePathEventInfo(HiEvent hiEvent, ExceptionInfo exceptionInfo) {
        hiEvent.putString(HIEVENT_KEY_SERVICE_NAME, exceptionInfo.getServiceName());
        hiEvent.putString(HIEVENT_KEY_INTERFACE_NAME, exceptionInfo.getInterfaceName());
        hiEvent.putInt(HIEVENT_KEY_ERROR_TYPE, exceptionInfo.getErrorType());
    }

    private static void setEvenironmentEventInfo(HiEvent hiEvent, ExceptionInfo exceptionInfo) {
        hiEvent.putInt(HIEVENT_KEY_EXCEPTION_ID, exceptionInfo.getExceptionId());
    }
}
