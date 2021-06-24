package ohos.aafwk.ability;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import ohos.aafwk.ability.FormAdapter;
import ohos.event.notification.NotificationRequest;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.system.Parameters;

/* access modifiers changed from: package-private */
public final class FormTimerManager {
    private static final long ABS_TIME = 5000;
    private static final String ACTION_UPDATEATTIMER = "form_update_at_timer";
    private static final int BASE_TIMER_POOL_SIZE = 1;
    private static final String FOUNDATION_NAME = "com.huawei.harmonyos.foundation";
    private static final int HOUR_PER_DAY = 24;
    private static final Object INSTANCE_LOCK = new Object();
    private static final String KEY_WAKEUP_TIME = "wakeUpTime";
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 218108160, "FormTimerManager");
    private static final int MAX_HOUR = 23;
    private static final int MAX_MININUTE = 59;
    private static final long MAX_PERIOD = (MIN_PERIOD * 336);
    private static final long MIN_PERIOD = (((long) (Parameters.getInt("persist.sys.fms.form.update.time", 30) * 60)) * 1000);
    private static final int MIN_PER_HOUR = 60;
    private static final int MIN_TIME = 0;
    private static final long TIME_CONVERSION = 1000;
    private static final int WORK_POOL_SIZE = 4;
    private final Object LOCK = new Object();
    private AlarmManager alarmManager = null;
    private RunnableScheduledFuture<?> baseTimerTask = null;
    private PendingIntent currentPendingIntent = null;
    private HashMap<Long, FormAdapter.RefreshRunnable> tasks = new HashMap<>();
    private ScheduledThreadPoolExecutor timerExecutor = null;
    private TimerReceiver timerReceiver = null;
    private LinkedList<UpdateAtItem> updateAtTasks = new LinkedList<>();
    private int wakeUpTime = Integer.MAX_VALUE;
    private ExecutorService workThreadPool = null;

    FormTimerManager() {
    }

    /* access modifiers changed from: private */
    public static class Holder {
        private static final FormTimerManager INSTANCE = new FormTimerManager();

        private Holder() {
        }
    }

    static FormTimerManager getInstance() {
        return Holder.INSTANCE;
    }

    /* access modifiers changed from: package-private */
    public boolean addFormTimer(FormAdapter.RefreshRunnable refreshRunnable) {
        if (refreshRunnable == null) {
            HiLog.error(LABEL_LOG, "addFormTimer task is null", new Object[0]);
            return false;
        }
        synchronized (this.LOCK) {
            if (refreshRunnable.isUpdateAt) {
                return addUpdateAtTimer(refreshRunnable);
            }
            return addIntervalTimer(refreshRunnable);
        }
    }

    /* access modifiers changed from: package-private */
    public void deleteFormTimer(long j) {
        synchronized (this.LOCK) {
            if (this.tasks.containsKey(Long.valueOf(j))) {
                deleteIntervalTimerLocked(j);
            } else {
                deleteUpdateAtTimerLocked(j);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateFormTimer(long j, FormAdapter.UpdateType updateType, FormAdapter.FormTimerCfg formTimerCfg) {
        if (updateType != null && formTimerCfg != null && formTimerCfg.enableUpdate) {
            int i = AnonymousClass1.$SwitchMap$ohos$aafwk$ability$FormAdapter$UpdateType[updateType.ordinal()];
            if (i == 1) {
                updateIntervalTimer(j, formTimerCfg);
            } else if (i == 2) {
                updateUpdateAtTimer(j, formTimerCfg);
            } else if (i == 3) {
                intervalToUpdateAtTimer(j, formTimerCfg);
            } else if (i == 4) {
                updateAtToIntervalTimer(j, formTimerCfg);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.aafwk.ability.FormTimerManager$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$aafwk$ability$FormAdapter$UpdateType = new int[FormAdapter.UpdateType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                ohos.aafwk.ability.FormAdapter$UpdateType[] r0 = ohos.aafwk.ability.FormAdapter.UpdateType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.aafwk.ability.FormTimerManager.AnonymousClass1.$SwitchMap$ohos$aafwk$ability$FormAdapter$UpdateType = r0
                int[] r0 = ohos.aafwk.ability.FormTimerManager.AnonymousClass1.$SwitchMap$ohos$aafwk$ability$FormAdapter$UpdateType     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.aafwk.ability.FormAdapter$UpdateType r1 = ohos.aafwk.ability.FormAdapter.UpdateType.TYPE_INTERVAL_CHANGE     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.aafwk.ability.FormTimerManager.AnonymousClass1.$SwitchMap$ohos$aafwk$ability$FormAdapter$UpdateType     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.aafwk.ability.FormAdapter$UpdateType r1 = ohos.aafwk.ability.FormAdapter.UpdateType.TYPE_ATTIME_CHANGE     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.aafwk.ability.FormTimerManager.AnonymousClass1.$SwitchMap$ohos$aafwk$ability$FormAdapter$UpdateType     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.aafwk.ability.FormAdapter$UpdateType r1 = ohos.aafwk.ability.FormAdapter.UpdateType.TYPE_INTERVAL_TO_ATTIME     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.aafwk.ability.FormTimerManager.AnonymousClass1.$SwitchMap$ohos$aafwk$ability$FormAdapter$UpdateType     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.aafwk.ability.FormAdapter$UpdateType r1 = ohos.aafwk.ability.FormAdapter.UpdateType.TYPE_ATTIME_TO_INTERVAL     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.FormTimerManager.AnonymousClass1.<clinit>():void");
        }
    }

    /* access modifiers changed from: package-private */
    public void onTimeOut() {
        ExecutorService workThreadPoolLocked;
        ArrayList<FormAdapter.RefreshRunnable> arrayList = new ArrayList();
        synchronized (this.LOCK) {
            long nanoSecondsToMills = nanoSecondsToMills(System.nanoTime());
            for (FormAdapter.RefreshRunnable refreshRunnable : this.tasks.values()) {
                if (refreshRunnable.refreshTime == Long.MAX_VALUE || nanoSecondsToMills - refreshRunnable.refreshTime >= refreshRunnable.period || Math.abs((nanoSecondsToMills - refreshRunnable.refreshTime) - refreshRunnable.period) < ABS_TIME) {
                    refreshRunnable.refreshTime = nanoSecondsToMills;
                    arrayList.add(refreshRunnable);
                }
            }
            workThreadPoolLocked = getWorkThreadPoolLocked();
        }
        if (!(workThreadPoolLocked == null || arrayList.isEmpty())) {
            for (FormAdapter.RefreshRunnable refreshRunnable2 : arrayList) {
                workThreadPoolLocked.execute(refreshRunnable2);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onUpdateAtTrigger(int i) {
        List<UpdateAtItem> findMatchedUpdateItemLocked;
        ExecutorService workThreadPoolLocked;
        HiLog.debug(LABEL_LOG, "onUpdateAtTrigger, updateTime:%{public}d", Integer.valueOf(i));
        synchronized (this.LOCK) {
            findMatchedUpdateItemLocked = findMatchedUpdateItemLocked(i);
            workThreadPoolLocked = (findMatchedUpdateItemLocked == null || findMatchedUpdateItemLocked.isEmpty()) ? null : getWorkThreadPoolLocked();
            updateAlarmLocked();
        }
        if (!(findMatchedUpdateItemLocked == null || findMatchedUpdateItemLocked.isEmpty() || workThreadPoolLocked == null)) {
            HiLog.info(LABEL_LOG, "updateat timer triggered, trigged time: %{public}d", Integer.valueOf(i));
            for (UpdateAtItem updateAtItem : findMatchedUpdateItemLocked) {
                workThreadPoolLocked.execute(updateAtItem.refreshTask);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void handleSystemTimeChanged() {
        HiLog.debug(LABEL_LOG, "handleSystemTimeChanged", new Object[0]);
        synchronized (this.LOCK) {
            if (!this.updateAtTasks.isEmpty()) {
                updateAlarmLocked();
            }
        }
    }

    private boolean addIntervalTimer(FormAdapter.RefreshRunnable refreshRunnable) {
        boolean addIntervalTimerLocked;
        if (refreshRunnable.period < MIN_PERIOD || refreshRunnable.period > MAX_PERIOD || refreshRunnable.period % MIN_PERIOD != 0) {
            HiLog.error(LABEL_LOG, "addIntervalTimer invalid param", new Object[0]);
            return false;
        }
        synchronized (this.LOCK) {
            addIntervalTimerLocked = addIntervalTimerLocked(refreshRunnable);
        }
        return addIntervalTimerLocked;
    }

    private boolean addUpdateAtTimer(FormAdapter.RefreshRunnable refreshRunnable) {
        boolean addUpdateAtTimerLocked;
        if (refreshRunnable.hour < 0 || refreshRunnable.hour > 23 || refreshRunnable.min < 0 || refreshRunnable.min > 59) {
            HiLog.error(LABEL_LOG, "addUpdateAtTimer time is invalid", new Object[0]);
            return false;
        }
        synchronized (this.LOCK) {
            addUpdateAtTimerLocked = addUpdateAtTimerLocked(refreshRunnable);
        }
        return addUpdateAtTimerLocked;
    }

    private void ensureInitBaseTimerLocked() {
        if (this.baseTimerTask == null) {
            HiLog.info(LABEL_LOG, "init base timer task", new Object[0]);
            ScheduledThreadPoolExecutor baseTimerExecutorLocked = getBaseTimerExecutorLocked();
            TimerRunnable timerRunnable = new TimerRunnable(null);
            long j = MIN_PERIOD;
            ScheduledFuture<?> scheduleAtFixedRate = baseTimerExecutorLocked.scheduleAtFixedRate(timerRunnable, j, j, TimeUnit.MILLISECONDS);
            if (scheduleAtFixedRate instanceof RunnableScheduledFuture) {
                this.baseTimerTask = (RunnableScheduledFuture) scheduleAtFixedRate;
            }
        }
    }

    private boolean addIntervalTimerLocked(FormAdapter.RefreshRunnable refreshRunnable) {
        if (this.tasks.containsKey(Long.valueOf(refreshRunnable.formId))) {
            HiLog.error(LABEL_LOG, "already exist formTimer, formId:%{public}d task", Long.valueOf(refreshRunnable.formId));
            return false;
        }
        ensureInitBaseTimerLocked();
        this.tasks.put(Long.valueOf(refreshRunnable.formId), refreshRunnable);
        HiLog.debug(LABEL_LOG, "addIntervalTimerLocked end", new Object[0]);
        return true;
    }

    private boolean addUpdateAtTimerLocked(FormAdapter.RefreshRunnable refreshRunnable) {
        Iterator<UpdateAtItem> it = this.updateAtTasks.iterator();
        while (it.hasNext()) {
            if (it.next().refreshTask.formId == refreshRunnable.formId) {
                HiLog.error(LABEL_LOG, "already exist formTimer, formId:%{public}d task", Long.valueOf(refreshRunnable.formId));
                return false;
            }
        }
        if (!initTimerReceiverLocked()) {
            HiLog.error(LABEL_LOG, "faied to init timer task handler.", new Object[0]);
            return false;
        }
        UpdateAtItem updateAtItem = new UpdateAtItem();
        updateAtItem.updateAtTime = (refreshRunnable.hour * 60) + refreshRunnable.min;
        updateAtItem.refreshTask = refreshRunnable;
        addItemLocked(updateAtItem);
        if (!updateAlarmLocked()) {
            HiLog.error(LABEL_LOG, "faied to update alarm.", new Object[0]);
            return false;
        }
        HiLog.info(LABEL_LOG, "FormAdapter updateAt timer,time %{public}d:%{public}d", Integer.valueOf(refreshRunnable.hour), Integer.valueOf(refreshRunnable.min));
        return true;
    }

    private void deleteIntervalTimerLocked(long j) {
        if (this.tasks.get(Long.valueOf(j)) != null) {
            this.tasks.remove(Long.valueOf(j));
            if (this.tasks.isEmpty()) {
                clearIntervalTimerResourceLocked();
            }
        }
    }

    private void deleteUpdateAtTimerLocked(long j) {
        UpdateAtItem updateAtItem;
        Iterator<UpdateAtItem> it = this.updateAtTasks.iterator();
        while (true) {
            if (!it.hasNext()) {
                updateAtItem = null;
                break;
            }
            updateAtItem = it.next();
            if (updateAtItem.refreshTask.formId == j) {
                it.remove();
                break;
            }
        }
        if (updateAtItem != null) {
            updateAlarmLocked();
        }
    }

    private void clearIntervalTimerResourceLocked() {
        ExecutorService executorService;
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = this.timerExecutor;
        if (scheduledThreadPoolExecutor != null) {
            scheduledThreadPoolExecutor.shutdownNow();
            this.timerExecutor = null;
        }
        if (this.baseTimerTask != null) {
            this.baseTimerTask = null;
        }
        if (this.updateAtTasks.isEmpty() && (executorService = this.workThreadPool) != null) {
            executorService.shutdownNow();
            this.workThreadPool = null;
        }
    }

    private void clearUpdateAtTimerResourceLocked() {
        AlarmManager alarmManagerLocked;
        ExecutorService executorService;
        if (this.updateAtTasks.isEmpty() && (alarmManagerLocked = getAlarmManagerLocked()) != null) {
            PendingIntent pendingIntent = this.currentPendingIntent;
            if (pendingIntent != null) {
                alarmManagerLocked.cancel(pendingIntent);
                this.currentPendingIntent = null;
            }
            this.wakeUpTime = Integer.MAX_VALUE;
            if (this.tasks.isEmpty() && (executorService = this.workThreadPool) != null) {
                executorService.shutdownNow();
                this.workThreadPool = null;
            }
        }
    }

    private void updateIntervalTimer(long j, FormAdapter.FormTimerCfg formTimerCfg) {
        if (formTimerCfg.updateDuration < MIN_PERIOD || formTimerCfg.updateDuration > MAX_PERIOD || formTimerCfg.updateDuration % MIN_PERIOD != 0) {
            HiLog.error(LABEL_LOG, "updateIntervalTimer invalid param", new Object[0]);
            return;
        }
        synchronized (this.LOCK) {
            FormAdapter.RefreshRunnable refreshRunnable = this.tasks.get(Long.valueOf(j));
            if (refreshRunnable != null) {
                refreshRunnable.period = formTimerCfg.updateDuration;
            }
        }
    }

    private void updateUpdateAtTimer(long j, FormAdapter.FormTimerCfg formTimerCfg) {
        if (formTimerCfg.updateAtHour < 0 || formTimerCfg.updateAtHour > 23 || formTimerCfg.updateAtMin < 0 || formTimerCfg.updateAtMin > 59) {
            HiLog.error(LABEL_LOG, "updateUpdateAtTimer time is invalid", new Object[0]);
            return;
        }
        synchronized (this.LOCK) {
            UpdateAtItem updateAtItem = null;
            Iterator<UpdateAtItem> it = this.updateAtTasks.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                UpdateAtItem next = it.next();
                if (next.refreshTask.formId == j) {
                    it.remove();
                    updateAtItem = next;
                    break;
                }
            }
            if (updateAtItem != null) {
                updateAtItem.refreshTask.hour = formTimerCfg.updateAtHour;
                updateAtItem.refreshTask.min = formTimerCfg.updateAtMin;
                updateAtItem.updateAtTime = (updateAtItem.refreshTask.hour * 60) + updateAtItem.refreshTask.min;
                addItemLocked(updateAtItem);
                updateAlarmLocked();
            }
        }
    }

    private void intervalToUpdateAtTimer(long j, FormAdapter.FormTimerCfg formTimerCfg) {
        if (formTimerCfg.updateAtHour < 0 || formTimerCfg.updateAtHour > 23 || formTimerCfg.updateAtMin < 0 || formTimerCfg.updateAtMin > 59) {
            HiLog.error(LABEL_LOG, "intervalToUpdateAtTimer time is invalid", new Object[0]);
            return;
        }
        synchronized (this.LOCK) {
            FormAdapter.RefreshRunnable refreshRunnable = this.tasks.get(Long.valueOf(j));
            if (refreshRunnable != null) {
                this.tasks.remove(Long.valueOf(j));
                refreshRunnable.isUpdateAt = true;
                refreshRunnable.hour = formTimerCfg.updateAtHour;
                refreshRunnable.min = formTimerCfg.updateAtMin;
                if (!addUpdateAtTimerLocked(refreshRunnable)) {
                    HiLog.error(LABEL_LOG, "intervalToUpdateAtTimer add task failed", new Object[0]);
                }
            }
        }
    }

    private void updateAtToIntervalTimer(long j, FormAdapter.FormTimerCfg formTimerCfg) {
        if (formTimerCfg.updateDuration < MIN_PERIOD || formTimerCfg.updateDuration > MAX_PERIOD || formTimerCfg.updateDuration % MIN_PERIOD != 0) {
            HiLog.error(LABEL_LOG, "updateAtToIntervalTimer invalid updateDuration param", new Object[0]);
            return;
        }
        synchronized (this.LOCK) {
            UpdateAtItem updateAtItem = null;
            Iterator<UpdateAtItem> it = this.updateAtTasks.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                UpdateAtItem next = it.next();
                if (next.refreshTask.formId == j) {
                    it.remove();
                    updateAtItem = next;
                    break;
                }
            }
            if (updateAtItem != null) {
                updateAlarmLocked();
                updateAtItem.refreshTask.isUpdateAt = false;
                updateAtItem.refreshTask.period = formTimerCfg.updateDuration;
                updateAtItem.refreshTask.refreshTime = Long.MAX_VALUE;
                if (!addIntervalTimerLocked(updateAtItem.refreshTask)) {
                    HiLog.error(LABEL_LOG, "updateAtToIntervalTimer add task failed", new Object[0]);
                }
            }
        }
    }

    private ScheduledThreadPoolExecutor getBaseTimerExecutorLocked() {
        if (this.timerExecutor == null) {
            this.timerExecutor = new ScheduledThreadPoolExecutor(1);
        }
        return this.timerExecutor;
    }

    private ExecutorService getWorkThreadPoolLocked() {
        if (this.workThreadPool == null) {
            this.workThreadPool = Executors.newFixedThreadPool(4);
        }
        return this.workThreadPool;
    }

    private AlarmManager getAlarmManagerLocked() {
        Context context;
        if (this.alarmManager == null && (context = FormAdapter.getInstance().getContext()) != null) {
            Object systemService = context.getSystemService(NotificationRequest.CLASSIFICATION_ALARM);
            if (systemService instanceof AlarmManager) {
                this.alarmManager = (AlarmManager) systemService;
            }
        }
        return this.alarmManager;
    }

    private boolean initTimerReceiverLocked() {
        if (this.timerReceiver != null) {
            return true;
        }
        Context context = FormAdapter.getInstance().getContext();
        if (context == null) {
            return false;
        }
        this.timerReceiver = new TimerReceiver(null);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_UPDATEATTIMER);
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        context.registerReceiver(this.timerReceiver, intentFilter);
        return true;
    }

    private void addItemLocked(UpdateAtItem updateAtItem) {
        if (this.updateAtTasks.isEmpty()) {
            this.updateAtTasks.add(updateAtItem);
        } else if (updateAtItem.updateAtTime < this.updateAtTasks.getFirst().updateAtTime) {
            this.updateAtTasks.addFirst(updateAtItem);
        } else {
            boolean z = false;
            Iterator<UpdateAtItem> it = this.updateAtTasks.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                UpdateAtItem next = it.next();
                if (updateAtItem.updateAtTime < next.updateAtTime) {
                    this.updateAtTasks.add(this.updateAtTasks.indexOf(next), updateAtItem);
                    z = true;
                    break;
                }
            }
            if (!z) {
                this.updateAtTasks.addLast(updateAtItem);
            }
        }
    }

    private boolean updateAlarmLocked() {
        AlarmManager alarmManagerLocked = getAlarmManagerLocked();
        if (alarmManagerLocked == null) {
            HiLog.error(LABEL_LOG, "faied to get alarm manager, can not updateAlarm", new Object[0]);
            return false;
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        long currentTimeMillis = System.currentTimeMillis();
        UpdateAtItem findNextUpdateItemLocked = findNextUpdateItemLocked((instance.get(11) * 60) + instance.get(12));
        if (findNextUpdateItemLocked == null) {
            clearUpdateAtTimerResourceLocked();
            HiLog.debug(LABEL_LOG, "no update at task in system now.", new Object[0]);
            return true;
        }
        int i = findNextUpdateItemLocked.updateAtTime;
        Calendar instance2 = Calendar.getInstance();
        instance2.setTimeInMillis(currentTimeMillis);
        instance2.set(11, findNextUpdateItemLocked.refreshTask.hour);
        instance2.set(5, Calendar.getInstance().get(5));
        instance2.set(12, findNextUpdateItemLocked.refreshTask.min);
        instance2.set(13, 0);
        instance2.set(14, 0);
        if (instance2.getTimeInMillis() < currentTimeMillis) {
            instance2.add(5, 1);
            i += 1440;
        }
        if (i == this.wakeUpTime) {
            HiLog.warn(LABEL_LOG, "wakeUpTime not change, no need update alarm.", new Object[0]);
            return true;
        }
        PendingIntent pendingIntent = getPendingIntent(findNextUpdateItemLocked.updateAtTime);
        if (pendingIntent == null) {
            HiLog.error(LABEL_LOG, "create pendingIntent failed.", new Object[0]);
            return false;
        }
        this.wakeUpTime = i;
        PendingIntent pendingIntent2 = this.currentPendingIntent;
        if (pendingIntent2 != null) {
            alarmManagerLocked.cancel(pendingIntent2);
        }
        this.currentPendingIntent = pendingIntent;
        alarmManagerLocked.setExact(0, instance2.getTimeInMillis(), pendingIntent);
        return true;
    }

    private UpdateAtItem findNextUpdateItemLocked(int i) {
        UpdateAtItem updateAtItem = null;
        if (this.updateAtTasks.isEmpty()) {
            return null;
        }
        Iterator<UpdateAtItem> it = this.updateAtTasks.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            UpdateAtItem next = it.next();
            if (next.updateAtTime > i) {
                updateAtItem = next;
                break;
            }
        }
        return updateAtItem == null ? this.updateAtTasks.getFirst() : updateAtItem;
    }

    private List<UpdateAtItem> findMatchedUpdateItemLocked(int i) {
        ArrayList arrayList = new ArrayList();
        Iterator<UpdateAtItem> it = this.updateAtTasks.iterator();
        while (it.hasNext()) {
            UpdateAtItem next = it.next();
            if (next.updateAtTime == i) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    private PendingIntent getPendingIntent(int i) {
        HiLog.debug(LABEL_LOG, "set new updateAtTime is : %{public}d", Integer.valueOf(i));
        Context context = FormAdapter.getInstance().getContext();
        if (context == null) {
            return null;
        }
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATEATTIMER);
        intent.setPackage(FOUNDATION_NAME);
        intent.putExtra(KEY_WAKEUP_TIME, i);
        return PendingIntent.getBroadcast(context, 0, intent, 134217728);
    }

    static long nanoSecondsToMills(long j) {
        return (j / 1000) / 1000;
    }

    /* access modifiers changed from: package-private */
    public static class UpdateAtItem {
        FormAdapter.RefreshRunnable refreshTask;
        int updateAtTime = -1;

        UpdateAtItem() {
        }
    }

    /* access modifiers changed from: private */
    public static class TimerRunnable implements Runnable {
        private TimerRunnable() {
        }

        /* synthetic */ TimerRunnable(AnonymousClass1 r1) {
            this();
        }

        public void run() {
            FormTimerManager.getInstance().onTimeOut();
        }
    }

    /* access modifiers changed from: private */
    public static class TimerReceiver extends BroadcastReceiver {
        private TimerReceiver() {
        }

        /* synthetic */ TimerReceiver(AnonymousClass1 r1) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if ("android.intent.action.TIME_SET".equals(action) || "android.intent.action.TIMEZONE_CHANGED".equals(action)) {
                    HiLog.info(FormTimerManager.LABEL_LOG, "TimerReceiver action:%{public}s.", action);
                    FormTimerManager.getInstance().handleSystemTimeChanged();
                } else if (FormTimerManager.ACTION_UPDATEATTIMER.equals(action)) {
                    int intExtra = intent.getIntExtra(FormTimerManager.KEY_WAKEUP_TIME, -1);
                    if (intExtra < 0) {
                        HiLog.info(FormTimerManager.LABEL_LOG, "TimerReceiver invalid updateTime:%{public}d.", Integer.valueOf(intExtra));
                        return;
                    }
                    FormTimerManager.getInstance().onUpdateAtTrigger(intExtra);
                } else {
                    HiLog.error(FormTimerManager.LABEL_LOG, "invalid action.", new Object[0]);
                }
            }
        }
    }
}
