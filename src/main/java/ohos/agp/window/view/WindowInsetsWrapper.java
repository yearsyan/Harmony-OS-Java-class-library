package ohos.agp.window.view;

import android.graphics.Insets;
import android.view.DisplayCutout;
import android.view.WindowInsets;
import ohos.agp.utils.Rect;

public final class WindowInsetsWrapper {
    private WindowInsets mWindowInsets;

    public static class ParameterInvalidException extends RuntimeException {
        private static final long serialVersionUID = 5459801466633442242L;

        public ParameterInvalidException() {
            this(null);
        }

        public ParameterInvalidException(String str) {
            super(str);
        }
    }

    public WindowInsetsWrapper(WindowInsets windowInsets) {
        if (windowInsets != null) {
            this.mWindowInsets = windowInsets;
            return;
        }
        throw new ParameterInvalidException("reason: windowInsets is null");
    }

    public Rect getNotSettableGestureRect() {
        Insets mandatorySystemGestureInsets = this.mWindowInsets.getMandatorySystemGestureInsets();
        return new Rect(mandatorySystemGestureInsets.left, mandatorySystemGestureInsets.top, mandatorySystemGestureInsets.right, mandatorySystemGestureInsets.bottom);
    }

    public Rect getGestureRect() {
        Insets systemGestureInsets = this.mWindowInsets.getSystemGestureInsets();
        return new Rect(systemGestureInsets.left, systemGestureInsets.top, systemGestureInsets.right, systemGestureInsets.bottom);
    }

    public WindowInsetsWrapper useDisplayNotch() {
        return new WindowInsetsWrapper(this.mWindowInsets.consumeDisplayCutout());
    }

    public WindowInsetsWrapper useUnchangedPadding() {
        return new WindowInsetsWrapper(this.mWindowInsets.consumeStableInsets());
    }

    public WindowInsetsWrapper useSystemComponentPadding() {
        return new WindowInsetsWrapper(this.mWindowInsets.consumeSystemWindowInsets());
    }

    public DisplayCutoutWrapper getDisplayNotch() {
        DisplayCutout displayCutout = this.mWindowInsets.getDisplayCutout();
        if (displayCutout == null) {
            return null;
        }
        return new DisplayCutoutWrapper(displayCutout);
    }

    public int getUnchangedPaddingBottom() {
        return this.mWindowInsets.getStableInsetBottom();
    }

    public int getUnchangedPaddingLeft() {
        return this.mWindowInsets.getStableInsetLeft();
    }

    public int getUnchangedPaddingRight() {
        return this.mWindowInsets.getStableInsetRight();
    }

    public int getUnchangedPaddingTop() {
        return this.mWindowInsets.getStableInsetTop();
    }

    public int getSystemComponentPaddingBottom() {
        return this.mWindowInsets.getSystemWindowInsetBottom();
    }

    public int getSystemComponentPaddingLeft() {
        return this.mWindowInsets.getSystemWindowInsetLeft();
    }

    public int getSystemComponentPaddingRight() {
        return this.mWindowInsets.getSystemWindowInsetRight();
    }

    public int getSystemComponentPaddingTop() {
        return this.mWindowInsets.getSystemWindowInsetTop();
    }

    public boolean hasNonZeroPadding() {
        return this.mWindowInsets.hasInsets();
    }

    public boolean hasUnchangedPadding() {
        return this.mWindowInsets.hasStableInsets();
    }

    public boolean hasSystemComponentPadding() {
        return this.mWindowInsets.hasSystemWindowInsets();
    }

    public boolean isPaddingFullyUsed() {
        return this.mWindowInsets.isConsumed();
    }

    public boolean isRoundCorner() {
        return this.mWindowInsets.isRound();
    }

    private ohos.agp.render.Insets transformInset(Insets insets) {
        return new ohos.agp.render.Insets(insets.left, insets.top, insets.right, insets.bottom);
    }
}
