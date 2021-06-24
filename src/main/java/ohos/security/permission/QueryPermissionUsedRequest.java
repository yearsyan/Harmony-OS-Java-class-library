package ohos.security.permission;

import java.util.ArrayList;
import java.util.List;
import ohos.global.icu.impl.PatternTokenizer;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;
import ohos.utils.fastjson.annotation.JSONField;

public class QueryPermissionUsedRequest implements Sequenceable {
    private static final int DEFAULT_CAPACITY = 10;
    @JSONField(name = "btm")
    private long beginTimeMillis;
    @JSONField(name = "bn")
    private String bundleName;
    @JSONField(name = "dl")
    private String deviceLabel;
    @JSONField(name = "etm")
    private long endTimeMillis;
    @JSONField(name = "f")
    private int flag = FlagEnum.FLAG_PERMISSION_USAGE_SUMMARY.getFlag();
    @JSONField(name = "pns")
    private List<String> permissionNames = new ArrayList(10);

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

    public String getDeviceLabel() {
        return this.deviceLabel;
    }

    public void setDeviceLabel(String str) {
        this.deviceLabel = str;
    }

    public String getBundleName() {
        return this.bundleName;
    }

    public void setBundleName(String str) {
        this.bundleName = str;
    }

    public List<String> getPermissionNames() {
        return this.permissionNames;
    }

    public void setPermissionNames(List<String> list) {
        this.permissionNames = list;
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

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int i) {
        this.flag = i;
    }

    public String toString() {
        return "QueryPermissionUsedRequest{deviceLabel='" + this.deviceLabel + PatternTokenizer.SINGLE_QUOTE + ", bundleName='" + this.bundleName + PatternTokenizer.SINGLE_QUOTE + ", permissionNames=" + this.permissionNames + ", beginTimeMillis=" + this.beginTimeMillis + ", endTimeMillis=" + this.endTimeMillis + ", flag=" + this.flag + '}';
    }

    public enum FlagEnum {
        FLAG_PERMISSION_USAGE_SUMMARY(0),
        FLAG_PERMISSION_USAGE_DETAIL(1);
        
        private int flag;

        private FlagEnum(int i) {
            this.flag = i;
        }

        public int getFlag() {
            return this.flag;
        }
    }
}
