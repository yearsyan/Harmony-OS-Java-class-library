package ohos.security.dpermission;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.security.permission.adapter.AppContextAdapter;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;

public class DPermissionInitializer {
    private static final HiLogLabel LABEL = HiLogLabelUtil.SERVICE.newHiLogLabel(TAG);
    private static final String TAG = "DPermissionInitializer";

    private DPermissionInitializer() {
    }

    public static DPermissionInitializer getInstance() {
        return SingletonClassHolder.INSTANCE;
    }

    public void init(Object obj) {
        HiLog.info(LABEL, "Starting init SA and Service begin", new Object[0]);
        if (obj == null) {
            HiLog.error(LABEL, "init: context is null.", new Object[0]);
            return;
        }
        try {
            AppContextAdapter.getInstance().init(obj);
            DPermissionDistributeService.getInstance().initialize();
        } catch (SecurityException unused) {
            HiLog.error(LABEL, "init: Add HwDPermissionService failed, encountered SecurityException.", new Object[0]);
        } catch (Exception unused2) {
            HiLog.error(LABEL, "init: Add HwDPermissionService failed, encountered Exception.", new Object[0]);
        }
    }

    private static final class SingletonClassHolder {
        private static final DPermissionInitializer INSTANCE = new DPermissionInitializer();

        private SingletonClassHolder() {
        }
    }
}
