package ohos.global.icu.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import ohos.com.sun.org.apache.xalan.internal.templates.Constants;
import ohos.devtools.JLogConstants;
import ohos.global.icu.impl.CalType;
import ohos.global.icu.impl.CalendarUtil;
import ohos.global.icu.impl.ICUCache;
import ohos.global.icu.impl.ICUData;
import ohos.global.icu.impl.ICUResourceBundle;
import ohos.global.icu.impl.SimpleCache;
import ohos.global.icu.impl.SimpleFormatterImpl;
import ohos.global.icu.impl.SoftCache;
import ohos.global.icu.impl.number.RoundingUtils;
import ohos.global.icu.text.DateFormat;
import ohos.global.icu.text.DateFormatSymbols;
import ohos.global.icu.text.SimpleDateFormat;
import ohos.global.icu.util.ULocale;
import ohos.telephony.TelephoneNumberUtils;
import ohos.workschedulerservice.controller.WorkStatus;

public abstract class Calendar implements Serializable, Cloneable, Comparable<Calendar> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int AM = 0;
    public static final int AM_PM = 9;
    public static final int APRIL = 3;
    public static final int AUGUST = 7;
    @Deprecated
    protected static final int BASE_FIELD_COUNT = 23;
    public static final int DATE = 5;
    static final int[][][] DATE_PRECEDENCE = {new int[][]{new int[]{5}, new int[]{3, 7}, new int[]{4, 7}, new int[]{8, 7}, new int[]{3, 18}, new int[]{4, 18}, new int[]{8, 18}, new int[]{6}, new int[]{37, 1}, new int[]{35, 17}}, new int[][]{new int[]{3}, new int[]{4}, new int[]{8}, new int[]{40, 7}, new int[]{40, 18}}};
    public static final int DAY_OF_MONTH = 5;
    public static final int DAY_OF_WEEK = 7;
    public static final int DAY_OF_WEEK_IN_MONTH = 8;
    public static final int DAY_OF_YEAR = 6;
    public static final int DECEMBER = 11;
    private static final String[] DEFAULT_PATTERNS = {"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm", "EEEE, yyyy MMMM dd", "yyyy MMMM d", "yyyy MMM d", "yy/MM/dd", "{1} {0}", "{1} {0}", "{1} {0}", "{1} {0}", "{1} {0}"};
    public static final int DOW_LOCAL = 18;
    static final int[][][] DOW_PRECEDENCE = {new int[][]{new int[]{7}, new int[]{18}}};
    public static final int DST_OFFSET = 16;
    protected static final int EPOCH_JULIAN_DAY = 2440588;
    public static final int ERA = 0;
    public static final int EXTENDED_YEAR = 19;
    public static final int FEBRUARY = 1;
    private static final int FIELD_DIFF_MAX_INT = Integer.MAX_VALUE;
    private static final String[] FIELD_NAME = {"ERA", "YEAR", "MONTH", "WEEK_OF_YEAR", "WEEK_OF_MONTH", "DAY_OF_MONTH", "DAY_OF_YEAR", "DAY_OF_WEEK", "DAY_OF_WEEK_IN_MONTH", "AM_PM", "HOUR", "HOUR_OF_DAY", "MINUTE", "SECOND", "MILLISECOND", "ZONE_OFFSET", "DST_OFFSET", "YEAR_WOY", "DOW_LOCAL", "EXTENDED_YEAR", "JULIAN_DAY", "MILLISECONDS_IN_DAY"};
    private static final int[] FIND_ZONE_TRANSITION_TIME_UNITS = {3600000, 1800000, 60000, 1000};
    public static final int FRIDAY = 6;
    protected static final int GREATEST_MINIMUM = 1;
    private static final int[][] GREGORIAN_MONTH_COUNT = {new int[]{31, 31, 0, 0}, new int[]{28, 29, 31, 31}, new int[]{31, 31, 59, 60}, new int[]{30, 30, 90, 91}, new int[]{31, 31, 120, 121}, new int[]{30, 30, 151, 152}, new int[]{31, 31, 181, 182}, new int[]{31, 31, 212, 213}, new int[]{30, 30, 243, 244}, new int[]{31, 31, 273, 274}, new int[]{30, 30, 304, 305}, new int[]{31, 31, JLogConstants.JLID_CAMERA_CLOSECAMERA_END, JLogConstants.JLID_ACTIVITY_LAUNCHING_BEGIN}};
    public static final int HOUR = 10;
    public static final int HOUR_OF_DAY = 11;
    protected static final int INTERNALLY_SET = 1;
    public static final int IS_LEAP_MONTH = 22;
    public static final int JANUARY = 0;
    protected static final int JAN_1_1_JULIAN_DAY = 1721426;
    public static final int JULIAN_DAY = 20;
    public static final int JULY = 6;
    public static final int JUNE = 5;
    protected static final int LEAST_MAXIMUM = 2;
    private static final int[][] LIMITS = {new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[]{1, 1, 7, 7}, new int[0], new int[]{0, 0, 1, 1}, new int[]{0, 0, 11, 11}, new int[]{0, 0, 23, 23}, new int[]{0, 0, 59, 59}, new int[]{0, 0, 59, 59}, new int[]{0, 0, RoundingUtils.MAX_INT_FRAC_SIG, RoundingUtils.MAX_INT_FRAC_SIG}, new int[]{-43200000, -43200000, 43200000, 43200000}, new int[]{0, 0, 3600000, 3600000}, new int[0], new int[]{1, 1, 7, 7}, new int[0], new int[]{MIN_JULIAN, MIN_JULIAN, 2130706432, 2130706432}, new int[]{0, 0, 86399999, 86399999}, new int[]{0, 0, 1, 1}};
    public static final int MARCH = 2;
    protected static final int MAXIMUM = 3;
    protected static final Date MAX_DATE = new Date(183882168921600000L);
    @Deprecated
    protected static final int MAX_FIELD_COUNT = 32;
    private static final int MAX_HOURS = 548;
    protected static final int MAX_JULIAN = 2130706432;
    protected static final long MAX_MILLIS = 183882168921600000L;
    public static final int MAY = 4;
    public static final int MILLISECOND = 14;
    public static final int MILLISECONDS_IN_DAY = 21;
    protected static final int MINIMUM = 0;
    protected static final int MINIMUM_USER_STAMP = 2;
    public static final int MINUTE = 12;
    protected static final Date MIN_DATE = new Date(-184303902528000000L);
    protected static final int MIN_JULIAN = -2130706432;
    protected static final long MIN_MILLIS = -184303902528000000L;
    public static final int MONDAY = 2;
    public static final int MONTH = 2;
    public static final int NOVEMBER = 10;
    public static final int OCTOBER = 9;
    protected static final long ONE_DAY = 86400000;
    protected static final int ONE_HOUR = 3600000;
    protected static final int ONE_MINUTE = 60000;
    protected static final int ONE_SECOND = 1000;
    protected static final long ONE_WEEK = 604800000;
    private static final ICUCache<String, PatternData> PATTERN_CACHE = new SimpleCache();
    public static final int PM = 1;
    private static final char QUOTE = '\'';
    protected static final int RESOLVE_REMAP = 32;
    public static final int SATURDAY = 7;
    public static final int SECOND = 13;
    public static final int SEPTEMBER = 8;
    private static int STAMP_MAX = JLogConstants.JLID_DISTRIBUTE_FILE_READ;
    public static final int SUNDAY = 1;
    public static final int THURSDAY = 5;
    public static final int TUESDAY = 3;
    public static final int UNDECIMBER = 12;
    protected static final int UNSET = 0;
    public static final int WALLTIME_FIRST = 1;
    public static final int WALLTIME_LAST = 0;
    public static final int WALLTIME_NEXT_VALID = 2;
    public static final int WEDNESDAY = 4;
    @Deprecated
    public static final int WEEKDAY = 0;
    @Deprecated
    public static final int WEEKEND = 1;
    @Deprecated
    public static final int WEEKEND_CEASE = 3;
    @Deprecated
    public static final int WEEKEND_ONSET = 2;
    private static final WeekDataCache WEEK_DATA_CACHE = new WeekDataCache();
    public static final int WEEK_OF_MONTH = 4;
    public static final int WEEK_OF_YEAR = 3;
    public static final int YEAR = 1;
    public static final int YEAR_WOY = 17;
    public static final int ZONE_OFFSET = 15;
    private static final long serialVersionUID = 6222646104888790989L;
    private ULocale actualLocale;
    private transient boolean areAllFieldsSet;
    private transient boolean areFieldsSet;
    private transient boolean areFieldsVirtuallySet;
    private transient int[] fields;
    private int firstDayOfWeek;
    private transient int gregorianDayOfMonth;
    private transient int gregorianDayOfYear;
    private transient int gregorianMonth;
    private transient int gregorianYear;
    private transient int internalSetMask;
    private transient boolean isTimeSet;
    private boolean lenient;
    private int minimalDaysInFirstWeek;
    private transient int nextStamp;
    private int repeatedWallTime;
    private int skippedWallTime;
    private transient int[] stamp;
    private long time;
    private ULocale validLocale;
    private int weekendCease;
    private int weekendCeaseMillis;
    private int weekendOnset;
    private int weekendOnsetMillis;
    private TimeZone zone;

    protected static final long julianDayToMillis(int i) {
        return ((long) (i - EPOCH_JULIAN_DAY)) * 86400000;
    }

    /* access modifiers changed from: protected */
    public int getDefaultDayInMonth(int i, int i2) {
        return 1;
    }

    /* access modifiers changed from: protected */
    public int getDefaultMonthInYear(int i) {
        return 0;
    }

    public String getType() {
        return "unknown";
    }

    /* access modifiers changed from: protected */
    public abstract int handleComputeMonthStart(int i, int i2, boolean z);

    /* access modifiers changed from: protected */
    public int[] handleCreateFields() {
        return new int[23];
    }

    /* access modifiers changed from: protected */
    public abstract int handleGetExtendedYear();

    /* access modifiers changed from: protected */
    public abstract int handleGetLimit(int i, int i2);

    @Deprecated
    public boolean haveDefaultCentury() {
        return true;
    }

    protected Calendar() {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    }

    protected Calendar(TimeZone timeZone, Locale locale) {
        this(timeZone, ULocale.forLocale(locale));
    }

    protected Calendar(TimeZone timeZone, ULocale uLocale) {
        this.lenient = true;
        this.repeatedWallTime = 0;
        this.skippedWallTime = 0;
        this.nextStamp = 2;
        this.zone = timeZone;
        setWeekData(getRegionForCalendar(uLocale));
        setCalendarLocale(uLocale);
        initInternal();
    }

    private void setCalendarLocale(ULocale uLocale) {
        if (!(uLocale.getVariant().length() == 0 && uLocale.getKeywords() == null)) {
            StringBuilder sb = new StringBuilder();
            sb.append(uLocale.getLanguage());
            String script = uLocale.getScript();
            if (script.length() > 0) {
                sb.append("_");
                sb.append(script);
            }
            String country = uLocale.getCountry();
            if (country.length() > 0) {
                sb.append("_");
                sb.append(country);
            }
            String keywordValue = uLocale.getKeywordValue("calendar");
            if (keywordValue != null) {
                sb.append("@calendar=");
                sb.append(keywordValue);
            }
            uLocale = new ULocale(sb.toString());
        }
        setLocale(uLocale, uLocale);
    }

    private void recalculateStamp() {
        int[] iArr;
        this.nextStamp = 1;
        for (int i = 0; i < this.stamp.length; i++) {
            int i2 = -1;
            int i3 = STAMP_MAX;
            int i4 = 0;
            while (true) {
                iArr = this.stamp;
                if (i4 >= iArr.length) {
                    break;
                }
                if (iArr[i4] > this.nextStamp && iArr[i4] < i3) {
                    i3 = iArr[i4];
                    i2 = i4;
                }
                i4++;
            }
            if (i2 < 0) {
                break;
            }
            int i5 = this.nextStamp + 1;
            this.nextStamp = i5;
            iArr[i2] = i5;
        }
        this.nextStamp++;
    }

    private void initInternal() {
        this.fields = handleCreateFields();
        int[] iArr = this.fields;
        if (iArr != null) {
            if (iArr.length >= 23 && iArr.length <= 32) {
                this.stamp = new int[iArr.length];
                int i = 4718695;
                for (int i2 = 23; i2 < this.fields.length; i2++) {
                    i |= 1 << i2;
                }
                this.internalSetMask = i;
                return;
            }
        }
        throw new IllegalStateException("Invalid fields[]");
    }

    public static Calendar getInstance() {
        return getInstanceInternal(null, null);
    }

    public static Calendar getInstance(TimeZone timeZone) {
        return getInstanceInternal(timeZone, null);
    }

    public static Calendar getInstance(Locale locale) {
        return getInstanceInternal(null, ULocale.forLocale(locale));
    }

    public static Calendar getInstance(ULocale uLocale) {
        return getInstanceInternal(null, uLocale);
    }

    public static Calendar getInstance(TimeZone timeZone, Locale locale) {
        return getInstanceInternal(timeZone, ULocale.forLocale(locale));
    }

    public static Calendar getInstance(TimeZone timeZone, ULocale uLocale) {
        return getInstanceInternal(timeZone, uLocale);
    }

    private static Calendar getInstanceInternal(TimeZone timeZone, ULocale uLocale) {
        if (uLocale == null) {
            uLocale = ULocale.getDefault(ULocale.Category.FORMAT);
        }
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        Calendar createInstance = createInstance(uLocale);
        createInstance.setTimeZone(timeZone);
        createInstance.setTimeInMillis(System.currentTimeMillis());
        return createInstance;
    }

    private static String getRegionForCalendar(ULocale uLocale) {
        String regionForSupplementalData = ULocale.getRegionForSupplementalData(uLocale, true);
        return regionForSupplementalData.length() == 0 ? "001" : regionForSupplementalData;
    }

    private static CalType getCalendarTypeForLocale(ULocale uLocale) {
        String calendarType = CalendarUtil.getCalendarType(uLocale);
        if (calendarType != null) {
            String lowerCase = calendarType.toLowerCase(Locale.ENGLISH);
            CalType[] values = CalType.values();
            for (CalType calType : values) {
                if (lowerCase.equals(calType.getId())) {
                    return calType;
                }
            }
        }
        return CalType.UNKNOWN;
    }

    private static Calendar createInstance(ULocale uLocale) {
        TimeZone timeZone = TimeZone.getDefault();
        CalType calendarTypeForLocale = getCalendarTypeForLocale(uLocale);
        if (calendarTypeForLocale == CalType.UNKNOWN) {
            calendarTypeForLocale = CalType.GREGORIAN;
        }
        switch (calendarTypeForLocale) {
            case GREGORIAN:
                return new GregorianCalendar(timeZone, uLocale);
            case ISO8601:
                GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone, uLocale);
                gregorianCalendar.setFirstDayOfWeek(2);
                gregorianCalendar.setMinimalDaysInFirstWeek(4);
                return gregorianCalendar;
            case BUDDHIST:
                return new BuddhistCalendar(timeZone, uLocale);
            case CHINESE:
                return new ChineseCalendar(timeZone, uLocale);
            case COPTIC:
                return new CopticCalendar(timeZone, uLocale);
            case DANGI:
                return new DangiCalendar(timeZone, uLocale);
            case ETHIOPIC:
                return new EthiopicCalendar(timeZone, uLocale);
            case ETHIOPIC_AMETE_ALEM:
                EthiopicCalendar ethiopicCalendar = new EthiopicCalendar(timeZone, uLocale);
                ethiopicCalendar.setAmeteAlemEra(true);
                return ethiopicCalendar;
            case HEBREW:
                return new HebrewCalendar(timeZone, uLocale);
            case INDIAN:
                return new IndianCalendar(timeZone, uLocale);
            case ISLAMIC_CIVIL:
            case ISLAMIC_UMALQURA:
            case ISLAMIC_TBLA:
            case ISLAMIC_RGSA:
            case ISLAMIC:
                return new IslamicCalendar(timeZone, uLocale);
            case JAPANESE:
                return new JapaneseCalendar(timeZone, uLocale);
            case PERSIAN:
                return new PersianCalendar(timeZone, uLocale);
            case ROC:
                return new TaiwanCalendar(timeZone, uLocale);
            default:
                throw new IllegalArgumentException("Unknown calendar type");
        }
    }

    public static Locale[] getAvailableLocales() {
        return ICUResourceBundle.getAvailableLocales();
    }

    public static ULocale[] getAvailableULocales() {
        return ICUResourceBundle.getAvailableULocales();
    }

    public static final String[] getKeywordValuesForLocale(String str, ULocale uLocale, boolean z) {
        UResourceBundle uResourceBundle;
        String regionForSupplementalData = ULocale.getRegionForSupplementalData(uLocale, true);
        ArrayList arrayList = new ArrayList();
        UResourceBundle uResourceBundle2 = UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER).get("calendarPreferenceData");
        try {
            uResourceBundle = uResourceBundle2.get(regionForSupplementalData);
        } catch (MissingResourceException unused) {
            uResourceBundle = uResourceBundle2.get("001");
        }
        String[] stringArray = uResourceBundle.getStringArray();
        if (z) {
            return stringArray;
        }
        for (String str2 : stringArray) {
            arrayList.add(str2);
        }
        CalType[] values = CalType.values();
        for (CalType calType : values) {
            if (!arrayList.contains(calType.getId())) {
                arrayList.add(calType.getId());
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public final Date getTime() {
        return new Date(getTimeInMillis());
    }

    public final void setTime(Date date) {
        setTimeInMillis(date.getTime());
    }

    public long getTimeInMillis() {
        if (!this.isTimeSet) {
            updateTime();
        }
        return this.time;
    }

    public void setTimeInMillis(long j) {
        if (j > 183882168921600000L) {
            if (isLenient()) {
                j = 183882168921600000L;
            } else {
                throw new IllegalArgumentException("millis value greater than upper bounds for a Calendar : " + j);
            }
        } else if (j < -184303902528000000L) {
            if (isLenient()) {
                j = -184303902528000000L;
            } else {
                throw new IllegalArgumentException("millis value less than lower bounds for a Calendar : " + j);
            }
        }
        this.time = j;
        this.areAllFieldsSet = false;
        this.areFieldsSet = false;
        this.areFieldsVirtuallySet = true;
        this.isTimeSet = true;
        int i = 0;
        while (true) {
            int[] iArr = this.fields;
            if (i < iArr.length) {
                this.stamp[i] = 0;
                iArr[i] = 0;
                i++;
            } else {
                return;
            }
        }
    }

    public final int get(int i) {
        complete();
        return this.fields[i];
    }

    /* access modifiers changed from: protected */
    public final int internalGet(int i) {
        return this.fields[i];
    }

    /* access modifiers changed from: protected */
    public final int internalGet(int i, int i2) {
        return this.stamp[i] > 0 ? this.fields[i] : i2;
    }

    public final void set(int i, int i2) {
        if (this.areFieldsVirtuallySet) {
            computeFields();
        }
        this.fields[i] = i2;
        if (this.nextStamp == STAMP_MAX) {
            recalculateStamp();
        }
        int[] iArr = this.stamp;
        int i3 = this.nextStamp;
        this.nextStamp = i3 + 1;
        iArr[i] = i3;
        this.areFieldsVirtuallySet = false;
        this.areFieldsSet = false;
        this.isTimeSet = false;
    }

    public final void set(int i, int i2, int i3) {
        set(1, i);
        set(2, i2);
        set(5, i3);
    }

    public final void set(int i, int i2, int i3, int i4, int i5) {
        set(1, i);
        set(2, i2);
        set(5, i3);
        set(11, i4);
        set(12, i5);
    }

    public final void set(int i, int i2, int i3, int i4, int i5, int i6) {
        set(1, i);
        set(2, i2);
        set(5, i3);
        set(11, i4);
        set(12, i5);
        set(13, i6);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0010, code lost:
        if ((r2 % 67) >= 33) goto L_0x001f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x001d, code lost:
        if (((-r2) % 67) <= 33) goto L_0x001f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int gregoYearFromIslamicStart(int r5) {
        /*
            r0 = 0
            r1 = 33
            r2 = 1397(0x575, float:1.958E-42)
            r3 = 1
            if (r5 < r2) goto L_0x0013
            int r2 = r5 + -1397
            int r4 = r2 / 67
            int r2 = r2 % 67
            int r4 = r4 * 2
            if (r2 < r1) goto L_0x0020
            goto L_0x001f
        L_0x0013:
            int r2 = r5 + -1396
            int r4 = r2 / 67
            int r4 = r4 - r3
            int r2 = -r2
            int r2 = r2 % 67
            int r4 = r4 * 2
            if (r2 > r1) goto L_0x0020
        L_0x001f:
            r0 = r3
        L_0x0020:
            int r4 = r4 + r0
            int r5 = r5 + 579
            int r5 = r5 - r4
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.Calendar.gregoYearFromIslamicStart(int):int");
    }

    @Deprecated
    public final int getRelatedYear() {
        int i = get(19);
        CalType calType = CalType.GREGORIAN;
        String type = getType();
        CalType[] values = CalType.values();
        int length = values.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            CalType calType2 = values[i2];
            if (type.equals(calType2.getId())) {
                calType = calType2;
                break;
            }
            i2++;
        }
        switch (calType) {
            case CHINESE:
                return i - 2637;
            case COPTIC:
                return i + 284;
            case DANGI:
                return i - 2333;
            case ETHIOPIC:
                return i + 8;
            case ETHIOPIC_AMETE_ALEM:
                return i - 5492;
            case HEBREW:
                return i - 3760;
            case INDIAN:
                return i + 79;
            case ISLAMIC_CIVIL:
            case ISLAMIC_UMALQURA:
            case ISLAMIC_TBLA:
            case ISLAMIC_RGSA:
            case ISLAMIC:
                return gregoYearFromIslamicStart(i);
            case JAPANESE:
            default:
                return i;
            case PERSIAN:
                return i + 622;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0010, code lost:
        if ((r2 % 65) >= 32) goto L_0x001f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x001d, code lost:
        if (((-r2) % 65) <= 32) goto L_0x001f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int firstIslamicStartYearFromGrego(int r5) {
        /*
            r0 = 0
            r1 = 32
            r2 = 1977(0x7b9, float:2.77E-42)
            r3 = 1
            if (r5 < r2) goto L_0x0013
            int r2 = r5 + -1977
            int r4 = r2 / 65
            int r2 = r2 % 65
            int r4 = r4 * 2
            if (r2 < r1) goto L_0x0020
            goto L_0x001f
        L_0x0013:
            int r2 = r5 + -1976
            int r4 = r2 / 65
            int r4 = r4 - r3
            int r2 = -r2
            int r2 = r2 % 65
            int r4 = r4 * 2
            if (r2 > r1) goto L_0x0020
        L_0x001f:
            r0 = r3
        L_0x0020:
            int r4 = r4 + r0
            int r5 = r5 + -579
            int r5 = r5 + r4
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.Calendar.firstIslamicStartYearFromGrego(int):int");
    }

    @Deprecated
    public final void setRelatedYear(int i) {
        CalType calType = CalType.GREGORIAN;
        String type = getType();
        CalType[] values = CalType.values();
        int length = values.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            CalType calType2 = values[i2];
            if (type.equals(calType2.getId())) {
                calType = calType2;
                break;
            }
            i2++;
        }
        switch (calType) {
            case CHINESE:
                i += 2637;
                break;
            case COPTIC:
                i -= 284;
                break;
            case DANGI:
                i += 2333;
                break;
            case ETHIOPIC:
                i -= 8;
                break;
            case ETHIOPIC_AMETE_ALEM:
                i += 5492;
                break;
            case HEBREW:
                i += 3760;
                break;
            case INDIAN:
                i -= 79;
                break;
            case ISLAMIC_CIVIL:
            case ISLAMIC_UMALQURA:
            case ISLAMIC_TBLA:
            case ISLAMIC_RGSA:
            case ISLAMIC:
                i = firstIslamicStartYearFromGrego(i);
                break;
            case PERSIAN:
                i -= 622;
                break;
        }
        set(19, i);
    }

    public final void clear() {
        int i = 0;
        while (true) {
            int[] iArr = this.fields;
            if (i < iArr.length) {
                this.stamp[i] = 0;
                iArr[i] = 0;
                i++;
            } else {
                this.areFieldsVirtuallySet = false;
                this.areAllFieldsSet = false;
                this.areFieldsSet = false;
                this.isTimeSet = false;
                return;
            }
        }
    }

    public final void clear(int i) {
        if (this.areFieldsVirtuallySet) {
            computeFields();
        }
        this.fields[i] = 0;
        this.stamp[i] = 0;
        this.areFieldsVirtuallySet = false;
        this.areAllFieldsSet = false;
        this.areFieldsSet = false;
        this.isTimeSet = false;
    }

    public final boolean isSet(int i) {
        return this.areFieldsVirtuallySet || this.stamp[i] != 0;
    }

    /* access modifiers changed from: protected */
    public void complete() {
        if (!this.isTimeSet) {
            updateTime();
        }
        if (!this.areFieldsSet) {
            computeFields();
            this.areFieldsSet = true;
            this.areAllFieldsSet = true;
        }
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Calendar calendar = (Calendar) obj;
        return isEquivalentTo(calendar) && getTimeInMillis() == calendar.getTime().getTime();
    }

    public boolean isEquivalentTo(Calendar calendar) {
        return getClass() == calendar.getClass() && isLenient() == calendar.isLenient() && getFirstDayOfWeek() == calendar.getFirstDayOfWeek() && getMinimalDaysInFirstWeek() == calendar.getMinimalDaysInFirstWeek() && getTimeZone().equals(calendar.getTimeZone()) && getRepeatedWallTimeOption() == calendar.getRepeatedWallTimeOption() && getSkippedWallTimeOption() == calendar.getSkippedWallTimeOption();
    }

    public int hashCode() {
        int i = this.lenient ? 1 : 0;
        return (this.zone.hashCode() << 11) | i | (this.firstDayOfWeek << 1) | (this.minimalDaysInFirstWeek << 4) | (this.repeatedWallTime << 7) | (this.skippedWallTime << 9);
    }

    private long compare(Object obj) {
        long j;
        if (obj instanceof Calendar) {
            j = ((Calendar) obj).getTimeInMillis();
        } else if (obj instanceof Date) {
            j = ((Date) obj).getTime();
        } else {
            throw new IllegalArgumentException(obj + "is not a Calendar or Date");
        }
        return getTimeInMillis() - j;
    }

    public boolean before(Object obj) {
        return compare(obj) < 0;
    }

    public boolean after(Object obj) {
        return compare(obj) > 0;
    }

    public int getActualMaximum(int i) {
        if (!(i == 0 || i == 18)) {
            if (i == 5) {
                Calendar calendar = (Calendar) clone();
                calendar.setLenient(true);
                calendar.prepareGetActual(i, false);
                return handleGetMonthLength(calendar.get(19), calendar.get(2));
            } else if (i == 6) {
                Calendar calendar2 = (Calendar) clone();
                calendar2.setLenient(true);
                calendar2.prepareGetActual(i, false);
                return handleGetYearLength(calendar2.get(19));
            } else if (!(i == 7 || i == 20 || i == 21)) {
                switch (i) {
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                        break;
                    default:
                        return getActualHelper(i, getLeastMaximum(i), getMaximum(i));
                }
            }
        }
        return getMaximum(i);
    }

    public int getActualMinimum(int i) {
        switch (i) {
            case 7:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            case 20:
            case 21:
                return getMinimum(i);
            case 8:
            case 17:
            case 19:
            default:
                return getActualHelper(i, getGreatestMinimum(i), getMinimum(i));
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001e, code lost:
        if (r6 != 19) goto L_0x0053;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void prepareGetActual(int r6, boolean r7) {
        /*
            r5 = this;
            r0 = 21
            r1 = 0
            r5.set(r0, r1)
            r0 = 1
            if (r6 == r0) goto L_0x004b
            r1 = 2
            r2 = 5
            if (r6 == r1) goto L_0x0043
            r1 = 3
            r3 = 7
            if (r6 == r1) goto L_0x0034
            r4 = 4
            if (r6 == r4) goto L_0x0034
            r7 = 8
            if (r6 == r7) goto L_0x0029
            r7 = 17
            if (r6 == r7) goto L_0x0021
            r7 = 19
            if (r6 == r7) goto L_0x004b
            goto L_0x0053
        L_0x0021:
            int r7 = r5.getGreatestMinimum(r1)
            r5.set(r1, r7)
            goto L_0x0053
        L_0x0029:
            r5.set(r2, r0)
            int r7 = r5.get(r3)
            r5.set(r3, r7)
            goto L_0x0053
        L_0x0034:
            int r1 = r5.firstDayOfWeek
            if (r7 == 0) goto L_0x003f
            int r1 = r1 + 6
            int r1 = r1 % r3
            if (r1 >= r0) goto L_0x003f
            int r1 = r1 + 7
        L_0x003f:
            r5.set(r3, r1)
            goto L_0x0053
        L_0x0043:
            int r7 = r5.getGreatestMinimum(r2)
            r5.set(r2, r7)
            goto L_0x0053
        L_0x004b:
            r7 = 6
            int r0 = r5.getGreatestMinimum(r7)
            r5.set(r7, r0)
        L_0x0053:
            int r7 = r5.getGreatestMinimum(r6)
            r5.set(r6, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.Calendar.prepareGetActual(int, boolean):void");
    }

    private int getActualHelper(int i, int i2, int i3) {
        int i4;
        if (i2 == i3) {
            return i2;
        }
        boolean z = true;
        int i5 = i3 > i2 ? 1 : -1;
        Calendar calendar = (Calendar) clone();
        calendar.complete();
        calendar.setLenient(true);
        if (i5 >= 0) {
            z = false;
        }
        calendar.prepareGetActual(i, z);
        calendar.set(i, i2);
        if (calendar.get(i) != i2 && i != 4 && i5 > 0) {
            return i2;
        }
        do {
            i4 = i2 + i5;
            calendar.add(i, i5);
            if (calendar.get(i) != i4) {
                break;
            }
            i2 = i4;
        } while (i4 != i3);
        return i2;
    }

    public final void roll(int i, boolean z) {
        roll(i, z ? 1 : -1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:101:0x01ef, code lost:
        if (r13 < 1) goto L_0x01f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x01d3, code lost:
        if (r0 != false) goto L_0x01f1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void roll(int r12, int r13) {
        /*
        // Method dump skipped, instructions count: 580
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.Calendar.roll(int, int):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0109  */
    /* JADX WARNING: Removed duplicated region for block: B:61:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:71:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void add(int r13, int r14) {
        /*
        // Method dump skipped, instructions count: 332
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.Calendar.add(int, int):void");
    }

    public String getDisplayName(Locale locale) {
        return getClass().getName();
    }

    public String getDisplayName(ULocale uLocale) {
        return getClass().getName();
    }

    public int compareTo(Calendar calendar) {
        int i = ((getTimeInMillis() - calendar.getTimeInMillis()) > 0 ? 1 : ((getTimeInMillis() - calendar.getTimeInMillis()) == 0 ? 0 : -1));
        if (i < 0) {
            return -1;
        }
        return i > 0 ? 1 : 0;
    }

    public DateFormat getDateTimeFormat(int i, int i2, Locale locale) {
        return formatHelper(this, ULocale.forLocale(locale), i, i2);
    }

    public DateFormat getDateTimeFormat(int i, int i2, ULocale uLocale) {
        return formatHelper(this, uLocale, i, i2);
    }

    /* access modifiers changed from: protected */
    public DateFormat handleGetDateFormat(String str, Locale locale) {
        return handleGetDateFormat(str, (String) null, ULocale.forLocale(locale));
    }

    /* access modifiers changed from: protected */
    public DateFormat handleGetDateFormat(String str, String str2, Locale locale) {
        return handleGetDateFormat(str, str2, ULocale.forLocale(locale));
    }

    /* access modifiers changed from: protected */
    public DateFormat handleGetDateFormat(String str, ULocale uLocale) {
        return handleGetDateFormat(str, (String) null, uLocale);
    }

    /* access modifiers changed from: protected */
    public DateFormat handleGetDateFormat(String str, String str2, ULocale uLocale) {
        FormatConfiguration formatConfiguration = new FormatConfiguration();
        formatConfiguration.pattern = str;
        formatConfiguration.override = str2;
        formatConfiguration.formatData = new DateFormatSymbols(this, uLocale);
        formatConfiguration.loc = uLocale;
        formatConfiguration.cal = this;
        return SimpleDateFormat.getInstance(formatConfiguration);
    }

    private static DateFormat formatHelper(Calendar calendar, ULocale uLocale, int i, int i2) {
        String str;
        if (i2 < -1 || i2 > 3) {
            throw new IllegalArgumentException("Illegal time style " + i2);
        } else if (i < -1 || i > 3) {
            throw new IllegalArgumentException("Illegal date style " + i);
        } else {
            PatternData make = PatternData.make(calendar, uLocale);
            String str2 = null;
            if (i2 >= 0 && i >= 0) {
                String dateTimePattern = make.getDateTimePattern(i);
                int i3 = i + 4;
                str = SimpleFormatterImpl.formatRawPattern(dateTimePattern, 2, 2, make.patterns[i2], make.patterns[i3]);
                if (make.overrides != null) {
                    str2 = mergeOverrideStrings(make.patterns[i3], make.patterns[i2], make.overrides[i3], make.overrides[i2]);
                }
            } else if (i2 >= 0) {
                str = make.patterns[i2];
                if (make.overrides != null) {
                    str2 = make.overrides[i2];
                }
            } else if (i >= 0) {
                int i4 = i + 4;
                str = make.patterns[i4];
                if (make.overrides != null) {
                    str2 = make.overrides[i4];
                }
            } else {
                throw new IllegalArgumentException("No date or time style specified");
            }
            DateFormat handleGetDateFormat = calendar.handleGetDateFormat(str, str2, uLocale);
            handleGetDateFormat.setCalendar(calendar);
            return handleGetDateFormat;
        }
    }

    /* access modifiers changed from: package-private */
    public static class PatternData {
        private String[] overrides;
        private String[] patterns;

        public PatternData(String[] strArr, String[] strArr2) {
            this.patterns = strArr;
            this.overrides = strArr2;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private String getDateTimePattern(int i) {
            int i2 = 8;
            if (this.patterns.length >= 13) {
                i2 = 8 + i + 1;
            }
            return this.patterns[i2];
        }

        /* access modifiers changed from: private */
        public static PatternData make(Calendar calendar, ULocale uLocale) {
            PatternData patternData;
            String type = calendar.getType();
            String str = uLocale.getBaseName() + "+" + type;
            PatternData patternData2 = (PatternData) Calendar.PATTERN_CACHE.get(str);
            if (patternData2 == null) {
                try {
                    patternData = Calendar.getPatternData(uLocale, type);
                } catch (MissingResourceException unused) {
                    patternData = new PatternData(Calendar.DEFAULT_PATTERNS, null);
                }
                patternData2 = patternData;
                Calendar.PATTERN_CACHE.put(str, patternData2);
            }
            return patternData2;
        }
    }

    /* access modifiers changed from: private */
    public static PatternData getPatternData(ULocale uLocale, String str) {
        ICUResourceBundle iCUResourceBundle = (ICUResourceBundle) UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, uLocale);
        ICUResourceBundle findWithFallback = iCUResourceBundle.findWithFallback("calendar/" + str + "/DateTimePatterns");
        if (findWithFallback == null) {
            findWithFallback = iCUResourceBundle.getWithFallback("calendar/gregorian/DateTimePatterns");
        }
        int size = findWithFallback.getSize();
        String[] strArr = new String[size];
        String[] strArr2 = new String[size];
        for (int i = 0; i < size; i++) {
            ICUResourceBundle iCUResourceBundle2 = (ICUResourceBundle) findWithFallback.get(i);
            int type = iCUResourceBundle2.getType();
            if (type == 0) {
                strArr[i] = iCUResourceBundle2.getString();
            } else if (type == 8) {
                strArr[i] = iCUResourceBundle2.getString(0);
                strArr2[i] = iCUResourceBundle2.getString(1);
            }
        }
        return new PatternData(strArr, strArr2);
    }

    @Deprecated
    public static String getDateTimePattern(Calendar calendar, ULocale uLocale, int i) {
        return PatternData.make(calendar, uLocale).getDateTimePattern(i);
    }

    private static String mergeOverrideStrings(String str, String str2, String str3, String str4) {
        if (str3 == null && str4 == null) {
            return null;
        }
        if (str3 == null) {
            return expandOverride(str2, str4);
        }
        if (str4 == null) {
            return expandOverride(str, str3);
        }
        if (str3.equals(str4)) {
            return str3;
        }
        return expandOverride(str, str3) + ";" + expandOverride(str2, str4);
    }

    private static String expandOverride(String str, String str2) {
        if (str2.indexOf(61) >= 0) {
            return str2;
        }
        boolean z = false;
        char c = ' ';
        StringBuilder sb = new StringBuilder();
        StringCharacterIterator stringCharacterIterator = new StringCharacterIterator(str);
        char first = stringCharacterIterator.first();
        while (true) {
            c = first;
            if (c == 65535) {
                return sb.toString();
            }
            if (c == '\'') {
                z = !z;
            } else if (!z && c != c) {
                if (sb.length() > 0) {
                    sb.append(";");
                }
                sb.append(c);
                sb.append("=");
                sb.append(str2);
            }
            first = stringCharacterIterator.next();
        }
    }

    @Deprecated
    public static class FormatConfiguration {
        private Calendar cal;
        private DateFormatSymbols formatData;
        private ULocale loc;
        private String override;
        private String pattern;

        private FormatConfiguration() {
        }

        @Deprecated
        public String getPatternString() {
            return this.pattern;
        }

        @Deprecated
        public String getOverrideString() {
            return this.override;
        }

        @Deprecated
        public Calendar getCalendar() {
            return this.cal;
        }

        @Deprecated
        public ULocale getLocale() {
            return this.loc;
        }

        @Deprecated
        public DateFormatSymbols getDateFormatSymbols() {
            return this.formatData;
        }
    }

    /* access modifiers changed from: protected */
    public void pinField(int i) {
        int actualMaximum = getActualMaximum(i);
        int actualMinimum = getActualMinimum(i);
        int[] iArr = this.fields;
        if (iArr[i] > actualMaximum) {
            set(i, actualMaximum);
        } else if (iArr[i] < actualMinimum) {
            set(i, actualMinimum);
        }
    }

    /* access modifiers changed from: protected */
    public int weekNumber(int i, int i2, int i3) {
        int firstDayOfWeek2 = (((i3 - getFirstDayOfWeek()) - i2) + 1) % 7;
        if (firstDayOfWeek2 < 0) {
            firstDayOfWeek2 += 7;
        }
        int i4 = ((i + firstDayOfWeek2) - 1) / 7;
        return 7 - firstDayOfWeek2 >= getMinimalDaysInFirstWeek() ? i4 + 1 : i4;
    }

    /* access modifiers changed from: protected */
    public final int weekNumber(int i, int i2) {
        return weekNumber(i, i, i2);
    }

    public int fieldDifference(Date date, int i) {
        long timeInMillis = getTimeInMillis();
        long time2 = date.getTime();
        int i2 = (timeInMillis > time2 ? 1 : (timeInMillis == time2 ? 0 : -1));
        int i3 = 0;
        if (i2 < 0) {
            int i4 = 0;
            int i5 = 1;
            while (true) {
                setTimeInMillis(timeInMillis);
                add(i, i5);
                int i6 = (getTimeInMillis() > time2 ? 1 : (getTimeInMillis() == time2 ? 0 : -1));
                if (i6 == 0) {
                    return i5;
                }
                if (i6 > 0) {
                    while (true) {
                        int i7 = i5 - i4;
                        if (i7 <= 1) {
                            i3 = i4;
                            break;
                        }
                        int i8 = (i7 / 2) + i4;
                        setTimeInMillis(timeInMillis);
                        add(i, i8);
                        int i9 = (getTimeInMillis() > time2 ? 1 : (getTimeInMillis() == time2 ? 0 : -1));
                        if (i9 == 0) {
                            return i8;
                        }
                        if (i9 > 0) {
                            i5 = i8;
                        } else {
                            i4 = i8;
                        }
                    }
                } else {
                    int i10 = Integer.MAX_VALUE;
                    if (i5 < Integer.MAX_VALUE) {
                        int i11 = i5 << 1;
                        if (i11 >= 0) {
                            i10 = i11;
                        }
                        i5 = i10;
                        i4 = i5;
                    } else {
                        throw new RuntimeException();
                    }
                }
            }
        } else if (i2 > 0) {
            int i12 = -1;
            do {
                i3 = i12;
                setTimeInMillis(timeInMillis);
                add(i, i3);
                int i13 = (getTimeInMillis() > time2 ? 1 : (getTimeInMillis() == time2 ? 0 : -1));
                if (i13 == 0) {
                    return i3;
                }
                if (i13 < 0) {
                    i3 = i3;
                    int i14 = i3;
                    while (i3 - i14 > 1) {
                        int i15 = ((i14 - i3) / 2) + i3;
                        setTimeInMillis(timeInMillis);
                        add(i, i15);
                        int i16 = (getTimeInMillis() > time2 ? 1 : (getTimeInMillis() == time2 ? 0 : -1));
                        if (i16 == 0) {
                            return i15;
                        }
                        if (i16 < 0) {
                            i14 = i15;
                        } else {
                            i3 = i15;
                        }
                    }
                } else {
                    i12 = i3 << 1;
                }
            } while (i12 != 0);
            throw new RuntimeException();
        }
        setTimeInMillis(timeInMillis);
        add(i, i3);
        return i3;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.zone = timeZone;
        this.areFieldsSet = false;
    }

    public TimeZone getTimeZone() {
        return this.zone;
    }

    public void setLenient(boolean z) {
        this.lenient = z;
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public void setRepeatedWallTimeOption(int i) {
        if (i == 0 || i == 1) {
            this.repeatedWallTime = i;
            return;
        }
        throw new IllegalArgumentException("Illegal repeated wall time option - " + i);
    }

    public int getRepeatedWallTimeOption() {
        return this.repeatedWallTime;
    }

    public void setSkippedWallTimeOption(int i) {
        if (i == 0 || i == 1 || i == 2) {
            this.skippedWallTime = i;
            return;
        }
        throw new IllegalArgumentException("Illegal skipped wall time option - " + i);
    }

    public int getSkippedWallTimeOption() {
        return this.skippedWallTime;
    }

    public void setFirstDayOfWeek(int i) {
        if (this.firstDayOfWeek == i) {
            return;
        }
        if (i < 1 || i > 7) {
            throw new IllegalArgumentException("Invalid day of week");
        }
        this.firstDayOfWeek = i;
        this.areFieldsSet = false;
    }

    public int getFirstDayOfWeek() {
        return this.firstDayOfWeek;
    }

    public void setMinimalDaysInFirstWeek(int i) {
        if (i < 1) {
            i = 1;
        } else if (i > 7) {
            i = 7;
        }
        if (this.minimalDaysInFirstWeek != i) {
            this.minimalDaysInFirstWeek = i;
            this.areFieldsSet = false;
        }
    }

    public int getMinimalDaysInFirstWeek() {
        return this.minimalDaysInFirstWeek;
    }

    /* access modifiers changed from: protected */
    public int getLimit(int i, int i2) {
        switch (i) {
            case 4:
                if (i2 == 0) {
                    if (getMinimalDaysInFirstWeek() == 1) {
                        return 1;
                    }
                    return 0;
                } else if (i2 == 1) {
                    return 1;
                } else {
                    int minimalDaysInFirstWeek2 = getMinimalDaysInFirstWeek();
                    int handleGetLimit = handleGetLimit(5, i2);
                    if (i2 == 2) {
                        return (handleGetLimit + (7 - minimalDaysInFirstWeek2)) / 7;
                    }
                    return ((handleGetLimit + 6) + (7 - minimalDaysInFirstWeek2)) / 7;
                }
            case 5:
            case 6:
            case 8:
            case 17:
            case 19:
            default:
                return handleGetLimit(i, i2);
            case 7:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            case 20:
            case 21:
            case 22:
                return LIMITS[i][i2];
        }
    }

    public final int getMinimum(int i) {
        return getLimit(i, 0);
    }

    public final int getMaximum(int i) {
        return getLimit(i, 3);
    }

    public final int getGreatestMinimum(int i) {
        return getLimit(i, 1);
    }

    public final int getLeastMaximum(int i) {
        return getLimit(i, 2);
    }

    @Deprecated
    public int getDayOfWeekType(int i) {
        if (i < 1 || i > 7) {
            throw new IllegalArgumentException("Invalid day of week");
        }
        int i2 = this.weekendOnset;
        int i3 = this.weekendCease;
        if (i2 != i3) {
            if (i2 < i3) {
                if (i < i2 || i > i3) {
                    return 0;
                }
            } else if (i > i3 && i < i2) {
                return 0;
            }
            if (i == this.weekendOnset) {
                return this.weekendOnsetMillis == 0 ? 1 : 2;
            }
            if (i != this.weekendCease || this.weekendCeaseMillis >= 86400000) {
                return 1;
            }
            return 3;
        } else if (i != i2) {
            return 0;
        } else {
            return this.weekendOnsetMillis == 0 ? 1 : 2;
        }
    }

    @Deprecated
    public int getWeekendTransition(int i) {
        if (i == this.weekendOnset) {
            return this.weekendOnsetMillis;
        }
        if (i == this.weekendCease) {
            return this.weekendCeaseMillis;
        }
        throw new IllegalArgumentException("Not weekend transition day");
    }

    public boolean isWeekend(Date date) {
        setTime(date);
        return isWeekend();
    }

    public boolean isWeekend() {
        int i = get(7);
        int dayOfWeekType = getDayOfWeekType(i);
        if (dayOfWeekType == 0) {
            return false;
        }
        if (dayOfWeekType == 1) {
            return true;
        }
        int internalGet = internalGet(14) + ((internalGet(13) + ((internalGet(12) + (internalGet(11) * 60)) * 60)) * 1000);
        int weekendTransition = getWeekendTransition(i);
        if (dayOfWeekType == 2) {
            if (internalGet < weekendTransition) {
                return false;
            }
        } else if (internalGet >= weekendTransition) {
            return false;
        }
        return true;
    }

    @Override // java.lang.Object
    public Object clone() {
        try {
            Calendar calendar = (Calendar) super.clone();
            calendar.fields = new int[this.fields.length];
            calendar.stamp = new int[this.fields.length];
            System.arraycopy(this.fields, 0, calendar.fields, 0, this.fields.length);
            System.arraycopy(this.stamp, 0, calendar.stamp, 0, this.fields.length);
            calendar.zone = (TimeZone) this.zone.clone();
            return calendar;
        } catch (CloneNotSupportedException e) {
            throw new ICUCloneNotSupportedException(e);
        }
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append("[time=");
        sb.append(this.isTimeSet ? String.valueOf(this.time) : "?");
        sb.append(",areFieldsSet=");
        sb.append(this.areFieldsSet);
        sb.append(",areAllFieldsSet=");
        sb.append(this.areAllFieldsSet);
        sb.append(",lenient=");
        sb.append(this.lenient);
        sb.append(",zone=");
        sb.append(this.zone);
        sb.append(",firstDayOfWeek=");
        sb.append(this.firstDayOfWeek);
        sb.append(",minimalDaysInFirstWeek=");
        sb.append(this.minimalDaysInFirstWeek);
        sb.append(",repeatedWallTime=");
        sb.append(this.repeatedWallTime);
        sb.append(",skippedWallTime=");
        sb.append(this.skippedWallTime);
        for (int i = 0; i < this.fields.length; i++) {
            sb.append(TelephoneNumberUtils.PAUSE);
            sb.append(fieldName(i));
            sb.append('=');
            if (isSet(i)) {
                str = String.valueOf(this.fields[i]);
            } else {
                str = "?";
            }
            sb.append(str);
        }
        sb.append(']');
        return sb.toString();
    }

    public static final class WeekData {
        public final int firstDayOfWeek;
        public final int minimalDaysInFirstWeek;
        public final int weekendCease;
        public final int weekendCeaseMillis;
        public final int weekendOnset;
        public final int weekendOnsetMillis;

        public WeekData(int i, int i2, int i3, int i4, int i5, int i6) {
            this.firstDayOfWeek = i;
            this.minimalDaysInFirstWeek = i2;
            this.weekendOnset = i3;
            this.weekendOnsetMillis = i4;
            this.weekendCease = i5;
            this.weekendCeaseMillis = i6;
        }

        public int hashCode() {
            return (((((((((this.firstDayOfWeek * 37) + this.minimalDaysInFirstWeek) * 37) + this.weekendOnset) * 37) + this.weekendOnsetMillis) * 37) + this.weekendCease) * 37) + this.weekendCeaseMillis;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof WeekData)) {
                return false;
            }
            WeekData weekData = (WeekData) obj;
            return this.firstDayOfWeek == weekData.firstDayOfWeek && this.minimalDaysInFirstWeek == weekData.minimalDaysInFirstWeek && this.weekendOnset == weekData.weekendOnset && this.weekendOnsetMillis == weekData.weekendOnsetMillis && this.weekendCease == weekData.weekendCease && this.weekendCeaseMillis == weekData.weekendCeaseMillis;
        }

        public String toString() {
            return "{" + this.firstDayOfWeek + ", " + this.minimalDaysInFirstWeek + ", " + this.weekendOnset + ", " + this.weekendOnsetMillis + ", " + this.weekendCease + ", " + this.weekendCeaseMillis + "}";
        }
    }

    public static WeekData getWeekDataForRegion(String str) {
        return WEEK_DATA_CACHE.createInstance(str, str);
    }

    public WeekData getWeekData() {
        return new WeekData(this.firstDayOfWeek, this.minimalDaysInFirstWeek, this.weekendOnset, this.weekendOnsetMillis, this.weekendCease, this.weekendCeaseMillis);
    }

    public Calendar setWeekData(WeekData weekData) {
        setFirstDayOfWeek(weekData.firstDayOfWeek);
        setMinimalDaysInFirstWeek(weekData.minimalDaysInFirstWeek);
        this.weekendOnset = weekData.weekendOnset;
        this.weekendOnsetMillis = weekData.weekendOnsetMillis;
        this.weekendCease = weekData.weekendCease;
        this.weekendCeaseMillis = weekData.weekendCeaseMillis;
        return this;
    }

    /* access modifiers changed from: private */
    public static WeekData getWeekDataForRegionInternal(String str) {
        UResourceBundle uResourceBundle;
        if (str == null) {
            str = "001";
        }
        UResourceBundle uResourceBundle2 = UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER).get("weekData");
        try {
            uResourceBundle = uResourceBundle2.get(str);
        } catch (MissingResourceException e) {
            if (!str.equals("001")) {
                uResourceBundle = uResourceBundle2.get("001");
            } else {
                throw e;
            }
        }
        int[] intVector = uResourceBundle.getIntVector();
        return new WeekData(intVector[0], intVector[1], intVector[2], intVector[3], intVector[4], intVector[5]);
    }

    /* access modifiers changed from: private */
    public static class WeekDataCache extends SoftCache<String, WeekData, String> {
        private WeekDataCache() {
        }

        /* access modifiers changed from: protected */
        public WeekData createInstance(String str, String str2) {
            return Calendar.getWeekDataForRegionInternal(str);
        }
    }

    private void setWeekData(String str) {
        if (str == null) {
            str = "001";
        }
        setWeekData((WeekData) WEEK_DATA_CACHE.getInstance(str, str));
    }

    private void updateTime() {
        computeTime();
        if (isLenient() || !this.areAllFieldsSet) {
            this.areFieldsSet = false;
        }
        this.isTimeSet = true;
        this.areFieldsVirtuallySet = false;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (!this.isTimeSet) {
            try {
                updateTime();
            } catch (IllegalArgumentException unused) {
            }
        }
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        initInternal();
        this.isTimeSet = true;
        this.areAllFieldsSet = false;
        this.areFieldsSet = false;
        this.areFieldsVirtuallySet = true;
        this.nextStamp = 2;
    }

    /* access modifiers changed from: protected */
    public void computeFields() {
        int[] iArr = new int[2];
        getTimeZone().getOffset(this.time, false, iArr);
        long j = this.time + ((long) iArr[0]) + ((long) iArr[1]);
        int i = this.internalSetMask;
        for (int i2 = 0; i2 < this.fields.length; i2++) {
            if ((i & 1) == 0) {
                this.stamp[i2] = 1;
            } else {
                this.stamp[i2] = 0;
            }
            i >>= 1;
        }
        long floorDivide = floorDivide(j, 86400000);
        int[] iArr2 = this.fields;
        iArr2[20] = ((int) floorDivide) + EPOCH_JULIAN_DAY;
        computeGregorianAndDOWFields(iArr2[20]);
        handleComputeFields(this.fields[20]);
        computeWeekFields();
        int i3 = (int) (j - (floorDivide * 86400000));
        int[] iArr3 = this.fields;
        iArr3[21] = i3;
        iArr3[14] = i3 % 1000;
        int i4 = i3 / 1000;
        iArr3[13] = i4 % 60;
        int i5 = i4 / 60;
        iArr3[12] = i5 % 60;
        int i6 = i5 / 60;
        iArr3[11] = i6;
        iArr3[9] = i6 / 12;
        iArr3[10] = i6 % 12;
        iArr3[15] = iArr[0];
        iArr3[16] = iArr[1];
    }

    private final void computeGregorianAndDOWFields(int i) {
        computeGregorianFields(i);
        int[] iArr = this.fields;
        int julianDayToDayOfWeek = julianDayToDayOfWeek(i);
        iArr[7] = julianDayToDayOfWeek;
        int firstDayOfWeek2 = (julianDayToDayOfWeek - getFirstDayOfWeek()) + 1;
        if (firstDayOfWeek2 < 1) {
            firstDayOfWeek2 += 7;
        }
        this.fields[18] = firstDayOfWeek2;
    }

    /* access modifiers changed from: protected */
    public final void computeGregorianFields(int i) {
        int[] iArr = new int[1];
        int floorDivide = floorDivide((long) (i - JAN_1_1_JULIAN_DAY), 146097, iArr);
        int i2 = 0;
        int floorDivide2 = floorDivide(iArr[0], 36524, iArr);
        int floorDivide3 = floorDivide(iArr[0], 1461, iArr);
        int floorDivide4 = floorDivide(iArr[0], (int) JLogConstants.JLID_APP_FRONZED_BEGIN, iArr);
        int i3 = (floorDivide * 400) + (floorDivide2 * 100) + (floorDivide3 * 4) + floorDivide4;
        int i4 = iArr[0];
        if (floorDivide2 == 4 || floorDivide4 == 4) {
            i4 = 365;
        } else {
            i3++;
        }
        boolean z = (i3 & 3) == 0 && (i3 % 100 != 0 || i3 % 400 == 0);
        char c = 2;
        if (i4 >= (z ? 60 : 59)) {
            i2 = z ? 1 : 2;
        }
        int i5 = (((i2 + i4) * 12) + 6) / JLogConstants.JLID_APP_UNFRONZED_BEGIN;
        int[] iArr2 = GREGORIAN_MONTH_COUNT[i5];
        if (z) {
            c = 3;
        }
        this.gregorianYear = i3;
        this.gregorianMonth = i5;
        this.gregorianDayOfMonth = (i4 - iArr2[c]) + 1;
        this.gregorianDayOfYear = i4 + 1;
    }

    private final void computeWeekFields() {
        int[] iArr = this.fields;
        int i = iArr[19];
        int i2 = iArr[7];
        int i3 = iArr[6];
        int firstDayOfWeek2 = ((i2 + 7) - getFirstDayOfWeek()) % 7;
        int firstDayOfWeek3 = (((i2 - i3) + 7001) - getFirstDayOfWeek()) % 7;
        int i4 = ((i3 - 1) + firstDayOfWeek3) / 7;
        if (7 - firstDayOfWeek3 >= getMinimalDaysInFirstWeek()) {
            i4++;
        }
        if (i4 == 0) {
            i4 = weekNumber(i3 + handleGetYearLength(i - 1), i2);
            i--;
        } else {
            int handleGetYearLength = handleGetYearLength(i);
            if (i3 >= handleGetYearLength - 5) {
                int i5 = ((firstDayOfWeek2 + handleGetYearLength) - i3) % 7;
                if (i5 < 0) {
                    i5 += 7;
                }
                if (6 - i5 >= getMinimalDaysInFirstWeek() && (i3 + 7) - firstDayOfWeek2 > handleGetYearLength) {
                    i++;
                    i4 = 1;
                }
            }
        }
        int[] iArr2 = this.fields;
        iArr2[3] = i4;
        iArr2[17] = i;
        int i6 = iArr2[5];
        iArr2[4] = weekNumber(i6, i2);
        this.fields[8] = ((i6 - 1) / 7) + 1;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0042, code lost:
        if (r8[4] < r8[r7]) goto L_0x0044;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int resolveFields(int[][][] r13) {
        /*
            r12 = this;
            r0 = 0
            r1 = -1
            r2 = r1
            r1 = r0
        L_0x0004:
            int r3 = r13.length
            r4 = 32
            if (r1 >= r3) goto L_0x004f
            if (r2 >= 0) goto L_0x004f
            r3 = r13[r1]
            r6 = r0
            r5 = r2
            r2 = r6
        L_0x0010:
            int r7 = r3.length
            if (r2 >= r7) goto L_0x004b
            r7 = r3[r2]
            r8 = r7[r0]
            if (r8 < r4) goto L_0x001b
            r8 = 1
            goto L_0x001c
        L_0x001b:
            r8 = r0
        L_0x001c:
            r9 = r0
        L_0x001d:
            int r10 = r7.length
            if (r8 >= r10) goto L_0x0030
            int[] r10 = r12.stamp
            r11 = r7[r8]
            r10 = r10[r11]
            if (r10 != 0) goto L_0x0029
            goto L_0x0048
        L_0x0029:
            int r9 = java.lang.Math.max(r9, r10)
            int r8 = r8 + 1
            goto L_0x001d
        L_0x0030:
            if (r9 <= r6) goto L_0x0048
            r7 = r7[r0]
            if (r7 < r4) goto L_0x0044
            r7 = r7 & 31
            r8 = 5
            if (r7 != r8) goto L_0x0044
            int[] r8 = r12.stamp
            r10 = 4
            r10 = r8[r10]
            r8 = r8[r7]
            if (r10 >= r8) goto L_0x0045
        L_0x0044:
            r5 = r7
        L_0x0045:
            if (r5 != r7) goto L_0x0048
            r6 = r9
        L_0x0048:
            int r2 = r2 + 1
            goto L_0x0010
        L_0x004b:
            int r1 = r1 + 1
            r2 = r5
            goto L_0x0004
        L_0x004f:
            if (r2 < r4) goto L_0x0053
            r2 = r2 & 31
        L_0x0053:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.Calendar.resolveFields(int[][][]):int");
    }

    /* access modifiers changed from: protected */
    public int newestStamp(int i, int i2, int i3) {
        while (i <= i2) {
            int[] iArr = this.stamp;
            if (iArr[i] > i3) {
                i3 = iArr[i];
            }
            i++;
        }
        return i3;
    }

    /* access modifiers changed from: protected */
    public final int getStamp(int i) {
        return this.stamp[i];
    }

    /* access modifiers changed from: protected */
    public int newerField(int i, int i2) {
        int[] iArr = this.stamp;
        return iArr[i2] > iArr[i] ? i2 : i;
    }

    /* access modifiers changed from: protected */
    public void validateFields() {
        for (int i = 0; i < this.fields.length; i++) {
            if (this.stamp[i] >= 2) {
                validateField(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void validateField(int i) {
        if (i == 5) {
            validateField(i, 1, handleGetMonthLength(handleGetExtendedYear(), internalGet(2)));
        } else if (i == 6) {
            validateField(i, 1, handleGetYearLength(handleGetExtendedYear()));
        } else if (i != 8) {
            validateField(i, getMinimum(i), getMaximum(i));
        } else if (internalGet(i) != 0) {
            validateField(i, getMinimum(i), getMaximum(i));
        } else {
            throw new IllegalArgumentException("DAY_OF_WEEK_IN_MONTH cannot be zero");
        }
    }

    /* access modifiers changed from: protected */
    public final void validateField(int i, int i2, int i3) {
        int i4 = this.fields[i];
        if (i4 < i2 || i4 > i3) {
            throw new IllegalArgumentException(fieldName(i) + '=' + i4 + ", valid range=" + i2 + Constants.ATTRVAL_PARENT + i3);
        }
    }

    /* access modifiers changed from: protected */
    public void computeTime() {
        long j;
        int[] iArr;
        int i;
        if (!isLenient()) {
            validateFields();
        }
        long julianDayToMillis = julianDayToMillis(computeJulianDay());
        if (this.stamp[21] >= 2 && newestStamp(9, 14, 0) <= this.stamp[21]) {
            i = internalGet(21);
        } else if (Math.max(Math.abs(internalGet(11)), Math.abs(internalGet(10))) > MAX_HOURS) {
            j = computeMillisInDayLong();
            iArr = this.stamp;
            if (iArr[15] < 2 || iArr[16] >= 2) {
                this.time = (julianDayToMillis + j) - ((long) (internalGet(15) + internalGet(16)));
            } else if (!this.lenient || this.skippedWallTime == 2) {
                int computeZoneOffset = computeZoneOffset(julianDayToMillis, j);
                long j2 = (julianDayToMillis + j) - ((long) computeZoneOffset);
                if (computeZoneOffset == this.zone.getOffset(j2)) {
                    this.time = j2;
                    return;
                } else if (this.lenient) {
                    Long immediatePreviousZoneTransition = getImmediatePreviousZoneTransition(j2);
                    if (immediatePreviousZoneTransition != null) {
                        this.time = immediatePreviousZoneTransition.longValue();
                        return;
                    }
                    throw new RuntimeException("Could not locate a time zone transition before " + j2);
                } else {
                    throw new IllegalArgumentException("The specified wall time does not exist due to time zone offset transition.");
                }
            } else {
                this.time = (julianDayToMillis + j) - ((long) computeZoneOffset(julianDayToMillis, j));
                return;
            }
        } else {
            i = computeMillisInDay();
        }
        j = (long) i;
        iArr = this.stamp;
        if (iArr[15] < 2) {
        }
        this.time = (julianDayToMillis + j) - ((long) (internalGet(15) + internalGet(16)));
    }

    private Long getImmediatePreviousZoneTransition(long j) {
        TimeZone timeZone = this.zone;
        if (timeZone instanceof BasicTimeZone) {
            TimeZoneTransition previousTransition = ((BasicTimeZone) timeZone).getPreviousTransition(j, true);
            if (previousTransition != null) {
                return Long.valueOf(previousTransition.getTime());
            }
            return null;
        }
        Long previousZoneTransitionTime = getPreviousZoneTransitionTime(timeZone, j, WorkStatus.WORKING_DELAY_TIME);
        return previousZoneTransitionTime == null ? getPreviousZoneTransitionTime(this.zone, j, 108000000) : previousZoneTransitionTime;
    }

    private static Long getPreviousZoneTransitionTime(TimeZone timeZone, long j, long j2) {
        long j3 = (j - j2) - 1;
        int offset = timeZone.getOffset(j);
        if (offset == timeZone.getOffset(j3)) {
            return null;
        }
        return findPreviousZoneTransitionTime(timeZone, offset, j, j3);
    }

    private static Long findPreviousZoneTransitionTime(TimeZone timeZone, int i, long j, long j2) {
        long j3;
        long j4;
        long j5;
        int[] iArr = FIND_ZONE_TRANSITION_TIME_UNITS;
        int length = iArr.length;
        boolean z = false;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                j3 = 0;
                break;
            }
            long j6 = (long) iArr[i2];
            long j7 = j2 / j6;
            long j8 = j / j6;
            if (j8 > j7) {
                j3 = (((j7 + j8) + 1) >>> 1) * j6;
                z = true;
                break;
            }
            i2++;
        }
        if (!z) {
            j3 = (j + j2) >>> 1;
        }
        if (z) {
            if (j3 == j) {
                j5 = j;
            } else if (timeZone.getOffset(j3) != i) {
                return findPreviousZoneTransitionTime(timeZone, i, j, j3);
            } else {
                j5 = j3;
            }
            j4 = j3 - 1;
        } else {
            j4 = (j + j2) >>> 1;
            j5 = j;
        }
        if (j4 == j2) {
            return Long.valueOf(j5);
        }
        if (timeZone.getOffset(j4) == i) {
            return findPreviousZoneTransitionTime(timeZone, i, j4, j2);
        }
        if (z) {
            return Long.valueOf(j5);
        }
        return findPreviousZoneTransitionTime(timeZone, i, j5, j4);
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public int computeMillisInDay() {
        int[] iArr = this.stamp;
        int i = iArr[11];
        int max = Math.max(iArr[10], iArr[9]);
        if (max <= i) {
            max = i;
        }
        int i2 = 0;
        if (max != 0) {
            if (max == i) {
                i2 = 0 + internalGet(11);
            } else {
                i2 = internalGet(10) + 0 + (internalGet(9) * 12);
            }
        }
        return (((((i2 * 60) + internalGet(12)) * 60) + internalGet(13)) * 1000) + internalGet(14);
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public long computeMillisInDayLong() {
        int[] iArr = this.stamp;
        int i = iArr[11];
        int max = Math.max(iArr[10], iArr[9]);
        if (max <= i) {
            max = i;
        }
        long j = 0;
        if (max != 0) {
            if (max == i) {
                j = 0 + ((long) internalGet(11));
            } else {
                j = ((long) internalGet(10)) + 0 + ((long) (internalGet(9) * 12));
            }
        }
        return (((((j * 60) + ((long) internalGet(12))) * 60) + ((long) internalGet(13))) * 1000) + ((long) internalGet(14));
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public int computeZoneOffset(long j, int i) {
        boolean z;
        int offset;
        int[] iArr = new int[2];
        long j2 = j + ((long) i);
        TimeZone timeZone = this.zone;
        if (timeZone instanceof BasicTimeZone) {
            ((BasicTimeZone) this.zone).getOffsetFromLocal(j2, this.skippedWallTime == 1 ? 12 : 4, this.repeatedWallTime == 1 ? 4 : 12, iArr);
        } else {
            timeZone.getOffset(j2, true, iArr);
            if (this.repeatedWallTime != 1 || (offset = (iArr[0] + iArr[1]) - this.zone.getOffset((j2 - ((long) (iArr[0] + iArr[1]))) - 21600000)) >= 0) {
                z = false;
            } else {
                this.zone.getOffset(((long) offset) + j2, true, iArr);
                z = true;
            }
            if (!z && this.skippedWallTime == 1) {
                this.zone.getOffset(j2 - ((long) (iArr[0] + iArr[1])), false, iArr);
            }
        }
        return iArr[0] + iArr[1];
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public int computeZoneOffset(long j, long j2) {
        boolean z;
        int offset;
        int[] iArr = new int[2];
        long j3 = j + j2;
        TimeZone timeZone = this.zone;
        if (timeZone instanceof BasicTimeZone) {
            ((BasicTimeZone) this.zone).getOffsetFromLocal(j3, this.skippedWallTime == 1 ? 12 : 4, this.repeatedWallTime == 1 ? 4 : 12, iArr);
        } else {
            timeZone.getOffset(j3, true, iArr);
            if (this.repeatedWallTime != 1 || (offset = (iArr[0] + iArr[1]) - this.zone.getOffset((j3 - ((long) (iArr[0] + iArr[1]))) - 21600000)) >= 0) {
                z = false;
            } else {
                this.zone.getOffset(((long) offset) + j3, true, iArr);
                z = true;
            }
            if (!z && this.skippedWallTime == 1) {
                this.zone.getOffset(j3 - ((long) (iArr[0] + iArr[1])), false, iArr);
            }
        }
        return iArr[0] + iArr[1];
    }

    /* access modifiers changed from: protected */
    public int computeJulianDay() {
        if (this.stamp[20] >= 2 && newestStamp(17, 19, newestStamp(0, 8, 0)) <= this.stamp[20]) {
            return internalGet(20);
        }
        int resolveFields = resolveFields(getFieldResolutionTable());
        if (resolveFields < 0) {
            resolveFields = 5;
        }
        return handleComputeJulianDay(resolveFields);
    }

    /* access modifiers changed from: protected */
    public int[][][] getFieldResolutionTable() {
        return DATE_PRECEDENCE;
    }

    /* access modifiers changed from: protected */
    public int handleGetMonthLength(int i, int i2) {
        return handleComputeMonthStart(i, i2 + 1, true) - handleComputeMonthStart(i, i2, true);
    }

    /* access modifiers changed from: protected */
    public int handleGetYearLength(int i) {
        return handleComputeMonthStart(i + 1, 0, false) - handleComputeMonthStart(i, 0, false);
    }

    /* access modifiers changed from: protected */
    public int handleComputeJulianDay(int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        int internalGet;
        boolean z = i == 5 || i == 4 || i == 8;
        if (i == 3 && newerField(17, 1) == 17) {
            i2 = internalGet(17);
        } else {
            i2 = handleGetExtendedYear();
        }
        internalSet(19, i2);
        int internalGet2 = z ? internalGet(2, getDefaultMonthInYear(i2)) : 0;
        int handleComputeMonthStart = handleComputeMonthStart(i2, internalGet2, z);
        if (i == 5) {
            if (isSet(5)) {
                internalGet = internalGet(5, getDefaultDayInMonth(i2, internalGet2));
            } else {
                internalGet = getDefaultDayInMonth(i2, internalGet2);
            }
        } else if (i == 6) {
            internalGet = internalGet(6);
        } else {
            int firstDayOfWeek2 = getFirstDayOfWeek();
            int julianDayToDayOfWeek = julianDayToDayOfWeek(handleComputeMonthStart + 1) - firstDayOfWeek2;
            if (julianDayToDayOfWeek < 0) {
                julianDayToDayOfWeek += 7;
            }
            int resolveFields = resolveFields(DOW_PRECEDENCE);
            if (resolveFields == 7) {
                i3 = internalGet(7) - firstDayOfWeek2;
            } else if (resolveFields != 18) {
                i3 = 0;
            } else {
                i3 = internalGet(18) - 1;
            }
            int i6 = i3 % 7;
            if (i6 < 0) {
                i6 += 7;
            }
            int i7 = (1 - julianDayToDayOfWeek) + i6;
            if (i == 8) {
                if (i7 < 1) {
                    i7 += 7;
                }
                int internalGet3 = internalGet(8, 1);
                if (internalGet3 >= 0) {
                    i4 = i7 + ((internalGet3 - 1) * 7);
                    return handleComputeMonthStart + i4;
                }
                i5 = ((handleGetMonthLength(i2, internalGet(2, 0)) - i7) / 7) + internalGet3 + 1;
            } else {
                if (7 - julianDayToDayOfWeek < getMinimalDaysInFirstWeek()) {
                    i7 += 7;
                }
                i5 = internalGet(i) - 1;
            }
            i4 = i7 + (i5 * 7);
            return handleComputeMonthStart + i4;
        }
        return handleComputeMonthStart + internalGet;
    }

    /* access modifiers changed from: protected */
    public int computeGregorianMonthStart(int i, int i2) {
        boolean z = false;
        if (i2 < 0 || i2 > 11) {
            int[] iArr = new int[1];
            i += floorDivide(i2, 12, iArr);
            i2 = iArr[0];
        }
        if (i % 4 == 0 && (i % 100 != 0 || i % 400 == 0)) {
            z = true;
        }
        int i3 = i - 1;
        int floorDivide = (((((i3 * JLogConstants.JLID_APP_FRONZED_BEGIN) + floorDivide(i3, 4)) - floorDivide(i3, 100)) + floorDivide(i3, 400)) + JAN_1_1_JULIAN_DAY) - 1;
        if (i2 == 0) {
            return floorDivide;
        }
        return floorDivide + GREGORIAN_MONTH_COUNT[i2][z ? (char) 3 : 2];
    }

    /* access modifiers changed from: protected */
    public void handleComputeFields(int i) {
        int i2;
        int i3;
        internalSet(2, getGregorianMonth());
        internalSet(5, getGregorianDayOfMonth());
        internalSet(6, getGregorianDayOfYear());
        int gregorianYear2 = getGregorianYear();
        internalSet(19, gregorianYear2);
        if (gregorianYear2 < 1) {
            i3 = 1 - gregorianYear2;
            i2 = 0;
        } else {
            i3 = gregorianYear2;
            i2 = 1;
        }
        internalSet(0, i2);
        internalSet(1, i3);
    }

    /* access modifiers changed from: protected */
    public final int getGregorianYear() {
        return this.gregorianYear;
    }

    /* access modifiers changed from: protected */
    public final int getGregorianMonth() {
        return this.gregorianMonth;
    }

    /* access modifiers changed from: protected */
    public final int getGregorianDayOfYear() {
        return this.gregorianDayOfYear;
    }

    /* access modifiers changed from: protected */
    public final int getGregorianDayOfMonth() {
        return this.gregorianDayOfMonth;
    }

    public final int getFieldCount() {
        return this.fields.length;
    }

    /* access modifiers changed from: protected */
    public final void internalSet(int i, int i2) {
        if (((1 << i) & this.internalSetMask) != 0) {
            this.fields[i] = i2;
            this.stamp[i] = 1;
            return;
        }
        throw new IllegalStateException("Subclass cannot set " + fieldName(i));
    }

    protected static final boolean isGregorianLeapYear(int i) {
        return i % 4 == 0 && (i % 100 != 0 || i % 400 == 0);
    }

    protected static final int gregorianMonthLength(int i, int i2) {
        return GREGORIAN_MONTH_COUNT[i2][isGregorianLeapYear(i) ? 1 : 0];
    }

    protected static final int gregorianPreviousMonthLength(int i, int i2) {
        if (i2 > 0) {
            return gregorianMonthLength(i, i2 - 1);
        }
        return 31;
    }

    protected static final long floorDivide(long j, long j2) {
        if (j >= 0) {
            return j / j2;
        }
        return ((j + 1) / j2) - 1;
    }

    protected static final int floorDivide(int i, int i2) {
        if (i >= 0) {
            return i / i2;
        }
        return ((i + 1) / i2) - 1;
    }

    protected static final int floorDivide(int i, int i2, int[] iArr) {
        if (i >= 0) {
            iArr[0] = i % i2;
            return i / i2;
        }
        int i3 = ((i + 1) / i2) - 1;
        iArr[0] = i - (i2 * i3);
        return i3;
    }

    protected static final int floorDivide(long j, int i, int[] iArr) {
        if (j >= 0) {
            long j2 = (long) i;
            iArr[0] = (int) (j % j2);
            return (int) (j / j2);
        }
        long j3 = (long) i;
        int i2 = (int) (((j + 1) / j3) - 1);
        iArr[0] = (int) (j - (((long) i2) * j3));
        return i2;
    }

    /* access modifiers changed from: protected */
    public String fieldName(int i) {
        try {
            return FIELD_NAME[i];
        } catch (ArrayIndexOutOfBoundsException unused) {
            return "Field " + i;
        }
    }

    protected static final int millisToJulianDay(long j) {
        return (int) (floorDivide(j, 86400000) + 2440588);
    }

    protected static final int julianDayToDayOfWeek(int i) {
        int i2 = (i + 2) % 7;
        return i2 < 1 ? i2 + 7 : i2;
    }

    /* access modifiers changed from: protected */
    public final long internalGetTimeInMillis() {
        return this.time;
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
}
