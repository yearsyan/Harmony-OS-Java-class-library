package ohos.global.icu.impl.coll;

import java.text.ParseException;
import ohos.global.icu.impl.Norm2AllModes;
import ohos.global.icu.impl.Normalizer2Impl;
import ohos.global.icu.impl.coll.CollationDataBuilder;
import ohos.global.icu.impl.coll.CollationRuleParser;
import ohos.global.icu.text.CanonicalIterator;
import ohos.global.icu.text.Normalizer2;
import ohos.global.icu.text.UnicodeSet;
import ohos.global.icu.text.UnicodeSetIterator;
import ohos.global.icu.util.ULocale;

public final class CollationBuilder extends CollationRuleParser.Sink {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final UnicodeSet COMPOSITES = new UnicodeSet("[:NFD_QC=N:]");
    private static final boolean DEBUG = false;
    private static final int HAS_BEFORE2 = 64;
    private static final int HAS_BEFORE3 = 32;
    private static final int IS_TAILORED = 8;
    private static final int MAX_INDEX = 1048575;
    private CollationTailoring base;
    private CollationData baseData;
    private long[] ces = new long[31];
    private int cesLength;
    private CollationDataBuilder dataBuilder;
    private boolean fastLatinEnabled;
    private Normalizer2 fcd = Norm2AllModes.getFCDNormalizer2();
    private Normalizer2Impl nfcImpl = Norm2AllModes.getNFCInstance().impl;
    private Normalizer2 nfd = Normalizer2.getNFDInstance();
    private UVector64 nodes;
    private UnicodeSet optimizeSet = new UnicodeSet();
    private CollationRootElements rootElements;
    private UVector32 rootPrimaryIndexes;
    private long variableTop;

    private static final int alignWeightRight(int i) {
        if (i != 0) {
            while ((i & 255) == 0) {
                i >>>= 8;
            }
        }
        return i;
    }

    /* access modifiers changed from: private */
    public static int indexFromTempCE(long j) {
        long j2 = j - 4629700417037541376L;
        return (((int) (j2 >> 24)) & 63) | (((int) (j2 >> 43)) & 1040384) | (((int) (j2 >> 42)) & 8128);
    }

    /* access modifiers changed from: private */
    public static int indexFromTempCE32(int i) {
        int i2 = i - 1077937696;
        return ((i2 >> 8) & 63) | ((i2 >> 11) & 1040384) | ((i2 >> 10) & 8128);
    }

    private static boolean isTailoredNode(long j) {
        return (j & 8) != 0;
    }

    /* access modifiers changed from: private */
    public static boolean isTempCE(long j) {
        int i = ((int) j) >>> 24;
        return 6 <= i && i <= 69;
    }

    /* access modifiers changed from: private */
    public static boolean isTempCE32(int i) {
        int i2;
        return (i & 255) >= 2 && 6 <= (i2 = (i >> 8) & 255) && i2 <= 69;
    }

    private static int nextIndexFromNode(long j) {
        return (((int) j) >> 8) & MAX_INDEX;
    }

    private static long nodeFromNextIndex(int i) {
        return (long) (i << 8);
    }

    private static long nodeFromPreviousIndex(int i) {
        return ((long) i) << 28;
    }

    private static long nodeFromStrength(int i) {
        return (long) i;
    }

    private static long nodeFromWeight16(int i) {
        return ((long) i) << 48;
    }

    private static long nodeFromWeight32(long j) {
        return j << 32;
    }

    private static boolean nodeHasAnyBefore(long j) {
        return (j & 96) != 0;
    }

    private static boolean nodeHasBefore2(long j) {
        return (j & 64) != 0;
    }

    private static boolean nodeHasBefore3(long j) {
        return (j & 32) != 0;
    }

    private static int previousIndexFromNode(long j) {
        return ((int) (j >> 28)) & MAX_INDEX;
    }

    private static int strengthFromNode(long j) {
        return ((int) j) & 3;
    }

    private static int strengthFromTempCE(long j) {
        return (((int) j) >> 8) & 3;
    }

    private static long tempCEFromIndexAndStrength(int i, int i2) {
        return (((long) (1040384 & i)) << 43) + 4629700417037541376L + (((long) (i & 8128)) << 42) + ((long) ((i & 63) << 24)) + ((long) (i2 << 8));
    }

