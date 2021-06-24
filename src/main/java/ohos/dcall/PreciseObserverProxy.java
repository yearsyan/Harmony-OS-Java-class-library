package ohos.dcall;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import ohos.dcall.DistributedCall;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.PacMap;

public class PreciseObserverProxy {
    private static final HiLogLabel TAG = new HiLogLabel(3, 218111744, "PreciseObserverProxy");
    private static volatile PreciseObserverProxy sInstance;
    private final Map<Integer, DistributedCall> mDistributedCalls = new ConcurrentHashMap();
    private EventHandler mEeventHandler = new EventHandler(EventRunner.create(true));
    private final Map<Integer, List<PreciseObserverRecord>> mPreciseObserverRecordsByCallId = new HashMap();

    private PreciseObserverProxy() {
    }

    public static PreciseObserverProxy getInstance() {
        if (sInstance == null) {
            synchronized (PreciseObserverProxy.class) {
                if (sInstance == null) {
                    sInstance = new PreciseObserverProxy();
                }
            }
        }
        return sInstance;
    }

    public int registerPreciseObserver(DistributedCall distributedCall, DistributedCall.PreciseObserver preciseObserver) {
        return registerPreciseObserver(distributedCall, preciseObserver, this.mEeventHandler);
    }

    private int registerPreciseObserver(DistributedCall distributedCall, DistributedCall.PreciseObserver preciseObserver, EventHandler eventHandler) {
        if (distributedCall == null) {
            HiLog.error(TAG, "registerCallback: call is null", new Object[0]);
            return -1;
        }
        int callId = distributedCall.getCallId();
        HiLog.info(TAG, "registerPreciseObserver: callId = %{public}d", Integer.valueOf(callId));
        unregisterPreciseObserver(callId, preciseObserver);
        if (preciseObserver == null || eventHandler == null) {
            return -1;
        }
        List<PreciseObserverRecord> list = this.mPreciseObserverRecordsByCallId.get(Integer.valueOf(callId));
        if (list == null) {
            HiLog.info(TAG, "registerPreciseObserver: create new record", new Object[0]);
            list = new CopyOnWriteArrayList<>();
            this.mPreciseObserverRecordsByCallId.put(Integer.valueOf(callId), list);
        }
        HiLog.info(TAG, "registerPreciseObserver: add new record", new Object[0]);
        list.add(new PreciseObserverRecord(preciseObserver, eventHandler));
        return 0;
    }

