package ohos.global.icu.text;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.ParsePosition;
import ohos.com.sun.org.apache.xpath.internal.XPath;
import ohos.global.icu.impl.FormattedStringBuilder;
import ohos.global.icu.impl.FormattedValueStringBuilderImpl;
import ohos.global.icu.impl.Utility;
import ohos.global.icu.impl.number.DecimalFormatProperties;
import ohos.global.icu.impl.number.DecimalQuantity;
import ohos.global.icu.impl.number.DecimalQuantity_DualStorageBCD;
import ohos.global.icu.impl.number.Padder;
import ohos.global.icu.impl.number.PatternStringParser;
import ohos.global.icu.impl.number.PatternStringUtils;
import ohos.global.icu.impl.number.parse.NumberParserImpl;
import ohos.global.icu.impl.number.parse.ParsedNumber;
import ohos.global.icu.number.LocalizedNumberFormatter;
import ohos.global.icu.number.NumberFormatter;
import ohos.global.icu.text.PluralRules;
import ohos.global.icu.util.Currency;
import ohos.global.icu.util.CurrencyAmount;
import ohos.global.icu.util.ULocale;

public class DecimalFormat extends NumberFormat {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int PAD_AFTER_PREFIX = 1;
    public static final int PAD_AFTER_SUFFIX = 3;
    public static final int PAD_BEFORE_PREFIX = 0;
    public static final int PAD_BEFORE_SUFFIX = 2;
    private static final long serialVersionUID = 864413376551465018L;
    volatile transient NumberParserImpl currencyParser;
    volatile transient DecimalFormatProperties exportedProperties;
    volatile transient LocalizedNumberFormatter formatter;
    private transient int icuMathContextForm;
    volatile transient NumberParserImpl parser;
    transient DecimalFormatProperties properties;
    private final int serialVersionOnStream;
    volatile transient DecimalFormatSymbols symbols;

    @Deprecated
    public interface PropertySetter {
        @Deprecated
        void set(DecimalFormatProperties decimalFormatProperties);
    }

    @Deprecated
    public int getParseMaxDigits() {
        return 1000;
    }

    @Deprecated
    public void setParseMaxDigits(int i) {
    }

    public DecimalFormat() {
        this.serialVersionOnStream = 5;
        this.icuMathContextForm = 0;
        String pattern = getPattern(ULocale.getDefault(ULocale.Category.FORMAT), 0);
        this.symbols = getDefaultSymbols();
        this.properties = new DecimalFormatProperties();
        this.exportedProperties = new DecimalFormatProperties();
        setPropertiesFromPattern(pattern, 1);
        refreshFormatter();
    }

    public DecimalFormat(String str) {
        this.serialVersionOnStream = 5;
        this.icuMathContextForm = 0;
        this.symbols = getDefaultSymbols();
        this.properties = new DecimalFormatProperties();
        this.exportedProperties = new DecimalFormatProperties();
        setPropertiesFromPattern(str, 1);
        refreshFormatter();
    }

    public DecimalFormat(String str, DecimalFormatSymbols decimalFormatSymbols) {
        this.serialVersionOnStream = 5;
        this.icuMathContextForm = 0;
        this.symbols = (DecimalFormatSymbols) decimalFormatSymbols.clone();
        this.properties = new DecimalFormatProperties();
        this.exportedProperties = new DecimalFormatProperties();
        setPropertiesFromPattern(str, 1);
        refreshFormatter();
    }

    public DecimalFormat(String str, DecimalFormatSymbols decimalFormatSymbols, CurrencyPluralInfo currencyPluralInfo, int i) {
        this(str, decimalFormatSymbols, i);
        this.properties.setCurrencyPluralInfo(currencyPluralInfo);
        refreshFormatter();
    }

    DecimalFormat(String str, DecimalFormatSymbols decimalFormatSymbols, int i) {
        this.serialVersionOnStream = 5;
        this.icuMathContextForm = 0;
        this.symbols = (DecimalFormatSymbols) decimalFormatSymbols.clone();
        this.properties = new DecimalFormatProperties();
        this.exportedProperties = new DecimalFormatProperties();
        if (i == 1 || i == 5 || i == 7 || i == 8 || i == 9 || i == 6) {
            setPropertiesFromPattern(str, 2);
        } else {
            setPropertiesFromPattern(str, 1);
        }
        refreshFormatter();
    }

