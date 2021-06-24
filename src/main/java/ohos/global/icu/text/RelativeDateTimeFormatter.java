package ohos.global.icu.text;

import java.io.InvalidObjectException;
import java.lang.reflect.Array;
import java.text.AttributedCharacterIterator;
import java.text.Format;
import java.util.EnumMap;
import java.util.Locale;
import ohos.agp.styles.attributes.TimePickerAttrsConstants;
import ohos.com.sun.org.apache.xpath.internal.XPath;
import ohos.global.icu.impl.CacheBase;
import ohos.global.icu.impl.FormattedStringBuilder;
import ohos.global.icu.impl.FormattedValueStringBuilderImpl;
import ohos.global.icu.impl.ICUData;
import ohos.global.icu.impl.ICUResourceBundle;
import ohos.global.icu.impl.SimpleFormatterImpl;
import ohos.global.icu.impl.SoftCache;
import ohos.global.icu.impl.StandardPlural;
import ohos.global.icu.impl.UResource;
import ohos.global.icu.impl.Utility;
import ohos.global.icu.impl.number.DecimalQuantity_DualStorageBCD;
import ohos.global.icu.lang.UCharacter;
import ohos.global.icu.text.DisplayContext;
import ohos.global.icu.util.ICUException;
import ohos.global.icu.util.ULocale;
import ohos.global.icu.util.UResourceBundle;

public final class RelativeDateTimeFormatter {
    private static final Cache cache = new Cache(null);
    private static final Style[] fallbackCache = new Style[3];
    private final BreakIterator breakIterator;
    private final DisplayContext capitalizationContext;
    private final String combinedDateAndTime;
    private final DateFormatSymbols dateFormatSymbols;
    private final ULocale locale;
    private final NumberFormat numberFormat;
    private final EnumMap<Style, EnumMap<RelativeUnit, String[][]>> patternMap;
    private final PluralRules pluralRules;
    private final EnumMap<Style, EnumMap<AbsoluteUnit, EnumMap<Direction, String>>> qualitativeUnitMap;
    private final Style style;
    private int[] styleToDateFormatSymbolsWidth = {1, 3, 2};

    public enum AbsoluteUnit {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        DAY,
        WEEK,
        MONTH,
        YEAR,
        NOW,
        QUARTER,
        HOUR,
        MINUTE
    }

    public enum Direction {
        LAST_2,
        LAST,
        THIS,
        NEXT,
        NEXT_2,
        PLAIN
    }

    public enum RelativeDateTimeUnit {
        YEAR,
        QUARTER,
        MONTH,
        WEEK,
        DAY,
        HOUR,
        MINUTE,
        SECOND,
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }

    public enum RelativeUnit {
        SECONDS,
        MINUTES,
        HOURS,
        DAYS,
        WEEKS,
        MONTHS,
        YEARS,
        QUARTERS
    }

    public enum Style {
        LONG,
        SHORT,
        NARROW;
        
        private static final int INDEX_COUNT = 3;
    }

    public static class Field extends Format.Field {
        public static final Field LITERAL = new Field("literal");
        public static final Field NUMERIC = new Field("numeric");
        private static final long serialVersionUID = -5327685528663492325L;

        private Field(String str) {
            super(str);
        }

        /* access modifiers changed from: protected */
        @Override // java.text.AttributedCharacterIterator.Attribute
        public Object readResolve() throws InvalidObjectException {
            if (getName().equals(LITERAL.getName())) {
                return LITERAL;
            }
            if (getName().equals(NUMERIC.getName())) {
                return NUMERIC;
            }
            throw new InvalidObjectException("An invalid object.");
        }
    }

    public static class FormattedRelativeDateTime implements FormattedValue {
        private final FormattedStringBuilder string;

        /* synthetic */ FormattedRelativeDateTime(FormattedStringBuilder formattedStringBuilder, AnonymousClass1 r2) {
            this(formattedStringBuilder);
        }

        private FormattedRelativeDateTime(FormattedStringBuilder formattedStringBuilder) {
            this.string = formattedStringBuilder;
        }

        @Override // ohos.global.icu.text.FormattedValue
        public String toString() {
            return this.string.toString();
        }

        public int length() {
            return this.string.length();
        }

