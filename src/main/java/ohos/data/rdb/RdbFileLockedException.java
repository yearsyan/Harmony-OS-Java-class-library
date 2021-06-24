package ohos.data.rdb;

public class RdbFileLockedException extends RdbException {
    private static final long serialVersionUID = -7025895189719204621L;

    public RdbFileLockedException() {
    }

    public RdbFileLockedException(String str) {
        super(str);
    }
}
