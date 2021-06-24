package ohos.global.icu.impl;

import java.util.Date;
import ohos.global.icu.util.AnnualTimeZoneRule;
import ohos.global.icu.util.BasicTimeZone;
import ohos.global.icu.util.DateTimeRule;
import ohos.global.icu.util.InitialTimeZoneRule;
import ohos.global.icu.util.SimpleTimeZone;
import ohos.global.icu.util.TimeArrayTimeZoneRule;
import ohos.global.icu.util.TimeZone;
import ohos.global.icu.util.TimeZoneRule;
import ohos.global.icu.util.TimeZoneTransition;
import ohos.global.icu.util.UResourceBundle;
import ohos.telephony.TelephoneNumberUtils;

public class OlsonTimeZone extends BasicTimeZone {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final boolean DEBUG = ICUDebug.enabled("olson");
    private static final int MAX_OFFSET_SECONDS = 86400;
    private static final int SECONDS_PER_DAY = 86400;
    private static final String ZONEINFORES = "zoneinfo64";
    private static final int currentSerialVersion = 1;
    static final long serialVersionUID = -6281977362477515376L;
    private volatile String canonicalID = null;
    private double finalStartMillis = Double.MAX_VALUE;
    private int finalStartYear = Integer.MAX_VALUE;
    private SimpleTimeZone finalZone = null;
    private transient SimpleTimeZone finalZoneWithStartYear;
    private transient TimeZoneTransition firstFinalTZTransition;
    private transient TimeZoneTransition firstTZTransition;
    private transient int firstTZTransitionIdx;
    private transient TimeArrayTimeZoneRule[] historicRules;
    private transient InitialTimeZoneRule initialRule;
    private volatile transient boolean isFrozen = false;
    private int serialVersionOnStream = 1;
    private int transitionCount;
    private transient boolean transitionRulesInitialized;
    private long[] transitionTimes64;
    private int typeCount;
    private byte[] typeMapData;
    private int[] typeOffsets;

    private int getInt(byte b) {
        return b & 255;
    }

    @Override // ohos.global.icu.util.TimeZone
    public int getOffset(int i, int i2, int i3, int i4, int i5, int i6) {
        if (i3 >= 0 && i3 <= 11) {
            return getOffset(i, i2, i3, i4, i5, i6, Grego.monthLength(i2, i3));
        }
        throw new IllegalArgumentException("Month is not in the legal range: " + i3);
    }

    public int getOffset(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        if ((i == 1 || i == 0) && i3 >= 0 && i3 <= 11 && i4 >= 1 && i4 <= i7 && i5 >= 1 && i5 <= 7 && i6 >= 0 && i6 < 86400000 && i7 >= 28 && i7 <= 31) {
            if (i == 0) {
                i2 = -i2;
            }
            SimpleTimeZone simpleTimeZone = this.finalZone;
            if (simpleTimeZone != null && i2 >= this.finalStartYear) {
                return simpleTimeZone.getOffset(i, i2, i3, i4, i5, i6);
            }
            int[] iArr = new int[2];
            getHistoricalOffset((Grego.fieldsToDay(i2, i3, i4) * 86400000) + ((long) i6), true, 3, 1, iArr);
            return iArr[0] + iArr[1];
        }
        throw new IllegalArgumentException();
    }

