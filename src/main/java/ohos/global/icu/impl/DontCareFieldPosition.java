package ohos.global.icu.impl;

import java.text.FieldPosition;

public final class DontCareFieldPosition extends FieldPosition {
    public static final DontCareFieldPosition INSTANCE = new DontCareFieldPosition();

    public void setBeginIndex(int i) {
    }

    public void setEndIndex(int i) {
    }

    private DontCareFieldPosition() {
        super(-913028704);
    }
}
