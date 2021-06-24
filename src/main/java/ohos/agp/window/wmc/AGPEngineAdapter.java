package ohos.agp.window.wmc;

import android.content.Context;
import android.view.Surface;
import android.view.View;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.ComponentContainer;
import ohos.agp.utils.SystemSettingsHelper;
import ohos.agp.window.aspbshell.AppInfoGetter;
import ohos.global.configuration.Configuration;
import ohos.global.resource.ResourceManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.KeyBoardEvent;
import ohos.multimodalinput.event.KeyEvent;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.MouseEvent;
import ohos.multimodalinput.event.RotationEvent;
import ohos.multimodalinput.event.TouchEvent;

public class AGPEngineAdapter implements IAGPEngineAdapter {
    private static final int DEVICE_TYPE_LITE_WEARABLE = 2;
    private static final int DEVICE_TYPE_PHONE = 0;
    private static final int DEVICE_TYPE_WEARABLE = 1;
    private static final HiLogLabel LABEL = new HiLogLabel(3, LogDomain.END, "AGPEngineAdapter");
    private static final int MI_FUNC_REMOVE_ALL = 3;
    private static final int MI_FUNC_SUBSCRIB = 1;
    private static final int MI_FUNC_UNSUBSCRIB = 2;
    private static final int SURFACE_STATE_CREATED = 1;
    private static final int SURFACE_STATE_HIDDEN = 3;
    private static final int SURFACE_STATE_SHOWING = 2;
    private static final int SURFACE_STATE_UNINITIALIZED = 0;
    private static boolean sIsRenderCacheInitialized = false;
    private Context mAndroidContext;
    private Configuration mConfiguration;
    private ohos.app.Context mContext;
    private int mFlag;
    private IAGPInputListener mInputListener;
    private long mNativeWindowPtr;
    private ComponentContainer mRootView;
    private Surface mSurface;
    private int mSurfaceHeight;
    private volatile int mSurfaceState = 0;
    private int mSurfaceWidth;

    public interface IAGPInputListener {
        void onInputStart();

        void onInputStop();

        void onNotifyFocus(int i);
    }

    private native long nativeCreate(float f, int i);

    private native void nativeDestroy(long j);

    private native boolean nativeDispatchKeyboardEvent(long j, int[] iArr, long[] jArr, String[] strArr);

    private native boolean nativeDispatchMouseEvent(long j, MouseEvent mouseEvent);

    private native boolean nativeDispatchRotationEvent(long j, RotationEvent rotationEvent);

    private native boolean nativeDispatchTouchEvent(long j, TouchEvent touchEvent, int[] iArr, float[] fArr);

    private native boolean nativeDraw(long j, Surface surface, int i, int i2);

    private native boolean nativeLoad(long j, Surface surface, int i, int i2);

    private native void nativeNotifyBarrierFree(long j, boolean z);

    private native void nativeNotifyWindowFocusChange(long j, boolean z);

    private native void nativePreSetContentLayout(long j, ComponentContainer componentContainer, int i, int i2);

    private native void nativeSaveAbility(long j, ohos.app.Context context);

    private native void nativeSaveContentView(long j, View view);

    private native void nativeSaveFlag(long j, int i);

    private native void nativeSetBackgroundColor(long j, int i, int i2, int i3);

    private native void nativeSetContentLayout(long j, ComponentContainer componentContainer);

    private native void nativeSetMultiModel(long j, long j2);

    private native void nativeSetTransparent(long j, int i);

    private native void nativeSetWindowOffset(long j, int i, int i2);

    private native void nativeSetWindowVisibleRect(long j, int i, int i2, int i3, int i4);

    private native void nativeSetupDiskCache(String str);

    private native void nativeStartRender(long j);

    private native void nativeStopRender(long j);

    @Override // ohos.agp.window.wmc.IAGPEngineAdapter
    public void loadEngine() {
    }

    @Override // ohos.agp.window.wmc.IAGPEngineAdapter
    public void processVSync(long j) {
    }

