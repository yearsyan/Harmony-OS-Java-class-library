package ohos.global.icu.impl.number;

import ohos.global.icu.impl.FormattedStringBuilder;

public class Padder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String FALLBACK_PADDING_STRING = " ";
    public static final Padder NONE = new Padder(null, -1, null);
    String paddingString;
    PadPosition position;
    int targetWidth;

    public enum PadPosition {
        BEFORE_PREFIX,
        AFTER_PREFIX,
        BEFORE_SUFFIX,
        AFTER_SUFFIX;

        public static PadPosition fromOld(int i) {
            if (i == 0) {
                return BEFORE_PREFIX;
            }
            if (i == 1) {
                return AFTER_PREFIX;
            }
            if (i == 2) {
                return BEFORE_SUFFIX;
            }
            if (i == 3) {
                return AFTER_SUFFIX;
            }
            throw new IllegalArgumentException("Don't know how to map " + i);
        }

        public int toOld() {
            int i = AnonymousClass1.$SwitchMap$ohos$global$icu$impl$number$Padder$PadPosition[ordinal()];
            if (i == 1) {
                return 0;
            }
            if (i == 2) {
                return 1;
            }
            if (i != 3) {
                return i != 4 ? -1 : 3;
            }
            return 2;
        }
    }

    /* renamed from: ohos.global.icu.impl.number.Padder$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$impl$number$Padder$PadPosition = new int[PadPosition.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                ohos.global.icu.impl.number.Padder$PadPosition[] r0 = ohos.global.icu.impl.number.Padder.PadPosition.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.global.icu.impl.number.Padder.AnonymousClass1.$SwitchMap$ohos$global$icu$impl$number$Padder$PadPosition = r0
                int[] r0 = ohos.global.icu.impl.number.Padder.AnonymousClass1.$SwitchMap$ohos$global$icu$impl$number$Padder$PadPosition     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.global.icu.impl.number.Padder$PadPosition r1 = ohos.global.icu.impl.number.Padder.PadPosition.BEFORE_PREFIX     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.global.icu.impl.number.Padder.AnonymousClass1.$SwitchMap$ohos$global$icu$impl$number$Padder$PadPosition     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.global.icu.impl.number.Padder$PadPosition r1 = ohos.global.icu.impl.number.Padder.PadPosition.AFTER_PREFIX     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.global.icu.impl.number.Padder.AnonymousClass1.$SwitchMap$ohos$global$icu$impl$number$Padder$PadPosition     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.global.icu.impl.number.Padder$PadPosition r1 = ohos.global.icu.impl.number.Padder.PadPosition.BEFORE_SUFFIX     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.global.icu.impl.number.Padder.AnonymousClass1.$SwitchMap$ohos$global$icu$impl$number$Padder$PadPosition     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.global.icu.impl.number.Padder$PadPosition r1 = ohos.global.icu.impl.number.Padder.PadPosition.AFTER_SUFFIX     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.number.Padder.AnonymousClass1.<clinit>():void");
        }
    }

    public Padder(String str, int i, PadPosition padPosition) {
        this.paddingString = str == null ? " " : str;
        this.targetWidth = i;
        this.position = padPosition == null ? PadPosition.BEFORE_PREFIX : padPosition;
    }

    public static Padder none() {
        return NONE;
    }

    public static Padder codePoints(int i, int i2, PadPosition padPosition) {
        if (i2 >= 0) {
            return new Padder(String.valueOf(Character.toChars(i)), i2, padPosition);
        }
        throw new IllegalArgumentException("Padding width must not be negative");
    }

    public static Padder forProperties(DecimalFormatProperties decimalFormatProperties) {
        return new Padder(decimalFormatProperties.getPadString(), decimalFormatProperties.getFormatWidth(), decimalFormatProperties.getPadPosition());
    }

    public boolean isValid() {
        return this.targetWidth > 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0059  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int padAndApply(ohos.global.icu.impl.number.Modifier r5, ohos.global.icu.impl.number.Modifier r6, ohos.global.icu.impl.FormattedStringBuilder r7, int r8, int r9) {
        /*
        // Method dump skipped, instructions count: 104
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.number.Padder.padAndApply(ohos.global.icu.impl.number.Modifier, ohos.global.icu.impl.number.Modifier, ohos.global.icu.impl.FormattedStringBuilder, int, int):int");
    }

    private static int addPaddingHelper(String str, int i, FormattedStringBuilder formattedStringBuilder, int i2) {
        for (int i3 = 0; i3 < i; i3++) {
            formattedStringBuilder.insert(i2, str, (Object) null);
        }
        return str.length() * i;
    }
}
