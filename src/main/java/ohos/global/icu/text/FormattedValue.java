package ohos.global.icu.text;

import java.text.AttributedCharacterIterator;

public interface FormattedValue extends CharSequence {
    <A extends Appendable> A appendTo(A a);

    boolean nextPosition(ConstrainedFieldPosition constrainedFieldPosition);

    AttributedCharacterIterator toCharacterIterator();

    String toString();
}