        public char charAt(int i) {
            return this.string.charAt(i);
        }

        public CharSequence subSequence(int i, int i2) {
            return this.string.subString(i, i2);
        }

        @Override // ohos.global.icu.text.FormattedValue
        public <A extends Appendable> A appendTo(A a) {
            return (A) Utility.appendTo(this.string, a);
        }

        @Override // ohos.global.icu.text.FormattedValue
        public boolean nextPosition(ConstrainedFieldPosition constrainedFieldPosition) {
            return FormattedValueStringBuilderImpl.nextPosition(this.string, constrainedFieldPosition, Field.NUMERIC);
        }

        @Override // ohos.global.icu.text.FormattedValue
        public AttributedCharacterIterator toCharacterIterator() {
            return FormattedValueStringBuilderImpl.toCharacterIterator(this.string, Field.NUMERIC);
        }
    }

    public static RelativeDateTimeFormatter getInstance() {
        return getInstance(ULocale.getDefault(), null, Style.LONG, DisplayContext.CAPITALIZATION_NONE);
    }

    public static RelativeDateTimeFormatter getInstance(ULocale uLocale) {
        return getInstance(uLocale, null, Style.LONG, DisplayContext.CAPITALIZATION_NONE);
    }

    public static RelativeDateTimeFormatter getInstance(Locale locale2) {
        return getInstance(ULocale.forLocale(locale2));
    }

    public static RelativeDateTimeFormatter getInstance(ULocale uLocale, NumberFormat numberFormat2) {
        return getInstance(uLocale, numberFormat2, Style.LONG, DisplayContext.CAPITALIZATION_NONE);
    }

    public static RelativeDateTimeFormatter getInstance(ULocale uLocale, NumberFormat numberFormat2, Style style2, DisplayContext displayContext) {
        NumberFormat numberFormat3;
        RelativeDateTimeFormatterData relativeDateTimeFormatterData = cache.get(uLocale);
        if (numberFormat2 == null) {
            numberFormat3 = NumberFormat.getInstance(uLocale);
        } else {
            numberFormat3 = (NumberFormat) numberFormat2.clone();
        }
        return new RelativeDateTimeFormatter(relativeDateTimeFormatterData.qualitativeUnitMap, relativeDateTimeFormatterData.relUnitPatternMap, SimpleFormatterImpl.compileToStringMinMaxArguments(relativeDateTimeFormatterData.dateTimePattern, new StringBuilder(), 2, 2), PluralRules.forLocale(uLocale), numberFormat3, style2, displayContext, displayContext == DisplayContext.CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE ? BreakIterator.getSentenceInstance(uLocale) : null, uLocale);
    }

    public static RelativeDateTimeFormatter getInstance(Locale locale2, NumberFormat numberFormat2) {
        return getInstance(ULocale.forLocale(locale2), numberFormat2);
    }

    public String format(double d, Direction direction, RelativeUnit relativeUnit) {
        return adjustForContext(formatImpl(d, direction, relativeUnit).toString());
    }

    public FormattedRelativeDateTime formatToValue(double d, Direction direction, RelativeUnit relativeUnit) {
        checkNoAdjustForContext();
        return new FormattedRelativeDateTime(formatImpl(d, direction, relativeUnit), null);
    }

    private FormattedStringBuilder formatImpl(double d, Direction direction, RelativeUnit relativeUnit) {
        String str;
        if (direction == Direction.LAST || direction == Direction.NEXT) {
            int i = direction == Direction.NEXT ? 1 : 0;
            FormattedStringBuilder formattedStringBuilder = new FormattedStringBuilder();
            NumberFormat numberFormat2 = this.numberFormat;
            if (numberFormat2 instanceof DecimalFormat) {
                DecimalQuantity_DualStorageBCD decimalQuantity_DualStorageBCD = new DecimalQuantity_DualStorageBCD(d);
                ((DecimalFormat) this.numberFormat).toNumberFormatter().formatImpl(decimalQuantity_DualStorageBCD, formattedStringBuilder);
                str = this.pluralRules.select(decimalQuantity_DualStorageBCD);
            } else {
                formattedStringBuilder.append(numberFormat2.format(d), (Object) null);
                str = this.pluralRules.select(d);
            }
            SimpleFormatterImpl.formatPrefixSuffix(getRelativeUnitPluralPattern(this.style, relativeUnit, i, StandardPlural.orOtherFromString(str)), Field.LITERAL, 0, formattedStringBuilder.length(), formattedStringBuilder);
            return formattedStringBuilder;
        }
        throw new IllegalArgumentException("direction must be NEXT or LAST");
    }

