package ohos.devicesecmgr;

public class RequestOption {
    private long challenge;
    private int extra;
    private int timeout;

    public RequestOption(long j, int i, int i2) {
        this.challenge = j;
        this.timeout = i;
        this.extra = i2;
    }

    public long getChallenge() {
        return this.challenge;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public int getExtra() {
        return this.extra;
    }
}
