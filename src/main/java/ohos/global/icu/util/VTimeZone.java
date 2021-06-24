package ohos.global.icu.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.StringTokenizer;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import ohos.com.sun.org.apache.xml.internal.utils.LocaleUtility;
import ohos.global.icu.impl.Grego;

public class VTimeZone extends BasicTimeZone {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String COLON = ":";
    private static final String COMMA = ",";
    private static final int DEF_DSTSAVINGS = 3600000;
    private static final long DEF_TZSTARTTIME = 0;
    private static final String EQUALS_SIGN = "=";
    private static final int ERR = 3;
    private static final String ICAL_BEGIN = "BEGIN";
    private static final String ICAL_BEGIN_VTIMEZONE = "BEGIN:VTIMEZONE";
    private static final String ICAL_BYDAY = "BYDAY";
    private static final String ICAL_BYMONTH = "BYMONTH";
    private static final String ICAL_BYMONTHDAY = "BYMONTHDAY";
    private static final String ICAL_DAYLIGHT = "DAYLIGHT";
    private static final String[] ICAL_DOW_NAMES = {"SU", "MO", "TU", "WE", "TH", "FR", "SA"};
    private static final String ICAL_DTSTART = "DTSTART";
    private static final String ICAL_END = "END";
    private static final String ICAL_END_VTIMEZONE = "END:VTIMEZONE";
    private static final String ICAL_FREQ = "FREQ";
    private static final String ICAL_LASTMOD = "LAST-MODIFIED";
    private static final String ICAL_RDATE = "RDATE";
    private static final String ICAL_RRULE = "RRULE";
    private static final String ICAL_STANDARD = "STANDARD";
    private static final String ICAL_TZID = "TZID";
    private static final String ICAL_TZNAME = "TZNAME";
    private static final String ICAL_TZOFFSETFROM = "TZOFFSETFROM";
    private static final String ICAL_TZOFFSETTO = "TZOFFSETTO";
    private static final String ICAL_TZURL = "TZURL";
    private static final String ICAL_UNTIL = "UNTIL";
    private static final String ICAL_VTIMEZONE = "VTIMEZONE";
    private static final String ICAL_YEARLY = "YEARLY";
    private static final String ICU_TZINFO_PROP = "X-TZINFO";
    private static String ICU_TZVERSION = null;
    private static final int INI = 0;
    private static final long MAX_TIME = Long.MAX_VALUE;
    private static final long MIN_TIME = Long.MIN_VALUE;
    private static final int[] MONTHLENGTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final String NEWLINE = "\r\n";
    private static final String SEMICOLON = ";";
    private static final int TZI = 2;
    private static final int VTZ = 1;
    private static final long serialVersionUID = -6851467294127795902L;
    private volatile transient boolean isFrozen = false;
    private Date lastmod = null;
    private String olsonzid = null;
    private BasicTimeZone tz;
    private String tzurl = null;
    private List<String> vtzlines;

    public static VTimeZone create(String str) {
        BasicTimeZone frozenICUTimeZone = TimeZone.getFrozenICUTimeZone(str, true);
        if (frozenICUTimeZone == null) {
            return null;
        }
        VTimeZone vTimeZone = new VTimeZone(str);
        vTimeZone.tz = (BasicTimeZone) frozenICUTimeZone.cloneAsThawed();
        vTimeZone.olsonzid = vTimeZone.tz.getID();
        return vTimeZone;
    }

    public static VTimeZone create(Reader reader) {
        VTimeZone vTimeZone = new VTimeZone();
        if (vTimeZone.load(reader)) {
            return vTimeZone;
        }
        return null;
    }

    @Override // ohos.global.icu.util.TimeZone
    public int getOffset(int i, int i2, int i3, int i4, int i5, int i6) {
        return this.tz.getOffset(i, i2, i3, i4, i5, i6);
    }

    @Override // ohos.global.icu.util.TimeZone
    public void getOffset(long j, boolean z, int[] iArr) {
        this.tz.getOffset(j, z, iArr);
    }

    @Override // ohos.global.icu.util.BasicTimeZone
    @Deprecated
    public void getOffsetFromLocal(long j, int i, int i2, int[] iArr) {
        this.tz.getOffsetFromLocal(j, i, i2, iArr);
    }

    @Override // ohos.global.icu.util.TimeZone
    public int getRawOffset() {
        return this.tz.getRawOffset();
    }

    @Override // ohos.global.icu.util.TimeZone
    public boolean inDaylightTime(Date date) {
        return this.tz.inDaylightTime(date);
    }

    @Override // ohos.global.icu.util.TimeZone
    public void setRawOffset(int i) {
        if (!isFrozen()) {
            this.tz.setRawOffset(i);
            return;
        }
        throw new UnsupportedOperationException("Attempt to modify a frozen VTimeZone instance.");
    }

    @Override // ohos.global.icu.util.TimeZone
    public boolean useDaylightTime() {
        return this.tz.useDaylightTime();
    }

