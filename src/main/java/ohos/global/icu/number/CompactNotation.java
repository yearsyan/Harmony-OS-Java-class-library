package ohos.global.icu.number;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import ohos.global.icu.impl.number.CompactData;
import ohos.global.icu.impl.number.DecimalQuantity;
import ohos.global.icu.impl.number.MicroProps;
import ohos.global.icu.impl.number.MicroPropsGenerator;
import ohos.global.icu.impl.number.MutablePatternModifier;
import ohos.global.icu.impl.number.PatternStringParser;
import ohos.global.icu.text.CompactDecimalFormat;
import ohos.global.icu.text.NumberFormat;
import ohos.global.icu.text.PluralRules;
import ohos.global.icu.util.ULocale;

public class CompactNotation extends Notation {
    final Map<String, Map<String, String>> compactCustomData;
    final CompactDecimalFormat.CompactStyle compactStyle;

    @Deprecated
    public static CompactNotation forCustomData(Map<String, Map<String, String>> map) {
        return new CompactNotation(map);
    }

    CompactNotation(CompactDecimalFormat.CompactStyle compactStyle2) {
        this.compactCustomData = null;
        this.compactStyle = compactStyle2;
    }

    CompactNotation(Map<String, Map<String, String>> map) {
        this.compactStyle = null;
        this.compactCustomData = map;
    }

    /* access modifiers changed from: package-private */
    public MicroPropsGenerator withLocaleData(ULocale uLocale, String str, CompactData.CompactType compactType, PluralRules pluralRules, MutablePatternModifier mutablePatternModifier, boolean z, MicroPropsGenerator microPropsGenerator) {
        return new CompactHandler(uLocale, str, compactType, pluralRules, mutablePatternModifier, z, microPropsGenerator);
    }

    private static class CompactHandler implements MicroPropsGenerator {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        final CompactData data;
        final MicroPropsGenerator parent;
        final Map<String, MutablePatternModifier.ImmutablePatternModifier> precomputedMods;
        final PluralRules rules;
        final MutablePatternModifier unsafePatternModifier;

        private CompactHandler(CompactNotation compactNotation, ULocale uLocale, String str, CompactData.CompactType compactType, PluralRules pluralRules, MutablePatternModifier mutablePatternModifier, boolean z, MicroPropsGenerator microPropsGenerator) {
            this.rules = pluralRules;
            this.parent = microPropsGenerator;
            this.data = new CompactData();
            if (compactNotation.compactStyle != null) {
                this.data.populate(uLocale, str, compactNotation.compactStyle, compactType);
            } else {
                this.data.populate(compactNotation.compactCustomData);
            }
            if (z) {
                this.precomputedMods = new HashMap();
                precomputeAllModifiers(mutablePatternModifier);
                this.unsafePatternModifier = null;
                return;
            }
            this.precomputedMods = null;
            this.unsafePatternModifier = mutablePatternModifier;
        }

        private void precomputeAllModifiers(MutablePatternModifier mutablePatternModifier) {
            HashSet<String> hashSet = new HashSet();
            this.data.getUniquePatterns(hashSet);
            for (String str : hashSet) {
                mutablePatternModifier.setPatternInfo(PatternStringParser.parseToPatternInfo(str), NumberFormat.Field.COMPACT);
                this.precomputedMods.put(str, mutablePatternModifier.createImmutable());
            }
        }

        @Override // ohos.global.icu.impl.number.MicroPropsGenerator
        public MicroProps processQuantity(DecimalQuantity decimalQuantity) {
            int i;
            MicroProps processQuantity = this.parent.processQuantity(decimalQuantity);
            int i2 = 0;
            if (decimalQuantity.isZeroish()) {
                processQuantity.rounder.apply(decimalQuantity);
                i = 0;
            } else {
                i = processQuantity.rounder.chooseMultiplierAndApply(decimalQuantity, this.data);
                if (!decimalQuantity.isZeroish()) {
                    i2 = decimalQuantity.getMagnitude();
                }
                i2 -= i;
            }
            String pattern = this.data.getPattern(i2, decimalQuantity.getStandardPlural(this.rules));
            if (pattern != null) {
                Map<String, MutablePatternModifier.ImmutablePatternModifier> map = this.precomputedMods;
                if (map != null) {
                    map.get(pattern).applyToMicros(processQuantity, decimalQuantity);
                } else {
                    this.unsafePatternModifier.setPatternInfo(PatternStringParser.parseToPatternInfo(pattern), NumberFormat.Field.COMPACT);
                    this.unsafePatternModifier.setNumberProperties(decimalQuantity.signum(), null);
                    processQuantity.modMiddle = this.unsafePatternModifier;
                }
            }
            decimalQuantity.adjustExponent(i * -1);
            processQuantity.rounder = null;
            return processQuantity;
        }
    }
}
