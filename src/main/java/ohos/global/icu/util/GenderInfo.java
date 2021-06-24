package ohos.global.icu.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import ohos.global.icu.impl.ICUCache;
import ohos.global.icu.impl.ICUData;
import ohos.global.icu.impl.ICUResourceBundle;
import ohos.global.icu.impl.SimpleCache;

@Deprecated
public class GenderInfo {
    private static Cache genderInfoCache = new Cache(null);
    private static GenderInfo neutral = new GenderInfo(ListGenderStyle.NEUTRAL);
    private final ListGenderStyle style;

    @Deprecated
    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    @Deprecated
    public static GenderInfo getInstance(ULocale uLocale) {
        return genderInfoCache.get(uLocale);
    }

    @Deprecated
    public static GenderInfo getInstance(Locale locale) {
        return getInstance(ULocale.forLocale(locale));
    }

    @Deprecated
    public enum ListGenderStyle {
        NEUTRAL,
        MIXED_NEUTRAL,
        MALE_TAINTS;
        
        private static Map<String, ListGenderStyle> fromNameMap = new HashMap(3);

        static {
            fromNameMap.put("neutral", NEUTRAL);
            fromNameMap.put("maleTaints", MALE_TAINTS);
            fromNameMap.put("mixedNeutral", MIXED_NEUTRAL);
        }

        @Deprecated
        public static ListGenderStyle fromName(String str) {
            ListGenderStyle listGenderStyle = fromNameMap.get(str);
            if (listGenderStyle != null) {
                return listGenderStyle;
            }
            throw new IllegalArgumentException("Unknown gender style name: " + str);
        }
    }

    @Deprecated
    public Gender getListGender(Gender... genderArr) {
        return getListGender(Arrays.asList(genderArr));
    }

