package ohos.agp.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import ohos.agp.animation.styledsolutions.GravitationalRankChain;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Text;
import ohos.agp.components.element.Element;
import ohos.agp.styles.Style;
import ohos.agp.styles.attributes.ListContainerAttrsConstants;
import ohos.agp.utils.Color;
import ohos.agp.utils.ErrorHandler;
import ohos.annotation.SystemApi;
import ohos.app.Context;
import ohos.system.Parameters;

public class ListContainer extends ComponentContainer implements Text.TextObserver {
    public static final int INVALID_INDEX = -1;
    private static final String NOT_ALLOWED = "Method is not allowed in ";
    private static final int NOT_SUPPORT_SORT_ANIMATION = 0;
    public static final float ROTATION_SENSITIVITY_DEFAULT = 0.6f;
    public static final float ROTATION_SENSITIVITY_HIGH = 1.0f;
    public static final float ROTATION_SENSITIVITY_LOW = 0.6f;
    private static final int SUPPORT_SORT_ANIMATION = 1;
    private Element mBoundaryElement;
    private final Map<Component, Integer> mComponentCache;
    int mFirstPosition;
    private GravitationalRankChain mGravitationalRankChain;
    private boolean mIsItemLongPressDragEnabled;
    ItemClickedListener mItemClickedListener;
    ItemLongClickedListener mItemLongClickedListener;
    private ItemLongPressAnimationListener mItemLongPressAnimationListener;
    private BaseItemProvider mItemProvider;
    ItemSelectedListener mItemSelectedListener;
    private final Set<ItemVisibilityChangedListener> mItemVisibilityChangedListeners;
    private int mMaxVisibleComponents;
    ScrollListener mScrollListener;
    int mSelectedIndex;
    private final int mSortAnimationConfig;
    private final Map<Integer, List<Component>> mTypedComponentPool;

    public interface ItemClickedListener {
        void onItemClicked(ListContainer listContainer, Component component, int i, long j);
    }

    public interface ItemLongClickedListener {
        boolean onItemLongClicked(ListContainer listContainer, Component component, int i, long j);
    }

    /* access modifiers changed from: private */
    public interface ItemLongPressAnimationListener {
        void onItemLongPressEndAnimation(LongPressInfo longPressInfo, ComponentContainer componentContainer);

        void onItemLongPressMoveAnimation(LongPressInfo longPressInfo, ComponentContainer componentContainer);

        void onItemLongPressStartAnimation(LongPressInfo longPressInfo, ComponentContainer componentContainer);
    }

    public interface ItemSelectedListener {
        void onItemSelected(ListContainer listContainer, Component component, int i, long j);
    }

    public interface ItemVisibilityChangedListener {
        void onItemAdded(Component component, int i);

        void onItemRemoved(Component component, int i);
    }

    public interface ScrollListener {
        void onScrollFinished();
    }

    private native void nativeBringToFront(long j, long j2);

    private native void nativeBringToRear(long j, long j2);

    private native int nativeGetBoundaryColor(long j);

    private native boolean nativeGetBoundarySwitch(long j);

    private native int nativeGetBoundaryThickness(long j);

    private native long nativeGetCachedComponentAt(long j, int i);

    private native int nativeGetCenterFocusablePosition(long j);

    private native int nativeGetContentEndOffSet(long j);

    private native int nativeGetContentStartOffSet(long j);

    private native int nativeGetExtraItemsNumber(long j);

    private native String nativeGetFilterText(long j);

    private native int nativeGetFirstVisiblePosition(long j);

    private native boolean nativeGetFooterBoundarySwitch(long j);

    private native long nativeGetHandle();

    private native boolean nativeGetHeaderBoundarySwitch(long j);

    private native int nativeGetLastVisiblePosition(long j);

    private native int nativeGetOrientation(long j);

    private native int nativeGetPositionForView(long j, Component component);

    private native boolean nativeGetReboundEffect(long j);