    public String formatNumeric(double d, RelativeDateTimeUnit relativeDateTimeUnit) {
        return adjustForContext(formatNumericImpl(d, relativeDateTimeUnit).toString());
    }

    public FormattedRelativeDateTime formatNumericToValue(double d, RelativeDateTimeUnit relativeDateTimeUnit) {
        checkNoAdjustForContext();
        return new FormattedRelativeDateTime(formatNumericImpl(d, relativeDateTimeUnit), null);
    }

    private FormattedStringBuilder formatNumericImpl(double d, RelativeDateTimeUnit relativeDateTimeUnit) {
        RelativeUnit relativeUnit = RelativeUnit.SECONDS;
        switch (relativeDateTimeUnit) {
            case YEAR:
                relativeUnit = RelativeUnit.YEARS;
                break;
            case QUARTER:
                relativeUnit = RelativeUnit.QUARTERS;
                break;
            case MONTH:
                relativeUnit = RelativeUnit.MONTHS;
                break;
            case WEEK:
                relativeUnit = RelativeUnit.WEEKS;
                break;
            case DAY:
                relativeUnit = RelativeUnit.DAYS;
                break;
            case HOUR:
                relativeUnit = RelativeUnit.HOURS;
                break;
            case MINUTE:
                relativeUnit = RelativeUnit.MINUTES;
                break;
            case SECOND:
                break;
            default:
                throw new UnsupportedOperationException("formatNumeric does not currently support RelativeUnit.SUNDAY..SATURDAY");
        }
        Direction direction = Direction.NEXT;
        if (Double.compare(d, XPath.MATCH_SCORE_QNAME) < 0) {
            direction = Direction.LAST;
            d = -d;
        }
        return formatImpl(d, direction, relativeUnit);
    }

    public String format(Direction direction, AbsoluteUnit absoluteUnit) {
        String formatAbsoluteImpl = formatAbsoluteImpl(direction, absoluteUnit);
        if (formatAbsoluteImpl != null) {
            return adjustForContext(formatAbsoluteImpl);
        }
        return null;
    }

    public FormattedRelativeDateTime formatToValue(Direction direction, AbsoluteUnit absoluteUnit) {
        checkNoAdjustForContext();
        String formatAbsoluteImpl = formatAbsoluteImpl(direction, absoluteUnit);
        if (formatAbsoluteImpl == null) {
            return null;
        }
        FormattedStringBuilder formattedStringBuilder = new FormattedStringBuilder();
        formattedStringBuilder.append(formatAbsoluteImpl, Field.LITERAL);
        return new FormattedRelativeDateTime(formattedStringBuilder, null);
    }

    private String formatAbsoluteImpl(Direction direction, AbsoluteUnit absoluteUnit) {
        if (absoluteUnit == AbsoluteUnit.NOW && direction != Direction.PLAIN) {
            throw new IllegalArgumentException("NOW can only accept direction PLAIN.");
        } else if (direction != Direction.PLAIN || AbsoluteUnit.SUNDAY.ordinal() > absoluteUnit.ordinal() || absoluteUnit.ordinal() > AbsoluteUnit.SATURDAY.ordinal()) {
            return getAbsoluteUnitString(this.style, absoluteUnit, direction);
        } else {
            return this.dateFormatSymbols.getWeekdays(1, this.styleToDateFormatSymbolsWidth[this.style.ordinal()])[(absoluteUnit.ordinal() - AbsoluteUnit.SUNDAY.ordinal()) + 1];
        }
    }

    public String format(double d, RelativeDateTimeUnit relativeDateTimeUnit) {
        return adjustForContext(formatRelativeImpl(d, relativeDateTimeUnit).toString());
    }

