package ohos.agp.window.view;

import android.graphics.Insets;
import android.view.DisplayCutout;
import java.util.ArrayList;
import java.util.List;
import ohos.agp.utils.Rect;
import ohos.agp.window.wmc.AGPWindowManager;

public final class DisplayCutoutWrapper {
    private DisplayCutout mDisplayCutout;

    public static class ParameterInvalidException extends RuntimeException {
        private static final long serialVersionUID = 5459801466633442242L;

        public ParameterInvalidException() {
            this(null);
        }

        public ParameterInvalidException(String str) {
            super(str);
        }
    }

    public DisplayCutoutWrapper(DisplayCutout displayCutout) {
        if (displayCutout != null) {
            this.mDisplayCutout = displayCutout;
            return;
        }
        throw new AGPWindowManager.BadWindowException("reason: displayCutout is null");
    }

    public Rect getNotchRectBottom() {
        return transfromRectAtoZ(this.mDisplayCutout.getBoundingRectBottom());
    }

    public Rect getNotchRectLeft() {
        return transfromRectAtoZ(this.mDisplayCutout.getBoundingRectLeft());
    }

    public Rect getNotchRectRight() {
        return transfromRectAtoZ(this.mDisplayCutout.getBoundingRectRight());
    }

    public Rect getNotchRectTop() {
        return transfromRectAtoZ(this.mDisplayCutout.getBoundingRectTop());
    }

    public List<Rect> getNotchRects() {
        List<android.graphics.Rect> boundingRects = this.mDisplayCutout.getBoundingRects();
        ArrayList arrayList = new ArrayList();
        for (android.graphics.Rect rect : boundingRects) {
            arrayList.add(transfromRectAtoZ(rect));
        }
        return arrayList;
    }

    public int getPaddingBottom() {
        return this.mDisplayCutout.getSafeInsetBottom();
    }

    public int getPaddingLeft() {
        return this.mDisplayCutout.getSafeInsetLeft();
    }

    public int getPaddingRight() {
        return this.mDisplayCutout.getSafeInsetRight();
    }

    public int getPaddingTop() {
        return this.mDisplayCutout.getSafeInsetTop();
    }

    public String toString() {
        return this.mDisplayCutout.toString();
    }

    public boolean equals(Object obj) {
        return this.mDisplayCutout.equals(obj);
    }

    public int hashCode() {
        return this.mDisplayCutout.hashCode();
    }

    private Rect transfromRectAtoZ(android.graphics.Rect rect) {
        return new Rect(rect.left, rect.top, rect.right, rect.bottom);
    }

    private android.graphics.Rect transfromRectZtoA(Rect rect) {
        return new android.graphics.Rect(rect.left, rect.top, rect.right, rect.bottom);
    }

    private Insets transfromInsetsZtoA(ohos.agp.render.Insets insets) {
        return Insets.of(insets.leftValue, insets.topValue, insets.rightValue, insets.bottomValue);
    }
}
