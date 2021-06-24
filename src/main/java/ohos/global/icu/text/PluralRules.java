package ohos.global.icu.text;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import ohos.bluetooth.A2dpCodecInfo;
import ohos.com.sun.org.apache.xalan.internal.templates.Constants;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import ohos.com.sun.org.apache.xpath.internal.XPath;
import ohos.global.icu.impl.PluralRulesLoader;
import ohos.global.icu.number.FormattedNumber;
import ohos.global.icu.util.Output;
import ohos.global.icu.util.ULocale;

public class PluralRules implements Serializable {
    static final UnicodeSet ALLOWED_ID = new UnicodeSet("[a-z]").freeze();
    static final Pattern AND_SEPARATED = Pattern.compile("\\s*and\\s*");
    static final Pattern AT_SEPARATED = Pattern.compile("\\s*\\Q\\E@\\s*");
    private static final String CATEGORY_SEPARATOR = ";  ";
    static final Pattern COMMA_SEPARATED = Pattern.compile("\\s*,\\s*");
    public static final PluralRules DEFAULT = new PluralRules(new RuleList().addRule(DEFAULT_RULE));
    private static final Rule DEFAULT_RULE = new Rule("other", NO_CONSTRAINT, null, null);
    static final Pattern DOTDOT_SEPARATED = Pattern.compile("\\s*\\Q..\\E\\s*");
    public static final String KEYWORD_FEW = "few";
    public static final String KEYWORD_MANY = "many";
    public static final String KEYWORD_ONE = "one";
    public static final String KEYWORD_OTHER = "other";
    public static final String KEYWORD_TWO = "two";
    public static final String KEYWORD_ZERO = "zero";
    private static final Constraint NO_CONSTRAINT = new Constraint() {
        /* class ohos.global.icu.text.PluralRules.AnonymousClass1 */
        private static final long serialVersionUID = 9163464945387899416L;

        @Override // ohos.global.icu.text.PluralRules.Constraint
        public boolean isFulfilled(IFixedDecimal iFixedDecimal) {
            return true;
        }

        @Override // ohos.global.icu.text.PluralRules.Constraint
        public boolean isLimited(SampleType sampleType) {
            return false;
        }

        public String toString() {
            return "";
        }
    };
    public static final double NO_UNIQUE_VALUE = -0.00123456777d;
    static final Pattern OR_SEPARATED = Pattern.compile("\\s*or\\s*");
    static final Pattern SEMI_SEPARATED = Pattern.compile("\\s*;\\s*");
    static final Pattern TILDE_SEPARATED = Pattern.compile("\\s*~\\s*");
    private static final long serialVersionUID = 1;
    private final transient Set<String> keywords;
    private final RuleList rules;

    /* access modifiers changed from: private */
    public interface Constraint extends Serializable {
        boolean isFulfilled(IFixedDecimal iFixedDecimal);

        boolean isLimited(SampleType sampleType);
    }

    @Deprecated
    public interface IFixedDecimal {
        @Deprecated
        double getPluralOperand(Operand operand);

        @Deprecated
        boolean isInfinite();

        @Deprecated
        boolean isNaN();
    }

    public enum KeywordStatus {
        INVALID,
        SUPPRESSED,
        UNIQUE,
        BOUNDED,
        UNBOUNDED
    }

    @Deprecated
    public enum Operand {
        n,
        i,
        f,
        t,
        v,
        w,
        e,
        j
    }

    public enum PluralType {
        CARDINAL,
        ORDINAL
    }

    @Deprecated
    public enum SampleType {
        INTEGER,
        DECIMAL
    }

    @Deprecated
    public static abstract class Factory {
        @Deprecated
        public abstract PluralRules forLocale(ULocale uLocale, PluralType pluralType);

        @Deprecated
        public abstract ULocale[] getAvailableULocales();

        @Deprecated
        public abstract ULocale getFunctionalEquivalent(ULocale uLocale, boolean[] zArr);

        @Deprecated
        public abstract boolean hasOverride(ULocale uLocale);

        @Deprecated
        protected Factory() {
        }

        @Deprecated
        public final PluralRules forLocale(ULocale uLocale) {
            return forLocale(uLocale, PluralType.CARDINAL);
        }

        @Deprecated
        public static PluralRulesLoader getDefaultFactory() {
            return PluralRulesLoader.loader;
        }
    }

    public static PluralRules parseDescription(String str) throws ParseException {
        String trim = str.trim();
        return trim.length() == 0 ? DEFAULT : new PluralRules(parseRuleChain(trim));
    }

    public static PluralRules createRules(String str) {
        try {
            return parseDescription(str);
        } catch (Exception unused) {
            return null;
        }
    }

    @Deprecated
    public static class FixedDecimal extends Number implements Comparable<FixedDecimal>, IFixedDecimal {
        static final long MAX = 1000000000000000000L;
        private static final long MAX_INTEGER_PART = 1000000000;
        private static final long serialVersionUID = -4756200506571685661L;
        private final int baseFactor;
        final long decimalDigits;
        final long decimalDigitsWithoutTrailingZeros;
        final boolean hasIntegerValue;
        final long integerValue;
        final boolean isNegative;
        final double source;
        final int visibleDecimalDigitCount;
        final int visibleDecimalDigitCountWithoutTrailingZeros;

        @Deprecated
        public double getSource() {
            return this.source;
        }

