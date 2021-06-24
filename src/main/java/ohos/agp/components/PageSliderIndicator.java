package ohos.agp.components;

import java.util.function.Consumer;
import ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator;
import ohos.agp.components.Component;
import ohos.agp.components.PageSlider;
import ohos.agp.components.element.Element;
import ohos.agp.database.Publisher;
import ohos.agp.styles.Style;
import ohos.agp.styles.attributes.PageSliderIndicatorAttrsConstants;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;
import ohos.system.Parameters;

public final class PageSliderIndicator extends Component {
    private static final int DOMAIN_ID = 218108928;
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 218108928, "PageSliderIndicator");
    private static final int NORMAL_INDEX = 0;
    private static final int SELECTED_INDEX = 1;
    private static final int SUPPORT_GRAVITATIONAL_ANIMATION = 1;
    private static final int TIME_MS_ONE_FRAME = 16;
    private Color mBackgroundEndColor;
    private Color mBackgroundStartColor;
    private Context mContext;
    private int mCountListener;
    private final Element[] mElements;
    private int mGravitationalAnimationConfig;
    private boolean mGravitationalAnimationEnable;
    private GravitationalPagerIndicatorAnimator mIndicatorAnimator;
    private GravitationalAnimationPageChangedListener mPageChangedListener;
    private PageSlider mPageSlider;
    private Color mSelectedDotColor;
    private final IndicatorSelectionHandler mSelectionHandler;
    private Color mUnselectedDotColor;

    private native int nativeGetCount(long j);

    private native long nativeGetHandle();

    private native int nativeGetItemOffset(long j);

    private native int nativeGetSelected(long j);

    private native void nativeSetAnimationEnable(long j, boolean z);

    private native void nativeSetContentWidthHeight(long j, int i, int i2);

    private native void nativeSetItemElements(long j, long[] jArr);

    private native void nativeSetItemOffset(long j, int i);

    private native void nativeSetPageSlider(long j, long j2);

    private native void nativeSetSelected(long j, int i);

    /* access modifiers changed from: private */
    public class GravitationalAnimationPageChangedListener implements PageSlider.PageChangedListener {
        private GravitationalAnimationPageChangedListener() {
        }

        @Override // ohos.agp.components.PageSlider.PageChangedListener
        public void onPageSliding(int i, float f, int i2) {
            HiLog.info(PageSliderIndicator.LABEL_LOG, "indicator PageChangedListener itemPos = %{public}d,itemPosOffset = %{public}f, itemPosOffsetPixels = %{public}d", Integer.valueOf(i), Float.valueOf(f), Integer.valueOf(i2));
            PageSliderIndicator.this.mIndicatorAnimator.setUpdateValue(i, f, i2, PageSliderIndicator.this.mPageSlider.getWidth());
        }

        @Override // ohos.agp.components.PageSlider.PageChangedListener
        public void onPageSlideStateChanged(int i) {
            HiLog.info(PageSliderIndicator.LABEL_LOG, "indicator PageChangedListener state = %{public}d", Integer.valueOf(i));
            PageSliderIndicator.this.mIndicatorAnimator.setPageSlideState(i);
        }

        @Override // ohos.agp.components.PageSlider.PageChangedListener
        public void onPageChosen(int i) {
            HiLog.info(PageSliderIndicator.LABEL_LOG, "indicator PageChangedListener itemPos = %{public}d", Integer.valueOf(i));
            PageSliderIndicator.this.mIndicatorAnimator.autoSlider(i, PageSliderIndicator.this.getSelected());
        }
    }

    /* access modifiers changed from: private */
    public class GravitationalAnimationTouchListener implements Component.TouchEventListener {
        private GravitationalAnimationTouchListener() {
        }

        @Override // ohos.agp.components.Component.TouchEventListener
        public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
            PageSliderIndicator.this.mIndicatorAnimator.handleTouch(touchEvent);
            return true;
        }
    }

    protected static class IndicatorSelectionHandler extends Publisher<PageSlider.PageChangedListener> {
        protected IndicatorSelectionHandler() {
        }

        /* access modifiers changed from: package-private */
        public int getListenersCount() {
            return this.mSubscribers.size();
        }

        /* access modifiers changed from: package-private */
        public void selectionChanged(int i) {
            for (PageSlider.PageChangedListener pageChangedListener : this.mSubscribers) {
                pageChangedListener.onPageChosen(i);
            }
        }
    }

    public PageSliderIndicator(Context context) {
        this(context, null);
    }

    public PageSliderIndicator(Context context, AttrSet attrSet) {
        this(context, attrSet, "PageSliderIndicatorDefaultStyle");
    }

    public PageSliderIndicator(Context context, AttrSet attrSet, String str) {
        super(context, attrSet, str);
        this.mGravitationalAnimationConfig = Parameters.getInt("hw_mc.graphic.pagesliderindicator_animation_option", 1);
        this.mCountListener = 0;
        this.mSelectionHandler = new IndicatorSelectionHandler();
        this.mElements = new Element[]{null, null};
        this.mGravitationalAnimationEnable = true;
        this.mPageChangedListener = new GravitationalAnimationPageChangedListener();
        if (attrSet != null) {
            attrSet.getAttr(PageSliderIndicatorAttrsConstants.UNSELECTED_DOT_COLOR).ifPresent(new Consumer() {
                /* class ohos.agp.components.$$Lambda$PageSliderIndicator$xMjWd78UBWwF7_XOBUJ_XG8LTp0 */

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PageSliderIndicator.this.lambda$new$0$PageSliderIndicator((Attr) obj);
                }
            });
            attrSet.getAttr(PageSliderIndicatorAttrsConstants.SELECTED_DOT_COLOR).ifPresent(new Consumer() {
                /* class ohos.agp.components.$$Lambda$PageSliderIndicator$T3Ivm_TpzfVyb4YfsfkeY0MewA */

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PageSliderIndicator.this.lambda$new$1$PageSliderIndicator((Attr) obj);
                }
            });
            attrSet.getAttr(PageSliderIndicatorAttrsConstants.BACKGROUNG_START_COLOR).ifPresent(new Consumer() {
                /* class ohos.agp.components.$$Lambda$PageSliderIndicator$t47P6yzS2MauVCNNcO9PhXlVRqw */

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PageSliderIndicator.this.lambda$new$2$PageSliderIndicator((Attr) obj);
                }
            });
            attrSet.getAttr(PageSliderIndicatorAttrsConstants.BACKGROUNG_END_COLOR).ifPresent(new Consumer() {
                /* class ohos.agp.components.$$Lambda$PageSliderIndicator$SknOW3lliS45rK16Utpu3eSrBXI */

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PageSliderIndicator.this.lambda$new$3$PageSliderIndicator((Attr) obj);
                }
            });
        }
        this.mIndicatorAnimator = new GravitationalPagerIndicatorAnimator();
        this.mContext = context;
    }

    public /* synthetic */ void lambda$new$0$PageSliderIndicator(Attr attr) {
        this.mUnselectedDotColor = attr.getColorValue();
    }

    public /* synthetic */ void lambda$new$1$PageSliderIndicator(Attr attr) {
        this.mSelectedDotColor = attr.getColorValue();
    }

    public /* synthetic */ void lambda$new$2$PageSliderIndicator(Attr attr) {
        this.mBackgroundStartColor = attr.getColorValue();
    }

    public /* synthetic */ void lambda$new$3$PageSliderIndicator(Attr attr) {
        this.mBackgroundEndColor = attr.getColorValue();
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Component
    public Style convertAttrToStyle(AttrSet attrSet) {
        if (this.mAttrsConstants == null) {
            this.mAttrsConstants = AttrHelper.getPageSliderIndicatorAttrsConstants();
        }
        return super.convertAttrToStyle(attrSet);
    }

    @Override // ohos.agp.components.Component
    public void applyStyle(Style style) {
        super.applyStyle(style);
        if (style.hasProperty(PageSliderIndicatorAttrsConstants.NORMAL_ELEMENT)) {
            setItemElement(style.getPropertyValue(PageSliderIndicatorAttrsConstants.NORMAL_ELEMENT).asElement(), getItemElements()[1]);
        }
        if (style.hasProperty(PageSliderIndicatorAttrsConstants.SELECTED_ELEMENT)) {
            setItemElement(getItemElements()[0], style.getPropertyValue(PageSliderIndicatorAttrsConstants.SELECTED_ELEMENT).asElement());
        }
    }

    public void setPageSlider(PageSlider pageSlider) {
        nativeSetPageSlider(this.mNativeViewPtr, pageSlider == null ? 0 : pageSlider.getNativeViewPtr());
        this.mPageSlider = pageSlider;
        if (isGravitationalAnimationOpen() && this.mPageSlider != null) {
            addGravitationalAnimation();
        }
    }

    private boolean isGravitationalAnimationOpen() {
        return this.mGravitationalAnimationConfig == 1 && this.mGravitationalAnimationEnable;
    }

    private void addGravitationalAnimation() {
        PageSlider pageSlider;
        this.mIndicatorAnimator.setComponent(this);
        this.mIndicatorAnimator.initSelectPosition();
        this.mIndicatorAnimator.setColor(this.mUnselectedDotColor, this.mSelectedDotColor);
        this.mIndicatorAnimator.setBackgroundColorStartAndEnd(this.mBackgroundStartColor, this.mBackgroundEndColor);
        nativeSetContentWidthHeight(this.mNativeViewPtr, this.mIndicatorAnimator.getContentWidth(), this.mIndicatorAnimator.getContentHeight());
        addDrawTask(this.mIndicatorAnimator);
        setTouchEventListener(new GravitationalAnimationTouchListener());
        GravitationalAnimationPageChangedListener gravitationalAnimationPageChangedListener = this.mPageChangedListener;
        if (gravitationalAnimationPageChangedListener != null && (pageSlider = this.mPageSlider) != null) {
            pageSlider.addPageChangedListener(gravitationalAnimationPageChangedListener);
        }
    }

    private void removeGravitationalAnimation() {
        PageSlider pageSlider;
        addDrawTask(null);
        this.mGravitationalAnimationEnable = false;
        this.mIndicatorAnimator = null;
        GravitationalAnimationPageChangedListener gravitationalAnimationPageChangedListener = this.mPageChangedListener;
        if (!(gravitationalAnimationPageChangedListener == null || (pageSlider = this.mPageSlider) == null)) {
            pageSlider.removePageChangedListener(gravitationalAnimationPageChangedListener);
        }
        setTouchEventListener(null);
        nativeSetAnimationEnable(this.mNativeViewPtr, this.mGravitationalAnimationEnable);
        long j = this.mNativeViewPtr;
        PageSlider pageSlider2 = this.mPageSlider;
        nativeSetPageSlider(j, pageSlider2 == null ? 0 : pageSlider2.getNativeViewPtr());
    }

    public PageSlider getPageSlider() {
        return this.mPageSlider;
    }

    public void setViewPager(PageSlider pageSlider) {
        setPageSlider(pageSlider);
    }

    public void addPageChangedListener(PageSlider.PageChangedListener pageChangedListener) {
        PageSlider pageSlider = getPageSlider();
        if (pageChangedListener != null && pageSlider != null) {
            pageSlider.addPageChangedListener(pageChangedListener);
            this.mCountListener++;
        }
    }

    public void removePageChangedListener(PageSlider.PageChangedListener pageChangedListener) {
        PageSlider pageSlider = getPageSlider();
        if (pageChangedListener != null && pageSlider != null) {
            pageSlider.removePageChangedListener(pageChangedListener);
            this.mCountListener--;
        }
    }

    public void addOnSelectionChangedListener(PageSlider.PageChangedListener pageChangedListener) {
        addPageChangedListener(pageChangedListener);
    }

    public void removeOnSelectionChangedListener(PageSlider.PageChangedListener pageChangedListener) {
        removePageChangedListener(pageChangedListener);
    }

    public int getPageChangedListenerCount() {
        return this.mCountListener;
    }

    public int getOnSelectionChangedListenerCount() {
        return getPageChangedListenerCount();
    }

    public int getCount() {
        return nativeGetCount(this.mNativeViewPtr);
    }

    public void setSelected(int i) {
        nativeSetSelected(this.mNativeViewPtr, i);
    }

    public int getSelected() {
        return nativeGetSelected(this.mNativeViewPtr);
    }

    public void setItemElement(Element element, Element element2) {
        long j;
        if (isGravitationalAnimationOpen()) {
            removeGravitationalAnimation();
        }
        Element[] elementArr = this.mElements;
        System.arraycopy(new Element[]{element, element2}, 0, elementArr, 0, elementArr.length);
        long j2 = 0;
        if (element == null) {
            j = 0;
        } else {
            j = element.getNativeElementPtr();
        }
        if (element2 != null) {
            j2 = element2.getNativeElementPtr();
        }
        nativeSetItemElements(this.mNativeViewPtr, new long[]{j, j2});
    }

    public void setItemNormalElement(Element element) {
        setItemElement(element, getItemSelectedElement());
    }

    public void setItemSelectedElement(Element element) {
        setItemElement(getItemNormalElement(), element);
    }

    public Element[] getItemElements() {
        Element[] elementArr = this.mElements;
        int length = elementArr.length;
        Element[] elementArr2 = new Element[length];
        System.arraycopy(elementArr, 0, elementArr2, 0, length);
        return elementArr2;
    }

    public Element getItemNormalElement() {
        return this.mElements[0];
    }

    public Element getItemSelectedElement() {
        return this.mElements[1];
    }

    public void setItemOffset(int i) {
        nativeSetItemOffset(this.mNativeViewPtr, i);
        if (isGravitationalAnimationOpen() && this.mIndicatorAnimator != null) {
            nativeSetContentWidthHeight(this.mNativeViewPtr, this.mIndicatorAnimator.getContentWidth(), this.mIndicatorAnimator.getContentHeight());
        }
        updateIndicatorPosition();
    }

    public int getItemOffset() {
        return nativeGetItemOffset(this.mNativeViewPtr);
    }

    @Override // ohos.agp.components.Component
    public void setWidth(int i) {
        if (isGravitationalAnimationOpen() && this.mIndicatorAnimator != null) {
            nativeSetContentWidthHeight(this.mNativeViewPtr, this.mIndicatorAnimator.getContentWidth(), this.mIndicatorAnimator.getContentHeight());
        }
        super.setWidth(i);
        updateIndicatorPosition();
    }

    @Override // ohos.agp.components.Component
    public void setHeight(int i) {
        if (isGravitationalAnimationOpen() && this.mIndicatorAnimator != null) {
            nativeSetContentWidthHeight(this.mNativeViewPtr, this.mIndicatorAnimator.getContentWidth(), this.mIndicatorAnimator.getContentHeight());
        }
        super.setHeight(i);
        updateIndicatorPosition();
    }

    @Override // ohos.agp.components.Component
    public void setPadding(int i, int i2, int i3, int i4) {
        super.setPadding(i, i2, i3, i4);
        updateIndicatorPosition();
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Component
    public void createNativePtr() {
        if (this.mNativeViewPtr == 0) {
            this.mNativeViewPtr = nativeGetHandle();
        }
    }

    private void updateIndicatorPosition() {
        if (isGravitationalAnimationOpen() && this.mIndicatorAnimator != null) {
            this.mContext.getUITaskDispatcher().delayDispatch(new Runnable() {
                /* class ohos.agp.components.PageSliderIndicator.AnonymousClass1 */

                public void run() {
                    if (PageSliderIndicator.this.mIndicatorAnimator != null) {
                        PageSliderIndicator.this.mIndicatorAnimator.initSelectPosition();
                        PageSliderIndicator pageSliderIndicator = PageSliderIndicator.this;
                        pageSliderIndicator.addDrawTask(pageSliderIndicator.mIndicatorAnimator);
                    }
                }
            }, 16);
        }
    }
}
