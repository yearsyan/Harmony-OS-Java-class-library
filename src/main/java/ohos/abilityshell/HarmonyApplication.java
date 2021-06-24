package ohos.abilityshell;

import android.app.ActivityManager;
import android.app.Application;
import android.app.IActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilityPackage;
import ohos.aafwk.ability.HarmonyosApplication;
import ohos.abilityshell.delegation.AbilityDelegator;
import ohos.app.AbilityContext;
import ohos.app.ContextDeal;
import ohos.app.DumpHelper;
import ohos.app.ProcessInfo;
import ohos.app.dispatcher.threading.AndroidTaskLooper;
import ohos.appexecfwk.utils.AppLog;
import ohos.bundle.AbilityInfo;
import ohos.bundle.BundleInfo;
import ohos.bundle.HapModuleInfo;
import ohos.eventhandler.EventRunner;
import ohos.global.resource.ResourceUtils;
import ohos.hiviewdfx.HiLogLabel;
import ohos.idn.BasicInfo;
import ohos.idn.DeviceManager;
import ohos.security.keystore.provider.HarmonyKeyStoreProvider;
import ohos.sysability.samgr.SysAbilityManager;
import ohos.sysability.samgr.SystemReadyCallbackSkeleton;
import ohos.system.Parameters;
import ohos.tools.C0000Bytrace;

public class HarmonyApplication extends Application {
    private static final Object CORESA_INIT_LOCK = new Object();
    private static final int CORESA_WAIT_TIME = 30000;
    private static final String IPC_JNI_SO_NAME = "ipc_core.z";
    private static final String SAMGR_CORESA_INITREADY = "sys.samgr.coresa.initready";
    private static final HiLogLabel SHELL_LABEL = new HiLogLabel(3, 218108160, "AbilityShell");
    private static String currentModuleName;
    private static HarmonyApplication harmonyApplication = null;
    private static boolean isUserApplicationStarted = false;
    private static Set<Runnable> loadDataAbilityTasks = new HashSet();
    private static Map<String, AbilityPackage> loadedHapMap = new HashMap();
    private static HarmonyosApplication userApplication = new HarmonyosApplication();
    private static CountDownLatch userApplicationLatch = new CountDownLatch(1);
    private Map<String, FormAbility> abilityMap = new HashMap();
    private ohos.app.Application application = new ohos.app.Application();
    private HandlerThread applicationHandleThread;
    private ApplicationPreLoadedHandler applicationHandler;
    private BundleInfo bundleInfo;
    private Date createTime = null;
    private SystemReadyCallback mCallback = new SystemReadyCallback();
    private final Object preloadLock = new Object();

