package ohos.event.commonevent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* access modifiers changed from: package-private */
public class CesResultReceiver extends BroadcastReceiver {
    private long nativeSubscriber;

    CesResultReceiver(long j) {
        this.nativeSubscriber = j;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            CesAdapterManager.getInstance().onResultReceiverReceive(this.nativeSubscriber, intent, getResultCode(), getResultData());
            CesAdapterManager.getInstance().releaseNativeSubscriber(this.nativeSubscriber);
            this.nativeSubscriber = 0;
        }
    }
}
