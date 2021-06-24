package ohos.aafwk.ability;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ohos.aafwk.ability.AbilityForm;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.AbilitySliceLifecycleExecutor;
import ohos.aafwk.ability.FormException;
import ohos.aafwk.ability.FormManager;
import ohos.aafwk.ability.IAbilityFormProvider;
import ohos.aafwk.ability.Lifecycle;
import ohos.aafwk.ability.continuation.IContinuationRegisterManager;
import ohos.aafwk.ability.startsetting.AbilityStartSetting;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.aafwk.utils.log.Log;
import ohos.aafwk.utils.log.LogLabel;
import ohos.agp.agpanimator.AnimatorOption;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ComponentParent;
import ohos.agp.components.ComponentProvider;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.LayoutScatterException;
import ohos.agp.window.service.Window;
import ohos.agp.window.service.WindowManager;
import ohos.app.AbilityContext;
import ohos.app.Context;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.bundle.AbilityInfo;
import ohos.bundle.BundleManager;
import ohos.bundle.ElementName;
import ohos.bundle.FormInfo;
import ohos.bundle.IBundleManager;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.global.resource.ResourceManager;
import ohos.miscservices.timeutility.Time;
import ohos.multimodalinput.event.KeyEvent;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.tools.C0000Bytrace;
import ohos.utils.Pair;
import ohos.utils.fastjson.JSONObject;

public class AbilitySlice extends AbilityContext implements ILifecycle {
    private static final String ABILITY_NAME_KEY = "abilityName";
    private static final String BUNDLE_NAME_KEY = "bundleName";
    private static final int CODE_ACQUIRE_FORM = 0;
    private static final int CODE_REACQUIRE_FORM = 2;
    private static final int CODE_UPDATE_FORM = 1;
    private static final String CONTINUE_ABILITY_FAILED = "continue ability failed.";
    private static final int DELETE_FORM = 3;
    private static final int DISABLE_FORM_UPDATE = 6;
    private static final int ENABLE_FORM_UPDATE = 5;
    private static final int FORM_ORIENTATION_LANDSCAPE = 2;
    private static final int FORM_ORIENTATION_PORTRAIT = 1;
    private static final LogLabel LABEL = LogLabel.create();
    private static final int MAX_VISIBLE_NOTIFY_LIST = 32;
    private static final int MESSAGE_EVENT = 101;
    private static final String NOT_HARMONYOS_COMPONENT_KEY = "notHarmonyOsComponent";
    private static final String NOT_OHOS_COMPONENT_KEY = "notOHOSComponent";
    private static final String PARAMS_KEY = "params";
    public static final String PARAM_FORM_CUSTOMIZE_KEY = "ohos.extra.param.key.form_customize";
    public static final String PARAM_FORM_DIMENSION_KEY = "ohos.extra.param.key.form_dimension";
    public static final String PARAM_FORM_HEIGHT_KEY = "ohos.extra.param.key.form_height";
    public static final String PARAM_FORM_IDENTITY_KEY = "ohos.extra.param.key.form_identity";
    @Deprecated
    public static final String PARAM_FORM_ID_KEY = "ohos.extra.param.key.form_id";
    public static final String PARAM_FORM_NAME_KEY = "ohos.extra.param.key.form_name";
    private static final String PARAM_FORM_ORIENTATION_KEY = "ohos.extra.param.key.form_orientation";
    public static final String PARAM_FORM_TEMPORARY_KEY = "ohos.extra.param.key.form_temporary";
    public static final String PARAM_FORM_WIDTH_KEY = "ohos.extra.param.key.form_width";
    public static final String PARAM_MODULE_NAME_KEY = "ohos.extra.param.key.module_name";
    private static final long PERMISSIBLE_ROUTER_INTERVAL = 300;
    private static final int RELEASE_CACHED_FORM = 9;
    private static final int RELEASE_FORM = 8;
    private static final int REQUEST_CODE_MARK = 65535;
    private static final int ROUTER_EVENT = 100;
    private static final String SYSTEM_APP_TYPE = "system";
    private static long routerInvokeTime;
    private final Object FORM_LOCK = new Object();
    private AbilitySliceManager abilitySliceManager;
    private final Map<AbilityForm, IAbilityConnection> acquiringRecords = new HashMap();
    private final Map<Long, FormCallback> appCallbacks = new HashMap();
    private final Map<Long, Component> componentMap = new HashMap();
    private volatile AbilitySliceLifecycleExecutor.LifecycleState currentState = AbilitySliceLifecycleExecutor.LifecycleState.INITIAL;
    private final Map<Long, InstantProvider> instantProviders = new HashMap();
    private volatile boolean isInitialized = false;
    private final Map<Long, Pair<Integer, DirectionalLayout>> layouts = new HashMap();
    private Lifecycle lifecycle = new Lifecycle();
    private final List<Long> lostedByReconnectTempForms = new ArrayList();
    private FormManager.DeathCallback myDeathCallback = null;
    private Intent resultData;
    private SliceUIContent uiContent;
    private final Map<Long, Intent> userReqParam = new HashMap();

    /* access modifiers changed from: protected */
    public void onAbilityResult(int i, int i2, Intent intent) {
    }

    /* access modifiers changed from: protected */
    public void onActive() {
    }

    /* access modifiers changed from: protected */
    public void onBackground() {
    }

    /* access modifiers changed from: protected */
    public void onForeground(Intent intent) {
    }

    public boolean onHotkeyTriggered(int i, KeyEvent keyEvent) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onInactive() {
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return false;
    }

