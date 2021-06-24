package ohos.ace.ability;

import com.huawei.ace.adapter.AceContextAdapter;
import com.huawei.ace.plugin.internal.PluginJNI;
import com.huawei.ace.runtime.AEventReport;
import com.huawei.ace.runtime.ALog;
import com.huawei.ace.runtime.AceContainer;
import com.huawei.ace.runtime.AceEnv;
import com.huawei.ace.runtime.AceEventCallback;
import com.huawei.ace.runtime.AcePage;
import com.huawei.ace.runtime.AceResourcePlugin;
import com.huawei.ace.runtime.AceResourceRegister;
import com.huawei.ace.runtime.ActionEventCallback;
import com.huawei.ace.runtime.IAceView;
import java.io.FileDescriptor;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import ohos.abilityshell.HarmonyApplication;
import ohos.ace.AceEventProcessor;
import ohos.ace.Logger;
import ohos.ace.TouchInfo;
import ohos.ace.ability.InstantComponent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentParent;
import ohos.agp.render.Canvas;
import ohos.agp.render.Picture;
import ohos.agp.window.wmc.DisplayManagerWrapper;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.InnerEvent;
import ohos.global.configuration.Configuration;
import ohos.global.i18n.utils.LocalesFallback;
import ohos.global.icu.impl.locale.LanguageTag;
import ohos.global.resource.LocaleFallBackException;
import ohos.global.resource.ResourceManager;
import ohos.multimodalinput.event.TouchEvent;

public class InstantComponent extends Component {
    private static final String LOG_TAG = "InstantComponent";
    private static final int MESSAGE_EVENT = 101;
    private static final int ROUTER_EVENT = 100;
    private static final int THEME_ID_DEFAULT = 117440515;
    private static int globalInstanceId = 1;
    private Context abilityContext;
    private ActionEventCallback actionCallbackHandler;
    private final String cardHapPath;
    private Class<?> clientClazz;
    private AceContainer container;
    private final Context context;
    private float density = 1.0f;
    private final CompletableFuture<Void> densityFuture;
    private EventHandler eventHandler;
    private int heightPixels = 0;
    private final int instanceId;
    private final String jsModuleName;
    private Method registerMethod;
    private Method unregisterMethod;
    private int virtualViewId;
    private int widthPixels = 0;

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native long nativeCreateViewHandle(int i);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native void nativeDestroyViewHandle(long j);

    private native boolean nativeDispatchKeyEvent(long j, int i, int i2, int i3, long j2, long j3);

    private native void nativeDispatchMouseEvent(long j, ByteBuffer byteBuffer, int i);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native boolean nativeDispatchPointerDataPacket(long j, ByteBuffer byteBuffer, int i);

    private native boolean nativeDispatchRotationEvent(long j, float f);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native void nativeDrawFrame(long j, long j2);

    private native int nativeGetAppBackgroundColor(long j);

    private native void nativeInitCacheFilePath(String str, String str2);

    private native long nativeInitResRegister(long j, AceResourceRegister aceResourceRegister);

    private native void nativeOnAppVisibilityChange(long j, boolean z);

    private native void nativeRegisterTexture(long j, long j2, long j3, Object obj);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native void nativeSetCallback(long j, Object obj);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native void nativeSetCardPositionWithAgp(long j, int i, float f, float f2);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native void nativeSetCardViewParams(long j, String str, boolean z);

    private native void nativeSetViewportMetrics(long j, float f, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14);

    private native void nativeUnregisterTexture(long j, long j2);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native void nativeViewChanged(long j, int i, int i2, int i3);

    private native void nativeViewDestroyed(long j);

    static {
        ALog.setLogger(new Logger());
        AceEnv.setContainerType(2);
        AceEnv.getInstance().setupNatives(1, 2);
    }

