package ohos.global.icu.impl.locale;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.TreeMap;
import ohos.com.sun.org.apache.xml.internal.utils.LocaleUtility;
import ohos.global.icu.impl.ICUData;
import ohos.global.icu.impl.ICUResourceBundle;
import ohos.global.icu.impl.UResource;
import ohos.global.icu.text.Bidi;
import ohos.global.icu.util.BytesTrie;
import ohos.global.icu.util.ULocale;

public final class XLikelySubtags {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final boolean DEBUG_OUTPUT = false;
    public static final XLikelySubtags INSTANCE = new XLikelySubtags(Data.load());
    private static final String PSEUDO_ACCENTS_PREFIX = "'";
    private static final String PSEUDO_BIDI_PREFIX = "+";
    private static final String PSEUDO_CRACKED_PREFIX = ",";
    public static final int SKIP_SCRIPT = 1;
    private final int defaultLsrIndex;
    private final Map<String, String> languageAliases;
    private final LSR[] lsrs;
    private final Map<String, String> regionAliases;
    private final BytesTrie trie;
    private final long[] trieFirstLetterStates = new long[26];
    private final long trieUndState;
    private final long trieUndZzzzState;

    public static final class Data {
        public final Map<String, String> languageAliases;
        public final LSR[] lsrs;
        public final Map<String, String> regionAliases;
        public final byte[] trie;

        public int hashCode() {
            return 1;
        }

        public Data(Map<String, String> map, Map<String, String> map2, byte[] bArr, LSR[] lsrArr) {
            this.languageAliases = map;
            this.regionAliases = map2;
            this.trie = bArr;
            this.lsrs = lsrArr;
        }

        private static UResource.Value getValue(UResource.Table table, String str, UResource.Value value) {
            if (table.findValue(str, value)) {
                return value;
            }
            throw new MissingResourceException("langInfo.res missing data", "", "likely/" + str);
        }

