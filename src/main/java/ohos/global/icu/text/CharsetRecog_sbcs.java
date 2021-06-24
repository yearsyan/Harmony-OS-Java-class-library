package ohos.global.icu.text;

import ohos.data.search.model.IndexType;
import ohos.media.camera.params.adapter.camera2ex.CameraMetadataEx;
import ohos.nfc.tag.MifareClassicTag;

abstract class CharsetRecog_sbcs extends CharsetRecognizer {
    /* access modifiers changed from: package-private */
    @Override // ohos.global.icu.text.CharsetRecognizer
    public abstract String getName();

    CharsetRecog_sbcs() {
    }

    /* access modifiers changed from: package-private */
    public static class NGramParser {
        private static final int N_GRAM_MASK = 16777215;
        protected int byteIndex = 0;
        protected byte[] byteMap;
        private int hitCount;
        private int ngram = 0;
        private int ngramCount;
        private int[] ngramList;
        protected byte spaceChar;

        public NGramParser(int[] iArr, byte[] bArr) {
            this.ngramList = iArr;
            this.byteMap = bArr;
            this.ngram = 0;
            this.hitCount = 0;
            this.ngramCount = 0;
        }

        private static int search(int[] iArr, int i) {
            int i2 = 32;
            if (iArr[32] > i) {
                i2 = 0;
            }
            int i3 = i2 + 16;
            if (iArr[i3] <= i) {
                i2 = i3;
            }
            int i4 = i2 + 8;
            if (iArr[i4] <= i) {
                i2 = i4;
            }
            int i5 = i2 + 4;
            if (iArr[i5] <= i) {
                i2 = i5;
            }
            int i6 = i2 + 2;
            if (iArr[i6] <= i) {
                i2 = i6;
            }
            int i7 = i2 + 1;
            if (iArr[i7] <= i) {
                i2 = i7;
            }
            if (iArr[i2] > i) {
                i2--;
            }
            if (i2 < 0 || iArr[i2] != i) {
                return -1;
            }
            return i2;
        }

        private void lookup(int i) {
            this.ngramCount++;
            if (search(this.ngramList, i) >= 0) {
                this.hitCount++;
            }
        }

        /* access modifiers changed from: protected */
        public void addByte(int i) {
            this.ngram = 16777215 & ((this.ngram << 8) + (i & 255));
            lookup(this.ngram);
        }

        private int nextByte(CharsetDetector charsetDetector) {
            if (this.byteIndex >= charsetDetector.fInputLen) {
                return -1;
            }
            byte[] bArr = charsetDetector.fInputBytes;
            int i = this.byteIndex;
            this.byteIndex = i + 1;
            return bArr[i] & 255;
        }

