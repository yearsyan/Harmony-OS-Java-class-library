package ohos.global.i18n.text;

import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import ohos.agp.styles.attributes.TimePickerAttrsConstants;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import ohos.com.sun.org.apache.xpath.internal.XPath;
import ohos.global.i18n.text.MeasureOptions;
import ohos.global.icu.impl.ICUData;
import ohos.global.icu.impl.ICUResourceBundle;
import ohos.global.icu.util.LocaleData;
import ohos.global.icu.util.ULocale;
import ohos.global.icu.util.UResourceBundle;
import ohos.global.icu.util.UResourceBundleIterator;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MeasureData {
    private static final double FT2_TO_M2 = 0.09290304d;
    private static final double FT3_TO_M3 = 0.028316846592000004d;
    private static final double FT_TO_M = 0.3048d;
    private static final double GAL_IMP_TO_M3 = 0.00454609d;
    private static final double GAL_TO_M3 = 0.0037854117840000006d;
    private static final double IN3_TO_M3 = 1.6387064000000003E-5d;
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218111488, "MeasureData");
    private HashMap<String, ConvertItem[]> convertRules = new HashMap<>();
    private HashMap<String, HashMap<String, String[]>> defaultPreferedUnits = new HashMap<>();
    private UResourceBundle measregionBundle = null;
    private LocaleData.MeasurementSystem measureSys;
    private UResourceBundle measurementData = null;
    private String regionCode;
    private UResourceBundle resourceBundle = null;
    private HashMap<String, HashMap<String, String>> usageData = new HashMap<>();

    private MeasureData(Locale locale) {
        if (locale == null) {
            this.regionCode = Locale.US.getCountry();
        } else {
            this.regionCode = locale.getCountry();
        }
        try {
            if (this.resourceBundle == null) {
                this.resourceBundle = UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
            }
            this.measurementData = this.resourceBundle.get("unitPreferenceData");
            try {
                this.measregionBundle = this.measurementData.get(this.regionCode);
            } catch (MissingResourceException unused) {
                this.measregionBundle = this.measurementData.get("001");
            }
        } catch (MissingResourceException e) {
            HiLog.error(LABEL, "Cannot find unit resources. %{public}s", e.getMessage());
        }
        this.measureSys = LocaleData.getMeasurementSystem(ULocale.forLocale(locale));
        initConvertRulesPartOne();
        initConvertRulesPartTwo();
        initDefaultPreferedUnits();
        initUsageDataPartOne();
        initUsageDataPartTwo();
        initUsageDataPartThree();
        initUsageDataPartFour();
    }

    private void initUsageDataPartOne() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("area-land-agricult", "hectare");
        hashMap.put("area-land-commercl", "hectare");
        hashMap.put("area-land-residntl", "hectare");
        hashMap.put("length-person", "centimeter");
        hashMap.put("length-person-small", "centimeter");
        hashMap.put("length-rainfall", "millimeter");
        hashMap.put("length-road", "kilometer");
        hashMap.put("length-road-small", "meter");
        hashMap.put("length-snowfall", "centimeter");
        hashMap.put("length-vehicle", "meter");
        hashMap.put("length-visiblty", "kilometer");
        hashMap.put("length-visiblty-small", "meter");
        hashMap.put("speed-road-travel", "kilometer-per-hour");
        hashMap.put("speed-wind", "kilometer-per-hour");
        hashMap.put("temperature-person", "celsius");
        hashMap.put("temperature-weather", "celsius");
        hashMap.put("volume-vehicle-fuel", "liter");
        this.usageData.put("001", hashMap);
        HashMap<String, String> hashMap2 = new HashMap<>();
        hashMap2.put("length-person", "meter centimeter");
        this.usageData.put("AT", hashMap2);
        HashMap<String, String> hashMap3 = new HashMap<>();
        hashMap3.put("length-person", "meter centimeter");
        this.usageData.put("BE", hashMap3);
        HashMap<String, String> hashMap4 = new HashMap<>();
        hashMap4.put("length-person-informal", "meter centimeter");
        hashMap4.put("length-rainfall", "centimeter");
        this.usageData.put("BR", hashMap4);
        HashMap<String, String> hashMap5 = new HashMap<>();
        hashMap5.put("temperature-weather", "fahrenheit");
        this.usageData.put("BS", hashMap5);
        HashMap<String, String> hashMap6 = new HashMap<>();
        hashMap6.put("temperature-weather", "fahrenheit");
        this.usageData.put("BZ", hashMap6);
        HashMap<String, String> hashMap7 = new HashMap<>();
        hashMap7.put("length-person-informal", "foot inch");
        hashMap7.put("length-person-small-informal", "inch");
        this.usageData.put("CA", hashMap7);
        HashMap<String, String> hashMap8 = new HashMap<>();
        hashMap8.put("length-person-informal", "meter centimeter");
        this.usageData.put("CN", hashMap8);
        HashMap<String, String> hashMap9 = new HashMap<>();
        hashMap9.put("length-person-informal", "meter centimeter");
        hashMap9.put("length-visiblty", "meter");
        this.usageData.put("DE", hashMap9);
        HashMap<String, String> hashMap10 = new HashMap<>();
        hashMap10.put("length-person-informal", "meter centimeter");
        this.usageData.put("DK", hashMap10);
    }

    private void initUsageDataPartTwo() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("length-person", "meter centimeter");
        this.usageData.put("DZ", hashMap);
        HashMap<String, String> hashMap2 = new HashMap<>();
        hashMap2.put("length-person", "meter centimeter");
        this.usageData.put("EG", hashMap2);
        HashMap<String, String> hashMap3 = new HashMap<>();
        hashMap3.put("length-person", "meter centimeter");
        this.usageData.put("ES", hashMap3);
        HashMap<String, String> hashMap4 = new HashMap<>();
        hashMap4.put("length-person", "meter centimeter");
        this.usageData.put("FR", hashMap4);
        HashMap<String, String> hashMap5 = new HashMap<>();
        hashMap5.put("area-land-agricult", "acre");
        hashMap5.put("area-land-commercl", "acre");
        hashMap5.put("area-land-residntl", "acre");
        hashMap5.put("length-person-informal", "foot inch");
        hashMap5.put("length-person-small-informal", "inch");
        hashMap5.put("length-road", "mile");
        hashMap5.put("length-road-small", "yard");
        hashMap5.put("length-vehicle", "foot inch");
        hashMap5.put("length-visiblty", "mile");
        hashMap5.put("length-visiblty-small", "foot");
        hashMap5.put("speed-road-travel", "mile-per-hour");
        this.usageData.put("GB", hashMap5);
        HashMap<String, String> hashMap6 = new HashMap<>();
        hashMap6.put("length-person", "meter centimeter");
        this.usageData.put("HK", hashMap6);
        HashMap<String, String> hashMap7 = new HashMap<>();
        hashMap7.put("length-person", "meter centimeter");
        this.usageData.put(SchemaSymbols.ATTVAL_ID, hashMap7);
        HashMap<String, String> hashMap8 = new HashMap<>();
        hashMap8.put("length-person", "meter centimeter");
        this.usageData.put("IL", hashMap8);
        HashMap<String, String> hashMap9 = new HashMap<>();
        hashMap9.put("length-person-informal", "foot inch");
        hashMap9.put("length-person-small-informal", "inch");
        this.usageData.put("IN", hashMap9);
        HashMap<String, String> hashMap10 = new HashMap<>();
        hashMap10.put("length-person", "meter centimeter");
        this.usageData.put("IT", hashMap10);
        HashMap<String, String> hashMap11 = new HashMap<>();
        hashMap11.put("length-person", "meter centimeter");
        this.usageData.put("JO", hashMap11);
        HashMap<String, String> hashMap12 = new HashMap<>();
        hashMap12.put("speed-wind", "meter-per-second");
        this.usageData.put("KR", hashMap12);
    }

    private void initUsageDataPartThree() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("temperature-weather", "fahrenheit");
        this.usageData.put("KY", hashMap);
        HashMap<String, String> hashMap2 = new HashMap<>();
        hashMap2.put("length-person-informal", "meter centimeter");
        hashMap2.put("length-vehicle", "meter centimeter");
        this.usageData.put("MX", hashMap2);
        HashMap<String, String> hashMap3 = new HashMap<>();
        hashMap3.put("length-person", "meter centimeter");
        this.usageData.put("MY", hashMap3);
        HashMap<String, String> hashMap4 = new HashMap<>();
        hashMap4.put("length-person-informal", "meter centimeter");
        hashMap4.put("length-visiblty", "meter");
        this.usageData.put("NL", hashMap4);
        HashMap<String, String> hashMap5 = new HashMap<>();
        hashMap5.put("length-person-informal", "meter centimeter");
        hashMap5.put("speed-wind", "meter-per-second");
        this.usageData.put("NO", hashMap5);
        HashMap<String, String> hashMap6 = new HashMap<>();
        hashMap6.put("length-person-informal", "meter centimeter");
        hashMap6.put("speed-wind", "meter-per-second");
        this.usageData.put("PL", hashMap6);
        HashMap<String, String> hashMap7 = new HashMap<>();
        hashMap7.put("temperature-weather", "fahrenheit");
        this.usageData.put("PR", hashMap7);
        HashMap<String, String> hashMap8 = new HashMap<>();
        hashMap8.put("length-person-informal", "meter centimeter");
        this.usageData.put("PT", hashMap8);
        HashMap<String, String> hashMap9 = new HashMap<>();
        hashMap9.put("temperature-weather", "fahrenheit");
        this.usageData.put("PW", hashMap9);
        HashMap<String, String> hashMap10 = new HashMap<>();
        hashMap10.put("length-person-informal", "meter centimeter");
        hashMap10.put("speed-wind", "meter-per-second");
        this.usageData.put("RU", hashMap10);
        HashMap<String, String> hashMap11 = new HashMap<>();
        hashMap11.put("length-person", "meter centimeter");
        this.usageData.put("SA", hashMap11);
        HashMap<String, String> hashMap12 = new HashMap<>();
        hashMap12.put("length-person", "meter centimeter");
        hashMap12.put("length-road-informal", "mile-scandinavian");
        hashMap12.put("speed-wind", "meter-per-second");
        this.usageData.put("SE", hashMap12);
        HashMap<String, String> hashMap13 = new HashMap<>();
        hashMap13.put("length-person", "meter centimeter");
        this.usageData.put("TR", hashMap13);
    }

    private void initUsageDataPartFour() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("area-land-agricult", "acre");
        hashMap.put("area-land-commercl", "acre");
        hashMap.put("area-land-residntl", "acre");
        hashMap.put("length-person", "inch");
        hashMap.put("length-person-informal", "foot inch");
        hashMap.put("length-person-small", "inch");
        hashMap.put("length-rainfall", "inch");
        hashMap.put("length-road", "mile");
        hashMap.put("length-road-small", "foot");
        hashMap.put("length-snowfall", "inch");
        hashMap.put("length-vehicle", "foot inch");
        hashMap.put("length-visiblty", "mile");
        hashMap.put("length-visiblty-small", "foot");
        hashMap.put("speed-road-travel", "mile-per-hour");
        hashMap.put("speed-wind", "mile-per-hour");
        hashMap.put("temperature-person", "fahrenheit");
        hashMap.put("temperature-weather", "fahrenheit");
        hashMap.put("volume-vehicle-fuel", "gallon");
        this.usageData.put("US", hashMap);
        HashMap<String, String> hashMap2 = new HashMap<>();
        hashMap2.put("length-person", "meter centimeter");
        this.usageData.put("VN", hashMap2);
    }

    private void initDefaultPreferedUnits() {
        HashMap<String, String[]> hashMap = new HashMap<>();
        hashMap.put("001", new String[]{"kilometer", "meter", "centimeter"});
        hashMap.put("GB", new String[]{"mile", "foot", "inch"});
        hashMap.put("US", new String[]{"mile", "foot", "inch"});
        this.defaultPreferedUnits.put("length", hashMap);
        HashMap<String, String[]> hashMap2 = new HashMap<>();
        hashMap2.put("001", new String[]{"square-kilometer", "hectare", "square-meter", "square-centimeter"});
        hashMap2.put("GB", new String[]{"square-mile", "acre", "square-foot", "square-inch"});
        hashMap2.put("US", new String[]{"square-mile", "acre", "square-foot", "square-inch"});
        this.defaultPreferedUnits.put("area", hashMap2);
        HashMap<String, String[]> hashMap3 = new HashMap<>();
        hashMap3.put("001", new String[]{"kilometer-per-hour"});
        hashMap3.put("GB", new String[]{"mile-per-hour"});
        hashMap3.put("US", new String[]{"mile-per-hour"});
        this.defaultPreferedUnits.put("speed", hashMap3);
        HashMap<String, String[]> hashMap4 = new HashMap<>();
        hashMap4.put("001", new String[]{"celsius"});
        hashMap4.put("US", new String[]{"fahrenheit"});
        this.defaultPreferedUnits.put("temperature", hashMap4);
        HashMap<String, String[]> hashMap5 = new HashMap<>();
        hashMap5.put("001", new String[]{"cubic-meter", "cubic-centimeter"});
        hashMap5.put("GB", new String[]{"cubic-foot", "cubic-inch"});
        hashMap5.put("US", new String[]{"cubic-foot", "cubic-inch"});
        this.defaultPreferedUnits.put("volume", hashMap5);
    }

    private void initConvertRulesPartOne() {
        this.convertRules.put("acre", new ConvertItem[]{new ConvertItem(4046.8564224d, XPath.MATCH_SCORE_QNAME, "square-meter", LocaleData.MeasurementSystem.UK)});
        this.convertRules.put("hectare", new ConvertItem[]{new ConvertItem(10000.0d, XPath.MATCH_SCORE_QNAME, "square-meter", LocaleData.MeasurementSystem.SI)});
        this.convertRules.put("dunam", new ConvertItem[]{new ConvertItem(1000.0d, XPath.MATCH_SCORE_QNAME, "square-meter", LocaleData.MeasurementSystem.SI)});
        this.convertRules.put("fathom", new ConvertItem[]{new ConvertItem(1.8288000000000002d, XPath.MATCH_SCORE_QNAME, "meter", LocaleData.MeasurementSystem.UK)});
        this.convertRules.put("foot", new ConvertItem[]{new ConvertItem(FT_TO_M, XPath.MATCH_SCORE_QNAME, "meter", LocaleData.MeasurementSystem.UK)});
        this.convertRules.put("furlong", new ConvertItem[]{new ConvertItem(201.168d, XPath.MATCH_SCORE_QNAME, "meter", LocaleData.MeasurementSystem.UK)});
        this.convertRules.put("inch", new ConvertItem[]{new ConvertItem(0.025400000000000002d, XPath.MATCH_SCORE_QNAME, "meter", LocaleData.MeasurementSystem.UK)});
        this.convertRules.put("meter", new ConvertItem[]{new ConvertItem(1.0d, XPath.MATCH_SCORE_QNAME, "meter", LocaleData.MeasurementSystem.SI)});
        this.convertRules.put("mile", new ConvertItem[]{new ConvertItem(1609.344d, XPath.MATCH_SCORE_QNAME, "meter", LocaleData.MeasurementSystem.UK)});
        this.convertRules.put("mile-scandinavian", new ConvertItem[]{new ConvertItem(10000.0d, XPath.MATCH_SCORE_QNAME, "meter", LocaleData.MeasurementSystem.UK)});
        this.convertRules.put("nautical-mile", new ConvertItem[]{new ConvertItem(1852.0d, XPath.MATCH_SCORE_QNAME, "meter", LocaleData.MeasurementSystem.UK)});
        this.convertRules.put("point", new ConvertItem[]{new ConvertItem(3.527777777777778E-4d, XPath.MATCH_SCORE_QNAME, "meter", LocaleData.MeasurementSystem.UK)});
        this.convertRules.put("yard", new ConvertItem[]{new ConvertItem(0.9144000000000001d, XPath.MATCH_SCORE_QNAME, "meter", LocaleData.MeasurementSystem.UK)});
        this.convertRules.put("knot", new ConvertItem[]{new ConvertItem(0.5144444444444445d, XPath.MATCH_SCORE_QNAME, "meter-per-second", LocaleData.MeasurementSystem.SI)});
        this.convertRules.put("celsius", new ConvertItem[]{new ConvertItem(1.0d, 273.15d, "kelvin", LocaleData.MeasurementSystem.SI)});
        this.convertRules.put("fahrenheit", new ConvertItem[]{new ConvertItem(0.5555555555555556d, 255.3722222222222d, "kelvin", LocaleData.MeasurementSystem.SI)});
        this.convertRules.put("kelvin", new ConvertItem[]{new ConvertItem(1.0d, XPath.MATCH_SCORE_QNAME, "kelvin", LocaleData.MeasurementSystem.SI)});
        this.convertRules.put("acre-foot", new ConvertItem[]{new ConvertItem(1233.4818375475202d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.US)});
        this.convertRules.put("bushel", new ConvertItem[]{new ConvertItem(0.03636872d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.UK), new ConvertItem(0.03523907016688001d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.US)});
    }

    private void initConvertRulesPartTwo() {
        this.convertRules.put("cup", new ConvertItem[]{new ConvertItem(2.84130625E-4d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.UK), new ConvertItem(2.3658823650000004E-4d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.US)});
        this.convertRules.put("cup-metric", new ConvertItem[]{new ConvertItem(2.5E-4d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.SI)});
        this.convertRules.put("fluid-ounce", new ConvertItem[]{new ConvertItem(2.9573529562500005E-5d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.US)});
        this.convertRules.put("fluid-ounce-imperial", new ConvertItem[]{new ConvertItem(2.84130625E-5d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.UK)});
        this.convertRules.put("gallon", new ConvertItem[]{new ConvertItem(GAL_TO_M3, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.US)});
        this.convertRules.put("gallon-imperial", new ConvertItem[]{new ConvertItem(GAL_IMP_TO_M3, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.UK)});
        this.convertRules.put("liter", new ConvertItem[]{new ConvertItem(0.001d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.SI)});
        this.convertRules.put("pint", new ConvertItem[]{new ConvertItem(5.6826125E-4d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.UK), new ConvertItem(4.7317647300000007E-4d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.US)});
        this.convertRules.put("pint-metric", new ConvertItem[]{new ConvertItem(5.0E-4d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.UK)});
        this.convertRules.put("quart", new ConvertItem[]{new ConvertItem(0.0011365225d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.UK), new ConvertItem(9.463529460000001E-4d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.US)});
        this.convertRules.put("tablespoon", new ConvertItem[]{new ConvertItem(1.77581640625E-5d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.UK), new ConvertItem(1.4786764781250002E-5d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.US)});
        this.convertRules.put("teaspoon", new ConvertItem[]{new ConvertItem(5.919388020833334E-6d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.UK), new ConvertItem(4.9289215937500005E-6d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.US)});
        this.convertRules.put("barrel", new ConvertItem[]{new ConvertItem(0.16365924d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.UK), new ConvertItem(0.158987294928d, XPath.MATCH_SCORE_QNAME, "cubic-meter", LocaleData.MeasurementSystem.US)});
        this.convertRules.put(TimePickerAttrsConstants.HOUR, new ConvertItem[]{new ConvertItem(3600.0d, XPath.MATCH_SCORE_QNAME, TimePickerAttrsConstants.SECOND, LocaleData.MeasurementSystem.SI)});
    }

    public static MeasureData getInstance(Locale locale) {
        return new MeasureData(locale);
    }

    public LocaleData.MeasurementSystem getMeasurementSystem() {
        return this.measureSys;
    }

    public HashMap<String, ConvertItem[]> getConvertRules() {
        return this.convertRules;
    }

    public String[] getPreferedUnits(MeasureOptions.Usage usage, String str) {
        if (!"default".equals(usage.getUsageType()) || this.defaultPreferedUnits.get(str) == null) {
            String unitWithSpecifiedUsage = getUnitWithSpecifiedUsage(usage, this.measregionBundle);
            if ("".equals(unitWithSpecifiedUsage)) {
                this.measregionBundle = this.measurementData.get("001");
                unitWithSpecifiedUsage = getUnitWithSpecifiedUsage(usage, this.measregionBundle);
            }
            return unitWithSpecifiedUsage.replaceAll("-and-", " ").split(" ");
        } else if (this.defaultPreferedUnits.get(str).get(this.regionCode) != null) {
            return this.defaultPreferedUnits.get(str).get(this.regionCode);
        } else {
            return this.defaultPreferedUnits.get(str).get("001");
        }
    }

    private String getUnitWithSpecifiedUsage(MeasureOptions.Usage usage, UResourceBundle uResourceBundle) {
        UResourceBundleIterator iterator = uResourceBundle.getIterator();
        while (iterator.hasNext()) {
            UResourceBundle next = iterator.next();
            int type = next.getType();
            if (next.getKey() != null && next.getKey().equals(usage.getUsageName()) && type == 0) {
                return next.getString();
            }
        }
        if (this.usageData.containsKey(this.regionCode)) {
            return getUnitWithSpecifiedUsageAndRegion(this.regionCode, usage.getUsageName());
        }
        return getUnitWithSpecifiedUsageAndRegion("001", usage.getUsageName());
    }

    private String getUnitWithSpecifiedUsageAndRegion(String str, String str2) {
        if (this.usageData.get(str).containsKey(str2)) {
            return this.usageData.get(str).get(str2);
        }
        return this.usageData.get("001").containsKey(str2) ? this.usageData.get("001").get(str2) : "";
    }

    public static class ConvertItem {
        private double factor;
        private LocaleData.MeasurementSystem measureSys;
        private double offset;
        private String target;

        public ConvertItem(double d, double d2, String str, LocaleData.MeasurementSystem measurementSystem) {
            this.factor = d;
            this.offset = d2;
            this.target = str;
            this.measureSys = measurementSystem;
        }

        public double getFactor() {
            return this.factor;
        }

        public double getOffset() {
            return this.offset;
        }

        public String getTarget() {
            return this.target;
        }

        public LocaleData.MeasurementSystem getMeasurementSystem() {
            return this.measureSys;
        }
    }
}
