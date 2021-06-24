package ohos.global.icu.text;

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.TreeMap;
import ohos.agp.styles.attributes.TimePickerAttrsConstants;
import ohos.global.icu.impl.ICUData;
import ohos.global.icu.impl.ICUResourceBundle;
import ohos.global.icu.impl.UResource;
import ohos.global.icu.number.LocalizedNumberFormatter;
import ohos.global.icu.text.MeasureFormat;
import ohos.global.icu.util.TimeUnit;
import ohos.global.icu.util.ULocale;
import ohos.global.icu.util.UResourceBundle;

@Deprecated
public class TimeUnitFormat extends MeasureFormat {
    @Deprecated
    public static final int ABBREVIATED_NAME = 1;
    private static final String DEFAULT_PATTERN_FOR_DAY = "{0} d";
    private static final String DEFAULT_PATTERN_FOR_HOUR = "{0} h";
    private static final String DEFAULT_PATTERN_FOR_MINUTE = "{0} min";
    private static final String DEFAULT_PATTERN_FOR_MONTH = "{0} m";
    private static final String DEFAULT_PATTERN_FOR_SECOND = "{0} s";
    private static final String DEFAULT_PATTERN_FOR_WEEK = "{0} w";
    private static final String DEFAULT_PATTERN_FOR_YEAR = "{0} y";
    @Deprecated
    public static final int FULL_NAME = 0;
    private static final int TOTAL_STYLES = 2;
    private static final long serialVersionUID = -3707773153184971529L;
    private NumberFormat format;
    private transient boolean isReady;
    private ULocale locale;
    private transient PluralRules pluralRules;
    private int style;
    private transient Map<TimeUnit, Map<String, Object[]>> timeUnitToCountToPatterns;

    @Deprecated
    public TimeUnitFormat() {
        this(ULocale.getDefault(), 0);
    }

    @Deprecated
    public TimeUnitFormat(ULocale uLocale) {
        this(uLocale, 0);
    }

