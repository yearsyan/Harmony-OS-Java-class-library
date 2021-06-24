package ohos.global.icu.impl.number;

import java.text.Format;
import ohos.global.icu.impl.FormattedStringBuilder;
import ohos.global.icu.impl.SimpleFormatterImpl;
import ohos.global.icu.impl.number.Modifier;
import ohos.global.icu.impl.number.range.PrefixInfixSuffixLengthHelper;
import ohos.global.icu.util.ICUException;

public class SimpleModifier implements Modifier {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ARG_NUM_LIMIT = 256;
    private final String compiledPattern;
    private final Format.Field field;
    private final Modifier.Parameters parameters;
    private final boolean strong;

    @Override // ohos.global.icu.impl.number.Modifier
    public boolean containsField(Format.Field field2) {
        return false;
    }

    public SimpleModifier(String str, Format.Field field2, boolean z) {
        this(str, field2, z, null);
    }

    public SimpleModifier(String str, Format.Field field2, boolean z, Modifier.Parameters parameters2) {
        this.compiledPattern = str;
        this.field = field2;
        this.strong = z;
        this.parameters = parameters2;
    }

    @Override // ohos.global.icu.impl.number.Modifier
    public int apply(FormattedStringBuilder formattedStringBuilder, int i, int i2) {
        return SimpleFormatterImpl.formatPrefixSuffix(this.compiledPattern, this.field, i, i2, formattedStringBuilder);
    }

    @Override // ohos.global.icu.impl.number.Modifier
    public int getPrefixLength() {
        return SimpleFormatterImpl.getPrefixLength(this.compiledPattern);
    }

    @Override // ohos.global.icu.impl.number.Modifier
    public int getCodePointCount() {
        return SimpleFormatterImpl.getLength(this.compiledPattern, true);
    }

    @Override // ohos.global.icu.impl.number.Modifier
    public boolean isStrong() {
        return this.strong;
    }

    @Override // ohos.global.icu.impl.number.Modifier
    public Modifier.Parameters getParameters() {
        return this.parameters;
    }

    @Override // ohos.global.icu.impl.number.Modifier
    public boolean semanticallyEquivalent(Modifier modifier) {
        if (!(modifier instanceof SimpleModifier)) {
            return false;
        }
        SimpleModifier simpleModifier = (SimpleModifier) modifier;
        Modifier.Parameters parameters2 = this.parameters;
        if (parameters2 != null && simpleModifier.parameters != null && parameters2.obj == simpleModifier.parameters.obj) {
            return true;
        }
        if (this.compiledPattern.equals(simpleModifier.compiledPattern) && this.field == simpleModifier.field && this.strong == simpleModifier.strong) {
            return true;
        }
        return false;
    }

    public static void formatTwoArgPattern(String str, FormattedStringBuilder formattedStringBuilder, int i, PrefixInfixSuffixLengthHelper prefixInfixSuffixLengthHelper, Format.Field field2) {
        int i2;
        int i3;
        int i4;
        if (SimpleFormatterImpl.getArgumentLimit(str) == 2) {
            char charAt = str.charAt(1);
            int i5 = 2;
            int i6 = 0;
            if (charAt < 256) {
                i3 = 0;
                i2 = 0;
            } else {
                int i7 = charAt - 256;
                int i8 = 2 + i7;
                formattedStringBuilder.insert(i + 0, str, 2, i8, field2);
                i5 = i8 + 1;
                i2 = i7;
                i3 = i7 + 0;
            }
            char charAt2 = str.charAt(i5);
            int i9 = i5 + 1;
            if (charAt2 < 256) {
                i4 = 0;
            } else {
                i4 = charAt2 - 256;
                int i10 = i9 + i4;
                formattedStringBuilder.insert(i + i3, str, i9, i10, field2);
                i3 += i4;
                i9 = i10 + 1;
            }
            if (i9 != str.length()) {
                i6 = str.charAt(i9) - 256;
                int i11 = i9 + 1;
                formattedStringBuilder.insert(i + i3, str, i11, i11 + i6, field2);
            }
            prefixInfixSuffixLengthHelper.lengthPrefix = i2;
            prefixInfixSuffixLengthHelper.lengthInfix = i4;
            prefixInfixSuffixLengthHelper.lengthSuffix = i6;
            return;
        }
        throw new ICUException();
    }
}
