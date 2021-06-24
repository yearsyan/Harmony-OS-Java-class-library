package ohos.global.icu.number;

import java.util.MissingResourceException;
import ohos.global.icu.impl.FormattedStringBuilder;
import ohos.global.icu.impl.ICUData;
import ohos.global.icu.impl.ICUResourceBundle;
import ohos.global.icu.impl.SimpleFormatterImpl;
import ohos.global.icu.impl.UResource;
import ohos.global.icu.impl.number.DecimalQuantity;
import ohos.global.icu.impl.number.MacroProps;
import ohos.global.icu.impl.number.MicroProps;
import ohos.global.icu.impl.number.Modifier;
import ohos.global.icu.impl.number.SimpleModifier;
import ohos.global.icu.impl.number.range.RangeMacroProps;
import ohos.global.icu.impl.number.range.StandardPluralRanges;
import ohos.global.icu.number.NumberRangeFormatter;
import ohos.global.icu.util.ULocale;
import ohos.global.icu.util.UResourceBundle;

class NumberRangeFormatterImpl {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    SimpleModifier fApproximatelyModifier;
    final NumberRangeFormatter.RangeCollapse fCollapse;
    final NumberRangeFormatter.RangeIdentityFallback fIdentityFallback;
    final StandardPluralRanges fPluralRanges;
    String fRangePattern;
    final boolean fSameFormatters;
    final NumberFormatterImpl formatterImpl1;
    final NumberFormatterImpl formatterImpl2;

    /* access modifiers changed from: package-private */
    public int identity2d(NumberRangeFormatter.RangeIdentityFallback rangeIdentityFallback, NumberRangeFormatter.RangeIdentityResult rangeIdentityResult) {
        return rangeIdentityFallback.ordinal() | (rangeIdentityResult.ordinal() << 4);
    }

    /* access modifiers changed from: private */
    public static final class NumberRangeDataSink extends UResource.Sink {
        String approximatelyPattern;
        String rangePattern;
        StringBuilder sb;

        NumberRangeDataSink(StringBuilder sb2) {
            this.sb = sb2;
        }

        @Override // ohos.global.icu.impl.UResource.Sink
        public void put(UResource.Key key, UResource.Value value, boolean z) {
            UResource.Table table = value.getTable();
            for (int i = 0; table.getKeyAndValue(i, key, value); i++) {
                if (key.contentEquals("range") && !hasRangeData()) {
                    this.rangePattern = SimpleFormatterImpl.compileToStringMinMaxArguments(value.getString(), this.sb, 2, 2);
                }
                if (key.contentEquals("approximately") && !hasApproxData()) {
                    this.approximatelyPattern = SimpleFormatterImpl.compileToStringMinMaxArguments(value.getString(), this.sb, 1, 1);
                }
            }
        }

        private boolean hasRangeData() {
            return this.rangePattern != null;
        }

        private boolean hasApproxData() {
            return this.approximatelyPattern != null;
        }

        public boolean isComplete() {
            return hasRangeData() && hasApproxData();
        }

        public void fillInDefaults() {
            if (!hasRangeData()) {
                this.rangePattern = SimpleFormatterImpl.compileToStringMinMaxArguments("{0}–{1}", this.sb, 2, 2);
            }
            if (!hasApproxData()) {
                this.approximatelyPattern = SimpleFormatterImpl.compileToStringMinMaxArguments("~{0}", this.sb, 1, 1);
            }
        }
    }

