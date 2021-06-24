package ohos.distributedschedule.scenario;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.sysability.samgr.SysAbilityManager;
import ohos.utils.system.safwk.java.SystemAbilityDefinition;

final class DistributedScenarioManagerClient {
    private static final int CODE_DTB_SCENARIO_MGR_BASE = 1;
    private static final int CODE_SUBSCRIBE = 1;
    private static final int CODE_UNSUBSCRIBE = 2;
    private static final int DEATH_RECIPIENT_FLAG = 0;
    private static final DistributedScenarioManagerClient INSTANCE = new DistributedScenarioManagerClient();
    private static final String IPC_DESCRIPTOR = "OHOS.DistributedSchedule.DtbContinueMgr";
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218109952, TAG);
    private static final int RESUME_DELAY_TIME = 2000;
    private static final int RETRY_TIME = 5;
    private static final String TAG = "DistributedScenarioManagerClient";
    private final IRemoteObject.DeathRecipient deathRecipient = new DistributedScenarioManagerDeathRecipient();
    private final Object remoteLock = new Object();
    private IRemoteObject remoteObj;
    private final Map<IScenarioSubscriber, SubscribeInfo> subscriberCache = new HashMap();

    static DistributedScenarioManagerClient getInstance() {
        return INSTANCE;
    }

    /* access modifiers changed from: package-private */
    public boolean subscribe(ScenarioSubscriber scenarioSubscriber) {
        if (scenarioSubscriber == null || scenarioSubscriber.getSubscribeInfo() == null) {
            HiLog.warn(LABEL, "subscribe with illegal subscriber.", new Object[0]);
            return false;
        }
        IRemoteObject remote = getRemote();
        if (remote != null) {
            return lambda$resumeSubscribers$0$DistributedScenarioManagerClient(remote, scenarioSubscriber.getSubscriber(), scenarioSubscriber.getSubscribeInfo());
        }
        HiLog.debug(LABEL, "subscribe remote is null.", new Object[0]);
        return false;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:23|24) */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x007d, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        ohos.hiviewdfx.HiLog.debug(ohos.distributedschedule.scenario.DistributedScenarioManagerClient.LABEL, "unsubscribe remote exception", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0090, code lost:
        r1.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0096, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x007f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unsubscribe(ohos.distributedschedule.scenario.ScenarioSubscriber r6) {
        /*
        // Method dump skipped, instructions count: 151
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.distributedschedule.scenario.DistributedScenarioManagerClient.unsubscribe(ohos.distributedschedule.scenario.ScenarioSubscriber):void");
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x006f, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        ohos.hiviewdfx.HiLog.debug(ohos.distributedschedule.scenario.DistributedScenarioManagerClient.LABEL, "subscribe remote exception", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x007c, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0082, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:22:0x0071 */
    /* renamed from: subscribeInner */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean lambda$resumeSubscribers$0$DistributedScenarioManagerClient(ohos.rpc.IRemoteObject r6, ohos.distributedschedule.scenario.IScenarioSubscriber r7, ohos.distributedschedule.scenario.SubscribeInfo r8) {
        /*
        // Method dump skipped, instructions count: 131
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.distributedschedule.scenario.DistributedScenarioManagerClient.lambda$resumeSubscribers$0$DistributedScenarioManagerClient(ohos.rpc.IRemoteObject, ohos.distributedschedule.scenario.IScenarioSubscriber, ohos.distributedschedule.scenario.SubscribeInfo):boolean");
    }

    /* access modifiers changed from: package-private */
    public void resumeSubscribers() {
        resetRemote();
        int i = 5;
        while (true) {
            int i2 = i - 1;
            if (i > 0) {
                HiLog.debug(LABEL, "waiting dtb scenario mgr restart.", new Object[0]);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException unused) {
                    HiLog.warn(LABEL, "resume subscriber interrupted exception.", new Object[0]);
                }
                IRemoteObject remote = getRemote();
                if (remote != null) {
                    synchronized (this.subscriberCache) {
                        this.subscriberCache.forEach(new BiConsumer(remote) {
                            /* class ohos.distributedschedule.scenario.$$Lambda$DistributedScenarioManagerClient$IcmOXyHVWVhbckt7SSBhAnD7EU */
                            private final /* synthetic */ IRemoteObject f$1;

                            {
                                this.f$1 = r2;
                            }

                            @Override // java.util.function.BiConsumer
                            public final void accept(Object obj, Object obj2) {
                                DistributedScenarioManagerClient.this.lambda$resumeSubscribers$0$DistributedScenarioManagerClient(this.f$1, (IScenarioSubscriber) obj, (SubscribeInfo) obj2);
                            }
                        });
                    }
                    return;
                }
                i = i2;
            } else {
                HiLog.debug(LABEL, "resume subscriber finally failed.", new Object[0]);
                return;
            }
        }
    }

    private IRemoteObject getRemote() {
        synchronized (this.remoteLock) {
            if (this.remoteObj != null) {
                return this.remoteObj;
            }
            this.remoteObj = SysAbilityManager.getSysAbility(SystemAbilityDefinition.DISTRIBUTED_SCENARIO_MGR_SA_ID);
            if (this.remoteObj == null) {
                HiLog.warn(LABEL, "getSysAbility failed.", new Object[0]);
                return this.remoteObj;
            }
            this.remoteObj.addDeathRecipient(this.deathRecipient, 0);
            HiLog.info(LABEL, "getSysAbility successfully.", new Object[0]);
            return this.remoteObj;
        }
    }

    private void resetRemote() {
        synchronized (this.remoteLock) {
            this.remoteObj = null;
        }
    }

    private void updateSubscriberCacheInfo(IScenarioSubscriber iScenarioSubscriber, SubscribeInfo subscribeInfo) {
        synchronized (this.subscriberCache) {
            this.subscriberCache.put(iScenarioSubscriber, new SubscribeInfo(subscribeInfo));
        }
    }

    private void removeSubscriberCacheInfo(IScenarioSubscriber iScenarioSubscriber) {
        synchronized (this.subscriberCache) {
            this.subscriberCache.remove(iScenarioSubscriber);
        }
    }

    private DistributedScenarioManagerClient() {
    }
}
