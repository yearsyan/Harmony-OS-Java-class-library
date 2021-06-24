package com.huawei.ace.systemplugin.httpaccess;

import com.huawei.ace.runtime.ALog;
import java.lang.Thread;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.LongAdder;

public class HttpThreadFactory implements ThreadFactory {
    private static final Object LOCK = new Object();
    private static final int MAX_THREAD_NUM = 1;
    private static final String TAG = "HttpThreadFactory";
    private LongAdder incrementer = new LongAdder();
    private String threadNamePrefix;

    public HttpThreadFactory(String str) {
        this.threadNamePrefix = str + "_";
    }

    public Thread newThread(Runnable runnable) {
        synchronized (LOCK) {
            if (this.incrementer.intValue() > 1) {
                ALog.e(TAG, "thread exceed max number!");
                return null;
            }
            Thread thread = new Thread(runnable, this.threadNamePrefix + System.currentTimeMillis());
            thread.setUncaughtExceptionHandler(new HttpUncaughtExceptionHandler());
            this.incrementer.increment();
            return thread;
        }
    }

    private static class HttpUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        private HttpUncaughtExceptionHandler() {
        }

        public void uncaughtException(Thread thread, Throwable th) {
            ALog.e(HttpThreadFactory.TAG, "thread : " + thread.getName() + " caught exception!");
        }
    }
}
