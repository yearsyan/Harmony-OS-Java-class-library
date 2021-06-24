package ohos.event.intentagent;

import android.app.PendingIntent;
import android.content.IIntentSender;
import ohos.rpc.IPCAdapter;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;

public final class IntentAgentAdapterUtils {
    public static IntentAgent getIntentAgent(PendingIntent pendingIntent) {
        if (pendingIntent == null) {
            return null;
        }
        return new IntentAgent(pendingIntent);
    }

    public static PendingIntent getPendingIntent(IntentAgent intentAgent) {
        if (intentAgent == null) {
            return null;
        }
        Object object = intentAgent.getObject();
        if (!(object instanceof PendingIntent)) {
            return null;
        }
        return (PendingIntent) object;
    }

    public static boolean writeToParcel(IntentAgent intentAgent, MessageParcel messageParcel) {
        IIntentSender target;
        IRemoteObject orElse;
        if (intentAgent == null || messageParcel == null) {
            return false;
        }
        Object object = intentAgent.getObject();
        if (!(object instanceof PendingIntent) || (target = ((PendingIntent) object).getTarget()) == null || (orElse = IPCAdapter.translateToIRemoteObject(target.asBinder()).orElse(null)) == null) {
            return false;
        }
        return messageParcel.writeRemoteObject(orElse);
    }

    private IntentAgentAdapterUtils() {
    }
}