    @Override // ohos.global.icu.util.TimeZone
    public void setRawOffset(int i) {
        DateTimeRule dateTimeRule;
        int i2;
        DateTimeRule dateTimeRule2;
        TimeZoneTransition previousTransition;
        if (isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen OlsonTimeZone instance.");
        } else if (getRawOffset() != i) {
            long currentTimeMillis = System.currentTimeMillis();
            if (((double) currentTimeMillis) < this.finalStartMillis) {
                SimpleTimeZone simpleTimeZone = new SimpleTimeZone(i, getID());
                boolean useDaylightTime = useDaylightTime();
                if (useDaylightTime) {
                    TimeZoneRule[] simpleTimeZoneRulesNear = getSimpleTimeZoneRulesNear(currentTimeMillis);
                    if (!(simpleTimeZoneRulesNear.length == 3 || (previousTransition = getPreviousTransition(currentTimeMillis, false)) == null)) {
                        simpleTimeZoneRulesNear = getSimpleTimeZoneRulesNear(previousTransition.getTime() - 1);
                    }
                    if (simpleTimeZoneRulesNear.length != 3 || !(simpleTimeZoneRulesNear[1] instanceof AnnualTimeZoneRule) || !(simpleTimeZoneRulesNear[2] instanceof AnnualTimeZoneRule)) {
                        simpleTimeZone.setStartRule(0, 1, 0);
                        simpleTimeZone.setEndRule(11, 31, 86399999);
                    } else {
                        AnnualTimeZoneRule annualTimeZoneRule = (AnnualTimeZoneRule) simpleTimeZoneRulesNear[1];
                        AnnualTimeZoneRule annualTimeZoneRule2 = (AnnualTimeZoneRule) simpleTimeZoneRulesNear[2];
                        int rawOffset = annualTimeZoneRule.getRawOffset() + annualTimeZoneRule.getDSTSavings();
                        int rawOffset2 = annualTimeZoneRule2.getRawOffset() + annualTimeZoneRule2.getDSTSavings();
                        if (rawOffset > rawOffset2) {
                            DateTimeRule rule = annualTimeZoneRule.getRule();
                            i2 = rawOffset - rawOffset2;
                            dateTimeRule = annualTimeZoneRule2.getRule();
                            dateTimeRule2 = rule;
                        } else {
                            dateTimeRule2 = annualTimeZoneRule2.getRule();
                            dateTimeRule = annualTimeZoneRule.getRule();
                            i2 = rawOffset2 - rawOffset;
                        }
                        simpleTimeZone.setStartRule(dateTimeRule2.getRuleMonth(), dateTimeRule2.getRuleWeekInMonth(), dateTimeRule2.getRuleDayOfWeek(), dateTimeRule2.getRuleMillisInDay());
                        simpleTimeZone.setEndRule(dateTimeRule.getRuleMonth(), dateTimeRule.getRuleWeekInMonth(), dateTimeRule.getRuleDayOfWeek(), dateTimeRule.getRuleMillisInDay());
                        simpleTimeZone.setDSTSavings(i2);
                    }
                }
                int[] timeToFields = Grego.timeToFields(currentTimeMillis, null);
                this.finalStartYear = timeToFields[0];
                this.finalStartMillis = (double) Grego.fieldsToDay(timeToFields[0], 0, 1);
                if (useDaylightTime) {
                    simpleTimeZone.setStartYear(this.finalStartYear);
                }
                this.finalZone = simpleTimeZone;
            } else {
                this.finalZone.setRawOffset(i);
            }
            this.transitionRulesInitialized = false;
        }
    }

    @Override // ohos.global.icu.util.TimeZone, java.lang.Object
    public Object clone() {
        if (isFrozen()) {
            return this;
        }
        return cloneAsThawed();
    }

    @Override // ohos.global.icu.util.TimeZone
    public void getOffset(long j, boolean z, int[] iArr) {
        SimpleTimeZone simpleTimeZone = this.finalZone;
        if (simpleTimeZone == null || ((double) j) < this.finalStartMillis) {
            getHistoricalOffset(j, z, 4, 12, iArr);
        } else {
            simpleTimeZone.getOffset(j, z, iArr);
        }
    }

    @Override // ohos.global.icu.util.BasicTimeZone
    public void getOffsetFromLocal(long j, int i, int i2, int[] iArr) {
        SimpleTimeZone simpleTimeZone = this.finalZone;
        if (simpleTimeZone == null || ((double) j) < this.finalStartMillis) {
            getHistoricalOffset(j, true, i, i2, iArr);
        } else {
            simpleTimeZone.getOffsetFromLocal(j, i, i2, iArr);
        }
    }

    @Override // ohos.global.icu.util.TimeZone
    public int getRawOffset() {
        int[] iArr = new int[2];
        getOffset(System.currentTimeMillis(), false, iArr);
        return iArr[0];
    }

