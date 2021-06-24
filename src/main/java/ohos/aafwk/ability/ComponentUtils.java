package ohos.aafwk.ability;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import ohos.agp.components.ComponentProvider;
import ohos.app.ContextDeal;
import ohos.global.resource.ResourceManagerInner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.interwork.ui.RemoteViewEx;

class ComponentUtils {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218108160, "ComponentUtils");
    private static final String RESOURCE_TABLE = ".ResourceTable";

    static boolean initLayout(Context context, FormItemInfo formItemInfo) {
        if (context == null || formItemInfo == null) {
            return false;
        }
        if (formItemInfo.previewLayoutId == 0 || formItemInfo.eSystemPreviewLayoutId == 0) {
            Context targetContext = getTargetContext(context, formItemInfo.getOriginalBundleName());
            if (targetContext == null) {
                HiLog.error(LABEL, "initLayout create target context failed.", new Object[0]);
                return false;
            }
            String packageName = formItemInfo.getPackageName();
            if (packageName == null) {
                HiLog.error(LABEL, "initLayout packageName is invalid.", new Object[0]);
                return false;
            }
            Class<?> targetClazz = getTargetClazz(formItemInfo.getHapSourceDirs(), packageName + RESOURCE_TABLE);
            if (targetClazz == null) {
                HiLog.error(LABEL, "initLayout getTargetClazz is null.", new Object[0]);
                return false;
            }
            if (formItemInfo.previewLayoutId == 0) {
                int layoutId = getLayoutId(targetClazz, formItemInfo.getLayoutIdConfig());
                if (layoutId == 0) {
                    HiLog.error(LABEL, "initLayout get layout id failed.", new Object[0]);
                    return false;
                }
                formItemInfo.previewLayoutId = layoutId;
            }
            int aResId = ResourceManagerInner.getAResId(formItemInfo.previewLayoutId, targetClazz, targetContext);
            if (aResId == 0) {
                HiLog.error(LABEL, "initLayout get remote esystem layout id failed.", new Object[0]);
                return false;
            }
            formItemInfo.eSystemPreviewLayoutId = aResId;
            return true;
        }
        HiLog.debug(LABEL, "initLayout z-resId:%{public}d, a-resId:%{public}d.", Integer.valueOf(formItemInfo.previewLayoutId), Integer.valueOf(formItemInfo.eSystemPreviewLayoutId));
        return true;
    }

    static boolean initLayout(Context context, FormRecord formRecord, FormResource formResource) {
        if (context == null || formRecord == null || formResource == null) {
            return false;
        }
        if (formResource.previewLayoutId != 0 && formResource.eSystemPreviewLayoutId != 0) {
            return true;
        }
        Context targetContext = getTargetContext(context, formRecord.originalBundleName);
        if (targetContext == null) {
            HiLog.error(LABEL, "initLayout formResource create target context failed.", new Object[0]);
            return false;
        }
        String str = formRecord.packageName;
        if (str == null) {
            HiLog.error(LABEL, "initLayout formResource packageName is invalid.", new Object[0]);
            return false;
        }
        Class<?> targetClazz = getTargetClazz(formRecord.hapSourceDirs, str + RESOURCE_TABLE);
        if (targetClazz == null) {
            HiLog.error(LABEL, "initLayout formResource getTargetClazz is null.", new Object[0]);
            return false;
        }
        if (formResource.previewLayoutId == 0) {
            int layoutId = getLayoutId(targetClazz, formResource.layoutIdConfig);
            if (layoutId == 0) {
                HiLog.error(LABEL, "initLayout formResource get layout id failed.", new Object[0]);
                return false;
            }
            formResource.previewLayoutId = layoutId;
        }
        int aResId = ResourceManagerInner.getAResId(formResource.previewLayoutId, targetClazz, targetContext);
        if (aResId == 0) {
            HiLog.error(LABEL, "initLayout formResource get remote esystem layout id failed.", new Object[0]);
            return false;
        }
        formResource.eSystemPreviewLayoutId = aResId;
        return true;
    }

    private static Context getTargetContext(Context context, String str) {
        if (str == null) {
            return null;
        }
        try {
            return context.createPackageContextAsUser(str, 3, new UserHandle(ActivityManager.getCurrentUser()));
        } catch (PackageManager.NameNotFoundException unused) {
            HiLog.error(LABEL, "getTargetContext cannot find such bundle:%{public}s", str);
            return null;
        }
    }

    private static Class<?> getTargetClazz(String[] strArr, String str) {
        if (strArr == null || strArr.length == 0) {
            return null;
        }
        try {
            PathClassLoader pathClassLoader = new PathClassLoader(strArr[0], Thread.currentThread().getContextClassLoader());
            for (int i = 1; i < strArr.length; i++) {
                pathClassLoader.addDexPath(strArr[i]);
            }
            return Class.forName(str, false, pathClassLoader);
        } catch (ClassNotFoundException unused) {
            HiLog.error(LABEL, "getTargetClazz ClassNotFoundException class=%{public}s", str);
            return null;
        }
    }

    private static int getLayoutId(Class<?> cls, String str) {
        if (cls == null || str == null || str.isEmpty()) {
            HiLog.debug(LABEL, "getLayoutId filedName:%{public}s", str);
            return 0;
        }
        Field[] fields = cls.getFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && field.getName().equals(str)) {
                try {
                    return field.getInt(cls);
                } catch (IllegalAccessException unused) {
                    continue;
                }
            }
        }
        return 0;
    }

    static RemoteViewEx getRemoteViewEx(Context context, String str, String str2, String[] strArr, ComponentProvider componentProvider) {
        if (context == null || str == null || str2 == null || strArr == null || componentProvider == null) {
            return null;
        }
        ohos.app.Context createContext = createContext(context, str2);
        if (createContext == null) {
            HiLog.error(LABEL, "getRemoteViewEx createContext failed.", new Object[0]);
            return null;
        }
        Class<?> targetClazz = getTargetClazz(strArr, str + RESOURCE_TABLE);
        if (targetClazz != null) {
            return new RemoteViewEx(createContext, componentProvider, ActivityManager.getCurrentUser(), targetClazz);
        }
        HiLog.error(LABEL, "getRemoteViewEx getTargetClazz is null.", new Object[0]);
        return null;
    }

    private static ohos.app.Context createContext(Context context, String str) {
        try {
            Context createPackageContextAsUser = context.createPackageContextAsUser(str, 3, new UserHandle(ActivityManager.getCurrentUser()));
            if (createPackageContextAsUser != null) {
                return new ContextDeal(createPackageContextAsUser, createPackageContextAsUser.getClassLoader());
            }
            HiLog.error(LABEL, "get esystem context failed.", new Object[0]);
            return null;
        } catch (PackageManager.NameNotFoundException unused) {
            HiLog.error(LABEL, "createContext cannot find such bundle:%{public}s", str);
            return null;
        }
    }

    private ComponentUtils() {
    }
}