    private static int weight16FromNode(long j) {
        return ((int) (j >> 48)) & 65535;
    }

    private static long weight32FromNode(long j) {
        return j >>> 32;
    }

    private static final class BundleImporter implements CollationRuleParser.Importer {
        BundleImporter() {
        }

        @Override // ohos.global.icu.impl.coll.CollationRuleParser.Importer
        public String getRules(String str, String str2) {
            return CollationLoader.loadRules(new ULocale(str), str2);
        }
    }

    public CollationBuilder(CollationTailoring collationTailoring) {
        this.base = collationTailoring;
        this.baseData = collationTailoring.data;
        this.rootElements = new CollationRootElements(collationTailoring.data.rootElements);
        this.variableTop = 0;
        this.dataBuilder = new CollationDataBuilder();
        this.fastLatinEnabled = true;
        this.cesLength = 0;
        this.rootPrimaryIndexes = new UVector32();
        this.nodes = new UVector64();
        this.nfcImpl.ensureCanonIterData();
        this.dataBuilder.initForTailoring(this.baseData);
    }

    public CollationTailoring parseAndBuild(String str) throws ParseException {
        if (this.baseData.rootElements != null) {
            CollationTailoring collationTailoring = new CollationTailoring(this.base.settings);
            CollationRuleParser collationRuleParser = new CollationRuleParser(this.baseData);
            this.variableTop = this.base.settings.readOnly().variableTop;
            collationRuleParser.setSink(this);
            collationRuleParser.setImporter(new BundleImporter());
            CollationSettings copyOnWrite = collationTailoring.settings.copyOnWrite();
            collationRuleParser.parse(str, copyOnWrite);
            if (this.dataBuilder.hasMappings()) {
                makeTailoredCEs();
                closeOverComposites();
                finalizeCEs();
                this.optimizeSet.add(0, 127);
                this.optimizeSet.add(192, 255);
                this.optimizeSet.remove(Normalizer2Impl.Hangul.HANGUL_BASE, Normalizer2Impl.Hangul.HANGUL_END);
                this.dataBuilder.optimize(this.optimizeSet);
                collationTailoring.ensureOwnedData();
                if (this.fastLatinEnabled) {
                    this.dataBuilder.enableFastLatin();
                }
                this.dataBuilder.build(collationTailoring.ownedData);
                this.dataBuilder = null;
            } else {
                collationTailoring.data = this.baseData;
            }
            copyOnWrite.fastLatinOptions = CollationFastLatin.getOptions(collationTailoring.data, copyOnWrite, copyOnWrite.fastLatinPrimaries);
            collationTailoring.setRules(str);
            collationTailoring.setVersion(this.base.version, 0);
            return collationTailoring;
        }
        throw new UnsupportedOperationException("missing root elements data, tailoring not supported");
    }

