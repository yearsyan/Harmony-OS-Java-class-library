package ohos.global.icu.text;

import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Map;
import ohos.global.icu.impl.CharacterIteratorWrapper;
import ohos.global.icu.impl.coll.Collation;
import ohos.global.icu.impl.coll.CollationData;
import ohos.global.icu.impl.coll.CollationIterator;
import ohos.global.icu.impl.coll.ContractionsAndExpansions;
import ohos.global.icu.impl.coll.FCDIterCollationIterator;
import ohos.global.icu.impl.coll.FCDUTF16CollationIterator;
import ohos.global.icu.impl.coll.IterCollationIterator;
import ohos.global.icu.impl.coll.UTF16CollationIterator;
import ohos.global.icu.impl.coll.UVector32;

public final class CollationElementIterator {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int IGNORABLE = 0;
    public static final int NULLORDER = -1;
    private byte dir_;
    private CollationIterator iter_;
    private UVector32 offsets_;
    private int otherHalf_;
    private RuleBasedCollator rbc_;
    private String string_;

    /* access modifiers changed from: private */
    public static final boolean ceNeedsTwoParts(long j) {
        return (j & 281470698455103L) != 0;
    }

    /* access modifiers changed from: private */
    public static final int getFirstHalf(long j, int i) {
        return (((int) j) & -65536) | ((i >> 16) & 65280) | ((i >> 8) & 255);
    }

    /* access modifiers changed from: private */
    public static final int getSecondHalf(long j, int i) {
        return (((int) j) << 16) | ((i >> 8) & 65280) | (i & 63);
    }

    public static final int primaryOrder(int i) {
        return (i >>> 16) & 65535;
    }

    public static final int secondaryOrder(int i) {
        return (i >>> 8) & 255;
    }

    public static final int tertiaryOrder(int i) {
        return i & 255;
    }

    public int hashCode() {
        return 42;
    }

    private CollationElementIterator(RuleBasedCollator ruleBasedCollator) {
        this.iter_ = null;
        this.rbc_ = ruleBasedCollator;
        this.otherHalf_ = 0;
        this.dir_ = 0;
        this.offsets_ = null;
    }

    CollationElementIterator(String str, RuleBasedCollator ruleBasedCollator) {
        this(ruleBasedCollator);
        setText(str);
    }

    CollationElementIterator(CharacterIterator characterIterator, RuleBasedCollator ruleBasedCollator) {
        this(ruleBasedCollator);
        setText(characterIterator);
    }

    CollationElementIterator(UCharacterIterator uCharacterIterator, RuleBasedCollator ruleBasedCollator) {
        this(ruleBasedCollator);
        setText(uCharacterIterator);
    }

    public int getOffset() {
        UVector32 uVector32;
        if (this.dir_ >= 0 || (uVector32 = this.offsets_) == null || uVector32.isEmpty()) {
            return this.iter_.getOffset();
        }
        int cEsLength = this.iter_.getCEsLength();
        if (this.otherHalf_ != 0) {
            cEsLength++;
        }
        return this.offsets_.elementAti(cEsLength);
    }

    public int next() {
        byte b = this.dir_;
        if (b > 1) {
            int i = this.otherHalf_;
            if (i != 0) {
                this.otherHalf_ = 0;
                return i;
            }
        } else if (b == 1) {
            this.dir_ = 2;
        } else if (b == 0) {
            this.dir_ = 2;
        } else {
            throw new IllegalStateException("Illegal change of direction");
        }
        this.iter_.clearCEsIfNoneRemaining();
        long nextCE = this.iter_.nextCE();
        if (nextCE == Collation.NO_CE) {
            return -1;
        }
        long j = nextCE >>> 32;
        int i2 = (int) nextCE;
        int firstHalf = getFirstHalf(j, i2);
        int secondHalf = getSecondHalf(j, i2);
        if (secondHalf != 0) {
            this.otherHalf_ = secondHalf | 192;
        }
        return firstHalf;
    }

