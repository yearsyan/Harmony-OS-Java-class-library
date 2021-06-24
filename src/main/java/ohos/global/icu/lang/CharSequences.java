package ohos.global.icu.lang;

import ohos.global.icu.impl.UCharacterProperty;
import ohos.global.icu.text.UTF16;

@Deprecated
public class CharSequences {
    @Deprecated
    public static int matchAfter(CharSequence charSequence, CharSequence charSequence2, int i, int i2) {
        int length = charSequence.length();
        int length2 = charSequence2.length();
        int i3 = i2;
        int i4 = i;
        while (i4 < length && i3 < length2 && charSequence.charAt(i4) == charSequence2.charAt(i3)) {
            i4++;
            i3++;
        }
        int i5 = i4 - i;
        return (i5 == 0 || onCharacterBoundary(charSequence, i4) || onCharacterBoundary(charSequence2, i3)) ? i5 : i5 - 1;
    }

    @Deprecated
    public int codePointLength(CharSequence charSequence) {
        return Character.codePointCount(charSequence, 0, charSequence.length());
    }

    @Deprecated
    public static final boolean equals(int i, CharSequence charSequence) {
        if (charSequence == null) {
            return false;
        }
        int length = charSequence.length();
        return length != 1 ? length == 2 && i > 65535 && i == Character.codePointAt(charSequence, 0) : i == charSequence.charAt(0);
    }

    @Deprecated
    public static final boolean equals(CharSequence charSequence, int i) {
        return equals(i, charSequence);
    }

    @Deprecated
    public static int compare(CharSequence charSequence, int i) {
        int charAt;
        if (i < 0 || i > 1114111) {
            throw new IllegalArgumentException();
        }
        int length = charSequence.length();
        if (length == 0) {
            return -1;
        }
        char charAt2 = charSequence.charAt(0);
        int i2 = i - 65536;
        if (i2 < 0) {
            int i3 = charAt2 - i;
            return i3 != 0 ? i3 : length - 1;
        }
        int i4 = charAt2 - ((char) ((i2 >>> 10) + 55296));
        if (i4 != 0) {
            return i4;
        }
        return (length <= 1 || (charAt = charSequence.charAt(1) - ((char) ((i2 & UCharacterProperty.MAX_SCRIPT) + UTF16.TRAIL_SURROGATE_MIN_VALUE))) == 0) ? length - 2 : charAt;
    }

    @Deprecated
    public static int compare(int i, CharSequence charSequence) {
        int compare = compare(charSequence, i);
        if (compare > 0) {
            return -1;
        }
        return compare < 0 ? 1 : 0;
    }

    @Deprecated
    public static int getSingleCodePoint(CharSequence charSequence) {
        int length = charSequence.length();
        boolean z = true;
        if (length < 1 || length > 2) {
            return Integer.MAX_VALUE;
        }
        int codePointAt = Character.codePointAt(charSequence, 0);
        boolean z2 = codePointAt < 65536;
        if (length != 1) {
            z = false;
        }
        if (z2 == z) {
            return codePointAt;
        }
        return Integer.MAX_VALUE;
    }

    @Deprecated
    public static final <T> boolean equals(T t, T t2) {
        if (t == null) {
            return t2 == null;
        }
        if (t2 == null) {
            return false;
        }
        return t.equals(t2);
    }

    @Deprecated
    public static int compare(CharSequence charSequence, CharSequence charSequence2) {
        int length = charSequence.length();
        int length2 = charSequence2.length();
        int i = length <= length2 ? length : length2;
        for (int i2 = 0; i2 < i; i2++) {
            int charAt = charSequence.charAt(i2) - charSequence2.charAt(i2);
            if (charAt != 0) {
                return charAt;
            }
        }
        return length - length2;
    }

    @Deprecated
    public static boolean equalsChars(CharSequence charSequence, CharSequence charSequence2) {
        return charSequence.length() == charSequence2.length() && compare(charSequence, charSequence2) == 0;
    }

    @Deprecated
    public static boolean onCharacterBoundary(CharSequence charSequence, int i) {
        return i <= 0 || i >= charSequence.length() || !Character.isHighSurrogate(charSequence.charAt(i + -1)) || !Character.isLowSurrogate(charSequence.charAt(i));
    }

    @Deprecated
    public static int indexOf(CharSequence charSequence, int i) {
        int i2 = 0;
        while (i2 < charSequence.length()) {
            int codePointAt = Character.codePointAt(charSequence, i2);
            if (codePointAt == i) {
                return i2;
            }
            i2 += Character.charCount(codePointAt);
        }
        return -1;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r0v1, resolved type: int[] */
    /* JADX DEBUG: Multi-variable search result rejected for r6v0, resolved type: int */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v1, types: [char] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @java.lang.Deprecated
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int[] codePoints(java.lang.CharSequence r8) {
        /*
            int r0 = r8.length()
            int[] r0 = new int[r0]
            r1 = 0
            r2 = r1
            r3 = r2
        L_0x0009:
            int r4 = r8.length()
            if (r2 >= r4) goto L_0x003d
            char r4 = r8.charAt(r2)
            r5 = 56320(0xdc00, float:7.8921E-41)
            if (r4 < r5) goto L_0x0035
            r5 = 57343(0xdfff, float:8.0355E-41)
            if (r4 > r5) goto L_0x0035
            if (r2 == 0) goto L_0x0035
            int r5 = r3 + -1
            r6 = r0[r5]
            char r6 = (char) r6
            r7 = 55296(0xd800, float:7.7486E-41)
            if (r6 < r7) goto L_0x0035
            r7 = 56319(0xdbff, float:7.892E-41)
            if (r6 > r7) goto L_0x0035
            int r4 = java.lang.Character.toCodePoint(r6, r4)
            r0[r5] = r4
            goto L_0x003a
        L_0x0035:
            int r5 = r3 + 1
            r0[r3] = r4
            r3 = r5
        L_0x003a:
            int r2 = r2 + 1
            goto L_0x0009
        L_0x003d:
            int r8 = r0.length
            if (r3 != r8) goto L_0x0041
            return r0
        L_0x0041:
            int[] r8 = new int[r3]
            java.lang.System.arraycopy(r0, r1, r8, r1, r3)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.lang.CharSequences.codePoints(java.lang.CharSequence):int[]");
    }

    private CharSequences() {
    }
}
