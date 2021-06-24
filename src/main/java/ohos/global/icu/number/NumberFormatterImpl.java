package ohos.global.icu.number;

import ohos.com.sun.org.apache.xalan.internal.templates.Constants;
import ohos.global.icu.impl.FormattedStringBuilder;
import ohos.global.icu.impl.StandardPlural;
import ohos.global.icu.impl.number.DecimalQuantity;
import ohos.global.icu.impl.number.DecimalQuantity_DualStorageBCD;
import ohos.global.icu.impl.number.MacroProps;
import ohos.global.icu.impl.number.MicroProps;
import ohos.global.icu.impl.number.MicroPropsGenerator;
import ohos.global.icu.number.NumberFormatter;
import ohos.global.icu.text.NumberFormat;
import ohos.global.icu.util.Currency;
import ohos.global.icu.util.MeasureUnit;
import ohos.usb.USBCore;

/* access modifiers changed from: package-private */
public class NumberFormatterImpl {
    private static final Currency DEFAULT_CURRENCY = Currency.getInstance("XXX");
    final MicroPropsGenerator microPropsGenerator;
    final MicroProps micros = new MicroProps(true);

    public NumberFormatterImpl(MacroProps macroProps) {
        this.microPropsGenerator = macrosToMicroGenerator(macroProps, this.micros, true);
    }

    public static int formatStatic(MacroProps macroProps, DecimalQuantity decimalQuantity, FormattedStringBuilder formattedStringBuilder) {
        MicroProps preProcessUnsafe = preProcessUnsafe(macroProps, decimalQuantity);
        int writeNumber = writeNumber(preProcessUnsafe, decimalQuantity, formattedStringBuilder, 0);
        return writeNumber + writeAffixes(preProcessUnsafe, formattedStringBuilder, 0, writeNumber);
    }

    public static int getPrefixSuffixStatic(MacroProps macroProps, byte b, StandardPlural standardPlural, FormattedStringBuilder formattedStringBuilder) {
        return getPrefixSuffixImpl(macrosToMicroGenerator(macroProps, new MicroProps(false), false), b, formattedStringBuilder);
    }

    public int format(DecimalQuantity decimalQuantity, FormattedStringBuilder formattedStringBuilder) {
        MicroProps preProcess = preProcess(decimalQuantity);
        int writeNumber = writeNumber(preProcess, decimalQuantity, formattedStringBuilder, 0);
        return writeNumber + writeAffixes(preProcess, formattedStringBuilder, 0, writeNumber);
    }

    public MicroProps preProcess(DecimalQuantity decimalQuantity) {
        MicroProps processQuantity = this.microPropsGenerator.processQuantity(decimalQuantity);
        if (processQuantity.integerWidth.maxInt == -1) {
            decimalQuantity.setMinInteger(processQuantity.integerWidth.minInt);
        } else {
            decimalQuantity.setMinInteger(processQuantity.integerWidth.minInt);
            decimalQuantity.applyMaxInteger(processQuantity.integerWidth.maxInt);
        }
        return processQuantity;
    }

    private static MicroProps preProcessUnsafe(MacroProps macroProps, DecimalQuantity decimalQuantity) {
        MicroProps processQuantity = macrosToMicroGenerator(macroProps, new MicroProps(false), false).processQuantity(decimalQuantity);
        if (processQuantity.integerWidth.maxInt == -1) {
            decimalQuantity.setMinInteger(processQuantity.integerWidth.minInt);
        } else {
            decimalQuantity.setMinInteger(processQuantity.integerWidth.minInt);
            decimalQuantity.applyMaxInteger(processQuantity.integerWidth.maxInt);
        }
        return processQuantity;
    }

    public int getPrefixSuffix(byte b, StandardPlural standardPlural, FormattedStringBuilder formattedStringBuilder) {
        return getPrefixSuffixImpl(this.microPropsGenerator, b, formattedStringBuilder);
    }

    private static int getPrefixSuffixImpl(MicroPropsGenerator microPropsGenerator2, byte b, FormattedStringBuilder formattedStringBuilder) {
        DecimalQuantity_DualStorageBCD decimalQuantity_DualStorageBCD = new DecimalQuantity_DualStorageBCD(0);
        if (b < 0) {
            decimalQuantity_DualStorageBCD.negate();
        }
        MicroProps processQuantity = microPropsGenerator2.processQuantity(decimalQuantity_DualStorageBCD);
        processQuantity.modMiddle.apply(formattedStringBuilder, 0, 0);
        return processQuantity.modMiddle.getPrefixLength();
    }

    public MicroProps getRawMicroProps() {
        return this.micros;
    }

    private static boolean unitIsCurrency(MeasureUnit measureUnit) {
        return measureUnit != null && "currency".equals(measureUnit.getType());
    }

    private static boolean unitIsNoUnit(MeasureUnit measureUnit) {
        return measureUnit == null || USBCore.USB_FUNC_NONE.equals(measureUnit.getType());
    }

    private static boolean unitIsPercent(MeasureUnit measureUnit) {
        return measureUnit != null && Constants.ATTRNAME_PERCENT.equals(measureUnit.getSubtype());
    }