    public int previous() {
        byte b = this.dir_;
        int i = 0;
        if (b < 0) {
            int i2 = this.otherHalf_;
            if (i2 != 0) {
                this.otherHalf_ = 0;
                return i2;
            }
        } else if (b == 0) {
            this.iter_.resetToOffset(this.string_.length());
            this.dir_ = -1;
        } else if (b == 1) {
            this.dir_ = -1;
        } else {
            throw new IllegalStateException("Illegal change of direction");
        }
        if (this.offsets_ == null) {
            this.offsets_ = new UVector32();
        }
        if (this.iter_.getCEsLength() == 0) {
            i = this.iter_.getOffset();
        }
        long previousCE = this.iter_.previousCE(this.offsets_);
        if (previousCE == Collation.NO_CE) {
            return -1;
        }
        long j = previousCE >>> 32;
        int i3 = (int) previousCE;
        int firstHalf = getFirstHalf(j, i3);
        int secondHalf = getSecondHalf(j, i3);
        if (secondHalf == 0) {
            return firstHalf;
        }
        if (this.offsets_.isEmpty()) {
            this.offsets_.addElement(this.iter_.getOffset());
            this.offsets_.addElement(i);
        }
        this.otherHalf_ = firstHalf;
        return secondHalf | 192;
    }

