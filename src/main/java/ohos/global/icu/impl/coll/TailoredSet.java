package ohos.global.icu.impl.coll;

import java.util.Iterator;
import ohos.global.icu.impl.Normalizer2Impl;
import ohos.global.icu.impl.Trie2;
import ohos.global.icu.text.UnicodeSet;

public final class TailoredSet {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private CollationData baseData;
    private CollationData data;
    private String suffix;
    private UnicodeSet tailored;
    private StringBuilder unreversedPrefix = new StringBuilder();

    public TailoredSet(UnicodeSet unicodeSet) {
        this.tailored = unicodeSet;
    }

    public void forData(CollationData collationData) {
        this.data = collationData;
        this.baseData = collationData.base;
        Iterator<Trie2.Range> it = this.data.trie.iterator();
        while (it.hasNext()) {
            Trie2.Range next = it.next();
            if (!next.leadSurrogate) {
                enumTailoredRange(next.startCodePoint, next.endCodePoint, next.value, this);
            } else {
                return;
            }
        }
    }

    private void enumTailoredRange(int i, int i2, int i3, TailoredSet tailoredSet) {
        if (i3 != 192) {
            tailoredSet.handleCE32(i, i2, i3);
        }
    }

    private void handleCE32(int i, int i2, int i3) {
        if (!Collation.isSpecialCE32(i3) || (i3 = this.data.getIndirectCE32(i3)) != 192) {
            do {
                CollationData collationData = this.baseData;
                int finalCE32 = collationData.getFinalCE32(collationData.getCE32(i));
                if (!Collation.isSelfContainedCE32(i3) || !Collation.isSelfContainedCE32(finalCE32)) {
                    compare(i, i3, finalCE32);
                } else if (i3 != finalCE32) {
                    this.tailored.add(i);
                }
                i++;
            } while (i <= i2);
        }
    }

    private void compare(int i, int i2, int i3) {
        int i4;
        if (Collation.isPrefixCE32(i2)) {
            int indexFromCE32 = Collation.indexFromCE32(i2);
            CollationData collationData = this.data;
            int finalCE32 = collationData.getFinalCE32(collationData.getCE32FromContexts(indexFromCE32));
            if (Collation.isPrefixCE32(i3)) {
                int indexFromCE322 = Collation.indexFromCE32(i3);
                CollationData collationData2 = this.baseData;
                int finalCE322 = collationData2.getFinalCE32(collationData2.getCE32FromContexts(indexFromCE322));
                comparePrefixes(i, this.data.contexts, indexFromCE32 + 2, this.baseData.contexts, indexFromCE322 + 2);
                i3 = finalCE322;
            } else {
                CollationData collationData3 = this.data;
                addPrefixes(collationData3, i, collationData3.contexts, indexFromCE32 + 2);
            }
            i2 = finalCE32;
        } else if (Collation.isPrefixCE32(i3)) {
            int indexFromCE323 = Collation.indexFromCE32(i3);
            CollationData collationData4 = this.baseData;
            int finalCE323 = collationData4.getFinalCE32(collationData4.getCE32FromContexts(indexFromCE323));
            CollationData collationData5 = this.baseData;
            addPrefixes(collationData5, i, collationData5.contexts, indexFromCE323 + 2);
            i3 = finalCE323;
        }
        if (Collation.isContractionCE32(i2)) {
            int indexFromCE324 = Collation.indexFromCE32(i2);
            if ((i2 & 256) != 0) {
                i2 = 1;
            } else {
                CollationData collationData6 = this.data;
                i2 = collationData6.getFinalCE32(collationData6.getCE32FromContexts(indexFromCE324));
            }
            if (Collation.isContractionCE32(i3)) {
                int indexFromCE325 = Collation.indexFromCE32(i3);
                if ((i3 & 256) != 0) {
                    i3 = 1;
                } else {
                    CollationData collationData7 = this.baseData;
                    i3 = collationData7.getFinalCE32(collationData7.getCE32FromContexts(indexFromCE325));
                }
                compareContractions(i, this.data.contexts, indexFromCE324 + 2, this.baseData.contexts, indexFromCE325 + 2);
            } else {
                addContractions(i, this.data.contexts, indexFromCE324 + 2);
            }
        } else if (Collation.isContractionCE32(i3)) {
            int indexFromCE326 = Collation.indexFromCE32(i3);
            CollationData collationData8 = this.baseData;
            int finalCE324 = collationData8.getFinalCE32(collationData8.getCE32FromContexts(indexFromCE326));
            addContractions(i, this.baseData.contexts, indexFromCE326 + 2);
            i3 = finalCE324;
        }
        int i5 = -1;
        if (Collation.isSpecialCE32(i2)) {
            i4 = Collation.tagFromCE32(i2);
        } else {
            i4 = -1;
        }
        if (Collation.isSpecialCE32(i3)) {
            i5 = Collation.tagFromCE32(i3);
        }
        if (i5 == 14) {
            if (!Collation.isLongPrimaryCE32(i2)) {
                add(i);
                return;
            }
            if (Collation.primaryFromLongPrimaryCE32(i2) != Collation.getThreeBytePrimaryForOffsetData(i, this.baseData.ces[Collation.indexFromCE32(i3)])) {
                add(i);
                return;
            }
        }
        if (i4 != i5) {
            add(i);
            return;
        }
        int i6 = 0;
        if (i4 == 5) {
            int lengthFromCE32 = Collation.lengthFromCE32(i2);
            if (lengthFromCE32 != Collation.lengthFromCE32(i3)) {
                add(i);
                return;
            }
            int indexFromCE327 = Collation.indexFromCE32(i2);
            int indexFromCE328 = Collation.indexFromCE32(i3);
            while (i6 < lengthFromCE32) {
                if (this.data.ce32s[indexFromCE327 + i6] != this.baseData.ce32s[indexFromCE328 + i6]) {
                    add(i);
                    return;
                }
                i6++;
            }
        } else if (i4 == 6) {
            int lengthFromCE322 = Collation.lengthFromCE32(i2);
            if (lengthFromCE322 != Collation.lengthFromCE32(i3)) {
                add(i);
                return;
            }
            int indexFromCE329 = Collation.indexFromCE32(i2);
            int indexFromCE3210 = Collation.indexFromCE32(i3);
            while (i6 < lengthFromCE322) {
                if (this.data.ces[indexFromCE329 + i6] != this.baseData.ces[indexFromCE3210 + i6]) {
                    add(i);
                    return;
                }
                i6++;
            }
        } else if (i4 == 12) {
            StringBuilder sb = new StringBuilder();
            int decompose = Normalizer2Impl.Hangul.decompose(i, sb);
            if (this.tailored.contains(sb.charAt(0)) || this.tailored.contains(sb.charAt(1)) || (decompose == 3 && this.tailored.contains(sb.charAt(2)))) {
                add(i);
            }
        } else if (i2 != i3) {
            add(i);
        }
    }

