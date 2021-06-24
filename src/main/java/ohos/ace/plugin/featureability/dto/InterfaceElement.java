package ohos.ace.plugin.featureability.dto;

public class InterfaceElement {
    private String bundleName;
    private String deviceId;
    private String name;
    private int type;

    public InterfaceElement() {
        this.type = 1;
    }

    public InterfaceElement(String str, String str2) {
        this.type = 1;
        this.bundleName = str;
        this.name = str2;
    }

    public InterfaceElement(String str, String str2, int i) {
        this(str, str2);
        this.type = i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String str = this.deviceId;
        String str2 = "";
        if (str == null) {
            str = str2;
        }
        sb.append(str);
        sb.append("#");
        String str3 = this.bundleName;
        if (str3 == null) {
            str3 = str2;
        }
        sb.append(str3);
        sb.append("#");
        String str4 = this.name;
        if (str4 != null) {
            str2 = str4;
        }
        sb.append(str2);
        sb.append("#");
        sb.append(this.type);
        return sb.toString();
    }

    public int hashCode() {
        int i = this.type;
        String str = this.deviceId;
        int i2 = 0;
        int hashCode = i + (str == null ? 0 : str.hashCode() * 32);
        String str2 = this.bundleName;
        int hashCode2 = hashCode + (str2 == null ? 0 : str2.hashCode() * 32 * 32);
        String str3 = this.name;
        if (str3 != null) {
            i2 = str3.hashCode() * 32 * 32 * 32;
        }
        return hashCode2 + i2;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof InterfaceElement)) {
            return false;
        }
        InterfaceElement interfaceElement = (InterfaceElement) obj;
        if (this.type != interfaceElement.getType() || !sameString(this.deviceId, interfaceElement.getDeviceId()) || !sameString(this.bundleName, interfaceElement.getBundleName()) || !sameString(this.name, interfaceElement.getName())) {
            return false;
        }
        return true;
    }

    private boolean sameString(String str, String str2) {
        if (str == null) {
            return str2 == null;
        }
        return str.equals(str2);
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setType(String str) {
        this.deviceId = str;
    }

    public String getBundleName() {
        return this.bundleName;
    }

    public void setBundleName(String str) {
        this.bundleName = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }
}