    public AGPEngineAdapter(ohos.app.Context context, int i, View view) {
        this.mContext = context;
        this.mFlag = i;
        HiLog.debug(LABEL, "AGPEngineAdapter", new Object[0]);
        if (!sIsRenderCacheInitialized && context != null) {
            sIsRenderCacheInitialized = true;
            nativeSetupDiskCache(context.getCacheDir().getAbsolutePath());
        }
        this.mNativeWindowPtr = nativeCreate(AttrHelper.getDensity(this.mContext), getDeviceType());
        long j = this.mNativeWindowPtr;
        if (j == 0) {
            HiLog.error(LABEL, "AGPWindow mNativeWindowPtr is null", new Object[0]);
        } else if (i != 10) {
            nativeSaveFlag(j, i);
            nativeSaveAbility(this.mNativeWindowPtr, context);
            nativeSaveContentView(this.mNativeWindowPtr, view);
            if (context == null) {
                HiLog.error(LABEL, "AGPWindow context is null", new Object[0]);
                return;
            }
            Object hostContext = context.getHostContext();
            if (hostContext instanceof Context) {
                this.mAndroidContext = (Context) hostContext;
                AppInfoGetter.setAppNameToNative(this.mAndroidContext.getApplicationContext());
                return;
            }
            HiLog.error(LABEL, "AGPWindow context.getHostContext() is not android content instance", new Object[0]);
        }
    }

    @Override // ohos.agp.window.wmc.IAGPEngineAdapter
    public void processSurfaceCreated(Surface surface) {
        HiLog.debug(LABEL, "processSurfaceCreated", new Object[0]);
        long j = this.mNativeWindowPtr;
        if (j != 0 && surface != null) {
            this.mSurface = surface;
            this.mSurfaceState = 1;
            nativeStartRender(j);
        }
    }

    @Override // ohos.agp.window.wmc.IAGPEngineAdapter
    public void processSurfaceChanged(Surface surface, int i, int i2, int i3) {
        HiLog.debug(LABEL, "ProcessSurfaceChanged w=%{public}d, h=%{public}d", Integer.valueOf(i2), Integer.valueOf(i3));
        if (this.mSurfaceState != 0) {
            long j = this.mNativeWindowPtr;
            if (j != 0 && surface != null) {
                this.mSurface = surface;
                this.mSurfaceWidth = i2;
                this.mSurfaceHeight = i3;
                nativeLoad(j, surface, i2, i3);
                this.mSurfaceState = 2;
            }
        }
    }

    @Override // ohos.agp.window.wmc.IAGPEngineAdapter
    public void processSurfaceDestroy(Surface surface) {
        HiLog.debug(LABEL, "processSurfaceDestroy", new Object[0]);
        if (this.mSurfaceState != 0 && this.mNativeWindowPtr != 0 && surface != null) {
            if (this.mSurfaceState >= 2) {
                nativeStopRender(this.mNativeWindowPtr);
            }
            this.mSurfaceState = 0;
        }
    }

    @Override // ohos.agp.window.wmc.IAGPEngineAdapter
    public boolean processTouchEvent(TouchEvent touchEvent) {
        if (this.mNativeWindowPtr == 0 || touchEvent == null) {
            return false;
        }
        int pointerCount = touchEvent.getPointerCount();
        float[] fArr = new float[(pointerCount * 3)];
        for (int i = 0; i < pointerCount; i++) {
            MmiPoint pointerPosition = touchEvent.getPointerPosition(i);
            int pointerId = touchEvent.getPointerId(i);
            int i2 = i * 3;
            fArr[i2] = pointerPosition.getX();
            fArr[i2 + 1] = pointerPosition.getY();
            fArr[i2 + 2] = (float) pointerId;
        }
        return nativeDispatchTouchEvent(this.mNativeWindowPtr, touchEvent, new int[]{touchEvent.getIndex(), touchEvent.getAction(), touchEvent.getPhase()}, fArr);
    }

    private void getKeyboardParam(KeyEvent keyEvent, int[] iArr, long[] jArr, String[] strArr) {
        if (keyEvent != null) {
            iArr[0] = !keyEvent.isKeyDown();
            iArr[1] = keyEvent.getKeyCode();
            iArr[2] = keyEvent.getSourceDevice();
            iArr[3] = keyEvent.getInputDeviceId();
            if (keyEvent instanceof KeyBoardEvent) {
                KeyBoardEvent keyBoardEvent = (KeyBoardEvent) keyEvent;
                iArr[4] = keyBoardEvent.isNoncharacterKeyPressed(2047) | keyBoardEvent.isNoncharacterKeyPressed(2048) ? 1 : 0;
                iArr[5] = keyBoardEvent.isNoncharacterKeyPressed(KeyEvent.KEY_CTRL_LEFT) | keyBoardEvent.isNoncharacterKeyPressed(KeyEvent.KEY_CTRL_RIGHT) ? 1 : 0;
                iArr[6] = keyBoardEvent.isNoncharacterKeyPressed(2045) | keyBoardEvent.isNoncharacterKeyPressed(KeyEvent.KEY_ALT_RIGHT) ? 1 : 0;
                iArr[7] = keyBoardEvent.isNoncharacterKeyPressed(KeyEvent.KEY_CAPS_LOCK) ? 1 : 0;
                iArr[8] = keyBoardEvent.getUnicode();
            }
            jArr[0] = keyEvent.getKeyDownDuration();
            jArr[1] = keyEvent.getOccurredTime();
            strArr[0] = keyEvent.getDeviceId();
        }
    }

