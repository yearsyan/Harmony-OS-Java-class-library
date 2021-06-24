package ohos.agp.components;

import ohos.agp.components.Component;
import ohos.agp.database.Publisher;
import ohos.agp.styles.Style;
import ohos.agp.utils.ErrorHandler;
import ohos.app.Context;

public class PageSlider extends StackLayout {
    public static final int DEFAULT_CACHED_PAGES_LIMIT = 1;
    public static final int INVALID_INDEX = -1;
    public static final float ROTATION_SENSITIVITY_DEFAULT = 1.4f;
    public static final float ROTATION_SENSITIVITY_HIGH = 1.4f;
    public static final float ROTATION_SENSITIVITY_LOW = 1.0f;
    public static final int SLIDING_STATE_DRAGGING = 1;
    public static final int SLIDING_STATE_IDLE = 0;
    public static final int SLIDING_STATE_SETTLING = 2;
    private int mPageCacheSize;
    private final PageChangePublisher mPageChangePublisher;
    private DirectionalLayout mPageHolder;
    private PageSliderProvider mPageSliderProvider;

    public interface PageChangedListener {
        void onPageChosen(int i);

        void onPageSlideStateChanged(int i);

        void onPageSliding(int i, float f, int i2);
    }

    private native int nativeGetCurrentItem(long j);

    private native int nativeGetCurrentScrollState(long j);

    private native long nativeGetHandle();

    private native int nativeGetOffscreenPageLimit(long j);

    private native int nativeGetOrientation(long j);

    private native int nativeGetPageMargin(long j);

    private native int nativeGetPageSwitchTime(long j);

    private native boolean nativeGetReboundEffect(long j);

    private native void nativeGetReboundEffectParams(long j, ReboundEffectParams reboundEffectParams);

    private native boolean nativeGetSlidingEnabled(long j);

    private native boolean nativeIsCircularModeEnabled(long j);

    private native void nativeSetAdapter(long j, PageSliderProvider pageSliderProvider);

    private native void nativeSetCircularModeEnabled(long j, boolean z);

    private native void nativeSetCurrentItem(long j, int i, boolean z);

    private native void nativeSetOrientation(long j, int i);

    private native void nativeSetPageCacheSize(long j, int i);

    private native void nativeSetPageMargin(long j, int i);

    private native void nativeSetPageSwitchTime(long j, int i);

    private native void nativeSetReboundEffect(long j, boolean z);

    private native void nativeSetReboundEffectParams(long j, int i, float f, int i2);

    private native void nativeSetSlidingEnabled(long j, boolean z);

    public PageSlider(Context context) {
        this(context, null);
    }

    public PageSlider(Context context, AttrSet attrSet) {
        this(context, attrSet, "PageSliderDefaultStyle");
    }

