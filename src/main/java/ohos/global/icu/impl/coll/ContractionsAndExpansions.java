package ohos.global.icu.impl.coll;

import java.util.Iterator;
import ohos.global.icu.impl.Trie2;
import ohos.global.icu.text.UnicodeSet;

public final class ContractionsAndExpansions {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private boolean addPrefixes;
    private long[] ces = new long[31];
    private int checkTailored = 0;
    private UnicodeSet contractions;
    private CollationData data;
    private UnicodeSet expansions;
    private UnicodeSet ranges;
    private CESink sink;
    private String suffix;
    private UnicodeSet tailored = new UnicodeSet();
    private StringBuilder unreversedPrefix = new StringBuilder();

    public interface CESink {
        void handleCE(long j);

        void handleExpansion(long[] jArr, int i, int i2);
    }

    public ContractionsAndExpansions(UnicodeSet unicodeSet, UnicodeSet unicodeSet2, CESink cESink, boolean z) {
        this.contractions = unicodeSet;
        this.expansions = unicodeSet2;
        this.sink = cESink;
        this.addPrefixes = z;
    }

    public void forData(CollationData collationData) {
        if (collationData.base != null) {
            this.checkTailored = -1;
        }
        this.data = collationData;
        Iterator<Trie2.Range> it = this.data.trie.iterator();
        while (it.hasNext()) {
            Trie2.Range next = it.next();
            if (next.leadSurrogate) {
                break;
            }
            enumCnERange(next.startCodePoint, next.endCodePoint, next.value, this);
        }
        if (collationData.base != null) {
            this.tailored.freeze();
            this.checkTailored = 1;
            this.data = collationData.base;
            Iterator<Trie2.Range> it2 = this.data.trie.iterator();
            while (it2.hasNext()) {
                Trie2.Range next2 = it2.next();
                if (!next2.leadSurrogate) {
                    enumCnERange(next2.startCodePoint, next2.endCodePoint, next2.value, this);
                } else {
                    return;
                }
            }
        }
    }

    private void enumCnERange(int i, int i2, int i3, ContractionsAndExpansions contractionsAndExpansions) {
        int i4 = contractionsAndExpansions.checkTailored;
        if (i4 != 0) {
            if (i4 < 0) {
                if (i3 != 192) {
                    contractionsAndExpansions.tailored.add(i, i2);
                } else {
                    return;
                }
            } else if (i == i2) {
                if (contractionsAndExpansions.tailored.contains(i)) {
                    return;
                }
            } else if (contractionsAndExpansions.tailored.containsSome(i, i2)) {
                if (contractionsAndExpansions.ranges == null) {
                    contractionsAndExpansions.ranges = new UnicodeSet();
                }
                contractionsAndExpansions.ranges.set(i, i2).removeAll(contractionsAndExpansions.tailored);
                int rangeCount = contractionsAndExpansions.ranges.getRangeCount();
                for (int i5 = 0; i5 < rangeCount; i5++) {
                    contractionsAndExpansions.handleCE32(contractionsAndExpansions.ranges.getRangeStart(i5), contractionsAndExpansions.ranges.getRangeEnd(i5), i3);
                }
            }
        }
        contractionsAndExpansions.handleCE32(i, i2, i3);
    }

    public void forCodePoint(CollationData collationData, int i) {
        int ce32 = collationData.getCE32(i);
        if (ce32 == 192) {
            collationData = collationData.base;
            ce32 = collationData.getCE32(i);
        }
        this.data = collationData;
        handleCE32(i, i, ce32);
    }

