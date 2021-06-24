package ohos.global.icu.impl.number;

import java.text.Format;
import ohos.global.icu.impl.FormattedStringBuilder;
import ohos.global.icu.impl.StandardPlural;
import ohos.global.icu.impl.number.AffixUtils;
import ohos.global.icu.impl.number.Modifier;
import ohos.global.icu.number.NumberFormatter;
import ohos.global.icu.text.DecimalFormatSymbols;
import ohos.global.icu.text.NumberFormat;
import ohos.global.icu.text.PluralRules;
import ohos.global.icu.util.Currency;

public class MutablePatternModifier implements Modifier, AffixUtils.SymbolProvider, MicroPropsGenerator {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    Currency currency;
    StringBuilder currentAffix;
    NumberFormat.Field field;
    final boolean isStrong;
    MicroPropsGenerator parent;
    AffixPatternProvider patternInfo;
    boolean perMilleReplacesPercent;
    StandardPlural plural;
    PluralRules rules;
    NumberFormatter.SignDisplay signDisplay;
    Modifier.Signum signum;
    DecimalFormatSymbols symbols;
    NumberFormatter.UnitWidth unitWidth;

    @Override // ohos.global.icu.impl.number.Modifier
    public boolean containsField(Format.Field field2) {
        return false;
    }

    @Override // ohos.global.icu.impl.number.Modifier
    public Modifier.Parameters getParameters() {
        return null;
    }

    @Override // ohos.global.icu.impl.number.Modifier
    public boolean semanticallyEquivalent(Modifier modifier) {
        return false;
    }

    public MutablePatternModifier(boolean z) {
        this.isStrong = z;
    }

    public void setPatternInfo(AffixPatternProvider affixPatternProvider, NumberFormat.Field field2) {
        this.patternInfo = affixPatternProvider;
        this.field = field2;
    }

    public void setPatternAttributes(NumberFormatter.SignDisplay signDisplay2, boolean z) {
        this.signDisplay = signDisplay2;
        this.perMilleReplacesPercent = z;
    }

    public void setSymbols(DecimalFormatSymbols decimalFormatSymbols, Currency currency2, NumberFormatter.UnitWidth unitWidth2, PluralRules pluralRules) {
        this.symbols = decimalFormatSymbols;
        this.currency = currency2;
        this.unitWidth = unitWidth2;
        this.rules = pluralRules;
    }

    public void setNumberProperties(Modifier.Signum signum2, StandardPlural standardPlural) {
        this.signum = signum2;
        this.plural = standardPlural;
    }

    public boolean needsPlurals() {
        return this.patternInfo.containsSymbolType(-7);
    }

    public ImmutablePatternModifier createImmutable() {
        FormattedStringBuilder formattedStringBuilder = new FormattedStringBuilder();
        FormattedStringBuilder formattedStringBuilder2 = new FormattedStringBuilder();
        if (needsPlurals()) {
            AdoptingModifierStore adoptingModifierStore = new AdoptingModifierStore();
            for (StandardPlural standardPlural : StandardPlural.VALUES) {
                setNumberProperties(Modifier.Signum.POS, standardPlural);
                adoptingModifierStore.setModifier(Modifier.Signum.POS, standardPlural, createConstantModifier(formattedStringBuilder, formattedStringBuilder2));
                setNumberProperties(Modifier.Signum.POS_ZERO, standardPlural);
                adoptingModifierStore.setModifier(Modifier.Signum.POS_ZERO, standardPlural, createConstantModifier(formattedStringBuilder, formattedStringBuilder2));
                setNumberProperties(Modifier.Signum.NEG_ZERO, standardPlural);
                adoptingModifierStore.setModifier(Modifier.Signum.NEG_ZERO, standardPlural, createConstantModifier(formattedStringBuilder, formattedStringBuilder2));
                setNumberProperties(Modifier.Signum.NEG, standardPlural);
                adoptingModifierStore.setModifier(Modifier.Signum.NEG, standardPlural, createConstantModifier(formattedStringBuilder, formattedStringBuilder2));
            }
            adoptingModifierStore.freeze();
            return new ImmutablePatternModifier(adoptingModifierStore, this.rules);
        }
        setNumberProperties(Modifier.Signum.POS, null);
        ConstantMultiFieldModifier createConstantModifier = createConstantModifier(formattedStringBuilder, formattedStringBuilder2);
        setNumberProperties(Modifier.Signum.POS_ZERO, null);
        ConstantMultiFieldModifier createConstantModifier2 = createConstantModifier(formattedStringBuilder, formattedStringBuilder2);
        setNumberProperties(Modifier.Signum.NEG_ZERO, null);
        ConstantMultiFieldModifier createConstantModifier3 = createConstantModifier(formattedStringBuilder, formattedStringBuilder2);
        setNumberProperties(Modifier.Signum.NEG, null);
        return new ImmutablePatternModifier(new AdoptingModifierStore(createConstantModifier, createConstantModifier2, createConstantModifier3, createConstantModifier(formattedStringBuilder, formattedStringBuilder2)), null);
    }

