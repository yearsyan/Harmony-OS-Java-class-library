package ohos.event.notification;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.tools.C0000Bytrace;
import ohos.utils.Parcel;

public class ZidaneNotificationManager {
    private static final HiLogLabel LABEL = new HiLogLabel(3, EventConstant.NOTIFICATION_DOMAIN, TAG);
    private static final String TAG = "ZidaneNotificationManager";

    private static native void nativePublishRemoteNotification(Parcel parcel, String str);

    static {
        try {
            HiLog.info(LABEL, "inner Load libans_client_jni.z.so", new Object[0]);
            System.loadLibrary("ans_client_jni.z");
        } catch (UnsatisfiedLinkError unused) {
            HiLog.error(LABEL, "ERROR: Could not load libans_client_jni.z.so ", new Object[0]);
        }
    }

    public void publishNotification(String str, NotificationRequest notificationRequest) {
        HiLog.debug(LABEL, "ZidaneNotificationManager::publishNotification ans not support yet", new Object[0]);
    }

    public void cancelNotification(String str) {
        HiLog.debug(LABEL, "ZidaneNotificationManager::cancelNotification ans not support yet", new Object[0]);
    }

    public void cancelAllNotifications(String str, int i) {
        HiLog.debug(LABEL, "ZidaneNotificationManager::cancelAllNotifications ans not support yet", new Object[0]);
    }

    /* access modifiers changed from: package-private */
    public void publishRemoteNotification(NotificationRequest notificationRequest, String str) {
        Parcel create = Parcel.create();
        if (!notificationRequest.marshalling(create)) {
            HiLog.error(LABEL, "ZidaneNotificationManager:publishRemoteNotification invalid remote notification", new Object[0]);
            create.reclaim();
            return;
        }
        C0000Bytrace.startTrace(2, "publishnotificationjni");
        nativePublishRemoteNotification(create, str);
        C0000Bytrace.finishTrace(2, "publishnotificationjni");
        create.reclaim();
    }
}
