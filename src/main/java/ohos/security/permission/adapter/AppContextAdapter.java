package ohos.security.permission.adapter;

import android.content.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;

public class AppContextAdapter {
    private static final HiLogLabel LABEL = HiLogLabelUtil.ADAPTER.newHiLogLabel(TAG);
    private static final Object LOCK = new Object();
    private static final String TAG = "AppContextAdapter";
    private Context context;

    private AppContextAdapter() {
    }

    public static AppContextAdapter getInstance() {
        return ApplicationHolder.INSTANCE;
    }

    public void init(Object obj) {
        synchronized (LOCK) {
            if (!(obj instanceof Context)) {
                HiLog.error(LABEL, "object param error", new Object[0]);
            } else {
                this.context = this.context == null ? ((Context) obj).getApplicationContext() : this.context;
            }
        }
    }

    public Context getContext() {
        Context context2 = this.context;
        if (context2 != null) {
            return context2;
        }
        throw new IllegalArgumentException("context is null");
    }

    /* access modifiers changed from: private */
    public static class ApplicationHolder {
        private static final AppContextAdapter INSTANCE = new AppContextAdapter();

        private ApplicationHolder() {
        }
    }
}
