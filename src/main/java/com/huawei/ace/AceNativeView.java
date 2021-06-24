package com.huawei.ace;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.TextureLayer;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import com.huawei.ace.plugin.clipboard.ClipboardPlugin;
import com.huawei.ace.plugin.editing.TextInputPlugin;
import com.huawei.ace.plugin.texture.AceLayerTexture;
import com.huawei.ace.plugin.texture.AceLayerTexturePlugin;
import com.huawei.ace.plugin.texture.IAceTexture;
import com.huawei.ace.plugin.texture.IAceTextureLayer;
import com.huawei.ace.runtime.ALog;
import com.huawei.ace.runtime.AceResourcePlugin;
import com.huawei.ace.runtime.AceResourceRegister;
import com.huawei.ace.runtime.IAceView;
import java.nio.ByteBuffer;

public final class AceNativeView extends View implements IAceView {
    private static final long INVALID_TEXTURELAYER_HANDLE = 0;
    private static final int LOCATION_SIZE = 2;
    private static final int OFFSET_NUMS = 2;
    private static final String TAG = "AceNativeView";
    private ClipboardPlugin clipboardPlugin;
    private final Context context;
    private long nativeViewPtr = 0;
    private final AceResourceRegister resRegister;
    private TextInputPlugin textInputPlugin;
    private final int viewId;

    private native long nativeCreateViewHandle(int i);

    private native void nativeDestroyViewHandle(long j);

    private native boolean nativeDispatchKeyEvent(long j, int i, int i2, int i3, long j2, long j3);

    private native void nativeDispatchMouseEvent(long j, ByteBuffer byteBuffer, int i);

    private native boolean nativeDispatchPointerDataPacket(long j, ByteBuffer byteBuffer, int i);

    private native boolean nativeDispatchRotationEvent(long j, float f);

    private native void nativeDrawFrame(long j, long j2);

    private native int nativeGetAppBackgroundColor(long j);

    private native void nativeInitCacheFilePath(String str, String str2);

    private native long nativeInitResRegister(long j, AceResourceRegister aceResourceRegister);

    private native void nativeOnAppVisibilityChange(long j, boolean z);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native void nativeRegisterTexture(long j, long j2, long j3, Object obj);

    private native void nativeSetCallback(long j, Object obj);

    private native void nativeSetCardViewParams(long j, String str, boolean z);

