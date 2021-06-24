package ohos.global.icu.text;

abstract class CharsetRecog_Unicode extends CharsetRecognizer {
    static int adjustConfidence(int i, int i2) {
        if (i == 0) {
            i2 -= 10;
        } else if ((i >= 32 && i <= 255) || i == 10) {
            i2 += 10;
        }
        if (i2 < 0) {
            return 0;
        }
        if (i2 > 100) {
            return 100;
        }
        return i2;
    }

    static int codeUnit16FromBytes(byte b, byte b2) {
        return ((b & 255) << 8) | (b2 & 255);
    }

    /* access modifiers changed from: package-private */
    @Override // ohos.global.icu.text.CharsetRecognizer
    public abstract String getName();

    /* access modifiers changed from: package-private */
    @Override // ohos.global.icu.text.CharsetRecognizer
    public abstract CharsetMatch match(CharsetDetector charsetDetector);

    CharsetRecog_Unicode() {
    }

    static class CharsetRecog_UTF_16_BE extends CharsetRecog_Unicode {
        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.text.CharsetRecognizer, ohos.global.icu.text.CharsetRecog_Unicode
        public String getName() {
            return "UTF-16BE";
        }

        CharsetRecog_UTF_16_BE() {
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.text.CharsetRecognizer, ohos.global.icu.text.CharsetRecog_Unicode
        public CharsetMatch match(CharsetDetector charsetDetector) {
            byte[] bArr = charsetDetector.fRawInput;
            int min = Math.min(bArr.length, 30);
            int i = 0;
            int i2 = 10;
            int i3 = 0;
            while (true) {
                if (i3 >= min - 1) {
                    break;
                }
                int codeUnit16FromBytes = codeUnit16FromBytes(bArr[i3], bArr[i3 + 1]);
                if (i3 != 0 || codeUnit16FromBytes != 65279) {
                    i2 = adjustConfidence(codeUnit16FromBytes, i2);
                    if (i2 == 0 || i2 == 100) {
                        break;
                    }
                    i3 += 2;
                } else {
                    i2 = 100;
                    break;
                }
            }
            if (min >= 4 || i2 >= 100) {
                i = i2;
            }
            if (i > 0) {
                return new CharsetMatch(charsetDetector, this, i);
            }
            return null;
        }
    }

    static class CharsetRecog_UTF_16_LE extends CharsetRecog_Unicode {
        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.text.CharsetRecognizer, ohos.global.icu.text.CharsetRecog_Unicode
        public String getName() {
            return "UTF-16LE";
        }

        CharsetRecog_UTF_16_LE() {
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.text.CharsetRecognizer, ohos.global.icu.text.CharsetRecog_Unicode
        public CharsetMatch match(CharsetDetector charsetDetector) {
            byte[] bArr = charsetDetector.fRawInput;
            int min = Math.min(bArr.length, 30);
            int i = 0;
            int i2 = 10;
            int i3 = 0;
            while (true) {
                if (i3 >= min - 1) {
                    break;
                }
                int codeUnit16FromBytes = codeUnit16FromBytes(bArr[i3 + 1], bArr[i3]);
                if (i3 != 0 || codeUnit16FromBytes != 65279) {
                    i2 = adjustConfidence(codeUnit16FromBytes, i2);
                    if (i2 == 0 || i2 == 100) {
                        break;
                    }
                    i3 += 2;
                } else {
                    i2 = 100;
                    break;
                }
            }
            if (min >= 4 || i2 >= 100) {
                i = i2;
            }
            if (i > 0) {
                return new CharsetMatch(charsetDetector, this, i);
            }
            return null;
        }
    }

    static abstract class CharsetRecog_UTF_32 extends CharsetRecog_Unicode {
        /* access modifiers changed from: package-private */
        public abstract int getChar(byte[] bArr, int i);

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.text.CharsetRecognizer, ohos.global.icu.text.CharsetRecog_Unicode
        public abstract String getName();

        CharsetRecog_UTF_32() {
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:37:0x0062  */
        /* JADX WARNING: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
        @Override // ohos.global.icu.text.CharsetRecognizer, ohos.global.icu.text.CharsetRecog_Unicode
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public ohos.global.icu.text.CharsetMatch match(ohos.global.icu.text.CharsetDetector r11) {
            /*
            // Method dump skipped, instructions count: 104
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.CharsetRecog_Unicode.CharsetRecog_UTF_32.match(ohos.global.icu.text.CharsetDetector):ohos.global.icu.text.CharsetMatch");
        }
    }

    static class CharsetRecog_UTF_32_BE extends CharsetRecog_UTF_32 {
        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.text.CharsetRecognizer, ohos.global.icu.text.CharsetRecog_Unicode.CharsetRecog_UTF_32, ohos.global.icu.text.CharsetRecog_Unicode
        public String getName() {
            return "UTF-32BE";
        }

        CharsetRecog_UTF_32_BE() {
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.text.CharsetRecog_Unicode.CharsetRecog_UTF_32
        public int getChar(byte[] bArr, int i) {
            return ((bArr[i + 0] & 255) << 24) | ((bArr[i + 1] & 255) << 16) | ((bArr[i + 2] & 255) << 8) | (bArr[i + 3] & 255);
        }
    }

    static class CharsetRecog_UTF_32_LE extends CharsetRecog_UTF_32 {
        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.text.CharsetRecognizer, ohos.global.icu.text.CharsetRecog_Unicode.CharsetRecog_UTF_32, ohos.global.icu.text.CharsetRecog_Unicode
        public String getName() {
            return "UTF-32LE";
        }

        CharsetRecog_UTF_32_LE() {
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.text.CharsetRecog_Unicode.CharsetRecog_UTF_32
        public int getChar(byte[] bArr, int i) {
            return ((bArr[i + 3] & 255) << 24) | ((bArr[i + 2] & 255) << 16) | ((bArr[i + 1] & 255) << 8) | (bArr[i + 0] & 255);
        }
    }
}
