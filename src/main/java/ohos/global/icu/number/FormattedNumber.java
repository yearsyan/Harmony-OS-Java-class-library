package ohos.global.icu.number;

import java.math.BigDecimal;
import java.text.AttributedCharacterIterator;
import ohos.global.icu.impl.FormattedStringBuilder;
import ohos.global.icu.impl.FormattedValueStringBuilderImpl;
import ohos.global.icu.impl.Utility;
import ohos.global.icu.impl.number.DecimalQuantity;
import ohos.global.icu.text.ConstrainedFieldPosition;
import ohos.global.icu.text.FormattedValue;
import ohos.global.icu.text.PluralRules;

public class FormattedNumber implements FormattedValue {
    final DecimalQuantity fq;
    final FormattedStringBuilder string;

    FormattedNumber(FormattedStringBuilder formattedStringBuilder, DecimalQuantity decimalQuantity) {
        this.string = formattedStringBuilder;
        this.fq = decimalQuantity;
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
        return FormattedValueStringBuilderImpl.nextPosition(this.string, constrainedFieldPosition, null);
    }

    @Override // ohos.global.icu.text.FormattedValue
    public AttributedCharacterIterator toCharacterIterator() {
        return FormattedValueStringBuilderImpl.toCharacterIterator(this.string, null);
    }

    public BigDecimal toBigDecimal() {
        return this.fq.toBigDecimal();
    }

    @Deprecated
    public PluralRules.IFixedDecimal getFixedDecimal() {
        return this.fq;
    }
}