    @Override // ohos.agp.window.wmc.IAGPEngineAdapter
    public boolean processKeyEvent(KeyEvent keyEvent) {
        if (keyEvent == null || this.mNativeWindowPtr == 0) {
            return false;
        }
        int[] iArr = new int[9];
        long[] jArr = new long[2];
        String[] strArr = new String[1];
        getKeyboardParam(keyEvent, iArr, jArr, strArr);
        return nativeDispatchKeyboardEvent(this.mNativeWindowPtr, iArr, jArr, strArr);
    }

    @Override // ohos.agp.window.wmc.IAGPEngineAdapter
    public boolean processMouseEvent(MouseEvent mouseEvent) {
        if (this.mNativeWindowPtr == 0) {
            return false;
        }
        boolean z = (mouseEvent.getPressedButtons() & 1) != 0;
        if (mouseEvent.getActionButton() == 1 || z) {
            HiLog.debug(LABEL, "processMouseEvent of left button.", new Object[0]);
            return nativeDispatchMouseEvent(this.mNativeWindowPtr, mouseEvent);
        }
        HiLog.debug(LABEL, "processMouseEvent of other buttons.", new Object[0]);
        return false;
    }

    @Override // ohos.agp.window.wmc.IAGPEngineAdapter
    public boolean processRotationEvent(RotationEvent rotationEvent) {
        HiLog.debug(LABEL, "processMouseEvent", new Object[0]);
        long j = this.mNativeWindowPtr;
        if (j == 0) {
            return false;
        }
        return nativeDispatchRotationEvent(j, rotationEvent);
    }

    @Override // ohos.agp.window.wmc.IAGPEngineAdapter
    public void processDestroy() {
        HiLog.debug(LABEL, "processDestroy", new Object[0]);
        if (this.mNativeWindowPtr != 0) {
            if (this.mSurfaceState >= 2) {
                nativeStopRender(this.mNativeWindowPtr);
            }
            this.mSurfaceState = 0;
            long j = this.mNativeWindowPtr;
            if (j != 0) {
                nativeDestroy(j);
                this.mNativeWindowPtr = 0;
            }
            this.mInputListener = null;
            this.mContext = null;
        }
    }

    @Override // ohos.agp.window.wmc.IAGPEngineAdapter
    public void processConfigurationChanged(android.content.res.Configuration configuration) {
        ohos.app.Context context;
        HiLog.debug(LABEL, "processConfigurationChanged", new Object[0]);
        if (this.mRootView == null || configuration == null || (context = this.mContext) == null) {
            HiLog.error(LABEL, "processConfigurationChanged: conditon is fail.", new Object[0]);
            return;
        }
        ResourceManager resourceManager = context.getResourceManager();
        if (resourceManager == null) {
            HiLog.error(LABEL, "processConfigurationChanged: can not get ResourceManager.", new Object[0]);
            return;
        }
        if (this.mConfiguration == null) {
            this.mConfiguration = resourceManager.getConfiguration();
            if (this.mConfiguration == null) {
                HiLog.error(LABEL, "processConfigurationChanged: can not get mConfiguration.", new Object[0]);
                return;
            }
        }
        this.mConfiguration.fontRatio = configuration.fontScale;
        this.mRootView.informConfigurationChanged(this.mConfiguration);
    }

    public void setContentLayout(ComponentContainer componentContainer) {
        if (this.mNativeWindowPtr != 0 && componentContainer != null) {
            this.mRootView = componentContainer;
            HiLog.debug(LABEL, "setContentLayout", new Object[0]);
            nativeSetContentLayout(this.mNativeWindowPtr, this.mRootView);
        }
    }

    public void setPreContentLayout(ComponentContainer componentContainer, int i, int i2) {
        HiLog.debug(LABEL, "setPreContentLayout", new Object[0]);
        long j = this.mNativeWindowPtr;
        if (j != 0 && componentContainer != null) {
            nativePreSetContentLayout(j, componentContainer, i, i2);
        }
    }

    public void setBackgroundColor(int i, int i2, int i3) {
        long j = this.mNativeWindowPtr;
        if (j != 0) {
            nativeSetBackgroundColor(j, i, i2, i3);
        }
    }

