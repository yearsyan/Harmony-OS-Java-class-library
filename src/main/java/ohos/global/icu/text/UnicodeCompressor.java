package ohos.global.icu.text;

import ohos.global.icu.impl.Normalizer2Impl;

public final class UnicodeCompressor implements SCSU {
    private static boolean[] sSingleTagTable = {false, true, true, true, true, true, true, true, true, false, false, true, true, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    private static boolean[] sUnicodeTagTable = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false};
    private int fCurrentWindow = 0;
    private int[] fIndexCount = new int[256];
    private int fMode = 0;
    private int[] fOffsets = new int[8];
    private int fTimeStamp = 0;
    private int[] fTimeStamps = new int[8];

    private static boolean isCompressible(int i) {
        return i < 13312 || i >= 57344;
    }

    public UnicodeCompressor() {
        reset();
    }

    public static byte[] compress(String str) {
        return compress(str.toCharArray(), 0, str.length());
    }

    public static byte[] compress(char[] cArr, int i, int i2) {
        UnicodeCompressor unicodeCompressor = new UnicodeCompressor();
        int max = Math.max(4, ((i2 - i) * 3) + 1);
        byte[] bArr = new byte[max];
        int compress = unicodeCompressor.compress(cArr, i, i2, null, bArr, 0, max);
        byte[] bArr2 = new byte[compress];
        System.arraycopy(bArr, 0, bArr2, 0, compress);
        return bArr2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:105:0x01e4, code lost:
        if ((r5 + 3) < r24) goto L_0x01e8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x01e8, code lost:
        r6 = r5 + 1;
        r22[r5] = 15;
        r5 = r4 >>> '\b';
        r4 = r4 & 255;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x01f4, code lost:
        if (ohos.global.icu.text.UnicodeCompressor.sUnicodeTagTable[r5] == false) goto L_0x01fb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x01f6, code lost:
        r9 = r6 + 1;
        r22[r6] = -16;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x01fb, code lost:
        r9 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x01fc, code lost:
        r6 = r9 + 1;
        r22[r9] = (byte) r5;
        r5 = r6 + 1;
        r22[r6] = (byte) r4;
        r17.fMode = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x0208, code lost:
        r4 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x02ac, code lost:
        if ((r5 + 3) < r24) goto L_0x02b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x02b0, code lost:
        r6 = r5 + 1;
        r22[r5] = 15;
        r5 = r4 >>> '\b';
        r4 = r4 & 255;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:148:0x02bc, code lost:
        if (ohos.global.icu.text.UnicodeCompressor.sUnicodeTagTable[r5] == false) goto L_0x02c3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:149:0x02be, code lost:
        r9 = r6 + 1;
        r22[r6] = -16;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:150:0x02c3, code lost:
        r9 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x02c4, code lost:
        r6 = r9 + 1;
        r22[r9] = (byte) r5;
        r5 = r6 + 1;
        r22[r6] = (byte) r4;
        r17.fMode = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x011b, code lost:
        if ((r5 + 2) < r24) goto L_0x011e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x011e, code lost:
        r8 = getLRDefinedWindow();
        r10 = r5 + 1;
        r22[r5] = (byte) (r8 + 232);
        r5 = r10 + 1;
        r22[r10] = (byte) r13;
        r10 = r5 + 1;
        r22[r5] = (byte) ((r4 - ohos.global.icu.text.UnicodeCompressor.sOffsetTable[r13]) + 128);
        r17.fOffsets[r8] = ohos.global.icu.text.UnicodeCompressor.sOffsetTable[r13];
        r17.fCurrentWindow = r8;
        r4 = r17.fTimeStamps;
        r5 = r17.fTimeStamp + 1;
        r17.fTimeStamp = r5;
        r4[r8] = r5;
        r17.fMode = 0;
        r4 = r7;
        r5 = r10;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int compress(char[] r18, int r19, int r20, int[] r21, byte[] r22, int r23, int r24) {
        /*
        // Method dump skipped, instructions count: 797
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.UnicodeCompressor.compress(char[], int, int, int[], byte[], int, int):int");
    }

    public void reset() {
        int[] iArr = this.fOffsets;
        iArr[0] = 128;
        iArr[1] = 192;
        iArr[2] = 1024;
        iArr[3] = 1536;
        iArr[4] = 2304;
        iArr[5] = 12352;
        iArr[6] = 12448;
        iArr[7] = 65280;
        for (int i = 0; i < 8; i++) {
            this.fTimeStamps[i] = 0;
        }
        for (int i2 = 0; i2 <= 255; i2++) {
            this.fIndexCount[i2] = 0;
        }
        this.fTimeStamp = 0;
        this.fCurrentWindow = 0;
        this.fMode = 0;
    }

    private static int makeIndex(int i) {
        int i2;
        if (i >= 192 && i < 320) {
            return 249;
        }
        if (i >= 592 && i < 720) {
            return 250;
        }
        if (i >= 880 && i < 1008) {
            return 251;
        }
        if (i >= 1328 && i < 1424) {
            return 252;
        }
        if (i >= 12352 && i < 12448) {
            return 253;
        }
        if (i >= 12448 && i < 12576) {
            return 254;
        }
        if (i >= 65376 && i < 65439) {
            return 255;
        }
        if (i >= 128 && i < 13312) {
            i2 = i / 128;
        } else if (i < 57344 || i > 65535) {
            return 0;
        } else {
            i2 = (i - Normalizer2Impl.Hangul.HANGUL_BASE) / 128;
        }
        return i2 & 255;
    }

    private boolean inDynamicWindow(int i, int i2) {
        int[] iArr = this.fOffsets;
        return i >= iArr[i2] && i < iArr[i2] + 128;
    }

    private static boolean inStaticWindow(int i, int i2) {
        return i >= sOffsets[i2] && i < sOffsets[i2] + 128;
    }

    private int findDynamicWindow(int i) {
        for (int i2 = 7; i2 >= 0; i2--) {
            if (inDynamicWindow(i, i2)) {
                int[] iArr = this.fTimeStamps;
                iArr[i2] = iArr[i2] + 1;
                return i2;
            }
        }
        return -1;
    }

    private static int findStaticWindow(int i) {
        for (int i2 = 7; i2 >= 0; i2--) {
            if (inStaticWindow(i, i2)) {
                return i2;
            }
        }
        return -1;
    }

    private int getLRDefinedWindow() {
        int i = -1;
        int i2 = Integer.MAX_VALUE;
        for (int i3 = 7; i3 >= 0; i3--) {
            int[] iArr = this.fTimeStamps;
            if (iArr[i3] < i2) {
                i2 = iArr[i3];
                i = i3;
            }
        }
        return i;
    }
}