    private ConstantMultiFieldModifier createConstantModifier(FormattedStringBuilder formattedStringBuilder, FormattedStringBuilder formattedStringBuilder2) {
        insertPrefix(formattedStringBuilder.clear(), 0);
        insertSuffix(formattedStringBuilder2.clear(), 0);
        if (this.patternInfo.hasCurrencySign()) {
            return new CurrencySpacingEnabledModifier(formattedStringBuilder, formattedStringBuilder2, !this.patternInfo.hasBody(), this.isStrong, this.symbols);
        }
        return new ConstantMultiFieldModifier(formattedStringBuilder, formattedStringBuilder2, !this.patternInfo.hasBody(), this.isStrong);
    }

    public static class ImmutablePatternModifier implements MicroPropsGenerator {
        MicroPropsGenerator parent = null;
        final AdoptingModifierStore pm;
        final PluralRules rules;

        ImmutablePatternModifier(AdoptingModifierStore adoptingModifierStore, PluralRules pluralRules) {
            this.pm = adoptingModifierStore;
            this.rules = pluralRules;
        }

        public ImmutablePatternModifier addToChain(MicroPropsGenerator microPropsGenerator) {
            this.parent = microPropsGenerator;
            return this;
        }

        @Override // ohos.global.icu.impl.number.MicroPropsGenerator
        public MicroProps processQuantity(DecimalQuantity decimalQuantity) {
            MicroProps processQuantity = this.parent.processQuantity(decimalQuantity);
            if (processQuantity.rounder != null) {
                processQuantity.rounder.apply(decimalQuantity);
            }
            if (processQuantity.modMiddle != null) {
                return processQuantity;
            }
            applyToMicros(processQuantity, decimalQuantity);
            return processQuantity;
        }

        public void applyToMicros(MicroProps microProps, DecimalQuantity decimalQuantity) {
            if (this.rules == null) {
                microProps.modMiddle = this.pm.getModifierWithoutPlural(decimalQuantity.signum());
                return;
            }
            microProps.modMiddle = this.pm.getModifier(decimalQuantity.signum(), RoundingUtils.getPluralSafe(microProps.rounder, this.rules, decimalQuantity));
        }
    }

    public MicroPropsGenerator addToChain(MicroPropsGenerator microPropsGenerator) {
        this.parent = microPropsGenerator;
        return this;
    }

    @Override // ohos.global.icu.impl.number.MicroPropsGenerator
    public MicroProps processQuantity(DecimalQuantity decimalQuantity) {
        MicroProps processQuantity = this.parent.processQuantity(decimalQuantity);
        if (processQuantity.rounder != null) {
            processQuantity.rounder.apply(decimalQuantity);
        }
        if (processQuantity.modMiddle != null) {
            return processQuantity;
        }
        if (needsPlurals()) {
            setNumberProperties(decimalQuantity.signum(), RoundingUtils.getPluralSafe(processQuantity.rounder, this.rules, decimalQuantity));
        } else {
            setNumberProperties(decimalQuantity.signum(), null);
        }
        processQuantity.modMiddle = this;
        return processQuantity;
    }

    @Override // ohos.global.icu.impl.number.Modifier
    public int apply(FormattedStringBuilder formattedStringBuilder, int i, int i2) {
        int insertPrefix = insertPrefix(formattedStringBuilder, i);
        int i3 = i2 + insertPrefix;
        int insertSuffix = insertSuffix(formattedStringBuilder, i3);
        int splice = !this.patternInfo.hasBody() ? formattedStringBuilder.splice(i + insertPrefix, i3, "", 0, 0, null) : 0;
        CurrencySpacingEnabledModifier.applyCurrencySpacing(formattedStringBuilder, i, insertPrefix, i3 + splice, insertSuffix, this.symbols);
        return insertPrefix + splice + insertSuffix;
    }

    @Override // ohos.global.icu.impl.number.Modifier
    public int getPrefixLength() {
        prepareAffix(true);
        return AffixUtils.unescapedCount(this.currentAffix, true, this);
    }