        /* access modifiers changed from: protected */
        public void parseCharacters(CharsetDetector charsetDetector) {
            while (true) {
                boolean z = false;
                while (true) {
                    int nextByte = nextByte(charsetDetector);
                    if (nextByte >= 0) {
                        byte b = this.byteMap[nextByte];
                        if (b != 0) {
                            if (b != this.spaceChar || !z) {
                                addByte(b);
                            }
                            if (b == this.spaceChar) {
                                z = true;
                            }
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        public int parse(CharsetDetector charsetDetector) {
            return parse(charsetDetector, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE);
        }

        public int parse(CharsetDetector charsetDetector, byte b) {
            this.spaceChar = b;
            parseCharacters(charsetDetector);
            addByte(this.spaceChar);
            double d = ((double) this.hitCount) / ((double) this.ngramCount);
            if (d > 0.33d) {
                return 98;
            }
            return (int) (d * 300.0d);
        }
    }

    static class NGramParser_IBM420 extends NGramParser {
        protected static byte[] unshapeMap = {64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 66, 66, 68, 69, 70, 71, 71, 73, 74, 75, 76, 77, 78, 79, 80, 73, 82, 83, 84, 85, 86, 86, 88, 88, 90, 91, 92, 93, 94, 95, MifareClassicTag.USE_KEY_A, MifareClassicTag.USE_KEY_B, 98, 99, 99, 101, 101, 103, 103, 105, 106, 107, 108, 109, 110, 111, 105, 113, 113, 115, 116, 117, 118, 119, 119, 121, 122, 123, 124, Bidi.MAX_EXPLICIT_LEVEL, Bidi.LEVEL_DEFAULT_LTR, Bidi.LEVEL_DEFAULT_RTL, Bidi.LEVEL_OVERRIDE, -127, -126, -125, -124, -123, -122, -121, -120, -119, Bidi.LEVEL_OVERRIDE, -117, -117, -115, -115, -113, -112, -111, -110, -109, -108, -107, -106, -105, -104, -103, -102, -102, -102, -102, -98, -98, -98, -95, -94, -93, -92, -91, -90, -89, -88, -87, -98, -85, -85, -83, -83, -81, -81, -79, -78, -77, -76, -75, -74, -73, -72, -71, -79, -69, -69, -67, -67, -65, -64, -63, -62, -61, -60, -59, -58, -57, -56, -55, -54, -65, -52, -65, -50, -49, -48, -47, -46, -45, -44, -43, -42, -41, -40, -39, -38, -38, -36, -36, -36, -33, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -5, -4, -3, -2, -1};
        private byte alef = 0;

        private byte isLamAlef(byte b) {
            if (b == -78 || b == -77) {
                return 71;
            }
            if (b == -76 || b == -75) {
                return 73;
            }
            return (b == -72 || b == -71) ? (byte) 86 : 0;
        }

        public NGramParser_IBM420(int[] iArr, byte[] bArr) {
            super(iArr, bArr);
        }

        private int nextByte(CharsetDetector charsetDetector) {
            int i;
            if (this.byteIndex >= charsetDetector.fInputLen || charsetDetector.fInputBytes[this.byteIndex] == 0) {
                return -1;
            }
            this.alef = isLamAlef(charsetDetector.fInputBytes[this.byteIndex]);
            if (this.alef != 0) {
                i = 177;
            } else {
                i = unshapeMap[charsetDetector.fInputBytes[this.byteIndex] & 255] & 255;
            }
            this.byteIndex++;
            return i;
        }

        /* access modifiers changed from: protected */
        @Override // ohos.global.icu.text.CharsetRecog_sbcs.NGramParser
        public void parseCharacters(CharsetDetector charsetDetector) {
            byte b;
            boolean z = false;
            while (true) {
                int nextByte = nextByte(charsetDetector);
                if (nextByte >= 0) {
                    byte b2 = this.byteMap[nextByte];
                    boolean z2 = true;
                    if (b2 != 0) {
                        if (b2 != this.spaceChar || !z) {
                            addByte(b2);
                        }
                        z = b2 == this.spaceChar;
                    }
                    if (!(this.alef == 0 || (b = this.byteMap[this.alef & 255]) == 0)) {
                        if (b != this.spaceChar || !z) {
                            addByte(b);
                        }
                        if (b != this.spaceChar) {
                            z2 = false;
                        }
                        z = z2;
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int match(CharsetDetector charsetDetector, int[] iArr, byte[] bArr) {
        return match(charsetDetector, iArr, bArr, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE);
    }

    /* access modifiers changed from: package-private */
    public int match(CharsetDetector charsetDetector, int[] iArr, byte[] bArr, byte b) {
        return new NGramParser(iArr, bArr).parse(charsetDetector, b);
    }

    /* access modifiers changed from: package-private */
    public int matchIBM420(CharsetDetector charsetDetector, int[] iArr, byte[] bArr, byte b) {
        return new NGramParser_IBM420(iArr, bArr).parse(charsetDetector, b);
    }

    static class NGramsPlusLang {
        String fLang;
        int[] fNGrams;

        NGramsPlusLang(String str, int[] iArr) {
            this.fLang = str;
            this.fNGrams = iArr;
        }
    }

    static class CharsetRecog_8859_1 extends CharsetRecog_sbcs {
        protected static byte[] byteMap = {CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, 0, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -86, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -75, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -70, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, -11, -10, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -8, -7, -6, -5, -4, -3, -2, -33, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, -11, -10, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -8, -7, -6, -5, -4, -3, -2, -1};
        private static NGramsPlusLang[] ngrams_8859_1 = {new NGramsPlusLang("da", new int[]{2122086, 2122100, 2122853, 2123118, 2123122, 2123375, 2123873, 2124064, 2125157, 2125671, 2126053, 2126697, 2126708, 2126953, 2127465, 6383136, 6385184, 6385252, 6386208, 6386720, 6579488, 6579566, 6579570, 6579572, 6627443, 6644768, 6644837, 6647328, 6647396, 6648352, 6648421, 6648608, 6648864, 6713202, 6776096, 6776174, 6776178, 6907749, 6908960, 6909543, 7038240, 7039845, 7103858, 7104871, 7105637, 7169380, 7234661, 7234848, 7235360, 7235429, 7300896, 7302432, 7303712, 7398688, 7479396, 7479397, 7479411, 7496992, 7566437, 7610483, 7628064, 7628146, 7629164, 7759218}), new NGramsPlusLang("de", new int[]{2122094, 2122101, 2122341, 2122849, 2122853, 2122857, 2123113, 2123621, 2123873, 2124142, 2125161, 2126691, 2126693, 2127214, 2127461, 2127471, 2127717, 2128501, 6448498, 6514720, 6514789, 6514804, 6578547, 6579566, 6579570, 6580581, 6627428, 6627443, 6646126, 6646132, 6647328, 6648352, 6648608, 6776174, 6841710, 6845472, 6906728, 6907168, 6909472, 6909541, 6911008, 7104867, 7105637, 7217249, 7217252, 7217267, 7234592, 7234661, 7234848, 7235360, 7235429, 7238757, 7479396, 7496805, 7497065, 7562088, 7566437, 7610468, 7628064, 7628142, 7628146, 7695972, 7695975, 7759218}), new NGramsPlusLang("en", new int[]{2122016, 2122094, 2122341, 2122607, 2123375, 2123873, 2123877, 2124142, 2125153, 2125670, 2125938, 2126437, 2126689, 2126708, 2126952, 2126959, 2127720, 6383972, 6384672, 6385184, 6385252, 6386464, 6386720, 6386789, 6386793, 6561889, 6561908, 6627425, 6627443, 6627444, 6644768, 6647412, 6648352, 6648608, 6713202, 6840692, 6841632, 6841714, 6906912, 6909472, 6909543, 6909806, 6910752, 7217249, 7217268, 7234592, 7235360, 7238688, 7300640, 7302688, 7303712, 7496992, 7500576, 7544929, 7544948, 7561577, 7566368, 7610484, 7628146, 7628897, 7628901, 7629167, 7630624, 7631648}), new NGramsPlusLang("es", new int[]{2122016, 2122593, 2122607, 2122853, 2123116, 2123118, 2123123, 2124142, 2124897, 2124911, 2125921, 2125935, 2125938, 2126197, 2126437, 2126693, 2127214, 2128160, 6365283, 6365284, 6365285, 6365292, 6365296, 6382441, 6382703, 6384672, 6386208, 6386464, 6515187, 6516590, 6579488, 6579564, 6582048, 6627428, 6627429, 6627436, 6646816, 6647328, 6647412, 6648608, 6648692, 6907246, 6943598, 7102752, 7106419, 7217253, 7238757, 7282788, 7282789, 7302688, 7303712, 7303968, 7364978, 7435621, 7495968, 7497075, 7544932, 7544933, 7544944, 7562528, 7628064, 7630624, 7693600, 15953440}), new NGramsPlusLang("fr", new int[]{2122101, 2122607, 2122849, 2122853, 2122869, 2123118, 2123124, 2124897, 2124901, 2125921, 2125935, 2125938, 2126197, 2126693, 2126703, 2127214, 2154528, 6385268, 6386793, 6513952, 6516590, 6579488, 6579571, 6583584, 6627425, 6627427, 6627428, 6627429, 6627436, 6627440, 6627443, 6647328, 6647412, 6648352, 6648608, 6648864, 6649202, 6909806, 6910752, 6911008, 7102752, 7103776, 7103859, 7169390, 7217252, 7234848, 7238432, 7238688, 7302688, 7302772, 7304562, 7435621, 7479404, 7496992, 7544929, 7544932, 7544933, 7544940, 7544944, 7610468, 7628064, 7629167, 7693600, 7696928}), new NGramsPlusLang("it", new int[]{2122092, 2122600, 2122607, 2122853, 2122857, 2123040, 2124140, 2124142, 2124897, 2125925, 2125938, 2127214, 6365283, 6365284, 6365296, 6365299, 6386799, 6514789, 6516590, 6579564, 6580512, 6627425, 6627427, 6627428, 6627433, 6627436, 6627440, 6627443, 6646816, 6646892, 6647412, 6648352, 6841632, 6889569, 6889571, 6889572, 6889587, 6906144, 6908960, 6909472, 6909806, 7102752, 7103776, 7104800, 7105633, 7234848, 7235872, 7237408, 7238757, 7282785, 7282788, 7282793, 7282803, 7302688, 7302757, 7366002, 7495968, 7496992, 7563552, 7627040, 7628064, 7629088, 7630624, 8022383}), new NGramsPlusLang("nl", new int[]{2122092, 2122341, 2122849, 2122853, 2122857, 2123109, 2123118, 2123621, 2123877, 2124142, 2125153, 2125157, 2125680, 2126949, 2127457, 2127461, 2127471, 2127717, 2128489, 6381934, 6381938, 6385184, 6385252, 6386208, 6386720, 6514804, 6579488, 6579566, 6579570, 6627426, 6627446, 6645102, 6645106, 6647328, 6648352, 6648435, 6648864, 6776174, 6841716, 6907168, 6909472, 6909543, 6910752, 7217250, 7217252, 7217253, 7217256, 7217263, 7217270, 7234661, 7235360, 7302756, 7303026, 7303200, 7303712, 7562088, 7566437, 7610468, 7628064, 7628142, 7628146, 7758190, 7759218, 7761775}), new NGramsPlusLang(IndexType.NO, new int[]{2122100, 2122102, 2122853, 2123118, 2123122, 2123375, 2123873, 2124064, 2125157, 2125671, 2126053, 2126693, 2126699, 2126703, 2126708, 2126953, 2127465, 2155808, 6385252, 6386208, 6386720, 6579488, 6579566, 6579572, 6627443, 6644768, 6647328, 6647397, 6648352, 6648421, 6648864, 6648948, 6713202, 6776174, 6908779, 6908960, 6909543, 7038240, 7039845, 7103776, 7105637, 7169380, 7169390, 7217267, 7234848, 7235360, 7235429, 7237221, 7300896, 7302432, 7303712, 7398688, 7479411, 7496992, 7565165, 7566437, 7610483, 7628064, 7628142, 7628146, 7629164, 7631904, 7631973, 7759218}), new NGramsPlusLang("pt", new int[]{2122016, 2122607, 2122849, 2122853, 2122863, 2123040, 2123123, 2125153, 2125423, 2125600, 2125921, 2125935, 2125938, 2126197, 2126437, 2126693, 2127213, 6365281, 6365283, 6365284, 6365296, 6382693, 6382703, 6384672, 6386208, 6386273, 6386464, 6516589, 6516590, 6578464, 6579488, 6582048, 6582131, 6627425, 6627428, 6647072, 6647412, 6648608, 6648692, 6906144, 6906721, 7169390, 7238757, 7238767, 7282785, 7282787, 7282788, 7282789, 7282800, 7303968, 7364978, 7435621, 7495968, 7497075, 7544929, 7544932, 7544933, 7544944, 7566433, 7628064, 7630624, 7693600, 14905120, 15197039}), new NGramsPlusLang("sv", new int[]{2122100, 2122102, 2122853, 2123118, 2123510, 2123873, 2124064, 2124142, 2124655, 2125157, 2125667, 2126053, 2126699, 2126703, 2126708, 2126953, 2127457, 2127465, 2155634, 6382693, 6385184, 6385252, 6386208, 6386804, 6514720, 6579488, 6579566, 6579570, 6579572, 6644768, 6647328, 6648352, 6648864, 6747762, 6776174, 6909036, 6909543, 7037216, 7105568, 7169380, 7217267, 7233824, 7234661, 7235360, 7235429, 7235950, 7299944, 7302432, 7302688, 7398688, 7479393, 7479411, 7495968, 7564129, 7565165, 7610483, 7627040, 7628064, 7628146, 7629164, 7631904, 7758194, 14971424, 16151072})};

        @Override // ohos.global.icu.text.CharsetRecog_sbcs, ohos.global.icu.text.CharsetRecognizer
        public String getName() {
            return "ISO-8859-1";
        }

        CharsetRecog_8859_1() {
        }

        @Override // ohos.global.icu.text.CharsetRecognizer
        public CharsetMatch match(CharsetDetector charsetDetector) {
            String str = charsetDetector.fC1Bytes ? "windows-1252" : "ISO-8859-1";
            int i = -1;
            NGramsPlusLang[] nGramsPlusLangArr = ngrams_8859_1;
            String str2 = null;
            for (NGramsPlusLang nGramsPlusLang : nGramsPlusLangArr) {
                int match = match(charsetDetector, nGramsPlusLang.fNGrams, byteMap);
                if (match > i) {
                    str2 = nGramsPlusLang.fLang;
                    i = match;
                }
            }
            if (i <= 0) {
                return null;
            }
            return new CharsetMatch(charsetDetector, this, i, str, str2);
        }
    }

    static class CharsetRecog_8859_2 extends CharsetRecog_sbcs {
        protected static byte[] byteMap = {CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, 0, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -79, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -77, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -75, -74, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -71, -70, -69, -68, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -66, -65, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -79, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -77, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -75, -74, -73, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -71, -70, -69, -68, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -66, -65, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, -11, -10, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -8, -7, -6, -5, -4, -3, -2, -33, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, -11, -10, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -8, -7, -6, -5, -4, -3, -2, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE};
        private static NGramsPlusLang[] ngrams_8859_2 = {new NGramsPlusLang("cs", new int[]{2122016, 2122361, 2122863, 2124389, 2125409, 2125413, 2125600, 2125668, 2125935, 2125938, 2126072, 2126447, 2126693, 2126703, 2126708, 2126959, 2127392, 2127481, 2128481, 6365296, 6513952, 6514720, 6627440, 6627443, 6627446, 6647072, 6647533, 6844192, 6844260, 6910836, 6972704, 7042149, 7103776, 7104800, 7233824, 7268640, 7269408, 7269664, 7282800, 7300206, 7301737, 7304052, 7304480, 7304801, 7368548, 7368554, 7369327, 7403621, 7562528, 7565173, 7566433, 7566441, 7566446, 7628146, 7630573, 7630624, 7676016, 12477728, 14773997, 15296623, 15540336, 15540339, 15559968, 16278884}), new NGramsPlusLang("hu", new int[]{2122016, 2122106, 2122341, 2123111, 2123116, 2123365, 2123873, 2123887, 2124147, 2124645, 2124649, 2124790, 2124901, 2125153, 2125157, 2125161, 2125413, 2126714, 2126949, 2156915, 6365281, 6365291, 6365293, 6365299, 6384416, 6385184, 6388256, 6447470, 6448494, 6645625, 6646560, 6646816, 6646885, 6647072, 6647328, 6648421, 6648864, 6648933, 6648948, 6781216, 6844263, 6909556, 6910752, 7020641, 7075450, 7169383, 7170414, 7217249, 7233899, 7234923, 7234925, 7238688, 7300985, 7544929, 7567973, 7567988, 7568097, 7596391, 7610465, 7631904, 7659891, 8021362, 14773792, 15299360}), new NGramsPlusLang("pl", new int[]{2122618, 2122863, 2124064, 2124389, 2124655, 2125153, 2125161, 2125409, 2125417, 2125668, 2125935, 2125938, 2126697, 2127648, 2127721, 2127737, 2128416, 2128481, 6365296, 6365303, 6385257, 6514720, 6519397, 6519417, 6582048, 6584937, 6627440, 6627443, 6627447, 6627450, 6645615, 6646304, 6647072, 6647401, 6778656, 6906144, 6907168, 6907242, 7037216, 7039264, 7039333, 7170405, 7233824, 7235937, 7235941, 7282800, 7305057, 7305065, 7368556, 7369313, 7369327, 7369338, 7502437, 7502457, 7563754, 7564137, 7566433, 7825765, 7955304, 7957792, 8021280, 8022373, 8026400, 15955744}), new NGramsPlusLang("ro", new int[]{2122016, 2122083, 2122593, 2122597, 2122607, 2122613, 2122853, 2122857, 2124897, 2125153, 2125925, 2125938, 2126693, 2126819, 2127214, 2144873, 2158190, 6365283, 6365284, 6386277, 6386720, 6386789, 6386976, 6513010, 6516590, 6518048, 6546208, 6579488, 6627425, 6627427, 6627428, 6627440, 6627443, 6644000, 6646048, 6646885, 6647412, 6648692, 6889569, 6889571, 6889572, 6889584, 6907168, 6908192, 6909472, 7102752, 7103776, 7106418, 7107945, 7234848, 7238770, 7303712, 7365998, 7496992, 7497057, 7501088, 7594784, 7628064, 7631477, 7660320, 7694624, 7695392, 12216608, 15625760})};

        @Override // ohos.global.icu.text.CharsetRecog_sbcs, ohos.global.icu.text.CharsetRecognizer
        public String getName() {
            return "ISO-8859-2";
        }

        CharsetRecog_8859_2() {
        }

        @Override // ohos.global.icu.text.CharsetRecognizer
        public CharsetMatch match(CharsetDetector charsetDetector) {
            String str = charsetDetector.fC1Bytes ? "windows-1250" : "ISO-8859-2";
            int i = -1;
            NGramsPlusLang[] nGramsPlusLangArr = ngrams_8859_2;
            String str2 = null;
            for (NGramsPlusLang nGramsPlusLang : nGramsPlusLangArr) {
                int match = match(charsetDetector, nGramsPlusLang.fNGrams, byteMap);
                if (match > i) {
                    str2 = nGramsPlusLang.fLang;
                    i = match;
                }
            }
            if (i <= 0) {
                return null;
            }
            return new CharsetMatch(charsetDetector, this, i, str, str2);
        }
    }

    static abstract class CharsetRecog_8859_5 extends CharsetRecog_sbcs {
        protected static byte[] byteMap = {CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, 0, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -5, -4, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -2, -1, -48, -47, -46, -45, -44, -43, -42, -41, -40, -39, -38, -37, -36, -35, -34, -33, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -48, -47, -46, -45, -44, -43, -42, -41, -40, -39, -38, -37, -36, -35, -34, -33, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -5, -4, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -2, -1};

        @Override // ohos.global.icu.text.CharsetRecog_sbcs, ohos.global.icu.text.CharsetRecognizer
        public String getName() {
            return "ISO-8859-5";
        }

        CharsetRecog_8859_5() {
        }
    }

    static class CharsetRecog_8859_5_ru extends CharsetRecog_8859_5 {
        private static int[] ngrams = {2150944, 2151134, 2151646, 2152400, 2152480, 2153168, 2153182, 2153936, 2153941, 2154193, 2154462, 2154464, 2154704, 2154974, 2154978, 2155230, 2156514, 2158050, 13688280, 13689580, 13884960, 14015468, 14015960, 14016994, 14017056, 14164191, 14210336, 14211104, 14216992, 14407133, 14407712, 14413021, 14536736, 14538016, 14538965, 14538991, 14540320, 14540498, 14557394, 14557407, 14557409, 14602784, 14602960, 14603230, 14604576, 14605292, 14605344, 14606818, 14671579, 14672085, 14672088, 14672094, 14733522, 14734804, 14803664, 14803666, 14803672, 14806816, 14865883, 14868000, 14868192, 14871584, 15196894, 15459616};

        @Override // ohos.global.icu.text.CharsetRecognizer
        public String getLanguage() {
            return "ru";
        }

        CharsetRecog_8859_5_ru() {
        }

        @Override // ohos.global.icu.text.CharsetRecognizer
        public CharsetMatch match(CharsetDetector charsetDetector) {
            int match = match(charsetDetector, ngrams, byteMap);
            if (match == 0) {
                return null;
            }
            return new CharsetMatch(charsetDetector, this, match);
        }
    }

    static abstract class CharsetRecog_8859_6 extends CharsetRecog_sbcs {
        protected static byte[] byteMap = {CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, 0, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -63, -62, -61, -60, -59, -58, -57, -56, -55, -54, -53, -52, -51, -50, -49, -48, -47, -46, -45, -44, -43, -42, -41, -40, -39, -38, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE};

        @Override // ohos.global.icu.text.CharsetRecog_sbcs, ohos.global.icu.text.CharsetRecognizer
        public String getName() {
            return "ISO-8859-6";
        }

        CharsetRecog_8859_6() {
        }
    }

    static class CharsetRecog_8859_6_ar extends CharsetRecog_8859_6 {
        private static int[] ngrams = {2148324, 2148326, 2148551, 2152932, 2154986, 2155748, 2156006, 2156743, 13050055, 13091104, 13093408, 13095200, 13100064, 13100227, 13100231, 13100232, 13100234, 13100236, 13100237, 13100239, 13100243, 13100249, 13100258, 13100261, 13100264, 13100266, 13100320, 13100576, 13100746, 13115591, 13181127, 13181153, 13181156, 13181157, 13181160, 13246663, 13574343, 13617440, 13705415, 13748512, 13836487, 14229703, 14279913, 14805536, 14950599, 14993696, 15001888, 15002144, 15016135, 15058720, 15059232, 15066656, 15081671, 15147207, 15189792, 15255524, 15263264, 15278279, 15343815, 15343845, 15343848, 15386912, 15388960, 15394336};

        @Override // ohos.global.icu.text.CharsetRecognizer
        public String getLanguage() {
            return "ar";
        }

        CharsetRecog_8859_6_ar() {
        }

        @Override // ohos.global.icu.text.CharsetRecognizer
        public CharsetMatch match(CharsetDetector charsetDetector) {
            int match = match(charsetDetector, ngrams, byteMap);
            if (match == 0) {
                return null;
            }
            return new CharsetMatch(charsetDetector, this, match);
        }
    }

    static abstract class CharsetRecog_8859_7 extends CharsetRecog_sbcs {
        protected static byte[] byteMap = {CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, 0, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -95, -94, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -36, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -35, -34, -33, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -4, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -3, -2, -64, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -13, -12, -11, -10, -9, -8, -7, -6, -5, -36, -35, -34, -33, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -5, -4, -3, -2, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE};

        @Override // ohos.global.icu.text.CharsetRecog_sbcs, ohos.global.icu.text.CharsetRecognizer
        public String getName() {
            return "ISO-8859-7";
        }

        CharsetRecog_8859_7() {
        }
    }

    static class CharsetRecog_8859_7_el extends CharsetRecog_8859_7 {
        private static int[] ngrams = {2154989, 2154992, 2155497, 2155753, 2156016, 2156320, 2157281, 2157797, 2158049, 2158368, 2158817, 2158831, 2158833, 2159604, 2159605, 2159847, 2159855, 14672160, 14754017, 14754036, 14805280, 14806304, 14807292, 14807584, 14936545, 15067424, 15069728, 15147252, 15199520, 15200800, 15278324, 15327520, 15330014, 15331872, 15393257, 15393268, 15525152, 15540449, 15540453, 15540464, 15589664, 15725088, 15725856, 15790069, 15790575, 15793184, 15868129, 15868133, 15868138, 15868144, 15868148, 15983904, 15984416, 15987951, 16048416, 16048617, 16050157, 16050162, 16050666, 16052000, 16052213, 16054765, 16379168, 16706848};

        @Override // ohos.global.icu.text.CharsetRecognizer
        public String getLanguage() {
            return "el";
        }

        CharsetRecog_8859_7_el() {
        }

        @Override // ohos.global.icu.text.CharsetRecognizer
        public CharsetMatch match(CharsetDetector charsetDetector) {
            String str = charsetDetector.fC1Bytes ? "windows-1253" : "ISO-8859-7";
            int match = match(charsetDetector, ngrams, byteMap);
            if (match == 0) {
                return null;
            }
            return new CharsetMatch(charsetDetector, this, match, str, "el");
        }
    }

    static abstract class CharsetRecog_8859_8 extends CharsetRecog_sbcs {
        protected static byte[] byteMap = {CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, 0, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -75, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE};

        @Override // ohos.global.icu.text.CharsetRecog_sbcs, ohos.global.icu.text.CharsetRecognizer
        public String getName() {
            return "ISO-8859-8";
        }

        CharsetRecog_8859_8() {
        }
    }

    static class CharsetRecog_8859_8_I_he extends CharsetRecog_8859_8 {
        private static int[] ngrams = {2154725, 2154727, 2154729, 2154746, 2154985, 2154990, 2155744, 2155749, 2155753, 2155758, 2155762, 2155769, 2155770, 2157792, 2157796, 2158304, 2159340, 2161132, 14744096, 14950624, 14950625, 14950628, 14950636, 14950638, 14950649, 15001056, 15065120, 15068448, 15068960, 15071264, 15071776, 15278308, 15328288, 15328762, 15329773, 15330592, 15331104, 15333408, 15333920, 15474912, 15474916, 15523872, 15524896, 15540448, 15540449, 15540452, 15540460, 15540462, 15540473, 15655968, 15671524, 15787040, 15788320, 15788525, 15920160, 16261348, 16312813, 16378912, 16392416, 16392417, 16392420, 16392428, 16392430, 16392441};

        @Override // ohos.global.icu.text.CharsetRecognizer
        public String getLanguage() {
            return "he";
        }

        @Override // ohos.global.icu.text.CharsetRecog_sbcs.CharsetRecog_8859_8, ohos.global.icu.text.CharsetRecog_sbcs, ohos.global.icu.text.CharsetRecognizer
        public String getName() {
            return "ISO-8859-8-I";
        }

        CharsetRecog_8859_8_I_he() {
        }

        @Override // ohos.global.icu.text.CharsetRecognizer
        public CharsetMatch match(CharsetDetector charsetDetector) {
            String str = charsetDetector.fC1Bytes ? "windows-1255" : "ISO-8859-8-I";
            int match = match(charsetDetector, ngrams, byteMap);
            if (match == 0) {
                return null;
            }
            return new CharsetMatch(charsetDetector, this, match, str, "he");
        }
    }

    static class CharsetRecog_8859_8_he extends CharsetRecog_8859_8 {
        private static int[] ngrams = {2154725, 2154732, 2155753, 2155756, 2155758, 2155760, 2157040, 2157810, 2157817, 2158053, 2158057, 2158565, 2158569, 2160869, 2160873, 2161376, 2161381, 2161385, 14688484, 14688492, 14688493, 14688506, 14738464, 14738916, 14740512, 14741024, 14754020, 14754029, 14754042, 14950628, 14950633, 14950636, 14950637, 14950639, 14950648, 14950650, 15002656, 15065120, 15066144, 15196192, 15327264, 15327520, 15328288, 15474916, 15474925, 15474938, 15528480, 15530272, 15591913, 15591920, 15591928, 15605988, 15605997, 15606010, 15655200, 15655968, 15918112, 16326884, 16326893, 16326906, 16376864, 16441376, 16442400, 16442857};

        @Override // ohos.global.icu.text.CharsetRecognizer
        public String getLanguage() {
            return "he";
        }

        CharsetRecog_8859_8_he() {
        }

        @Override // ohos.global.icu.text.CharsetRecognizer
        public CharsetMatch match(CharsetDetector charsetDetector) {
            String str = charsetDetector.fC1Bytes ? "windows-1255" : "ISO-8859-8";
            int match = match(charsetDetector, ngrams, byteMap);
            if (match == 0) {
                return null;
            }
            return new CharsetMatch(charsetDetector, this, match, str, "he");
        }
    }

    static abstract class CharsetRecog_8859_9 extends CharsetRecog_sbcs {
        protected static byte[] byteMap = {CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, 0, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -86, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -75, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -70, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, -11, -10, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -8, -7, -6, -5, -4, 105, -2, -33, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, -11, -10, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -8, -7, -6, -5, -4, -3, -2, -1};

        @Override // ohos.global.icu.text.CharsetRecog_sbcs, ohos.global.icu.text.CharsetRecognizer
        public String getName() {
            return "ISO-8859-9";
        }

        CharsetRecog_8859_9() {
        }
    }

    static class CharsetRecog_8859_9_tr extends CharsetRecog_8859_9 {
        private static int[] ngrams = {2122337, 2122345, 2122357, 2122849, 2122853, 2123621, 2123873, 2124140, 2124641, 2124655, 2125153, 2125676, 2126689, 2126945, 2127461, 2128225, 6365282, 6384416, 6384737, 6384993, 6385184, 6385405, 6386208, 6386273, 6386429, 6386685, 6388065, 6449522, 6578464, 6579488, 6580512, 6627426, 6627435, 6644841, 6647328, 6648352, 6648425, 6648681, 6909029, 6909472, 6909545, 6910496, 7102830, 7102834, 7103776, 7103858, 7217249, 7217250, 7217259, 7234657, 7234661, 7234848, 7235872, 7235950, 7273760, 7498094, 7535982, 7759136, 7954720, 7958386, 16608800, 16608868, 16609021, 16642301};

        @Override // ohos.global.icu.text.CharsetRecognizer
        public String getLanguage() {
            return "tr";
        }

        CharsetRecog_8859_9_tr() {
        }

        @Override // ohos.global.icu.text.CharsetRecognizer
        public CharsetMatch match(CharsetDetector charsetDetector) {
            String str = charsetDetector.fC1Bytes ? "windows-1254" : "ISO-8859-9";
            int match = match(charsetDetector, ngrams, byteMap);
            if (match == 0) {
                return null;
            }
            return new CharsetMatch(charsetDetector, this, match, str, "tr");
        }
    }

    static class CharsetRecog_windows_1251 extends CharsetRecog_sbcs {
        private static byte[] byteMap = {CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, 0, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -112, -125, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -125, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -102, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -100, -99, -98, -97, -112, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -102, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -100, -99, -98, -97, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -94, -94, -68, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -76, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -72, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -70, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -65, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -77, -77, -76, -75, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -72, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -70, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -68, -66, -66, -65, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -5, -4, -3, -2, -1, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -5, -4, -3, -2, -1};
        private static int[] ngrams = {2155040, 2155246, 2155758, 2156512, 2156576, 2157280, 2157294, 2158048, 2158053, 2158305, 2158574, 2158576, 2158816, 2159086, 2159090, 2159342, 2160626, 2162162, 14740968, 14742268, 14937632, 15068156, 15068648, 15069682, 15069728, 15212783, 15263008, 15263776, 15269664, 15459821, 15460384, 15465709, 15589408, 15590688, 15591653, 15591679, 15592992, 15593186, 15605986, 15605999, 15606001, 15655456, 15655648, 15655918, 15657248, 15657980, 15658016, 15659506, 15724267, 15724773, 15724776, 15724782, 15786210, 15787492, 15856352, 15856354, 15856360, 15859488, 15918571, 15920672, 15920880, 15924256, 16249582, 16512288};

        @Override // ohos.global.icu.text.CharsetRecognizer
        public String getLanguage() {
            return "ru";
        }

        @Override // ohos.global.icu.text.CharsetRecog_sbcs, ohos.global.icu.text.CharsetRecognizer
        public String getName() {
            return "windows-1251";
        }

        CharsetRecog_windows_1251() {
        }

        @Override // ohos.global.icu.text.CharsetRecognizer
        public CharsetMatch match(CharsetDetector charsetDetector) {
            int match = match(charsetDetector, ngrams, byteMap);
            if (match == 0) {
                return null;
            }
            return new CharsetMatch(charsetDetector, this, match);
        }
    }

    static class CharsetRecog_windows_1256 extends CharsetRecog_sbcs {
        private static byte[] byteMap = {CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, 0, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -127, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -125, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -120, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -118, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -100, -115, -114, -113, -112, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -104, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -102, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -100, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -97, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -86, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -75, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -64, -63, -62, -61, -60, -59, -58, -57, -56, -55, -54, -53, -52, -51, -50, -49, -48, -47, -46, -45, -44, -43, -42, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -40, -39, -38, -37, -36, -35, -34, -33, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -12, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -7, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -5, -4, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -1};
        private static int[] ngrams = {2148321, 2148324, 2148551, 2153185, 2153965, 2154977, 2155492, 2156231, 13050055, 13091104, 13093408, 13095200, 13099296, 13099459, 13099463, 13099464, 13099466, 13099468, 13099469, 13099471, 13099475, 13099482, 13099486, 13099491, 13099494, 13099501, 13099808, 13100064, 13100234, 13115591, 13181127, 13181149, 13181153, 13181155, 13181158, 13246663, 13574343, 13617440, 13705415, 13748512, 13836487, 14295239, 14344684, 14544160, 14753991, 14797088, 14806048, 14806304, 14885063, 14927648, 14928160, 14935072, 14950599, 15016135, 15058720, 15124449, 15131680, 15474887, 15540423, 15540451, 15540454, 15583520, 15585568, 15590432};

        @Override // ohos.global.icu.text.CharsetRecognizer
        public String getLanguage() {
            return "ar";
        }

        @Override // ohos.global.icu.text.CharsetRecog_sbcs, ohos.global.icu.text.CharsetRecognizer
        public String getName() {
            return "windows-1256";
        }

        CharsetRecog_windows_1256() {
        }

        @Override // ohos.global.icu.text.CharsetRecognizer
        public CharsetMatch match(CharsetDetector charsetDetector) {
            int match = match(charsetDetector, ngrams, byteMap);
            if (match == 0) {
                return null;
            }
            return new CharsetMatch(charsetDetector, this, match);
        }
    }

    static class CharsetRecog_KOI8_R extends CharsetRecog_sbcs {
        private static byte[] byteMap = {CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, 0, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, MifareClassicTag.USE_KEY_B, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -93, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -93, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, CameraMetadataEx.HUAWEI_EXT_SCENE_MODE_BASE, -64, -63, -62, -61, -60, -59, -58, -57, -56, -55, -54, -53, -52, -51, -50, -49, -48, -47, -46, -45, -44, -43, -42, -41, -40, -39, -38, -37, -36, -35, -34, -33, -64, -63, -62, -61, -60, -59, -58, -57, -56, -55, -54, -53, -52, -51, -50, -49, -48, -47, -46, -45, -44, -43, -42, -41, -40, -39, -38, -37, -36, -35, -34, -33};
        private static int[] ngrams = {2147535, 2148640, 2149313, 2149327, 2150081, 2150085, 2150338, 2150607, 2150610, 2151105, 2151375, 2151380, 2151631, 2152224, 2152399, 2153153, 2153684, 2154196, 12701385, 12702936, 12963032, 12963529, 12964820, 12964896, 13094688, 13181136, 13223200, 13224224, 13226272, 13419982, 13420832, 13424846, 13549856, 13550880, 13552069, 13552081, 13553440, 13553623, 13574352, 13574355, 13574359, 13617103, 13617696, 13618392, 13618464, 13620180, 13621024, 13621185, 13684684, 13685445, 13685449, 13685455, 13812183, 13813188, 13881632, 13882561, 13882569, 13882583, 13944268, 13946656, 13946834, 13948960, 14272544, 14603471};

        @Override // ohos.global.icu.text.CharsetRecognizer
        public String getLanguage() {
            return "ru";
        }

        @Override // ohos.global.icu.text.CharsetRecog_sbcs, ohos.global.icu.text.CharsetRecognizer
        public String getName() {
            return "KOI8-R";
        }

        CharsetRecog_KOI8_R() {
        }

        @Override // ohos.global.icu.text.CharsetRecognizer
        public CharsetMatch match(CharsetDetector charsetDetector) {
            int match = match(charsetDetector, ngrams, byteMap);
            if (match == 0) {
                return null;
            }
            return new CharsetMatch(charsetDetector, this, match);
        }
    }

    static abstract class CharsetRecog_IBM424_he extends CharsetRecog_sbcs {
        protected static byte[] byteMap = {64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 64, 64, 64, 64, 64, 64, 64, 81, 82, 83, 84, 85, 86, 87, 88, 89, 64, 64, 64, 64, 64, 64, 64, 64, 98, 99, 100, 101, 102, 103, 104, 105, 64, 64, 64, 64, 64, 64, 64, 113, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 0, 64, 64, 64, -127, -126, -125, -124, -123, -122, -121, -120, -119, 64, 64, 64, 64, 64, 64, 64, -111, -110, -109, -108, -107, -106, -105, -104, -103, 64, 64, 64, 64, 64, 64, -96, 64, -94, -93, -92, -91, -90, -89, -88, -87, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, -127, -126, -125, -124, -123, -122, -121, -120, -119, 64, 64, 64, 64, 64, 64, 64, -111, -110, -109, -108, -107, -106, -105, -104, -103, 64, 64, 64, 64, 64, 64, 64, 64, -94, -93, -92, -91, -90, -89, -88, -87, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64};

        @Override // ohos.global.icu.text.CharsetRecognizer
        public String getLanguage() {
            return "he";
        }

        CharsetRecog_IBM424_he() {
        }
    }

    static class CharsetRecog_IBM424_he_rtl extends CharsetRecog_IBM424_he {
        private static int[] ngrams = {4211014, 4211016, 4211025, 4211057, 4211281, 4211286, 4212033, 4212038, 4212049, 4212054, 4212066, 4212073, 4212081, 4215873, 4215877, 4216385, 4219476, 4221268, 4288832, 4538433, 4538434, 4538437, 4538452, 4538454, 4538473, 4539969, 4604224, 4609344, 4609856, 4614208, 4616512, 5324869, 5326144, 5326449, 5329237, 5330240, 5330752, 5335104, 5337408, 5521473, 5521477, 5521728, 5522752, 5587009, 5587010, 5587013, 5587028, 5587030, 5587049, 5653824, 5718085, 5784896, 5787968, 5787989, 6444096, 6832197, 6836565, 6902848, 7422017, 7422018, 7422021, 7422036, 7422038, 7422057};

        @Override // ohos.global.icu.text.CharsetRecog_sbcs, ohos.global.icu.text.CharsetRecognizer
        public String getName() {
            return "IBM424_rtl";
        }

        CharsetRecog_IBM424_he_rtl() {
        }

        @Override // ohos.global.icu.text.CharsetRecognizer
        public CharsetMatch match(CharsetDetector charsetDetector) {
            int match = match(charsetDetector, ngrams, byteMap, (byte) 64);
            if (match == 0) {
                return null;
            }
            return new CharsetMatch(charsetDetector, this, match);
        }
    }

    static class CharsetRecog_IBM424_he_ltr extends CharsetRecog_IBM424_he {
        private static int[] ngrams = {4211014, 4211028, 4212049, 4212052, 4212054, 4212056, 4215128, 4215906, 4215913, 4216134, 4216145, 4216646, 4216657, 4220998, 4221009, 4223297, 4223302, 4223313, 4276293, 4276308, 4276309, 4276337, 4277568, 4277829, 4281408, 4281920, 4341829, 4341845, 4341873, 4538437, 4538449, 4538452, 4538453, 4538455, 4538472, 4538481, 4543552, 4604224, 4605248, 4735296, 5325120, 5325376, 5326144, 5521477, 5521493, 5521521, 5530176, 5531968, 5591377, 5591384, 5591400, 5652549, 5652565, 5652593, 5653056, 5653824, 6440256, 6897733, 6897749, 6897777, 6899008, 7422272, 7423296, 7423569};

        @Override // ohos.global.icu.text.CharsetRecog_sbcs, ohos.global.icu.text.CharsetRecognizer
        public String getName() {
            return "IBM424_ltr";
        }

        CharsetRecog_IBM424_he_ltr() {
        }

        @Override // ohos.global.icu.text.CharsetRecognizer
        public CharsetMatch match(CharsetDetector charsetDetector) {
            int match = match(charsetDetector, ngrams, byteMap, (byte) 64);
            if (match == 0) {
                return null;
            }
            return new CharsetMatch(charsetDetector, this, match);
        }
    }

    static abstract class CharsetRecog_IBM420_ar extends CharsetRecog_sbcs {
        protected static byte[] byteMap = {64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 66, 67, 68, 69, 70, 71, 72, 73, 64, 64, 64, 64, 64, 64, 64, 81, 82, 64, 64, 85, 86, 87, 88, 89, 64, 64, 64, 64, 64, 64, 64, 64, 98, 99, 100, 101, 102, 103, 104, 105, 64, 64, 64, 64, 64, 64, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 64, 64, 64, 64, 64, 64, Bidi.LEVEL_OVERRIDE, -127, -126, -125, -124, -123, -122, -121, -120, -119, -118, -117, -116, -115, -114, -113, -112, -111, -110, -109, -108, -107, -106, -105, -104, -103, -102, -101, -100, -99, -98, -97, -96, 64, -94, -93, -92, -91, -90, -89, -88, -87, -86, -85, -84, -83, -82, -81, -80, -79, -78, -77, -76, -75, 64, 64, -72, -71, -70, -69, -68, -67, -66, -65, 64, -127, -126, -125, -124, -123, -122, -121, -120, -119, 64, -53, 64, -51, 64, -49, 64, -111, -110, -109, -108, -107, -106, -105, -104, -103, -38, -37, -36, -35, -34, -33, 64, 64, -94, -93, -92, -91, -90, -89, -88, -87, -22, -21, 64, -19, -18, -17, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, -5, -4, -3, -2, 64};

        @Override // ohos.global.icu.text.CharsetRecognizer
        public String getLanguage() {
            return "ar";
        }

        CharsetRecog_IBM420_ar() {
        }
    }

    static class CharsetRecog_IBM420_ar_rtl extends CharsetRecog_IBM420_ar {
        private static int[] ngrams = {4216497, 4216509, 4216918, 4233905, 4238300, 4239793, 4242365, 4247382, 5652566, 5654080, 5661504, 5666112, 5681472, 5681481, 5681494, 5681496, 5681507, 5681511, 5681513, 5681523, 5681528, 5681562, 5681581, 5681595, 5681615, 5681628, 5684032, 5684544, 5684579, 5783638, 6438998, 6439083, 6439089, 6439099, 6439119, 6504534, 7553110, 7561792, 7684182, 7692864, 7880790, 10109014, 10138074, 11263040, 11616342, 11625024, 11655744, 11656256, 12271702, 12277312, 12280384, 12303680, 12402774, 12533846, 12539456, 13588145, 13614400, 14303318, 14434390, 14434491, 14434511, 14443072, 14447936, 14466368};

        @Override // ohos.global.icu.text.CharsetRecog_sbcs, ohos.global.icu.text.CharsetRecognizer
        public String getName() {
            return "IBM420_rtl";
        }

        CharsetRecog_IBM420_ar_rtl() {
        }

        @Override // ohos.global.icu.text.CharsetRecognizer
        public CharsetMatch match(CharsetDetector charsetDetector) {
            int matchIBM420 = matchIBM420(charsetDetector, ngrams, byteMap, (byte) 64);
            if (matchIBM420 == 0) {
                return null;
            }
            return new CharsetMatch(charsetDetector, this, matchIBM420);
        }
    }

    static class CharsetRecog_IBM420_ar_ltr extends CharsetRecog_IBM420_ar {
        private static int[] ngrams = {4212310, 4216507, 4216511, 4219507, 4219509, 4219569, 4219579, 4219612, 4219734, 4224342, 4224476, 4239702, 4242262, 4242774, 4242875, 4242895, 4242908, 4250289, 4250795, 4250801, 4829526, 5652566, 5652568, 5652578, 5652579, 5652595, 5652597, 5652600, 5652634, 5652657, 5652667, 5652669, 5652671, 5652698, 5652700, 5658688, 5681494, 5689152, 5812566, 6533462, 6536534, 6795606, 6926678, 7582038, 7909718, 10137942, 11223138, 11383126, 11616354, 11621952, 11622095, 11639360, 11645248, 12271714, 12271836, 12300630, 12408384, 12434240, 13582434, 13582556, 13611350, 14332314, 14461760, 14463318};

        @Override // ohos.global.icu.text.CharsetRecog_sbcs, ohos.global.icu.text.CharsetRecognizer
        public String getName() {
            return "IBM420_ltr";
        }

        CharsetRecog_IBM420_ar_ltr() {
        }

        @Override // ohos.global.icu.text.CharsetRecognizer
        public CharsetMatch match(CharsetDetector charsetDetector) {
            int matchIBM420 = matchIBM420(charsetDetector, ngrams, byteMap, (byte) 64);
            if (matchIBM420 == 0) {
                return null;
            }
            return new CharsetMatch(charsetDetector, this, matchIBM420);
        }
    }
}
