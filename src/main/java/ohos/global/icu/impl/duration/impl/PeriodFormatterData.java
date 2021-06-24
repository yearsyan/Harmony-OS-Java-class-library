package ohos.global.icu.impl.duration.impl;

import java.io.PrintStream;
import java.util.Arrays;
import ohos.global.icu.impl.duration.TimeUnit;
import ohos.global.icu.impl.duration.impl.DataRecord;
import ohos.global.icu.impl.duration.impl.Utils;

public class PeriodFormatterData {
    private static final int FORM_DUAL = 2;
    private static final int FORM_HALF_SPELLED = 6;
    private static final int FORM_PAUCAL = 3;
    private static final int FORM_PLURAL = 0;
    private static final int FORM_SINGULAR = 1;
    private static final int FORM_SINGULAR_NO_OMIT = 5;
    private static final int FORM_SINGULAR_SPELLED = 4;
    public static boolean trace = false;
    final DataRecord dr;
    String localeName;

    public PeriodFormatterData(String str, DataRecord dataRecord) {
        this.dr = dataRecord;
        this.localeName = str;
        if (str == null) {
            throw new NullPointerException("localename is null");
        } else if (dataRecord == null) {
            throw new NullPointerException("data record is null");
        }
    }

    public int pluralization() {
        return this.dr.pl;
    }

    public boolean allowZero() {
        return this.dr.allowZero;
    }

    public boolean weeksAloneOnly() {
        return this.dr.weeksAloneOnly;
    }

    public int useMilliseconds() {
        return this.dr.useMilliseconds;
    }

    public boolean appendPrefix(int i, int i2, StringBuffer stringBuffer) {
        DataRecord.ScopeData scopeData;
        String str;
        if (this.dr.scopeData == null || (scopeData = this.dr.scopeData[(i * 3) + i2]) == null || (str = scopeData.prefix) == null) {
            return false;
        }
        stringBuffer.append(str);
        return scopeData.requiresDigitPrefix;
    }

    public void appendSuffix(int i, int i2, StringBuffer stringBuffer) {
        DataRecord.ScopeData scopeData;
        String str;
        if (this.dr.scopeData != null && (scopeData = this.dr.scopeData[(i * 3) + i2]) != null && (str = scopeData.suffix) != null) {
            if (trace) {
                PrintStream printStream = System.out;
                printStream.println("appendSuffix '" + str + "'");
            }
            stringBuffer.append(str);
        }
    }

    public boolean appendUnit(TimeUnit timeUnit, int i, int i2, int i3, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, StringBuffer stringBuffer) {
        boolean z6;
        boolean z7;
        TimeUnit timeUnit2;
        String str;
        byte b;
        int i4 = i;
        int ordinal = timeUnit.ordinal();
        if (this.dr.requiresSkipMarker == null || !this.dr.requiresSkipMarker[ordinal] || this.dr.skippedUnitMarker == null) {
            z6 = false;
        } else {
            if (!z5 && z4) {
                stringBuffer.append(this.dr.skippedUnitMarker);
            }
            z6 = true;
        }
        if (i3 != 0) {
            boolean z8 = i3 == 1;
            DataRecord dataRecord = this.dr;
            String[] strArr = z8 ? dataRecord.mediumNames : dataRecord.shortNames;
            if (strArr == null || strArr[ordinal] == null) {
                strArr = z8 ? this.dr.shortNames : this.dr.mediumNames;
            }
            if (!(strArr == null || strArr[ordinal] == null)) {
                appendCount(timeUnit, false, false, i, i2, z, strArr[ordinal], z4, stringBuffer);
                return false;
            }
        }
        int i5 = i2;
        if (i5 == 2 && this.dr.halfSupport != null && (b = this.dr.halfSupport[ordinal]) != 0 && (b == 1 || (b == 2 && i4 <= 1000))) {
            i4 = (i4 / 500) * 500;
            i5 = 3;
        }
        if (!z3 || !z4) {
            timeUnit2 = timeUnit;
            z7 = false;
        } else {
            timeUnit2 = timeUnit;
            z7 = true;
        }
        int computeForm = computeForm(timeUnit2, i4, i5, z7);
        if (computeForm == 4) {
            if (this.dr.singularNames == null) {
                str = this.dr.pluralNames[ordinal][1];
                computeForm = 1;
            } else {
                str = this.dr.singularNames[ordinal];
            }
        } else if (computeForm == 5) {
            str = this.dr.pluralNames[ordinal][1];
        } else if (computeForm == 6) {
            str = this.dr.halfNames[ordinal];
        } else {
            try {
                str = this.dr.pluralNames[ordinal][computeForm];
            } catch (NullPointerException e) {
                System.out.println("Null Pointer in PeriodFormatterData[" + this.localeName + "].au px: " + ordinal + " form: " + computeForm + " pn: " + Arrays.toString(this.dr.pluralNames));
                throw e;
            }
        }
        if (str == null) {
            str = this.dr.pluralNames[ordinal][0];
            computeForm = 0;
        }
        int appendCount = appendCount(timeUnit, computeForm == 4 || computeForm == 6 || (this.dr.omitSingularCount && computeForm == 1) || (this.dr.omitDualCount && computeForm == 2), z2, i4, i5, z, str, z4, stringBuffer);
        if (z4 && appendCount >= 0) {
            String str2 = null;
            if (this.dr.rqdSuffixes != null && appendCount < this.dr.rqdSuffixes.length) {
                str2 = this.dr.rqdSuffixes[appendCount];
            }
            if (str2 == null && this.dr.optSuffixes != null && appendCount < this.dr.optSuffixes.length) {
                str2 = this.dr.optSuffixes[appendCount];
            }
            if (str2 != null) {
                stringBuffer.append(str2);
            }
        }
        return z6;
    }

