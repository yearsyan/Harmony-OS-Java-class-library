package ohos.global.i18n.text;

import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeMap;
import ohos.com.sun.org.apache.xpath.internal.XPath;
import ohos.global.i18n.text.MeasureOptions;
import ohos.global.icu.text.MeasureFormat;
import ohos.global.icu.text.NumberFormat;
import ohos.global.icu.util.LocaleData;
import ohos.global.icu.util.Measure;
import ohos.global.icu.util.ULocale;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MeasureFormatterImpl extends MeasureFormatter {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218111488, "MeasureFormatterImpl");
    private Locale locale;
    private MeasureConvert measConvert;
    private MeasureData measData;
    private int precision = 3;

    public MeasureFormatterImpl(Locale locale2) {
        if (locale2 == null) {
            this.locale = Locale.US;
        } else {
            this.locale = locale2;
        }
        initMeasureData();
    }

    private void initMeasureData() {
        this.measData = MeasureData.getInstance(this.locale);
        if (this.measData == null) {
            this.measConvert = new MeasureConvert(MeasureData.getInstance(Locale.US));
        }
        this.measConvert = new MeasureConvert(this.measData);
    }

    @Override // ohos.global.i18n.text.MeasureFormatter
    public void setFractionPrecision(int i) {
        if (i >= 0) {
            this.precision = i;
        } else {
            HiLog.error(LABEL, "Not valid precision value.", new Object[0]);
        }
    }

    @Override // ohos.global.i18n.text.MeasureFormatter
    public String format(MeasureOptions.Unit unit, double d, MeasureOptions.Usage usage, MeasureOptions.FormatStyle formatStyle, MeasureOptions.Style style) throws NotSupportConvertException {
        if (unit == null || usage == null || formatStyle == null || style == null || (!usage.getUsageType().equals(unit.getUnitType()) && !MeasureOptions.Usage.DEFAULT.equals(usage))) {
            throw new NotSupportConvertException("Cannot format invalid value.");
        }
        String[] preferedUnits = this.measData.getPreferedUnits(usage, unit.getUnitType());
        if (style == MeasureOptions.Style.AUTO_STYLE_OFF) {
            for (String str : preferedUnits) {
                if (unit.getUnitName().equals(str)) {
                    return format(unit, d, formatStyle);
                }
            }
            MeasureOptions.Unit[] values = MeasureOptions.Unit.values();
            for (MeasureOptions.Unit unit2 : values) {
                if ((unit2.getUnitType().equals(usage.getUsageType()) || MeasureOptions.Usage.DEFAULT.equals(usage)) && preferedUnits.length > 0 && unit2.getUnitName().equals(preferedUnits[0])) {
                    return format(unit2, this.measConvert.convert(unit, unit2, d), formatStyle);
                }
            }
            return format(unit, d, formatStyle);
        } else if (preferedUnits.length != 1 || !unit.getUnitName().equals(preferedUnits[0])) {
            return selectPreferedUnitsAndAutoFormat(preferedUnits, unit, usage, d, formatStyle);
        } else {
            return format(unit, d, formatStyle);
        }
    }

    private String selectPreferedUnitsAndAutoFormat(String[] strArr, MeasureOptions.Unit unit, MeasureOptions.Usage usage, double d, MeasureOptions.FormatStyle formatStyle) {
        ArrayList<MeasureOptions.Unit> arrayList = new ArrayList<>();
        ArrayList<MeasureOptions.Unit> arrayList2 = new ArrayList<>();
        LocaleData.MeasurementSystem measurementSystem = this.measData.getMeasurementSystem();
        for (String str : strArr) {
            MeasureOptions.Unit[] values = MeasureOptions.Unit.values();
            for (MeasureOptions.Unit unit2 : values) {
                if ((unit2.getUnitType().equals(usage.getUsageType()) || "default".equals(usage.getUsageType())) && unit2.getUnitName().equals(str) && unit2.getMeasurementSystem().equals(measurementSystem)) {
                    arrayList.add(unit2);
                }
                if ((unit2.getUnitType().equals(usage.getUsageType()) || "default".equals(usage.getUsageType())) && unit2.getUnitName().equals(str) && !unit2.getMeasurementSystem().equals(measurementSystem)) {
                    arrayList2.add(unit2);
                }
            }
        }
        return autoStyleMeasure(arrayList, arrayList2, formatStyle, unit, d);
    }

    @Override // ohos.global.i18n.text.MeasureFormatter
    public String format(MeasureOptions.Unit unit, double d, MeasureOptions.Unit unit2, MeasureOptions.FormatStyle formatStyle) throws NotSupportConvertException {
        if (unit == null || unit2 == null || formatStyle == null || unit.getUnitType() != unit2.getUnitType()) {
            throw new NotSupportConvertException("The conversion is not supported!");
        }
        try {
            return format(unit2, this.measConvert.convert(unit, unit2, d), formatStyle);
        } catch (NotSupportConvertException unused) {
            HiLog.error(LABEL, "The conversion is not supported!", new Object[0]);
            return "";
        }
    }

    @Override // ohos.global.i18n.text.MeasureFormatter
    public String format(MeasureOptions.Unit unit, double d, MeasureOptions.FormatStyle formatStyle) {
        if (unit == null || formatStyle == null) {
            HiLog.error(LABEL, "Cannot format invalid value.", new Object[0]);
            return "";
        }
        NumberFormat instance = NumberFormat.getInstance(this.locale);
        instance.setMaximumFractionDigits(this.precision);
        return MeasureFormat.getInstance(ULocale.forLocale(this.locale), formatStyle.getFormatWidth(), instance).format(new Measure(Double.valueOf(d), unit.getMeasureUnit()));
    }

    private String autoStyleMeasure(ArrayList<MeasureOptions.Unit> arrayList, ArrayList<MeasureOptions.Unit> arrayList2, MeasureOptions.FormatStyle formatStyle, MeasureOptions.Unit unit, double d) {
        ArrayList<TreeMap<Double, MeasureOptions.Unit>> arrayList3;
        new TreeMap();
        new TreeMap();
        new ArrayList();
        if (arrayList.size() > 0) {
            arrayList3 = groupUnitValues(arrayList, unit, d);
        } else {
            arrayList3 = groupUnitValues(arrayList2, unit, d);
        }
        TreeMap<Double, MeasureOptions.Unit> treeMap = arrayList3.get(0);
        TreeMap<Double, MeasureOptions.Unit> treeMap2 = arrayList3.get(1);
        if (treeMap.size() > 0) {
            return format(treeMap.get(treeMap.firstKey()), treeMap.firstKey().doubleValue(), formatStyle);
        }
        if (treeMap2.size() > 0) {
            return format(treeMap2.get(treeMap2.lastKey()), treeMap2.lastKey().doubleValue(), formatStyle);
        }
        return format(unit, d, formatStyle);
    }

    private ArrayList<TreeMap<Double, MeasureOptions.Unit>> groupUnitValues(ArrayList<MeasureOptions.Unit> arrayList, MeasureOptions.Unit unit, double d) {
        TreeMap<Double, MeasureOptions.Unit> treeMap = new TreeMap<>();
        TreeMap<Double, MeasureOptions.Unit> treeMap2 = new TreeMap<>();
        for (int i = 0; i < arrayList.size(); i++) {
            MeasureOptions.Unit unit2 = arrayList.get(i);
            double d2 = XPath.MATCH_SCORE_QNAME;
            try {
                d2 = this.measConvert.convert(unit, unit2, d);
            } catch (NotSupportConvertException unused) {
                HiLog.error(LABEL, "The conversion is not supported!", new Object[0]);
            }
            if (d2 >= 1.0d) {
                treeMap.put(Double.valueOf(d2), unit2);
            } else {
                treeMap2.put(Double.valueOf(d2), unit2);
            }
        }
        ArrayList<TreeMap<Double, MeasureOptions.Unit>> arrayList2 = new ArrayList<>();
        arrayList2.add(treeMap);
        arrayList2.add(treeMap2);
        return arrayList2;
    }
}