    @Override // ohos.global.icu.impl.number.Modifier
    public int getCodePointCount() {
        prepareAffix(true);
        int unescapedCount = AffixUtils.unescapedCount(this.currentAffix, false, this);
        prepareAffix(false);
        return unescapedCount + AffixUtils.unescapedCount(this.currentAffix, false, this);
    }

    @Override // ohos.global.icu.impl.number.Modifier
    public boolean isStrong() {
        return this.isStrong;
    }

    private int insertPrefix(FormattedStringBuilder formattedStringBuilder, int i) {
        prepareAffix(true);
        return AffixUtils.unescape(this.currentAffix, formattedStringBuilder, i, this, this.field);
    }

    private int insertSuffix(FormattedStringBuilder formattedStringBuilder, int i) {
        prepareAffix(false);
        return AffixUtils.unescape(this.currentAffix, formattedStringBuilder, i, this, this.field);
    }

    private void prepareAffix(boolean z) {
        if (this.currentAffix == null) {
            this.currentAffix = new StringBuilder();
        }
        PatternStringUtils.patternInfoToStringBuilder(this.patternInfo, z, PatternStringUtils.resolveSignDisplay(this.signDisplay, this.signum), this.plural, this.perMilleReplacesPercent, this.currentAffix);
    }

    /* renamed from: ohos.global.icu.impl.number.MutablePatternModifier$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$number$NumberFormatter$UnitWidth = new int[NumberFormatter.UnitWidth.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                ohos.global.icu.number.NumberFormatter$UnitWidth[] r0 = ohos.global.icu.number.NumberFormatter.UnitWidth.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.global.icu.impl.number.MutablePatternModifier.AnonymousClass1.$SwitchMap$ohos$global$icu$number$NumberFormatter$UnitWidth = r0
                int[] r0 = ohos.global.icu.impl.number.MutablePatternModifier.AnonymousClass1.$SwitchMap$ohos$global$icu$number$NumberFormatter$UnitWidth     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.global.icu.number.NumberFormatter$UnitWidth r1 = ohos.global.icu.number.NumberFormatter.UnitWidth.SHORT     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.global.icu.impl.number.MutablePatternModifier.AnonymousClass1.$SwitchMap$ohos$global$icu$number$NumberFormatter$UnitWidth     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.global.icu.number.NumberFormatter$UnitWidth r1 = ohos.global.icu.number.NumberFormatter.UnitWidth.NARROW     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.global.icu.impl.number.MutablePatternModifier.AnonymousClass1.$SwitchMap$ohos$global$icu$number$NumberFormatter$UnitWidth     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.global.icu.number.NumberFormatter$UnitWidth r1 = ohos.global.icu.number.NumberFormatter.UnitWidth.FORMAL     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.global.icu.impl.number.MutablePatternModifier.AnonymousClass1.$SwitchMap$ohos$global$icu$number$NumberFormatter$UnitWidth     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.global.icu.number.NumberFormatter$UnitWidth r1 = ohos.global.icu.number.NumberFormatter.UnitWidth.VARIANT     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.number.MutablePatternModifier.AnonymousClass1.<clinit>():void");
        }
    }

    @Override // ohos.global.icu.impl.number.AffixUtils.SymbolProvider
    public CharSequence getSymbol(int i) {
        int i2 = 3;
        switch (i) {
            case -9:
                return this.currency.getName(this.symbols.getULocale(), 3, (boolean[]) null);
            case -8:
                return "�";
            case -7:
                return this.currency.getName(this.symbols.getULocale(), 2, this.plural.getKeyword(), (boolean[]) null);
            case -6:
                return this.currency.getCurrencyCode();
            case -5:
                if (this.unitWidth == NumberFormatter.UnitWidth.ISO_CODE) {
                    return this.currency.getCurrencyCode();
                }
                if (this.unitWidth == NumberFormatter.UnitWidth.HIDDEN) {
                    return "";
                }
                int i3 = AnonymousClass1.$SwitchMap$ohos$global$icu$number$NumberFormatter$UnitWidth[this.unitWidth.ordinal()];
                if (i3 == 1) {
                    i2 = 0;
                } else if (i3 != 2) {
                    if (i3 == 3) {
                        i2 = 4;
                    } else if (i3 == 4) {
                        i2 = 5;
                    } else {
                        throw new AssertionError();
                    }
                }
                return this.currency.getName(this.symbols.getULocale(), i2, (boolean[]) null);
            case -4:
                return this.symbols.getPerMillString();
            case -3:
                return this.symbols.getPercentString();
            case -2:
                return this.symbols.getPlusSignString();
            case -1:
                return this.symbols.getMinusSignString();
            default:
                throw new AssertionError();
        }
    }
}
