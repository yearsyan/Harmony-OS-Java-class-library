package ohos.hiviewdfx;

import android.app.AppGlobals;
import android.app.Application;
import android.os.Debug;
import android.os.Environment;
import ark.system.Debugger;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DebugImpl {
    private static final HiLogLabel DEBUG_LABEL = new HiLogLabel(3, 218115338, "DEBUG_JAVA");
    private static final String DEFAULT_TRACE_BODY = "dmtrace";
    private static final String DEFAULT_TRACE_EXTENSION = ".trace";
    private static final int DESTRUCTION = 2;
    private static final int HEAP = 1;
    private static final int LOCAL = 0;
    private static final int OTHER = 2;
    private static final int REMOTE = 1;
    private static final int RUNTIME = 0;

    public static native long getNativeHeapAllocatedSize();

    public static native long getNativeHeapFreeSize();

    public static native long getNativeHeapSize();

    public static native long threadCpuTimeNanos();

    static {
        System.loadLibrary("debug_jni.z");
    }

    private DebugImpl() {
    }

    public static int getProcessPssSum() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return memoryInfo.getTotalPss();
    }

    public static int getProcessPrivateDirtySum() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return memoryInfo.getTotalPrivateDirty();
    }

    public static int getProcessPss(int i) {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        if (i == 0) {
            return memoryInfo.dalvikPss;
        }
        if (i == 1) {
            return memoryInfo.nativePss;
        }
        if (i != 2) {
            return 0;
        }
        return memoryInfo.otherPss;
    }

    public static int getProcessSwappablePss(int i) {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        if (i == 0) {
            return memoryInfo.dalvikSwappablePss;
        }
        if (i == 1) {
            return memoryInfo.nativeSwappablePss;
        }
        if (i != 2) {
            return 0;
        }
        return memoryInfo.otherSwappablePss;
    }

    public static int getProcessSwappablePssSum() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return memoryInfo.getTotalSwappablePss();
    }

    public static int getProcessRss(int i) {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        if (i == 0) {
            return memoryInfo.dalvikRss;
        }
        if (i == 1) {
            return memoryInfo.nativeRss;
        }
        if (i != 2) {
            return 0;
        }
        return memoryInfo.otherRss;
    }

    public static int getProcessRssSum() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return memoryInfo.getTotalRss();
    }

    public static int getProcessPrivateDirty(int i) {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        if (i == 0) {
            return memoryInfo.dalvikPrivateDirty;
        }
        if (i == 1) {
            return memoryInfo.nativePrivateDirty;
        }
        if (i != 2) {
            return 0;
        }
        return memoryInfo.otherPrivateDirty;
    }

    public static int getProcessSharedDirty(int i) {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        if (i == 0) {
            return memoryInfo.dalvikSharedDirty;
        }
        if (i == 1) {
            return memoryInfo.nativeSharedDirty;
        }
        if (i != 2) {
            return 0;
        }
        return memoryInfo.otherSharedDirty;
    }

    public static int getProcessSharedDirtySum() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return memoryInfo.getTotalSharedDirty();
    }

    public static int getProcessPrivateClean(int i) {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        if (i == 0) {
            return memoryInfo.dalvikPrivateClean;
        }
        if (i == 1) {
            return memoryInfo.nativePrivateClean;
        }
        if (i != 2) {
            return 0;
        }
        return memoryInfo.otherPrivateClean;
    }

    public static int getProcessPrivateCleanSum() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return memoryInfo.getTotalPrivateClean();
    }

    public static int getProcessSharedClean(int i) {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        if (i == 0) {
            return memoryInfo.dalvikSharedClean;
        }
        if (i == 1) {
            return memoryInfo.nativeSharedClean;
        }
        if (i != 2) {
            return 0;
        }
        return memoryInfo.otherSharedClean;
    }

    public static int getProcessSharedCleanSum() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return memoryInfo.getTotalSharedClean();
    }

    public static int getProcessSwappedOut(int i) {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        if (i == 0) {
            return memoryInfo.dalvikSwappedOut;
        }
        if (i == 1) {
            return memoryInfo.nativeSwappedOut;
        }
        if (i != 2) {
            return 0;
        }
        return memoryInfo.otherSwappedOut;
    }

    public static int getProcessSwappedOutSum() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return memoryInfo.getTotalSwappedOut();
    }

    public static int getProcessSwappedOutPss(int i) {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        if (i == 0) {
            return memoryInfo.dalvikSwappedOutPss;
        }
        if (i == 1) {
            return memoryInfo.nativeSwappedOutPss;
        }
        if (i != 2) {
            return 0;
        }
        return memoryInfo.otherSwappedOutPss;
    }

    public static int getProcessSwappedOutPssSum() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return memoryInfo.getTotalSwappedOutPss();
    }

    public static int getIpcCount(int i) {
        if (i == 0) {
            return Debug.getBinderLocalObjectCount();
        }
        if (i == 1) {
            return Debug.getBinderProxyObjectCount();
        }
        if (i != 2) {
            return 0;
        }
        return Debug.getBinderDeathObjectCount();
    }

    public static int getIpcSentCount() {
        return Debug.getBinderSentTransactions();
    }

    public static int getIpcReceivedCount() {
        return Debug.getBinderReceivedTransactions();
    }

    public static String getMemoryStatistic(String str) {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return memoryInfo.getMemoryStat(str);
    }

    public static Map<String, String> getMemoryStatistics() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return memoryInfo.getMemoryStats();
    }

    public static void functionTraceBegin() {
        Debugger.startMethodTracing(fixTracePath(null), 0, 0, false, 0);
    }

    public static void functionTraceBegin(String str) {
        functionTraceBegin(str, 0, 0);
    }

    public static void functionTraceBegin(String str, int i) {
        functionTraceBegin(str, i, 0);
    }

    public static void functionTraceBegin(String str, int i, int i2) {
        Debugger.startMethodTracing(fixTracePath(str), i, i2, false, 0);
    }

    private static String fixTracePath(String str) {
        File file;
        if (str == null || str.length() == 0 || str.charAt(0) != '/') {
            Application initialApplication = AppGlobals.getInitialApplication();
            if (initialApplication != null) {
                file = initialApplication.getExternalFilesDir(null);
            } else {
                file = Environment.getDataDirectory();
            }
            if (str != null) {
                try {
                    if (str.length() != 0) {
                        str = new File(file, str).getCanonicalPath();
                    }
                } catch (IOException unused) {
                    HiLog.error(DEBUG_LABEL, "fixTracePath error by IOexception", new Object[0]);
                    return str;
                }
            }
            str = new File(file, DEFAULT_TRACE_BODY).getCanonicalPath();
        }
        if (str.endsWith(DEFAULT_TRACE_EXTENSION)) {
            return str;
        }
        return str + DEFAULT_TRACE_EXTENSION;
    }

    public static void functionTraceSamplingBegin(String str, int i, int i2) {
        Debugger.startMethodTracing(fixTracePath(str), i, 0, true, i2);
    }

    public static void functionTraceEnd() {
        Debug.stopMethodTracing();
    }

    public static void dumpHeapFile(String str) throws IOException {
        Debugger.dumpHprofData(str, null);
    }

    public static String getRuntimeStatistic(String str) {
        return Debugger.getRuntimeStat("art." + str);
    }

    public static Map<String, String> getRuntimeStatistics() {
        Map<String, String> runtimeStats = Debug.getRuntimeStats();
        HashMap hashMap = new HashMap();
        for (Map.Entry<String, String> entry : runtimeStats.entrySet()) {
            hashMap.put(entry.getKey().substring(4), entry.getValue());
        }
        return hashMap;
    }

    public static void connectToDebugger() {
        Debug.waitForDebugger();
    }

    public static boolean getDebuggerConnectStatus() {
        return Debug.isDebuggerConnected();
    }

    public static boolean isConnectingToDebugger() {
        return Debug.waitingForDebugger();
    }

    public static int getCountofLoadClasses() {
        return Debugger.getLoadedClassCount();
    }

    public static void dumpLoadClasses(int i) {
        Debugger.printLoadedClasses(i);
    }

    public static void emulatorTraceBegin() {
        Debug.startNativeTracing();
    }

    public static void emulatorTraceEnd() {
        Debug.stopNativeTracing();
    }

    public static void emulatorTraceEnable() {
        Debugger.startEmulatorTracing();
    }

    public static void attachAgent(String str, String str2, ClassLoader classLoader) throws IOException {
        Preconditions.checkNotNull(str);
        Preconditions.checkArgument(!str.contains("="));
        if (str2 == null) {
            Debugger.attachAgent(str, classLoader);
            return;
        }
        Debugger.attachAgent(str + "=" + str2, classLoader);
    }
}