    /* access modifiers changed from: package-private */
    @Override // ohos.global.icu.impl.coll.CollationRuleParser.Sink
    public void addReset(int i, CharSequence charSequence) {
        int i2;
        int i3;
        if (charSequence.charAt(0) == 65534) {
            this.ces[0] = getSpecialResetPosition(charSequence);
            this.cesLength = 1;
        } else {
            this.cesLength = this.dataBuilder.getCEs(this.nfd.normalize(charSequence), this.ces, 0);
            if (this.cesLength > 31) {
                throw new IllegalArgumentException("reset position maps to too many collation elements (more than 31)");
            }
        }
        if (i != 15) {
            int findOrInsertNodeForCEs = findOrInsertNodeForCEs(i);
            long elementAti = this.nodes.elementAti(findOrInsertNodeForCEs);
            while (strengthFromNode(elementAti) > i) {
                findOrInsertNodeForCEs = previousIndexFromNode(elementAti);
                elementAti = this.nodes.elementAti(findOrInsertNodeForCEs);
            }
            if (strengthFromNode(elementAti) == i && isTailoredNode(elementAti)) {
                i2 = previousIndexFromNode(elementAti);
            } else if (i == 0) {
                long weight32FromNode = weight32FromNode(elementAti);
                if (weight32FromNode == 0) {
                    throw new UnsupportedOperationException("reset primary-before ignorable not possible");
                } else if (weight32FromNode <= this.rootElements.getFirstPrimary()) {
                    throw new UnsupportedOperationException("reset primary-before first non-ignorable not supported");
                } else if (weight32FromNode != 4278321664L) {
                    i2 = findOrInsertNodeForPrimary(this.rootElements.getPrimaryBefore(weight32FromNode, this.baseData.isCompressiblePrimary(weight32FromNode)));
                    while (true) {
                        int nextIndexFromNode = nextIndexFromNode(this.nodes.elementAti(i2));
                        if (nextIndexFromNode == 0) {
                            break;
                        }
                        i2 = nextIndexFromNode;
                    }
                } else {
                    throw new UnsupportedOperationException("reset primary-before [first trailing] not supported");
                }
            } else {
                int findCommonNode = findCommonNode(findOrInsertNodeForCEs, 1);
                if (i >= 2) {
                    findCommonNode = findCommonNode(findCommonNode, 2);
                }
                long elementAti2 = this.nodes.elementAti(findCommonNode);
                if (strengthFromNode(elementAti2) != i) {
                    i2 = findOrInsertWeakNode(findCommonNode, getWeight16Before(findCommonNode, elementAti2, i), i);
                } else if (weight16FromNode(elementAti2) == 0) {
                    throw new UnsupportedOperationException(i == 1 ? "reset secondary-before secondary ignorable not possible" : "reset tertiary-before completely ignorable not possible");
                } else {
                    int weight16Before = getWeight16Before(findCommonNode, elementAti2, i);
                    int previousIndexFromNode = previousIndexFromNode(elementAti2);
                    int i4 = previousIndexFromNode;
                    while (true) {
                        long elementAti3 = this.nodes.elementAti(i4);
                        int strengthFromNode = strengthFromNode(elementAti3);
                        if (strengthFromNode >= i) {
                            if (strengthFromNode == i && !isTailoredNode(elementAti3)) {
                                i3 = weight16FromNode(elementAti3);
                                break;
                            }
                            i4 = previousIndexFromNode(elementAti3);
                        } else {
                            i3 = 1280;
                            break;
                        }
                    }
                    if (i3 != weight16Before) {
                        previousIndexFromNode = insertNodeBetween(previousIndexFromNode, findCommonNode, nodeFromWeight16(weight16Before) | nodeFromStrength(i));
                    }
                    i2 = previousIndexFromNode;
                }
                i = ceStrength(this.ces[this.cesLength - 1]);
            }
            this.ces[this.cesLength - 1] = tempCEFromIndexAndStrength(i2, i);
        }
    }

