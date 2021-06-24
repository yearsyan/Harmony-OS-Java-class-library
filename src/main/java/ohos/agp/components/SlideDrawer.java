package ohos.agp.components;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.SlideDrawer;
import ohos.agp.styles.Style;
import ohos.agp.styles.attributes.SlideDrawerAttrsConstants;
import ohos.agp.utils.ErrorHandler;
import ohos.app.Context;

public class SlideDrawer extends ComponentContainer {
    public static final DisplayMode DEFAULT_DISPLAY_MODE = DisplayMode.WITH_ANIMATION;
    public static final float DEFAULT_OPEN_THRESHOLD = 0.25f;
    private static final int UNSUPPORTED_DIRECTION = -1;
    private List<SlideDisable> mSlideDisables;
    private List<SlideListener> mSlideListeners;

    public enum DisplayMode {
        NO_ANIMATION,
        WITH_ANIMATION
    }

    public enum DrawerState {
        OPEN,
        MIDDLE,
        CLOSE
    }

    public enum SlideDirection {
        START,
        TOP,
        END,
        BOTTOM
    }

    public interface SlideDisable {
        boolean shouldDisableSlide(SlideDrawer slideDrawer, SlideDirection slideDirection, DragInfo dragInfo);
    }

    public interface SlideListener {
        void onClose(SlideDrawer slideDrawer, SlideDirection slideDirection);

        void onMiddle(SlideDrawer slideDrawer, SlideDirection slideDirection, DrawerState drawerState);

        void onOpen(SlideDrawer slideDrawer, SlideDirection slideDirection);

        void onSlideChange(SlideDrawer slideDrawer, SlideDirection slideDirection, int i, int i2);
    }

    private native boolean nativeClose(long j, int i);

    private native boolean nativeCloseSmoothly(long j, int i);

    private native boolean nativeCloseSmoothlyInterval(long j, int i, int i2);

    private native int nativeGetDisplayMode(long j, int i);

    private native int[] nativeGetDisplayModeArray(long j);

    private native float nativeGetOpenThreshold(long j);

    private native int nativeGetSlideDirection(long j);

    private native int nativeGetSlideDistance(long j, int i);

    private native float nativeGetSlideDistanceRatio(long j);

    private native long nativeGetSlideDrawerHandle();

    private native int nativeGetSlideStates(long j);

    private native boolean nativeIsSlideEnabled(long j);

    private native boolean nativeIsTouchForClose(long j);

    private native boolean nativeOpen(long j, int i);

    private native boolean nativeOpenSmoothly(long j, int i);

    private native boolean nativeOpenSmoothlyInterval(long j, int i, int i2);

    private native void nativeSetDisplayMode(long j, int i);

    private native void nativeSetDisplayModeAllDirections(long j, int[] iArr);

    private native void nativeSetDisplayModeByDirection(long j, int i, int i2);

    private native void nativeSetMaximumSlideDistance(long j, int i);

    private native void nativeSetOpenThreshold(long j, float f);

    private native void nativeSetSlideEnabled(long j, boolean z);

    private native void nativeSetTouchForClose(long j, boolean z);

    private native void nativeSubscribeSlideDisables(long j);

    private native void nativeSubscribeSlideEvents(long j);

    private native boolean nativeToggle(long j, int i);

    private native boolean nativeToggleSmoothly(long j, int i);

    private native boolean nativeToggleSmoothlyInterval(long j, int i, int i2);

    private native void nativeUnsubscribeSlideDisables(long j);

    private native void nativeUnsubscribeSlideEvents(long j);

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.ComponentContainer, ohos.agp.components.Component
    public void createNativePtr() {
        if (this.mNativeViewPtr == 0) {
            this.mNativeViewPtr = nativeGetSlideDrawerHandle();
        }
    }

    public SlideDrawer(Context context) {
        this(context, null);
    }

    public SlideDrawer(Context context, AttrSet attrSet) {
        this(context, attrSet, "SlideDrawerDefaultStyle");
    }

