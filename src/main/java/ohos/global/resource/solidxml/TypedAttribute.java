package ohos.global.resource.solidxml;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ohos.global.configuration.Configuration;
import ohos.global.configuration.DeviceCapability;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.system.Parameters;

public abstract class TypedAttribute {
    public static final int BOOLEAN_ATTR = 0;
    public static final int COLOR_ATTR = 1;
    private static final String DENSITY_INDEPENDENT_PIXEL = "dp";
    public static final int FLOAT_ATTR = 2;
    private static final String FONT_SIZE_PIXEL = "fp";
    public static final int GRAPHIC_ATTR = 8;
    public static final int INTEGER_ATTR = 3;
    public static final int LAYOUT_ATTR = 5;
    public static final int MEDIA_ATTR = 6;
    public static final int PATTERN_ATTR = 7;
    private static final String PIXEL_UNIT = "px";
    private static final Pattern REGEX_UNIT = Pattern.compile("^(?<value>-?\\d+(\\.\\d+)?)(?<unit>px|dp|sp|vp|fp)?$");
    private static final String SCALE_INDEPENDENT_PIXEL = "sp";
    public static final int STRING_ATTR = 4;
    public static final int UNDEFINED_ATTR = -1;
    private static final String VIRTUAL_PIXEL = "vp";

    public abstract boolean getBooleanValue() throws NotExistException, IOException, WrongTypeException;

    public abstract int getColorValue() throws NotExistException, IOException, WrongTypeException;

    public abstract float getFloatValue() throws NotExistException, IOException, WrongTypeException;

    public abstract int getIntegerValue() throws NotExistException, IOException, WrongTypeException;

    public abstract SolidXml getLayoutValue() throws NotExistException, IOException, WrongTypeException;

    public abstract Resource getMediaResource() throws NotExistException, IOException, WrongTypeException;

    public abstract String getMediaValue() throws NotExistException, IOException, WrongTypeException;

    public abstract String getName();

    public abstract String getOriginalValue();

    public abstract String getParsedValue() throws NotExistException, IOException, WrongTypeException;

    public abstract Pattern getPatternValue() throws NotExistException, IOException, WrongTypeException;

    public abstract int getPixelValue(boolean z) throws NotExistException, IOException, WrongTypeException;

    public abstract int getResId() throws NotExistException, IOException, WrongTypeException;

    public abstract ResourceManager getResourceManager();

    public abstract String getStringValue() throws NotExistException, IOException, WrongTypeException;

    public abstract int getType();

    public static float computeTranslateRatio(DeviceCapability deviceCapability) {
        int parseInt;
        if (((float) deviceCapability.screenDensity) / 160.0f > 0.0f) {
            parseInt = deviceCapability.screenDensity;
        } else {
            try {
                parseInt = Integer.parseInt(Parameters.get("hw.lcd.density", "160"));
            } catch (NumberFormatException unused) {
                return 1.0f;
            }
        }
        return ((float) parseInt) / 160.0f;
    }

    public static int computePixelValue(String str, Configuration configuration, DeviceCapability deviceCapability, boolean z) throws WrongTypeException {
        float computeFloatValue = computeFloatValue(str, configuration, deviceCapability);
        if (z) {
            return (int) computeFloatValue;
        }
        return Math.round(computeFloatValue);
    }

    public static float computeFloatValue(String str, Configuration configuration, DeviceCapability deviceCapability) throws WrongTypeException {
        Matcher matcher = REGEX_UNIT.matcher(str);
        if (matcher.find()) {
            try {
                String group = matcher.group("unit");
                float parseFloat = Float.parseFloat(matcher.group("value"));
                float computeTranslateRatio = computeTranslateRatio(deviceCapability);
                if (!VIRTUAL_PIXEL.equals(group)) {
                    if (!DENSITY_INDEPENDENT_PIXEL.equals(group)) {
                        if (!FONT_SIZE_PIXEL.equals(group) && !SCALE_INDEPENDENT_PIXEL.equals(group)) {
                            return parseFloat;
                        }
                        computeTranslateRatio *= configuration.fontRatio != 0.0f ? configuration.fontRatio : 1.0f;
                    }
                }
                return parseFloat * computeTranslateRatio;
            } catch (IllegalArgumentException | IllegalStateException unused) {
                throw new WrongTypeException("float format not correct.");
            }
        } else {
            throw new WrongTypeException("float not match the regex pattern.");
        }
    }

    public static class AttrData {
        private String attrName;
        private int attrType;
        private String attrValue;

        public AttrData() {
            this("", "");
        }

        public AttrData(String str, String str2) {
            this.attrType = -1;
            this.attrName = str;
            this.attrValue = str2;
        }

        public String getAttrName() {
            return this.attrName;
        }

        public void setAttrName(String str) {
            this.attrName = str;
        }

        public String getAttrValue() {
            return this.attrValue;
        }

        public void setAttrValue(String str) {
            this.attrValue = str;
        }

        public int getAttrType() {
            return this.attrType;
        }

        public void setAttrType(int i) {
            this.attrType = i;
        }
    }
}
