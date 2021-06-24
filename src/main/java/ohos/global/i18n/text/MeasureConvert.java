package ohos.global.i18n.text;

import java.util.HashMap;
import java.util.Map;
import ohos.com.sun.org.apache.xpath.internal.XPath;
import ohos.global.i18n.text.MeasureData;
import ohos.global.i18n.text.MeasureOptions;
import ohos.global.icu.util.LocaleData;
import ohos.hiviewdfx.HiLogLabel;

public class MeasureConvert {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218111488, "MeasureConvert");
    private static final String PER_DELIMITER = "-per-";
    private HashMap<String, MeasureData.ConvertItem[]> convertRules;
    private MeasureData measData;
    private HashMap<String, Integer> powerValue = new HashMap<>();
    private HashMap<String, Double> prefixValue = new HashMap<>();

    public MeasureConvert(MeasureData measureData) {
        this.measData = measureData;
        this.convertRules = measureData.getConvertRules();
        initPrefixData();
    }

    private void initPrefixData() {
        this.prefixValue.put("deci", Double.valueOf(Math.pow(10.0d, -1.0d)));
        this.prefixValue.put("centi", Double.valueOf(Math.pow(10.0d, -2.0d)));
        this.prefixValue.put("milli", Double.valueOf(Math.pow(10.0d, -3.0d)));
        this.prefixValue.put("micro", Double.valueOf(Math.pow(10.0d, -6.0d)));
        this.prefixValue.put("nano", Double.valueOf(Math.pow(10.0d, -9.0d)));
        this.prefixValue.put("pico", Double.valueOf(Math.pow(10.0d, -12.0d)));
        this.prefixValue.put("kilo", Double.valueOf(Math.pow(10.0d, 3.0d)));
        this.prefixValue.put("hecto", Double.valueOf(Math.pow(10.0d, 2.0d)));
        this.prefixValue.put("mega", Double.valueOf(Math.pow(10.0d, 6.0d)));
        this.powerValue.put("square-", 2);
        this.powerValue.put("cubic-", 3);
    }

    public double convert(MeasureOptions.Unit unit, MeasureOptions.Unit unit2, double d) throws NotSupportConvertException {
        double d2;
        String unitName = unit.getUnitName();
        String unitName2 = unit2.getUnitName();
        LocaleData.MeasurementSystem measurementSystem = unit.getMeasurementSystem();
        LocaleData.MeasurementSystem measurementSystem2 = unit2.getMeasurementSystem();
        if (unitName == null || unitName2 == null || measurementSystem == null || measurementSystem2 == null) {
            throw new NotSupportConvertException("The conversion is not supported!");
        }
        double[] computeValue = computeValue(unitName, measurementSystem);
        double[] computeValue2 = computeValue(unitName2, measurementSystem2);
        if (computeValue == null || computeValue.length != 2) {
            d2 = 0.0d;
        } else {
            d2 = computeValue[1] + (computeValue[0] * d);
        }
        if (computeValue2 == null || computeValue2.length != 2) {
            return XPath.MATCH_SCORE_QNAME;
        }
        return (d2 - computeValue2[1]) / computeValue2[0];
    }

    private double[] computeValue(String str, LocaleData.MeasurementSystem measurementSystem) {
        double[] dArr = {XPath.MATCH_SCORE_QNAME, XPath.MATCH_SCORE_QNAME};
        if (str.contains(PER_DELIMITER)) {
            String[] split = str.split(PER_DELIMITER);
            dArr[0] = computeValue(split[0], measurementSystem)[0] / computeValue(split[1], measurementSystem)[0];
            return dArr;
        }
        dArr[0] = computeSIPrefixValue(str);
        if (dArr[0] == XPath.MATCH_SCORE_QNAME) {
            dArr[0] = computePowerValue(str, measurementSystem);
        }
        if (dArr[0] == XPath.MATCH_SCORE_QNAME) {
            double[] computeFactorValue = computeFactorValue(str, measurementSystem);
            dArr[0] = computeFactorValue[0];
            dArr[1] = computeFactorValue[1];
        }
        if (dArr[0] == XPath.MATCH_SCORE_QNAME) {
            dArr[0] = 1.0d;
        }
        return dArr;
    }

    private double computePowerValue(String str, LocaleData.MeasurementSystem measurementSystem) {
        double d = 0.0d;
        for (Map.Entry<String, Integer> entry : this.powerValue.entrySet()) {
            if (entry.getKey() != null && str.startsWith(entry.getKey())) {
                String substring = str.substring(entry.getKey().length(), str.length());
                double computeSIPrefixValue = computeSIPrefixValue(substring);
                if (computeSIPrefixValue == XPath.MATCH_SCORE_QNAME) {
                    computeSIPrefixValue = computeFactorValue(substring, measurementSystem)[0];
                }
                d = entry.getValue() != null ? Math.pow(computeSIPrefixValue, (double) entry.getValue().intValue()) : computeSIPrefixValue;
            }
        }
        return d;
    }

    private double computeSIPrefixValue(String str) {
        double d = XPath.MATCH_SCORE_QNAME;
        for (Map.Entry<String, Double> entry : this.prefixValue.entrySet()) {
            if (entry.getKey() != null && str.startsWith(entry.getKey())) {
                d = entry.getValue().doubleValue();
            }
        }
        return d;
    }

    private double[] computeFactorValue(String str, LocaleData.MeasurementSystem measurementSystem) {
        double[] dArr = {XPath.MATCH_SCORE_QNAME, XPath.MATCH_SCORE_QNAME};
        MeasureData.ConvertItem[] convertItemArr = this.convertRules.get(str);
        if (!(convertItemArr == null || convertItemArr.length == 0)) {
            if (convertItemArr.length > 1) {
                for (MeasureData.ConvertItem convertItem : convertItemArr) {
                    dArr[0] = convertItem.getFactor();
                    dArr[1] = convertItem.getOffset();
                    if (measurementSystem.equals(convertItem.getMeasurementSystem())) {
                        return dArr;
                    }
                }
            } else {
                MeasureData.ConvertItem convertItem2 = convertItemArr[0];
                dArr[0] = convertItem2.getFactor();
                dArr[1] = convertItem2.getOffset();
            }
        }
        return dArr;
    }
}
