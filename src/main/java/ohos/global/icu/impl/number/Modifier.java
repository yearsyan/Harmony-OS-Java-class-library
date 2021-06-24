package ohos.global.icu.impl.number;

import java.text.Format;
import ohos.global.icu.impl.FormattedStringBuilder;
import ohos.global.icu.impl.StandardPlural;

public interface Modifier {

    public static class Parameters {
        public ModifierStore obj;
        public StandardPlural plural;
        public Signum signum;
    }

    public enum Signum {
        NEG,
        NEG_ZERO,
        POS_ZERO,
        POS;
        
        static final int COUNT = values().length;
    }

    int apply(FormattedStringBuilder formattedStringBuilder, int i, int i2);

    boolean containsField(Format.Field field);

    int getCodePointCount();

    Parameters getParameters();

    int getPrefixLength();

    boolean isStrong();

    boolean semanticallyEquivalent(Modifier modifier);
}
