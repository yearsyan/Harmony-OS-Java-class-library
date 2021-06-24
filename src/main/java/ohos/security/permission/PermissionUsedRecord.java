package ohos.security.permission;

import java.util.ArrayList;
import java.util.List;
import ohos.global.icu.impl.PatternTokenizer;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;
import ohos.utils.fastjson.annotation.JSONField;

public class PermissionUsedRecord implements Sequenceable {
    private static final int DEFAULT_CAPACITY = 10;
    @JSONField(name = "acb")
    private int accessCountBg;
    @JSONField(name = "acf")
    private int accessCountFg;
    @JSONField(name = "arb")
    private List<Long> accessRecordBg = new ArrayList(10);
    @JSONField(name = "arf")
    private List<Long> accessRecordFg = new ArrayList(10);
    @JSONField(name = "lat")
    private long lastAccessTime;
    @JSONField(name = "lrt")
    private long lastRejectTime;
    @JSONField(name = "pn")
    private String permissionName;
    @JSONField(name = "rcb")
    private int rejectCountBg;
    @JSONField(name = "rcf")
    private int rejectCountFg;
    @JSONField(name = "rrb")
    private List<Long> rejectRecordBg = new ArrayList(10);
    @JSONField(name = "rrf")
    private List<Long> rejectRecordFg = new ArrayList(10);

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

    public String getPermissionName() {
        return this.permissionName;
    }

    public void setPermissionName(String str) {
        this.permissionName = str;
    }

    public int getAccessCountFg() {
        return this.accessCountFg;
    }

    public void setAccessCountFg(int i) {
        this.accessCountFg = i;
    }

    public int getRejectCountFg() {
        return this.rejectCountFg;
    }

    public void setRejectCountFg(int i) {
        this.rejectCountFg = i;
    }

    public int getAccessCountBg() {
        return this.accessCountBg;
    }

    public void setAccessCountBg(int i) {
        this.accessCountBg = i;
    }

    public int getRejectCountBg() {
        return this.rejectCountBg;
    }

    public void setRejectCountBg(int i) {
        this.rejectCountBg = i;
    }

    public long getLastAccessTime() {
        return this.lastAccessTime;
    }

    public void setLastAccessTime(long j) {
        this.lastAccessTime = j;
    }

    public long getLastRejectTime() {
        return this.lastRejectTime;
    }

    public void setLastRejectTime(long j) {
        this.lastRejectTime = j;
    }

    public List<Long> getAccessRecordFg() {
        return this.accessRecordFg;
    }

    public void setAccessRecordFg(List<Long> list) {
        this.accessRecordFg = list;
    }

    public List<Long> getRejectRecordFg() {
        return this.rejectRecordFg;
    }

    public void setRejectRecordFg(List<Long> list) {
        this.rejectRecordFg = list;
    }

    public List<Long> getAccessRecordBg() {
        return this.accessRecordBg;
    }

    public void setAccessRecordBg(List<Long> list) {
        this.accessRecordBg = list;
    }

    public List<Long> getRejectRecordBg() {
        return this.rejectRecordBg;
    }

    public void setRejectRecordBg(List<Long> list) {
        this.rejectRecordBg = list;
    }

    public String toString() {
        return "PermissionUsedRecord{permissionName='" + this.permissionName + PatternTokenizer.SINGLE_QUOTE + ", accessCountFg=" + this.accessCountFg + ", rejectCountFg=" + this.rejectCountFg + ", accessCountBg=" + this.accessCountBg + ", rejectCountBg=" + this.rejectCountBg + ", lastAccessTime=" + this.lastAccessTime + ", lastRejectTime=" + this.lastRejectTime + ", accessRecordFg=" + this.accessRecordFg + ", rejectRecordFg=" + this.rejectRecordFg + ", accessRecordBg=" + this.accessRecordBg + ", rejectRecordBg=" + this.rejectRecordBg + '}';
    }
}
