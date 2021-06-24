package ohos.global.icu.impl.coll;

public final class CollationFastLatin {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final int BAIL_OUT = 1;
    public static final int BAIL_OUT_RESULT = -2;
    static final int CASE_AND_TERTIARY_MASK = 31;
    static final int CASE_MASK = 24;
    static final int COMMON_SEC = 160;
    static final int COMMON_SEC_PLUS_OFFSET = 192;
    static final int COMMON_TER = 0;
    static final int COMMON_TER_PLUS_OFFSET = 32;
    static final int CONTRACTION = 1024;
    static final int CONTR_CHAR_MASK = 511;
    static final int CONTR_LENGTH_SHIFT = 9;
    static final int EOS = 2;
    static final int EXPANSION = 2048;
    static final int INDEX_MASK = 1023;
    public static final int LATIN_LIMIT = 384;
    public static final int LATIN_MAX = 383;
    static final int LATIN_MAX_UTF8_LEAD = 197;
    static final int LONG_INC = 8;
    static final int LONG_PRIMARY_MASK = 65528;
    static final int LOWER_CASE = 8;
    static final int MAX_LONG = 4088;
    static final int MAX_SEC_AFTER = 352;
    static final int MAX_SEC_BEFORE = 128;
    static final int MAX_SEC_HIGH = 992;
    static final int MAX_SHORT = 64512;
    static final int MAX_TER_AFTER = 7;
    static final int MERGE_WEIGHT = 3;
    static final int MIN_LONG = 3072;
    static final int MIN_SEC_AFTER = 192;
    static final int MIN_SEC_BEFORE = 0;
    static final int MIN_SEC_HIGH = 384;
    static final int MIN_SHORT = 4096;
    static final int NUM_FAST_CHARS = 448;
    static final int PUNCT_LIMIT = 8256;
    static final int PUNCT_START = 8192;
    static final int SECONDARY_MASK = 992;
    static final int SEC_INC = 32;
    static final int SEC_OFFSET = 32;
    static final int SHORT_INC = 1024;
    static final int SHORT_PRIMARY_MASK = 64512;
    static final int TERTIARY_MASK = 7;
    static final int TER_OFFSET = 32;
    static final int TWO_CASES_MASK = 1572888;
    static final int TWO_COMMON_SEC_PLUS_OFFSET = 12583104;
    static final int TWO_COMMON_TER_PLUS_OFFSET = 2097184;
    static final int TWO_LONG_PRIMARIES_MASK = -458760;
    static final int TWO_LOWER_CASES = 524296;
    static final int TWO_SECONDARIES_MASK = 65012704;
    static final int TWO_SEC_OFFSETS = 2097184;
    static final int TWO_SHORT_PRIMARIES_MASK = -67044352;
    static final int TWO_TERTIARIES_MASK = 458759;
    static final int TWO_TER_OFFSETS = 2097184;
    public static final int VERSION = 2;

    private static int getCases(int i, boolean z, int i2) {
        if (i2 > 65535) {
            int i3 = 65535 & i2;
            if (i3 >= 4096) {
                return (!z || (-67108864 & i2) != 0) ? i2 & TWO_CASES_MASK : i2 & 24;
            }
            if (i3 > i) {
                return TWO_LOWER_CASES;
            }
        } else if (i2 >= 4096) {
            int i4 = i2 & 24;
            if (!z && (i2 & 992) >= 384) {
                i4 |= 524288;
            }
            return i4;
        } else if (i2 > i) {
            return 8;
        } else {
            if (i2 < MIN_LONG) {
                return i2;
            }
        }
        return 0;
    }

    static int getCharIndex(char c) {
        if (c <= 383) {
            return c;
        }
        if (8192 > c || c >= PUNCT_LIMIT) {
            return -1;
        }
        return c - 7808;
    }

