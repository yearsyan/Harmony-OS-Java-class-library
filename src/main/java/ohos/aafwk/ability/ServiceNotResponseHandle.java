package ohos.aafwk.ability;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import ohos.aafwk.ability.FormAdapter;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class ServiceNotResponseHandle {
    static final int ERR_EXCEED_REQUEST = 1;
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 218108160, "ServiceNotResponseHandle");
    private static final long MAX_WAIT_TIME = 4000;
    static final int RESULT_OK = 0;
    private static final int SNR_COUNT = 40;
    private static final Object SNR_LOCK = new Object();
    private static Map<String, LinkedList<FormAdapter.BaseConnection>> connMap = new HashMap();
    private static volatile ServiceNotResponseHandle handle;

    static ServiceNotResponseHandle getInstance() {
        if (handle == null) {
            synchronized (SNR_LOCK) {
                if (handle == null) {
                    handle = new ServiceNotResponseHandle();
                }
            }
        }
        return handle;
    }

    /* access modifiers changed from: package-private */
    public int addRequest(FormAdapter.BaseConnection baseConnection) {
        synchronized (SNR_LOCK) {
            LinkedList<FormAdapter.BaseConnection> linkedList = connMap.get(baseConnection.getSupplyInfo());
            if (linkedList == null) {
                linkedList = new LinkedList<>();
                connMap.put(baseConnection.getSupplyInfo(), linkedList);
            }
            if (linkedList.size() < 40) {
                linkedList.add(baseConnection);
                return 0;
            }
            HiLog.warn(LABEL_LOG, "too many request not response form ams, app:%{public}s", baseConnection.getSupplyInfo());
            FormAdapter.BaseConnection peek = linkedList.peek();
            if (System.currentTimeMillis() - peek.createTime < MAX_WAIT_TIME) {
                HiLog.error(LABEL_LOG, "too many request come, app:%{public}s", baseConnection.getSupplyInfo());
                return 1;
            }
            FormAdapter.disconnectAbility(FormAdapter.getInstance().getContext(), peek);
            linkedList.poll();
            linkedList.add(baseConnection);
            return 0;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean removeRequest(FormAdapter.BaseConnection baseConnection) {
        synchronized (SNR_LOCK) {
            LinkedList<FormAdapter.BaseConnection> linkedList = connMap.get(baseConnection.getSupplyInfo());
            if (linkedList != null) {
                if (!linkedList.isEmpty()) {
                    boolean remove = linkedList.remove(baseConnection);
                    if (linkedList.size() == 0) {
                        connMap.remove(baseConnection.getSupplyInfo());
                    }
                    return remove;
                }
            }
            HiLog.warn(LABEL_LOG, "connQueue is null or empty, app:%{public}s", baseConnection.getSupplyInfo());
            return false;
        }
    }
}
