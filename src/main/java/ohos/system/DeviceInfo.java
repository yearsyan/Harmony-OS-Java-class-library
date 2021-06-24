package ohos.system;

import java.util.Optional;
import ohos.annotation.SystemApi;
import ohos.system.controller.DeviceIdController;

public final class DeviceInfo {
    private static final String LOCALE = Parameters.get("ro.product.locale");
    private static final String LOCALE_LANGUAGE = Parameters.get("ro.product.locale.language");
    private static final String LOCALE_REGION = Parameters.get("ro.product.locale.region");
    private static final String MODEL = Parameters.get("ro.product.model");
    private static final String NAME = Parameters.get("ro.product.name");

    private DeviceInfo() {
    }

    public static String getModel() {
        return MODEL;
    }

    public static String getName() {
        return NAME;
    }

    public static String getLocale() {
        return LOCALE;
    }

    public static String getLocaleLanguage() {
        return LOCALE_LANGUAGE;
    }

    public static String getLocaleRegion() {
        return LOCALE_REGION;
    }

    @SystemApi
    public static Optional<String> getUdid() {
        return DeviceIdController.getUdid();
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x002f  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0035 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getDeviceType() {
        /*
            java.lang.String r0 = "ro.build.characteristics"
            java.lang.String r1 = ""
            java.lang.String r0 = ohos.system.Parameters.get(r0, r1)
            int r1 = r0.hashCode()
            r2 = 112903375(0x6bac4cf, float:7.025461E-35)
            r3 = 1
            if (r1 == r2) goto L_0x0022
            r2 = 297574343(0x11bc9fc7, float:2.975964E-28)
            if (r1 == r2) goto L_0x0018
            goto L_0x002c
        L_0x0018:
            java.lang.String r1 = "fitnessWatch"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x002c
            r1 = r3
            goto L_0x002d
        L_0x0022:
            java.lang.String r1 = "watch"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x002c
            r1 = 0
            goto L_0x002d
        L_0x002c:
            r1 = -1
        L_0x002d:
            if (r1 == 0) goto L_0x0035
            if (r1 == r3) goto L_0x0032
            return r0
        L_0x0032:
            java.lang.String r0 = "liteWearable"
            return r0
        L_0x0035:
            java.lang.String r0 = "wearable"
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.system.DeviceInfo.getDeviceType():java.lang.String");
    }
}
