package ohos.abilityshell.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.abilityshell.AbilityShellData;
import ohos.appexecfwk.utils.AppLog;
import ohos.bundle.AbilityInfo;
import ohos.bundle.BundleInfo;
import ohos.bundle.ShellInfo;
import ohos.hiviewdfx.HiLogLabel;

public class AbilityShellConverterUtils {
    private static final String FORM_SERVICE_SHELL_SUFFIX = "ShellServiceForm";
    private static final String PAGE_SHELL_SUFFIX = "ShellActivity";
    private static final String PROVIDER_SHELL_SUFFIX = "ShellProvider";
    private static final String SERVICE_SHELL_SUFFIX = "ShellService";
    private static final int SHELL_ACTIVITY = 1;
    private static final HiLogLabel SHELL_LABEL = new HiLogLabel(3, 218108160, "AbilityShell");
    private static final int SHELL_SERVICE = 2;
    private static final String SHELL_TYPE = "shellType";

    private AbilityShellConverterUtils() {
    }

    private static boolean isDifferentPkg(AbilityInfo abilityInfo) {
        String bundleName = abilityInfo.getBundleName();
        return isNotEmpty(bundleName) && isNotEmpty(abilityInfo.getOriginalBundleName()) && !bundleName.equals(abilityInfo.getOriginalBundleName());
    }

    public static ShellInfo convertToShellInfo(AbilityInfo abilityInfo) {
        if (abilityInfo == null || abilityInfo.getType() == null) {
            AppLog.e(SHELL_LABEL, "AbilityShellConverterUtils::convertToShellInfo param invalid", new Object[0]);
            return null;
        }
        AppLog.d(SHELL_LABEL, "AbilityShellConverterUtils::convertToShellInfo Ability bundleName= %{public}s, packageName=%{public}s, className: %{public}s, originalClassName=%{public}s, type=%{public}s", abilityInfo.getBundleName(), abilityInfo.getOriginalBundleName(), abilityInfo.getClassName(), abilityInfo.getOriginalClassName(), abilityInfo.getType());
        ShellInfo shellInfo = new ShellInfo();
        boolean isDifferentPkg = isDifferentPkg(abilityInfo);
        String shellAbilityClassName = getShellAbilityClassName(isDifferentPkg, abilityInfo, abilityInfo.getType());
        int i = AnonymousClass1.$SwitchMap$ohos$bundle$AbilityInfo$AbilityType[abilityInfo.getType().ordinal()];
        if (i == 1) {
            shellInfo.setType(ShellInfo.ShellType.ACTIVITY);
        } else if (i == 2) {
            shellInfo.setType(ShellInfo.ShellType.SERVICE);
        } else if (i != 3) {
            AppLog.w(SHELL_LABEL, "AbilityShellConverterUtils::convertToShellInfo unknown type", new Object[0]);
        } else {
            shellInfo.setType(ShellInfo.ShellType.PROVIDER);
        }
        if (shellAbilityClassName == null) {
            AppLog.e(SHELL_LABEL, "AbilityShellConverterUtils::convertToShellInfo failed", new Object[0]);
            return null;
        }
        shellInfo.setPackageName(isDifferentPkg ? abilityInfo.getOriginalBundleName() : abilityInfo.getBundleName());
        shellInfo.setName(shellAbilityClassName);
        AppLog.d(SHELL_LABEL, "AbilityShellConverterUtils::convertToShellInfo Shell package: %{private}s, class: %{private}s", shellInfo.getPackageName(), shellInfo.getName());
        return shellInfo;
    }

    private static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    private static String getFullOriginalClassName(String str, String str2) {
        return (!isNotEmpty(str) || !isNotEmpty(str2) || str2.charAt(0) != '.') ? str2 : str.concat(str2);
    }

    private static boolean isDifferentClassName(boolean z, String str, String str2) {
        return z && isNotEmpty(str2) && !str2.equals(str);
    }

    private static String getShellAbilityClassName(boolean z, AbilityInfo abilityInfo, AbilityInfo.AbilityType abilityType) {
        String className = abilityInfo.getClassName();
        String fullOriginalClassName = getFullOriginalClassName(abilityInfo.getBundleName(), abilityInfo.getOriginalClassName());
        if (isDifferentClassName(z, className, fullOriginalClassName)) {
            return fullOriginalClassName;
        }
        int i = AnonymousClass1.$SwitchMap$ohos$bundle$AbilityInfo$AbilityType[abilityType.ordinal()];
        if (i == 1) {
            return className.concat("ShellActivity");
        }
        if (i == 2) {
            return className.concat("ShellService");
        }
        if (i != 3) {
            return null;
        }
        return className.concat("ShellProvider");
    }