    @Deprecated
    public Gender getListGender(List<Gender> list) {
        if (list.size() == 0) {
            return Gender.OTHER;
        }
        boolean z = false;
        if (list.size() == 1) {
            return list.get(0);
        }
        int i = AnonymousClass1.$SwitchMap$ohos$global$icu$util$GenderInfo$ListGenderStyle[this.style.ordinal()];
        if (i == 1) {
            return Gender.OTHER;
        }
        if (i == 2) {
            boolean z2 = false;
            for (Gender gender : list) {
                int i2 = AnonymousClass1.$SwitchMap$ohos$global$icu$util$GenderInfo$Gender[gender.ordinal()];
                if (i2 != 1) {
                    if (i2 != 2) {
                        if (i2 == 3) {
                            return Gender.OTHER;
                        }
                    } else if (z2) {
                        return Gender.OTHER;
                    } else {
                        z = true;
                    }
                } else if (z) {
                    return Gender.OTHER;
                } else {
                    z2 = true;
                }
            }
            if (z) {
                return Gender.MALE;
            }
            return Gender.FEMALE;
        } else if (i != 3) {
            return Gender.OTHER;
        } else {
            for (Gender gender2 : list) {
                if (gender2 != Gender.FEMALE) {
                    return Gender.MALE;
                }
            }
            return Gender.FEMALE;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.global.icu.util.GenderInfo$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$util$GenderInfo$Gender = new int[Gender.values().length];
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$util$GenderInfo$ListGenderStyle = new int[ListGenderStyle.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|20) */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x003d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0047 */
        static {
            /*
                ohos.global.icu.util.GenderInfo$ListGenderStyle[] r0 = ohos.global.icu.util.GenderInfo.ListGenderStyle.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.global.icu.util.GenderInfo.AnonymousClass1.$SwitchMap$ohos$global$icu$util$GenderInfo$ListGenderStyle = r0
                r0 = 1
                int[] r1 = ohos.global.icu.util.GenderInfo.AnonymousClass1.$SwitchMap$ohos$global$icu$util$GenderInfo$ListGenderStyle     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.global.icu.util.GenderInfo$ListGenderStyle r2 = ohos.global.icu.util.GenderInfo.ListGenderStyle.NEUTRAL     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                r1 = 2
                int[] r2 = ohos.global.icu.util.GenderInfo.AnonymousClass1.$SwitchMap$ohos$global$icu$util$GenderInfo$ListGenderStyle     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.global.icu.util.GenderInfo$ListGenderStyle r3 = ohos.global.icu.util.GenderInfo.ListGenderStyle.MIXED_NEUTRAL     // Catch:{ NoSuchFieldError -> 0x001f }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                r2 = 3
                int[] r3 = ohos.global.icu.util.GenderInfo.AnonymousClass1.$SwitchMap$ohos$global$icu$util$GenderInfo$ListGenderStyle     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.global.icu.util.GenderInfo$ListGenderStyle r4 = ohos.global.icu.util.GenderInfo.ListGenderStyle.MALE_TAINTS     // Catch:{ NoSuchFieldError -> 0x002a }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                ohos.global.icu.util.GenderInfo$Gender[] r3 = ohos.global.icu.util.GenderInfo.Gender.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                ohos.global.icu.util.GenderInfo.AnonymousClass1.$SwitchMap$ohos$global$icu$util$GenderInfo$Gender = r3
                int[] r3 = ohos.global.icu.util.GenderInfo.AnonymousClass1.$SwitchMap$ohos$global$icu$util$GenderInfo$Gender     // Catch:{ NoSuchFieldError -> 0x003d }
                ohos.global.icu.util.GenderInfo$Gender r4 = ohos.global.icu.util.GenderInfo.Gender.FEMALE     // Catch:{ NoSuchFieldError -> 0x003d }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x003d }
                r3[r4] = r0     // Catch:{ NoSuchFieldError -> 0x003d }
            L_0x003d:
                int[] r0 = ohos.global.icu.util.GenderInfo.AnonymousClass1.$SwitchMap$ohos$global$icu$util$GenderInfo$Gender     // Catch:{ NoSuchFieldError -> 0x0047 }
                ohos.global.icu.util.GenderInfo$Gender r3 = ohos.global.icu.util.GenderInfo.Gender.MALE     // Catch:{ NoSuchFieldError -> 0x0047 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0047 }
                r0[r3] = r1     // Catch:{ NoSuchFieldError -> 0x0047 }
            L_0x0047:
                int[] r0 = ohos.global.icu.util.GenderInfo.AnonymousClass1.$SwitchMap$ohos$global$icu$util$GenderInfo$Gender     // Catch:{ NoSuchFieldError -> 0x0051 }
                ohos.global.icu.util.GenderInfo$Gender r1 = ohos.global.icu.util.GenderInfo.Gender.OTHER     // Catch:{ NoSuchFieldError -> 0x0051 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0051 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0051 }
            L_0x0051:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.GenderInfo.AnonymousClass1.<clinit>():void");
        }
    }

    @Deprecated
    public GenderInfo(ListGenderStyle listGenderStyle) {
        this.style = listGenderStyle;
    }

    /* access modifiers changed from: private */
    public static class Cache {
        private final ICUCache<ULocale, GenderInfo> cache;

        private Cache() {
            this.cache = new SimpleCache();
        }

        /* synthetic */ Cache(AnonymousClass1 r1) {
            this();
        }

        public GenderInfo get(ULocale uLocale) {
            GenderInfo genderInfo = this.cache.get(uLocale);
            if (genderInfo == null) {
                genderInfo = load(uLocale);
                if (genderInfo == null) {
                    ULocale fallback = uLocale.getFallback();
                    genderInfo = fallback == null ? GenderInfo.neutral : get(fallback);
                }
                this.cache.put(uLocale, genderInfo);
            }
            return genderInfo;
        }

        private static GenderInfo load(ULocale uLocale) {
            try {
                return new GenderInfo(ListGenderStyle.fromName(UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, "genderList", ICUResourceBundle.ICU_DATA_CLASS_LOADER, true).get("genderList").getString(uLocale.toString())));
            } catch (MissingResourceException unused) {
                return null;
            }
        }
    }
}
