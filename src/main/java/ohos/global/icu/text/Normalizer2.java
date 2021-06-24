package ohos.global.icu.text;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import ohos.global.icu.impl.ICUBinary;
import ohos.global.icu.impl.Norm2AllModes;
import ohos.global.icu.text.Normalizer;
import ohos.global.icu.util.ICUUncheckedIOException;

public abstract class Normalizer2 {

    public enum Mode {
        COMPOSE,
        DECOMPOSE,
        FCD,
        COMPOSE_CONTIGUOUS
    }

    public abstract StringBuilder append(StringBuilder sb, CharSequence charSequence);

    public int composePair(int i, int i2) {
        return -1;
    }

    public int getCombiningClass(int i) {
        return 0;
    }

    public abstract String getDecomposition(int i);

    public String getRawDecomposition(int i) {
        return null;
    }

    public abstract boolean hasBoundaryAfter(int i);

    public abstract boolean hasBoundaryBefore(int i);

    public abstract boolean isInert(int i);

    public abstract boolean isNormalized(CharSequence charSequence);

    public abstract Appendable normalize(CharSequence charSequence, Appendable appendable);

    public abstract StringBuilder normalize(CharSequence charSequence, StringBuilder sb);

    public abstract StringBuilder normalizeSecondAndAppend(StringBuilder sb, CharSequence charSequence);

    public abstract Normalizer.QuickCheckResult quickCheck(CharSequence charSequence);

    public abstract int spanQuickCheckYes(CharSequence charSequence);

    public static Normalizer2 getNFCInstance() {
        return Norm2AllModes.getNFCInstance().comp;
    }

    public static Normalizer2 getNFDInstance() {
        return Norm2AllModes.getNFCInstance().decomp;
    }

    public static Normalizer2 getNFKCInstance() {
        return Norm2AllModes.getNFKCInstance().comp;
    }

    public static Normalizer2 getNFKDInstance() {
        return Norm2AllModes.getNFKCInstance().decomp;
    }

    public static Normalizer2 getNFKCCasefoldInstance() {
        return Norm2AllModes.getNFKC_CFInstance().comp;
    }

    public static Normalizer2 getInstance(InputStream inputStream, String str, Mode mode) {
        ByteBuffer byteBuffer;
        if (inputStream != null) {
            try {
                byteBuffer = ICUBinary.getByteBufferFromInputStreamAndCloseStream(inputStream);
            } catch (IOException e) {
                throw new ICUUncheckedIOException(e);
            }
        } else {
            byteBuffer = null;
        }
        Norm2AllModes instance = Norm2AllModes.getInstance(byteBuffer, str);
        int i = AnonymousClass1.$SwitchMap$ohos$global$icu$text$Normalizer2$Mode[mode.ordinal()];
        if (i == 1) {
            return instance.comp;
        }
        if (i == 2) {
            return instance.decomp;
        }
        if (i == 3) {
            return instance.fcd;
        }
        if (i != 4) {
            return null;
        }
        return instance.fcc;
    }

    /* renamed from: ohos.global.icu.text.Normalizer2$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$text$Normalizer2$Mode = new int[Mode.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                ohos.global.icu.text.Normalizer2$Mode[] r0 = ohos.global.icu.text.Normalizer2.Mode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.global.icu.text.Normalizer2.AnonymousClass1.$SwitchMap$ohos$global$icu$text$Normalizer2$Mode = r0
                int[] r0 = ohos.global.icu.text.Normalizer2.AnonymousClass1.$SwitchMap$ohos$global$icu$text$Normalizer2$Mode     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.global.icu.text.Normalizer2$Mode r1 = ohos.global.icu.text.Normalizer2.Mode.COMPOSE     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.global.icu.text.Normalizer2.AnonymousClass1.$SwitchMap$ohos$global$icu$text$Normalizer2$Mode     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.global.icu.text.Normalizer2$Mode r1 = ohos.global.icu.text.Normalizer2.Mode.DECOMPOSE     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.global.icu.text.Normalizer2.AnonymousClass1.$SwitchMap$ohos$global$icu$text$Normalizer2$Mode     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.global.icu.text.Normalizer2$Mode r1 = ohos.global.icu.text.Normalizer2.Mode.FCD     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.global.icu.text.Normalizer2.AnonymousClass1.$SwitchMap$ohos$global$icu$text$Normalizer2$Mode     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.global.icu.text.Normalizer2$Mode r1 = ohos.global.icu.text.Normalizer2.Mode.COMPOSE_CONTIGUOUS     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.Normalizer2.AnonymousClass1.<clinit>():void");
        }
    }

    public String normalize(CharSequence charSequence) {
        if (charSequence instanceof String) {
            int spanQuickCheckYes = spanQuickCheckYes(charSequence);
            if (spanQuickCheckYes == charSequence.length()) {
                return (String) charSequence;
            }
            if (spanQuickCheckYes != 0) {
                StringBuilder sb = new StringBuilder(charSequence.length());
                sb.append(charSequence, 0, spanQuickCheckYes);
                return normalizeSecondAndAppend(sb, charSequence.subSequence(spanQuickCheckYes, charSequence.length())).toString();
            }
        }
        return normalize(charSequence, new StringBuilder(charSequence.length())).toString();
    }

    @Deprecated
    protected Normalizer2() {
    }
}
