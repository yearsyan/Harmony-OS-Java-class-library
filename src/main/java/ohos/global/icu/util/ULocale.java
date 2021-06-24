package ohos.global.icu.util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;
import ohos.ai.cv.text.TextParamKey;
import ohos.com.sun.org.apache.xml.internal.utils.LocaleUtility;
import ohos.data.search.model.IndexType;
import ohos.global.icu.impl.CacheBase;
import ohos.global.icu.impl.ICUData;
import ohos.global.icu.impl.ICUResourceBundle;
import ohos.global.icu.impl.ICUResourceTableAccess;
import ohos.global.icu.impl.LocaleIDParser;
import ohos.global.icu.impl.LocaleIDs;
import ohos.global.icu.impl.SoftCache;
import ohos.global.icu.impl.locale.AsciiUtil;
import ohos.global.icu.impl.locale.BaseLocale;
import ohos.global.icu.impl.locale.Extension;
import ohos.global.icu.impl.locale.InternalLocaleBuilder;
import ohos.global.icu.impl.locale.KeyTypeData;
import ohos.global.icu.impl.locale.LanguageTag;
import ohos.global.icu.impl.locale.LocaleExtensions;
import ohos.global.icu.impl.locale.LocaleSyntaxException;
import ohos.global.icu.impl.locale.ParseStatus;
import ohos.global.icu.impl.locale.UnicodeLocaleExtension;
import ohos.global.icu.lang.UScript;
import ohos.global.icu.text.DateFormat;
import ohos.global.icu.text.LocaleDisplayNames;
import ohos.global.icu.util.LocaleMatcher;
import ohos.telephony.TelephoneNumberUtils;

public final class ULocale implements Serializable, Comparable<ULocale> {
    public static Type ACTUAL_LOCALE = new Type();
    private static final SoftCache<Locale, ULocale, Void> CACHE = new SoftCache<Locale, ULocale, Void>() {
        /* class ohos.global.icu.util.ULocale.AnonymousClass2 */

        /* access modifiers changed from: protected */
        public ULocale createInstance(Locale locale, Void r2) {
            return JDKLocaleHelper.toULocale(locale);
        }
    };
    public static final ULocale CANADA = new ULocale("en_CA", Locale.CANADA);
    public static final ULocale CANADA_FRENCH = new ULocale("fr_CA", Locale.CANADA_FRENCH);
    private static String[][] CANONICALIZE_MAP = {new String[]{"art__LOJBAN", "jbo"}, new String[]{"cel__GAULISH", "cel__GAULISH"}, new String[]{"de__1901", "de__1901"}, new String[]{"de__1906", "de__1906"}, new String[]{"en__BOONT", "en__BOONT"}, new String[]{"en__SCOUSE", "en__SCOUSE"}, new String[]{"hy__AREVELA", "hy", null, null}, new String[]{"hy__AREVMDA", "hyw", null, null}, new String[]{"sl__ROZAJ", "sl__ROZAJ"}, new String[]{"zh__GUOYU", "zh"}, new String[]{"zh__HAKKA", "hak"}, new String[]{"zh__XIANG", "hsn"}, new String[]{"zh_GAN", "gan"}, new String[]{"zh_MIN", "zh__MIN"}, new String[]{"zh_MIN_NAN", "nan"}, new String[]{"zh_WUU", "wuu"}, new String[]{"zh_YUE", "yue"}};
    public static final ULocale CHINA = new ULocale("zh_Hans_CN");
    public static final ULocale CHINESE = new ULocale("zh", Locale.CHINESE);
    private static final Locale EMPTY_LOCALE = new Locale("", "");
    private static final String EMPTY_STRING = "";
    public static final ULocale ENGLISH = new ULocale("en", Locale.ENGLISH);
    public static final ULocale FRANCE = new ULocale("fr_FR", Locale.FRANCE);
    public static final ULocale FRENCH = new ULocale("fr", Locale.FRENCH);
    public static final ULocale GERMAN = new ULocale("de", Locale.GERMAN);
    public static final ULocale GERMANY = new ULocale("de_DE", Locale.GERMANY);
    public static final ULocale ITALIAN = new ULocale("it", Locale.ITALIAN);
    public static final ULocale ITALY = new ULocale("it_IT", Locale.ITALY);
    public static final ULocale JAPAN = new ULocale("ja_JP", Locale.JAPAN);
    public static final ULocale JAPANESE = new ULocale("ja", Locale.JAPANESE);
    public static final ULocale KOREA = new ULocale("ko_KR", Locale.KOREA);
    public static final ULocale KOREAN = new ULocale("ko", Locale.KOREAN);
    private static final String LANG_DIR_STRING = "root-en-es-pt-zh-ja-ko-de-fr-it-ar+he+fa+ru-nl-pl-th-tr-";
    private static final String LOCALE_ATTRIBUTE_KEY = "attribute";
    public static final ULocale PRC = CHINA;
    public static final char PRIVATE_USE_EXTENSION = 'x';
    public static final ULocale ROOT = new ULocale("", EMPTY_LOCALE);
    public static final ULocale SIMPLIFIED_CHINESE = new ULocale("zh_Hans");
    public static final ULocale TAIWAN = new ULocale("zh_Hant_TW");
    public static final ULocale TRADITIONAL_CHINESE = new ULocale("zh_Hant");
    public static final ULocale UK = new ULocale("en_GB", Locale.UK);
    private static final String UNDEFINED_LANGUAGE = "und";
    private static final String UNDEFINED_REGION = "ZZ";
    private static final String UNDEFINED_SCRIPT = "Zzzz";
    private static final char UNDERSCORE = '_';
    private static final Pattern UND_PATTERN = Pattern.compile("^und(?=$|[_-])", 2);
    public static final char UNICODE_LOCALE_EXTENSION = 'u';
    public static final ULocale US = new ULocale("en_US", Locale.US);
    public static Type VALID_LOCALE = new Type();
    private static Locale[] defaultCategoryLocales = new Locale[Category.values().length];
    private static ULocale[] defaultCategoryULocales = new ULocale[Category.values().length];
    private static Locale defaultLocale = Locale.getDefault();
    private static ULocale defaultULocale = forLocale(defaultLocale);
    private static CacheBase<String, String, Void> nameCache = new SoftCache<String, String, Void>() {
        /* class ohos.global.icu.util.ULocale.AnonymousClass1 */

        /* access modifiers changed from: protected */
        public String createInstance(String str, Void r2) {
            return new LocaleIDParser(str).getName();
        }
    };
    private static final long serialVersionUID = 3715177670352309217L;
    private volatile transient BaseLocale baseLocale;
    private volatile transient LocaleExtensions extensions;
    private volatile transient Locale locale;
    private String localeID;

