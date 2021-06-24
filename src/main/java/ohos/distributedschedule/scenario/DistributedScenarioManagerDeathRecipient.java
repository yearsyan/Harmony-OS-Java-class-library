package ohos.distributedschedule.scenario;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

/* access modifiers changed from: package-private */
public final class DistributedScenarioManagerDeathRecipient implements IRemoteObject.DeathRecipient {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218109952, TAG);
    private static final String TAG = "DistributedScenarioManagerDeathRecipient";

    DistributedScenarioManagerDeathRecipient() {
    }

    @Override // ohos.rpc.IRemoteObject.DeathRecipient
    public void onRemoteDied() {
        HiLog.info(LABEL, "receive death recipient.", new Object[0]);
        DistributedScenarioManagerClient.getInstance().resumeSubscribers();
    }
}
