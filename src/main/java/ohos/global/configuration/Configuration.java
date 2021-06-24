package ohos.global.configuration;

import java.util.Locale;

public class Configuration {
    public static final int AUTO_MODE = -1;
    public static final int DARK_MODE = 0;
    private static final int DEFAULT_BUFFER_LENGTH = 128;
    private static final int DEFAULT_VALUE = 1;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_UNDEFINED = -1;
    public static final int DIRECTION_VERTICAL = 0;
    public static final int LIGHT_MODE = 1;
    public static final int MCC_UNDEFINED = 0;
    public static final int MNC_UNDEFINED = 0;
    public static final float SCALE_UNDEFINED = -1.0f;
    public static volatile int sysColorMode = -1;
    public int colorMode;
    public int direction;
    public float fontRatio;
    public boolean isLayoutRTL;
    private LocaleProfile localeProfile;
    public int mcc;
    public int mnc;
    public int realColorMode;

    public Configuration() {
        this.localeProfile = new LocaleProfile(new Locale[]{Locale.getDefault()});
        this.direction = -1;
        this.fontRatio = 1.0f;
        this.isLayoutRTL = false;
        this.colorMode = -1;
        this.realColorMode = -1;
        this.mcc = 0;
        this.mnc = 0;
    }

    public Configuration(Configuration configuration) {
        if (configuration == null) {
            this.direction = -1;
            this.fontRatio = -1.0f;
            this.isLayoutRTL = false;
            this.colorMode = -1;
            this.realColorMode = -1;
            this.mcc = 0;
            this.mnc = 0;
        } else {
            LocaleProfile localeProfile2 = configuration.localeProfile;
            if (localeProfile2 != null) {
                this.localeProfile = new LocaleProfile(localeProfile2.getLocales());
            }
            this.direction = configuration.direction;
            this.fontRatio = configuration.fontRatio;
            this.isLayoutRTL = configuration.isLayoutRTL;
            this.colorMode = configuration.colorMode;
            this.realColorMode = configuration.realColorMode;
            this.mcc = configuration.mcc;
            this.mnc = configuration.mnc;
        }
        checkLocaleProfile();
    }

    public LocaleProfile getLocaleProfile() {
        return this.localeProfile;
    }

    public void setLocaleProfile(LocaleProfile localeProfile2) {
        this.localeProfile = localeProfile2;
        checkLocaleProfile();
    }

    public Locale getFirstLocale() {
        return getLocaleProfile().getLocales()[0];
    }

    private void checkLocaleProfile() {
        Locale locale = Locale.getDefault();
        LocaleProfile localeProfile2 = this.localeProfile;
        if (localeProfile2 == null || localeProfile2.getLocales() == null || this.localeProfile.getLocales().length <= 0) {
            this.localeProfile = new LocaleProfile(new Locale[]{locale});
        } else if (this.localeProfile.getLocales()[0] != null) {
            Locale locale2 = this.localeProfile.getLocales()[0];
        } else {
            this.localeProfile.getLocales()[0] = locale;
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Configuration)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Configuration configuration = (Configuration) obj;
        return configuration.direction == this.direction && configuration.fontRatio == this.fontRatio && configuration.isLayoutRTL == this.isLayoutRTL && configuration.localeProfile.equals(this.localeProfile) && configuration.colorMode == this.colorMode && configuration.realColorMode == this.realColorMode && configuration.mcc == this.mcc && configuration.mnc == this.mnc;
    }

    public int hashCode() {
        return ((((((((((((((527 + this.direction) * 31) + Float.floatToIntBits(this.fontRatio)) * 31) + (this.isLayoutRTL ? 1 : 0)) * 31) + this.localeProfile.hashCode()) * 31) + this.colorMode) * 31) + this.realColorMode) * 31) + this.mcc) * 31) + this.mnc;
    }

    public enum Position {
        ORIENTATION_POS(0),
        RESOLUTION_POS(1),
        DEVICETYPE_POS(2),
        NIGHTMODE_POS(3),
        MCC_POS(4),
        MNC_POS(5);
        
        private int value;

        public int getValue() {
            return this.value;
        }

        private Position(int i) {
            this.value = i;
        }
    }

    public int getSystemColorMode() {
        return sysColorMode;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0080  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String toString() {
        /*
        // Method dump skipped, instructions count: 154
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.configuration.Configuration.toString():java.lang.String");
    }
}
