package ohos.workschedulerservice;

import android.content.Context;
import com.huawei.ohos.workscheduleradapter.WorkSchedulerCommon;
import java.util.Iterator;
import java.util.List;
import ohos.aafwk.content.Intent;
import ohos.bundle.BundleManager;
import ohos.bundle.CommonEventInfo;
import ohos.event.commonevent.CommonEventData;
import ohos.event.commonevent.CommonEventManager;
import ohos.event.commonevent.CommonEventSupport;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.RemoteException;
import ohos.system.Parameters;
import ohos.utils.LightweightMap;
import ohos.utils.LightweightSet;
import ohos.workschedulerservice.controller.CommonEventStatus;

public final class CommonEventRegister {
    private static final int APP_FIRST_UID = 10000;
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, 218109696, "CommonEventRegister");
    private static final int LONG_WAIT_TIME = 3000;
    private static final int NORMAL_WAIT_TIME = 1000;
    private static final int SHORT_WAIT_TIME = 500;
    private final LightweightSet<String> bundleList = new LightweightSet<>();
    private BundleManager bundleManager;
    private final LightweightMap<String, String> commonEventList = new LightweightMap<>();
    private final LightweightMap<Integer, LightweightSet<CommonEventStatus>> commonEventMap = new LightweightMap<>();
    private Context context;
    private final LightweightMap<String, String> eventDataList = new LightweightMap<>();
    private final Object lock = new Object();
    private WorkQueueManager workQueueMgr;

    public CommonEventRegister(Context context2, WorkQueueManager workQueueManager) {
        this.context = context2;
        this.workQueueMgr = workQueueManager;
    }

    public boolean init() {
        if (this.context == null || this.workQueueMgr == null) {
            HiLog.error(LOG_LABEL, "workQueueManager or context is null, init failed", new Object[0]);
            return false;
        }
        initCommonEventList();
        initBundleList();
        initEventDataList();
        readRegistAsync();
        return true;
    }

    public void updateHapStatus(int i, String str) {
        boolean z;
        if (this.workQueueMgr == null || this.context == null || "".equals(str)) {
            HiLog.error(LOG_LABEL, "workQueueMgr or reason is null, updateHapStatus failed!", new Object[0]);
            return;
        }
        HiLog.info(LOG_LABEL, "updateHapStatus: %{public}d need upadte, reason: %{public}s.", Integer.valueOf(i), str);
        synchronized (this.lock) {
            if ("add".equals(str)) {
                z = getEventInfos(i);
            } else if ("remove".equals(str)) {
                z = removeEventInfos(i);
            } else {
                HiLog.debug(LOG_LABEL, "updateHapStatus, forcestop Hap here, no need operation for commonevent!", new Object[0]);
                return;
            }
            if (z) {
                HiLog.info(LOG_LABEL, "updateHapStatus: need upadte.", new Object[0]);
                this.workQueueMgr.tryStartCommonEvent();
            }
        }
    }

    public void parseEventInfo(CommonEventInfo commonEventInfo) {
        if (this.workQueueMgr == null || commonEventInfo == null) {
            HiLog.error(LOG_LABEL, "workQueueMgr is null, parseEventInfo failed!", new Object[0]);
            return;
        }
        synchronized (this.commonEventMap) {
            String bundleName = commonEventInfo.getBundleName();
            String permission = commonEventInfo.getPermission();
            int uid = commonEventInfo.getUid();
            WorkSchedulerCommon.getUserIdFromUid(uid);
            List<String> events = commonEventInfo.getEvents();
            HiLog.info(LOG_LABEL, "parseEventInfo: %{public}s - %{public}d here.", bundleName, Integer.valueOf(events.size()));
            for (String str : events) {
                boolean isAllowedRegist = isAllowedRegist(uid, bundleName, str);
                HiLog.debug(LOG_LABEL, "%{public}d %{public}s %{public}s %{public}s - %{public}b", Integer.valueOf(uid), bundleName, str, permission, Boolean.valueOf(isAllowedRegist));
                if (isAllowedRegist) {
                    LightweightSet<CommonEventStatus> lightweightSet = this.commonEventMap.get(Integer.valueOf(uid));
                    CommonEventStatus commonEventStatus = new CommonEventStatus(commonEventInfo, str);
                    if (lightweightSet == null) {
                        lightweightSet = new LightweightSet<>();
                        this.commonEventMap.put(Integer.valueOf(uid), lightweightSet);
                    } else if (hasStatusInList(lightweightSet, commonEventStatus)) {
                    }
                    lightweightSet.add(commonEventStatus);
                    this.workQueueMgr.tryRegistCommonEvent(str, commonEventStatus);
                }
            }
        }
    }

    public int getListCountByUid(int i) {
        if (i < 0) {
            HiLog.error(LOG_LABEL, "getListCountByUid failed, uid is illegal!", new Object[0]);
            return 0;
        }
        synchronized (this.commonEventMap) {
            LightweightSet<CommonEventStatus> lightweightSet = this.commonEventMap.get(Integer.valueOf(i));
            if (lightweightSet == null) {
                return 0;
            }
            return lightweightSet.size();
        }
    }

    private boolean hasStatusInList(LightweightSet<CommonEventStatus> lightweightSet, CommonEventStatus commonEventStatus) {
        Iterator<CommonEventStatus> it = lightweightSet.iterator();
        while (it.hasNext()) {
            if (it.next().isSameStatus(commonEventStatus)) {
                return true;
            }
        }
        return false;
    }

    private void readRegistAsync() {
        new Thread(new GetRegisteredEventRunnable()).start();
    }

    /* access modifiers changed from: private */
    public class GetRegisteredEventRunnable implements Runnable {
        GetRegisteredEventRunnable() {
        }

        public void run() {
            while (CommonEventRegister.this.bundleManager == null) {
                try {
                    Thread.sleep(500);
                    CommonEventRegister.this.bundleManager = WorkSchedulerCommon.getBundleManager();
                } catch (InterruptedException unused) {
                    HiLog.error(CommonEventRegister.LOG_LABEL, "GetBundleManager occur exception.", new Object[0]);
                }
            }
            HiLog.info(CommonEventRegister.LOG_LABEL, "GetBundleManager success! Wait 3s for event infos is ready!", new Object[0]);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException unused2) {
                HiLog.error(CommonEventRegister.LOG_LABEL, "GetBundleManager wait occur exception.", new Object[0]);
            }
            synchronized (CommonEventRegister.this.lock) {
                if (CommonEventRegister.this.getEventInfos()) {
                    CommonEventRegister.this.workQueueMgr.tryStartCommonEvent();
                }
            }
            HiLog.info(CommonEventRegister.LOG_LABEL, "Get foundation status begin!", new Object[0]);
            while (!"1".equals(Parameters.get("sys.boot_completed", "0"))) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException unused3) {
                    HiLog.error(CommonEventRegister.LOG_LABEL, "GetRegisteredEventRunnable occur exception.", new Object[0]);
                }
            }
            HiLog.info(CommonEventRegister.LOG_LABEL, "Get foundation status success! Wait 3s!", new Object[0]);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException unused4) {
                HiLog.error(CommonEventRegister.LOG_LABEL, "GetRegisteredEventRunnable wait occur exception.", new Object[0]);
            }
            CommonEventRegister.this.sendReadyEvent();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void sendReadyEvent() {
        try {
            CommonEventManager.publishCommonEvent(new CommonEventData(new Intent().setAction("usual.event.FOUNDATION_READY"), 0, ""));
        } catch (RemoteException unused) {
            HiLog.debug(LOG_LABEL, "subscribeCommonEvent occur exception!", new Object[0]);
        }
        HiLog.info(LOG_LABEL, "sendReadyEvent success!", new Object[0]);
    }

    private boolean removeEventInfos(int i) {
        synchronized (this.commonEventMap) {
            LightweightSet<CommonEventStatus> lightweightSet = this.commonEventMap.get(Integer.valueOf(i));
            if (lightweightSet == null) {
                return false;
            }
            Iterator<CommonEventStatus> it = lightweightSet.iterator();
            while (it.hasNext()) {
                CommonEventStatus next = it.next();
                if (next != null) {
                    this.workQueueMgr.tryUnRegistCommonEvent(next.getEventName(), next);
                }
            }
            this.commonEventMap.remove(Integer.valueOf(i));
            return true;
        }
    }

    private boolean getEventInfos(int i) {
        BundleManager bundleManager2 = this.bundleManager;
        if (bundleManager2 == null) {
            HiLog.error(LOG_LABEL, "bundleManager is null, getEventInfos failed!", new Object[0]);
            return false;
        }
        try {
            List<CommonEventInfo> queryCommonEventInfos = bundleManager2.queryCommonEventInfos(i);
            if (queryCommonEventInfos == null) {
                HiLog.error(LOG_LABEL, "no common event infos, skip regist!", new Object[0]);
                return false;
            }
            HiLog.info(LOG_LABEL, "total %{public}d need regist.", Integer.valueOf(queryCommonEventInfos.size()));
            for (int size = queryCommonEventInfos.size() - 1; size >= 0; size--) {
                parseEventInfo(queryCommonEventInfos.get(size));
            }
            return true;
        } catch (RemoteException unused) {
            HiLog.error(LOG_LABEL, "getEventInfos occur exception.", new Object[0]);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean getEventInfos() {
        BundleManager bundleManager2 = this.bundleManager;
        if (bundleManager2 == null) {
            HiLog.error(LOG_LABEL, "bundleManager is null, getEventInfos failed!", new Object[0]);
            return false;
        }
        try {
            List<CommonEventInfo> queryCommonEventInfos = bundleManager2.queryCommonEventInfos();
            if (queryCommonEventInfos == null) {
                HiLog.error(LOG_LABEL, "no common event infos, skip regist!", new Object[0]);
                return false;
            }
            HiLog.info(LOG_LABEL, "total %{public}d need regist.", Integer.valueOf(queryCommonEventInfos.size()));
            for (int size = queryCommonEventInfos.size() - 1; size >= 0; size--) {
                parseEventInfo(queryCommonEventInfos.get(size));
            }
            return true;
        } catch (RemoteException unused) {
            HiLog.info(LOG_LABEL, "getEventInfos occur exception.", new Object[0]);
        }
    }

    private boolean isAllowedRegist(int i, String str, String str2) {
        HiLog.debug(LOG_LABEL, "isAllowedRegist is checking %{public}s, %{public}d", str, Integer.valueOf(i));
        if (!(this.bundleList.isEmpty() ? i <= 10000 : this.bundleList.contains(str))) {
            BundleManager bundleManager2 = this.bundleManager;
            if (bundleManager2 == null) {
                HiLog.error(LOG_LABEL, "bundleManager is null, check bundle permission failed!", new Object[0]);
                return false;
            }
            String str3 = null;
            try {
                str3 = bundleManager2.getAppType(str);
            } catch (RemoteException unused) {
                HiLog.error(LOG_LABEL, "isAllowedRegist occur exception.", new Object[0]);
            }
            HiLog.debug(LOG_LABEL, "hap type is %{public}s", str3);
            if (!"system".equals(str3)) {
                return false;
            }
        }
        if (this.commonEventList.isEmpty()) {
            return true;
        }
        String str4 = this.commonEventList.get(str2);
        if (str4 == null) {
            HiLog.error(LOG_LABEL, "event is not allowed to regist, check %{public}s failed!", str2);
            return false;
        }
        if (!str4.isEmpty()) {
            if (this.bundleManager == null) {
                HiLog.error(LOG_LABEL, "bundleManager is null, check event permission failed!", new Object[0]);
                return false;
            } else if (this.context.checkPermission(str4, -1, i) != 0) {
                HiLog.error(LOG_LABEL, "event has no %{public}s regist, check permission failed!", str4);
                return false;
            }
        }
        return true;
    }

    private void initCommonEventList() {
        this.commonEventList.put(CommonEventSupport.COMMON_EVENT_LOCALE_CHANGED, "");
        this.commonEventList.put(CommonEventSupport.COMMON_EVENT_TIME_CHANGED, "");
        this.commonEventList.put(CommonEventSupport.COMMON_EVENT_TIMEZONE_CHANGED, "");
        this.commonEventList.put(CommonEventSupport.COMMON_EVENT_DATE_CHANGED, "");
        this.commonEventList.put("usual.event.FOUNDATION_READY", "android.permission.RECEIVE_BOOT_COMPLETED");
        this.commonEventList.put("ohos.telecom.action.SHOW_MISSED_CALLS_NOTIFICATION", "");
        this.commonEventList.put("android.intent.action.EVENT_REMINDER", "");
        this.commonEventList.put("android.intent.action.PROVIDER_CHANGED", "");
        this.commonEventList.put("android.telecom.action.SHOW_MISSED_CALLS_NOTIFICATION", "");
    }

    private void initEventDataList() {
        this.eventDataList.put(CommonEventSupport.COMMON_EVENT_BOOT_COMPLETED, "");
        this.eventDataList.put(CommonEventSupport.COMMON_EVENT_LOCKED_BOOT_COMPLETED, "");
        this.eventDataList.put(CommonEventSupport.COMMON_EVENT_LOCALE_CHANGED, "");
        this.eventDataList.put(CommonEventSupport.COMMON_EVENT_TIME_CHANGED, "");
        this.eventDataList.put(CommonEventSupport.COMMON_EVENT_TIMEZONE_CHANGED, "");
        this.eventDataList.put(CommonEventSupport.COMMON_EVENT_DATE_CHANGED, "");
        this.eventDataList.put("android.intent.action.EVENT_REMINDER", "");
        this.eventDataList.put("android.intent.action.PROVIDER_CHANGED", "");
        this.eventDataList.put("ohos.telecom.action.SHOW_MISSED_CALLS_NOTIFICATION", "");
        this.eventDataList.put("android.telecom.action.SHOW_MISSED_CALLS_NOTIFICATION", "");
    }

    private void initBundleList() {
        HiLog.info(LOG_LABEL, "initBundleList success!", new Object[0]);
    }
}
