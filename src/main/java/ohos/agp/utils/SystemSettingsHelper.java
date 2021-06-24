package ohos.agp.utils;

import ohos.aafwk.ability.DataAbilityHelper;
import ohos.app.Context;
import ohos.sysappcomponents.settings.SystemSettings;

public class SystemSettingsHelper {
    private SystemSettingsHelper() {
    }

    public static boolean getHapticFeedbackStatus(Context context) {
        return "1".equals(SystemSettings.getValue(DataAbilityHelper.creator(context), SystemSettings.Sound.HAPTIC_FEEDBACK_STATUS));
    }
}