    static {
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "load ipc jni library");
        try {
            System.loadLibrary(IPC_JNI_SO_NAME);
        } catch (UnsatisfiedLinkError unused) {
            AppLog.w("HarmonyApplication::Could not load ipc_core.z.so", new Object[0]);
        }
        C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "load ipc jni library");
    }

    public static HarmonyApplication getInstance() {
        return harmonyApplication;
    }

    public void waitForUserApplicationStart() {
        if (!isUserApplicationStarted) {
            try {
                userApplicationLatch.await();
            } catch (InterruptedException unused) {
                AppLog.e(SHELL_LABEL, "waitForUserApplicationStart InterruptedException occur", new Object[0]);
            }
        }
        if (!isUserApplicationStarted) {
            AppLog.e(SHELL_LABEL, "user application start timeout!", new Object[0]);
        }
    }

    public Map<String, AbilityPackage> getLoadedHapMap() {
        return loadedHapMap;
    }

    public static void registerDataAbility(Runnable runnable) {
        loadDataAbilityTasks.add(runnable);
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        checkSystemReady();
        super.attachBaseContext(context);
        setHarmonyApplication(this);
        initApplicationHandlerThread();
        if ("watch".equalsIgnoreCase(Parameters.get("ro.build.characteristics", ""))) {
            sendApplicationHandleMessage(2, null, 0);
        } else {
            tryLoadHarmony();
        }
    }

    private void checkSystemReady() {
        if (Parameters.getInt(SAMGR_CORESA_INITREADY, 0) == 0) {
            int registerSystemReadyCallback = SysAbilityManager.registerSystemReadyCallback(this.mCallback);
            AppLog.i(SHELL_LABEL, "coreSa registerSystemReadyCallback result %{public}d:", Integer.valueOf(registerSystemReadyCallback));
            if (registerSystemReadyCallback == 0) {
                try {
                    synchronized (CORESA_INIT_LOCK) {
                        CORESA_INIT_LOCK.wait(30000);
                        AppLog.i(SHELL_LABEL, "coreSa wait over", new Object[0]);
                    }
                } catch (InterruptedException unused) {
                    AppLog.w(SHELL_LABEL, "CORESA_INIT_LOCK wait failed", new Object[0]);
                }
            }
        } else {
            AppLog.d(SHELL_LABEL, "coreSa already init", new Object[0]);
        }
    }

    /* access modifiers changed from: private */
    public class SystemReadyCallback extends SystemReadyCallbackSkeleton {
        private SystemReadyCallback() {
        }

        @Override // ohos.sysability.samgr.ISystemReadyCallback
        public void onSystemReadyNotify() {
            AppLog.i(HarmonyApplication.SHELL_LABEL, "coreSa onSystemReadyNotify", new Object[0]);
            synchronized (HarmonyApplication.CORESA_INIT_LOCK) {
                HarmonyApplication.CORESA_INIT_LOCK.notifyAll();
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        AppLog.d(SHELL_LABEL, "harmonyApplication onCreate call", new Object[0]);
        EventRunner.setMainEventRunner();
        try {
            if (this.applicationHandleThread != null) {
                IActivityManager service = ActivityManager.getService();
                service.setHmThreadToRtg("mode:set;tids:" + this.applicationHandleThread.getThreadId());
            }
        } catch (RemoteException unused) {
            AppLog.e(SHELL_LABEL, "setHmThreadToRtg %{public}d failed", Integer.valueOf(this.applicationHandleThread.getThreadId()));
        }
        AbilityDelegator.getInstance().setClassLoader(getClassLoader());
        sendApplicationHandleMessage(4, null, 0);
    }

    public void onTerminate() {
        super.onTerminate();
        waitForUserApplicationStart();
        userApplication.onTerminate();
        if (loadedHapMap.containsKey(currentModuleName)) {
            loadedHapMap.get(currentModuleName).onEnd();
        }
    }

    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        waitForUserApplicationStart();
        userApplication.memoryLevelChange(i);
        if (loadedHapMap.containsKey(currentModuleName)) {
            loadedHapMap.get(currentModuleName).memoryLevelChange(i);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        waitForUserApplicationStart();
        AppLog.d(SHELL_LABEL, "HarmonyApplication::onConfigurationChanged has been call", new Object[0]);
        ohos.global.configuration.Configuration convert = ResourceUtils.convert(configuration);
        userApplication.configurationChanged(convert);
        if (loadedHapMap.containsKey(currentModuleName)) {
            loadedHapMap.get(currentModuleName).configurationChanged(convert);
        }
    }

    public final ohos.app.Application getApplication() {
        return this.application;
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        new DumpHelper(this.application).dump(str, fileDescriptor, printWriter, strArr);
    }

    public void setBundleInfo(BundleInfo bundleInfo2) {
        this.bundleInfo = bundleInfo2;
    }

    public BundleInfo getBundleInfo() {
        return this.bundleInfo;
    }

    public void setCurrentModule(String str) {
        currentModuleName = str;
    }

    public FormAbility getFormAbility(String str) {
        AppLog.d(SHELL_LABEL, "HarmonyApplication::getAbility key %{private}s", str);
        return this.abilityMap.get(str);
    }

    public void addFormAbility(String str, FormAbility formAbility) {
        AppLog.d(SHELL_LABEL, "HarmonyApplication::addAbility key %{private}s", str);
        this.abilityMap.put(str, formAbility);
    }

    public void removeOrSubRef(String str, Ability ability) {
        FormAbility formAbility = getFormAbility(str);
        if (formAbility == null || ability != formAbility.getAbility()) {
            if (formAbility == null) {
                AppLog.e(SHELL_LABEL, "HarmonyApplication::removeOrSubRef find %{private}s failed", str);
                return;
            }
            AppLog.d(SHELL_LABEL, "HarmonyApplication::Ability:%{private}s != caller:%{private}s", formAbility.getAbility(), ability);
        } else if (formAbility.getRefCount() == 1) {
            AppLog.d(SHELL_LABEL, "HarmonyApplication::removeOrSubRef remove key %{private}s", str);
            this.abilityMap.remove(str);
        } else {
            AppLog.d(SHELL_LABEL, "HarmonyApplication::removeOrSubRef subRefCount key %{private}s", str);
            formAbility.subRefCount();
        }
    }

    private void setHarmonyApplication(HarmonyApplication harmonyApplication2) {
        harmonyApplication = harmonyApplication2;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setApplicationEnv() {
        ContextDeal contextDeal = new ContextDeal(getApplicationContext(), getClassLoader());
        this.application.attachBaseContext(contextDeal);
        this.application.setAppCreateTime(this.createTime);
        contextDeal.setApplication(this.application);
        contextDeal.setMainLooper(new AndroidTaskLooper(Looper.getMainLooper()));
        new DeviceManager().getLocalNodeBasicInfo().ifPresent(new Consumer() {
            /* class ohos.abilityshell.$$Lambda$HarmonyApplication$58EpbRWKl26riizbOr2Bb9sLbo */

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                HarmonyApplication.this.lambda$setApplicationEnv$0$HarmonyApplication((BasicInfo) obj);
            }
        });
        this.application.setProcessInfo(new ProcessInfo(getApplicationInfo().processName, Process.myPid()));
        setAppDataPath();
        BundleInfo bundleInfo2 = this.bundleInfo;
        if (bundleInfo2 != null) {
            this.application.setBundleInfo(bundleInfo2);
            contextDeal.initApplicationResourceManager(this.bundleInfo.name);
            HarmonyKeyStoreProvider.waitLoadComplete();
            createUserApplication(this.bundleInfo.getEntryModuleName());
            isUserApplicationStarted = true;
            userApplicationLatch.countDown();
        }
    }

    public /* synthetic */ void lambda$setApplicationEnv$0$HarmonyApplication(BasicInfo basicInfo) {
        this.application.setLocalDeviceId(basicInfo.getNodeId());
    }

    public Bundle getMetaDataFromApp() {
        Bundle bundle;
        try {
            bundle = getPackageManager().getApplicationInfo(getPackageName(), 128).metaData;
        } catch (PackageManager.NameNotFoundException unused) {
            AppLog.e(SHELL_LABEL, "Cannot find this pkgName %{public}s", this.getPackageName());
            bundle = null;
        }
        return bundle == null ? new Bundle() : bundle;
    }

    public ClassLoader getClassLoaderByAbilityInfo(AbilityInfo abilityInfo) {
        AbilityPackage abilityPackage = loadedHapMap.get(abilityInfo.getModuleName());
        if (abilityPackage == null) {
            return null;
        }
        return abilityPackage.getClassLoader();
    }

    public void createUserApplication(String str) {
        setUserApplication(str);
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "userApplication onStart");
        userApplication.onStart();
        if (loadedHapMap.containsKey(currentModuleName)) {
            loadedHapMap.get(currentModuleName).onInitialize();
        }
        C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "userApplication onStart");
    }

    public void loadClass(ClassLoader classLoader, String str) {
        BundleInfo bundleInfo2 = this.bundleInfo;
        if (bundleInfo2 == null) {
            AppLog.d(SHELL_LABEL, "bundleInfo is null", new Object[0]);
            return;
        }
        HapModuleInfo hapModuleInfo = bundleInfo2.getHapModuleInfo(str);
        if (hapModuleInfo == null || hapModuleInfo.getName() == null) {
            AppLog.d(SHELL_LABEL, "hap moduleInfo is null", new Object[0]);
            return;
        }
        String name = hapModuleInfo.getName();
        if (loadedHapMap.containsKey(str)) {
            AppLog.d(SHELL_LABEL, "this module %{public}s has been load", name);
            return;
        }
        try {
            Object newInstance = classLoader.loadClass(name).newInstance();
            if (newInstance instanceof AbilityPackage) {
                AbilityPackage abilityPackage = (AbilityPackage) newInstance;
                abilityPackage.setClassLoader(classLoader);
                loadedHapMap.put(str, abilityPackage);
                attachHapModuleContext(abilityPackage, hapModuleInfo);
                currentModuleName = str;
                this.application.setHarmonyosAbilityPackage(str, newInstance);
                abilityPackage.onInitialize();
                return;
            }
            AbilityPackage abilityPackage2 = new AbilityPackage();
            abilityPackage2.setClassLoader(classLoader);
            loadedHapMap.put(str, abilityPackage2);
            AppLog.d(SHELL_LABEL, "HarmonyApplication::setApplicationEnv class not AbilityPackage exception %{public}s", name);
        } catch (ClassNotFoundException unused) {
            AbilityPackage abilityPackage3 = new AbilityPackage();
            abilityPackage3.setClassLoader(classLoader);
            loadedHapMap.put(str, abilityPackage3);
            AppLog.d(SHELL_LABEL, "HarmonyApplication::setApplicationEnv class not found exception %{public}s", name);
        } catch (IllegalAccessException | InstantiationException unused2) {
            AppLog.d(SHELL_LABEL, "HarmonyApplication::setApplicationEnv newInstance failed", new Object[0]);
        }
    }

    private void setUserApplication(String str) {
        BundleInfo bundleInfo2 = this.bundleInfo;
        if (bundleInfo2 == null) {
            AppLog.d(SHELL_LABEL, "bundleInfo is null", new Object[0]);
            return;
        }
        HapModuleInfo hapModuleInfo = bundleInfo2.getHapModuleInfo(str);
        if (hapModuleInfo == null || hapModuleInfo.getName() == null) {
            AppLog.d(SHELL_LABEL, "entry hap moduleInfo is null", new Object[0]);
            return;
        }
        String name = hapModuleInfo.getName();
        try {
            Object newInstance = getClassLoader().loadClass(name).newInstance();
            if (newInstance instanceof HarmonyosApplication) {
                userApplication = (HarmonyosApplication) newInstance;
                attachHapModuleContext(userApplication, null);
                this.application.setHarmonyosApplication(newInstance);
                loadedHapMap.put(str, new AbilityPackage());
            } else if (newInstance instanceof AbilityPackage) {
                loadedHapMap.put(str, (AbilityPackage) newInstance);
                attachHapModuleContext((AbilityPackage) newInstance, hapModuleInfo);
                currentModuleName = str;
                this.application.setHarmonyosAbilityPackage(str, newInstance);
            }
        } catch (ClassNotFoundException unused) {
            this.application.setHarmonyosApplication(new HarmonyosApplication());
            loadedHapMap.put(str, new AbilityPackage());
            this.application.setHarmonyosAbilityPackage(str, new AbilityPackage());
            AppLog.d(SHELL_LABEL, "HarmonyApplication::setApplicationEnv class not found exception %{public}s", name);
        } catch (IllegalAccessException | InstantiationException unused2) {
            AppLog.d(SHELL_LABEL, "HarmonyApplication::setApplicationEnv newInstance failed", new Object[0]);
        }
    }

    private void attachHapModuleContext(AbilityContext abilityContext, HapModuleInfo hapModuleInfo) {
        if (abilityContext != null) {
            ContextDeal contextDeal = new ContextDeal(getApplicationContext(), getClassLoader());
            contextDeal.setMainLooper(new AndroidTaskLooper(Looper.getMainLooper()));
            contextDeal.setApplication(this.application);
            if (abilityContext instanceof AbilityPackage) {
                contextDeal.setHapModuleInfo(hapModuleInfo);
            }
            abilityContext.attachBaseContext(contextDeal);
        }
    }

    private void setAppDataPath() {
        String str;
        String str2 = null;
        try {
            str = getInstance().getApplicationContext().getDataDir().getCanonicalPath();
            try {
                str2 = getInstance().getApplicationContext().createDeviceProtectedStorageContext().getDataDir().getCanonicalPath();
            } catch (IOException unused) {
            }
        } catch (IOException unused2) {
            str = null;
            AppLog.e(SHELL_LABEL, "HarmonyApplication::setAppDataPath fail to get dataDir", new Object[0]);
            AppLog.d(SHELL_LABEL, "HarmonyApplication::setAppDataPath AppDataPath: %{private}s, DeviceProtectedPath: %{private}s", str, str2);
            this.application.setAppDataPath(str);
            this.application.setDeviceProtectedPath(str2);
        }
        AppLog.d(SHELL_LABEL, "HarmonyApplication::setAppDataPath AppDataPath: %{private}s, DeviceProtectedPath: %{private}s", str, str2);
        this.application.setAppDataPath(str);
        this.application.setDeviceProtectedPath(str2);
    }

    public Ability getTopAbility() {
        ohos.app.Application application2 = this.application;
        if (application2 == null) {
            return null;
        }
        Object topAbility = application2.getTopAbility();
        if (topAbility instanceof Ability) {
            return (Ability) topAbility;
        }
        return null;
    }

    public void setTopAbility(Ability ability) {
        ohos.app.Application application2 = this.application;
        if (application2 != null) {
            application2.setTopAbility(ability);
        }
    }

    public HapModuleInfo getHapModuleInfoByAbilityInfo(AbilityInfo abilityInfo) {
        if (abilityInfo == null || abilityInfo.getModuleName() == null) {
            AppLog.d(SHELL_LABEL, "AbilityInfo or moduleInfo is null", new Object[0]);
            return null;
        }
        BundleInfo bundleInfo2 = this.bundleInfo;
        if (bundleInfo2 != null) {
            return bundleInfo2.getHapModuleInfo(abilityInfo.getModuleName());
        }
        AppLog.d(SHELL_LABEL, "bundleInfo is null", new Object[0]);
        return null;
    }

    private void initApplicationHandlerThread() {
        if (this.applicationHandleThread == null) {
            this.applicationHandleThread = new HandlerThread("application-accelerate-Thread", 10);
            this.applicationHandleThread.start();
        }
    }

    /* access modifiers changed from: private */
    public final class ApplicationPreLoadedHandler extends Handler {
        private static final int ASYNC_THREAD_QUIT = 0;
        private static final int CREATE_APPLIACATION_ENV = 4;
        private static final int PRE_TRY_LOAD_HARMONY = 2;

        public ApplicationPreLoadedHandler(Looper looper) {
            super(looper, null, true);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                synchronized (HarmonyApplication.this.preloadLock) {
                    if (!(HarmonyApplication.this.applicationHandleThread == null || HarmonyApplication.this.applicationHandleThread.getLooper() == null)) {
                        HarmonyApplication.this.applicationHandleThread.getLooper().quit();
                        HarmonyApplication.this.applicationHandleThread = null;
                        HarmonyApplication.this.applicationHandler = null;
                    }
                }
            } else if (i == 2) {
                HarmonyApplication.this.tryLoadHarmony();
            } else if (i != 4) {
                AppLog.e(HarmonyApplication.SHELL_LABEL, "Invalid preload activity message msg: %{public}d", Integer.valueOf(message.what));
            } else {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "Applicaion on Create");
                HarmonyApplication.this.createTime = new Date();
                HarmonyApplication.this.setApplicationEnv();
                for (Runnable runnable : HarmonyApplication.loadDataAbilityTasks) {
                    runnable.run();
                }
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "Application on Create");
                HarmonyApplication.this.sendApplicationHandleMessage(0, null, 0);
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void sendApplicationHandleMessage(int i, Object obj, long j) {
        synchronized (this.preloadLock) {
            initApplicationHandlerThread();
            if (this.applicationHandler == null) {
                this.applicationHandler = new ApplicationPreLoadedHandler(this.applicationHandleThread.getLooper());
            }
            this.applicationHandler.sendMessageDelayed(this.applicationHandler.obtainMessage(i, obj), j);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void tryLoadHarmony() {
        new HarmonyLoader(getBaseContext()).tryLoadHarmony(getBaseContext());
    }
}
