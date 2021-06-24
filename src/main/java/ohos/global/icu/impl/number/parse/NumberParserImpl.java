package ohos.global.icu.impl.number.parse;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ohos.global.icu.impl.StringSegment;
import ohos.global.icu.impl.number.DecimalFormatProperties;
import ohos.global.icu.impl.number.Grouper;
import ohos.global.icu.impl.number.PatternStringParser;
import ohos.global.icu.number.NumberFormatter;
import ohos.global.icu.text.DecimalFormatSymbols;
import ohos.global.icu.util.Currency;
import ohos.global.icu.util.CurrencyAmount;
import ohos.global.icu.util.ULocale;

public class NumberParserImpl {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private boolean frozen;
    private final List<NumberParseMatcher> matchers = new ArrayList();
    private final int parseFlags;

    public static NumberParserImpl createSimpleParser(ULocale uLocale, String str, int i) {
        NumberParserImpl numberParserImpl = new NumberParserImpl(i);
        Currency instance = Currency.getInstance("USD");
        DecimalFormatSymbols instance2 = DecimalFormatSymbols.getInstance(uLocale);
        IgnorablesMatcher instance3 = IgnorablesMatcher.getInstance(i);
        AffixTokenMatcherFactory affixTokenMatcherFactory = new AffixTokenMatcherFactory();
        affixTokenMatcherFactory.currency = instance;
        affixTokenMatcherFactory.symbols = instance2;
        affixTokenMatcherFactory.ignorables = instance3;
        affixTokenMatcherFactory.locale = uLocale;
        affixTokenMatcherFactory.parseFlags = i;
        PatternStringParser.ParsedPatternInfo parseToPatternInfo = PatternStringParser.parseToPatternInfo(str);
        AffixMatcher.createMatchers(parseToPatternInfo, numberParserImpl, affixTokenMatcherFactory, instance3, i);
        Grouper withLocaleData = Grouper.forStrategy(NumberFormatter.GroupingStrategy.AUTO).withLocaleData(uLocale, parseToPatternInfo);
        numberParserImpl.addMatcher(instance3);
        numberParserImpl.addMatcher(DecimalMatcher.getInstance(instance2, withLocaleData, i));
        numberParserImpl.addMatcher(MinusSignMatcher.getInstance(instance2, false));
        numberParserImpl.addMatcher(PlusSignMatcher.getInstance(instance2, false));
        numberParserImpl.addMatcher(PercentMatcher.getInstance(instance2));
        numberParserImpl.addMatcher(PermilleMatcher.getInstance(instance2));
        numberParserImpl.addMatcher(NanMatcher.getInstance(instance2, i));
        numberParserImpl.addMatcher(InfinityMatcher.getInstance(instance2));
        numberParserImpl.addMatcher(PaddingMatcher.getInstance("@"));
        numberParserImpl.addMatcher(ScientificMatcher.getInstance(instance2, withLocaleData));
        numberParserImpl.addMatcher(CombinedCurrencyMatcher.getInstance(instance, instance2, i));
        numberParserImpl.addMatcher(new RequireNumberValidator());
        numberParserImpl.freeze();
        return numberParserImpl;
    }

    public static Number parseStatic(String str, ParsePosition parsePosition, DecimalFormatProperties decimalFormatProperties, DecimalFormatSymbols decimalFormatSymbols) {
        NumberParserImpl createParserFromProperties = createParserFromProperties(decimalFormatProperties, decimalFormatSymbols, false);
        ParsedNumber parsedNumber = new ParsedNumber();
        createParserFromProperties.parse(str, true, parsedNumber);
        if (parsedNumber.success()) {
            parsePosition.setIndex(parsedNumber.charEnd);
            return parsedNumber.getNumber();
        }
        parsePosition.setErrorIndex(parsedNumber.charEnd);
        return null;
    }

    public static CurrencyAmount parseStaticCurrency(String str, ParsePosition parsePosition, DecimalFormatProperties decimalFormatProperties, DecimalFormatSymbols decimalFormatSymbols) {
        NumberParserImpl createParserFromProperties = createParserFromProperties(decimalFormatProperties, decimalFormatSymbols, true);
        ParsedNumber parsedNumber = new ParsedNumber();
        createParserFromProperties.parse(str, true, parsedNumber);
        if (parsedNumber.success()) {
            parsePosition.setIndex(parsedNumber.charEnd);
            return new CurrencyAmount(parsedNumber.getNumber(), Currency.getInstance(parsedNumber.currencyCode));
        }
        parsePosition.setErrorIndex(parsedNumber.charEnd);
        return null;
    }

