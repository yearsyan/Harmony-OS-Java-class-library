package ohos.security.permission;

import java.util.ArrayList;
import java.util.List;
import ohos.global.icu.impl.PatternTokenizer;
import ohos.security.permission.infrastructure.utils.LogUtil;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;
import ohos.utils.fastjson.annotation.JSONField;

public class BundlePermissionUsedRecord implements Sequenceable {
    private static final int DEFAULT_CAPACITY = 10;
    @JSONField(name = "bl")
    private String bundleLabel;
    @JSONField(name = "bn")
    private String bundleName;
    @JSONField(name = "di")
    private String deviceId;
    @JSONField(name = "dl")
    private String deviceLabel;
    @JSONField(name = "prs")
    private List<PermissionUsedRecord> permissionUsedRecords = new ArrayList(10);

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

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String str) {
        this.deviceId = str;
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

    public String getBundleLabel() {
        return this.bundleLabel;
    }

    public void setBundleLabel(String str) {
        this.bundleLabel = str;
    }

    public List<PermissionUsedRecord> getPermissionUsedRecords() {
        return this.permissionUsedRecords;
    }

    public void setPermissionUsedRecords(List<PermissionUsedRecord> list) {
        this.permissionUsedRecords = list;
    }

    public String toString() {
        return "BundlePermissionUsedRecord{deviceId='" + LogUtil.mask(this.deviceId) + PatternTokenizer.SINGLE_QUOTE + ", deviceLabel='" + this.deviceLabel + PatternTokenizer.SINGLE_QUOTE + ", bundleName='" + this.bundleName + PatternTokenizer.SINGLE_QUOTE + ", bundleLabel='" + this.bundleLabel + PatternTokenizer.SINGLE_QUOTE + ", permissionUsedRecords=" + this.permissionUsedRecords + '}';
    }
}
