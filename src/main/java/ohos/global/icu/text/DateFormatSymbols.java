package ohos.global.icu.text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import ohos.global.icu.impl.CacheBase;
import ohos.global.icu.impl.CalendarUtil;
import ohos.global.icu.impl.ICUData;
import ohos.global.icu.impl.ICUResourceBundle;
import ohos.global.icu.impl.SoftCache;
import ohos.global.icu.impl.UResource;
import ohos.global.icu.impl.Utility;
import ohos.global.icu.text.TimeZoneNames;
import ohos.global.icu.util.Calendar;
import ohos.global.icu.util.ICUCloneNotSupportedException;
import ohos.global.icu.util.ICUException;
import ohos.global.icu.util.TimeZone;
import ohos.global.icu.util.ULocale;
import ohos.global.icu.util.UResourceBundle;
import ohos.global.icu.util.UResourceBundleIterator;

public class DateFormatSymbols implements Serializable, Cloneable {
    public static final int ABBREVIATED = 0;
    static final String ALTERNATE_TIME_SEPARATOR = ".";
    private static final String[][] CALENDAR_CLASSES = {new String[]{"GregorianCalendar", "gregorian"}, new String[]{"JapaneseCalendar", "japanese"}, new String[]{"BuddhistCalendar", "buddhist"}, new String[]{"TaiwanCalendar", "roc"}, new String[]{"PersianCalendar", "persian"}, new String[]{"IslamicCalendar", "islamic"}, new String[]{"HebrewCalendar", "hebrew"}, new String[]{"ChineseCalendar", "chinese"}, new String[]{"IndianCalendar", "indian"}, new String[]{"CopticCalendar", "coptic"}, new String[]{"EthiopicCalendar", "ethiopic"}};
    private static final String[] DAY_PERIOD_KEYS = {"midnight", "noon", "morning1", "afternoon1", "evening1", "night1", "morning2", "afternoon2", "evening2", "night2"};
    static final String DEFAULT_TIME_SEPARATOR = ":";
    private static CacheBase<String, DateFormatSymbols, ULocale> DFSCACHE = new SoftCache<String, DateFormatSymbols, ULocale>() {
        /* class ohos.global.icu.text.DateFormatSymbols.AnonymousClass1 */

        /* access modifiers changed from: protected */
        public DateFormatSymbols createInstance(String str, ULocale uLocale) {
            int indexOf = str.indexOf(43) + 1;
            int indexOf2 = str.indexOf(43, indexOf);
            if (indexOf2 < 0) {
                indexOf2 = str.length();
            }
            return new DateFormatSymbols(uLocale, null, str.substring(indexOf, indexOf2));
        }
    };
    @Deprecated
    public static final int DT_CONTEXT_COUNT = 3;
    static final int DT_LEAP_MONTH_PATTERN_FORMAT_ABBREV = 1;
    static final int DT_LEAP_MONTH_PATTERN_FORMAT_NARROW = 2;
    static final int DT_LEAP_MONTH_PATTERN_FORMAT_WIDE = 0;
    static final int DT_LEAP_MONTH_PATTERN_NUMERIC = 6;
    static final int DT_LEAP_MONTH_PATTERN_STANDALONE_ABBREV = 4;
    static final int DT_LEAP_MONTH_PATTERN_STANDALONE_NARROW = 5;
    static final int DT_LEAP_MONTH_PATTERN_STANDALONE_WIDE = 3;
    static final int DT_MONTH_PATTERN_COUNT = 7;
    @Deprecated
    public static final int DT_WIDTH_COUNT = 4;
    public static final int FORMAT = 0;
    private static final String[] LEAP_MONTH_PATTERNS_PATHS = new String[7];
    public static final int NARROW = 2;
    @Deprecated
    public static final int NUMERIC = 2;
    public static final int SHORT = 3;
    public static final int STANDALONE = 1;
    public static final int WIDE = 1;
    private static final Map<String, CapitalizationContextUsage> contextUsageTypeMap = new HashMap();
    static final int millisPerHour = 3600000;
    static final String patternChars = "GyMdkHmsSEDFwWahKzYeugAZvcLQqVUOXxrbB";
    private static final long serialVersionUID = -5987973545549424702L;
    String[] abbreviatedDayPeriods;
    private ULocale actualLocale;
    String[] ampms;
    String[] ampmsNarrow;
    Map<CapitalizationContextUsage, boolean[]> capitalization;
    String[] eraNames;
    String[] eras;
    String[] leapMonthPatterns;
    String localPatternChars;
    String[] months;
    String[] narrowDayPeriods;
    String[] narrowEras;
    String[] narrowMonths;
    String[] narrowWeekdays;
    String[] quarters;
    private ULocale requestedLocale;
    String[] shortMonths;
    String[] shortQuarters;
    String[] shortWeekdays;
    String[] shortYearNames;
    String[] shortZodiacNames;
    String[] shorterWeekdays;
    String[] standaloneAbbreviatedDayPeriods;
    String[] standaloneMonths;
    String[] standaloneNarrowDayPeriods;
    String[] standaloneNarrowMonths;
    String[] standaloneNarrowWeekdays;
    String[] standaloneQuarters;
    String[] standaloneShortMonths;
    String[] standaloneShortQuarters;
    String[] standaloneShortWeekdays;
    String[] standaloneShorterWeekdays;
    String[] standaloneWeekdays;
    String[] standaloneWideDayPeriods;
    private String timeSeparator;
    private ULocale validLocale;
    String[] weekdays;
    String[] wideDayPeriods;
    private String[][] zoneStrings;

    /* access modifiers changed from: package-private */
    public enum CapitalizationContextUsage {
        OTHER,
        MONTH_FORMAT,
        MONTH_STANDALONE,
        MONTH_NARROW,
        DAY_FORMAT,
        DAY_STANDALONE,
        DAY_NARROW,
        ERA_WIDE,
        ERA_ABBREV,
        ERA_NARROW,
        ZONE_LONG,
        ZONE_SHORT,
        METAZONE_LONG,
        METAZONE_SHORT
    }

    @Deprecated
    public static ResourceBundle getDateFormatBundle(Class<? extends Calendar> cls, Locale locale) throws MissingResourceException {
        return null;
    }

    @Deprecated
    public static ResourceBundle getDateFormatBundle(Class<? extends Calendar> cls, ULocale uLocale) throws MissingResourceException {
        return null;
    }

    @Deprecated
    public static ResourceBundle getDateFormatBundle(Calendar calendar, Locale locale) throws MissingResourceException {
        return null;
    }

    @Deprecated
    public static ResourceBundle getDateFormatBundle(Calendar calendar, ULocale uLocale) throws MissingResourceException {
        return null;
    }