    public void reset() {
        this.iter_.resetToOffset(0);
        this.otherHalf_ = 0;
        this.dir_ = 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0034 A[LOOP:1: B:13:0x0034->B:18:0x0049, LOOP_START, PHI: r0 
      PHI: (r0v6 int) = (r0v5 int), (r0v7 int) binds: [B:12:0x0032, B:18:0x0049] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setOffset(int r4) {
        /*
            r3 = this;
            if (r4 <= 0) goto L_0x004c
            java.lang.String r0 = r3.string_
            int r0 = r0.length()
            if (r4 >= r0) goto L_0x004c
            r0 = r4
        L_0x000b:
            java.lang.String r1 = r3.string_
            char r1 = r1.charAt(r0)
            ohos.global.icu.text.RuleBasedCollator r2 = r3.rbc_
            boolean r2 = r2.isUnsafe(r1)
            if (r2 == 0) goto L_0x0032
            boolean r1 = java.lang.Character.isHighSurrogate(r1)
            if (r1 == 0) goto L_0x002e
            ohos.global.icu.text.RuleBasedCollator r1 = r3.rbc_
            java.lang.String r2 = r3.string_
            int r2 = r2.codePointAt(r0)
            boolean r1 = r1.isUnsafe(r2)
            if (r1 != 0) goto L_0x002e
            goto L_0x0032
        L_0x002e:
            int r0 = r0 + -1
            if (r0 > 0) goto L_0x000b
        L_0x0032:
            if (r0 >= r4) goto L_0x004c
        L_0x0034:
            ohos.global.icu.impl.coll.CollationIterator r1 = r3.iter_
            r1.resetToOffset(r0)
        L_0x0039:
            ohos.global.icu.impl.coll.CollationIterator r1 = r3.iter_
            r1.nextCE()
            ohos.global.icu.impl.coll.CollationIterator r1 = r3.iter_
            int r1 = r1.getOffset()
            if (r1 == r0) goto L_0x0039
            if (r1 > r4) goto L_0x0049
            r0 = r1
        L_0x0049:
            if (r1 < r4) goto L_0x0034
            r4 = r0
        L_0x004c:
            ohos.global.icu.impl.coll.CollationIterator r0 = r3.iter_
            r0.resetToOffset(r4)
            r4 = 0
            r3.otherHalf_ = r4
            r4 = 1
            r3.dir_ = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.CollationElementIterator.setOffset(int):void");
    }

    public void setText(String str) {
        CollationIterator collationIterator;
        this.string_ = str;
        boolean isNumeric = this.rbc_.settings.readOnly().isNumeric();
        if (this.rbc_.settings.readOnly().dontCheckFCD()) {
            collationIterator = new UTF16CollationIterator(this.rbc_.data, isNumeric, this.string_, 0);
        } else {
            collationIterator = new FCDUTF16CollationIterator(this.rbc_.data, isNumeric, this.string_, 0);
        }
        this.iter_ = collationIterator;
        this.otherHalf_ = 0;
        this.dir_ = 0;
    }

    public void setText(UCharacterIterator uCharacterIterator) {
        CollationIterator collationIterator;
        this.string_ = uCharacterIterator.getText();
        try {
            UCharacterIterator uCharacterIterator2 = (UCharacterIterator) uCharacterIterator.clone();
            uCharacterIterator2.setToStart();
            boolean isNumeric = this.rbc_.settings.readOnly().isNumeric();
            if (this.rbc_.settings.readOnly().dontCheckFCD()) {
                collationIterator = new IterCollationIterator(this.rbc_.data, isNumeric, uCharacterIterator2);
            } else {
                collationIterator = new FCDIterCollationIterator(this.rbc_.data, isNumeric, uCharacterIterator2, 0);
            }
            this.iter_ = collationIterator;
            this.otherHalf_ = 0;
            this.dir_ = 0;
        } catch (CloneNotSupportedException unused) {
            setText(uCharacterIterator.getText());
        }
    }

    public void setText(CharacterIterator characterIterator) {
        CollationIterator collationIterator;
        CharacterIteratorWrapper characterIteratorWrapper = new CharacterIteratorWrapper(characterIterator);
        characterIteratorWrapper.setToStart();
        this.string_ = characterIteratorWrapper.getText();
        boolean isNumeric = this.rbc_.settings.readOnly().isNumeric();
        if (this.rbc_.settings.readOnly().dontCheckFCD()) {
            collationIterator = new IterCollationIterator(this.rbc_.data, isNumeric, characterIteratorWrapper);
        } else {
            collationIterator = new FCDIterCollationIterator(this.rbc_.data, isNumeric, characterIteratorWrapper, 0);
        }
        this.iter_ = collationIterator;
        this.otherHalf_ = 0;
        this.dir_ = 0;
    }

    private static final class MaxExpSink implements ContractionsAndExpansions.CESink {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private Map<Integer, Integer> maxExpansions;

        @Override // ohos.global.icu.impl.coll.ContractionsAndExpansions.CESink
        public void handleCE(long j) {
        }

        MaxExpSink(Map<Integer, Integer> map) {
            this.maxExpansions = map;
        }

        @Override // ohos.global.icu.impl.coll.ContractionsAndExpansions.CESink
        public void handleExpansion(long[] jArr, int i, int i2) {
            if (i2 > 1) {
                int i3 = 0;
                for (int i4 = 0; i4 < i2; i4++) {
                    i3 += CollationElementIterator.ceNeedsTwoParts(jArr[i + i4]) ? 2 : 1;
                }
                long j = jArr[(i + i2) - 1];
                long j2 = j >>> 32;
                int i5 = (int) j;
                int secondHalf = CollationElementIterator.getSecondHalf(j2, i5);
                int firstHalf = secondHalf == 0 ? CollationElementIterator.getFirstHalf(j2, i5) : secondHalf | 192;
                Integer num = this.maxExpansions.get(Integer.valueOf(firstHalf));
                if (num == null || i3 > num.intValue()) {
                    this.maxExpansions.put(Integer.valueOf(firstHalf), Integer.valueOf(i3));
                }
            }
        }
    }

    static final Map<Integer, Integer> computeMaxExpansions(CollationData collationData) {
        HashMap hashMap = new HashMap();
        new ContractionsAndExpansions(null, null, new MaxExpSink(hashMap), true).forData(collationData);
        return hashMap;
    }

    public int getMaxExpansion(int i) {
        return getMaxExpansion(this.rbc_.tailoring.maxExpansions, i);
    }

    static int getMaxExpansion(Map<Integer, Integer> map, int i) {
        Integer num;
        if (i == 0) {
            return 1;
        }
        if (map != null && (num = map.get(Integer.valueOf(i))) != null) {
            return num.intValue();
        }
        if ((i & 192) == 192) {
            return 2;
        }
        return 1;
    }

    private byte normalizeDir() {
        byte b = this.dir_;
        if (b == 1) {
            return 0;
        }
        return b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CollationElementIterator)) {
            return false;
        }
        CollationElementIterator collationElementIterator = (CollationElementIterator) obj;
        return this.rbc_.equals(collationElementIterator.rbc_) && this.otherHalf_ == collationElementIterator.otherHalf_ && normalizeDir() == collationElementIterator.normalizeDir() && this.string_.equals(collationElementIterator.string_) && this.iter_.equals(collationElementIterator.iter_);
    }

    @Deprecated
    public RuleBasedCollator getRuleBasedCollator() {
        return this.rbc_;
    }
}
