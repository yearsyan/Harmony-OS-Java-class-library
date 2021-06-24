package ohos.workschedulerservice;

import android.content.Context;
import android.content.SharedPreferences;
import com.huawei.ohos.workscheduleradapter.WorkschedulerAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.workscheduler.WorkInfo;
import ohos.workschedulerservice.controller.WorkStatus;

public final class WorkStore {
    private static final int ABILITY_NAME_INDEX = 2;
    private static final int BATTERY_LEVEL_INDEX = 4;
    private static final int BATTERY_STATUS_INDEX = 5;
    private static final int BUNDLE_NAME_INDEX = 1;
    private static final int CHARGE_TYPE_INDEX = 3;
    private static final int CONDITION_CHARGE = 4;
    private static final int CONDITION_DEVICE_IDLE = 2;
    private static final int CONDITION_PERSIST = 32;
    private static final int CONDITION_REPEAT = 8;
    private static final int CONDITION_STORAGE = 64;
    private static final int CYCLETIME_INDEX = 8;
    private static final String FILE_NAME = "persisted_work";
    private static final int IDLE_WAIT_INDEX = 2;
    private static final int KEY_SIZE = 5;
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, 218109696, TAG);
    private static final int NETWORK_INDEX = 1;
    private static final int REPEAT_COUNTER_INDEX = 6;
    private static final int STATUE_INDEX = 0;
    private static final int STORAGE_INDEX = 7;
    private static final String TAG = "WorkStore";
    private static final int UID_INDEX = 3;
    private static final int USERID_INDEX = 4;
    private static final int VALUE_SIZE = 9;
    private static final int WORKID_INDEX = 0;
    private SharedPreferences preferences;
    private Context workContext;

    public WorkStore(Context context) {
        if (context == null) {
            HiLog.warn(LOG_LABEL, "context is null.", new Object[0]);
            return;
        }
        this.workContext = context;
        this.preferences = this.workContext.createDeviceProtectedStorageContext().getSharedPreferences(FILE_NAME, 32768);
    }

    public void readWorkStatusAsync(WorkQueueManager workQueueManager) {
        if (workQueueManager != null) {
            List<WorkStatus> readWorkStatus = readWorkStatus();
            HiLog.info(LOG_LABEL, "Read work start.", new Object[0]);
            for (WorkStatus workStatus : readWorkStatus) {
                workQueueManager.tryStartSignWork(workStatus.getWork(), workStatus.getUid(), workStatus.getUserId());
            }
        }
    }

    public void writeWorkStatusAsync(WorkStatus workStatus) {
        if (workStatus != null && workStatus.hasPersistCondition()) {
            HiLog.info(LOG_LABEL, "write work.", new Object[0]);
            writeWorkStatus(workStatus);
        }
    }

    public void removeWorkStatusAsync(WorkStatus workStatus) {
        if (workStatus != null && workStatus.hasPersistCondition()) {
            removeWorkStatus(workStatus);
        }
    }

    public void clearWorkStatusAsync() {
        clearWorkStatus();
    }

    private void writeWorkStatus(WorkStatus workStatus) {
        SharedPreferences sharedPreferences = this.preferences;
        if (sharedPreferences != null) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            HiLog.debug(LOG_LABEL, "writeWorkStatus workid : %{private}d", Integer.valueOf(workStatus.getWorkId()));
            boolean commit = edit.putString(generateKey(workStatus), generateValue(workStatus)).commit();
            HiLog.debug(LOG_LABEL, "commit ret %{public}s", Boolean.valueOf(commit));
        }
    }

    private void removeWorkStatus(WorkStatus workStatus) {
        SharedPreferences sharedPreferences = this.preferences;
        if (sharedPreferences != null) {
            sharedPreferences.edit().remove(generateKey(workStatus)).commit();
        }
    }

    private void clearWorkStatus() {
        SharedPreferences sharedPreferences = this.preferences;
        if (sharedPreferences != null) {
            sharedPreferences.edit().clear().commit();
        }
    }

    private List<WorkStatus> readWorkStatus() {
        SharedPreferences sharedPreferences = this.preferences;
        if (sharedPreferences == null || this.workContext == null) {
            HiLog.warn(LOG_LABEL, "readWorkStatus failed, maybe context is null.", new Object[0]);
            return new ArrayList();
        }
        Map<String, ?> all = sharedPreferences.getAll();
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            BaseWorkInfo buildBaseWorkInfo = buildBaseWorkInfo(entry.getKey());
            if (buildBaseWorkInfo == null) {
                HiLog.debug(LOG_LABEL, "readWorkStatus BaseWorkInfo invalid, continue.", new Object[0]);
            } else if (!(entry.getValue() instanceof String)) {
                HiLog.debug(LOG_LABEL, "readWorkStatus map value invalid, continue.", new Object[0]);
            } else {
                WorkInfo buildWorkInfo = buildWorkInfo((String) entry.getValue(), buildBaseWorkInfo);
                if (buildWorkInfo == null) {
                    HiLog.debug(LOG_LABEL, "readWorkStatus workInfo invalid, continue.", new Object[0]);
                } else {
                    WorkStatus generateWorkStatus = WorkStatus.generateWorkStatus(buildWorkInfo, buildBaseWorkInfo.getUid(), buildBaseWorkInfo.getUserId());
                    generateWorkStatus.updateActiveLevel(WorkschedulerAdapter.getStandbyLevel(this.workContext, generateWorkStatus.getBundleName()));
                    arrayList.add(generateWorkStatus);
                }
            }
        }
        return arrayList;
    }

    private BaseWorkInfo buildBaseWorkInfo(String str) {
        String[] split = str.split("#");
        if (split.length < 5) {
            HiLog.error(LOG_LABEL, "buildBaseWorkInfo baseInfo invalid.", new Object[0]);
            return null;
        }
        Optional<Integer> parseToInt = parseToInt(split[0]);
        Optional<Integer> parseToInt2 = parseToInt(split[3]);
        Optional<Integer> parseToInt3 = parseToInt(split[4]);
        if (!parseToInt.isPresent() || !parseToInt2.isPresent() || !parseToInt3.isPresent()) {
            HiLog.error(LOG_LABEL, "buildBaseWorkInfo id invalid.", new Object[0]);
            return null;
        }
        String str2 = split[1];
        String str3 = split[2];
        if (str2 != null && str3 != null && !str2.isEmpty() && !str3.isEmpty()) {
            return new BaseWorkInfo(new WorkInfo.Builder().setAbilityInfo(parseToInt.get().intValue(), str2, str3), parseToInt2.get().intValue(), parseToInt3.get().intValue());
        }
        HiLog.error(LOG_LABEL, "buildBaseWorkInfo bundleName or abilityName invalid.", new Object[0]);
        return null;
    }

    private WorkInfo buildWorkInfo(String str, BaseWorkInfo baseWorkInfo) {
        String[] split = str.split("#");
        if (split.length < 9) {
            HiLog.error(LOG_LABEL, "buildWorkInfo values invalid.", new Object[0]);
            return null;
        }
        Optional<Integer> parseToInt = parseToInt(split[0]);
        Optional<Integer> parseToInt2 = parseToInt(split[1]);
        Optional<Integer> parseToInt3 = parseToInt(split[2]);
        Optional<Integer> parseToInt4 = parseToInt(split[3]);
        Optional<Integer> parseToInt5 = parseToInt(split[5]);
        Optional<Integer> parseToInt6 = parseToInt(split[7]);
        Optional<Integer> parseToInt7 = parseToInt(split[6]);
        Optional<Long> parseToLong = parseToLong(split[8]);
        if (!parseToInt.isPresent() || !parseToInt2.isPresent() || !parseToInt3.isPresent() || !parseToInt4.isPresent() || !parseToInt5.isPresent() || !parseToInt6.isPresent() || !parseToInt7.isPresent() || !parseToLong.isPresent()) {
            HiLog.error(LOG_LABEL, "buildWorkInfo id invalid.", new Object[0]);
            return null;
        }
        WorkInfo.Builder builder = baseWorkInfo.getBuilder();
        WorkCondition resolveWorkCondition = resolveWorkCondition(parseToInt.get().intValue());
        requestNetwork(builder, parseToInt2.get().intValue());
        requestDeviceIdleType(builder, resolveWorkCondition.isIdle(), parseToInt3.get().intValue());
        requestChargingType(builder, resolveWorkCondition.isCharging(), parseToInt4.get().intValue());
        requestBatteryStatus(builder, parseToInt5.get().intValue());
        requestStorageStatus(builder, parseToInt6.get().intValue());
        builder.requestPersisted(resolveWorkCondition.isPersisted());
        requestRepeatCycle(builder, parseToLong.get().longValue(), parseToInt7.get().intValue());
        return builder.build();
    }

    private void requestDeviceIdleType(WorkInfo.Builder builder, boolean z, int i) {
        if (i >= 60000 && i <= 1200000) {
            builder.requestDeviceIdleType(z, i);
        }
    }

    private void requestBatteryStatus(WorkInfo.Builder builder, int i) {
        if (i > 0 && i <= 2) {
            builder.requestBatteryStatus(i);
        }
    }

    private void requestStorageStatus(WorkInfo.Builder builder, int i) {
        if (i > 0 && i <= 2) {
            builder.requestStorageStatus(i);
        }
    }

    private void requestRepeatCycle(WorkInfo.Builder builder, long j, int i) {
        if (j >= 1200000 && i >= 1) {
            builder.requestRepeatCycle(j, i);
        }
    }

    private void requestNetwork(WorkInfo.Builder builder, int i) {
        if (i >= 0) {
            int i2 = 0;
            while (i != 0) {
                if ((i & 1) != 0) {
                    if (i2 <= 5) {
                        builder.requestNetworkType(i2);
                    } else {
                        return;
                    }
                }
                i >>= 1;
                i2++;
            }
        }
    }

    private void requestChargingType(WorkInfo.Builder builder, boolean z, int i) {
        if (i >= 0) {
            int i2 = 0;
            while (i != 0) {
                if ((i & 1) != 0) {
                    if (i2 <= 3) {
                        builder.requestChargingType(z, i2);
                    } else {
                        return;
                    }
                }
                i >>= 1;
                i2++;
            }
        }
    }

    private WorkCondition resolveWorkCondition(int i) {
        WorkCondition workCondition = new WorkCondition();
        if ((i & 2) != 0) {
            workCondition.setIdle(true);
        }
        if ((i & 4) != 0) {
            workCondition.setCharging(true);
        }
        if ((i & 8) != 0) {
            workCondition.setRepeated(true);
        }
        if ((i & 32) != 0) {
            workCondition.setPersisted(true);
        }
        return workCondition;
    }

    private String generateKey(WorkStatus workStatus) {
        return workStatus.getWorkId() + '#' + workStatus.getWork().getBundleName() + '#' + workStatus.getWork().getAbilityName() + '#' + workStatus.getUid() + '#' + workStatus.getUserId();
    }

    private String generateValue(WorkStatus workStatus) {
        StringBuilder sb = new StringBuilder();
        sb.append(workStatus.getRequestStatus());
        sb.append('#');
        sb.append(workStatus.getNetworkType());
        sb.append('#');
        sb.append(workStatus.getWork().getRequestIdleWaitTime());
        sb.append('#');
        sb.append(workStatus.getWork().getRequestChargeType());
        sb.append('#');
        sb.append(workStatus.getWork().getRequestBatteryLevel());
        sb.append('#');
        sb.append(workStatus.getWork().getRequestBatteryStatus());
        sb.append('#');
        sb.append(workStatus.getWork().getRepeatCounter());
        sb.append('#');
        sb.append(workStatus.getWork().getRequestStorageType());
        sb.append('#');
        sb.append(workStatus.getWork().getRepeatCycleTime());
        return sb.toString();
    }

    private Optional<Integer> parseToInt(String str) {
        try {
            return Optional.of(Integer.valueOf(Integer.parseInt(str)));
        } catch (NumberFormatException unused) {
            HiLog.debug(LOG_LABEL, "parseToInt failed, value : %{private}s.", str);
            return Optional.empty();
        }
    }

    private Optional<Long> parseToLong(String str) {
        try {
            return Optional.of(Long.valueOf(Long.parseLong(str)));
        } catch (NumberFormatException unused) {
            HiLog.debug(LOG_LABEL, "parseToLong failed, value : %{private}s.", str);
            return Optional.empty();
        }
    }

    /* access modifiers changed from: private */
    public static final class BaseWorkInfo {
        private final WorkInfo.Builder builder;
        private final int uid;
        private final int userId;

        public BaseWorkInfo(WorkInfo.Builder builder2, int i, int i2) {
            this.builder = builder2;
            this.uid = i;
            this.userId = i2;
        }

        public WorkInfo.Builder getBuilder() {
            return this.builder;
        }

        public int getUid() {
            return this.uid;
        }

        public int getUserId() {
            return this.userId;
        }
    }

    /* access modifiers changed from: private */
    public static final class WorkCondition {
        private boolean requestCharging = false;
        private boolean requestIdle = false;
        private boolean requestPersisted = false;
        private boolean requestRepeated = false;

        public boolean isIdle() {
            return this.requestIdle;
        }

        public boolean isCharging() {
            return this.requestCharging;
        }

        public boolean isRepeated() {
            return this.requestRepeated;
        }

        public boolean isPersisted() {
            return this.requestPersisted;
        }

        public void setIdle(boolean z) {
            this.requestIdle = z;
        }

        public void setCharging(boolean z) {
            this.requestCharging = z;
        }

        public void setRepeated(boolean z) {
            this.requestRepeated = z;
        }

        public void setPersisted(boolean z) {
            this.requestPersisted = z;
        }
    }
}
