package ohos.agp.window.wmc;

import android.graphics.Point;
import android.view.ActionMode;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import com.android.internal.policy.PhoneWindow;
import java.util.Optional;
import ohos.aafwk.utils.log.LogDomain;
import ohos.accessibility.AccessibilityEventInfo;
import ohos.accessibility.BarrierFreeInnerClient;
import ohos.agp.window.wmc.AGPWindow;
import ohos.agp.window.wmc.AGPWindowManager;
import ohos.app.Context;
import ohos.bundle.AbilityInfo;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.KeyEvent;
import ohos.multimodalinput.event.MouseEvent;
import ohos.multimodalinput.event.MultimodalEvent;
import ohos.multimodalinput.event.RotationEvent;
import ohos.multimodalinput.event.TouchEvent;
import ohos.multimodalinput.eventimpl.MultimodalEventFactory;

public class AGPCommonDialogWindow extends AGPBaseDialogWindow implements Window.Callback {
    private static final float DEFAULT_SCALE = 0.5f;
    private static final HiLogLabel LABEL = new HiLogLabel(3, LogDomain.END, "AGPWindow");
    private static final int TOUCH_ERROR = -1;
    private static final int TOUCH_INSIDE = 1;
    private static final int TOUCH_OUTSIDE = 0;
    private EventListener keyEventListener;
    protected View mDecor;
    private IComDialogPAEventLisener mIComdDialogPAListener;
    private boolean mIsKeyDownOut;
    private OnWindowParamUpdatedCallback mOnWindowParamUpdatedCallback;
    private OnWindowSelectionUpdatedCallback mOnWindowSelectionUpdatedCallback;
    private EventListener touchEventListener;

    public interface EventListener {
        boolean keyProcess(KeyEvent keyEvent);

        boolean touchProcess(TouchEvent touchEvent);
    }

    public interface IComDialogPAEventLisener {
        void onDispatch(AccessibilityEventInfo accessibilityEventInfo);
    }

    public interface OnWindowParamUpdatedCallback {
        void onWindowParamUpdated(AGPWindow.LayoutParams layoutParams);
    }

    public interface OnWindowSelectionUpdatedCallback {
        void onAGPWindowSelectionUpdated(boolean z);
    }

    public boolean dispatchKeyShortcutEvent(android.view.KeyEvent keyEvent) {
        return false;
    }

