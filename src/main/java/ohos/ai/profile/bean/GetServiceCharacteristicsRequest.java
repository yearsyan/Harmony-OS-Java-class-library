package ohos.ai.profile.bean;

import ohos.annotation.SystemApi;

@SystemApi
public class GetServiceCharacteristicsRequest {
    private int dataSourceType;
    private String devId;
    private String extraInfo;
    private boolean isSync;
    private String pkgName;
    private String sid;

    public String getPkgName() {
        return this.pkgName;
    }

    public void setPkgName(String str) {
        this.pkgName = str;
    }

    public String getDevId() {
        return this.devId;
    }

    public void setDevId(String str) {
        this.devId = str;
    }

    public String getSid() {
        return this.sid;
    }

    public void setSid(String str) {
        this.sid = str;
    }

    public boolean isSync() {
        return this.isSync;
    }

    public void setSync(boolean z) {
        this.isSync = z;
    }

    public int getDataSourceType() {
        return this.dataSourceType;
    }

    public void setDataSourceType(int i) {
        this.dataSourceType = i;
    }

    public String getExtraInfo() {
        return this.extraInfo;
    }

    public void setExtraInfo(String str) {
        this.extraInfo = str;
    }
}
