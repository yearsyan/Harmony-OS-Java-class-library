package ohos.agp.window.service;

import java.util.List;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.utils.Rect;
import ohos.agp.window.view.DisplayCutoutWrapper;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class DisplayNotch {
    private static final HiLogLabel LABEL = new HiLogLabel(3, LogDomain.END, "DisplayNotch");
    private DisplayCutoutWrapper mDisplayCutoutWrapper = null;

    public DisplayNotch() {
    }

    public DisplayNotch(DisplayCutoutWrapper displayCutoutWrapper) {
        this.mDisplayCutoutWrapper = displayCutoutWrapper;
    }

    public Rect getNotchRectBottom() {
        checkDisplayCutoutWrapper();
        return this.mDisplayCutoutWrapper.getNotchRectBottom();
    }

    public Rect getNotchRectLeft() {
        checkDisplayCutoutWrapper();
        return this.mDisplayCutoutWrapper.getNotchRectLeft();
    }

    public Rect getNotchRectRight() {
        checkDisplayCutoutWrapper();
        return this.mDisplayCutoutWrapper.getNotchRectRight();
    }

    public Rect getNotchRectTop() {
        checkDisplayCutoutWrapper();
        return this.mDisplayCutoutWrapper.getNotchRectTop();
    }

    public List<Rect> getNotchRects() {
        checkDisplayCutoutWrapper();
        return this.mDisplayCutoutWrapper.getNotchRects();
    }

    public int getPaddingBottom() {
        checkDisplayCutoutWrapper();
        return this.mDisplayCutoutWrapper.getPaddingBottom();
    }

    public int getPaddingLeft() {
        checkDisplayCutoutWrapper();
        return this.mDisplayCutoutWrapper.getPaddingLeft();
    }

    public int getPaddingRight() {
        checkDisplayCutoutWrapper();
        return this.mDisplayCutoutWrapper.getPaddingRight();
    }

    public int getPaddingTop() {
        checkDisplayCutoutWrapper();
        return this.mDisplayCutoutWrapper.getPaddingTop();
    }

    public String toString() {
        checkDisplayCutoutWrapper();
        return this.mDisplayCutoutWrapper.toString();
    }

    public boolean equals(Object obj) {
        checkDisplayCutoutWrapper();
        return this.mDisplayCutoutWrapper.equals(obj);
    }

    public int hashCode() {
        checkDisplayCutoutWrapper();
        return this.mDisplayCutoutWrapper.hashCode();
    }

    private void checkDisplayCutoutWrapper() {
        if (this.mDisplayCutoutWrapper == null) {
            HiLog.error(LABEL, "mDisplayCutoutWrapper is null", new Object[0]);
            throw new DisplayCutoutWrapper.ParameterInvalidException("reason: mDisplayCutoutWrapper is null");
        }
    }
}
