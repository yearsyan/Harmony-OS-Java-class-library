package ohos.app.dispatcher;

import ohos.app.dispatcher.threading.WorkerPoolConfig;

public class SpecWorkerPoolConfig implements WorkerPoolConfig {
    private static final int DEFAULT_CORE_THREAD_COUNT = 16;
    private static final long DEFAULT_KEEP_ALIVE_TIME = 50;
    private static final int DEFAULT_MAX_THREAD_COUNT = 32;
    private int coreThreadCount;
    private long keepAliveTime;
    private int maxThreadCount;

    public SpecWorkerPoolConfig() {
        this(32, 16);
    }

    public SpecWorkerPoolConfig(int i, int i2) {
        this(i, i2, DEFAULT_KEEP_ALIVE_TIME);
    }

    public SpecWorkerPoolConfig(int i, int i2, long j) {
        this.maxThreadCount = i;
        this.coreThreadCount = i2;
        this.keepAliveTime = j;
    }

    public SpecWorkerPoolConfig(long j) {
        this(32, 16, j);
    }

    @Override // ohos.app.dispatcher.threading.WorkerPoolConfig
    public int getMaxThreadCount() {
        return this.maxThreadCount;
    }

    @Override // ohos.app.dispatcher.threading.WorkerPoolConfig
    public int getCoreThreadCount() {
        return this.coreThreadCount;
    }

    @Override // ohos.app.dispatcher.threading.WorkerPoolConfig
    public long getKeepAliveTime() {
        return this.keepAliveTime;
    }
}
