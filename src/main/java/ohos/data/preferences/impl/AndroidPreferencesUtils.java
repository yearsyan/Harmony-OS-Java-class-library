package ohos.data.preferences.impl;

import android.content.SharedPreferences;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class AndroidPreferencesUtils {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218109520, "AndroidPreferencesUtils");

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x007a */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0097 A[SYNTHETIC, Splitter:B:37:0x0097] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<java.lang.String> readFile(java.io.File r10) {
        /*
        // Method dump skipped, instructions count: 169
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.preferences.impl.AndroidPreferencesUtils.readFile(java.io.File):java.util.List");
    }

    public static boolean isAndroidFile(List<String> list) {
        if (list != null && list.size() >= 2) {
            String str = list.get(0);
            if (!str.startsWith("<?xml") || !str.endsWith("?>") || !list.get(1).startsWith("<map")) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isEmptyAndroidFile(List<String> list) {
        if (list == null || list.size() != 2) {
            return false;
        }
        String str = list.get(1);
        return str.startsWith("<map") && str.endsWith("/>");
    }

    public static SharedPreferences getSharedPreferences(File file) {
        try {
            Constructor<?> declaredConstructor = Class.forName("android.app.SharedPreferencesImpl").getDeclaredConstructor(File.class, Integer.TYPE);
            declaredConstructor.setAccessible(true);
            Object newInstance = declaredConstructor.newInstance(file, 0);
            if (newInstance instanceof SharedPreferences) {
                return (SharedPreferences) newInstance;
            }
            HiLog.error(LABEL, "failed to get SharedPreferences when getSharedPreferences", new Object[0]);
            return null;
        } catch (InstantiationException e) {
            HiLog.error(LABEL, "movePreferences has InstantiationException,eMsg:%{public}s", e.getMessage());
            return null;
        } catch (InvocationTargetException e2) {
            HiLog.error(LABEL, "movePreferences has InvocationTargetException,eMsg:%{public}s", e2.getMessage());
            return null;
        } catch (NoSuchMethodException e3) {
            HiLog.error(LABEL, "movePreferences has NoSuchMethodException,eMsg:%{public}s", e3.getMessage());
            return null;
        } catch (IllegalAccessException e4) {
            HiLog.error(LABEL, "movePreferences has IllegalAccessException,eMsg:%{public}s", e4.getMessage());
            return null;
        } catch (ClassNotFoundException e5) {
            HiLog.error(LABEL, "movePreferences has ClassNotFoundException,eMsg:%{public}s", e5.getMessage());
            return null;
        }
    }
}
