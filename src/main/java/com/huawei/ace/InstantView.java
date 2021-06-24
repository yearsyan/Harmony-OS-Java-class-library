package com.huawei.ace;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.UserHandle;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import com.huawei.ace.plugin.internal.PluginJNI;
import com.huawei.ace.runtime.ALog;
import com.huawei.ace.runtime.AceApplicationInfo;
import com.huawei.ace.runtime.AceContainer;
import com.huawei.ace.runtime.AceEnv;
import com.huawei.ace.runtime.AceEventCallback;
import com.huawei.ace.runtime.AcePage;
import com.huawei.ace.runtime.ActionEventCallback;
import com.huawei.ace.runtime.IAceView;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Method;

public final class InstantView extends ViewGroup {
    private static final int ACCESSIBILITY_TYPE_ACE = 1;
    private static final int DARK_BACKGROUND = -16777216;
    private static final int LIGHT_BACKGROUND = -1;
    private static final String LOG_TAG = "InstantView";
    private static final ViewGroup.LayoutParams MATCH_PARENT = new ViewGroup.LayoutParams(-1, -1);
    private static final int MESSAGE_EVENT = 101;
    private static final int ROUTER_EVENT = 100;
    private static final int THEME_ID_DEFAULT = 117440515;
    private static int globalInstanceId = 1;
    private AceNativeView aceNativeView = null;
    private ActionEventCallback actionCallbackHandler = new ActionEventCallback() {
        /* class com.huawei.ace.InstantView.AnonymousClass1 */

        @Override // com.huawei.ace.runtime.ActionEventCallback
        public void onRouterEvent(String str) {
            ALog.i(InstantView.LOG_TAG, "fire router event, params.");
            if (InstantView.this.eventHandler != null) {
                Message obtain = Message.obtain();
                obtain.what = 100;
                obtain.obj = str;
                InstantView.this.eventHandler.sendMessage(obtain);
                return;
            }
            ALog.e(InstantView.LOG_TAG, "fail to fire router event due to handler is null");
        }

        @Override // com.huawei.ace.runtime.ActionEventCallback
        public void onMessageEvent(String str) {
            ALog.i(InstantView.LOG_TAG, "fire message event, params.");
            if (InstantView.this.eventHandler != null) {
                Message obtain = Message.obtain();
                obtain.what = 101;
                obtain.obj = str;
                InstantView.this.eventHandler.sendMessage(obtain);
                return;
            }
            ALog.e(InstantView.LOG_TAG, "fail to fire message event due to handler is null");
        }
    };
    private final String cardHapPath;
    private AceContainer container;
    private final Context context;
    private float density = 1.0f;
    private Handler eventHandler;
    private final int instanceId;
    private final String jsModuleName;
    private String keyId = "";
    private View nativeView;
    private Method registerMethod = null;
    private Class<?> registerThreadClazz = null;

    static {
        ALog.setLogger(new Logger());
        AceEnv.setContainerType(2);
        AceEnv.getInstance().setupNatives(0, 1);
    }

    public InstantView(Context context2, String str, String str2) {
        super(context2);
        int i = globalInstanceId;
        globalInstanceId = i + 1;
        this.instanceId = i;
        this.context = context2;
        this.cardHapPath = str;
        this.jsModuleName = str2;
        Resources resources = getResources();
        if (resources != null) {
            this.density = resources.getDisplayMetrics().density;
        } else {
            ALog.e(LOG_TAG, "fail to get the density");
        }
        setFocusable(true);
    }

    public void setEventHandler(Handler handler) {
        this.eventHandler = handler;
    }

    public void render(String str) {
        render(str, 0, 0, null, null, null);
    }

