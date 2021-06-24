package ohos.aafwk.ability.continuation;

import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

public class ExtraParams implements Sequenceable {
    public static final String DEVICETYPE_CHILDREN_WATCH = "085";
    public static final String DEVICETYPE_DESKTOP_PC = "00B";
    public static final String DEVICETYPE_LAPTOP = "00C";
    public static final String DEVICETYPE_SMART_CAR = "083";
    public static final String DEVICETYPE_SMART_PAD = "011";
    public static final String DEVICETYPE_SMART_PHONE = "00E";
    public static final String DEVICETYPE_SMART_SPEAKER = "00A";
    public static final String DEVICETYPE_SMART_TV = "09C";
    public static final String DEVICETYPE_SMART_WATCH = "06D";
    private String description;
    private String[] devType;
    private String jsonParams;
    private String targetBundleName;

    public ExtraParams() {
        this(null, null, null, null);
    }

    public ExtraParams(String[] strArr, String str, String str2, String str3) {
        this.devType = strArr;
        this.targetBundleName = str;
        this.description = str2;
        this.jsonParams = str3;
    }

    public String[] getDevType() {
        return this.devType;
    }

    public void setDevType(String[] strArr) {
        this.devType = strArr;
    }

    public String getTargetBundleName() {
        return this.targetBundleName;
    }

    public void setTargetBundleName(String str) {
        this.targetBundleName = str;
    }

    public String getDescription() {
        return this.description;
    }

    public void setJsonParams(String str) {
        this.jsonParams = str;
    }

    public String getJsonParams() {
        return this.jsonParams;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        if (parcel.writeStringArray(this.devType) && parcel.writeString(this.targetBundleName) && parcel.writeString(this.description) && parcel.writeString(this.jsonParams)) {
            return true;
        }
        return false;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        this.devType = parcel.readStringArray();
        this.targetBundleName = parcel.readString();
        this.description = parcel.readString();
        this.jsonParams = parcel.readString();
        return true;
    }
}
