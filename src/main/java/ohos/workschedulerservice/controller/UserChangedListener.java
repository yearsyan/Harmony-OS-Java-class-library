package ohos.workschedulerservice.controller;

import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import ohos.aafwk.content.Intent;
import ohos.event.commonevent.CommonEventData;
import ohos.event.commonevent.CommonEventManager;
import ohos.event.commonevent.CommonEventSubscribeInfo;
import ohos.event.commonevent.CommonEventSubscriber;
import ohos.event.commonevent.CommonEventSupport;
import ohos.event.commonevent.MatchingSkills;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.RemoteException;
import ohos.workschedulerservice.WorkQueueManager;

public final class UserChangedListener extends StateListener {
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, 218109696, TAG);
    private static final String TAG = "UserChangedListener";
    private static final String USERID_PARAM = "android.intent.extra.user_handle";
    private AtomicInteger currentUserId = new AtomicInteger(-1);
    private AtomicBoolean receivedFlag = new AtomicBoolean(false);
    private UserCommonEventSubscriber userCommonEventSubscriber;

    @Override // ohos.workschedulerservice.controller.StateListener
    public void tryStartSignWork(WorkStatus workStatus) {
    }

    @Override // ohos.workschedulerservice.controller.StateListener
    public void tryStopSignWork(WorkStatus workStatus) {
    }

    public UserChangedListener(WorkQueueManager workQueueManager) {
        super(workQueueManager);
    }

    public void init() {
        HiLog.info(LOG_LABEL, "try to subscribe common event", new Object[0]);
        if (this.workQueueMgr != null) {
            trySubscribeCommonEvent();
        }
    }

    private void trySubscribeCommonEvent() {
        if (this.userCommonEventSubscriber == null) {
            this.userCommonEventSubscriber = new UserCommonEventSubscriber(getInfo());
        }
        try {
            CommonEventManager.subscribeCommonEvent(this.userCommonEventSubscriber);
            HiLog.info(LOG_LABEL, "subscribe common event success", new Object[0]);
        } catch (IllegalArgumentException | RemoteException unused) {
            HiLog.error(LOG_LABEL, "subscribeCommonEvent occur exception.", new Object[0]);
        }
    }

    @Override // ohos.workschedulerservice.controller.StateListener
    public void updateTrackedTasks(WorkStatus workStatus) {
        HiLog.debug(LOG_LABEL, "updateTrackedTasks.", new Object[0]);
    }

    private CommonEventSubscribeInfo getInfo() {
        MatchingSkills matchingSkills = new MatchingSkills();
        matchingSkills.addEvent(CommonEventSupport.COMMON_EVENT_USER_SWITCHED);
        CommonEventSubscribeInfo commonEventSubscribeInfo = new CommonEventSubscribeInfo(matchingSkills);
        commonEventSubscribeInfo.setUserId(-1);
        return commonEventSubscribeInfo;
    }

    public boolean getFlag() {
        return this.receivedFlag.get();
    }

    public void setFlag(boolean z) {
        this.receivedFlag.set(z);
    }

    @Override // ohos.workschedulerservice.controller.StateListener
    public void dumpStateListenerStatus(PrintWriter printWriter, String str) {
        if (printWriter == null || str == null) {
            HiLog.error(LOG_LABEL, "error dump PrintWriter or prefix input", new Object[0]);
            return;
        }
        printWriter.println();
        printWriter.println("UserChangedListener:");
        printWriter.println(str + "currentUserId:" + this.currentUserId.get());
    }

    /* access modifiers changed from: private */
    public final class UserCommonEventSubscriber extends CommonEventSubscriber {
        UserCommonEventSubscriber(CommonEventSubscribeInfo commonEventSubscribeInfo) {
            super(commonEventSubscribeInfo);
        }

        @Override // ohos.event.commonevent.CommonEventSubscriber
        public void onReceiveEvent(CommonEventData commonEventData) {
            Intent intent;
            if (commonEventData != null && (intent = commonEventData.getIntent()) != null) {
                String action = intent.getAction();
                int intParam = intent.getIntParam(UserChangedListener.USERID_PARAM, -1);
                if (intParam == -1) {
                    HiLog.debug(UserChangedListener.LOG_LABEL, "get userId from action failed.", new Object[0]);
                    return;
                }
                UserChangedListener.this.currentUserId.set(intParam);
                if (CommonEventSupport.COMMON_EVENT_USER_SWITCHED.equals(action)) {
                    UserChangedListener.this.workQueueMgr.onUserStateChanged(intParam);
                    UserChangedListener.this.receivedFlag.set(true);
                    return;
                }
                HiLog.debug(UserChangedListener.LOG_LABEL, "UserChangedListener other action.", new Object[0]);
            }
        }
    }
}