        public static Data load() throws MissingResourceException {
            Map map;
            Map map2;
            UResource.Value valueWithFallback = ICUResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, "langInfo", ICUResourceBundle.ICU_DATA_CLASS_LOADER, ICUResourceBundle.OpenType.DIRECT).getValueWithFallback("likely");
            UResource.Table table = valueWithFallback.getTable();
            if (table.findValue("languageAliases", valueWithFallback)) {
                String[] stringArray = valueWithFallback.getStringArray();
                map = new HashMap(stringArray.length / 2);
                for (int i = 0; i < stringArray.length; i += 2) {
                    map.put(stringArray[i], stringArray[i + 1]);
                }
            } else {
                map = Collections.emptyMap();
            }
            if (table.findValue("regionAliases", valueWithFallback)) {
                String[] stringArray2 = valueWithFallback.getStringArray();
                map2 = new HashMap(stringArray2.length / 2);
                for (int i2 = 0; i2 < stringArray2.length; i2 += 2) {
                    map2.put(stringArray2[i2], stringArray2[i2 + 1]);
                }
            } else {
                map2 = Collections.emptyMap();
            }
            ByteBuffer binary = getValue(table, "trie", valueWithFallback).getBinary();
            byte[] bArr = new byte[binary.remaining()];
            binary.get(bArr);
            String[] stringArray3 = getValue(table, "lsrs", valueWithFallback).getStringArray();
            LSR[] lsrArr = new LSR[(stringArray3.length / 3)];
            int i3 = 0;
            int i4 = 0;
            while (i3 < stringArray3.length) {
                lsrArr[i4] = new LSR(stringArray3[i3], stringArray3[i3 + 1], stringArray3[i3 + 2], 0);
                i3 += 3;
                i4++;
            }
            return new Data(map, map2, bArr, lsrArr);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !getClass().equals(obj.getClass())) {
                return false;
            }
            Data data = (Data) obj;
            return this.languageAliases.equals(data.languageAliases) && this.regionAliases.equals(data.regionAliases) && Arrays.equals(this.trie, data.trie) && Arrays.equals(this.lsrs, data.lsrs);
        }
    }

    private XLikelySubtags(Data data) {
        this.languageAliases = data.languageAliases;
        this.regionAliases = data.regionAliases;
        this.trie = new BytesTrie(data.trie, 0);
        this.lsrs = data.lsrs;
        this.trie.next(42);
        this.trieUndState = this.trie.getState64();
        this.trie.next(42);
        this.trieUndZzzzState = this.trie.getState64();
        this.trie.next(42);
        this.defaultLsrIndex = this.trie.getValue();
        this.trie.reset();
        for (char c = 'a'; c <= 'z'; c = (char) (c + 1)) {
            if (this.trie.next(c) == BytesTrie.Result.NO_VALUE) {
                this.trieFirstLetterStates[c - 'a'] = this.trie.getState64();
            }
            this.trie.reset();
        }
    }

    public ULocale canonicalize(ULocale uLocale) {
        String language = uLocale.getLanguage();
        String str = this.languageAliases.get(language);
        String country = uLocale.getCountry();
        String str2 = this.regionAliases.get(country);
        if (str == null && str2 == null) {
            return uLocale;
        }
        if (str != null) {
            language = str;
        }
        String script = uLocale.getScript();
        if (str2 == null) {
            str2 = country;
        }
        return new ULocale(language, script, str2);
    }

    private static String getCanonical(Map<String, String> map, String str) {
        String str2 = map.get(str);
        return str2 == null ? str : str2;
    }

    public LSR makeMaximizedLsrFrom(ULocale uLocale) {
        if (uLocale.getName().startsWith("@x=")) {
            return new LSR(uLocale.toLanguageTag(), "", "", 7);
        }
        return makeMaximizedLsr(uLocale.getLanguage(), uLocale.getScript(), uLocale.getCountry(), uLocale.getVariant());
    }

    public LSR makeMaximizedLsrFrom(Locale locale) {
        String languageTag = locale.toLanguageTag();
        if (languageTag.startsWith("x-")) {
            return new LSR(languageTag, "", "", 7);
        }
        return makeMaximizedLsr(locale.getLanguage(), locale.getScript(), locale.getCountry(), locale.getVariant());
    }

    private LSR makeMaximizedLsr(String str, String str2, String str3, String str4) {
        int i = 7;
        if (str3.length() == 2 && str3.charAt(0) == 'X') {
            switch (str3.charAt(1)) {
                case 'A':
                    return new LSR(PSEUDO_ACCENTS_PREFIX + str, PSEUDO_ACCENTS_PREFIX + str2, str3, 7);
                case 'B':
                    return new LSR(PSEUDO_BIDI_PREFIX + str, PSEUDO_BIDI_PREFIX + str2, str3, 7);
                case 'C':
                    return new LSR(PSEUDO_CRACKED_PREFIX + str, PSEUDO_CRACKED_PREFIX + str2, str3, 7);
            }
        }
        if (str4.startsWith("PS")) {
            if (str3.isEmpty()) {
                i = 6;
            }
            char c = 65535;
            int hashCode = str4.hashCode();
            if (hashCode != -1925944433) {
                if (hashCode != 264103053) {
                    if (hashCode == 426453367 && str4.equals("PSCRACK")) {
                        c = 2;
                    }
                } else if (str4.equals("PSACCENT")) {
                    c = 0;
                }
            } else if (str4.equals("PSBIDI")) {
                c = 1;
            }
            if (c == 0) {
                String str5 = PSEUDO_ACCENTS_PREFIX + str;
                String str6 = PSEUDO_ACCENTS_PREFIX + str2;
                if (str3.isEmpty()) {
                    str3 = "XA";
                }
                return new LSR(str5, str6, str3, i);
            } else if (c == 1) {
                String str7 = PSEUDO_BIDI_PREFIX + str;
                String str8 = PSEUDO_BIDI_PREFIX + str2;
                if (str3.isEmpty()) {
                    str3 = "XB";
                }
                return new LSR(str7, str8, str3, i);
            } else if (c == 2) {
                String str9 = PSEUDO_CRACKED_PREFIX + str;
                String str10 = PSEUDO_CRACKED_PREFIX + str2;
                if (str3.isEmpty()) {
                    str3 = "XC";
                }
                return new LSR(str9, str10, str3, i);
            }
        }
        return maximize(getCanonical(this.languageAliases, str), str2, getCanonical(this.regionAliases, str3));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00c4, code lost:
        if (r6.isEmpty() == false) goto L_0x00c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00d3, code lost:
        if (r6.isEmpty() == false) goto L_0x00c6;
     */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0080  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0094  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00c0  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00c9  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00f3  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f6 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00f7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private ohos.global.icu.impl.locale.LSR maximize(java.lang.String r16, java.lang.String r17, java.lang.String r18) {
        /*
        // Method dump skipped, instructions count: 271
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.locale.XLikelySubtags.maximize(java.lang.String, java.lang.String, java.lang.String):ohos.global.icu.impl.locale.LSR");
    }

    /* access modifiers changed from: package-private */
    public int compareLikely(LSR lsr, LSR lsr2, int i) {
        int i2;
        int i3;
        if (!lsr.language.equals(lsr2.language)) {
            return -4;
        }
        if (!lsr.script.equals(lsr2.script)) {
            if (i < 0 || (i & 2) != 0) {
                i3 = getLikelyIndex(lsr.language, "");
                i = i3 << 2;
            } else {
                i3 = i >> 2;
            }
            return lsr.script.equals(this.lsrs[i3].script) ? i | 1 : i & -2;
        } else if (lsr.region.equals(lsr2.region)) {
            return i & -2;
        } else {
            if (i < 0 || (i & 2) == 0) {
                i2 = getLikelyIndex(lsr.language, lsr.region);
                i = (i2 << 2) | 2;
            } else {
                i2 = i >> 2;
            }
            return lsr.region.equals(this.lsrs[i2].region) ? i | 1 : i & -2;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x004e  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0056  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x007c  */
    /* JADX WARNING: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getLikelyIndex(java.lang.String r10, java.lang.String r11) {
        /*
        // Method dump skipped, instructions count: 129
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.locale.XLikelySubtags.getLikelyIndex(java.lang.String, java.lang.String):int");
    }

    private static final int trieNext(BytesTrie bytesTrie, String str, int i) {
        BytesTrie.Result result;
        if (!str.isEmpty()) {
            int length = str.length() - 1;
            while (true) {
                char charAt = str.charAt(i);
                if (i >= length) {
                    result = bytesTrie.next(charAt | 128);
                    break;
                } else if (!bytesTrie.next(charAt).hasNext()) {
                    return -1;
                } else {
                    i++;
                }
            }
        } else {
            result = bytesTrie.next(42);
        }
        int i2 = AnonymousClass1.$SwitchMap$ohos$global$icu$util$BytesTrie$Result[result.ordinal()];
        if (i2 == 1) {
            return -1;
        }
        if (i2 == 2) {
            return 0;
        }
        if (i2 == 3) {
            return 1;
        }
        if (i2 != 4) {
            return -1;
        }
        return bytesTrie.getValue();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.global.icu.impl.locale.XLikelySubtags$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$util$BytesTrie$Result = new int[BytesTrie.Result.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                ohos.global.icu.util.BytesTrie$Result[] r0 = ohos.global.icu.util.BytesTrie.Result.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.global.icu.impl.locale.XLikelySubtags.AnonymousClass1.$SwitchMap$ohos$global$icu$util$BytesTrie$Result = r0
                int[] r0 = ohos.global.icu.impl.locale.XLikelySubtags.AnonymousClass1.$SwitchMap$ohos$global$icu$util$BytesTrie$Result     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.global.icu.util.BytesTrie$Result r1 = ohos.global.icu.util.BytesTrie.Result.NO_MATCH     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.global.icu.impl.locale.XLikelySubtags.AnonymousClass1.$SwitchMap$ohos$global$icu$util$BytesTrie$Result     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.global.icu.util.BytesTrie$Result r1 = ohos.global.icu.util.BytesTrie.Result.NO_VALUE     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.global.icu.impl.locale.XLikelySubtags.AnonymousClass1.$SwitchMap$ohos$global$icu$util$BytesTrie$Result     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.global.icu.util.BytesTrie$Result r1 = ohos.global.icu.util.BytesTrie.Result.INTERMEDIATE_VALUE     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.global.icu.impl.locale.XLikelySubtags.AnonymousClass1.$SwitchMap$ohos$global$icu$util$BytesTrie$Result     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.global.icu.util.BytesTrie$Result r1 = ohos.global.icu.util.BytesTrie.Result.FINAL_VALUE     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.locale.XLikelySubtags.AnonymousClass1.<clinit>():void");
        }
    }

    /* access modifiers changed from: package-private */
    public LSR minimizeSubtags(String str, String str2, String str3, ULocale.Minimize minimize) {
        boolean z;
        LSR maximize = maximize(str, str2, str3);
        BytesTrie bytesTrie = new BytesTrie(this.trie);
        int trieNext = trieNext(bytesTrie, maximize.language, 0);
        if (trieNext == 0 && (trieNext = trieNext(bytesTrie, "", 0)) == 0) {
            trieNext = trieNext(bytesTrie, "", 0);
        }
        LSR lsr = this.lsrs[trieNext];
        if (!maximize.script.equals(lsr.script)) {
            z = false;
        } else if (maximize.region.equals(lsr.region)) {
            return new LSR(maximize.language, "", "", 0);
        } else {
            if (minimize == ULocale.Minimize.FAVOR_REGION) {
                return new LSR(maximize.language, "", maximize.region, 0);
            }
            z = true;
        }
        if (maximize(str, str2, "").equals(maximize)) {
            return new LSR(maximize.language, maximize.script, "", 0);
        }
        return z ? new LSR(maximize.language, "", maximize.region, 0) : maximize;
    }

    private Map<String, LSR> getTable() {
        TreeMap treeMap = new TreeMap();
        StringBuilder sb = new StringBuilder();
        Iterator<BytesTrie.Entry> it = this.trie.iterator();
        while (it.hasNext()) {
            BytesTrie.Entry next = it.next();
            int i = 0;
            sb.setLength(0);
            int bytesLength = next.bytesLength();
            while (i < bytesLength) {
                int i2 = i + 1;
                byte byteAt = next.byteAt(i);
                if (byteAt == 42) {
                    sb.append("*-");
                } else if (byteAt >= 0) {
                    sb.append((char) byteAt);
                } else {
                    sb.append((char) (byteAt & Bidi.LEVEL_DEFAULT_RTL));
                    sb.append(LocaleUtility.IETF_SEPARATOR);
                }
                i = i2;
            }
            sb.setLength(sb.length() - 1);
            treeMap.put(sb.toString(), this.lsrs[next.value]);
        }
        return treeMap;
    }

    public String toString() {
        return getTable().toString();
    }
}
