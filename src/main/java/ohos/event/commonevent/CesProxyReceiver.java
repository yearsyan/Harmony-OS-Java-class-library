package ohos.event.commonevent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ohos.tools.C0000Bytrace;

/* access modifiers changed from: package-private */
public class CesProxyReceiver extends BroadcastReceiver {
    CesProxyReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            C0000Bytrace.startTrace(2, "JavaSubscriberOnReceive");
            CesAdapterManager.getInstance().onReceiveCommonEvent(intent, getResultCode(), getResultData(), isOrderedBroadcast(), hashCode());
            C0000Bytrace.finishTrace(2, "JavaSubscriberOnReceive");
        }
    }
}