    @Override // ohos.global.icu.util.TimeZone
    public boolean useDaylightTime() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleTimeZone simpleTimeZone = this.finalZone;
        if (simpleTimeZone != null && ((double) currentTimeMillis) >= this.finalStartMillis) {
            return simpleTimeZone != null && simpleTimeZone.useDaylightTime();
        }
        int[] timeToFields = Grego.timeToFields(currentTimeMillis, null);
        long fieldsToDay = Grego.fieldsToDay(timeToFields[0], 0, 1) * 86400;
        long fieldsToDay2 = Grego.fieldsToDay(timeToFields[0] + 1, 0, 1) * 86400;
        for (int i = 0; i < this.transitionCount; i++) {
            long[] jArr = this.transitionTimes64;
            if (jArr[i] >= fieldsToDay2) {
                break;
            } else if ((jArr[i] >= fieldsToDay && dstOffsetAt(i) != 0) || (this.transitionTimes64[i] > fieldsToDay && i > 0 && dstOffsetAt(i - 1) != 0)) {
                return true;
            }
        }
        return false;
    }

    @Override // ohos.global.icu.util.TimeZone
    public boolean observesDaylightTime() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleTimeZone simpleTimeZone = this.finalZone;
        if (simpleTimeZone != null) {
            if (simpleTimeZone.useDaylightTime()) {
                return true;
            }
            if (((double) currentTimeMillis) >= this.finalStartMillis) {
                return false;
            }
        }
        long floorDivide = Grego.floorDivide(currentTimeMillis, 1000);
        int i = this.transitionCount - 1;
        if (dstOffsetAt(i) != 0) {
            return true;
        }
        while (i >= 0 && this.transitionTimes64[i] > floorDivide) {
            if (dstOffsetAt(i - 1) != 0) {
                return true;
            }
            i--;
        }
        return false;
    }

    @Override // ohos.global.icu.util.TimeZone
    public int getDSTSavings() {
        SimpleTimeZone simpleTimeZone = this.finalZone;
        if (simpleTimeZone != null) {
            return simpleTimeZone.getDSTSavings();
        }
        return super.getDSTSavings();
    }

    @Override // ohos.global.icu.util.TimeZone
    public boolean inDaylightTime(Date date) {
        int[] iArr = new int[2];
        getOffset(date.getTime(), false, iArr);
        return iArr[1] != 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002a, code lost:
        if (r1.hasSameRules(r3) != false) goto L_0x002d;
     */
    @Override // ohos.global.icu.util.TimeZone
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasSameRules(ohos.global.icu.util.TimeZone r7) {
        /*
            r6 = this;
            r0 = 1
            if (r6 != r7) goto L_0x0004
            return r0
        L_0x0004:
            boolean r1 = super.hasSameRules(r7)
            r2 = 0
            if (r1 != 0) goto L_0x000c
            return r2
        L_0x000c:
            boolean r1 = r7 instanceof ohos.global.icu.impl.OlsonTimeZone
            if (r1 != 0) goto L_0x0011
            return r2
        L_0x0011:
            ohos.global.icu.impl.OlsonTimeZone r7 = (ohos.global.icu.impl.OlsonTimeZone) r7
            ohos.global.icu.util.SimpleTimeZone r1 = r6.finalZone
            if (r1 != 0) goto L_0x001c
            ohos.global.icu.util.SimpleTimeZone r1 = r7.finalZone
            if (r1 == 0) goto L_0x002d
            return r2
        L_0x001c:
            ohos.global.icu.util.SimpleTimeZone r3 = r7.finalZone
            if (r3 == 0) goto L_0x0059
            int r4 = r6.finalStartYear
            int r5 = r7.finalStartYear
            if (r4 != r5) goto L_0x0059
            boolean r1 = r1.hasSameRules(r3)
            if (r1 != 0) goto L_0x002d
            goto L_0x0059
        L_0x002d:
            int r1 = r6.transitionCount
            int r3 = r7.transitionCount
            if (r1 != r3) goto L_0x0059
            long[] r1 = r6.transitionTimes64
            long[] r3 = r7.transitionTimes64
            boolean r1 = java.util.Arrays.equals(r1, r3)
            if (r1 == 0) goto L_0x0059
            int r1 = r6.typeCount
            int r3 = r7.typeCount
            if (r1 != r3) goto L_0x0059
            byte[] r1 = r6.typeMapData
            byte[] r3 = r7.typeMapData
            boolean r1 = java.util.Arrays.equals(r1, r3)
            if (r1 == 0) goto L_0x0059
            int[] r6 = r6.typeOffsets
            int[] r7 = r7.typeOffsets
            boolean r6 = java.util.Arrays.equals(r6, r7)
            if (r6 != 0) goto L_0x0058
            goto L_0x0059
        L_0x0058:
            return r0
        L_0x0059:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.OlsonTimeZone.hasSameRules(ohos.global.icu.util.TimeZone):boolean");
    }

    public String getCanonicalID() {
        if (this.canonicalID == null) {
            synchronized (this) {
                if (this.canonicalID == null) {
                    this.canonicalID = getCanonicalID(getID());
                    if (this.canonicalID == null) {
                        this.canonicalID = getID();
                    }
                }
            }
        }
        return this.canonicalID;
    }

    private void constructEmpty() {
        this.transitionCount = 0;
        this.transitionTimes64 = null;
        this.typeMapData = null;
        this.typeCount = 1;
        this.typeOffsets = new int[]{0, 0};
        this.finalZone = null;
        this.finalStartYear = Integer.MAX_VALUE;
        this.finalStartMillis = Double.MAX_VALUE;
        this.transitionRulesInitialized = false;
    }

    public OlsonTimeZone(UResourceBundle uResourceBundle, UResourceBundle uResourceBundle2, String str) {
        super(str);
        construct(uResourceBundle, uResourceBundle2, str);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r4v9, resolved type: ohos.global.icu.util.SimpleTimeZone */
    /* JADX DEBUG: Multi-variable search result rejected for r4v15, resolved type: ohos.global.icu.util.SimpleTimeZone */
    /* JADX DEBUG: Multi-variable search result rejected for r4v21, resolved type: ohos.global.icu.util.SimpleTimeZone */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:100:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x01ca  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void construct(ohos.global.icu.util.UResourceBundle r32, ohos.global.icu.util.UResourceBundle r33, java.lang.String r34) {
        /*
        // Method dump skipped, instructions count: 476
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.OlsonTimeZone.construct(ohos.global.icu.util.UResourceBundle, ohos.global.icu.util.UResourceBundle, java.lang.String):void");
    }

    public OlsonTimeZone(String str) {
        super(str);
        UResourceBundle bundleInstance = UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, ZONEINFORES, ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        construct(bundleInstance, ZoneMeta.openOlsonResource(bundleInstance, str), str);
    }

    @Override // ohos.global.icu.util.TimeZone
    public void setID(String str) {
        if (!isFrozen()) {
            if (this.canonicalID == null) {
                this.canonicalID = getCanonicalID(getID());
                if (this.canonicalID == null) {
                    this.canonicalID = getID();
                }
            }
            SimpleTimeZone simpleTimeZone = this.finalZone;
            if (simpleTimeZone != null) {
                simpleTimeZone.setID(str);
            }
            super.setID(str);
            this.transitionRulesInitialized = false;
            return;
        }
        throw new UnsupportedOperationException("Attempt to modify a frozen OlsonTimeZone instance.");
    }

    private void getHistoricalOffset(long j, boolean z, int i, int i2, int[] iArr) {
        int i3;
        int i4;
        if (this.transitionCount != 0) {
            long floorDivide = Grego.floorDivide(j, 1000);
            if (z || floorDivide >= this.transitionTimes64[0]) {
                int i5 = this.transitionCount - 1;
                while (i5 >= 0) {
                    long j2 = this.transitionTimes64[i5];
                    if (z && floorDivide >= j2 - 86400) {
                        int i6 = i5 - 1;
                        int zoneOffsetAt = zoneOffsetAt(i6);
                        boolean z2 = dstOffsetAt(i6) != 0;
                        int zoneOffsetAt2 = zoneOffsetAt(i5);
                        boolean z3 = dstOffsetAt(i5) != 0;
                        boolean z4 = z2 && !z3;
                        boolean z5 = !z2 && z3;
                        j2 += (zoneOffsetAt2 - zoneOffsetAt < 0 ? ((i3 = i2 & 3) != 1 || !z4) && ((i3 != 3 || !z5) && ((i3 == 1 && z5) || ((i3 == 3 && z4) || (i2 & 12) == 4))) : ((i4 = i & 3) == 1 && z4) || ((i4 == 3 && z5) || ((i4 != 1 || !z5) && ((i4 != 3 || !z4) && (i & 12) == 12)))) ? (long) zoneOffsetAt : (long) zoneOffsetAt2;
                    }
                    if (floorDivide >= j2) {
                        break;
                    }
                    i5--;
                }
                iArr[0] = rawOffsetAt(i5) * 1000;
                iArr[1] = dstOffsetAt(i5) * 1000;
                return;
            }
            iArr[0] = initialRawOffset() * 1000;
            iArr[1] = initialDstOffset() * 1000;
            return;
        }
        iArr[0] = initialRawOffset() * 1000;
        iArr[1] = initialDstOffset() * 1000;
    }

    private int zoneOffsetAt(int i) {
        int i2 = i >= 0 ? getInt(this.typeMapData[i]) * 2 : 0;
        int[] iArr = this.typeOffsets;
        return iArr[i2] + iArr[i2 + 1];
    }

    private int rawOffsetAt(int i) {
        return this.typeOffsets[i >= 0 ? getInt(this.typeMapData[i]) * 2 : 0];
    }

    private int dstOffsetAt(int i) {
        return this.typeOffsets[(i >= 0 ? getInt(this.typeMapData[i]) * 2 : 0) + 1];
    }

    private int initialRawOffset() {
        return this.typeOffsets[0];
    }

    private int initialDstOffset() {
        return this.typeOffsets[1];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append('[');
        sb.append("transitionCount=" + this.transitionCount);
        sb.append(",typeCount=" + this.typeCount);
        sb.append(",transitionTimes=");
        if (this.transitionTimes64 != null) {
            sb.append('[');
            for (int i = 0; i < this.transitionTimes64.length; i++) {
                if (i > 0) {
                    sb.append(TelephoneNumberUtils.PAUSE);
                }
                sb.append(Long.toString(this.transitionTimes64[i]));
            }
            sb.append(']');
        } else {
            sb.append("null");
        }
        sb.append(",typeOffsets=");
        if (this.typeOffsets != null) {
            sb.append('[');
            for (int i2 = 0; i2 < this.typeOffsets.length; i2++) {
                if (i2 > 0) {
                    sb.append(TelephoneNumberUtils.PAUSE);
                }
                sb.append(Integer.toString(this.typeOffsets[i2]));
            }
            sb.append(']');
        } else {
            sb.append("null");
        }
        sb.append(",typeMapData=");
        if (this.typeMapData != null) {
            sb.append('[');
            for (int i3 = 0; i3 < this.typeMapData.length; i3++) {
                if (i3 > 0) {
                    sb.append(TelephoneNumberUtils.PAUSE);
                }
                sb.append(Byte.toString(this.typeMapData[i3]));
            }
        } else {
            sb.append("null");
        }
        sb.append(",finalStartYear=" + this.finalStartYear);
        sb.append(",finalStartMillis=" + this.finalStartMillis);
        sb.append(",finalZone=" + this.finalZone);
        sb.append(']');
        return sb.toString();
    }

    private static UResourceBundle loadRule(UResourceBundle uResourceBundle, String str) {
        return uResourceBundle.get("Rules").get(str);
    }

    @Override // ohos.global.icu.util.TimeZone
    public boolean equals(Object obj) {
        SimpleTimeZone simpleTimeZone;
        SimpleTimeZone simpleTimeZone2;
        if (!super.equals(obj)) {
            return false;
        }
        OlsonTimeZone olsonTimeZone = (OlsonTimeZone) obj;
        if (!Utility.arrayEquals(this.typeMapData, (Object) olsonTimeZone.typeMapData)) {
            if (this.finalStartYear != olsonTimeZone.finalStartYear) {
                return false;
            }
            if (!(this.finalZone == null && olsonTimeZone.finalZone == null) && ((simpleTimeZone = this.finalZone) == null || (simpleTimeZone2 = olsonTimeZone.finalZone) == null || !simpleTimeZone.equals(simpleTimeZone2) || this.transitionCount != olsonTimeZone.transitionCount || this.typeCount != olsonTimeZone.typeCount || !Utility.arrayEquals((Object) this.transitionTimes64, (Object) olsonTimeZone.transitionTimes64) || !Utility.arrayEquals(this.typeOffsets, (Object) olsonTimeZone.typeOffsets) || !Utility.arrayEquals(this.typeMapData, (Object) olsonTimeZone.typeMapData))) {
                return false;
            }
        }
        return true;
    }

    @Override // ohos.global.icu.util.TimeZone
    public int hashCode() {
        int i = this.finalStartYear;
        int i2 = this.transitionCount;
        int i3 = i ^ ((i >>> 4) + i2);
        int i4 = i2 >>> 6;
        int i5 = this.typeCount;
        long j = (long) (i3 ^ (i4 + i5));
        long doubleToLongBits = ((long) (i5 >>> 8)) + Double.doubleToLongBits(this.finalStartMillis);
        SimpleTimeZone simpleTimeZone = this.finalZone;
        int i6 = 0;
        int hashCode = (int) (j ^ ((doubleToLongBits + ((long) (simpleTimeZone == null ? 0 : simpleTimeZone.hashCode()))) + ((long) super.hashCode())));
        if (this.transitionTimes64 != null) {
            int i7 = hashCode;
            int i8 = 0;
            while (true) {
                long[] jArr = this.transitionTimes64;
                if (i8 >= jArr.length) {
                    break;
                }
                i7 = (int) (((long) i7) + ((jArr[i8] >>> 8) ^ jArr[i8]));
                i8++;
            }
            hashCode = i7;
        }
        int i9 = hashCode;
        int i10 = 0;
        while (true) {
            int[] iArr = this.typeOffsets;
            if (i10 >= iArr.length) {
                break;
            }
            i9 += (iArr[i10] >>> 8) ^ iArr[i10];
            i10++;
        }
        if (this.typeMapData != null) {
            while (true) {
                byte[] bArr = this.typeMapData;
                if (i6 >= bArr.length) {
                    break;
                }
                i9 += bArr[i6] & 255;
                i6++;
            }
        }
        return i9;
    }

    @Override // ohos.global.icu.util.BasicTimeZone
    public TimeZoneTransition getNextTransition(long j, boolean z) {
        int i;
        initTransitionRules();
        if (this.finalZone != null) {
            if (z && j == this.firstFinalTZTransition.getTime()) {
                return this.firstFinalTZTransition;
            }
            if (j >= this.firstFinalTZTransition.getTime()) {
                if (this.finalZone.useDaylightTime()) {
                    return this.finalZoneWithStartYear.getNextTransition(j, z);
                }
                return null;
            }
        }
        if (this.historicRules == null) {
            return null;
        }
        int i2 = this.transitionCount;
        while (true) {
            i2--;
            if (i2 < this.firstTZTransitionIdx || j > this.transitionTimes64[i2] * 1000 || (!z && i == 0)) {
            }
        }
        if (i2 == this.transitionCount - 1) {
            return this.firstFinalTZTransition;
        }
        if (i2 < this.firstTZTransitionIdx) {
            return this.firstTZTransition;
        }
        int i3 = i2 + 1;
        TimeArrayTimeZoneRule timeArrayTimeZoneRule = this.historicRules[getInt(this.typeMapData[i3])];
        TimeArrayTimeZoneRule timeArrayTimeZoneRule2 = this.historicRules[getInt(this.typeMapData[i2])];
        long j2 = this.transitionTimes64[i3] * 1000;
        if (timeArrayTimeZoneRule2.getName().equals(timeArrayTimeZoneRule.getName()) && timeArrayTimeZoneRule2.getRawOffset() == timeArrayTimeZoneRule.getRawOffset() && timeArrayTimeZoneRule2.getDSTSavings() == timeArrayTimeZoneRule.getDSTSavings()) {
            return getNextTransition(j2, false);
        }
        return new TimeZoneTransition(j2, timeArrayTimeZoneRule2, timeArrayTimeZoneRule);
    }

    @Override // ohos.global.icu.util.BasicTimeZone
    public TimeZoneTransition getPreviousTransition(long j, boolean z) {
        int i;
        initTransitionRules();
        if (this.finalZone != null) {
            if (z && j == this.firstFinalTZTransition.getTime()) {
                return this.firstFinalTZTransition;
            }
            if (j > this.firstFinalTZTransition.getTime()) {
                if (this.finalZone.useDaylightTime()) {
                    return this.finalZoneWithStartYear.getPreviousTransition(j, z);
                }
                return this.firstFinalTZTransition;
            }
        }
        if (this.historicRules == null) {
            return null;
        }
        int i2 = this.transitionCount;
        while (true) {
            i2--;
            if (i2 < this.firstTZTransitionIdx || j > this.transitionTimes64[i2] * 1000 || (z && i == 0)) {
                int i3 = this.firstTZTransitionIdx;
            }
        }
        int i32 = this.firstTZTransitionIdx;
        if (i2 < i32) {
            return null;
        }
        if (i2 == i32) {
            return this.firstTZTransition;
        }
        TimeArrayTimeZoneRule timeArrayTimeZoneRule = this.historicRules[getInt(this.typeMapData[i2])];
        TimeArrayTimeZoneRule timeArrayTimeZoneRule2 = this.historicRules[getInt(this.typeMapData[i2 - 1])];
        long j2 = this.transitionTimes64[i2] * 1000;
        if (timeArrayTimeZoneRule2.getName().equals(timeArrayTimeZoneRule.getName()) && timeArrayTimeZoneRule2.getRawOffset() == timeArrayTimeZoneRule.getRawOffset() && timeArrayTimeZoneRule2.getDSTSavings() == timeArrayTimeZoneRule.getDSTSavings()) {
            return getPreviousTransition(j2, false);
        }
        return new TimeZoneTransition(j2, timeArrayTimeZoneRule2, timeArrayTimeZoneRule);
    }

    @Override // ohos.global.icu.util.BasicTimeZone
    public TimeZoneRule[] getTimeZoneRules() {
        int i;
        int i2;
        initTransitionRules();
        if (this.historicRules != null) {
            int i3 = 0;
            i = 1;
            while (true) {
                TimeArrayTimeZoneRule[] timeArrayTimeZoneRuleArr = this.historicRules;
                if (i3 >= timeArrayTimeZoneRuleArr.length) {
                    break;
                }
                if (timeArrayTimeZoneRuleArr[i3] != null) {
                    i++;
                }
                i3++;
            }
        } else {
            i = 1;
        }
        SimpleTimeZone simpleTimeZone = this.finalZone;
        if (simpleTimeZone != null) {
            i = simpleTimeZone.useDaylightTime() ? i + 2 : i + 1;
        }
        TimeZoneRule[] timeZoneRuleArr = new TimeZoneRule[i];
        timeZoneRuleArr[0] = this.initialRule;
        if (this.historicRules != null) {
            int i4 = 0;
            i2 = 1;
            while (true) {
                TimeArrayTimeZoneRule[] timeArrayTimeZoneRuleArr2 = this.historicRules;
                if (i4 >= timeArrayTimeZoneRuleArr2.length) {
                    break;
                }
                if (timeArrayTimeZoneRuleArr2[i4] != null) {
                    timeZoneRuleArr[i2] = timeArrayTimeZoneRuleArr2[i4];
                    i2++;
                }
                i4++;
            }
        } else {
            i2 = 1;
        }
        SimpleTimeZone simpleTimeZone2 = this.finalZone;
        if (simpleTimeZone2 != null) {
            if (simpleTimeZone2.useDaylightTime()) {
                TimeZoneRule[] timeZoneRules = this.finalZoneWithStartYear.getTimeZoneRules();
                timeZoneRuleArr[i2] = timeZoneRules[1];
                timeZoneRuleArr[i2 + 1] = timeZoneRules[2];
            } else {
                timeZoneRuleArr[i2] = new TimeArrayTimeZoneRule(getID() + "(STD)", this.finalZone.getRawOffset(), 0, new long[]{(long) this.finalStartMillis}, 2);
            }
        }
        return timeZoneRuleArr;
    }

    private synchronized void initTransitionRules() {
        long j;
        TimeZoneRule timeZoneRule;
        if (!this.transitionRulesInitialized) {
            TimeZoneRule timeZoneRule2 = null;
            this.initialRule = null;
            this.firstTZTransition = null;
            this.firstFinalTZTransition = null;
            this.historicRules = null;
            this.firstTZTransitionIdx = 0;
            this.finalZoneWithStartYear = null;
            String str = getID() + "(STD)";
            String str2 = getID() + "(DST)";
            int initialRawOffset = initialRawOffset() * 1000;
            int initialDstOffset = initialDstOffset() * 1000;
            this.initialRule = new InitialTimeZoneRule(initialDstOffset == 0 ? str : str2, initialRawOffset, initialDstOffset);
            if (this.transitionCount > 0) {
                int i = 0;
                while (i < this.transitionCount && getInt(this.typeMapData[i]) == 0) {
                    this.firstTZTransitionIdx++;
                    i++;
                }
                if (i != this.transitionCount) {
                    long[] jArr = new long[this.transitionCount];
                    int i2 = 0;
                    while (true) {
                        long j2 = 1000;
                        if (i2 >= this.typeCount) {
                            break;
                        }
                        int i3 = this.firstTZTransitionIdx;
                        int i4 = 0;
                        while (i3 < this.transitionCount) {
                            if (i2 == getInt(this.typeMapData[i3])) {
                                long j3 = this.transitionTimes64[i3] * j2;
                                if (((double) j3) < this.finalStartMillis) {
                                    jArr[i4] = j3;
                                    i4++;
                                }
                            }
                            i3++;
                            j2 = 1000;
                        }
                        if (i4 > 0) {
                            long[] jArr2 = new long[i4];
                            System.arraycopy(jArr, 0, jArr2, 0, i4);
                            int i5 = i2 * 2;
                            int i6 = this.typeOffsets[i5] * 1000;
                            int i7 = this.typeOffsets[i5 + 1] * 1000;
                            if (this.historicRules == null) {
                                this.historicRules = new TimeArrayTimeZoneRule[this.typeCount];
                            }
                            this.historicRules[i2] = new TimeArrayTimeZoneRule(i7 == 0 ? str : str2, i6, i7, jArr2, 2);
                        }
                        i2++;
                    }
                    this.firstTZTransition = new TimeZoneTransition(this.transitionTimes64[this.firstTZTransitionIdx] * 1000, this.initialRule, this.historicRules[getInt(this.typeMapData[this.firstTZTransitionIdx])]);
                }
            }
            if (this.finalZone != null) {
                long j4 = (long) this.finalStartMillis;
                if (this.finalZone.useDaylightTime()) {
                    this.finalZoneWithStartYear = (SimpleTimeZone) this.finalZone.clone();
                    this.finalZoneWithStartYear.setStartYear(this.finalStartYear);
                    TimeZoneTransition nextTransition = this.finalZoneWithStartYear.getNextTransition(j4, false);
                    timeZoneRule = nextTransition.getTo();
                    j = nextTransition.getTime();
                } else {
                    this.finalZoneWithStartYear = this.finalZone;
                    timeZoneRule = new TimeArrayTimeZoneRule(this.finalZone.getID(), this.finalZone.getRawOffset(), 0, new long[]{j4}, 2);
                    j = j4;
                }
                if (this.transitionCount > 0) {
                    timeZoneRule2 = this.historicRules[getInt(this.typeMapData[this.transitionCount - 1])];
                }
                if (timeZoneRule2 == null) {
                    timeZoneRule2 = this.initialRule;
                }
                this.firstFinalTZTransition = new TimeZoneTransition(j, timeZoneRule2, timeZoneRule);
            }
            this.transitionRulesInitialized = true;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0026  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readObject(java.io.ObjectInputStream r6) throws java.io.IOException, java.lang.ClassNotFoundException {
        /*
            r5 = this;
            r6.defaultReadObject()
            int r6 = r5.serialVersionOnStream
            r0 = 0
            r1 = 1
            if (r6 >= r1) goto L_0x0029
            java.lang.String r6 = r5.getID()
            if (r6 == 0) goto L_0x0023
            java.lang.String r2 = "ohos/global/icu/impl/data/icudt67b"
            java.lang.String r3 = "zoneinfo64"
            java.lang.ClassLoader r4 = ohos.global.icu.impl.ICUResourceBundle.ICU_DATA_CLASS_LOADER     // Catch:{ Exception -> 0x0023 }
            ohos.global.icu.util.UResourceBundle r2 = ohos.global.icu.util.UResourceBundle.getBundleInstance(r2, r3, r4)     // Catch:{ Exception -> 0x0023 }
            ohos.global.icu.util.UResourceBundle r3 = ohos.global.icu.impl.ZoneMeta.openOlsonResource(r2, r6)     // Catch:{ Exception -> 0x0023 }
            r5.construct(r2, r3, r6)     // Catch:{ Exception -> 0x0023 }
            goto L_0x0024
        L_0x0023:
            r1 = r0
        L_0x0024:
            if (r1 != 0) goto L_0x0029
            r5.constructEmpty()
        L_0x0029:
            r5.transitionRulesInitialized = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.OlsonTimeZone.readObject(java.io.ObjectInputStream):void");
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
        OlsonTimeZone olsonTimeZone = (OlsonTimeZone) super.cloneAsThawed();
        SimpleTimeZone simpleTimeZone = this.finalZone;
        if (simpleTimeZone != null) {
            olsonTimeZone.finalZone = (SimpleTimeZone) simpleTimeZone.clone();
        }
        olsonTimeZone.isFrozen = false;
        return olsonTimeZone;
    }
}