    private void handleCE32(int i, int i2, int i3) {
        while ((i3 & 255) >= 192) {
            switch (Collation.tagFromCE32(i3)) {
                case 0:
                    return;
                case 1:
                    CESink cESink = this.sink;
                    if (cESink != null) {
                        cESink.handleCE(Collation.ceFromLongPrimaryCE32(i3));
                        return;
                    }
                    return;
                case 2:
                    CESink cESink2 = this.sink;
                    if (cESink2 != null) {
                        cESink2.handleCE(Collation.ceFromLongSecondaryCE32(i3));
                        return;
                    }
                    return;
                case 3:
                case 7:
                case 13:
                    throw new AssertionError(String.format("Unexpected CE32 tag type %d for ce32=0x%08x", Integer.valueOf(Collation.tagFromCE32(i3)), Integer.valueOf(i3)));
                case 4:
                    if (this.sink != null) {
                        this.ces[0] = Collation.latinCE0FromCE32(i3);
                        this.ces[1] = Collation.latinCE1FromCE32(i3);
                        this.sink.handleExpansion(this.ces, 0, 2);
                    }
                    if (this.unreversedPrefix.length() == 0) {
                        addExpansions(i, i2);
                        return;
                    }
                    return;
                case 5:
                    if (this.sink != null) {
                        int indexFromCE32 = Collation.indexFromCE32(i3);
                        int lengthFromCE32 = Collation.lengthFromCE32(i3);
                        for (int i4 = 0; i4 < lengthFromCE32; i4++) {
                            this.ces[i4] = Collation.ceFromCE32(this.data.ce32s[indexFromCE32 + i4]);
                        }
                        this.sink.handleExpansion(this.ces, 0, lengthFromCE32);
                    }
                    if (this.unreversedPrefix.length() == 0) {
                        addExpansions(i, i2);
                        return;
                    }
                    return;
                case 6:
                    if (this.sink != null) {
                        this.sink.handleExpansion(this.data.ces, Collation.indexFromCE32(i3), Collation.lengthFromCE32(i3));
                    }
                    if (this.unreversedPrefix.length() == 0) {
                        addExpansions(i, i2);
                        return;
                    }
                    return;
                case 8:
                    handlePrefixes(i, i2, i3);
                    return;
                case 9:
                    handleContractions(i, i2, i3);
                    return;
                case 10:
                    i3 = this.data.ce32s[Collation.indexFromCE32(i3)];
                    break;
                case 11:
                    i3 = this.data.ce32s[0];
                    break;
                case 12:
                    if (this.sink != null) {
                        UTF16CollationIterator uTF16CollationIterator = new UTF16CollationIterator(this.data);
                        StringBuilder sb = new StringBuilder(1);
                        for (int i5 = i; i5 <= i2; i5++) {
                            sb.setLength(0);
                            sb.appendCodePoint(i5);
                            uTF16CollationIterator.setText(false, sb, 0);
                            this.sink.handleExpansion(uTF16CollationIterator.getCEs(), 0, uTF16CollationIterator.fetchCEs() - 1);
                        }
                    }
                    if (this.unreversedPrefix.length() == 0) {
                        addExpansions(i, i2);
                        return;
                    }
                    return;
                case 14:
                case 15:
                    return;
            }
        }
        CESink cESink3 = this.sink;
        if (cESink3 != null) {
            cESink3.handleCE(Collation.ceFromSimpleCE32(i3));
        }
    }