    private static int getPrimaries(int i, int i2) {
        int i3;
        int i4 = 65535 & i2;
        if (i4 >= 4096) {
            i3 = TWO_SHORT_PRIMARIES_MASK;
        } else if (i4 > i) {
            i3 = TWO_LONG_PRIMARIES_MASK;
        } else if (i4 >= MIN_LONG) {
            return 0;
        } else {
            return i2;
        }
        return i3 & i2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0013, code lost:
        if ((r4 & 992) >= 384) goto L_0x0026;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int getQuaternaries(int r3, int r4) {
        /*
            r0 = 64512(0xfc00, float:9.04E-41)
            r1 = -67044352(0xfffffffffc00fc00, float:-2.6789007E36)
            r2 = 65535(0xffff, float:9.1834E-41)
            if (r4 > r2) goto L_0x0022
            r2 = 4096(0x1000, float:5.74E-42)
            if (r4 < r2) goto L_0x0016
            r3 = r4 & 992(0x3e0, float:1.39E-42)
            r4 = 384(0x180, float:5.38E-43)
            if (r3 < r4) goto L_0x0018
            goto L_0x0026
        L_0x0016:
            if (r4 <= r3) goto L_0x001a
        L_0x0018:
            r4 = r0
            goto L_0x002c
        L_0x001a:
            r3 = 3072(0xc00, float:4.305E-42)
            if (r4 < r3) goto L_0x002c
            r3 = 65528(0xfff8, float:9.1824E-41)
            goto L_0x002b
        L_0x0022:
            r0 = r4 & r2
            if (r0 <= r3) goto L_0x0028
        L_0x0026:
            r4 = r1
            goto L_0x002c
        L_0x0028:
            r3 = -458760(0xfffffffffff8fff8, float:NaN)
        L_0x002b:
            r4 = r4 & r3
        L_0x002c:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.coll.CollationFastLatin.getQuaternaries(int, int):int");
    }

    private static int getSecondariesFromOneShortCE(int i) {
        int i2 = i & 992;
        return i2 < 384 ? i2 + 32 : ((i2 + 32) << 16) | 192;
    }

    private static int getTertiaries(int i, boolean z, int i2) {
        int i3;
        int i4;
        if (i2 > 65535) {
            int i5 = 65535 & i2;
            if (i5 >= 4096) {
                return (z ? 2031647 & i2 : i2 & TWO_TERTIARIES_MASK) + 2097184;
            } else if (i5 > i) {
                int i6 = (i2 & TWO_TERTIARIES_MASK) + 2097184;
                return z ? i6 | TWO_LOWER_CASES : i6;
            }
        } else if (i2 >= 4096) {
            if (z) {
                i3 = (i2 & 31) + 32;
                if ((i2 & 992) >= 384) {
                    i4 = 2621440;
                }
                return i3;
            }
            i3 = (i2 & 7) + 32;
            if ((i2 & 992) >= 384) {
                i4 = 2097152;
            }
            return i3;
            return i4 | i3;
        } else if (i2 > i) {
            int i7 = (i2 & 7) + 32;
            return z ? i7 | 8 : i7;
        } else if (i2 < MIN_LONG) {
            return i2;
        }
        return 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:49:0x0091  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00b7 A[LOOP:2: B:61:0x00b3->B:63:0x00b7, LOOP_END] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getOptions(ohos.global.icu.impl.coll.CollationData r21, ohos.global.icu.impl.coll.CollationSettings r22, char[] r23) {
        /*
        // Method dump skipped, instructions count: 195
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.coll.CollationFastLatin.getOptions(ohos.global.icu.impl.coll.CollationData, ohos.global.icu.impl.coll.CollationSettings, char[]):int");
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:156:0x020b */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:326:0x0246 */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:204:0x029f */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:338:0x02da */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:254:0x032c */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:350:0x0367 */
    /* JADX WARNING: Code restructure failed: missing block: B:128:0x01af, code lost:
        if (r8 != 2) goto L_0x01b2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:225:0x02e2, code lost:
        r13 = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00e7, code lost:
        if (r7 != 2) goto L_0x00ea;
     */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x015a  */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x01b6  */
    /* JADX WARNING: Removed duplicated region for block: B:306:0x01ae A[EDGE_INSN: B:306:0x01ae->B:127:0x01ae ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:311:0x01a5 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int compareUTF16(char[] r21, char[] r22, int r23, java.lang.CharSequence r24, java.lang.CharSequence r25, int r26) {
        /*
        // Method dump skipped, instructions count: 931
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.coll.CollationFastLatin.compareUTF16(char[], char[], int, java.lang.CharSequence, java.lang.CharSequence, int):int");
    }

    private static int lookup(char[] cArr, int i) {
        if (8192 <= i && i < PUNCT_LIMIT) {
            return cArr[(i - 8192) + 384];
        }
        if (i == 65534) {
            return 3;
        }
        return i == 65535 ? 64680 : 1;
    }

    private static long nextPair(char[] cArr, int i, int i2, CharSequence charSequence, int i3) {
        long j;
        int i4;
        if (i2 >= MIN_LONG || i2 < 1024) {
            return (long) i2;
        }
        if (i2 >= 2048) {
            int i5 = (i2 & 1023) + 448;
            return ((long) cArr[i5]) | (((long) cArr[i5 + 1]) << 16);
        }
        int i6 = (i2 & 1023) + 448;
        boolean z = false;
        if (i3 != charSequence.length()) {
            int charAt = charSequence.charAt(i3);
            if (charAt > 383) {
                if (8192 <= charAt && charAt < PUNCT_LIMIT) {
                    charAt = (charAt - 8192) + 384;
                } else if (charAt != 65534 && charAt != 65535) {
                    return 1;
                } else {
                    charAt = -1;
                }
            }
            char c = cArr[i6];
            int i7 = i6;
            do {
                i7 += c >> '\t';
                c = cArr[i7];
                i4 = c & 511;
            } while (i4 < charAt);
            if (i4 == charAt) {
                i6 = i7;
                z = true;
            }
        }
        int i8 = cArr[i6] >> '\t';
        if (i8 == 1) {
            return 1;
        }
        char c2 = cArr[i6 + 1];
        if (i8 == 2) {
            j = (long) c2;
        } else {
            j = (((long) cArr[i6 + 2]) << 16) | ((long) c2);
        }
        return z ? ~j : j;
    }

    private static int getSecondaries(int i, int i2) {
        if (i2 > 65535) {
            int i3 = 65535 & i2;
            if (i3 >= 4096) {
                return 2097184 + (TWO_SECONDARIES_MASK & i2);
            }
            if (i3 > i) {
                return TWO_COMMON_SEC_PLUS_OFFSET;
            }
        } else if (i2 >= 4096) {
            return getSecondariesFromOneShortCE(i2);
        } else {
            if (i2 > i) {
                return 192;
            }
            if (i2 < MIN_LONG) {
                return i2;
            }
        }
        return 0;
    }

    private CollationFastLatin() {
    }
}