    /* JADX WARNING: Removed duplicated region for block: B:119:0x0173  */
    /* JADX WARNING: Removed duplicated region for block: B:127:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int appendCount(ohos.global.icu.impl.duration.TimeUnit r14, boolean r15, boolean r16, int r17, int r18, boolean r19, java.lang.String r20, boolean r21, java.lang.StringBuffer r22) {
        /*
        // Method dump skipped, instructions count: 373
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.duration.impl.PeriodFormatterData.appendCount(ohos.global.icu.impl.duration.TimeUnit, boolean, boolean, int, int, boolean, java.lang.String, boolean, java.lang.StringBuffer):int");
    }

    public void appendCountValue(int i, int i2, int i3, StringBuffer stringBuffer) {
        int i4 = i / 1000;
        if (i3 == 0) {
            appendInteger(i4, i2, 10, stringBuffer);
            return;
        }
        if (this.dr.requiresDigitSeparator && stringBuffer.length() > 0) {
            stringBuffer.append(' ');
        }
        appendDigits((long) i4, i2, 10, stringBuffer);
        int i5 = i % 1000;
        if (i3 == 1) {
            i5 /= 100;
        } else if (i3 == 2) {
            i5 /= 10;
        }
        stringBuffer.append(this.dr.decimalSep);
        appendDigits((long) i5, i3, i3, stringBuffer);
        if (this.dr.requiresDigitSeparator) {
            stringBuffer.append(' ');
        }
    }

    public void appendInteger(int i, int i2, int i3, StringBuffer stringBuffer) {
        String str;
        if (this.dr.numberNames == null || i >= this.dr.numberNames.length || (str = this.dr.numberNames[i]) == null) {
            if (this.dr.requiresDigitSeparator && stringBuffer.length() > 0) {
                stringBuffer.append(' ');
            }
            byte b = this.dr.numberSystem;
            if (b == 0) {
                appendDigits((long) i, i2, i3, stringBuffer);
            } else if (b == 1) {
                stringBuffer.append(Utils.chineseNumber((long) i, Utils.ChineseDigits.TRADITIONAL));
            } else if (b == 2) {
                stringBuffer.append(Utils.chineseNumber((long) i, Utils.ChineseDigits.SIMPLIFIED));
            } else if (b == 3) {
                stringBuffer.append(Utils.chineseNumber((long) i, Utils.ChineseDigits.KOREAN));
            }
            if (this.dr.requiresDigitSeparator) {
                stringBuffer.append(' ');
                return;
            }
            return;
        }
        stringBuffer.append(str);
    }

    public void appendDigits(long j, int i, int i2, StringBuffer stringBuffer) {
        char[] cArr = new char[i2];
        long j2 = j;
        int i3 = i2;
        while (i3 > 0 && j2 > 0) {
            i3--;
            cArr[i3] = (char) ((int) (((long) this.dr.zero) + (j2 % 10)));
            j2 /= 10;
        }
        int i4 = i2 - i;
        while (i3 > i4) {
            i3--;
            cArr[i3] = this.dr.zero;
        }
        stringBuffer.append(cArr, i3, i2 - i3);
    }

    public void appendSkippedUnit(StringBuffer stringBuffer) {
        if (this.dr.skippedUnitMarker != null) {
            stringBuffer.append(this.dr.skippedUnitMarker);
        }
    }

    public boolean appendUnitSeparator(TimeUnit timeUnit, boolean z, boolean z2, boolean z3, StringBuffer stringBuffer) {
        if ((z && this.dr.unitSep != null) || this.dr.shortUnitSep != null) {
            if (!z || this.dr.unitSep == null) {
                stringBuffer.append(this.dr.shortUnitSep);
            } else {
                int i = (z2 ? 2 : 0) + (z3 ? 1 : 0);
                stringBuffer.append(this.dr.unitSep[i]);
                if (this.dr.unitSepRequiresDP == null || !this.dr.unitSepRequiresDP[i]) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00b4, code lost:
        if (r8.dr.fractionHandling != 2) goto L_0x00b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00fa, code lost:
        if (r0 > 10) goto L_0x00fc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0123, code lost:
        if (r12 != false) goto L_0x014a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x012c, code lost:
        if (r0 > 11) goto L_0x00fc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x0144, code lost:
        if (r0 == 1) goto L_0x0146;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x0148, code lost:
        if (r0 == 1) goto L_0x014a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int computeForm(ohos.global.icu.impl.duration.TimeUnit r9, int r10, int r11, boolean r12) {
        /*
        // Method dump skipped, instructions count: 334
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.duration.impl.PeriodFormatterData.computeForm(ohos.global.icu.impl.duration.TimeUnit, int, int, boolean):int");
    }
}
