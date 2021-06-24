package ohos.global.icu.number;

import ohos.global.icu.impl.number.DecimalFormatProperties;
import ohos.global.icu.impl.number.PatternStringParser;
import ohos.global.icu.text.DecimalFormatSymbols;

/* access modifiers changed from: package-private */
public final class NumberPropertyMapper {
    NumberPropertyMapper() {
    }

    public static UnlocalizedNumberFormatter create(DecimalFormatProperties decimalFormatProperties, DecimalFormatSymbols decimalFormatSymbols) {
        return (UnlocalizedNumberFormatter) NumberFormatter.with().macros(oldToNew(decimalFormatProperties, decimalFormatSymbols, null));
    }

    public static UnlocalizedNumberFormatter create(DecimalFormatProperties decimalFormatProperties, DecimalFormatSymbols decimalFormatSymbols, DecimalFormatProperties decimalFormatProperties2) {
        return (UnlocalizedNumberFormatter) NumberFormatter.with().macros(oldToNew(decimalFormatProperties, decimalFormatSymbols, decimalFormatProperties2));
    }

    public static UnlocalizedNumberFormatter create(String str, DecimalFormatSymbols decimalFormatSymbols) {
        return create(PatternStringParser.parseToProperties(str), decimalFormatSymbols);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:68:0x00f5, code lost:
        if (r8 > 999) goto L_0x00ef;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x012a, code lost:
        if (r13 > r3) goto L_0x0124;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static ohos.global.icu.impl.number.MacroProps oldToNew(ohos.global.icu.impl.number.DecimalFormatProperties r19, ohos.global.icu.text.DecimalFormatSymbols r20, ohos.global.icu.impl.number.DecimalFormatProperties r21) {
        /*
        // Method dump skipped, instructions count: 677
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.number.NumberPropertyMapper.oldToNew(ohos.global.icu.impl.number.DecimalFormatProperties, ohos.global.icu.text.DecimalFormatSymbols, ohos.global.icu.impl.number.DecimalFormatProperties):ohos.global.icu.impl.number.MacroProps");
    }
}
