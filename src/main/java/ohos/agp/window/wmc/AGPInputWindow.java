package ohos.agp.window.wmc;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.WindowManager;
import java.util.Optional;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.components.ComponentContainer;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IPCAdapter;
import ohos.rpc.IRemoteObject;

public class AGPInputWindow extends AGPCommonDialogWindow {
    private static final HiLogLabel LABEL = new HiLogLabel(3, LogDomain.END, "AGPInputWindow");
    private final Rect mBounds = new Rect();
    final Callback mCallback;
    private boolean mShowing;
    final boolean mTakesFocus;
    private AGPInputWindowState mWindowState = AGPInputWindowState.TOKEN_PENDING;

    /* access modifiers changed from: private */
    public enum AGPInputWindowState {
        TOKEN_PENDING,
        TOKEN_SET,
        SHOWN_AT_LEAST_ONCE,
        REJECTED_AT_LEAST_ONCE,
        DESTROYED
    }

    public interface Callback {
        void onBackPressed();
    }

    /* renamed from: ohos.agp.window.wmc.AGPInputWindow$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$agp$window$wmc$AGPInputWindow$AGPInputWindowState = new int[AGPInputWindowState.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            /*
                ohos.agp.window.wmc.AGPInputWindow$AGPInputWindowState[] r0 = ohos.agp.window.wmc.AGPInputWindow.AGPInputWindowState.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.agp.window.wmc.AGPInputWindow.AnonymousClass1.$SwitchMap$ohos$agp$window$wmc$AGPInputWindow$AGPInputWindowState = r0
                int[] r0 = ohos.agp.window.wmc.AGPInputWindow.AnonymousClass1.$SwitchMap$ohos$agp$window$wmc$AGPInputWindow$AGPInputWindowState     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.agp.window.wmc.AGPInputWindow$AGPInputWindowState r1 = ohos.agp.window.wmc.AGPInputWindow.AGPInputWindowState.TOKEN_PENDING     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.agp.window.wmc.AGPInputWindow.AnonymousClass1.$SwitchMap$ohos$agp$window$wmc$AGPInputWindow$AGPInputWindowState     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.agp.window.wmc.AGPInputWindow$AGPInputWindowState r1 = ohos.agp.window.wmc.AGPInputWindow.AGPInputWindowState.TOKEN_SET     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.agp.window.wmc.AGPInputWindow.AnonymousClass1.$SwitchMap$ohos$agp$window$wmc$AGPInputWindow$AGPInputWindowState     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.agp.window.wmc.AGPInputWindow$AGPInputWindowState r1 = ohos.agp.window.wmc.AGPInputWindow.AGPInputWindowState.SHOWN_AT_LEAST_ONCE     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.agp.window.wmc.AGPInputWindow.AnonymousClass1.$SwitchMap$ohos$agp$window$wmc$AGPInputWindow$AGPInputWindowState     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.agp.window.wmc.AGPInputWindow$AGPInputWindowState r1 = ohos.agp.window.wmc.AGPInputWindow.AGPInputWindowState.REJECTED_AT_LEAST_ONCE     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = ohos.agp.window.wmc.AGPInputWindow.AnonymousClass1.$SwitchMap$ohos$agp$window$wmc$AGPInputWindow$AGPInputWindowState     // Catch:{ NoSuchFieldError -> 0x0040 }
                ohos.agp.window.wmc.AGPInputWindow$AGPInputWindowState r1 = ohos.agp.window.wmc.AGPInputWindow.AGPInputWindowState.DESTROYED     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.agp.window.wmc.AGPInputWindow.AnonymousClass1.<clinit>():void");
        }
    }

    public void setToken(IRemoteObject iRemoteObject) {
        int i = AnonymousClass1.$SwitchMap$ohos$agp$window$wmc$AGPInputWindow$AGPInputWindowState[this.mWindowState.ordinal()];
        if (i == 1) {
            Optional<Object> translateToIBinder = IPCAdapter.translateToIBinder(iRemoteObject);
            if (!translateToIBinder.isPresent() || !(translateToIBinder.get() instanceof IBinder)) {
                this.mAndroidParam.token = null;
                HiLog.info(LABEL, "Set token null.", new Object[0]);
            } else {
                this.mAndroidParam.token = (IBinder) translateToIBinder.get();
                HiLog.info(LABEL, "Set token success.", new Object[0]);
            }
            this.mAndroidWindow.setAttributes(this.mAndroidParam);
            this.mWindowState = AGPInputWindowState.TOKEN_SET;
        } else if (i == 2 || i == 3 || i == 4) {
            throw new IllegalStateException("setToken can be called only once");
        } else if (i == 5) {
            HiLog.debug(LABEL, "Ignoring setToken() because window is already destroyed.", new Object[0]);
        } else {
            throw new IllegalStateException("Unexpected state=" + this.mWindowState);
        }
    }

    public AGPInputWindow(Context context, String str, Callback callback, int i, boolean z) {
        super(context, 8);
        this.mAndroidWindow.setType(i);
        this.mAndroidWindow.setTitle(str);
        this.mAndroidWindow.setGravity(AGPWindowManager.getAndroidGravity(80));
        this.mCallback = callback;
        this.mTakesFocus = z;
        this.mAndroidParam = this.mAndroidWindow.getAttributes();
        this.mShowing = false;
        updateWidthHeight();
        this.mAndroidWindow.setAttributes(this.mAndroidParam);
        setFlags();
    }

    public void setGravity(int i) {
        this.mAndroidParam.gravity = AGPWindowManager.getAndroidGravity(i);
        updateWidthHeight();
        this.mAndroidWindow.setAttributes(this.mAndroidParam);
    }

    public int getGravity() {
        return AGPWindowManager.getZidaneTextAlignment(this.mAndroidParam.gravity);
    }

    @Override // ohos.agp.window.wmc.AGPWindow
    public void setContentLayout(ComponentContainer componentContainer) {
        super.setPreContentLayout(componentContainer, this.mDeviceWidth, this.mDeviceHeight);
        super.setContentLayout(componentContainer);
    }

    @Override // ohos.agp.window.wmc.AGPWindow, ohos.agp.window.wmc.AGPCommonDialogWindow
    public void show() {
        int i = AnonymousClass1.$SwitchMap$ohos$agp$window$wmc$AGPInputWindow$AGPInputWindowState[this.mWindowState.ordinal()];
        if (i == 1) {
            throw new IllegalStateException("Window token is not set yet.");
        } else if (i == 2 || i == 3) {
            doPerformance();
        } else if (i == 4) {
            HiLog.debug(LABEL, "Not trying to call show() because it was already rejected once.", new Object[0]);
        } else if (i == 5) {
            HiLog.debug(LABEL, "Ignoring show() because the window is already destroyed.", new Object[0]);
        } else {
            throw new IllegalStateException("Unexpected state=" + this.mWindowState);
        }
    }

    @Override // ohos.agp.window.wmc.AGPWindow, ohos.agp.window.wmc.AGPCommonDialogWindow
    public void destroy() {
        int i = AnonymousClass1.$SwitchMap$ohos$agp$window$wmc$AGPInputWindow$AGPInputWindowState[this.mWindowState.ordinal()];
        if (i == 1 || i == 2) {
            this.mWindowState = AGPInputWindowState.DESTROYED;
        } else if (i == 3) {
            try {
                this.mAndroidWindow.setWindowAnimations(0);
                super.destroy();
            } catch (WindowManager.BadTokenException unused) {
                HiLog.debug(LABEL, "Window token is already invalidated. No need to destroy it.", new Object[0]);
            }
            this.mWindowState = AGPInputWindowState.DESTROYED;
        } else if (i == 4) {
            HiLog.debug(LABEL, "Not trying to dismiss the window because it is most likely unnecessary.", new Object[0]);
            this.mWindowState = AGPInputWindowState.DESTROYED;
        } else if (i != 5) {
            throw new IllegalStateException("Unexpected state=" + this.mWindowState);
        } else {
            throw new IllegalStateException("destroy can be called only once");
        }
    }

    @Override // ohos.agp.window.wmc.AGPWindow, ohos.agp.window.wmc.AGPCommonDialogWindow
    public void hide() {
        if (this.mDecor == null) {
            HiLog.error(LABEL, "hide mDecor is null.", new Object[0]);
        } else {
            this.mDecor.setVisibility(8);
        }
    }

    @Override // ohos.agp.window.wmc.AGPCommonDialogWindow
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent == null) {
            HiLog.error(LABEL, "dispatchTouchEvent event is null", new Object[0]);
            return false;
        } else if (this.mDecor == null) {
            HiLog.error(LABEL, "dispatchTouchEvent mDecor is null", new Object[0]);
            return false;
        } else {
            this.mDecor.getHitRect(this.mBounds);
            if (motionEvent.isWithinBoundsNoHistory((float) this.mBounds.left, (float) this.mBounds.top, (float) (this.mBounds.right - 1), (float) (this.mBounds.bottom - 1))) {
                return super.dispatchTouchEvent(motionEvent);
            }
            MotionEvent clampNoHistory = motionEvent.clampNoHistory((float) this.mBounds.left, (float) this.mBounds.top, (float) (this.mBounds.right - 1), (float) (this.mBounds.bottom - 1));
            boolean dispatchTouchEvent = super.dispatchTouchEvent(clampNoHistory);
            clampNoHistory.recycle();
            return dispatchTouchEvent;
        }
    }

    public void onBackPressed() {
        Callback callback = this.mCallback;
        if (callback == null) {
            HiLog.error(LABEL, "mCallback is null.", new Object[0]);
        } else {
            callback.onBackPressed();
        }
    }

    @Override // ohos.agp.window.wmc.AGPWindow, ohos.agp.window.wmc.AGPBaseDialogWindow
    public void onSizeChange(Configuration configuration) {
        HiLog.debug(LABEL, "onSizeChange.", new Object[0]);
    }

    private void doPerformance() {
        boolean z;
        try {
            boolean z2 = true;
            if (this.mShowing) {
                ComponentContainer containerLayout = getContainerLayout();
                if (containerLayout != null) {
                    containerLayout.setWidth(this.mAndroidParam.width);
                    containerLayout.setHeight(this.mAndroidParam.height);
                    getDisplaySize(this.mContext);
                    super.setPreContentLayout(containerLayout, this.mDeviceWidth, this.mDeviceHeight);
                    WindowManager.LayoutParams attributes = this.mAndroidWindow.getAttributes();
                    if (attributes.width != containerLayout.getWidth()) {
                        attributes.width = containerLayout.getWidth();
                        z = true;
                    } else {
                        z = false;
                    }
                    if (attributes.height != containerLayout.getHeight()) {
                        attributes.height = containerLayout.getHeight();
                    } else {
                        z2 = z;
                    }
                    if (z2) {
                        this.mAndroidWindow.setAttributes(attributes);
                    }
                }
                if (this.mDecor != null) {
                    this.mDecor.setVisibility(0);
                    return;
                }
                return;
            }
            handleWrapContent();
            super.show();
            this.mShowing = true;
            HiLog.debug(LABEL, "set mShowing true.", new Object[0]);
            this.mWindowState = AGPInputWindowState.SHOWN_AT_LEAST_ONCE;
        } catch (WindowManager.BadTokenException unused) {
            HiLog.debug(LABEL, "Window token is already invalidated. show() does nothing.", new Object[0]);
            this.mWindowState = AGPInputWindowState.REJECTED_AT_LEAST_ONCE;
        }
    }

    private void setFlags() {
        int i;
        int i2;
        if (this.mTakesFocus) {
            i2 = 288;
            i = 298;
        } else {
            i2 = 264;
            i = 266;
        }
        this.mAndroidWindow.setFlags(i2, i);
    }

    private void updateWidthHeight() {
        if (this.mAndroidParam == null) {
            HiLog.debug(LABEL, "updateWidthHeight mAndroidParam is null", new Object[0]);
        } else if (this.mAndroidParam.gravity == AGPWindowManager.getAndroidGravity(80) || this.mAndroidParam.gravity == AGPWindowManager.getAndroidGravity(48)) {
            this.mAndroidParam.height = -2;
            this.mAndroidParam.width = -1;
        } else {
            this.mAndroidParam.height = -1;
            this.mAndroidParam.width = -2;
        }
    }

    private void handleWrapContent() {
        if (this.mAndroidParam.width == -2) {
            this.mAndroidParam.width = getContainerLayout().getWidth();
        }
        if (this.mAndroidParam.height == -2) {
            this.mAndroidParam.height = getContainerLayout().getHeight();
        }
    }
}
