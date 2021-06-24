package ohos.com.sun.org.apache.xalan.internal.lib;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import ohos.com.sun.org.apache.xml.internal.utils.LocaleUtility;
import ohos.com.sun.org.apache.xpath.internal.objects.XBoolean;
import ohos.com.sun.org.apache.xpath.internal.objects.XNumber;
import ohos.com.sun.org.apache.xpath.internal.objects.XObject;
import ohos.global.icu.impl.locale.LanguageTag;
import ohos.global.icu.text.DateFormat;

public class ExsltDatetime {
    static final String EMPTY_STR = "";
    static final String d = "yyyy-MM-dd";
    static final String dt = "yyyy-MM-dd'T'HH:mm:ss";
    static final String gd = "---dd";
    static final String gm = "--MM--";
    static final String gmd = "--MM-dd";
    static final String gy = "yyyy";
    static final String gym = "yyyy-MM";
    static final String t = "HH:mm:ss";

    public static String dateTime() {
        Calendar instance = Calendar.getInstance();
        StringBuffer stringBuffer = new StringBuffer(new SimpleDateFormat(dt).format(instance.getTime()));
        int i = instance.get(15) + instance.get(16);
        if (i == 0) {
            stringBuffer.append(Constants.HASIDCALL_INDEX_SIG);
        } else {
            int i2 = i / 3600000;
            int i3 = i % 3600000;
            stringBuffer.append(i2 < 0 ? LocaleUtility.IETF_SEPARATOR : '+');
            stringBuffer.append(formatDigits(i2));
            stringBuffer.append(':');
            stringBuffer.append(formatDigits(i3));
        }
        return stringBuffer.toString();
    }

    private static String formatDigits(int i) {
        String valueOf = String.valueOf(Math.abs(i));
        if (valueOf.length() != 1) {
            return valueOf;
        }
        return '0' + valueOf;
    }