    public boolean onKeyPressAndHold(int i, KeyEvent keyEvent) {
        return false;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onOrientationChanged(AbilityInfo.DisplayOrientation displayOrientation) {
    }

    /* access modifiers changed from: protected */
    public void onResult(int i, Intent intent) {
    }

    /* access modifiers changed from: protected */
    public void onStart(Intent intent) {
    }

    /* access modifiers changed from: protected */
    public void onStop() {
    }

    /* access modifiers changed from: private */
    public class JsFormEventHandler extends EventHandler {
        private Form form;

        public JsFormEventHandler(Form form2) {
            super(EventRunner.current());
            this.form = form2;
        }

        @Override // ohos.eventhandler.EventHandler
        public void processEvent(InnerEvent innerEvent) {
            int i = innerEvent.eventId;
            if (i == 100) {
                if (!isSequentialEvent()) {
                    JSONObject jSONObject = (JSONObject) innerEvent.object;
                    if (jSONObject == null || !jSONObject.containsKey(AbilitySlice.ABILITY_NAME_KEY)) {
                        Log.error(AbilitySlice.LABEL, "param illegal, abilityName not exist", new Object[0]);
                        return;
                    }
                    boolean isOHOSComponent = isOHOSComponent(jSONObject);
                    String bundleName = getBundleName(isOHOSComponent, this.form);
                    if (isOHOSComponent || isIndependentPackage(this.form) || isSystemApp(bundleName)) {
                        String string = jSONObject.getString(AbilitySlice.ABILITY_NAME_KEY);
                        ElementName elementName = new ElementName("", bundleName, string);
                        Intent intent = new Intent();
                        intent.setElement(elementName);
                        if (jSONObject.containsKey(AbilitySlice.PARAMS_KEY)) {
                            intent.setParam(AbilitySlice.PARAMS_KEY, jSONObject.getString(AbilitySlice.PARAMS_KEY));
                        }
                        intent.setParam(AbilitySlice.PARAM_FORM_ID_KEY, (int) this.form.formId);
                        intent.setParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, this.form.formId);
                        Log.debug(AbilitySlice.LABEL, "process route event, bundleName: %{public}s, abilityName: %{public}s, formId: %{public}d", bundleName, string, Long.valueOf(this.form.formId));
                        if (!isOHOSComponent) {
                            intent.addFlags(16);
                        }
                        startAbilityFromForm(intent);
                        AbilitySlice.this.addUsageRecord(this.form.getBundleName(), this.form.getAbilityName());
                        return;
                    }
                    Log.warn(AbilitySlice.LABEL, "not system application, cannot mixture package router", new Object[0]);
                }
            } else if (i == 101) {
                Log.debug(AbilitySlice.LABEL, "process message event", new Object[0]);
                if (innerEvent.object instanceof Intent) {
                    try {
                        AbilitySlice.this.requestFormWithIntent(this.form.formId, (Intent) innerEvent.object);
                    } catch (FormException unused) {
                        Log.error(AbilitySlice.LABEL, "process request form fail", new Object[0]);
                    }
                }
            } else {
                Log.error(AbilitySlice.LABEL, "err event type: %{public}d", Integer.valueOf(i));
            }
        }

        private void startAbilityFromForm(Intent intent) {
            if (this.form.getFormAnimation() == null) {
                AbilitySlice.this.startAbility(intent);
                return;
            }
            AnimatorOption onGetAnimation = this.form.getFormAnimation().onGetAnimation();
            if (onGetAnimation != null) {
                AbilityStartSetting emptySetting = AbilityStartSetting.getEmptySetting();
                emptySetting.addAnimatorOption(onGetAnimation);
                AbilitySlice.this.startAbility(intent, emptySetting);
                return;
            }
            AbilitySlice.this.startAbility(intent);
        }

        private boolean isSequentialEvent() {
            long realActiveTime = Time.getRealActiveTime();
            if (realActiveTime <= AbilitySlice.routerInvokeTime || realActiveTime - AbilitySlice.routerInvokeTime >= AbilitySlice.PERMISSIBLE_ROUTER_INTERVAL) {
                long unused = AbilitySlice.routerInvokeTime = realActiveTime;
                return false;
            }
            Log.info(AbilitySlice.LABEL, "The time interval between current router event and the previous is less than 300ms, ignore current", new Object[0]);
            return true;
        }

        private boolean isOHOSComponent(JSONObject jSONObject) {
            if (jSONObject == null) {
                Log.error(AbilitySlice.LABEL, "json object is null when parse if is ohos", new Object[0]);
                return true;
            }
            return (jSONObject.getBooleanValue(AbilitySlice.NOT_HARMONYOS_COMPONENT_KEY) ^ true) && (jSONObject.getBooleanValue(AbilitySlice.NOT_OHOS_COMPONENT_KEY) ^ true);
        }

        private String getBundleName(boolean z, Form form2) {
            if (z) {
                return form2.getBundleName();
            }
            String relatedBundleName = form2.getRelatedBundleName();
            if (relatedBundleName == null || relatedBundleName.isEmpty()) {
                return form2.getOriginalBundleName();
            }
            return relatedBundleName;
        }

        private boolean isIndependentPackage(Form form2) {
            String relatedBundleName = form2.getRelatedBundleName();
            return relatedBundleName != null && !relatedBundleName.isEmpty();
        }

        private boolean isSystemApp(String str) {
            BundleManager instance = BundleManager.getInstance();
            if (instance == null) {
                Log.error(AbilitySlice.LABEL, "get bundleManager error", new Object[0]);
                return false;
            }
            try {
                String appType = instance.getAppType(str);
                Log.info(AbilitySlice.LABEL, "app type is: %{public}s", appType);
                return appType.equals(AbilitySlice.SYSTEM_APP_TYPE);
            } catch (RemoteException e) {
                Log.error(AbilitySlice.LABEL, "error occur when judge is system app: %{public}s", e.getMessage());
                return false;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void init(Context context, AbilitySliceManager abilitySliceManager2) {
        if (this.isInitialized) {
            Log.warn(LABEL, "AbilitySlice is already init", new Object[0]);
        } else if (context == null || abilitySliceManager2 == null) {
            Log.error(LABEL, "illegal initialization: context = %{public}s, asm = %{public}s", context, abilitySliceManager2);
            throw new IllegalArgumentException("illegal initialization: context = " + context + ", asm = " + abilitySliceManager2);
        } else {
            this.isInitialized = true;
            attachBaseContext(context);
            this.abilitySliceManager = abilitySliceManager2;
            this.uiContent = new SliceUIContent(abilitySliceManager2.getWindowProxy());
            Log.debug(LABEL, "AbilitySlice init done", new Object[0]);
        }
    }

    /* access modifiers changed from: protected */
    public void onBackPressed() {
        terminate();
    }

    public void postTask(Runnable runnable, long j) {
        getAbility().postTask(runnable, j);
    }

    /* access modifiers changed from: private */
    public class AbilityFormAcquireConnection implements IAbilityConnection {
        private AbilityForm abilityForm;
        private AbilityForm.OnAcquiredCallback acquiredCallback;
        private Intent intent;

        AbilityFormAcquireConnection(Intent intent2, AbilityForm.OnAcquiredCallback onAcquiredCallback) {
            this.acquiredCallback = onAcquiredCallback;
            this.intent = new Intent(intent2);
        }

        @Override // ohos.aafwk.ability.IAbilityConnection
        public void onAbilityConnectDone(ElementName elementName, IRemoteObject iRemoteObject, int i) {
            if (iRemoteObject != null) {
                try {
                    this.abilityForm = IAbilityFormProvider.FormProviderStub.asProxy(iRemoteObject).acquireAbilityForm();
                    if (AbilitySlice.this.getUITaskDispatcher() == null) {
                        Log.error(AbilitySlice.LABEL, "ability slice is not init, acquire ability form failed", new Object[0]);
                        return;
                    }
                    if (!(this.abilityForm == null || AbilitySlice.this.abilitySliceManager == null)) {
                        Log.info(AbilitySlice.LABEL, "set intent / shell and dispatcher for form", new Object[0]);
                        this.abilityForm.setUITaskDispatcher(AbilitySlice.this.getUITaskDispatcher());
                        this.abilityForm.setFullPageIntentElement(this.intent.getElement());
                        if (!this.abilityForm.asClient(AbilitySlice.this.abilitySliceManager.getContext())) {
                            Log.warn(AbilitySlice.LABEL, "ability form asClient failed", new Object[0]);
                            this.abilityForm = null;
                        } else {
                            synchronized (AbilitySlice.this.acquiringRecords) {
                                AbilitySlice.this.acquiringRecords.put(this.abilityForm, this);
                            }
                        }
                    }
                    AbilitySlice.this.getUITaskDispatcher().asyncDispatch(new Runnable() {
                        /* class ohos.aafwk.ability.$$Lambda$AbilitySlice$AbilityFormAcquireConnection$Da1vHuJcewOrurHdibuRRcHUtiA */

                        public final void run() {
                            AbilitySlice.AbilityFormAcquireConnection.this.lambda$onAbilityConnectDone$0$AbilitySlice$AbilityFormAcquireConnection();
                        }
                    });
                    Log.info(AbilitySlice.LABEL, "acquire ability form done", new Object[0]);
                } catch (RemoteException unused) {
                    Log.error(AbilitySlice.LABEL, "acquire ability form failed through RPC", new Object[0]);
                }
            }
        }

        public /* synthetic */ void lambda$onAbilityConnectDone$0$AbilitySlice$AbilityFormAcquireConnection() {
            this.acquiredCallback.onAcquired(this.abilityForm);
        }

        @Override // ohos.aafwk.ability.IAbilityConnection
        public void onAbilityDisconnectDone(ElementName elementName, int i) {
            if (AbilitySlice.this.getUITaskDispatcher() == null) {
                Log.error(AbilitySlice.LABEL, "ability slice is not init, no need post destroy event to user", new Object[0]);
                return;
            }
            AbilitySlice.this.getUITaskDispatcher().asyncDispatch(new Runnable() {
                /* class ohos.aafwk.ability.$$Lambda$AbilitySlice$AbilityFormAcquireConnection$Ty3rRCH5YvdgW_XkYIR5SX_Iu0 */

                public final void run() {
                    AbilitySlice.AbilityFormAcquireConnection.this.lambda$onAbilityDisconnectDone$1$AbilitySlice$AbilityFormAcquireConnection();
                }
            });
            synchronized (AbilitySlice.this.acquiringRecords) {
                AbilitySlice.this.acquiringRecords.remove(this.abilityForm);
            }
            Log.info(AbilitySlice.LABEL, "release ability form done: %{public}d", Integer.valueOf(i));
        }

        public /* synthetic */ void lambda$onAbilityDisconnectDone$1$AbilitySlice$AbilityFormAcquireConnection() {
            this.acquiredCallback.onDestroyed(this.abilityForm);
        }
    }

    /* access modifiers changed from: private */
    public static class SliceUIContent extends UIContent {
        SliceUIContent(AbilityWindow abilityWindow) {
            super(abilityWindow);
        }

        private synchronized AnimatorProperty createCurComponentAnimator(AbilitySliceAnimator abilitySliceAnimator, boolean z) {
            ComponentParent componentParent = this.curComponentContainer != null ? this.curComponentContainer.getComponentParent() : null;
            Component component = componentParent instanceof Component ? (Component) componentParent : null;
            if (component == null || !component.isBoundToWindow()) {
                return null;
            }
            if (abilitySliceAnimator == null) {
                return component.createAnimatorProperty();
            }
            if (abilitySliceAnimator.isDefaultAnimator()) {
                abilitySliceAnimator.constructDefaultAnimator((float) component.getRight());
            }
            if (z) {
                component.setContentPositionX(abilitySliceAnimator.getFromX());
                component.setContentPositionY(abilitySliceAnimator.getFromY());
            } else {
                component.setContentPositionX((abilitySliceAnimator.getToX() * 2.0f) - abilitySliceAnimator.getFromX());
                component.setContentPositionY((abilitySliceAnimator.getToY() * 2.0f) - abilitySliceAnimator.getFromY());
            }
            return component.createAnimatorProperty();
        }

        /* access modifiers changed from: package-private */
        public synchronized void componentEnterAnimator(AbilitySliceAnimator abilitySliceAnimator) {
            AnimatorProperty createCurComponentAnimator = createCurComponentAnimator(abilitySliceAnimator, true);
            if (createCurComponentAnimator != null) {
                Log.info(AbilitySlice.LABEL, "start enter animator", new Object[0]);
                abilitySliceAnimator.buildEnterAnimator(createCurComponentAnimator).start();
            }
        }

        /* access modifiers changed from: package-private */
        public synchronized void componentExitAnimator(AbilitySliceAnimator abilitySliceAnimator) {
            AnimatorProperty createCurComponentAnimator = createCurComponentAnimator(abilitySliceAnimator, false);
            if (createCurComponentAnimator != null) {
                Log.info(AbilitySlice.LABEL, "start exit animator", new Object[0]);
                abilitySliceAnimator.buildExitAnimator(createCurComponentAnimator).start();
            }
        }

        /* access modifiers changed from: package-private */
        public synchronized void stopComponentAnimator() {
            AnimatorProperty createCurComponentAnimator = createCurComponentAnimator(null, false);
            if (createCurComponentAnimator != null) {
                createCurComponentAnimator.end();
            }
        }
    }

    public String toString() {
        return "[Slice@" + Integer.toHexString(hashCode()) + ", " + getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + "]";
    }

    public Window getWindow() {
        checkInitialization("getWindow failed");
        AbilityWindow windowProxy = this.abilitySliceManager.getWindowProxy();
        if (windowProxy != null) {
            return windowProxy.getWindow();
        }
        Log.error(LABEL, "windowProxy is null, getLayoutParams failed", new Object[0]);
        return null;
    }

    public WindowManager.LayoutConfig getLayoutParams() {
        checkInitialization("getLayoutParams failed");
        AbilityWindow windowProxy = this.abilitySliceManager.getWindowProxy();
        if (windowProxy != null) {
            return windowProxy.getLayoutParams();
        }
        Log.error(LABEL, "windowProxy is null, getLayoutParams failed", new Object[0]);
        return null;
    }

    public void setLayoutParams(WindowManager.LayoutConfig layoutConfig) {
        checkInitialization("setLayoutParams failed");
        AbilityWindow windowProxy = this.abilitySliceManager.getWindowProxy();
        if (windowProxy == null) {
            Log.error(LABEL, "windowProxy is null, setLayoutParams failed", new Object[0]);
        } else {
            windowProxy.setLayoutParams(layoutConfig);
        }
    }

    public void setIsAmbientMode(boolean z) {
        checkInitialization("setIsAmbientMode failed");
        AbilityWindow windowProxy = this.abilitySliceManager.getWindowProxy();
        if (windowProxy == null) {
            Log.error(LABEL, "windowProxy is null, setIsAmbientMode failed", new Object[0]);
        } else {
            windowProxy.setIsAmbientMode(z);
        }
    }

    public IContinuationRegisterManager getContinuationRegisterManager() {
        Log.debug(LABEL, "getContinuationRegisterManager called", new Object[0]);
        checkInitialization("getContinuationRegisterManager failed");
        return this.abilitySliceManager.getContinuationRegisterManager();
    }

    public final void setUIContent(int i) {
        if (i >= 0) {
            checkInitialization("setUIContent failed");
            this.uiContent.updateUIContent(i);
            return;
        }
        Log.error(LABEL, "ui layout resource must be valid [%{public}d]", Integer.valueOf(i));
        throw new AbilitySliceRuntimeException("UI layout resource must be valid");
    }

    public void setUIContent(ComponentContainer componentContainer) {
        if (componentContainer != null) {
            checkInitialization("setUIContent with componentContainer failed");
            this.uiContent.updateUIContent(componentContainer);
            return;
        }
        Log.error(LABEL, "componentContainer must be valid", new Object[0]);
        throw new AbilitySliceRuntimeException("componentContainer must be valid");
    }

    /* access modifiers changed from: package-private */
    public final ComponentContainer getCurrentUI() {
        SliceUIContent sliceUIContent = this.uiContent;
        if (sliceUIContent != null) {
            return sliceUIContent.curComponentContainer;
        }
        Log.error(LABEL, "ui Content is not inited", new Object[0]);
        return null;
    }

    public Component findComponentById(int i) {
        checkInitialization("find component by ID failed");
        return this.uiContent.findComponentById(i);
    }

    public final void present(AbilitySlice abilitySlice, Intent intent) {
        if (abilitySlice != null) {
            checkInitialization("present failed");
            this.abilitySliceManager.present(this, abilitySlice, intent);
            return;
        }
        Log.error(LABEL, "present failed, can not present a null target", new Object[0]);
        throw new IllegalArgumentException("can not present a null target");
    }

    public final void presentForResult(AbilitySlice abilitySlice, Intent intent, int i) {
        if (abilitySlice == null) {
            Log.error(LABEL, "present failed, can not present a null target", new Object[0]);
            throw new IllegalArgumentException("can not present a null target");
        } else if (abilitySlice.equals(this)) {
            Log.error(LABEL, "present failed, can not present self for result", new Object[0]);
            throw new IllegalArgumentException("can not present self for result");
        } else if (i >= 0) {
            checkInitialization("presentForResult failed");
            this.abilitySliceManager.presentForResult(this, abilitySlice, intent, i);
        } else {
            Log.error(LABEL, "present failed, requestCode must not be negative", new Object[0]);
            throw new IllegalArgumentException("requestCode must not be negative");
        }
    }

    public final void setResult(Intent intent) {
        this.resultData = intent;
    }

    public final void terminate() {
        checkInitialization("terminate failed");
        this.abilitySliceManager.terminate(this, this.resultData);
    }

    public void startAbility(Intent intent) {
        startAbility(intent, AbilityStartSetting.getEmptySetting());
    }

    public void startAbility(Intent intent, AbilityStartSetting abilityStartSetting) {
        if (intent != null) {
            checkInitialization("startAbility failed");
            this.abilitySliceManager.startAbility(intent, abilityStartSetting);
            return;
        }
        Log.error(LABEL, "intent must be assigned for startAbility", new Object[0]);
        throw new IllegalArgumentException("intent must be assigned for startAbility");
    }

    public void startAbilityForResult(Intent intent, int i, AbilityStartSetting abilityStartSetting) {
        if (intent != null) {
            checkValidRequestCode(i);
            checkInitialization("startAbilityForResult failed");
            this.abilitySliceManager.startAbilityForResult(this, intent, i, abilityStartSetting);
            return;
        }
        Log.error(LABEL, "intent must be assigned for startAbilityForResult", new Object[0]);
        throw new IllegalArgumentException("intent must be assigned for startAbilityForResult");
    }

    public void startAbilityForResult(Intent intent, int i) {
        startAbilityForResult(intent, i, AbilityStartSetting.getEmptySetting());
    }

    public final AbilitySliceLifecycleExecutor.LifecycleState getState() {
        return this.currentState;
    }

    @Override // ohos.app.Context, ohos.app.AbilityContext
    public final boolean stopAbility(Intent intent) throws IllegalStateException, IllegalArgumentException {
        if (intent != null) {
            checkInitialization("stopAbility failed");
            return this.abilitySliceManager.stopAbility(intent);
        }
        Log.error(LABEL, "intent must be assigned for stopAbility", new Object[0]);
        throw new IllegalArgumentException("intent must be assigned for stopAbility");
    }

    @Override // ohos.app.Context, ohos.app.AbilityContext
    public void terminateAbility() {
        checkInitialization("terminateAbility failed");
        this.abilitySliceManager.terminateAbility();
    }

    @Override // ohos.app.Context, ohos.app.AbilityContext
    public final boolean connectAbility(Intent intent, IAbilityConnection iAbilityConnection) throws IllegalStateException, IllegalArgumentException {
        checkInitialization("connect ability failed.");
        return this.abilitySliceManager.connectAbility(this, intent, iAbilityConnection);
    }

    @Override // ohos.app.Context, ohos.app.AbilityContext
    public final void disconnectAbility(IAbilityConnection iAbilityConnection) throws IllegalStateException, IllegalArgumentException {
        Log.info(LABEL, "disconnect ability from slice", new Object[0]);
        if (iAbilityConnection != null) {
            checkInitialization("disconnect ability failed.");
            this.abilitySliceManager.disconnectAbility(this, iAbilityConnection);
            return;
        }
        throw new IllegalArgumentException("disconnect Ability failed. conn is null.");
    }

    public void continueAbility() throws IllegalStateException, UnsupportedOperationException {
        checkInitialization(CONTINUE_ABILITY_FAILED);
        this.abilitySliceManager.continueAbility(null);
    }

    public void continueAbility(String str) throws IllegalStateException, UnsupportedOperationException {
        checkInitialization(CONTINUE_ABILITY_FAILED);
        this.abilitySliceManager.continueAbility(str);
    }

    public void continueAbilityReversibly() throws IllegalStateException, UnsupportedOperationException {
        checkInitialization(CONTINUE_ABILITY_FAILED);
        this.abilitySliceManager.continueAbilityReversibly(null);
    }

    public void continueAbilityReversibly(String str) throws IllegalStateException, UnsupportedOperationException {
        checkInitialization(CONTINUE_ABILITY_FAILED);
        this.abilitySliceManager.continueAbilityReversibly(str);
    }

    public boolean reverseContinueAbility() throws IllegalStateException, UnsupportedOperationException {
        checkInitialization("reverse continue ability failed.");
        return this.abilitySliceManager.reverseContinueAbility();
    }

    public final ContinuationState getContinuationState() throws UnsupportedOperationException {
        checkInitialization("get continuation state failed.");
        return this.abilitySliceManager.getContinuationState();
    }

    public final String getOriginalDeviceId() throws UnsupportedOperationException {
        checkInitialization("get original device id failed.");
        return this.abilitySliceManager.getOriginalDeviceId();
    }

    @Override // ohos.app.Context, ohos.app.AbilityContext
    public void setDisplayOrientation(AbilityInfo.DisplayOrientation displayOrientation) {
        if (displayOrientation != null) {
            checkInitialization("set new orientation failed");
            if (this.abilitySliceManager.getWindowProxy() == null) {
                Log.error(LABEL, "window proxy is not initialized yet, set new orientation failed", new Object[0]);
            } else {
                super.setDisplayOrientation(displayOrientation);
            }
        } else {
            throw new IllegalArgumentException("invalid requested display orientation");
        }
    }

    public boolean acquireAbilityFormAsync(Intent intent, AbilityForm.OnAcquiredCallback onAcquiredCallback) {
        Log.info(LABEL, "acquire ability form", new Object[0]);
        if (intent == null || onAcquiredCallback == null) {
            throw new IllegalArgumentException("passing in intent and acquiredCallback must not be null");
        } else if (verifyCallingOrSelfPermission("ohos.permission.REQUIRE_FORM") != 0) {
            Log.warn(LABEL, "acquireAbilityFormAsync permission denied", new Object[0]);
            return false;
        } else {
            AbilityFormAcquireConnection abilityFormAcquireConnection = new AbilityFormAcquireConnection(intent, onAcquiredCallback);
            intent.getOperation().setFlags(32);
            boolean connectAbility = connectAbility(intent, abilityFormAcquireConnection);
            Log.info(LABEL, "acquire ability form done: %{public}b", Boolean.valueOf(connectAbility));
            return connectAbility;
        }
    }

    public boolean acquireForm(Intent intent, FormCallback formCallback) throws FormException {
        if (intent == null || formCallback == null) {
            throw new FormException(FormException.FormError.INPUT_PARAM_INVALID, "passing in intent and callback must not be null");
        } else if (FormManager.getRecoverStatus() != 2) {
            Intent intent2 = new Intent(intent);
            setDefaultIntentParam(intent2);
            if (checkIntentValid(intent2)) {
                LogLabel logLabel = LABEL;
                Log.info(logLabel, "begin to acquire form %{public}s, %{public}s, %{public}s", "bundleName is " + intent2.getElement().getBundleName(), "className is " + intent2.getElement().getAbilityName(), "formId is " + intent.getLongParam(PARAM_FORM_IDENTITY_KEY, 0));
                FormManager instance = FormManager.getInstance();
                if (instance != null) {
                    Form addForm = instance.addForm(intent2, FormHostClient.getInstance());
                    if (addForm != null) {
                        TaskDispatcher uITaskDispatcher = getUITaskDispatcher();
                        if (uITaskDispatcher != null) {
                            synchronized (FormHostClient.class) {
                                if (!FormHostClient.getInstance().containsForm(addForm.formId)) {
                                    FormHostClient.getInstance().addForm(this, addForm.formId);
                                } else {
                                    Log.error(LABEL, "handleAcquireResult, form has already acquired, do not support acquire twice", new Object[0]);
                                    throw new FormException(FormException.FormError.FORM_DUPLICATE_ADDED);
                                }
                            }
                            uITaskDispatcher.asyncDispatch(new Runnable(intent2, addForm, formCallback) {
                                /* class ohos.aafwk.ability.$$Lambda$AbilitySlice$KPz7ChdxokPnIqpTBw0iqo64s7w */
                                private final /* synthetic */ Intent f$1;
                                private final /* synthetic */ Form f$2;
                                private final /* synthetic */ AbilitySlice.FormCallback f$3;

                                {
                                    this.f$1 = r2;
                                    this.f$2 = r3;
                                    this.f$3 = r4;
                                }

                                public final void run() {
                                    AbilitySlice.this.lambda$acquireForm$0$AbilitySlice(this.f$1, this.f$2, this.f$3);
                                }
                            });
                            return true;
                        }
                        throw new FormException(FormException.FormError.INTERNAL_ERROR, "ui dispatcher is not found");
                    }
                    throw new FormException(FormException.FormError.INTERNAL_ERROR, "fms acquire form failed");
                }
                throw new FormException(FormException.FormError.FMS_RPC_ERROR);
            }
            throw new FormException(FormException.FormError.INPUT_PARAM_INVALID, "intent param is not correct");
        } else {
            throw new FormException(FormException.FormError.FORM_IN_RECOVER);
        }
    }

    private void setDefaultIntentParam(Intent intent) {
        ResourceManager resourceManager = getResourceManager();
        if (resourceManager == null || resourceManager.getConfiguration() == null || resourceManager.getConfiguration().direction != 1) {
            intent.setParam(PARAM_FORM_ORIENTATION_KEY, 1);
        } else {
            intent.setParam(PARAM_FORM_ORIENTATION_KEY, 2);
        }
        int intParam = intent.getIntParam(PARAM_FORM_ID_KEY, -1);
        if (intParam >= 0) {
            intent.setParam(PARAM_FORM_IDENTITY_KEY, (long) intParam);
            return;
        }
        if (!intent.hasParameter(PARAM_FORM_IDENTITY_KEY)) {
            intent.setParam(PARAM_FORM_IDENTITY_KEY, 0L);
        }
        if (!intent.hasParameter(PARAM_FORM_TEMPORARY_KEY)) {
            intent.setParam(PARAM_FORM_TEMPORARY_KEY, false);
        }
        if (!intent.hasParameter(PARAM_FORM_CUSTOMIZE_KEY)) {
            intent.setParam(PARAM_FORM_CUSTOMIZE_KEY, new IntentParams());
        }
    }

    private boolean checkIntentValid(Intent intent) throws FormException {
        ElementName element = intent.getElement();
        if (element == null) {
            Log.error(LABEL, "bundleName and abilityName is not set in intent", new Object[0]);
            return false;
        }
        String bundleName = element.getBundleName();
        String abilityName = element.getAbilityName();
        String stringParam = intent.getStringParam(PARAM_MODULE_NAME_KEY);
        if (isEmpty(bundleName) || isEmpty(abilityName) || isEmpty(stringParam)) {
            Log.error(LABEL, "bundleName or abilityName or moduleName is not set in intent", new Object[0]);
            return false;
        }
        long longParam = intent.getLongParam(PARAM_FORM_IDENTITY_KEY, -1);
        int i = (longParam > 0 ? 1 : (longParam == 0 ? 0 : -1));
        if (i < 0) {
            Log.error(LABEL, "form id should not be negative in intent", new Object[0]);
            return false;
        }
        synchronized (FormHostClient.class) {
            if (FormHostClient.getInstance().containsForm(longParam)) {
                Log.error(LABEL, "form has already acquired, do not support acquire twice", new Object[0]);
                throw new FormException(FormException.FormError.FORM_DUPLICATE_ADDED);
            }
        }
        if (intent.getIntParam(PARAM_FORM_DIMENSION_KEY, 1) <= 0) {
            Log.error(LABEL, "dimension should not be zero or negative in intent", new Object[0]);
            return false;
        }
        int intParam = intent.getIntParam(PARAM_FORM_WIDTH_KEY, -2);
        int intParam2 = intent.getIntParam(PARAM_FORM_HEIGHT_KEY, -2);
        if (intParam < -2 || intParam2 < -2) {
            Log.error(LABEL, "width or height is not set correctly in intent", new Object[0]);
            return false;
        } else if (!intent.getBooleanParam(PARAM_FORM_TEMPORARY_KEY, false) || i == 0) {
            return true;
        } else {
            Log.error(LABEL, "can not select form id when acquire temporary form", new Object[0]);
            return false;
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /* access modifiers changed from: private */
    /* renamed from: handleAcquireResult */
    public void lambda$acquireForm$0$AbilitySlice(Intent intent, Form form, FormCallback formCallback) {
        Pair<Integer, DirectionalLayout> pair;
        if (intent == null || form == null || formCallback == null) {
            Log.error(LABEL, "handleAcquireResult, param is invalid", new Object[0]);
            return;
        }
        synchronized (this.FORM_LOCK) {
            if (this.userReqParam.isEmpty()) {
                this.myDeathCallback = new MyDeathCallback();
                FormManager.registerDeathCallback(this.myDeathCallback);
            }
            intent.setParam(PARAM_FORM_IDENTITY_KEY, form.formId);
            this.userReqParam.put(Long.valueOf(form.formId), intent);
            this.appCallbacks.put(Long.valueOf(form.formId), formCallback);
            int intParam = intent.getIntParam(PARAM_FORM_WIDTH_KEY, -2);
            int intParam2 = intent.getIntParam(PARAM_FORM_HEIGHT_KEY, -2);
            DirectionalLayout directionalLayout = new DirectionalLayout(this);
            directionalLayout.setLayoutConfig(new ComponentContainer.LayoutConfig(intParam, intParam2));
            if (form.getInstantProvider() != null) {
                pair = new Pair<>(-1, directionalLayout);
            } else if (form.remoteComponent != null) {
                pair = new Pair<>(Integer.valueOf(form.remoteComponent.getLayoutId()), directionalLayout);
            } else {
                pair = new Pair<>(Integer.valueOf(form.previewID), directionalLayout);
            }
            this.layouts.put(Long.valueOf(form.formId), pair);
        }
        handleFormMessage(0, form);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x008a, code lost:
        if (r10 == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x008c, code lost:
        if (r6 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x008e, code lost:
        ohos.aafwk.utils.log.Log.info(ohos.aafwk.ability.AbilitySlice.LABEL, "handleMessage, call user implement of form %{public}d", java.lang.Long.valueOf(r11.formId));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x009f, code lost:
        if (r3 == null) goto L_0x00ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00a1, code lost:
        if (r4 != false) goto L_0x00ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00a3, code lost:
        r3.setClickedListener(new ohos.aafwk.ability.$$Lambda$AbilitySlice$cexAsY0d3Tn57dkTJb2uBEHFds(r9, r11));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00ab, code lost:
        r6.onAcquired(r5, r11);
        r10 = r11.getInstantProvider();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00b2, code lost:
        if (r10 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00b4, code lost:
        r10.setAbilityHandler(new ohos.aafwk.ability.AbilitySlice.JsFormEventHandler(r9, r11));
        r10.setEventHandler();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void handleFormMessage(int r10, ohos.aafwk.ability.Form r11) {
        /*
        // Method dump skipped, instructions count: 195
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.AbilitySlice.handleFormMessage(int, ohos.aafwk.ability.Form):void");
    }

    public /* synthetic */ void lambda$handleFormMessage$1$AbilitySlice(Form form, Component component) {
        startFullPage(form);
    }

    private int handleJsFormMessage(InstantProvider instantProvider, Form form, DirectionalLayout directionalLayout, Component component) {
        if (instantProvider.hasUpgrade()) {
            int upgradeJsComponent = upgradeJsComponent(form, directionalLayout, instantProvider);
            EventHandler eventHandler = null;
            InstantProvider instantProvider2 = this.instantProviders.get(Long.valueOf(form.formId));
            if (instantProvider2 != null) {
                eventHandler = instantProvider2.getAbilityHandler();
                instantProvider2.destroy();
            }
            if (eventHandler == null) {
                eventHandler = new JsFormEventHandler(form);
            }
            instantProvider.setAbilityHandler(eventHandler);
            instantProvider.setEventHandler();
            this.instantProviders.put(Long.valueOf(form.formId), instantProvider);
            return upgradeJsComponent;
        } else if (component == null) {
            return addJsPreviewComponent(form, directionalLayout, instantProvider);
        } else {
            return applyJsComponentAction(component, instantProvider);
        }
    }

    private int addPreviewComponent(Form form, DirectionalLayout directionalLayout) {
        Log.debug(LABEL, "generate component using preview ID of form %{public}d", Long.valueOf(form.formId));
        ComponentContainer previewComponents = getPreviewComponents(getContext(), form.getBundleName(), form.previewID);
        if (previewComponents == null) {
            return 1;
        }
        addComponentToLayout(previewComponents, directionalLayout);
        this.componentMap.put(Long.valueOf(form.formId), previewComponents);
        return 0;
    }

    private int addJsPreviewComponent(Form form, DirectionalLayout directionalLayout, InstantProvider instantProvider) {
        try {
            ComponentContainer.LayoutConfig layoutConfig = directionalLayout.getLayoutConfig();
            instantProvider.setFormSize(layoutConfig.width, layoutConfig.height);
            Component instantComponent = instantProvider.getInstantComponent(getContext());
            directionalLayout.addComponent(instantComponent);
            this.componentMap.put(Long.valueOf(form.formId), instantComponent);
            this.instantProviders.put(Long.valueOf(form.formId), instantProvider);
            return 0;
        } catch (InstantProviderException e) {
            Log.error(LABEL, "get instant component failed, err: %{public}s", e.getMessage());
            return 1;
        }
    }

    private int upgradeJsComponent(Form form, DirectionalLayout directionalLayout, InstantProvider instantProvider) {
        Log.debug(LABEL, "upgradeJsComponent", new Object[0]);
        if (form == null) {
            Log.error(LABEL, "form is null when upgrade", new Object[0]);
            return 1;
        }
        try {
            Component upgrade = instantProvider.upgrade(getContext());
            instantProvider.update();
            directionalLayout.removeAllComponents();
            directionalLayout.addComponent(upgrade);
            this.componentMap.put(Long.valueOf(form.formId), upgrade);
            return 0;
        } catch (InstantProviderException e) {
            Log.error(LABEL, "upgrade js component failed, err: %{public}s", e.getMessage());
            return 1;
        }
    }

    private int addCachedFormComponent(Form form, DirectionalLayout directionalLayout) {
        Log.debug(LABEL, "addCachedFormComponent, component not generated, apply one", new Object[0]);
        try {
            form.remoteComponent.inflateLayout(getContext());
            ComponentContainer allComponents = form.remoteComponent.getAllComponents();
            if (allComponents == null) {
                return 2;
            }
            form.remoteComponent.applyAction(allComponents);
            addComponentToLayout(allComponents, directionalLayout);
            this.componentMap.put(Long.valueOf(form.formId), allComponents);
            return 0;
        } catch (ComponentProvider.ComponentProviderException | LayoutScatterException unused) {
            Log.error(LABEL, "addCachedFormComponent, component provider apply failed", new Object[0]);
            return 2;
        }
    }

    private int applyComponentAction(Form form, Component component) {
        Log.debug(LABEL, "component existed, apply action, formId:%{public}d", Long.valueOf(form.formId));
        if (component instanceof ComponentContainer) {
            try {
                form.remoteComponent.applyAction((ComponentContainer) component);
            } catch (ComponentProvider.ComponentProviderException unused) {
                Log.error(LABEL, "applyComponentAction, apply action on component container failed", new Object[0]);
                return 3;
            }
        }
        return 0;
    }

    private int applyJsComponentAction(Component component, InstantProvider instantProvider) {
        instantProvider.setComponent(component);
        instantProvider.update();
        return 0;
    }

    private int addNewLayoutComponent(Form form, Component component, DirectionalLayout directionalLayout) {
        Log.debug(LABEL, "layout id changed, generate new component of from %{public}d", Long.valueOf(form.formId));
        try {
            form.remoteComponent.inflateLayout(getContext());
            ComponentContainer allComponents = form.remoteComponent.getAllComponents();
            if (allComponents == null) {
                return 2;
            }
            form.remoteComponent.applyAction(allComponents);
            directionalLayout.removeComponent(component);
            ComponentContainer.LayoutConfig layoutConfig = directionalLayout.getLayoutConfig();
            layoutConfig.width = allComponents.getLayoutConfig().width;
            layoutConfig.height = allComponents.getLayoutConfig().height;
            directionalLayout.setLayoutConfig(layoutConfig);
            directionalLayout.addComponent(allComponents, -1, -1);
            this.layouts.put(Long.valueOf(form.formId), new Pair<>(Integer.valueOf(form.remoteComponent.getLayoutId()), directionalLayout));
            this.componentMap.put(Long.valueOf(form.formId), allComponents);
            return 0;
        } catch (ComponentProvider.ComponentProviderException | LayoutScatterException unused) {
            Log.error(LABEL, "addNewLayoutView, remote component apply failed", new Object[0]);
            return 2;
        }
    }

    private void startFullPage(Form form) {
        if (form == null) {
            Log.error(LABEL, "startFullPage, form is null", new Object[0]);
            return;
        }
        Intent intent = new Intent();
        intent.setElement(new ElementName("", form.getBundleName(), form.getAbilityName()));
        intent.setParam(PARAM_FORM_ID_KEY, (int) form.formId);
        intent.setParam(PARAM_FORM_IDENTITY_KEY, form.formId);
        if (form.getFormAnimation() == null) {
            startAbility(intent);
        } else {
            AnimatorOption onGetAnimation = form.getFormAnimation().onGetAnimation();
            if (onGetAnimation != null) {
                AbilityStartSetting emptySetting = AbilityStartSetting.getEmptySetting();
                emptySetting.addAnimatorOption(onGetAnimation);
                startAbility(intent, emptySetting);
            } else {
                startAbility(intent);
            }
        }
        addUsageRecord(form.getBundleName(), form.getAbilityName());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void addUsageRecord(String str, String str2) {
        if (str != null && !str.isEmpty() && str2 != null && !str2.isEmpty()) {
            IBundleManager bundleManager = getBundleManager();
            if (bundleManager == null) {
                Log.error(LABEL, "getBundleManager failed when addUsageRecord", new Object[0]);
                return;
            }
            try {
                bundleManager.addUsageRecord(str, str2);
            } catch (RemoteException e) {
                Log.error(LABEL, "addUsageRecord exception %{public}s", e.getMessage());
            }
        }
    }

    private void addComponentToLayout(Component component, DirectionalLayout directionalLayout) {
        ComponentContainer.LayoutConfig layoutConfig = directionalLayout.getLayoutConfig();
        if (layoutConfig.width == -2 && layoutConfig.height == -2) {
            layoutConfig.width = component.getLayoutConfig().width;
            layoutConfig.height = component.getLayoutConfig().height;
            directionalLayout.setLayoutConfig(layoutConfig);
            Log.debug(LABEL, "addComponentToLayout current component width %{public}d, height %{public}d", Integer.valueOf(layoutConfig.width), Integer.valueOf(layoutConfig.height));
        }
        directionalLayout.addComponent(component, -1, -1);
    }

    private ComponentContainer getPreviewComponents(Context context, String str, int i) {
        Log.debug(LABEL, "getPreviewComponents....", new Object[0]);
        Context createBundleContext = context.createBundleContext(str, 2);
        if (createBundleContext == null) {
            Log.debug(LABEL, "getPreviewComponents context is null", new Object[0]);
            return null;
        }
        ResourceManager resourceManager = createBundleContext.getResourceManager();
        if (resourceManager == null) {
            Log.info(LABEL, "getPreviewComponents resManager is null", new Object[0]);
            return null;
        }
        try {
            Component parse = LayoutScatter.getInstance(this).clone(createBundleContext, resourceManager).parse(i, null, false);
            if (parse == null) {
                Log.info(LABEL, "getPreviewComponents component is null", new Object[0]);
                return null;
            } else if (parse instanceof ComponentContainer) {
                ComponentContainer componentContainer = (ComponentContainer) parse;
                Log.info(LABEL, "getPreviewComponents addComponent success.", new Object[0]);
                return componentContainer;
            } else {
                Log.info(LABEL, "getPreviewComponents addComponent failed", new Object[0]);
                return null;
            }
        } catch (ComponentProvider.ComponentProviderException | LayoutScatterException e) {
            Log.error(LABEL, "getPreviewComponents, parse layout id failed %{public}s", e.getMessage());
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public void processFormUninstall(long j) {
        FormCallback formCallback;
        synchronized (this.FORM_LOCK) {
            formCallback = this.appCallbacks.get(Long.valueOf(j));
            cleanFormResource(j);
        }
        if (formCallback != null) {
            formCallback.onFormUninstalled(j);
        }
    }

    /* access modifiers changed from: package-private */
    public void processFormUpdate(Form form) {
        if (form == null) {
            Log.error(LABEL, "on acquired ability form error, form is empty", new Object[0]);
        } else if (getContext().getUITaskDispatcher() == null) {
            Log.error(LABEL, "get ui dispatcher failed", new Object[0]);
        } else {
            Log.debug(LABEL, "on acquired ability form %{public}d", Long.valueOf(form.formId));
            getContext().getUITaskDispatcher().asyncDispatch(new Runnable(form) {
                /* class ohos.aafwk.ability.$$Lambda$AbilitySlice$Uz5yombypW4vMgJFvU8NQSPM0G4 */
                private final /* synthetic */ Form f$1;

                {
                    this.f$1 = r2;
                }

                public final void run() {
                    AbilitySlice.this.lambda$processFormUpdate$2$AbilitySlice(this.f$1);
                }
            });
        }
    }

    public /* synthetic */ void lambda$processFormUpdate$2$AbilitySlice(Form form) {
        handleFormMessage(1, form);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002d, code lost:
        r0 = ohos.aafwk.ability.FormManager.getInstance();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0031, code lost:
        if (r0 == null) goto L_0x0050;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003b, code lost:
        if (r0.deleteForm(r6, ohos.aafwk.ability.FormHostClient.getInstance(), r8) == false) goto L_0x0048;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003d, code lost:
        r8 = r5.FORM_LOCK;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003f, code lost:
        monitor-enter(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        cleanFormResource(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0043, code lost:
        monitor-exit(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0044, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004f, code lost:
        throw new ohos.aafwk.ability.FormException(ohos.aafwk.ability.FormException.FormError.INTERNAL_ERROR);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0050, code lost:
        ohos.aafwk.utils.log.Log.error(ohos.aafwk.ability.AbilitySlice.LABEL, "deleteForm, formManager is null", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0060, code lost:
        throw new ohos.aafwk.ability.FormException(ohos.aafwk.ability.FormException.FormError.FMS_RPC_ERROR);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean deleteForm(long r6, int r8) throws ohos.aafwk.ability.FormException {
        /*
        // Method dump skipped, instructions count: 108
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.AbilitySlice.deleteForm(long, int):boolean");
    }

    /* access modifiers changed from: private */
    public class MyDeathCallback implements FormManager.DeathCallback {
        private MyDeathCallback() {
        }

        @Override // ohos.aafwk.ability.FormManager.DeathCallback
        public void onDeathReceived() {
            ArrayList<Long> arrayList;
            synchronized (AbilitySlice.this.FORM_LOCK) {
                arrayList = new ArrayList(AbilitySlice.this.userReqParam.keySet());
            }
            for (Long l : arrayList) {
                long longValue = l.longValue();
                try {
                    if (AbilitySlice.this.isInitialized) {
                        synchronized (AbilitySlice.this.FORM_LOCK) {
                            Intent intent = (Intent) AbilitySlice.this.userReqParam.get(Long.valueOf(longValue));
                            if (intent == null) {
                                Log.error(AbilitySlice.LABEL, "form %{public}d is already deleted when recover", Long.valueOf(longValue));
                            } else if (intent.getBooleanParam(AbilitySlice.PARAM_FORM_TEMPORARY_KEY, false) && !AbilitySlice.this.lostedByReconnectTempForms.contains(Long.valueOf(longValue))) {
                                AbilitySlice.this.lostedByReconnectTempForms.add(Long.valueOf(longValue));
                            } else if (!AbilitySlice.this.reAcquireForm(longValue, intent)) {
                                Log.error(AbilitySlice.LABEL, "reacquire form %{public}d failed", Long.valueOf(longValue));
                            }
                        }
                    } else {
                        return;
                    }
                } catch (FormException e) {
                    Log.error(AbilitySlice.LABEL, "reacquire form exception %{public}s", e.getMessage());
                    if (e.getErrorCode() != FormException.FormError.FMS_RPC_ERROR && e.getErrorCode() != FormException.FormError.SEND_FMS_MSG_ERROR) {
                        synchronized (AbilitySlice.this.FORM_LOCK) {
                            FormCallback formCallback = (FormCallback) AbilitySlice.this.appCallbacks.get(Long.valueOf(longValue));
                            if (formCallback == null) {
                                Log.error(AbilitySlice.LABEL, "notify failed, lack of callback for form %{public}d", Long.valueOf(longValue));
                                return;
                            }
                            Log.info(AbilitySlice.LABEL, "notify restore failed event to user for form %{public}d", Long.valueOf(longValue));
                            Form form = new Form();
                            form.formId = longValue;
                            formCallback.onAcquired(4, form);
                        }
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean reAcquireForm(long j, Intent intent) throws FormException {
        S s;
        Pair<Integer, DirectionalLayout> pair;
        Log.info(LABEL, "reacquire form start for %{public}d", Long.valueOf(j));
        synchronized (this.FORM_LOCK) {
            Pair<Integer, DirectionalLayout> pair2 = this.layouts.get(Long.valueOf(j));
            s = pair2 != null ? pair2.s : null;
            if (s == null) {
                throw new FormException(FormException.FormError.INTERNAL_ERROR, "layout is null when reacquire form");
            }
        }
        ResourceManager resourceManager = getResourceManager();
        if (resourceManager == null || resourceManager.getConfiguration() == null || resourceManager.getConfiguration().direction != 1) {
            intent.setParam(PARAM_FORM_ORIENTATION_KEY, 1);
        } else {
            intent.setParam(PARAM_FORM_ORIENTATION_KEY, 2);
        }
        FormManager instance = FormManager.getInstance();
        if (instance != null) {
            Form addForm = instance.addForm(intent, FormHostClient.getInstance());
            if (addForm == null || addForm.formId <= 0 || addForm.formId != j) {
                throw new FormException(FormException.FormError.INTERNAL_ERROR, "fms reacquire form failed");
            }
            TaskDispatcher uITaskDispatcher = getUITaskDispatcher();
            if (uITaskDispatcher != null) {
                synchronized (this.FORM_LOCK) {
                    if (addForm.getInstantProvider() != null) {
                        pair = new Pair<>(-1, s);
                    } else if (addForm.remoteComponent == null) {
                        return true;
                    } else {
                        pair = new Pair<>(Integer.valueOf(addForm.remoteComponent.getLayoutId()), s);
                    }
                    this.layouts.put(Long.valueOf(j), pair);
                    uITaskDispatcher.asyncDispatch(new Runnable(addForm) {
                        /* class ohos.aafwk.ability.$$Lambda$AbilitySlice$JMx_0LRkNrqpyjJFIq1jxsQ */
                        private final /* synthetic */ Form f$1;

                        {
                            this.f$1 = r2;
                        }

                        public final void run() {
                            AbilitySlice.this.lambda$reAcquireForm$3$AbilitySlice(this.f$1);
                        }
                    });
                    return true;
                }
            }
            throw new FormException(FormException.FormError.INTERNAL_ERROR, "ui dispatcher is not found");
        }
        throw new FormException(FormException.FormError.FMS_RPC_ERROR);
    }

    public /* synthetic */ void lambda$reAcquireForm$3$AbilitySlice(Form form) {
        handleFormMessage(2, form);
    }

    private void cleanAllFormsResource() {
        synchronized (this.FORM_LOCK) {
            for (Long l : new ArrayList(this.appCallbacks.keySet())) {
                cleanFormResource(l.longValue());
            }
            Log.info(LABEL, "clean all forms resource success", new Object[0]);
        }
    }

    private void cleanFormResource(long j) {
        long j2;
        FormManager.DeathCallback deathCallback;
        Iterator<Long> it = this.userReqParam.keySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                j2 = -1;
                break;
            }
            j2 = it.next().longValue();
            if ((j2 & 4294967295L) == (4294967295L & j)) {
                break;
            }
        }
        if (j2 != -1) {
            Log.debug(LABEL, "clean id is %{public}d", Long.valueOf(j2));
            this.layouts.remove(Long.valueOf(j2));
            this.componentMap.remove(Long.valueOf(j2));
            this.appCallbacks.remove(Long.valueOf(j2));
            this.userReqParam.remove(Long.valueOf(j2));
            this.lostedByReconnectTempForms.remove(Long.valueOf(j2));
            synchronized (FormHostClient.class) {
                FormHostClient.getInstance().removeForm(this, j2);
            }
            if (this.appCallbacks.isEmpty() && (deathCallback = this.myDeathCallback) != null) {
                FormManager.unregisterDeathCallback(deathCallback);
                this.myDeathCallback = null;
            }
            InstantProvider instantProvider = this.instantProviders.get(Long.valueOf(j2));
            if (instantProvider != null) {
                instantProvider.destroy();
                this.instantProviders.remove(Long.valueOf(j2));
            }
        }
    }

    @Deprecated
    public boolean deleteForm(int i) throws FormException {
        return deleteForm((long) i);
    }

    public boolean deleteForm(long j) throws FormException {
        if (j > 0) {
            return deleteForm(j, 3);
        }
        throw new FormException(FormException.FormError.INPUT_PARAM_INVALID, "passing in form id can't be negative");
    }

    @Deprecated
    public boolean releaseForm(int i) throws FormException {
        return releaseForm((long) i, false);
    }

    public boolean releaseForm(long j) throws FormException {
        return releaseForm(j, false);
    }

    public boolean releaseForm(long j, boolean z) throws FormException {
        if (j > 0) {
            return deleteForm(j, z ? 9 : 8);
        }
        throw new FormException(FormException.FormError.INPUT_PARAM_INVALID, "passing in form id can't be negative");
    }

    public boolean castTempForm(long j) throws FormException {
        if (j > 0) {
            Log.info(LABEL, "castTempForm begin of temp form %{public}d", Long.valueOf(j));
            if (FormManager.getRecoverStatus() != 2) {
                FormManager instance = FormManager.getInstance();
                if (instance == null) {
                    Log.error(LABEL, "castTempForm, formManager is null", new Object[0]);
                    throw new FormException(FormException.FormError.FMS_RPC_ERROR);
                } else if (instance.castTempForm(j, FormHostClient.getInstance())) {
                    synchronized (this.FORM_LOCK) {
                        if (this.userReqParam.get(Long.valueOf(j)) != null) {
                            this.userReqParam.get(Long.valueOf(j)).setParam(PARAM_FORM_TEMPORARY_KEY, false);
                        }
                    }
                    return true;
                } else {
                    throw new FormException(FormException.FormError.INTERNAL_ERROR);
                }
            } else {
                throw new FormException(FormException.FormError.FORM_IN_RECOVER);
            }
        } else {
            throw new FormException(FormException.FormError.INPUT_PARAM_INVALID, "passing in form id can't be negative");
        }
    }

    public void notifyVisibleForms(List<Long> list) throws FormException {
        notifyWhetherVisibleForms(list, 1);
    }

    public void notifyInvisibleForms(List<Long> list) throws FormException {
        notifyWhetherVisibleForms(list, 2);
    }

    private void notifyWhetherVisibleForms(List<Long> list, int i) throws FormException {
        if (list == null || list.size() == 0 || list.size() > 32) {
            throw new FormException(FormException.FormError.INPUT_PARAM_INVALID, "formIds is empty or exceed 32");
        } else if (FormManager.getRecoverStatus() != 2) {
            FormManager instance = FormManager.getInstance();
            if (instance == null) {
                Log.error(LABEL, "notifyWhetherVisibleForm, formManager is null", new Object[0]);
                throw new FormException(FormException.FormError.FMS_RPC_ERROR);
            } else if (!instance.notifyWhetherVisibleForms(list, i, FormHostClient.getInstance())) {
                throw new FormException(FormException.FormError.INTERNAL_ERROR);
            }
        } else {
            throw new FormException(FormException.FormError.FORM_IN_RECOVER);
        }
    }

    public int checkAndDeleteInvalidForms(List<Long> list) throws FormException {
        if (list != null) {
            Log.info(LABEL, "checkAndDeleteInvalidForms begin: persistedIds size is %{public}d", Integer.valueOf(list.size()));
            if (FormManager.getRecoverStatus() != 2) {
                FormManager instance = FormManager.getInstance();
                if (instance != null) {
                    return instance.checkAndDeleteInvalidForms(list, FormHostClient.getInstance());
                }
                Log.error(LABEL, "checkAndDeleteInvalidForms, formManager is null", new Object[0]);
                throw new FormException(FormException.FormError.FMS_RPC_ERROR);
            }
            throw new FormException(FormException.FormError.FORM_IN_RECOVER);
        }
        throw new FormException(FormException.FormError.INPUT_PARAM_INVALID, "checkAndDeleteInvalidForms, request persistedIds is null");
    }

    public List<FormInfo> getAllFormsInfo() throws FormException {
        IBundleManager bundleManager = getBundleManager();
        if (bundleManager != null) {
            try {
                return bundleManager.getAllFormsInfo();
            } catch (RemoteException e) {
                Log.error(LABEL, "getAllFormsInfo exception %{public}s", e.getMessage());
                FormException.FormError formError = FormException.FormError.SEND_BMS_MSG_ERROR;
                throw new FormException(formError, "get forms info error: " + e.getMessage());
            } catch (SecurityException unused) {
                Log.error(LABEL, "getAllFormsInfo no GET_BUNDLE_INFO_PRIVILEGED permission", new Object[0]);
                throw new FormException(FormException.FormError.PERMISSION_DENY, "check permission deny, need to request ohos.permission.GET_BUNDLE_INFO_PRIVILEGED");
            }
        } else {
            Log.error(LABEL, "getBundleManager failed when getAllFormsInfo", new Object[0]);
            throw new FormException(FormException.FormError.BMS_RPC_ERROR);
        }
    }

    @Deprecated
    public List<FormInfo> getAllForms() throws FormException {
        return getAllFormsInfo();
    }

    private boolean lifecycleUpdate(List<Long> list, int i) throws FormException {
        if (FormManager.getRecoverStatus() != 2) {
            FormManager instance = FormManager.getInstance();
            if (instance != null) {
                return instance.lifecycleUpdate(list, FormHostClient.getInstance(), i);
            }
            Log.error(LABEL, "lifecycleUpdate, get form manager instance failed", new Object[0]);
            throw new FormException(FormException.FormError.FMS_RPC_ERROR);
        }
        throw new FormException(FormException.FormError.FORM_IN_RECOVER);
    }

    private void changeFormsUpdateFlag(int i) {
        synchronized (this.FORM_LOCK) {
            if (!this.appCallbacks.isEmpty()) {
                ArrayList arrayList = new ArrayList(this.appCallbacks.keySet());
                try {
                    lifecycleUpdate(arrayList, i);
                    Log.debug(LABEL, "change update flag %{public}d success", Integer.valueOf(i));
                } catch (FormException e) {
                    Log.error(LABEL, "change update flag %{public}d failed: %{public}s", Integer.valueOf(i), e.getMessage());
                }
            }
        }
    }

    public List<FormInfo> getFormsInfoByApp(String str) throws FormException {
        IBundleManager bundleManager = getBundleManager();
        if (bundleManager != null) {
            try {
                return bundleManager.getFormsInfoByApp(str);
            } catch (RemoteException e) {
                Log.error(LABEL, "getFormsInfoByApp exception %{public}s", e.getMessage());
                FormException.FormError formError = FormException.FormError.SEND_BMS_MSG_ERROR;
                throw new FormException(formError, "get form infos error: " + e.getMessage());
            } catch (SecurityException unused) {
                Log.error(LABEL, "getFormsInfoByApp no GET_BUNDLE_INFO_PRIVILEGED permission", new Object[0]);
                throw new FormException(FormException.FormError.PERMISSION_DENY, "check permission deny, need to request ohos.permission.GET_BUNDLE_INFO_PRIVILEGED");
            }
        } else {
            Log.error(LABEL, "getBundleManager failed when getFormsInfoByApp", new Object[0]);
            throw new FormException(FormException.FormError.BMS_RPC_ERROR);
        }
    }

    @Deprecated
    public List<FormInfo> getFormsByApp(String str) throws FormException {
        return getFormsInfoByApp(str);
    }

    public List<FormInfo> getFormsInfoByModule(String str, String str2) throws FormException {
        IBundleManager bundleManager = getBundleManager();
        if (bundleManager != null) {
            try {
                return bundleManager.getFormsInfoByModule(str, str2);
            } catch (RemoteException e) {
                Log.error(LABEL, "getFormsInfoByModule exception %{public}s", e.getMessage());
                FormException.FormError formError = FormException.FormError.SEND_BMS_MSG_ERROR;
                throw new FormException(formError, "get form infos error: " + e.getMessage());
            } catch (SecurityException unused) {
                Log.error(LABEL, "getFormsInfoByModule no GET_BUNDLE_INFO_PRIVILEGED permission", new Object[0]);
                throw new FormException(FormException.FormError.PERMISSION_DENY, "check permission deny, need to request ohos.permission.GET_BUNDLE_INFO_PRIVILEGED");
            }
        } else {
            Log.error(LABEL, "getBundleManager failed when getFormsInfoByModule", new Object[0]);
            throw new FormException(FormException.FormError.BMS_RPC_ERROR);
        }
    }

    @Deprecated
    public List<FormInfo> getFormsByModule(String str, String str2) throws FormException {
        return getFormsInfoByModule(str, str2);
    }

    @Deprecated
    public boolean requestForm(int i) throws FormException {
        return requestForm((long) i);
    }

    public boolean requestForm(long j) throws FormException {
        return requestFormWithIntent(j, new Intent());
    }

    @Deprecated
    public boolean requestForm(long j, Intent intent) throws FormException {
        return requestFormWithIntent(j, intent);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean requestFormWithIntent(long j, Intent intent) throws FormException {
        if (j <= 0 || intent == null) {
            Log.error(LABEL, "requestFormWithIntent, input param intent null", new Object[0]);
            throw new FormException(FormException.FormError.INPUT_PARAM_INVALID, "request form or intent is invalid");
        } else if (FormManager.getRecoverStatus() != 2) {
            FormManager instance = FormManager.getInstance();
            if (instance == null) {
                Log.error(LABEL, "requestFormWithIntent, failed to get form manager", new Object[0]);
                throw new FormException(FormException.FormError.FMS_RPC_ERROR);
            } else if (instance.requestForm(j, FormHostClient.getInstance(), intent)) {
                return true;
            } else {
                throw new FormException(FormException.FormError.INTERNAL_ERROR);
            }
        } else {
            throw new FormException(FormException.FormError.FORM_IN_RECOVER);
        }
    }

    public void releaseAbilityForm(AbilityForm abilityForm) {
        Log.info(LABEL, "release ability form", new Object[0]);
        if (abilityForm != null) {
            synchronized (this.acquiringRecords) {
                IAbilityConnection iAbilityConnection = this.acquiringRecords.get(abilityForm);
                if (iAbilityConnection != null) {
                    disconnectAbility(iAbilityConnection);
                    iAbilityConnection.onAbilityDisconnectDone(new ElementName(), 0);
                    abilityForm.release();
                    Log.info(LABEL, "release ability form done", new Object[0]);
                } else {
                    throw new IllegalArgumentException("passing in abilityForm is not acquired");
                }
            }
            return;
        }
        throw new IllegalArgumentException("passing in abilityForm must not be null");
    }

    @Override // ohos.aafwk.ability.ILifecycle
    public final Lifecycle getLifecycle() {
        return this.lifecycle;
    }

    public final Ability getAbility() {
        checkInitialization("get ability failed");
        return this.abilitySliceManager.getAbility();
    }

    private void dispatchLifecycle(Lifecycle.Event event, Intent intent) throws IllegalStateException {
        this.lifecycle.dispatchLifecycle(event, intent);
    }

    /* access modifiers changed from: package-private */
    public final void start(Intent intent) throws LifecycleException {
        if (this.currentState == AbilitySliceLifecycleExecutor.LifecycleState.INITIAL) {
            setUiAttachedAllowed(true);
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnStart");
            onStart(intent);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnStart");
            dispatchLifecycle(Lifecycle.Event.ON_START, intent);
            if (getAbility().supportHighPerformanceUI() || this.uiContent.isLatestUIAttached() || this.uiContent.isUiAttachedDisable()) {
                this.currentState = AbilitySliceLifecycleExecutor.LifecycleState.INACTIVE;
            } else {
                Log.error(LABEL, "UI must be setup correctly in onStart()", new Object[0]);
                throw new AbilitySliceRuntimeException("UI must be setup correctly in onStart()");
            }
        } else {
            throw new LifecycleException("Action(\" start \") is illegal for current state [" + this.currentState + "]");
        }
    }

    /* access modifiers changed from: package-private */
    public final void active() throws LifecycleException {
        if (this.currentState == AbilitySliceLifecycleExecutor.LifecycleState.INACTIVE) {
            setUiAttachedAllowed(true);
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnActive");
            onActive();
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnActive");
            dispatchLifecycle(Lifecycle.Event.ON_ACTIVE, null);
            this.currentState = AbilitySliceLifecycleExecutor.LifecycleState.ACTIVE;
            synchronized (this.acquiringRecords) {
                for (AbilityForm abilityForm : this.acquiringRecords.keySet()) {
                    if (abilityForm != null) {
                        abilityForm.enableUpdatePush();
                    }
                }
            }
            changeFormsUpdateFlag(5);
            return;
        }
        throw new LifecycleException("Action(\" Active \") is illegal for current state [" + this.currentState + "]");
    }

    /* access modifiers changed from: package-private */
    public final void inactive() throws LifecycleException {
        if (this.currentState == AbilitySliceLifecycleExecutor.LifecycleState.ACTIVE) {
            setUiAttachedAllowed(true);
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnInactive");
            onInactive();
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnInactive");
            dispatchLifecycle(Lifecycle.Event.ON_INACTIVE, null);
            this.currentState = AbilitySliceLifecycleExecutor.LifecycleState.INACTIVE;
            return;
        }
        throw new LifecycleException("Action(\" inactive \") is illegal for current state [" + this.currentState + "]");
    }

    /* access modifiers changed from: package-private */
    public final void background() throws LifecycleException {
        if (this.currentState == AbilitySliceLifecycleExecutor.LifecycleState.BACKGROUND) {
            Log.warn("topslice has been changed to background", new Object[0]);
        } else if (this.currentState == AbilitySliceLifecycleExecutor.LifecycleState.INACTIVE) {
            setUiAttachedAllowed(false);
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnBackground");
            onBackground();
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnBackground");
            dispatchLifecycle(Lifecycle.Event.ON_BACKGROUND, null);
            this.currentState = AbilitySliceLifecycleExecutor.LifecycleState.BACKGROUND;
            if (this.abilitySliceManager.getAbilityState() == Ability.STATE_ACTIVE) {
                this.uiContent.setLatestUIAttachedFlag(false);
            } else if (Log.isDebuggable()) {
                Log.debug(LABEL, "the entire ability is moving to background, no need to reset UIAttached flag", new Object[0]);
            }
            synchronized (this.acquiringRecords) {
                for (AbilityForm abilityForm : this.acquiringRecords.keySet()) {
                    if (abilityForm != null) {
                        abilityForm.disableUpdatePush();
                    }
                }
            }
            changeFormsUpdateFlag(6);
        } else {
            throw new LifecycleException("Action(\" background \") is illegal for current state [" + this.currentState + "]");
        }
    }

    /* access modifiers changed from: package-private */
    public final void foreground(Intent intent) throws LifecycleException {
        if (this.currentState == AbilitySliceLifecycleExecutor.LifecycleState.INACTIVE) {
            Log.warn("topslice has been changed to inactive", new Object[0]);
        } else if (this.currentState == AbilitySliceLifecycleExecutor.LifecycleState.BACKGROUND) {
            setUiAttachedAllowed(true);
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnForeground");
            onForeground(intent);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnForeground");
            dispatchLifecycle(Lifecycle.Event.ON_FOREGROUND, intent);
            this.currentState = AbilitySliceLifecycleExecutor.LifecycleState.INACTIVE;
            this.uiContent.ensureLatestUIAttached();
        } else {
            throw new LifecycleException("Action(\" foreground \") is illegal for current state [" + this.currentState + "]");
        }
    }

    /* access modifiers changed from: package-private */
    public final void stop() throws LifecycleException {
        if (this.currentState == AbilitySliceLifecycleExecutor.LifecycleState.BACKGROUND) {
            setUiAttachedAllowed(false);
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnStop");
            onStop();
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnStop");
            dispatchLifecycle(Lifecycle.Event.ON_STOP, null);
            this.currentState = AbilitySliceLifecycleExecutor.LifecycleState.INITIAL;
            try {
                this.abilitySliceManager.disconnectAbility(this, null);
            } catch (IllegalStateException unused) {
                Log.info(LABEL, "conn is busy while slice disconnectAbility", new Object[0]);
            }
            this.uiContent.reset();
            this.uiContent = null;
            this.isInitialized = false;
            cleanAllFormsResource();
            return;
        }
        throw new LifecycleException("Action(\" stop \") is illegal for current state [" + this.currentState + "]");
    }

    /* access modifiers changed from: package-private */
    public final void dumpAbilitySlice(String str, PrintWriter printWriter, String str2) {
        printWriter.println(str + "Ability slice state: " + this.currentState);
        printWriter.print(str);
        if (this.resultData != null) {
            printWriter.println("Ability slice result: " + this.resultData.toUri());
        }
        printWriter.println(str + "Ability slice uiContent:");
        SliceUIContent sliceUIContent = this.uiContent;
        if (sliceUIContent == null) {
            printWriter.print(Ability.PREFIX + str);
            printWriter.println("null");
        } else {
            sliceUIContent.dump(Ability.PREFIX + str, printWriter, str2);
        }
        printWriter.println(str + "Ability slice connected service list:");
        AbilitySliceManager abilitySliceManager2 = this.abilitySliceManager;
        abilitySliceManager2.dumpServiceList(Ability.PREFIX + str, printWriter, this);
    }

    /* access modifiers changed from: package-private */
    public boolean scheduleStartContinuation() {
        if (!(this instanceof IAbilityContinuation)) {
            return true;
        }
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnStartContinuation");
        boolean onStartContinuation = ((IAbilityContinuation) this).onStartContinuation();
        C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnStartContinuation");
        return onStartContinuation;
    }

    /* access modifiers changed from: package-private */
    public boolean scheduleSaveData(IntentParams intentParams) {
        if (!(this instanceof IAbilityContinuation)) {
            return true;
        }
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnSaveData");
        boolean onSaveData = ((IAbilityContinuation) this).onSaveData(intentParams);
        C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnSaveData");
        return onSaveData;
    }

    /* access modifiers changed from: package-private */
    public boolean scheduleRestoreData(IntentParams intentParams) {
        if (!(this instanceof IAbilityContinuation)) {
            return true;
        }
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnRestoreData");
        boolean onRestoreData = ((IAbilityContinuation) this).onRestoreData(intentParams);
        C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnRestoreData");
        return onRestoreData;
    }

    /* access modifiers changed from: package-private */
    public void scheduleCompleteContinuation(int i) {
        if (this instanceof IAbilityContinuation) {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnCompleteContinuation");
            ((IAbilityContinuation) this).onCompleteContinuation(i);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "sliceOnCompleteContinuation");
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyRemoteTerminated() {
        if (this instanceof IAbilityContinuation) {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "notifyRemoteTerminated");
            ((IAbilityContinuation) this).onRemoteTerminated();
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "notifyRemoteTerminated");
        }
    }

    /* access modifiers changed from: package-private */
    public void componentEnterAnimator() {
        AbilitySliceAnimator abilitySliceAnimator = this.abilitySliceManager.getAbilitySliceAnimator();
        if (abilitySliceAnimator == null) {
            Log.info(LABEL, "ability slice animator is set to null", new Object[0]);
        } else {
            this.uiContent.componentEnterAnimator(abilitySliceAnimator);
        }
    }

    /* access modifiers changed from: package-private */
    public void componentExitAnimator() {
        AbilitySliceAnimator abilitySliceAnimator = this.abilitySliceManager.getAbilitySliceAnimator();
        if (abilitySliceAnimator == null) {
            Log.info(LABEL, "ability slice animator is set to null", new Object[0]);
        } else {
            this.uiContent.componentExitAnimator(abilitySliceAnimator);
        }
    }

    /* access modifiers changed from: package-private */
    public void stopComponentAnimator() {
        this.uiContent.stopComponentAnimator();
    }

    /* access modifiers changed from: package-private */
    public void setUiAttachedAllowed(boolean z) {
        this.uiContent.setUiAttachedAllowed(z);
    }

    /* access modifiers changed from: package-private */
    public void setLatestUIAttachedFlag(boolean z) {
        this.uiContent.setLatestUIAttachedFlag(z);
    }

    /* access modifiers changed from: package-private */
    public void setUiAttachedDisable(boolean z) {
        this.uiContent.setUiAttachedDisable(z);
    }

    private void checkInitialization(String str) {
        if (this.abilitySliceManager == null) {
            Log.error(LABEL, "abilitySliceManager is null, %{public}s", str);
            throw new IllegalStateException("abilitySliceManager is null, " + str);
        }
    }

    private void checkValidRequestCode(int i) {
        if ((-65536 & i) != 0) {
            throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
        }
    }

    public interface FormCallback {
        public static final int OHOS_FORM_ACQUIRE_SUCCESS = 0;
        public static final int OHOS_FORM_APPLY_FAILURE = 2;
        public static final int OHOS_FORM_PREVIEW_FAILURE = 1;
        public static final int OHOS_FORM_REAPPLY_FAILURE = 3;
        public static final int OHOS_FORM_RESTORE_FAILURE = 4;

        void onAcquired(int i, Form form);

        @Deprecated
        void onFormUninstalled(int i);

        default void onFormUninstalled(long j) {
            onFormUninstalled((int) j);
        }
    }
}