    public InstantComponent(Context context2, String str, String str2) {
        super(context2);
        int i = globalInstanceId;
        globalInstanceId = i + 1;
        this.instanceId = i;
        this.abilityContext = null;
        this.registerMethod = null;
        this.unregisterMethod = null;
        this.clientClazz = null;
        this.virtualViewId = -1;
        this.actionCallbackHandler = new ActionEventCallback() {
            /* class ohos.ace.ability.InstantComponent.AnonymousClass1 */

            @Override // com.huawei.ace.runtime.ActionEventCallback
            public void onRouterEvent(String str) {
                ALog.d(InstantComponent.LOG_TAG, "fire router event, start ability.");
                if (InstantComponent.this.eventHandler != null) {
                    InstantComponent.this.eventHandler.sendEvent(InnerEvent.get(100, 0, str));
                }
            }

            @Override // com.huawei.ace.runtime.ActionEventCallback
            public void onMessageEvent(String str) {
                ALog.d(InstantComponent.LOG_TAG, "fire message event.");
                if (InstantComponent.this.eventHandler != null) {
                    InstantComponent.this.eventHandler.sendEvent(InnerEvent.get(101, 0, str));
                }
            }
        };
        this.context = context2;
        this.cardHapPath = str;
        this.jsModuleName = str2;
        this.densityFuture = CompletableFuture.runAsync(new Runnable(context2) {
            /* class ohos.ace.ability.$$Lambda$InstantComponent$cxi_thdby47seKTahshbxay8VXs */
            private final /* synthetic */ Context f$1;

            {
                this.f$1 = r2;
            }

            public final void run() {
                InstantComponent.this.lambda$new$0$InstantComponent(this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$InstantComponent(Context context2) {
        Optional<DisplayManagerWrapper.DisplayWrapper> defaultDisplay = DisplayManagerWrapper.getInstance().getDefaultDisplay(context2);
        if (defaultDisplay.isPresent()) {
            DisplayManagerWrapper.DisplayMetricsWrapper displayRealMetricsWrapper = defaultDisplay.get().getDisplayRealMetricsWrapper();
            this.density = displayRealMetricsWrapper.density;
            this.widthPixels = displayRealMetricsWrapper.widthPixels;
            this.heightPixels = displayRealMetricsWrapper.heightPixels;
            return;
        }
        ALog.e(LOG_TAG, "fail to find density info");
    }

    public void setEventHandler(EventHandler eventHandler2) {
        this.eventHandler = eventHandler2;
    }

    public void render(String str) {
        render(str, 0, 0, null, null, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0060  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void render(java.lang.String r7, int r8, int r9, java.lang.String[] r10, int[] r11, java.io.FileDescriptor[] r12) {
        /*
        // Method dump skipped, instructions count: 114
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.ace.ability.InstantComponent.render(java.lang.String, int, int, java.lang.String[], int[], java.io.FileDescriptor[]):void");
    }

    public void destroy() {
        Class<?> cls;
        if (this.container == null) {
            ALog.d(LOG_TAG, "Not rendering");
            return;
        }
        Method method = this.unregisterMethod;
        if (!(method == null || (cls = this.clientClazz) == null)) {
            try {
                method.invoke(cls, this.abilityContext, Integer.valueOf(this.virtualViewId));
            } catch (ReflectiveOperationException e) {
                ALog.e(LOG_TAG, "unRegisterBarrierFreeView fail to invoke method " + e.getMessage());
            }
        }
        this.container.destroyContainer();
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Component
    public void onAttributeConfigChanged(Configuration configuration) {
        if (this.container == null) {
            ALog.e(LOG_TAG, "container is null");
            return;
        }
        int colorMode = this.context.getColorMode();
        if (configuration != null) {
            colorMode = configuration.getSystemColorMode();
        }
        this.container.setColorMode(colorMode == 0 ? 1 : 0);
        this.container.updateFontWeightScale();
    }

    private void createContainer(String str, int i, int i2, String[] strArr, int[] iArr, FileDescriptor[] fileDescriptorArr) {
        this.container = AceEnv.createContainer(new AceEventCallback() {
            /* class ohos.ace.ability.InstantComponent.AnonymousClass3 */

            @Override // com.huawei.ace.runtime.AceEventCallback
            public String onEvent(int i, String str, String str2) {
                return "";
            }

            @Override // com.huawei.ace.runtime.AceEventCallback
            public void onFinish() {
            }

            @Override // com.huawei.ace.runtime.AceEventCallback
            public void onStatusBarBgColorChanged(int i) {
            }
        }, new PluginJNI(), this.instanceId, strArr, iArr, fileDescriptorArr);
        AceContainer aceContainer = this.container;
        if (aceContainer != null) {
            aceContainer.setActionEventCallback(this.actionCallbackHandler);
            this.container.setMultimodalObject(0);
            this.container.addCustomAssetPath(this.cardHapPath, new String[]{"assets/js/" + this.jsModuleName + "/", "assets/js/share/"});
            initTheme();
            AceContainer aceContainer2 = this.container;
            AcePage createPage = aceContainer2.createPage();
            if (str == null) {
                str = "";
            }
            aceContainer2.setPageContent(createPage, "", str);
            try {
                this.densityFuture.get();
            } catch (InterruptedException | CancellationException | ExecutionException unused) {
                ALog.e(LOG_TAG, "Failed to get density");
            }
            this.container.getView(this.density, i, i2).viewCreated();
        }
    }

    private void initTheme() {
        Configuration configuration;
        int colorMode = this.context.getColorMode();
        ResourceManager resourceManager = this.context.getResourceManager();
        if (!(colorMode != -1 || resourceManager == null || (configuration = resourceManager.getConfiguration()) == null)) {
            colorMode = configuration.getSystemColorMode();
        }
        this.container.setColorMode(colorMode == 0 ? 1 : 0);
        this.container.initResourceManager(null, this.cardHapPath, this.context.getThemeId());
    }

    /* access modifiers changed from: private */
    public String onLocaleFallback(String str, String[] strArr) {
        ArrayList arrayList = new ArrayList(strArr.length);
        Collections.addAll(arrayList, strArr);
        StringBuilder sb = new StringBuilder();
        try {
            ArrayList<String> findValidAndSort = LocalesFallback.getInstance().findValidAndSort(str, arrayList);
            if (findValidAndSort.size() > 0) {
                for (String str2 : findValidAndSort) {
                    sb.append(str2);
                    sb.append(",");
                }
            }
        } catch (LocaleFallBackException unused) {
            ALog.e(LOG_TAG, "findValidAndSort failed");
        }
        sb.append("en-US");
        return sb.toString();
    }

    public void updateInstantData(String str, String[] strArr, int[] iArr, FileDescriptor[] fileDescriptorArr) {
        AceContainer aceContainer = this.container;
        if (aceContainer == null) {
            ALog.w(LOG_TAG, "container is null");
        } else {
            aceContainer.updateInstantData(str, strArr, iArr, fileDescriptorArr);
        }
    }

    /* access modifiers changed from: private */
    public class AceViewDelegate implements IAceView, Component.AccessibilityFocusListener, Component.TouchEventListener {
        private static final int ACCESSIBILITY_TYPE_ACE = 1;
        private static final int OFFSET_NUMS = 2;
        private int height = 0;
        private long nativeViewPtr = 0;
        private int orientation = 0;
        private Picture picture = null;
        private int width = 0;

        @Override // com.huawei.ace.runtime.IAceView
        public void addResourcePlugin(AceResourcePlugin aceResourcePlugin) {
        }

        public float getShowScaleX() {
            return 1.0f;
        }

        public float getShowScaleY() {
            return 1.0f;
        }

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

        public AceViewDelegate(int i) {
            AceContextAdapter.initVsync(InstantComponent.this.context.getHostContext());
            this.nativeViewPtr = InstantComponent.this.nativeCreateViewHandle(i);
            InstantComponent.this.nativeSetCallback(this.nativeViewPtr, this);
        }

        private void registerCallbacks() {
            this.picture = new Picture();
            InstantComponent.this.addDrawTask(new Component.DrawTask() {
                /* class ohos.ace.ability.$$Lambda$InstantComponent$AceViewDelegate$BGW51FM0G0uyaEO8ptuNQQDpA54 */

                @Override // ohos.agp.components.Component.DrawTask
                public final void onDraw(Component component, Canvas canvas) {
                    InstantComponent.AceViewDelegate.this.lambda$registerCallbacks$0$InstantComponent$AceViewDelegate(component, canvas);
                }
            });
            InstantComponent.this.setLayoutRefreshedListener(new Component.LayoutRefreshedListener() {
                /* class ohos.ace.ability.$$Lambda$InstantComponent$AceViewDelegate$Ti7MYXq7Djy42rJ8lfMB8CSyNE */

                @Override // ohos.agp.components.Component.LayoutRefreshedListener
                public final void onRefreshed(Component component) {
                    InstantComponent.AceViewDelegate.this.lambda$registerCallbacks$1$InstantComponent$AceViewDelegate(component);
                }
            });
            InstantComponent.this.setBindStateChangedListener(new Component.BindStateChangedListener() {
                /* class ohos.ace.ability.InstantComponent.AceViewDelegate.AnonymousClass1 */

                @Override // ohos.agp.components.Component.BindStateChangedListener
                public void onComponentUnboundFromWindow(Component component) {
                }

                @Override // ohos.agp.components.Component.BindStateChangedListener
                public void onComponentBoundToWindow(Component component) {
                    AceViewDelegate aceViewDelegate = AceViewDelegate.this;
                    aceViewDelegate.width = InstantComponent.this.getWidth();
                    AceViewDelegate aceViewDelegate2 = AceViewDelegate.this;
                    aceViewDelegate2.height = InstantComponent.this.getHeight();
                    AceViewDelegate aceViewDelegate3 = AceViewDelegate.this;
                    aceViewDelegate3.orientation = aceViewDelegate3.getOrientation();
                    InstantComponent.this.nativeViewChanged(AceViewDelegate.this.nativeViewPtr, AceViewDelegate.this.width, AceViewDelegate.this.height, AceViewDelegate.this.orientation);
                    AceViewDelegate.this.invalidate();
                }
            });
            InstantComponent.this.setTouchEventListener(this);
            InstantComponent.this.setAccessibilityFocusListener(this);
        }

        public /* synthetic */ void lambda$registerCallbacks$0$InstantComponent$AceViewDelegate(Component component, Canvas canvas) {
            long nativeHandle = this.picture.getNativeHandle();
            if (nativeHandle != 0) {
                InstantComponent.this.nativeDrawFrame(this.nativeViewPtr, nativeHandle);
                canvas.drawPicture(this.picture);
            }
        }

        public /* synthetic */ void lambda$registerCallbacks$1$InstantComponent$AceViewDelegate(Component component) {
            int width2 = InstantComponent.this.getWidth();
            int height2 = InstantComponent.this.getHeight();
            int orientation2 = getOrientation();
            if (this.width != width2 || this.height != height2 || this.orientation != orientation2) {
                this.width = width2;
                this.height = height2;
                this.orientation = orientation2;
                InstantComponent.this.nativeViewChanged(this.nativeViewPtr, this.width, this.height, this.orientation);
            }
        }

        @Override // ohos.agp.components.Component.TouchEventListener
        public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
            if (this.nativeViewPtr == 0 || touchEvent.getAction() == 0) {
                return false;
            }
            if (ALog.isDebuggable()) {
                ALog.d(InstantComponent.LOG_TAG, "processTouchEventInner type = " + touchEvent.getAction() + ", screen.x = " + touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getX() + ", screen.y = " + touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getY() + ", window.x = " + touchEvent.getPointerPosition(touchEvent.getIndex()).getX() + ", window.y = " + touchEvent.getPointerPosition(touchEvent.getIndex()).getY() + ", actionId = " + touchEvent.getIndex() + ", pointerId = " + touchEvent.getPointerId(touchEvent.getIndex()) + ", force = " + touchEvent.getForcePrecision() + ", maxforce = " + touchEvent.getMaxForce() + ", radius = " + touchEvent.getRadius(touchEvent.getIndex()) + ", source = " + touchEvent.getSourceDevice() + ", deviceid = " + touchEvent.getInputDeviceId());
                for (int i = 0; i < InstantComponent.this.getLocationOnScreen().length; i++) {
                    ALog.d(InstantComponent.LOG_TAG, "location on screen: " + InstantComponent.this.getLocationOnScreen()[i]);
                }
            }
            float scaleX = InstantComponent.this.getScaleX();
            float scaleY = InstantComponent.this.getScaleY();
            ComponentParent componentParent = component.getComponentParent();
            Component component2 = null;
            if (componentParent instanceof Component) {
                component2 = (Component) componentParent;
            }
            while (component2 != null) {
                scaleX *= component2.getScaleX();
                scaleY *= component2.getScaleY();
                ComponentParent componentParent2 = component2.getComponentParent();
                if (!(componentParent2 instanceof Component)) {
                    break;
                }
                component2 = (Component) componentParent2;
            }
            ByteBuffer processTouchEvent = AceEventProcessor.processTouchEvent(touchEvent, new TouchInfo(0.0f, 0.0f, scaleX, scaleY));
            InstantComponent.this.nativeDispatchPointerDataPacket(this.nativeViewPtr, processTouchEvent, processTouchEvent.position());
            return true;
        }

        @Override // ohos.agp.components.Component.AccessibilityFocusListener
        public void onAccessibilityFocus(Component component, int i, boolean z) {
            int i2;
            int i3;
            ALog.d(InstantComponent.LOG_TAG, "registerBarrierFreeView viewId = " + i + " focus = " + z);
            if (InstantComponent.this.abilityContext == null) {
                InstantComponent.this.abilityContext = HarmonyApplication.getInstance().getTopAbility();
                if (InstantComponent.this.abilityContext == null) {
                    return;
                }
            }
            InstantComponent.this.virtualViewId = i;
            try {
                if (InstantComponent.this.clientClazz == null) {
                    InstantComponent.this.clientClazz = Class.forName("ohos.accessibility.BarrierFreeInnerClient");
                    InstantComponent.this.registerMethod = InstantComponent.this.clientClazz.getMethod("registerBarrierFreeView", Context.class, Integer.TYPE, Integer.TYPE);
                    InstantComponent.this.unregisterMethod = InstantComponent.this.clientClazz.getMethod("unRegisterBarrierFreeView", Context.class, Integer.TYPE);
                }
                if (z) {
                    InstantComponent.this.registerMethod.invoke(InstantComponent.this.clientClazz, InstantComponent.this.abilityContext, Integer.valueOf(i), 1);
                }
            } catch (ReflectiveOperationException e) {
                InstantComponent.this.clientClazz = null;
                ALog.e(InstantComponent.LOG_TAG, "Failed to invoke method of barrier free" + e.getMessage());
            }
            if (z) {
                int[] windowOffset = AceContextAdapter.getWindowOffset(InstantComponent.this.abilityContext.getHostContext());
                if (windowOffset.length == 2) {
                    i3 = windowOffset[0];
                    i2 = windowOffset[1];
                } else {
                    i2 = 0;
                    i3 = 0;
                }
                int[] locationOnScreen = InstantComponent.this.getLocationOnScreen();
                if (locationOnScreen.length == 2) {
                    InstantComponent.this.nativeSetCardPositionWithAgp(this.nativeViewPtr, i, (float) (locationOnScreen[0] - i3), (float) (locationOnScreen[1] - i2));
                }
                String mapKey = getMapKey(InstantComponent.this.abilityContext, InstantComponent.this.virtualViewId);
                ALog.d(InstantComponent.LOG_TAG, "setViewAccessibility key " + mapKey);
                InstantComponent.this.nativeSetCardViewParams(this.nativeViewPtr, mapKey, true);
            }
        }

        @Override // com.huawei.ace.runtime.IAceView
        public long getNativePtr() {
            return this.nativeViewPtr;
        }

        @Override // com.huawei.ace.runtime.IAceView
        public void releaseNativeView() {
            long j = this.nativeViewPtr;
            if (j != 0) {
                InstantComponent.this.nativeDestroyViewHandle(j);
                this.nativeViewPtr = 0;
            }
        }

        @Override // com.huawei.ace.runtime.IAceView
        public void viewCreated() {
            InstantComponent.this.setFocusable(8);
            InstantComponent.this.setClickable(true);
            registerCallbacks();
        }

        private String getMapKey(Context context, int i) {
            if (context == null) {
                return i + "";
            }
            return context.toString() + LanguageTag.SEP + i;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private int getOrientation() {
            ResourceManager resourceManager = InstantComponent.this.getResourceManager();
            if (resourceManager == null) {
                ALog.e(InstantComponent.LOG_TAG, "getOrientation failed, the resource is null");
                AEventReport.sendAppStartException(6);
                return 0;
            }
            Configuration configuration = resourceManager.getConfiguration();
            if (configuration != null) {
                return configuration.direction;
            }
            ALog.e(InstantComponent.LOG_TAG, "getOrientation failed, the resource is null");
            AEventReport.sendAppStartException(6);
            return 0;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void invalidate() {
            InstantComponent.this.invalidate();
        }

        public void requestInvalidate() {
            if (isWindowInScreen()) {
                invalidate();
            }
        }

        private boolean isWindowInScreen() {
            int[] locationOnScreen = InstantComponent.this.getLocationOnScreen();
            if (locationOnScreen.length != 2) {
                ALog.e(InstantComponent.LOG_TAG, "fail to get location");
                return true;
            }
            if (ALog.isDebuggable()) {
                ALog.d(InstantComponent.LOG_TAG, "screenWidth = " + InstantComponent.this.widthPixels + ", screenHeight = " + InstantComponent.this.heightPixels + ", window.x = " + locationOnScreen[0] + ", window.y = " + locationOnScreen[1]);
            }
            boolean z = locationOnScreen[0] < 0 || locationOnScreen[0] > InstantComponent.this.widthPixels;
            boolean z2 = locationOnScreen[1] < 0 || locationOnScreen[1] > InstantComponent.this.heightPixels;
            if (z || z2) {
                return false;
            }
            return true;
        }
    }
}
