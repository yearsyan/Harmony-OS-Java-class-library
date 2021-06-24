package ohos.ace;

import com.huawei.ace.adapter.AceContextAdapter;
import com.huawei.ace.adapter.AceTextInputAdapter;
import com.huawei.ace.adapter.AceViewAdapter;
import com.huawei.ace.adapter.ViewportMetricsAdapter;
import com.huawei.ace.plugin.clipboard.ClipboardPlugin;
import com.huawei.ace.plugin.editing.TextInputPlugin;
import com.huawei.ace.plugin.texture.AceLayerTexture;
import com.huawei.ace.plugin.texture.AceLayerTexturePlugin;
import com.huawei.ace.plugin.texture.IAceTexture;
import com.huawei.ace.plugin.vibrator.VibratorPlugin;
import com.huawei.ace.runtime.AEventReport;
import com.huawei.ace.runtime.ALog;
import com.huawei.ace.runtime.AceResourcePlugin;
import com.huawei.ace.runtime.AceResourceRegister;
import com.huawei.ace.runtime.IAceView;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.function.Consumer;
import ohos.ace.ability.AceAbility;
import ohos.app.Context;
import ohos.global.resource.ResourceManager;
import ohos.multimodalinput.event.KeyEvent;
import ohos.multimodalinput.event.MouseEvent;
import ohos.multimodalinput.event.RotationEvent;
import ohos.multimodalinput.event.TouchEvent;

public final class AceNativeView extends AceViewAdapter implements IAceView {
    private static final int DEVICE_TYPE_DEFAULT = 0;
    private static final int DEVICE_TYPE_TV = 1;
    private static final long KEY_LONG_PRESS_TIME = 300;
    private static final int ORIENTATION_DEFAULT = 0;
    private static final int SEMI_TASK_BACKGROUND_COLOR = 33554431;
    private static final String TAG = "AceNativeView";
    private static final int UNSUPPORTED_ACTION_TYPE = -1;
    private static final int WINDOW_MODAL_DIALOG = 3;
    private static final int WINDOW_MODAL_NORMAL = 0;
    private static final int WINDOW_MODAL_SEMI = 1;
    private static final int WINDOW_MODAL_SEMI_FULL_SCREEN = 2;
    private ClipboardPlugin clipboardPlugin;
    private final Context context;
    private AceContextAdapter contextAdapter;
    private final IAceView.ViewportMetrics metrics;
    private long nativeViewPtr = 0;
    private final AceResourceRegister resRegister;
    private TextInputPlugin textInputPlugin;
    private VibratorPlugin vibratorPlugin;
    private final int viewId;
    private int windowModal = 0;

    private native long nativeCreateViewHandle(int i);

    private native void nativeDestroyViewHandle(long j);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native boolean nativeDispatchKeyEvent(long j, int i, int i2, int i3, long j2, long j3);

    private native void nativeDispatchMouseEvent(long j, ByteBuffer byteBuffer, int i);

    private native boolean nativeDispatchPointerDataPacket(long j, ByteBuffer byteBuffer, int i);

    private native boolean nativeDispatchRotationEvent(long j, float f);

    private native void nativeDrawFrame(long j, long j2);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
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

    public AceNativeView(Context context2, int i, float f) {
        super(context2.getHostContext());
        this.viewId = i;
        this.context = context2;
        this.nativeViewPtr = nativeCreateViewHandle(this.viewId);
        this.metrics = new IAceView.ViewportMetrics();
        this.metrics.devicePixelRatio = f;
        nativeSetCallback(this.nativeViewPtr, this);
        this.resRegister = new AceResourceRegister();
        initResRegister();
        AceDisplayManager.initRefreshRate(context2);
        if (context2 instanceof AceAbility) {
            this.contextAdapter = new AceContextAdapter(((AceAbility) context2).getHostContext());
            if (!this.contextAdapter.invalid()) {
                initCacheFilePath(this.contextAdapter);
            }
        }
    }

    public void initResRegister() {
        long j = this.nativeViewPtr;
        if (j != 0) {
            this.resRegister.setRegisterPtr(nativeInitResRegister(j, this.resRegister));
            this.resRegister.registerPlugin(AceLayerTexturePlugin.createRegister(new IAceTexture() {
                /* class ohos.ace.AceNativeView.AnonymousClass1 */

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
            }, createIAceTextureLayer()));
        }
    }

