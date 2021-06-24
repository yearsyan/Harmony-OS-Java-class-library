package ohos.global.icu.util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;
import ohos.global.icu.impl.Grego;

public class RuleBasedTimeZone extends BasicTimeZone {
    private static final long serialVersionUID = 7580833058949327935L;
    private AnnualTimeZoneRule[] finalRules;
    private List<TimeZoneRule> historicRules;
    private transient List<TimeZoneTransition> historicTransitions;
    private final InitialTimeZoneRule initialRule;
    private volatile transient boolean isFrozen = false;
    private transient boolean upToDate;

    private static int getLocalDelta(int i, int i2, int i3, int i4, int i5, int i6) {
        int i7 = i + i2;
        int i8 = i3 + i4;
        boolean z = false;
        boolean z2 = i2 != 0 && i4 == 0;
        if (i2 == 0 && i4 != 0) {
            z = true;
        }
        if (i8 - i7 >= 0) {
            int i9 = i5 & 3;
            if (i9 == 1 && z2) {
                return i7;
            }
            if (i9 == 3 && z) {
                return i7;
            }
            if ((i9 != 1 || !z) && ((i9 != 3 || !z2) && (i5 & 12) == 12)) {
                return i7;
            }
        } else {
            int i10 = i6 & 3;
            if ((i10 != 1 || !z2) && (i10 != 3 || !z)) {
                if (i10 == 1 && z) {
                    return i7;
                }
                if ((i10 == 3 && z2) || (i6 & 12) == 4) {
                    return i7;
                }
            }
        }
        return i8;
    }

    public RuleBasedTimeZone(String str, InitialTimeZoneRule initialTimeZoneRule) {
        super(str);
        this.initialRule = initialTimeZoneRule;
    }

    public void addTransitionRule(TimeZoneRule timeZoneRule) {
        if (isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen RuleBasedTimeZone instance.");
        } else if (timeZoneRule.isTransitionRule()) {
            if (timeZoneRule instanceof AnnualTimeZoneRule) {
                AnnualTimeZoneRule annualTimeZoneRule = (AnnualTimeZoneRule) timeZoneRule;
                if (annualTimeZoneRule.getEndYear() == Integer.MAX_VALUE) {
                    AnnualTimeZoneRule[] annualTimeZoneRuleArr = this.finalRules;
                    if (annualTimeZoneRuleArr == null) {
                        this.finalRules = new AnnualTimeZoneRule[2];
                        this.finalRules[0] = annualTimeZoneRule;
                    } else if (annualTimeZoneRuleArr[1] == null) {
                        annualTimeZoneRuleArr[1] = annualTimeZoneRule;
                    } else {
                        throw new IllegalStateException("Too many final rules");
                    }
                    this.upToDate = false;
                }
            }
            if (this.historicRules == null) {
                this.historicRules = new ArrayList();
            }
            this.historicRules.add(timeZoneRule);
            this.upToDate = false;
        } else {
            throw new IllegalArgumentException("Rule must be a transition rule");
        }
    }

    @Override // ohos.global.icu.util.TimeZone
    public int getOffset(int i, int i2, int i3, int i4, int i5, int i6) {
        if (i == 0) {
            i2 = 1 - i2;
        }
        int[] iArr = new int[2];
        getOffset((Grego.fieldsToDay(i2, i3, i4) * 86400000) + ((long) i6), true, 3, 1, iArr);
        return iArr[0] + iArr[1];
    }

    @Override // ohos.global.icu.util.TimeZone
    public void getOffset(long j, boolean z, int[] iArr) {
        getOffset(j, z, 4, 12, iArr);
    }

    @Override // ohos.global.icu.util.BasicTimeZone
    @Deprecated
    public void getOffsetFromLocal(long j, int i, int i2, int[] iArr) {
        getOffset(j, true, i, i2, iArr);
    }

    @Override // ohos.global.icu.util.TimeZone
    public int getRawOffset() {
        int[] iArr = new int[2];
        getOffset(System.currentTimeMillis(), false, iArr);
        return iArr[0];
    }

    @Override // ohos.global.icu.util.TimeZone
    public boolean inDaylightTime(Date date) {
        int[] iArr = new int[2];
        getOffset(date.getTime(), false, iArr);
        return iArr[1] != 0;
    }

    @Override // ohos.global.icu.util.TimeZone
    public void setRawOffset(int i) {
        throw new UnsupportedOperationException("setRawOffset in RuleBasedTimeZone is not supported.");
    }

    @Override // ohos.global.icu.util.TimeZone
    public boolean useDaylightTime() {
        long currentTimeMillis = System.currentTimeMillis();
        int[] iArr = new int[2];
        getOffset(currentTimeMillis, false, iArr);
        if (iArr[1] != 0) {
            return true;
        }
        TimeZoneTransition nextTransition = getNextTransition(currentTimeMillis, false);
        if (nextTransition == null || nextTransition.getTo().getDSTSavings() == 0) {
            return false;
        }
        return true;
    }