        @Deprecated
        public int getVisibleDecimalDigitCount() {
            return this.visibleDecimalDigitCount;
        }

        @Deprecated
        public int getVisibleDecimalDigitCountWithoutTrailingZeros() {
            return this.visibleDecimalDigitCountWithoutTrailingZeros;
        }

        @Deprecated
        public long getDecimalDigits() {
            return this.decimalDigits;
        }

        @Deprecated
        public long getDecimalDigitsWithoutTrailingZeros() {
            return this.decimalDigitsWithoutTrailingZeros;
        }

        @Deprecated
        public long getIntegerValue() {
            return this.integerValue;
        }

        @Deprecated
        public boolean isHasIntegerValue() {
            return this.hasIntegerValue;
        }

        @Deprecated
        public boolean isNegative() {
            return this.isNegative;
        }

        @Deprecated
        public int getBaseFactor() {
            return this.baseFactor;
        }

        @Deprecated
        public FixedDecimal(double d, int i, long j) {
            boolean z = true;
            this.isNegative = d < XPath.MATCH_SCORE_QNAME;
            this.source = this.isNegative ? -d : d;
            this.visibleDecimalDigitCount = i;
            this.decimalDigits = j;
            this.integerValue = d > 1.0E18d ? MAX : (long) d;
            this.hasIntegerValue = this.source != ((double) this.integerValue) ? false : z;
            if (j == 0) {
                this.decimalDigitsWithoutTrailingZeros = 0;
                this.visibleDecimalDigitCountWithoutTrailingZeros = 0;
            } else {
                int i2 = i;
                while (j % 10 == 0) {
                    j /= 10;
                    i2--;
                }
                this.decimalDigitsWithoutTrailingZeros = j;
                this.visibleDecimalDigitCountWithoutTrailingZeros = i2;
            }
            this.baseFactor = (int) Math.pow(10.0d, (double) i);
        }

        @Deprecated
        public FixedDecimal(double d, int i) {
            this(d, i, (long) getFractionalDigits(d, i));
        }

        private static int getFractionalDigits(double d, int i) {
            if (i == 0) {
                return 0;
            }
            if (d < XPath.MATCH_SCORE_QNAME) {
                d = -d;
            }
            int pow = (int) Math.pow(10.0d, (double) i);
            return (int) (Math.round(d * ((double) pow)) % ((long) pow));
        }

        @Deprecated
        public FixedDecimal(double d) {
            this(d, decimals(d));
        }

        @Deprecated
        public FixedDecimal(long j) {
            this((double) j, 0);
        }

        @Deprecated
        public static int decimals(double d) {
            if (Double.isInfinite(d) || Double.isNaN(d)) {
                return 0;
            }
            if (d < XPath.MATCH_SCORE_QNAME) {
                d = -d;
            }
            if (d == Math.floor(d)) {
                return 0;
            }
            if (d < 1.0E9d) {
                long j = ((long) (d * 1000000.0d)) % 1000000;
                int i = 10;
                for (int i2 = 6; i2 > 0; i2--) {
                    if (j % ((long) i) != 0) {
                        return i2;
                    }
                    i *= 10;
                }
                return 0;
            }
            String format = String.format(Locale.ENGLISH, "%1.15e", Double.valueOf(d));
            int lastIndexOf = format.lastIndexOf(101);
            int i3 = lastIndexOf + 1;
            if (format.charAt(i3) == '+') {
                i3++;
            }
            int parseInt = (lastIndexOf - 2) - Integer.parseInt(format.substring(i3));
            if (parseInt < 0) {
                return 0;
            }
            int i4 = lastIndexOf - 1;
            while (parseInt > 0 && format.charAt(i4) == '0') {
                parseInt--;
                i4--;
            }
            return parseInt;
        }

        @Deprecated
        public FixedDecimal(String str) {
            this(Double.parseDouble(str), getVisibleFractionCount(str));
        }

        private static int getVisibleFractionCount(String str) {
            String trim = str.trim();
            int indexOf = trim.indexOf(46) + 1;
            if (indexOf == 0) {
                return 0;
            }
            return trim.length() - indexOf;
        }

        @Override // ohos.global.icu.text.PluralRules.IFixedDecimal
        @Deprecated
        public double getPluralOperand(Operand operand) {
            switch (operand) {
                case n:
                    return this.source;
                case i:
                    return (double) this.integerValue;
                case f:
                    return (double) this.decimalDigits;
                case t:
                    return (double) this.decimalDigitsWithoutTrailingZeros;
                case v:
                    return (double) this.visibleDecimalDigitCount;
                case w:
                    return (double) this.visibleDecimalDigitCountWithoutTrailingZeros;
                case e:
                    return XPath.MATCH_SCORE_QNAME;
                default:
                    return this.source;
            }
        }

        @Deprecated
        public static Operand getOperand(String str) {
            return Operand.valueOf(str);
        }

