package ohos.workschedulerservice.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import ohos.aafwk.content.Intent;
import ohos.batterymanager.BatteryInfo;
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

public final class BatteryStateListener extends StateListener {
    private static final int BATTERY_INFO_DEFAULT_VALUE = -1;
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, 218109696, TAG);
    private static final String TAG = "BatteryStateListener";
    private BatteryStateEventSubscriber batteryEventSubscriber;
    private int batteryLevel;
    private int batteryStatus;
    private int chargerType;
    private boolean isCharging;
    private boolean isPowerConnect;
    private boolean listenerEnable;
    private final Object lock = new Object();
    private final ArrayList<WorkStatus> trackedTasks = new ArrayList<>();

    public BatteryStateListener(WorkQueueManager workQueueManager) {
        super(workQueueManager);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x004f  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0079  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void init() {
        /*
        // Method dump skipped, instructions count: 189
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.workschedulerservice.controller.BatteryStateListener.init():void");
    }

    @Override // ohos.workschedulerservice.controller.StateListener
    public void dumpStateListenerStatus(PrintWriter printWriter, String str) {
        if (printWriter == null || str == null) {
            HiLog.error(LOG_LABEL, "error dump PrintWriter or prefix input", new Object[0]);
            return;
        }
        printWriter.println();
        printWriter.println("BatteryStateListener:");
        synchronized (this.lock) {
            printWriter.println(str + "isCharging:" + this.isCharging);
            printWriter.println(str + "isPowerConnect:" + this.isPowerConnect);
            printWriter.println(str + "chargerType:" + this.chargerType);
            printWriter.println(str + "batteryLevel:" + this.batteryLevel);
            printWriter.println(str + "batteryStatus:" + this.batteryStatus);
            printWriter.println(str + "listenerEnable:" + this.listenerEnable);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.workschedulerservice.controller.BatteryStateListener$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$batterymanager$BatteryInfo$BatteryPluggedType = new int[BatteryInfo.BatteryPluggedType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                ohos.batterymanager.BatteryInfo$BatteryPluggedType[] r0 = ohos.batterymanager.BatteryInfo.BatteryPluggedType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.workschedulerservice.controller.BatteryStateListener.AnonymousClass1.$SwitchMap$ohos$batterymanager$BatteryInfo$BatteryPluggedType = r0
                int[] r0 = ohos.workschedulerservice.controller.BatteryStateListener.AnonymousClass1.$SwitchMap$ohos$batterymanager$BatteryInfo$BatteryPluggedType     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.batterymanager.BatteryInfo$BatteryPluggedType r1 = ohos.batterymanager.BatteryInfo.BatteryPluggedType.AC     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.workschedulerservice.controller.BatteryStateListener.AnonymousClass1.$SwitchMap$ohos$batterymanager$BatteryInfo$BatteryPluggedType     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.batterymanager.BatteryInfo$BatteryPluggedType r1 = ohos.batterymanager.BatteryInfo.BatteryPluggedType.USB     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.workschedulerservice.controller.BatteryStateListener.AnonymousClass1.$SwitchMap$ohos$batterymanager$BatteryInfo$BatteryPluggedType     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.batterymanager.BatteryInfo$BatteryPluggedType r1 = ohos.batterymanager.BatteryInfo.BatteryPluggedType.WIRELESS     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.workschedulerservice.controller.BatteryStateListener.AnonymousClass1.<clinit>():void");
        }
    }

    private int convertFromBatteryPluggedType(BatteryInfo.BatteryPluggedType batteryPluggedType) {
        int i = AnonymousClass1.$SwitchMap$ohos$batterymanager$BatteryInfo$BatteryPluggedType[batteryPluggedType.ordinal()];
        int i2 = 2;
        if (i != 1) {
            i2 = i != 2 ? i != 3 ? 0 : 8 : 4;
        }
        return i2 != 0 ? i2 | 1 : i2;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int convertFromCommonEventChargeType(int i) {
        int i2;
        if (i == PluggedTypeFromCommonEvent.WIRELESS.getValue()) {
            i2 = 8;
        } else {
            i2 = i == PluggedTypeFromCommonEvent.NONE.getValue() ? 0 : 1 << i;
        }
        return i2 != 0 ? i2 | 1 : i2;
    }

    /* access modifiers changed from: private */
    public enum PluggedTypeFromCommonEvent {
        NONE(0),
        AC(1),
        USB(2),
        WIRELESS(4);
        
        private final int value;

        private PluggedTypeFromCommonEvent(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }
    }

    private boolean enableBatteryListener() {
        MatchingSkills matchingSkills = new MatchingSkills();
        matchingSkills.addEvent(CommonEventSupport.COMMON_EVENT_BATTERY_CHANGED);
        matchingSkills.addEvent(CommonEventSupport.COMMON_EVENT_BATTERY_LOW);
        matchingSkills.addEvent(CommonEventSupport.COMMON_EVENT_BATTERY_OKAY);
        matchingSkills.addEvent(CommonEventSupport.COMMON_EVENT_POWER_CONNECTED);
        matchingSkills.addEvent(CommonEventSupport.COMMON_EVENT_POWER_DISCONNECTED);
        matchingSkills.addEvent(CommonEventSupport.COMMON_EVENT_DISCHARGING);
        matchingSkills.addEvent(CommonEventSupport.COMMON_EVENT_CHARGING);
        CommonEventSubscribeInfo commonEventSubscribeInfo = new CommonEventSubscribeInfo(matchingSkills);
        commonEventSubscribeInfo.setUserId(-1);
        this.batteryEventSubscriber = new BatteryStateEventSubscriber(commonEventSubscribeInfo);
        try {
            CommonEventManager.subscribeCommonEvent(this.batteryEventSubscriber);
            return true;
        } catch (RemoteException unused) {
            HiLog.error(LOG_LABEL, "subscribeCommonEvent occur exception.", new Object[0]);
            this.batteryEventSubscriber = null;
            return false;
        }
    }

    private boolean disableBatteryListener() {
        BatteryStateEventSubscriber batteryStateEventSubscriber = this.batteryEventSubscriber;
        if (batteryStateEventSubscriber == null) {
            return true;
        }
        try {
            CommonEventManager.unsubscribeCommonEvent(batteryStateEventSubscriber);
            return true;
        } catch (RemoteException unused) {
            HiLog.error(LOG_LABEL, "unsubscribeCommonEvent occur exception", new Object[0]);
            return false;
        }
    }

    @Override // ohos.workschedulerservice.controller.StateListener
    public void tryStartSignWork(WorkStatus workStatus) {
        synchronized (this.lock) {
            if (workStatus.hasBatteryCondition() || workStatus.hasChargeCondition()) {
                this.trackedTasks.add(workStatus);
                workStatus.changeChargingSatisfiedCondition(isGoodPower(), this.chargerType);
                workStatus.changeBatteryLevelSatisfiedCondition(this.batteryLevel);
                workStatus.changeBatteryStatusSatisfiedCondition(this.batteryStatus);
                updateListenerStatus();
            }
        }
    }

    @Override // ohos.workschedulerservice.controller.StateListener
    public void tryStopSignWork(WorkStatus workStatus) {
        synchronized (this.lock) {
            this.trackedTasks.remove(workStatus);
            updateListenerStatus();
        }
    }

    @Override // ohos.workschedulerservice.controller.StateListener
    public void updateTrackedTasks(WorkStatus workStatus) {
        synchronized (this.lock) {
            updateTasks(this.trackedTasks, workStatus);
        }
    }

    private void updateListenerStatus() {
        if (this.trackedTasks.isEmpty()) {
            if (this.listenerEnable) {
                this.listenerEnable = !disableBatteryListener();
            }
        } else if (!this.listenerEnable) {
            this.listenerEnable = enableBatteryListener();
        }
    }

    public final class BatteryStateEventSubscriber extends CommonEventSubscriber {
        BatteryStateEventSubscriber(CommonEventSubscribeInfo commonEventSubscribeInfo) {
            super(commonEventSubscribeInfo);
        }

        @Override // ohos.event.commonevent.CommonEventSubscriber
        public void onReceiveEvent(CommonEventData commonEventData) {
            synchronized (BatteryStateListener.this.lock) {
                if (commonEventData != null) {
                    Intent intent = commonEventData.getIntent();
                    if (intent != null) {
                        String action = intent.getAction();
                        if (CommonEventSupport.COMMON_EVENT_BATTERY_CHANGED.equals(action)) {
                            int intParam = intent.getIntParam(BatteryInfo.OHOS_CHARGE_TYPE, -1);
                            if (intParam != -1) {
                                BatteryStateListener.this.chargerType = BatteryStateListener.this.convertFromCommonEventChargeType(intParam);
                            }
                            int intParam2 = intent.getIntParam(BatteryInfo.OHOS_BATTERY_CAPACITY, -1);
                            if (intParam2 != -1) {
                                BatteryStateListener.this.batteryLevel = intParam2;
                            }
                            BatteryStateListener.this.updateStateFromCommonEvent(intent);
                        } else if (CommonEventSupport.COMMON_EVENT_BATTERY_LOW.equals(action)) {
                            BatteryStateListener.this.batteryStatus = 1;
                        } else if (CommonEventSupport.COMMON_EVENT_BATTERY_OKAY.equals(action)) {
                            BatteryStateListener.this.batteryStatus = 2;
                        } else if (CommonEventSupport.COMMON_EVENT_POWER_CONNECTED.equals(action)) {
                            BatteryStateListener.this.isPowerConnect = true;
                        } else if (CommonEventSupport.COMMON_EVENT_POWER_DISCONNECTED.equals(action)) {
                            BatteryStateListener.this.isPowerConnect = false;
                        } else if (CommonEventSupport.COMMON_EVENT_DISCHARGING.equals(action)) {
                            BatteryStateListener.this.isCharging = false;
                        } else if (CommonEventSupport.COMMON_EVENT_CHARGING.equals(action)) {
                            BatteryStateListener.this.isCharging = true;
                        } else {
                            HiLog.error(BatteryStateListener.LOG_LABEL, "Action %s is not used but received ", action);
                        }
                        HiLog.info(BatteryStateListener.LOG_LABEL, "Action %{public}s Battery status:%{public}s,type:%{public}d,isCharging:%{public}s level:%{public}d,isPowerConnect %{public}s", action, Integer.valueOf(BatteryStateListener.this.batteryStatus), Integer.valueOf(BatteryStateListener.this.chargerType), Boolean.valueOf(BatteryStateListener.this.isCharging), Integer.valueOf(BatteryStateListener.this.batteryLevel), Boolean.valueOf(BatteryStateListener.this.isPowerConnect));
                        BatteryStateListener.this.updateTrackedWorks();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateStateFromCommonEvent(Intent intent) {
        if (intent.getBooleanParam(BatteryInfo.OHOS_BATTERY_LOW, false)) {
            this.batteryStatus = 1;
        } else {
            this.batteryStatus = 2;
        }
        int intParam = intent.getIntParam(BatteryInfo.OHOS_CHARGE_STATE, -1);
        if (intParam == BatteryInfo.BatteryChargeState.ENABLE.ordinal() || intParam == BatteryInfo.BatteryChargeState.FULL.ordinal()) {
            this.isCharging = true;
        } else {
            this.isCharging = false;
        }
    }

    private boolean isGoodPower() {
        if (this.batteryStatus == 2) {
            return this.isCharging;
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateTrackedWorks() {
        boolean z = false;
        HiLog.info(LOG_LABEL, "updateTrackedWorks", new Object[0]);
        int i = this.chargerType;
        if (this.isPowerConnect) {
            i |= 1;
        }
        Iterator<WorkStatus> it = this.trackedTasks.iterator();
        while (it.hasNext()) {
            WorkStatus next = it.next();
            z = z | next.changeChargingSatisfiedCondition(isGoodPower(), i) | next.changeBatteryLevelSatisfiedCondition(this.batteryLevel) | next.changeBatteryStatusSatisfiedCondition(this.batteryStatus);
            if (z) {
                this.workQueueMgr.onDeviceStateChanged(next, 5);
            }
        }
    }
}
