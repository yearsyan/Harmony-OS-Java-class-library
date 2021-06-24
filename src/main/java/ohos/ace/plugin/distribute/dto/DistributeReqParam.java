package ohos.ace.plugin.distribute.dto;

import java.util.List;
import ohos.bundle.ElementName;

public class DistributeReqParam extends ElementName {
    private String action;
    private String data;
    private int deviceType = 0;
    private List<String> entities;
    private int flag;
    private String url;

    public String getAction() {
        return this.action;
    }

    public void setAction(String str) {
        this.action = str;
    }

    public List<String> getEntities() {
        return this.entities;
    }

    public void setEntities(List<String> list) {
        this.entities = list;
    }

    public int getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(int i) {
        this.deviceType = i;
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int i) {
        this.flag = i;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String str) {
        this.data = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }
}