        @Deprecated
        public int compareTo(FixedDecimal fixedDecimal) {
            long j = this.integerValue;
            long j2 = fixedDecimal.integerValue;
            if (j != j2) {
                return j < j2 ? -1 : 1;
            }
            double d = this.source;
            double d2 = fixedDecimal.source;
            if (d != d2) {
                return d < d2 ? -1 : 1;
            }
            int i = this.visibleDecimalDigitCount;
            int i2 = fixedDecimal.visibleDecimalDigitCount;
            if (i != i2) {
                return i < i2 ? -1 : 1;
            }
            int i3 = ((this.decimalDigits - fixedDecimal.decimalDigits) > 0 ? 1 : ((this.decimalDigits - fixedDecimal.decimalDigits) == 0 ? 0 : -1));
            if (i3 != 0) {
                return i3 < 0 ? -1 : 1;
            }
            return 0;
        }

        @Deprecated
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof FixedDecimal)) {
                return false;
            }
            FixedDecimal fixedDecimal = (FixedDecimal) obj;
            return this.source == fixedDecimal.source && this.visibleDecimalDigitCount == fixedDecimal.visibleDecimalDigitCount && this.decimalDigits == fixedDecimal.decimalDigits;
        }

        @Deprecated
        public int hashCode() {
            return (int) (this.decimalDigits + ((long) ((this.visibleDecimalDigitCount + ((int) (this.source * 37.0d))) * 37)));
        }

        @Deprecated
        public String toString() {
            Locale locale = Locale.ROOT;
            return String.format(locale, "%." + this.visibleDecimalDigitCount + "f", Double.valueOf(this.source));
        }

        @Deprecated
        public boolean hasIntegerValue() {
            return this.hasIntegerValue;
        }

        @Deprecated
        public int intValue() {
            return (int) this.integerValue;
        }

        @Deprecated
        public long longValue() {
            return this.integerValue;
        }

        @Deprecated
        public float floatValue() {
            return (float) this.source;
        }

        @Deprecated
        public double doubleValue() {
            return this.isNegative ? -this.source : this.source;
        }

        @Deprecated
        public long getShiftedValue() {
            return (this.integerValue * ((long) this.baseFactor)) + this.decimalDigits;
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            throw new NotSerializableException();
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            throw new NotSerializableException();
        }

        @Override // ohos.global.icu.text.PluralRules.IFixedDecimal
        @Deprecated
        public boolean isNaN() {
            return Double.isNaN(this.source);
        }

        @Override // ohos.global.icu.text.PluralRules.IFixedDecimal
        @Deprecated
        public boolean isInfinite() {
            return Double.isInfinite(this.source);
        }
    }

    @Deprecated
    public static class FixedDecimalRange {
        @Deprecated
        public final FixedDecimal end;
        @Deprecated
        public final FixedDecimal start;

        @Deprecated
        public FixedDecimalRange(FixedDecimal fixedDecimal, FixedDecimal fixedDecimal2) {
            if (fixedDecimal.visibleDecimalDigitCount == fixedDecimal2.visibleDecimalDigitCount) {
                this.start = fixedDecimal;
                this.end = fixedDecimal2;
                return;
            }
            throw new IllegalArgumentException("Ranges must have the same number of visible decimals: " + fixedDecimal + "~" + fixedDecimal2);
        }

        @Deprecated
        public String toString() {
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append(this.start);
            if (this.end == this.start) {
                str = "";
            } else {
                str = "~" + this.end;
            }
            sb.append(str);
            return sb.toString();
        }
    }

    @Deprecated
    public static class FixedDecimalSamples {
        @Deprecated
        public final boolean bounded;
        @Deprecated
        public final SampleType sampleType;
        @Deprecated
        public final Set<FixedDecimalRange> samples;

        private FixedDecimalSamples(SampleType sampleType2, Set<FixedDecimalRange> set, boolean z) {
            this.sampleType = sampleType2;
            this.samples = set;
            this.bounded = z;
        }

        static FixedDecimalSamples parse(String str) {
            SampleType sampleType2;
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            if (str.startsWith("integer")) {
                sampleType2 = SampleType.INTEGER;
            } else if (str.startsWith(SchemaSymbols.ATTVAL_DECIMAL)) {
                sampleType2 = SampleType.DECIMAL;
            } else {
                throw new IllegalArgumentException("Samples must start with 'integer' or 'decimal'");
            }
            String[] split = PluralRules.COMMA_SEPARATED.split(str.substring(7).trim());
            boolean z = false;
            boolean z2 = true;
            for (String str2 : split) {
                if (str2.equals("…") || str2.equals("...")) {
                    z2 = false;
                    z = true;
                } else if (!z) {
                    String[] split2 = PluralRules.TILDE_SEPARATED.split(str2);
                    int length = split2.length;
                    if (length == 1) {
                        FixedDecimal fixedDecimal = new FixedDecimal(split2[0]);
                        checkDecimal(sampleType2, fixedDecimal);
                        linkedHashSet.add(new FixedDecimalRange(fixedDecimal, fixedDecimal));
                    } else if (length == 2) {
                        FixedDecimal fixedDecimal2 = new FixedDecimal(split2[0]);
                        FixedDecimal fixedDecimal3 = new FixedDecimal(split2[1]);
                        checkDecimal(sampleType2, fixedDecimal2);
                        checkDecimal(sampleType2, fixedDecimal3);
                        linkedHashSet.add(new FixedDecimalRange(fixedDecimal2, fixedDecimal3));
                    } else {
                        throw new IllegalArgumentException("Ill-formed number range: " + str2);
                    }
                } else {
                    throw new IllegalArgumentException("Can only have … at the end of samples: " + str2);
                }
            }
            return new FixedDecimalSamples(sampleType2, Collections.unmodifiableSet(linkedHashSet), z2);
        }

        private static void checkDecimal(SampleType sampleType2, FixedDecimal fixedDecimal) {
            boolean z = true;
            boolean z2 = sampleType2 == SampleType.INTEGER;
            if (fixedDecimal.getVisibleDecimalDigitCount() != 0) {
                z = false;
            }
            if (z2 != z) {
                throw new IllegalArgumentException("Ill-formed number range: " + fixedDecimal);
            }
        }

        @Deprecated
        public Set<Double> addSamples(Set<Double> set) {
            for (FixedDecimalRange fixedDecimalRange : this.samples) {
                long shiftedValue = fixedDecimalRange.end.getShiftedValue();
                for (long shiftedValue2 = fixedDecimalRange.start.getShiftedValue(); shiftedValue2 <= shiftedValue; shiftedValue2++) {
                    set.add(Double.valueOf(((double) shiftedValue2) / ((double) fixedDecimalRange.start.baseFactor)));
                }
            }
            return set;
        }

        @Deprecated
        public String toString() {
            StringBuilder sb = new StringBuilder("@");
            sb.append(this.sampleType.toString().toLowerCase(Locale.ENGLISH));
            boolean z = true;
            for (FixedDecimalRange fixedDecimalRange : this.samples) {
                if (z) {
                    z = false;
                } else {
                    sb.append(",");
                }
                sb.append(' ');
                sb.append(fixedDecimalRange);
            }
            if (!this.bounded) {
                sb.append(", …");
            }
            return sb.toString();
        }

        @Deprecated
        public Set<FixedDecimalRange> getSamples() {
            return this.samples;
        }

        @Deprecated
        public void getStartEndSamples(Set<FixedDecimal> set) {
            for (FixedDecimalRange fixedDecimalRange : this.samples) {
                set.add(fixedDecimalRange.start);
                set.add(fixedDecimalRange.end);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public static class SimpleTokenizer {
        static final UnicodeSet BREAK_AND_IGNORE = new UnicodeSet(9, 10, 12, 13, 32, 32).freeze();
        static final UnicodeSet BREAK_AND_KEEP = new UnicodeSet(33, 33, 37, 37, 44, 44, 46, 46, 61, 61).freeze();

        SimpleTokenizer() {
        }

        static String[] split(String str) {
            ArrayList arrayList = new ArrayList();
            int i = -1;
            for (int i2 = 0; i2 < str.length(); i2++) {
                char charAt = str.charAt(i2);
                if (BREAK_AND_IGNORE.contains(charAt)) {
                    if (i >= 0) {
                        arrayList.add(str.substring(i, i2));
                    }
                } else if (BREAK_AND_KEEP.contains(charAt)) {
                    if (i >= 0) {
                        arrayList.add(str.substring(i, i2));
                    }
                    arrayList.add(str.substring(i2, i2 + 1));
                } else {
                    if (i < 0) {
                        i = i2;
                    }
                }
                i = -1;
            }
            if (i >= 0) {
                arrayList.add(str.substring(i));
            }
            return (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:138:0x021b A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00df  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00f2  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0113  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x016e  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0179  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static ohos.global.icu.text.PluralRules.Constraint parseConstraint(java.lang.String r33) throws java.text.ParseException {
        /*
        // Method dump skipped, instructions count: 635
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.PluralRules.parseConstraint(java.lang.String):ohos.global.icu.text.PluralRules$Constraint");
    }

    private static ParseException unexpected(String str, String str2) {
        return new ParseException("unexpected token '" + str + "' in '" + str2 + "'", -1);
    }

    private static String nextToken(String[] strArr, int i, String str) throws ParseException {
        if (i < strArr.length) {
            return strArr[i];
        }
        throw new ParseException("missing token at end of '" + str + "'", -1);
    }

    /* access modifiers changed from: private */
    public static Rule parseRule(String str) throws ParseException {
        FixedDecimalSamples fixedDecimalSamples;
        Constraint constraint;
        if (str.length() == 0) {
            return DEFAULT_RULE;
        }
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        int indexOf = lowerCase.indexOf(58);
        if (indexOf != -1) {
            String trim = lowerCase.substring(0, indexOf).trim();
            if (isValidKeyword(trim)) {
                boolean z = true;
                String trim2 = lowerCase.substring(indexOf + 1).trim();
                String[] split = AT_SEPARATED.split(trim2);
                int length = split.length;
                FixedDecimalSamples fixedDecimalSamples2 = null;
                if (length == 1) {
                    fixedDecimalSamples = null;
                } else if (length == 2) {
                    fixedDecimalSamples = FixedDecimalSamples.parse(split[1]);
                    if (fixedDecimalSamples.sampleType != SampleType.DECIMAL) {
                        fixedDecimalSamples2 = fixedDecimalSamples;
                        fixedDecimalSamples = null;
                    }
                } else if (length == 3) {
                    fixedDecimalSamples2 = FixedDecimalSamples.parse(split[1]);
                    FixedDecimalSamples parse = FixedDecimalSamples.parse(split[2]);
                    if (fixedDecimalSamples2.sampleType == SampleType.INTEGER && parse.sampleType == SampleType.DECIMAL) {
                        fixedDecimalSamples = parse;
                    } else {
                        throw new IllegalArgumentException("Must have @integer then @decimal in " + trim2);
                    }
                } else {
                    throw new IllegalArgumentException("Too many samples in " + trim2);
                }
                boolean equals = trim.equals("other");
                if (split[0].length() != 0) {
                    z = false;
                }
                if (equals == z) {
                    if (equals) {
                        constraint = NO_CONSTRAINT;
                    } else {
                        constraint = parseConstraint(split[0]);
                    }
                    return new Rule(trim, constraint, fixedDecimalSamples2, fixedDecimalSamples);
                }
                throw new IllegalArgumentException("The keyword 'other' must have no constraints, just samples.");
            }
            throw new ParseException("keyword '" + trim + " is not valid", 0);
        }
        throw new ParseException("missing ':' in rule description '" + lowerCase + "'", 0);
    }

    private static RuleList parseRuleChain(String str) throws ParseException {
        String[] split;
        RuleList ruleList = new RuleList();
        if (str.endsWith(";")) {
            str = str.substring(0, str.length() - 1);
        }
        for (String str2 : SEMI_SEPARATED.split(str)) {
            Rule parseRule = parseRule(str2.trim());
            RuleList.access$276(ruleList, (parseRule.integerSamples == null && parseRule.decimalSamples == null) ? 0 : 1);
            ruleList.addRule(parseRule);
        }
        return ruleList.finish();
    }

    /* access modifiers changed from: private */
    public static class RangeConstraint implements Constraint, Serializable {
        private static final long serialVersionUID = 1;
        private final boolean inRange;
        private final boolean integersOnly;
        private final double lowerBound;
        private final int mod;
        private final Operand operand;
        private final long[] range_list;
        private final double upperBound;

        RangeConstraint(int i, boolean z, Operand operand2, boolean z2, double d, double d2, long[] jArr) {
            this.mod = i;
            this.inRange = z;
            this.integersOnly = z2;
            this.lowerBound = d;
            this.upperBound = d2;
            this.range_list = jArr;
            this.operand = operand2;
        }

        @Override // ohos.global.icu.text.PluralRules.Constraint
        public boolean isFulfilled(IFixedDecimal iFixedDecimal) {
            double pluralOperand = iFixedDecimal.getPluralOperand(this.operand);
            if ((this.integersOnly && pluralOperand - ((double) ((long) pluralOperand)) != XPath.MATCH_SCORE_QNAME) || (this.operand == Operand.j && iFixedDecimal.getPluralOperand(Operand.v) != XPath.MATCH_SCORE_QNAME)) {
                return !this.inRange;
            }
            int i = this.mod;
            if (i != 0) {
                pluralOperand %= (double) i;
            }
            boolean z = pluralOperand >= this.lowerBound && pluralOperand <= this.upperBound;
            if (z && this.range_list != null) {
                z = false;
                int i2 = 0;
                while (!z) {
                    long[] jArr = this.range_list;
                    if (i2 >= jArr.length) {
                        break;
                    }
                    z = pluralOperand >= ((double) jArr[i2]) && pluralOperand <= ((double) jArr[i2 + 1]);
                    i2 += 2;
                }
            }
            if (this.inRange == z) {
                return true;
            }
            return false;
        }

        @Override // ohos.global.icu.text.PluralRules.Constraint
        public boolean isLimited(SampleType sampleType) {
            double d = this.lowerBound;
            boolean z = (this.operand == Operand.v || this.operand == Operand.w || this.operand == Operand.f || this.operand == Operand.t) && this.inRange != ((d > this.upperBound ? 1 : (d == this.upperBound ? 0 : -1)) == 0 && (d > XPath.MATCH_SCORE_QNAME ? 1 : (d == XPath.MATCH_SCORE_QNAME ? 0 : -1)) == 0);
            int i = AnonymousClass2.$SwitchMap$ohos$global$icu$text$PluralRules$SampleType[sampleType.ordinal()];
            if (i != 1) {
                if (i != 2) {
                    return false;
                }
                return (!z || this.operand == Operand.n || this.operand == Operand.j) && (this.integersOnly || this.lowerBound == this.upperBound) && this.mod == 0 && this.inRange;
            } else if (!z) {
                return (this.operand == Operand.n || this.operand == Operand.i || this.operand == Operand.j) && this.mod == 0 && this.inRange;
            } else {
                return true;
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x002d, code lost:
            if (r10.inRange != false) goto L_0x0044;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0038, code lost:
            if (r10.inRange != false) goto L_0x0044;
         */
        /* JADX WARNING: Removed duplicated region for block: B:22:0x004b  */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x0065  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.String toString() {
            /*
            // Method dump skipped, instructions count: 115
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.PluralRules.RangeConstraint.toString():java.lang.String");
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.global.icu.text.PluralRules$2  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$text$PluralRules$SampleType = new int[SampleType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(18:0|(2:1|2)|3|(2:5|6)|7|9|10|11|12|13|14|15|16|17|18|19|20|(3:21|22|24)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(21:0|1|2|3|(2:5|6)|7|9|10|11|12|13|14|15|16|17|18|19|20|21|22|24) */
        /* JADX WARNING: Can't wrap try/catch for region: R(22:0|1|2|3|5|6|7|9|10|11|12|13|14|15|16|17|18|19|20|21|22|24) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0032 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x003c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0047 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0052 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x005d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0068 */
        static {
            /*
            // Method dump skipped, instructions count: 116
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.PluralRules.AnonymousClass2.<clinit>():void");
        }
    }

    /* access modifiers changed from: private */
    public static void addRange(StringBuilder sb, double d, double d2, boolean z) {
        if (z) {
            sb.append(",");
        }
        if (d == d2) {
            sb.append(format(d));
            return;
        }
        sb.append(format(d) + Constants.ATTRVAL_PARENT + format(d2));
    }

    private static String format(double d) {
        long j = (long) d;
        return d == ((double) j) ? String.valueOf(j) : String.valueOf(d);
    }

    private static abstract class BinaryConstraint implements Constraint, Serializable {
        private static final long serialVersionUID = 1;
        protected final Constraint a;
        protected final Constraint b;

        protected BinaryConstraint(Constraint constraint, Constraint constraint2) {
            this.a = constraint;
            this.b = constraint2;
        }
    }

    /* access modifiers changed from: private */
    public static class AndConstraint extends BinaryConstraint {
        private static final long serialVersionUID = 7766999779862263523L;

        AndConstraint(Constraint constraint, Constraint constraint2) {
            super(constraint, constraint2);
        }

        @Override // ohos.global.icu.text.PluralRules.Constraint
        public boolean isFulfilled(IFixedDecimal iFixedDecimal) {
            return this.a.isFulfilled(iFixedDecimal) && this.b.isFulfilled(iFixedDecimal);
        }

        @Override // ohos.global.icu.text.PluralRules.Constraint
        public boolean isLimited(SampleType sampleType) {
            return this.a.isLimited(sampleType) || this.b.isLimited(sampleType);
        }

        public String toString() {
            return this.a.toString() + " and " + this.b.toString();
        }
    }

    /* access modifiers changed from: private */
    public static class OrConstraint extends BinaryConstraint {
        private static final long serialVersionUID = 1405488568664762222L;

        OrConstraint(Constraint constraint, Constraint constraint2) {
            super(constraint, constraint2);
        }

        @Override // ohos.global.icu.text.PluralRules.Constraint
        public boolean isFulfilled(IFixedDecimal iFixedDecimal) {
            return this.a.isFulfilled(iFixedDecimal) || this.b.isFulfilled(iFixedDecimal);
        }

        @Override // ohos.global.icu.text.PluralRules.Constraint
        public boolean isLimited(SampleType sampleType) {
            return this.a.isLimited(sampleType) && this.b.isLimited(sampleType);
        }

        public String toString() {
            return this.a.toString() + " or " + this.b.toString();
        }
    }

    /* access modifiers changed from: private */
    public static class Rule implements Serializable {
        private static final long serialVersionUID = 1;
        private final Constraint constraint;
        private final FixedDecimalSamples decimalSamples;
        private final FixedDecimalSamples integerSamples;
        private final String keyword;

        public Rule(String str, Constraint constraint2, FixedDecimalSamples fixedDecimalSamples, FixedDecimalSamples fixedDecimalSamples2) {
            this.keyword = str;
            this.constraint = constraint2;
            this.integerSamples = fixedDecimalSamples;
            this.decimalSamples = fixedDecimalSamples2;
        }

        public Rule and(Constraint constraint2) {
            return new Rule(this.keyword, new AndConstraint(this.constraint, constraint2), this.integerSamples, this.decimalSamples);
        }

        public Rule or(Constraint constraint2) {
            return new Rule(this.keyword, new OrConstraint(this.constraint, constraint2), this.integerSamples, this.decimalSamples);
        }

        public String getKeyword() {
            return this.keyword;
        }

        public boolean appliesTo(IFixedDecimal iFixedDecimal) {
            return this.constraint.isFulfilled(iFixedDecimal);
        }

        public boolean isLimited(SampleType sampleType) {
            return this.constraint.isLimited(sampleType);
        }

        public String toString() {
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append(this.keyword);
            sb.append(": ");
            sb.append(this.constraint.toString());
            String str2 = "";
            if (this.integerSamples == null) {
                str = str2;
            } else {
                str = " " + this.integerSamples.toString();
            }
            sb.append(str);
            if (this.decimalSamples != null) {
                str2 = " " + this.decimalSamples.toString();
            }
            sb.append(str2);
            return sb.toString();
        }

        public int hashCode() {
            return this.constraint.hashCode() ^ this.keyword.hashCode();
        }

        public String getConstraint() {
            return this.constraint.toString();
        }
    }

    /* access modifiers changed from: private */
    public static class RuleList implements Serializable {
        private static final long serialVersionUID = 1;
        private boolean hasExplicitBoundingInfo;
        private final List<Rule> rules;

        private RuleList() {
            this.hasExplicitBoundingInfo = false;
            this.rules = new ArrayList();
        }

        /* JADX WARN: Type inference failed for: r2v2, types: [byte, boolean] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static /* synthetic */ boolean access$276(ohos.global.icu.text.PluralRules.RuleList r1, int r2) {
            /*
                boolean r0 = r1.hasExplicitBoundingInfo
                r2 = r2 | r0
                byte r2 = (byte) r2
                r1.hasExplicitBoundingInfo = r2
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.PluralRules.RuleList.access$276(ohos.global.icu.text.PluralRules$RuleList, int):boolean");
        }

        public RuleList addRule(Rule rule) {
            String keyword = rule.getKeyword();
            for (Rule rule2 : this.rules) {
                if (keyword.equals(rule2.getKeyword())) {
                    throw new IllegalArgumentException("Duplicate keyword: " + keyword);
                }
            }
            this.rules.add(rule);
            return this;
        }

        public RuleList finish() throws ParseException {
            Iterator<Rule> it = this.rules.iterator();
            Rule rule = null;
            while (it.hasNext()) {
                Rule next = it.next();
                if ("other".equals(next.getKeyword())) {
                    it.remove();
                    rule = next;
                }
            }
            if (rule == null) {
                rule = PluralRules.parseRule("other:");
            }
            this.rules.add(rule);
            return this;
        }

        private Rule selectRule(IFixedDecimal iFixedDecimal) {
            for (Rule rule : this.rules) {
                if (rule.appliesTo(iFixedDecimal)) {
                    return rule;
                }
            }
            return null;
        }

        public String select(IFixedDecimal iFixedDecimal) {
            return (iFixedDecimal.isInfinite() || iFixedDecimal.isNaN()) ? "other" : selectRule(iFixedDecimal).getKeyword();
        }

        public Set<String> getKeywords() {
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            for (Rule rule : this.rules) {
                linkedHashSet.add(rule.getKeyword());
            }
            return linkedHashSet;
        }

        public boolean isLimited(String str, SampleType sampleType) {
            if (!this.hasExplicitBoundingInfo) {
                return computeLimited(str, sampleType);
            }
            FixedDecimalSamples decimalSamples = getDecimalSamples(str, sampleType);
            if (decimalSamples == null) {
                return true;
            }
            return decimalSamples.bounded;
        }

        public boolean computeLimited(String str, SampleType sampleType) {
            boolean z = false;
            for (Rule rule : this.rules) {
                if (str.equals(rule.getKeyword())) {
                    if (!rule.isLimited(sampleType)) {
                        return false;
                    }
                    z = true;
                }
            }
            return z;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Rule rule : this.rules) {
                if (sb.length() != 0) {
                    sb.append(PluralRules.CATEGORY_SEPARATOR);
                }
                sb.append(rule);
            }
            return sb.toString();
        }

        public String getRules(String str) {
            for (Rule rule : this.rules) {
                if (rule.getKeyword().equals(str)) {
                    return rule.getConstraint();
                }
            }
            return null;
        }

        public boolean select(IFixedDecimal iFixedDecimal, String str) {
            for (Rule rule : this.rules) {
                if (rule.getKeyword().equals(str) && rule.appliesTo(iFixedDecimal)) {
                    return true;
                }
            }
            return false;
        }

        public FixedDecimalSamples getDecimalSamples(String str, SampleType sampleType) {
            for (Rule rule : this.rules) {
                if (rule.getKeyword().equals(str)) {
                    return sampleType == SampleType.INTEGER ? rule.integerSamples : rule.decimalSamples;
                }
            }
            return null;
        }
    }

    private boolean addConditional(Set<IFixedDecimal> set, Set<IFixedDecimal> set2, double d) {
        FixedDecimal fixedDecimal = new FixedDecimal(d);
        if (set.contains(fixedDecimal) || set2.contains(fixedDecimal)) {
            return false;
        }
        set2.add(fixedDecimal);
        return true;
    }

    public static PluralRules forLocale(ULocale uLocale) {
        return Factory.getDefaultFactory().forLocale(uLocale, PluralType.CARDINAL);
    }

    public static PluralRules forLocale(Locale locale) {
        return forLocale(ULocale.forLocale(locale));
    }

    public static PluralRules forLocale(ULocale uLocale, PluralType pluralType) {
        return Factory.getDefaultFactory().forLocale(uLocale, pluralType);
    }

    public static PluralRules forLocale(Locale locale, PluralType pluralType) {
        return forLocale(ULocale.forLocale(locale), pluralType);
    }

    private static boolean isValidKeyword(String str) {
        return ALLOWED_ID.containsAll(str);
    }

    private PluralRules(RuleList ruleList) {
        this.rules = ruleList;
        this.keywords = Collections.unmodifiableSet(ruleList.getKeywords());
    }

    public int hashCode() {
        return this.rules.hashCode();
    }

    public String select(double d) {
        return this.rules.select(new FixedDecimal(d));
    }

    public String select(FormattedNumber formattedNumber) {
        return this.rules.select(formattedNumber.getFixedDecimal());
    }

    @Deprecated
    public String select(double d, int i, long j) {
        return this.rules.select(new FixedDecimal(d, i, j));
    }

    @Deprecated
    public String select(IFixedDecimal iFixedDecimal) {
        return this.rules.select(iFixedDecimal);
    }

    @Deprecated
    public boolean matches(FixedDecimal fixedDecimal, String str) {
        return this.rules.select(fixedDecimal, str);
    }

    public Set<String> getKeywords() {
        return this.keywords;
    }

    public double getUniqueKeywordValue(String str) {
        Collection<Double> allKeywordValues = getAllKeywordValues(str);
        if (allKeywordValues == null || allKeywordValues.size() != 1) {
            return -0.00123456777d;
        }
        return allKeywordValues.iterator().next().doubleValue();
    }

    public Collection<Double> getAllKeywordValues(String str) {
        return getAllKeywordValues(str, SampleType.INTEGER);
    }

    @Deprecated
    public Collection<Double> getAllKeywordValues(String str, SampleType sampleType) {
        Collection<Double> samples;
        if (isLimited(str, sampleType) && (samples = getSamples(str, sampleType)) != null) {
            return Collections.unmodifiableCollection(samples);
        }
        return null;
    }

    public Collection<Double> getSamples(String str) {
        return getSamples(str, SampleType.INTEGER);
    }

    @Deprecated
    public Collection<Double> getSamples(String str, SampleType sampleType) {
        if (!this.keywords.contains(str)) {
            return null;
        }
        TreeSet treeSet = new TreeSet();
        if (this.rules.hasExplicitBoundingInfo) {
            FixedDecimalSamples decimalSamples = this.rules.getDecimalSamples(str, sampleType);
            if (decimalSamples == null) {
                return Collections.unmodifiableSet(treeSet);
            }
            return Collections.unmodifiableSet(decimalSamples.addSamples(treeSet));
        }
        int i = isLimited(str, sampleType) ? Integer.MAX_VALUE : 20;
        int i2 = AnonymousClass2.$SwitchMap$ohos$global$icu$text$PluralRules$SampleType[sampleType.ordinal()];
        int i3 = 0;
        if (i2 == 1) {
            while (i3 < 200 && addSample(str, Integer.valueOf(i3), i, treeSet)) {
                i3++;
            }
            addSample(str, Integer.valueOf((int) A2dpCodecInfo.CODEC_PRIORITY_HIGHEST), i, treeSet);
        } else if (i2 == 2) {
            while (i3 < 2000 && addSample(str, new FixedDecimal(((double) i3) / 10.0d, 1), i, treeSet)) {
                i3++;
            }
            addSample(str, new FixedDecimal(1000000.0d, 1), i, treeSet);
        }
        if (treeSet.size() == 0) {
            return null;
        }
        return Collections.unmodifiableSet(treeSet);
    }

    private boolean addSample(String str, Number number, int i, Set<Double> set) {
        if (!(number instanceof FixedDecimal ? select((FixedDecimal) number) : select(number.doubleValue())).equals(str)) {
            return true;
        }
        set.add(Double.valueOf(number.doubleValue()));
        return i + -1 >= 0;
    }

    @Deprecated
    public FixedDecimalSamples getDecimalSamples(String str, SampleType sampleType) {
        return this.rules.getDecimalSamples(str, sampleType);
    }

    public static ULocale[] getAvailableULocales() {
        return Factory.getDefaultFactory().getAvailableULocales();
    }

    public static ULocale getFunctionalEquivalent(ULocale uLocale, boolean[] zArr) {
        return Factory.getDefaultFactory().getFunctionalEquivalent(uLocale, zArr);
    }

    public String toString() {
        return this.rules.toString();
    }

    public boolean equals(Object obj) {
        return (obj instanceof PluralRules) && equals((PluralRules) obj);
    }

    public boolean equals(PluralRules pluralRules) {
        return pluralRules != null && toString().equals(pluralRules.toString());
    }

    public KeywordStatus getKeywordStatus(String str, int i, Set<Double> set, Output<Double> output) {
        return getKeywordStatus(str, i, set, output, SampleType.INTEGER);
    }

    @Deprecated
    public KeywordStatus getKeywordStatus(String str, int i, Set<Double> set, Output<Double> output, SampleType sampleType) {
        if (output != null) {
            output.value = null;
        }
        if (!this.keywords.contains(str)) {
            return KeywordStatus.INVALID;
        }
        if (!isLimited(str, sampleType)) {
            return KeywordStatus.UNBOUNDED;
        }
        Collection<Double> samples = getSamples(str, sampleType);
        int size = samples.size();
        if (set == null) {
            set = Collections.emptySet();
        }
        if (size <= set.size()) {
            HashSet hashSet = new HashSet(samples);
            for (Double d : set) {
                hashSet.remove(Double.valueOf(d.doubleValue() - ((double) i)));
            }
            if (hashSet.size() == 0) {
                return KeywordStatus.SUPPRESSED;
            }
            if (output != null && hashSet.size() == 1) {
                output.value = (T) hashSet.iterator().next();
            }
            return size == 1 ? KeywordStatus.UNIQUE : KeywordStatus.BOUNDED;
        } else if (size != 1) {
            return KeywordStatus.BOUNDED;
        } else {
            if (output != null) {
                output.value = (T) samples.iterator().next();
            }
            return KeywordStatus.UNIQUE;
        }
    }

    @Deprecated
    public String getRules(String str) {
        return this.rules.getRules(str);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        throw new NotSerializableException();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        throw new NotSerializableException();
    }

    private Object writeReplace() throws ObjectStreamException {
        return new PluralRulesSerialProxy(toString());
    }

    @Deprecated
    public int compareTo(PluralRules pluralRules) {
        return toString().compareTo(pluralRules.toString());
    }

    /* access modifiers changed from: package-private */
    public Boolean isLimited(String str) {
        return Boolean.valueOf(this.rules.isLimited(str, SampleType.INTEGER));
    }

    @Deprecated
    public boolean isLimited(String str, SampleType sampleType) {
        return this.rules.isLimited(str, sampleType);
    }

    @Deprecated
    public boolean computeLimited(String str, SampleType sampleType) {
        return this.rules.computeLimited(str, sampleType);
    }
}
