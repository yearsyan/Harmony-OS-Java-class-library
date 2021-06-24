package ohos.abilityshell.utils;

import android.app.ActivityOptions;
import android.graphics.Rect;
import android.os.Bundle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import ohos.aafwk.ability.startsetting.AbilityStartSetting;
import ohos.agp.agpanimator.AnimatorOption;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class AbilityStartSettingConverter {
    private static final int BOUNDS_BOTTOM = 3;
    private static final int BOUNDS_LEFT = 0;
    private static final int BOUNDS_LENGTH = 4;
    private static final int BOUNDS_RIGHT = 2;
    private static final int BOUNDS_TOP = 1;
    private static final String KEY_BOUNDS = "android:activity.launchBounds";
    private static final String KEY_WINDOW_DISPLAY_ID = "android.activity.launchDisplayId";
    private static final String KEY_WINDOW_MODE = "android.activity.windowingMode";
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218108160, "AbilityStartSettingConverter");
    private static Map<String, Class<?>> convertorClasses = new HashMap();
    private static Map<String, String> convertorPairs = new HashMap();

    static {
        convertorClasses.put(AbilityStartSetting.BOUNDS_KEY, int[].class);
        convertorClasses.put(AbilityStartSetting.WINDOW_MODE_KEY, Integer.class);
        convertorClasses.put(AbilityStartSetting.WINDOW_DISPLAY_ID_KEY, Integer.class);
        convertorPairs.put(AbilityStartSetting.BOUNDS_KEY, KEY_BOUNDS);
        convertorPairs.put(AbilityStartSetting.WINDOW_MODE_KEY, KEY_WINDOW_MODE);
        convertorPairs.put(AbilityStartSetting.WINDOW_DISPLAY_ID_KEY, KEY_WINDOW_DISPLAY_ID);
    }

    public static Optional<ActivityOptions> createActivityOptions(AbilityStartSetting abilityStartSetting) {
        if (abilityStartSetting == null) {
            HiLog.error(LABEL, "abilityStartSetting or intent is null.", new Object[0]);
            throw new IllegalArgumentException("abilityStartSetting or intent is null.");
        } else if (abilityStartSetting.isEmpty()) {
            HiLog.error(LABEL, "abilityStartSetting is empty not convert.", new Object[0]);
            return Optional.empty();
        } else {
            Bundle bundle = new Bundle();
            addProperties(bundle, abilityStartSetting);
            AnimatorOption animatorOption = abilityStartSetting.getAnimatorOption();
            if (animatorOption == null) {
                return Optional.of(new ActivityOptions(bundle));
            }
            ActivityOptions translation = animatorOption.translation();
            new Bundle();
            Bundle bundle2 = translation.toBundle();
            bundle2.putAll(bundle);
            return Optional.of(new ActivityOptions(bundle2));
        }
    }

    private static void addProperties(Bundle bundle, AbilityStartSetting abilityStartSetting) {
        Set<String> propertiesKey = abilityStartSetting.getPropertiesKey();
        HashSet<String> hashSet = new HashSet();
        hashSet.addAll(propertiesKey);
        for (String str : hashSet) {
            if (convertorClasses.containsKey(str)) {
                char c = 65535;
                if (str.hashCode() == -867266433 && str.equals(AbilityStartSetting.BOUNDS_KEY)) {
                    c = 0;
                }
                if (c != 0) {
                    addNormalProperties(bundle, str, abilityStartSetting.getProperty(str));
                } else {
                    addBounds(bundle, abilityStartSetting.getProperty(str));
                }
            }
        }
    }

    private static void addNormalProperties(Bundle bundle, String str, Object obj) {
        if (!Integer.class.equals(convertorClasses.get(str))) {
            HiLog.error(LABEL, "abilityStartSetting cant convert property : %{public}s", str);
        } else if (obj instanceof Integer) {
            bundle.putInt(convertorPairs.get(str), ((Integer) obj).intValue());
        }
    }

    private static void addBounds(Bundle bundle, Object obj) {
        if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            if (iArr.length == 4) {
                bundle.putParcelable(KEY_BOUNDS, new Rect(iArr[0], iArr[1], iArr[2], iArr[3]));
            }
        }
    }
}
