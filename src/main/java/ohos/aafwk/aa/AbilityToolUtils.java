package ohos.aafwk.aa;

import android.content.ComponentName;
import android.content.Intent;
import java.util.Locale;
import java.util.Optional;
import ohos.abilityshell.utils.IntentConverter;
import ohos.bundle.ElementName;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class AbilityToolUtils {
    private static final int AA_ARGS_START_POS = 1;
    public static final int ACTIVITY = 0;
    private static final String INTENT_PARAM = "param";
    private static final String OPTION_ACTION = "-a";
    private static final String OPTION_CATEGORY = "-c";
    private static final String OPTION_COMPONENT_NAME = "-n";
    private static final String OPTION_EXTRA_BOOL = "--ez";
    private static final String OPTION_EXTRA_INT = "--ei";
    private static final String OPTION_EXTRA_STRING = "--es";
    private static final String OPTION_FLAG = "-f";
    private static final String OPTION_URI = "-d";
    private static final String PAGE_SHELL_SUFFIX = "ShellActivity";
    private static final String PARAM_URI = "{has params};end";
    public static final int SERVICE = 1;
    private static final String SERVICE_SHELL_SUFFIX = "ShellService";
    private static final HiLogLabel TAG = new HiLogLabel(3, 218108160, "AbilityToolUtils");
    private static final String TRUE_STR = "true";

    /* JADX WARNING: Removed duplicated region for block: B:45:0x009d  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00a8  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00c6  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00d1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.content.Intent getIntentFromArgs(java.lang.String[] r9, ohos.aafwk.aa.AbilityOptionHandler r10) {
        /*
        // Method dump skipped, instructions count: 258
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.aa.AbilityToolUtils.getIntentFromArgs(java.lang.String[], ohos.aafwk.aa.AbilityOptionHandler):android.content.Intent");
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0047  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00bc  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void handleIntentRequiredOption(java.lang.String r4, android.content.Intent r5, android.net.Uri r6, ohos.aafwk.aa.AbilityCommandArgs r7) {
        /*
        // Method dump skipped, instructions count: 192
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.aa.AbilityToolUtils.handleIntentRequiredOption(java.lang.String, android.content.Intent, android.net.Uri, ohos.aafwk.aa.AbilityCommandArgs):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x008b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void handleIntentExtraOption(java.lang.String r5, android.content.Intent r6, ohos.aafwk.aa.AbilityCommandArgs r7) {
        /*
        // Method dump skipped, instructions count: 143
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.aa.AbilityToolUtils.handleIntentExtraOption(java.lang.String, android.content.Intent, ohos.aafwk.aa.AbilityCommandArgs):void");
    }

    private static boolean getArgExtraBool(String str) {
        return "true".equals(str.toLowerCase(Locale.ROOT));
    }

    private static void handleAbilityOptions(AbilityOptionHandler abilityOptionHandler, String str) {
        if (abilityOptionHandler == null) {
            HiLog.error(TAG, "AbilityToolUtils::handleAbilityOptions handler is null", new Object[0]);
            throw new IllegalArgumentException("handler is null");
        } else if (!abilityOptionHandler.handleAbilityOption(str)) {
            printMessageToConsole("handleAbilityOptions Unknow option:" + str);
            throw new IllegalArgumentException("Unknown option: " + str);
        }
    }

    private static Optional<String> getAbilityNameWithSuffix(Intent intent, int i) {
        if (intent == null) {
            HiLog.error(TAG, "AbilityToolUtils::getAbilityNameWithSuffix intent is null", new Object[0]);
            return Optional.empty();
        }
        ComponentName component = intent.getComponent();
        if (component != null) {
            return getAbilityNameWithSuffix(component.getClassName(), i);
        }
        HiLog.error(TAG, "AbilityToolUtils::getAbilityNameWithSuffix component name is null", new Object[0]);
        return Optional.empty();
    }

    public static Optional<String> getAbilityNameWithSuffix(String str, int i) {
        if (str == null) {
            HiLog.error(TAG, "AbilityToolUtils::getAbilityNameWithSuffix name is invalid", new Object[0]);
            return Optional.empty();
        }
        Optional<String> empty = Optional.empty();
        if (i == 0) {
            return removeAbilitySuffix(str, "ShellActivity");
        }
        if (i == 1) {
            return removeAbilitySuffix(str, "ShellService");
        }
        HiLog.warn(TAG, "AbilityToolUtils::getAbilityNameWithSuffix unknow suffixtype", new Object[0]);
        return empty;
    }

    private static Optional<String> removeAbilitySuffix(String str, String str2) {
        if (str == null || str2 == null) {
            HiLog.error(TAG, "AbilityToolUtils::removeAbilitySuffix param is invalid", new Object[0]);
            return Optional.empty();
        } else if (!str.endsWith(str2)) {
            HiLog.error(TAG, "AbilityToolUtils::removeAbilitySuffix suffix inconsistent with name", new Object[0]);
            return Optional.empty();
        } else {
            int lastIndexOf = str.lastIndexOf(str2);
            if (lastIndexOf != -1) {
                return Optional.of(str.substring(0, lastIndexOf));
            }
            return Optional.empty();
        }
    }

    public static String getIntentUri(Intent intent, int i) {
        if (intent == null) {
            HiLog.error(TAG, "AbilityToolUtils::getIntentUri param is invalid", new Object[0]);
            return "";
        }
        Optional<ohos.aafwk.content.Intent> createZidaneIntent = IntentConverter.createZidaneIntent(intent, null);
        if (!createZidaneIntent.isPresent()) {
            HiLog.error(TAG, "AbilityToolUtils::getIntentUri createZidaneIntent failed", new Object[0]);
            return "";
        }
        ohos.aafwk.content.Intent intent2 = createZidaneIntent.get();
        ElementName element = intent2.getElement();
        if (element == null) {
            HiLog.error(TAG, "AbilityToolUtils::getIntentUri element is null", new Object[0]);
            return "";
        }
        Optional<String> abilityNameWithSuffix = getAbilityNameWithSuffix(intent, i);
        if (!abilityNameWithSuffix.isPresent()) {
            HiLog.error(TAG, "AbilityToolUtils::getIntentUri abilityname is null", new Object[0]);
            return "";
        }
        element.setAbilityName(abilityNameWithSuffix.get());
        intent2.setElement(element);
        return removeParamInfo(intent2.toUri());
    }

    private static String removeParamInfo(String str) {
        int indexOf = str.indexOf("param");
        if (indexOf == -1) {
            return str;
        }
        return str.substring(0, indexOf) + PARAM_URI;
    }

    public static String getElementUri(Intent intent, int i) {
        if (intent == null) {
            HiLog.error(TAG, "AbilityToolUtils::getElementUri param is invalid", new Object[0]);
            return "";
        }
        ComponentName component = intent.getComponent();
        if (component == null) {
            HiLog.error(TAG, "AbilityToolUtils::getElementUri component name is null", new Object[0]);
            return "";
        }
        Optional<String> abilityNameWithSuffix = getAbilityNameWithSuffix(component.getClassName(), i);
        if (!abilityNameWithSuffix.isPresent()) {
            HiLog.error(TAG, "AbilityToolUtils::getElementUri ability name is null", new Object[0]);
            return "";
        }
        String packageName = component.getPackageName();
        if (packageName == null) {
            HiLog.error(TAG, "AbilityToolUtils::getElementUri bundle name is null", new Object[0]);
            return "";
        }
        return "/" + packageName + "/" + abilityNameWithSuffix.get();
    }

    public static void printMessageToConsole(String str) {
        System.out.println(str);
    }
}
