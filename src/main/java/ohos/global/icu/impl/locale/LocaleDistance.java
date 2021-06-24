package ohos.global.icu.impl.locale;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.TreeMap;
import ohos.com.sun.org.apache.xalan.internal.templates.Constants;
import ohos.com.sun.org.apache.xml.internal.utils.LocaleUtility;
import ohos.global.icu.impl.ICUData;
import ohos.global.icu.impl.ICUResourceBundle;
import ohos.global.icu.impl.UResource;
import ohos.global.icu.text.Bidi;
import ohos.global.icu.util.BytesTrie;
import ohos.global.icu.util.LocaleMatcher;
import ohos.global.icu.util.ULocale;

public class LocaleDistance {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ABOVE_THRESHOLD = 100;
    private static final boolean DEBUG_OUTPUT = false;
    private static final int DISTANCE_FRACTION_MASK = 7;
    private static final int DISTANCE_INT_SHIFT = 7;
    private static final int DISTANCE_IS_FINAL = 256;
    private static final int DISTANCE_IS_FINAL_OR_SKIP_SCRIPT = 384;
    private static final int DISTANCE_MASK = 1023;
    private static final int DISTANCE_SHIFT = 3;
    public static final int DISTANCE_SKIP_SCRIPT = 128;
    public static final int END_OF_SUBTAG = 128;
    private static final int INDEX_NEG_1 = -1024;
    private static final int INDEX_SHIFT = 10;
    public static final LocaleDistance INSTANCE = new LocaleDistance(Data.load());
    public static final int IX_DEF_LANG_DISTANCE = 0;
    public static final int IX_DEF_REGION_DISTANCE = 2;
    public static final int IX_DEF_SCRIPT_DISTANCE = 1;
    public static final int IX_LIMIT = 4;
    public static final int IX_MIN_REGION_DISTANCE = 3;
    private final int defaultDemotionPerDesiredLocale = getDistanceFloor(getBestIndexAndDistance(new LSR("en", "Latn", "US", 7), new LSR[]{new LSR("en", "Latn", "GB", 7)}, 1, shiftDistance(50), LocaleMatcher.FavorSubtag.LANGUAGE, LocaleMatcher.Direction.WITH_ONE_WAY));
    private final int defaultLanguageDistance;
    private final int defaultRegionDistance;
    private final int defaultScriptDistance;
    private final int minRegionDistance;
    private final Set<LSR> paradigmLSRs;
    private final String[] partitionArrays;
    private final byte[] regionToPartitionsIndex;
    private final BytesTrie trie;

    private static final int getDistanceFloor(int i) {
        return (i & 1023) >> 3;
    }

    public static final int getIndex(int i) {
        return i >> 10;
    }

    public static final int getShiftedDistance(int i) {
        return i & 1023;
    }

    public static final int shiftDistance(int i) {
        return i << 3;
    }

    public static final double getDistanceDouble(int i) {
        return ((double) getShiftedDistance(i)) / 8.0d;
    }

    public static final class Data {
        public int[] distances;
        public Set<LSR> paradigmLSRs;
        public String[] partitionArrays;
        public byte[] regionToPartitionsIndex;
        public byte[] trie;

        public int hashCode() {
            return 1;
        }

        public Data(byte[] bArr, byte[] bArr2, String[] strArr, Set<LSR> set, int[] iArr) {
            this.trie = bArr;
            this.regionToPartitionsIndex = bArr2;
            this.partitionArrays = strArr;
            this.paradigmLSRs = set;
            this.distances = iArr;
        }

        private static UResource.Value getValue(UResource.Table table, String str, UResource.Value value) {
            if (table.findValue(str, value)) {
                return value;
            }
            throw new MissingResourceException("langInfo.res missing data", "", "match/" + str);
        }