    private int getWeight16Before(int i, long j, int i2) {
        int i3 = 1280;
        int weight16FromNode = strengthFromNode(j) == 2 ? weight16FromNode(j) : 1280;
        while (strengthFromNode(j) > 1) {
            j = this.nodes.elementAti(previousIndexFromNode(j));
        }
        if (isTailoredNode(j)) {
            return 256;
        }
        if (strengthFromNode(j) == 1) {
            i3 = weight16FromNode(j);
        }
        while (strengthFromNode(j) > 0) {
            j = this.nodes.elementAti(previousIndexFromNode(j));
        }
        if (isTailoredNode(j)) {
            return 256;
        }
        long weight32FromNode = weight32FromNode(j);
        if (i2 == 1) {
            return this.rootElements.getSecondaryBefore(weight32FromNode, i3);
        }
        return this.rootElements.getTertiaryBefore(weight32FromNode, i3, weight16FromNode);
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x00d5  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x014b A[LOOP:1: B:50:0x014b->B:57:0x0169, LOOP_START, PHI: r6 r7 
      PHI: (r6v2 int) = (r6v1 int), (r6v3 int) binds: [B:33:0x00d3, B:57:0x0169] A[DONT_GENERATE, DONT_INLINE]
      PHI: (r7v2 long) = (r7v1 long), (r7v3 long) binds: [B:33:0x00d3, B:57:0x0169] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long getSpecialResetPosition(java.lang.CharSequence r10) {
        /*
        // Method dump skipped, instructions count: 444
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.coll.CollationBuilder.getSpecialResetPosition(java.lang.CharSequence):long");
    }

    /* access modifiers changed from: package-private */
    @Override // ohos.global.icu.impl.coll.CollationRuleParser.Sink
    public void addRelation(int i, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        String str;
        if (charSequence.length() == 0) {
            str = "";
        } else {
            str = this.nfd.normalize(charSequence);
        }
        String normalize = this.nfd.normalize(charSequence2);
        int length = normalize.length();
        if (length >= 2) {
            char charAt = normalize.charAt(0);
            if (Normalizer2Impl.Hangul.isJamoL(charAt) || Normalizer2Impl.Hangul.isJamoV(charAt)) {
                throw new UnsupportedOperationException("contractions starting with conjoining Jamo L or V not supported");
            }
            char charAt2 = normalize.charAt(length - 1);
            if (Normalizer2Impl.Hangul.isJamoL(charAt2) || (Normalizer2Impl.Hangul.isJamoV(charAt2) && Normalizer2Impl.Hangul.isJamoL(normalize.charAt(length - 2)))) {
                throw new UnsupportedOperationException("contractions ending with conjoining Jamo L or L+V not supported");
            }
        }
        if (i != 15) {
            int findOrInsertNodeForCEs = findOrInsertNodeForCEs(i);
            long j = this.ces[this.cesLength - 1];
            if (i == 0 && !isTempCE(j) && (j >>> 32) == 0) {
                throw new UnsupportedOperationException("tailoring primary after ignorables not supported");
            } else if (i == 3 && j == 0) {
                throw new UnsupportedOperationException("tailoring quaternary after tertiary ignorables not supported");
            } else {
                int insertTailoredNodeAfter = insertTailoredNodeAfter(findOrInsertNodeForCEs, i);
                int ceStrength = ceStrength(j);
                if (i >= ceStrength) {
                    i = ceStrength;
                }
                this.ces[this.cesLength - 1] = tempCEFromIndexAndStrength(insertTailoredNodeAfter, i);
            }
        }
        setCaseBits(normalize);
        int i2 = this.cesLength;
        if (charSequence3.length() != 0) {
            this.cesLength = this.dataBuilder.getCEs(this.nfd.normalize(charSequence3), this.ces, this.cesLength);
            if (this.cesLength > 31) {
                throw new IllegalArgumentException("extension string adds too many collation elements (more than 31 total)");
            }
        }
        addWithClosure(str, normalize, this.ces, this.cesLength, ((!str.contentEquals(charSequence) || !normalize.contentEquals(charSequence2)) && !ignorePrefix(charSequence) && !ignoreString(charSequence2)) ? addIfDifferent(charSequence, charSequence2, this.ces, this.cesLength, -1) : -1);
        this.cesLength = i2;
    }

    private int findOrInsertNodeForCEs(int i) {
        long j;
        while (true) {
            int i2 = this.cesLength;
            if (i2 == 0) {
                j = 0;
                this.ces[0] = 0;
                this.cesLength = 1;
                break;
            }
            j = this.ces[i2 - 1];
            if (ceStrength(j) <= i) {
                break;
            }
            this.cesLength--;
        }
        if (isTempCE(j)) {
            return indexFromTempCE(j);
        }
        if (((int) (j >>> 56)) != 254) {
            return findOrInsertNodeForRootCE(j, i);
        }
        throw new UnsupportedOperationException("tailoring relative to an unassigned code point not supported");
    }

    private int findOrInsertNodeForRootCE(long j, int i) {
        int findOrInsertNodeForPrimary = findOrInsertNodeForPrimary(j >>> 32);
        if (i < 1) {
            return findOrInsertNodeForPrimary;
        }
        int i2 = (int) j;
        int findOrInsertWeakNode = findOrInsertWeakNode(findOrInsertNodeForPrimary, i2 >>> 16, 1);
        return i >= 2 ? findOrInsertWeakNode(findOrInsertWeakNode, i2 & Collation.ONLY_TERTIARY_MASK, 2) : findOrInsertWeakNode;
    }

    private static final int binarySearchForRootPrimaryNode(int[] iArr, int i, long[] jArr, long j) {
        if (i == 0) {
            return -1;
        }
        int i2 = 0;
        while (true) {
            int i3 = (int) ((((long) i2) + ((long) i)) / 2);
            int i4 = (j > (jArr[iArr[i3]] >>> 32) ? 1 : (j == (jArr[iArr[i3]] >>> 32) ? 0 : -1));
            if (i4 == 0) {
                return i3;
            }
            if (i4 < 0) {
                if (i3 == i2) {
                    return ~i2;
                }
                i = i3;
            } else if (i3 == i2) {
                return ~(i2 + 1);
            } else {
                i2 = i3;
            }
        }
    }

    private int findOrInsertNodeForPrimary(long j) {
        int binarySearchForRootPrimaryNode = binarySearchForRootPrimaryNode(this.rootPrimaryIndexes.getBuffer(), this.rootPrimaryIndexes.size(), this.nodes.getBuffer(), j);
        if (binarySearchForRootPrimaryNode >= 0) {
            return this.rootPrimaryIndexes.elementAti(binarySearchForRootPrimaryNode);
        }
        int size = this.nodes.size();
        this.nodes.addElement(nodeFromWeight32(j));
        this.rootPrimaryIndexes.insertElementAt(size, ~binarySearchForRootPrimaryNode);
        return size;
    }

    private int findOrInsertWeakNode(int i, int i2, int i3) {
        int nextIndexFromNode;
        if (i2 == 1280) {
            return findCommonNode(i, i3);
        }
        long elementAti = this.nodes.elementAti(i);
        if (i2 != 0 && i2 < 1280) {
            long j = (long) (i3 == 1 ? 64 : 32);
            if ((elementAti & j) == 0) {
                long nodeFromWeight16 = nodeFromWeight16(1280) | nodeFromStrength(i3);
                if (i3 == 1) {
                    nodeFromWeight16 |= 32 & elementAti;
                    elementAti &= -33;
                }
                this.nodes.setElementAt(elementAti | j, i);
                int nextIndexFromNode2 = nextIndexFromNode(elementAti);
                int insertNodeBetween = insertNodeBetween(i, nextIndexFromNode2, nodeFromStrength(i3) | nodeFromWeight16(i2));
                insertNodeBetween(insertNodeBetween, nextIndexFromNode2, nodeFromWeight16);
                return insertNodeBetween;
            }
        }
        while (true) {
            nextIndexFromNode = nextIndexFromNode(elementAti);
            if (nextIndexFromNode == 0) {
                break;
            }
            elementAti = this.nodes.elementAti(nextIndexFromNode);
            int strengthFromNode = strengthFromNode(elementAti);
            if (strengthFromNode <= i3) {
                if (strengthFromNode < i3) {
                    break;
                } else if (isTailoredNode(elementAti)) {
                    continue;
                } else {
                    int weight16FromNode = weight16FromNode(elementAti);
                    if (weight16FromNode == i2) {
                        return nextIndexFromNode;
                    }
                    if (weight16FromNode > i2) {
                        break;
                    }
                }
            }
            i = nextIndexFromNode;
        }
        return insertNodeBetween(i, nextIndexFromNode, nodeFromStrength(i3) | nodeFromWeight16(i2));
    }

    private int insertTailoredNodeAfter(int i, int i2) {
        int nextIndexFromNode;
        if (i2 >= 1) {
            i = findCommonNode(i, 1);
            if (i2 >= 2) {
                i = findCommonNode(i, 2);
            }
        }
        long elementAti = this.nodes.elementAti(i);
        while (true) {
            nextIndexFromNode = nextIndexFromNode(elementAti);
            if (nextIndexFromNode == 0) {
                break;
            }
            long elementAti2 = this.nodes.elementAti(nextIndexFromNode);
            if (strengthFromNode(elementAti2) <= i2) {
                break;
            }
            i = nextIndexFromNode;
            elementAti = elementAti2;
        }
        return insertNodeBetween(i, nextIndexFromNode, 8 | nodeFromStrength(i2));
    }

    private int insertNodeBetween(int i, int i2, long j) {
        int size = this.nodes.size();
        this.nodes.addElement(j | nodeFromPreviousIndex(i) | nodeFromNextIndex(i2));
        this.nodes.setElementAt(changeNodeNextIndex(this.nodes.elementAti(i), size), i);
        if (i2 != 0) {
            this.nodes.setElementAt(changeNodePreviousIndex(this.nodes.elementAti(i2), size), i2);
        }
        return size;
    }

    private int findCommonNode(int i, int i2) {
        long elementAti = this.nodes.elementAti(i);
        if (strengthFromNode(elementAti) >= i2) {
            return i;
        }
        if (i2 != 1 ? !nodeHasBefore3(elementAti) : !nodeHasBefore2(elementAti)) {
            return i;
        }
        long elementAti2 = this.nodes.elementAti(nextIndexFromNode(elementAti));
        while (true) {
            int nextIndexFromNode = nextIndexFromNode(elementAti2);
            elementAti2 = this.nodes.elementAti(nextIndexFromNode);
            if (!isTailoredNode(elementAti2) && strengthFromNode(elementAti2) <= i2 && weight16FromNode(elementAti2) >= 1280) {
                return nextIndexFromNode;
            }
        }
    }

    private void setCaseBits(CharSequence charSequence) {
        int i = 0;
        for (int i2 = 0; i2 < this.cesLength; i2++) {
            if (ceStrength(this.ces[i2]) == 0) {
                i++;
            }
        }
        long j = 0;
        if (i > 0) {
            UTF16CollationIterator uTF16CollationIterator = new UTF16CollationIterator(this.baseData, false, charSequence, 0);
            int fetchCEs = uTF16CollationIterator.fetchCEs() - 1;
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            long j2 = 0;
            while (true) {
                if (i3 >= fetchCEs) {
                    break;
                }
                long ce = uTF16CollationIterator.getCE(i3);
                if ((ce >>> 32) != 0) {
                    i4++;
                    int i6 = (((int) ce) >> 14) & 3;
                    if (i4 < i) {
                        j2 |= ((long) i6) << ((i4 - 1) * 2);
                    } else if (i4 == i) {
                        i5 = i6;
                    } else if (i6 != i5) {
                        i5 = 1;
                        break;
                    }
                }
                i3++;
            }
            j = i4 >= i ? j2 | (((long) i5) << ((i - 1) * 2)) : j2;
        }
        for (int i7 = 0; i7 < this.cesLength; i7++) {
            long j3 = this.ces[i7] & -49153;
            int ceStrength = ceStrength(j3);
            if (ceStrength == 0) {
                j3 |= (3 & j) << 14;
                j >>>= 2;
            } else if (ceStrength == 2) {
                j3 |= 32768;
            }
            this.ces[i7] = j3;
        }
    }

    /* access modifiers changed from: package-private */
    @Override // ohos.global.icu.impl.coll.CollationRuleParser.Sink
    public void suppressContractions(UnicodeSet unicodeSet) {
        this.dataBuilder.suppressContractions(unicodeSet);
    }

    /* access modifiers changed from: package-private */
    @Override // ohos.global.icu.impl.coll.CollationRuleParser.Sink
    public void optimize(UnicodeSet unicodeSet) {
        this.optimizeSet.addAll(unicodeSet);
    }

    private int addWithClosure(CharSequence charSequence, CharSequence charSequence2, long[] jArr, int i, int i2) {
        int addOnlyClosure = addOnlyClosure(charSequence, charSequence2, jArr, i, addIfDifferent(charSequence, charSequence2, jArr, i, i2));
        addTailComposites(charSequence, charSequence2);
        return addOnlyClosure;
    }

    private int addOnlyClosure(CharSequence charSequence, CharSequence charSequence2, long[] jArr, int i, int i2) {
        if (charSequence.length() == 0) {
            CanonicalIterator canonicalIterator = new CanonicalIterator(charSequence2.toString());
            int i3 = i2;
            while (true) {
                String next = canonicalIterator.next();
                if (next == null) {
                    return i3;
                }
                if (!ignoreString(next) && !next.contentEquals(charSequence2)) {
                    i3 = addIfDifferent("", next, jArr, i, i3);
                }
            }
        } else {
            CanonicalIterator canonicalIterator2 = new CanonicalIterator(charSequence.toString());
            CanonicalIterator canonicalIterator3 = new CanonicalIterator(charSequence2.toString());
            while (true) {
                String next2 = canonicalIterator2.next();
                if (next2 == null) {
                    return i2;
                }
                if (!ignorePrefix(next2)) {
                    boolean contentEquals = next2.contentEquals(charSequence);
                    int i4 = i2;
                    while (true) {
                        String next3 = canonicalIterator3.next();
                        if (next3 == null) {
                            break;
                        } else if (!ignoreString(next3) && (!contentEquals || !next3.contentEquals(charSequence2))) {
                            i4 = addIfDifferent(next2, next3, jArr, i, i4);
                        }
                    }
                    canonicalIterator3.reset();
                    i2 = i4;
                }
            }
        }
    }

    private void addTailComposites(CharSequence charSequence, CharSequence charSequence2) {
        int cEs;
        int addIfDifferent;
        int length = charSequence2.length();
        while (length != 0) {
            int codePointBefore = Character.codePointBefore(charSequence2, length);
            if (this.nfd.getCombiningClass(codePointBefore) != 0) {
                length -= Character.charCount(codePointBefore);
            } else if (!Normalizer2Impl.Hangul.isJamoL(codePointBefore)) {
                UnicodeSet unicodeSet = new UnicodeSet();
                if (this.nfcImpl.getCanonStartSet(codePointBefore, unicodeSet)) {
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    long[] jArr = new long[31];
                    UnicodeSetIterator unicodeSetIterator = new UnicodeSetIterator(unicodeSet);
                    while (unicodeSetIterator.next()) {
                        int i = unicodeSetIterator.codepoint;
                        if (mergeCompositeIntoString(charSequence2, length, i, this.nfd.getDecomposition(i), sb, sb2) && (cEs = this.dataBuilder.getCEs(charSequence, sb, jArr, 0)) <= 31 && (addIfDifferent = addIfDifferent(charSequence, sb2, jArr, cEs, -1)) != -1) {
                            addOnlyClosure(charSequence, sb, jArr, cEs, addIfDifferent);
                        }
                    }
                    return;
                }
                return;
            } else {
                return;
            }
        }
    }

    private boolean mergeCompositeIntoString(CharSequence charSequence, int i, int i2, CharSequence charSequence2, StringBuilder sb, StringBuilder sb2) {
        int offsetByCodePoints = Character.offsetByCodePoints(charSequence2, 0, 1);
        if (offsetByCodePoints == charSequence2.length() || equalSubSequences(charSequence, i, charSequence2, offsetByCodePoints)) {
            return false;
        }
        sb.setLength(0);
        sb.append(charSequence, 0, i);
        sb2.setLength(0);
        sb2.append(charSequence, 0, i - offsetByCodePoints);
        sb2.appendCodePoint(i2);
        int i3 = 0;
        int i4 = 0;
        int i5 = offsetByCodePoints;
        int i6 = i;
        int i7 = -1;
        while (true) {
            if (i7 < 0) {
                if (i6 >= charSequence.length()) {
                    break;
                }
                i7 = Character.codePointAt(charSequence, i6);
                i3 = this.nfd.getCombiningClass(i7);
            }
            if (i5 >= charSequence2.length()) {
                break;
            }
            int codePointAt = Character.codePointAt(charSequence2, i5);
            int combiningClass = this.nfd.getCombiningClass(codePointAt);
            if (combiningClass == 0 || i3 < combiningClass) {
                return false;
            }
            if (combiningClass < i3) {
                sb.appendCodePoint(codePointAt);
                i5 += Character.charCount(codePointAt);
            } else if (codePointAt != i7) {
                return false;
            } else {
                sb.appendCodePoint(codePointAt);
                i5 += Character.charCount(codePointAt);
                i6 += Character.charCount(codePointAt);
                i7 = -1;
            }
            i4 = combiningClass;
        }
        if (i7 >= 0) {
            if (i3 < i4) {
                return false;
            }
            sb.append(charSequence, i6, charSequence.length());
            sb2.append(charSequence, i6, charSequence.length());
        } else if (i5 < charSequence2.length()) {
            sb.append(charSequence2, i5, charSequence2.length());
        }
        return true;
    }

    private boolean equalSubSequences(CharSequence charSequence, int i, CharSequence charSequence2, int i2) {
        int length = charSequence.length();
        if (length - i != charSequence2.length() - i2) {
            return false;
        }
        while (i < length) {
            int i3 = i + 1;
            int i4 = i2 + 1;
            if (charSequence.charAt(i) != charSequence2.charAt(i2)) {
                return false;
            }
            i = i3;
            i2 = i4;
        }
        return true;
    }

    private boolean ignorePrefix(CharSequence charSequence) {
        return !isFCD(charSequence);
    }

    private boolean ignoreString(CharSequence charSequence) {
        return !isFCD(charSequence) || Normalizer2Impl.Hangul.isHangul(charSequence.charAt(0));
    }

    private boolean isFCD(CharSequence charSequence) {
        return this.fcd.isNormalized(charSequence);
    }

    static {
        COMPOSITES.remove(Normalizer2Impl.Hangul.HANGUL_BASE, Normalizer2Impl.Hangul.HANGUL_END);
    }

    private void closeOverComposites() {
        UnicodeSetIterator unicodeSetIterator = new UnicodeSetIterator(COMPOSITES);
        while (unicodeSetIterator.next()) {
            this.cesLength = this.dataBuilder.getCEs(this.nfd.getDecomposition(unicodeSetIterator.codepoint), this.ces, 0);
            if (this.cesLength <= 31) {
                addIfDifferent("", unicodeSetIterator.getString(), this.ces, this.cesLength, -1);
            }
        }
    }

    private int addIfDifferent(CharSequence charSequence, CharSequence charSequence2, long[] jArr, int i, int i2) {
        long[] jArr2 = new long[31];
        if (!sameCEs(jArr, i, jArr2, this.dataBuilder.getCEs(charSequence, charSequence2, jArr2, 0))) {
            if (i2 == -1) {
                i2 = this.dataBuilder.encodeCEs(jArr, i);
            }
            this.dataBuilder.addCE32(charSequence, charSequence2, i2);
        }
        return i2;
    }

    private static boolean sameCEs(long[] jArr, int i, long[] jArr2, int i2) {
        if (i != i2) {
            return false;
        }
        for (int i3 = 0; i3 < i; i3++) {
            if (jArr[i3] != jArr2[i3]) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:56:0x0143  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0157  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x015a A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void makeTailoredCEs() {
        /*
        // Method dump skipped, instructions count: 482
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.coll.CollationBuilder.makeTailoredCEs():void");
    }

    private static int countTailoredNodes(long[] jArr, int i, int i2) {
        int i3 = 0;
        while (i != 0) {
            long j = jArr[i];
            if (strengthFromNode(j) < i2) {
                break;
            }
            if (strengthFromNode(j) == i2) {
                if (!isTailoredNode(j)) {
                    break;
                }
                i3++;
            }
            i = nextIndexFromNode(j);
        }
        return i3;
    }

    /* access modifiers changed from: private */
    public static final class CEFinalizer implements CollationDataBuilder.CEModifier {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private long[] finalCEs;

        CEFinalizer(long[] jArr) {
            this.finalCEs = jArr;
        }

        @Override // ohos.global.icu.impl.coll.CollationDataBuilder.CEModifier
        public long modifyCE32(int i) {
            if (!CollationBuilder.isTempCE32(i)) {
                return Collation.NO_CE;
            }
            return ((long) ((i & 192) << 8)) | this.finalCEs[CollationBuilder.indexFromTempCE32(i)];
        }

        @Override // ohos.global.icu.impl.coll.CollationDataBuilder.CEModifier
        public long modifyCE(long j) {
            if (!CollationBuilder.isTempCE(j)) {
                return Collation.NO_CE;
            }
            return (j & 49152) | this.finalCEs[CollationBuilder.indexFromTempCE(j)];
        }
    }

    private void finalizeCEs() {
        CollationDataBuilder collationDataBuilder = new CollationDataBuilder();
        collationDataBuilder.initForTailoring(this.baseData);
        collationDataBuilder.copyFrom(this.dataBuilder, new CEFinalizer(this.nodes.getBuffer()));
        this.dataBuilder = collationDataBuilder;
    }

    private static int ceStrength(long j) {
        if (isTempCE(j)) {
            return strengthFromTempCE(j);
        }
        if ((-72057594037927936L & j) != 0) {
            return 0;
        }
        if ((((int) j) & -16777216) != 0) {
            return 1;
        }
        return j != 0 ? 2 : 15;
    }

    private static long changeNodePreviousIndex(long j, int i) {
        return (j & -281474708275201L) | nodeFromPreviousIndex(i);
    }

    private static long changeNodeNextIndex(long j, int i) {
        return (j & -268435201) | nodeFromNextIndex(i);
    }
}
