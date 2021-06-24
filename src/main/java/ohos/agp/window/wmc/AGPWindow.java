package ohos.agp.window.wmc;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.session.MediaController;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewRootImpl;
import android.view.ViewRootImplEx;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputContentInfo;
import android.widget.FrameLayout;
import com.huawei.android.view.WindowManagerEx;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import ohos.aafwk.utils.log.LogDomain;
import ohos.accessibility.BarrierFreeInnerClient;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.RootContainerView;
import ohos.agp.components.Text;
import ohos.agp.utils.Rect;
import ohos.agp.vsync.VsyncScheduler;
import ohos.agp.window.aspbshell.AGPContainerView;
import ohos.agp.window.aspbshell.AGPWindowInternal;
import ohos.agp.window.aspbshell.TextInputConnection;
import ohos.agp.window.service.ComponentPadding;
import ohos.agp.window.service.IApplyComponentPaddingListener;
import ohos.agp.window.view.AGPSurfaceControl;
import ohos.agp.window.view.WindowInsetsWrapper;
import ohos.agp.window.wmc.AGPEngineAdapter;
import ohos.agp.window.wmc.AGPWindowManager;
import ohos.app.Context;
import ohos.bluetooth.BluetoothDeviceClass;
import ohos.bundle.AbilityInfo;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.ResourceManagerInner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.miscservices.inputmethod.EditingCapability;
import ohos.miscservices.inputmethod.InputDataChannel;
import ohos.miscservices.inputmethod.InputMethodController;
import ohos.miscservices.inputmethod.adapter.CompletionInfoAdapter;
import ohos.miscservices.inputmethod.adapter.ExtractedTextAdapter;
import ohos.miscservices.inputmethod.adapter.ExtractedTextRequestAdapter;
import ohos.miscservices.inputmethod.adapter.InputContentInfoAdapter;
import ohos.multimodalinput.event.KeyEvent;
import ohos.multimodalinput.event.MouseEvent;
import ohos.multimodalinput.event.MultimodalEvent;
import ohos.multimodalinput.event.RotationEvent;
import ohos.multimodalinput.event.TouchEvent;
import ohos.multimodalinput.eventimpl.MultimodalEventFactory;
import ohos.system.Parameters;

public class AGPWindow {
    private static final long GET_INPUTHEIGHT_DELAY = 200;
    private static final int ID_COPY = 16908321;
    private static final int ID_CUT = 16908320;
    private static final int ID_INVALID_FUNCTION = -1;
    private static final int ID_PASTE = 16908322;
    private static final int ID_SELECT_ALL = 16908319;
    private static final int IM_HIDDEN_HEIGHT = 0;
    private static final int INVALID_VISIBLILITY = -1;
    private static final HiLogLabel LABEL = new HiLogLabel(3, LogDomain.END, "AGPWindow");
    private static final int VIEW_REQUEST_FOCUS = 2;
    private static final int WINDOW_ANIMATION_NONE = -1;
    private static final float WINDOW_SURFACE_ALPHA = 0.0f;
    private static final int WINDOW_WIDTH_OFFSET = 0;
    private static final int WIN_NO_TRANSLUENT = 1;
    private static Context mBarrierfreeContext;
    private final boolean IS_WATCH;
    protected Rect boundRect;
    private boolean hasSetPreContentLayout;
    private boolean isTransparent;
    private WeakReference<Text> lastInputFocus;
    private boolean mACEWindowFlag;
    private AGPEngineAdapter mAGPEngine;
    private long mAGPMultiModel;
    protected AGPSurfaceControl mAGPSurfaceControl;
    private AGPWindowInternal mAGPWindowInternal;
    protected android.content.Context mAndroidContext;
    protected WindowManager.LayoutParams mAndroidParam;
    protected Window mAndroidWindow;
    private Color mBackgroundColor;
    protected Context mContext;
    private IAGPEngineAdapter mEngine;
    private int mEngineMode;
    protected int mFlag;
    private boolean mIsShowing;
    protected boolean mIsShown;
    private final Map<Integer, Component> mLayoutById;
    private RootContainerView mRootContainerView;
    protected AGPContainerView mSurfaceView;
    private TextInputConnection.ITextViewListener mTextViewListener;
    private ComponentContainer mViewGroup;
    private int mWindowAnimations;
    protected int mWindowFlag;
    private ComponentContainer mWindowRoot;
    protected boolean movable;
    protected Move move;
    private int preInputHeight;
    protected boolean swipeDismissEnabled;
    protected SwipeManager swipeManager;
    private boolean viewGroupResizedFlag;

    public static class LayoutParams {
        public static final int FIRST_SUB_WINDOW = 1000;
        public static final int FIRST_SYSTEM_WINDOW = 2000;
        public static final int INPUT_ADJUST_NOTHING = 48;
        public static final int INPUT_ADJUST_PAN = 32;
        public static final int INPUT_ADJUST_RESIZE = 16;
        public static final int INPUT_ADJUST_UNSPECIFIED = 0;
        public static final int INPUT_IS_FORWARD_NAVIGATION = 256;
        public static final int INPUT_MASK_ADJUST = 240;
        public static final int INPUT_MASK_STATE = 15;
        public static final int INPUT_STATE_ALWAYS_HIDDEN = 3;
        public static final int INPUT_STATE_ALWAYS_VISIBLE = 5;
        public static final int INPUT_STATE_HIDDEN = 2;
        public static final int INPUT_STATE_UNCHANGED = 1;
        public static final int INPUT_STATE_UNSPECIFIED = 0;
        public static final int INPUT_STATE_VISIBLE = 4;
        public static final int INVALID_WINDOW_TYPE = -1;
        public static final int START_REMOTE_INPUT_FLAG = 1;
        public static final int TYPE_APPLICATION = 2;
        public static final int TYPE_APPLICATION_MEDIA = 1001;
        public static final int TYPE_APPLICATION_OVERLAY = 2038;
        public static final int TYPE_APPLICATION_PANEL = 1000;
        public static final int TYPE_DREAM = 2023;
        public static final int TYPE_KEYGUARD = 2004;
        public static final int TYPE_NAVIGATION_BAR = 2019;
        public static final int TYPE_STATUS_BAR = 2000;
        public static final int TYPE_TOAST = 2005;
        public static final int TYPE_VOICE_INTERACTION = 2031;
        public float alpha = 1.0f;
        public float dimAmount = 1.0f;
        public int flags;
        public int format = -1;
        public int gravity;
        public int height;
        public int layoutInDisplaySideMode = 0;
        public float screenBrightness = -1.0f;
        public String title;
        public IBinder token;
        public int type;
        public int width;
        public int windowAnimations = -1;
        public int x;
        public int y;
    }

    public interface OnDismissListener {
        void onDismissed();
    }

    public interface OnSwipeChangedListener {
        void onSwipeCancelled();

        void onSwipeProgressChanged(float f, float f2);
    }

    private int aospToOhosFlags(int i) {
        return (i >>> 1) | (i << 31);
    }

    private void invalidate() {
    }

    private native void nativeDestroyMultiModel(long j);

    private native long nativeInitMultimodal();

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native boolean nativeSetWindowtoken(long j, IBinder iBinder);

    private int ohosToAospFlags(int i) {
        return (i << 1) | (i >>> 31);
    }

    private int reverseParams(int i) {
        return (i >>> 2) | (i << 30);
    }

    private int transitParams(int i) {
        return (i << 2) | (i >>> 30);
    }

    public void load() {
    }

    public void onSizeChange(Configuration configuration) {
    }

    public AGPWindow(android.content.Context context) {
        this.IS_WATCH = "watch".equals(Parameters.get("ro.build.characteristics", ""));
        this.swipeDismissEnabled = false;
        this.mIsShown = false;
        this.isTransparent = false;
        this.preInputHeight = 0;
        this.viewGroupResizedFlag = false;
        this.hasSetPreContentLayout = false;
        this.mIsShowing = false;
        this.mWindowAnimations = -1;
        this.mLayoutById = new HashMap(0);
        this.mACEWindowFlag = false;
        if (context != null) {
            this.mFlag = 10;
            createSurfaceView(context);
            this.mAGPWindowInternal = new AGPWindowInternal(context);
            this.mAndroidContext = context;
            this.mAGPWindowInternal.setContentView(this.mSurfaceView);
            this.mRootContainerView = new RootContainerView(this.mContext);
            return;
        }
        throw new AGPWindowManager.BadWindowException("AGPWindow: android Context is null");
    }

    public AGPWindow(Context context) {
        this(context, 1);
    }

    public AGPWindow(Context context, int i) {
        this.IS_WATCH = "watch".equals(Parameters.get("ro.build.characteristics", ""));
        this.swipeDismissEnabled = false;
        this.mIsShown = false;
        this.isTransparent = false;
        this.preInputHeight = 0;
        this.viewGroupResizedFlag = false;
        this.hasSetPreContentLayout = false;
        this.mIsShowing = false;
        this.mWindowAnimations = -1;
        this.mLayoutById = new HashMap(0);
        this.mACEWindowFlag = false;
        if (context != null) {
            this.mFlag = i;
            this.mContext = context;
            this.mRootContainerView = new RootContainerView(this.mContext);
            Object hostContext = context.getHostContext();
            if (hostContext instanceof android.content.Context) {
                this.mAndroidContext = (android.content.Context) hostContext;
                createSurfaceView(this.mAndroidContext);
                if (context.getAbilityInfo() == null || context.getAbilityInfo().getType() != AbilityInfo.AbilityType.PAGE) {
                    HiLog.debug(LABEL, "AGPWindow the context type is not page", new Object[0]);
                    this.mAndroidWindow = null;
                } else {
                    if (HiLog.isDebuggable()) {
                        HiLog.debug(LABEL, "AGPWindow the context type is page", new Object[0]);
                    }
                    this.mAndroidWindow = ((Activity) this.mAndroidContext).getWindow();
                }
                addOrClearFlag(1, 1);
                if (this.mFlag == 1) {
                    if (HiLog.isDebuggable()) {
                        HiLog.debug(LABEL, "AGPWindow mSurfaceView is set as content view", new Object[0]);
                    }
                    android.content.Context context2 = this.mAndroidContext;
                    if (context2 instanceof Activity) {
                        Activity activity = (Activity) context2;
                        FrameLayout frameLayout = new FrameLayout(activity);
                        View view = new View(activity);
                        view.setAlpha(0.0f);
                        frameLayout.addView(this.mSurfaceView, -1, -1);
                        frameLayout.addView(view, -1, -1);
                        activity.setContentView(frameLayout);
                        return;
                    }
                    HiLog.error(LABEL, "AGPWindow can not get activity", new Object[0]);
                    return;
                }
                return;
            }
            HiLog.error(LABEL, "AGPWindow context.getHostContext() is not android content instance", new Object[0]);
            return;
        }
        HiLog.error(LABEL, "agpWindow context is null", new Object[0]);
        throw new AGPWindowManager.BadWindowException("AGPWindow: context is null");
    }

    private void addOrClearFlag(int i, int i2) {
        Window window;
        if (this.IS_WATCH && (window = this.mAndroidWindow) != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.ohosFlags = (i & i2) | (attributes.ohosFlags & (~i2));
        }
    }

    private void createSurfaceView(android.content.Context context) {
        if (context == null) {
            HiLog.error(LABEL, "createSurfaceView failed, AGPWindow androidContext is null", new Object[0]);
            return;
        }
        this.mSurfaceView = new AGPContainerView(context);
        this.mSurfaceView.setZOrderMediaOverlay(true);
        this.mSurfaceView.setSurfaceListener(new SurfaceViewListener());
    }