    private static boolean unitIsPermille(MeasureUnit measureUnit) {
        return measureUnit != null && "permille".equals(measureUnit.getSubtype());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v13, types: [ohos.global.icu.impl.number.AffixPatternProvider] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static ohos.global.icu.impl.number.MicroPropsGenerator macrosToMicroGenerator(ohos.global.icu.impl.number.MacroProps r18, ohos.global.icu.impl.number.MicroProps r19, boolean r20) {
        /*
        // Method dump skipped, instructions count: 576
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.number.NumberFormatterImpl.macrosToMicroGenerator(ohos.global.icu.impl.number.MacroProps, ohos.global.icu.impl.number.MicroProps, boolean):ohos.global.icu.impl.number.MicroPropsGenerator");
    }

    public static int writeAffixes(MicroProps microProps, FormattedStringBuilder formattedStringBuilder, int i, int i2) {
        int apply = microProps.modInner.apply(formattedStringBuilder, i, i2);
        if (microProps.padding.isValid()) {
            microProps.padding.padAndApply(microProps.modMiddle, microProps.modOuter, formattedStringBuilder, i, i2 + apply);
            return apply;
        }
        int apply2 = apply + microProps.modMiddle.apply(formattedStringBuilder, i, i2 + apply);
        return apply2 + microProps.modOuter.apply(formattedStringBuilder, i, i2 + apply2);
    }

    public static int writeNumber(MicroProps microProps, DecimalQuantity decimalQuantity, FormattedStringBuilder formattedStringBuilder, int i) {
        int i2;
        String str;
        int insert;
        if (decimalQuantity.isInfinite()) {
            insert = formattedStringBuilder.insert(i + 0, microProps.symbols.getInfinity(), NumberFormat.Field.INTEGER);
        } else if (decimalQuantity.isNaN()) {
            insert = formattedStringBuilder.insert(i + 0, microProps.symbols.getNaN(), NumberFormat.Field.INTEGER);
        } else {
            int writeIntegerDigits = writeIntegerDigits(microProps, decimalQuantity, formattedStringBuilder, i + 0) + 0;
            if (decimalQuantity.getLowerDisplayMagnitude() < 0 || microProps.decimal == NumberFormatter.DecimalSeparatorDisplay.ALWAYS) {
                int i3 = writeIntegerDigits + i;
                if (microProps.useCurrency) {
                    str = microProps.symbols.getMonetaryDecimalSeparatorString();
                } else {
                    str = microProps.symbols.getDecimalSeparatorString();
                }
                writeIntegerDigits += formattedStringBuilder.insert(i3, str, NumberFormat.Field.DECIMAL_SEPARATOR);
            }
            int writeFractionDigits = writeFractionDigits(microProps, decimalQuantity, formattedStringBuilder, writeIntegerDigits + i) + writeIntegerDigits;
            if (writeFractionDigits != 0) {
                return writeFractionDigits;
            }
            if (microProps.symbols.getCodePointZero() != -1) {
                i2 = formattedStringBuilder.insertCodePoint(i, microProps.symbols.getCodePointZero(), NumberFormat.Field.INTEGER);
            } else {
                i2 = formattedStringBuilder.insert(i, microProps.symbols.getDigitStringsLocal()[0], NumberFormat.Field.INTEGER);
            }
            return i2 + writeFractionDigits;
        }
        return insert + 0;
    }

    private static int writeIntegerDigits(MicroProps microProps, DecimalQuantity decimalQuantity, FormattedStringBuilder formattedStringBuilder, int i) {
        int i2;
        String str;
        int upperDisplayMagnitude = decimalQuantity.getUpperDisplayMagnitude() + 1;
        int i3 = 0;
        for (int i4 = 0; i4 < upperDisplayMagnitude; i4++) {
            if (microProps.grouping.groupAtPosition(i4, decimalQuantity)) {
                if (microProps.useCurrency) {
                    str = microProps.symbols.getMonetaryGroupingSeparatorString();
                } else {
                    str = microProps.symbols.getGroupingSeparatorString();
                }
                i3 += formattedStringBuilder.insert(i, str, NumberFormat.Field.GROUPING_SEPARATOR);
            }
            byte digit = decimalQuantity.getDigit(i4);
            if (microProps.symbols.getCodePointZero() != -1) {
                i2 = formattedStringBuilder.insertCodePoint(i, microProps.symbols.getCodePointZero() + digit, NumberFormat.Field.INTEGER);
            } else {
                i2 = formattedStringBuilder.insert(i, microProps.symbols.getDigitStringsLocal()[digit], NumberFormat.Field.INTEGER);
            }
            i3 += i2;
        }
        return i3;
    }

    private static int writeFractionDigits(MicroProps microProps, DecimalQuantity decimalQuantity, FormattedStringBuilder formattedStringBuilder, int i) {
        int i2;
        int i3 = -decimalQuantity.getLowerDisplayMagnitude();
        int i4 = 0;
        for (int i5 = 0; i5 < i3; i5++) {
            byte digit = decimalQuantity.getDigit((-i5) - 1);
            if (microProps.symbols.getCodePointZero() != -1) {
                i2 = formattedStringBuilder.insertCodePoint(i4 + i, microProps.symbols.getCodePointZero() + digit, NumberFormat.Field.FRACTION);
            } else {
                i2 = formattedStringBuilder.insert(i4 + i, microProps.symbols.getDigitStringsLocal()[digit], NumberFormat.Field.FRACTION);
            }
            i4 += i2;
        }
        return i4;
    }
}