    public int unregisterPreciseObserver(int i, DistributedCall.PreciseObserver preciseObserver) {
        HiLog.info(TAG, "unregisterPreciseObserver: callId = %{public}d", Integer.valueOf(i));
        if (preciseObserver != null) {
            List<PreciseObserverRecord> list = this.mPreciseObserverRecordsByCallId.get(Integer.valueOf(i));
            if (list == null) {
                HiLog.error(TAG, "unregisterPreciseObserver error, no registered callObserver.", new Object[0]);
                return -1;
            }
            PreciseObserverRecord preciseObserverRecord = null;
            Iterator<PreciseObserverRecord> it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                PreciseObserverRecord next = it.next();
                if (next.getPreciseObserver() == preciseObserver) {
                    preciseObserverRecord = next;
                    break;
                }
            }
            if (preciseObserverRecord != null) {
                HiLog.info(TAG, "unregisterPreciseObserver: remove record", new Object[0]);
                list.remove(preciseObserverRecord);
                return 0;
            }
        }
        return -1;
    }

    public void triggerStatusChanged(DistributedCall distributedCall, final int i) {
        HiLog.info(TAG, "triggerStatusChanged: newStatus = %{public}d.", Integer.valueOf(i));
        if (distributedCall == null) {
            HiLog.error(TAG, "triggerStatusChanged error, no call.", new Object[0]);
            return;
        }
        List<PreciseObserverRecord> list = this.mPreciseObserverRecordsByCallId.get(Integer.valueOf(distributedCall.getCallId()));
        if (list == null) {
            HiLog.error(TAG, "triggerStatusChanged error, no registered callObserver.", new Object[0]);
            return;
        }
        final DistributedCall updateDistributeCall = updateDistributeCall(distributedCall);
        if (updateDistributeCall == null) {
            HiLog.error(TAG, "triggerStatusChanged error, no realcall.", new Object[0]);
            return;
        }
        for (PreciseObserverRecord preciseObserverRecord : list) {
            final DistributedCall.PreciseObserver preciseObserver = preciseObserverRecord.getPreciseObserver();
            preciseObserverRecord.getHandler().postTask(new Runnable() {
                /* class ohos.dcall.PreciseObserverProxy.AnonymousClass1 */

                public void run() {
                    HiLog.info(PreciseObserverProxy.TAG, "triggerStatusChanged: onStateChanged", new Object[0]);
                    preciseObserver.onStatusChanged(updateDistributeCall, i);
                }
            }, 0, EventHandler.Priority.IMMEDIATE);
        }
    }

    public void triggerInfoChanged(DistributedCall distributedCall, final DistributedCall.Info info) {
        HiLog.info(TAG, "triggerInfoChanged", new Object[0]);
        if (distributedCall == null) {
            HiLog.error(TAG, "triggerInfoChanged error, no call.", new Object[0]);
        } else if (info == null) {
            HiLog.error(TAG, "triggerInfoChanged error, no info.", new Object[0]);
        } else {
            List<PreciseObserverRecord> list = this.mPreciseObserverRecordsByCallId.get(Integer.valueOf(distributedCall.getCallId()));
            if (list == null) {
                HiLog.error(TAG, "triggerInfoChanged error, no registered callObserver.", new Object[0]);
                return;
            }
            final DistributedCall updateDistributeCall = updateDistributeCall(distributedCall);
            if (updateDistributeCall == null) {
                HiLog.error(TAG, "triggerInfoChanged error, no realcall.", new Object[0]);
                return;
            }
            for (PreciseObserverRecord preciseObserverRecord : list) {
                final DistributedCall.PreciseObserver preciseObserver = preciseObserverRecord.getPreciseObserver();
                preciseObserverRecord.getHandler().postTask(new Runnable() {
                    /* class ohos.dcall.PreciseObserverProxy.AnonymousClass2 */

                    public void run() {
                        HiLog.info(PreciseObserverProxy.TAG, "triggerInfoChanged: onInfoChanged", new Object[0]);
                        preciseObserver.onInfoChanged(updateDistributeCall, info);
                    }
                }, 0, EventHandler.Priority.IMMEDIATE);
            }
        }
    }

    public void triggerPostDialWait(DistributedCall distributedCall, final String str) {
        if (distributedCall == null) {
            HiLog.error(TAG, "triggerPostDialWait error, no call.", new Object[0]);
            return;
        }
        List<PreciseObserverRecord> list = this.mPreciseObserverRecordsByCallId.get(Integer.valueOf(distributedCall.getCallId()));
        if (list == null) {
            HiLog.error(TAG, "triggerPostDialWait error, no registered callObserver.", new Object[0]);
            return;
        }
        final DistributedCall updateDistributeCall = updateDistributeCall(distributedCall);
        if (updateDistributeCall == null) {
            HiLog.error(TAG, "triggerPostDialWait error, no realcall.", new Object[0]);
            return;
        }
        for (PreciseObserverRecord preciseObserverRecord : list) {
            final DistributedCall.PreciseObserver preciseObserver = preciseObserverRecord.getPreciseObserver();
            preciseObserverRecord.getHandler().postTask(new Runnable() {
                /* class ohos.dcall.PreciseObserverProxy.AnonymousClass3 */

                public void run() {
                    HiLog.info(PreciseObserverProxy.TAG, "triggerPostDialWait: onPostDialWait", new Object[0]);
                    preciseObserver.onPostDialDtmfWait(updateDistributeCall, str);
                }
            }, 0, EventHandler.Priority.IMMEDIATE);
        }
    }

    public void triggerCallCompleted(DistributedCall distributedCall) {
        HiLog.info(TAG, "triggerCallCompleted", new Object[0]);
        if (distributedCall == null) {
            HiLog.error(TAG, "triggerCallCompleted error, no call.", new Object[0]);
            return;
        }
        int callId = distributedCall.getCallId();
        List<PreciseObserverRecord> list = this.mPreciseObserverRecordsByCallId.get(Integer.valueOf(callId));
        if (list == null) {
            HiLog.error(TAG, "triggerCallCompleted error, no registered callObserver.", new Object[0]);
            return;
        }
        final DistributedCall updateDistributeCall = updateDistributeCall(distributedCall);
        if (updateDistributeCall == null) {
            HiLog.error(TAG, "triggerCallCompleted error, no realcall.", new Object[0]);
            return;
        }
        for (PreciseObserverRecord preciseObserverRecord : list) {
            final DistributedCall.PreciseObserver preciseObserver = preciseObserverRecord.getPreciseObserver();
            preciseObserverRecord.getHandler().postTask(new Runnable() {
                /* class ohos.dcall.PreciseObserverProxy.AnonymousClass4 */

                public void run() {
                    HiLog.info(PreciseObserverProxy.TAG, "triggerCallCompleted: onCallCompleted", new Object[0]);
                    preciseObserver.onCallCompleted(updateDistributeCall);
                }
            }, 0, EventHandler.Priority.IMMEDIATE);
        }
        list.clear();
        this.mPreciseObserverRecordsByCallId.remove(Integer.valueOf(callId));
    }

    public void triggerOnCallEventChanged(DistributedCall distributedCall, final String str, final PacMap pacMap) {
        if (distributedCall == null) {
            HiLog.error(TAG, "triggerOnCallEventChanged error, no call.", new Object[0]);
            return;
        }
        List<PreciseObserverRecord> list = this.mPreciseObserverRecordsByCallId.get(Integer.valueOf(distributedCall.getCallId()));
        if (list == null) {
            HiLog.error(TAG, "triggerOnCallEventChanged error, no registered callObserver.", new Object[0]);
            return;
        }
        final DistributedCall updateDistributeCall = updateDistributeCall(distributedCall);
        if (updateDistributeCall == null) {
            HiLog.error(TAG, "triggerOnCallEventChanged error, no realcall.", new Object[0]);
            return;
        }
        for (PreciseObserverRecord preciseObserverRecord : list) {
            final DistributedCall.PreciseObserver preciseObserver = preciseObserverRecord.getPreciseObserver();
            preciseObserverRecord.getHandler().postTask(new Runnable() {
                /* class ohos.dcall.PreciseObserverProxy.AnonymousClass5 */

                public void run() {
                    HiLog.info(PreciseObserverProxy.TAG, "triggerOnCallEventChanged: onCallEventChanged", new Object[0]);
                    preciseObserver.onCallEventChanged(updateDistributeCall, str, pacMap);
                }
            }, 0, EventHandler.Priority.IMMEDIATE);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean onCallCreated(DistributedCall distributedCall) {
        if (distributedCall == null) {
            return false;
        }
        int callId = distributedCall.getCallId();
        HiLog.info(TAG, "onCallCreated: callId is %{public}d.", Integer.valueOf(callId));
        this.mDistributedCalls.put(Integer.valueOf(callId), distributedCall);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean onCallDeleted(DistributedCall distributedCall) {
        if (distributedCall == null) {
            return false;
        }
        int callId = distributedCall.getCallId();
        HiLog.info(TAG, "onCallDeleted: callId is %{public}d.", Integer.valueOf(callId));
        this.mDistributedCalls.remove(Integer.valueOf(callId));
        return true;
    }

    /* access modifiers changed from: package-private */
    public DistributedCall getCallById(int i) {
        return this.mDistributedCalls.get(Integer.valueOf(i));
    }

    private DistributedCall updateDistributeCall(DistributedCall distributedCall) {
        if (distributedCall == null) {
            return null;
        }
        DistributedCall distributedCall2 = this.mDistributedCalls.get(Integer.valueOf(distributedCall.getCallId()));
        if (distributedCall2 == null) {
            HiLog.info(TAG, "updateDistributeCall: can not find callId %{public}d.", Integer.valueOf(distributedCall.getCallId()));
            return null;
        }
        distributedCall2.updateDetails(distributedCall);
        return distributedCall2;
    }

    /* access modifiers changed from: package-private */
    public class PreciseObserverRecord {
        private final EventHandler mHandler;
        private final DistributedCall.PreciseObserver mObserver;

        public PreciseObserverRecord(DistributedCall.PreciseObserver preciseObserver, EventHandler eventHandler) {
            this.mObserver = preciseObserver;
            this.mHandler = eventHandler;
        }

        public DistributedCall.PreciseObserver getPreciseObserver() {
            return this.mObserver;
        }

        public EventHandler getHandler() {
            return this.mHandler;
        }
    }
}
