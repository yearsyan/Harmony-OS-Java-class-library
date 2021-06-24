package ohos.global.icu.impl.number;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import ohos.com.sun.org.apache.xpath.internal.XPath;
import ohos.global.icu.impl.PatternTokenizer;
import ohos.global.icu.impl.StandardPlural;
import ohos.global.icu.impl.locale.LanguageTag;
import ohos.global.icu.impl.number.Modifier;
import ohos.global.icu.impl.number.Padder;
import ohos.global.icu.number.NumberFormatter;
import ohos.global.icu.text.DateFormat;
import ohos.global.icu.text.DecimalFormatSymbols;

public class PatternStringUtils {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    public enum PatternSignType {
        POS,
        POS_SIGN,
        NEG;
        
        public static final PatternSignType[] VALUES = values();
    }

    public static boolean ignoreRoundingIncrement(BigDecimal bigDecimal, int i) {
        double doubleValue = bigDecimal.doubleValue();
        if (doubleValue == XPath.MATCH_SCORE_QNAME) {
            return true;
        }
        if (i < 0) {
            return false;
        }
        double d = doubleValue * 2.0d;
        int i2 = 0;
        while (i2 <= i && d <= 1.0d) {
            i2++;
            d *= 10.0d;
        }
        return i2 > i;
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x010b A[LOOP:2: B:21:0x0102->B:23:0x010b, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0113 A[LOOP:3: B:24:0x0110->B:26:0x0113, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0126  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x012e  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0137  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0184  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x01a9 A[LOOP:6: B:66:0x01a9->B:68:0x01b1, LOOP_START, PHI: r1 
      PHI: (r1v9 int) = (r1v5 int), (r1v13 int) binds: [B:65:0x01a7, B:68:0x01b1] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0208  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0210  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String propertiesToPatternString(ohos.global.icu.impl.number.DecimalFormatProperties r25) {
        /*
        // Method dump skipped, instructions count: 559
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.number.PatternStringUtils.propertiesToPatternString(ohos.global.icu.impl.number.DecimalFormatProperties):java.lang.String");
    }

    private static int escapePaddingString(CharSequence charSequence, StringBuilder sb, int i) {
        if (charSequence == null || charSequence.length() == 0) {
            charSequence = " ";
        }
        int length = sb.length();
        int i2 = 1;
        if (charSequence.length() != 1) {
            sb.insert(i, PatternTokenizer.SINGLE_QUOTE);
            for (int i3 = 0; i3 < charSequence.length(); i3++) {
                char charAt = charSequence.charAt(i3);
                if (charAt == '\'') {
                    sb.insert(i + i2, "''");
                    i2 += 2;
                } else {
                    sb.insert(i + i2, charAt);
                    i2++;
                }
            }
            sb.insert(i + i2, PatternTokenizer.SINGLE_QUOTE);
        } else if (charSequence.equals("'")) {
            sb.insert(i, "''");
        } else {
            sb.insert(i, charSequence);
        }
        return sb.length() - length;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r19v0, resolved type: boolean */
    /* JADX WARN: Multi-variable type inference failed */
    public static String convertLocalized(String str, DecimalFormatSymbols decimalFormatSymbols, boolean z) {
        if (str == null) {
            return null;
        }
        char c = 2;
        String[][] strArr = (String[][]) Array.newInstance(String.class, 21, 2);
        int i = !z ? 1 : 0;
        char c2 = 0;
        strArr[0][i] = "%";
        strArr[0][z ? 1 : 0] = decimalFormatSymbols.getPercentString();
        strArr[1][i] = "‰";
        strArr[1][z] = decimalFormatSymbols.getPerMillString();
        strArr[2][i] = ".";
        strArr[2][z] = decimalFormatSymbols.getDecimalSeparatorString();
        strArr[3][i] = ",";
        strArr[3][z] = decimalFormatSymbols.getGroupingSeparatorString();
        strArr[4][i] = LanguageTag.SEP;
        strArr[4][z] = decimalFormatSymbols.getMinusSignString();
        char c3 = 5;
        strArr[5][i] = "+";
        strArr[5][z] = decimalFormatSymbols.getPlusSignString();
        strArr[6][i] = ";";
        strArr[6][z] = Character.toString(decimalFormatSymbols.getPatternSeparator());
        strArr[7][i] = "@";
        strArr[7][z] = Character.toString(decimalFormatSymbols.getSignificantDigit());
        strArr[8][i] = DateFormat.ABBR_WEEKDAY;
        strArr[8][z] = decimalFormatSymbols.getExponentSeparator();
        strArr[9][i] = "*";
        strArr[9][z] = Character.toString(decimalFormatSymbols.getPadEscape());
        strArr[10][i] = "#";
        strArr[10][z] = Character.toString(decimalFormatSymbols.getDigit());
        for (int i2 = 0; i2 < 10; i2++) {
            int i3 = i2 + 11;
            strArr[i3][i] = Character.toString((char) (i2 + 48));
            strArr[i3][z] = decimalFormatSymbols.getDigitStringsLocal()[i2];
        }
        for (int i4 = 0; i4 < strArr.length; i4++) {
            strArr[i4][z] = strArr[i4][z].replace(PatternTokenizer.SINGLE_QUOTE, (char) 8217);
        }
        StringBuilder sb = new StringBuilder();
        int i5 = 0;
        char c4 = 0;
        while (i5 < str.length()) {
            char charAt = str.charAt(i5);
            if (charAt == '\'') {
                if (c4 == 0) {
                    sb.append(PatternTokenizer.SINGLE_QUOTE);
                } else if (c4 == 1) {
                    sb.append(PatternTokenizer.SINGLE_QUOTE);
                    c4 = 0;
                } else if (c4 == c) {
                    c4 = 3;
                } else if (c4 == 3) {
                    sb.append(PatternTokenizer.SINGLE_QUOTE);
                    sb.append(PatternTokenizer.SINGLE_QUOTE);
                } else if (c4 == 4) {
                    c4 = c3;
                } else {
                    sb.append(PatternTokenizer.SINGLE_QUOTE);
                    sb.append(PatternTokenizer.SINGLE_QUOTE);
                    c4 = 4;
                }
                c4 = 1;
            } else {
                if (c4 == 0 || c4 == 3 || c4 == 4) {
                    int length = strArr.length;
                    int i6 = 0;
                    while (true) {
                        if (i6 < length) {
                            String[] strArr2 = strArr[i6];
                            if (str.regionMatches(i5, strArr2[0], 0, strArr2[0].length())) {
                                i5 += strArr2[0].length() - 1;
                                if (c4 == 3 || c4 == 4) {
                                    sb.append(PatternTokenizer.SINGLE_QUOTE);
                                    c = 0;
                                } else {
                                    c = c4;
                                }
                                sb.append(strArr2[1]);
                            } else {
                                i6++;
                            }
                        } else {
                            int length2 = strArr.length;
                            int i7 = 0;
                            while (true) {
                                if (i7 < length2) {
                                    String[] strArr3 = strArr[i7];
                                    if (str.regionMatches(i5, strArr3[1], 0, strArr3[1].length())) {
                                        if (c4 == 0) {
                                            sb.append(PatternTokenizer.SINGLE_QUOTE);
                                            c = 4;
                                        } else {
                                            c = c4;
                                        }
                                        sb.append(charAt);
                                    } else {
                                        i7++;
                                    }
                                } else {
                                    if (c4 == 3 || c4 == 4) {
                                        sb.append(PatternTokenizer.SINGLE_QUOTE);
                                        c = 0;
                                    } else {
                                        c = c4;
                                    }
                                    sb.append(charAt);
                                }
                            }
                        }
                    }
                } else {
                    sb.append(charAt);
                }
                c4 = c;
            }
            i5++;
            c = 2;
            c3 = 5;
        }
        if (c4 == 3 || c4 == 4) {
            sb.append(PatternTokenizer.SINGLE_QUOTE);
        } else {
            c2 = c4;
        }
        if (c2 == 0) {
            return sb.toString();
        }
        throw new IllegalArgumentException("Malformed localized pattern: unterminated quote");
    }

    public static void patternInfoToStringBuilder(AffixPatternProvider affixPatternProvider, boolean z, PatternSignType patternSignType, StandardPlural standardPlural, boolean z2, StringBuilder sb) {
        char c;
        int i = 1;
        int i2 = (patternSignType != PatternSignType.POS_SIGN || affixPatternProvider.positiveHasPlusSign()) ? 0 : 1;
        boolean z3 = affixPatternProvider.hasNegativeSubpattern() && (patternSignType == PatternSignType.NEG || (affixPatternProvider.negativeHasMinusSign() && i2 != 0));
        int i3 = z3 ? 512 : 0;
        if (z) {
            i3 |= 256;
        }
        if (standardPlural != null) {
            i3 |= standardPlural.ordinal();
        }
        if (!z || z3) {
            i = 0;
        } else if (patternSignType != PatternSignType.NEG) {
            i = i2;
        }
        int length = affixPatternProvider.length(i3) + i;
        sb.setLength(0);
        for (int i4 = 0; i4 < length; i4++) {
            if (i != 0 && i4 == 0) {
                c = '-';
            } else if (i != 0) {
                c = affixPatternProvider.charAt(i3, i4 - 1);
            } else {
                c = affixPatternProvider.charAt(i3, i4);
            }
            if (i2 != 0 && c == '-') {
                c = '+';
            }
            if (z2 && c == '%') {
                c = 8240;
            }
            sb.append(c);
        }
    }

    /* renamed from: ohos.global.icu.impl.number.PatternStringUtils$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$impl$number$Modifier$Signum = new int[Modifier.Signum.values().length];
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$impl$number$Padder$PadPosition = new int[Padder.PadPosition.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(34:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|23|24|25|26|27|28|29|30|31|33|34|35|36|37|38|39|40|42) */
        /* JADX WARNING: Can't wrap try/catch for region: R(35:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|23|24|25|26|27|28|29|30|31|33|34|35|36|37|38|39|40|42) */
        /* JADX WARNING: Can't wrap try/catch for region: R(36:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|23|24|25|26|27|28|29|30|31|33|34|35|36|37|38|39|40|42) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0035 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0069 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0073 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x007d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x009a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x00a4 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x00ae */
        static {
            /*
            // Method dump skipped, instructions count: 185
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.number.PatternStringUtils.AnonymousClass1.<clinit>():void");
        }
    }

    public static PatternSignType resolveSignDisplay(NumberFormatter.SignDisplay signDisplay, Modifier.Signum signum) {
        switch (signDisplay) {
            case AUTO:
            case ACCOUNTING:
                int i = AnonymousClass1.$SwitchMap$ohos$global$icu$impl$number$Modifier$Signum[signum.ordinal()];
                if (i == 1 || i == 2) {
                    return PatternSignType.NEG;
                }
                if (i == 3 || i == 4) {
                    return PatternSignType.POS;
                }
            case ALWAYS:
            case ACCOUNTING_ALWAYS:
                int i2 = AnonymousClass1.$SwitchMap$ohos$global$icu$impl$number$Modifier$Signum[signum.ordinal()];
                if (i2 == 1 || i2 == 2) {
                    return PatternSignType.NEG;
                }
                if (i2 == 3 || i2 == 4) {
                    return PatternSignType.POS_SIGN;
                }
            case EXCEPT_ZERO:
            case ACCOUNTING_EXCEPT_ZERO:
                int i3 = AnonymousClass1.$SwitchMap$ohos$global$icu$impl$number$Modifier$Signum[signum.ordinal()];
                if (i3 == 1) {
                    return PatternSignType.NEG;
                }
                if (i3 == 2 || i3 == 3) {
                    return PatternSignType.POS;
                }
                if (i3 == 4) {
                    return PatternSignType.POS_SIGN;
                }
                break;
            case NEVER:
                return PatternSignType.POS;
        }
        throw new AssertionError("Unreachable");
    }
}