    public long setEngine(int i, IAGPEngineAdapter iAGPEngineAdapter) {
        this.mEngineMode = i;
        this.mEngine = iAGPEngineAdapter;
        if (i == 3) {
            this.mAGPMultiModel = nativeInitMultimodal();
        } else {
            this.mAGPMultiModel = 0;
        }
        return this.mAGPMultiModel;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void createEngineAdapter(int i) {
        HiLog.debug(LABEL, "createEngineAdapter", new Object[0]);
        int i2 = this.mEngineMode;
        if (i2 == 2) {
            create3DEngineAdapter(i);
        } else if (i2 == 3) {
            HiLog.debug(LABEL, "Here Create ACE engine.", new Object[0]);
            if (this.mTextViewListener == null) {
                HiLog.error(LABEL, "ACE text view listener is null, input method events will not be processed.", new Object[0]);
            }
        } else {
            create2DEngineAdapter(i);
        }
    }

    private void create2DEngineAdapter(int i) {
        this.mTextViewListener = new TextViewListener();
        this.mSurfaceView.setInputChannelListener(this.mTextViewListener);
        HiLog.debug(LABEL, "Create 2D engine adapter.", new Object[0]);
        Window window = this.mAndroidWindow;
        this.mAGPEngine = new AGPEngineAdapter(this.mContext, i, window == null ? null : window.findViewById(16908290));
        this.mAGPMultiModel = nativeInitMultimodal();
        AGPEngineAdapter aGPEngineAdapter = this.mAGPEngine;
        this.mEngine = aGPEngineAdapter;
        if (this.isTransparent) {
            aGPEngineAdapter.setTransparent(true);
        }
        this.mAGPEngine.setInputListener(new AGPEngineAdapter.IAGPInputListener() {
            /* class ohos.agp.window.wmc.AGPWindow.AnonymousClass1 */

            @Override // ohos.agp.window.wmc.AGPEngineAdapter.IAGPInputListener
            public void onInputStart() {
                HiLog.debug(AGPWindow.LABEL, "onInputStart", new Object[0]);
                AGPWindow.this.startInput();
            }

            @Override // ohos.agp.window.wmc.AGPEngineAdapter.IAGPInputListener
            public void onInputStop() {
                HiLog.debug(AGPWindow.LABEL, "onInputStop", new Object[0]);
                AGPWindow.this.stopInput();
            }

            @Override // ohos.agp.window.wmc.AGPEngineAdapter.IAGPInputListener
            public void onNotifyFocus(int i) {
                HiLog.debug(AGPWindow.LABEL, "onNotifyFocus hasFocus =%{public}d", Integer.valueOf(i));
                AGPWindow.this.setFocusFlag(i);
            }
        });
    }

    private void create3DEngineAdapter(int i) {
        HiLog.debug(LABEL, "Create 3D engine adapter.", new Object[0]);
        VsyncScheduler.getInstance().lambda$postRequestVsync$1$VsyncScheduler(new VsyncScheduler.FrameCallback() {
            /* class ohos.agp.window.wmc.AGPWindow.AnonymousClass2 */

            @Override // ohos.agp.vsync.VsyncScheduler.FrameCallback
            public void doFrame(long j) {
                HiLog.debug(AGPWindow.LABEL, "VsyncScheduler doFrame time=%{public}l.", Long.valueOf(j));
                if (j > 0 && AGPWindow.this.mEngine != null) {
                    AGPWindow.this.mEngine.processVSync(j);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void unRegisterBarrierFree() {
        Window window;
        int i = this.mFlag;
        if (i == 1) {
            if (this.mContext == null) {
                HiLog.debug(LABEL, "BF:unRegister BarrierFreeAbility failed", new Object[0]);
                return;
            } else {
                HiLog.debug(LABEL, "BF:unRegister unRegisterBarrierFreeAbility", new Object[0]);
                BarrierFreeInnerClient.unRegisterBarrierFreeAbility(this.mContext);
            }
        } else if (i == 2) {
            if (this.mContext == null || (window = this.mAndroidWindow) == null) {
                HiLog.debug(LABEL, "BF:unRegister BarrierFreeDialog failed", new Object[0]);
                return;
            }
            View findViewById = window.findViewById(16908290);
            HiLog.debug(LABEL, "BF:unRegister unRegisterBarrierFreeDialog", new Object[0]);
            BarrierFreeInnerClient.unRegisterBarrierFreeDialog(this.mContext, findViewById);
        }
        AGPEngineAdapter aGPEngineAdapter = this.mAGPEngine;
        if (aGPEngineAdapter != null) {
            aGPEngineAdapter.notifyBarrierFree(false);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyBarrierFree() {
        if (!this.mACEWindowFlag && this.mFlag != 5) {
            Context context = mBarrierfreeContext;
            if (context != null && !context.equals(this.mContext)) {
                HiLog.debug(LABEL, "BF:unRegister ability", new Object[0]);
                BarrierFreeInnerClient.unRegisterBarrierFreeAbility(mBarrierfreeContext);
            }
            int i = this.mFlag;
            if (i == 1) {
                if (this.mContext != null) {
                    HiLog.debug(LABEL, "BF:Register BarrierFreeAbility", new Object[0]);
                    BarrierFreeInnerClient.registerBarrierFreeAbility(this.mContext, 0);
                }
            } else if (i == 2) {
                Window window = this.mAndroidWindow;
                if (window != null) {
                    View decorView = window.getDecorView();
                    View findViewById = this.mAndroidWindow.findViewById(16908290);
                    if (!(findViewById == null || decorView == null)) {
                        HiLog.debug(LABEL, "BF:Register BarrierFreeDialog", new Object[0]);
                        BarrierFreeInnerClient.registerBarrierFreeDialog(this.mContext, findViewById, decorView, 0);
                    }
                } else {
                    return;
                }
            }
            setBarrierfreeContext(this.mContext);
            AGPEngineAdapter aGPEngineAdapter = this.mAGPEngine;
            if (aGPEngineAdapter != null) {
                aGPEngineAdapter.notifyBarrierFree(true);
            }
        }
    }

    protected static void releaseBarrierFree() {
        if (mBarrierfreeContext != null) {
            HiLog.debug(LABEL, "unregister Ability", new Object[0]);
            BarrierFreeInnerClient.unRegisterBarrierFreeAbility(mBarrierfreeContext);
            setBarrierfreeContext(null);
        }
    }

    public void setACEWindowFlag(boolean z) {
        this.mACEWindowFlag = z;
    }

    public void setMovable(boolean z) {
        this.movable = z;
    }

    public boolean isMovable() {
        return this.movable;
    }

    public Rect getBoundRect() {
        return this.boundRect;
    }

    public void setBoundRect(Rect rect) {
        this.boundRect = rect;
    }

    public void setTransparent(boolean z) {
        if (this.isTransparent != z) {
            if (z) {
                addOrClearFlag(0, 1);
                this.mSurfaceView.getHolder().setFormat(-3);
            }
            AGPEngineAdapter aGPEngineAdapter = this.mAGPEngine;
            if (aGPEngineAdapter != null) {
                aGPEngineAdapter.setTransparent(z);
            } else {
                HiLog.debug(LABEL, "setTransparent not find mAGPEngine.", new Object[0]);
            }
            this.isTransparent = z;
        }
    }

    public boolean dispatchTouchEventFromAndroid(MotionEvent motionEvent) {
        boolean z = false;
        if (this.mAGPEngine == null || motionEvent == null) {
            HiLog.error(LABEL, "dispatchManipulationEvent event or mAGPEngine be null.", new Object[0]);
            return false;
        }
        float f = (float) (-getWindowOffsetY());
        motionEvent.offsetLocation(0.0f, f);
        Optional<MultimodalEvent> createEvent = MultimodalEventFactory.createEvent(motionEvent);
        if (createEvent.isPresent()) {
            MultimodalEvent multimodalEvent = createEvent.get();
            if (multimodalEvent instanceof TouchEvent) {
                z = this.mAGPEngine.processTouchEvent((TouchEvent) multimodalEvent);
            }
        }
        motionEvent.offsetLocation(-0.0f, -f);
        return z;
    }

    public boolean dispatchManipulationEvent(MultimodalEvent multimodalEvent) {
        HiLog.debug(LABEL, "dispatchManipulationEvent event.", new Object[0]);
        if (multimodalEvent instanceof KeyEvent) {
            return dispatchKeyboardEvent((KeyEvent) multimodalEvent);
        }
        if (multimodalEvent instanceof TouchEvent) {
            return dispatchTouchEvent((TouchEvent) multimodalEvent);
        }
        return false;
    }

    public boolean dispatchKeyboardEvent(KeyEvent keyEvent) {
        if (keyEvent == null) {
            HiLog.error(LABEL, "dispatchKeyboardEvent event is null.", new Object[0]);
            return false;
        }
        HiLog.debug(LABEL, "Window dispatchKeyboardEvent event called, code =%{public}d", Integer.valueOf(keyEvent.getKeyCode()));
        IAGPEngineAdapter iAGPEngineAdapter = this.mEngine;
        if (iAGPEngineAdapter != null) {
            return iAGPEngineAdapter.processKeyEvent(keyEvent);
        }
        HiLog.error(LABEL, "dispatchKeyboardEvent not find mEngine.", new Object[0]);
        return false;
    }

    public boolean dispatchTouchEvent(TouchEvent touchEvent) {
        boolean z = false;
        if (touchEvent == null) {
            HiLog.error(LABEL, "dispatchTouchEvent event is null.", new Object[0]);
            return false;
        }
        int action = touchEvent.getAction();
        if (HiLog.isDebuggable()) {
            HiLog.debug(LABEL, "Window dispatchTouchEvent called, touch type =%{public}d", Integer.valueOf(action));
        }
        if (this.swipeDismissEnabled && processSwipeDismiss(touchEvent)) {
            HiLog.debug(LABEL, "Window process swipe dismiss process finish, event continue.", new Object[0]);
        }
        adjustLayout(action);
        int i = -getWindowOffsetY();
        touchEvent.setScreenOffset(0.0f, (float) i);
        handleMovable(touchEvent);
        IAGPEngineAdapter iAGPEngineAdapter = this.mEngine;
        if (iAGPEngineAdapter != null) {
            z = iAGPEngineAdapter.processTouchEvent(touchEvent);
        } else {
            HiLog.error(LABEL, "dispatchTouchEvent not find mEngine.", new Object[0]);
        }
        touchEvent.setScreenOffset(0.0f, (float) (-i));
        return z;
    }

    public boolean setSwipeToDismiss(boolean z) {
        HiLog.debug(LABEL, "Window set swipe to dismiss begin, isEnabled =%{public}s", String.valueOf(z));
        Window window = this.mAndroidWindow;
        if (window != null) {
            window.setCloseOnSwipeEnabled(false);
        }
        if (!z) {
            this.swipeDismissEnabled = false;
            SwipeManager swipeManager2 = this.swipeManager;
            if (swipeManager2 != null) {
                swipeManager2.resetMembers();
                this.swipeManager = null;
            }
            return true;
        } else if (this.mAndroidContext != null) {
            this.swipeDismissEnabled = true;
            this.swipeManager = new SwipeManager();
            this.swipeManager.setOnDismissListener(new OnDismissListener() {
                /* class ohos.agp.window.wmc.AGPWindow.AnonymousClass3 */

                @Override // ohos.agp.window.wmc.AGPWindow.OnDismissListener
                public void onDismissed() {
                    if (AGPWindow.this.mContext != null && AGPWindow.this.mContext.getUITaskDispatcher() != null) {
                        AGPWindow.this.mContext.getUITaskDispatcher().delayDispatch(new Runnable() {
                            /* class ohos.agp.window.wmc.AGPWindow.AnonymousClass3.AnonymousClass1 */

                            public void run() {
                                HiLog.debug(AGPWindow.LABEL, "Swipe dismiss terminate ability start.", new Object[0]);
                                AGPWindow.this.mContext.terminateAbility();
                            }
                        }, 0);
                    }
                }
            });
            this.mRootContainerView.setSlideRecognizerMode(2);
            this.mRootContainerView.setOnSlideListener(new RootContainerView.OnSlideListener() {
                /* class ohos.agp.window.wmc.AGPWindow.AnonymousClass4 */

                @Override // ohos.agp.components.RootContainerView.OnSlideListener
                public void onSlideStart(RootContainerView rootContainerView) {
                    if (AGPWindow.this.swipeManager != null) {
                        AGPWindow.this.swipeManager.setCanScroll(false);
                        HiLog.debug(AGPWindow.LABEL, "Window Swipe dismiss onSlideStart called, canScroll is false.", new Object[0]);
                    }
                }
            });
            return true;
        } else {
            HiLog.debug(LABEL, "Window set swipe to dismiss failed because context invalid.", new Object[0]);
            return false;
        }
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        SwipeManager swipeManager2 = this.swipeManager;
        if (swipeManager2 != null) {
            swipeManager2.setOnDismissListener(onDismissListener);
        }
    }

    public void setOnSwipeChangedListener(OnSwipeChangedListener onSwipeChangedListener) {
        SwipeManager swipeManager2 = this.swipeManager;
        if (swipeManager2 != null) {
            swipeManager2.setOnSwipeChangedListener(onSwipeChangedListener);
        }
    }

    public void setWindowOnSwipe() {
        if (this.swipeManager != null) {
            HiLog.debug(LABEL, "Swipe manager set window on swipe called.", new Object[0]);
            this.swipeManager.setOnSwipeChangedListener(new SwipeChangedListener());
        }
    }

    public boolean dispatchMouseEvent(MouseEvent mouseEvent) {
        boolean z = false;
        if (mouseEvent == null) {
            HiLog.error(LABEL, "dispatchMouseEvent event is null.", new Object[0]);
            return false;
        }
        HiLog.debug(LABEL, "dispatchMouseEvent event start, event type =%{public}d", Integer.valueOf(mouseEvent.getAction()));
        int i = -getWindowOffsetY();
        mouseEvent.setCursorOffset(0.0f, (float) i);
        HiLog.debug(LABEL, "dispatchMouseEvent event end, event y =%{public}d", Float.valueOf(mouseEvent.getCursor().getY()));
        IAGPEngineAdapter iAGPEngineAdapter = this.mEngine;
        if (iAGPEngineAdapter != null) {
            z = iAGPEngineAdapter.processMouseEvent(mouseEvent);
        } else {
            HiLog.error(LABEL, "dispatchMouseEvent not find mEngine.", new Object[0]);
        }
        mouseEvent.setCursorOffset(0.0f, (float) (-i));
        return z;
    }

    public boolean dispatchRotationEvent(RotationEvent rotationEvent) {
        if (rotationEvent == null) {
            HiLog.error(LABEL, "dispatchRotationEvent event is null.", new Object[0]);
            return false;
        }
        HiLog.debug(LABEL, "dispatchRotationEvent event start, event value =%{public}s", String.valueOf(rotationEvent.getRotationValue()));
        IAGPEngineAdapter iAGPEngineAdapter = this.mEngine;
        if (iAGPEngineAdapter != null) {
            return iAGPEngineAdapter.processRotationEvent(rotationEvent);
        }
        HiLog.error(LABEL, "dispatchRotationEvent not find mEngine.", new Object[0]);
        return false;
    }

    public void show() {
        int i;
        if (!this.mIsShown && ((i = this.mEngineMode) == 2 || i == 3)) {
            createEngineAdapter(this.mFlag);
        }
        this.mIsShowing = true;
        this.mIsShown = true;
        AGPWindowManager.getInstance().onWindowShow(this);
    }

    public void hide() {
        this.mIsShowing = false;
        AGPWindowManager.getInstance().onWindowHide(this);
    }

    public void setFocusFlag(int i) {
        AGPContainerView aGPContainerView = this.mSurfaceView;
        if (aGPContainerView == null) {
            return;
        }
        if ((i & 2) != 0) {
            aGPContainerView.requestFocus();
        } else {
            aGPContainerView.setFocusFlag(i);
        }
    }

    public boolean isShowing() {
        return this.mIsShowing;
    }

    public void destroy() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            HiLog.info(LABEL, "destroyOnUiThread.", new Object[0]);
            destroyOnUiThread();
        } else {
            Context context = this.mContext;
            if (!(context == null || context.getUITaskDispatcher() == null)) {
                this.mContext.getUITaskDispatcher().delayDispatch(new Runnable() {
                    /* class ohos.agp.window.wmc.AGPWindow.AnonymousClass5 */

                    public void run() {
                        HiLog.info(AGPWindow.LABEL, "destroyOnUiThread delay.", new Object[0]);
                        AGPWindow.this.destroyOnUiThread();
                    }
                }, 0);
            }
        }
        this.mIsShown = false;
        this.mIsShowing = false;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void destroyOnUiThread() {
        AGPContainerView aGPContainerView = this.mSurfaceView;
        if (aGPContainerView != null) {
            aGPContainerView.destroy();
            this.mSurfaceView = null;
        }
        IAGPEngineAdapter iAGPEngineAdapter = this.mEngine;
        if (iAGPEngineAdapter != null) {
            iAGPEngineAdapter.processDestroy();
            this.mEngine = null;
        } else {
            HiLog.error(LABEL, "destroy not find mEngine.", new Object[0]);
        }
        long j = this.mAGPMultiModel;
        if (j != 0) {
            nativeDestroyMultiModel(j);
            this.mAGPMultiModel = 0;
        }
        this.mContext = null;
        this.mAndroidContext = null;
        this.mTextViewListener = null;
        this.mAndroidWindow = null;
    }

    public void setOnApplyComponentPaddingListener(final IApplyComponentPaddingListener iApplyComponentPaddingListener) {
        Window window = this.mAndroidWindow;
        if (window == null) {
            HiLog.error(LABEL, "setOnApplyComponentPaddingListener failed due to mAndroidWindow is null", new Object[0]);
            return;
        }
        View peekDecorView = window.peekDecorView();
        if (peekDecorView == null) {
            HiLog.error(LABEL, "setOnApplyComponentPaddingListener failed due to decorView is null", new Object[0]);
        } else {
            peekDecorView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                /* class ohos.agp.window.wmc.AGPWindow.AnonymousClass6 */

                public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    HiLog.info(AGPWindow.LABEL, "notifyPaddingChange...", new Object[0]);
                    iApplyComponentPaddingListener.notifyPaddingChange(new ComponentPadding(new WindowInsetsWrapper(windowInsets)));
                    return view.onApplyWindowInsets(windowInsets);
                }
            });
        }
    }

    public void setAttributes(LayoutParams layoutParams) {
        Window window = this.mAndroidWindow;
        if (window == null || layoutParams == null || this.mAndroidParam == null || this.mAndroidContext == null) {
            HiLog.error(LABEL, "setAttributes mAndroidWindow or param be null.", new Object[0]);
            return;
        }
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = AGPWindowManager.getAndroidGravity(layoutParams.gravity);
        attributes.x = layoutParams.x;
        attributes.y = layoutParams.y;
        if (layoutParams.width != this.mSurfaceView.getActualWidth()) {
            attributes.width = layoutParams.width;
        }
        if (layoutParams.height != this.mSurfaceView.getActualHeight()) {
            attributes.height = layoutParams.height;
        }
        attributes.alpha = layoutParams.alpha;
        attributes.dimAmount = layoutParams.dimAmount;
        attributes.screenBrightness = layoutParams.screenBrightness;
        attributes.flags = ohosToAospFlags(layoutParams.flags);
        attributes.layoutInDisplaySideMode = layoutParams.layoutInDisplaySideMode;
        attributes.format = layoutParams.format;
        if (layoutParams.title == null) {
            HiLog.error(LABEL, "setAttributes title can not be null.", new Object[0]);
        } else if (this.mAndroidParam.getTitle().toString().equals(layoutParams.title)) {
            HiLog.debug(LABEL, "setAttributes title not change.", new Object[0]);
        } else {
            this.mAndroidParam.setTitle(layoutParams.title);
        }
        if (layoutParams.windowAnimations != -1) {
            try {
                attributes.windowAnimations = ResourceManagerInner.getAResId(layoutParams.windowAnimations, Class.forName(this.mAndroidContext.getPackageName() + ".ResourceTable", false, this.mAndroidContext.getClassLoader()), this.mAndroidContext);
                HiLog.debug(LABEL, "setAttributes for window animation finished, A resource id: %{public}d.", Integer.valueOf(attributes.windowAnimations));
                this.mWindowAnimations = layoutParams.windowAnimations;
            } catch (ClassNotFoundException unused) {
                HiLog.error(LABEL, "Theme class for window animation not found, resource id: %{public}d.", Integer.valueOf(layoutParams.windowAnimations));
            }
        }
        this.mAndroidWindow.setAttributes(attributes);
        setWindowOffset();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setWindowOffset() {
        AGPEngineAdapter aGPEngineAdapter = this.mAGPEngine;
        if (aGPEngineAdapter != null) {
            aGPEngineAdapter.setWindowOffset(getWindowOffsetX(), getWindowOffsetY());
        }
    }

    public void updateAttributes(LayoutParams layoutParams) {
        WindowManager.LayoutParams layoutParams2 = this.mAndroidParam;
        if (layoutParams2 == null || layoutParams == null) {
            HiLog.error(LABEL, "updateAttributes mAndroidParam or param be null.", new Object[0]);
            return;
        }
        layoutParams2.gravity = AGPWindowManager.getAndroidGravity(layoutParams.gravity);
        this.mAndroidParam.type = layoutParams.type;
        this.mAndroidParam.x = layoutParams.x;
        this.mAndroidParam.y = layoutParams.y;
        this.mAndroidParam.width = layoutParams.width;
        this.mAndroidParam.height = layoutParams.height;
        this.mAndroidParam.alpha = layoutParams.alpha;
        this.mAndroidParam.dimAmount = layoutParams.dimAmount;
        this.mAndroidParam.screenBrightness = layoutParams.screenBrightness;
        this.mAndroidParam.flags = ohosToAospFlags(layoutParams.flags);
    }

    public final LayoutParams getAttributes() {
        LayoutParams layoutParams = new LayoutParams();
        int i = this.mFlag;
        if (i == 1 || i == 10) {
            android.content.Context context = this.mAndroidContext;
            if (context == null || !(context instanceof Activity)) {
                HiLog.error(LABEL, "AGPWindow getAttributes failed due to mAndroidContext is not acitivity", new Object[0]);
                return null;
            }
            Window window = ((Activity) context).getWindow();
            if (window == null) {
                HiLog.error(LABEL, "AGPWindow getAttributes failed due to getWindow return null", new Object[0]);
                return null;
            }
            this.mAndroidParam = window.getAttributes();
        }
        WindowManager.LayoutParams layoutParams2 = this.mAndroidParam;
        if (layoutParams2 != null) {
            if (this.mFlag == 6) {
                layoutParams.token = layoutParams2.token;
            }
            layoutParams.type = this.mAndroidParam.type;
            layoutParams.x = this.mAndroidParam.x;
            layoutParams.y = this.mAndroidParam.y;
            layoutParams.width = this.mAndroidParam.width;
            layoutParams.height = this.mAndroidParam.height;
            layoutParams.alpha = this.mAndroidParam.alpha;
            layoutParams.dimAmount = this.mAndroidParam.dimAmount;
            layoutParams.screenBrightness = this.mAndroidParam.screenBrightness;
            layoutParams.gravity = AGPWindowManager.getZidaneTextAlignment(this.mAndroidParam.gravity);
            layoutParams.flags = aospToOhosFlags(this.mAndroidParam.flags);
            int i2 = this.mWindowAnimations;
            if (i2 != -1) {
                layoutParams.windowAnimations = i2;
            }
            if (layoutParams.width == -1 || layoutParams.width == -2) {
                layoutParams.width = this.mSurfaceView.getActualWidth();
            }
            if (layoutParams.height == -1 || layoutParams.height == -2) {
                layoutParams.height = this.mSurfaceView.getActualHeight();
            }
            layoutParams.title = this.mAndroidParam.getTitle().toString();
            layoutParams.format = this.mAndroidParam.format;
            layoutParams.layoutInDisplaySideMode = this.mAndroidParam.layoutInDisplaySideMode;
            return layoutParams;
        }
        HiLog.error(LABEL, "AGPWindow getAttributes failed due to mAndroidParam is null", new Object[0]);
        return null;
    }

    public final LayoutParams transferAttributes(WindowManager.LayoutParams layoutParams) {
        LayoutParams layoutParams2 = new LayoutParams();
        if (layoutParams == null) {
            HiLog.error(LABEL, "AGPWindow transferAttributes failed due to input AndroidParam is null", new Object[0]);
            return null;
        }
        if (this.mFlag == 6) {
            layoutParams2.token = layoutParams.token;
        }
        layoutParams2.type = layoutParams.type;
        layoutParams2.x = layoutParams.x;
        layoutParams2.y = layoutParams.y;
        layoutParams2.width = layoutParams.width;
        layoutParams2.height = layoutParams.height;
        layoutParams2.alpha = layoutParams.alpha;
        layoutParams2.dimAmount = layoutParams.dimAmount;
        layoutParams2.screenBrightness = layoutParams.screenBrightness;
        layoutParams2.gravity = AGPWindowManager.getZidaneTextAlignment(layoutParams.gravity);
        layoutParams2.flags = aospToOhosFlags(layoutParams.flags);
        int i = this.mWindowAnimations;
        if (i != -1) {
            layoutParams2.windowAnimations = i;
        }
        if (layoutParams2.width == -1 || layoutParams2.width == -2) {
            layoutParams2.width = this.mSurfaceView.getActualWidth();
        }
        if (layoutParams2.height == -1 || layoutParams2.height == -2) {
            layoutParams2.height = this.mSurfaceView.getActualHeight();
        }
        layoutParams2.title = layoutParams.getTitle().toString();
        layoutParams2.format = this.mAndroidParam.format;
        layoutParams2.layoutInDisplaySideMode = this.mAndroidParam.layoutInDisplaySideMode;
        return layoutParams2;
    }

    public Optional<Component> findComponentById(int i) {
        ComponentContainer componentContainer = this.mViewGroup;
        if (componentContainer != null) {
            return Optional.ofNullable(componentContainer.findComponentById(i));
        }
        HiLog.debug(LABEL, "Window find component empty.", new Object[0]);
        return Optional.empty();
    }

    public void setContentLayout(int i) {
        if (this.mContext != null) {
            Component component = this.mLayoutById.get(Integer.valueOf(i));
            if (component == null) {
                component = LayoutScatter.getInstance(this.mContext).parse(i, null, false);
                this.mLayoutById.put(Integer.valueOf(i), component);
            }
            if (component instanceof ComponentContainer) {
                setContentLayout((ComponentContainer) component);
            }
        }
    }

    public void setContentLayout(ComponentContainer componentContainer) {
        this.mViewGroup = componentContainer;
        if (this.mAGPEngine == null) {
            this.mEngineMode = 1;
            createEngineAdapter(this.mFlag);
        }
        if (this.mAGPEngine != null) {
            if (this.hasSetPreContentLayout) {
                this.mWindowRoot = componentContainer;
            } else {
                this.mRootContainerView.removeAllComponents();
                this.mRootContainerView.addComponent(componentContainer);
                this.mWindowRoot = this.mRootContainerView;
            }
            this.mAGPEngine.setContentLayout(this.mWindowRoot);
            if (this.isTransparent) {
                setTransparent(true);
            }
        } else {
            HiLog.error(LABEL, "setContentLayout not find mEngine.", new Object[0]);
        }
        invalidate();
        if (this.mIsShowing) {
            notifyBarrierFree();
        }
    }

    public void setPreContentLayout(ComponentContainer componentContainer, int i, int i2) {
        this.mViewGroup = componentContainer;
        if (this.mAGPEngine == null) {
            this.mEngineMode = 1;
            createEngineAdapter(this.mFlag);
        }
        AGPEngineAdapter aGPEngineAdapter = this.mAGPEngine;
        if (aGPEngineAdapter != null) {
            aGPEngineAdapter.setPreContentLayout(componentContainer, i, i2);
        } else {
            HiLog.error(LABEL, "setPreContentLayout not find mEngine.", new Object[0]);
        }
        this.hasSetPreContentLayout = true;
    }

    public ComponentContainer getContainerLayout() {
        return this.mViewGroup;
    }

    public ComponentContainer getWindowRoot() {
        return this.mWindowRoot;
    }

    public void setBackgroundColor(int i, int i2, int i3) {
        this.mBackgroundColor = new Color(i, i2, i3);
        AGPEngineAdapter aGPEngineAdapter = this.mAGPEngine;
        if (aGPEngineAdapter != null) {
            aGPEngineAdapter.setBackgroundColor(i, i2, i3);
        } else {
            HiLog.error(LABEL, "setBackgroundColor not find mEngine.", new Object[0]);
        }
        invalidate();
    }

    public Color getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public void setBackground(String str) {
        if (this.mAndroidWindow == null) {
            HiLog.error(LABEL, "AGPWindow setBackground failed due to Window is null.", new Object[0]);
        } else if (new File(str).exists()) {
            if (this.mAGPEngine != null) {
                setTransparent(true);
            } else {
                HiLog.error(LABEL, "setBackground not find mAGPEngine.", new Object[0]);
            }
            AGPContainerView aGPContainerView = this.mSurfaceView;
            if (aGPContainerView != null) {
                aGPContainerView.setZOrderOnTop(true);
                this.mSurfaceView.getHolder().setFormat(-2);
                this.mSurfaceView.setAlpha(0.0f);
            }
            Bitmap decodeFile = BitmapFactory.decodeFile(str);
            if (decodeFile == null) {
                HiLog.error(LABEL, "The drawablePath bitmap cannot be decoded and returned null.", new Object[0]);
                return;
            }
            this.mAndroidWindow.setBackgroundDrawable(new BitmapDrawable(decodeFile));
            HiLog.debug(LABEL, "AGPWindow setBackground success.", new Object[0]);
        }
    }

    public void setNavigationBarColor(int i) {
        Window window = this.mAndroidWindow;
        if (window == null) {
            HiLog.error(LABEL, "setNavigationBarColor failed due to Window is null", new Object[0]);
        } else {
            window.setNavigationBarColor(i);
        }
    }

    public void setStatusBarColor(int i) {
        Window window = this.mAndroidWindow;
        if (window == null) {
            HiLog.error(LABEL, "setStatusBarColor failed due to Window is null", new Object[0]);
        } else {
            window.setStatusBarColor(i);
        }
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        if (i < 0 || i2 < 0 || i3 < 0 || i4 < 0) {
            HiLog.error(LABEL, "setPadding failed due to input < 0", new Object[0]);
            return;
        }
        Window window = this.mAndroidWindow;
        if (window == null) {
            HiLog.error(LABEL, "setPadding failed due to Window is null", new Object[0]);
            return;
        }
        View decorView = window.getDecorView();
        if (decorView == null) {
            HiLog.error(LABEL, "setPadding failed due to decorView is null", new Object[0]);
        } else {
            decorView.setPadding(i, i2, i3, i4);
        }
    }

    public void setStatusBarVisibility(int i) {
        Window window = this.mAndroidWindow;
        if (window == null) {
            HiLog.error(LABEL, "setStatusBarVisibility failed due to Window is null", new Object[0]);
            return;
        }
        View decorView = window.getDecorView();
        if (decorView == null) {
            HiLog.error(LABEL, "setStatusBarVisibility failed due to decorView is null", new Object[0]);
        } else {
            decorView.setSystemUiVisibility(transitParams(i));
        }
    }

    public int getStatusBarVisibility() {
        Window window = this.mAndroidWindow;
        if (window == null) {
            HiLog.error(LABEL, "getStatusBarVisibility failed due to Window is null", new Object[0]);
            return -1;
        }
        View decorView = window.getDecorView();
        if (decorView != null) {
            return reverseParams(decorView.getSystemUiVisibility());
        }
        HiLog.error(LABEL, "getStatusBarVisibility failed due to decorView is null", new Object[0]);
        return -1;
    }

    public AGPContainerView getSurfaceView() {
        return this.mSurfaceView;
    }

    public android.content.Context getContext() {
        return this.mAndroidContext;
    }

    public Context getMyContext() {
        return this.mContext;
    }

    public int getWindowFlag() {
        return this.mWindowFlag;
    }

    public void setWindowFlag(int i) {
        this.mWindowFlag = i;
    }

    public TextInputConnection.ITextViewListener getTextViewListener() {
        return this.mTextViewListener;
    }

    public void setTextViewListener(TextViewListenerWrapper textViewListenerWrapper) {
        this.mTextViewListener = textViewListenerWrapper;
        AGPContainerView aGPContainerView = this.mSurfaceView;
        if (aGPContainerView != null) {
            aGPContainerView.setInputChannelListener(textViewListenerWrapper);
        } else {
            HiLog.error(LABEL, "AGP container view is empty, make sure to initialize surface view.", new Object[0]);
        }
        HiLog.debug(LABEL, "Set ACE text view listener successfully.", new Object[0]);
    }

    public void startInput() {
        ComponentContainer componentContainer;
        InputMethodController instance = InputMethodController.getInstance();
        if (instance == null || (componentContainer = this.mViewGroup) == null) {
            HiLog.error(LABEL, "StartInput failed because InputMethodController or viewgroup invalid.", new Object[0]);
            return;
        }
        Component findFocus = componentContainer.findFocus();
        if (findFocus instanceof Text) {
            Text text = (Text) findFocus;
            WeakReference<Text> weakReference = this.lastInputFocus;
            if (weakReference == null || !text.equals(weakReference.get())) {
                this.lastInputFocus = new WeakReference<>(text);
                instance.startInput(0, true);
                HiLog.debug(LABEL, "StartInput InputMethodController successful, focus view changed.", new Object[0]);
                return;
            }
            instance.startInput(0, false);
            HiLog.debug(LABEL, "StartInput InputMethodController successful, focus view not changed.", new Object[0]);
            return;
        }
        HiLog.error(LABEL, "StartInput InputMethodController failed because focus text view invalid.", new Object[0]);
    }

    public void stopInput() {
        InputMethodController instance = InputMethodController.getInstance();
        if (instance == null) {
            HiLog.error(LABEL, "InputMethodController mIMController is null", new Object[0]);
        } else if (!instance.stopInput(0)) {
            HiLog.debug(LABEL, "stopInput InputMethodController failed", new Object[0]);
        }
    }

    public void setAVController(Object obj) {
        Window window;
        if (obj == null || (window = this.mAndroidWindow) == null || !(obj instanceof MediaController)) {
            HiLog.error(LABEL, "Set media controller failed because type error.", new Object[0]);
            return;
        }
        window.setMediaController((MediaController) obj);
        HiLog.debug(LABEL, "Set media controller successful.", new Object[0]);
    }

    public Optional<Object> getAVController() {
        if (this.mAndroidWindow != null) {
            HiLog.debug(LABEL, "Get media controller from window.", new Object[0]);
            return Optional.ofNullable(this.mAndroidWindow.getMediaController());
        }
        HiLog.debug(LABEL, "Get media controller empty because window empty.", new Object[0]);
        return Optional.empty();
    }

    public void setVolumeControlStream(int i) {
        Window window = this.mAndroidWindow;
        if (window != null) {
            window.setVolumeControlStream(i);
            HiLog.debug(LABEL, "Set the audio stream successful.", new Object[0]);
            return;
        }
        HiLog.error(LABEL, "Set the audio stream failed because window does not exist.", new Object[0]);
    }

    public int getVolumeControlStream() {
        if (this.mAndroidWindow != null) {
            HiLog.debug(LABEL, "Get the audio stream from window.", new Object[0]);
            return this.mAndroidWindow.getVolumeControlStream();
        }
        HiLog.debug(LABEL, "Get the audio stream failed empty because window empty.", new Object[0]);
        return Integer.MIN_VALUE;
    }

    public boolean hasWindowFocus() {
        View decorView;
        Window window = this.mAndroidWindow;
        if (window != null && (decorView = window.getDecorView()) != null) {
            return decorView.hasWindowFocus();
        }
        HiLog.debug(LABEL, "Check window focus failed because window empty or abnormal.", new Object[0]);
        return false;
    }

    public Optional<Component> getWindowFocus() {
        ComponentContainer componentContainer = this.mViewGroup;
        if (componentContainer != null) {
            return Optional.ofNullable(componentContainer.findFocus());
        }
        HiLog.debug(LABEL, "Window does not have a focus.", new Object[0]);
        return Optional.empty();
    }

    public void setIsAmbientMode(boolean z) {
        Window window = this.mAndroidWindow;
        if (window == null) {
            HiLog.debug(LABEL, "Window is invalid, cannot set ambient mode.", new Object[0]);
            return;
        }
        View peekDecorView = window.peekDecorView();
        if (peekDecorView != null) {
            ViewRootImpl viewRootImpl = peekDecorView.getViewRootImpl();
            if (viewRootImpl != null) {
                ViewRootImplEx.setIsAmbientMode(viewRootImpl, z);
                HiLog.debug(LABEL, "Window set ambient mode success, now mode is: %{public}s", String.valueOf(z));
                return;
            }
            HiLog.debug(LABEL, "Window set ambient mode: %{public}s failed due to viewRoot is invalid.", String.valueOf(z));
            return;
        }
        HiLog.debug(LABEL, "Window set ambient mode: %{public}s failed due to decorView is invalid.", String.valueOf(z));
    }

    public AGPSurfaceControl getAGPSurfaceControl() {
        if (this.mAGPSurfaceControl == null) {
            this.mAGPSurfaceControl = new AGPSurfaceControl(this.mAndroidWindow);
        }
        return this.mAGPSurfaceControl;
    }

    public void setWindowBlur(int i, int i2) {
        AGPContainerView aGPContainerView;
        if (this.mAndroidWindow != null && (aGPContainerView = this.mSurfaceView) != null) {
            aGPContainerView.setZOrderOnTop(true);
            WindowManager.LayoutParams attributes = this.mAndroidWindow.getAttributes();
            WindowManagerEx.LayoutParamsEx layoutParamsEx = new WindowManagerEx.LayoutParamsEx(attributes);
            layoutParamsEx.addHwFlags(i);
            layoutParamsEx.setBlurStyle(i2);
            this.mAndroidWindow.setAttributes(attributes);
        }
    }

    public void setBlurStyle(int i, int i2) {
        Window window = this.mAndroidWindow;
        if (window != null && this.mSurfaceView != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            new WindowManagerEx.LayoutParamsEx(attributes).setBlurStyle(i, i2);
            this.mAndroidWindow.setAttributes(attributes);
        }
    }

    public LayoutScatter getLayoutScatter() {
        return LayoutScatter.getInstance(this.mContext);
    }

    public boolean isActive() {
        Window window = this.mAndroidWindow;
        if (window == null) {
            return false;
        }
        return window.isActive();
    }

    public boolean isWCGMode() {
        Window window = this.mAndroidWindow;
        if (window == null) {
            return false;
        }
        return window.isWideColorGamut();
    }

    public void setPixelFormat(int i) {
        Window window = this.mAndroidWindow;
        if (window != null) {
            window.setFormat(i);
        }
    }

    /* access modifiers changed from: protected */
    public static class Move {
        protected float lastX;
        protected float lastY;
        protected int[] location = new int[2];
        protected float nowX;
        protected float nowY;
        protected float tranX;
        protected float tranY;

        protected Move() {
        }
    }

    public static class Color {
        private int mBlue;
        private int mGreen;
        private int mRed;

        Color(int i, int i2, int i3) {
            this.mRed = i;
            this.mGreen = i2;
            this.mBlue = i3;
        }

        public int getBlue() {
            return this.mBlue;
        }

        public int getGreen() {
            return this.mGreen;
        }

        public int getRed() {
            return this.mRed;
        }
    }

    public class TextViewListener implements TextInputConnection.ITextViewListener {
        private static final int COPY = 1;
        private static final int CUT = 3;
        private static final int PASTE = 2;
        private static final int SELECT_ALL = 0;

        public TextViewListener() {
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean commitText(CharSequence charSequence, int i) {
            HiLog.debug(AGPWindow.LABEL, "AGPWindow TextViewListener commitText was called", new Object[0]);
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    boolean insertText = ((Text) findFocus).getInputDataChannel().insertText(charSequence.toString());
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call insertText of input data channel success.", new Object[0]);
                    return insertText;
                }
            }
            return false;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean commitContent(InputContentInfo inputContentInfo, int i, Bundle bundle) {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    boolean insertRichContent = ((Text) findFocus).getInputDataChannel().insertRichContent(InputContentInfoAdapter.convertToRichContent(inputContentInfo));
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call insertRichContent of input data channel success.", new Object[0]);
                    return insertRichContent;
                }
            }
            return false;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean deleteSurroundingText(int i, int i2) {
            if (AGPWindow.this.mViewGroup == null) {
                return false;
            }
            Component findFocus = AGPWindow.this.mViewGroup.findFocus();
            if (!(findFocus instanceof Text)) {
                return false;
            }
            InputDataChannel inputDataChannel = ((Text) findFocus).getInputDataChannel();
            if (!inputDataChannel.deleteForward(i2) || !inputDataChannel.deleteBackward(i)) {
                return false;
            }
            HiLog.debug(AGPWindow.LABEL, "AGPWindow call delete events of input data channel success.", new Object[0]);
            return true;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public CharSequence getTextBeforeCursor(int i, int i2) {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    String forward = ((Text) findFocus).getInputDataChannel().getForward(i);
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call getForward of input data channel success.", new Object[0]);
                    return forward;
                }
            }
            return "";
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public CharSequence getTextAfterCursor(int i, int i2) {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    String backward = ((Text) findFocus).getInputDataChannel().getBackward(i);
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call getBackward of input data channel success.", new Object[0]);
                    return backward;
                }
            }
            return "";
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean sendKeyEvent(android.view.KeyEvent keyEvent) {
            boolean z;
            if (AGPWindow.this.mViewGroup == null) {
                return false;
            }
            Component findFocus = AGPWindow.this.mViewGroup.findFocus();
            if (findFocus instanceof Text) {
                Text text = (Text) findFocus;
                Optional<MultimodalEvent> createEvent = MultimodalEventFactory.createEvent(keyEvent);
                if (!createEvent.isPresent()) {
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow createEvent failed!", new Object[0]);
                    return false;
                }
                MultimodalEvent multimodalEvent = createEvent.get();
                if (!(multimodalEvent instanceof KeyEvent)) {
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow createEvent is not KeyEvent", new Object[0]);
                    return false;
                }
                boolean sendKeyEvent = text.getInputDataChannel().sendKeyEvent((KeyEvent) multimodalEvent);
                if (sendKeyEvent) {
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call sendKeyEvent of input data channel success.", new Object[0]);
                    return sendKeyEvent;
                }
                z = AGPWindow.this.dispatchKeyEventFromDecorView(keyEvent);
                HiLog.debug(AGPWindow.LABEL, "AGPWindow dispatchKeyEvent again success.", new Object[0]);
            } else {
                z = AGPWindow.this.dispatchKeyEventFromDecorView(keyEvent);
                HiLog.debug(AGPWindow.LABEL, "AGPWindow call  dispatchKeyEvent again success in not Text.", new Object[0]);
            }
            return z;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean setComposingRegion(int i, int i2) {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    boolean markText = ((Text) findFocus).getInputDataChannel().markText(i, i2);
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call markText of input data channel success.", new Object[0]);
                    return markText;
                }
            }
            return false;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean finishComposingText() {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    boolean unmarkText = ((Text) findFocus).getInputDataChannel().unmarkText();
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call unmarkText of input data channel success.", new Object[0]);
                    return unmarkText;
                }
            }
            return false;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean setComposingText(CharSequence charSequence, int i) {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    boolean replaceMarkedText = ((Text) findFocus).getInputDataChannel().replaceMarkedText(charSequence.toString());
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call replaceMarkedText of input data channel success.", new Object[0]);
                    return replaceMarkedText;
                }
            }
            return false;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public ExtractedText getExtractedText(ExtractedTextRequest extractedTextRequest, int i) {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    Text text = (Text) findFocus;
                    EditingCapability convertToEditingCapability = ExtractedTextRequestAdapter.convertToEditingCapability(extractedTextRequest, i);
                    if (i != 0) {
                        text.updateEditingInfo(convertToEditingCapability, extractedTextRequest.token, extractedTextRequest.flags);
                    }
                    ExtractedText convertToExtractedText = ExtractedTextAdapter.convertToExtractedText(text.getInputDataChannel().subscribeEditingText(convertToEditingCapability));
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call subscribeEditingText of input data channel success.", new Object[0]);
                    return convertToExtractedText;
                }
            }
            return null;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean performEditorAction(int i) {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    boolean sendKeyFunction = ((Text) findFocus).getInputDataChannel().sendKeyFunction(TextViewListenerWrapper.convertToEnterKeyType(i));
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call sendKeyFunction of input data channel success.", new Object[0]);
                    return sendKeyFunction;
                }
            }
            return false;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean setSelection(int i, int i2) {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    boolean selectText = ((Text) findFocus).getInputDataChannel().selectText(i, i2);
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call selectText of input data channel success.", new Object[0]);
                    return selectText;
                }
            }
            return false;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public void closeConnection() {
            if (AGPWindow.this.mViewGroup != null) {
                try {
                    Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                    if (findFocus instanceof Text) {
                        ((Text) findFocus).getInputDataChannel().close();
                        HiLog.debug(AGPWindow.LABEL, "AGPWindow call close of input data channel success.", new Object[0]);
                    }
                } catch (IllegalStateException unused) {
                    HiLog.error(AGPWindow.LABEL, "AGPWindow call close input data channel but view group has been released.", new Object[0]);
                }
            }
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public void updateEditorInfo(EditorInfo editorInfo) {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    Text text = (Text) findFocus;
                    editorInfo.inputType = TextViewListenerWrapper.convertToInputType(text.getTextInputType());
                    if (text.isMultipleLine()) {
                        editorInfo.inputType |= 131072;
                    }
                    editorInfo.imeOptions = TextViewListenerWrapper.convertToImeOption(text.getInputMethodOption());
                }
            }
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public int getCursorCapsMode(int i) {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    int autoCapitalizeMode = ((Text) findFocus).getInputDataChannel().getAutoCapitalizeMode(i);
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call get getAutoCapitalizeMode of input data channel success.", new Object[0]);
                    return autoCapitalizeMode;
                }
            }
            return 0;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean beginBatchEdit() {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    boolean lock = ((Text) findFocus).getInputDataChannel().lock();
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call lock of input data channel success.", new Object[0]);
                    return lock;
                }
            }
            return false;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean endBatchEdit() {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    boolean unlock = ((Text) findFocus).getInputDataChannel().unlock();
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call unlock of input data channel success.", new Object[0]);
                    return unlock;
                }
            }
            return false;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public CharSequence getSelectedText(int i) {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    String selectedText = ((Text) findFocus).getInputDataChannel().getSelectedText(i);
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call getSelectedText of input data channel success.", new Object[0]);
                    return selectedText;
                }
            }
            return "";
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean clearMetaKeyStates(int i) {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    boolean clearNoncharacterKeyState = ((Text) findFocus).getInputDataChannel().clearNoncharacterKeyState(i);
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call clearNoncharacterKeyState of input data channel success.", new Object[0]);
                    return clearNoncharacterKeyState;
                }
            }
            return false;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean commitCompletion(CompletionInfo completionInfo) {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    boolean recommendText = ((Text) findFocus).getInputDataChannel().recommendText(CompletionInfoAdapter.convertToRecommendationInfo(completionInfo));
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call recommendText of input data channel success.", new Object[0]);
                    return recommendText;
                }
            }
            return false;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean commitCorrection(CorrectionInfo correctionInfo) {
            if (!(AGPWindow.this.mViewGroup == null || correctionInfo == null)) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    boolean reviseText = ((Text) findFocus).getInputDataChannel().reviseText(correctionInfo.getOffset(), correctionInfo.getOldText().toString(), correctionInfo.getNewText().toString());
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call reviseText of input data channel success.", new Object[0]);
                    return reviseText;
                }
            }
            return false;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean performContextMenuAction(int i) {
            int i2;
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    Text text = (Text) findFocus;
                    switch (i) {
                        case AGPWindow.ID_SELECT_ALL /*{ENCODED_INT: 16908319}*/:
                            i2 = 0;
                            break;
                        case AGPWindow.ID_CUT /*{ENCODED_INT: 16908320}*/:
                            i2 = 3;
                            break;
                        case AGPWindow.ID_COPY /*{ENCODED_INT: 16908321}*/:
                            i2 = 1;
                            break;
                        case AGPWindow.ID_PASTE /*{ENCODED_INT: 16908322}*/:
                            i2 = 2;
                            break;
                        default:
                            i2 = -1;
                            break;
                    }
                    boolean sendMenuFunction = text.getInputDataChannel().sendMenuFunction(i2);
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call sendMenuFunction of input data channel success.", new Object[0]);
                    return sendMenuFunction;
                }
            }
            return false;
        }

        @Override // ohos.agp.window.aspbshell.TextInputConnection.ITextViewListener
        public boolean requestCursorUpdates(int i) {
            if (AGPWindow.this.mViewGroup != null) {
                Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                if (findFocus instanceof Text) {
                    boolean subscribeCaretContext = ((Text) findFocus).getInputDataChannel().subscribeCaretContext(i);
                    HiLog.debug(AGPWindow.LABEL, "AGPWindow call subscribeCaretContext of input data channel success.", new Object[0]);
                    return subscribeCaretContext;
                }
            }
            return false;
        }
    }

    /* access modifiers changed from: private */
    public class SurfaceViewListener implements AGPContainerView.ISurfaceViewListener {
        private SurfaceViewListener() {
        }

        @Override // ohos.agp.window.aspbshell.AGPContainerView.ISurfaceViewListener
        public void onSurfaceCreated(Surface surface) {
            HiLog.debug(AGPWindow.LABEL, "onSurfaceCreated", new Object[0]);
            if (AGPWindow.this.mEngine == null) {
                AGPWindow.this.mEngineMode = 1;
                AGPWindow aGPWindow = AGPWindow.this;
                aGPWindow.createEngineAdapter(aGPWindow.mFlag);
            }
            if (AGPWindow.this.mEngine != null) {
                AGPWindow.this.mEngine.processSurfaceCreated(surface);
            } else {
                HiLog.error(AGPWindow.LABEL, "onSurfaceCreated not find mEngine.", new Object[0]);
            }
        }

        @Override // ohos.agp.window.aspbshell.AGPContainerView.ISurfaceViewListener
        public void onSurfaceChanged(Surface surface, int i, int i2, int i3) {
            HiLog.debug(AGPWindow.LABEL, "onSurfaceChanged", new Object[0]);
            AGPWindow.this.setWindowOffset();
            if (AGPWindow.this.mEngine != null) {
                AGPWindow.this.mEngine.processSurfaceChanged(surface, i, i2, i3);
                if (AGPWindow.this.swipeDismissEnabled && AGPWindow.this.swipeManager != null && AGPWindow.this.swipeManager.windowWidth == 0) {
                    HiLog.debug(AGPWindow.LABEL, "Surface change calls swipe manager to init.", new Object[0]);
                    AGPWindow.this.swipeManager.init(AGPWindow.this.mAndroidContext, AGPWindow.this.mContext);
                }
            } else {
                HiLog.error(AGPWindow.LABEL, "onSurfaceChanged not find mEngine.", new Object[0]);
            }
            if (AGPWindow.this.mAGPMultiModel != 0) {
                HiLog.debug(AGPWindow.LABEL, "try to get windowtoken", new Object[0]);
                IBinder windowToken = AGPWindow.this.mSurfaceView.getWindowToken();
                if (windowToken == null) {
                    HiLog.error(AGPWindow.LABEL, "token is null", new Object[0]);
                    return;
                }
                AGPWindow aGPWindow = AGPWindow.this;
                if (!aGPWindow.nativeSetWindowtoken(aGPWindow.mAGPMultiModel, windowToken)) {
                    HiLog.error(AGPWindow.LABEL, "failed to set windowtoken for multimodel", new Object[0]);
                } else if (AGPWindow.this.mAGPEngine != null) {
                    AGPWindow.this.mAGPEngine.setMultiModel(AGPWindow.this.mAGPMultiModel);
                }
            }
        }

        @Override // ohos.agp.window.aspbshell.AGPContainerView.ISurfaceViewListener
        public void onSurfaceDestroyed(Surface surface) {
            HiLog.debug(AGPWindow.LABEL, "onSurfaceDestroyed", new Object[0]);
            if (AGPWindow.this.mEngine != null) {
                AGPWindow.this.mEngine.processSurfaceDestroy(surface);
            } else {
                HiLog.error(AGPWindow.LABEL, "onSurfaceDestroyed not find mEngine.", new Object[0]);
            }
        }

        @Override // ohos.agp.window.aspbshell.AGPContainerView.ISurfaceViewListener
        public void onConfigurationChanged(Configuration configuration) {
            HiLog.debug(AGPWindow.LABEL, "onConfigurationChanged", new Object[0]);
            if (AGPWindow.this.mEngine != null) {
                AGPWindow.this.onSizeChange(configuration);
                AGPWindow.this.mEngine.processConfigurationChanged(configuration);
                return;
            }
            HiLog.error(AGPWindow.LABEL, "ConfigurationChanged not find mEngine..", new Object[0]);
        }

        @Override // ohos.agp.window.aspbshell.AGPContainerView.ISurfaceViewListener
        public void onGlobalLayout() {
            if (AGPWindow.this.mAndroidWindow == null) {
                HiLog.error(AGPWindow.LABEL, "onGlobalLayout mAndroidWindow is null!", new Object[0]);
                return;
            }
            View decorView = AGPWindow.this.mAndroidWindow.getDecorView();
            if (decorView == null || AGPWindow.this.mViewGroup == null || AGPWindow.this.mAGPEngine == null) {
                HiLog.error(AGPWindow.LABEL, "onGlobalLayout decorView or mViewGroup or mAGPEngine is null!", new Object[0]);
                return;
            }
            android.graphics.Rect rect = new android.graphics.Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            AGPWindow.this.mAGPEngine.setWindowVisibleRect(rect.left, rect.top, rect.right, rect.bottom);
            HiLog.debug(AGPWindow.LABEL, "onGlobalLayout window visible rect is: %{public}d, %{public}d,%{public}d, %{public}d", Integer.valueOf(rect.left), Integer.valueOf(rect.top), Integer.valueOf(rect.right), Integer.valueOf(rect.bottom));
            AGPWindow.this.mViewGroup.postLayout();
        }

        @Override // ohos.agp.window.aspbshell.AGPContainerView.ISurfaceViewListener
        public void onWindowFocusChange(boolean z) {
            if (AGPWindow.this.mAGPEngine == null) {
                HiLog.error(AGPWindow.LABEL, "onWindowFocusChange mAGPEngine is null!", new Object[0]);
            } else {
                AGPWindow.this.mAGPEngine.notifyWindowFocusChange(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public class SwipeManager {
        private static final float MAX_DISTANCE_THRESHOLD = 0.33f;
        private static final int MILLISECOND_UNIT = 1000;
        private static final float MIN_DISTANCE_THRESHOLD = 0.1f;
        private static final int SLOP_RATIO = 2;
        private int activeTouchId;
        private boolean activityTranslucencyConverted;
        private boolean blockGesture = false;
        private boolean canScroll = true;
        private boolean discardSwiping;
        private OnDismissListener dismissListener;
        private boolean dismissed;
        private long downTime;
        private float downX;
        private float downY;
        private boolean isRtl = false;
        private float lastX;
        private int minFlingVelocity;
        private int slop;
        private final SwipeAnimator swipeAnimator = new SwipeAnimator();
        private OnSwipeChangedListener swipeChangedListener;
        private boolean swiping;
        private VelocityTracker velocityTracker;
        private int windowWidth;

        private float progressOfAlpha(float f) {
            return 1.0f - ((f * f) * f);
        }

        protected SwipeManager() {
        }

        public void setCanScroll(boolean z) {
            this.canScroll = z;
        }

        public boolean verifySwiping(TouchEvent touchEvent) {
            checkGesture(touchEvent);
            if (this.blockGesture) {
                return true;
            }
            switch (touchEvent.getAction()) {
                case 1:
                    resetMembers();
                    this.downTime = touchEvent.getStartTime();
                    this.downX = touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getX();
                    this.downY = touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getY();
                    this.activeTouchId = touchEvent.getPointerId(touchEvent.getIndex());
                    this.velocityTracker = VelocityTracker.obtain();
                    MotionEvent obtain = MotionEvent.obtain(touchEvent.getStartTime(), touchEvent.getOccurredTime(), 0, this.downX, this.downY, 0);
                    this.velocityTracker.addMovement(obtain);
                    obtain.recycle();
                    break;
                case 3:
                    if (this.velocityTracker != null && !this.discardSwiping) {
                        if (isValidPointIndex(touchEvent)) {
                            updateSwiping(touchEvent);
                            break;
                        } else {
                            this.discardSwiping = true;
                            break;
                        }
                    }
                case 4:
                    this.activeTouchId = touchEvent.getPointerId(touchEvent.getIndex());
                    break;
                case 5:
                    if (touchEvent.getPointerId(touchEvent.getIndex()) == this.activeTouchId) {
                        this.activeTouchId = touchEvent.getPointerId(touchEvent.getIndex() == 0 ? 1 : 0);
                        break;
                    }
                    break;
            }
            if (this.discardSwiping || !this.swiping) {
                return false;
            }
            return true;
        }

        private boolean isValidPointIndex(TouchEvent touchEvent) {
            for (int pointerCount = touchEvent.getPointerCount() - 1; pointerCount >= 0; pointerCount--) {
                if (touchEvent.getPointerId(pointerCount) == this.activeTouchId) {
                    return true;
                }
            }
            return false;
        }

        public void executeSwiping(TouchEvent touchEvent) {
            checkGesture(touchEvent);
            if (!this.blockGesture) {
                int action = touchEvent.getAction();
                if (action == 2) {
                    updateDismiss(touchEvent);
                    float x = touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getX() - this.downX;
                    if (this.isRtl) {
                        x = -x;
                    }
                    if (this.dismissed) {
                        HiLog.debug(AGPWindow.LABEL, "Action PRIMARY_POINT_UP, executeSwiping animate dismiss started.", new Object[0]);
                        this.swipeAnimator.animateDismiss(x);
                    } else if (!this.swiping || this.lastX == -2.14748365E9f) {
                        HiLog.debug(AGPWindow.LABEL, "Action PRIMARY_POINT_UP, but neither animate dismiss nor recovery.", new Object[0]);
                    } else {
                        HiLog.debug(AGPWindow.LABEL, "Action PRIMARY_POINT_UP, executeSwiping animate recovery started.", new Object[0]);
                        this.swipeAnimator.animateRecovery(x);
                    }
                    resetMembers();
                } else if (action == 3) {
                    MotionEvent obtain = MotionEvent.obtain(this.downTime, touchEvent.getOccurredTime(), 2, touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getX(), touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getY(), 0);
                    this.velocityTracker.addMovement(obtain);
                    obtain.recycle();
                    this.lastX = touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getX();
                    updateSwiping(touchEvent);
                    if (!this.swiping) {
                        return;
                    }
                    if (!this.isRtl) {
                        setProgress(this.lastX - this.downX);
                    } else {
                        setProgress(this.downX - this.lastX);
                    }
                } else if (action == 6) {
                    cancel();
                    resetMembers();
                }
            }
        }

        public void setOnDismissListener(OnDismissListener onDismissListener) {
            this.dismissListener = onDismissListener;
        }

        public void setOnSwipeChangedListener(OnSwipeChangedListener onSwipeChangedListener) {
            if (!AGPWindow.this.IS_WATCH) {
                HiLog.error(AGPWindow.LABEL, "setOnSwipeChangedListener failed type", new Object[0]);
            } else {
                this.swipeChangedListener = onSwipeChangedListener;
            }
        }

        /* access modifiers changed from: protected */
        public void init(android.content.Context context, Context context2) {
            ResourceManager resourceManager;
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            this.slop = viewConfiguration.getScaledTouchSlop();
            HiLog.debug(AGPWindow.LABEL, "Swipe manager init slop: %{public}d.", Integer.valueOf(this.slop));
            this.minFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
            if (AGPWindow.this.mViewGroup != null) {
                this.windowWidth = AGPWindow.this.mViewGroup.getWidth();
                HiLog.debug(AGPWindow.LABEL, "Swipe manager init windowWidth: %{public}d.", Integer.valueOf(this.windowWidth));
            }
            if (context2 != null && (resourceManager = context2.getResourceManager()) != null) {
                ohos.global.configuration.Configuration configuration = resourceManager.getConfiguration();
                this.isRtl = configuration != null ? configuration.isLayoutRTL : false;
                HiLog.debug(AGPWindow.LABEL, "Swipe manager isLayoutRTL: %{public}s.", String.valueOf(this.isRtl));
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void resetMembers() {
            VelocityTracker velocityTracker2 = this.velocityTracker;
            if (velocityTracker2 != null) {
                velocityTracker2.recycle();
            }
            this.velocityTracker = null;
            this.downTime = 0;
            this.downX = 0.0f;
            this.lastX = -2.14748365E9f;
            this.downY = 0.0f;
            this.swiping = false;
            this.dismissed = false;
            this.discardSwiping = false;
            if (AGPWindow.this.mFlag != 2 && AGPWindow.this.mFlag != 3) {
                this.canScroll = true;
            }
        }

        private void updateSwiping(TouchEvent touchEvent) {
            if (!this.canScroll) {
                boolean z = this.swiping;
                if (!z) {
                    float x = touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getX() - this.downX;
                    float y = touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getY() - this.downY;
                    if (!this.isRtl) {
                        float f = (x * x) + (y * y);
                        int i = this.slop;
                        if (f > ((float) (i * i))) {
                            this.swiping = x > ((float) (i * 2)) && Math.abs(y) < Math.abs(x);
                        } else {
                            this.swiping = false;
                        }
                    } else {
                        float f2 = (x * x) + (y * y);
                        int i2 = this.slop;
                        if (f2 > ((float) (i2 * i2))) {
                            this.swiping = (-x) > ((float) (i2 * 2)) && Math.abs(y) < Math.abs(x);
                        } else {
                            this.swiping = false;
                        }
                    }
                }
                if (this.swiping && !z && !AGPWindow.this.isTransparent && AGPWindow.this.mAndroidContext != null && (AGPWindow.this.mAndroidContext instanceof Activity)) {
                    try {
                        Object invoke = Activity.class.getMethod("convertToTranslucent", Class.forName("android.app.Activity$TranslucentConversionListener"), ActivityOptions.class).invoke((Activity) AGPWindow.this.mAndroidContext, null, null);
                        if (invoke instanceof Boolean) {
                            this.activityTranslucencyConverted = ((Boolean) invoke).booleanValue();
                        }
                        HiLog.debug(AGPWindow.LABEL, "Update swiping window convertToTranslucent done: %{public}s.", String.valueOf(this.activityTranslucencyConverted));
                    } catch (InvocationTargetException unused) {
                        HiLog.error(AGPWindow.LABEL, "Activity convert to translucent failed, target invoke failed.", new Object[0]);
                    } catch (NoSuchMethodException unused2) {
                        HiLog.error(AGPWindow.LABEL, "Activity convert to translucent failed, method not found.", new Object[0]);
                    } catch (IllegalAccessException unused3) {
                        HiLog.error(AGPWindow.LABEL, "Activity convert to translucent failed, access illegal.", new Object[0]);
                    } catch (ClassNotFoundException unused4) {
                        HiLog.error(AGPWindow.LABEL, "Activity convert to translucent failed, class not found.", new Object[0]);
                    }
                }
            }
        }

        private void updateDismiss(TouchEvent touchEvent) {
            if (!this.canScroll) {
                float x = touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getX() - this.downX;
                if (this.isRtl) {
                    x = -x;
                }
                this.velocityTracker.computeCurrentVelocity(1000);
                float xVelocity = this.velocityTracker.getXVelocity();
                if (this.lastX == -2.14748365E9f && touchEvent.getOccurredTime() != this.downTime) {
                    xVelocity = x / (((float) (touchEvent.getOccurredTime() - this.downTime)) / 1000.0f);
                }
                boolean z = true;
                if (!this.dismissed) {
                    float f = 0.0f;
                    if (this.minFlingVelocity != 0) {
                        f = ((float) this.windowWidth) * Math.max(Math.min(((Math.abs(xVelocity) * -0.23000002f) / ((float) this.minFlingVelocity)) + MAX_DISTANCE_THRESHOLD, (float) MAX_DISTANCE_THRESHOLD), (float) MIN_DISTANCE_THRESHOLD);
                    }
                    if (!this.isRtl) {
                        if ((x > f && touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getX() >= this.lastX) || xVelocity >= ((float) this.minFlingVelocity)) {
                            this.dismissed = true;
                        }
                    } else if ((x > f && touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getX() <= this.lastX) || (-xVelocity) >= ((float) this.minFlingVelocity)) {
                        this.dismissed = true;
                    }
                }
                if (this.dismissed && this.swiping) {
                    if ((!this.isRtl && xVelocity < ((float) (-this.minFlingVelocity))) || (this.isRtl && xVelocity > ((float) this.minFlingVelocity))) {
                        z = false;
                    }
                    this.dismissed = z;
                }
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void cancel() {
            if (!AGPWindow.this.isTransparent && AGPWindow.this.mAndroidContext != null && (AGPWindow.this.mAndroidContext instanceof Activity)) {
                Activity activity = (Activity) AGPWindow.this.mAndroidContext;
                if (this.activityTranslucencyConverted) {
                    try {
                        Activity.class.getMethod("convertFromTranslucent", new Class[0]).invoke(activity, new Object[0]);
                        this.activityTranslucencyConverted = false;
                        HiLog.debug(AGPWindow.LABEL, "Cancel window swiping convertFromTranslucent done.", new Object[0]);
                    } catch (NoSuchMethodException unused) {
                        HiLog.error(AGPWindow.LABEL, "Activity convert from translucent failed, method not found.", new Object[0]);
                    } catch (IllegalAccessException unused2) {
                        HiLog.error(AGPWindow.LABEL, "Activity convert from translucent failed, access illegal.", new Object[0]);
                    } catch (InvocationTargetException unused3) {
                        HiLog.error(AGPWindow.LABEL, "Activity convert from translucent failed, target invoke failed.", new Object[0]);
                    }
                }
            }
            OnSwipeChangedListener onSwipeChangedListener = this.swipeChangedListener;
            if (onSwipeChangedListener != null) {
                onSwipeChangedListener.onSwipeCancelled();
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void dismiss() {
            HiLog.debug(AGPWindow.LABEL, "Swipe manager dismiss called.", new Object[0]);
            OnDismissListener onDismissListener = this.dismissListener;
            if (onDismissListener != null) {
                onDismissListener.onDismissed();
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void setProgress(float f) {
            int i;
            OnSwipeChangedListener onSwipeChangedListener = this.swipeChangedListener;
            if (onSwipeChangedListener != null && f >= 0.0f && (i = this.windowWidth) > 0) {
                onSwipeChangedListener.onSwipeProgressChanged(progressOfAlpha(f / ((float) i)), f);
            }
        }

        private void checkGesture(TouchEvent touchEvent) {
            if (touchEvent.getAction() == 1) {
                this.blockGesture = this.swipeAnimator.isAnimating();
            }
        }

        /* access modifiers changed from: private */
        public class SwipeAnimator implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
            private final float INTERPOLATOR_FACTOR = 1.5f;
            private final long SWIPE_DURATION;
            private final TimeInterpolator SWIPE_INTERPOLATOR;
            private boolean dismissComplete;
            private final ValueAnimator swipeAnimator;
            private boolean wasCanceled;

            public void onAnimationRepeat(Animator animator) {
            }

            SwipeAnimator() {
                this.SWIPE_DURATION = AGPWindow.this.IS_WATCH ? AGPWindow.GET_INPUTHEIGHT_DELAY : 250;
                this.SWIPE_INTERPOLATOR = new DecelerateInterpolator(1.5f);
                this.swipeAnimator = new ValueAnimator();
                this.wasCanceled = false;
                this.dismissComplete = false;
                this.swipeAnimator.addUpdateListener(this);
                this.swipeAnimator.addListener(this);
            }

            /* access modifiers changed from: package-private */
            public void animateDismiss(float f) {
                if (SwipeManager.this.windowWidth > 0) {
                    animate(f / ((float) SwipeManager.this.windowWidth), 1.0f, this.SWIPE_DURATION, this.SWIPE_INTERPOLATOR, true);
                }
            }

            /* access modifiers changed from: package-private */
            public void animateRecovery(float f) {
                if (SwipeManager.this.windowWidth > 0) {
                    animate(f / ((float) SwipeManager.this.windowWidth), 0.0f, this.SWIPE_DURATION, this.SWIPE_INTERPOLATOR, false);
                }
            }

            /* access modifiers changed from: package-private */
            public boolean isAnimating() {
                return this.swipeAnimator.isStarted();
            }

            private void animate(float f, float f2, long j, TimeInterpolator timeInterpolator, boolean z) {
                this.swipeAnimator.cancel();
                this.dismissComplete = z;
                this.swipeAnimator.setFloatValues(f, f2);
                this.swipeAnimator.setDuration(j);
                this.swipeAnimator.setInterpolator(timeInterpolator);
                this.swipeAnimator.start();
            }

            public void onAnimationStart(Animator animator) {
                this.wasCanceled = false;
            }

            public void onAnimationEnd(Animator animator) {
                HiLog.debug(AGPWindow.LABEL, "SwipeAnimator on animation end called.", new Object[0]);
                if (this.wasCanceled) {
                    return;
                }
                if (this.dismissComplete) {
                    SwipeManager.this.dismiss();
                } else {
                    SwipeManager.this.cancel();
                }
            }

            public void onAnimationCancel(Animator animator) {
                this.wasCanceled = true;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (valueAnimator.getAnimatedValue() instanceof Float) {
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    SwipeManager swipeManager = SwipeManager.this;
                    swipeManager.setProgress(floatValue * ((float) swipeManager.windowWidth));
                }
            }
        }
    }

    private class SwipeChangedListener implements OnSwipeChangedListener {
        private SwipeChangedListener() {
        }

        @Override // ohos.agp.window.wmc.AGPWindow.OnSwipeChangedListener
        public void onSwipeProgressChanged(float f, float f2) {
            AGPWindow.this.setWatchTranslation(f2, true);
        }

        @Override // ohos.agp.window.wmc.AGPWindow.OnSwipeChangedListener
        public void onSwipeCancelled() {
            AGPWindow.this.setWatchTranslation(0.0f, false);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setWatchTranslation(float f, boolean z) {
        Window window = this.mAndroidWindow;
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            if ((z && attributes.x == 0) || (!z && attributes.x != 0)) {
                attributes.x = z ? 1 : 0;
                this.mAndroidWindow.setAttributes(attributes);
                this.mAndroidWindow.setFlags(attributes.x == 0 ? 1024 : 512, BluetoothDeviceClass.MajorClass.IMAGING);
            }
            View peekDecorView = this.mAndroidWindow.peekDecorView();
            if (peekDecorView != null) {
                peekDecorView.setTranslationX(f);
            }
        }
    }

    public void setSoftInputMode(int i) {
        Window window = this.mAndroidWindow;
        if (window != null) {
            window.setSoftInputMode(i);
        } else {
            HiLog.warn(LABEL, "Set soft input mode failed because window is invalid.", new Object[0]);
        }
    }

    /* access modifiers changed from: protected */
    public int getWindowOffsetX() {
        Window window;
        View findViewById;
        android.content.Context context = this.mAndroidContext;
        if (context == null || !(context instanceof Activity) || (window = ((Activity) context).getWindow()) == null || (findViewById = window.findViewById(16908290)) == null) {
            return 0;
        }
        int[] iArr = new int[2];
        findViewById.getLocationInWindow(iArr);
        return iArr[0];
    }

    /* access modifiers changed from: protected */
    public int getWindowOffsetY() {
        Window window;
        View findViewById;
        android.content.Context context = this.mAndroidContext;
        if (context == null || !(context instanceof Activity) || (this instanceof AGPBaseDialogWindow) || (window = ((Activity) context).getWindow()) == null || (findViewById = window.findViewById(16908290)) == null) {
            return 0;
        }
        int[] iArr = new int[2];
        findViewById.getLocationInWindow(iArr);
        return iArr[1];
    }

    /* access modifiers changed from: protected */
    public boolean processSwipeDismiss(TouchEvent touchEvent) {
        SwipeManager swipeManager2 = this.swipeManager;
        if (swipeManager2 == null || !swipeManager2.verifySwiping(touchEvent)) {
            return false;
        }
        this.swipeManager.executeSwiping(touchEvent);
        return true;
    }

    public boolean dispatchTouchEventFromDialog(TouchEvent touchEvent) {
        HiLog.debug(LABEL, "dispatchTouchEventFromDialog TouchEvent.", new Object[0]);
        IAGPEngineAdapter iAGPEngineAdapter = this.mEngine;
        if (iAGPEngineAdapter != null && touchEvent != null) {
            return iAGPEngineAdapter.processTouchEvent(touchEvent);
        }
        HiLog.error(LABEL, "dispatchTouchEventFromDialog not find mEngine.", new Object[0]);
        return false;
    }

    private void handleMovable(TouchEvent touchEvent) {
        if (touchEvent == null) {
            HiLog.error(LABEL, "handleMovable event is null.", new Object[0]);
        } else if (this.movable) {
            if (this.move == null) {
                this.move = new Move();
            }
            int action = touchEvent.getAction();
            if (action == 1) {
                this.move.lastX = touchEvent.getPointerScreenPosition(0).getX();
                this.move.lastY = touchEvent.getPointerScreenPosition(0).getY();
            } else if (action == 3) {
                this.move.nowX = touchEvent.getPointerScreenPosition(0).getX();
                this.move.nowY = touchEvent.getPointerScreenPosition(0).getY();
                Move move2 = this.move;
                move2.tranX = move2.nowX - this.move.lastX;
                Move move3 = this.move;
                move3.tranY = move3.nowY - this.move.lastY;
                WindowManager.LayoutParams layoutParams = this.mAndroidParam;
                if (layoutParams == null) {
                    HiLog.error(LABEL, "handleMovable mAndroidParam is null", new Object[0]);
                    return;
                }
                if (this.boundRect != null) {
                    this.mAndroidWindow.getDecorView().getLocationOnScreen(this.move.location);
                    if (((float) this.boundRect.left) <= ((float) this.move.location[0]) + this.move.tranX && ((float) this.boundRect.right) >= ((float) this.move.location[0]) + this.move.tranX + ((float) this.mAndroidParam.width) && ((float) this.boundRect.top) <= ((float) this.move.location[1]) + this.move.tranY && ((float) this.boundRect.bottom) >= ((float) this.move.location[1]) + this.move.tranY + ((float) this.mAndroidParam.height)) {
                        WindowManager.LayoutParams layoutParams2 = this.mAndroidParam;
                        layoutParams2.x = (int) (((float) layoutParams2.x) + this.move.tranX);
                        WindowManager.LayoutParams layoutParams3 = this.mAndroidParam;
                        layoutParams3.y = (int) (((float) layoutParams3.y) + this.move.tranY);
                        this.mAndroidWindow.setAttributes(this.mAndroidParam);
                    }
                } else {
                    layoutParams.x = (int) (((float) layoutParams.x) + this.move.tranX);
                    WindowManager.LayoutParams layoutParams4 = this.mAndroidParam;
                    layoutParams4.y = (int) (((float) layoutParams4.y) + this.move.tranY);
                    this.mAndroidWindow.setAttributes(this.mAndroidParam);
                }
                Move move4 = this.move;
                move4.lastX = move4.nowX;
                Move move5 = this.move;
                move5.lastY = move5.nowY;
            }
        }
    }

    private void adjustLayout(int i) {
        Context context = this.mContext;
        if (context != null && context.getUITaskDispatcher() != null && i == 2) {
            this.mContext.getUITaskDispatcher().delayDispatch(new Runnable() {
                /* class ohos.agp.window.wmc.AGPWindow.AnonymousClass7 */

                public void run() {
                    if (AGPWindow.this.mViewGroup != null && AGPWindow.this.mSurfaceView != null) {
                        Component findFocus = AGPWindow.this.mViewGroup.findFocus();
                        if (findFocus instanceof Text) {
                            Text text = (Text) findFocus;
                            if (text.isAdjustInputPanel()) {
                                int keyboardWindowHeight = InputMethodController.getInstance().getKeyboardWindowHeight();
                                int actualHeight = AGPWindow.this.mSurfaceView.getActualHeight() - text.getLocationOnScreen()[1];
                                HiLog.debug(AGPWindow.LABEL, "Focus top=%{public}d", Integer.valueOf(text.getLocationOnScreen()[1]));
                                if (actualHeight < keyboardWindowHeight) {
                                    AGPWindow.this.mViewGroup.setHeight(AGPWindow.this.mViewGroup.getHeight() - keyboardWindowHeight);
                                    HiLog.debug(AGPWindow.LABEL, "ViewGroup reduces, height=%{public}d", Integer.valueOf(AGPWindow.this.mViewGroup.getHeight()));
                                    AGPWindow.this.viewGroupResizedFlag = true;
                                    AGPWindow.this.preInputHeight = keyboardWindowHeight;
                                } else if (AGPWindow.this.preInputHeight > 0 && keyboardWindowHeight == 0 && AGPWindow.this.viewGroupResizedFlag) {
                                    AGPWindow.this.mViewGroup.setHeight(AGPWindow.this.mViewGroup.getHeight() + AGPWindow.this.preInputHeight);
                                    HiLog.debug(AGPWindow.LABEL, "ViewGroup enlarged, height=%{public}d", Integer.valueOf(AGPWindow.this.mViewGroup.getHeight()));
                                    AGPWindow.this.preInputHeight = 0;
                                    AGPWindow.this.viewGroupResizedFlag = false;
                                }
                            }
                        }
                    }
                }
            }, GET_INPUTHEIGHT_DELAY);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean dispatchKeyEventFromDecorView(android.view.KeyEvent keyEvent) {
        View decorView;
        Window window = this.mAndroidWindow;
        if (window == null || (decorView = window.getDecorView()) == null) {
            return false;
        }
        return decorView.dispatchKeyEvent(keyEvent);
    }

    private static void setBarrierfreeContext(Context context) {
        mBarrierfreeContext = context;
    }
}