    @Override // ohos.global.icu.util.TimeZone
    public boolean observesDaylightTime() {
        return this.tz.observesDaylightTime();
    }

    @Override // ohos.global.icu.util.TimeZone
    public boolean hasSameRules(TimeZone timeZone) {
        if (this == timeZone) {
            return true;
        }
        if (timeZone instanceof VTimeZone) {
            return this.tz.hasSameRules(((VTimeZone) timeZone).tz);
        }
        return this.tz.hasSameRules(timeZone);
    }

    public String getTZURL() {
        return this.tzurl;
    }

    public void setTZURL(String str) {
        if (!isFrozen()) {
            this.tzurl = str;
            return;
        }
        throw new UnsupportedOperationException("Attempt to modify a frozen VTimeZone instance.");
    }

    public Date getLastModified() {
        return this.lastmod;
    }

    public void setLastModified(Date date) {
        if (!isFrozen()) {
            this.lastmod = date;
            return;
        }
        throw new UnsupportedOperationException("Attempt to modify a frozen VTimeZone instance.");
    }

    public void write(Writer writer) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        List<String> list = this.vtzlines;
        if (list != null) {
            for (String str : list) {
                if (str.startsWith("TZURL:")) {
                    if (this.tzurl != null) {
                        bufferedWriter.write(ICAL_TZURL);
                        bufferedWriter.write(COLON);
                        bufferedWriter.write(this.tzurl);
                        bufferedWriter.write("\r\n");
                    }
                } else if (!str.startsWith("LAST-MODIFIED:")) {
                    bufferedWriter.write(str);
                    bufferedWriter.write("\r\n");
                } else if (this.lastmod != null) {
                    bufferedWriter.write(ICAL_LASTMOD);
                    bufferedWriter.write(COLON);
                    bufferedWriter.write(getUTCDateTimeString(this.lastmod.getTime()));
                    bufferedWriter.write("\r\n");
                }
            }
            bufferedWriter.flush();
            return;
        }
        String[] strArr = null;
        if (!(this.olsonzid == null || ICU_TZVERSION == null)) {
            strArr = new String[]{"X-TZINFO:" + this.olsonzid + "[" + ICU_TZVERSION + "]"};
        }
        writeZone(writer, this.tz, strArr);
    }

    public void write(Writer writer, long j) throws IOException {
        TimeZoneRule[] timeZoneRules = this.tz.getTimeZoneRules(j);
        RuleBasedTimeZone ruleBasedTimeZone = new RuleBasedTimeZone(this.tz.getID(), (InitialTimeZoneRule) timeZoneRules[0]);
        for (int i = 1; i < timeZoneRules.length; i++) {
            ruleBasedTimeZone.addTransitionRule(timeZoneRules[i]);
        }
        String[] strArr = null;
        if (!(this.olsonzid == null || ICU_TZVERSION == null)) {
            strArr = new String[]{"X-TZINFO:" + this.olsonzid + "[" + ICU_TZVERSION + "/Partial@" + j + "]"};
        }
        writeZone(writer, ruleBasedTimeZone, strArr);
    }

    public void writeSimple(Writer writer, long j) throws IOException {
        TimeZoneRule[] simpleTimeZoneRulesNear = this.tz.getSimpleTimeZoneRulesNear(j);
        RuleBasedTimeZone ruleBasedTimeZone = new RuleBasedTimeZone(this.tz.getID(), (InitialTimeZoneRule) simpleTimeZoneRulesNear[0]);
        for (int i = 1; i < simpleTimeZoneRulesNear.length; i++) {
            ruleBasedTimeZone.addTransitionRule(simpleTimeZoneRulesNear[i]);
        }
        String[] strArr = null;
        if (!(this.olsonzid == null || ICU_TZVERSION == null)) {
            strArr = new String[]{"X-TZINFO:" + this.olsonzid + "[" + ICU_TZVERSION + "/Simple@" + j + "]"};
        }
        writeZone(writer, ruleBasedTimeZone, strArr);
    }

    @Override // ohos.global.icu.util.BasicTimeZone
    public TimeZoneTransition getNextTransition(long j, boolean z) {
        return this.tz.getNextTransition(j, z);
    }

    @Override // ohos.global.icu.util.BasicTimeZone
    public TimeZoneTransition getPreviousTransition(long j, boolean z) {
        return this.tz.getPreviousTransition(j, z);
    }

    @Override // ohos.global.icu.util.BasicTimeZone
    public boolean hasEquivalentTransitions(TimeZone timeZone, long j, long j2) {
        if (this == timeZone) {
            return true;
        }
        return this.tz.hasEquivalentTransitions(timeZone, j, j2);
    }

    @Override // ohos.global.icu.util.BasicTimeZone
    public TimeZoneRule[] getTimeZoneRules() {
        return this.tz.getTimeZoneRules();
    }

    @Override // ohos.global.icu.util.BasicTimeZone
    public TimeZoneRule[] getTimeZoneRules(long j) {
        return this.tz.getTimeZoneRules(j);
    }

    @Override // ohos.global.icu.util.TimeZone, java.lang.Object
    public Object clone() {
        if (isFrozen()) {
            return this;
        }
        return cloneAsThawed();
    }

    static {
        try {
            ICU_TZVERSION = TimeZone.getTZDataVersion();
        } catch (MissingResourceException unused) {
            ICU_TZVERSION = null;
        }
    }

    private VTimeZone() {
    }

    private VTimeZone(String str) {
        super(str);
    }

    private boolean load(Reader reader) {
        boolean z;
        try {
            this.vtzlines = new LinkedList();
            StringBuilder sb = new StringBuilder();
            boolean z2 = false;
            boolean z3 = false;
            while (true) {
                int read = reader.read();
                z = true;
                if (read == -1) {
                    if (!z2 || !sb.toString().startsWith(ICAL_END_VTIMEZONE)) {
                        z = false;
                    } else {
                        this.vtzlines.add(sb.toString());
                    }
                } else if (read != 13) {
                    if (z3) {
                        if (!(read == 9 || read == 32)) {
                            if (z2 && sb.length() > 0) {
                                this.vtzlines.add(sb.toString());
                            }
                            sb.setLength(0);
                            if (read != 10) {
                                sb.append((char) read);
                            }
                        }
                        z3 = false;
                    } else if (read == 10) {
                        if (z2) {
                            if (sb.toString().startsWith(ICAL_END_VTIMEZONE)) {
                                this.vtzlines.add(sb.toString());
                                break;
                            }
                        } else if (sb.toString().startsWith(ICAL_BEGIN_VTIMEZONE)) {
                            this.vtzlines.add(sb.toString());
                            sb.setLength(0);
                            z3 = false;
                            z2 = true;
                        }
                        z3 = true;
                    } else {
                        sb.append((char) read);
                    }
                }
            }
            if (!z) {
                return false;
            }
            return parse();
        } catch (IOException unused) {
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v6, types: [ohos.global.icu.util.TimeArrayTimeZoneRule] */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x0200, code lost:
        if (r6.equals(ohos.global.icu.util.VTimeZone.ICAL_VTIMEZONE) != false) goto L_0x0202;
     */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x0215  */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x0210 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x017b  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x017d  */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean parse() {
        /*
        // Method dump skipped, instructions count: 799
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.VTimeZone.parse():boolean");
    }

    private static String getDefaultTZName(String str, boolean z) {
        if (z) {
            return str + "(DST)";
        }
        return str + "(STD)";
    }

    private static TimeZoneRule createRuleByRRULE(String str, int i, int i2, long j, List<String> list, int i3) {
        int i4;
        int[] iArr;
        DateTimeRule dateTimeRule;
        int length;
        int i5;
        boolean z;
        if (list == null || list.size() == 0) {
            return null;
        }
        long[] jArr = new long[1];
        int[] parseRRULE = parseRRULE(list.get(0), jArr);
        if (parseRRULE == null) {
            return null;
        }
        int i6 = parseRRULE[0];
        int i7 = parseRRULE[1];
        int i8 = parseRRULE[2];
        int i9 = 3;
        int i10 = parseRRULE[3];
        int i11 = 7;
        int i12 = -1;
        if (list.size() == 1) {
            if (parseRRULE.length > 4) {
                if (parseRRULE.length != 10 || i6 == -1 || i7 == 0) {
                    return null;
                }
                int[] iArr2 = new int[7];
                i10 = 31;
                for (int i13 = 0; i13 < 7; i13++) {
                    iArr2[i13] = parseRRULE[i13 + 3];
                    iArr2[i13] = iArr2[i13] > 0 ? iArr2[i13] : MONTHLENGTH[i6] + iArr2[i13] + 1;
                    if (iArr2[i13] < i10) {
                        i10 = iArr2[i13];
                    }
                }
                for (int i14 = 1; i14 < 7; i14++) {
                    int i15 = 0;
                    while (true) {
                        if (i15 >= 7) {
                            z = false;
                            break;
                        } else if (iArr2[i15] == i10 + i14) {
                            z = true;
                            break;
                        } else {
                            i15++;
                        }
                    }
                    if (!z) {
                        return null;
                    }
                }
            }
            iArr = null;
            i4 = i3;
        } else if (i6 == -1 || i7 == 0 || i10 == 0) {
            return null;
        } else {
            if (list.size() > 7) {
                return null;
            }
            int length2 = parseRRULE.length - 3;
            int i16 = 31;
            for (int i17 = 0; i17 < length2; i17++) {
                int i18 = parseRRULE[i17 + 3];
                if (i18 <= 0) {
                    i18 = MONTHLENGTH[i6] + i18 + 1;
                }
                if (i18 < i16) {
                    i16 = i18;
                }
            }
            int i19 = 1;
            int i20 = i6;
            int i21 = -1;
            while (i19 < list.size()) {
                long[] jArr2 = new long[1];
                int[] parseRRULE2 = parseRRULE(list.get(i19), jArr2);
                if (jArr2[0] > jArr[0]) {
                    jArr = jArr2;
                }
                if (parseRRULE2[0] == i12 || parseRRULE2[1] == 0 || parseRRULE2[i9] == 0 || (length2 = length2 + (length = parseRRULE2.length - i9)) > 7 || parseRRULE2[1] != i7) {
                    return null;
                }
                if (parseRRULE2[0] != i6) {
                    if (i21 == -1) {
                        int i22 = parseRRULE2[0] - i6;
                        if (i22 == -11 || i22 == -1) {
                            i5 = parseRRULE2[0];
                            i20 = i5;
                            i16 = 31;
                        } else if (i22 != 11 && i22 != 1) {
                            return null;
                        } else {
                            i5 = parseRRULE2[0];
                        }
                        i21 = i5;
                    } else if (!(parseRRULE2[0] == i6 || parseRRULE2[0] == i21)) {
                        return null;
                    }
                }
                if (parseRRULE2[0] == i20) {
                    for (int i23 = 0; i23 < length; i23++) {
                        int i24 = parseRRULE2[i23 + 3];
                        if (i24 <= 0) {
                            i24 = MONTHLENGTH[parseRRULE2[0]] + i24 + 1;
                        }
                        if (i24 < i16) {
                            i16 = i24;
                        }
                    }
                }
                i19++;
                i9 = 3;
                i11 = 7;
                i12 = -1;
            }
            iArr = null;
            if (length2 != i11) {
                return null;
            }
            i4 = i3;
            i6 = i20;
            i10 = i16;
        }
        int[] timeToFields = Grego.timeToFields(j + ((long) i4), iArr);
        int i25 = timeToFields[0];
        int i26 = i6 == -1 ? timeToFields[1] : i6;
        if (i7 == 0 && i8 == 0 && i10 == 0) {
            i10 = timeToFields[2];
        }
        int i27 = timeToFields[5];
        int i28 = Integer.MAX_VALUE;
        if (jArr[0] != MIN_TIME) {
            Grego.timeToFields(jArr[0], timeToFields);
            i28 = timeToFields[0];
        }
        if (i7 == 0 && i8 == 0 && i10 != 0) {
            dateTimeRule = new DateTimeRule(i26, i10, i27, 0);
        } else if (i7 != 0 && i8 != 0 && i10 == 0) {
            dateTimeRule = new DateTimeRule(i26, i8, i7, i27, 0);
        } else if (i7 == 0 || i8 != 0 || i10 == 0) {
            return null;
        } else {
            dateTimeRule = new DateTimeRule(i26, i10, i7, true, i27, 0);
        }
        return new AnnualTimeZoneRule(str, i, i2, dateTimeRule, i25, i28);
    }

    private static int[] parseRRULE(String str, long[] jArr) {
        int[] iArr;
        int i;
        int parseInt;
        StringTokenizer stringTokenizer = new StringTokenizer(str, SEMICOLON);
        int i2 = -1;
        int i3 = 0;
        int i4 = 0;
        boolean z = false;
        boolean z2 = false;
        long j = Long.MIN_VALUE;
        int[] iArr2 = null;
        int i5 = -1;
        while (true) {
            if (!stringTokenizer.hasMoreTokens()) {
                break;
            }
            String nextToken = stringTokenizer.nextToken();
            int indexOf = nextToken.indexOf(EQUALS_SIGN);
            if (indexOf == i2) {
                break;
            }
            String substring = nextToken.substring(0, indexOf);
            String substring2 = nextToken.substring(indexOf + 1);
            if (substring.equals(ICAL_FREQ)) {
                if (!substring2.equals(ICAL_YEARLY)) {
                    break;
                }
                z = true;
            } else if (substring.equals(ICAL_UNTIL)) {
                try {
                    j = parseDateTimeString(substring2, 0);
                } catch (IllegalArgumentException unused) {
                }
            } else if (substring.equals(ICAL_BYMONTH)) {
                if (substring2.length() > 2 || Integer.parseInt(substring2) - 1 < 0 || i5 >= 12) {
                    break;
                }
            } else if (substring.equals(ICAL_BYDAY)) {
                int length = substring2.length();
                if (length < 2 || length > 4) {
                    break;
                }
                if (length > 2) {
                    if (substring2.charAt(0) != '+') {
                        if (substring2.charAt(0) != '-') {
                            if (length == 4) {
                                break;
                            }
                        } else {
                            i = -1;
                            int i6 = length - 3;
                            int i7 = length - 2;
                            parseInt = Integer.parseInt(substring2.substring(i6, i7));
                            if (parseInt == 0 || parseInt > 4) {
                                break;
                            }
                            substring2 = substring2.substring(i7);
                            i4 = parseInt * i;
                        }
                    }
                    i = 1;
                    int i62 = length - 3;
                    int i72 = length - 2;
                    parseInt = Integer.parseInt(substring2.substring(i62, i72));
                    substring2 = substring2.substring(i72);
                    i4 = parseInt * i;
                }
                int i8 = 0;
                while (true) {
                    String[] strArr = ICAL_DOW_NAMES;
                    if (i8 < strArr.length && !substring2.equals(strArr[i8])) {
                        i8++;
                    }
                }
                if (i8 >= ICAL_DOW_NAMES.length) {
                    break;
                }
                i3 = i8 + 1;
            } else if (substring.equals(ICAL_BYMONTHDAY)) {
                StringTokenizer stringTokenizer2 = new StringTokenizer(substring2, COMMA);
                int[] iArr3 = new int[stringTokenizer2.countTokens()];
                int i9 = 0;
                while (stringTokenizer2.hasMoreTokens()) {
                    int i10 = i9 + 1;
                    try {
                        iArr3[i9] = Integer.parseInt(stringTokenizer2.nextToken());
                        i9 = i10;
                    } catch (NumberFormatException unused2) {
                        iArr2 = iArr3;
                        z2 = true;
                    }
                }
                iArr2 = iArr3;
            }
            i2 = -1;
        }
        z2 = true;
        if (z2 || !z) {
            return null;
        }
        jArr[0] = j;
        if (iArr2 == null) {
            iArr = new int[4];
            iArr[3] = 0;
        } else {
            iArr = new int[(iArr2.length + 3)];
            for (int i11 = 0; i11 < iArr2.length; i11++) {
                iArr[i11 + 3] = iArr2[i11];
            }
        }
        iArr[0] = i5;
        iArr[1] = i3;
        iArr[2] = i4;
        return iArr;
    }

    private static TimeZoneRule createRuleByRDATE(String str, int i, int i2, long j, List<String> list, int i3) {
        long[] jArr;
        int i4 = 0;
        if (list == null || list.size() == 0) {
            jArr = new long[]{j};
        } else {
            long[] jArr2 = new long[list.size()];
            try {
                for (String str2 : list) {
                    int i5 = i4 + 1;
                    jArr2[i4] = parseDateTimeString(str2, i3);
                    i4 = i5;
                }
                jArr = jArr2;
            } catch (IllegalArgumentException unused) {
                return null;
            }
        }
        return new TimeArrayTimeZoneRule(str, i, i2, jArr, 2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r18v7 */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x034a  */
    /* JADX WARNING: Removed duplicated region for block: B:131:0x046e  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0151  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x01b5  */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x02fa  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeZone(java.io.Writer r60, ohos.global.icu.util.BasicTimeZone r61, java.lang.String[] r62) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 1310
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.VTimeZone.writeZone(java.io.Writer, ohos.global.icu.util.BasicTimeZone, java.lang.String[]):void");
    }

    private static boolean isEquivalentDateRule(int i, int i2, int i3, DateTimeRule dateTimeRule) {
        if (i != dateTimeRule.getRuleMonth() || i3 != dateTimeRule.getRuleDayOfWeek() || dateTimeRule.getTimeRuleType() != 0) {
            return false;
        }
        if (dateTimeRule.getDateRuleType() == 1 && dateTimeRule.getRuleWeekInMonth() == i2) {
            return true;
        }
        int ruleDayOfMonth = dateTimeRule.getRuleDayOfMonth();
        if (dateTimeRule.getDateRuleType() == 2) {
            if (ruleDayOfMonth % 7 == 1 && (ruleDayOfMonth + 6) / 7 == i2) {
                return true;
            }
            if (i != 1) {
                int[] iArr = MONTHLENGTH;
                if ((iArr[i] - ruleDayOfMonth) % 7 == 6 && i2 == (((iArr[i] - ruleDayOfMonth) + 1) / 7) * -1) {
                    return true;
                }
            }
        }
        if (dateTimeRule.getDateRuleType() == 3) {
            if (ruleDayOfMonth % 7 == 0 && ruleDayOfMonth / 7 == i2) {
                return true;
            }
            if (i != 1) {
                int[] iArr2 = MONTHLENGTH;
                if ((iArr2[i] - ruleDayOfMonth) % 7 == 0 && i2 == (((iArr2[i] - ruleDayOfMonth) / 7) + 1) * -1) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private static void writeZonePropsByTime(Writer writer, boolean z, String str, int i, int i2, long j, boolean z2) throws IOException {
        beginZoneProps(writer, z, str, i, i2, j);
        if (z2) {
            writer.write(ICAL_RDATE);
            writer.write(COLON);
            writer.write(getDateTimeString(j + ((long) i)));
            writer.write("\r\n");
        }
        endZoneProps(writer, z);
    }

    private static void writeZonePropsByDOM(Writer writer, boolean z, String str, int i, int i2, int i3, int i4, long j, long j2) throws IOException {
        beginZoneProps(writer, z, str, i, i2, j);
        beginRRULE(writer, i3);
        writer.write(ICAL_BYMONTHDAY);
        writer.write(EQUALS_SIGN);
        writer.write(Integer.toString(i4));
        if (j2 != MAX_TIME) {
            appendUNTIL(writer, getDateTimeString(j2 + ((long) i)));
        }
        writer.write("\r\n");
        endZoneProps(writer, z);
    }

    private static void writeZonePropsByDOW(Writer writer, boolean z, String str, int i, int i2, int i3, int i4, int i5, long j, long j2) throws IOException {
        beginZoneProps(writer, z, str, i, i2, j);
        beginRRULE(writer, i3);
        writer.write(ICAL_BYDAY);
        writer.write(EQUALS_SIGN);
        writer.write(Integer.toString(i4));
        writer.write(ICAL_DOW_NAMES[i5 - 1]);
        if (j2 != MAX_TIME) {
            appendUNTIL(writer, getDateTimeString(j2 + ((long) i)));
        }
        writer.write("\r\n");
        endZoneProps(writer, z);
    }

    private static void writeZonePropsByDOW_GEQ_DOM(Writer writer, boolean z, String str, int i, int i2, int i3, int i4, int i5, long j, long j2) throws IOException {
        int i6;
        int i7;
        int i8 = 7;
        if (i4 % 7 == 1) {
            writeZonePropsByDOW(writer, z, str, i, i2, i3, (i4 + 6) / 7, i5, j, j2);
            return;
        }
        if (i3 != 1) {
            int[] iArr = MONTHLENGTH;
            if ((iArr[i3] - i4) % 7 == 6) {
                writeZonePropsByDOW(writer, z, str, i, i2, i3, (((iArr[i3] - i4) + 1) / 7) * -1, i5, j, j2);
                return;
            }
        }
        beginZoneProps(writer, z, str, i, i2, j);
        if (i4 <= 0) {
            int i9 = 1 - i4;
            int i10 = 7 - i9;
            int i11 = i3 - 1;
            writeZonePropsByDOW_GEQ_DOM_sub(writer, i11 < 0 ? 11 : i11, -i9, i5, i9, MAX_TIME, i);
            i6 = i10;
            i7 = 1;
        } else {
            int i12 = i4 + 6;
            int[] iArr2 = MONTHLENGTH;
            if (i12 > iArr2[i3]) {
                int i13 = i12 - iArr2[i3];
                i8 = 7 - i13;
                int i14 = i3 + 1;
                writeZonePropsByDOW_GEQ_DOM_sub(writer, i14 > 11 ? 0 : i14, 1, i5, i13, MAX_TIME, i);
            }
            i6 = i8;
            i7 = i4;
        }
        writeZonePropsByDOW_GEQ_DOM_sub(writer, i3, i7, i5, i6, j2, i);
        endZoneProps(writer, z);
    }

    private static void writeZonePropsByDOW_GEQ_DOM_sub(Writer writer, int i, int i2, int i3, int i4, long j, int i5) throws IOException {
        boolean z = i == 1;
        if (i2 < 0 && !z) {
            i2 = MONTHLENGTH[i] + i2 + 1;
        }
        beginRRULE(writer, i);
        writer.write(ICAL_BYDAY);
        writer.write(EQUALS_SIGN);
        writer.write(ICAL_DOW_NAMES[i3 - 1]);
        writer.write(SEMICOLON);
        writer.write(ICAL_BYMONTHDAY);
        writer.write(EQUALS_SIGN);
        writer.write(Integer.toString(i2));
        for (int i6 = 1; i6 < i4; i6++) {
            writer.write(COMMA);
            writer.write(Integer.toString(i2 + i6));
        }
        if (j != MAX_TIME) {
            appendUNTIL(writer, getDateTimeString(j + ((long) i5)));
        }
        writer.write("\r\n");
    }

    private static void writeZonePropsByDOW_LEQ_DOM(Writer writer, boolean z, String str, int i, int i2, int i3, int i4, int i5, long j, long j2) throws IOException {
        if (i4 % 7 == 0) {
            writeZonePropsByDOW(writer, z, str, i, i2, i3, i4 / 7, i5, j, j2);
            return;
        }
        if (i3 != 1) {
            int[] iArr = MONTHLENGTH;
            if ((iArr[i3] - i4) % 7 == 0) {
                writeZonePropsByDOW(writer, z, str, i, i2, i3, (((iArr[i3] - i4) / 7) + 1) * -1, i5, j, j2);
                return;
            }
        }
        if (i3 == 1 && i4 == 29) {
            writeZonePropsByDOW(writer, z, str, i, i2, 1, -1, i5, j, j2);
        } else {
            writeZonePropsByDOW_GEQ_DOM(writer, z, str, i, i2, i3, i4 - 6, i5, j, j2);
        }
    }

    private static void writeFinalRule(Writer writer, boolean z, AnnualTimeZoneRule annualTimeZoneRule, int i, int i2, long j) throws IOException {
        DateTimeRule wallTimeRule = toWallTimeRule(annualTimeZoneRule.getRule(), i, i2);
        int ruleMillisInDay = wallTimeRule.getRuleMillisInDay();
        long j2 = ruleMillisInDay < 0 ? j + ((long) (0 - ruleMillisInDay)) : ruleMillisInDay >= 86400000 ? j - ((long) (ruleMillisInDay - 86399999)) : j;
        int rawOffset = annualTimeZoneRule.getRawOffset() + annualTimeZoneRule.getDSTSavings();
        int dateRuleType = wallTimeRule.getDateRuleType();
        if (dateRuleType == 0) {
            writeZonePropsByDOM(writer, z, annualTimeZoneRule.getName(), i + i2, rawOffset, wallTimeRule.getRuleMonth(), wallTimeRule.getRuleDayOfMonth(), j2, MAX_TIME);
        } else if (dateRuleType == 1) {
            writeZonePropsByDOW(writer, z, annualTimeZoneRule.getName(), i + i2, rawOffset, wallTimeRule.getRuleMonth(), wallTimeRule.getRuleWeekInMonth(), wallTimeRule.getRuleDayOfWeek(), j2, MAX_TIME);
        } else if (dateRuleType == 2) {
            writeZonePropsByDOW_GEQ_DOM(writer, z, annualTimeZoneRule.getName(), i + i2, rawOffset, wallTimeRule.getRuleMonth(), wallTimeRule.getRuleDayOfMonth(), wallTimeRule.getRuleDayOfWeek(), j2, MAX_TIME);
        } else if (dateRuleType == 3) {
            writeZonePropsByDOW_LEQ_DOM(writer, z, annualTimeZoneRule.getName(), i + i2, rawOffset, wallTimeRule.getRuleMonth(), wallTimeRule.getRuleDayOfMonth(), wallTimeRule.getRuleDayOfWeek(), j2, MAX_TIME);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0041  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0089  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0093  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static ohos.global.icu.util.DateTimeRule toWallTimeRule(ohos.global.icu.util.DateTimeRule r11, int r12, int r13) {
        /*
        // Method dump skipped, instructions count: 161
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.VTimeZone.toWallTimeRule(ohos.global.icu.util.DateTimeRule, int, int):ohos.global.icu.util.DateTimeRule");
    }

    private static void beginZoneProps(Writer writer, boolean z, String str, int i, int i2, long j) throws IOException {
        writer.write(ICAL_BEGIN);
        writer.write(COLON);
        if (z) {
            writer.write(ICAL_DAYLIGHT);
        } else {
            writer.write(ICAL_STANDARD);
        }
        writer.write("\r\n");
        writer.write(ICAL_TZOFFSETTO);
        writer.write(COLON);
        writer.write(millisToOffset(i2));
        writer.write("\r\n");
        writer.write(ICAL_TZOFFSETFROM);
        writer.write(COLON);
        writer.write(millisToOffset(i));
        writer.write("\r\n");
        writer.write(ICAL_TZNAME);
        writer.write(COLON);
        writer.write(str);
        writer.write("\r\n");
        writer.write(ICAL_DTSTART);
        writer.write(COLON);
        writer.write(getDateTimeString(j + ((long) i)));
        writer.write("\r\n");
    }

    private static void endZoneProps(Writer writer, boolean z) throws IOException {
        writer.write(ICAL_END);
        writer.write(COLON);
        if (z) {
            writer.write(ICAL_DAYLIGHT);
        } else {
            writer.write(ICAL_STANDARD);
        }
        writer.write("\r\n");
    }

    private static void beginRRULE(Writer writer, int i) throws IOException {
        writer.write(ICAL_RRULE);
        writer.write(COLON);
        writer.write(ICAL_FREQ);
        writer.write(EQUALS_SIGN);
        writer.write(ICAL_YEARLY);
        writer.write(SEMICOLON);
        writer.write(ICAL_BYMONTH);
        writer.write(EQUALS_SIGN);
        writer.write(Integer.toString(i + 1));
        writer.write(SEMICOLON);
    }

    private static void appendUNTIL(Writer writer, String str) throws IOException {
        if (str != null) {
            writer.write(SEMICOLON);
            writer.write(ICAL_UNTIL);
            writer.write(EQUALS_SIGN);
            writer.write(str);
        }
    }

    private void writeHeader(Writer writer) throws IOException {
        writer.write(ICAL_BEGIN);
        writer.write(COLON);
        writer.write(ICAL_VTIMEZONE);
        writer.write("\r\n");
        writer.write(ICAL_TZID);
        writer.write(COLON);
        writer.write(this.tz.getID());
        writer.write("\r\n");
        if (this.tzurl != null) {
            writer.write(ICAL_TZURL);
            writer.write(COLON);
            writer.write(this.tzurl);
            writer.write("\r\n");
        }
        if (this.lastmod != null) {
            writer.write(ICAL_LASTMOD);
            writer.write(COLON);
            writer.write(getUTCDateTimeString(this.lastmod.getTime()));
            writer.write("\r\n");
        }
    }

    private static void writeFooter(Writer writer) throws IOException {
        writer.write(ICAL_END);
        writer.write(COLON);
        writer.write(ICAL_VTIMEZONE);
        writer.write("\r\n");
    }

    private static String getDateTimeString(long j) {
        int[] timeToFields = Grego.timeToFields(j, null);
        StringBuilder sb = new StringBuilder(15);
        sb.append(numToString(timeToFields[0], 4));
        sb.append(numToString(timeToFields[1] + 1, 2));
        sb.append(numToString(timeToFields[2], 2));
        sb.append('T');
        int i = timeToFields[5];
        int i2 = i / 3600000;
        int i3 = i % 3600000;
        sb.append(numToString(i2, 2));
        sb.append(numToString(i3 / 60000, 2));
        sb.append(numToString((i3 % 60000) / 1000, 2));
        return sb.toString();
    }

    private static String getUTCDateTimeString(long j) {
        return getDateTimeString(j) + Constants.HASIDCALL_INDEX_SIG;
    }

    /* JADX WARNING: Removed duplicated region for block: B:60:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00ba  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static long parseDateTimeString(java.lang.String r11, int r12) {
        /*
        // Method dump skipped, instructions count: 194
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.VTimeZone.parseDateTimeString(java.lang.String, int):long");
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0054  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int offsetStrToMillis(java.lang.String r8) {
        /*
            r0 = 1
            r1 = 0
            if (r8 != 0) goto L_0x0005
            goto L_0x0044
        L_0x0005:
            int r2 = r8.length()
            r3 = 7
            r4 = 5
            if (r2 == r4) goto L_0x0010
            if (r2 == r3) goto L_0x0010
            goto L_0x0044
        L_0x0010:
            char r5 = r8.charAt(r1)
            r6 = 43
            if (r5 != r6) goto L_0x001a
            r5 = r0
            goto L_0x001f
        L_0x001a:
            r6 = 45
            if (r5 != r6) goto L_0x0044
            r5 = -1
        L_0x001f:
            r6 = 3
            java.lang.String r7 = r8.substring(r0, r6)     // Catch:{ NumberFormatException -> 0x0040 }
            int r7 = java.lang.Integer.parseInt(r7)     // Catch:{ NumberFormatException -> 0x0040 }
            java.lang.String r6 = r8.substring(r6, r4)     // Catch:{ NumberFormatException -> 0x003e }
            int r6 = java.lang.Integer.parseInt(r6)     // Catch:{ NumberFormatException -> 0x003e }
            if (r2 != r3) goto L_0x003b
            java.lang.String r8 = r8.substring(r4, r3)     // Catch:{ NumberFormatException -> 0x0042 }
            int r8 = java.lang.Integer.parseInt(r8)     // Catch:{ NumberFormatException -> 0x0042 }
            r1 = r8
        L_0x003b:
            r8 = r1
            r1 = r0
            goto L_0x0048
        L_0x003e:
            r6 = r1
            goto L_0x0042
        L_0x0040:
            r6 = r1
            r7 = r6
        L_0x0042:
            r8 = r1
            goto L_0x0048
        L_0x0044:
            r8 = r1
            r5 = r8
            r6 = r5
            r7 = r6
        L_0x0048:
            if (r1 == 0) goto L_0x0054
            int r7 = r7 * 60
            int r7 = r7 + r6
            int r7 = r7 * 60
            int r7 = r7 + r8
            int r5 = r5 * r7
            int r5 = r5 * 1000
            return r5
        L_0x0054:
            java.lang.IllegalArgumentException r8 = new java.lang.IllegalArgumentException
            java.lang.String r0 = "Bad offset string"
            r8.<init>(r0)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.VTimeZone.offsetStrToMillis(java.lang.String):int");
    }

    private static String millisToOffset(int i) {
        StringBuilder sb = new StringBuilder(7);
        if (i >= 0) {
            sb.append('+');
        } else {
            sb.append(LocaleUtility.IETF_SEPARATOR);
            i = -i;
        }
        int i2 = i / 1000;
        int i3 = i2 % 60;
        int i4 = (i2 - i3) / 60;
        sb.append(numToString(i4 / 60, 2));
        sb.append(numToString(i4 % 60, 2));
        sb.append(numToString(i3, 2));
        return sb.toString();
    }

    private static String numToString(int i, int i2) {
        String num = Integer.toString(i);
        int length = num.length();
        if (length >= i2) {
            return num.substring(length - i2, length);
        }
        StringBuilder sb = new StringBuilder(i2);
        while (length < i2) {
            sb.append('0');
            length++;
        }
        sb.append(num);
        return sb.toString();
    }

    @Override // ohos.global.icu.util.TimeZone, ohos.global.icu.util.Freezable
    public boolean isFrozen() {
        return this.isFrozen;
    }

    @Override // ohos.global.icu.util.TimeZone, ohos.global.icu.util.TimeZone, ohos.global.icu.util.Freezable
    public TimeZone freeze() {
        this.isFrozen = true;
        return this;
    }

    @Override // ohos.global.icu.util.TimeZone, ohos.global.icu.util.TimeZone, ohos.global.icu.util.Freezable
    public TimeZone cloneAsThawed() {
        VTimeZone vTimeZone = (VTimeZone) super.cloneAsThawed();
        vTimeZone.tz = (BasicTimeZone) this.tz.cloneAsThawed();
        vTimeZone.isFrozen = false;
        return vTimeZone;
    }
}
