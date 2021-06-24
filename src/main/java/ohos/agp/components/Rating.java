package ohos.agp.components;

import java.util.function.Predicate;
import ohos.agp.components.element.Element;
import ohos.agp.styles.Style;
import ohos.app.Context;

public class Rating extends AbsSlider {
    public static final int RATING_MAX_ITEMS = 255;
    private Element mFilled;
    private Element mHalfFilled;
    private RatingChangedListener mRatingChangeListener;
    private Element mUnfilled;

    public interface RatingChangedListener {
        void onProgressChanged(Rating rating, int i, boolean z);

        void onStartTrackingTouch(Rating rating);

        void onStopTrackingTouch(Rating rating);
    }

    private native float nativeGetGrainSize(long j);

    private native float nativeGetRating(long j);

    private native long nativeGetRatingHandle();

    private native int nativeGetRatingItems(long j);

    private native boolean nativeIsIndicator(long j);

    private native void nativeSetFilledElement(long j, long j2);

    private native void nativeSetGrainSize(long j, float f);

    private native void nativeSetHalfFilledElement(long j, long j2);

    private native void nativeSetIsIndicator(long j, boolean z);

    private native void nativeSetRatingChangesListener(long j, RatingChangedListener ratingChangedListener);

    private native void nativeSetRatingItems(long j, int i);

    private native void nativeSetScore(long j, float f);

    private native void nativeSetUnfilledElement(long j, long j2);

    public Rating(Context context) {
        this(context, null);
    }

    public Rating(Context context, AttrSet attrSet) {
        this(context, attrSet, "RatingDefaultStyle");
    }

    public Rating(Context context, AttrSet attrSet, String str) {
        super(context, attrSet, str);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.AbsSeekBar, ohos.agp.components.ProgressBar, ohos.agp.components.Component
    public Style convertAttrToStyle(AttrSet attrSet) {
        if (this.mAttrsConstants == null) {
            this.mAttrsConstants = AttrHelper.getRatingAttrsConstants();
        }
        return super.convertAttrToStyle(attrSet);
    }

    public float getGrainSize() {
        return nativeGetGrainSize(this.mNativeViewPtr);
    }

    public /* synthetic */ boolean lambda$setGrainSize$0$Rating(Float f) {
        return f.floatValue() > 0.0f && f.floatValue() < ((float) getRatingItems());
    }

    public void setGrainSize(float f) {
        validateParam(Float.valueOf(f), new Predicate() {
            /* class ohos.agp.components.$$Lambda$Rating$CQcAv3Rq5R1GDtm8DyRBfpdiC5g */

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Rating.this.lambda$setGrainSize$0$Rating((Float) obj);
            }
        }, "Grain must be positive and less than number of rating items");
        nativeSetGrainSize(this.mNativeViewPtr, f);
    }

    static /* synthetic */ boolean lambda$setScore$1(Float f) {
        return f.floatValue() >= 0.0f;
    }

    public void setScore(float f) {
        validateParam(Float.valueOf(f), $$Lambda$Rating$rh0Xcl12KoXVdh0shQQ_JrZDejc.INSTANCE, "Score must be non negative");
        nativeSetScore(this.mNativeViewPtr, f);
    }

    public float getScore() {
        return nativeGetRating(this.mNativeViewPtr);
    }

    public void setIsOperable(boolean z) {
        nativeSetIsIndicator(this.mNativeViewPtr, z);
    }

    public boolean isOperable() {
        return nativeIsIndicator(this.mNativeViewPtr);
    }

    static /* synthetic */ boolean lambda$setRatingItems$2(Integer num) {
        return num.intValue() >= 0 && num.intValue() <= 255;
    }

    public void setRatingItems(int i) {
        validateParam(Integer.valueOf(i), $$Lambda$Rating$fn2d83906PWMtZM3JZsxBK2Ipzg.INSTANCE, "The number must be positive and less than upper limit RATING_MAX_ITEMS");
        nativeSetRatingItems(this.mNativeViewPtr, i);
    }

    public int getRatingItems() {
        return nativeGetRatingItems(this.mNativeViewPtr);
    }

    public void setRatingChangedListener(RatingChangedListener ratingChangedListener) {
        this.mRatingChangeListener = ratingChangedListener;
        nativeSetRatingChangesListener(this.mNativeViewPtr, ratingChangedListener);
    }

    public void setFilledElement(Element element) {
        this.mFilled = element;
        nativeSetFilledElement(this.mNativeViewPtr, element == null ? 0 : element.getNativeElementPtr());
    }

    public Element getFilledElement() {
        return this.mFilled;
    }

    public void setUnfilledElement(Element element) {
        this.mUnfilled = element;
        nativeSetUnfilledElement(this.mNativeViewPtr, element == null ? 0 : element.getNativeElementPtr());
    }

    public Element getHalfFilledElement() {
        return this.mHalfFilled;
    }

    public void setHalfFilledElement(Element element) {
        this.mHalfFilled = element;
        nativeSetHalfFilledElement(this.mNativeViewPtr, element == null ? 0 : element.getNativeElementPtr());
    }

    public Element getUnfilledElement() {
        return this.mUnfilled;
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.ProgressBar, ohos.agp.components.Component
    public void createNativePtr() {
        if (this.mNativeViewPtr == 0) {
            this.mNativeViewPtr = nativeGetRatingHandle();
        }
    }
}
