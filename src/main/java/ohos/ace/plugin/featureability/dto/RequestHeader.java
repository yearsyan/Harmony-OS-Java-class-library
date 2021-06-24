package ohos.ace.plugin.featureability.dto;

import ohos.ace.plugin.featureability.FeatureAbilityConstants;

public class RequestHeader {
    private Integer code;
    private InterfaceElement element;
    private String token;
    private int type = 0;

    public String toString() {
        return this.element.toString() + ": messageCode = " + String.valueOf(this.code) + ", syncOption = " + this.type;
    }

    public boolean isSync() {
        return FeatureAbilityConstants.isSyncRequest(this.type);
    }

    public boolean isRequestLocal() {
        return FeatureAbilityConstants.isLocalInterface(this.element.getType());
    }

    public boolean isRequestExtend() {
        return FeatureAbilityConstants.isExtendInterface(this.element.getType());
    }

    public boolean isRequestZidl() {
        return !isRequestLocal() && isRequestExtend();
    }

    public InterfaceElement getElement() {
        return this.element;
    }

    public void setElement(InterfaceElement interfaceElement) {
        this.element = interfaceElement;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String str) {
        this.token = str;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer num) {
        this.code = num;
    }
}