    /* JADX WARN: Type inference failed for: r5v3, types: [ohos.global.icu.util.CharsTrie$Iterator] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void handlePrefixes(int r3, int r4, int r5) {
        /*
            r2 = this;
            int r5 = ohos.global.icu.impl.coll.Collation.indexFromCE32(r5)
            ohos.global.icu.impl.coll.CollationData r0 = r2.data
            int r0 = r0.getCE32FromContexts(r5)
            r2.handleCE32(r3, r4, r0)
            boolean r0 = r2.addPrefixes
            if (r0 != 0) goto L_0x0012
            return
        L_0x0012:
            ohos.global.icu.util.CharsTrie r0 = new ohos.global.icu.util.CharsTrie
            ohos.global.icu.impl.coll.CollationData r1 = r2.data
            java.lang.String r1 = r1.contexts
            int r5 = r5 + 2
            r0.<init>(r1, r5)
            ohos.global.icu.util.CharsTrie$Iterator r5 = r0.iterator()
        L_0x0021:
            boolean r0 = r5.hasNext()
            if (r0 == 0) goto L_0x0040
            ohos.global.icu.util.CharsTrie$Entry r0 = r5.next()
            java.lang.CharSequence r1 = r0.chars
            r2.setPrefix(r1)
            ohos.global.icu.text.UnicodeSet r1 = r2.contractions
            r2.addStrings(r3, r4, r1)
            ohos.global.icu.text.UnicodeSet r1 = r2.expansions
            r2.addStrings(r3, r4, r1)
            int r0 = r0.value
            r2.handleCE32(r3, r4, r0)
            goto L_0x0021
        L_0x0040:
            r2.resetPrefix()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.coll.ContractionsAndExpansions.handlePrefixes(int, int, int):void");
    }

    /* JADX WARN: Type inference failed for: r5v3, types: [ohos.global.icu.util.CharsTrie$Iterator] */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleContractions(int r3, int r4, int r5) {
        /*
            r2 = this;
            int r0 = ohos.global.icu.impl.coll.Collation.indexFromCE32(r5)
            r5 = r5 & 256(0x100, float:3.59E-43)
            if (r5 == 0) goto L_0x0009
            goto L_0x0012
        L_0x0009:
            ohos.global.icu.impl.coll.CollationData r5 = r2.data
            int r5 = r5.getCE32FromContexts(r0)
            r2.handleCE32(r3, r4, r5)
        L_0x0012:
            ohos.global.icu.util.CharsTrie r5 = new ohos.global.icu.util.CharsTrie
            ohos.global.icu.impl.coll.CollationData r1 = r2.data
            java.lang.String r1 = r1.contexts
            int r0 = r0 + 2
            r5.<init>(r1, r0)
            ohos.global.icu.util.CharsTrie$Iterator r5 = r5.iterator()
        L_0x0021:
            boolean r0 = r5.hasNext()
            if (r0 == 0) goto L_0x004b
            ohos.global.icu.util.CharsTrie$Entry r0 = r5.next()
            java.lang.CharSequence r1 = r0.chars
            java.lang.String r1 = r1.toString()
            r2.suffix = r1
            ohos.global.icu.text.UnicodeSet r1 = r2.contractions
            r2.addStrings(r3, r4, r1)
            java.lang.StringBuilder r1 = r2.unreversedPrefix
            int r1 = r1.length()
            if (r1 == 0) goto L_0x0045
            ohos.global.icu.text.UnicodeSet r1 = r2.expansions
            r2.addStrings(r3, r4, r1)
        L_0x0045:
            int r0 = r0.value
            r2.handleCE32(r3, r4, r0)
            goto L_0x0021
        L_0x004b:
            r3 = 0
            r2.suffix = r3
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.coll.ContractionsAndExpansions.handleContractions(int, int, int):void");
    }

    /* access modifiers changed from: package-private */
    public void addExpansions(int i, int i2) {
        if (this.unreversedPrefix.length() == 0 && this.suffix == null) {
            UnicodeSet unicodeSet = this.expansions;
            if (unicodeSet != null) {
                unicodeSet.add(i, i2);
                return;
            }
            return;
        }
        addStrings(i, i2, this.expansions);
    }

    /* access modifiers changed from: package-private */
    public void addStrings(int i, int i2, UnicodeSet unicodeSet) {
        if (unicodeSet != null) {
            StringBuilder sb = new StringBuilder(this.unreversedPrefix);
            do {
                sb.appendCodePoint(i);
                String str = this.suffix;
                if (str != null) {
                    sb.append(str);
                }
                unicodeSet.add(sb);
                sb.setLength(this.unreversedPrefix.length());
                i++;
            } while (i <= i2);
        }
    }

    private void setPrefix(CharSequence charSequence) {
        this.unreversedPrefix.setLength(0);
        StringBuilder sb = this.unreversedPrefix;
        sb.append(charSequence);
        sb.reverse();
    }

    private void resetPrefix() {
        this.unreversedPrefix.setLength(0);
    }
}