    public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
        return false;
    }

    public void onActionModeFinished(ActionMode actionMode) {
    }

    public void onActionModeStarted(ActionMode actionMode) {
    }

    public void onAttachedToWindow() {
    }

    public void onContentChanged() {
    }

    public boolean onCreatePanelMenu(int i, Menu menu) {
        return false;
    }

    public View onCreatePanelView(int i) {
        return null;
    }

    public void onDetachedFromWindow() {
    }

    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        return false;
    }

    public boolean onMenuOpened(int i, Menu menu) {
        return false;
    }

    public void onPanelClosed(int i, Menu menu) {
    }

    public boolean onPreparePanel(int i, View view, Menu menu) {
        return false;
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onSearchRequested(SearchEvent searchEvent) {
        return false;
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return null;
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
        return null;
    }

    public AGPCommonDialogWindow(Context context, int i) {
        super(context, i);
        this.mAndroidWindow = new PhoneWindow(this.mAndroidContext);
        if (context.getAbilityInfo() == null || context.getAbilityInfo().getType() != AbilityInfo.AbilityType.PAGE) {
            HiLog.debug(LABEL, "AGPCommonDialogWindow construct func", new Object[0]);
            this.mAndroidWindow.setType(2038);
        }
        this.mAndroidWindow.requestFeature(1);
        FrameLayout frameLayout = new FrameLayout(this.mAndroidContext);
        View view = new View(this.mAndroidContext);
        view.setAlpha(0.0f);
        frameLayout.addView(this.mSurfaceView, -1, -1);
        frameLayout.addView(view, -1, -1);
        this.mAndroidWindow.setContentView(frameLayout);
        this.mAndroidWindow.setCallback(this);
        this.mAndroidWindow.setWindowManager(this.mAndroidWindowManager, null, null);
        this.mAndroidParam = this.mAndroidWindow.getAttributes();
        this.mAndroidWindow.setGravity(17);
    }

    public void setDefaultLayoutParam() {
        if (this.mAndroidParam == null) {
            HiLog.error(LABEL, "setDefaultLayoutParam failed due to mAndroidParam is null", new Object[0]);
            return;
        }
        Display defaultDisplay = this.mAndroidWindowManager.getDefaultDisplay();
        Point point = new Point();
        if (defaultDisplay == null) {
            HiLog.error(LABEL, "setDefaultLayoutParam failed due to display is null", new Object[0]);
            return;
        }
        defaultDisplay.getSize(point);
        this.mAndroidParam.x = 0;
        this.mAndroidParam.y = 0;
        this.mAndroidParam.width = (int) (((float) point.x) * 0.5f);
        this.mAndroidParam.height = (int) (((float) point.y) * 0.5f);
    }

    @Override // ohos.agp.window.wmc.AGPWindow
    public void show() {
        if (this.mAndroidParam == null) {
            HiLog.error(LABEL, "show failed due to mAndroidParam is null", new Object[0]);
            return;
        }
        this.mDecor = this.mAndroidWindow.getDecorView();
        if (this.mIsShown && this.mDecor.getVisibility() != 0) {
            this.mDecor.setVisibility(0);
        }
        setPadding(0, 0, 0, 0);
        if (!this.mIsShown) {
            try {
                this.mAndroidWindowManager.addView(this.mDecor, this.mAndroidParam);
                this.mAndroidWindow.addFlags(2);
            } catch (WindowManager.BadTokenException e) {
                HiLog.error(LABEL, "AGPCommonDialogWindow show failed because permission denied", new Object[0]);
                throw new AGPWindowManager.BadWindowException("Permission denied" + e.getLocalizedMessage());
            }
        }
        super.show();
    }

    @Override // ohos.agp.window.wmc.AGPWindow
    public void hide() {
        View view = this.mDecor;
        if (view != null) {
            view.setVisibility(8);
        }
        super.hide();
    }

    @Override // ohos.agp.window.wmc.AGPWindow
    public void destroy() {
        if (this.mDecor != null) {
            HiLog.debug(LABEL, "AGPCommonDialogWindow removeView", new Object[0]);
            this.mAndroidWindowManager.removeView(this.mDecor);
        }
        super.destroy();
    }

    public boolean dispatchKeyEvent(android.view.KeyEvent keyEvent) {
        boolean z;
        if (keyEvent == null) {
            HiLog.debug(LABEL, "dispatchKeyEvent event is null", new Object[0]);
            return false;
        }
        MultimodalEvent orElse = MultimodalEventFactory.createEvent(keyEvent).orElse(null);
        if (orElse instanceof KeyEvent) {
            KeyEvent keyEvent2 = (KeyEvent) orElse;
            EventListener eventListener = this.keyEventListener;
            z = eventListener != null ? eventListener.keyProcess(keyEvent2) : dispatchKeyboardEvent(keyEvent2);
        } else {
            z = false;
        }
        if (z) {
            return z;
        }
        if (keyEvent.getKeyCode() == 4 && keyEvent.getAction() == 0) {
            HiLog.debug(LABEL, "Default process keyevent by window.", new Object[0]);
            if (this.mDialogDestoryListener != null) {
                this.mDialogDestoryListener.dialogDestroy();
            } else {
                destroy();
            }
            return true;
        }
        HiLog.debug(LABEL, "Default process keyevent by A.", new Object[0]);
        if (this.mAndroidWindow != null) {
            return this.mAndroidWindow.superDispatchKeyEvent(keyEvent);
        }
        HiLog.error(LABEL, "superDispatchKeyEvent failed due to Window is null", new Object[0]);
        return false;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent == null) {
            HiLog.debug(LABEL, "dispatchTouchEvent event is null", new Object[0]);
            return false;
        } else if (onTouchEvent(motionEvent)) {
            return true;
        } else {
            if (this.mAndroidWindow != null) {
                return this.mAndroidWindow.superDispatchTouchEvent(motionEvent);
            }
            HiLog.error(LABEL, "superDispatchTouchEvent failed due to Window is null", new Object[0]);
            return false;
        }
    }

    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        boolean z;
        if (motionEvent == null) {
            HiLog.debug(LABEL, "Dialog dispatchGenericMotionEvent event is null", new Object[0]);
            return false;
        }
        MultimodalEvent orElse = MultimodalEventFactory.createEvent(motionEvent).orElse(null);
        if (orElse instanceof MouseEvent) {
            HiLog.debug(LABEL, "Dialog dispatch MouseEvent from A GenericMotionEvent", new Object[0]);
            z = dispatchMouseEvent((MouseEvent) orElse);
        } else if (orElse instanceof RotationEvent) {
            HiLog.debug(LABEL, "Dialog dispatch RotationEvent from A GenericMotionEvent", new Object[0]);
            z = dispatchRotationEvent((RotationEvent) orElse);
        } else {
            HiLog.debug(LABEL, "Dialog dispatch unsupported event from A GenericMotionEvent", new Object[0]);
            z = false;
        }
        if (z) {
            return z;
        }
        HiLog.debug(LABEL, "Default process GenericMotionEvent by A.", new Object[0]);
        if (this.mAndroidWindow != null) {
            return this.mAndroidWindow.superDispatchGenericMotionEvent(motionEvent);
        }
        HiLog.error(LABEL, "superDispatchGenericMotionEvent failed due to Window is null", new Object[0]);
        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        synchronized (this) {
            if (accessibilityEvent != null) {
                if (this.mIComdDialogPAListener != null) {
                    AccessibilityEventInfo accessibilityEventInfo = new AccessibilityEventInfo();
                    BarrierFreeInnerClient.fillBarrierFreeEventInfo(accessibilityEvent, accessibilityEventInfo);
                    this.mIComdDialogPAListener.onDispatch(accessibilityEventInfo);
                    BarrierFreeInnerClient.fillAccessibilityEventInfo(this.mContext, accessibilityEventInfo, accessibilityEvent);
                    return true;
                }
            }
            HiLog.debug(LABEL, "Populate Accessibility Event event is null", new Object[0]);
            return false;
        }
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams layoutParams) {
        if (!(this.mDecor == null || this.mAndroidParam == null)) {
            this.mAndroidWindowManager.updateViewLayout(this.mDecor, this.mAndroidParam);
        }
        if (this.mOnWindowParamUpdatedCallback != null) {
            AGPWindow.LayoutParams transferAttributes = transferAttributes(layoutParams);
            if (transferAttributes == null) {
                HiLog.error(LABEL, "The transfer is failed due to Android window attribute is null.", new Object[0]);
            } else {
                this.mOnWindowParamUpdatedCallback.onWindowParamUpdated(transferAttributes);
            }
        }
    }

    public void setKeyEventListener(EventListener eventListener) {
        this.keyEventListener = eventListener;
    }

    public void setTouchEventListener(EventListener eventListener) {
        this.touchEventListener = eventListener;
    }

    public void registerOnWindowParamUpdatedCallback(OnWindowParamUpdatedCallback onWindowParamUpdatedCallback) {
        this.mOnWindowParamUpdatedCallback = onWindowParamUpdatedCallback;
    }

    public void onWindowFocusChanged(boolean z) {
        OnWindowSelectionUpdatedCallback onWindowSelectionUpdatedCallback = this.mOnWindowSelectionUpdatedCallback;
        if (onWindowSelectionUpdatedCallback != null) {
            onWindowSelectionUpdatedCallback.onAGPWindowSelectionUpdated(z);
        }
    }

    public void registerOnWindowSelectionUpdatedCallback(OnWindowSelectionUpdatedCallback onWindowSelectionUpdatedCallback) {
        this.mOnWindowSelectionUpdatedCallback = onWindowSelectionUpdatedCallback;
    }

    public void setComDialogPAEventListener(IComDialogPAEventLisener iComDialogPAEventLisener) {
        synchronized (this) {
            this.mIComdDialogPAListener = iComDialogPAEventLisener;
        }
    }

    private int isTouchOutsideWindow(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 4) {
            return 0;
        }
        if (this.mDecor == null) {
            HiLog.error(LABEL, "AGPCommonDialogWindow isTouchOutsideWindow return true due to mDecor is null", new Object[0]);
            return -1;
        }
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (this.mAndroidContext != null) {
            int scaledWindowTouchSlop = ViewConfiguration.get(this.mAndroidContext).getScaledWindowTouchSlop();
            int i = -scaledWindowTouchSlop;
            if (x >= i && y >= i && x <= this.mDecor.getWidth() + scaledWindowTouchSlop && y <= this.mDecor.getHeight() + scaledWindowTouchSlop) {
                if (motionEvent.getAction() == 0 || motionEvent.getAction() == 1) {
                    this.mIsKeyDownOut = false;
                }
                return 1;
            } else if (motionEvent.getAction() == 0) {
                this.mIsKeyDownOut = true;
                return 0;
            } else if (!this.mIsKeyDownOut) {
                return 1;
            } else {
                if (motionEvent.getAction() == 1) {
                    this.mIsKeyDownOut = false;
                }
                return 0;
            }
        } else {
            HiLog.error(LABEL, "isTouchOutsideWindow() mAndroidContext is null", new Object[0]);
            return -1;
        }
    }

    private boolean onTouchEvent(MotionEvent motionEvent) {
        if (isTouchOutsideWindow(motionEvent) != 0) {
            if (isTouchOutsideWindow(motionEvent) == 1) {
                handleMovable(motionEvent);
                Optional<MultimodalEvent> createEvent = MultimodalEventFactory.createEvent(motionEvent);
                if (createEvent.isPresent()) {
                    MultimodalEvent multimodalEvent = createEvent.get();
                    if (multimodalEvent instanceof TouchEvent) {
                        TouchEvent touchEvent = (TouchEvent) multimodalEvent;
                        if (this.swipeDismissEnabled && processSwipeDismiss(touchEvent)) {
                            HiLog.debug(LABEL, "AGPCommonDialog process swipe process true, event continue.", new Object[0]);
                        }
                        EventListener eventListener = this.touchEventListener;
                        if (eventListener != null) {
                            return eventListener.touchProcess(touchEvent);
                        }
                        return dispatchTouchEventFromDialog(touchEvent);
                    }
                }
                HiLog.error(LABEL, "MultimodalEvent is null or multimodalEvent is not instance of Touchevent", new Object[0]);
            }
            return false;
        } else if (this.mListener != null) {
            return this.mListener.isTouchOutside();
        } else {
            HiLog.error(LABEL, "AGPCommonDialogWindow onTouchEvent return true due to mListener is null", new Object[0]);
            return false;
        }
    }

    private void handleMovable(MotionEvent motionEvent) {
        if (this.movable) {
            if (this.move == null) {
                this.move = new AGPWindow.Move();
            }
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                this.move.lastX = motionEvent.getRawX();
                this.move.lastY = motionEvent.getRawY();
            } else if (actionMasked == 2) {
                this.move.nowX = motionEvent.getRawX();
                this.move.nowY = motionEvent.getRawY();
                this.move.tranX = this.move.nowX - this.move.lastX;
                this.move.tranY = this.move.nowY - this.move.lastY;
                if (this.mAndroidParam == null) {
                    HiLog.error(LABEL, "handleMovable mAndroidParam is null", new Object[0]);
                    return;
                }
                if (this.boundRect != null) {
                    View view = this.mDecor;
                    if (view == null) {
                        HiLog.error(LABEL, "handleMovable mDecor is null", new Object[0]);
                        return;
                    }
                    view.getLocationOnScreen(this.move.location);
                    if (((float) this.boundRect.left) <= ((float) this.move.location[0]) + this.move.tranX && ((float) this.boundRect.right) >= ((float) this.move.location[0]) + this.move.tranX + ((float) this.mAndroidParam.width) && ((float) this.boundRect.top) <= ((float) this.move.location[1]) + this.move.tranY && ((float) this.boundRect.bottom) >= ((float) this.move.location[1]) + this.move.tranY + ((float) this.mAndroidParam.height)) {
                        WindowManager.LayoutParams layoutParams = this.mAndroidParam;
                        layoutParams.x = (int) (((float) layoutParams.x) + this.move.tranX);
                        WindowManager.LayoutParams layoutParams2 = this.mAndroidParam;
                        layoutParams2.y = (int) (((float) layoutParams2.y) + this.move.tranY);
                        this.mAndroidWindow.setAttributes(this.mAndroidParam);
                    }
                } else {
                    WindowManager.LayoutParams layoutParams3 = this.mAndroidParam;
                    layoutParams3.x = (int) (((float) layoutParams3.x) + this.move.tranX);
                    WindowManager.LayoutParams layoutParams4 = this.mAndroidParam;
                    layoutParams4.y = (int) (((float) layoutParams4.y) + this.move.tranY);
                    this.mAndroidWindow.setAttributes(this.mAndroidParam);
                }
                this.move.lastX = this.move.nowX;
                this.move.lastY = this.move.nowY;
            }
        }
    }

    public boolean setSwipeToDismiss(boolean z, AGPWindow.OnDismissListener onDismissListener) {
        HiLog.debug(LABEL, "AGPCommonDialogWindow set swipe to dismiss begin.", new Object[0]);
        boolean swipeToDismiss = setSwipeToDismiss(z);
        if (!z || !swipeToDismiss) {
            return swipeToDismiss;
        }
        if (this.mAndroidContext == null || this.swipeManager == null) {
            HiLog.debug(LABEL, "AGPCommonDialogWindow set swipe to dismiss failed because context invalid.", new Object[0]);
            return false;
        }
        this.swipeManager.setOnDismissListener(onDismissListener);
        setWindowOnSwipe();
        this.swipeManager.setCanScroll(false);
        this.swipeManager.init(this.mAndroidContext, this.mContext);
        return true;
    }
}
