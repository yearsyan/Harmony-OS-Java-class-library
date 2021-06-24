package ohos.abilityshell;

import android.content.Context;
import dalvik.system.PathClassLoader;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import ohos.appexecfwk.utils.AppLog;
import ohos.bundle.BundleInfo;
import ohos.bundle.ShellApplicationCallback;
import ohos.hiviewdfx.HiLogLabel;
import ohos.security.keystore.provider.HarmonyKeyStoreProvider;
import ohos.system.Parameters;
import ohos.tools.C0000Bytrace;

public class HarmonyLoader {
    private static final String DALVIK_SYSTEM_CLASS_LOADER = "dalvik.system.PathClassLoader";
    private static final String LIBS = "!/libs/";
    private static final CountDownLatch LOADER_LATCH = new CountDownLatch(1);
    private static final String SAMGR_CORESA_INITREADY = "sys.samgr.coresa.initready";
    private static final HiLogLabel SHELL_LABEL = new HiLogLabel(3, 218108160, "AbilityShell");
    private static volatile BundleMgrBridge bundleMgrImpl;
    private static boolean enableSplitClassLoader = HarmonyApplication.getInstance().getMetaDataFromApp().getBoolean("loaderIsolation");
    private static boolean isLoadDone = false;
    private Context applicationContext;
    private final Object classLoadLock = new Object();
    private ApplicationChangeReceive receive;

    HarmonyLoader(Context context) {
        this.applicationContext = context;
    }

    private static void setLoaderDone(boolean z) {
        isLoadDone = z;
    }

    public static void waitForLoadHarmony() {
        AppLog.d(SHELL_LABEL, "waitForLoadHarmony begin", new Object[0]);
        if (!isLoadDone) {
            try {
                LOADER_LATCH.await();
            } catch (InterruptedException unused) {
                AppLog.e(SHELL_LABEL, "waitForLoadHarmony InterruptedException occur", new Object[0]);
            }
        }
        if (!isLoadDone) {
            AppLog.d(SHELL_LABEL, "load harmony application failed!", new Object[0]);
        }
        AppLog.d(SHELL_LABEL, "waitForLoadHarmony end", new Object[0]);
    }

