package ohos.security.permission;

import android.os.Bundle;
import com.huawei.security.dpermission.permissionusingremind.OnPermissionUsingReminder;
import java.util.HashSet;
import java.util.Set;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.RemoteException;
import ohos.security.permission.adapter.DistributedPermissionManagerAdapter;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;

final class PermissionReminderInner {
    private static final String BUNDLE_LABEL_KEY = "bundle_babel_key";
    private static final String BUNDLE_NAME_KEY = "bundle_name_key";
    private static final int DEFAULT_SIZE = 16;
    private static final String DEVICE_ID_KEY = "device_id_key";
    private static final String DEVICE_LABEL_KEY = "device_label_key";
    private static final HiLogLabel LABEL = HiLogLabelUtil.INNER_KIT.newHiLogLabel(TAG);
    private static final Object LOCK = new Object();
    private static final String PERMISSION_NAME_KEY = "permission_name_key";
    private static final Set<OnUsingPermissionReminder> REMINDER_SET = new HashSet(16);
    private static final int SUCCESS_CODE = 0;
    private static final String TAG = "PermissionReminderInner";
    private static CallbackAdapter callbackAdapter;

    private PermissionReminderInner() {
    }

    static int registerUsingPermissionReminder(OnUsingPermissionReminder onUsingPermissionReminder) {
        synchronized (LOCK) {
            if (REMINDER_SET.contains(onUsingPermissionReminder)) {
                return 0;
            }
            if (callbackAdapter == null) {
                callbackAdapter = new CallbackAdapter();
            }
            int registerOnPermissionUsingReminder = DistributedPermissionManagerAdapter.getInstance().registerOnPermissionUsingReminder(callbackAdapter);
            if (registerOnPermissionUsingReminder == 0) {
                REMINDER_SET.add(onUsingPermissionReminder);
            }
            return registerOnPermissionUsingReminder;
        }
    }

    static void unregisterUsingPermissionReminder(OnUsingPermissionReminder onUsingPermissionReminder) {
        synchronized (LOCK) {
            if (REMINDER_SET.contains(onUsingPermissionReminder)) {
                if (callbackAdapter != null) {
                    REMINDER_SET.remove(onUsingPermissionReminder);
                    if (REMINDER_SET.isEmpty()) {
                        DistributedPermissionManagerAdapter.getInstance().unregisterOnPermissionUsingReminder(callbackAdapter);
                        callbackAdapter = null;
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static PermissionReminderInfo assembleReminderInfoFromBundle(Bundle bundle) {
        PermissionReminderInfo permissionReminderInfo = new PermissionReminderInfo();
        permissionReminderInfo.setDeviceId(bundle.getString(DEVICE_ID_KEY, ""));
        permissionReminderInfo.setDeviceLabel(bundle.getString(DEVICE_LABEL_KEY, ""));
        permissionReminderInfo.setBundleName(bundle.getString(BUNDLE_NAME_KEY, ""));
        permissionReminderInfo.setBundleLabel(bundle.getString(BUNDLE_LABEL_KEY, ""));
        permissionReminderInfo.setPermName(bundle.getString(PERMISSION_NAME_KEY, ""));
        return permissionReminderInfo;
    }

    /* access modifiers changed from: private */
    public static final class CallbackAdapter implements OnPermissionUsingReminder {
        private CallbackAdapter() {
        }

        public void onPermissionStartUsing(Bundle bundle) {
            if (bundle != null) {
                PermissionReminderInfo assembleReminderInfoFromBundle = PermissionReminderInner.assembleReminderInfoFromBundle(bundle);
                synchronized (PermissionReminderInner.LOCK) {
                    for (OnUsingPermissionReminder onUsingPermissionReminder : PermissionReminderInner.REMINDER_SET) {
                        try {
                            onUsingPermissionReminder.startUsingPermission(assembleReminderInfoFromBundle);
                        } catch (RemoteException unused) {
                            HiLog.error(PermissionReminderInner.LABEL, "Failed to notify start using because of RemoteException", new Object[0]);
                        }
                    }
                }
            }
        }

        public void onPermissionStopUsing(Bundle bundle) {
            if (bundle != null) {
                PermissionReminderInfo assembleReminderInfoFromBundle = PermissionReminderInner.assembleReminderInfoFromBundle(bundle);
                synchronized (PermissionReminderInner.LOCK) {
                    for (OnUsingPermissionReminder onUsingPermissionReminder : PermissionReminderInner.REMINDER_SET) {
                        try {
                            onUsingPermissionReminder.stopUsingPermission(assembleReminderInfoFromBundle);
                        } catch (RemoteException unused) {
                            HiLog.error(PermissionReminderInner.LABEL, "Failed to notify stop using because of RemoteException", new Object[0]);
                        }
                    }
                }
            }
        }
    }
}