    private static void getNumberRangeData(ULocale uLocale, String str, NumberRangeFormatterImpl numberRangeFormatterImpl) {
        StringBuilder sb = new StringBuilder();
        NumberRangeDataSink numberRangeDataSink = new NumberRangeDataSink(sb);
        ICUResourceBundle iCUResourceBundle = (ICUResourceBundle) UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, uLocale);
        sb.append("NumberElements/");
        sb.append(str);
        sb.append("/miscPatterns");
        try {
            iCUResourceBundle.getAllItemsWithFallback(sb.toString(), numberRangeDataSink);
        } catch (MissingResourceException unused) {
        }
        if (!numberRangeDataSink.isComplete()) {
            iCUResourceBundle.getAllItemsWithFallback("NumberElements/latn/miscPatterns", numberRangeDataSink);
        }
        numberRangeDataSink.fillInDefaults();
        numberRangeFormatterImpl.fRangePattern = numberRangeDataSink.rangePattern;
        numberRangeFormatterImpl.fApproximatelyModifier = new SimpleModifier(numberRangeDataSink.approximatelyPattern, null, false);
    }

    public NumberRangeFormatterImpl(RangeMacroProps rangeMacroProps) {
        MacroProps macroProps;
        MacroProps macroProps2;
        NumberRangeFormatter.RangeIdentityFallback rangeIdentityFallback;
        if (rangeMacroProps.formatter1 != null) {
            macroProps = rangeMacroProps.formatter1.resolve();
        } else {
            macroProps = NumberFormatter.withLocale(rangeMacroProps.loc).resolve();
        }
        this.formatterImpl1 = new NumberFormatterImpl(macroProps);
        if (rangeMacroProps.formatter2 != null) {
            macroProps2 = rangeMacroProps.formatter2.resolve();
        } else {
            macroProps2 = NumberFormatter.withLocale(rangeMacroProps.loc).resolve();
        }
        this.formatterImpl2 = new NumberFormatterImpl(macroProps2);
        this.fSameFormatters = rangeMacroProps.sameFormatters != 0;
        this.fCollapse = rangeMacroProps.collapse != null ? rangeMacroProps.collapse : NumberRangeFormatter.RangeCollapse.AUTO;
        if (rangeMacroProps.identityFallback != null) {
            rangeIdentityFallback = rangeMacroProps.identityFallback;
        } else {
            rangeIdentityFallback = NumberRangeFormatter.RangeIdentityFallback.APPROXIMATELY;
        }
        this.fIdentityFallback = rangeIdentityFallback;
        String str = this.formatterImpl1.getRawMicroProps().nsName;
        if (str == null || !str.equals(this.formatterImpl2.getRawMicroProps().nsName)) {
            throw new IllegalArgumentException("Both formatters must have same numbering system");
        }
        getNumberRangeData(rangeMacroProps.loc, str, this);
        this.fPluralRanges = new StandardPluralRanges(rangeMacroProps.loc);
    }

    public FormattedNumberRange format(DecimalQuantity decimalQuantity, DecimalQuantity decimalQuantity2, boolean z) {
        MicroProps microProps;
        NumberRangeFormatter.RangeIdentityResult rangeIdentityResult;
        FormattedStringBuilder formattedStringBuilder = new FormattedStringBuilder();
        MicroProps preProcess = this.formatterImpl1.preProcess(decimalQuantity);
        if (this.fSameFormatters) {
            microProps = this.formatterImpl1.preProcess(decimalQuantity2);
        } else {
            microProps = this.formatterImpl2.preProcess(decimalQuantity2);
        }
        if (!preProcess.modInner.semanticallyEquivalent(microProps.modInner) || !preProcess.modMiddle.semanticallyEquivalent(microProps.modMiddle) || !preProcess.modOuter.semanticallyEquivalent(microProps.modOuter)) {
            formatRange(decimalQuantity, decimalQuantity2, formattedStringBuilder, preProcess, microProps);
            return new FormattedNumberRange(formattedStringBuilder, decimalQuantity, decimalQuantity2, NumberRangeFormatter.RangeIdentityResult.NOT_EQUAL);
        }
        if (z) {
            rangeIdentityResult = NumberRangeFormatter.RangeIdentityResult.EQUAL_BEFORE_ROUNDING;
        } else if (decimalQuantity.equals(decimalQuantity2)) {
            rangeIdentityResult = NumberRangeFormatter.RangeIdentityResult.EQUAL_AFTER_ROUNDING;
        } else {
            rangeIdentityResult = NumberRangeFormatter.RangeIdentityResult.NOT_EQUAL;
        }
        int identity2d = identity2d(this.fIdentityFallback, rangeIdentityResult);
        if (!(identity2d == 0 || identity2d == 1)) {
            if (identity2d != 2) {
                if (identity2d != 3) {
                    switch (identity2d) {
                        case 16:
                            break;
                        case 17:
                        case 18:
                            break;
                        default:
                            switch (identity2d) {
                            }
                        case 19:
                            formatRange(decimalQuantity, decimalQuantity2, formattedStringBuilder, preProcess, microProps);
                            break;
                    }
                    return new FormattedNumberRange(formattedStringBuilder, decimalQuantity, decimalQuantity2, rangeIdentityResult);
                }
                formatRange(decimalQuantity, decimalQuantity2, formattedStringBuilder, preProcess, microProps);
                return new FormattedNumberRange(formattedStringBuilder, decimalQuantity, decimalQuantity2, rangeIdentityResult);
            }
            formatApproximately(decimalQuantity, decimalQuantity2, formattedStringBuilder, preProcess, microProps);
            return new FormattedNumberRange(formattedStringBuilder, decimalQuantity, decimalQuantity2, rangeIdentityResult);
        }
        formatSingleValue(decimalQuantity, decimalQuantity2, formattedStringBuilder, preProcess, microProps);
        return new FormattedNumberRange(formattedStringBuilder, decimalQuantity, decimalQuantity2, rangeIdentityResult);
    }

    private void formatSingleValue(DecimalQuantity decimalQuantity, DecimalQuantity decimalQuantity2, FormattedStringBuilder formattedStringBuilder, MicroProps microProps, MicroProps microProps2) {
        if (this.fSameFormatters) {
            NumberFormatterImpl.writeAffixes(microProps, formattedStringBuilder, 0, NumberFormatterImpl.writeNumber(microProps, decimalQuantity, formattedStringBuilder, 0));
        } else {
            formatRange(decimalQuantity, decimalQuantity2, formattedStringBuilder, microProps, microProps2);
        }
    }

    private void formatApproximately(DecimalQuantity decimalQuantity, DecimalQuantity decimalQuantity2, FormattedStringBuilder formattedStringBuilder, MicroProps microProps, MicroProps microProps2) {
        if (this.fSameFormatters) {
            int writeNumber = NumberFormatterImpl.writeNumber(microProps, decimalQuantity, formattedStringBuilder, 0);
            int apply = writeNumber + microProps.modInner.apply(formattedStringBuilder, 0, writeNumber);
            int apply2 = apply + microProps.modMiddle.apply(formattedStringBuilder, 0, apply);
            microProps.modOuter.apply(formattedStringBuilder, 0, apply2 + this.fApproximatelyModifier.apply(formattedStringBuilder, 0, apply2));
            return;
        }
        formatRange(decimalQuantity, decimalQuantity2, formattedStringBuilder, microProps, microProps2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.global.icu.number.NumberRangeFormatterImpl$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$number$NumberRangeFormatter$RangeCollapse = new int[NumberRangeFormatter.RangeCollapse.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                ohos.global.icu.number.NumberRangeFormatter$RangeCollapse[] r0 = ohos.global.icu.number.NumberRangeFormatter.RangeCollapse.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.global.icu.number.NumberRangeFormatterImpl.AnonymousClass1.$SwitchMap$ohos$global$icu$number$NumberRangeFormatter$RangeCollapse = r0
                int[] r0 = ohos.global.icu.number.NumberRangeFormatterImpl.AnonymousClass1.$SwitchMap$ohos$global$icu$number$NumberRangeFormatter$RangeCollapse     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.global.icu.number.NumberRangeFormatter$RangeCollapse r1 = ohos.global.icu.number.NumberRangeFormatter.RangeCollapse.ALL     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.global.icu.number.NumberRangeFormatterImpl.AnonymousClass1.$SwitchMap$ohos$global$icu$number$NumberRangeFormatter$RangeCollapse     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.global.icu.number.NumberRangeFormatter$RangeCollapse r1 = ohos.global.icu.number.NumberRangeFormatter.RangeCollapse.AUTO     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.global.icu.number.NumberRangeFormatterImpl.AnonymousClass1.$SwitchMap$ohos$global$icu$number$NumberRangeFormatter$RangeCollapse     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.global.icu.number.NumberRangeFormatter$RangeCollapse r1 = ohos.global.icu.number.NumberRangeFormatter.RangeCollapse.UNIT     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.number.NumberRangeFormatterImpl.AnonymousClass1.<clinit>():void");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0074  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00c9  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00f2  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x010c  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x014e  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0176  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0190  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void formatRange(ohos.global.icu.impl.number.DecimalQuantity r11, ohos.global.icu.impl.number.DecimalQuantity r12, ohos.global.icu.impl.FormattedStringBuilder r13, ohos.global.icu.impl.number.MicroProps r14, ohos.global.icu.impl.number.MicroProps r15) {
        /*
        // Method dump skipped, instructions count: 439
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.number.NumberRangeFormatterImpl.formatRange(ohos.global.icu.impl.number.DecimalQuantity, ohos.global.icu.impl.number.DecimalQuantity, ohos.global.icu.impl.FormattedStringBuilder, ohos.global.icu.impl.number.MicroProps, ohos.global.icu.impl.number.MicroProps):void");
    }

    /* access modifiers changed from: package-private */
    public Modifier resolveModifierPlurals(Modifier modifier, Modifier modifier2) {
        Modifier.Parameters parameters;
        Modifier.Parameters parameters2 = modifier.getParameters();
        if (parameters2 == null || (parameters = modifier2.getParameters()) == null) {
            return modifier;
        }
        return parameters2.obj.getModifier(parameters2.signum, this.fPluralRanges.resolve(parameters2.plural, parameters.plural));
    }
}
