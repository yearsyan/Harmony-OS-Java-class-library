package ohos.agp.components;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.Attr;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.element.Element;
import ohos.agp.render.Canvas;
import ohos.agp.styles.Style;
import ohos.agp.styles.Value;
import ohos.agp.styles.attributes.ViewAttrsConstants;
import ohos.agp.utils.CallbackHelper;
import ohos.agp.utils.Color;
import ohos.agp.utils.DimensFloat;
import ohos.agp.utils.ErrorHandler;
import ohos.agp.utils.MemoryCleaner;
import ohos.agp.utils.MemoryCleanerRegistry;
import ohos.agp.utils.MimeData;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import ohos.global.configuration.Configuration;
import ohos.global.resource.ResourceManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.KeyEvent;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.RotationEvent;
import ohos.multimodalinput.event.SpeechEvent;
import ohos.multimodalinput.event.TouchEvent;

public class Component {
    public static final int ACCESSIBILITY_ADAPTABLE = 0;
    public static final int ACCESSIBILITY_DISABLE = 1;
    public static final int ACCESSIBILITY_ENABLE = 2;
    public static final int AXIS_X = 0;
    public static final int AXIS_Y = 1;
    protected static final int CALLBACK_PARAM_AFTER_INDEX = 1;
    protected static final int CALLBACK_PARAM_BEFORE_INDEX = 0;
    protected static final int CALLBACK_PARAM_DIFFERENCE_INDEX = 2;
    public static final float DEFAULT_SCALE = 1.0f;
    public static final int DRAG_DOWN = 2;
    public static final int DRAG_HORIZONTAL = 2;
    public static final int DRAG_HORIZONTAL_VERTICAL = 1;
    public static final int DRAG_LEFT = 3;
    public static final int DRAG_RIGHT = 4;
    public static final int DRAG_UP = 1;
    public static final int DRAG_VERTICAL = 3;
    private static final int FADE_BOTTOM = 3;
    private static final int FADE_LEFT = 0;
    private static final int FADE_RIGHT = 2;
    private static final int FADE_TOP = 1;
    public static final int FOCUS_ADAPTABLE = 4;
    public static final int FOCUS_DISABLE = 0;
    public static final int FOCUS_ENABLE = 8;
    public static final int FOCUS_NEXT = 4;
    public static final int FOCUS_PREVIOUS = 5;
    public static final int FOCUS_SIDE_BOTTOM = 3;
    public static final int FOCUS_SIDE_LEFT = 0;
    public static final int FOCUS_SIDE_RIGHT = 2;
    public static final int FOCUS_SIDE_TOP = 1;
    public static final int HIDE = 2;
    public static final int HORIZONTAL = 0;
    public static final int ID_DEFAULT = -1;
    public static final int INHERITED_MODE = 0;
    public static final int INVISIBLE = 1;
    private static final float MAX_DRAG_ACCEPT_ANGLE = 75.0f;
    private static final float MIN_DRAG_ACCEPT_ANGLE = 15.0f;
    public static final int OVAL_MODE = 2;
    private static final int PAIR_MODE = 2;
    protected static final int POSITION_X_INDEX = 0;
    protected static final int POSITION_Y_INDEX = 1;
    private static final int RADIUS_ARRAY_LENGTH = 8;
    public static final int RECT_MODE = 1;
    public static final int SCROLL_AUTO_STAGE = 2;
    public static final int SCROLL_IDLE_STAGE = 0;
    public static final int SCROLL_NORMAL_STAGE = 1;
    private static final Map<String, BiConsumer<Component, Value>> STYLE_METHOD_MAP = new HashMap<String, BiConsumer<Component, Value>>() {
        /* class ohos.agp.components.Component.AnonymousClass1 */

        {
            put("width", $$Lambda$Component$1$hAMy_VgFb9BKb4HQMePz8Kgk_c.INSTANCE);
            put("height", $$Lambda$Component$1$NtlcPYlUC6ZxjDb934DFgdEqF98.INSTANCE);
            put(ViewAttrsConstants.BACKGROUND_ELEMENT, $$Lambda$Component$1$8ZzEEmhAcUEr40uXuxsrxA5Bb1Y.INSTANCE);
            put(ViewAttrsConstants.FOREGROUND_ELEMENT, $$Lambda$Component$1$pPMKm7uGIO71bKx97TDmZ642s.INSTANCE);
        }
    };
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "AGP_VIEW");
    public static final int VERTICAL = 1;
    public static final int VISIBLE = 0;
    private CornerMark cornerMark;
    private AccessibilityFocusListener mAccessibilityFocusListener;
    protected ViewAttrsConstants mAttrsConstants;
    protected Element mBackgroundElement;
    private BindStateChangedListener mBindStateChangedListener;
    private CanAcceptScrollListener mCanAcceptScrollListener;
    protected Canvas mCanvasForTaskOverContent;
    protected Canvas mCanvasForTaskUnderContent;
    private ClickedListener mClickedListener;
    Map<Integer, ComponentHolder> mComponentHolderMap;
    private final ComponentObserverManager mComponentObserverManager;
    protected ComponentParent mComponentParent;
    private ComponentStateChangedListener mComponentStateChangedListener;
    private ComponentTreeObserver mComponentTreeObserver;
    protected Context mContext;
    private DoubleClickedListener mDoubleClickedListener;
    private MimeData mDraggableViewClipData;
    private DraggedListener mDraggedListener;
    protected DrawTask mDrawTaskOverContent;
    protected DrawTask mDrawTaskUnderContent;
    protected EstimateSizeListener mEstimateSizeListener;
    private FocusChangedListener mFocusChangedListener;
    protected Element mForegroundElement;
    private ForwardTouchListener mForwardTouchListener;
    private InnerForwardTouchListener mInnerForwardTouchListener;
    private InnerTouchEventListener mInnerTouchListener;
    protected KeyEventListener mKeyEventListener;
    protected ComponentContainer.LayoutConfig mLayoutConfig;
    private LayoutRefreshedListener mLayoutRefreshedListener;
    private LongClickedListener mLongClickedListener;
    protected long mNativeViewPtr;
    protected final ArrayList<AvailabilityObserver> mObserverList;
    private final ArrayList<BindStateChangedListener> mOnBindStateChangeListeners;
    private OnDragListener mOnDragListener;
    protected float[] mPosition;
    protected RotationEventListener mRotationEventListener;
    private ScaledListener mScaledListener;
    private ScrolledListener mScrolledListener;
    private ScrolledListener mScrolledListenerInternal;
    private final ArrayList<ScrolledListener> mScrolledListeners;
    private Component mShadowComponent;
    private SpeechEventListener mSpeechEventListener;
    private Object mTag;
    private TouchEventListener mTouchEventListener;

    public interface AccessibilityFocusListener {
        void onAccessibilityFocus(Component component, int i, boolean z);
    }

    public interface AvailabilityObserver {
        void onComponentRemoved(Component component);
    }

    public interface BindStateChangedListener {
        void onComponentBoundToWindow(Component component);

        void onComponentUnboundFromWindow(Component component);
    }

    public interface CanAcceptScrollListener {
        boolean canAcceptScroll(Component component, int i, boolean z);
    }

    public interface ClickedListener {
        void onClick(Component component);
    }

    public interface ComponentStateChangedListener {
        void onComponentStateChanged(Component component, int i);
    }

    public interface DoubleClickedListener {
        void onDoubleClick(Component component);
    }

    public interface DraggedListener {
        void onDragCancel(Component component, DragInfo dragInfo);

        void onDragDown(Component component, DragInfo dragInfo);

        void onDragEnd(Component component, DragInfo dragInfo);

        default boolean onDragPreAccept(Component component, int i) {
            return true;
        }

        void onDragStart(Component component, DragInfo dragInfo);

        void onDragUpdate(Component component, DragInfo dragInfo);
    }

    public interface DrawTask {
        public static final int BETWEEN_BACKGROUND_AND_CONTENT = 1;
        public static final int BETWEEN_CONTENT_AND_FOREGROUND = 2;

        void onDraw(Component component, Canvas canvas);
    }

    public interface EstimateSizeListener {
        boolean onEstimateSize(int i, int i2);
    }

    public interface FocusChangedListener {
        void onFocusChange(Component component, boolean z);
    }

    private interface ForwardTouchListener {
        boolean onForwardTouch(Component component, TouchEvent touchEvent);
    }

    public enum GestureType {
        TAP,
        LONG_PRESS,
        DOUBLE_TAP,
        HORZ_VERT_DRAG,
        HORIZONTAL_DRAG,
        VERTICAL_DRAG,
        SCALE
    }

    /* access modifiers changed from: private */
    public interface InnerForwardTouchListener {
        boolean onForwardTouch(Component component, TouchEvent touchEvent, int i, int i2);
    }

    /* access modifiers changed from: private */
    public interface InnerTouchEventListener {
        boolean onTouchEvent(Component component, TouchEvent touchEvent, int i, int i2, boolean z);
    }

    public interface KeyEventListener {
        boolean onKeyEvent(Component component, KeyEvent keyEvent);
    }

    public enum LayoutDirection {
        LTR,
        RTL,
        INHERIT,
        LOCALE
    }

    public interface LayoutRefreshedListener {
        void onRefreshed(Component component);
    }

    public interface LongClickedListener {
        void onLongClicked(Component component);
    }

    public interface OnDragListener {
        boolean onDrag(Component component, DragEvent dragEvent);
    }

    public interface RotationEventListener {
        boolean onRotationEvent(Component component, RotationEvent rotationEvent);
    }

    public interface ScaledListener {
        void onScaleEnd(Component component, ScaleInfo scaleInfo);

        void onScaleStart(Component component, ScaleInfo scaleInfo);

        void onScaleUpdate(Component component, ScaleInfo scaleInfo);
    }

    public interface ScrolledListener {
        void onContentScrolled(Component component, int i, int i2, int i3, int i4);

        default void scrolledStageUpdate(Component component, int i) {
        }
    }

    public interface SpeechEventListener {
        boolean onSpeechEvent(Component component, SpeechEvent speechEvent);
    }

    public interface TouchEventListener {
        boolean onTouchEvent(Component component, TouchEvent touchEvent);
    }

    private native void nativeAddDrawTaskOverContent(long j, DrawTask drawTask, long j2);

    private native void nativeAddDrawTaskUnderContent(long j, DrawTask drawTask, long j2);

    private native void nativeAddOnAttachStateChangeCallback(long j, BindStateChangedListener bindStateChangedListener);

    private native void nativeAnnounceAccessibility(long j, String str);

    private native void nativeApplyStyle(long j, long j2);

    private native void nativeArrange(long j, int i, int i2, int i3, int i4);

    private native boolean nativeCanScroll(long j, int i);

    private native void nativeClearFocus(long j);

    private native void nativeCreateVoiceEvent(long j, String str, int i, boolean z);

    private native void nativeEnableCornerMark(long j, boolean z);

    private native void nativeEnableScrolledListener(long j, boolean z);

    private native void nativeEstimateSize(long j, int i, int i2);

    private native boolean nativeExecuteDoubleClick(long j);

    private native boolean nativeExecuteLongClick(long j);

    private native long nativeFindFocus(long j);

    private native long nativeFindNextFocus(long j, int i);

    private native boolean nativeFindRequestNextFocus(long j, int i);

    private native int nativeGetAccessibility(long j);

    private native float nativeGetAlpha(long j);

    private native String nativeGetBarrierfreeDescription(long j);

    private native int nativeGetBottom(long j);

    private native boolean nativeGetBoundaryFadeEffectEnable(long j);

    private native float nativeGetBoundaryFadeEffectRate(long j, int i);

    private native float[] nativeGetCenterZoomFactor(long j);

    private native boolean nativeGetCentralScrollMode(long j);

    private native boolean nativeGetClipEnabled(long j);

    private native int[] nativeGetComponentPosition(long j);

    private native int[] nativeGetComponentSize(long j);

    private native String nativeGetContentDescription(long j);

    private native boolean nativeGetContentEnable(long j);

    private native float nativeGetContentPositionX(long j);

    private native float nativeGetContentPositionY(long j);

    private native float nativeGetDragAcceptAngle(long j);

    private native int nativeGetEstimatedHeight(long j);

    private native int nativeGetEstimatedWidth(long j);

    private native int nativeGetFadeEffectBoundaryWidth(long j);

    private native int nativeGetFadeEffectColor(long j);

    private native boolean nativeGetFocusBorderEnable(long j);

    private native int nativeGetFocusBorderPadding(long j);

    private native float[] nativeGetFocusBorderRadii(long j);

    private native int nativeGetFocusBorderWidth(long j);

    private native int nativeGetFocusable(long j);

    private native int nativeGetForegroundGravity(long j);

    private native int nativeGetGesturePriorityCallback(long j, int i);

    private native int nativeGetHeight(long j);

    private native int[] nativeGetHorizontalPosition(long j);

    private native int nativeGetId(long j);

    private native int nativeGetLayoutDirection(long j);

    private native int nativeGetLayoutDirectionResolved(long j);

    private native int nativeGetLeft(long j);

    private native boolean nativeGetLocalVisibleRect(long j, Rect rect);

    private native void nativeGetLocationOnScreen(long j, int[] iArr);

    private native int nativeGetMinHeight(long j);

    private native int nativeGetMinWidth(long j);

    private native int nativeGetMode(long j);

    private native int nativeGetModeResolved(long j);

    private native String nativeGetName(long j);

    private native int nativeGetPaddingBottom(long j);

    private native int nativeGetPaddingEnd(long j);

    private native int nativeGetPaddingLeft(long j);

    private native int nativeGetPaddingRight(long j);

    private native int nativeGetPaddingStart(long j);

    private native int nativeGetPaddingTop(long j);

    private native float nativeGetPivotX(long j);

    private native float nativeGetPivotY(long j);

    private native int nativeGetRight(long j);

    private native float nativeGetRotation(long j);

    private native float nativeGetRotationSensitivity(long j);

    private native float nativeGetScaleX(long j);

    private native float nativeGetScaleY(long j);

    private native int nativeGetScrollX(long j);

    private native int nativeGetScrollY(long j);

    private native int nativeGetScrollbarBackgroundColor(long j);

    private native int nativeGetScrollbarColor(long j);

    private native int nativeGetScrollbarFadingDelay(long j);

    private native int nativeGetScrollbarFadingDuration(long j);

    private native float nativeGetScrollbarRadius(long j);

    private native boolean nativeGetScrollbarRoundRect(long j);

    private native float nativeGetScrollbarStartAngle(long j);

    private native float nativeGetScrollbarSweepAngle(long j);

    private native int nativeGetScrollbarThickness(long j);

    private native int nativeGetTop(long j);

    private native float nativeGetTranslationX(long j);

    private native float nativeGetTranslationY(long j);

    private native int nativeGetUserNextFocus(long j, int i);

    private native int[] nativeGetVerticalPosition(long j);

    private native long nativeGetViewHandle();

    private native int nativeGetVisibility(long j);

    private native int nativeGetWidth(long j);

    private native boolean nativeGetWindowVisibleRect(long j, Rect rect);

    private native boolean nativeHasFocus(long j);

    private native void nativeInvalidate(long j);

    private native boolean nativeIsAxisXScrollBarEnabled(long j);

    private native boolean nativeIsAxisYScrollBarEnabled(long j);

    private native boolean nativeIsBoundToWindow(long j);

    private native boolean nativeIsClickable(long j);

    private native boolean nativeIsComponentDisplayed(long j);

    private native boolean nativeIsEnabled(long j);

    private native boolean nativeIsFocusable(long j);

    private native boolean nativeIsFocused(long j);

    private native boolean nativeIsLongClickOn(long j);

    private native boolean nativeIsPressed(long j);

    private native boolean nativeIsRtl(long j);

    private native boolean nativeIsScrollbarFadingOn(long j);

    private native boolean nativeIsScrollbarOverlapEnabled(long j);

    private native boolean nativeIsSelected(long j);

    private native boolean nativeIsTouchFocusable(long j);

    private native boolean nativeIsVibrationEffectEnabled(long j);

    private native void nativeNotifyAccessibility(long j, int i);

    private native void nativeObjectBind(long j);

    private native boolean nativePerformClick(long j);

    private native boolean nativePerformNewDrag(long j);

    private native boolean nativePerformScale(long j);

    private native void nativeRegisterEstimateSize(long j, EstimateSizeListener estimateSizeListener);

    private native void nativeRemoveOnAttachStateChangeCallback(long j, int i);

    private native boolean nativeRequestFocus(long j);

    private native void nativeRequestLayout(long j);

    private native void nativeScrollBy(long j, int i, int i2);

    private native void nativeScrollTo(long j, int i, int i2);

    private native void nativeSetAccessibility(long j, int i);

    private native void nativeSetAlpha(long j, float f);

    private native void nativeSetAxisXScrollBarEnabled(long j, boolean z);

    private native void nativeSetAxisYScrollBarEnabled(long j, boolean z);

    private native void nativeSetBackground(long j, long j2);

    private native void nativeSetBadgeInfo(long j, String[] strArr, String[] strArr2);

    private native void nativeSetBarrierfreeDescription(long j, String str);

    private native void nativeSetBarrierfreeFocusCallback(long j, AccessibilityFocusListener accessibilityFocusListener);

    private native void nativeSetBottom(long j, int i);

    private native void nativeSetBoundaryFadeEffectEnable(long j, boolean z);

    private native void nativeSetCanAcceptScrollListener(long j, CanAcceptScrollListener canAcceptScrollListener);

    private native void nativeSetCenterZoomFactor(long j, float f, float f2);

    private native void nativeSetCentralScrollMode(long j, boolean z);

    private native void nativeSetClickable(long j, boolean z);

    private native void nativeSetClipEnabled(long j, boolean z);

    private native void nativeSetComponentPosition(long j, int i, int i2, int i3, int i4);

    private native void nativeSetComponentSize(long j, int i, int i2);

    private native void nativeSetContentDescription(long j, String str);

    private native void nativeSetContentEnable(long j, boolean z);

    private native void nativeSetContentPosition(long j, float f, float f2);

    private native void nativeSetContentPositionX(long j, float f);

    private native void nativeSetContentPositionY(long j, float f);

    private native void nativeSetCornerMark(long j, long j2);

    private native void nativeSetDragAcceptAngle(long j, float f);

    private native void nativeSetEnabled(long j, boolean z);

    private native void nativeSetEstimatedSize(long j, int i, int i2);

    private native void nativeSetFadeEffectBoundaryWidth(long j, int i);

    private native void nativeSetFadeEffectColor(long j, int i);

    private native void nativeSetFocusBorderEnable(long j, boolean z);

    private native void nativeSetFocusBorderPadding(long j, int i);

    private native void nativeSetFocusBorderRadii(long j, float[] fArr);

    private native void nativeSetFocusBorderWidth(long j, int i);

    private native void nativeSetFocusable(long j, int i);

    private native void nativeSetForeground(long j, long j2);

    private native void nativeSetForegroundGravity(long j, int i);

    private native void nativeSetGesturePriorityCallback(long j, int i, int i2);

    private native void nativeSetHeight(long j, int i);

    private native void nativeSetHorizontalPosition(long j, int i, int i2);

    private native void nativeSetId(long j, int i);

    private native void nativeSetLayoutDirection(long j, int i);

    private native void nativeSetLeft(long j, int i);

    private native void nativeSetLongClickable(long j, boolean z);

    private native void nativeSetMinHeight(long j, int i);

    private native void nativeSetMinWidth(long j, int i);

    private native void nativeSetMode(long j, int i);

    private native void nativeSetName(long j, String str);

    private native void nativeSetObserverManager(long j);

    private native void nativeSetOnClickCallback(long j, ClickedListener clickedListener);

    private native void nativeSetOnDoubleClickCallback(long j, DoubleClickedListener doubleClickedListener);

    private native void nativeSetOnDragCallback(long j, int i, DraggedListener draggedListener);

    private native void nativeSetOnFocusChangedCallback(long j, FocusChangedListener focusChangedListener);

    private native void nativeSetOnForwardTouchCallback(long j, InnerForwardTouchListener innerForwardTouchListener);

    private native void nativeSetOnKeyCallback(long j, KeyEventListener keyEventListener);

    private native void nativeSetOnLayoutRefreshListener(long j, LayoutRefreshedListener layoutRefreshedListener);

    private native void nativeSetOnLongClickCallback(long j, LongClickedListener longClickedListener);

    private native void nativeSetOnRotationCallback(long j, RotationEventListener rotationEventListener);

    private native void nativeSetOnScaleCallback(long j, ScaledListener scaledListener);

    private native void nativeSetOnTouchEventCallback(long j, InnerTouchEventListener innerTouchEventListener);

    private native void nativeSetOnViewStateChangedListener(long j, ComponentStateChangedListener componentStateChangedListener);

    private native void nativeSetOnVoiceEventCallback(long j, SpeechEventListener speechEventListener);

    private native void nativeSetPadding(long j, int i, int i2, int i3, int i4);

    private native void nativeSetPaddingRelative(long j, int i, int i2, int i3, int i4);

    private native void nativeSetPivotX(long j, float f);

    private native void nativeSetPivotY(long j, float f);

    private native void nativeSetPosition(long j, int i, int i2);

    private native void nativeSetPressed(long j, boolean z);

    private native void nativeSetRight(long j, int i);

    private native void nativeSetRotation(long j, float f);

    private native void nativeSetRotationSensitivity(long j, float f);

    private native void nativeSetScaleX(long j, float f);

    private native void nativeSetScaleY(long j, float f);

    private native void nativeSetScrollbarBackgroundColor(long j, int i);

    private native void nativeSetScrollbarColor(long j, int i);

    private native void nativeSetScrollbarFadingDelay(long j, int i);

    private native void nativeSetScrollbarFadingDuration(long j, int i);

    private native void nativeSetScrollbarFadingEnabled(long j, boolean z);

    private native void nativeSetScrollbarOverlapEnabled(long j, boolean z);

    private native void nativeSetScrollbarRadius(long j, float f);

    private native void nativeSetScrollbarRoundRect(long j, boolean z);

    private native void nativeSetScrollbarStartAngle(long j, float f);

    private native void nativeSetScrollbarSweepAngle(long j, float f);

    private native void nativeSetScrollbarThickness(long j, int i);

    private native void nativeSetSelected(long j, boolean z);

    private native void nativeSetSynonyms(long j, String[] strArr);

    private native void nativeSetTop(long j, int i);

    private native void nativeSetTouchFocusable(long j, boolean z);

    private native void nativeSetTranslationX(long j, float f);

    private native void nativeSetTranslationY(long j, float f);

    private native void nativeSetUserNextFocus(long j, int i, int i2);

    private native void nativeSetVerticalPosition(long j, int i, int i2);

    private native void nativeSetVibrationEffectEnabled(long j, boolean z);

    private native void nativeSetVisibility(long j, int i);

    private native void nativeSetWidth(long j, int i);

    private native boolean nativeStartDragAndDrop(long j, long j2);

    private native void nativeSubscribeRtlPropertiesChangedCallback(long j);

    private native void nativeSubscribeVoiceEvent(long j);

    private native void nativeUnsubscribeRtlPropertiesChangedCallback(long j);

    private native void nativeUnsubscribeVoiceEvents(long j);

    /* access modifiers changed from: protected */
    public void onRtlChanged(LayoutDirection layoutDirection) {
    }

    public void release() {
    }

    static {
        System.loadLibrary("agp.z");
    }

    public enum FadeEffectEnum {
        FADEEFFECT_NULL(1),
        FADEEFFECT_SCROLLBAR(2),
        FADEEFFECT_BOUNDARY(3);
        
        private final int nativeValue;

        private FadeEffectEnum(int i) {
            this.nativeValue = i;
        }

        /* access modifiers changed from: protected */
        public int getValue() {
            return this.nativeValue;
        }

        public static FadeEffectEnum getByInt(int i) {
            return (FadeEffectEnum) Arrays.stream(values()).filter(new Predicate(i) {
                /* class ohos.agp.components.$$Lambda$Component$FadeEffectEnum$4sb9qlGMVlHYYdllsk6WK9dtf4 */
                private final /* synthetic */ int f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return Component.FadeEffectEnum.lambda$getByInt$0(this.f$0, (Component.FadeEffectEnum) obj);
                }
            }).findAny().orElse(FADEEFFECT_NULL);
        }

        static /* synthetic */ boolean lambda$getByInt$0(int i, FadeEffectEnum fadeEffectEnum) {
            return fadeEffectEnum.getValue() == i;
        }
    }

    /* access modifiers changed from: protected */
    public static class ComponentCleaner implements MemoryCleaner {
        protected long mNativePtr;

        private native void nativeViewRelease(long j);

        public ComponentCleaner(long j) {
            this.mNativePtr = j;
        }

        @Override // ohos.agp.utils.MemoryCleaner
        public void run() {
            long j = this.mNativePtr;
            if (j != 0) {
                nativeViewRelease(j);
                this.mNativePtr = 0;
            }
        }
    }

    private static class TouchEventProxy extends TouchEvent {
        private TouchEvent mBase;
        private boolean mIsCancelEvent = false;

        TouchEventProxy(TouchEvent touchEvent) {
            this.mBase = touchEvent;
        }

        /* access modifiers changed from: package-private */
        public void wrapAsCancel() {
            this.mIsCancelEvent = true;
        }

        @Override // ohos.multimodalinput.event.TouchEvent
        public int getAction() {
            if (this.mIsCancelEvent) {
                return 6;
            }
            return this.mBase.getAction();
        }

        @Override // ohos.multimodalinput.event.TouchEvent
        public int getIndex() {
            return this.mBase.getIndex();
        }

        @Override // ohos.multimodalinput.event.TouchEvent
        public float getForcePrecision() {
            return this.mBase.getForcePrecision();
        }

        @Override // ohos.multimodalinput.event.TouchEvent
        public float getMaxForce() {
            return this.mBase.getMaxForce();
        }

        @Override // ohos.multimodalinput.event.TouchEvent
        public int getTapCount() {
            return this.mBase.getTapCount();
        }

        @Override // ohos.multimodalinput.event.ManipulationEvent
        public long getStartTime() {
            return this.mBase.getStartTime();
        }

        @Override // ohos.multimodalinput.event.ManipulationEvent
        public int getPhase() {
            if (this.mIsCancelEvent) {
                return 4;
            }
            return this.mBase.getPhase();
        }

        @Override // ohos.multimodalinput.event.ManipulationEvent
        public MmiPoint getPointerPosition(int i) {
            return this.mBase.getPointerPosition(i);
        }

        @Override // ohos.multimodalinput.event.ManipulationEvent
        public void setScreenOffset(float f, float f2) {
            this.mBase.setScreenOffset(f, f2);
        }

        @Override // ohos.multimodalinput.event.ManipulationEvent
        public MmiPoint getPointerScreenPosition(int i) {
            return this.mBase.getPointerScreenPosition(i);
        }

        @Override // ohos.multimodalinput.event.ManipulationEvent
        public int getPointerCount() {
            return this.mBase.getPointerCount();
        }

        @Override // ohos.multimodalinput.event.ManipulationEvent
        public int getPointerId(int i) {
            return this.mBase.getPointerId(i);
        }

        @Override // ohos.multimodalinput.event.ManipulationEvent
        public float getForce(int i) {
            return this.mBase.getForce(i);
        }

        @Override // ohos.multimodalinput.event.ManipulationEvent
        public float getRadius(int i) {
            return this.mBase.getRadius(i);
        }

        @Override // ohos.multimodalinput.event.MultimodalEvent
        public int getSourceDevice() {
            return this.mBase.getSourceDevice();
        }

        @Override // ohos.multimodalinput.event.MultimodalEvent
        public String getDeviceId() {
            return this.mBase.getDeviceId();
        }

        @Override // ohos.multimodalinput.event.MultimodalEvent
        public int getInputDeviceId() {
            return this.mBase.getInputDeviceId();
        }

        @Override // ohos.multimodalinput.event.MultimodalEvent
        public long getOccurredTime() {
            return this.mBase.getOccurredTime();
        }
    }

    public Component(Context context) {
        this(context, null);
    }

    public Component(Context context, AttrSet attrSet) {
        this(context, attrSet, (String) null);
    }

    public Component(Context context, AttrSet attrSet, String str) {
        this(context, attrSet, Style.getPatternId(context, str));
    }

    public Component(Context context, AttrSet attrSet, int i) {
        this.mNativeViewPtr = 0;
        this.mComponentParent = null;
        this.mBackgroundElement = null;
        this.mForegroundElement = null;
        this.mObserverList = new ArrayList<>();
        this.mEstimateSizeListener = null;
        this.mDrawTaskUnderContent = null;
        this.mCanvasForTaskUnderContent = null;
        this.mDrawTaskOverContent = null;
        this.mCanvasForTaskOverContent = null;
        this.mKeyEventListener = null;
        this.mRotationEventListener = null;
        this.mComponentTreeObserver = null;
        this.mTag = null;
        this.cornerMark = null;
        this.mComponentObserverManager = new ComponentObserverManager();
        this.mComponentHolderMap = new HashMap();
        this.mClickedListener = null;
        this.mDoubleClickedListener = null;
        this.mScaledListener = null;
        this.mOnDragListener = null;
        this.mDraggedListener = null;
        this.mFocusChangedListener = null;
        this.mLongClickedListener = null;
        this.mTouchEventListener = null;
        this.mInnerTouchListener = null;
        this.mForwardTouchListener = null;
        this.mInnerForwardTouchListener = null;
        this.mSpeechEventListener = null;
        this.mScrolledListener = null;
        this.mCanAcceptScrollListener = null;
        this.mScrolledListeners = new ArrayList<>();
        this.mScrolledListenerInternal = new ScrolledListener() {
            /* class ohos.agp.components.Component.AnonymousClass2 */

            @Override // ohos.agp.components.Component.ScrolledListener
            public void onContentScrolled(Component component, int i, int i2, int i3, int i4) {
                Iterator it = Component.this.mScrolledListeners.iterator();
                while (it.hasNext()) {
                    ((ScrolledListener) it.next()).onContentScrolled(component, i, i2, i3, i4);
                }
            }

            @Override // ohos.agp.components.Component.ScrolledListener
            public void scrolledStageUpdate(Component component, int i) {
                Iterator it = Component.this.mScrolledListeners.iterator();
                while (it.hasNext()) {
                    ((ScrolledListener) it.next()).scrolledStageUpdate(component, i);
                }
            }
        };
        this.mComponentStateChangedListener = null;
        this.mLayoutRefreshedListener = null;
        this.mAccessibilityFocusListener = null;
        this.mDraggableViewClipData = null;
        this.mShadowComponent = null;
        this.mOnBindStateChangeListeners = new ArrayList<>();
        createNativePtr();
        registerCleaner();
        this.mLayoutConfig = new ComponentContainer.LayoutConfig();
        this.mContext = context;
        long j = this.mNativeViewPtr;
        if (j != 0) {
            nativeObjectBind(j);
        }
        if (context == null) {
            HiLog.info(TAG, "Context is null", new Object[0]);
        }
        applyStyle(convertAttrToStyle(AttrHelper.mergeStyle(context, attrSet, i)));
        long j2 = this.mNativeViewPtr;
        if (j2 != 0) {
            nativeSetObserverManager(j2);
        }
    }

    public ComponentHolder findComponentHolderById(int i) {
        return this.mComponentHolderMap.getOrDefault(Integer.valueOf(i), null);
    }

    /* access modifiers changed from: protected */
    public void handleInvalidParams(String str) {
        ErrorHandler.handleInvalidParams(getClass().getName() + ": " + str);
    }

    /* access modifiers changed from: protected */
    public <T> boolean validateParam(T t, Predicate<T> predicate, String str) {
        return ErrorHandler.validateParam(t, predicate, getClass().getName() + ": " + str);
    }

    /* access modifiers changed from: protected */
    public Style convertAttrToStyle(AttrSet attrSet) {
        if (this.mAttrsConstants == null) {
            this.mAttrsConstants = AttrHelper.getComponentAttrsConstants();
        }
        for (int i = 0; i < attrSet.getLength(); i++) {
            attrSet.getAttr(i).ifPresent(new Consumer() {
                /* class ohos.agp.components.$$Lambda$Component$yk3OoPuikXHgLWZhm7YQqaNZcU */

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Component.this.lambda$convertAttrToStyle$0$Component((Attr) obj);
                }
            });
        }
        return Style.fromAttrSet(attrSet);
    }

    public /* synthetic */ void lambda$convertAttrToStyle$0$Component(Attr attr) {
        if (attr.getType() == Attr.AttrType.NONE) {
            attr.setType(this.mAttrsConstants.getType(attr.getName()));
            attr.setContext(this.mContext);
        }
    }

    public final boolean startDragAndDrop(MimeData mimeData, DragFeedbackProvider dragFeedbackProvider) {
        this.mShadowComponent = dragFeedbackProvider.getComponent();
        Component component = this.mShadowComponent;
        long nativeViewPtr = component != null ? component.getNativeViewPtr() : 0;
        getRoot().mDraggableViewClipData = mimeData;
        return nativeStartDragAndDrop(getNativeViewPtr(), nativeViewPtr);
    }

    private Component getRoot() {
        ComponentParent componentParent = getComponentParent();
        Component component = componentParent instanceof Component ? (Component) componentParent : null;
        while (true) {
            this = component;
            if (this == null) {
                return this;
            }
            ComponentParent componentParent2 = this.getComponentParent();
            if (!(componentParent2 instanceof Component)) {
                return this;
            }
            component = (Component) componentParent2;
        }
    }

    /* access modifiers changed from: protected */
    public void createNativePtr() {
        if (this.mNativeViewPtr == 0) {
            this.mNativeViewPtr = nativeGetViewHandle();
        }
    }

    /* access modifiers changed from: protected */
    public void registerCleaner() {
        MemoryCleanerRegistry.getInstance().registerWithNativeBind(this, new ComponentCleaner(this.mNativeViewPtr), this.mNativeViewPtr);
    }

    public final ComponentParent getComponentParent() {
        return this.mComponentParent;
    }

    /* access modifiers changed from: package-private */
    public void assignParent(ComponentParent componentParent) {
        if (equals(componentParent)) {
            throw new IllegalArgumentException("Not allowed to specify itself as parent");
        } else if (componentParent == null) {
            this.mComponentParent = null;
        } else if (this.mComponentParent == null) {
            this.mComponentParent = componentParent;
        } else {
            throw new IllegalArgumentException("component " + this + " being added, but it already has a parent");
        }
    }

    /* access modifiers changed from: protected */
    public void setEstimateSizeListener(EstimateSizeListener estimateSizeListener) {
        if (this.mEstimateSizeListener != estimateSizeListener) {
            this.mEstimateSizeListener = estimateSizeListener;
            nativeRegisterEstimateSize(this.mNativeViewPtr, this.mEstimateSizeListener);
        }
    }

    public void addDrawTask(DrawTask drawTask) {
        addDrawTask(drawTask, 2);
    }

    public void addDrawTask(DrawTask drawTask, int i) {
        HiLog.debug(TAG, "addDrawTask", new Object[0]);
        if (i == 1) {
            this.mDrawTaskUnderContent = drawTask;
            if (this.mCanvasForTaskUnderContent == null) {
                this.mCanvasForTaskUnderContent = new Canvas();
            }
            nativeAddDrawTaskUnderContent(this.mNativeViewPtr, this.mDrawTaskUnderContent, this.mCanvasForTaskUnderContent.getNativePtr());
        } else if (i != 2) {
            HiLog.error(TAG, "addDrawTask fail! Invalid number of layers.", new Object[0]);
        } else {
            this.mDrawTaskOverContent = drawTask;
            if (this.mCanvasForTaskOverContent == null) {
                this.mCanvasForTaskOverContent = new Canvas();
            }
            nativeAddDrawTaskOverContent(this.mNativeViewPtr, this.mDrawTaskOverContent, this.mCanvasForTaskOverContent.getNativePtr());
        }
    }

    public void invalidate() {
        nativeInvalidate(this.mNativeViewPtr);
    }

    public void setContentEnable(boolean z) {
        HiLog.debug(TAG, "setContentEnable", new Object[0]);
        nativeSetContentEnable(this.mNativeViewPtr, z);
    }

    public boolean getContentEnable() {
        HiLog.debug(TAG, "getContentEnable", new Object[0]);
        return nativeGetContentEnable(this.mNativeViewPtr);
    }

    public AnimatorProperty createAnimatorProperty() {
        return new AnimatorProperty(this);
    }

    public long getNativeViewPtr() {
        return this.mNativeViewPtr;
    }

    public Context getContext() {
        return this.mContext;
    }

    public void setPosition(int i, int i2) {
        nativeSetPosition(this.mNativeViewPtr, i, i2);
    }

    public void setContentPosition(float f, float f2) {
        nativeSetContentPosition(this.mNativeViewPtr, f, f2);
    }

    public void setContentPositionX(float f) {
        nativeSetContentPositionX(this.mNativeViewPtr, f);
    }

    public void setContentPositionY(float f) {
        nativeSetContentPositionY(this.mNativeViewPtr, f);
    }

    public float getContentPositionX() {
        return nativeGetContentPositionX(this.mNativeViewPtr);
    }

    public float getContentPositionY() {
        return nativeGetContentPositionY(this.mNativeViewPtr);
    }

    public float[] getContentPosition() {
        return new float[]{getContentPositionX(), getContentPositionY()};
    }

    public int[] getLocationOnScreen() {
        int[] iArr = new int[2];
        nativeGetLocationOnScreen(this.mNativeViewPtr, iArr);
        return iArr;
    }

    public void setWidth(int i) {
        if (this.mLayoutConfig.width != i) {
            this.mLayoutConfig.width = i;
            nativeSetWidth(this.mNativeViewPtr, i);
        }
    }

    public void setComponentSize(int i, int i2) {
        if (this.mLayoutConfig.width != i || this.mLayoutConfig.height != i2) {
            ComponentContainer.LayoutConfig layoutConfig = this.mLayoutConfig;
            layoutConfig.width = i;
            layoutConfig.height = i2;
            nativeSetComponentSize(this.mNativeViewPtr, i, i2);
        }
    }

    public DimensFloat getComponentSize() {
        int[] nativeGetComponentSize = nativeGetComponentSize(this.mNativeViewPtr);
        return new DimensFloat(nativeGetComponentSize[0], nativeGetComponentSize[1]);
    }

    public int getWidth() {
        return nativeGetWidth(this.mNativeViewPtr);
    }

    public void setHeight(int i) {
        if (this.mLayoutConfig.height != i) {
            this.mLayoutConfig.height = i;
            nativeSetHeight(this.mNativeViewPtr, i);
        }
    }

    public void setRotationSensitivity(float f) {
        nativeSetRotationSensitivity(this.mNativeViewPtr, f);
    }

    public float getRotationSensitivity() {
        return nativeGetRotationSensitivity(this.mNativeViewPtr);
    }

    public int getHeight() {
        return nativeGetHeight(this.mNativeViewPtr);
    }

    public int getLeft() {
        return nativeGetLeft(this.mNativeViewPtr);
    }

    public void setLeft(int i) {
        nativeSetLeft(this.mNativeViewPtr, i);
    }

    public int getRight() {
        return nativeGetRight(this.mNativeViewPtr);
    }

    public void setRight(int i) {
        nativeSetRight(this.mNativeViewPtr, i);
    }

    public int getTop() {
        return nativeGetTop(this.mNativeViewPtr);
    }

    public void setTop(int i) {
        nativeSetTop(this.mNativeViewPtr, i);
    }

    public int getBottom() {
        return nativeGetBottom(this.mNativeViewPtr);
    }

    public void setBottom(int i) {
        nativeSetBottom(this.mNativeViewPtr, i);
    }

    public void setComponentPosition(int i, int i2, int i3, int i4) {
        ErrorHandler.validateParamNonNegative((long) (i3 - i));
        ErrorHandler.validateParamNonNegative((long) (i4 - i2));
        nativeSetComponentPosition(this.mNativeViewPtr, i, i2, i3, i4);
    }

    public void setComponentPosition(Rect rect) {
        ErrorHandler.validateParamNotNull(rect);
        setComponentPosition(rect.left, rect.top, rect.right, rect.bottom);
    }

    public void setHorizontalPosition(int i, int i2) {
        ErrorHandler.validateParamNonNegative((long) (i2 - i));
        nativeSetHorizontalPosition(this.mNativeViewPtr, i, i2);
    }

    public void setVerticalPosition(int i, int i2) {
        ErrorHandler.validateParamNonNegative((long) (i2 - i));
        nativeSetVerticalPosition(this.mNativeViewPtr, i, i2);
    }

    public int[] getHorizontalPosition() {
        return nativeGetHorizontalPosition(this.mNativeViewPtr);
    }

    public int[] getVerticalPosition() {
        return nativeGetVerticalPosition(this.mNativeViewPtr);
    }

    public Rect getComponentPosition() {
        int[] nativeGetComponentPosition = nativeGetComponentPosition(this.mNativeViewPtr);
        return new Rect(nativeGetComponentPosition[0], nativeGetComponentPosition[1], nativeGetComponentPosition[2], nativeGetComponentPosition[3]);
    }

    public void setMarginLeft(int i) {
        if (this.mLayoutConfig.getMarginLeft() != i) {
            this.mLayoutConfig.setMarginLeft(i);
            setLayoutConfig(this.mLayoutConfig);
        }
    }

    public void setMarginTop(int i) {
        if (this.mLayoutConfig.getMarginTop() != i) {
            this.mLayoutConfig.setMarginTop(i);
            setLayoutConfig(this.mLayoutConfig);
        }
    }

    public void setMarginRight(int i) {
        if (this.mLayoutConfig.getMarginRight() != i) {
            this.mLayoutConfig.setMarginRight(i);
            setLayoutConfig(this.mLayoutConfig);
        }
    }

    public void setMarginBottom(int i) {
        if (this.mLayoutConfig.getMarginBottom() != i) {
            this.mLayoutConfig.setMarginBottom(i);
            setLayoutConfig(this.mLayoutConfig);
        }
    }

    public void setMarginsLeftAndRight(int i, int i2) {
        if (this.mLayoutConfig.getMarginLeft() != i || this.mLayoutConfig.getMarginRight() != i2) {
            this.mLayoutConfig.setMarginsLeftAndRight(i, i2);
            setLayoutConfig(this.mLayoutConfig);
        }
    }

    public void setMarginsTopAndBottom(int i, int i2) {
        if (this.mLayoutConfig.getMarginTop() != i || this.mLayoutConfig.getMarginBottom() != i2) {
            this.mLayoutConfig.setMarginsTopAndBottom(i, i2);
            setLayoutConfig(this.mLayoutConfig);
        }
    }

    public int getMarginLeft() {
        return this.mLayoutConfig.getMarginLeft();
    }

    public int getMarginTop() {
        return this.mLayoutConfig.getMarginTop();
    }

    public int getMarginRight() {
        return this.mLayoutConfig.getMarginRight();
    }

    public int getMarginBottom() {
        return this.mLayoutConfig.getMarginBottom();
    }

    public int[] getMargins() {
        return this.mLayoutConfig.getMargins();
    }

    public int[] getMarginsLeftAndRight() {
        return this.mLayoutConfig.getMarginsLeftAndRight();
    }

    public int[] getMarginsTopAndBottom() {
        return this.mLayoutConfig.getMarginsTopAndBottom();
    }

    @Deprecated
    public void setForegroundGravity(int i) {
        nativeSetForegroundGravity(this.mNativeViewPtr, i);
    }

    @Deprecated
    public int getForegroundGravity() {
        return nativeGetForegroundGravity(this.mNativeViewPtr);
    }

    public void setLayoutConfig(ComponentContainer.LayoutConfig layoutConfig) {
        if (layoutConfig != null) {
            try {
                Object clone = layoutConfig.clone();
                ComponentContainer.LayoutConfig layoutConfig2 = null;
                if (clone instanceof ComponentContainer.LayoutConfig) {
                    layoutConfig2 = (ComponentContainer.LayoutConfig) clone;
                }
                if (this.mComponentParent != null) {
                    layoutConfig2 = this.mComponentParent.verifyLayoutConfig(layoutConfig2);
                }
                this.mLayoutConfig = layoutConfig2;
            } catch (CloneNotSupportedException unused) {
                ComponentParent componentParent = this.mComponentParent;
                if (componentParent != null) {
                    layoutConfig = componentParent.verifyLayoutConfig(layoutConfig);
                }
                this.mLayoutConfig = layoutConfig;
            }
            ComponentContainer.LayoutConfig layoutConfig3 = this.mLayoutConfig;
            if (layoutConfig3 != null) {
                layoutConfig3.applyToComponent(this);
                return;
            }
            return;
        }
        throw new NullPointerException("Layout parameters cannot be null for " + this);
    }

    public ComponentContainer.LayoutConfig getLayoutConfig() {
        return this.mLayoutConfig;
    }

    public void setMinHeight(int i) {
        nativeSetMinHeight(this.mNativeViewPtr, i);
    }

    public int getMinHeight() {
        return nativeGetMinHeight(this.mNativeViewPtr);
    }

    public void setMinWidth(int i) {
        nativeSetMinWidth(this.mNativeViewPtr, i);
    }

    public int getMinWidth() {
        return nativeGetMinWidth(this.mNativeViewPtr);
    }

    public void setComponentMinSize(int i, int i2) {
        nativeSetMinWidth(this.mNativeViewPtr, i);
        nativeSetMinHeight(this.mNativeViewPtr, i2);
    }

    public DimensFloat getComponentMinSize() {
        return new DimensFloat(nativeGetMinWidth(this.mNativeViewPtr), nativeGetMinHeight(this.mNativeViewPtr));
    }

    public int getScrollValue(int i) {
        if (i == 0) {
            return nativeGetScrollX(this.mNativeViewPtr);
        }
        if (i == 1) {
            return nativeGetScrollY(this.mNativeViewPtr);
        }
        return 0;
    }

    public void setVisibility(int i) {
        if (i == 1 || i == 0 || i == 2) {
            nativeSetVisibility(this.mNativeViewPtr, i);
        }
    }

    public int getVisibility() {
        return nativeGetVisibility(this.mNativeViewPtr);
    }

    public void addAvailabilityObserver(AvailabilityObserver availabilityObserver) {
        if (availabilityObserver != null) {
            this.mObserverList.add(availabilityObserver);
        }
    }

    public void removeAvailabilityObserver(AvailabilityObserver availabilityObserver) {
        if (availabilityObserver != null) {
            this.mObserverList.remove(availabilityObserver);
        }
    }

    public void announceAccessibility(String str) {
        nativeAnnounceAccessibility(this.mNativeViewPtr, str);
    }

    public void setAccessibilityDescription(String str) {
        nativeSetBarrierfreeDescription(this.mNativeViewPtr, str);
    }

    public String getAccessibilityDescription() {
        return nativeGetBarrierfreeDescription(this.mNativeViewPtr);
    }

    public void notifyAccessibility(int i) {
        nativeNotifyAccessibility(this.mNativeViewPtr, i);
    }

    public void setAccessibility(int i) {
        nativeSetAccessibility(this.mNativeViewPtr, i);
    }

    public int getAccessibility() {
        return nativeGetAccessibility(this.mNativeViewPtr);
    }

    /* access modifiers changed from: protected */
    public void notifyAllForRemove() {
        Iterator<AvailabilityObserver> it = this.mObserverList.iterator();
        while (it.hasNext()) {
            it.next().onComponentRemoved(this);
        }
    }

    public boolean isBoundToWindow() {
        return nativeIsBoundToWindow(this.mNativeViewPtr);
    }

    public boolean isComponentDisplayed() {
        return nativeIsComponentDisplayed(this.mNativeViewPtr);
    }

    public void setComponentStateChangedListener(ComponentStateChangedListener componentStateChangedListener) {
        this.mComponentStateChangedListener = componentStateChangedListener;
        nativeSetOnViewStateChangedListener(this.mNativeViewPtr, this.mComponentStateChangedListener);
    }

    public ComponentStateChangedListener getComponentStateChangedListener() {
        return this.mComponentStateChangedListener;
    }

    public void setClickedListener(ClickedListener clickedListener) {
        this.mClickedListener = clickedListener;
        nativeSetOnClickCallback(this.mNativeViewPtr, this.mClickedListener);
    }

    public ClickedListener getClickedListener() {
        return this.mClickedListener;
    }

    public void setDoubleClickedListener(DoubleClickedListener doubleClickedListener) {
        this.mDoubleClickedListener = doubleClickedListener;
        nativeSetOnDoubleClickCallback(this.mNativeViewPtr, this.mDoubleClickedListener);
    }

    public void setScaledListener(ScaledListener scaledListener) {
        this.mScaledListener = scaledListener;
        nativeSetOnScaleCallback(this.mNativeViewPtr, this.mScaledListener);
    }

    public ScaledListener getScaledListener() {
        return this.mScaledListener;
    }

    public void setGesturePriority(GestureType gestureType, int i) {
        nativeSetGesturePriorityCallback(this.mNativeViewPtr, gestureType.ordinal(), i);
    }

    public int getGesturePriority(GestureType gestureType) {
        return nativeGetGesturePriorityCallback(this.mNativeViewPtr, gestureType.ordinal());
    }

    public void setOnDragListener(OnDragListener onDragListener) {
        this.mOnDragListener = onDragListener;
    }

    public void setDraggedListener(int i, DraggedListener draggedListener) {
        this.mDraggedListener = draggedListener;
        nativeSetOnDragCallback(this.mNativeViewPtr, i, this.mDraggedListener);
    }

    public DraggedListener getDraggedListener() {
        return this.mDraggedListener;
    }

    public void setDragAcceptAngle(float f) {
        if (f < MIN_DRAG_ACCEPT_ANGLE || f > MAX_DRAG_ACCEPT_ANGLE) {
            HiLog.debug(TAG, "SetDragAcceptAngle input angle out of acceptable range", new Object[0]);
        } else {
            nativeSetDragAcceptAngle(this.mNativeViewPtr, f);
        }
    }

    public float getDragAcceptAngle() {
        return nativeGetDragAcceptAngle(this.mNativeViewPtr);
    }

    private boolean onDragFromNative(Component component, int i, float f, float f2) {
        return onDrag(component, DragEvent.obtain(i, f, f2, this.mDraggableViewClipData));
    }

    public boolean onDrag(Component component, DragEvent dragEvent) {
        boolean z = false;
        if (dragEvent == null) {
            return false;
        }
        if (dragEvent.getAction() == 5 && component == this) {
            this.mShadowComponent = null;
        }
        if (dragEvent.isBroadcast()) {
            OnDragListener onDragListener = this.mOnDragListener;
            if (onDragListener != null) {
                return onDragListener.onDrag(this, dragEvent);
            }
            return false;
        } else if (component == null) {
            return false;
        } else {
            OnDragListener onDragListener2 = component.mOnDragListener;
            if (onDragListener2 != null) {
                z = onDragListener2.onDrag(component, dragEvent);
            }
            if (z || dragEvent.getAction() != 6 || component.getComponentParent() == null) {
                return z;
            }
            ComponentParent componentParent = component.getComponentParent();
            return componentParent instanceof Component ? onDrag((Component) componentParent, dragEvent) : z;
        }
    }

    public void setFocusChangedListener(FocusChangedListener focusChangedListener) {
        this.mFocusChangedListener = focusChangedListener;
        nativeSetOnFocusChangedCallback(this.mNativeViewPtr, this.mFocusChangedListener);
    }

    public void setKeyEventListener(KeyEventListener keyEventListener) {
        this.mKeyEventListener = keyEventListener;
        nativeSetOnKeyCallback(this.mNativeViewPtr, this.mKeyEventListener);
    }

    public KeyEventListener getKeyEventListener() {
        return this.mKeyEventListener;
    }

    public void setRotationEventListener(RotationEventListener rotationEventListener) {
        this.mRotationEventListener = rotationEventListener;
        nativeSetOnRotationCallback(this.mNativeViewPtr, this.mRotationEventListener);
    }

    public RotationEventListener getRotationEventListener() {
        return this.mRotationEventListener;
    }

    public void setLongClickedListener(LongClickedListener longClickedListener) {
        this.mLongClickedListener = longClickedListener;
        nativeSetOnLongClickCallback(this.mNativeViewPtr, this.mLongClickedListener);
    }

    public void setTouchEventListener(TouchEventListener touchEventListener) {
        this.mTouchEventListener = touchEventListener;
        if (touchEventListener == null) {
            this.mInnerTouchListener = null;
        } else {
            this.mInnerTouchListener = new InnerTouchEventListener() {
                /* class ohos.agp.components.$$Lambda$Component$YwnZU7Hwf31fUJOQ52tK1xDD4 */

                @Override // ohos.agp.components.Component.InnerTouchEventListener
                public final boolean onTouchEvent(Component component, TouchEvent touchEvent, int i, int i2, boolean z) {
                    return Component.this.lambda$setTouchEventListener$1$Component(component, touchEvent, i, i2, z);
                }
            };
        }
        nativeSetOnTouchEventCallback(this.mNativeViewPtr, this.mInnerTouchListener);
    }

    public /* synthetic */ boolean lambda$setTouchEventListener$1$Component(Component component, TouchEvent touchEvent, int i, int i2, boolean z) {
        boolean z2;
        if (this.mTouchEventListener == null || touchEvent == null) {
            return false;
        }
        touchEvent.setScreenOffset((float) (-i), (float) (-i2));
        if (z) {
            TouchEventProxy touchEventProxy = new TouchEventProxy(touchEvent);
            touchEventProxy.wrapAsCancel();
            z2 = this.mTouchEventListener.onTouchEvent(component, touchEventProxy);
        } else {
            z2 = this.mTouchEventListener.onTouchEvent(component, touchEvent);
        }
        touchEvent.setScreenOffset((float) i, (float) i2);
        return z2;
    }

    private void setForwardTouchListener(ForwardTouchListener forwardTouchListener) {
        this.mForwardTouchListener = forwardTouchListener;
        if (forwardTouchListener == null) {
            this.mInnerForwardTouchListener = null;
        } else {
            this.mInnerForwardTouchListener = new InnerForwardTouchListener() {
                /* class ohos.agp.components.$$Lambda$Component$6UXBKLc_sWoYKERET8uSQXWH4E */

                @Override // ohos.agp.components.Component.InnerForwardTouchListener
                public final boolean onForwardTouch(Component component, TouchEvent touchEvent, int i, int i2) {
                    return Component.this.lambda$setForwardTouchListener$2$Component(component, touchEvent, i, i2);
                }
            };
        }
        nativeSetOnForwardTouchCallback(this.mNativeViewPtr, this.mInnerForwardTouchListener);
    }

    public /* synthetic */ boolean lambda$setForwardTouchListener$2$Component(Component component, TouchEvent touchEvent, int i, int i2) {
        if (this.mForwardTouchListener == null || touchEvent == null) {
            return true;
        }
        touchEvent.setScreenOffset((float) (-i), (float) (-i2));
        boolean onForwardTouch = this.mForwardTouchListener.onForwardTouch(component, touchEvent);
        touchEvent.setScreenOffset((float) i, (float) i2);
        return onForwardTouch;
    }

    private ForwardTouchListener getForwardTouchListener() {
        return this.mForwardTouchListener;
    }

    public TouchEventListener getTouchEventListener() {
        return this.mTouchEventListener;
    }

    public void setLayoutRefreshedListener(LayoutRefreshedListener layoutRefreshedListener) {
        this.mLayoutRefreshedListener = layoutRefreshedListener;
        nativeSetOnLayoutRefreshListener(this.mNativeViewPtr, this.mLayoutRefreshedListener);
    }

    public LayoutRefreshedListener getLayoutRefreshedListener() {
        return this.mLayoutRefreshedListener;
    }

    public /* synthetic */ boolean lambda$setScrolledListener$3$Component(ScrolledListener scrolledListener) {
        return scrolledListener.equals(this.mScrolledListener);
    }

    public void setScrolledListener(ScrolledListener scrolledListener) {
        this.mScrolledListeners.removeIf(new Predicate() {
            /* class ohos.agp.components.$$Lambda$Component$KmJQCt_U4tv_R3NW11Xs4XnMo */

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Component.this.lambda$setScrolledListener$3$Component((Component.ScrolledListener) obj);
            }
        });
        this.mScrolledListener = scrolledListener;
        addScrolledListener(scrolledListener);
    }

    public void addScrolledListener(ScrolledListener scrolledListener) {
        this.mScrolledListeners.add(scrolledListener);
        if (this.mScrolledListeners.size() == 1) {
            nativeEnableScrolledListener(this.mNativeViewPtr, true);
        }
    }

    public void removeScrolledListener(ScrolledListener scrolledListener) {
        this.mScrolledListeners.remove(scrolledListener);
        if (this.mScrolledListeners.size() == 0) {
            nativeEnableScrolledListener(this.mNativeViewPtr, false);
        }
    }

    public void setCanAcceptScrollListener(CanAcceptScrollListener canAcceptScrollListener) {
        this.mCanAcceptScrollListener = canAcceptScrollListener;
        nativeSetCanAcceptScrollListener(this.mNativeViewPtr, this.mCanAcceptScrollListener);
    }

    public boolean canScroll(int i) {
        return nativeCanScroll(this.mNativeViewPtr, i);
    }

    public boolean simulateClick() {
        return nativePerformClick(this.mNativeViewPtr);
    }

    public boolean performScale() {
        return nativePerformScale(this.mNativeViewPtr);
    }

    public boolean simulateDrag() {
        return nativePerformNewDrag(this.mNativeViewPtr);
    }

    public boolean callOnClick() {
        ClickedListener clickedListener = this.mClickedListener;
        if (clickedListener != null) {
            clickedListener.onClick(this);
            return true;
        }
        HiLog.error(TAG, "callOnClick fail, need to setClickedListener.", new Object[0]);
        return false;
    }

    public boolean executeLongClick() {
        return nativeExecuteLongClick(this.mNativeViewPtr);
    }

    public boolean executeDoubleClick() {
        return nativeExecuteDoubleClick(this.mNativeViewPtr);
    }

    public void setClickable(boolean z) {
        nativeSetClickable(this.mNativeViewPtr, z);
    }

    public boolean isClickable() {
        return nativeIsClickable(this.mNativeViewPtr);
    }

    public void setEnabled(boolean z) {
        nativeSetEnabled(this.mNativeViewPtr, z);
    }

    public void setClipEnabled(boolean z) {
        nativeSetClipEnabled(this.mNativeViewPtr, z);
    }

    public boolean getClipEnabled() {
        return nativeGetClipEnabled(this.mNativeViewPtr);
    }

    public boolean isEnabled() {
        return nativeIsEnabled(this.mNativeViewPtr);
    }

    public void setFocusable(int i) {
        nativeSetFocusable(this.mNativeViewPtr, i);
    }

    public void setTouchFocusable(boolean z) {
        nativeSetTouchFocusable(this.mNativeViewPtr, z);
    }

    public int getFocusable() {
        return nativeGetFocusable(this.mNativeViewPtr);
    }

    public void setId(int i) {
        nativeSetId(this.mNativeViewPtr, i);
    }

    public int getId() {
        return nativeGetId(this.mNativeViewPtr);
    }

    public void setName(String str) {
        nativeSetName(this.mNativeViewPtr, str);
    }

    public String getName() {
        return nativeGetName(this.mNativeViewPtr);
    }

    public void setLongClickable(boolean z) {
        nativeSetLongClickable(this.mNativeViewPtr, z);
    }

    public boolean isLongClickOn() {
        return nativeIsLongClickOn(this.mNativeViewPtr);
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        nativeSetPadding(this.mNativeViewPtr, i, i2, i3, i4);
    }

    public void setPaddingRelative(int i, int i2, int i3, int i4) {
        nativeSetPaddingRelative(this.mNativeViewPtr, i, i2, i3, i4);
    }

    public int getPaddingBottom() {
        return nativeGetPaddingBottom(this.mNativeViewPtr);
    }

    public int getPaddingEnd() {
        return nativeGetPaddingEnd(this.mNativeViewPtr);
    }

    public int getPaddingLeft() {
        return nativeGetPaddingLeft(this.mNativeViewPtr);
    }

    public int getPaddingRight() {
        return nativeGetPaddingRight(this.mNativeViewPtr);
    }

    public int getPaddingStart() {
        return nativeGetPaddingStart(this.mNativeViewPtr);
    }

    public int getPaddingTop() {
        return nativeGetPaddingTop(this.mNativeViewPtr);
    }

    public void setPaddingTop(int i) {
        setPadding(getPaddingLeft(), i, getPaddingRight(), getPaddingBottom());
    }

    public void setPaddingBottom(int i) {
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), i);
    }

    public void setPaddingLeft(int i) {
        setPadding(i, getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    public void setPaddingRight(int i) {
        setPadding(getPaddingLeft(), getPaddingTop(), i, getPaddingBottom());
    }

    public void setHorizontalPadding(int i, int i2) {
        setPadding(i, getPaddingTop(), i2, getPaddingBottom());
    }

    public void setVerticalPadding(int i, int i2) {
        setPadding(getPaddingLeft(), i, getPaddingRight(), i2);
    }

    public int[] getPadding() {
        return new int[]{getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom()};
    }

    public int[] getHorizontalPadding() {
        return new int[]{getPaddingLeft(), getPaddingRight()};
    }

    public int[] getVerticalPadding() {
        return new int[]{getPaddingTop(), getPaddingBottom()};
    }

    public void setPressState(boolean z) {
        nativeSetPressed(this.mNativeViewPtr, z);
    }

    public boolean isPressed() {
        return nativeIsPressed(this.mNativeViewPtr);
    }

    public void setSelected(boolean z) {
        nativeSetSelected(this.mNativeViewPtr, z);
    }

    public boolean isSelected() {
        return nativeIsSelected(this.mNativeViewPtr);
    }

    public boolean hasFocus() {
        return nativeHasFocus(this.mNativeViewPtr);
    }

    public boolean isFocusable() {
        return nativeIsFocusable(this.mNativeViewPtr);
    }

    public boolean isTouchFocusable() {
        return nativeIsTouchFocusable(this.mNativeViewPtr);
    }

    public boolean isFocused() {
        return nativeIsFocused(this.mNativeViewPtr);
    }

    public boolean requestFocus() {
        return nativeRequestFocus(this.mNativeViewPtr);
    }

    public void clearFocus() {
        nativeClearFocus(this.mNativeViewPtr);
    }

    public void postLayout() {
        nativeRequestLayout(this.mNativeViewPtr);
    }

    public void arrange(int i, int i2, int i3, int i4) {
        nativeArrange(this.mNativeViewPtr, i, i2, i3, i4);
    }

    public void estimateSize(int i, int i2) {
        nativeEstimateSize(this.mNativeViewPtr, i, i2);
    }

    /* access modifiers changed from: protected */
    public final void setEstimatedSize(int i, int i2) {
        nativeSetEstimatedSize(this.mNativeViewPtr, i, i2);
    }

    public final int getEstimatedWidth() {
        return nativeGetEstimatedWidth(this.mNativeViewPtr);
    }

    public final int getEstimatedHeight() {
        return nativeGetEstimatedHeight(this.mNativeViewPtr);
    }

    public void setBindStateChangedListener(BindStateChangedListener bindStateChangedListener) {
        if (this.mBindStateChangedListener == null) {
            this.mBindStateChangedListener = new BindStateChangedListener() {
                /* class ohos.agp.components.Component.AnonymousClass3 */

                @Override // ohos.agp.components.Component.BindStateChangedListener
                public void onComponentBoundToWindow(Component component) {
                    Iterator it = Component.this.mOnBindStateChangeListeners.iterator();
                    while (it.hasNext()) {
                        ((BindStateChangedListener) it.next()).onComponentBoundToWindow(component);
                    }
                }

                @Override // ohos.agp.components.Component.BindStateChangedListener
                public void onComponentUnboundFromWindow(Component component) {
                    Iterator it = Component.this.mOnBindStateChangeListeners.iterator();
                    while (it.hasNext()) {
                        ((BindStateChangedListener) it.next()).onComponentUnboundFromWindow(component);
                    }
                }
            };
            nativeAddOnAttachStateChangeCallback(this.mNativeViewPtr, this.mBindStateChangedListener);
        }
        this.mOnBindStateChangeListeners.add(bindStateChangedListener);
    }

    public void removeBindStateChangedListener(BindStateChangedListener bindStateChangedListener) {
        if (this.mOnBindStateChangeListeners.remove(bindStateChangedListener) && this.mOnBindStateChangeListeners.isEmpty()) {
            nativeRemoveOnAttachStateChangeCallback(this.mNativeViewPtr, 0);
            this.mBindStateChangedListener = null;
        }
    }

    public CornerMark bindCornerMark() {
        if (this.cornerMark == null) {
            this.cornerMark = new CornerMark(this.mContext);
            nativeSetCornerMark(this.mNativeViewPtr, this.cornerMark.getNativeCornerMarkPtr());
        }
        return this.cornerMark;
    }

    public CornerMark getCornerMark() {
        return this.cornerMark;
    }

    public void enableCornerMark(boolean z) {
        if (this.cornerMark != null) {
            nativeEnableCornerMark(this.mNativeViewPtr, z);
        }
    }

    public void setBackground(Element element) {
        this.mBackgroundElement = element;
        nativeSetBackground(this.mNativeViewPtr, element == null ? 0 : element.getNativeElementPtr());
    }

    public void setForeground(Element element) {
        this.mForegroundElement = element;
        nativeSetForeground(this.mNativeViewPtr, element == null ? 0 : element.getNativeElementPtr());
    }

    public Element getBackgroundElement() {
        return this.mBackgroundElement;
    }

    public Element getForegroundElement() {
        return this.mForegroundElement;
    }

    public void setFocusBorderEnable(boolean z) {
        nativeSetFocusBorderEnable(this.mNativeViewPtr, z);
    }

    public void setFocusBorderWidth(int i) {
        if (i <= 0) {
            HiLog.error(TAG, "width is invalid.", new Object[0]);
        } else {
            nativeSetFocusBorderWidth(this.mNativeViewPtr, i);
        }
    }

    public void setFocusBorderPadding(int i) {
        if (i < 0) {
            HiLog.error(TAG, "padding is invalid.", new Object[0]);
        } else {
            nativeSetFocusBorderPadding(this.mNativeViewPtr, i);
        }
    }

    public void setFocusBorderRadius(float f) {
        if (f < 0.0f) {
            HiLog.error(TAG, "radius is invalid.", new Object[0]);
            return;
        }
        float[] fArr = new float[8];
        Arrays.fill(fArr, f);
        setFocusBorderRadius(fArr);
    }

    public void setFocusBorderRadius(float[] fArr) {
        if (fArr.length != 8) {
            HiLog.error(TAG, "radii is invalid.", new Object[0]);
        } else {
            nativeSetFocusBorderRadii(this.mNativeViewPtr, fArr);
        }
    }

    public boolean getFocusBorderEnable() {
        return nativeGetFocusBorderEnable(this.mNativeViewPtr);
    }

    public int getFocusBorderWidth() {
        return nativeGetFocusBorderWidth(this.mNativeViewPtr);
    }

    public int getFocusBorderPadding() {
        return nativeGetFocusBorderPadding(this.mNativeViewPtr);
    }

    public float[] getFocusBorderRadius() {
        return nativeGetFocusBorderRadii(this.mNativeViewPtr);
    }

    public void setRotation(float f) {
        nativeSetRotation(this.mNativeViewPtr, f);
    }

    public void setPivotX(float f) {
        nativeSetPivotX(this.mNativeViewPtr, f);
    }

    public void setPivotY(float f) {
        nativeSetPivotY(this.mNativeViewPtr, f);
    }

    public void setPivot(float f, float f2) {
        nativeSetPivotX(this.mNativeViewPtr, f);
        nativeSetPivotY(this.mNativeViewPtr, f2);
    }

    public void setPivot(Point point) {
        nativeSetPivotX(this.mNativeViewPtr, point.getPointX());
        nativeSetPivotY(this.mNativeViewPtr, point.getPointY());
    }

    public void setScaleX(float f) {
        nativeSetScaleX(this.mNativeViewPtr, f);
    }

    public void setScaleY(float f) {
        nativeSetScaleY(this.mNativeViewPtr, f);
    }

    public void setScale(float f, float f2) {
        nativeSetScaleX(this.mNativeViewPtr, f);
        nativeSetScaleY(this.mNativeViewPtr, f2);
    }

    public void setTranslationX(float f) {
        nativeSetTranslationX(this.mNativeViewPtr, f);
    }

    public void setTranslationY(float f) {
        nativeSetTranslationY(this.mNativeViewPtr, f);
    }

    public void setTranslation(float f, float f2) {
        nativeSetTranslationX(this.mNativeViewPtr, f);
        nativeSetTranslationY(this.mNativeViewPtr, f2);
    }

    public float getRotation() {
        return nativeGetRotation(this.mNativeViewPtr);
    }

    public float getPivotX() {
        return nativeGetPivotX(this.mNativeViewPtr);
    }

    public float getPivotY() {
        return nativeGetPivotY(this.mNativeViewPtr);
    }

    public Point getPivot() {
        return new Point(nativeGetPivotX(this.mNativeViewPtr), nativeGetPivotY(this.mNativeViewPtr));
    }

    public float getScaleX() {
        return nativeGetScaleX(this.mNativeViewPtr);
    }

    public float getScaleY() {
        return nativeGetScaleY(this.mNativeViewPtr);
    }

    public DimensFloat getScale() {
        return new DimensFloat(nativeGetScaleX(this.mNativeViewPtr), nativeGetScaleY(this.mNativeViewPtr));
    }

    public float getTranslationX() {
        return nativeGetTranslationX(this.mNativeViewPtr);
    }

    public float getTranslationY() {
        return nativeGetTranslationY(this.mNativeViewPtr);
    }

    public DimensFloat getTranslation() {
        return new DimensFloat(nativeGetTranslationX(this.mNativeViewPtr), nativeGetTranslationY(this.mNativeViewPtr));
    }

    public ResourceManager getResourceManager() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getResourceManager();
    }

    public ComponentTreeObserver getComponentTreeObserver() {
        if (this.mComponentTreeObserver == null) {
            this.mComponentTreeObserver = new ComponentTreeObserver(this);
        }
        return this.mComponentTreeObserver;
    }

    public void scrollTo(int i, int i2) {
        nativeScrollTo(this.mNativeViewPtr, i, i2);
    }

    public void scrollBy(int i, int i2) {
        nativeScrollBy(this.mNativeViewPtr, i, i2);
    }

    public boolean getSelfVisibleRect(Rect rect) {
        if (rect == null) {
            return false;
        }
        return nativeGetLocalVisibleRect(this.mNativeViewPtr, rect);
    }

    public boolean getWindowVisibleRect(Rect rect) {
        if (rect != null) {
            return nativeGetWindowVisibleRect(this.mNativeViewPtr, rect);
        }
        HiLog.error(TAG, "getWindowVisibleRect visibleRect is null!", new Object[0]);
        return false;
    }

    public void setAlpha(float f) {
        nativeSetAlpha(this.mNativeViewPtr, f);
    }

    public float getAlpha() {
        return nativeGetAlpha(this.mNativeViewPtr);
    }

    public void setComponentDescription(CharSequence charSequence) {
        nativeSetContentDescription(this.mNativeViewPtr, charSequence.toString());
    }

    public CharSequence getComponentDescription() {
        return nativeGetContentDescription(this.mNativeViewPtr);
    }

    public Component findComponentById(int i) {
        if (getId() == i) {
            return this;
        }
        return null;
    }

    public Component findFocus() {
        Object find = CallbackHelper.find(nativeFindFocus(this.mNativeViewPtr));
        if (find instanceof Component) {
            return (Component) find;
        }
        return null;
    }

    public Component findNextFocusableComponent(int i) {
        if (i < 0 || i > 5) {
            return null;
        }
        Object find = CallbackHelper.find(nativeFindNextFocus(this.mNativeViewPtr, i));
        if (find instanceof Component) {
            return (Component) find;
        }
        return null;
    }

    public boolean findRequestNextFocus(int i) {
        if (i < 0 || i > 5) {
            return false;
        }
        return nativeFindRequestNextFocus(this.mNativeViewPtr, i);
    }

    public void setUserNextFocus(int i, int i2) {
        if (i >= 0 && i <= 5) {
            nativeSetUserNextFocus(this.mNativeViewPtr, i, i2);
        }
    }

    public int getUserNextFocus(int i) {
        if (i < 0 || i > 5) {
            return -1;
        }
        return nativeGetUserNextFocus(this.mNativeViewPtr, i);
    }

    public void subscribeVoiceEvents(VoiceEvent voiceEvent) {
        if (voiceEvent == null || this.mNativeViewPtr == 0) {
            HiLog.error(TAG, "subscribeVoiceEvents VoiceEvent or viewPtr is null.", new Object[0]);
        } else if (createVoiceEvent(voiceEvent.getSpeech(), voiceEvent.getScene(), true)) {
            setEventSynonyms(voiceEvent.getSynonyms());
            setEventBadges(voiceEvent.getBadge());
            HiLog.debug(TAG, "VoiceEvent sendDataToView", new Object[0]);
            nativeSubscribeVoiceEvent(this.mNativeViewPtr);
            if (this.mSpeechEventListener == null) {
                nativeSetOnVoiceEventCallback(this.mNativeViewPtr, new DefSpeechEventListener());
            }
        }
    }

    public void unsubscribeVoiceEvents() {
        nativeUnsubscribeVoiceEvents(this.mNativeViewPtr);
    }

    public void setSpeechEventListener(SpeechEventListener speechEventListener) {
        this.mSpeechEventListener = speechEventListener;
        nativeSetOnVoiceEventCallback(this.mNativeViewPtr, this.mSpeechEventListener);
    }

    public SpeechEventListener getSpeechEventListener() {
        return this.mSpeechEventListener;
    }

    public void setTag(Object obj) {
        this.mTag = obj;
    }

    public Object getTag() {
        return this.mTag;
    }

    public void enableScrollBar(int i, boolean z) {
        if (i == 0) {
            nativeSetAxisXScrollBarEnabled(this.mNativeViewPtr, z);
        }
        if (i == 1) {
            nativeSetAxisYScrollBarEnabled(this.mNativeViewPtr, z);
        }
    }

    public boolean isScrollBarOn(int i) {
        if (i == 0) {
            return nativeIsAxisXScrollBarEnabled(this.mNativeViewPtr);
        }
        if (i == 1) {
            return nativeIsAxisYScrollBarEnabled(this.mNativeViewPtr);
        }
        return false;
    }

    @Deprecated
    public void setScrollbarFadingEnabled(boolean z) {
        nativeSetScrollbarFadingEnabled(this.mNativeViewPtr, z);
    }

    @Deprecated
    public boolean isScrollbarFadingOn() {
        return nativeIsScrollbarFadingOn(this.mNativeViewPtr);
    }

    /* renamed from: ohos.agp.components.Component$4  reason: invalid class name */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$ohos$agp$components$Component$FadeEffectEnum = new int[FadeEffectEnum.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            /*
                ohos.agp.components.Component$FadeEffectEnum[] r0 = ohos.agp.components.Component.FadeEffectEnum.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.agp.components.Component.AnonymousClass4.$SwitchMap$ohos$agp$components$Component$FadeEffectEnum = r0
                int[] r0 = ohos.agp.components.Component.AnonymousClass4.$SwitchMap$ohos$agp$components$Component$FadeEffectEnum     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.agp.components.Component$FadeEffectEnum r1 = ohos.agp.components.Component.FadeEffectEnum.FADEEFFECT_SCROLLBAR     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.agp.components.Component.AnonymousClass4.$SwitchMap$ohos$agp$components$Component$FadeEffectEnum     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.agp.components.Component$FadeEffectEnum r1 = ohos.agp.components.Component.FadeEffectEnum.FADEEFFECT_BOUNDARY     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.agp.components.Component.AnonymousClass4.<clinit>():void");
        }
    }

    public void enableFadeEffect(FadeEffectEnum fadeEffectEnum) {
        int i = AnonymousClass4.$SwitchMap$ohos$agp$components$Component$FadeEffectEnum[fadeEffectEnum.ordinal()];
        if (i == 1) {
            nativeSetScrollbarFadingEnabled(this.mNativeViewPtr, true);
        } else if (i == 2) {
            nativeSetBoundaryFadeEffectEnable(this.mNativeViewPtr, true);
        }
    }

    public void disableFadeEffect(FadeEffectEnum fadeEffectEnum) {
        int i = AnonymousClass4.$SwitchMap$ohos$agp$components$Component$FadeEffectEnum[fadeEffectEnum.ordinal()];
        if (i == 1) {
            nativeSetScrollbarFadingEnabled(this.mNativeViewPtr, false);
        } else if (i == 2) {
            nativeSetBoundaryFadeEffectEnable(this.mNativeViewPtr, false);
        }
    }

    public boolean isFadeEffected(FadeEffectEnum fadeEffectEnum) {
        int i = AnonymousClass4.$SwitchMap$ohos$agp$components$Component$FadeEffectEnum[fadeEffectEnum.ordinal()];
        if (i == 1) {
            return nativeIsScrollbarFadingOn(this.mNativeViewPtr);
        }
        if (i != 2) {
            return false;
        }
        return nativeGetBoundaryFadeEffectEnable(this.mNativeViewPtr);
    }

    public void setScrollbarFadingDelay(int i) {
        nativeSetScrollbarFadingDelay(this.mNativeViewPtr, i);
    }

    public void setScrollbarRoundRect(boolean z) {
        nativeSetScrollbarRoundRect(this.mNativeViewPtr, z);
    }

    public void setScrollbarRadius(float f) {
        nativeSetScrollbarRadius(this.mNativeViewPtr, f);
    }

    public boolean getScrollbarRoundRect() {
        return nativeGetScrollbarRoundRect(this.mNativeViewPtr);
    }

    public float getScrollbarRadius() {
        return nativeGetScrollbarRadius(this.mNativeViewPtr);
    }

    public int getScrollbarFadingDelay() {
        return nativeGetScrollbarFadingDelay(this.mNativeViewPtr);
    }

    public void setScrollbarFadingDuration(int i) {
        nativeSetScrollbarFadingDuration(this.mNativeViewPtr, i);
    }

    public int getScrollbarFadingDuration() {
        return nativeGetScrollbarFadingDuration(this.mNativeViewPtr);
    }

    public void setScrollbarColor(Color color) {
        nativeSetScrollbarColor(this.mNativeViewPtr, color.getValue());
    }

    public Color getScrollbarColor() {
        return new Color(nativeGetScrollbarColor(this.mNativeViewPtr));
    }

    public void setScrollbarBackgroundColor(Color color) {
        nativeSetScrollbarBackgroundColor(this.mNativeViewPtr, color.getValue());
    }

    public Color getScrollbarBackgroundColor() {
        return new Color(nativeGetScrollbarBackgroundColor(this.mNativeViewPtr));
    }

    public void setScrollbarThickness(int i) {
        nativeSetScrollbarThickness(this.mNativeViewPtr, i);
    }

    public int getScrollbarThickness() {
        return nativeGetScrollbarThickness(this.mNativeViewPtr);
    }

    public void setScrollbarStartAngle(float f) {
        nativeSetScrollbarStartAngle(this.mNativeViewPtr, f);
    }

    public float getScrollbarStartAngle() {
        return nativeGetScrollbarStartAngle(this.mNativeViewPtr);
    }

    public void setScrollbarSweepAngle(float f) {
        nativeSetScrollbarSweepAngle(this.mNativeViewPtr, f);
    }

    public float getScrollbarSweepAngle() {
        return nativeGetScrollbarSweepAngle(this.mNativeViewPtr);
    }

    public void setScrollbarOverlapEnabled(boolean z) {
        nativeSetScrollbarOverlapEnabled(this.mNativeViewPtr, z);
    }

    public void setVibrationEffectEnabled(boolean z) {
        nativeSetVibrationEffectEnabled(this.mNativeViewPtr, z);
    }

    public boolean isVibrationEffectEnabled() {
        return nativeIsVibrationEffectEnabled(this.mNativeViewPtr);
    }

    public boolean isScrollbarOverlapEnabled() {
        return nativeIsScrollbarOverlapEnabled(this.mNativeViewPtr);
    }

    public boolean isRtl() {
        return nativeIsRtl(this.mNativeViewPtr);
    }

    public void applyStyle(Style style) {
        for (Map.Entry<String, BiConsumer<Component, Value>> entry : STYLE_METHOD_MAP.entrySet()) {
            if (style.hasProperty(entry.getKey())) {
                entry.getValue().accept(this, style.getPropertyValue(entry.getKey()));
            }
        }
        nativeApplyStyle(this.mNativeViewPtr, style.getNativeStylePtr());
    }

    public void setCentralScrollMode(boolean z) {
        nativeSetCentralScrollMode(this.mNativeViewPtr, z);
    }

    public boolean getCentralScrollMode() {
        return nativeGetCentralScrollMode(this.mNativeViewPtr);
    }

    public void setMode(int i) {
        nativeSetMode(this.mNativeViewPtr, i);
    }

    public int getMode() {
        return nativeGetMode(this.mNativeViewPtr);
    }

    public int getModeResolved() {
        return nativeGetModeResolved(this.mNativeViewPtr);
    }

    public void setCenterZoomFactor(float f, float f2) {
        nativeSetCenterZoomFactor(this.mNativeViewPtr, f, f2);
    }

    public float[] getCenterZoomFactor() {
        return nativeGetCenterZoomFactor(this.mNativeViewPtr);
    }

    public void informConfigurationChanged(Configuration configuration) {
        HiLog.debug(TAG, "informConfigurationChanged enter", new Object[0]);
        onAttributeConfigChanged(configuration);
    }

    /* access modifiers changed from: protected */
    public void onAttributeConfigChanged(Configuration configuration) {
        HiLog.debug(TAG, "onAttributeConfigChanged enter", new Object[0]);
    }

    public void setLayoutDirection(LayoutDirection layoutDirection) {
        nativeSetLayoutDirection(this.mNativeViewPtr, layoutDirection.ordinal());
    }

    public LayoutDirection getLayoutDirection() {
        int nativeGetLayoutDirection = nativeGetLayoutDirection(this.mNativeViewPtr);
        if (nativeGetLayoutDirection == 0) {
            return LayoutDirection.LTR;
        }
        if (nativeGetLayoutDirection == 1) {
            return LayoutDirection.RTL;
        }
        if (nativeGetLayoutDirection != 2) {
            return LayoutDirection.LOCALE;
        }
        return LayoutDirection.INHERIT;
    }

    public LayoutDirection getLayoutDirectionResolved() {
        return nativeGetLayoutDirectionResolved(this.mNativeViewPtr) == LayoutDirection.LTR.ordinal() ? LayoutDirection.LTR : LayoutDirection.RTL;
    }

    public void setFadeEffectBoundaryWidth(int i) {
        nativeSetFadeEffectBoundaryWidth(this.mNativeViewPtr, i);
    }

    public int getFadeEffectBoundaryWidth() {
        return nativeGetFadeEffectBoundaryWidth(this.mNativeViewPtr);
    }

    public void setFadeEffectColor(Color color) {
        nativeSetFadeEffectColor(this.mNativeViewPtr, color.getValue());
    }

    public Color getFadeEffectColor() {
        return new Color(nativeGetFadeEffectColor(this.mNativeViewPtr));
    }

    private void resolveLayoutParamsFromNative(int i) {
        ComponentContainer.LayoutConfig layoutConfig = this.mLayoutConfig;
        if (layoutConfig != null) {
            layoutConfig.resolveLayoutDirection(i != LayoutDirection.LTR.ordinal() ? LayoutDirection.RTL : LayoutDirection.LTR);
        }
    }

    private void onRtlPropertiesChangedFromNative(int i) {
        onRtlChanged(i == LayoutDirection.LTR.ordinal() ? LayoutDirection.LTR : LayoutDirection.RTL);
    }

    private boolean isLayoutRtlFromContext() {
        ResourceManager resourceManager;
        Configuration configuration;
        Context context = this.mContext;
        if (context == null || (resourceManager = context.getResourceManager()) == null || (configuration = resourceManager.getConfiguration()) == null) {
            return false;
        }
        return configuration.isLayoutRTL;
    }

    /* access modifiers changed from: package-private */
    public void subscribeRtlPropertiesChangedCallback() {
        nativeSubscribeRtlPropertiesChangedCallback(this.mNativeViewPtr);
    }

    /* access modifiers changed from: package-private */
    public void unsubscribeRtlPropertiesChangedCallback() {
        nativeUnsubscribeRtlPropertiesChangedCallback(this.mNativeViewPtr);
    }

    private boolean createVoiceEvent(String str, int i, boolean z) {
        if (str == null || this.mNativeViewPtr == 0) {
            return false;
        }
        HiLog.debug(TAG, "createVoiceEvent enter", new Object[0]);
        nativeCreateVoiceEvent(this.mNativeViewPtr, str, i, z);
        return true;
    }

    private void setEventSynonyms(List<String> list) {
        if (list != null && this.mNativeViewPtr != 0 && list.size() != 0) {
            HiLog.debug(TAG, "createVoiceEvent", new Object[0]);
            nativeSetSynonyms(this.mNativeViewPtr, (String[]) list.toArray(new String[list.size()]));
        }
    }

    private void setEventBadges(List<String[]> list) {
        if (!(list == null || this.mNativeViewPtr == 0 || list.size() == 0)) {
            int size = list.size();
            String[] strArr = new String[size];
            String[] strArr2 = new String[size];
            for (int i = 0; i < size; i++) {
                String[] strArr3 = list.get(i);
                strArr[i] = strArr3[0];
                strArr2[i] = strArr3[1];
            }
            HiLog.debug(TAG, "setEventBadges", new Object[0]);
            nativeSetBadgeInfo(this.mNativeViewPtr, strArr, strArr2);
        }
    }

    @Deprecated
    public static class MeasureSpec {
        public static final int ESTIMATED_STATE_BIT_MASK = -16777216;
        private static final int MODE_MASK = -1073741824;
        private static final int MODE_SHIFT = 30;
        public static final int NOT_EXCEED = Integer.MIN_VALUE;
        public static final int PRECISE = 1073741824;
        public static final int UNCONSTRAINT = 0;

        public static int getMeasureSpec(int i, int i2) {
            return (i & 1073741823) | (i2 & MODE_MASK);
        }

        public static int getMode(int i) {
            return i & MODE_MASK;
        }

        public static int getSize(int i) {
            return i & 1073741823;
        }

        public static int getSizeAndConfig(int i, int i2, int i3) {
            int mode = getMode(i2);
            int size = getSize(i2);
            if (mode == Integer.MIN_VALUE ? !(size == 0 || size >= i) : mode == 1073741824) {
                i = size;
            }
            return i | (-16777216 & i3);
        }
    }

    public static class EstimateSpec {
        public static final int ESTIMATED_STATE_BIT_MASK = -16777216;
        private static final int MODE_MASK = -1073741824;
        private static final int MODE_SHIFT = 30;
        public static final int NOT_EXCEED = Integer.MIN_VALUE;
        public static final int PRECISE = 1073741824;
        public static final int UNCONSTRAINT = 0;

        public static int getMode(int i) {
            return i & MODE_MASK;
        }

        public static int getSize(int i) {
            return i & 1073741823;
        }

        public static int getSizeWithMode(int i, int i2) {
            return (i & 1073741823) | (i2 & MODE_MASK);
        }

        public static int getChildSizeWithMode(int i, int i2, int i3) {
            int mode = getMode(i2);
            int size = getSize(i2);
            if (mode == Integer.MIN_VALUE ? !(size == 0 || size >= i) : mode == 1073741824) {
                i = size;
            }
            return i | (-16777216 & i3);
        }
    }

    public static class DragFeedbackProvider {
        private WeakReference<Component> mComponentWeakReference = null;

        public DragFeedbackProvider(Component component) {
            if (component != null) {
                this.mComponentWeakReference = new WeakReference<>(component);
            }
        }

        public void onProvideShadowMetrics(Point point, Point point2) {
            Component component = getComponent();
            if (component != null) {
                component.setWidth((int) point.position[0]);
                component.setHeight((int) point.position[1]);
                component.setLeft((int) point2.position[0]);
                component.setTop((int) point2.position[1]);
                return;
            }
            HiLog.error(Component.TAG, "fail! Asked for drag thumb metrics but no component", new Object[0]);
        }

        public final Component getComponent() {
            WeakReference<Component> weakReference = this.mComponentWeakReference;
            if (weakReference == null) {
                return null;
            }
            return weakReference.get();
        }
    }

    public static class VoiceEvent {
        private boolean isBadge;
        private List<String[]> mBadges;
        private int mScene;
        private final String mSpeech;
        private List<String> mSynonyms;

        public VoiceEvent(String str, int i, boolean z) {
            this.mSpeech = str;
            this.mScene = i;
            this.isBadge = z;
        }

        public VoiceEvent(String str) {
            this(str, 0, false);
        }

        public void addSynonyms(String str) {
            if (this.mSynonyms == null) {
                this.mSynonyms = new ArrayList();
            }
            this.mSynonyms.add(str);
        }

        public void setScene(int i) {
            this.mScene = i;
        }

        public void setBadge(boolean z) {
            this.isBadge = z;
        }

        public void addBadges(String str, String str2) {
            if (str != null && str2 != null) {
                String[] strArr = {str, str2};
                if (this.mBadges == null) {
                    this.mBadges = new ArrayList();
                }
                this.mBadges.add(strArr);
            }
        }

        public String getSpeech() {
            return this.mSpeech;
        }

        public List<String> getSynonyms() {
            return this.mSynonyms;
        }

        public List<String[]> getBadge() {
            return this.mBadges;
        }

        public int getScene() {
            return this.mScene;
        }

        public void sendDataToComponent(Component component) {
            HiLog.debug(Component.TAG, "VoiceEvent sendDataToView", new Object[0]);
            component.subscribeVoiceEvents(this);
        }
    }

    public static class DefSpeechEventListener implements SpeechEventListener {
        @Override // ohos.agp.components.Component.SpeechEventListener
        public boolean onSpeechEvent(Component component, SpeechEvent speechEvent) {
            if (component == null || speechEvent == null || speechEvent.getAction() != 3) {
                return false;
            }
            return component.callOnClick();
        }
    }

    private static class ComponentObserverManager {
        private final Map<String, ComponentObserverHandler<?>> observerHandlerMap;

        private ComponentObserverManager() {
            this.observerHandlerMap = new HashMap();
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void addObserverHandler(ComponentObserverHandler<?> componentObserverHandler) {
            if (this.observerHandlerMap.containsKey(componentObserverHandler.getClass().getName())) {
                HiLog.debug(Component.TAG, "Observer Manager already contain this Handler.", new Object[0]);
            } else {
                this.observerHandlerMap.put(componentObserverHandler.getClass().getName(), componentObserverHandler);
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void removeObserverHandler(ComponentObserverHandler<?> componentObserverHandler) {
            if (!this.observerHandlerMap.containsKey(componentObserverHandler.getClass().getName())) {
                HiLog.debug(Component.TAG, "Observer Manager not contain this Handler.", new Object[0]);
            } else {
                this.observerHandlerMap.remove(componentObserverHandler.getClass().getName());
            }
        }

        private void onChange(String str, int[] iArr) {
            if (this.observerHandlerMap.containsKey(str)) {
                this.observerHandlerMap.get(str).onChange(iArr);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void addObserverHandler(ComponentObserverHandler<?> componentObserverHandler) {
        this.mComponentObserverManager.addObserverHandler(componentObserverHandler);
    }

    /* access modifiers changed from: protected */
    public void removeObserverHandler(ComponentObserverHandler<?> componentObserverHandler) {
        this.mComponentObserverManager.removeObserverHandler(componentObserverHandler);
    }

    @Deprecated
    public void setBoundaryFadeEffectEnable(boolean z) {
        nativeSetBoundaryFadeEffectEnable(this.mNativeViewPtr, z);
    }

    @Deprecated
    public boolean isBoundaryFadeEffectEnable() {
        return nativeGetBoundaryFadeEffectEnable(this.mNativeViewPtr);
    }

    public float getBoundaryFadeEffectLeftRate() {
        return nativeGetBoundaryFadeEffectRate(this.mNativeViewPtr, 0);
    }

    public float getBoundaryFadeEffectTopRate() {
        return nativeGetBoundaryFadeEffectRate(this.mNativeViewPtr, 1);
    }

    public float getBoundaryFadeEffectRightRate() {
        return nativeGetBoundaryFadeEffectRate(this.mNativeViewPtr, 2);
    }

    public float getBoundaryFadeEffectBottomRate() {
        return nativeGetBoundaryFadeEffectRate(this.mNativeViewPtr, 3);
    }

    public void setAccessibilityFocusListener(AccessibilityFocusListener accessibilityFocusListener) {
        this.mAccessibilityFocusListener = accessibilityFocusListener;
        nativeSetBarrierfreeFocusCallback(this.mNativeViewPtr, this.mAccessibilityFocusListener);
    }

    public AccessibilityFocusListener getAccessibilityFocusListener() {
        return this.mAccessibilityFocusListener;
    }
}
