package ohos.poweradapter;

import android.os.IPowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class PowerAdapter {
    private static final int DBG = 218114307;
    private static final HiLogLabel LABEL = new HiLogLabel(3, DBG, TAG);
    private static final String TAG = "PowerAdapter";

    public static boolean isInteractive() {
        boolean z;
        IPowerManager asInterface = IPowerManager.Stub.asInterface(ServiceManager.getService("power"));
        if (asInterface == null) {
            HiLog.error(LABEL, "PowerManager service is null", new Object[0]);
            return false;
        }
        try {
            z = asInterface.isInteractive();
        } catch (RemoteException e) {
            HiLog.error(LABEL, "isInteractive exception %{public}s", e.getMessage());
            z = false;
        }
        HiLog.debug(LABEL, "isInteractive is %{public}b", Boolean.valueOf(z));
        return z;
    }
}