    public void render(String str, int i, int i2, String[] strArr, int[] iArr, FileDescriptor[] fileDescriptorArr) {
        if (this.container != null) {
            ALog.w(LOG_TAG, "Already rendering");
            return;
        }
        ALog.i(LOG_TAG, "Render Js Card");
        boolean z = true;
        AceEnv.setViewCreator(new AceViewCreator(this.context, 1) {
            /* class com.huawei.ace.InstantView.AnonymousClass2 */

            @Override // com.huawei.ace.runtime.IAceViewCreator, com.huawei.ace.AceViewCreator
            public IAceView createView(int i, float f) {
                return new AceNativeView(InstantView.this.context, i);
            }
        });
        AceEnv.getInstance().setupFirstFrameHandler(0);
        if (this.context.getApplicationContext().getApplicationInfo() == null || (this.context.getApplicationContext().getApplicationInfo().flags & 2) == 0) {
            z = false;
        }
        AceApplicationInfo.getInstance().setPackageInfo(this.context.getPackageName(), getUid(this.context), z, false);
        AceApplicationInfo.getInstance().setPid(Process.myPid());
        AceApplicationInfo.getInstance().setSyncLocale();
        AceApplicationInfo.getInstance().setUserId(UserHandle.myUserId());
        createContainer(this.cardHapPath, str, i, i2, strArr, iArr, fileDescriptorArr);
    }

    public void refresh() {
        if (this.container == null) {
            ALog.e(LOG_TAG, "refresh: Not rendering");
        }
    }

    public void destroy() {
        if (this.container == null) {
            ALog.e(LOG_TAG, "destroy: Not rendering");
            return;
        }
        try {
            Class<?> cls = Class.forName("ohos.accessibility.BarrierFreeInnerClient");
            Method method = cls.getMethod("unRegisterBarrierFreeObject", ViewGroup.class);
            if (method != null) {
                method.invoke(cls, this);
                this.keyId = "";
            } else {
                ALog.e(LOG_TAG, "unRegisterMethod is null or unRegisterThreadClazz is null");
            }
        } catch (ReflectiveOperationException e) {
            ALog.e(LOG_TAG, "unRegisterBarrierFreeObject fail to invoke method " + e.getMessage());
        }
        this.container.destroyContainer();
    }

