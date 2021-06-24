package com.huawei.security.dpermission;

import android.content.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;

public class DPermissionInitializer {
    private static final HiLogLabel LABEL = HiLogLabelUtil.SERVICE.newHiLogLabel(TAG);
    private static final String TAG = "DPermissionInitializer";

    public void init(Object obj) {
    }

    private DPermissionInitializer() {
    }

    public static DPermissionInitializer getInstance() {
        return SingletonClassHolder.INSTANCE;
    }

    @Deprecated
    public void init(Context context) {
        HiLog.info(LABEL, "Starting init SA and Service begin, Deprecated method.", new Object[0]);
        ohos.security.dpermission.DPermissionInitializer.getInstance().init(context);
    }

    private static final class SingletonClassHolder {
        private static final DPermissionInitializer INSTANCE = new DPermissionInitializer();

        private SingletonClassHolder() {
        }
    }
}