    public void initTextInputPlugins(int i) {
        this.clipboardPlugin = new ClipboardPlugin(this);
        this.textInputPlugin = new TextInputPlugin(this, i);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public void initVibratorPlugin() {
        this.vibratorPlugin = new VibratorPlugin(this);
    }

    public void initCacheFilePath(AceContextAdapter aceContextAdapter) {
        if (this.nativeViewPtr != 0) {
            if (aceContextAdapter.invalid()) {
                AEventReport.sendAppStartException(7);
                ALog.e(TAG, "Get context failed!");
                return;
            }
            File filesDir = aceContextAdapter.getContext().getFilesDir();
            if (filesDir == null) {
                AEventReport.sendAppStartException(7);
                ALog.e(TAG, "Get cache path failed!");
                return;
            }
            File file = new File(filesDir, "cache_images");
            if (!file.exists() && !file.mkdirs()) {
                AEventReport.sendAppStartException(8);
                ALog.e(TAG, "Create cache path failed!");
            }
            File file2 = new File(filesDir, "cache_files");
            if (!file2.exists() && !file2.mkdirs()) {
                AEventReport.sendAppStartException(7);
                ALog.e(TAG, "Create cache path failed!");
            }
            nativeInitCacheFilePath(file.getPath(), file2.getPath());
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.huawei.ace.adapter.AceViewAdapter
    public void processDraw(long j) {
        nativeDrawFrame(this.nativeViewPtr, j);
    }

    /* access modifiers changed from: protected */
    @Override // com.huawei.ace.adapter.AceViewAdapter
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        ResourceManager resourceManager;
        super.onSizeChanged(i, i2, i3, i4);
        Context context2 = this.context;
        nativeViewChanged(this.nativeViewPtr, i, i2, (context2 == null || (resourceManager = context2.getResourceManager()) == null) ? 0 : resourceManager.getConfiguration().direction);
    }

    /* access modifiers changed from: protected */
    @Override // com.huawei.ace.adapter.AceViewAdapter
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override // com.huawei.ace.adapter.AceViewAdapter
    public boolean processTouchEvent(TouchEvent touchEvent) {
        if (this.nativeViewPtr == 0 || touchEvent == null || touchEvent.getAction() == 0) {
            return false;
        }
        if (!(touchEvent.getPointerScreenPosition(touchEvent.getIndex()) == null || touchEvent.getPointerPosition(touchEvent.getIndex()) == null)) {
            ALog.i(TAG, "processTouchEventInner type = " + touchEvent.getAction() + ", screen.x = " + touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getX() + ", screen.y = " + touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getY() + ", window.x = " + touchEvent.getPointerPosition(touchEvent.getIndex()).getX() + ", window.y = " + touchEvent.getPointerPosition(touchEvent.getIndex()).getY() + ", actionId = " + touchEvent.getIndex() + ", pointerId = " + touchEvent.getPointerId(touchEvent.getIndex()) + ", force = " + touchEvent.getForcePrecision() + ", maxforce = " + touchEvent.getMaxForce() + ", radius = " + touchEvent.getRadius(touchEvent.getIndex()) + ", source = " + touchEvent.getSourceDevice() + ", deviceid = " + touchEvent.getInputDeviceId());
        }
        if (AceEventProcessor.actionToActionType(touchEvent.getAction()) == -1) {
            return false;
        }
        ByteBuffer processTouchEvent = AceEventProcessor.processTouchEvent(touchEvent, new TouchInfo((float) this.offsetX, (float) this.offsetY, 1.0f, 1.0f));
        return nativeDispatchPointerDataPacket(this.nativeViewPtr, processTouchEvent, processTouchEvent.position());
    }

    @Override // com.huawei.ace.adapter.AceViewAdapter
    public boolean processKeyEvent(KeyEvent keyEvent) {
        int keyCode;
        if (this.nativeViewPtr == 0 || (keyCode = keyEvent.getKeyCode()) <= -1 || keyCode > KeyEvent.getMaxKeyCode()) {
            return false;
        }
        int keyToKeyCode = AceEventProcessor.keyToKeyCode(keyCode);
        long occurredTime = keyEvent.getOccurredTime();
        return nativeDispatchKeyEvent(this.nativeViewPtr, keyToKeyCode, AceEventProcessor.keyActionToActionType(keyEvent.isKeyDown()), (int) (keyEvent.getKeyDownDuration() / KEY_LONG_PRESS_TIME), occurredTime, occurredTime - keyEvent.getKeyDownDuration());
    }

    @Override // com.huawei.ace.adapter.AceViewAdapter
    public boolean processMouseEventInner(MouseEvent mouseEvent) {
        if (this.nativeViewPtr == 0) {
            return false;
        }
        ByteBuffer processMouseEvent = AceEventProcessor.processMouseEvent(mouseEvent);
        nativeDispatchMouseEvent(this.nativeViewPtr, processMouseEvent, processMouseEvent.position());
        return false;
    }

    @Override // com.huawei.ace.adapter.AceViewAdapter
    public void createInputConnection(AceTextInputAdapter aceTextInputAdapter) {
        TextInputPlugin textInputPlugin2 = this.textInputPlugin;
        if (textInputPlugin2 != null) {
            aceTextInputAdapter.setInputConnection(textInputPlugin2.createInputConnection(this, aceTextInputAdapter.getEditorInfo()));
        }
    }

    public boolean processRotationEventInner(RotationEvent rotationEvent) {
        long j = this.nativeViewPtr;
        if (j == 0) {
            return false;
        }
        return nativeDispatchRotationEvent(j, rotationEvent.getRotationValue());
    }

    private void updateViewportMetrics() {
        long j = this.nativeViewPtr;
        if (j != 0) {
            nativeSetViewportMetrics(j, this.metrics.devicePixelRatio, this.metrics.physicalWidth, this.metrics.physicalHeight, this.metrics.physicalPaddingTop, this.metrics.physicalPaddingRight, this.metrics.physicalPaddingBottom, this.metrics.physicalPaddingLeft, this.metrics.physicalViewInsetTop, this.metrics.physicalViewInsetRight, this.metrics.physicalViewInsetBottom, this.metrics.physicalViewInsetLeft, this.metrics.systemGestureInsetTop, this.metrics.systemGestureInsetRight, this.metrics.systemGestureInsetBottom, this.metrics.systemGestureInsetLeft);
        }
    }

    @Override // com.huawei.ace.runtime.IAceView
    public long getNativePtr() {
        return this.nativeViewPtr;
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void releaseNativeView() {
        AceContextAdapter aceContextAdapter;
        long j = this.nativeViewPtr;
        if (j != 0) {
            nativeDestroyViewHandle(j);
            this.nativeViewPtr = 0;
        }
        int i = this.windowModal;
        if ((i == 1 || i == 3) && (aceContextAdapter = this.contextAdapter) != null) {
            aceContextAdapter.clearHomeKeyPressedListener();
        }
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void addResourcePlugin(AceResourcePlugin aceResourcePlugin) {
        this.resRegister.registerPlugin(aceResourcePlugin);
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void setWindowModal(int i) {
        this.windowModal = i;
        if (i == 1 || i == 3) {
            setTaskBackgroundColor(SEMI_TASK_BACKGROUND_COLOR);
        }
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void viewCreated() {
        this.contextAdapter.setOnApplyWindowInsetsListener(new ViewportMetricsAdapter(), new Consumer() {
            /* class ohos.ace.$$Lambda$AceNativeView$HJ5HrTarW8zfpnAnwHnO7aClrPk */

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AceNativeView.this.lambda$viewCreated$0$AceNativeView((ViewportMetricsAdapter) obj);
            }
        });
        int i = this.windowModal;
        if (i == 1 || i == 3) {
            this.contextAdapter.setOnHomeKeyPressedListener(new AceContextAdapter.HomeKeyPressedCallback() {
                /* class ohos.ace.AceNativeView.AnonymousClass2 */

                @Override // com.huawei.ace.adapter.AceContextAdapter.HomeKeyPressedCallback
                public void onHomeKeyPressed() {
                    long currentTimeMillis = System.currentTimeMillis();
                    int keyActionToActionType = AceEventProcessor.keyActionToActionType(false);
                    int keyToKeyCode = AceEventProcessor.keyToKeyCode(1);
                    AceNativeView aceNativeView = AceNativeView.this;
                    aceNativeView.nativeDispatchKeyEvent(aceNativeView.nativeViewPtr, keyToKeyCode, keyActionToActionType, 0, currentTimeMillis, currentTimeMillis);
                    if (AceNativeView.this.windowModal == 1 || AceNativeView.this.windowModal == 3) {
                        AceNativeView aceNativeView2 = AceNativeView.this;
                        aceNativeView2.setBackgroundColor(aceNativeView2.nativeGetAppBackgroundColor(aceNativeView2.nativeViewPtr));
                    }
                }
            });
        }
    }

    public /* synthetic */ void lambda$viewCreated$0$AceNativeView(ViewportMetricsAdapter viewportMetricsAdapter) {
        this.metrics.physicalPaddingTop = viewportMetricsAdapter.physicalPaddingTop;
        this.metrics.physicalViewInsetBottom = viewportMetricsAdapter.physicalViewInsetBottom;
        updateViewportMetrics();
    }

    public void requestInvalidate() {
        invalidate();
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void onPause() {
        this.resRegister.onActivityPause();
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void onResume() {
        this.resRegister.onActivityResume();
        this.aceWindowBlurAdapter.onActivityResume();
        int i = this.windowModal;
        if (i == 1 || i == 3) {
            setTaskBackgroundColor(SEMI_TASK_BACKGROUND_COLOR);
        }
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void onStart(int i) {
        int i2 = this.windowModal;
        if (i2 == 1 || i2 == 3 || i2 == 2) {
            initExcludeFromRecents(i);
        }
    }

    @Override // com.huawei.ace.runtime.IAceView
    public void onStop() {
        int i = this.windowModal;
        if (i == 1 || i == 3 || i == 2) {
            excludeFromRecents(true);
        }
    }

    @Override // com.huawei.ace.runtime.IAceView, com.huawei.ace.adapter.AceViewAdapter
    public void updateWindowBlurRegion(float[][] fArr) {
        super.updateWindowBlurRegion(fArr);
    }
}
