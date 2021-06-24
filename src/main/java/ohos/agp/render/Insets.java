package ohos.agp.render;

import ohos.agp.utils.Rect;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

public class Insets implements Sequenceable {
    public static final Insets NULL_INSET = new Insets(0, 0, 0, 0);
    public final int bottomValue;
    public final int leftValue;
    public final int rightValue;
    public final int topValue;

    @Override // ohos.utils.Sequenceable
    public boolean hasFileDescriptor() {
        return false;
    }

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        return false;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        return false;
    }

    public Insets(int i, int i2, int i3, int i4) {
        this.leftValue = i;
        this.topValue = i2;
        this.rightValue = i3;
        this.bottomValue = i4;
    }

    public static Insets setValue(int i, int i2, int i3, int i4) {
        if (i == 0 && i2 == 0 && i3 == 0 && i4 == 0) {
            return NULL_INSET;
        }
        return new Insets(i, i2, i2, i4);
    }

    public static Insets setValue(Rect rect) {
        if (rect == null) {
            return NULL_INSET;
        }
        return setValue(rect.left, rect.top, rect.right, rect.bottom);
    }
}