    private static DecimalFormatSymbols getDefaultSymbols() {
        return DecimalFormatSymbols.getInstance();
    }

    public synchronized void applyPattern(String str) {
        setPropertiesFromPattern(str, 0);
        this.properties.setPositivePrefix(null);
        this.properties.setNegativePrefix(null);
        this.properties.setPositiveSuffix(null);
        this.properties.setNegativeSuffix(null);
        this.properties.setCurrencyPluralInfo(null);
        refreshFormatter();
    }

    public synchronized void applyLocalizedPattern(String str) {
        applyPattern(PatternStringUtils.convertLocalized(str, this.symbols, false));
    }

    @Override // ohos.global.icu.text.NumberFormat, java.lang.Object
    public Object clone() {
        DecimalFormat decimalFormat = (DecimalFormat) super.clone();
        decimalFormat.symbols = (DecimalFormatSymbols) this.symbols.clone();
        decimalFormat.properties = this.properties.clone();
        decimalFormat.exportedProperties = new DecimalFormatProperties();
        decimalFormat.refreshFormatter();
        return decimalFormat;
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(0);
        objectOutputStream.writeObject(this.properties);
        objectOutputStream.writeObject(this.symbols);
    }

    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v3, types: [byte, boolean] */
    /* JADX WARN: Type inference failed for: r4v4 */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readObject(java.io.ObjectInputStream r19) throws java.io.IOException, java.lang.ClassNotFoundException {
        /*
        // Method dump skipped, instructions count: 955
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.DecimalFormat.readObject(java.io.ObjectInputStream):void");
    }

    @Override // ohos.global.icu.text.NumberFormat
    public StringBuffer format(double d, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        DecimalQuantity_DualStorageBCD decimalQuantity_DualStorageBCD = new DecimalQuantity_DualStorageBCD(d);
        FormattedStringBuilder formattedStringBuilder = new FormattedStringBuilder();
        this.formatter.formatImpl(decimalQuantity_DualStorageBCD, formattedStringBuilder);
        fieldPositionHelper(decimalQuantity_DualStorageBCD, formattedStringBuilder, fieldPosition, stringBuffer.length());
        Utility.appendTo(formattedStringBuilder, stringBuffer);
        return stringBuffer;
    }

    @Override // ohos.global.icu.text.NumberFormat
    public StringBuffer format(long j, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        DecimalQuantity_DualStorageBCD decimalQuantity_DualStorageBCD = new DecimalQuantity_DualStorageBCD(j);
        FormattedStringBuilder formattedStringBuilder = new FormattedStringBuilder();
        this.formatter.formatImpl(decimalQuantity_DualStorageBCD, formattedStringBuilder);
        fieldPositionHelper(decimalQuantity_DualStorageBCD, formattedStringBuilder, fieldPosition, stringBuffer.length());
        Utility.appendTo(formattedStringBuilder, stringBuffer);
        return stringBuffer;
    }

    @Override // ohos.global.icu.text.NumberFormat
    public StringBuffer format(BigInteger bigInteger, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        DecimalQuantity_DualStorageBCD decimalQuantity_DualStorageBCD = new DecimalQuantity_DualStorageBCD(bigInteger);
        FormattedStringBuilder formattedStringBuilder = new FormattedStringBuilder();
        this.formatter.formatImpl(decimalQuantity_DualStorageBCD, formattedStringBuilder);
        fieldPositionHelper(decimalQuantity_DualStorageBCD, formattedStringBuilder, fieldPosition, stringBuffer.length());
        Utility.appendTo(formattedStringBuilder, stringBuffer);
        return stringBuffer;
    }

    @Override // ohos.global.icu.text.NumberFormat
    public StringBuffer format(BigDecimal bigDecimal, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        DecimalQuantity_DualStorageBCD decimalQuantity_DualStorageBCD = new DecimalQuantity_DualStorageBCD(bigDecimal);
        FormattedStringBuilder formattedStringBuilder = new FormattedStringBuilder();
        this.formatter.formatImpl(decimalQuantity_DualStorageBCD, formattedStringBuilder);
        fieldPositionHelper(decimalQuantity_DualStorageBCD, formattedStringBuilder, fieldPosition, stringBuffer.length());
        Utility.appendTo(formattedStringBuilder, stringBuffer);
        return stringBuffer;
    }

    @Override // ohos.global.icu.text.NumberFormat
    public StringBuffer format(ohos.global.icu.math.BigDecimal bigDecimal, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        DecimalQuantity_DualStorageBCD decimalQuantity_DualStorageBCD = new DecimalQuantity_DualStorageBCD(bigDecimal);
        FormattedStringBuilder formattedStringBuilder = new FormattedStringBuilder();
        this.formatter.formatImpl(decimalQuantity_DualStorageBCD, formattedStringBuilder);
        fieldPositionHelper(decimalQuantity_DualStorageBCD, formattedStringBuilder, fieldPosition, stringBuffer.length());
        Utility.appendTo(formattedStringBuilder, stringBuffer);
        return stringBuffer;
    }

    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        if (obj instanceof Number) {
            return this.formatter.format((Number) obj).toCharacterIterator();
        }
        throw new IllegalArgumentException();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public StringBuffer format(CurrencyAmount currencyAmount, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        DecimalFormatSymbols decimalFormatSymbols = (DecimalFormatSymbols) this.symbols.clone();
        decimalFormatSymbols.setCurrency(currencyAmount.getCurrency());
        DecimalQuantity_DualStorageBCD decimalQuantity_DualStorageBCD = new DecimalQuantity_DualStorageBCD(currencyAmount.getNumber());
        FormattedStringBuilder formattedStringBuilder = new FormattedStringBuilder();
        ((LocalizedNumberFormatter) ((LocalizedNumberFormatter) this.formatter.symbols(decimalFormatSymbols)).unit(currencyAmount.getCurrency())).formatImpl(decimalQuantity_DualStorageBCD, formattedStringBuilder);
        fieldPositionHelper(decimalQuantity_DualStorageBCD, formattedStringBuilder, fieldPosition, stringBuffer.length());
        Utility.appendTo(formattedStringBuilder, stringBuffer);
        return stringBuffer;
    }

    @Override // ohos.global.icu.text.NumberFormat
    public Number parse(String str, ParsePosition parsePosition) {
        if (str != null) {
            if (parsePosition == null) {
                parsePosition = new ParsePosition(0);
            }
            if (parsePosition.getIndex() < 0) {
                throw new IllegalArgumentException("Cannot start parsing at a negative offset");
            } else if (parsePosition.getIndex() >= str.length()) {
                return null;
            } else {
                ParsedNumber parsedNumber = new ParsedNumber();
                int index = parsePosition.getIndex();
                NumberParserImpl parser2 = getParser();
                parser2.parse(str, index, true, parsedNumber);
                if (parsedNumber.success()) {
                    parsePosition.setIndex(parsedNumber.charEnd);
                    Number number = parsedNumber.getNumber(parser2.getParseFlags());
                    return number instanceof BigDecimal ? safeConvertBigDecimal((BigDecimal) number) : number;
                }
                parsePosition.setErrorIndex(index + parsedNumber.charEnd);
                return null;
            }
        } else {
            throw new IllegalArgumentException("Text cannot be null");
        }
    }

    @Override // ohos.global.icu.text.NumberFormat
    public CurrencyAmount parseCurrency(CharSequence charSequence, ParsePosition parsePosition) {
        if (charSequence != null) {
            if (parsePosition == null) {
                parsePosition = new ParsePosition(0);
            }
            if (parsePosition.getIndex() < 0) {
                throw new IllegalArgumentException("Cannot start parsing at a negative offset");
            } else if (parsePosition.getIndex() >= charSequence.length()) {
                return null;
            } else {
                ParsedNumber parsedNumber = new ParsedNumber();
                int index = parsePosition.getIndex();
                NumberParserImpl currencyParser2 = getCurrencyParser();
                currencyParser2.parse(charSequence.toString(), index, true, parsedNumber);
                if (parsedNumber.success()) {
                    parsePosition.setIndex(parsedNumber.charEnd);
                    Number number = parsedNumber.getNumber(currencyParser2.getParseFlags());
                    if (number instanceof BigDecimal) {
                        number = safeConvertBigDecimal((BigDecimal) number);
                    }
                    return new CurrencyAmount(number, Currency.getInstance(parsedNumber.currencyCode));
                }
                parsePosition.setErrorIndex(index + parsedNumber.charEnd);
                return null;
            }
        } else {
            throw new IllegalArgumentException("Text cannot be null");
        }
    }

    public synchronized DecimalFormatSymbols getDecimalFormatSymbols() {
        return (DecimalFormatSymbols) this.symbols.clone();
    }

    public synchronized void setDecimalFormatSymbols(DecimalFormatSymbols decimalFormatSymbols) {
        this.symbols = (DecimalFormatSymbols) decimalFormatSymbols.clone();
        refreshFormatter();
    }

    public synchronized String getPositivePrefix() {
        return this.formatter.getAffixImpl(true, false);
    }

    public synchronized void setPositivePrefix(String str) {
        if (str != null) {
            this.properties.setPositivePrefix(str);
            refreshFormatter();
        } else {
            throw new NullPointerException();
        }
    }

    public synchronized String getNegativePrefix() {
        return this.formatter.getAffixImpl(true, true);
    }

    public synchronized void setNegativePrefix(String str) {
        if (str != null) {
            this.properties.setNegativePrefix(str);
            refreshFormatter();
        } else {
            throw new NullPointerException();
        }
    }

    public synchronized String getPositiveSuffix() {
        return this.formatter.getAffixImpl(false, false);
    }

    public synchronized void setPositiveSuffix(String str) {
        if (str != null) {
            this.properties.setPositiveSuffix(str);
            refreshFormatter();
        } else {
            throw new NullPointerException();
        }
    }

    public synchronized String getNegativeSuffix() {
        return this.formatter.getAffixImpl(false, true);
    }

    public synchronized void setNegativeSuffix(String str) {
        if (str != null) {
            this.properties.setNegativeSuffix(str);
            refreshFormatter();
        } else {
            throw new NullPointerException();
        }
    }

    public synchronized boolean isSignAlwaysShown() {
        return this.properties.getSignAlwaysShown();
    }

    public synchronized void setSignAlwaysShown(boolean z) {
        this.properties.setSignAlwaysShown(z);
        refreshFormatter();
    }

    public synchronized int getMultiplier() {
        if (this.properties.getMultiplier() != null) {
            return this.properties.getMultiplier().intValue();
        }
        return (int) Math.pow(10.0d, (double) this.properties.getMagnitudeMultiplier());
    }

    public synchronized void setMultiplier(int i) {
        if (i != 0) {
            int i2 = i;
            int i3 = 0;
            while (true) {
                if (i2 == 1) {
                    break;
                }
                i3++;
                int i4 = i2 / 10;
                if (i4 * 10 != i2) {
                    i3 = -1;
                    break;
                }
                i2 = i4;
            }
            if (i3 != -1) {
                this.properties.setMagnitudeMultiplier(i3);
                this.properties.setMultiplier(null);
            } else {
                this.properties.setMagnitudeMultiplier(0);
                this.properties.setMultiplier(BigDecimal.valueOf((long) i));
            }
            refreshFormatter();
        } else {
            throw new IllegalArgumentException("Multiplier must be nonzero.");
        }
    }

    public synchronized BigDecimal getRoundingIncrement() {
        return this.exportedProperties.getRoundingIncrement();
    }

    public synchronized void setRoundingIncrement(BigDecimal bigDecimal) {
        if (bigDecimal != null) {
            if (bigDecimal.compareTo(BigDecimal.ZERO) == 0) {
                this.properties.setMaximumFractionDigits(Integer.MAX_VALUE);
                return;
            }
        }
        this.properties.setRoundingIncrement(bigDecimal);
        refreshFormatter();
    }

    public synchronized void setRoundingIncrement(ohos.global.icu.math.BigDecimal bigDecimal) {
        setRoundingIncrement(bigDecimal == null ? null : bigDecimal.toBigDecimal());
    }

    public synchronized void setRoundingIncrement(double d) {
        if (d == XPath.MATCH_SCORE_QNAME) {
            setRoundingIncrement((BigDecimal) null);
        } else {
            setRoundingIncrement(BigDecimal.valueOf(d));
        }
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized int getRoundingMode() {
        int i;
        RoundingMode roundingMode = this.exportedProperties.getRoundingMode();
        if (roundingMode == null) {
            i = 0;
        } else {
            i = roundingMode.ordinal();
        }
        return i;
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized void setRoundingMode(int i) {
        this.properties.setRoundingMode(RoundingMode.valueOf(i));
        refreshFormatter();
    }

    public synchronized MathContext getMathContext() {
        return this.exportedProperties.getMathContext();
    }

    public synchronized void setMathContext(MathContext mathContext) {
        this.properties.setMathContext(mathContext);
        refreshFormatter();
    }

    public synchronized ohos.global.icu.math.MathContext getMathContextICU() {
        MathContext mathContext;
        mathContext = getMathContext();
        return new ohos.global.icu.math.MathContext(mathContext.getPrecision(), this.icuMathContextForm, false, mathContext.getRoundingMode().ordinal());
    }

    public synchronized void setMathContextICU(ohos.global.icu.math.MathContext mathContext) {
        MathContext mathContext2;
        this.icuMathContextForm = mathContext.getForm();
        if (mathContext.getLostDigits()) {
            mathContext2 = new MathContext(mathContext.getDigits(), RoundingMode.UNNECESSARY);
        } else {
            mathContext2 = new MathContext(mathContext.getDigits(), RoundingMode.valueOf(mathContext.getRoundingMode()));
        }
        setMathContext(mathContext2);
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized int getMinimumIntegerDigits() {
        return this.exportedProperties.getMinimumIntegerDigits();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized void setMinimumIntegerDigits(int i) {
        int maximumIntegerDigits = this.properties.getMaximumIntegerDigits();
        if (maximumIntegerDigits >= 0 && maximumIntegerDigits < i) {
            this.properties.setMaximumIntegerDigits(i);
        }
        this.properties.setMinimumIntegerDigits(i);
        refreshFormatter();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized int getMaximumIntegerDigits() {
        return this.exportedProperties.getMaximumIntegerDigits();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized void setMaximumIntegerDigits(int i) {
        int minimumIntegerDigits = this.properties.getMinimumIntegerDigits();
        if (minimumIntegerDigits >= 0 && minimumIntegerDigits > i) {
            this.properties.setMinimumIntegerDigits(i);
        }
        this.properties.setMaximumIntegerDigits(i);
        refreshFormatter();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized int getMinimumFractionDigits() {
        return this.exportedProperties.getMinimumFractionDigits();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized void setMinimumFractionDigits(int i) {
        int maximumFractionDigits = this.properties.getMaximumFractionDigits();
        if (maximumFractionDigits >= 0 && maximumFractionDigits < i) {
            this.properties.setMaximumFractionDigits(i);
        }
        this.properties.setMinimumFractionDigits(i);
        refreshFormatter();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized int getMaximumFractionDigits() {
        return this.exportedProperties.getMaximumFractionDigits();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized void setMaximumFractionDigits(int i) {
        int minimumFractionDigits = this.properties.getMinimumFractionDigits();
        if (minimumFractionDigits >= 0 && minimumFractionDigits > i) {
            this.properties.setMinimumFractionDigits(i);
        }
        this.properties.setMaximumFractionDigits(i);
        refreshFormatter();
    }

    public synchronized boolean areSignificantDigitsUsed() {
        return (this.properties.getMinimumSignificantDigits() == -1 && this.properties.getMaximumSignificantDigits() == -1) ? false : true;
    }

    public synchronized void setSignificantDigitsUsed(boolean z) {
        int minimumSignificantDigits = this.properties.getMinimumSignificantDigits();
        int maximumSignificantDigits = this.properties.getMaximumSignificantDigits();
        int i = -1;
        if (z) {
            if (!(minimumSignificantDigits == -1 && maximumSignificantDigits == -1)) {
                return;
            }
        } else if (minimumSignificantDigits == -1 && maximumSignificantDigits == -1) {
            return;
        }
        int i2 = z ? 1 : -1;
        if (z) {
            i = 6;
        }
        this.properties.setMinimumSignificantDigits(i2);
        this.properties.setMaximumSignificantDigits(i);
        refreshFormatter();
    }

    public synchronized int getMinimumSignificantDigits() {
        return this.exportedProperties.getMinimumSignificantDigits();
    }

    public synchronized void setMinimumSignificantDigits(int i) {
        int maximumSignificantDigits = this.properties.getMaximumSignificantDigits();
        if (maximumSignificantDigits >= 0 && maximumSignificantDigits < i) {
            this.properties.setMaximumSignificantDigits(i);
        }
        this.properties.setMinimumSignificantDigits(i);
        refreshFormatter();
    }

    public synchronized int getMaximumSignificantDigits() {
        return this.exportedProperties.getMaximumSignificantDigits();
    }

    public synchronized void setMaximumSignificantDigits(int i) {
        int minimumSignificantDigits = this.properties.getMinimumSignificantDigits();
        if (minimumSignificantDigits >= 0 && minimumSignificantDigits > i) {
            this.properties.setMinimumSignificantDigits(i);
        }
        this.properties.setMaximumSignificantDigits(i);
        refreshFormatter();
    }

    public synchronized int getFormatWidth() {
        return this.properties.getFormatWidth();
    }

    public synchronized void setFormatWidth(int i) {
        this.properties.setFormatWidth(i);
        refreshFormatter();
    }

    public synchronized char getPadCharacter() {
        String padString = this.properties.getPadString();
        if (padString == null) {
            return " ".charAt(0);
        }
        return padString.charAt(0);
    }

    public synchronized void setPadCharacter(char c) {
        this.properties.setPadString(Character.toString(c));
        refreshFormatter();
    }

    public synchronized int getPadPosition() {
        int i;
        Padder.PadPosition padPosition = this.properties.getPadPosition();
        if (padPosition == null) {
            i = 0;
        } else {
            i = padPosition.toOld();
        }
        return i;
    }

    public synchronized void setPadPosition(int i) {
        this.properties.setPadPosition(Padder.PadPosition.fromOld(i));
        refreshFormatter();
    }

    public synchronized boolean isScientificNotation() {
        return this.properties.getMinimumExponentDigits() != -1;
    }

    public synchronized void setScientificNotation(boolean z) {
        if (z) {
            this.properties.setMinimumExponentDigits(1);
        } else {
            this.properties.setMinimumExponentDigits(-1);
        }
        refreshFormatter();
    }

    public synchronized byte getMinimumExponentDigits() {
        return (byte) this.properties.getMinimumExponentDigits();
    }

    public synchronized void setMinimumExponentDigits(byte b) {
        this.properties.setMinimumExponentDigits(b);
        refreshFormatter();
    }

    public synchronized boolean isExponentSignAlwaysShown() {
        return this.properties.getExponentSignAlwaysShown();
    }

    public synchronized void setExponentSignAlwaysShown(boolean z) {
        this.properties.setExponentSignAlwaysShown(z);
        refreshFormatter();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized boolean isGroupingUsed() {
        return this.properties.getGroupingUsed();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized void setGroupingUsed(boolean z) {
        this.properties.setGroupingUsed(z);
        refreshFormatter();
    }

    public synchronized int getGroupingSize() {
        if (this.properties.getGroupingSize() < 0) {
            return 0;
        }
        return this.properties.getGroupingSize();
    }

    public synchronized void setGroupingSize(int i) {
        this.properties.setGroupingSize(i);
        refreshFormatter();
    }

    public synchronized int getSecondaryGroupingSize() {
        int secondaryGroupingSize = this.properties.getSecondaryGroupingSize();
        if (secondaryGroupingSize < 0) {
            return 0;
        }
        return secondaryGroupingSize;
    }

    public synchronized void setSecondaryGroupingSize(int i) {
        this.properties.setSecondaryGroupingSize(i);
        refreshFormatter();
    }

    public synchronized int getMinimumGroupingDigits() {
        if (this.properties.getMinimumGroupingDigits() <= 0) {
            return 1;
        }
        return this.properties.getMinimumGroupingDigits();
    }

    public synchronized void setMinimumGroupingDigits(int i) {
        this.properties.setMinimumGroupingDigits(i);
        refreshFormatter();
    }

    public synchronized boolean isDecimalSeparatorAlwaysShown() {
        return this.properties.getDecimalSeparatorAlwaysShown();
    }

    public synchronized void setDecimalSeparatorAlwaysShown(boolean z) {
        this.properties.setDecimalSeparatorAlwaysShown(z);
        refreshFormatter();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized Currency getCurrency() {
        return this.exportedProperties.getCurrency();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized void setCurrency(Currency currency) {
        this.properties.setCurrency(currency);
        if (currency != null) {
            this.symbols.setCurrency(currency);
        }
        refreshFormatter();
    }

    public synchronized Currency.CurrencyUsage getCurrencyUsage() {
        Currency.CurrencyUsage currencyUsage;
        currencyUsage = this.properties.getCurrencyUsage();
        if (currencyUsage == null) {
            currencyUsage = Currency.CurrencyUsage.STANDARD;
        }
        return currencyUsage;
    }

    public synchronized void setCurrencyUsage(Currency.CurrencyUsage currencyUsage) {
        this.properties.setCurrencyUsage(currencyUsage);
        refreshFormatter();
    }

    public synchronized CurrencyPluralInfo getCurrencyPluralInfo() {
        return this.properties.getCurrencyPluralInfo();
    }

    public synchronized void setCurrencyPluralInfo(CurrencyPluralInfo currencyPluralInfo) {
        this.properties.setCurrencyPluralInfo(currencyPluralInfo);
        refreshFormatter();
    }

    public synchronized boolean isParseBigDecimal() {
        return this.properties.getParseToBigDecimal();
    }

    public synchronized void setParseBigDecimal(boolean z) {
        this.properties.setParseToBigDecimal(z);
        refreshFormatter();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized boolean isParseStrict() {
        return this.properties.getParseMode() == DecimalFormatProperties.ParseMode.STRICT;
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized void setParseStrict(boolean z) {
        this.properties.setParseMode(z ? DecimalFormatProperties.ParseMode.STRICT : DecimalFormatProperties.ParseMode.LENIENT);
        refreshFormatter();
    }

    @Deprecated
    public synchronized void setParseStrictMode(DecimalFormatProperties.ParseMode parseMode) {
        this.properties.setParseMode(parseMode);
        refreshFormatter();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized boolean isParseIntegerOnly() {
        return this.properties.getParseIntegerOnly();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized void setParseIntegerOnly(boolean z) {
        this.properties.setParseIntegerOnly(z);
        refreshFormatter();
    }

    public synchronized boolean isDecimalPatternMatchRequired() {
        return this.properties.getDecimalPatternMatchRequired();
    }

    public synchronized void setDecimalPatternMatchRequired(boolean z) {
        this.properties.setDecimalPatternMatchRequired(z);
        refreshFormatter();
    }

    public synchronized boolean isParseNoExponent() {
        return this.properties.getParseNoExponent();
    }

    public synchronized void setParseNoExponent(boolean z) {
        this.properties.setParseNoExponent(z);
        refreshFormatter();
    }

    public synchronized boolean isParseCaseSensitive() {
        return this.properties.getParseCaseSensitive();
    }

    public synchronized void setParseCaseSensitive(boolean z) {
        this.properties.setParseCaseSensitive(z);
        refreshFormatter();
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized boolean equals(Object obj) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DecimalFormat)) {
            return false;
        }
        DecimalFormat decimalFormat = (DecimalFormat) obj;
        if (this.properties.equals(decimalFormat.properties) && this.symbols.equals(decimalFormat.symbols)) {
            z = true;
        }
        return z;
    }

    @Override // ohos.global.icu.text.NumberFormat
    public synchronized int hashCode() {
        return this.properties.hashCode() ^ this.symbols.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append("@");
        sb.append(Integer.toHexString(hashCode()));
        sb.append(" { symbols@");
        sb.append(Integer.toHexString(this.symbols.hashCode()));
        synchronized (this) {
            this.properties.toStringBare(sb);
        }
        sb.append(" }");
        return sb.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x004c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String toPattern() {
        /*
        // Method dump skipped, instructions count: 112
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.DecimalFormat.toPattern():java.lang.String");
    }

    public synchronized String toLocalizedPattern() {
        return PatternStringUtils.convertLocalized(toPattern(), this.symbols, true);
    }

    public LocalizedNumberFormatter toNumberFormatter() {
        return this.formatter;
    }

    @Deprecated
    public PluralRules.IFixedDecimal getFixedDecimal(double d) {
        return this.formatter.format(d).getFixedDecimal();
    }

    /* access modifiers changed from: package-private */
    public void refreshFormatter() {
        if (this.exportedProperties != null) {
            ULocale locale = getLocale(ULocale.ACTUAL_LOCALE);
            if (locale == null) {
                locale = this.symbols.getLocale(ULocale.ACTUAL_LOCALE);
            }
            if (locale == null) {
                locale = this.symbols.getULocale();
            }
            this.formatter = NumberFormatter.fromDecimalFormat(this.properties, this.symbols, this.exportedProperties).locale(locale);
            this.parser = null;
            this.currencyParser = null;
        }
    }

    /* access modifiers changed from: package-private */
    public NumberParserImpl getParser() {
        if (this.parser == null) {
            this.parser = NumberParserImpl.createParserFromProperties(this.properties, this.symbols, false);
        }
        return this.parser;
    }

    /* access modifiers changed from: package-private */
    public NumberParserImpl getCurrencyParser() {
        if (this.currencyParser == null) {
            this.currencyParser = NumberParserImpl.createParserFromProperties(this.properties, this.symbols, true);
        }
        return this.currencyParser;
    }

    private Number safeConvertBigDecimal(BigDecimal bigDecimal) {
        try {
            return new ohos.global.icu.math.BigDecimal(bigDecimal);
        } catch (NumberFormatException unused) {
            if (bigDecimal.signum() > 0 && bigDecimal.scale() < 0) {
                return Double.valueOf(Double.POSITIVE_INFINITY);
            }
            if (bigDecimal.scale() < 0) {
                return Double.valueOf(Double.NEGATIVE_INFINITY);
            }
            if (bigDecimal.signum() < 0) {
                return Double.valueOf(-0.0d);
            }
            return Double.valueOf((double) XPath.MATCH_SCORE_QNAME);
        }
    }

    /* access modifiers changed from: package-private */
    public void setPropertiesFromPattern(String str, int i) {
        if (str != null) {
            PatternStringParser.parseToExistingProperties(str, this.properties, i);
            return;
        }
        throw new NullPointerException();
    }

    static void fieldPositionHelper(DecimalQuantity decimalQuantity, FormattedStringBuilder formattedStringBuilder, FieldPosition fieldPosition, int i) {
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(0);
        decimalQuantity.populateUFieldPosition(fieldPosition);
        if (FormattedValueStringBuilderImpl.nextFieldPosition(formattedStringBuilder, fieldPosition) && i != 0) {
            fieldPosition.setBeginIndex(fieldPosition.getBeginIndex() + i);
            fieldPosition.setEndIndex(fieldPosition.getEndIndex() + i);
        }
    }

    @Deprecated
    public synchronized void setProperties(PropertySetter propertySetter) {
        propertySetter.set(this.properties);
        refreshFormatter();
    }
}
