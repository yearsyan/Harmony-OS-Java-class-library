package ohos.event;

import android.content.Context;
import ohos.event.commonevent.CesAdapterManager;

public class CommonEventMgrAdapter {
    public static void init(Context context) {
        CesAdapterManager.getInstance().init(context);
    }
}