    public static ShellInfo convertToShellInfoSupportDiffPkg(AbilityInfo abilityInfo, BundleInfo bundleInfo) {
        return convertToShellInfo(abilityInfo);
    }

    public static AbilityInfo convertToAbilityInfo(ShellInfo shellInfo) {
        String str;
        String str2;
        if (shellInfo == null) {
            AppLog.e(SHELL_LABEL, "AbilityShellConverterUtils::convertToAbilityInfo param invalid", new Object[0]);
            return null;
        }
        AbilityInfo abilityInfo = new AbilityInfo();
        int i = AnonymousClass1.$SwitchMap$ohos$bundle$ShellInfo$ShellType[shellInfo.getType().ordinal()];
        if (i == 1) {
            str = removeShellSuffix(shellInfo.getName(), "ShellActivity");
            abilityInfo.setType(AbilityInfo.AbilityType.PAGE);
        } else if (i == 2) {
            abilityInfo.setType(AbilityInfo.AbilityType.SERVICE);
            if (isFormShell(shellInfo)) {
                abilityInfo.setType(AbilityInfo.AbilityType.PAGE);
                str2 = FORM_SERVICE_SHELL_SUFFIX;
            } else {
                str2 = "ShellService";
            }
            str = removeShellSuffix(shellInfo.getName(), str2);
        } else if (i != 3) {
            AppLog.w(SHELL_LABEL, "AbilityShellConverterUtils::convertToAbilityInfo unknown type", new Object[0]);
            str = null;
        } else {
            str = removeShellSuffix(shellInfo.getName(), "ShellProvider");
            abilityInfo.setType(AbilityInfo.AbilityType.DATA);
        }
        if (str == null) {
            AppLog.e(SHELL_LABEL, "AbilityShellConverterUtils::convertToAbilityInfo failed", new Object[0]);
            return null;
        }
        abilityInfo.setBundleName(shellInfo.getPackageName());
        abilityInfo.setClassName(str);
        return abilityInfo;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.abilityshell.utils.AbilityShellConverterUtils$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$bundle$AbilityInfo$AbilityType = new int[AbilityInfo.AbilityType.values().length];
        static final /* synthetic */ int[] $SwitchMap$ohos$bundle$ShellInfo$ShellType = new int[ShellInfo.ShellType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|20) */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x003d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0047 */
        static {
            /*
                ohos.bundle.ShellInfo$ShellType[] r0 = ohos.bundle.ShellInfo.ShellType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.abilityshell.utils.AbilityShellConverterUtils.AnonymousClass1.$SwitchMap$ohos$bundle$ShellInfo$ShellType = r0
                r0 = 1
                int[] r1 = ohos.abilityshell.utils.AbilityShellConverterUtils.AnonymousClass1.$SwitchMap$ohos$bundle$ShellInfo$ShellType     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.bundle.ShellInfo$ShellType r2 = ohos.bundle.ShellInfo.ShellType.ACTIVITY     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                r1 = 2
                int[] r2 = ohos.abilityshell.utils.AbilityShellConverterUtils.AnonymousClass1.$SwitchMap$ohos$bundle$ShellInfo$ShellType     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.bundle.ShellInfo$ShellType r3 = ohos.bundle.ShellInfo.ShellType.SERVICE     // Catch:{ NoSuchFieldError -> 0x001f }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                r2 = 3
                int[] r3 = ohos.abilityshell.utils.AbilityShellConverterUtils.AnonymousClass1.$SwitchMap$ohos$bundle$ShellInfo$ShellType     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.bundle.ShellInfo$ShellType r4 = ohos.bundle.ShellInfo.ShellType.PROVIDER     // Catch:{ NoSuchFieldError -> 0x002a }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                ohos.bundle.AbilityInfo$AbilityType[] r3 = ohos.bundle.AbilityInfo.AbilityType.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                ohos.abilityshell.utils.AbilityShellConverterUtils.AnonymousClass1.$SwitchMap$ohos$bundle$AbilityInfo$AbilityType = r3
                int[] r3 = ohos.abilityshell.utils.AbilityShellConverterUtils.AnonymousClass1.$SwitchMap$ohos$bundle$AbilityInfo$AbilityType     // Catch:{ NoSuchFieldError -> 0x003d }
                ohos.bundle.AbilityInfo$AbilityType r4 = ohos.bundle.AbilityInfo.AbilityType.PAGE     // Catch:{ NoSuchFieldError -> 0x003d }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x003d }
                r3[r4] = r0     // Catch:{ NoSuchFieldError -> 0x003d }
            L_0x003d:
                int[] r0 = ohos.abilityshell.utils.AbilityShellConverterUtils.AnonymousClass1.$SwitchMap$ohos$bundle$AbilityInfo$AbilityType     // Catch:{ NoSuchFieldError -> 0x0047 }
                ohos.bundle.AbilityInfo$AbilityType r3 = ohos.bundle.AbilityInfo.AbilityType.SERVICE     // Catch:{ NoSuchFieldError -> 0x0047 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0047 }
                r0[r3] = r1     // Catch:{ NoSuchFieldError -> 0x0047 }
            L_0x0047:
                int[] r0 = ohos.abilityshell.utils.AbilityShellConverterUtils.AnonymousClass1.$SwitchMap$ohos$bundle$AbilityInfo$AbilityType     // Catch:{ NoSuchFieldError -> 0x0051 }
                ohos.bundle.AbilityInfo$AbilityType r1 = ohos.bundle.AbilityInfo.AbilityType.DATA     // Catch:{ NoSuchFieldError -> 0x0051 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0051 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0051 }
            L_0x0051:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.abilityshell.utils.AbilityShellConverterUtils.AnonymousClass1.<clinit>():void");
        }
    }

    public static ShellInfo convertToFormShellInfo(AbilityInfo abilityInfo, ShellInfo.ShellType shellType) {
        if (abilityInfo == null || abilityInfo.getType() != AbilityInfo.AbilityType.PAGE) {
            AppLog.e(SHELL_LABEL, "AbilityShellConverterUtils::convertToFormShellInfo info invalid", new Object[0]);
            return null;
        } else if (shellType != ShellInfo.ShellType.SERVICE) {
            AppLog.e(SHELL_LABEL, "AbilityShellConverterUtils::convertToFormShellInfo type invalid", new Object[0]);
            return null;
        } else {
            AppLog.d(SHELL_LABEL, "AbilityShellConverterUtils::convertToFormShellInfo Ability package: %{private}s, class: %{private}s", abilityInfo.getBundleName(), abilityInfo.getClassName());
            ShellInfo shellInfo = new ShellInfo();
            if (abilityInfo.getClassName() != null) {
                shellInfo.setName(abilityInfo.getClassName().concat(FORM_SERVICE_SHELL_SUFFIX));
            }
            shellInfo.setPackageName(abilityInfo.getBundleName());
            shellInfo.setType(shellType);
            AppLog.d(SHELL_LABEL, "AbilityShellConverterUtils::convertToFormShellInfo Shell package: %{private}s, class: %{private}s", shellInfo.getPackageName(), shellInfo.getName());
            return shellInfo;
        }
    }

    public static boolean isFormShell(ShellInfo shellInfo) {
        if (shellInfo == null || shellInfo.getType() != ShellInfo.ShellType.SERVICE) {
            AppLog.e(SHELL_LABEL, "AbilityShellConverterUtils::isFormShell param invalid", new Object[0]);
            return false;
        } else if (shellInfo.getName() != null) {
            return shellInfo.getName().endsWith(FORM_SERVICE_SHELL_SUFFIX);
        } else {
            return false;
        }
    }

    public static ResolveInfo getAndroidComponent(Context context, Intent intent) {
        if (!(context == null || intent == null || (intent.getFlags() & 16) == 0)) {
            Optional<android.content.Intent> createAndroidIntent = IntentConverter.createAndroidIntent(intent, null);
            if (!createAndroidIntent.isPresent()) {
                AppLog.e("AbilityShellConverterUtils::getAndroidComponent createAndroidIntent failed", new Object[0]);
                return null;
            }
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                AppLog.e("AbilityShellConverterUtils::getAndroidComponent packageManager is null", new Object[0]);
                return null;
            }
            List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(createAndroidIntent.get(), 0);
            if (!queryIntentActivities.isEmpty()) {
                addIntentType(intent, 1);
                return queryIntentActivities.get(0);
            }
            List<ResolveInfo> queryIntentServices = packageManager.queryIntentServices(createAndroidIntent.get(), 0);
            if (!queryIntentServices.isEmpty()) {
                addIntentType(intent, 2);
                return queryIntentServices.get(0);
            }
            AppLog.d("AbilityShellConverterUtils::getAndroidComponent no matched android.", new Object[0]);
        }
        return null;
    }

    public static AbilityShellData createAbilityShellDataByResolveInfo(ResolveInfo resolveInfo, boolean z) {
        String str;
        if (resolveInfo == null) {
            AppLog.e("AbilityShellConverterUtils::createAbilityShellDataByResolveInfo info is null!", new Object[0]);
            return null;
        }
        ShellInfo.ShellType shellType = ShellInfo.ShellType.UNKNOWN;
        String str2 = "";
        if (resolveInfo.activityInfo != null) {
            str2 = resolveInfo.activityInfo.packageName;
            str = resolveInfo.activityInfo.name;
            shellType = ShellInfo.ShellType.ACTIVITY;
        } else {
            str = str2;
        }
        if (resolveInfo.serviceInfo != null) {
            str2 = resolveInfo.serviceInfo.packageName;
            str = resolveInfo.serviceInfo.name;
            shellType = ShellInfo.ShellType.SERVICE;
        }
        if (shellType == ShellInfo.ShellType.UNKNOWN) {
            AppLog.w("AbilityShellConverterUtils::createAbilityShellDataByResolveInfo unknown type", new Object[0]);
        }
        AbilityInfo abilityInfo = new AbilityInfo();
        abilityInfo.setBundleName(str2);
        abilityInfo.setClassName(str);
        ShellInfo shellInfo = new ShellInfo();
        shellInfo.setPackageName(str2);
        shellInfo.setName(str);
        shellInfo.setType(shellType);
        return new AbilityShellData(z, abilityInfo, shellInfo);
    }

    public static List<AbilityShellData> getAndroidShellDatas(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return Collections.emptyList();
        }
        Optional<android.content.Intent> createAndroidIntent = IntentConverter.createAndroidIntent(intent, null);
        if (!createAndroidIntent.isPresent()) {
            return Collections.emptyList();
        }
        List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(createAndroidIntent.get(), 65536);
        if (queryIntentActivities.isEmpty()) {
            return Collections.emptyList();
        }
        addIntentType(intent, 1);
        ArrayList arrayList = new ArrayList();
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            arrayList.add(convertToAbilityShellData(packageManager, resolveInfo, 1));
        }
        return arrayList;
    }

    public static String convertToHarmonyClassName(String str) {
        return removeShellSuffix(str, "ShellActivity");
    }

    private static String removeShellSuffix(String str, String str2) {
        if (str == null) {
            AppLog.e(SHELL_LABEL, "AbilityShellConverterUtils::removeShellSuffix parameter name is null", new Object[0]);
            return null;
        }
        int lastIndexOf = str.lastIndexOf(str2);
        if (lastIndexOf != -1) {
            return str.substring(0, lastIndexOf);
        }
        AppLog.e(SHELL_LABEL, "AbilityShellConverterUtils::removeShellSuffix %{private}s not contain %{private}s", str, str2);
        return null;
    }

    private static AbilityShellData convertToAbilityShellData(PackageManager packageManager, ResolveInfo resolveInfo, int i) {
        if (resolveInfo == null) {
            return new AbilityShellData(true, new AbilityInfo(), new ShellInfo());
        }
        String str = resolveInfo.activityInfo.packageName;
        String str2 = resolveInfo.activityInfo.name;
        CharSequence loadLabel = resolveInfo.loadLabel(packageManager);
        String charSequence = loadLabel != null ? loadLabel.toString() : "";
        AbilityInfo abilityInfo = new AbilityInfo();
        abilityInfo.setBundleName(str);
        abilityInfo.setClassName(str2);
        abilityInfo.label = charSequence;
        ShellInfo shellInfo = new ShellInfo();
        shellInfo.setPackageName(str);
        shellInfo.setName(str2);
        if (i != 1) {
            AppLog.w("AndroidUtils::convertToAbilityShellData::type not supported", new Object[0]);
        } else {
            shellInfo.setType(ShellInfo.ShellType.ACTIVITY);
        }
        return new AbilityShellData(true, abilityInfo, shellInfo);
    }

    private static void addIntentType(Intent intent, int i) {
        IntentParams params = intent.getParams();
        if (params == null) {
            params = new IntentParams();
        }
        params.setParam(SHELL_TYPE, Integer.valueOf(i));
        intent.setParams(params);
    }
}