    public SlideDrawer(Context context, AttrSet attrSet, String str) {
        super(context, attrSet, str);
        this.mSlideListeners = new ArrayList();
        this.mSlideDisables = new ArrayList();
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Component
    public Style convertAttrToStyle(AttrSet attrSet) {
        if (this.mAttrsConstants == null) {
            this.mAttrsConstants = AttrHelper.getSlideDrawerAttrsConstants();
        }
        return super.convertAttrToStyle(attrSet);
    }

    public void addSlideListener(SlideListener slideListener) {
        ErrorHandler.validateParamNotNull(slideListener);
        if (this.mSlideListeners.isEmpty()) {
            nativeSubscribeSlideEvents(this.mNativeViewPtr);
        }
        this.mSlideListeners.add(slideListener);
    }

    public void removeSlideListener(SlideListener slideListener) {
        ErrorHandler.validateParamNotNull(slideListener);
        this.mSlideListeners.remove(slideListener);
        if (this.mSlideListeners.isEmpty()) {
            nativeUnsubscribeSlideEvents(this.mNativeViewPtr);
        }
    }

    public void removeAllSlideListeners() {
        if (!this.mSlideListeners.isEmpty()) {
            this.mSlideListeners.clear();
            nativeUnsubscribeSlideEvents(this.mNativeViewPtr);
        }
    }

    public boolean hasSlideListeners() {
        return !this.mSlideListeners.isEmpty();
    }

    public void addSlideDisable(SlideDisable slideDisable) {
        ErrorHandler.validateParamNotNull(slideDisable);
        if (this.mSlideDisables.isEmpty()) {
            nativeSubscribeSlideDisables(this.mNativeViewPtr);
        }
        this.mSlideDisables.add(slideDisable);
    }

    public void removeSlideDisable(SlideDisable slideDisable) {
        ErrorHandler.validateParamNotNull(slideDisable);
        this.mSlideDisables.remove(slideDisable);
        if (this.mSlideDisables.isEmpty()) {
            nativeUnsubscribeSlideDisables(this.mNativeViewPtr);
        }
    }

    public void removeAllSlideDisables() {
        if (!this.mSlideDisables.isEmpty()) {
            this.mSlideDisables.clear();
            nativeUnsubscribeSlideDisables(this.mNativeViewPtr);
        }
    }

    public boolean hasSlideDisables() {
        return !this.mSlideDisables.isEmpty();
    }

    public void setTouchForClose(boolean z) {
        nativeSetTouchForClose(this.mNativeViewPtr, z);
    }

    public boolean isTouchForClose() {
        return nativeIsTouchForClose(this.mNativeViewPtr);
    }

    public void setSlideEnabled(boolean z) {
        nativeSetSlideEnabled(this.mNativeViewPtr, z);
    }

    public boolean isSlideEnabled() {
        return nativeIsSlideEnabled(this.mNativeViewPtr);
    }

    public void setOpenThreshold(float f) {
        ErrorHandler.validateParamPercent(f);
        nativeSetOpenThreshold(this.mNativeViewPtr, f);
    }

    public float getOpenThreshold() {
        return nativeGetOpenThreshold(this.mNativeViewPtr);
    }

    public float getSlideDistanceRatio() {
        return nativeGetSlideDistanceRatio(this.mNativeViewPtr);
    }

    public void setMaximumSlideDistance(int i) {
        ErrorHandler.validateParamNonNegative((long) i);
        nativeSetMaximumSlideDistance(this.mNativeViewPtr, i);
    }

    public int getSlideDistance(SlideDirection slideDirection) {
        ErrorHandler.validateParamNotNull(slideDirection);
        return nativeGetSlideDistance(this.mNativeViewPtr, slideDirection.ordinal());
    }

    public DrawerState getSlideState() {
        return DrawerState.values()[nativeGetSlideStates(this.mNativeViewPtr)];
    }

    public SlideDirection getSlideDirection() {
        int nativeGetSlideDirection = nativeGetSlideDirection(this.mNativeViewPtr);
        if (nativeGetSlideDirection == -1) {
            return null;
        }
        return SlideDirection.values()[nativeGetSlideDirection];
    }

    public void setDisplayMode(DisplayMode displayMode) {
        ErrorHandler.validateParamNotNull(displayMode);
        nativeSetDisplayMode(this.mNativeViewPtr, displayMode.ordinal());
    }

    public void setDisplayMode(DisplayMode displayMode, SlideDirection slideDirection) {
        ErrorHandler.validateParamNotNull(displayMode);
        ErrorHandler.validateParamNotNull(slideDirection);
        nativeSetDisplayModeByDirection(this.mNativeViewPtr, displayMode.ordinal(), slideDirection.ordinal());
    }

    public void setDisplayMode(DisplayMode displayMode, DisplayMode displayMode2, DisplayMode displayMode3, DisplayMode displayMode4) {
        ErrorHandler.validateParamNotNull(displayMode);
        ErrorHandler.validateParamNotNull(displayMode2);
        ErrorHandler.validateParamNotNull(displayMode3);
        ErrorHandler.validateParamNotNull(displayMode4);
        nativeSetDisplayModeAllDirections(this.mNativeViewPtr, new int[]{displayMode.ordinal(), displayMode2.ordinal(), displayMode3.ordinal(), displayMode4.ordinal()});
    }

    public DisplayMode getDisplayMode(SlideDirection slideDirection) {
        ErrorHandler.validateParamNotNull(slideDirection);
        return DisplayMode.values()[nativeGetDisplayMode(this.mNativeViewPtr, slideDirection.ordinal())];
    }

    public DisplayMode[] getDisplayMode() {
        int[] nativeGetDisplayModeArray = nativeGetDisplayModeArray(this.mNativeViewPtr);
        int i = nativeGetDisplayModeArray[0];
        int i2 = nativeGetDisplayModeArray[1];
        int i3 = nativeGetDisplayModeArray[2];
        int i4 = nativeGetDisplayModeArray[3];
        DisplayMode[] values = DisplayMode.values();
        return new DisplayMode[]{values[i], values[i2], values[i3], values[i4]};
    }

    public boolean open() {
        return nativeOpen(this.mNativeViewPtr, -1);
    }

    public boolean open(SlideDirection slideDirection) {
        return nativeOpen(this.mNativeViewPtr, slideDirection == null ? -1 : slideDirection.ordinal());
    }

    public boolean openSmoothly() {
        return nativeOpenSmoothly(this.mNativeViewPtr, -1);
    }

    public boolean openSmoothly(SlideDirection slideDirection) {
        return nativeOpenSmoothly(this.mNativeViewPtr, slideDirection == null ? -1 : slideDirection.ordinal());
    }

    public boolean openSmoothly(int i) {
        ErrorHandler.validateParamNonNegative((long) i);
        return nativeOpenSmoothlyInterval(this.mNativeViewPtr, -1, i);
    }

    public boolean openSmoothly(SlideDirection slideDirection, int i) {
        int i2;
        ErrorHandler.validateParamNonNegative((long) i);
        long j = this.mNativeViewPtr;
        if (slideDirection == null) {
            i2 = -1;
        } else {
            i2 = slideDirection.ordinal();
        }
        return nativeOpenSmoothlyInterval(j, i2, i);
    }

    public boolean close() {
        return nativeClose(this.mNativeViewPtr, -1);
    }

    public boolean close(SlideDirection slideDirection) {
        return nativeClose(this.mNativeViewPtr, slideDirection == null ? -1 : slideDirection.ordinal());
    }

    public boolean closeSmoothly() {
        return nativeCloseSmoothly(this.mNativeViewPtr, -1);
    }

    public boolean closeSmoothly(SlideDirection slideDirection) {
        int i;
        long j = this.mNativeViewPtr;
        if (slideDirection == null) {
            i = -1;
        } else {
            i = slideDirection.ordinal();
        }
        return nativeCloseSmoothly(j, i);
    }

    public boolean closeSmoothly(int i) {
        ErrorHandler.validateParamNonNegative((long) i);
        return nativeCloseSmoothlyInterval(this.mNativeViewPtr, -1, i);
    }

    public boolean closeSmoothly(SlideDirection slideDirection, int i) {
        int i2;
        ErrorHandler.validateParamNonNegative((long) i);
        long j = this.mNativeViewPtr;
        if (slideDirection == null) {
            i2 = -1;
        } else {
            i2 = slideDirection.ordinal();
        }
        return nativeCloseSmoothlyInterval(j, i2, i);
    }

    public boolean toggle() {
        return nativeToggle(this.mNativeViewPtr, -1);
    }

    public boolean toggle(SlideDirection slideDirection) {
        return nativeToggle(this.mNativeViewPtr, slideDirection == null ? -1 : slideDirection.ordinal());
    }

    public boolean toggleSmoothly() {
        return nativeToggleSmoothly(this.mNativeViewPtr, -1);
    }

    public boolean toggleSmoothly(SlideDirection slideDirection) {
        int i;
        long j = this.mNativeViewPtr;
        if (slideDirection == null) {
            i = -1;
        } else {
            i = slideDirection.ordinal();
        }
        return nativeToggleSmoothly(j, i);
    }

    public boolean toggleSmoothly(int i) {
        ErrorHandler.validateParamNonNegative((long) i);
        return nativeToggleSmoothlyInterval(this.mNativeViewPtr, -1, i);
    }

    public boolean toggleSmoothly(SlideDirection slideDirection, int i) {
        int i2;
        ErrorHandler.validateParamNonNegative((long) i);
        long j = this.mNativeViewPtr;
        if (slideDirection == null) {
            i2 = -1;
        } else {
            i2 = slideDirection.ordinal();
        }
        return nativeToggleSmoothlyInterval(j, i2, i);
    }

    @Override // ohos.agp.components.ComponentParent, ohos.agp.components.ComponentContainer
    public ComponentContainer.LayoutConfig verifyLayoutConfig(ComponentContainer.LayoutConfig layoutConfig) {
        if (layoutConfig instanceof LayoutConfig) {
            return layoutConfig;
        }
        return new LayoutConfig(layoutConfig);
    }

    @Override // ohos.agp.components.ComponentContainer
    public ComponentContainer.LayoutConfig createLayoutConfig(Context context, AttrSet attrSet) {
        return new LayoutConfig(context, attrSet);
    }

    @Override // ohos.agp.components.ComponentParent, ohos.agp.components.ComponentContainer
    public void removeComponents(int i, int i2) {
        int i3;
        if (i < 0 || i >= getChildCount() || i2 < 0 || (i3 = i2 + i) > getChildCount()) {
            throw new IllegalStateException("Incorrect parameters for removeViews.");
        }
        for (i3 = i2 + i; i3 > i; i3--) {
            removeComponentAt(i3 - 1);
        }
    }

    private void onOpenFromNative(int i) {
        SlideDirection slideDirection = SlideDirection.values()[i];
        for (SlideListener slideListener : this.mSlideListeners) {
            slideListener.onOpen(this, slideDirection);
        }
    }

    private void onMiddleFromNative(int i, int i2) {
        SlideDirection slideDirection = SlideDirection.values()[i];
        DrawerState drawerState = DrawerState.values()[i2];
        for (SlideListener slideListener : this.mSlideListeners) {
            slideListener.onMiddle(this, slideDirection, drawerState);
        }
    }

    private void onCloseFromNative(int i) {
        SlideDirection slideDirection = SlideDirection.values()[i];
        for (SlideListener slideListener : this.mSlideListeners) {
            slideListener.onClose(this, slideDirection);
        }
    }

    private void onSlideChangeFromNative(int i, int i2, int i3) {
        SlideDirection slideDirection = SlideDirection.values()[i];
        for (SlideListener slideListener : this.mSlideListeners) {
            slideListener.onSlideChange(this, slideDirection, i2, i3);
        }
    }

    private boolean shouldDisableSlideFromNative(int i, DragInfo dragInfo) {
        for (SlideDisable slideDisable : this.mSlideDisables) {
            if (slideDisable != null && slideDisable.shouldDisableSlide(this, SlideDirection.values()[i], dragInfo)) {
                return true;
            }
        }
        return false;
    }

    public static class LayoutConfig extends ComponentContainer.LayoutConfig {
        private static final int MAIN_COMPONENT = -1;
        public SlideDirection direction = null;

        private native void nativeSetSlideDrawerLayoutConfig(long j, int[] iArr, boolean z);

        public LayoutConfig() {
        }

        public LayoutConfig(Context context, AttrSet attrSet) {
            super(context, attrSet);
            attrSet.getAttr(SlideDrawerAttrsConstants.SlideDrawerConfigAttrsConstants.SLIDE_DIRECTION).ifPresent(new Consumer() {
                /* class ohos.agp.components.$$Lambda$SlideDrawer$LayoutConfig$I7EQbEtKxnbVsk99QRz7re70tM0 */

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    SlideDrawer.LayoutConfig.this.lambda$new$0$SlideDrawer$LayoutConfig((Attr) obj);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$SlideDrawer$LayoutConfig(Attr attr) {
            int integerValue = attr.getIntegerValue();
            if (integerValue >= 0 && integerValue < SlideDirection.values().length) {
                this.direction = SlideDirection.values()[integerValue];
            }
        }

        public LayoutConfig(int i, int i2) {
            super(i, i2);
        }

        public LayoutConfig(int i, int i2, SlideDirection slideDirection) {
            super(i, i2);
            this.direction = slideDirection;
        }

        public LayoutConfig(LayoutConfig layoutConfig) {
            super(layoutConfig);
            this.direction = layoutConfig.direction;
        }

        public LayoutConfig(ComponentContainer.LayoutConfig layoutConfig) {
            super(layoutConfig);
        }

        @Override // ohos.agp.components.ComponentContainer.LayoutConfig
        public void applyToComponent(Component component) {
            int[] iArr = new int[7];
            iArr[0] = this.width;
            iArr[1] = this.height;
            iArr[2] = isMarginsRelative() ? getHorizontalStartMargin() : getMarginLeft();
            iArr[3] = getMarginTop();
            iArr[4] = isMarginsRelative() ? getHorizontalEndMargin() : getMarginRight();
            iArr[5] = getMarginBottom();
            SlideDirection slideDirection = this.direction;
            iArr[6] = slideDirection == null ? -1 : slideDirection.ordinal();
            nativeSetSlideDrawerLayoutConfig(component.getNativeViewPtr(), iArr, isMarginsRelative());
        }
    }
}
