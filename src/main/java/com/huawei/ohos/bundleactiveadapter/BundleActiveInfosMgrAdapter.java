package com.huawei.ohos.bundleactiveadapter;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import ohos.app.Context;

public final class BundleActiveInfosMgrAdapter {
    public static final int BY_ANNUALLY = 4;
    public static final int BY_DAILY = 1;
    public static final int BY_MONTHLY = 3;
    public static final int BY_OPTIMIZED = 0;
    public static final int BY_WEEKLY = 2;
    public static final int USAGE_PRIORITY_GROUP_ALIVE = 1;
    public static final int USAGE_PRIORITY_GROUP_NEVER = 5;
    public static final int USAGE_PRIORITY_GROUP_OFTEN = 3;
    public static final int USAGE_PRIORITY_GROUP_PREFERENCE = 2;
    public static final int USAGE_PRIORITY_GROUP_PRIVILEGE = 0;
    public static final int USAGE_PRIORITY_GROUP_SELDOM = 4;
    private final UsageStatsManager mUsageStateManager;

    private BundleActiveInfosMgrAdapter(UsageStatsManager usageStatsManager) {
        this.mUsageStateManager = usageStatsManager;
    }

    public static BundleActiveInfosMgrAdapter newInstance(Context context) {
        if (context == null) {
            return null;
        }
        Object hostContext = context.getHostContext();
        if (!(hostContext instanceof android.content.Context)) {
            return null;
        }
        Object systemService = ((android.content.Context) hostContext).getSystemService("usagestats");
        if (!(systemService instanceof UsageStatsManager)) {
            return null;
        }
        return new BundleActiveInfosMgrAdapter((UsageStatsManager) systemService);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x002a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<com.huawei.ohos.bundleactiveadapter.BundleActiveInfosAdapter> queryBundleActiveInfosByInterval(int r9, long r10, long r12) {
        /*
            r8 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            if (r9 < 0) goto L_0x0032
            r1 = 4
            if (r9 <= r1) goto L_0x000b
            goto L_0x0032
        L_0x000b:
            r2 = 3
            r3 = 2
            r4 = 1
            if (r9 == 0) goto L_0x0018
            if (r9 == r4) goto L_0x001e
            if (r9 == r3) goto L_0x001c
            if (r9 == r2) goto L_0x0020
            if (r9 == r1) goto L_0x001a
        L_0x0018:
            r3 = r1
            goto L_0x0020
        L_0x001a:
            r3 = r2
            goto L_0x0020
        L_0x001c:
            r3 = r4
            goto L_0x0020
        L_0x001e:
            r1 = 0
            goto L_0x0018
        L_0x0020:
            android.app.usage.UsageStatsManager r2 = r8.mUsageStateManager
            r4 = r10
            r6 = r12
            java.util.List r8 = r2.queryUsageStats(r3, r4, r6)
            if (r8 == 0) goto L_0x0032
            com.huawei.ohos.bundleactiveadapter.-$$Lambda$BundleActiveInfosMgrAdapter$cvuNA7rebcoJ19Frrl-YI_FeZwE r9 = new com.huawei.ohos.bundleactiveadapter.-$$Lambda$BundleActiveInfosMgrAdapter$cvuNA7rebcoJ19Frrl-YI_FeZwE
            r9.<init>(r0)
            r8.forEach(r9)
        L_0x0032:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.ohos.bundleactiveadapter.BundleActiveInfosMgrAdapter.queryBundleActiveInfosByInterval(int, long, long):java.util.List");
    }

    public Map<String, BundleActiveInfosAdapter> queryBundleActiveInfos(long j, long j2) {
        HashMap hashMap = new HashMap();
        Map<String, UsageStats> queryAndAggregateUsageStats = this.mUsageStateManager.queryAndAggregateUsageStats(j, j2);
        if (queryAndAggregateUsageStats != null) {
            queryAndAggregateUsageStats.forEach(new BiConsumer(hashMap) {
                /* class com.huawei.ohos.bundleactiveadapter.$$Lambda$BundleActiveInfosMgrAdapter$eZWGYFzb1w4NmEvMqOi9UzVabuU */
                private final /* synthetic */ HashMap f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    BundleActiveInfosMgrAdapter.lambda$queryBundleActiveInfos$1(this.f$0, (String) obj, (UsageStats) obj2);
                }
            });
        }
        return hashMap;
    }

    public BundleActiveStatesAdapter queryBundleActiveStates(long j, long j2) {
        return new BundleActiveStatesAdapter(this.mUsageStateManager.queryEvents(j, j2));
    }

    public BundleActiveStatesAdapter queryCurrentBundleActiveStates(long j, long j2) {
        return new BundleActiveStatesAdapter(this.mUsageStateManager.queryEventsForSelf(j, j2));
    }

    public boolean isIdleState(String str) {
        return this.mUsageStateManager.isAppInactive(str);
    }

    public int queryAppUsagePriorityGroup() {
        int appStandbyBucket = this.mUsageStateManager.getAppStandbyBucket();
        if (appStandbyBucket == 5) {
            return 0;
        }
        if (appStandbyBucket == 10) {
            return 1;
        }
        if (appStandbyBucket == 20) {
            return 2;
        }
        if (appStandbyBucket == 30) {
            return 3;
        }
        if (appStandbyBucket == 40) {
            return 4;
        }
        if (appStandbyBucket != 50) {
        }
        return 5;
    }
}