    public enum AvailableType {
        DEFAULT,
        ONLY_LEGACY_ALIASES,
        WITH_LEGACY_ALIASES
    }

    public enum Category {
        DISPLAY,
        FORMAT
    }

    @Deprecated
    public enum Minimize {
        FAVOR_SCRIPT,
        FAVOR_REGION
    }

    @Override // java.lang.Object
    public Object clone() {
        return this;
    }

    static {
        int i = 0;
        if (JDKLocaleHelper.hasLocaleCategories()) {
            Category[] values = Category.values();
            int length = values.length;
            while (i < length) {
                Category category = values[i];
                int ordinal = category.ordinal();
                defaultCategoryLocales[ordinal] = JDKLocaleHelper.getDefault(category);
                defaultCategoryULocales[ordinal] = forLocale(defaultCategoryLocales[ordinal]);
                i++;
            }
        } else {
            Category[] values2 = Category.values();
            int length2 = values2.length;
            while (i < length2) {
                int ordinal2 = values2[i].ordinal();
                defaultCategoryLocales[ordinal2] = defaultLocale;
                defaultCategoryULocales[ordinal2] = defaultULocale;
                i++;
            }
        }
    }

    private ULocale(String str, Locale locale2) {
        this.localeID = str;
        this.locale = locale2;
    }

    public static ULocale forLocale(Locale locale2) {
        if (locale2 == null) {
            return null;
        }
        return CACHE.getInstance(locale2, null);
    }

    public ULocale(String str) {
        this.localeID = getName(str);
    }

    public ULocale(String str, String str2) {
        this(str, str2, (String) null);
    }

    public ULocale(String str, String str2, String str3) {
        this.localeID = getName(lscvToID(str, str2, str3, ""));
    }

    public static ULocale createCanonical(String str) {
        return new ULocale(canonicalize(str), (Locale) null);
    }

    public static ULocale createCanonical(ULocale uLocale) {
        return createCanonical(uLocale.getName());
    }

    private static String lscvToID(String str, String str2, String str3, String str4) {
        StringBuilder sb = new StringBuilder();
        if (str != null && str.length() > 0) {
            sb.append(str);
        }
        if (str2 != null && str2.length() > 0) {
            sb.append(UNDERSCORE);
            sb.append(str2);
        }
        if (str3 != null && str3.length() > 0) {
            sb.append(UNDERSCORE);
            sb.append(str3);
        }
        if (str4 != null && str4.length() > 0) {
            if (str3 == null || str3.length() == 0) {
                sb.append(UNDERSCORE);
            }
            sb.append(UNDERSCORE);
            sb.append(str4);
        }
        return sb.toString();
    }

    public Locale toLocale() {
        if (this.locale == null) {
            this.locale = JDKLocaleHelper.toLocale(this);
        }
        return this.locale;
    }

    public static ULocale getDefault() {
        synchronized (ULocale.class) {
            if (defaultULocale == null) {
                return ROOT;
            }
            Locale locale2 = Locale.getDefault();
            if (!defaultLocale.equals(locale2)) {
                defaultLocale = locale2;
                defaultULocale = forLocale(locale2);
                if (!JDKLocaleHelper.hasLocaleCategories()) {
                    for (Category category : Category.values()) {
                        int ordinal = category.ordinal();
                        defaultCategoryLocales[ordinal] = locale2;
                        defaultCategoryULocales[ordinal] = forLocale(locale2);
                    }
                }
            }
            return defaultULocale;
        }
    }

    public static synchronized void setDefault(ULocale uLocale) {
        synchronized (ULocale.class) {
            defaultLocale = uLocale.toLocale();
            Locale.setDefault(defaultLocale);
            defaultULocale = uLocale;
            for (Category category : Category.values()) {
                setDefault(category, uLocale);
            }
        }
    }

    public static ULocale getDefault(Category category) {
        synchronized (ULocale.class) {
            int ordinal = category.ordinal();
            if (defaultCategoryULocales[ordinal] == null) {
                return ROOT;
            }
            if (JDKLocaleHelper.hasLocaleCategories()) {
                Locale locale2 = JDKLocaleHelper.getDefault(category);
                if (!defaultCategoryLocales[ordinal].equals(locale2)) {
                    defaultCategoryLocales[ordinal] = locale2;
                    defaultCategoryULocales[ordinal] = forLocale(locale2);
                }
            } else {
                Locale locale3 = Locale.getDefault();
                if (!defaultLocale.equals(locale3)) {
                    defaultLocale = locale3;
                    defaultULocale = forLocale(locale3);
                    for (Category category2 : Category.values()) {
                        int ordinal2 = category2.ordinal();
                        defaultCategoryLocales[ordinal2] = locale3;
                        defaultCategoryULocales[ordinal2] = forLocale(locale3);
                    }
                }
            }
            return defaultCategoryULocales[ordinal];
        }
    }

    public static synchronized void setDefault(Category category, ULocale uLocale) {
        synchronized (ULocale.class) {
            Locale locale2 = uLocale.toLocale();
            int ordinal = category.ordinal();
            defaultCategoryULocales[ordinal] = uLocale;
            defaultCategoryLocales[ordinal] = locale2;
            JDKLocaleHelper.setDefault(category, locale2);
        }
    }

