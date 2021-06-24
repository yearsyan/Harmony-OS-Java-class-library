package ohos.data.distributed.common;

public enum KvStoreErrorCode {
    SUCCESS(0, "SUCCESS"),
    INVALID_ARGUMENT(1, "INVALID_ARGUMENT"),
    SERVER_UNAVAILABLE(2, "SERVER_UNAVAILABLE"),
    STORE_NOT_OPEN(3, "STORE_NOT_OPEN"),
    STORE_NOT_FOUND(4, "STORE_NOT_FOUND"),
    STORE_ALREADY_SUBSCRIBE(5, "STORE_ALREADY_SUBSCRIBE"),
    STORE_NOT_SUBSCRIBE(6, "STORE_NOT_SUBSCRIBE"),
    KEY_NOT_FOUND(7, "KEY_NOT_FOUND"),
    DB_ERROR(8, "DB_ERROR"),
    NETWORK_ERROR(9, "NETWORK_ERROR"),
    NO_DEVICE_CONNECTED(10, "NO_DEVICE_CONNECTED"),
    PERMISSION_DENIED(11, "PERMISSION_DENIED"),
    IPC_ERROR(12, "IPC_ERROR"),
    UTF_8_NOT_SUPPORT(13, "UTF_8_NOT_SUPPORT"),
    DEVICE_NOT_FOUND(14, "DEVICE_NOT_FOUND"),
    UNKNOWN_ERROR(15, "UNKNOWN_ERROR"),
    NOT_SUPPORT(16, "NOT_SUPPORT"),
    SCHEMA_MISMATCH(17, "SCHEMA_MISMATCH"),
    INVALID_SCHEMA(18, "INVALID_SCHEMA"),
    READ_ONLY(19, "READ_ONLY"),
    INVALID_VALUE_FIELDS(20, "INVALID_VALUE_FIELDS"),
    INVALID_FIELD_TYPE(21, "INVALID_FIELD_TYPE"),
    CONSTRAIN_VIOLATION(22, "CONSTRAIN_VIOLATION"),
    INVALID_FORMAT(23, "INVALID_FORMAT"),
    DEFAULT_DB_ERROR(24, "DEFAULT_DB_ERROR"),
    INVALID_QUERY_FORMAT(25, "INVALID_QUERY_FORMAT"),
    INVALID_QUERY_FIELD(26, "INVALID_QUERY_FIELD"),
    RECOVER_SUCCESS(27, "RECOVER_SUCCESS"),
    RECOVER_FAILED(28, "RECOVER_FAILED"),
    INVALID_VALUE_TYPE(29, "INVALID_VALUE_TYPE"),
    EXCEED_MAX_ACCESS_RATE(31, "EXCEED_MAX_ACCESS_RATE");
    
    private int errorCode;
    private String errorMsg;

    private KvStoreErrorCode(int i, String str) {
        this.errorCode = i;
        this.errorMsg = str;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }
}