    /* access modifiers changed from: protected */
    public void setWindowOffset(int i, int i2) {
        HiLog.debug(LABEL, "NativesetWindowOffset x=%{public}d y=%{public}d", Integer.valueOf(i), Integer.valueOf(i2));
        long j = this.mNativeWindowPtr;
        if (j != 0) {
            nativeSetWindowOffset(j, i, i2);
        }
    }

    public void setTransparent(boolean z) {
        long j = this.mNativeWindowPtr;
        if (j != 0) {
            if (z) {
                nativeSetTransparent(j, 1);
            } else {
                nativeSetTransparent(j, 0);
            }
        }
    }

    public void draw() {
        HiLog.debug(LABEL, "draw", new Object[0]);
        if (this.mSurface != null) {
            HiLog.debug(LABEL, "mSurface != null", new Object[0]);
            draw(this.mSurface, this.mSurfaceWidth, this.mSurfaceHeight);
        }
    }

    public void draw(Surface surface, int i, int i2) {
        HiLog.debug(LABEL, "draw", new Object[0]);
        if (this.mNativeWindowPtr != 0 && this.mSurface != null) {
            HiLog.debug(LABEL, "mNativeWindowPtr != 0", new Object[0]);
            nativeDraw(this.mNativeWindowPtr, surface, i, i2);
        }
    }

    public void setMultiModel(long j) {
        if (this.mNativeWindowPtr != 0 && j != 0) {
            HiLog.debug(LABEL, "MMI: setMultiModel to native.", new Object[0]);
            nativeSetMultiModel(this.mNativeWindowPtr, j);
        }
    }

    public void setInputListener(IAGPInputListener iAGPInputListener) {
        this.mInputListener = iAGPInputListener;
    }

    public void startInput() {
        IAGPInputListener iAGPInputListener = this.mInputListener;
        if (iAGPInputListener != null) {
            iAGPInputListener.onInputStart();
        }
    }

    public void notifyFocus(int i) {
        IAGPInputListener iAGPInputListener = this.mInputListener;
        if (iAGPInputListener != null) {
            iAGPInputListener.onNotifyFocus(i);
        }
    }

    public void stopInput() {
        IAGPInputListener iAGPInputListener = this.mInputListener;
        if (iAGPInputListener != null) {
            iAGPInputListener.onInputStop();
        }
    }

    public boolean getHapticFeedbackStatus() {
        return SystemSettingsHelper.getHapticFeedbackStatus(this.mContext);
    }

    public void setWindowVisibleRect(int i, int i2, int i3, int i4) {
        if (i >= i3 || i2 >= i4) {
            HiLog.error(LABEL, "setWindowVisibleRect invalid param!!", new Object[0]);
        } else {
            nativeSetWindowVisibleRect(this.mNativeWindowPtr, i, i2, i3, i4);
        }
    }

    public void notifyWindowFocusChange(boolean z) {
        nativeNotifyWindowFocusChange(this.mNativeWindowPtr, z);
    }

    /* access modifiers changed from: protected */
    public void notifyBarrierFree(boolean z) {
        HiLog.debug(LABEL, "notifyBarrierFree", new Object[0]);
        long j = this.mNativeWindowPtr;
        if (j != 0) {
            nativeNotifyBarrierFree(j, z);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0032  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0037 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getDeviceType() {
        /*
            r4 = this;
            java.lang.String r4 = "ro.build.characteristics"
            java.lang.String r0 = ""
            java.lang.String r4 = ohos.system.Parameters.get(r4, r0)
            int r0 = r4.hashCode()
            r1 = 112903375(0x6bac4cf, float:7.025461E-35)
            r2 = 0
            r3 = 1
            if (r0 == r1) goto L_0x0024
            r1 = 297574343(0x11bc9fc7, float:2.975964E-28)
            if (r0 == r1) goto L_0x001a
            goto L_0x002f
        L_0x001a:
            java.lang.String r0 = "fitnessWatch"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x002f
            r4 = r3
            goto L_0x0030
        L_0x0024:
            java.lang.String r0 = "watch"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x002f
            r4 = r2
            goto L_0x0030
        L_0x002f:
            r4 = -1
        L_0x0030:
            if (r4 == 0) goto L_0x0037
            if (r4 == r3) goto L_0x0035
            return r2
        L_0x0035:
            r4 = 2
            return r4
        L_0x0037:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.agp.window.wmc.AGPEngineAdapter.getDeviceType():int");
    }
}