    public int hashCode() {
        return this.localeID.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ULocale) {
            return this.localeID.equals(((ULocale) obj).localeID);
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0093, code lost:
        if (r5.hasNext() != false) goto L_0x0095;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int compareTo(ohos.global.icu.util.ULocale r9) {
        /*
        // Method dump skipped, instructions count: 158
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.ULocale.compareTo(ohos.global.icu.util.ULocale):int");
    }

    public static ULocale[] getAvailableLocales() {
        return (ULocale[]) ICUResourceBundle.getAvailableULocales().clone();
    }

    public static Collection<ULocale> getAvailableLocalesByType(AvailableType availableType) {
        List list;
        if (availableType != null) {
            if (availableType == AvailableType.WITH_LEGACY_ALIASES) {
                list = new ArrayList();
                Collections.addAll(list, ICUResourceBundle.getAvailableULocales(AvailableType.DEFAULT));
                Collections.addAll(list, ICUResourceBundle.getAvailableULocales(AvailableType.ONLY_LEGACY_ALIASES));
            } else {
                list = Arrays.asList(ICUResourceBundle.getAvailableULocales(availableType));
            }
            return Collections.unmodifiableList(list);
        }
        throw new IllegalArgumentException();
    }

    public static String[] getISOCountries() {
        return LocaleIDs.getISOCountries();
    }

    public static String[] getISOLanguages() {
        return LocaleIDs.getISOLanguages();
    }

    public String getLanguage() {
        return base().getLanguage();
    }

    public static String getLanguage(String str) {
        return new LocaleIDParser(str).getLanguage();
    }

    public String getScript() {
        return base().getScript();
    }

    public static String getScript(String str) {
        return new LocaleIDParser(str).getScript();
    }

    public String getCountry() {
        return base().getRegion();
    }

    public static String getCountry(String str) {
        return new LocaleIDParser(str).getCountry();
    }

    @Deprecated
    public static String getRegionForSupplementalData(ULocale uLocale, boolean z) {
        String keywordValue = uLocale.getKeywordValue("rg");
        if (keywordValue != null && keywordValue.length() == 6) {
            String upperString = AsciiUtil.toUpperString(keywordValue);
            if (upperString.endsWith(DateFormat.ABBR_UTC_TZ)) {
                return upperString.substring(0, 2);
            }
        }
        String country = uLocale.getCountry();
        return (country.length() != 0 || !z) ? country : addLikelySubtags(uLocale).getCountry();
    }

    public String getVariant() {
        return base().getVariant();
    }

    public static String getVariant(String str) {
        return new LocaleIDParser(str).getVariant();
    }

    public static String getFallback(String str) {
        return getFallbackString(getName(str));
    }

    public ULocale getFallback() {
        if (this.localeID.length() == 0 || this.localeID.charAt(0) == '@') {
            return null;
        }
        return new ULocale(getFallbackString(this.localeID), (Locale) null);
    }

    private static String getFallbackString(String str) {
        int indexOf = str.indexOf(64);
        if (indexOf == -1) {
            indexOf = str.length();
        }
        int lastIndexOf = str.lastIndexOf(95, indexOf);
        if (lastIndexOf == -1) {
            lastIndexOf = 0;
        } else {
            while (lastIndexOf > 0 && str.charAt(lastIndexOf - 1) == '_') {
                lastIndexOf--;
            }
        }
        return str.substring(0, lastIndexOf) + str.substring(indexOf);
    }

    public String getBaseName() {
        return getBaseName(this.localeID);
    }

    public static String getBaseName(String str) {
        if (str.indexOf(64) == -1) {
            return str;
        }
        return new LocaleIDParser(str).getBaseName();
    }

    public String getName() {
        return this.localeID;
    }

    private static int getShortestSubtagLength(String str) {
        int length = str.length();
        int i = length;
        int i2 = 0;
        boolean z = true;
        for (int i3 = 0; i3 < length; i3++) {
            if (str.charAt(i3) == '_' || str.charAt(i3) == '-') {
                if (i2 != 0 && i2 < i) {
                    i = i2;
                }
                z = true;
            } else {
                if (z) {
                    i2 = 0;
                    z = false;
                }
                i2++;
            }
        }
        return i;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001f, code lost:
        if (r0.length() == 0) goto L_0x0036;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getName(java.lang.String r3) {
        /*
            java.lang.String r0 = ""
            if (r3 == 0) goto L_0x0022
            java.lang.String r1 = "@"
            boolean r1 = r3.contains(r1)
            if (r1 != 0) goto L_0x0022
            int r1 = getShortestSubtagLength(r3)
            r2 = 1
            if (r1 != r2) goto L_0x0022
            ohos.global.icu.util.ULocale r0 = forLanguageTag(r3)
            java.lang.String r0 = r0.getName()
            int r1 = r0.length()
            if (r1 != 0) goto L_0x002a
            goto L_0x0036
        L_0x0022:
            java.lang.String r1 = "root"
            boolean r1 = r1.equalsIgnoreCase(r3)
            if (r1 == 0) goto L_0x002c
        L_0x002a:
            r3 = r0
            goto L_0x0036
        L_0x002c:
            java.util.regex.Pattern r1 = ohos.global.icu.util.ULocale.UND_PATTERN
            java.util.regex.Matcher r3 = r1.matcher(r3)
            java.lang.String r3 = r3.replaceFirst(r0)
        L_0x0036:
            ohos.global.icu.impl.CacheBase<java.lang.String, java.lang.String, java.lang.Void> r0 = ohos.global.icu.util.ULocale.nameCache
            r1 = 0
            java.lang.Object r3 = r0.getInstance(r3, r1)
            java.lang.String r3 = (java.lang.String) r3
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.ULocale.getName(java.lang.String):java.lang.String");
    }

    public String toString() {
        return this.localeID;
    }

    public Iterator<String> getKeywords() {
        return getKeywords(this.localeID);
    }

    public static Iterator<String> getKeywords(String str) {
        return new LocaleIDParser(str).getKeywords();
    }

    public String getKeywordValue(String str) {
        return getKeywordValue(this.localeID, str);
    }

    public static String getKeywordValue(String str, String str2) {
        return new LocaleIDParser(str).getKeywordValue(str2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:73:0x0274 A[Catch:{ MissingResourceException -> 0x02b6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0279 A[Catch:{ MissingResourceException -> 0x02b6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0287 A[Catch:{ MissingResourceException -> 0x02b6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x028c A[Catch:{ MissingResourceException -> 0x02b6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x02c0 A[SYNTHETIC, Splitter:B:85:0x02c0] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String canonicalize(java.lang.String r18) {
        /*
        // Method dump skipped, instructions count: 858
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.ULocale.canonicalize(java.lang.String):java.lang.String");
    }

    public ULocale setKeywordValue(String str, String str2) {
        return new ULocale(setKeywordValue(this.localeID, str, str2), (Locale) null);
    }

    public static String setKeywordValue(String str, String str2, String str3) {
        LocaleIDParser localeIDParser = new LocaleIDParser(str);
        localeIDParser.setKeywordValue(str2, str3);
        return localeIDParser.getName();
    }

    public String getISO3Language() {
        return getISO3Language(this.localeID);
    }

    public static String getISO3Language(String str) {
        return LocaleIDs.getISO3Language(getLanguage(str));
    }

    public String getISO3Country() {
        return getISO3Country(this.localeID);
    }

    public static String getISO3Country(String str) {
        return LocaleIDs.getISO3Country(getCountry(str));
    }

    public boolean isRightToLeft() {
        int indexOf;
        String script = getScript();
        if (script.length() == 0) {
            String language = getLanguage();
            if (!language.isEmpty() && (indexOf = LANG_DIR_STRING.indexOf(language)) >= 0) {
                char charAt = LANG_DIR_STRING.charAt(indexOf + language.length());
                if (charAt == '+') {
                    return true;
                }
                if (charAt == '-') {
                    return false;
                }
            }
            script = addLikelySubtags(this).getScript();
            if (script.length() == 0) {
                return false;
            }
        }
        return UScript.isRightToLeft(UScript.getCodeFromName(script));
    }

    public String getDisplayLanguage() {
        return getDisplayLanguageInternal(this, getDefault(Category.DISPLAY), false);
    }

    public String getDisplayLanguage(ULocale uLocale) {
        return getDisplayLanguageInternal(this, uLocale, false);
    }

    public static String getDisplayLanguage(String str, String str2) {
        return getDisplayLanguageInternal(new ULocale(str), new ULocale(str2), false);
    }

    public static String getDisplayLanguage(String str, ULocale uLocale) {
        return getDisplayLanguageInternal(new ULocale(str), uLocale, false);
    }

    public String getDisplayLanguageWithDialect() {
        return getDisplayLanguageInternal(this, getDefault(Category.DISPLAY), true);
    }

    public String getDisplayLanguageWithDialect(ULocale uLocale) {
        return getDisplayLanguageInternal(this, uLocale, true);
    }

    public static String getDisplayLanguageWithDialect(String str, String str2) {
        return getDisplayLanguageInternal(new ULocale(str), new ULocale(str2), true);
    }

    public static String getDisplayLanguageWithDialect(String str, ULocale uLocale) {
        return getDisplayLanguageInternal(new ULocale(str), uLocale, true);
    }

    private static String getDisplayLanguageInternal(ULocale uLocale, ULocale uLocale2, boolean z) {
        return LocaleDisplayNames.getInstance(uLocale2).languageDisplayName(z ? uLocale.getBaseName() : uLocale.getLanguage());
    }

    public String getDisplayScript() {
        return getDisplayScriptInternal(this, getDefault(Category.DISPLAY));
    }

    @Deprecated
    public String getDisplayScriptInContext() {
        return getDisplayScriptInContextInternal(this, getDefault(Category.DISPLAY));
    }

    public String getDisplayScript(ULocale uLocale) {
        return getDisplayScriptInternal(this, uLocale);
    }

    @Deprecated
    public String getDisplayScriptInContext(ULocale uLocale) {
        return getDisplayScriptInContextInternal(this, uLocale);
    }

    public static String getDisplayScript(String str, String str2) {
        return getDisplayScriptInternal(new ULocale(str), new ULocale(str2));
    }

    @Deprecated
    public static String getDisplayScriptInContext(String str, String str2) {
        return getDisplayScriptInContextInternal(new ULocale(str), new ULocale(str2));
    }

    public static String getDisplayScript(String str, ULocale uLocale) {
        return getDisplayScriptInternal(new ULocale(str), uLocale);
    }

    @Deprecated
    public static String getDisplayScriptInContext(String str, ULocale uLocale) {
        return getDisplayScriptInContextInternal(new ULocale(str), uLocale);
    }

    private static String getDisplayScriptInternal(ULocale uLocale, ULocale uLocale2) {
        return LocaleDisplayNames.getInstance(uLocale2).scriptDisplayName(uLocale.getScript());
    }

    private static String getDisplayScriptInContextInternal(ULocale uLocale, ULocale uLocale2) {
        return LocaleDisplayNames.getInstance(uLocale2).scriptDisplayNameInContext(uLocale.getScript());
    }

    public String getDisplayCountry() {
        return getDisplayCountryInternal(this, getDefault(Category.DISPLAY));
    }

    public String getDisplayCountry(ULocale uLocale) {
        return getDisplayCountryInternal(this, uLocale);
    }

    public static String getDisplayCountry(String str, String str2) {
        return getDisplayCountryInternal(new ULocale(str), new ULocale(str2));
    }

    public static String getDisplayCountry(String str, ULocale uLocale) {
        return getDisplayCountryInternal(new ULocale(str), uLocale);
    }

    private static String getDisplayCountryInternal(ULocale uLocale, ULocale uLocale2) {
        return LocaleDisplayNames.getInstance(uLocale2).regionDisplayName(uLocale.getCountry());
    }

    public String getDisplayVariant() {
        return getDisplayVariantInternal(this, getDefault(Category.DISPLAY));
    }

    public String getDisplayVariant(ULocale uLocale) {
        return getDisplayVariantInternal(this, uLocale);
    }

    public static String getDisplayVariant(String str, String str2) {
        return getDisplayVariantInternal(new ULocale(str), new ULocale(str2));
    }

    public static String getDisplayVariant(String str, ULocale uLocale) {
        return getDisplayVariantInternal(new ULocale(str), uLocale);
    }

    private static String getDisplayVariantInternal(ULocale uLocale, ULocale uLocale2) {
        return LocaleDisplayNames.getInstance(uLocale2).variantDisplayName(uLocale.getVariant());
    }

    public static String getDisplayKeyword(String str) {
        return getDisplayKeywordInternal(str, getDefault(Category.DISPLAY));
    }

    public static String getDisplayKeyword(String str, String str2) {
        return getDisplayKeywordInternal(str, new ULocale(str2));
    }

    public static String getDisplayKeyword(String str, ULocale uLocale) {
        return getDisplayKeywordInternal(str, uLocale);
    }

    private static String getDisplayKeywordInternal(String str, ULocale uLocale) {
        return LocaleDisplayNames.getInstance(uLocale).keyDisplayName(str);
    }

    public String getDisplayKeywordValue(String str) {
        return getDisplayKeywordValueInternal(this, str, getDefault(Category.DISPLAY));
    }

    public String getDisplayKeywordValue(String str, ULocale uLocale) {
        return getDisplayKeywordValueInternal(this, str, uLocale);
    }

    public static String getDisplayKeywordValue(String str, String str2, String str3) {
        return getDisplayKeywordValueInternal(new ULocale(str), str2, new ULocale(str3));
    }

    public static String getDisplayKeywordValue(String str, String str2, ULocale uLocale) {
        return getDisplayKeywordValueInternal(new ULocale(str), str2, uLocale);
    }

    private static String getDisplayKeywordValueInternal(ULocale uLocale, String str, ULocale uLocale2) {
        String lowerString = AsciiUtil.toLowerString(str.trim());
        return LocaleDisplayNames.getInstance(uLocale2).keyValueDisplayName(lowerString, uLocale.getKeywordValue(lowerString));
    }

    public String getDisplayName() {
        return getDisplayNameInternal(this, getDefault(Category.DISPLAY));
    }

    public String getDisplayName(ULocale uLocale) {
        return getDisplayNameInternal(this, uLocale);
    }

    public static String getDisplayName(String str, String str2) {
        return getDisplayNameInternal(new ULocale(str), new ULocale(str2));
    }

    public static String getDisplayName(String str, ULocale uLocale) {
        return getDisplayNameInternal(new ULocale(str), uLocale);
    }

    private static String getDisplayNameInternal(ULocale uLocale, ULocale uLocale2) {
        return LocaleDisplayNames.getInstance(uLocale2).localeDisplayName(uLocale);
    }

    public String getDisplayNameWithDialect() {
        return getDisplayNameWithDialectInternal(this, getDefault(Category.DISPLAY));
    }

    public String getDisplayNameWithDialect(ULocale uLocale) {
        return getDisplayNameWithDialectInternal(this, uLocale);
    }

    public static String getDisplayNameWithDialect(String str, String str2) {
        return getDisplayNameWithDialectInternal(new ULocale(str), new ULocale(str2));
    }

    public static String getDisplayNameWithDialect(String str, ULocale uLocale) {
        return getDisplayNameWithDialectInternal(new ULocale(str), uLocale);
    }

    private static String getDisplayNameWithDialectInternal(ULocale uLocale, ULocale uLocale2) {
        return LocaleDisplayNames.getInstance(uLocale2, LocaleDisplayNames.DialectHandling.DIALECT_NAMES).localeDisplayName(uLocale);
    }

    public String getCharacterOrientation() {
        return ICUResourceTableAccess.getTableString(ICUData.ICU_BASE_NAME, this, "layout", "characters", "characters");
    }

    public String getLineOrientation() {
        return ICUResourceTableAccess.getTableString(ICUData.ICU_BASE_NAME, this, "layout", TextParamKey.TEXT_LINES, TextParamKey.TEXT_LINES);
    }

    public static final class Type {
        private Type() {
        }
    }

    public static ULocale acceptLanguage(String str, ULocale[] uLocaleArr, boolean[] zArr) {
        if (zArr != null) {
            zArr[0] = true;
        }
        try {
            LocalePriorityList build = LocalePriorityList.add(str).build();
            LocaleMatcher.Builder builder = LocaleMatcher.builder();
            for (ULocale uLocale : uLocaleArr) {
                builder.addSupportedULocale(uLocale);
            }
            LocaleMatcher.Result bestMatchResult = builder.build().getBestMatchResult(build);
            if (bestMatchResult.getDesiredIndex() >= 0) {
                if (zArr != null && bestMatchResult.getDesiredULocale().equals(bestMatchResult.getSupportedULocale())) {
                    zArr[0] = false;
                }
                return bestMatchResult.getSupportedULocale();
            }
        } catch (IllegalArgumentException unused) {
        }
        return null;
    }

    public static ULocale acceptLanguage(ULocale[] uLocaleArr, ULocale[] uLocaleArr2, boolean[] zArr) {
        LocaleMatcher.Result result;
        if (zArr != null) {
            zArr[0] = true;
        }
        LocaleMatcher.Builder builder = LocaleMatcher.builder();
        for (ULocale uLocale : uLocaleArr2) {
            builder.addSupportedULocale(uLocale);
        }
        LocaleMatcher build = builder.build();
        if (uLocaleArr.length == 1) {
            result = build.getBestMatchResult(uLocaleArr[0]);
        } else {
            result = build.getBestMatchResult(Arrays.asList(uLocaleArr));
        }
        if (result.getDesiredIndex() < 0) {
            return null;
        }
        if (zArr != null && result.getDesiredULocale().equals(result.getSupportedULocale())) {
            zArr[0] = false;
        }
        return result.getSupportedULocale();
    }

    public static ULocale acceptLanguage(String str, boolean[] zArr) {
        return acceptLanguage(str, getAvailableLocales(), zArr);
    }

    public static ULocale acceptLanguage(ULocale[] uLocaleArr, boolean[] zArr) {
        return acceptLanguage(uLocaleArr, getAvailableLocales(), zArr);
    }

    public static ULocale addLikelySubtags(ULocale uLocale) {
        String[] strArr = new String[3];
        int parseTagString = parseTagString(uLocale.localeID, strArr);
        String createLikelySubtagsString = createLikelySubtagsString(strArr[0], strArr[1], strArr[2], parseTagString < uLocale.localeID.length() ? uLocale.localeID.substring(parseTagString) : null);
        return createLikelySubtagsString == null ? uLocale : new ULocale(createLikelySubtagsString);
    }

    public static ULocale minimizeSubtags(ULocale uLocale) {
        return minimizeSubtags(uLocale, Minimize.FAVOR_REGION);
    }

    @Deprecated
    public static ULocale minimizeSubtags(ULocale uLocale, Minimize minimize) {
        String[] strArr = new String[3];
        int parseTagString = parseTagString(uLocale.localeID, strArr);
        String str = strArr[0];
        String str2 = strArr[1];
        String str3 = strArr[2];
        String substring = parseTagString < uLocale.localeID.length() ? uLocale.localeID.substring(parseTagString) : null;
        String createLikelySubtagsString = createLikelySubtagsString(str, str2, str3, null);
        if (isEmptyString(createLikelySubtagsString)) {
            return uLocale;
        }
        if (createLikelySubtagsString(str, null, null, null).equals(createLikelySubtagsString)) {
            return new ULocale(createTagString(str, null, null, substring));
        }
        if (minimize == Minimize.FAVOR_REGION) {
            if (str3.length() != 0 && createLikelySubtagsString(str, null, str3, null).equals(createLikelySubtagsString)) {
                return new ULocale(createTagString(str, null, str3, substring));
            }
            if (str2.length() != 0 && createLikelySubtagsString(str, str2, null, null).equals(createLikelySubtagsString)) {
                return new ULocale(createTagString(str, str2, null, substring));
            }
        } else if (str2.length() != 0 && createLikelySubtagsString(str, str2, null, null).equals(createLikelySubtagsString)) {
            return new ULocale(createTagString(str, str2, null, substring));
        } else {
            if (str3.length() != 0 && createLikelySubtagsString(str, null, str3, null).equals(createLikelySubtagsString)) {
                return new ULocale(createTagString(str, null, str3, substring));
            }
        }
        return uLocale;
    }

    private static boolean isEmptyString(String str) {
        return str == null || str.length() == 0;
    }

    private static void appendTag(String str, StringBuilder sb) {
        if (sb.length() != 0) {
            sb.append(UNDERSCORE);
        }
        sb.append(str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0035  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x005b  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0095  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0098  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00a6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String createTagString(java.lang.String r4, java.lang.String r5, java.lang.String r6, java.lang.String r7, java.lang.String r8) {
        /*
        // Method dump skipped, instructions count: 179
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.ULocale.createTagString(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.lang.String");
    }

    static String createTagString(String str, String str2, String str3, String str4) {
        return createTagString(str, str2, str3, str4, null);
    }

    private static int parseTagString(String str, String[] strArr) {
        LocaleIDParser localeIDParser = new LocaleIDParser(str);
        String language = localeIDParser.getLanguage();
        String script = localeIDParser.getScript();
        String country = localeIDParser.getCountry();
        if (isEmptyString(language)) {
            strArr[0] = UNDEFINED_LANGUAGE;
        } else {
            strArr[0] = language;
        }
        if (script.equals(UNDEFINED_SCRIPT)) {
            strArr[1] = "";
        } else {
            strArr[1] = script;
        }
        if (country.equals(UNDEFINED_REGION)) {
            strArr[2] = "";
        } else {
            strArr[2] = country;
        }
        String variant = localeIDParser.getVariant();
        if (!isEmptyString(variant)) {
            int indexOf = str.indexOf(variant);
            return indexOf > 0 ? indexOf - 1 : indexOf;
        }
        int indexOf2 = str.indexOf(64);
        return indexOf2 == -1 ? str.length() : indexOf2;
    }

    private static String lookupLikelySubtags(String str) {
        try {
            return UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, "likelySubtags").getString(str);
        } catch (MissingResourceException unused) {
            return null;
        }
    }

    private static String createLikelySubtagsString(String str, String str2, String str3, String str4) {
        String lookupLikelySubtags;
        String lookupLikelySubtags2;
        String lookupLikelySubtags3;
        if (!isEmptyString(str2) && !isEmptyString(str3) && (lookupLikelySubtags3 = lookupLikelySubtags(createTagString(str, str2, str3, null))) != null) {
            return createTagString(null, null, null, str4, lookupLikelySubtags3);
        }
        if (!isEmptyString(str2) && (lookupLikelySubtags2 = lookupLikelySubtags(createTagString(str, str2, null, null))) != null) {
            return createTagString(null, null, str3, str4, lookupLikelySubtags2);
        }
        if (!isEmptyString(str3) && (lookupLikelySubtags = lookupLikelySubtags(createTagString(str, null, str3, null))) != null) {
            return createTagString(null, str2, null, str4, lookupLikelySubtags);
        }
        String lookupLikelySubtags4 = lookupLikelySubtags(createTagString(str, null, null, null));
        if (lookupLikelySubtags4 != null) {
            return createTagString(null, str2, str3, str4, lookupLikelySubtags4);
        }
        return null;
    }

    public String getExtension(char c) {
        if (LocaleExtensions.isValidKey(c)) {
            return extensions().getExtensionValue(Character.valueOf(c));
        }
        throw new IllegalArgumentException("Invalid extension key: " + c);
    }

    public Set<Character> getExtensionKeys() {
        return extensions().getKeys();
    }

    public Set<String> getUnicodeLocaleAttributes() {
        return extensions().getUnicodeLocaleAttributes();
    }

    public String getUnicodeLocaleType(String str) {
        if (LocaleExtensions.isValidUnicodeLocaleKey(str)) {
            return extensions().getUnicodeLocaleType(str);
        }
        throw new IllegalArgumentException("Invalid Unicode locale key: " + str);
    }

    public Set<String> getUnicodeLocaleKeys() {
        return extensions().getUnicodeLocaleKeys();
    }

    public String toLanguageTag() {
        BaseLocale base = base();
        LocaleExtensions extensions2 = extensions();
        if (base.getVariant().equalsIgnoreCase("POSIX")) {
            base = BaseLocale.getInstance(base.getLanguage(), base.getScript(), base.getRegion(), "");
            if (extensions2.getUnicodeLocaleType("va") == null) {
                InternalLocaleBuilder internalLocaleBuilder = new InternalLocaleBuilder();
                try {
                    internalLocaleBuilder.setLocale(BaseLocale.ROOT, extensions2);
                    internalLocaleBuilder.setUnicodeLocaleKeyword("va", "posix");
                    extensions2 = internalLocaleBuilder.getLocaleExtensions();
                } catch (LocaleSyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        LanguageTag parseLocale = LanguageTag.parseLocale(base, extensions2);
        StringBuilder sb = new StringBuilder();
        String language = parseLocale.getLanguage();
        if (language.length() > 0) {
            sb.append(LanguageTag.canonicalizeLanguage(language));
        }
        String script = parseLocale.getScript();
        if (script.length() > 0) {
            sb.append(LanguageTag.SEP);
            sb.append(LanguageTag.canonicalizeScript(script));
        }
        String region = parseLocale.getRegion();
        if (region.length() > 0) {
            sb.append(LanguageTag.SEP);
            sb.append(LanguageTag.canonicalizeRegion(region));
        }
        ArrayList arrayList = new ArrayList(parseLocale.getVariants());
        Collections.sort(arrayList);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            sb.append(LanguageTag.SEP);
            sb.append(LanguageTag.canonicalizeVariant((String) it.next()));
        }
        for (String str : parseLocale.getExtensions()) {
            sb.append(LanguageTag.SEP);
            sb.append(LanguageTag.canonicalizeExtension(str));
        }
        String privateuse = parseLocale.getPrivateuse();
        if (privateuse.length() > 0) {
            if (sb.length() > 0) {
                sb.append(LanguageTag.SEP);
            }
            sb.append(LanguageTag.PRIVATEUSE);
            sb.append(LanguageTag.SEP);
            sb.append(LanguageTag.canonicalizePrivateuse(privateuse));
        }
        return sb.toString();
    }

    public static ULocale forLanguageTag(String str) {
        LanguageTag parse = LanguageTag.parse(str, null);
        InternalLocaleBuilder internalLocaleBuilder = new InternalLocaleBuilder();
        internalLocaleBuilder.setLanguageTag(parse);
        return getInstance(internalLocaleBuilder.getBaseLocale(), internalLocaleBuilder.getLocaleExtensions());
    }

    public static String toUnicodeLocaleKey(String str) {
        String bcpKey = KeyTypeData.toBcpKey(str);
        return (bcpKey != null || !UnicodeLocaleExtension.isKey(str)) ? bcpKey : AsciiUtil.toLowerString(str);
    }

    public static String toUnicodeLocaleType(String str, String str2) {
        String bcpType = KeyTypeData.toBcpType(str, str2, null, null);
        return (bcpType != null || !UnicodeLocaleExtension.isType(str2)) ? bcpType : AsciiUtil.toLowerString(str2);
    }

    public static String toLegacyKey(String str) {
        String legacyKey = KeyTypeData.toLegacyKey(str);
        return (legacyKey != null || !str.matches("[0-9a-zA-Z]+")) ? legacyKey : AsciiUtil.toLowerString(str);
    }

    public static String toLegacyType(String str, String str2) {
        String legacyType = KeyTypeData.toLegacyType(str, str2, null, null);
        return (legacyType != null || !str2.matches("[0-9a-zA-Z]+([_/\\-][0-9a-zA-Z]+)*")) ? legacyType : AsciiUtil.toLowerString(str2);
    }

    public static final class Builder {
        private final InternalLocaleBuilder _locbld = new InternalLocaleBuilder();

        public Builder setLocale(ULocale uLocale) {
            try {
                this._locbld.setLocale(uLocale.base(), uLocale.extensions());
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder setLanguageTag(String str) {
            ParseStatus parseStatus = new ParseStatus();
            LanguageTag parse = LanguageTag.parse(str, parseStatus);
            if (!parseStatus.isError()) {
                this._locbld.setLanguageTag(parse);
                return this;
            }
            throw new IllformedLocaleException(parseStatus.getErrorMessage(), parseStatus.getErrorIndex());
        }

        public Builder setLanguage(String str) {
            try {
                this._locbld.setLanguage(str);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder setScript(String str) {
            try {
                this._locbld.setScript(str);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder setRegion(String str) {
            try {
                this._locbld.setRegion(str);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder setVariant(String str) {
            try {
                this._locbld.setVariant(str);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder setExtension(char c, String str) {
            try {
                this._locbld.setExtension(c, str);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder setUnicodeLocaleKeyword(String str, String str2) {
            try {
                this._locbld.setUnicodeLocaleKeyword(str, str2);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder addUnicodeLocaleAttribute(String str) {
            try {
                this._locbld.addUnicodeLocaleAttribute(str);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder removeUnicodeLocaleAttribute(String str) {
            try {
                this._locbld.removeUnicodeLocaleAttribute(str);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder clear() {
            this._locbld.clear();
            return this;
        }

        public Builder clearExtensions() {
            this._locbld.clearExtensions();
            return this;
        }

        public ULocale build() {
            return ULocale.getInstance(this._locbld.getBaseLocale(), this._locbld.getLocaleExtensions());
        }
    }

    /* access modifiers changed from: private */
    public static ULocale getInstance(BaseLocale baseLocale2, LocaleExtensions localeExtensions) {
        String lscvToID = lscvToID(baseLocale2.getLanguage(), baseLocale2.getScript(), baseLocale2.getRegion(), baseLocale2.getVariant());
        Set<Character> keys = localeExtensions.getKeys();
        if (!keys.isEmpty()) {
            TreeMap treeMap = new TreeMap();
            for (Character ch : keys) {
                Extension extension = localeExtensions.getExtension(ch);
                if (extension instanceof UnicodeLocaleExtension) {
                    UnicodeLocaleExtension unicodeLocaleExtension = (UnicodeLocaleExtension) extension;
                    for (String str : unicodeLocaleExtension.getUnicodeLocaleKeys()) {
                        String unicodeLocaleType = unicodeLocaleExtension.getUnicodeLocaleType(str);
                        String legacyKey = toLegacyKey(str);
                        if (unicodeLocaleType.length() == 0) {
                            unicodeLocaleType = "yes";
                        }
                        String legacyType = toLegacyType(str, unicodeLocaleType);
                        if (!legacyKey.equals("va") || !legacyType.equals("posix") || baseLocale2.getVariant().length() != 0) {
                            treeMap.put(legacyKey, legacyType);
                        } else {
                            lscvToID = lscvToID + "_POSIX";
                        }
                    }
                    Set<String> unicodeLocaleAttributes = unicodeLocaleExtension.getUnicodeLocaleAttributes();
                    if (unicodeLocaleAttributes.size() > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (String str2 : unicodeLocaleAttributes) {
                            if (sb.length() > 0) {
                                sb.append(LocaleUtility.IETF_SEPARATOR);
                            }
                            sb.append(str2);
                        }
                        treeMap.put("attribute", sb.toString());
                    }
                } else {
                    treeMap.put(String.valueOf(ch), extension.getValue());
                }
            }
            if (!treeMap.isEmpty()) {
                StringBuilder sb2 = new StringBuilder(lscvToID);
                sb2.append("@");
                boolean z = false;
                for (Map.Entry entry : treeMap.entrySet()) {
                    if (z) {
                        sb2.append(";");
                    } else {
                        z = true;
                    }
                    sb2.append((String) entry.getKey());
                    sb2.append("=");
                    sb2.append((String) entry.getValue());
                }
                lscvToID = sb2.toString();
            }
        }
        return new ULocale(lscvToID);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private BaseLocale base() {
        String str;
        String str2;
        String str3;
        if (this.baseLocale == null) {
            String str4 = "";
            if (!equals(ROOT)) {
                LocaleIDParser localeIDParser = new LocaleIDParser(this.localeID);
                String language = localeIDParser.getLanguage();
                str2 = localeIDParser.getScript();
                str = localeIDParser.getCountry();
                str3 = localeIDParser.getVariant();
                str4 = language;
            } else {
                str3 = str4;
                str2 = str3;
                str = str2;
            }
            this.baseLocale = BaseLocale.getInstance(str4, str2, str, str3);
        }
        return this.baseLocale;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private LocaleExtensions extensions() {
        if (this.extensions == null) {
            Iterator<String> keywords = getKeywords();
            if (keywords == null) {
                this.extensions = LocaleExtensions.EMPTY_EXTENSIONS;
            } else {
                InternalLocaleBuilder internalLocaleBuilder = new InternalLocaleBuilder();
                while (keywords.hasNext()) {
                    String next = keywords.next();
                    if (next.equals("attribute")) {
                        for (String str : getKeywordValue(next).split("[-_]")) {
                            try {
                                internalLocaleBuilder.addUnicodeLocaleAttribute(str);
                            } catch (LocaleSyntaxException unused) {
                            }
                        }
                    } else if (next.length() >= 2) {
                        String unicodeLocaleKey = toUnicodeLocaleKey(next);
                        String unicodeLocaleType = toUnicodeLocaleType(next, getKeywordValue(next));
                        if (!(unicodeLocaleKey == null || unicodeLocaleType == null)) {
                            try {
                                internalLocaleBuilder.setUnicodeLocaleKeyword(unicodeLocaleKey, unicodeLocaleType);
                            } catch (LocaleSyntaxException unused2) {
                            }
                        }
                    } else if (next.length() == 1 && next.charAt(0) != 'u') {
                        internalLocaleBuilder.setExtension(next.charAt(0), getKeywordValue(next).replace("_", LanguageTag.SEP));
                    }
                }
                this.extensions = internalLocaleBuilder.getLocaleExtensions();
            }
        }
        return this.extensions;
    }

    /* access modifiers changed from: private */
    public static final class JDKLocaleHelper {
        private static Object eDISPLAY = null;
        private static Object eFORMAT = null;
        private static boolean hasLocaleCategories = true;
        private static Method mGetDefault;
        private static Method mSetDefault;

        /* JADX DEBUG: Multi-variable search result rejected for r6v3, resolved type: java.lang.Object[] */
        /* JADX WARN: Multi-variable type inference failed */
        static {
            Class<?> cls;
            try {
                Class<?>[] declaredClasses = Locale.class.getDeclaredClasses();
                int length = declaredClasses.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        cls = null;
                        break;
                    }
                    cls = declaredClasses[i];
                    if (cls.getName().equals("java.util.Locale$Category")) {
                        break;
                    }
                    i++;
                }
                if (cls != null) {
                    mGetDefault = Locale.class.getDeclaredMethod("getDefault", cls);
                    mSetDefault = Locale.class.getDeclaredMethod("setDefault", cls, Locale.class);
                    Method method = cls.getMethod("name", null);
                    Object[] enumConstants = cls.getEnumConstants();
                    for (Object obj : enumConstants) {
                        String str = (String) method.invoke(obj, null);
                        if (str.equals("DISPLAY")) {
                            eDISPLAY = obj;
                        } else if (str.equals("FORMAT")) {
                            eFORMAT = obj;
                        }
                    }
                    if (eDISPLAY == null) {
                        return;
                    }
                    if (eFORMAT != null) {
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException unused) {
            }
        }

        private JDKLocaleHelper() {
        }

        public static boolean hasLocaleCategories() {
            return hasLocaleCategories;
        }

        public static ULocale toULocale(Locale locale) {
            TreeSet<String> treeSet;
            TreeMap treeMap;
            String language = locale.getLanguage();
            String country = locale.getCountry();
            String variant = locale.getVariant();
            String script = locale.getScript();
            Set<Character> extensionKeys = locale.getExtensionKeys();
            if (!extensionKeys.isEmpty()) {
                treeMap = null;
                treeSet = null;
                for (Character ch : extensionKeys) {
                    if (ch.charValue() == 'u') {
                        Set<String> unicodeLocaleAttributes = locale.getUnicodeLocaleAttributes();
                        if (!unicodeLocaleAttributes.isEmpty()) {
                            treeSet = new TreeSet();
                            for (String str : unicodeLocaleAttributes) {
                                treeSet.add(str);
                            }
                        }
                        for (String str2 : locale.getUnicodeLocaleKeys()) {
                            String unicodeLocaleType = locale.getUnicodeLocaleType(str2);
                            if (unicodeLocaleType != null) {
                                if (!str2.equals("va")) {
                                    if (treeMap == null) {
                                        treeMap = new TreeMap();
                                    }
                                    treeMap.put(str2, unicodeLocaleType);
                                } else if (variant.length() == 0) {
                                    variant = unicodeLocaleType;
                                } else {
                                    variant = unicodeLocaleType + "_" + variant;
                                }
                            }
                        }
                    } else {
                        String extension = locale.getExtension(ch.charValue());
                        if (extension != null) {
                            if (treeMap == null) {
                                treeMap = new TreeMap();
                            }
                            treeMap.put(String.valueOf(ch), extension);
                        }
                    }
                }
            } else {
                treeMap = null;
                treeSet = null;
            }
            if (language.equals(IndexType.NO) && country.equals("NO") && variant.equals("NY")) {
                language = "nn";
                variant = "";
            }
            StringBuilder sb = new StringBuilder(language);
            if (script.length() > 0) {
                sb.append(ULocale.UNDERSCORE);
                sb.append(script);
            }
            if (country.length() > 0) {
                sb.append(ULocale.UNDERSCORE);
                sb.append(country);
            }
            if (variant.length() > 0) {
                if (country.length() == 0) {
                    sb.append(ULocale.UNDERSCORE);
                }
                sb.append(ULocale.UNDERSCORE);
                sb.append(variant);
            }
            if (treeSet != null) {
                StringBuilder sb2 = new StringBuilder();
                for (String str3 : treeSet) {
                    if (sb2.length() != 0) {
                        sb2.append(LocaleUtility.IETF_SEPARATOR);
                    }
                    sb2.append(str3);
                }
                if (treeMap == null) {
                    treeMap = new TreeMap();
                }
                treeMap.put("attribute", sb2.toString());
            }
            if (treeMap != null) {
                sb.append('@');
                boolean z = false;
                for (Map.Entry entry : treeMap.entrySet()) {
                    String str4 = (String) entry.getKey();
                    String str5 = (String) entry.getValue();
                    if (str4.length() != 1) {
                        str4 = ULocale.toLegacyKey(str4);
                        if (str5.length() == 0) {
                            str5 = "yes";
                        }
                        str5 = ULocale.toLegacyType(str4, str5);
                    }
                    if (z) {
                        sb.append(TelephoneNumberUtils.WAIT);
                    } else {
                        z = true;
                    }
                    sb.append(str4);
                    sb.append('=');
                    sb.append(str5);
                }
            }
            return new ULocale(ULocale.getName(sb.toString()), locale);
        }

        public static Locale toLocale(ULocale uLocale) {
            Locale locale;
            String name = uLocale.getName();
            if (uLocale.getScript().length() > 0 || name.contains("@")) {
                locale = Locale.forLanguageTag(AsciiUtil.toUpperString(uLocale.toLanguageTag()));
            } else {
                locale = null;
            }
            return locale == null ? new Locale(uLocale.getLanguage(), uLocale.getCountry(), uLocale.getVariant()) : locale;
        }

        public static Locale getDefault(Category category) {
            Object obj;
            if (hasLocaleCategories) {
                int i = AnonymousClass3.$SwitchMap$ohos$global$icu$util$ULocale$Category[category.ordinal()];
                if (i == 1) {
                    obj = eDISPLAY;
                } else if (i != 2) {
                    obj = null;
                } else {
                    obj = eFORMAT;
                }
                if (obj != null) {
                    try {
                        return (Locale) mGetDefault.invoke(null, obj);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException unused) {
                    }
                }
            }
            return Locale.getDefault();
        }

        public static void setDefault(Category category, Locale locale) {
            Object obj;
            if (hasLocaleCategories) {
                int i = AnonymousClass3.$SwitchMap$ohos$global$icu$util$ULocale$Category[category.ordinal()];
                if (i == 1) {
                    obj = eDISPLAY;
                } else if (i != 2) {
                    obj = null;
                } else {
                    obj = eFORMAT;
                }
                if (obj != null) {
                    try {
                        mSetDefault.invoke(null, obj, locale);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException unused) {
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.global.icu.util.ULocale$3  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$util$ULocale$Category = new int[Category.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            /*
                ohos.global.icu.util.ULocale$Category[] r0 = ohos.global.icu.util.ULocale.Category.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.global.icu.util.ULocale.AnonymousClass3.$SwitchMap$ohos$global$icu$util$ULocale$Category = r0
                int[] r0 = ohos.global.icu.util.ULocale.AnonymousClass3.$SwitchMap$ohos$global$icu$util$ULocale$Category     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.global.icu.util.ULocale$Category r1 = ohos.global.icu.util.ULocale.Category.DISPLAY     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.global.icu.util.ULocale.AnonymousClass3.$SwitchMap$ohos$global$icu$util$ULocale$Category     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.global.icu.util.ULocale$Category r1 = ohos.global.icu.util.ULocale.Category.FORMAT     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.ULocale.AnonymousClass3.<clinit>():void");
        }
    }
}
