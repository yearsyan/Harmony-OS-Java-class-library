package ohos.com.sun.org.apache.xerces.internal.impl.dv.xs;

import ohos.com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import ohos.com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import ohos.global.icu.impl.locale.LanguageTag;
import ohos.global.icu.text.DateFormat;

class PrecisionDecimalDV extends TypeValidator {
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public short getAllowedFacets() {
        return 4088;
    }

    PrecisionDecimalDV() {
    }

    static final class XPrecisionDecimal {
        private String canonical;
        int fracDigits = 0;
        String fvalue = "";
        int intDigits = 0;
        String ivalue = "";
        int pvalue = 0;
        int sign = 1;
        int totalDigits = 0;

        XPrecisionDecimal(String str) throws NumberFormatException {
            if (str.equals("NaN")) {
                this.ivalue = str;
                this.sign = 0;
            }
            if (str.equals("+INF") || str.equals("INF") || str.equals("-INF")) {
                this.ivalue = str.charAt(0) == '+' ? str.substring(1) : str;
            } else {
                initD(str);
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:20:0x003f  */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x0085 A[ADDED_TO_REGION] */
        /* JADX WARNING: Removed duplicated region for block: B:43:0x0091  */
        /* JADX WARNING: Removed duplicated region for block: B:50:0x00b0  */
        /* JADX WARNING: Removed duplicated region for block: B:53:0x00ba  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void initD(java.lang.String r10) throws java.lang.NumberFormatException {
            /*
            // Method dump skipped, instructions count: 219
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.dv.xs.PrecisionDecimalDV.XPrecisionDecimal.initD(java.lang.String):void");
        }

        private static String canonicalToStringForHashCode(String str, String str2, int i, int i2) {
            if ("NaN".equals(str)) {
                return "NaN";
            }
            if (!"INF".equals(str)) {
                StringBuilder sb = new StringBuilder();
                int length = str.length();
                int length2 = str2.length();
                while (length2 > 0 && str2.charAt(length2 - 1) == '0') {
                    length2--;
                }
                int i3 = 0;
                int i4 = 0;
                while (i4 < length && str.charAt(i4) == '0') {
                    i4++;
                }
                int length3 = str.length();
                String str3 = LanguageTag.SEP;
                if (i4 < length3) {
                    if (i != -1) {
                        str3 = "";
                    }
                    sb.append(str3);
                    sb.append(str.charAt(i4));
                    i4++;
                } else {
                    if (length2 > 0) {
                        while (i3 < length2 && str2.charAt(i3) == '0') {
                            i3++;
                        }
                        if (i3 < length2) {
                            if (i != -1) {
                                str3 = "";
                            }
                            sb.append(str3);
                            sb.append(str2.charAt(i3));
                            i3++;
                            i2 -= i3;
                        }
                    }
                    return "0";
                }
                if (i4 < length || i3 < length2) {
                    sb.append('.');
                }
                while (i4 < length) {
                    sb.append(str.charAt(i4));
                    i2++;
                    i4++;
                }
                while (i3 < length2) {
                    sb.append(str2.charAt(i3));
                    i3++;
                }
                if (i2 != 0) {
                    sb.append(DateFormat.ABBR_WEEKDAY);
                    sb.append(i2);
                }
                return sb.toString();
            } else if (i < 0) {
                return "-INF";
            } else {
                return "INF";
            }
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof XPrecisionDecimal)) {
                return false;
            }
            return compareTo((XPrecisionDecimal) obj) == 0;
        }

        public int hashCode() {
            return canonicalToStringForHashCode(this.ivalue, this.fvalue, this.sign, this.pvalue).hashCode();
        }

        private int compareFractionalPart(XPrecisionDecimal xPrecisionDecimal) {
            if (this.fvalue.equals(xPrecisionDecimal.fvalue)) {
                return 0;
            }
            StringBuffer stringBuffer = new StringBuffer(this.fvalue);
            StringBuffer stringBuffer2 = new StringBuffer(xPrecisionDecimal.fvalue);
            truncateTrailingZeros(stringBuffer, stringBuffer2);
            return stringBuffer.toString().compareTo(stringBuffer2.toString());
        }

        private void truncateTrailingZeros(StringBuffer stringBuffer, StringBuffer stringBuffer2) {
            int length = stringBuffer.length();
            while (true) {
                length--;
                if (length < 0 || stringBuffer.charAt(length) != '0') {
                    int length2 = stringBuffer2.length() - 1;
                } else {
                    stringBuffer.deleteCharAt(length);
                }
            }
            int length22 = stringBuffer2.length() - 1;
            while (length22 >= 0 && stringBuffer2.charAt(length22) == '0') {
                stringBuffer2.deleteCharAt(length22);
                length22--;
            }
        }

        public int compareTo(XPrecisionDecimal xPrecisionDecimal) {
            if (this.sign == 0) {
                return 2;
            }
            if (this.ivalue.equals("INF") || xPrecisionDecimal.ivalue.equals("INF")) {
                if (this.ivalue.equals(xPrecisionDecimal.ivalue)) {
                    return 0;
                }
                return this.ivalue.equals("INF") ? 1 : -1;
            } else if (!this.ivalue.equals("-INF") && !xPrecisionDecimal.ivalue.equals("-INF")) {
                int i = this.sign;
                int i2 = xPrecisionDecimal.sign;
                if (i != i2) {
                    return i > i2 ? 1 : -1;
                }
                return i * compare(xPrecisionDecimal);
            } else if (this.ivalue.equals(xPrecisionDecimal.ivalue)) {
                return 0;
            } else {
                return this.ivalue.equals("-INF") ? -1 : 1;
            }
        }

        private int compare(XPrecisionDecimal xPrecisionDecimal) {
            if (this.pvalue == 0 && xPrecisionDecimal.pvalue == 0) {
                return intComp(xPrecisionDecimal);
            }
            int i = this.pvalue;
            int i2 = xPrecisionDecimal.pvalue;
            if (i == i2) {
                return intComp(xPrecisionDecimal);
            }
            int i3 = this.intDigits;
            int i4 = i3 + i;
            int i5 = xPrecisionDecimal.intDigits;
            if (i4 != i5 + i2) {
                return i3 + i > i5 + i2 ? 1 : -1;
            }
            int i6 = 0;
            if (i > i2) {
                int i7 = i - i2;
                StringBuffer stringBuffer = new StringBuffer(this.ivalue);
                StringBuffer stringBuffer2 = new StringBuffer(this.fvalue);
                while (i6 < i7) {
                    if (i6 < this.fracDigits) {
                        stringBuffer.append(this.fvalue.charAt(i6));
                        stringBuffer2.deleteCharAt(i6);
                    } else {
                        stringBuffer.append('0');
                    }
                    i6++;
                }
                return compareDecimal(stringBuffer.toString(), xPrecisionDecimal.ivalue, stringBuffer2.toString(), xPrecisionDecimal.fvalue);
            }
            int i8 = i2 - i;
            StringBuffer stringBuffer3 = new StringBuffer(xPrecisionDecimal.ivalue);
            StringBuffer stringBuffer4 = new StringBuffer(xPrecisionDecimal.fvalue);
            while (i6 < i8) {
                if (i6 < xPrecisionDecimal.fracDigits) {
                    stringBuffer3.append(xPrecisionDecimal.fvalue.charAt(i6));
                    stringBuffer4.deleteCharAt(i6);
                } else {
                    stringBuffer3.append('0');
                }
                i6++;
            }
            return compareDecimal(this.ivalue, stringBuffer3.toString(), this.fvalue, stringBuffer4.toString());
        }

        private int intComp(XPrecisionDecimal xPrecisionDecimal) {
            int i = this.intDigits;
            int i2 = xPrecisionDecimal.intDigits;
            if (i != i2) {
                return i > i2 ? 1 : -1;
            }
            return compareDecimal(this.ivalue, xPrecisionDecimal.ivalue, this.fvalue, xPrecisionDecimal.fvalue);
        }

        private int compareDecimal(String str, String str2, String str3, String str4) {
            int compareTo = str.compareTo(str3);
            if (compareTo != 0) {
                return compareTo > 0 ? 1 : -1;
            }
            if (str2.equals(str4)) {
                return 0;
            }
            StringBuffer stringBuffer = new StringBuffer(str2);
            StringBuffer stringBuffer2 = new StringBuffer(str4);
            truncateTrailingZeros(stringBuffer, stringBuffer2);
            int compareTo2 = stringBuffer.toString().compareTo(stringBuffer2.toString());
            if (compareTo2 == 0) {
                return 0;
            }
            return compareTo2 > 0 ? 1 : -1;
        }

        public synchronized String toString() {
            if (this.canonical == null) {
                makeCanonical();
            }
            return this.canonical;
        }

        private void makeCanonical() {
            this.canonical = "TBD by Working Group";
        }

        public boolean isIdentical(XPrecisionDecimal xPrecisionDecimal) {
            if (this.ivalue.equals(xPrecisionDecimal.ivalue) && (this.ivalue.equals("INF") || this.ivalue.equals("-INF") || this.ivalue.equals("NaN"))) {
                return true;
            }
            if (this.sign == xPrecisionDecimal.sign && this.intDigits == xPrecisionDecimal.intDigits && this.fracDigits == xPrecisionDecimal.fracDigits && this.pvalue == xPrecisionDecimal.pvalue && this.ivalue.equals(xPrecisionDecimal.ivalue) && this.fvalue.equals(xPrecisionDecimal.fvalue)) {
                return true;
            }
            return false;
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public Object getActualValue(String str, ValidationContext validationContext) throws InvalidDatatypeValueException {
        try {
            return new XPrecisionDecimal(str);
        } catch (NumberFormatException unused) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{str, "precisionDecimal"});
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public int compare(Object obj, Object obj2) {
        return ((XPrecisionDecimal) obj).compareTo((XPrecisionDecimal) obj2);
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public int getFractionDigits(Object obj) {
        return ((XPrecisionDecimal) obj).fracDigits;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public int getTotalDigits(Object obj) {
        return ((XPrecisionDecimal) obj).totalDigits;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public boolean isIdentical(Object obj, Object obj2) {
        if (!(obj2 instanceof XPrecisionDecimal) || !(obj instanceof XPrecisionDecimal)) {
            return false;
        }
        return ((XPrecisionDecimal) obj).isIdentical((XPrecisionDecimal) obj2);
    }
}
