package ohos.distributedhardware.devicemanager;

public interface ReturnCode {
    public static final int ERR_ABANDON_BY_NEW_REQUEST = 2011;
    public static final int ERR_ADD_MEMBER_FAIL = 2024;
    public static final int ERR_CALLBACK_ALREADY_EXIST = 1004;
    public static final int ERR_CALLBACK_OUT_OF_LIMIT = 1003;
    public static final int ERR_CHANNEL_BROKEN = 2010;
    public static final int ERR_CONNECT_DISCOVERY_SERVICE_FAIL = 2005;
    public static final int ERR_CREATE_GROUP_FAIL = 2020;
    public static final int ERR_DISCOVERY_SERVICE_DISCONNECTED = 2004;
    public static final int ERR_DM_SERVICE_DISCONNECTED = 1006;
    public static final int ERR_GET_LOCAL_DEVICE_INFO_FAIL = 1011;
    public static final int ERR_JOIN_GROUP_FAIL = 2021;
    public static final int ERR_NOT_FOREGROUND_APP_WHEN_CALL = 1010;
    public static final int ERR_NOT_FOREGROUND_APP_WHEN_RUN = 2007;
    public static final int ERR_OPEN_CHANNEL_FAIL = 2008;
    public static final int ERR_PARA_INVALID = 1001;
    public static final int ERR_REGISTER_DESTROYED = 1005;
    public static final int ERR_REQUEST_CANCEL = 2013;
    public static final int ERR_REQUEST_QR_FAIL = 2014;
    public static final int ERR_SESSION_NOT_EXIST = 2019;
    public static final int ERR_SET_FRIEND_LIST_FAIL = 2027;
    public static final int ERR_START_REMOTE_DM = 2006;
    public static final int ERR_TIMEOUT = 2012;
    public static final int ERR_UNREGISTER_CALLBACK_NOT_EXIST = 1002;
    public static final int ERR_USER_BUSY = 2016;
    public static final int ERR_USER_REJECT = 2015;
    public static final int ERR_WRITE_EXT_INFO_FAIL = 2025;
    public static final int STOP_MIGRATE_ACTION = 1;
    public static final int SUCCESS = 0;
}
