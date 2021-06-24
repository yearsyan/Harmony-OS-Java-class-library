package ohos.global.icu.impl.duration.impl;

import java.util.Locale;

public class Utils {
    public static final Locale localeFromString(String str) {
        String str2;
        int indexOf = str.indexOf("_");
        String str3 = "";
        if (indexOf != -1) {
            str2 = str.substring(indexOf + 1);
            str = str.substring(0, indexOf);
        } else {
            str2 = str3;
        }
        int indexOf2 = str2.indexOf("_");
        if (indexOf2 != -1) {
            str3 = str2.substring(indexOf2 + 1);
            str2 = str2.substring(0, indexOf2);
        }
        return new Locale(str, str2, str3);
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x006c  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0094  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String chineseNumber(long r16, ohos.global.icu.impl.duration.impl.Utils.ChineseDigits r18) {
        /*
        // Method dump skipped, instructions count: 391
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.duration.impl.Utils.chineseNumber(long, ohos.global.icu.impl.duration.impl.Utils$ChineseDigits):java.lang.String");
    }

    public static class ChineseDigits {
        public static final ChineseDigits DEBUG = new ChineseDigits("0123456789s", "sbq", "WYZ", 'L', false);
        public static final ChineseDigits KOREAN = new ChineseDigits("영일이삼사오육칠팔구십", "십백천", "만억?", 51060, true);
        public static final ChineseDigits SIMPLIFIED = new ChineseDigits("零一二三四五六七八九十", "十百千", "万亿兆", 20004, false);
        public static final ChineseDigits TRADITIONAL = new ChineseDigits("零一二三四五六七八九十", "十百千", "萬億兆", 20841, false);
        final char[] digits;
        final boolean ko;
        final char[] levels;
        final char liang;
        final char[] units;

        ChineseDigits(String str, String str2, String str3, char c, boolean z) {
            this.digits = str.toCharArray();
            this.units = str2.toCharArray();
            this.levels = str3.toCharArray();
            this.liang = c;
            this.ko = z;
        }
    }
}