    private native void nativeGetReboundEffectParams(long j, ReboundEffectParams reboundEffectParams);

    private native int nativeGetSelectedItemIndex(long j);

    private native int nativeGetShaderColor(long j);

    private native void nativeScrollTo(long j, int i);

    private native void nativeScrollToCenter(long j, int i);

    private native void nativeSetBoundary(long j, long j2);

    private native void nativeSetBoundaryColor(long j, int i);

    private native void nativeSetBoundarySwitch(long j, boolean z);

    private native void nativeSetBoundaryThickness(long j, int i);

    private native void nativeSetContentEndOffSet(long j, int i);

    private native void nativeSetContentOffSet(long j, int i, int i2);

    private native void nativeSetContentStartOffSet(long j, int i);

    private native void nativeSetExtraItemsNumber(long j, int i);

    private native void nativeSetFilterText(long j, String str);

    private native void nativeSetFooterBoundarySwitch(long j, boolean z);

    private native void nativeSetHeaderBoundarySwitch(long j, boolean z);

    private native void nativeSetItemClickCallback(long j, ItemClickedListener itemClickedListener);

    private native void nativeSetItemLongClickCallback(long j, ItemLongClickedListener itemLongClickedListener);

    private native void nativeSetItemLongPressAnimationCallback(long j, ItemLongPressAnimationListener itemLongPressAnimationListener);

    private native void nativeSetItemSelectedCallback(long j, ItemSelectedListener itemSelectedListener);

    private native void nativeSetOrientation(long j, int i);

    private native long nativeSetProvider(long j, BaseItemProvider baseItemProvider);

    private native void nativeSetReboundEffect(long j, boolean z);

    private native void nativeSetReboundEffectParams(long j, int i, float f, int i2);

    private native void nativeSetScrollListener(long j, ScrollListener scrollListener);

    private native void nativeSetSelectedItemIndex(long j, int i);

    private native void nativeSetShaderColor(long j, int i);

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.ComponentContainer, ohos.agp.components.Component
    public void createNativePtr() {
        if (this.mNativeViewPtr == 0) {
            this.mNativeViewPtr = nativeGetHandle();
        }
    }

    public ListContainer(Context context) {
        this(context, null);
    }

    public ListContainer(Context context, AttrSet attrSet) {
        this(context, attrSet, "ListContainerDefaultStyle");
    }