    public void tryLoadHarmony(Context context) throws IllegalStateException {
        AppLog.i(SHELL_LABEL, "tryLoadHarmony start", new Object[0]);
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "tryLoadHarmony");
        if (context != null) {
            if (bundleMgrImpl == null) {
                bundleMgrImpl = new BundleMgrBridge();
            }
            if (this.receive == null) {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "attach application");
                this.receive = new ApplicationChangeReceive();
                BundleInfo attachApplication = bundleMgrImpl.attachApplication(context.getPackageName(), this.receive);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "attach application");
                if (attachApplication == null) {
                    String str = "failed to attach Application";
                    if (Parameters.getInt(SAMGR_CORESA_INITREADY, 0) == 0) {
                        str = str + ", the coreSa are not ready yet, need to wait until coreSa are readybefore starting the application";
                    }
                    throw new IllegalStateException(str);
                }
                HarmonyApplication.getInstance().setBundleInfo(attachApplication);
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "KeyStore install");
                HarmonyKeyStoreProvider.install(attachApplication.isMultiFrameworkBundle() ? 1 : 0);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "KeyStore install");
                loadHarmony(attachApplication);
                setLoaderDone(true);
                LOADER_LATCH.countDown();
            }
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "tryLoadHarmony");
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void loadHarmony(BundleInfo bundleInfo) {
        if (this.applicationContext == null || bundleInfo == null) {
            AppLog.w(SHELL_LABEL, "loadHarmony application context or bundleInfo is null", new Object[0]);
            return;
        }
        String moduleDir = bundleInfo.getModuleDir(bundleInfo.getEntryModuleName());
        if (moduleDir.isEmpty()) {
            AppLog.w(SHELL_LABEL, "loadHarmony entry hapSourceDir is empty", new Object[0]);
        } else {
            loadBegin(bundleInfo, moduleDir, bundleInfo.getEntryModuleName(), true);
        }
    }

    public void loadFeature(String str) {
        BundleInfo bundleInfo = HarmonyApplication.getInstance().getBundleInfo();
        if (this.applicationContext == null || bundleInfo == null) {
            AppLog.w(SHELL_LABEL, "loadFeature application context or bundleInfo is null", new Object[0]);
        } else if (str == null || !str.equals(bundleInfo.getEntryModuleName())) {
            String moduleDir = bundleInfo.getModuleDir(str);
            if (moduleDir.isEmpty()) {
                AppLog.w(SHELL_LABEL, "loadFeature hapSourceDir is empty %{public}s", str);
                return;
            }
            loadBegin(bundleInfo, moduleDir, str, false);
        } else {
            AppLog.i(SHELL_LABEL, "loadFeature this module is entry hap", new Object[0]);
        }
    }

    private void loadBegin(BundleInfo bundleInfo, String str, String str2, boolean z) {
        String cpuAbi = bundleInfo.getCpuAbi();
        boolean compressNativeLibs = bundleInfo.getCompressNativeLibs();
        boolean z2 = !compressNativeLibs && cpuAbi != null && !cpuAbi.isEmpty();
        boolean z3 = enableSplitClassLoader && !z;
        AppLog.i(SHELL_LABEL, "loadBegin %{public}s compress: %{public}b cpu: %{public}s split: %{public}b", str2, Boolean.valueOf(compressNativeLibs), cpuAbi, Boolean.valueOf(z3));
        PathClassLoader createHapClassLoader = z3 ? createHapClassLoader(str) : getBaseDexClassLoader();
        synchronized (this.classLoadLock) {
            if (!z3) {
                try {
                    createHapClassLoader.addDexPath(str);
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (z2) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(str + LIBS + cpuAbi);
                createHapClassLoader.addNativePath(arrayList);
            } else {
                addExtractedLibsPath(createHapClassLoader);
            }
            if (!z) {
                HarmonyApplication.getInstance().loadClass(createHapClassLoader, str2);
            }
        }
    }

    private PathClassLoader createHapClassLoader(String str) {
        return new PathClassLoader(str, ClassLoader.getSystemClassLoader().getParent());
    }

    private PathClassLoader getBaseDexClassLoader() {
        ClassLoader classLoader = this.applicationContext.getClassLoader();
        ClassLoader parent = classLoader.getParent();
        if (parent == null || !(parent instanceof PathClassLoader)) {
            AppLog.d(SHELL_LABEL, "getBaseDexClassLoader %{public}s", classLoader);
            if (classLoader instanceof PathClassLoader) {
                return (PathClassLoader) classLoader;
            }
            throw new IllegalStateException("not supported class loader");
        }
        AppLog.d(SHELL_LABEL, "getBaseDexClassLoader %{public}s", classLoader);
        AppLog.d(SHELL_LABEL, "getBaseDexClassLoader parent: %{public}s", parent);
        return (PathClassLoader) parent;
    }

    /* access modifiers changed from: private */
    public class ApplicationChangeReceive extends ShellApplicationCallback {
        private ApplicationChangeReceive() {
        }

        @Override // ohos.bundle.IShellApplication, ohos.bundle.ShellApplicationCallback
        public void onBundleUpdated(BundleInfo bundleInfo) {
            AppLog.i(HarmonyLoader.SHELL_LABEL, "onBundleUpdated %{public}s", bundleInfo);
            HarmonyApplication.getInstance().setBundleInfo(bundleInfo);
            HarmonyApplication.getInstance().getApplication().setBundleInfo(bundleInfo);
            HarmonyLoader.this.loadHarmony(bundleInfo);
            for (String str : HarmonyApplication.getInstance().getLoadedHapMap().keySet()) {
                HarmonyLoader.this.loadFeature(str);
            }
            HarmonyResources.setNewResourceState();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        ohos.appexecfwk.utils.AppLog.e(ohos.abilityshell.HarmonyLoader.SHELL_LABEL, "adjustLibPathOrder file path error", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00b3, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00b4, code lost:
        ohos.appexecfwk.utils.AppLog.e(ohos.abilityshell.HarmonyLoader.SHELL_LABEL, "adjustLibPathOrder error %{public}s ", r8.getMessage());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0090 */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00b3 A[ExcHandler: IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException (r8v3 'e' java.lang.Exception A[CUSTOM_DECLARE]), Splitter:B:15:0x0090] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void adjustLibPathOrder(java.lang.String r8, dalvik.system.PathClassLoader r9) {
        /*
        // Method dump skipped, instructions count: 196
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.abilityshell.HarmonyLoader.adjustLibPathOrder(java.lang.String, dalvik.system.PathClassLoader):void");
    }

    private void addExtractedLibsPath(PathClassLoader pathClassLoader) {
        String str = this.applicationContext.getApplicationInfo().nativeLibraryDir;
        if (str == null) {
            AppLog.e(SHELL_LABEL, "addExtractedLibsPath nativeLibraryDir is null", new Object[0]);
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        pathClassLoader.addNativePath(arrayList);
        AppLog.i(SHELL_LABEL, "addExtractedLibsPath addNativePath %{public}s to classloader success.", str);
        adjustLibPathOrder(str, pathClassLoader);
    }
}