    public PageSlider(Context context, AttrSet attrSet, String str) {
        super(context, attrSet, str);
        this.mPageChangePublisher = new PageChangePublisher();
        this.mPageHolder = new DirectionalLayout(context);
        this.mPageHolder.setLayoutDirection(Component.LayoutDirection.INHERIT);
        int nativeGetOffscreenPageLimit = nativeGetOffscreenPageLimit(this.mNativeViewPtr);
        int i = nativeGetOffscreenPageLimit + 1 + nativeGetOffscreenPageLimit;
        for (int i2 = 0; i2 < i; i2++) {
            StackLayout stackLayout = new StackLayout(context);
            stackLayout.setLayoutDirection(Component.LayoutDirection.INHERIT);
            this.mPageHolder.addComponent(stackLayout);
        }
        this.mPageCacheSize = nativeGetOffscreenPageLimit;
        addComponent(this.mPageHolder);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Component, ohos.agp.components.StackLayout
    public Style convertAttrToStyle(AttrSet attrSet) {
        if (this.mAttrsConstants == null) {
            this.mAttrsConstants = AttrHelper.getPageSliderAttrsConstants();
        }
        return super.convertAttrToStyle(attrSet);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.ComponentContainer, ohos.agp.components.Component, ohos.agp.components.StackLayout
    public void createNativePtr() {
        if (this.mNativeViewPtr == 0) {
            this.mNativeViewPtr = nativeGetHandle();
        }
    }

    public void setProvider(PageSliderProvider pageSliderProvider) {
        nativeSetAdapter(this.mNativeViewPtr, pageSliderProvider);
        this.mPageSliderProvider = pageSliderProvider;
    }

    public PageSliderProvider getProvider() {
        return this.mPageSliderProvider;
    }

    public int getCurrentPage() {
        return nativeGetCurrentItem(this.mNativeViewPtr);
    }

    public void addPageChangedListener(PageChangedListener pageChangedListener) {
        ErrorHandler.validateParamNotNull(pageChangedListener);
        this.mPageChangePublisher.registerSubscriber(pageChangedListener);
    }

    public void removePageChangedListener(PageChangedListener pageChangedListener) {
        ErrorHandler.validateParamNotNull(pageChangedListener);
        this.mPageChangePublisher.unregisterSubscriber(pageChangedListener);
    }

    public int getCurrentSlidingState() {
        return nativeGetCurrentScrollState(this.mNativeViewPtr);
    }

    static /* synthetic */ boolean lambda$setOrientation$0(Integer num) {
        return num.intValue() == 0 || num.intValue() == 1;
    }

    public void setOrientation(int i) {
        validateParam(Integer.valueOf(i), $$Lambda$PageSlider$ecjfeqnE_rvk0P_Ja87SXEW5Jo4.INSTANCE, "orientation value must be HORIZONTAL or VERTICAL");
        nativeSetOrientation(this.mNativeViewPtr, i);
    }

    public int getOrientation() {
        return nativeGetOrientation(this.mNativeViewPtr);
    }

    public void setPageMargin(int i) {
        nativeSetPageMargin(this.mNativeViewPtr, i);
    }

    public int getPageMargin() {
        return nativeGetPageMargin(this.mNativeViewPtr);
    }

    static /* synthetic */ boolean lambda$setPageCacheSize$1(Integer num) {
        return num.intValue() > 0;
    }

    public void setPageCacheSize(int i) {
        validateParam(Integer.valueOf(i), $$Lambda$PageSlider$ZCCYIb5JDuD2_GYUzT4PXrDaAU.INSTANCE, "count must be greater than 0");
        if (i != this.mPageCacheSize) {
            int i2 = i + 1 + i;
            this.mPageHolder.removeAllComponents();
            for (int i3 = 0; i3 < i2; i3++) {
                StackLayout stackLayout = new StackLayout(this.mContext);
                stackLayout.setLayoutDirection(Component.LayoutDirection.INHERIT);
                this.mPageHolder.addComponent(stackLayout);
            }
            this.mPageCacheSize = i;
            nativeSetPageCacheSize(this.mNativeViewPtr, i);
        }
    }

    public int getCachedPagesLimit() {
        return nativeGetOffscreenPageLimit(this.mNativeViewPtr);
    }

    static /* synthetic */ boolean lambda$setCurrentPage$2(Integer num) {
        return num.intValue() >= 0;
    }

    public void setCurrentPage(int i) {
        validateParam(Integer.valueOf(i), $$Lambda$PageSlider$seTho3HWXGQecYRXHIIK8ehwvEc.INSTANCE, "itemPos must be greater than or equal to 0");
        nativeSetCurrentItem(this.mNativeViewPtr, i, true);
    }

    static /* synthetic */ boolean lambda$setCurrentPage$3(Integer num) {
        return num.intValue() >= 0;
    }

    public void setCurrentPage(int i, boolean z) {
        validateParam(Integer.valueOf(i), $$Lambda$PageSlider$Zo__HoLVroEY9fkwjgF7Bn_T6U.INSTANCE, "itemPos must be greater than or equal to 0");
        nativeSetCurrentItem(this.mNativeViewPtr, i, z);
    }

    public void setSlidingPossible(boolean z) {
        nativeSetSlidingEnabled(this.mNativeViewPtr, z);
    }

    public boolean getSlidingPossible() {
        return nativeGetSlidingEnabled(this.mNativeViewPtr);
    }

    public void setCircularModeEnabled(boolean z) {
        nativeSetCircularModeEnabled(this.mNativeViewPtr, z);
    }

    public boolean isCircularModeEnabled() {
        return nativeIsCircularModeEnabled(this.mNativeViewPtr);
    }

    public void setReboundEffect(boolean z) {
        nativeSetReboundEffect(this.mNativeViewPtr, z);
    }

    public boolean getReboundEffect() {
        return nativeGetReboundEffect(this.mNativeViewPtr);
    }

    public void setReboundEffectParams(int i, float f, int i2) {
        validateReboundEffectParams(i, f, i2);
        nativeSetReboundEffectParams(this.mNativeViewPtr, i, f, i2);
    }

    public void setReboundEffectParams(ReboundEffectParams reboundEffectParams) {
        ErrorHandler.validateParamNotNull(reboundEffectParams);
        setReboundEffectParams(reboundEffectParams.overscrollPercent, reboundEffectParams.overscrollRate, reboundEffectParams.remainVisiblePercent);
    }

    public ReboundEffectParams getReboundEffectParams() {
        ReboundEffectParams reboundEffectParams = new ReboundEffectParams(0, 0.0f, 0);
        nativeGetReboundEffectParams(this.mNativeViewPtr, reboundEffectParams);
        return reboundEffectParams;
    }

    public static class ReboundEffectParams {
        private int overscrollPercent;
        private float overscrollRate;
        private int remainVisiblePercent;

        public ReboundEffectParams(int i, float f, int i2) {
            this.overscrollPercent = i;
            this.overscrollRate = f;
            this.remainVisiblePercent = i2;
        }

        public int getOverscrollPercent() {
            return this.overscrollPercent;
        }

        public float getOverscrollRate() {
            return this.overscrollRate;
        }

        public int getRemainVisiblePercent() {
            return this.remainVisiblePercent;
        }

        public void setOverscrollPercent(int i) {
            this.overscrollPercent = i;
        }

        public void setOverscrollRate(float f) {
            this.overscrollRate = f;
        }

        public void setRemainVisiblePercent(int i) {
            this.remainVisiblePercent = i;
        }
    }

    public void setPageSwitchTime(int i) {
        nativeSetPageSwitchTime(this.mNativeViewPtr, i);
    }

    public int getPageSwitchTime() {
        return nativeGetPageSwitchTime(this.mNativeViewPtr);
    }

    /* access modifiers changed from: package-private */
    public static class PageChangePublisher extends Publisher<PageChangedListener> {
        PageChangePublisher() {
        }

        /* access modifiers changed from: package-private */
        public void notifyPageScrolled(int i, float f, int i2) {
            for (int size = this.mSubscribers.size() - 1; size >= 0; size--) {
                ((PageChangedListener) this.mSubscribers.get(size)).onPageSliding(i, f, i2);
            }
        }

        /* access modifiers changed from: package-private */
        public void notifyPageScrollStateChanged(int i) {
            for (int size = this.mSubscribers.size() - 1; size >= 0; size--) {
                ((PageChangedListener) this.mSubscribers.get(size)).onPageSlideStateChanged(i);
            }
        }

        /* access modifiers changed from: package-private */
        public void notifyPageSelected(int i) {
            for (int size = this.mSubscribers.size() - 1; size >= 0; size--) {
                ((PageChangedListener) this.mSubscribers.get(size)).onPageChosen(i);
            }
        }
    }

    static /* synthetic */ boolean lambda$validateReboundEffectParams$4(Integer num) {
        return num.intValue() >= 0 && num.intValue() <= 100;
    }

    private void validateReboundEffectParams(int i, float f, int i2) {
        validateParam(Integer.valueOf(i), $$Lambda$PageSlider$vd43LKLNrC2eM12xS5rwqTXJHCc.INSTANCE, "overscrollPercent must be in range [0, 100]");
        validateParam(Float.valueOf(f), $$Lambda$PageSlider$CMLeiOh4iU1CWuIKytZc3GbdEk.INSTANCE, "overscrollRate must be greater than 0");
        validateParam(Integer.valueOf(i2), $$Lambda$PageSlider$DkxmjJAaGGGL0059ehqV8XIAy7M.INSTANCE, "remainVisiblePercent must be in range [0, 100]");
    }

    static /* synthetic */ boolean lambda$validateReboundEffectParams$5(Float f) {
        return f.floatValue() > 0.0f;
    }

    static /* synthetic */ boolean lambda$validateReboundEffectParams$6(Integer num) {
        return num.intValue() >= 0 && num.intValue() <= 100;
    }

    private void notifyPageScrolled(int i, float f, int i2) {
        this.mPageChangePublisher.notifyPageScrolled(i, f, i2);
    }

    private void notifyPageScrollStateChanged(int i) {
        this.mPageChangePublisher.notifyPageScrollStateChanged(i);
    }

    private void notifyPageSelected(int i) {
        this.mPageChangePublisher.notifyPageSelected(i);
    }
}
