package ohos.security.permission.adapter;

import android.common.HwFrameworkSecurityPartsFactory;
import com.huawei.security.dpermission.DistributedPermissionManager;
import com.huawei.security.dpermission.permissionusingremind.OnPermissionUsingReminder;
import ohos.hiviewdfx.HiLogLabel;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;

public class DistributedPermissionManagerAdapter {
    public static final int DENIED = -1;
    public static final int GRANTED = 0;
    private static final HiLogLabel LABEL = HiLogLabelUtil.ADAPTER.newHiLogLabel(TAG);
    private static final String TAG = "DistributedPermissionManagerAdapter";

    private DistributedPermissionManagerAdapter() {
    }

    /* access modifiers changed from: private */
    public static final class SingletonClassHolder {
        private static final DistributedPermissionManagerAdapter INSTANCE = new DistributedPermissionManagerAdapter();

        private SingletonClassHolder() {
        }
    }

    public static DistributedPermissionManagerAdapter getInstance() {
        return SingletonClassHolder.INSTANCE;
    }

    public int registerOnPermissionUsingReminder(OnPermissionUsingReminder onPermissionUsingReminder) {
        return DistributedPermissionManager.getDefault().registerOnPermissionUsingReminder(onPermissionUsingReminder);
    }

    public int unregisterOnPermissionUsingReminder(OnPermissionUsingReminder onPermissionUsingReminder) {
        return DistributedPermissionManager.getDefault().unregisterOnPermissionUsingReminder(onPermissionUsingReminder);
    }

    public int checkDPermission(int i, String str) {
        return HwFrameworkSecurityPartsFactory.getInstance().getDPermissionManager().checkDPermission(i, str);
    }
}