    public FormattedRelativeDateTime formatToValue(double d, RelativeDateTimeUnit relativeDateTimeUnit) {
        FormattedStringBuilder formattedStringBuilder;
        checkNoAdjustForContext();
        CharSequence formatRelativeImpl = formatRelativeImpl(d, relativeDateTimeUnit);
        if (formatRelativeImpl instanceof FormattedStringBuilder) {
            formattedStringBuilder = (FormattedStringBuilder) formatRelativeImpl;
        } else {
            FormattedStringBuilder formattedStringBuilder2 = new FormattedStringBuilder();
            formattedStringBuilder2.append(formatRelativeImpl, Field.LITERAL);
            formattedStringBuilder = formattedStringBuilder2;
        }
        return new FormattedRelativeDateTime(formattedStringBuilder, null);
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0052  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0054  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0063  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0066  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0069  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0079  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x007c  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0082  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.CharSequence formatRelativeImpl(double r9, ohos.global.icu.text.RelativeDateTimeFormatter.RelativeDateTimeUnit r11) {
        /*
        // Method dump skipped, instructions count: 186
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.RelativeDateTimeFormatter.formatRelativeImpl(double, ohos.global.icu.text.RelativeDateTimeFormatter$RelativeDateTimeUnit):java.lang.CharSequence");
    }

    private String getAbsoluteUnitString(Style style2, AbsoluteUnit absoluteUnit, Direction direction) {
        EnumMap<Direction, String> enumMap;
        String str;
        do {
            EnumMap<AbsoluteUnit, EnumMap<Direction, String>> enumMap2 = this.qualitativeUnitMap.get(style2);
            if (enumMap2 != null && (enumMap = enumMap2.get(absoluteUnit)) != null && (str = enumMap.get(direction)) != null) {
                return str;
            }
            style2 = fallbackCache[style2.ordinal()];
        } while (style2 != null);
        return null;
    }

    public String combineDateAndTime(String str, String str2) {
        return SimpleFormatterImpl.formatCompiledPattern(this.combinedDateAndTime, str2, str);
    }

    public NumberFormat getNumberFormat() {
        NumberFormat numberFormat2;
        synchronized (this.numberFormat) {
            numberFormat2 = (NumberFormat) this.numberFormat.clone();
        }
        return numberFormat2;
    }

    public DisplayContext getCapitalizationContext() {
        return this.capitalizationContext;
    }

    public Style getFormatStyle() {
        return this.style;
    }

    private String adjustForContext(String str) {
        String titleCase;
        if (this.breakIterator == null || str.length() == 0 || !UCharacter.isLowerCase(UCharacter.codePointAt(str, 0))) {
            return str;
        }
        synchronized (this.breakIterator) {
            titleCase = UCharacter.toTitleCase(this.locale, str, this.breakIterator, 768);
        }
        return titleCase;
    }

    private void checkNoAdjustForContext() {
        if (this.breakIterator != null) {
            throw new UnsupportedOperationException("Capitalization context is not supported in formatV");
        }
    }

    private RelativeDateTimeFormatter(EnumMap<Style, EnumMap<AbsoluteUnit, EnumMap<Direction, String>>> enumMap, EnumMap<Style, EnumMap<RelativeUnit, String[][]>> enumMap2, String str, PluralRules pluralRules2, NumberFormat numberFormat2, Style style2, DisplayContext displayContext, BreakIterator breakIterator2, ULocale uLocale) {
        this.qualitativeUnitMap = enumMap;
        this.patternMap = enumMap2;
        this.combinedDateAndTime = str;
        this.pluralRules = pluralRules2;
        this.numberFormat = numberFormat2;
        this.style = style2;
        if (displayContext.type() == DisplayContext.Type.CAPITALIZATION) {
            this.capitalizationContext = displayContext;
            this.breakIterator = breakIterator2;
            this.locale = uLocale;
            this.dateFormatSymbols = new DateFormatSymbols(uLocale);
            return;
        }
        throw new IllegalArgumentException(displayContext.toString());
    }

    private String getRelativeUnitPluralPattern(Style style2, RelativeUnit relativeUnit, int i, StandardPlural standardPlural) {
        String relativeUnitPattern;
        if (standardPlural == StandardPlural.OTHER || (relativeUnitPattern = getRelativeUnitPattern(style2, relativeUnit, i, standardPlural)) == null) {
            return getRelativeUnitPattern(style2, relativeUnit, i, StandardPlural.OTHER);
        }
        return relativeUnitPattern;
    }

    private String getRelativeUnitPattern(Style style2, RelativeUnit relativeUnit, int i, StandardPlural standardPlural) {
        String[][] strArr;
        int ordinal = standardPlural.ordinal();
        do {
            EnumMap<RelativeUnit, String[][]> enumMap = this.patternMap.get(style2);
            if (enumMap != null && (strArr = enumMap.get(relativeUnit)) != null && strArr[i][ordinal] != null) {
                return strArr[i][ordinal];
            }
            style2 = fallbackCache[style2.ordinal()];
        } while (style2 != null);
        return null;
    }

    /* access modifiers changed from: private */
    public static class RelativeDateTimeFormatterData {
        public final String dateTimePattern;
        public final EnumMap<Style, EnumMap<AbsoluteUnit, EnumMap<Direction, String>>> qualitativeUnitMap;
        EnumMap<Style, EnumMap<RelativeUnit, String[][]>> relUnitPatternMap;

        public RelativeDateTimeFormatterData(EnumMap<Style, EnumMap<AbsoluteUnit, EnumMap<Direction, String>>> enumMap, EnumMap<Style, EnumMap<RelativeUnit, String[][]>> enumMap2, String str) {
            this.qualitativeUnitMap = enumMap;
            this.relUnitPatternMap = enumMap2;
            this.dateTimePattern = str;
        }
    }

    /* access modifiers changed from: private */
    public static class Cache {
        private final CacheBase<String, RelativeDateTimeFormatterData, ULocale> cache;

        private Cache() {
            this.cache = new SoftCache<String, RelativeDateTimeFormatterData, ULocale>() {
                /* class ohos.global.icu.text.RelativeDateTimeFormatter.Cache.AnonymousClass1 */

                /* access modifiers changed from: protected */
                public RelativeDateTimeFormatterData createInstance(String str, ULocale uLocale) {
                    return new Loader(uLocale).load();
                }
            };
        }

        /* synthetic */ Cache(AnonymousClass1 r1) {
            this();
        }

        public RelativeDateTimeFormatterData get(ULocale uLocale) {
            return this.cache.getInstance(uLocale.toString(), uLocale);
        }
    }

    /* access modifiers changed from: private */
    public static Direction keyToDirection(UResource.Key key) {
        if (key.contentEquals("-2")) {
            return Direction.LAST_2;
        }
        if (key.contentEquals("-1")) {
            return Direction.LAST;
        }
        if (key.contentEquals("0")) {
            return Direction.THIS;
        }
        if (key.contentEquals("1")) {
            return Direction.NEXT;
        }
        if (key.contentEquals("2")) {
            return Direction.NEXT_2;
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static final class RelDateTimeDataSink extends UResource.Sink {
        int pastFutureIndex;
        EnumMap<Style, EnumMap<AbsoluteUnit, EnumMap<Direction, String>>> qualitativeUnitMap = new EnumMap<>(Style.class);
        StringBuilder sb = new StringBuilder();
        Style style;
        EnumMap<Style, EnumMap<RelativeUnit, String[][]>> styleRelUnitPatterns = new EnumMap<>(Style.class);
        DateTimeUnit unit;

        /* access modifiers changed from: private */
        public enum DateTimeUnit {
            SECOND(RelativeUnit.SECONDS, null),
            MINUTE(RelativeUnit.MINUTES, AbsoluteUnit.MINUTE),
            HOUR(RelativeUnit.HOURS, AbsoluteUnit.HOUR),
            DAY(RelativeUnit.DAYS, AbsoluteUnit.DAY),
            WEEK(RelativeUnit.WEEKS, AbsoluteUnit.WEEK),
            MONTH(RelativeUnit.MONTHS, AbsoluteUnit.MONTH),
            QUARTER(RelativeUnit.QUARTERS, AbsoluteUnit.QUARTER),
            YEAR(RelativeUnit.YEARS, AbsoluteUnit.YEAR),
            SUNDAY(null, AbsoluteUnit.SUNDAY),
            MONDAY(null, AbsoluteUnit.MONDAY),
            TUESDAY(null, AbsoluteUnit.TUESDAY),
            WEDNESDAY(null, AbsoluteUnit.WEDNESDAY),
            THURSDAY(null, AbsoluteUnit.THURSDAY),
            FRIDAY(null, AbsoluteUnit.FRIDAY),
            SATURDAY(null, AbsoluteUnit.SATURDAY);
            
            AbsoluteUnit absUnit;
            RelativeUnit relUnit;

            private DateTimeUnit(RelativeUnit relativeUnit, AbsoluteUnit absoluteUnit) {
                this.relUnit = relativeUnit;
                this.absUnit = absoluteUnit;
            }

            /* access modifiers changed from: private */
            public static final DateTimeUnit orNullFromString(CharSequence charSequence) {
                int length = charSequence.length();
                if (length != 3) {
                    if (length != 4) {
                        if (length != 5) {
                            if (length != 6) {
                                if (length == 7 && "quarter".contentEquals(charSequence)) {
                                    return QUARTER;
                                }
                                return null;
                            } else if (TimePickerAttrsConstants.MINUTE.contentEquals(charSequence)) {
                                return MINUTE;
                            } else {
                                if (TimePickerAttrsConstants.SECOND.contentEquals(charSequence)) {
                                    return SECOND;
                                }
                                return null;
                            }
                        } else if ("month".contentEquals(charSequence)) {
                            return MONTH;
                        } else {
                            return null;
                        }
                    } else if (TimePickerAttrsConstants.HOUR.contentEquals(charSequence)) {
                        return HOUR;
                    } else {
                        if ("week".contentEquals(charSequence)) {
                            return WEEK;
                        }
                        if ("year".contentEquals(charSequence)) {
                            return YEAR;
                        }
                        return null;
                    }
                } else if ("day".contentEquals(charSequence)) {
                    return DAY;
                } else {
                    if ("sun".contentEquals(charSequence)) {
                        return SUNDAY;
                    }
                    if ("mon".contentEquals(charSequence)) {
                        return MONDAY;
                    }
                    if ("tue".contentEquals(charSequence)) {
                        return TUESDAY;
                    }
                    if ("wed".contentEquals(charSequence)) {
                        return WEDNESDAY;
                    }
                    if ("thu".contentEquals(charSequence)) {
                        return THURSDAY;
                    }
                    if ("fri".contentEquals(charSequence)) {
                        return FRIDAY;
                    }
                    if ("sat".contentEquals(charSequence)) {
                        return SATURDAY;
                    }
                    return null;
                }
            }
        }

        private Style styleFromKey(UResource.Key key) {
            if (key.endsWith("-short")) {
                return Style.SHORT;
            }
            if (key.endsWith("-narrow")) {
                return Style.NARROW;
            }
            return Style.LONG;
        }

        private Style styleFromAlias(UResource.Value value) {
            String aliasString = value.getAliasString();
            if (aliasString.endsWith("-short")) {
                return Style.SHORT;
            }
            if (aliasString.endsWith("-narrow")) {
                return Style.NARROW;
            }
            return Style.LONG;
        }

        private static int styleSuffixLength(Style style2) {
            int i = AnonymousClass1.$SwitchMap$ohos$global$icu$text$RelativeDateTimeFormatter$Style[style2.ordinal()];
            if (i != 1) {
                return i != 2 ? 0 : 7;
            }
            return 6;
        }

        public void consumeTableRelative(UResource.Key key, UResource.Value value) {
            AbsoluteUnit absoluteUnit;
            UResource.Table table = value.getTable();
            for (int i = 0; table.getKeyAndValue(i, key, value); i++) {
                if (value.getType() == 0) {
                    String string = value.getString();
                    EnumMap<AbsoluteUnit, EnumMap<Direction, String>> enumMap = this.qualitativeUnitMap.get(this.style);
                    if (this.unit.relUnit != RelativeUnit.SECONDS || !key.contentEquals("0")) {
                        Direction keyToDirection = RelativeDateTimeFormatter.keyToDirection(key);
                        if (!(keyToDirection == null || (absoluteUnit = this.unit.absUnit) == null)) {
                            if (enumMap == null) {
                                enumMap = new EnumMap<>(AbsoluteUnit.class);
                                this.qualitativeUnitMap.put(this.style, enumMap);
                            }
                            EnumMap<Direction, String> enumMap2 = enumMap.get(absoluteUnit);
                            if (enumMap2 == null) {
                                enumMap2 = new EnumMap<>(Direction.class);
                                enumMap.put(absoluteUnit, enumMap2);
                            }
                            if (enumMap2.get(keyToDirection) == null) {
                                enumMap2.put(keyToDirection, value.getString());
                            }
                        }
                    } else {
                        EnumMap<Direction, String> enumMap3 = enumMap.get(AbsoluteUnit.NOW);
                        if (enumMap3 == null) {
                            enumMap3 = new EnumMap<>(Direction.class);
                            enumMap.put(AbsoluteUnit.NOW, enumMap3);
                        }
                        if (enumMap3.get(Direction.PLAIN) == null) {
                            enumMap3.put(Direction.PLAIN, string);
                        }
                    }
                }
            }
        }

        public void consumeTableRelativeTime(UResource.Key key, UResource.Value value) {
            if (this.unit.relUnit != null) {
                UResource.Table table = value.getTable();
                for (int i = 0; table.getKeyAndValue(i, key, value); i++) {
                    if (key.contentEquals("past")) {
                        this.pastFutureIndex = 0;
                    } else if (key.contentEquals("future")) {
                        this.pastFutureIndex = 1;
                    }
                    consumeTimeDetail(key, value);
                }
            }
        }

        public void consumeTimeDetail(UResource.Key key, UResource.Value value) {
            UResource.Table table = value.getTable();
            EnumMap<RelativeUnit, String[][]> enumMap = this.styleRelUnitPatterns.get(this.style);
            if (enumMap == null) {
                enumMap = new EnumMap<>(RelativeUnit.class);
                this.styleRelUnitPatterns.put(this.style, enumMap);
            }
            String[][] strArr = enumMap.get(this.unit.relUnit);
            if (strArr == null) {
                strArr = (String[][]) Array.newInstance(String.class, 2, StandardPlural.COUNT);
                enumMap.put(this.unit.relUnit, strArr);
            }
            for (int i = 0; table.getKeyAndValue(i, key, value); i++) {
                if (value.getType() == 0) {
                    int indexFromString = StandardPlural.indexFromString(key.toString());
                    int i2 = this.pastFutureIndex;
                    if (strArr[i2][indexFromString] == null) {
                        strArr[i2][indexFromString] = SimpleFormatterImpl.compileToStringMinMaxArguments(value.getString(), this.sb, 0, 1);
                    }
                }
            }
        }

        private void handlePlainDirection(UResource.Key key, UResource.Value value) {
            AbsoluteUnit absoluteUnit = this.unit.absUnit;
            if (absoluteUnit != null) {
                EnumMap<AbsoluteUnit, EnumMap<Direction, String>> enumMap = this.qualitativeUnitMap.get(this.style);
                if (enumMap == null) {
                    enumMap = new EnumMap<>(AbsoluteUnit.class);
                    this.qualitativeUnitMap.put(this.style, enumMap);
                }
                EnumMap<Direction, String> enumMap2 = enumMap.get(absoluteUnit);
                if (enumMap2 == null) {
                    enumMap2 = new EnumMap<>(Direction.class);
                    enumMap.put(absoluteUnit, enumMap2);
                }
                if (enumMap2.get(Direction.PLAIN) == null) {
                    enumMap2.put(Direction.PLAIN, value.toString());
                }
            }
        }

        public void consumeTimeUnit(UResource.Key key, UResource.Value value) {
            UResource.Table table = value.getTable();
            for (int i = 0; table.getKeyAndValue(i, key, value); i++) {
                if (key.contentEquals("dn") && value.getType() == 0) {
                    handlePlainDirection(key, value);
                }
                if (value.getType() == 2) {
                    if (key.contentEquals("relative")) {
                        consumeTableRelative(key, value);
                    } else if (key.contentEquals("relativeTime")) {
                        consumeTableRelativeTime(key, value);
                    }
                }
            }
        }

        private void handleAlias(UResource.Key key, UResource.Value value, boolean z) {
            Style styleFromKey = styleFromKey(key);
            if (DateTimeUnit.orNullFromString(key.substring(0, key.length() - styleSuffixLength(styleFromKey))) != null) {
                Style styleFromAlias = styleFromAlias(value);
                if (styleFromKey == styleFromAlias) {
                    throw new ICUException("Invalid style fallback from " + styleFromKey + " to itself");
                } else if (RelativeDateTimeFormatter.fallbackCache[styleFromKey.ordinal()] == null) {
                    RelativeDateTimeFormatter.fallbackCache[styleFromKey.ordinal()] = styleFromAlias;
                } else if (RelativeDateTimeFormatter.fallbackCache[styleFromKey.ordinal()] != styleFromAlias) {
                    throw new ICUException("Inconsistent style fallback for style " + styleFromKey + " to " + styleFromAlias);
                }
            }
        }

        @Override // ohos.global.icu.impl.UResource.Sink
        public void put(UResource.Key key, UResource.Value value, boolean z) {
            if (value.getType() != 3) {
                UResource.Table table = value.getTable();
                for (int i = 0; table.getKeyAndValue(i, key, value); i++) {
                    if (value.getType() == 3) {
                        handleAlias(key, value, z);
                    } else {
                        this.style = styleFromKey(key);
                        this.unit = DateTimeUnit.orNullFromString(key.substring(0, key.length() - styleSuffixLength(this.style)));
                        if (this.unit != null) {
                            consumeTimeUnit(key, value);
                        }
                    }
                }
            }
        }

        RelDateTimeDataSink() {
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.global.icu.text.RelativeDateTimeFormatter$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$text$RelativeDateTimeFormatter$Style = new int[Style.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(36:0|(2:1|2)|3|(2:5|6)|7|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|40) */
        /* JADX WARNING: Can't wrap try/catch for region: R(37:0|(2:1|2)|3|5|6|7|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|40) */
        /* JADX WARNING: Can't wrap try/catch for region: R(38:0|1|2|3|5|6|7|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|40) */
        /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0032 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x003c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0047 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0052 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x005d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0068 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0073 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x007f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x008b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0097 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x00a3 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00af */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x00bb */
        /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x00c7 */
        static {
            /*
            // Method dump skipped, instructions count: 212
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.RelativeDateTimeFormatter.AnonymousClass1.<clinit>():void");
        }
    }

    /* access modifiers changed from: private */
    public static class Loader {
        private final ULocale ulocale;

        public Loader(ULocale uLocale) {
            this.ulocale = uLocale;
        }

        private String getDateTimePattern(ICUResourceBundle iCUResourceBundle) {
            String stringWithFallback = iCUResourceBundle.getStringWithFallback("calendar/default");
            if (stringWithFallback == null || stringWithFallback.equals("")) {
                stringWithFallback = "gregorian";
            }
            ICUResourceBundle findWithFallback = iCUResourceBundle.findWithFallback("calendar/" + stringWithFallback + "/DateTimePatterns");
            if (findWithFallback == null && stringWithFallback.equals("gregorian")) {
                findWithFallback = iCUResourceBundle.findWithFallback("calendar/gregorian/DateTimePatterns");
            }
            if (findWithFallback == null || findWithFallback.getSize() < 9) {
                return "{1} {0}";
            }
            if (findWithFallback.get(8).getType() == 8) {
                return findWithFallback.get(8).getString(0);
            }
            return findWithFallback.getString(8);
        }

        public RelativeDateTimeFormatterData load() {
            Style style;
            RelDateTimeDataSink relDateTimeDataSink = new RelDateTimeDataSink();
            ICUResourceBundle iCUResourceBundle = (ICUResourceBundle) UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, this.ulocale);
            iCUResourceBundle.getAllItemsWithFallback("fields", relDateTimeDataSink);
            for (Style style2 : Style.values()) {
                Style style3 = RelativeDateTimeFormatter.fallbackCache[style2.ordinal()];
                if (style3 != null && (style = RelativeDateTimeFormatter.fallbackCache[style3.ordinal()]) != null && RelativeDateTimeFormatter.fallbackCache[style.ordinal()] != null) {
                    throw new IllegalStateException("Style fallback too deep");
                }
            }
            return new RelativeDateTimeFormatterData(relDateTimeDataSink.qualitativeUnitMap, relDateTimeDataSink.styleRelUnitPatterns, getDateTimePattern(iCUResourceBundle));
        }
    }
}