        public static Data load() throws MissingResourceException {
            LinkedHashSet linkedHashSet;
            UResource.Value valueWithFallback = ICUResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, "langInfo", ICUResourceBundle.ICU_DATA_CLASS_LOADER, ICUResourceBundle.OpenType.DIRECT).getValueWithFallback(Constants.ATTRNAME_MATCH);
            UResource.Table table = valueWithFallback.getTable();
            ByteBuffer binary = getValue(table, "trie", valueWithFallback).getBinary();
            byte[] bArr = new byte[binary.remaining()];
            binary.get(bArr);
            ByteBuffer binary2 = getValue(table, "regionToPartitions", valueWithFallback).getBinary();
            byte[] bArr2 = new byte[binary2.remaining()];
            binary2.get(bArr2);
            if (bArr2.length >= 1677) {
                String[] stringArray = getValue(table, "partitions", valueWithFallback).getStringArray();
                if (table.findValue("paradigms", valueWithFallback)) {
                    String[] stringArray2 = valueWithFallback.getStringArray();
                    LinkedHashSet linkedHashSet2 = new LinkedHashSet(stringArray2.length / 3);
                    for (int i = 0; i < stringArray2.length; i += 3) {
                        linkedHashSet2.add(new LSR(stringArray2[i], stringArray2[i + 1], stringArray2[i + 2], 0));
                    }
                    linkedHashSet = linkedHashSet2;
                } else {
                    linkedHashSet = Collections.emptySet();
                }
                int[] intVector = getValue(table, "distances", valueWithFallback).getIntVector();
                if (intVector.length >= 4) {
                    return new Data(bArr, bArr2, stringArray, linkedHashSet, intVector);
                }
                throw new MissingResourceException("langInfo.res intvector too short", "", "match/distances");
            }
            throw new MissingResourceException("langInfo.res binary data too short", "", "match/regionToPartitions");
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !getClass().equals(obj.getClass())) {
                return false;
            }
            Data data = (Data) obj;
            return Arrays.equals(this.trie, data.trie) && Arrays.equals(this.regionToPartitionsIndex, data.regionToPartitionsIndex) && Arrays.equals(this.partitionArrays, data.partitionArrays) && this.paradigmLSRs.equals(data.paradigmLSRs) && Arrays.equals(this.distances, data.distances);
        }
    }

    private LocaleDistance(Data data) {
        this.trie = new BytesTrie(data.trie, 0);
        this.regionToPartitionsIndex = data.regionToPartitionsIndex;
        this.partitionArrays = data.partitionArrays;
        this.paradigmLSRs = data.paradigmLSRs;
        this.defaultLanguageDistance = data.distances[0];
        this.defaultScriptDistance = data.distances[1];
        this.defaultRegionDistance = data.distances[2];
        this.minRegionDistance = data.distances[3];
    }

    public int testOnlyDistance(ULocale uLocale, ULocale uLocale2, int i, LocaleMatcher.FavorSubtag favorSubtag) {
        LSR makeMaximizedLsrFrom = XLikelySubtags.INSTANCE.makeMaximizedLsrFrom(uLocale2);
        return getDistanceFloor(getBestIndexAndDistance(XLikelySubtags.INSTANCE.makeMaximizedLsrFrom(uLocale), new LSR[]{makeMaximizedLsrFrom}, 1, shiftDistance(i), favorSubtag, LocaleMatcher.Direction.WITH_ONE_WAY));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0135, code lost:
        if (isMatch(r15, r24, r11, r28) == false) goto L_0x015b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getBestIndexAndDistance(ohos.global.icu.impl.locale.LSR r24, ohos.global.icu.impl.locale.LSR[] r25, int r26, int r27, ohos.global.icu.util.LocaleMatcher.FavorSubtag r28, ohos.global.icu.util.LocaleMatcher.Direction r29) {
        /*
        // Method dump skipped, instructions count: 379
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.locale.LocaleDistance.getBestIndexAndDistance(ohos.global.icu.impl.locale.LSR, ohos.global.icu.impl.locale.LSR[], int, int, ohos.global.icu.util.LocaleMatcher$FavorSubtag, ohos.global.icu.util.LocaleMatcher$Direction):int");
    }

    private boolean isMatch(LSR lsr, LSR lsr2, int i, LocaleMatcher.FavorSubtag favorSubtag) {
        return getBestIndexAndDistance(lsr, new LSR[]{lsr2}, 1, i, favorSubtag, null) >= 0;
    }

    private static final int getDesSuppScriptDistance(BytesTrie bytesTrie, long j, String str, String str2) {
        int i = 0;
        int trieNext = trieNext(bytesTrie, str, false);
        if (trieNext >= 0) {
            trieNext = trieNext(bytesTrie, str2, true);
        }
        if (trieNext >= 0) {
            return trieNext;
        }
        BytesTrie.Result next = bytesTrie.resetToState64(j).next(42);
        if (!str.equals(str2)) {
            i = bytesTrie.getValue();
        }
        return next == BytesTrie.Result.FINAL_VALUE ? i | 256 : i;
    }

    private static final int getRegionPartitionsDistance(BytesTrie bytesTrie, long j, String str, String str2, int i) {
        int i2;
        int length = str.length();
        int length2 = str2.length();
        if (length != 1 || length2 != 1) {
            int i3 = 0;
            boolean z = false;
            int i4 = 0;
            while (true) {
                int i5 = i3 + 1;
                if (bytesTrie.next(str.charAt(i3) | 128).hasNext()) {
                    long state64 = length2 > 1 ? bytesTrie.getState64() : 0;
                    int i6 = 0;
                    while (true) {
                        int i7 = i6 + 1;
                        if (bytesTrie.next(str2.charAt(i6) | 128).hasValue()) {
                            i2 = bytesTrie.getValue();
                        } else if (z) {
                            i2 = 0;
                        } else {
                            i2 = getFallbackRegionDistance(bytesTrie, j);
                            z = true;
                        }
                        if (i2 <= i) {
                            if (i4 < i2) {
                                i4 = i2;
                            }
                            if (i7 >= length2) {
                                break;
                            }
                            bytesTrie.resetToState64(state64);
                            i6 = i7;
                        } else {
                            return i2;
                        }
                    }
                } else if (!z) {
                    int fallbackRegionDistance = getFallbackRegionDistance(bytesTrie, j);
                    if (fallbackRegionDistance > i) {
                        return fallbackRegionDistance;
                    }
                    if (i4 < fallbackRegionDistance) {
                        i4 = fallbackRegionDistance;
                    }
                    z = true;
                }
                if (i5 >= length) {
                    return i4;
                }
                bytesTrie.resetToState64(j);
                i3 = i5;
            }
        } else if (!bytesTrie.next(str.charAt(0) | 128).hasNext() || !bytesTrie.next(str2.charAt(0) | 128).hasValue()) {
            return getFallbackRegionDistance(bytesTrie, j);
        } else {
            return bytesTrie.getValue();
        }
    }

    private static final int getFallbackRegionDistance(BytesTrie bytesTrie, long j) {
        bytesTrie.resetToState64(j).next(42);
        return bytesTrie.getValue();
    }

    private static final int trieNext(BytesTrie bytesTrie, String str, boolean z) {
        if (str.isEmpty()) {
            return -1;
        }
        int length = str.length() - 1;
        int i = 0;
        while (true) {
            char charAt = str.charAt(i);
            if (i >= length) {
                BytesTrie.Result next = bytesTrie.next(charAt | 128);
                if (z) {
                    if (next.hasValue()) {
                        int value = bytesTrie.getValue();
                        return next == BytesTrie.Result.FINAL_VALUE ? value | 256 : value;
                    }
                } else if (next.hasNext()) {
                    return 0;
                }
                return -1;
            } else if (!bytesTrie.next(charAt).hasNext()) {
                return -1;
            } else {
                i++;
            }
        }
    }

    public String toString() {
        return testOnlyGetDistanceTable().toString();
    }

    private String partitionsForRegion(LSR lsr) {
        return this.partitionArrays[this.regionToPartitionsIndex[lsr.regionIndex]];
    }

    public boolean isParadigmLSR(LSR lsr) {
        for (LSR lsr2 : this.paradigmLSRs) {
            if (lsr.isEquivalentTo(lsr2)) {
                return true;
            }
        }
        return false;
    }

    public int getDefaultScriptDistance() {
        return this.defaultScriptDistance;
    }

    /* access modifiers changed from: package-private */
    public int getDefaultRegionDistance() {
        return this.defaultRegionDistance;
    }

    public int getDefaultDemotionPerDesiredLocale() {
        return this.defaultDemotionPerDesiredLocale;
    }

    public Map<String, Integer> testOnlyGetDistanceTable() {
        TreeMap treeMap = new TreeMap();
        StringBuilder sb = new StringBuilder();
        Iterator<BytesTrie.Entry> it = this.trie.iterator();
        while (it.hasNext()) {
            BytesTrie.Entry next = it.next();
            sb.setLength(0);
            int bytesLength = next.bytesLength();
            for (int i = 0; i < bytesLength; i++) {
                byte byteAt = next.byteAt(i);
                if (byteAt == 42) {
                    sb.append("*-*-");
                } else if (byteAt >= 0) {
                    sb.append((char) byteAt);
                } else {
                    sb.append((char) (byteAt & Bidi.LEVEL_DEFAULT_RTL));
                    sb.append(LocaleUtility.IETF_SEPARATOR);
                }
            }
            sb.setLength(sb.length() - 1);
            treeMap.put(sb.toString(), Integer.valueOf(next.value));
        }
        return treeMap;
    }

    public void testOnlyPrintDistanceTable() {
        String str;
        for (Map.Entry<String, Integer> entry : testOnlyGetDistanceTable().entrySet()) {
            int intValue = entry.getValue().intValue();
            if ((intValue & 128) != 0) {
                intValue &= -129;
                str = " skip script";
            } else {
                str = "";
            }
            PrintStream printStream = System.out;
            printStream.println(entry.getKey() + '=' + intValue + str);
        }
    }
}
