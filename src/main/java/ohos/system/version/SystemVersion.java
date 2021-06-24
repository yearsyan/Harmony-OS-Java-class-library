package ohos.system.version;

import java.util.Locale;
import java.util.regex.Pattern;
import ohos.system.Parameters;

public class SystemVersion {
    private static final String RELEASE = "Release";
    private static final String RELEASE_TYPE = Parameters.get("hw_sc.build.os.releasetype", RELEASE);
    private static final String VERSION = Parameters.get("hw_sc.build.os.version", "1.0");
    private static int buildVersion = 0;
    private static int featureVersion = 0;
    private static int majorVersion = 1;
    private static int seniorVersion = 0;

    static {
        initVersionValue();
    }

    private static void initVersionValue() {
        if (VERSION != null && Pattern.compile("^([1-9]\\d|[1-9])(((\\.([1-9]\\d|\\d)){2}(\\.([1-9]\\d\\d)))|(\\.([1-9]\\d|\\d)){0,3})$").matcher(VERSION).find()) {
            String[] split = VERSION.split("\\.");
            for (int i = 0; i < split.length; i++) {
                if (i == 0) {
                    majorVersion = Integer.parseInt(split[i]);
                } else if (i == 1) {
                    seniorVersion = Integer.parseInt(split[i]);
                } else if (i == 2) {
                    featureVersion = Integer.parseInt(split[i]);
                } else if (i == 3) {
                    buildVersion = Integer.parseInt(split[i]);
                } else {
                    return;
                }
            }
        }
    }

    public static String getVersion() {
        String str = RELEASE_TYPE;
        if (str == null || str.trim().length() == 0 || RELEASE_TYPE.equalsIgnoreCase(RELEASE)) {
            return VERSION;
        }
        return String.format(Locale.ROOT, "%s %s", VERSION, RELEASE_TYPE);
    }

    public static int getMajorVerion() {
        return majorVersion;
    }

    public static int getMajorVersion() {
        return majorVersion;
    }

    public static int getSeniorVerion() {
        return seniorVersion;
    }

    public static int getSeniorVersion() {
        return seniorVersion;
    }

    public static int getFeatureVersion() {
        return featureVersion;
    }

    public static int getBuildVersion() {
        return buildVersion;
    }

    public static String getReleaseType() {
        return RELEASE_TYPE;
    }

    public static int getApiVersion() {
        return Parameters.getInt("hw_sc.build.os.apiversion", 1);
    }
}