    private void createContainer(String str, String str2, int i, int i2, String[] strArr, int[] iArr, FileDescriptor[] fileDescriptorArr) {
        this.container = AceEnv.createContainer(new AceEventCallback() {
            /* class com.huawei.ace.InstantView.AnonymousClass3 */

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
            initDeviceInfo();
            this.container.addCustomAssetPath(str, new String[]{"assets/js/" + this.jsModuleName + "/", "assets/js/share/"});
            initTheme(this.container);
            AceContainer aceContainer2 = this.container;
            AcePage createPage = aceContainer2.createPage();
            if (str2 == null) {
                str2 = "";
            }
            aceContainer2.setPageContent(createPage, "", str2);
            IAceView view = this.container.getView(this.density, i, i2);
            if (view instanceof View) {
                this.nativeView = (View) view;
                addView(this.nativeView, MATCH_PARENT);
            }
            view.viewCreated();
        }
    }

    private void initTheme(AceContainer aceContainer) {
        Configuration configuration;
        Context context2 = this.context;
        if (context2 != null) {
            Resources resources = context2.getResources();
            int i = 0;
            if (!(resources == null || (configuration = resources.getConfiguration()) == null || (configuration.uiMode & 48) != 32)) {
                i = 1;
            }
            aceContainer.setColorMode(i);
            aceContainer.initResourceManager(this.cardHapPath, 117440515);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (z) {
            this.nativeView.layout(0, 0, i3 - i, i4 - i2);
        }
    }

    public static void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AceEnv.dump(str, fileDescriptor, printWriter, strArr);
    }

    public void updateInstantData(String str, String[] strArr, int[] iArr, FileDescriptor[] fileDescriptorArr) {
        AceContainer aceContainer = this.container;
        if (aceContainer == null) {
            ALog.e(LOG_TAG, "container is null");
        } else {
            aceContainer.updateInstantData(str, strArr, iArr, fileDescriptorArr);
        }
    }

    public void updateInstantData(String str) {
        AceContainer aceContainer = this.container;
        if (aceContainer == null) {
            ALog.e(LOG_TAG, "container is null");
        } else {
            aceContainer.updateInstantData(str, null, null, null);
        }
    }

    private String getMapKey(ViewGroup viewGroup) {
        return viewGroup == null ? "" : viewGroup.toString();
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int eventType = accessibilityEvent.getEventType();
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        if (eventType == 32768 && this.nativeView != null) {
            if (this.keyId.equals(toString())) {
                ALog.i(LOG_TAG, "current key " + this.keyId + " is already register in BarrierFreeInnerClient");
                return;
            }
            try {
                if (this.registerMethod == null) {
                    this.registerThreadClazz = Class.forName("ohos.accessibility.BarrierFreeInnerClient");
                    this.registerMethod = this.registerThreadClazz.getMethod("registerBarrierFreeObject", ViewGroup.class, Integer.TYPE);
                }
                if (!(this.registerMethod == null || this.registerThreadClazz == null)) {
                    this.registerMethod.invoke(this.registerThreadClazz, this, 1);
                }
            } catch (ReflectiveOperationException e) {
                ALog.e(LOG_TAG, "registerBarrierFreeObject fail to invoke method " + e.getMessage());
            }
            if (this.aceNativeView == null) {
                View view = this.nativeView;
                if (view instanceof AceNativeView) {
                    this.aceNativeView = (AceNativeView) view;
                }
                if (this.aceNativeView == null) {
                    return;
                }
            }
            this.keyId = getMapKey(this);
            ALog.d(LOG_TAG, "setViewAccessibility key " + this.keyId);
            this.aceNativeView.setViewAccessibilityParams(this.keyId, true);
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        if (this.container == null) {
            ALog.e(LOG_TAG, "container is null");
            return;
        }
        int i = 0;
        if (configuration != null && (configuration.uiMode & 48) == 32) {
            i = 1;
        }
        this.container.setColorMode(i);
        this.container.updateFontWeightScale();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            if (this.registerMethod == null) {
                this.registerThreadClazz = Class.forName("ohos.accessibility.BarrierFreeInnerClient");
                this.registerMethod = this.registerThreadClazz.getMethod("registerBarrierFreeObject", ViewGroup.class, Integer.TYPE);
            }
            if (this.registerMethod == null || this.registerThreadClazz == null) {
                ALog.e(LOG_TAG, "registerMethod is null or registerThreadClazz is null");
            } else {
                this.registerMethod.invoke(this.registerThreadClazz, this, 1);
            }
        } catch (ReflectiveOperationException e) {
            ALog.e(LOG_TAG, "registerBarrierFreeObject fail to invoke method " + e.getMessage());
        }
        if (this.aceNativeView == null) {
            View view = this.nativeView;
            if (view instanceof AceNativeView) {
                this.aceNativeView = (AceNativeView) view;
            }
            if (this.aceNativeView == null) {
                ALog.e(LOG_TAG, "fail to cast to aceNativeView");
                return;
            }
        }
        this.keyId = getMapKey(this);
        this.aceNativeView.setViewAccessibilityParams(this.keyId, true);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            Class<?> cls = Class.forName("ohos.accessibility.BarrierFreeInnerClient");
            Method method = cls.getMethod("unRegisterBarrierFreeObject", ViewGroup.class);
            if (method != null) {
                method.invoke(cls, this);
                this.keyId = "";
                return;
            }
            ALog.e(LOG_TAG, "unRegisterMethod is null or unRegisterThreadClazz is null");
        } catch (ReflectiveOperationException e) {
            ALog.e(LOG_TAG, "unRegisterBarrierFreeObject fail to invoke method " + e.getMessage());
        }
    }

    private int getUid(Context context2) {
        try {
            PackageManager packageManager = context2.getPackageManager();
            if (packageManager == null) {
                return 0;
            }
            return packageManager.getApplicationInfo(context2.getPackageName(), 128).uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void initDeviceInfo() {
        Resources resources = getResources();
        if (resources != null) {
            boolean isScreenRound = resources.getConfiguration().isScreenRound();
            int i = resources.getConfiguration().orientation;
            this.container.initDeviceInfo(resources.getDisplayMetrics().widthPixels, resources.getDisplayMetrics().heightPixels, i, resources.getDisplayMetrics().density, isScreenRound);
        }
    }
}