    @Deprecated
    public TimeUnitFormat(Locale locale2) {
        this(locale2, 0);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Deprecated
    public TimeUnitFormat(ULocale uLocale, int i) {
        super(uLocale, i == 0 ? MeasureFormat.FormatWidth.WIDE : MeasureFormat.FormatWidth.SHORT);
        this.format = super.getNumberFormatInternal();
        if (i < 0 || i >= 2) {
            throw new IllegalArgumentException("style should be either FULL_NAME or ABBREVIATED_NAME style");
        }
        this.style = i;
        this.isReady = false;
    }

    private TimeUnitFormat(ULocale uLocale, int i, NumberFormat numberFormat) {
        this(uLocale, i);
        if (numberFormat != null) {
            setNumberFormat((NumberFormat) numberFormat.clone());
        }
    }

    @Deprecated
    public TimeUnitFormat(Locale locale2, int i) {
        this(ULocale.forLocale(locale2), i);
    }

    @Deprecated
    public TimeUnitFormat setLocale(ULocale uLocale) {
        setLocale(uLocale, uLocale);
        clearCache();
        return this;
    }

    @Deprecated
    public TimeUnitFormat setLocale(Locale locale2) {
        return setLocale(ULocale.forLocale(locale2));
    }

    @Deprecated
    public TimeUnitFormat setNumberFormat(NumberFormat numberFormat) {
        if (numberFormat == this.format) {
            return this;
        }
        if (numberFormat == null) {
            ULocale uLocale = this.locale;
            if (uLocale == null) {
                this.isReady = false;
            } else {
                this.format = NumberFormat.getNumberInstance(uLocale);
            }
        } else {
            this.format = numberFormat;
        }
        clearCache();
        return this;
    }

    @Override // ohos.global.icu.text.MeasureFormat
    @Deprecated
    public NumberFormat getNumberFormat() {
        return (NumberFormat) this.format.clone();
    }

    /* access modifiers changed from: package-private */
    @Override // ohos.global.icu.text.MeasureFormat
    public NumberFormat getNumberFormatInternal() {
        return this.format;
    }

    /* access modifiers changed from: package-private */
    @Override // ohos.global.icu.text.MeasureFormat
    public LocalizedNumberFormatter getNumberFormatter() {
        return ((DecimalFormat) this.format).toNumberFormatter();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v9, types: [java.lang.Number] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @Override // ohos.global.icu.text.MeasureFormat, ohos.global.icu.text.MeasureFormat
    @java.lang.Deprecated
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.global.icu.util.TimeUnitAmount parseObject(java.lang.String r20, java.text.ParsePosition r21) {
        /*
        // Method dump skipped, instructions count: 259
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.TimeUnitFormat.parseObject(java.lang.String, java.text.ParsePosition):ohos.global.icu.util.TimeUnitAmount");
    }

    private void setup() {
        if (this.locale == null) {
            NumberFormat numberFormat = this.format;
            if (numberFormat != null) {
                this.locale = numberFormat.getLocale(null);
            } else {
                this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
            }
            ULocale uLocale = this.locale;
            setLocale(uLocale, uLocale);
        }
        if (this.format == null) {
            this.format = NumberFormat.getNumberInstance(this.locale);
        }
        this.pluralRules = PluralRules.forLocale(this.locale);
        this.timeUnitToCountToPatterns = new HashMap();
        Set<String> keywords = this.pluralRules.getKeywords();
        setup("units/duration", this.timeUnitToCountToPatterns, 0, keywords);
        setup("unitsShort/duration", this.timeUnitToCountToPatterns, 1, keywords);
        this.isReady = true;
    }

    /* access modifiers changed from: private */
    public static final class TimeUnitFormatSetupSink extends UResource.Sink {
        boolean beenHere = false;
        ULocale locale;
        Set<String> pluralKeywords;
        int style;
        Map<TimeUnit, Map<String, Object[]>> timeUnitToCountToPatterns;

        TimeUnitFormatSetupSink(Map<TimeUnit, Map<String, Object[]>> map, int i, Set<String> set, ULocale uLocale) {
            this.timeUnitToCountToPatterns = map;
            this.style = i;
            this.pluralKeywords = set;
            this.locale = uLocale;
        }

        @Override // ohos.global.icu.impl.UResource.Sink
        public void put(UResource.Key key, UResource.Value value, boolean z) {
            TimeUnit timeUnit;
            if (!this.beenHere) {
                this.beenHere = true;
                UResource.Table table = value.getTable();
                for (int i = 0; table.getKeyAndValue(i, key, value); i++) {
                    String key2 = key.toString();
                    if (key2.equals("year")) {
                        timeUnit = TimeUnit.YEAR;
                    } else if (key2.equals("month")) {
                        timeUnit = TimeUnit.MONTH;
                    } else if (key2.equals("day")) {
                        timeUnit = TimeUnit.DAY;
                    } else if (key2.equals(TimePickerAttrsConstants.HOUR)) {
                        timeUnit = TimeUnit.HOUR;
                    } else if (key2.equals(TimePickerAttrsConstants.MINUTE)) {
                        timeUnit = TimeUnit.MINUTE;
                    } else if (key2.equals(TimePickerAttrsConstants.SECOND)) {
                        timeUnit = TimeUnit.SECOND;
                    } else if (key2.equals("week")) {
                        timeUnit = TimeUnit.WEEK;
                    }
                    Map<String, Object[]> map = this.timeUnitToCountToPatterns.get(timeUnit);
                    if (map == null) {
                        map = new TreeMap<>();
                        this.timeUnitToCountToPatterns.put(timeUnit, map);
                    }
                    UResource.Table table2 = value.getTable();
                    for (int i2 = 0; table2.getKeyAndValue(i2, key, value); i2++) {
                        String key3 = key.toString();
                        if (this.pluralKeywords.contains(key3)) {
                            Object[] objArr = map.get(key3);
                            if (objArr == null) {
                                objArr = new Object[2];
                                map.put(key3, objArr);
                            }
                            if (objArr[this.style] == null) {
                                objArr[this.style] = new MessageFormat(value.getString(), this.locale);
                            }
                        }
                    }
                }
            }
        }
    }

    private void setup(String str, Map<TimeUnit, Map<String, Object[]>> map, int i, Set<String> set) {
        try {
            try {
                ((ICUResourceBundle) UResourceBundle.getBundleInstance(ICUData.ICU_UNIT_BASE_NAME, this.locale)).getAllItemsWithFallback(str, new TimeUnitFormatSetupSink(map, i, set, this.locale));
            } catch (MissingResourceException unused) {
            }
        } catch (MissingResourceException unused2) {
        }
        TimeUnit[] values = TimeUnit.values();
        Set<String> keywords = this.pluralRules.getKeywords();
        for (TimeUnit timeUnit : values) {
            Map<String, Object[]> map2 = map.get(timeUnit);
            if (map2 == null) {
                map2 = new TreeMap<>();
                map.put(timeUnit, map2);
            }
            for (String str2 : keywords) {
                if (map2.get(str2) == null || map2.get(str2)[i] == null) {
                    searchInTree(str, i, timeUnit, str2, str2, map2);
                }
            }
        }
    }

    private void searchInTree(String str, int i, TimeUnit timeUnit, String str2, String str3, Map<String, Object[]> map) {
        ULocale uLocale = this.locale;
        String timeUnit2 = timeUnit.toString();
        while (uLocale != null) {
            try {
                MessageFormat messageFormat = new MessageFormat(((ICUResourceBundle) UResourceBundle.getBundleInstance(ICUData.ICU_UNIT_BASE_NAME, uLocale)).getWithFallback(str).getWithFallback(timeUnit2).getStringWithFallback(str3), this.locale);
                Object[] objArr = map.get(str2);
                if (objArr == null) {
                    objArr = new Object[2];
                    map.put(str2, objArr);
                }
                objArr[i] = messageFormat;
                return;
            } catch (MissingResourceException unused) {
                uLocale = uLocale.getFallback();
            }
        }
        if (uLocale == null && str.equals("unitsShort")) {
            searchInTree("units", i, timeUnit, str2, str3, map);
            if (!(map.get(str2) == null || map.get(str2)[i] == null)) {
                return;
            }
        }
        if (str3.equals("other")) {
            MessageFormat messageFormat2 = null;
            if (timeUnit == TimeUnit.SECOND) {
                messageFormat2 = new MessageFormat(DEFAULT_PATTERN_FOR_SECOND, this.locale);
            } else if (timeUnit == TimeUnit.MINUTE) {
                messageFormat2 = new MessageFormat(DEFAULT_PATTERN_FOR_MINUTE, this.locale);
            } else if (timeUnit == TimeUnit.HOUR) {
                messageFormat2 = new MessageFormat(DEFAULT_PATTERN_FOR_HOUR, this.locale);
            } else if (timeUnit == TimeUnit.WEEK) {
                messageFormat2 = new MessageFormat(DEFAULT_PATTERN_FOR_WEEK, this.locale);
            } else if (timeUnit == TimeUnit.DAY) {
                messageFormat2 = new MessageFormat(DEFAULT_PATTERN_FOR_DAY, this.locale);
            } else if (timeUnit == TimeUnit.MONTH) {
                messageFormat2 = new MessageFormat(DEFAULT_PATTERN_FOR_MONTH, this.locale);
            } else if (timeUnit == TimeUnit.YEAR) {
                messageFormat2 = new MessageFormat(DEFAULT_PATTERN_FOR_YEAR, this.locale);
            }
            Object[] objArr2 = map.get(str2);
            if (objArr2 == null) {
                objArr2 = new Object[2];
                map.put(str2, objArr2);
            }
            objArr2[i] = messageFormat2;
            return;
        }
        searchInTree(str, i, timeUnit, str2, "other", map);
    }

    @Override // java.lang.Object
    @Deprecated
    public Object clone() {
        TimeUnitFormat timeUnitFormat = (TimeUnitFormat) super.clone();
        timeUnitFormat.format = (NumberFormat) this.format.clone();
        return timeUnitFormat;
    }

    private Object writeReplace() throws ObjectStreamException {
        return super.toTimeUnitProxy();
    }

    private Object readResolve() throws ObjectStreamException {
        return new TimeUnitFormat(this.locale, this.style, this.format);
    }
}
