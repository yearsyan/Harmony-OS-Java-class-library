package ohos.global.icu.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.concurrent.ConcurrentHashMap;
import ohos.global.icu.impl.TextTrieMap;
import ohos.global.icu.text.LocaleDisplayNames;
import ohos.global.icu.text.TimeZoneFormat;
import ohos.global.icu.text.TimeZoneNames;
import ohos.global.icu.util.Freezable;
import ohos.global.icu.util.Output;
import ohos.global.icu.util.TimeZone;
import ohos.global.icu.util.ULocale;

public class TimeZoneGenericNames implements Serializable, Freezable<TimeZoneGenericNames> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long DST_CHECK_RANGE = 15897600000L;
    private static Cache GENERIC_NAMES_CACHE = new Cache(null);
    private static final TimeZoneNames.NameType[] GENERIC_NON_LOCATION_TYPES = {TimeZoneNames.NameType.LONG_GENERIC, TimeZoneNames.NameType.SHORT_GENERIC};
    private static final long serialVersionUID = 2729910342063468417L;
    private volatile transient boolean _frozen;
    private transient ConcurrentHashMap<String, String> _genericLocationNamesMap;
    private transient ConcurrentHashMap<String, String> _genericPartialLocationNamesMap;
    private transient TextTrieMap<NameInfo> _gnamesTrie;
    private transient boolean _gnamesTrieFullyLoaded;
    private final ULocale _locale;
    private transient WeakReference<LocaleDisplayNames> _localeDisplayNamesRef;
    private transient MessageFormat[] _patternFormatters;
    private transient String _region;
    private TimeZoneNames _tznames;

    /* synthetic */ TimeZoneGenericNames(ULocale uLocale, AnonymousClass1 r2) {
        this(uLocale);
    }

    public enum GenericNameType {
        LOCATION("LONG", "SHORT"),
        LONG(new String[0]),
        SHORT(new String[0]);
        
        String[] _fallbackTypeOf;

        private GenericNameType(String... strArr) {
            this._fallbackTypeOf = strArr;
        }

        public boolean isFallbackTypeOf(GenericNameType genericNameType) {
            String genericNameType2 = genericNameType.toString();
            for (String str : this._fallbackTypeOf) {
                if (str.equals(genericNameType2)) {
                    return true;
                }
            }
            return false;
        }
    }

    public enum Pattern {
        REGION_FORMAT("regionFormat", "({0})"),
        FALLBACK_FORMAT("fallbackFormat", "{1} ({0})");
        
        String _defaultVal;
        String _key;

        private Pattern(String str, String str2) {
            this._key = str;
            this._defaultVal = str2;
        }

        /* access modifiers changed from: package-private */
        public String key() {
            return this._key;
        }

        /* access modifiers changed from: package-private */
        public String defaultValue() {
            return this._defaultVal;
        }
    }

    public TimeZoneGenericNames(ULocale uLocale, TimeZoneNames timeZoneNames) {
        this._locale = uLocale;
        this._tznames = timeZoneNames;
        init();
    }

    private void init() {
        if (this._tznames == null) {
            this._tznames = TimeZoneNames.getInstance(this._locale);
        }
        this._genericLocationNamesMap = new ConcurrentHashMap<>();
        this._genericPartialLocationNamesMap = new ConcurrentHashMap<>();
        this._gnamesTrie = new TextTrieMap<>(true);
        this._gnamesTrieFullyLoaded = false;
        String canonicalCLDRID = ZoneMeta.getCanonicalCLDRID(TimeZone.getDefault());
        if (canonicalCLDRID != null) {
            loadStrings(canonicalCLDRID);
        }
    }

    private TimeZoneGenericNames(ULocale uLocale) {
        this(uLocale, (TimeZoneNames) null);
    }

    public static TimeZoneGenericNames getInstance(ULocale uLocale) {
        return (TimeZoneGenericNames) GENERIC_NAMES_CACHE.getInstance(uLocale.getBaseName(), uLocale);
    }

    public String getDisplayName(TimeZone timeZone, GenericNameType genericNameType, long j) {
        String canonicalCLDRID;
        int i = AnonymousClass1.$SwitchMap$ohos$global$icu$impl$TimeZoneGenericNames$GenericNameType[genericNameType.ordinal()];
        if (i == 1) {
            String canonicalCLDRID2 = ZoneMeta.getCanonicalCLDRID(timeZone);
            if (canonicalCLDRID2 != null) {
                return getGenericLocationName(canonicalCLDRID2);
            }
        } else if (i == 2 || i == 3) {
            String formatGenericNonLocationName = formatGenericNonLocationName(timeZone, genericNameType, j);
            if (formatGenericNonLocationName != null || (canonicalCLDRID = ZoneMeta.getCanonicalCLDRID(timeZone)) == null) {
                return formatGenericNonLocationName;
            }
            return getGenericLocationName(canonicalCLDRID);
        }
        return null;
    }

    public String getGenericLocationName(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        String str2 = this._genericLocationNamesMap.get(str);
        if (str2 == null) {
            Output output = new Output();
            String canonicalCountry = ZoneMeta.getCanonicalCountry(str, output);
            if (canonicalCountry != null) {
                if (output.value.booleanValue()) {
                    String regionDisplayName = getLocaleDisplayNames().regionDisplayName(canonicalCountry);
                    str2 = formatPattern(Pattern.REGION_FORMAT, regionDisplayName);
                } else {
                    String exemplarLocationName = this._tznames.getExemplarLocationName(str);
                    str2 = formatPattern(Pattern.REGION_FORMAT, exemplarLocationName);
                }
            }
            if (str2 == null) {
                this._genericLocationNamesMap.putIfAbsent(str.intern(), "");
            } else {
                synchronized (this) {
                    String intern = str.intern();
                    String putIfAbsent = this._genericLocationNamesMap.putIfAbsent(intern, str2.intern());
                    if (putIfAbsent == null) {
                        this._gnamesTrie.put(str2, new NameInfo(intern, GenericNameType.LOCATION));
                    } else {
                        str2 = putIfAbsent;
                    }
                }
            }
            return str2;
        } else if (str2.length() == 0) {
            return null;
        } else {
            return str2;
        }
    }

    public TimeZoneGenericNames setFormatPattern(Pattern pattern, String str) {
        if (!isFrozen()) {
            if (!this._genericLocationNamesMap.isEmpty()) {
                this._genericLocationNamesMap = new ConcurrentHashMap<>();
            }
            if (!this._genericPartialLocationNamesMap.isEmpty()) {
                this._genericPartialLocationNamesMap = new ConcurrentHashMap<>();
            }
            this._gnamesTrie = null;
            this._gnamesTrieFullyLoaded = false;
            if (this._patternFormatters == null) {
                this._patternFormatters = new MessageFormat[Pattern.values().length];
            }
            this._patternFormatters[pattern.ordinal()] = new MessageFormat(str);
            return this;
        }
        throw new UnsupportedOperationException("Attempt to modify frozen object");
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x009e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String formatGenericNonLocationName(ohos.global.icu.util.TimeZone r19, ohos.global.icu.impl.TimeZoneGenericNames.GenericNameType r20, long r21) {
        /*
        // Method dump skipped, instructions count: 280
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.TimeZoneGenericNames.formatGenericNonLocationName(ohos.global.icu.util.TimeZone, ohos.global.icu.impl.TimeZoneGenericNames$GenericNameType, long):java.lang.String");
    }

    private synchronized String formatPattern(Pattern pattern, String... strArr) {
        int ordinal;
        String str;
        if (this._patternFormatters == null) {
            this._patternFormatters = new MessageFormat[Pattern.values().length];
        }
        ordinal = pattern.ordinal();
        if (this._patternFormatters[ordinal] == null) {
            try {
                str = ((ICUResourceBundle) ICUResourceBundle.getBundleInstance(ICUData.ICU_ZONE_BASE_NAME, this._locale)).getStringWithFallback("zoneStrings/" + pattern.key());
            } catch (MissingResourceException unused) {
                str = pattern.defaultValue();
            }
            this._patternFormatters[ordinal] = new MessageFormat(str);
        }
        return this._patternFormatters[ordinal].format(strArr);
    }

    private synchronized LocaleDisplayNames getLocaleDisplayNames() {
        LocaleDisplayNames localeDisplayNames;
        localeDisplayNames = null;
        if (this._localeDisplayNamesRef != null) {
            localeDisplayNames = this._localeDisplayNamesRef.get();
        }
        if (localeDisplayNames == null) {
            localeDisplayNames = LocaleDisplayNames.getInstance(this._locale);
            this._localeDisplayNamesRef = new WeakReference<>(localeDisplayNames);
        }
        return localeDisplayNames;
    }

    private synchronized void loadStrings(String str) {
        if (str != null) {
            if (str.length() != 0) {
                getGenericLocationName(str);
                for (String str2 : this._tznames.getAvailableMetaZoneIDs(str)) {
                    if (!str.equals(this._tznames.getReferenceZoneID(str2, getTargetRegion()))) {
                        TimeZoneNames.NameType[] nameTypeArr = GENERIC_NON_LOCATION_TYPES;
                        int length = nameTypeArr.length;
                        for (int i = 0; i < length; i++) {
                            TimeZoneNames.NameType nameType = nameTypeArr[i];
                            String metaZoneDisplayName = this._tznames.getMetaZoneDisplayName(str2, nameType);
                            if (metaZoneDisplayName != null) {
                                getPartialLocationName(str, str2, nameType == TimeZoneNames.NameType.LONG_GENERIC, metaZoneDisplayName);
                            }
                        }
                    }
                }
            }
        }
    }

    private synchronized String getTargetRegion() {
        if (this._region == null) {
            this._region = this._locale.getCountry();
            if (this._region.length() == 0) {
                this._region = ULocale.addLikelySubtags(this._locale).getCountry();
                if (this._region.length() == 0) {
                    this._region = "001";
                }
            }
        }
        return this._region;
    }

    private String getPartialLocationName(String str, String str2, boolean z, String str3) {
        String str4;
        String str5 = str + "&" + str2 + "#" + (z ? "L" : "S");
        String str6 = this._genericPartialLocationNamesMap.get(str5);
        if (str6 != null) {
            return str6;
        }
        String canonicalCountry = ZoneMeta.getCanonicalCountry(str);
        if (canonicalCountry != null) {
            str4 = str.equals(this._tznames.getReferenceZoneID(str2, canonicalCountry)) ? getLocaleDisplayNames().regionDisplayName(canonicalCountry) : this._tznames.getExemplarLocationName(str);
        } else {
            str4 = this._tznames.getExemplarLocationName(str);
            if (str4 == null) {
                str4 = str;
            }
        }
        String formatPattern = formatPattern(Pattern.FALLBACK_FORMAT, str4, str3);
        synchronized (this) {
            String putIfAbsent = this._genericPartialLocationNamesMap.putIfAbsent(str5.intern(), formatPattern.intern());
            if (putIfAbsent == null) {
                this._gnamesTrie.put(formatPattern, new NameInfo(str.intern(), z ? GenericNameType.LONG : GenericNameType.SHORT));
            } else {
                formatPattern = putIfAbsent;
            }
        }
        return formatPattern;
    }

    /* access modifiers changed from: private */
    public static class NameInfo {
        final GenericNameType type;
        final String tzID;

        NameInfo(String str, GenericNameType genericNameType) {
            this.tzID = str;
            this.type = genericNameType;
        }
    }

    public static class GenericMatchInfo {
        final int matchLength;
        final GenericNameType nameType;
        final TimeZoneFormat.TimeType timeType;
        final String tzID;

        /* synthetic */ GenericMatchInfo(GenericNameType genericNameType, String str, int i, AnonymousClass1 r4) {
            this(genericNameType, str, i);
        }

        /* synthetic */ GenericMatchInfo(GenericNameType genericNameType, String str, int i, TimeZoneFormat.TimeType timeType2, AnonymousClass1 r5) {
            this(genericNameType, str, i, timeType2);
        }

        private GenericMatchInfo(GenericNameType genericNameType, String str, int i) {
            this(genericNameType, str, i, TimeZoneFormat.TimeType.UNKNOWN);
        }

        private GenericMatchInfo(GenericNameType genericNameType, String str, int i, TimeZoneFormat.TimeType timeType2) {
            this.nameType = genericNameType;
            this.tzID = str;
            this.matchLength = i;
            this.timeType = timeType2;
        }

        public GenericNameType nameType() {
            return this.nameType;
        }

        public String tzID() {
            return this.tzID;
        }

        public TimeZoneFormat.TimeType timeType() {
            return this.timeType;
        }

        public int matchLength() {
            return this.matchLength;
        }
    }

    /* access modifiers changed from: private */
    public static class GenericNameSearchHandler implements TextTrieMap.ResultHandler<NameInfo> {
        private Collection<GenericMatchInfo> _matches;
        private int _maxMatchLen;
        private EnumSet<GenericNameType> _types;

        GenericNameSearchHandler(EnumSet<GenericNameType> enumSet) {
            this._types = enumSet;
        }

        @Override // ohos.global.icu.impl.TextTrieMap.ResultHandler
        public boolean handlePrefixMatch(int i, Iterator<NameInfo> it) {
            while (it.hasNext()) {
                NameInfo next = it.next();
                EnumSet<GenericNameType> enumSet = this._types;
                if (enumSet == null || enumSet.contains(next.type)) {
                    GenericMatchInfo genericMatchInfo = new GenericMatchInfo(next.type, next.tzID, i, (AnonymousClass1) null);
                    if (this._matches == null) {
                        this._matches = new LinkedList();
                    }
                    this._matches.add(genericMatchInfo);
                    if (i > this._maxMatchLen) {
                        this._maxMatchLen = i;
                    }
                }
            }
            return true;
        }

        public Collection<GenericMatchInfo> getMatches() {
            return this._matches;
        }

        public int getMaxMatchLen() {
            return this._maxMatchLen;
        }

        public void resetResults() {
            this._matches = null;
            this._maxMatchLen = 0;
        }
    }

    public GenericMatchInfo findBestMatch(String str, int i, EnumSet<GenericNameType> enumSet) {
        if (str == null || str.length() == 0 || i < 0 || i >= str.length()) {
            throw new IllegalArgumentException("bad input text or range");
        }
        Collection<TimeZoneNames.MatchInfo> findTimeZoneNames = findTimeZoneNames(str, i, enumSet);
        GenericMatchInfo genericMatchInfo = null;
        if (findTimeZoneNames != null) {
            TimeZoneNames.MatchInfo matchInfo = null;
            for (TimeZoneNames.MatchInfo matchInfo2 : findTimeZoneNames) {
                if (matchInfo == null || matchInfo2.matchLength() > matchInfo.matchLength()) {
                    matchInfo = matchInfo2;
                }
            }
            if (matchInfo != null) {
                genericMatchInfo = createGenericMatchInfo(matchInfo);
                if (genericMatchInfo.matchLength() == str.length() - i && genericMatchInfo.timeType != TimeZoneFormat.TimeType.STANDARD) {
                    return genericMatchInfo;
                }
            }
        }
        Collection<GenericMatchInfo> findLocal = findLocal(str, i, enumSet);
        if (findLocal != null) {
            for (GenericMatchInfo genericMatchInfo2 : findLocal) {
                if (genericMatchInfo == null || genericMatchInfo2.matchLength() >= genericMatchInfo.matchLength()) {
                    genericMatchInfo = genericMatchInfo2;
                }
            }
        }
        return genericMatchInfo;
    }

    public Collection<GenericMatchInfo> find(String str, int i, EnumSet<GenericNameType> enumSet) {
        if (str == null || str.length() == 0 || i < 0 || i >= str.length()) {
            throw new IllegalArgumentException("bad input text or range");
        }
        Collection<GenericMatchInfo> findLocal = findLocal(str, i, enumSet);
        Collection<TimeZoneNames.MatchInfo> findTimeZoneNames = findTimeZoneNames(str, i, enumSet);
        if (findTimeZoneNames != null) {
            for (TimeZoneNames.MatchInfo matchInfo : findTimeZoneNames) {
                if (findLocal == null) {
                    findLocal = new LinkedList<>();
                }
                findLocal.add(createGenericMatchInfo(matchInfo));
            }
        }
        return findLocal;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.global.icu.impl.TimeZoneGenericNames$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$impl$TimeZoneGenericNames$GenericNameType = new int[GenericNameType.values().length];
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$text$TimeZoneNames$NameType = new int[TimeZoneNames.NameType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(18:0|1|2|3|5|6|7|9|10|(2:11|12)|13|15|16|17|18|19|20|22) */
        /* JADX WARNING: Can't wrap try/catch for region: R(19:0|1|2|3|5|6|7|9|10|11|12|13|15|16|17|18|19|20|22) */
        /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0048 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0052 */
        static {
            /*
                ohos.global.icu.text.TimeZoneNames$NameType[] r0 = ohos.global.icu.text.TimeZoneNames.NameType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.global.icu.impl.TimeZoneGenericNames.AnonymousClass1.$SwitchMap$ohos$global$icu$text$TimeZoneNames$NameType = r0
                r0 = 1
                int[] r1 = ohos.global.icu.impl.TimeZoneGenericNames.AnonymousClass1.$SwitchMap$ohos$global$icu$text$TimeZoneNames$NameType     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.global.icu.text.TimeZoneNames$NameType r2 = ohos.global.icu.text.TimeZoneNames.NameType.LONG_STANDARD     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                r1 = 2
                int[] r2 = ohos.global.icu.impl.TimeZoneGenericNames.AnonymousClass1.$SwitchMap$ohos$global$icu$text$TimeZoneNames$NameType     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.global.icu.text.TimeZoneNames$NameType r3 = ohos.global.icu.text.TimeZoneNames.NameType.LONG_GENERIC     // Catch:{ NoSuchFieldError -> 0x001f }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                r2 = 3
                int[] r3 = ohos.global.icu.impl.TimeZoneGenericNames.AnonymousClass1.$SwitchMap$ohos$global$icu$text$TimeZoneNames$NameType     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.global.icu.text.TimeZoneNames$NameType r4 = ohos.global.icu.text.TimeZoneNames.NameType.SHORT_STANDARD     // Catch:{ NoSuchFieldError -> 0x002a }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r3 = ohos.global.icu.impl.TimeZoneGenericNames.AnonymousClass1.$SwitchMap$ohos$global$icu$text$TimeZoneNames$NameType     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.global.icu.text.TimeZoneNames$NameType r4 = ohos.global.icu.text.TimeZoneNames.NameType.SHORT_GENERIC     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r5 = 4
                r3[r4] = r5     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                ohos.global.icu.impl.TimeZoneGenericNames$GenericNameType[] r3 = ohos.global.icu.impl.TimeZoneGenericNames.GenericNameType.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                ohos.global.icu.impl.TimeZoneGenericNames.AnonymousClass1.$SwitchMap$ohos$global$icu$impl$TimeZoneGenericNames$GenericNameType = r3
                int[] r3 = ohos.global.icu.impl.TimeZoneGenericNames.AnonymousClass1.$SwitchMap$ohos$global$icu$impl$TimeZoneGenericNames$GenericNameType     // Catch:{ NoSuchFieldError -> 0x0048 }
                ohos.global.icu.impl.TimeZoneGenericNames$GenericNameType r4 = ohos.global.icu.impl.TimeZoneGenericNames.GenericNameType.LOCATION     // Catch:{ NoSuchFieldError -> 0x0048 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0048 }
                r3[r4] = r0     // Catch:{ NoSuchFieldError -> 0x0048 }
            L_0x0048:
                int[] r0 = ohos.global.icu.impl.TimeZoneGenericNames.AnonymousClass1.$SwitchMap$ohos$global$icu$impl$TimeZoneGenericNames$GenericNameType     // Catch:{ NoSuchFieldError -> 0x0052 }
                ohos.global.icu.impl.TimeZoneGenericNames$GenericNameType r3 = ohos.global.icu.impl.TimeZoneGenericNames.GenericNameType.LONG     // Catch:{ NoSuchFieldError -> 0x0052 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0052 }
                r0[r3] = r1     // Catch:{ NoSuchFieldError -> 0x0052 }
            L_0x0052:
                int[] r0 = ohos.global.icu.impl.TimeZoneGenericNames.AnonymousClass1.$SwitchMap$ohos$global$icu$impl$TimeZoneGenericNames$GenericNameType     // Catch:{ NoSuchFieldError -> 0x005c }
                ohos.global.icu.impl.TimeZoneGenericNames$GenericNameType r1 = ohos.global.icu.impl.TimeZoneGenericNames.GenericNameType.SHORT     // Catch:{ NoSuchFieldError -> 0x005c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x005c }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x005c }
            L_0x005c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.TimeZoneGenericNames.AnonymousClass1.<clinit>():void");
        }
    }

    private GenericMatchInfo createGenericMatchInfo(TimeZoneNames.MatchInfo matchInfo) {
        GenericNameType genericNameType;
        String str;
        TimeZoneFormat.TimeType timeType = TimeZoneFormat.TimeType.UNKNOWN;
        int i = AnonymousClass1.$SwitchMap$ohos$global$icu$text$TimeZoneNames$NameType[matchInfo.nameType().ordinal()];
        if (i == 1) {
            genericNameType = GenericNameType.LONG;
            timeType = TimeZoneFormat.TimeType.STANDARD;
        } else if (i == 2) {
            genericNameType = GenericNameType.LONG;
        } else if (i == 3) {
            genericNameType = GenericNameType.SHORT;
            timeType = TimeZoneFormat.TimeType.STANDARD;
        } else if (i == 4) {
            genericNameType = GenericNameType.SHORT;
        } else {
            throw new IllegalArgumentException("Unexpected MatchInfo name type - " + matchInfo.nameType());
        }
        String tzID = matchInfo.tzID();
        if (tzID == null) {
            str = this._tznames.getReferenceZoneID(matchInfo.mzID(), getTargetRegion());
        } else {
            str = tzID;
        }
        return new GenericMatchInfo(genericNameType, str, matchInfo.matchLength(), timeType, null);
    }

    private Collection<TimeZoneNames.MatchInfo> findTimeZoneNames(String str, int i, EnumSet<GenericNameType> enumSet) {
        EnumSet<TimeZoneNames.NameType> noneOf = EnumSet.noneOf(TimeZoneNames.NameType.class);
        if (enumSet.contains(GenericNameType.LONG)) {
            noneOf.add(TimeZoneNames.NameType.LONG_GENERIC);
            noneOf.add(TimeZoneNames.NameType.LONG_STANDARD);
        }
        if (enumSet.contains(GenericNameType.SHORT)) {
            noneOf.add(TimeZoneNames.NameType.SHORT_GENERIC);
            noneOf.add(TimeZoneNames.NameType.SHORT_STANDARD);
        }
        if (!noneOf.isEmpty()) {
            return this._tznames.find(str, i, noneOf);
        }
        return null;
    }

    private synchronized Collection<GenericMatchInfo> findLocal(String str, int i, EnumSet<GenericNameType> enumSet) {
        GenericNameSearchHandler genericNameSearchHandler = new GenericNameSearchHandler(enumSet);
        this._gnamesTrie.find(str, i, genericNameSearchHandler);
        if (genericNameSearchHandler.getMaxMatchLen() != str.length() - i) {
            if (!this._gnamesTrieFullyLoaded) {
                for (String str2 : TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL, null, null)) {
                    loadStrings(str2);
                }
                this._gnamesTrieFullyLoaded = true;
                genericNameSearchHandler.resetResults();
                this._gnamesTrie.find(str, i, genericNameSearchHandler);
                return genericNameSearchHandler.getMatches();
            }
        }
        return genericNameSearchHandler.getMatches();
    }

    /* access modifiers changed from: private */
    public static class Cache extends SoftCache<String, TimeZoneGenericNames, ULocale> {
        private Cache() {
        }

        /* synthetic */ Cache(AnonymousClass1 r1) {
            this();
        }

        /* access modifiers changed from: protected */
        public TimeZoneGenericNames createInstance(String str, ULocale uLocale) {
            return new TimeZoneGenericNames(uLocale, (AnonymousClass1) null).freeze();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        init();
    }

    @Override // ohos.global.icu.util.Freezable
    public boolean isFrozen() {
        return this._frozen;
    }

    @Override // ohos.global.icu.util.Freezable
    public TimeZoneGenericNames freeze() {
        this._frozen = true;
        return this;
    }

    @Override // ohos.global.icu.util.Freezable
    public TimeZoneGenericNames cloneAsThawed() {
        try {
            TimeZoneGenericNames timeZoneGenericNames = (TimeZoneGenericNames) super.clone();
            try {
                timeZoneGenericNames._frozen = false;
                return timeZoneGenericNames;
            } catch (Throwable unused) {
                return timeZoneGenericNames;
            }
        } catch (Throwable unused2) {
            return null;
        }
    }
}