    public DateFormatSymbols() {
        this(ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public DateFormatSymbols(Locale locale) {
        this(ULocale.forLocale(locale));
    }

    public DateFormatSymbols(ULocale uLocale) {
        this.eras = null;
        this.eraNames = null;
        this.narrowEras = null;
        this.months = null;
        this.shortMonths = null;
        this.narrowMonths = null;
        this.standaloneMonths = null;
        this.standaloneShortMonths = null;
        this.standaloneNarrowMonths = null;
        this.weekdays = null;
        this.shortWeekdays = null;
        this.shorterWeekdays = null;
        this.narrowWeekdays = null;
        this.standaloneWeekdays = null;
        this.standaloneShortWeekdays = null;
        this.standaloneShorterWeekdays = null;
        this.standaloneNarrowWeekdays = null;
        this.ampms = null;
        this.ampmsNarrow = null;
        this.timeSeparator = null;
        this.shortQuarters = null;
        this.quarters = null;
        this.standaloneShortQuarters = null;
        this.standaloneQuarters = null;
        this.leapMonthPatterns = null;
        this.shortYearNames = null;
        this.shortZodiacNames = null;
        this.zoneStrings = null;
        this.localPatternChars = null;
        this.abbreviatedDayPeriods = null;
        this.wideDayPeriods = null;
        this.narrowDayPeriods = null;
        this.standaloneAbbreviatedDayPeriods = null;
        this.standaloneWideDayPeriods = null;
        this.standaloneNarrowDayPeriods = null;
        this.capitalization = null;
        initializeData(uLocale, CalendarUtil.getCalendarType(uLocale));
    }

    public static DateFormatSymbols getInstance() {
        return new DateFormatSymbols();
    }

    public static DateFormatSymbols getInstance(Locale locale) {
        return new DateFormatSymbols(locale);
    }

    public static DateFormatSymbols getInstance(ULocale uLocale) {
        return new DateFormatSymbols(uLocale);
    }

    public static Locale[] getAvailableLocales() {
        return ICUResourceBundle.getAvailableLocales();
    }

    public static ULocale[] getAvailableULocales() {
        return ICUResourceBundle.getAvailableULocales();
    }

    static {
        contextUsageTypeMap.put("month-format-except-narrow", CapitalizationContextUsage.MONTH_FORMAT);
        contextUsageTypeMap.put("month-standalone-except-narrow", CapitalizationContextUsage.MONTH_STANDALONE);
        contextUsageTypeMap.put("month-narrow", CapitalizationContextUsage.MONTH_NARROW);
        contextUsageTypeMap.put("day-format-except-narrow", CapitalizationContextUsage.DAY_FORMAT);
        contextUsageTypeMap.put("day-standalone-except-narrow", CapitalizationContextUsage.DAY_STANDALONE);
        contextUsageTypeMap.put("day-narrow", CapitalizationContextUsage.DAY_NARROW);
        contextUsageTypeMap.put("era-name", CapitalizationContextUsage.ERA_WIDE);
        contextUsageTypeMap.put("era-abbr", CapitalizationContextUsage.ERA_ABBREV);
        contextUsageTypeMap.put("era-narrow", CapitalizationContextUsage.ERA_NARROW);
        contextUsageTypeMap.put("zone-long", CapitalizationContextUsage.ZONE_LONG);
        contextUsageTypeMap.put("zone-short", CapitalizationContextUsage.ZONE_SHORT);
        contextUsageTypeMap.put("metazone-long", CapitalizationContextUsage.METAZONE_LONG);
        contextUsageTypeMap.put("metazone-short", CapitalizationContextUsage.METAZONE_SHORT);
        String[] strArr = LEAP_MONTH_PATTERNS_PATHS;
        strArr[0] = "monthPatterns/format/wide";
        strArr[1] = "monthPatterns/format/abbreviated";
        strArr[2] = "monthPatterns/format/narrow";
        strArr[3] = "monthPatterns/stand-alone/wide";
        strArr[4] = "monthPatterns/stand-alone/abbreviated";
        strArr[5] = "monthPatterns/stand-alone/narrow";
        strArr[6] = "monthPatterns/numeric/all";
    }

    public String[] getEras() {
        return duplicate(this.eras);
    }

    public void setEras(String[] strArr) {
        this.eras = duplicate(strArr);
    }

    public String[] getEraNames() {
        return duplicate(this.eraNames);
    }

    public void setEraNames(String[] strArr) {
        this.eraNames = duplicate(strArr);
    }

    public String[] getNarrowEras() {
        return duplicate(this.narrowEras);
    }

    public void setNarrowEras(String[] strArr) {
        this.narrowEras = duplicate(strArr);
    }

    public String[] getMonths() {
        return duplicate(this.months);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0021, code lost:
        if (r6 != 3) goto L_0x002c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x000f, code lost:
        if (r6 != 3) goto L_0x002c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String[] getMonths(int r5, int r6) {
        /*
            r4 = this;
            r0 = 3
            r1 = 2
            r2 = 1
            r3 = 0
            if (r5 == 0) goto L_0x001b
            if (r5 == r2) goto L_0x0009
            goto L_0x002c
        L_0x0009:
            if (r6 == 0) goto L_0x0018
            if (r6 == r2) goto L_0x0015
            if (r6 == r1) goto L_0x0012
            if (r6 == r0) goto L_0x0018
            goto L_0x002c
        L_0x0012:
            java.lang.String[] r3 = r4.standaloneNarrowMonths
            goto L_0x002c
        L_0x0015:
            java.lang.String[] r3 = r4.standaloneMonths
            goto L_0x002c
        L_0x0018:
            java.lang.String[] r3 = r4.standaloneShortMonths
            goto L_0x002c
        L_0x001b:
            if (r6 == 0) goto L_0x002a
            if (r6 == r2) goto L_0x0027
            if (r6 == r1) goto L_0x0024
            if (r6 == r0) goto L_0x002a
            goto L_0x002c
        L_0x0024:
            java.lang.String[] r3 = r4.narrowMonths
            goto L_0x002c
        L_0x0027:
            java.lang.String[] r3 = r4.months
            goto L_0x002c
        L_0x002a:
            java.lang.String[] r3 = r4.shortMonths
        L_0x002c:
            if (r3 == 0) goto L_0x0033
            java.lang.String[] r4 = r4.duplicate(r3)
            return r4
        L_0x0033:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
            java.lang.String r5 = "Bad context or width argument"
            r4.<init>(r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.DateFormatSymbols.getMonths(int, int):java.lang.String[]");
    }

    public void setMonths(String[] strArr) {
        this.months = duplicate(strArr);
    }

    public void setMonths(String[] strArr, int i, int i2) {
        if (i != 0) {
            if (i == 1) {
                if (i2 == 0) {
                    this.standaloneShortMonths = duplicate(strArr);
                } else if (i2 == 1) {
                    this.standaloneMonths = duplicate(strArr);
                } else if (i2 == 2) {
                    this.standaloneNarrowMonths = duplicate(strArr);
                }
            }
        } else if (i2 == 0) {
            this.shortMonths = duplicate(strArr);
        } else if (i2 == 1) {
            this.months = duplicate(strArr);
        } else if (i2 == 2) {
            this.narrowMonths = duplicate(strArr);
        }
    }

    public String[] getShortMonths() {
        return duplicate(this.shortMonths);
    }

    public void setShortMonths(String[] strArr) {
        this.shortMonths = duplicate(strArr);
    }

    public String[] getWeekdays() {
        return duplicate(this.weekdays);
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0044  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String[] getWeekdays(int r5, int r6) {
        /*
            r4 = this;
            r0 = 3
            r1 = 2
            r2 = 1
            r3 = 0
            if (r5 == 0) goto L_0x0024
            if (r5 == r2) goto L_0x0009
            goto L_0x003d
        L_0x0009:
            if (r6 == 0) goto L_0x0021
            if (r6 == r2) goto L_0x001e
            if (r6 == r1) goto L_0x001b
            if (r6 == r0) goto L_0x0012
            goto L_0x003d
        L_0x0012:
            java.lang.String[] r5 = r4.standaloneShorterWeekdays
            if (r5 == 0) goto L_0x0017
        L_0x0016:
            goto L_0x0019
        L_0x0017:
            java.lang.String[] r5 = r4.standaloneShortWeekdays
        L_0x0019:
            r3 = r5
            goto L_0x003d
        L_0x001b:
            java.lang.String[] r3 = r4.standaloneNarrowWeekdays
            goto L_0x003d
        L_0x001e:
            java.lang.String[] r3 = r4.standaloneWeekdays
            goto L_0x003d
        L_0x0021:
            java.lang.String[] r3 = r4.standaloneShortWeekdays
            goto L_0x003d
        L_0x0024:
            if (r6 == 0) goto L_0x003b
            if (r6 == r2) goto L_0x0038
            if (r6 == r1) goto L_0x0035
            if (r6 == r0) goto L_0x002d
            goto L_0x003d
        L_0x002d:
            java.lang.String[] r5 = r4.shorterWeekdays
            if (r5 == 0) goto L_0x0032
            goto L_0x0016
        L_0x0032:
            java.lang.String[] r5 = r4.shortWeekdays
            goto L_0x0019
        L_0x0035:
            java.lang.String[] r3 = r4.narrowWeekdays
            goto L_0x003d
        L_0x0038:
            java.lang.String[] r3 = r4.weekdays
            goto L_0x003d
        L_0x003b:
            java.lang.String[] r3 = r4.shortWeekdays
        L_0x003d:
            if (r3 == 0) goto L_0x0044
            java.lang.String[] r4 = r4.duplicate(r3)
            return r4
        L_0x0044:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
            java.lang.String r5 = "Bad context or width argument"
            r4.<init>(r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.DateFormatSymbols.getWeekdays(int, int):java.lang.String[]");
    }

    public void setWeekdays(String[] strArr, int i, int i2) {
        if (i != 0) {
            if (i == 1) {
                if (i2 == 0) {
                    this.standaloneShortWeekdays = duplicate(strArr);
                } else if (i2 == 1) {
                    this.standaloneWeekdays = duplicate(strArr);
                } else if (i2 == 2) {
                    this.standaloneNarrowWeekdays = duplicate(strArr);
                } else if (i2 == 3) {
                    this.standaloneShorterWeekdays = duplicate(strArr);
                }
            }
        } else if (i2 == 0) {
            this.shortWeekdays = duplicate(strArr);
        } else if (i2 == 1) {
            this.weekdays = duplicate(strArr);
        } else if (i2 == 2) {
            this.narrowWeekdays = duplicate(strArr);
        } else if (i2 == 3) {
            this.shorterWeekdays = duplicate(strArr);
        }
    }

    public void setWeekdays(String[] strArr) {
        this.weekdays = duplicate(strArr);
    }

    public String[] getShortWeekdays() {
        return duplicate(this.shortWeekdays);
    }

    public void setShortWeekdays(String[] strArr) {
        this.shortWeekdays = duplicate(strArr);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001e, code lost:
        if (r6 == 3) goto L_0x0024;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x000f, code lost:
        if (r6 == 3) goto L_0x0015;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String[] getQuarters(int r5, int r6) {
        /*
            r4 = this;
            r0 = 3
            r1 = 2
            r2 = 1
            r3 = 0
            if (r5 == 0) goto L_0x0018
            if (r5 == r2) goto L_0x0009
            goto L_0x0026
        L_0x0009:
            if (r6 == 0) goto L_0x0015
            if (r6 == r2) goto L_0x0012
            if (r6 == r1) goto L_0x0026
            if (r6 == r0) goto L_0x0015
            goto L_0x0026
        L_0x0012:
            java.lang.String[] r3 = r4.standaloneQuarters
            goto L_0x0026
        L_0x0015:
            java.lang.String[] r3 = r4.standaloneShortQuarters
            goto L_0x0026
        L_0x0018:
            if (r6 == 0) goto L_0x0024
            if (r6 == r2) goto L_0x0021
            if (r6 == r1) goto L_0x0026
            if (r6 == r0) goto L_0x0024
            goto L_0x0026
        L_0x0021:
            java.lang.String[] r3 = r4.quarters
            goto L_0x0026
        L_0x0024:
            java.lang.String[] r3 = r4.shortQuarters
        L_0x0026:
            if (r3 == 0) goto L_0x002d
            java.lang.String[] r4 = r4.duplicate(r3)
            return r4
        L_0x002d:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
            java.lang.String r5 = "Bad context or width argument"
            r4.<init>(r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.DateFormatSymbols.getQuarters(int, int):java.lang.String[]");
    }

    public void setQuarters(String[] strArr, int i, int i2) {
        if (i != 0) {
            if (i == 1) {
                if (i2 == 0) {
                    this.standaloneShortQuarters = duplicate(strArr);
                } else if (i2 == 1) {
                    this.standaloneQuarters = duplicate(strArr);
                }
            }
        } else if (i2 == 0) {
            this.shortQuarters = duplicate(strArr);
        } else if (i2 == 1) {
            this.quarters = duplicate(strArr);
        }
    }

    public String[] getYearNames(int i, int i2) {
        String[] strArr = this.shortYearNames;
        if (strArr != null) {
            return duplicate(strArr);
        }
        return null;
    }

    public void setYearNames(String[] strArr, int i, int i2) {
        if (i == 0 && i2 == 0) {
            this.shortYearNames = duplicate(strArr);
        }
    }

    public String[] getZodiacNames(int i, int i2) {
        String[] strArr = this.shortZodiacNames;
        if (strArr != null) {
            return duplicate(strArr);
        }
        return null;
    }

    public void setZodiacNames(String[] strArr, int i, int i2) {
        if (i == 0 && i2 == 0) {
            this.shortZodiacNames = duplicate(strArr);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0017, code lost:
        if (r6 != 3) goto L_0x002d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0027, code lost:
        if (r6 != 3) goto L_0x002d;
     */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x002f  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0034  */
    @java.lang.Deprecated
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getLeapMonthPattern(int r5, int r6) {
        /*
            r4 = this;
            java.lang.String[] r0 = r4.leapMonthPatterns
            if (r0 == 0) goto L_0x003c
            r0 = -1
            r1 = 3
            r2 = 2
            r3 = 1
            if (r5 == 0) goto L_0x0021
            if (r5 == r3) goto L_0x0011
            if (r5 == r2) goto L_0x000f
            goto L_0x002d
        L_0x000f:
            r0 = 6
            goto L_0x002d
        L_0x0011:
            if (r6 == 0) goto L_0x001f
            if (r6 == r3) goto L_0x001d
            if (r6 == r2) goto L_0x001a
            if (r6 == r1) goto L_0x001f
            goto L_0x002d
        L_0x001a:
            r5 = 5
            r0 = r5
            goto L_0x002d
        L_0x001d:
            r0 = r1
            goto L_0x002d
        L_0x001f:
            r0 = r3
            goto L_0x002d
        L_0x0021:
            if (r6 == 0) goto L_0x001f
            if (r6 == r3) goto L_0x002c
            if (r6 == r2) goto L_0x002a
            if (r6 == r1) goto L_0x001f
            goto L_0x002d
        L_0x002a:
            r0 = r2
            goto L_0x002d
        L_0x002c:
            r0 = 0
        L_0x002d:
            if (r0 < 0) goto L_0x0034
            java.lang.String[] r4 = r4.leapMonthPatterns
            r4 = r4[r0]
            return r4
        L_0x0034:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
            java.lang.String r5 = "Bad context or width argument"
            r4.<init>(r5)
            throw r4
        L_0x003c:
            r4 = 0
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.DateFormatSymbols.getLeapMonthPattern(int, int):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
    @java.lang.Deprecated
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setLeapMonthPattern(java.lang.String r4, int r5, int r6) {
        /*
            r3 = this;
            java.lang.String[] r0 = r3.leapMonthPatterns
            if (r0 == 0) goto L_0x002d
            r0 = -1
            r1 = 2
            r2 = 1
            if (r5 == 0) goto L_0x001b
            if (r5 == r2) goto L_0x0010
            if (r5 == r1) goto L_0x000e
            goto L_0x0027
        L_0x000e:
            r0 = 6
            goto L_0x0027
        L_0x0010:
            if (r6 == 0) goto L_0x0026
            if (r6 == r2) goto L_0x0019
            if (r6 == r1) goto L_0x0017
            goto L_0x0027
        L_0x0017:
            r0 = 5
            goto L_0x0027
        L_0x0019:
            r0 = 3
            goto L_0x0027
        L_0x001b:
            if (r6 == 0) goto L_0x0026
            if (r6 == r2) goto L_0x0024
            if (r6 == r1) goto L_0x0022
            goto L_0x0027
        L_0x0022:
            r0 = r1
            goto L_0x0027
        L_0x0024:
            r0 = 0
            goto L_0x0027
        L_0x0026:
            r0 = r2
        L_0x0027:
            if (r0 < 0) goto L_0x002d
            java.lang.String[] r3 = r3.leapMonthPatterns
            r3[r0] = r4
        L_0x002d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.DateFormatSymbols.setLeapMonthPattern(java.lang.String, int, int):void");
    }

    public String[] getAmPmStrings() {
        return duplicate(this.ampms);
    }

    public void setAmPmStrings(String[] strArr) {
        this.ampms = duplicate(strArr);
    }

    @Deprecated
    public String getTimeSeparatorString() {
        return this.timeSeparator;
    }

    @Deprecated
    public void setTimeSeparatorString(String str) {
        this.timeSeparator = str;
    }

    public String[][] getZoneStrings() {
        String[][] strArr = this.zoneStrings;
        if (strArr != null) {
            return duplicate(strArr);
        }
        String[] availableIDs = TimeZone.getAvailableIDs();
        TimeZoneNames instance = TimeZoneNames.getInstance(this.validLocale);
        instance.loadAllDisplayNames();
        TimeZoneNames.NameType[] nameTypeArr = {TimeZoneNames.NameType.LONG_STANDARD, TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.LONG_DAYLIGHT, TimeZoneNames.NameType.SHORT_DAYLIGHT};
        long currentTimeMillis = System.currentTimeMillis();
        String[][] strArr2 = (String[][]) Array.newInstance(String.class, availableIDs.length, 5);
        for (int i = 0; i < availableIDs.length; i++) {
            String canonicalID = TimeZone.getCanonicalID(availableIDs[i]);
            if (canonicalID == null) {
                canonicalID = availableIDs[i];
            }
            strArr2[i][0] = availableIDs[i];
            instance.getDisplayNames(canonicalID, nameTypeArr, currentTimeMillis, strArr2[i], 1);
        }
        this.zoneStrings = strArr2;
        return this.zoneStrings;
    }

    public void setZoneStrings(String[][] strArr) {
        this.zoneStrings = duplicate(strArr);
    }

    public String getLocalPatternChars() {
        return this.localPatternChars;
    }

    public void setLocalPatternChars(String str) {
        this.localPatternChars = str;
    }

    @Override // java.lang.Object
    public Object clone() {
        try {
            return (DateFormatSymbols) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new ICUCloneNotSupportedException(e);
        }
    }

    public int hashCode() {
        return this.requestedLocale.toString().hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DateFormatSymbols dateFormatSymbols = (DateFormatSymbols) obj;
        return Utility.arrayEquals(this.eras, dateFormatSymbols.eras) && Utility.arrayEquals(this.eraNames, dateFormatSymbols.eraNames) && Utility.arrayEquals(this.narrowEras, dateFormatSymbols.narrowEras) && Utility.arrayEquals(this.months, dateFormatSymbols.months) && Utility.arrayEquals(this.shortMonths, dateFormatSymbols.shortMonths) && Utility.arrayEquals(this.narrowMonths, dateFormatSymbols.narrowMonths) && Utility.arrayEquals(this.standaloneMonths, dateFormatSymbols.standaloneMonths) && Utility.arrayEquals(this.standaloneShortMonths, dateFormatSymbols.standaloneShortMonths) && Utility.arrayEquals(this.standaloneNarrowMonths, dateFormatSymbols.standaloneNarrowMonths) && Utility.arrayEquals(this.weekdays, dateFormatSymbols.weekdays) && Utility.arrayEquals(this.shortWeekdays, dateFormatSymbols.shortWeekdays) && Utility.arrayEquals(this.shorterWeekdays, dateFormatSymbols.shorterWeekdays) && Utility.arrayEquals(this.narrowWeekdays, dateFormatSymbols.narrowWeekdays) && Utility.arrayEquals(this.standaloneWeekdays, dateFormatSymbols.standaloneWeekdays) && Utility.arrayEquals(this.standaloneShortWeekdays, dateFormatSymbols.standaloneShortWeekdays) && Utility.arrayEquals(this.standaloneShorterWeekdays, dateFormatSymbols.standaloneShorterWeekdays) && Utility.arrayEquals(this.standaloneNarrowWeekdays, dateFormatSymbols.standaloneNarrowWeekdays) && Utility.arrayEquals(this.ampms, dateFormatSymbols.ampms) && Utility.arrayEquals(this.ampmsNarrow, dateFormatSymbols.ampmsNarrow) && Utility.arrayEquals(this.abbreviatedDayPeriods, dateFormatSymbols.abbreviatedDayPeriods) && Utility.arrayEquals(this.wideDayPeriods, dateFormatSymbols.wideDayPeriods) && Utility.arrayEquals(this.narrowDayPeriods, dateFormatSymbols.narrowDayPeriods) && Utility.arrayEquals(this.standaloneAbbreviatedDayPeriods, dateFormatSymbols.standaloneAbbreviatedDayPeriods) && Utility.arrayEquals(this.standaloneWideDayPeriods, dateFormatSymbols.standaloneWideDayPeriods) && Utility.arrayEquals(this.standaloneNarrowDayPeriods, dateFormatSymbols.standaloneNarrowDayPeriods) && Utility.arrayEquals(this.timeSeparator, dateFormatSymbols.timeSeparator) && arrayOfArrayEquals(this.zoneStrings, dateFormatSymbols.zoneStrings) && this.requestedLocale.getDisplayName().equals(dateFormatSymbols.requestedLocale.getDisplayName()) && Utility.arrayEquals(this.localPatternChars, dateFormatSymbols.localPatternChars);
    }

    /* access modifiers changed from: protected */
    public void initializeData(ULocale uLocale, String str) {
        String str2 = uLocale.getBaseName() + '+' + str;
        String keywordValue = uLocale.getKeywordValue("numbers");
        if (keywordValue != null && keywordValue.length() > 0) {
            str2 = str2 + '+' + keywordValue;
        }
        initializeData(DFSCACHE.getInstance(str2, uLocale));
    }

    /* access modifiers changed from: package-private */
    public void initializeData(DateFormatSymbols dateFormatSymbols) {
        this.eras = dateFormatSymbols.eras;
        this.eraNames = dateFormatSymbols.eraNames;
        this.narrowEras = dateFormatSymbols.narrowEras;
        this.months = dateFormatSymbols.months;
        this.shortMonths = dateFormatSymbols.shortMonths;
        this.narrowMonths = dateFormatSymbols.narrowMonths;
        this.standaloneMonths = dateFormatSymbols.standaloneMonths;
        this.standaloneShortMonths = dateFormatSymbols.standaloneShortMonths;
        this.standaloneNarrowMonths = dateFormatSymbols.standaloneNarrowMonths;
        this.weekdays = dateFormatSymbols.weekdays;
        this.shortWeekdays = dateFormatSymbols.shortWeekdays;
        this.shorterWeekdays = dateFormatSymbols.shorterWeekdays;
        this.narrowWeekdays = dateFormatSymbols.narrowWeekdays;
        this.standaloneWeekdays = dateFormatSymbols.standaloneWeekdays;
        this.standaloneShortWeekdays = dateFormatSymbols.standaloneShortWeekdays;
        this.standaloneShorterWeekdays = dateFormatSymbols.standaloneShorterWeekdays;
        this.standaloneNarrowWeekdays = dateFormatSymbols.standaloneNarrowWeekdays;
        this.ampms = dateFormatSymbols.ampms;
        this.ampmsNarrow = dateFormatSymbols.ampmsNarrow;
        this.timeSeparator = dateFormatSymbols.timeSeparator;
        this.shortQuarters = dateFormatSymbols.shortQuarters;
        this.quarters = dateFormatSymbols.quarters;
        this.standaloneShortQuarters = dateFormatSymbols.standaloneShortQuarters;
        this.standaloneQuarters = dateFormatSymbols.standaloneQuarters;
        this.leapMonthPatterns = dateFormatSymbols.leapMonthPatterns;
        this.shortYearNames = dateFormatSymbols.shortYearNames;
        this.shortZodiacNames = dateFormatSymbols.shortZodiacNames;
        this.abbreviatedDayPeriods = dateFormatSymbols.abbreviatedDayPeriods;
        this.wideDayPeriods = dateFormatSymbols.wideDayPeriods;
        this.narrowDayPeriods = dateFormatSymbols.narrowDayPeriods;
        this.standaloneAbbreviatedDayPeriods = dateFormatSymbols.standaloneAbbreviatedDayPeriods;
        this.standaloneWideDayPeriods = dateFormatSymbols.standaloneWideDayPeriods;
        this.standaloneNarrowDayPeriods = dateFormatSymbols.standaloneNarrowDayPeriods;
        this.zoneStrings = dateFormatSymbols.zoneStrings;
        this.localPatternChars = dateFormatSymbols.localPatternChars;
        this.capitalization = dateFormatSymbols.capitalization;
        this.actualLocale = dateFormatSymbols.actualLocale;
        this.validLocale = dateFormatSymbols.validLocale;
        this.requestedLocale = dateFormatSymbols.requestedLocale;
    }

    /* access modifiers changed from: private */
    public static final class CalendarDataSink extends UResource.Sink {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final String CALENDAR_ALIAS_PREFIX = "/LOCALE/calendar/";
        List<String> aliasPathPairs = new ArrayList();
        private String aliasRelativePath;
        Map<String, String[]> arrays = new TreeMap();
        String currentCalendarType = null;
        Map<String, Map<String, String>> maps = new TreeMap();
        String nextCalendarType = null;
        private Set<String> resourcesToVisit;

        /* access modifiers changed from: private */
        public enum AliasType {
            SAME_CALENDAR,
            DIFFERENT_CALENDAR,
            GREGORIAN,
            NONE
        }

        CalendarDataSink() {
        }

        /* access modifiers changed from: package-private */
        public void visitAllResources() {
            this.resourcesToVisit = null;
        }

        /* access modifiers changed from: package-private */
        public void preEnumerate(String str) {
            this.currentCalendarType = str;
            this.nextCalendarType = null;
            this.aliasPathPairs.clear();
        }

        /* JADX WARNING: Removed duplicated region for block: B:60:0x0123  */
        /* JADX WARNING: Removed duplicated region for block: B:61:0x0131  */
        @Override // ohos.global.icu.impl.UResource.Sink
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void put(ohos.global.icu.impl.UResource.Key r7, ohos.global.icu.impl.UResource.Value r8, boolean r9) {
            /*
            // Method dump skipped, instructions count: 323
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.DateFormatSymbols.CalendarDataSink.put(ohos.global.icu.impl.UResource$Key, ohos.global.icu.impl.UResource$Value, boolean):void");
        }

        /* access modifiers changed from: protected */
        public void processResource(String str, UResource.Key key, UResource.Value value) {
            UResource.Table table = value.getTable();
            HashMap hashMap = null;
            for (int i = 0; table.getKeyAndValue(i, key, value); i++) {
                if (!key.endsWith("%variant")) {
                    String key2 = key.toString();
                    if (value.getType() == 0) {
                        if (i == 0) {
                            hashMap = new HashMap();
                            this.maps.put(str, hashMap);
                        }
                        hashMap.put(key2, value.getString());
                    } else {
                        String str2 = str + "/" + key2;
                        if ((!str2.startsWith("cyclicNameSets") || "cyclicNameSets/years/format/abbreviated".startsWith(str2) || "cyclicNameSets/zodiacs/format/abbreviated".startsWith(str2) || "cyclicNameSets/dayParts/format/abbreviated".startsWith(str2)) && !this.arrays.containsKey(str2) && !this.maps.containsKey(str2)) {
                            if (processAliasFromValue(str2, value) == AliasType.SAME_CALENDAR) {
                                this.aliasPathPairs.add(this.aliasRelativePath);
                                this.aliasPathPairs.add(str2);
                            } else if (value.getType() == 8) {
                                this.arrays.put(str2, value.getStringArray());
                            } else if (value.getType() == 2) {
                                processResource(str2, key, value);
                            }
                        }
                    }
                }
            }
        }

        private AliasType processAliasFromValue(String str, UResource.Value value) {
            int indexOf;
            if (value.getType() != 3) {
                return AliasType.NONE;
            }
            String aliasString = value.getAliasString();
            if (aliasString.startsWith(CALENDAR_ALIAS_PREFIX) && aliasString.length() > 17 && (indexOf = aliasString.indexOf(47, 17)) > 17) {
                String substring = aliasString.substring(17, indexOf);
                this.aliasRelativePath = aliasString.substring(indexOf + 1);
                if (this.currentCalendarType.equals(substring) && !str.equals(this.aliasRelativePath)) {
                    return AliasType.SAME_CALENDAR;
                }
                if (!this.currentCalendarType.equals(substring) && str.equals(this.aliasRelativePath)) {
                    if (substring.equals("gregorian")) {
                        return AliasType.GREGORIAN;
                    }
                    String str2 = this.nextCalendarType;
                    if (str2 == null || str2.equals(substring)) {
                        this.nextCalendarType = substring;
                        return AliasType.DIFFERENT_CALENDAR;
                    }
                }
            }
            throw new ICUException("Malformed 'calendar' alias. Path: " + aliasString);
        }
    }

    private DateFormatSymbols(ULocale uLocale, ICUResourceBundle iCUResourceBundle, String str) {
        this.eras = null;
        this.eraNames = null;
        this.narrowEras = null;
        this.months = null;
        this.shortMonths = null;
        this.narrowMonths = null;
        this.standaloneMonths = null;
        this.standaloneShortMonths = null;
        this.standaloneNarrowMonths = null;
        this.weekdays = null;
        this.shortWeekdays = null;
        this.shorterWeekdays = null;
        this.narrowWeekdays = null;
        this.standaloneWeekdays = null;
        this.standaloneShortWeekdays = null;
        this.standaloneShorterWeekdays = null;
        this.standaloneNarrowWeekdays = null;
        this.ampms = null;
        this.ampmsNarrow = null;
        this.timeSeparator = null;
        this.shortQuarters = null;
        this.quarters = null;
        this.standaloneShortQuarters = null;
        this.standaloneQuarters = null;
        this.leapMonthPatterns = null;
        this.shortYearNames = null;
        this.shortZodiacNames = null;
        this.zoneStrings = null;
        this.localPatternChars = null;
        this.abbreviatedDayPeriods = null;
        this.wideDayPeriods = null;
        this.narrowDayPeriods = null;
        this.standaloneAbbreviatedDayPeriods = null;
        this.standaloneWideDayPeriods = null;
        this.standaloneNarrowDayPeriods = null;
        this.capitalization = null;
        initializeData(uLocale, iCUResourceBundle, str);
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public void initializeData(ULocale uLocale, ICUResourceBundle iCUResourceBundle, String str) {
        ICUResourceBundle iCUResourceBundle2;
        String str2;
        CapitalizationContextUsage capitalizationContextUsage;
        Map<String, String> map;
        String str3;
        CalendarDataSink calendarDataSink = new CalendarDataSink();
        if (iCUResourceBundle == null) {
            iCUResourceBundle = (ICUResourceBundle) UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, uLocale);
        }
        while (str != null) {
            ICUResourceBundle findWithFallback = iCUResourceBundle.findWithFallback("calendar/" + str);
            if (findWithFallback != null) {
                calendarDataSink.preEnumerate(str);
                findWithFallback.getAllItemsWithFallback("", calendarDataSink);
                if (str.equals("gregorian")) {
                    break;
                }
                str = calendarDataSink.nextCalendarType;
                if (str == null) {
                    calendarDataSink.visitAllResources();
                }
            } else if (!"gregorian".equals(str)) {
                calendarDataSink.visitAllResources();
            } else {
                throw new MissingResourceException("The 'gregorian' calendar type wasn't found for the locale: " + uLocale.getBaseName(), getClass().getName(), "gregorian");
            }
            str = "gregorian";
        }
        Map<String, String[]> map2 = calendarDataSink.arrays;
        Map<String, Map<String, String>> map3 = calendarDataSink.maps;
        this.eras = map2.get("eras/abbreviated");
        this.eraNames = map2.get("eras/wide");
        this.narrowEras = map2.get("eras/narrow");
        this.months = map2.get("monthNames/format/wide");
        this.shortMonths = map2.get("monthNames/format/abbreviated");
        this.narrowMonths = map2.get("monthNames/format/narrow");
        this.standaloneMonths = map2.get("monthNames/stand-alone/wide");
        this.standaloneShortMonths = map2.get("monthNames/stand-alone/abbreviated");
        this.standaloneNarrowMonths = map2.get("monthNames/stand-alone/narrow");
        String[] strArr = map2.get("dayNames/format/wide");
        this.weekdays = new String[8];
        String[] strArr2 = this.weekdays;
        strArr2[0] = "";
        System.arraycopy(strArr, 0, strArr2, 1, strArr.length);
        String[] strArr3 = map2.get("dayNames/format/abbreviated");
        this.shortWeekdays = new String[8];
        String[] strArr4 = this.shortWeekdays;
        strArr4[0] = "";
        System.arraycopy(strArr3, 0, strArr4, 1, strArr3.length);
        String[] strArr5 = map2.get("dayNames/format/short");
        this.shorterWeekdays = new String[8];
        String[] strArr6 = this.shorterWeekdays;
        strArr6[0] = "";
        System.arraycopy(strArr5, 0, strArr6, 1, strArr5.length);
        String[] strArr7 = map2.get("dayNames/format/narrow");
        if (strArr7 == null && (strArr7 = map2.get("dayNames/stand-alone/narrow")) == null && (strArr7 = map2.get("dayNames/format/abbreviated")) == null) {
            throw new MissingResourceException("Resource not found", getClass().getName(), "dayNames/format/abbreviated");
        }
        this.narrowWeekdays = new String[8];
        String[] strArr8 = this.narrowWeekdays;
        strArr8[0] = "";
        System.arraycopy(strArr7, 0, strArr8, 1, strArr7.length);
        String[] strArr9 = map2.get("dayNames/stand-alone/wide");
        this.standaloneWeekdays = new String[8];
        String[] strArr10 = this.standaloneWeekdays;
        strArr10[0] = "";
        System.arraycopy(strArr9, 0, strArr10, 1, strArr9.length);
        String[] strArr11 = map2.get("dayNames/stand-alone/abbreviated");
        this.standaloneShortWeekdays = new String[8];
        String[] strArr12 = this.standaloneShortWeekdays;
        strArr12[0] = "";
        System.arraycopy(strArr11, 0, strArr12, 1, strArr11.length);
        String[] strArr13 = map2.get("dayNames/stand-alone/short");
        this.standaloneShorterWeekdays = new String[8];
        String[] strArr14 = this.standaloneShorterWeekdays;
        strArr14[0] = "";
        System.arraycopy(strArr13, 0, strArr14, 1, strArr13.length);
        String[] strArr15 = map2.get("dayNames/stand-alone/narrow");
        this.standaloneNarrowWeekdays = new String[8];
        String[] strArr16 = this.standaloneNarrowWeekdays;
        strArr16[0] = "";
        System.arraycopy(strArr15, 0, strArr16, 1, strArr15.length);
        this.ampms = map2.get("AmPmMarkers");
        this.ampmsNarrow = map2.get("AmPmMarkersNarrow");
        this.quarters = map2.get("quarters/format/wide");
        this.shortQuarters = map2.get("quarters/format/abbreviated");
        this.standaloneQuarters = map2.get("quarters/stand-alone/wide");
        this.standaloneShortQuarters = map2.get("quarters/stand-alone/abbreviated");
        this.abbreviatedDayPeriods = loadDayPeriodStrings(map3.get("dayPeriod/format/abbreviated"));
        this.wideDayPeriods = loadDayPeriodStrings(map3.get("dayPeriod/format/wide"));
        this.narrowDayPeriods = loadDayPeriodStrings(map3.get("dayPeriod/format/narrow"));
        this.standaloneAbbreviatedDayPeriods = loadDayPeriodStrings(map3.get("dayPeriod/stand-alone/abbreviated"));
        this.standaloneWideDayPeriods = loadDayPeriodStrings(map3.get("dayPeriod/stand-alone/wide"));
        this.standaloneNarrowDayPeriods = loadDayPeriodStrings(map3.get("dayPeriod/stand-alone/narrow"));
        for (int i = 0; i < 7; i++) {
            String str4 = LEAP_MONTH_PATTERNS_PATHS[i];
            if (!(str4 == null || (map = map3.get(str4)) == null || (str3 = map.get("leap")) == null)) {
                if (this.leapMonthPatterns == null) {
                    this.leapMonthPatterns = new String[7];
                }
                this.leapMonthPatterns[i] = str3;
            }
        }
        this.shortYearNames = map2.get("cyclicNameSets/years/format/abbreviated");
        this.shortZodiacNames = map2.get("cyclicNameSets/zodiacs/format/abbreviated");
        this.requestedLocale = uLocale;
        ICUResourceBundle iCUResourceBundle3 = (ICUResourceBundle) UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, uLocale);
        this.localPatternChars = patternChars;
        ULocale uLocale2 = iCUResourceBundle3.getULocale();
        setLocale(uLocale2, uLocale2);
        this.capitalization = new HashMap();
        boolean[] zArr = {false, false};
        for (CapitalizationContextUsage capitalizationContextUsage2 : CapitalizationContextUsage.values()) {
            this.capitalization.put(capitalizationContextUsage2, zArr);
        }
        try {
            iCUResourceBundle2 = iCUResourceBundle3.getWithFallback("contextTransforms");
        } catch (MissingResourceException unused) {
            iCUResourceBundle2 = null;
        }
        if (iCUResourceBundle2 != null) {
            UResourceBundleIterator iterator = iCUResourceBundle2.getIterator();
            while (iterator.hasNext()) {
                UResourceBundle next = iterator.next();
                int[] intVector = next.getIntVector();
                if (intVector.length >= 2 && (capitalizationContextUsage = contextUsageTypeMap.get(next.getKey())) != null) {
                    boolean[] zArr2 = new boolean[2];
                    zArr2[0] = intVector[0] != 0;
                    zArr2[1] = intVector[1] != 0;
                    this.capitalization.put(capitalizationContextUsage, zArr2);
                }
            }
        }
        NumberingSystem instance = NumberingSystem.getInstance(uLocale);
        if (instance == null) {
            str2 = "latn";
        } else {
            str2 = instance.getName();
        }
        try {
            setTimeSeparatorString(iCUResourceBundle3.getStringWithFallback("NumberElements/" + str2 + "/symbols/timeSeparator"));
        } catch (MissingResourceException unused2) {
            setTimeSeparatorString(DEFAULT_TIME_SEPARATOR);
        }
    }

    private static final boolean arrayOfArrayEquals(Object[][] objArr, Object[][] objArr2) {
        boolean z = true;
        if (objArr == objArr2) {
            return true;
        }
        int i = 0;
        if (objArr == null || objArr2 == null || objArr.length != objArr2.length) {
            return false;
        }
        while (i < objArr.length && (z = Utility.arrayEquals(objArr[i], (Object) objArr2[i]))) {
            i++;
        }
        return z;
    }

    private String[] loadDayPeriodStrings(Map<String, String> map) {
        String[] strArr = new String[DAY_PERIOD_KEYS.length];
        if (map != null) {
            int i = 0;
            while (true) {
                String[] strArr2 = DAY_PERIOD_KEYS;
                if (i >= strArr2.length) {
                    break;
                }
                strArr[i] = map.get(strArr2[i]);
                i++;
            }
        }
        return strArr;
    }

    private final String[] duplicate(String[] strArr) {
        return (String[]) strArr.clone();
    }

    private final String[][] duplicate(String[][] strArr) {
        String[][] strArr2 = new String[strArr.length][];
        for (int i = 0; i < strArr.length; i++) {
            strArr2[i] = duplicate(strArr[i]);
        }
        return strArr2;
    }

    public DateFormatSymbols(Calendar calendar, Locale locale) {
        this.eras = null;
        this.eraNames = null;
        this.narrowEras = null;
        this.months = null;
        this.shortMonths = null;
        this.narrowMonths = null;
        this.standaloneMonths = null;
        this.standaloneShortMonths = null;
        this.standaloneNarrowMonths = null;
        this.weekdays = null;
        this.shortWeekdays = null;
        this.shorterWeekdays = null;
        this.narrowWeekdays = null;
        this.standaloneWeekdays = null;
        this.standaloneShortWeekdays = null;
        this.standaloneShorterWeekdays = null;
        this.standaloneNarrowWeekdays = null;
        this.ampms = null;
        this.ampmsNarrow = null;
        this.timeSeparator = null;
        this.shortQuarters = null;
        this.quarters = null;
        this.standaloneShortQuarters = null;
        this.standaloneQuarters = null;
        this.leapMonthPatterns = null;
        this.shortYearNames = null;
        this.shortZodiacNames = null;
        this.zoneStrings = null;
        this.localPatternChars = null;
        this.abbreviatedDayPeriods = null;
        this.wideDayPeriods = null;
        this.narrowDayPeriods = null;
        this.standaloneAbbreviatedDayPeriods = null;
        this.standaloneWideDayPeriods = null;
        this.standaloneNarrowDayPeriods = null;
        this.capitalization = null;
        initializeData(ULocale.forLocale(locale), calendar.getType());
    }

    public DateFormatSymbols(Calendar calendar, ULocale uLocale) {
        this.eras = null;
        this.eraNames = null;
        this.narrowEras = null;
        this.months = null;
        this.shortMonths = null;
        this.narrowMonths = null;
        this.standaloneMonths = null;
        this.standaloneShortMonths = null;
        this.standaloneNarrowMonths = null;
        this.weekdays = null;
        this.shortWeekdays = null;
        this.shorterWeekdays = null;
        this.narrowWeekdays = null;
        this.standaloneWeekdays = null;
        this.standaloneShortWeekdays = null;
        this.standaloneShorterWeekdays = null;
        this.standaloneNarrowWeekdays = null;
        this.ampms = null;
        this.ampmsNarrow = null;
        this.timeSeparator = null;
        this.shortQuarters = null;
        this.quarters = null;
        this.standaloneShortQuarters = null;
        this.standaloneQuarters = null;
        this.leapMonthPatterns = null;
        this.shortYearNames = null;
        this.shortZodiacNames = null;
        this.zoneStrings = null;
        this.localPatternChars = null;
        this.abbreviatedDayPeriods = null;
        this.wideDayPeriods = null;
        this.narrowDayPeriods = null;
        this.standaloneAbbreviatedDayPeriods = null;
        this.standaloneWideDayPeriods = null;
        this.standaloneNarrowDayPeriods = null;
        this.capitalization = null;
        initializeData(uLocale, calendar.getType());
    }

    public DateFormatSymbols(Class<? extends Calendar> cls, Locale locale) {
        this(cls, ULocale.forLocale(locale));
    }

    public DateFormatSymbols(Class<? extends Calendar> cls, ULocale uLocale) {
        String str = null;
        this.eras = null;
        this.eraNames = null;
        this.narrowEras = null;
        this.months = null;
        this.shortMonths = null;
        this.narrowMonths = null;
        this.standaloneMonths = null;
        this.standaloneShortMonths = null;
        this.standaloneNarrowMonths = null;
        this.weekdays = null;
        this.shortWeekdays = null;
        this.shorterWeekdays = null;
        this.narrowWeekdays = null;
        this.standaloneWeekdays = null;
        this.standaloneShortWeekdays = null;
        this.standaloneShorterWeekdays = null;
        this.standaloneNarrowWeekdays = null;
        this.ampms = null;
        this.ampmsNarrow = null;
        this.timeSeparator = null;
        this.shortQuarters = null;
        this.quarters = null;
        this.standaloneShortQuarters = null;
        this.standaloneQuarters = null;
        this.leapMonthPatterns = null;
        this.shortYearNames = null;
        this.shortZodiacNames = null;
        this.zoneStrings = null;
        this.localPatternChars = null;
        this.abbreviatedDayPeriods = null;
        this.wideDayPeriods = null;
        this.narrowDayPeriods = null;
        this.standaloneAbbreviatedDayPeriods = null;
        this.standaloneWideDayPeriods = null;
        this.standaloneNarrowDayPeriods = null;
        this.capitalization = null;
        String name = cls.getName();
        String substring = name.substring(name.lastIndexOf(46) + 1);
        String[][] strArr = CALENDAR_CLASSES;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            String[] strArr2 = strArr[i];
            if (strArr2[0].equals(substring)) {
                str = strArr2[1];
                break;
            }
            i++;
        }
        initializeData(uLocale, str == null ? substring.replaceAll("Calendar", "").toLowerCase(Locale.ENGLISH) : str);
    }

    public DateFormatSymbols(ResourceBundle resourceBundle, Locale locale) {
        this(resourceBundle, ULocale.forLocale(locale));
    }

    public DateFormatSymbols(ResourceBundle resourceBundle, ULocale uLocale) {
        this.eras = null;
        this.eraNames = null;
        this.narrowEras = null;
        this.months = null;
        this.shortMonths = null;
        this.narrowMonths = null;
        this.standaloneMonths = null;
        this.standaloneShortMonths = null;
        this.standaloneNarrowMonths = null;
        this.weekdays = null;
        this.shortWeekdays = null;
        this.shorterWeekdays = null;
        this.narrowWeekdays = null;
        this.standaloneWeekdays = null;
        this.standaloneShortWeekdays = null;
        this.standaloneShorterWeekdays = null;
        this.standaloneNarrowWeekdays = null;
        this.ampms = null;
        this.ampmsNarrow = null;
        this.timeSeparator = null;
        this.shortQuarters = null;
        this.quarters = null;
        this.standaloneShortQuarters = null;
        this.standaloneQuarters = null;
        this.leapMonthPatterns = null;
        this.shortYearNames = null;
        this.shortZodiacNames = null;
        this.zoneStrings = null;
        this.localPatternChars = null;
        this.abbreviatedDayPeriods = null;
        this.wideDayPeriods = null;
        this.narrowDayPeriods = null;
        this.standaloneAbbreviatedDayPeriods = null;
        this.standaloneWideDayPeriods = null;
        this.standaloneNarrowDayPeriods = null;
        this.capitalization = null;
        initializeData(uLocale, (ICUResourceBundle) resourceBundle, CalendarUtil.getCalendarType(uLocale));
    }

    public final ULocale getLocale(ULocale.Type type) {
        return type == ULocale.ACTUAL_LOCALE ? this.actualLocale : this.validLocale;
    }

    /* access modifiers changed from: package-private */
    public final void setLocale(ULocale uLocale, ULocale uLocale2) {
        boolean z = true;
        boolean z2 = uLocale == null;
        if (uLocale2 != null) {
            z = false;
        }
        if (z2 == z) {
            this.validLocale = uLocale;
            this.actualLocale = uLocale2;
            return;
        }
        throw new IllegalArgumentException();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
    }
}