    public static NumberParserImpl createDefaultParserForLocale(ULocale uLocale) {
        return createParserFromProperties(PatternStringParser.parseToProperties("0"), DecimalFormatSymbols.getInstance(uLocale), false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0123  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x013b  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0156  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static ohos.global.icu.impl.number.parse.NumberParserImpl createParserFromProperties(ohos.global.icu.impl.number.DecimalFormatProperties r11, ohos.global.icu.text.DecimalFormatSymbols r12, boolean r13) {
        /*
        // Method dump skipped, instructions count: 354
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.number.parse.NumberParserImpl.createParserFromProperties(ohos.global.icu.impl.number.DecimalFormatProperties, ohos.global.icu.text.DecimalFormatSymbols, boolean):ohos.global.icu.impl.number.parse.NumberParserImpl");
    }

    public NumberParserImpl(int i) {
        this.parseFlags = i;
        this.frozen = false;
    }

    public void addMatcher(NumberParseMatcher numberParseMatcher) {
        this.matchers.add(numberParseMatcher);
    }

    public void addMatchers(Collection<? extends NumberParseMatcher> collection) {
        this.matchers.addAll(collection);
    }

    public void freeze() {
        this.frozen = true;
    }

    public int getParseFlags() {
        return this.parseFlags;
    }

    public void parse(String str, boolean z, ParsedNumber parsedNumber) {
        parse(str, 0, z, parsedNumber);
    }

    public void parse(String str, int i, boolean z, ParsedNumber parsedNumber) {
        StringSegment stringSegment = new StringSegment(str, (this.parseFlags & 1) != 0);
        stringSegment.adjustOffset(i);
        if (z) {
            parseGreedy(stringSegment, parsedNumber);
        } else if ((this.parseFlags & 16384) != 0) {
            parseLongestRecursive(stringSegment, parsedNumber, 1);
        } else {
            parseLongestRecursive(stringSegment, parsedNumber, -100);
        }
        for (NumberParseMatcher numberParseMatcher : this.matchers) {
            numberParseMatcher.postProcess(parsedNumber);
        }
        parsedNumber.postProcess();
    }

    private void parseGreedy(StringSegment stringSegment, ParsedNumber parsedNumber) {
        while (true) {
            int i = 0;
            while (true) {
                if (i < this.matchers.size() && stringSegment.length() != 0) {
                    NumberParseMatcher numberParseMatcher = this.matchers.get(i);
                    if (numberParseMatcher.smokeTest(stringSegment)) {
                        int offset = stringSegment.getOffset();
                        numberParseMatcher.match(stringSegment, parsedNumber);
                        if (stringSegment.getOffset() != offset) {
                        }
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    private void parseLongestRecursive(StringSegment stringSegment, ParsedNumber parsedNumber, int i) {
        if (!(stringSegment.length() == 0 || i == 0)) {
            ParsedNumber parsedNumber2 = new ParsedNumber();
            parsedNumber2.copyFrom(parsedNumber);
            ParsedNumber parsedNumber3 = new ParsedNumber();
            int offset = stringSegment.getOffset();
            for (int i2 = 0; i2 < this.matchers.size(); i2++) {
                NumberParseMatcher numberParseMatcher = this.matchers.get(i2);
                if (numberParseMatcher.smokeTest(stringSegment)) {
                    int i3 = 0;
                    while (i3 < stringSegment.length()) {
                        i3 += Character.charCount(stringSegment.codePointAt(i3));
                        parsedNumber3.copyFrom(parsedNumber2);
                        stringSegment.setLength(i3);
                        boolean match = numberParseMatcher.match(stringSegment, parsedNumber3);
                        stringSegment.resetLength();
                        if (stringSegment.getOffset() - offset == i3) {
                            parseLongestRecursive(stringSegment, parsedNumber3, i + 1);
                            if (parsedNumber3.isBetterThan(parsedNumber)) {
                                parsedNumber.copyFrom(parsedNumber3);
                            }
                        }
                        stringSegment.setOffset(offset);
                        if (!match) {
                            break;
                        }
                    }
                }
            }
        }
    }

    public String toString() {
        return "<NumberParserImpl matchers=" + this.matchers.toString() + ">";
    }
}
