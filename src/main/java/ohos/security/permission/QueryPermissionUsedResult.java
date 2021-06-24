package ohos.security.permission;

import java.util.ArrayList;
import java.util.List;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;
import ohos.utils.fastjson.annotation.JSONField;

public class QueryPermissionUsedResult implements Sequenceable {
    private static final int DEFAULT_CAPACITY = 10;
    @JSONField(name = "btm")
    private long beginTimeMillis;
    @JSONField(name = "dprs")
    private List<BundlePermissionUsedRecord> bundlePermissionUsedRecords = new ArrayList(10);
    @JSONField(name = "c")
    private int code;
    @JSONField(name = "etm")
    private long endTimeMillis;

    @Override // ohos.utils.Sequenceable
    @Deprecated
    public boolean marshalling(Parcel parcel) {
        return false;
    }

    @Override // ohos.utils.Sequenceable
    @Deprecated
    public boolean unmarshalling(Parcel parcel) {
        return false;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public long getBeginTimeMillis() {
        return this.beginTimeMillis;
    }

    public void setBeginTimeMillis(long j) {
        this.beginTimeMillis = j;
    }

    public long getEndTimeMillis() {
        return this.endTimeMillis;
    }

    public void setEndTimeMillis(long j) {
        this.endTimeMillis = j;
    }

    public List<BundlePermissionUsedRecord> getBundlePermissionUsedRecords() {
        return this.bundlePermissionUsedRecords;
    }

    public void setBundlePermissionUsedRecords(List<BundlePermissionUsedRecord> list) {
        this.bundlePermissionUsedRecords = list;
    }

    public String toString() {
        return "QueryPermissionUsedResult{code=" + this.code + ", beginTimeMillis=" + this.beginTimeMillis + ", endTimeMillis=" + this.endTimeMillis + ", bundlePermissionUsedRecords=" + this.bundlePermissionUsedRecords + '}';
    }
}
