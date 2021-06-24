package ohos.hiviewdfx;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

public final class FaultLogger {
    public static final int FAULT_TYPE_APP_FREEZE = 4;
    public static final int FAULT_TYPE_CPP_CRASH = 2;
    public static final int FAULT_TYPE_JAVA_CRASH = 1;
    public static final int FAULT_TYPE_JS_CRASH = 3;
    public static final int FAULT_TYPE_NO_SPECIFIC = 0;
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218115344, "FaultLogger");

    /* access modifiers changed from: private */
    public static native void nativeDestoryFaultLogQueryResult(long j);

    /* access modifiers changed from: private */
    public static native void nativeDestroyFaultLogInfo(long j);

    /* access modifiers changed from: private */
    public static native FaultLogInfo nativeFaultLogQueryResultGetNext(long j);

    /* access modifiers changed from: private */
    public static native boolean nativeFaultLogQueryResultHasNext(long j);

    /* access modifiers changed from: private */
    public static native FileDescriptor nativeGetFaultLogFileDescriptor(long j);

    private static native long nativeQuerySelfFaultLog(int i, int i2);

    static {
        System.loadLibrary("faultlogger_jni.z");
    }

    public static Optional<FaultLogQueryResult> querySelfFaultLog(int i, int i2) {
        long nativeQuerySelfFaultLog = nativeQuerySelfFaultLog(i, i2);
        return Optional.ofNullable(nativeQuerySelfFaultLog > 0 ? new FaultLogQueryResult(nativeQuerySelfFaultLog) : null);
    }

    public static final class FaultLogInfo {
        private FileInputStream logFileStream = null;
        private long nativeHandle = 0;
        private int pid = -1;
        private String reason = "";
        private String summary = "";
        private long timestamp = 0;
        private int type = 0;
        private int uid = -1;

        public int getPid() {
            return this.pid;
        }

        public int getUid() {
            return this.uid;
        }

        public int getFaultType() {
            return this.type;
        }

        public long getTimestamp() {
            return this.timestamp;
        }

        public String getReason() {
            return this.reason;
        }

        public String getSummary() {
            return this.summary;
        }

        public Optional<FileInputStream> getLogFileStream() {
            FileDescriptor nativeGetFaultLogFileDescriptor = FaultLogger.nativeGetFaultLogFileDescriptor(this.nativeHandle);
            if (nativeGetFaultLogFileDescriptor != null && nativeGetFaultLogFileDescriptor.valid()) {
                this.logFileStream = new FileInputStream(nativeGetFaultLogFileDescriptor);
            }
            return Optional.ofNullable(this.logFileStream);
        }

        public FaultLogInfo() {
        }

        public FaultLogInfo(int i, int i2, int i3, long j, long j2, String str, String str2) {
            this.pid = i;
            this.uid = i2;
            this.type = i3;
            this.timestamp = j;
            this.nativeHandle = j2;
            this.reason = str;
            this.summary = str2;
        }

        /* access modifiers changed from: package-private */
        public void destroy() {
            FileInputStream fileInputStream = this.logFileStream;
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                    this.logFileStream = null;
                } catch (IOException unused) {
                    HiLog.error(FaultLogger.LABEL, "Failed to close log file stream.", new Object[0]);
                }
            }
            long j = this.nativeHandle;
            if (j > 0) {
                FaultLogger.nativeDestroyFaultLogInfo(j);
                this.nativeHandle = 0;
            }
        }
    }

    public static final class FaultLogQueryResult {
        private boolean hasDestroyed = false;
        private ArrayList<FaultLogInfo> infos = new ArrayList<>();
        private long nativeHandle = 0;

        public boolean hasNext() {
            long j = this.nativeHandle;
            if (j == 0) {
                return false;
            }
            return FaultLogger.nativeFaultLogQueryResultHasNext(j);
        }

        public Optional<FaultLogInfo> next() {
            long j = this.nativeHandle;
            FaultLogInfo nativeFaultLogQueryResultGetNext = j > 0 ? FaultLogger.nativeFaultLogQueryResultGetNext(j) : null;
            if (nativeFaultLogQueryResultGetNext != null) {
                this.infos.add(nativeFaultLogQueryResultGetNext);
            }
            return Optional.ofNullable(nativeFaultLogQueryResultGetNext);
        }

        public void destroy() {
            if (!this.hasDestroyed) {
                this.hasDestroyed = true;
                Iterator<FaultLogInfo> it = this.infos.iterator();
                while (it.hasNext()) {
                    it.next().destroy();
                }
                long j = this.nativeHandle;
                if (j > 0) {
                    FaultLogger.nativeDestoryFaultLogQueryResult(j);
                }
                this.nativeHandle = 0;
            }
        }

        /* access modifiers changed from: protected */
        public void finalize() throws Throwable {
            if (!this.hasDestroyed) {
                try {
                    destroy();
                } finally {
                    super.finalize();
                }
            }
        }

        public FaultLogQueryResult() {
        }

        public FaultLogQueryResult(long j) {
            this.nativeHandle = j;
        }
    }
}