    private native void nativeSetViewportMetrics(long j, float f, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native void nativeUnregisterTexture(long j, long j2);

    private native void nativeViewChanged(long j, int i, int i2, int i3);

    private native void nativeViewDestroyed(long j);

    @Override // com.huawei.ace.runtime.IAceView
    public void initDeviceType() {
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void onPause() {
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void onResume() {
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void onStart(int i) {
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void onStop() {
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void setWindowModal(int i) {
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void updateWindowBlurRegion(float[][] fArr) {
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void viewCreated() {
    }

    public AceNativeView(Context context2, int i) {
        super(context2);
        this.viewId = i;
        this.context = context2;
        Object systemService = this.context.getSystemService("window");
        if (systemService instanceof WindowManager) {
            AceVsyncWaiter.getInstance((WindowManager) systemService);
        }
        this.nativeViewPtr = nativeCreateViewHandle(this.viewId);
        nativeSetCallback(this.nativeViewPtr, this);
        this.resRegister = new AceResourceRegister();
        initResRegister();
    }

    public void initResRegister() {
        long j = this.nativeViewPtr;
        if (j != 0) {
            this.resRegister.setRegisterPtr(nativeInitResRegister(j, this.resRegister));
            AnonymousClass1 r0 = new IAceTextureLayer() {
                /* class com.huawei.ace.AceNativeView.AnonymousClass1 */

                @Override // com.huawei.ace.plugin.texture.IAceTextureLayer
                public TextureLayer createTextureLayer() {
                    if (AceNativeView.this.getThreadedRenderer() != null) {
                        return AceNativeView.this.getThreadedRenderer().createTextureLayer();
                    }
                    return null;
                }
            };
            this.resRegister.registerPlugin(AceLayerTexturePlugin.createRegister(new IAceTexture() {
                /* class com.huawei.ace.AceNativeView.AnonymousClass2 */

                @Override // com.huawei.ace.plugin.texture.IAceTexture
                public void registerTexture(long j, Object obj) {
                    long j2 = 0;
                    if (AceNativeView.this.nativeViewPtr != 0) {
                        if (obj instanceof AceLayerTexture) {
                            j2 = ((AceLayerTexture) obj).getLayerHandle();
                        }
                        AceNativeView aceNativeView = AceNativeView.this;
                        aceNativeView.nativeRegisterTexture(aceNativeView.nativeViewPtr, j, j2, obj);
                    }
                }

                @Override // com.huawei.ace.plugin.texture.IAceTexture
                public void markTextureFrameAvailable(long j) {
                    AceNativeView.this.invalidate();
                }

                @Override // com.huawei.ace.plugin.texture.IAceTexture
                public void unregisterTexture(long j) {
                    if (AceNativeView.this.nativeViewPtr != 0) {
                        AceNativeView aceNativeView = AceNativeView.this;
                        aceNativeView.nativeUnregisterTexture(aceNativeView.nativeViewPtr, j);
                    }
                }
            }, r0));
        }
    }

    public void requestInvalidate() {
        if (isWindowInScreen()) {
            invalidate();
        }
    }

    private boolean isWindowInScreen() {
        int[] iArr = new int[2];
        getLocationOnScreen(iArr);
        int i = getResources().getDisplayMetrics().widthPixels;
        int i2 = getResources().getDisplayMetrics().heightPixels;
        if (ALog.isDebuggable()) {
            ALog.d(TAG, "screenWidth = " + i + ", screenHeight = " + i2 + ", window.x = " + iArr[0] + ", window.y = " + iArr[1]);
        }
        return !(iArr[0] < 0 || iArr[0] > i) && !(iArr[1] < 0 || iArr[1] > i2);
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        nativeOnAppVisibilityChange(this.nativeViewPtr, i == 0);
    }

    public void initTextInputPlugins(int i) {
        this.clipboardPlugin = new ClipboardPlugin(this);
        this.textInputPlugin = new TextInputPlugin(this, i);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public void setViewAccessibilityParams(String str, boolean z) {
        long j = this.nativeViewPtr;
        if (j != 0) {
            nativeSetCardViewParams(j, str, z);
        }
    }

    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        TextInputPlugin textInputPlugin2 = this.textInputPlugin;
        if (textInputPlugin2 != null) {
            return textInputPlugin2.createInputConnection(this, editorInfo);
        }
        return super.onCreateInputConnection(editorInfo);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        nativeDrawFrame(this.nativeViewPtr, canvas.getNativeCanvasWrapper());
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        nativeViewChanged(this.nativeViewPtr, i, i2, getOrientation());
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        int width = getWidth();
        int height = getHeight();
        if (width != 0 && height != 0) {
            nativeViewChanged(this.nativeViewPtr, width, height, getOrientation());
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.nativeViewPtr == 0) {
            return super.onTouchEvent(motionEvent);
        }
        if (motionEvent == null) {
            return false;
        }
        ALog.i(TAG, "processTouchEventInner type = " + motionEvent.getActionMasked() + ", window.x = " + motionEvent.getX(motionEvent.getActionIndex()) + ", window.y = " + motionEvent.getY(motionEvent.getActionIndex()) + ", actionId = " + motionEvent.getActionIndex() + ", pointerId = " + motionEvent.getPointerId(motionEvent.getActionIndex()) + ", force = " + motionEvent.getPressure(motionEvent.getActionIndex()));
        ByteBuffer processTouchEvent = AceEventProcessor.processTouchEvent(motionEvent);
        nativeDispatchPointerDataPacket(this.nativeViewPtr, processTouchEvent, processTouchEvent.position());
        return true;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        long j = this.nativeViewPtr;
        if (j == 0) {
            return super.onKeyDown(i, keyEvent);
        }
        if (nativeDispatchKeyEvent(j, keyEvent.getKeyCode(), keyEvent.getAction(), keyEvent.getRepeatCount(), keyEvent.getEventTime(), keyEvent.getDownTime())) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        long j = this.nativeViewPtr;
        if (j == 0) {
            return super.onKeyUp(i, keyEvent);
        }
        if (nativeDispatchKeyEvent(j, keyEvent.getKeyCode(), keyEvent.getAction(), keyEvent.getRepeatCount(), keyEvent.getEventTime(), keyEvent.getDownTime())) {
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }

    @Override // com.huawei.ace.runtime.IAceView
    public long getNativePtr() {
        return this.nativeViewPtr;
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void releaseNativeView() {
        long j = this.nativeViewPtr;
        if (j != 0) {
            nativeDestroyViewHandle(j);
            this.nativeViewPtr = 0;
        }
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void addResourcePlugin(AceResourcePlugin aceResourcePlugin) {
        this.resRegister.registerPlugin(aceResourcePlugin);
    }

    private int getOrientation() {
        if (getResources() == null || getResources().getConfiguration() == null) {
            return 1;
        }
        return getResources().getConfiguration().orientation;
    }

    public float getShowScaleX() {
        Rect rect = new Rect();
        getBoundsOnScreen(rect);
        return ((float) rect.width()) / ((float) getWidth());
    }

    public float getShowScaleY() {
        Rect rect = new Rect();
        getBoundsOnScreen(rect);
        return ((float) rect.height()) / ((float) getHeight());
    }
}