    /* JADX WARN: Type inference failed for: r8v1, types: [ohos.global.icu.util.CharsTrie$Iterator] */
    /* JADX WARN: Type inference failed for: r9v2, types: [ohos.global.icu.util.CharsTrie$Iterator] */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void comparePrefixes(int r7, java.lang.CharSequence r8, int r9, java.lang.CharSequence r10, int r11) {
        /*
        // Method dump skipped, instructions count: 128
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.coll.TailoredSet.comparePrefixes(int, java.lang.CharSequence, int, java.lang.CharSequence, int):void");
    }

    /* JADX WARN: Type inference failed for: r8v1, types: [ohos.global.icu.util.CharsTrie$Iterator] */
    /* JADX WARN: Type inference failed for: r9v2, types: [ohos.global.icu.util.CharsTrie$Iterator] */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void compareContractions(int r7, java.lang.CharSequence r8, int r9, java.lang.CharSequence r10, int r11) {
        /*
        // Method dump skipped, instructions count: 118
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.coll.TailoredSet.compareContractions(int, java.lang.CharSequence, int, java.lang.CharSequence, int):void");
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [ohos.global.icu.util.CharsTrie$Iterator] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addPrefixes(ohos.global.icu.impl.coll.CollationData r2, int r3, java.lang.CharSequence r4, int r5) {
        /*
            r1 = this;
            ohos.global.icu.util.CharsTrie r0 = new ohos.global.icu.util.CharsTrie
            r0.<init>(r4, r5)
            ohos.global.icu.util.CharsTrie$Iterator r4 = r0.iterator()
        L_0x0009:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x001b
            ohos.global.icu.util.CharsTrie$Entry r5 = r4.next()
            java.lang.CharSequence r0 = r5.chars
            int r5 = r5.value
            r1.addPrefix(r2, r0, r3, r5)
            goto L_0x0009
        L_0x001b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.coll.TailoredSet.addPrefixes(ohos.global.icu.impl.coll.CollationData, int, java.lang.CharSequence, int):void");
    }

    private void addPrefix(CollationData collationData, CharSequence charSequence, int i, int i2) {
        setPrefix(charSequence);
        int finalCE32 = collationData.getFinalCE32(i2);
        if (Collation.isContractionCE32(finalCE32)) {
            addContractions(i, collationData.contexts, Collation.indexFromCE32(finalCE32) + 2);
        }
        this.tailored.add(new StringBuilder(this.unreversedPrefix.appendCodePoint(i)));
        resetPrefix();
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [ohos.global.icu.util.CharsTrie$Iterator] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addContractions(int r2, java.lang.CharSequence r3, int r4) {
        /*
            r1 = this;
            ohos.global.icu.util.CharsTrie r0 = new ohos.global.icu.util.CharsTrie
            r0.<init>(r3, r4)
            ohos.global.icu.util.CharsTrie$Iterator r3 = r0.iterator()
        L_0x0009:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x0019
            ohos.global.icu.util.CharsTrie$Entry r4 = r3.next()
            java.lang.CharSequence r4 = r4.chars
            r1.addSuffix(r2, r4)
            goto L_0x0009
        L_0x0019:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.coll.TailoredSet.addContractions(int, java.lang.CharSequence, int):void");
    }

    private void addSuffix(int i, CharSequence charSequence) {
        UnicodeSet unicodeSet = this.tailored;
        StringBuilder appendCodePoint = new StringBuilder(this.unreversedPrefix).appendCodePoint(i);
        appendCodePoint.append(charSequence);
        unicodeSet.add(appendCodePoint);
    }

    private void add(int i) {
        if (this.unreversedPrefix.length() == 0 && this.suffix == null) {
            this.tailored.add(i);
            return;
        }
        StringBuilder sb = new StringBuilder(this.unreversedPrefix);
        sb.appendCodePoint(i);
        String str = this.suffix;
        if (str != null) {
            sb.append(str);
        }
        this.tailored.add(sb);
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
