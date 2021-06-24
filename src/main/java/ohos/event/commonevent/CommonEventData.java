package ohos.event.commonevent;

import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

public final class CommonEventData implements Sequenceable {
    private static final HiLogLabel LABEL = new HiLogLabel(3, EventConstant.COMMON_EVENT_DOMAIN, TAG);
    public static final Sequenceable.Producer<CommonEventData> PRODUCER = $$Lambda$CommonEventData$H60yIkNMEwaO7WJPFNvFbxNnfk.INSTANCE;
    private static final String TAG = "CommonEventData";
    private int code;
    private String data;
    private Intent intent;

    static /* synthetic */ CommonEventData lambda$static$0(Parcel parcel) {
        CommonEventData commonEventData = new CommonEventData();
        commonEventData.unmarshalling(parcel);
        return commonEventData;
    }

    public CommonEventData() {
        this(null);
    }

    public CommonEventData(Intent intent2) {
        this(intent2, 0, null);
    }

    public CommonEventData(Intent intent2, int i, String str) {
        if (intent2 != null) {
            this.intent = new Intent(intent2);
        }
        this.code = i;
        this.data = str;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public int getCode() {
        return this.code;
    }

    public void setData(String str) {
        this.data = str;
    }

    public String getData() {
        return this.data;
    }

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        if (parcel == null) {
            HiLog.error(LABEL, "Parcel is null", new Object[0]);
            return false;
        }
        if (this.intent == null) {
            if (!parcel.writeInt(0)) {
                HiLog.warn(LABEL, "write intent failed.", new Object[0]);
                return false;
            }
        } else if (!parcel.writeInt(1)) {
            HiLog.warn(LABEL, "write intent failed.", new Object[0]);
            return false;
        } else {
            parcel.writeSequenceable(this.intent);
        }
        if (!parcel.writeInt(this.code)) {
            HiLog.warn(LABEL, "write code failed.", new Object[0]);
            return false;
        } else if (parcel.writeString(this.data)) {
            return true;
        } else {
            HiLog.warn(LABEL, "write data failed.", new Object[0]);
            return false;
        }
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        if (parcel == null) {
            HiLog.error(LABEL, "Parcel is null", new Object[0]);
            return false;
        }
        int readInt = parcel.readInt();
        if (readInt == 1) {
            this.intent = new Intent();
            if (!parcel.readSequenceable(this.intent)) {
                HiLog.warn(LABEL, "read intent failed.", new Object[0]);
                return false;
            }
        } else if (readInt == 0) {
            this.intent = null;
        } else {
            HiLog.warn(LABEL, "read invalid parcel.", new Object[0]);
            return false;
        }
        this.code = parcel.readInt();
        this.data = parcel.readString();
        return true;
    }

    public String toString() {
        return "CommonEventData[ code = " + this.code + " data = " + this.data + "]";
    }
}
