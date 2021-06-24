package ohos.event;

import android.content.Context;
import ohos.event.notification.AnsAdapterManager;

public class NotificationMgrAdapter {
    public static void init(Context context) {
        AnsAdapterManager.initAnsNative();
    }

    private NotificationMgrAdapter() {
    }
}
