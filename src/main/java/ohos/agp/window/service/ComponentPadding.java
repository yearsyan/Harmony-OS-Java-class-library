package ohos.agp.window.service;

import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.utils.Rect;
import ohos.agp.window.view.DisplayCutoutWrapper;
import ohos.agp.window.view.WindowInsetsWrapper;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class ComponentPadding {
    private static final HiLogLabel LABEL = new HiLogLabel(3, LogDomain.END, "ComponentPadding");
    private WindowInsetsWrapper mWindowInsetsWrapper;

    public ComponentPadding() {
    }

    public ComponentPadding(WindowInsetsWrapper windowInsetsWrapper) {
        this.mWindowInsetsWrapper = windowInsetsWrapper;
    }

    public Rect getNotSettableGestureRect() {
        checkInsetsWrapper();
        return this.mWindowInsetsWrapper.getNotSettableGestureRect();
    }

    public Rect getGestureRect() {
        checkInsetsWrapper();
        return this.mWindowInsetsWrapper.getGestureRect();
    }

    public ComponentPadding useDisplayNotch() {
        checkInsetsWrapper();
        return new ComponentPadding(this.mWindowInsetsWrapper.useDisplayNotch());
    }

    public ComponentPadding useUnchangedPadding() {
        checkInsetsWrapper();
        return new ComponentPadding(this.mWindowInsetsWrapper.useUnchangedPadding());
    }

    public ComponentPadding useSystemComponentPadding() {
        checkInsetsWrapper();
        return new ComponentPadding(this.mWindowInsetsWrapper.useSystemComponentPadding());
    }

    public DisplayNotch getDisplayNotch() {
        checkInsetsWrapper();
        DisplayCutoutWrapper displayNotch = this.mWindowInsetsWrapper.getDisplayNotch();
        if (displayNotch == null) {
            return null;
        }
        return new DisplayNotch(displayNotch);
    }

    public int getUnchangedPaddingBottom() {
        checkInsetsWrapper();
        return this.mWindowInsetsWrapper.getUnchangedPaddingBottom();
    }

    public int getUnchangedPaddingLeft() {
        checkInsetsWrapper();
        return this.mWindowInsetsWrapper.getUnchangedPaddingLeft();
    }

    public int getUnchangedPaddingRight() {
        checkInsetsWrapper();
        return this.mWindowInsetsWrapper.getUnchangedPaddingRight();
    }

    public int getUnchangedPaddingTop() {
        checkInsetsWrapper();
        return this.mWindowInsetsWrapper.getUnchangedPaddingTop();
    }

    public int getSystemComponentPaddingBottom() {
        checkInsetsWrapper();
        return this.mWindowInsetsWrapper.getSystemComponentPaddingBottom();
    }

    public int getSystemComponentPaddingLeft() {
        checkInsetsWrapper();
        return this.mWindowInsetsWrapper.getSystemComponentPaddingLeft();
    }

    public int getSystemComponentPaddingRight() {
        checkInsetsWrapper();
        return this.mWindowInsetsWrapper.getSystemComponentPaddingRight();
    }

    public int getSystemComponentPaddingTop() {
        checkInsetsWrapper();
        return this.mWindowInsetsWrapper.getSystemComponentPaddingTop();
    }

    public boolean hasNonZeroPadding() {
        checkInsetsWrapper();
        return this.mWindowInsetsWrapper.hasNonZeroPadding();
    }

    public boolean hasUnchangedPadding() {
        checkInsetsWrapper();
        return this.mWindowInsetsWrapper.hasUnchangedPadding();
    }

    public boolean hasSystemComponentPadding() {
        checkInsetsWrapper();
        return this.mWindowInsetsWrapper.hasSystemComponentPadding();
    }

    public boolean isPaddingFullyUsed() {
        checkInsetsWrapper();
        return this.mWindowInsetsWrapper.isPaddingFullyUsed();
    }

    public boolean isRoundCorner() {
        checkInsetsWrapper();
        return this.mWindowInsetsWrapper.isRoundCorner();
    }

    private void checkInsetsWrapper() {
        if (this.mWindowInsetsWrapper == null) {
            HiLog.error(LABEL, "mWindowInsetsWrapper is null", new Object[0]);
            throw new WindowInsetsWrapper.ParameterInvalidException("reason: mWindowInsetsWrapper is null");
        }
    }
}