    @Override // ohos.global.icu.util.TimeZone
    public boolean observesDaylightTime() {
        long currentTimeMillis = System.currentTimeMillis();
        int[] iArr = new int[2];
        getOffset(currentTimeMillis, false, iArr);
        if (iArr[1] != 0) {
            return true;
        }
        AnnualTimeZoneRule[] annualTimeZoneRuleArr = this.finalRules;
        BitSet bitSet = annualTimeZoneRuleArr == null ? null : new BitSet(annualTimeZoneRuleArr.length);
        while (true) {
            TimeZoneTransition nextTransition = getNextTransition(currentTimeMillis, false);
            if (nextTransition == null) {
                break;
            }
            TimeZoneRule to = nextTransition.getTo();
            if (to.getDSTSavings() != 0) {
                return true;
            }
            if (bitSet != null) {
                int i = 0;
                while (true) {
                    AnnualTimeZoneRule[] annualTimeZoneRuleArr2 = this.finalRules;
                    if (i >= annualTimeZoneRuleArr2.length) {
                        break;
                    }
                    if (annualTimeZoneRuleArr2[i].equals(to)) {
                        bitSet.set(i);
                    }
                    i++;
                }
                if (bitSet.cardinality() == this.finalRules.length) {
                    break;
                }
            }
            currentTimeMillis = nextTransition.getTime();
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0050, code lost:
        if (r7.finalRules == null) goto L_0x0053;
     */
    @Override // ohos.global.icu.util.TimeZone
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasSameRules(ohos.global.icu.util.TimeZone r7) {
        /*
        // Method dump skipped, instructions count: 163
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.RuleBasedTimeZone.hasSameRules(ohos.global.icu.util.TimeZone):boolean");
    }

    @Override // ohos.global.icu.util.BasicTimeZone
    public TimeZoneRule[] getTimeZoneRules() {
        int i;
        List<TimeZoneRule> list = this.historicRules;
        int size = list != null ? list.size() + 1 : 1;
        AnnualTimeZoneRule[] annualTimeZoneRuleArr = this.finalRules;
        if (annualTimeZoneRuleArr != null) {
            size = annualTimeZoneRuleArr[1] != null ? size + 2 : size + 1;
        }
        TimeZoneRule[] timeZoneRuleArr = new TimeZoneRule[size];
        timeZoneRuleArr[0] = this.initialRule;
        if (this.historicRules != null) {
            i = 1;
            while (i < this.historicRules.size() + 1) {
                timeZoneRuleArr[i] = this.historicRules.get(i - 1);
                i++;
            }
        } else {
            i = 1;
        }
        AnnualTimeZoneRule[] annualTimeZoneRuleArr2 = this.finalRules;
        if (annualTimeZoneRuleArr2 != null) {
            int i2 = i + 1;
            timeZoneRuleArr[i] = annualTimeZoneRuleArr2[0];
            if (annualTimeZoneRuleArr2[1] != null) {
                timeZoneRuleArr[i2] = annualTimeZoneRuleArr2[1];
            }
        }
        return timeZoneRuleArr;
    }

    @Override // ohos.global.icu.util.BasicTimeZone
    public TimeZoneTransition getNextTransition(long j, boolean z) {
        complete();
        List<TimeZoneTransition> list = this.historicTransitions;
        if (list == null) {
            return null;
        }
        TimeZoneTransition timeZoneTransition = list.get(0);
        int i = (timeZoneTransition.getTime() > j ? 1 : (timeZoneTransition.getTime() == j ? 0 : -1));
        boolean z2 = true;
        if (i > 0 || (z && i == 0)) {
            z2 = false;
        } else {
            int size = this.historicTransitions.size() - 1;
            TimeZoneTransition timeZoneTransition2 = this.historicTransitions.get(size);
            long time = timeZoneTransition2.getTime();
            if (!z || time != j) {
                if (time > j) {
                    int i2 = size - 1;
                    while (i2 > 0) {
                        TimeZoneTransition timeZoneTransition3 = this.historicTransitions.get(i2);
                        int i3 = (timeZoneTransition3.getTime() > j ? 1 : (timeZoneTransition3.getTime() == j ? 0 : -1));
                        if (i3 < 0 || (!z && i3 == 0)) {
                            break;
                        }
                        i2--;
                        timeZoneTransition2 = timeZoneTransition3;
                    }
                } else {
                    AnnualTimeZoneRule[] annualTimeZoneRuleArr = this.finalRules;
                    if (annualTimeZoneRuleArr == null) {
                        return null;
                    }
                    Date nextStart = annualTimeZoneRuleArr[0].getNextStart(j, annualTimeZoneRuleArr[1].getRawOffset(), this.finalRules[1].getDSTSavings(), z);
                    AnnualTimeZoneRule[] annualTimeZoneRuleArr2 = this.finalRules;
                    Date nextStart2 = annualTimeZoneRuleArr2[1].getNextStart(j, annualTimeZoneRuleArr2[0].getRawOffset(), this.finalRules[0].getDSTSavings(), z);
                    if (nextStart2.after(nextStart)) {
                        long time2 = nextStart.getTime();
                        AnnualTimeZoneRule[] annualTimeZoneRuleArr3 = this.finalRules;
                        timeZoneTransition = new TimeZoneTransition(time2, annualTimeZoneRuleArr3[1], annualTimeZoneRuleArr3[0]);
                    } else {
                        long time3 = nextStart2.getTime();
                        AnnualTimeZoneRule[] annualTimeZoneRuleArr4 = this.finalRules;
                        timeZoneTransition = new TimeZoneTransition(time3, annualTimeZoneRuleArr4[0], annualTimeZoneRuleArr4[1]);
                    }
                }
            }
            z2 = false;
            timeZoneTransition = timeZoneTransition2;
        }
        TimeZoneRule from = timeZoneTransition.getFrom();
        TimeZoneRule to = timeZoneTransition.getTo();
        if (from.getRawOffset() != to.getRawOffset() || from.getDSTSavings() != to.getDSTSavings()) {
            return timeZoneTransition;
        }
        if (z2) {
            return null;
        }
        return getNextTransition(timeZoneTransition.getTime(), false);
    }

    @Override // ohos.global.icu.util.BasicTimeZone
    public TimeZoneTransition getPreviousTransition(long j, boolean z) {
        complete();
        List<TimeZoneTransition> list = this.historicTransitions;
        if (list == null) {
            return null;
        }
        TimeZoneTransition timeZoneTransition = list.get(0);
        long time = timeZoneTransition.getTime();
        if (!z || time != j) {
            if (time >= j) {
                return null;
            }
            int size = this.historicTransitions.size() - 1;
            TimeZoneTransition timeZoneTransition2 = this.historicTransitions.get(size);
            long time2 = timeZoneTransition2.getTime();
            if (!z || time2 != j) {
                if (time2 >= j) {
                    while (true) {
                        size--;
                        if (size < 0) {
                            break;
                        }
                        timeZoneTransition2 = this.historicTransitions.get(size);
                        int i = (timeZoneTransition2.getTime() > j ? 1 : (timeZoneTransition2.getTime() == j ? 0 : -1));
                        if (i < 0 || (z && i == 0)) {
                            break;
                        }
                    }
                } else {
                    AnnualTimeZoneRule[] annualTimeZoneRuleArr = this.finalRules;
                    if (annualTimeZoneRuleArr != null) {
                        Date previousStart = annualTimeZoneRuleArr[0].getPreviousStart(j, annualTimeZoneRuleArr[1].getRawOffset(), this.finalRules[1].getDSTSavings(), z);
                        AnnualTimeZoneRule[] annualTimeZoneRuleArr2 = this.finalRules;
                        Date previousStart2 = annualTimeZoneRuleArr2[1].getPreviousStart(j, annualTimeZoneRuleArr2[0].getRawOffset(), this.finalRules[0].getDSTSavings(), z);
                        if (previousStart2.before(previousStart)) {
                            long time3 = previousStart.getTime();
                            AnnualTimeZoneRule[] annualTimeZoneRuleArr3 = this.finalRules;
                            timeZoneTransition = new TimeZoneTransition(time3, annualTimeZoneRuleArr3[1], annualTimeZoneRuleArr3[0]);
                        } else {
                            long time4 = previousStart2.getTime();
                            AnnualTimeZoneRule[] annualTimeZoneRuleArr4 = this.finalRules;
                            timeZoneTransition = new TimeZoneTransition(time4, annualTimeZoneRuleArr4[0], annualTimeZoneRuleArr4[1]);
                        }
                    }
                }
            }
            timeZoneTransition = timeZoneTransition2;
        }
        TimeZoneRule from = timeZoneTransition.getFrom();
        TimeZoneRule to = timeZoneTransition.getTo();
        return (from.getRawOffset() == to.getRawOffset() && from.getDSTSavings() == to.getDSTSavings()) ? getPreviousTransition(timeZoneTransition.getTime(), false) : timeZoneTransition;
    }

    @Override // ohos.global.icu.util.TimeZone, java.lang.Object
    public Object clone() {
        if (isFrozen()) {
            return this;
        }
        return cloneAsThawed();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v5, types: [ohos.global.icu.util.AnnualTimeZoneRule[]] */
    /* JADX WARN: Type inference failed for: r10v6 */
    /* JADX WARN: Type inference failed for: r13v5, types: [ohos.global.icu.util.TimeZoneRule] */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void complete() {
        /*
        // Method dump skipped, instructions count: 478
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.RuleBasedTimeZone.complete():void");
    }

    private void getOffset(long j, boolean z, int i, int i2, int[] iArr) {
        TimeZoneRule timeZoneRule;
        complete();
        List<TimeZoneTransition> list = this.historicTransitions;
        if (list == null) {
            timeZoneRule = this.initialRule;
        } else if (j < getTransitionTime(list.get(0), z, i, i2)) {
            timeZoneRule = this.initialRule;
        } else {
            int size = this.historicTransitions.size() - 1;
            if (j > getTransitionTime(this.historicTransitions.get(size), z, i, i2)) {
                TimeZoneRule findRuleInFinal = this.finalRules != null ? findRuleInFinal(j, z, i, i2) : null;
                timeZoneRule = findRuleInFinal == null ? this.historicTransitions.get(size).getTo() : findRuleInFinal;
            } else {
                while (size >= 0 && j < getTransitionTime(this.historicTransitions.get(size), z, i, i2)) {
                    size--;
                }
                timeZoneRule = this.historicTransitions.get(size).getTo();
            }
        }
        iArr[0] = timeZoneRule.getRawOffset();
        iArr[1] = timeZoneRule.getDSTSavings();
    }

    private TimeZoneRule findRuleInFinal(long j, boolean z, int i, int i2) {
        AnnualTimeZoneRule[] annualTimeZoneRuleArr = this.finalRules;
        if (!(annualTimeZoneRuleArr == null || annualTimeZoneRuleArr.length != 2 || annualTimeZoneRuleArr[0] == null || annualTimeZoneRuleArr[1] == null)) {
            long localDelta = z ? j - ((long) getLocalDelta(annualTimeZoneRuleArr[1].getRawOffset(), this.finalRules[1].getDSTSavings(), this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), i, i2)) : j;
            AnnualTimeZoneRule[] annualTimeZoneRuleArr2 = this.finalRules;
            Date previousStart = annualTimeZoneRuleArr2[0].getPreviousStart(localDelta, annualTimeZoneRuleArr2[1].getRawOffset(), this.finalRules[1].getDSTSavings(), true);
            long localDelta2 = z ? j - ((long) getLocalDelta(this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), i, i2)) : j;
            AnnualTimeZoneRule[] annualTimeZoneRuleArr3 = this.finalRules;
            Date previousStart2 = annualTimeZoneRuleArr3[1].getPreviousStart(localDelta2, annualTimeZoneRuleArr3[0].getRawOffset(), this.finalRules[0].getDSTSavings(), true);
            if (previousStart != null && previousStart2 != null) {
                boolean after = previousStart.after(previousStart2);
                AnnualTimeZoneRule[] annualTimeZoneRuleArr4 = this.finalRules;
                return after ? annualTimeZoneRuleArr4[0] : annualTimeZoneRuleArr4[1];
            } else if (previousStart != null) {
                return this.finalRules[0];
            } else {
                if (previousStart2 != null) {
                    return this.finalRules[1];
                }
            }
        }
        return null;
    }

    private static long getTransitionTime(TimeZoneTransition timeZoneTransition, boolean z, int i, int i2) {
        long time = timeZoneTransition.getTime();
        return z ? time + ((long) getLocalDelta(timeZoneTransition.getFrom().getRawOffset(), timeZoneTransition.getFrom().getDSTSavings(), timeZoneTransition.getTo().getRawOffset(), timeZoneTransition.getTo().getDSTSavings(), i, i2)) : time;
    }

    @Override // ohos.global.icu.util.TimeZone, ohos.global.icu.util.Freezable
    public boolean isFrozen() {
        return this.isFrozen;
    }

    @Override // ohos.global.icu.util.TimeZone, ohos.global.icu.util.TimeZone, ohos.global.icu.util.Freezable
    public TimeZone freeze() {
        complete();
        this.isFrozen = true;
        return this;
    }

    @Override // ohos.global.icu.util.TimeZone, ohos.global.icu.util.TimeZone, ohos.global.icu.util.Freezable
    public TimeZone cloneAsThawed() {
        RuleBasedTimeZone ruleBasedTimeZone = (RuleBasedTimeZone) super.cloneAsThawed();
        List<TimeZoneRule> list = this.historicRules;
        if (list != null) {
            ruleBasedTimeZone.historicRules = new ArrayList(list);
        }
        AnnualTimeZoneRule[] annualTimeZoneRuleArr = this.finalRules;
        if (annualTimeZoneRuleArr != null) {
            ruleBasedTimeZone.finalRules = (AnnualTimeZoneRule[]) annualTimeZoneRuleArr.clone();
        }
        ruleBasedTimeZone.isFrozen = false;
        return ruleBasedTimeZone;
    }
}