    public static String date(String str) throws ParseException {
        Date testFormats;
        String[] eraDatetimeZone = getEraDatetimeZone(str);
        String str2 = eraDatetimeZone[0];
        String str3 = eraDatetimeZone[1];
        String str4 = eraDatetimeZone[2];
        if (str3 == null || str4 == null || (testFormats = testFormats(str3, new String[]{dt, d})) == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(d);
        simpleDateFormat.setLenient(false);
        String format = simpleDateFormat.format(testFormats);
        if (format.length() == 0) {
            return "";
        }
        return str2 + format + str4;
    }

    public static String date() {
        String str = dateTime().toString();
        String substring = str.substring(0, str.indexOf("T"));
        String substring2 = str.substring(getZoneStart(str));
        return substring + substring2;
    }

    public static String time(String str) throws ParseException {
        Date testFormats;
        String[] eraDatetimeZone = getEraDatetimeZone(str);
        String str2 = eraDatetimeZone[1];
        String str3 = eraDatetimeZone[2];
        if (str2 == null || str3 == null || (testFormats = testFormats(str2, new String[]{dt, d, t})) == null) {
            return "";
        }
        String format = new SimpleDateFormat(t).format(testFormats);
        return format + str3;
    }

    public static String time() {
        String str = dateTime().toString();
        return str.substring(str.indexOf("T") + 1);
    }

    public static double year(String str) throws ParseException {
        String[] eraDatetimeZone = getEraDatetimeZone(str);
        boolean z = false;
        if (eraDatetimeZone[0].length() == 0) {
            z = true;
        }
        String str2 = eraDatetimeZone[1];
        if (str2 == null) {
            return Double.NaN;
        }
        double number = getNumber(str2, new String[]{dt, d, gym, gy}, 1);
        return (z || number == Double.NaN) ? number : -number;
    }

    public static double year() {
        return (double) Calendar.getInstance().get(1);
    }

    public static double monthInYear(String str) throws ParseException {
        String str2 = getEraDatetimeZone(str)[1];
        if (str2 == null) {
            return Double.NaN;
        }
        return getNumber(str2, new String[]{dt, d, gym, gm, gmd}, 2) + 1.0d;
    }

    public static double monthInYear() {
        return (double) (Calendar.getInstance().get(2) + 1);
    }

    public static double weekInYear(String str) throws ParseException {
        String str2 = getEraDatetimeZone(str)[1];
        if (str2 == null) {
            return Double.NaN;
        }
        return getNumber(str2, new String[]{dt, d}, 3);
    }

    public static double weekInYear() {
        return (double) Calendar.getInstance().get(3);
    }

    public static double dayInYear(String str) throws ParseException {
        String str2 = getEraDatetimeZone(str)[1];
        if (str2 == null) {
            return Double.NaN;
        }
        return getNumber(str2, new String[]{dt, d}, 6);
    }

    public static double dayInYear() {
        return (double) Calendar.getInstance().get(6);
    }

    public static double dayInMonth(String str) throws ParseException {
        return getNumber(getEraDatetimeZone(str)[1], new String[]{dt, d, gmd, gd}, 5);
    }

    public static double dayInMonth() {
        return (double) Calendar.getInstance().get(5);
    }

    public static double dayOfWeekInMonth(String str) throws ParseException {
        String str2 = getEraDatetimeZone(str)[1];
        if (str2 == null) {
            return Double.NaN;
        }
        return getNumber(str2, new String[]{dt, d}, 8);
    }

    public static double dayOfWeekInMonth() {
        return (double) Calendar.getInstance().get(8);
    }

    public static double dayInWeek(String str) throws ParseException {
        String str2 = getEraDatetimeZone(str)[1];
        if (str2 == null) {
            return Double.NaN;
        }
        return getNumber(str2, new String[]{dt, d}, 7);
    }

    public static double dayInWeek() {
        return (double) Calendar.getInstance().get(7);
    }

    public static double hourInDay(String str) throws ParseException {
        String str2 = getEraDatetimeZone(str)[1];
        if (str2 == null) {
            return Double.NaN;
        }
        return getNumber(str2, new String[]{dt, t}, 11);
    }

    public static double hourInDay() {
        return (double) Calendar.getInstance().get(11);
    }

    public static double minuteInHour(String str) throws ParseException {
        String str2 = getEraDatetimeZone(str)[1];
        if (str2 == null) {
            return Double.NaN;
        }
        return getNumber(str2, new String[]{dt, t}, 12);
    }

    public static double minuteInHour() {
        return (double) Calendar.getInstance().get(12);
    }

    public static double secondInMinute(String str) throws ParseException {
        String str2 = getEraDatetimeZone(str)[1];
        if (str2 == null) {
            return Double.NaN;
        }
        return getNumber(str2, new String[]{dt, t}, 13);
    }

    public static double secondInMinute() {
        return (double) Calendar.getInstance().get(13);
    }

    public static XObject leapYear(String str) throws ParseException {
        boolean z = true;
        String str2 = getEraDatetimeZone(str)[1];
        if (str2 == null) {
            return new XNumber(Double.NaN);
        }
        double number = getNumber(str2, new String[]{dt, d, gym, gy}, 1);
        if (number == Double.NaN) {
            return new XNumber(Double.NaN);
        }
        int i = (int) number;
        if (i % 400 != 0 && (i % 100 == 0 || i % 4 != 0)) {
            z = false;
        }
        return new XBoolean(z);
    }

    public static boolean leapYear() {
        int i = Calendar.getInstance().get(1);
        if (i % 400 == 0) {
            return true;
        }
        if (i % 100 == 0 || i % 4 != 0) {
            return false;
        }
        return true;
    }

    public static String monthName(String str) throws ParseException {
        if (getEraDatetimeZone(str)[1] == null) {
            return "";
        }
        return getNameOrAbbrev(str, new String[]{dt, d, gym, gm}, DateFormat.MONTH);
    }

    public static String monthName() {
        Calendar.getInstance();
        return getNameOrAbbrev(DateFormat.MONTH);
    }

    public static String monthAbbreviation(String str) throws ParseException {
        if (getEraDatetimeZone(str)[1] == null) {
            return "";
        }
        return getNameOrAbbrev(str, new String[]{dt, d, gym, gm}, DateFormat.ABBR_MONTH);
    }

    public static String monthAbbreviation() {
        return getNameOrAbbrev(DateFormat.ABBR_MONTH);
    }

    public static String dayName(String str) throws ParseException {
        if (getEraDatetimeZone(str)[1] == null) {
            return "";
        }
        return getNameOrAbbrev(str, new String[]{dt, d}, DateFormat.WEEKDAY);
    }

    public static String dayName() {
        return getNameOrAbbrev(DateFormat.WEEKDAY);
    }

    public static String dayAbbreviation(String str) throws ParseException {
        if (getEraDatetimeZone(str)[1] == null) {
            return "";
        }
        return getNameOrAbbrev(str, new String[]{dt, d}, "EEE");
    }

    public static String dayAbbreviation() {
        return getNameOrAbbrev("EEE");
    }

    private static String[] getEraDatetimeZone(String str) {
        String str2;
        String str3 = "";
        if (str.charAt(0) != '-' || str.startsWith("--")) {
            str2 = str3;
        } else {
            str = str.substring(1);
            str2 = LanguageTag.SEP;
        }
        int zoneStart = getZoneStart(str);
        if (zoneStart > 0) {
            str3 = str.substring(zoneStart);
            str = str.substring(0, zoneStart);
        } else if (zoneStart == -2) {
            str3 = null;
        }
        return new String[]{str2, str, str3};
    }

    private static int getZoneStart(String str) {
        if (str.indexOf(Constants.HASIDCALL_INDEX_SIG) == str.length() - 1) {
            return str.length() - 1;
        }
        if (str.length() < 6 || str.charAt(str.length() - 3) != ':') {
            return -1;
        }
        if (str.charAt(str.length() - 6) != '+' && str.charAt(str.length() - 6) != '-') {
            return -1;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            simpleDateFormat.setLenient(false);
            simpleDateFormat.parse(str.substring(str.length() - 5));
            return str.length() - 6;
        } catch (ParseException e) {
            PrintStream printStream = System.out;
            printStream.println("ParseException " + e.getErrorOffset());
            return -2;
        }
    }

    private static Date testFormats(String str, String[] strArr) throws ParseException {
        for (int i = 0; i < strArr.length; i++) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strArr[i]);
                simpleDateFormat.setLenient(false);
                return simpleDateFormat.parse(str);
            } catch (ParseException unused) {
            }
        }
        return null;
    }

    private static double getNumber(String str, String[] strArr, int i) throws ParseException {
        Calendar instance = Calendar.getInstance();
        instance.setLenient(false);
        Date testFormats = testFormats(str, strArr);
        if (testFormats == null) {
            return Double.NaN;
        }
        instance.setTime(testFormats);
        return (double) instance.get(i);
    }

    private static String getNameOrAbbrev(String str, String[] strArr, String str2) throws ParseException {
        for (int i = 0; i < strArr.length; i++) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strArr[i], Locale.ENGLISH);
                simpleDateFormat.setLenient(false);
                Date parse = simpleDateFormat.parse(str);
                simpleDateFormat.applyPattern(str2);
                return simpleDateFormat.format(parse);
            } catch (ParseException unused) {
            }
        }
        return "";
    }

    private static String getNameOrAbbrev(String str) {
        return new SimpleDateFormat(str, Locale.ENGLISH).format(Calendar.getInstance().getTime());
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:37|38|39) */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:40|41|42) */
    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        r1 = new java.text.SimpleDateFormat(ohos.com.sun.org.apache.xalan.internal.lib.ExsltDatetime.gm);
        r1.setLenient(false);
        r1 = r1.parse(r12);
        r3 = new java.text.SimpleDateFormat(strip("Gy", r13));
        r3.setTimeZone(r9);
        r12 = r3.format(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0170, code lost:
        return r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        r0 = new java.text.SimpleDateFormat(ohos.com.sun.org.apache.xalan.internal.lib.ExsltDatetime.gd);
        r0.setLenient(false);
        r12 = r0.parse(r12);
        r0 = new java.text.SimpleDateFormat(strip("GyM", r13));
        r0.setTimeZone(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0191, code lost:
        return r0.format(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0192, code lost:
        return "";
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x0152 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:40:0x0171 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String formatDate(java.lang.String r12, java.lang.String r13) {
        /*
        // Method dump skipped, instructions count: 403
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xalan.internal.lib.ExsltDatetime.formatDate(java.lang.String, java.lang.String):java.lang.String");
    }

    private static String strip(String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer(str2.length());
        int i = 0;
        while (i < str2.length()) {
            char charAt = str2.charAt(i);
            if (charAt == '\'') {
                int indexOf = str2.indexOf(39, i + 1);
                if (indexOf == -1) {
                    indexOf = str2.length();
                }
                stringBuffer.append(str2.substring(i, indexOf));
                i = indexOf;
            } else {
                if (str.indexOf(charAt) <= -1) {
                    stringBuffer.append(charAt);
                }
                i++;
            }
        }
        return stringBuffer.toString();
    }
}
