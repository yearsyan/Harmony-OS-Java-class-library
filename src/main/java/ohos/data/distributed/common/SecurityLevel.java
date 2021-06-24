package ohos.data.distributed.common;

public enum SecurityLevel {
    NO_LEVEL(0),
    S0(1),
    S1(2),
    S2(3),
    S3(5),
    S4(6);
    
    private int code;

    private SecurityLevel(int i) {
        this.code = i;
    }

    public int getCode() {
        return this.code;
    }
}
