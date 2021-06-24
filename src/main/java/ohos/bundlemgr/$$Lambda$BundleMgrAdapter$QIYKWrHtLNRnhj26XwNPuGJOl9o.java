package ohos.bundlemgr;

import java.lang.Thread;
import ohos.appexecfwk.utils.AppLog;

/* renamed from: ohos.bundlemgr.-$$Lambda$BundleMgrAdapter$QIYKWrHtLNRnhj26XwNPuGJOl9o  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$BundleMgrAdapter$QIYKWrHtLNRnhj26XwNPuGJOl9o implements Thread.UncaughtExceptionHandler {
    public static final /* synthetic */ $$Lambda$BundleMgrAdapter$QIYKWrHtLNRnhj26XwNPuGJOl9o INSTANCE = new $$Lambda$BundleMgrAdapter$QIYKWrHtLNRnhj26XwNPuGJOl9o();

    private /* synthetic */ $$Lambda$BundleMgrAdapter$QIYKWrHtLNRnhj26XwNPuGJOl9o() {
    }

    public final void uncaughtException(Thread thread, Throwable th) {
        AppLog.e(BundleMgrAdapter.BMS_ADAPTER_LABEL, "thread %{public}s exception %{public}s", thread.getName(), th.getMessage());
    }
}