    public ListContainer(Context context, AttrSet attrSet, String str) {
        super(context, attrSet, str);
        this.mFirstPosition = 0;
        this.mSelectedIndex = -1;
        this.mSortAnimationConfig = Parameters.getInt("hw_mc.graphic.listcontainer_sort_animation_option", 0);
        this.mGravitationalRankChain = new GravitationalRankChain();
        this.mItemVisibilityChangedListeners = new HashSet();
        this.mComponentCache = new HashMap();
        this.mTypedComponentPool = new HashMap();
        this.mIsItemLongPressDragEnabled = true;
        this.mItemLongPressAnimationListener = new ItemLongPressAnimationListener() {
            /* class ohos.agp.components.ListContainer.AnonymousClass1 */

            @Override // ohos.agp.components.ListContainer.ItemLongPressAnimationListener
            public void onItemLongPressStartAnimation(LongPressInfo longPressInfo, ComponentContainer componentContainer) {
                if (ListContainer.this.mIsItemLongPressDragEnabled && longPressInfo != null) {
                    ListContainer.this.mGravitationalRankChain.startDraggingItemZoomInAnimation(longPressInfo.component, componentContainer);
                }
            }

            @Override // ohos.agp.components.ListContainer.ItemLongPressAnimationListener
            public void onItemLongPressMoveAnimation(LongPressInfo longPressInfo, ComponentContainer componentContainer) {
                if (ListContainer.this.mIsItemLongPressDragEnabled && longPressInfo != null) {
                    ListContainer.this.mGravitationalRankChain.touchDraggingUpdateAnimation(longPressInfo.component, longPressInfo.deltaPoint, componentContainer);
                }
            }

            @Override // ohos.agp.components.ListContainer.ItemLongPressAnimationListener
            public void onItemLongPressEndAnimation(LongPressInfo longPressInfo, ComponentContainer componentContainer) {
                if (ListContainer.this.mIsItemLongPressDragEnabled && longPressInfo != null) {
                    ListContainer.this.mGravitationalRankChain.endDragingItemZoomOutAnimation(longPressInfo.component, componentContainer);
                }
            }
        };
        DirectionalLayoutManager directionalLayoutManager = new DirectionalLayoutManager();
        if (attrSet != null) {
            attrSet.getAttr("orientation").ifPresent(new Consumer(directionalLayoutManager) {
                /* class ohos.agp.components.$$Lambda$ListContainer$3UyIb41I0fV_y0GOl5FyomBDFC8 */
                private final /* synthetic */ LayoutManager f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$1.setOrientation(new AttrWrapper(Context.this, (Attr) obj).getIntegerValue());
                }
            });
        }
        setLayoutManager(directionalLayoutManager);
    }

    @Override // ohos.agp.components.ComponentContainer
    public void setLayoutManager(LayoutManager layoutManager) {
        super.setLayoutManager(layoutManager);
        setItemLongPressAnimationListener(this.mItemLongPressAnimationListener, false);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Component
    public Style convertAttrToStyle(AttrSet attrSet) {
        if (this.mAttrsConstants == null) {
            this.mAttrsConstants = AttrHelper.getListContainerAttrsConstants();
        }
        return super.convertAttrToStyle(attrSet);
    }

    @Override // ohos.agp.components.Component
    public void applyStyle(Style style) {
        super.applyStyle(style);
        if (style.hasProperty(ListContainerAttrsConstants.BOUNDARY)) {
            setBoundary(style.getPropertyValue(ListContainerAttrsConstants.BOUNDARY).asElement());
        }
    }

    @Override // ohos.agp.components.Text.TextObserver
    public void onTextUpdated(String str, int i, int i2, int i3) {
        setTextFilter(str);
    }

    public void scrollTo(int i) {
        nativeScrollTo(this.mNativeViewPtr, i);
    }

    public void setContentStartOffSet(int i) {
        nativeSetContentStartOffSet(this.mNativeViewPtr, i);
    }

    public void setContentEndOffSet(int i) {
        nativeSetContentEndOffSet(this.mNativeViewPtr, i);
    }

    public void setContentOffSet(int i, int i2) {
        nativeSetContentOffSet(this.mNativeViewPtr, i, i2);
    }

    public int getContentStartOffset() {
        return nativeGetContentStartOffSet(this.mNativeViewPtr);
    }

    public int getContentEndOffset() {
        return nativeGetContentEndOffSet(this.mNativeViewPtr);
    }

    public void scrollToCenter(int i) {
        nativeScrollToCenter(this.mNativeViewPtr, i);
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

    public void setItemClickedListener(ItemClickedListener itemClickedListener) {
        this.mItemClickedListener = itemClickedListener;
        nativeSetItemClickCallback(this.mNativeViewPtr, itemClickedListener);
    }

    public boolean executeItemClick(Component component, int i, long j) {
        ItemClickedListener itemClickedListener = this.mItemClickedListener;
        if (itemClickedListener == null) {
            return false;
        }
        itemClickedListener.onItemClicked(this, component, i, j);
        return true;
    }

    private void setItemLongPressAnimationListener(ItemLongPressAnimationListener itemLongPressAnimationListener, boolean z) {
        if (!(this.mSortAnimationConfig == 1 || z) || getOrientation() != 1) {
            nativeSetItemLongPressAnimationCallback(this.mNativeViewPtr, null);
            return;
        }
        this.mItemLongPressAnimationListener = itemLongPressAnimationListener;
        nativeSetItemLongPressAnimationCallback(this.mNativeViewPtr, itemLongPressAnimationListener);
    }

    @SystemApi
    public void setIsItemLongPressDragEnabled(boolean z) {
        this.mIsItemLongPressDragEnabled = z;
        setItemLongPressAnimationListener(this.mItemLongPressAnimationListener, true);
    }

    public void setItemLongClickedListener(ItemLongClickedListener itemLongClickedListener) {
        this.mItemLongClickedListener = itemLongClickedListener;
        nativeSetItemLongClickCallback(this.mNativeViewPtr, itemLongClickedListener);
    }

    /* access modifiers changed from: protected */
    public boolean onItemLongClicked(ListContainer listContainer, Component component, int i, long j) {
        ItemLongClickedListener itemLongClickedListener = this.mItemLongClickedListener;
        if (itemLongClickedListener != null) {
            return itemLongClickedListener.onItemLongClicked(this, component, i, j);
        }
        return false;
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
        nativeSetScrollListener(this.mNativeViewPtr, scrollListener);
    }

    public void setItemSelectedListener(ItemSelectedListener itemSelectedListener) {
        this.mItemSelectedListener = itemSelectedListener;
        nativeSetItemSelectedCallback(this.mNativeViewPtr, itemSelectedListener);
    }

    /* access modifiers changed from: protected */
    public void onItemSelected(ListContainer listContainer, Component component, int i, long j) {
        ItemSelectedListener itemSelectedListener = this.mItemSelectedListener;
        if (itemSelectedListener != null) {
            itemSelectedListener.onItemSelected(this, component, i, j);
        }
    }

    @Override // ohos.agp.components.ComponentContainer, ohos.agp.components.Component
    public Component findComponentById(int i) {
        if (getId() == i) {
            return this;
        }
        return null;
    }

    @Override // ohos.agp.components.ComponentContainer
    public void addComponent(Component component) {
        throw new UnsupportedOperationException(NOT_ALLOWED + getClass().getCanonicalName());
    }

    @Override // ohos.agp.components.ComponentContainer
    public void addComponent(Component component, int i, int i2) {
        throw new UnsupportedOperationException(NOT_ALLOWED + getClass().getCanonicalName());
    }

    @Override // ohos.agp.components.ComponentContainer
    public void addComponent(Component component, ComponentContainer.LayoutConfig layoutConfig) {
        throw new UnsupportedOperationException(NOT_ALLOWED + getClass().getCanonicalName());
    }

    @Override // ohos.agp.components.ComponentContainer
    public void addComponent(Component component, int i, ComponentContainer.LayoutConfig layoutConfig) {
        throw new UnsupportedOperationException(NOT_ALLOWED + getClass().getCanonicalName());
    }

    @Override // ohos.agp.components.ComponentContainer
    public void addComponent(Component component, int i) {
        throw new UnsupportedOperationException(NOT_ALLOWED + getClass().getCanonicalName());
    }

    @Override // ohos.agp.components.ComponentParent, ohos.agp.components.ComponentContainer
    public void removeComponent(Component component) {
        throw new UnsupportedOperationException(NOT_ALLOWED + getClass().getCanonicalName());
    }

    @Override // ohos.agp.components.ComponentParent, ohos.agp.components.ComponentContainer
    public void removeComponentAt(int i) {
        throw new UnsupportedOperationException(NOT_ALLOWED + getClass().getCanonicalName());
    }

    @Override // ohos.agp.components.ComponentContainer
    public void removeComponentById(int i) {
        throw new UnsupportedOperationException(NOT_ALLOWED + getClass().getCanonicalName());
    }

    @Override // ohos.agp.components.ComponentParent, ohos.agp.components.ComponentContainer
    public void removeComponents(int i, int i2) {
        throw new UnsupportedOperationException(NOT_ALLOWED + getClass().getCanonicalName());
    }

    @Override // ohos.agp.components.ComponentContainer
    public void removeAllComponents() {
        throw new UnsupportedOperationException(NOT_ALLOWED + getClass().getCanonicalName());
    }

    @Override // ohos.agp.components.ComponentContainer
    public int getChildCount() {
        BaseItemProvider baseItemProvider = this.mItemProvider;
        if (baseItemProvider == null) {
            return 0;
        }
        return baseItemProvider.getCount();
    }

    static /* synthetic */ boolean lambda$getComponentAt$1(Integer num) {
        return num.intValue() >= 0;
    }

    @Override // ohos.agp.components.ComponentContainer
    public Component getComponentAt(int i) {
        validateParam(Integer.valueOf(i), $$Lambda$ListContainer$VVJQgj0LS0Jut8nMscxT36v54c.INSTANCE, "position must be non negative");
        return findCachedComponent(nativeGetCachedComponentAt(this.mNativeViewPtr, i));
    }

    static /* synthetic */ boolean lambda$setExtraItemsNumber$2(Integer num) {
        return num.intValue() >= 0;
    }

    public void setExtraItemsNumber(int i) {
        validateParam(Integer.valueOf(i), $$Lambda$ListContainer$yJbOiyP80p_pjFFioYgF_WkgTZA.INSTANCE, "number of extra items must be non negative");
        nativeSetExtraItemsNumber(this.mNativeViewPtr, i);
    }

    public int getExtraItemsNumber() {
        return nativeGetExtraItemsNumber(this.mNativeViewPtr);
    }

    public void addItemVisibilityChangedListener(ItemVisibilityChangedListener itemVisibilityChangedListener) {
        if (!this.mItemVisibilityChangedListeners.contains(itemVisibilityChangedListener)) {
            this.mItemVisibilityChangedListeners.add(itemVisibilityChangedListener);
        }
    }

    public void removeItemVisibilityChangedListener(ItemVisibilityChangedListener itemVisibilityChangedListener) {
        this.mItemVisibilityChangedListeners.remove(itemVisibilityChangedListener);
    }

    private Component getConvertComponent(int i) {
        List<Component> list = this.mTypedComponentPool.get(Integer.valueOf(i));
        if (list == null) {
            list = new ArrayList<>();
            this.mTypedComponentPool.put(Integer.valueOf(i), list);
        }
        if (list.size() <= 0) {
            return null;
        }
        Component component = list.get(0);
        list.remove(0);
        return component;
    }

    private void putConvertComponent(int i, Component component) {
        List<Component> list = this.mTypedComponentPool.get(Integer.valueOf(i));
        if (list == null) {
            list = new ArrayList<>();
            this.mTypedComponentPool.put(Integer.valueOf(i), list);
        }
        if (list.size() < this.mMaxVisibleComponents) {
            list.add(component);
        }
        this.mComponentCache.remove(component);
    }

    private Component findCachedComponent(long j) {
        for (Component component : this.mComponentCache.keySet()) {
            if (j == component.getNativeViewPtr()) {
                return component;
            }
        }
        return null;
    }

    private Component takeComponent(int i) {
        BaseItemProvider baseItemProvider = this.mItemProvider;
        if (baseItemProvider == null) {
            return null;
        }
        int itemComponentType = baseItemProvider.getItemComponentType(i);
        Component component = this.mItemProvider.getComponent(i, getConvertComponent(itemComponentType), this);
        if (component != null) {
            this.mComponentCache.put(component, Integer.valueOf(itemComponentType));
            if (this.mComponentCache.size() > this.mMaxVisibleComponents) {
                this.mMaxVisibleComponents = this.mComponentCache.size();
            }
            for (ItemVisibilityChangedListener itemVisibilityChangedListener : this.mItemVisibilityChangedListeners) {
                itemVisibilityChangedListener.onItemAdded(component, i);
            }
        }
        return component;
    }

    private Component takeChangedComponent(int i, long j) {
        if (this.mItemProvider == null) {
            return null;
        }
        Component findCachedComponent = findCachedComponent(j);
        Component component = this.mItemProvider.getComponent(i, findCachedComponent, this);
        if (component != findCachedComponent) {
            int itemComponentType = this.mItemProvider.getItemComponentType(i);
            if (findCachedComponent != null) {
                for (ItemVisibilityChangedListener itemVisibilityChangedListener : this.mItemVisibilityChangedListeners) {
                    itemVisibilityChangedListener.onItemRemoved(findCachedComponent, i);
                }
                putConvertComponent(itemComponentType, findCachedComponent);
            }
            if (component != null) {
                this.mComponentCache.put(component, Integer.valueOf(itemComponentType));
                for (ItemVisibilityChangedListener itemVisibilityChangedListener2 : this.mItemVisibilityChangedListeners) {
                    itemVisibilityChangedListener2.onItemAdded(component, i);
                }
            }
        }
        return component;
    }

    private void releaseComponent(int i, long j) {
        Component findCachedComponent = findCachedComponent(j);
        if (findCachedComponent != null) {
            for (ItemVisibilityChangedListener itemVisibilityChangedListener : this.mItemVisibilityChangedListeners) {
                itemVisibilityChangedListener.onItemRemoved(findCachedComponent, i);
            }
            putConvertComponent(this.mComponentCache.get(findCachedComponent).intValue(), findCachedComponent);
            if (this.mComponentCache.isEmpty()) {
                this.mTypedComponentPool.clear();
            }
        }
    }

    public int getIndexForComponent(Component component) {
        return nativeGetPositionForView(this.mNativeViewPtr, component);
    }

    @Deprecated
    public int getLastVisibleItemPosition() {
        return nativeGetLastVisiblePosition(this.mNativeViewPtr);
    }

    @Deprecated
    public int getFirstVisibleItemPosition() {
        return nativeGetFirstVisiblePosition(this.mNativeViewPtr);
    }

    public int getItemPosByVisibleIndex(int i) {
        int visibleIndexCount = getVisibleIndexCount();
        if (visibleIndexCount <= 0 || i < 0 || i > visibleIndexCount - 1) {
            return -1;
        }
        return nativeGetFirstVisiblePosition(this.mNativeViewPtr) + i;
    }

    public int getVisibleIndexCount() {
        int nativeGetLastVisiblePosition = (nativeGetLastVisiblePosition(this.mNativeViewPtr) - nativeGetFirstVisiblePosition(this.mNativeViewPtr)) + 1;
        if (nativeGetLastVisiblePosition >= 0) {
            return nativeGetLastVisiblePosition;
        }
        return 0;
    }

    public int getCenterFocusablePosition() {
        return nativeGetCenterFocusablePosition(this.mNativeViewPtr);
    }

    public void setReboundEffect(boolean z) {
        nativeSetReboundEffect(this.mNativeViewPtr, z);
    }

    public boolean getReboundEffect() {
        return nativeGetReboundEffect(this.mNativeViewPtr);
    }

    public void setShaderColor(Color color) {
        nativeSetShaderColor(this.mNativeViewPtr, color.getValue());
    }

    public Color getShaderColor() {
        return new Color(nativeGetShaderColor(this.mNativeViewPtr));
    }

    public void setOrientation(int i) {
        if (i == 1 || i == 0) {
            nativeSetOrientation(this.mNativeViewPtr, i);
        }
    }

    public void setBoundary(Element element) {
        this.mBoundaryElement = element;
        nativeSetBoundary(this.mNativeViewPtr, element == null ? 0 : element.getNativeElementPtr());
    }

    public Element getBoundary() {
        return this.mBoundaryElement;
    }

    public void setFooterBoundarySwitch(boolean z) {
        nativeSetFooterBoundarySwitch(this.mNativeViewPtr, z);
    }

    public boolean getFooterBoundarySwitch() {
        return nativeGetFooterBoundarySwitch(this.mNativeViewPtr);
    }

    public void setHeaderBoundarySwitch(boolean z) {
        nativeSetHeaderBoundarySwitch(this.mNativeViewPtr, z);
    }

    public boolean getHeaderBoundarySwitch() {
        return nativeGetHeaderBoundarySwitch(this.mNativeViewPtr);
    }

    public void setBoundarySwitch(boolean z) {
        nativeSetBoundarySwitch(this.mNativeViewPtr, z);
    }

    public boolean getBoundarySwitch() {
        return nativeGetBoundarySwitch(this.mNativeViewPtr);
    }

    public void setBoundaryColor(Color color) {
        nativeSetBoundaryColor(this.mNativeViewPtr, color.getValue());
    }

    public Color getBoundaryColor() {
        return new Color(nativeGetBoundaryColor(this.mNativeViewPtr));
    }

    static /* synthetic */ boolean lambda$setBoundaryThickness$3(Integer num) {
        return num.intValue() >= 0;
    }

    public void setBoundaryThickness(int i) {
        if (validateParam(Integer.valueOf(i), $$Lambda$ListContainer$9I2wSLzFDBaQ8yP5osaKU6HqzOg.INSTANCE, "negative thickness")) {
            nativeSetBoundaryThickness(this.mNativeViewPtr, i);
        }
    }

    public int getBoundaryThickness() {
        return nativeGetBoundaryThickness(this.mNativeViewPtr);
    }

    public void setSelectedItemIndex(int i) {
        nativeSetSelectedItemIndex(this.mNativeViewPtr, i);
    }

    public int getSelectedItemIndex() {
        return nativeGetSelectedItemIndex(this.mNativeViewPtr);
    }

    public void setTextFilter(String str) {
        nativeSetFilterText(this.mNativeViewPtr, str);
    }

    public String getTextFilter() {
        return nativeGetFilterText(this.mNativeViewPtr);
    }

    public BaseItemProvider getItemProvider() {
        return this.mItemProvider;
    }

    public void setItemProvider(BaseItemProvider baseItemProvider) {
        nativeSetProvider(this.mNativeViewPtr, baseItemProvider);
        this.mItemProvider = baseItemProvider;
    }

    public int getOrientation() {
        return nativeGetOrientation(this.mNativeViewPtr);
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

    static /* synthetic */ boolean lambda$validateReboundEffectParams$4(Integer num) {
        return num.intValue() >= 0 && num.intValue() <= 100;
    }

    private void validateReboundEffectParams(int i, float f, int i2) {
        validateParam(Integer.valueOf(i), $$Lambda$ListContainer$papT0rA67zjVMFnzmoAYFR4pUmA.INSTANCE, "overscrollPercent must be in range [0, 100]");
        validateParam(Float.valueOf(f), $$Lambda$ListContainer$SQ6BSNGRgqiLqFc4VQGQknNfX0.INSTANCE, "overscrollRate must be greater than 0");
        validateParam(Integer.valueOf(i2), $$Lambda$ListContainer$euklv7OZEpa2L39APqIqNrU2qKc.INSTANCE, "remainVisiblePercent must be in range [0, 100]");
    }

    static /* synthetic */ boolean lambda$validateReboundEffectParams$5(Float f) {
        return f.floatValue() > 0.0f;
    }

    static /* synthetic */ boolean lambda$validateReboundEffectParams$6(Integer num) {
        return num.intValue() >= 0 && num.intValue() <= 100;
    }

    @Override // ohos.agp.components.ComponentParent, ohos.agp.components.ComponentContainer
    public void moveChildToFront(Component component) {
        nativeBringToFront(this.mNativeViewPtr, component.getNativeViewPtr());
    }

    public void moveChildToRear(Component component) {
        nativeBringToRear(this.mNativeViewPtr, component.getNativeViewPtr());
    }
}
