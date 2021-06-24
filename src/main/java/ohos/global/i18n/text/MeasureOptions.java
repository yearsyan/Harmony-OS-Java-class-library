package ohos.global.i18n.text;

import ohos.global.icu.text.MeasureFormat;
import ohos.global.icu.util.LocaleData;
import ohos.global.icu.util.MeasureUnit;

public class MeasureOptions {

    public enum Style {
        AUTO_STYLE_ON,
        AUTO_STYLE_OFF
    }

    public enum FormatStyle {
        WIDE(MeasureFormat.FormatWidth.WIDE),
        SHORT(MeasureFormat.FormatWidth.SHORT),
        NARROW(MeasureFormat.FormatWidth.NARROW);
        
        private MeasureFormat.FormatWidth formatWidth;

        private FormatStyle(MeasureFormat.FormatWidth formatWidth2) {
            this.formatWidth = formatWidth2;
        }

        public MeasureFormat.FormatWidth getFormatWidth() {
            return this.formatWidth;
        }
    }

    public enum Usage {
        DEFAULT("default", "default"),
        AREA_LAND_AGRICULT("area-land-agricult", "area"),
        AREA_LAND_COMMERCL("area-land-commercl", "area"),
        AREA_LAND_RESIDNTL("area-land-residntl", "area"),
        LENGTH_PERSON("length-person", "length"),
        LENGTH_PERSON_SMALL("length-person-small", "length"),
        LENGTH_RAINFALL("length-rainfall", "length"),
        LENGTH_ROAD("length-road", "length"),
        LENGTH_ROAD_SMALL("length-road-small", "length"),
        LENGTH_SNOWFALL("length-snowfall", "length"),
        LENGTH_VEHICLE("length-vehicle", "length"),
        LENGTH_VISIBLTY("length-visiblty", "length"),
        LENGTH_VISIBLTY_SMALL("length-visiblty-small", "length"),
        LENGTH_PERSON_INFORMAL("length-person-informal", "length"),
        LENGTH_PERSON_SMALL_INFORMAL("length-person-small-informal", "length"),
        LENGTH_ROAD_INFORMAL("length-road-informal", "length"),
        SPEED_ROAD_TRAVEL("speed-road-travel", "speed"),
        SPEED_WIND("speed-wind", "speed"),
        TEMPERATURE_PERSON("temperature-person", "temperature"),
        TEMPERATURE_WEATHER("temperature-weather", "temperature"),
        VOLUME_VEHICLE_FUEL("volume-vehicle-fuel", "volume");
        
        private String type;
        private String usageName;

        private Usage(String str, String str2) {
            this.usageName = str;
            this.type = str2;
        }

        public String getUsageName() {
            return this.usageName;
        }

        public String getUsageType() {
            return this.type;
        }
    }

    public enum Unit {
        AREA_UK_ACRE(MeasureUnit.ACRE, LocaleData.MeasurementSystem.UK),
        AREA_SI_HECTARE(MeasureUnit.HECTARE, LocaleData.MeasurementSystem.SI),
        AREA_SI_SQUARE_CENTIMETER(MeasureUnit.SQUARE_CENTIMETER, LocaleData.MeasurementSystem.SI),
        AREA_UK_SQUARE_FOOT(MeasureUnit.SQUARE_FOOT, LocaleData.MeasurementSystem.UK),
        AREA_UK_SQUARE_INCH(MeasureUnit.SQUARE_INCH, LocaleData.MeasurementSystem.UK),
        AREA_SI_SQUARE_KILOMETER(MeasureUnit.SQUARE_KILOMETER, LocaleData.MeasurementSystem.SI),
        AREA_SI_SQUARE_METER(MeasureUnit.SQUARE_METER, LocaleData.MeasurementSystem.SI),
        AREA_UK_SQUARE_MILE(MeasureUnit.SQUARE_MILE, LocaleData.MeasurementSystem.UK),
        AREA_UK_SQUARE_YARD(MeasureUnit.SQUARE_YARD, LocaleData.MeasurementSystem.UK),
        AREA_DUNAM(MeasureUnit.DUNAM, LocaleData.MeasurementSystem.SI),
        LENGTH_SI_CENTIMETER(MeasureUnit.CENTIMETER, LocaleData.MeasurementSystem.SI),
        LENGTH_SI_DECIMETER(MeasureUnit.DECIMETER, LocaleData.MeasurementSystem.SI),
        LENGTH_UK_FATHOM(MeasureUnit.FATHOM, LocaleData.MeasurementSystem.UK),
        LENGTH_UK_FOOT(MeasureUnit.FOOT, LocaleData.MeasurementSystem.UK),
        LENGTH_UK_FURLONG(MeasureUnit.FURLONG, LocaleData.MeasurementSystem.UK),
        LENGTH_UK_INCH(MeasureUnit.INCH, LocaleData.MeasurementSystem.UK),
        LENGTH_SI_KILOMETER(MeasureUnit.KILOMETER, LocaleData.MeasurementSystem.SI),
        LENGTH_SI_METER(MeasureUnit.METER, LocaleData.MeasurementSystem.SI),
        LENGTH_SI_MICROMETER(MeasureUnit.MICROMETER, LocaleData.MeasurementSystem.SI),
        LENGTH_UK_MILE(MeasureUnit.MILE, LocaleData.MeasurementSystem.UK),
        LENGTH_MILE_SCANDINAVIAN(MeasureUnit.MILE_SCANDINAVIAN, LocaleData.MeasurementSystem.SI),
        LENGTH_SI_MILLIMETER(MeasureUnit.MILLIMETER, LocaleData.MeasurementSystem.SI),
        LENGTH_SI_NANOMETER(MeasureUnit.NANOMETER, LocaleData.MeasurementSystem.SI),
        LENGTH_NAUTICAL_MILE(MeasureUnit.NAUTICAL_MILE, LocaleData.MeasurementSystem.SI),
        LENGTH_SI_PICOMETER(MeasureUnit.PICOMETER, LocaleData.MeasurementSystem.SI),
        LENGTH_UK_POINT(MeasureUnit.POINT, LocaleData.MeasurementSystem.UK),
        LENGTH_UK_YARD(MeasureUnit.YARD, LocaleData.MeasurementSystem.UK),
        SPEED_SI_KILOMETER_PER_HOUR(MeasureUnit.KILOMETER_PER_HOUR, LocaleData.MeasurementSystem.SI),
        SPEED_KNOT(MeasureUnit.KNOT, LocaleData.MeasurementSystem.SI),
        SPEED_SI_METER_PER_SECOND(MeasureUnit.METER_PER_SECOND, LocaleData.MeasurementSystem.SI),
        SPEED_UK_MILE_PER_HOUR(MeasureUnit.MILE_PER_HOUR, LocaleData.MeasurementSystem.UK),
        TEMPERATURE_SI_CELSIUS(MeasureUnit.CELSIUS, LocaleData.MeasurementSystem.SI),
        TEMPERATURE_SI_FAHRENHEIT(MeasureUnit.FAHRENHEIT, LocaleData.MeasurementSystem.SI),
        TEMPERATURE_SI_KELVIN(MeasureUnit.KELVIN, LocaleData.MeasurementSystem.SI),
        VOLUME_US_ACRE_FOOT(MeasureUnit.ACRE_FOOT, LocaleData.MeasurementSystem.UK),
        VOLUME_UK_BARREL(MeasureUnit.BARREL, LocaleData.MeasurementSystem.UK),
        VOLUME_US_BARREL(MeasureUnit.BARREL, LocaleData.MeasurementSystem.US),
        VOLUME_UK_BUSHEL(MeasureUnit.BUSHEL, LocaleData.MeasurementSystem.UK),
        VOLUME_US_BUSHEL(MeasureUnit.BUSHEL, LocaleData.MeasurementSystem.US),
        VOLUME_SI_CENTILITER(MeasureUnit.CENTILITER, LocaleData.MeasurementSystem.SI),
        VOLUME_SI_CUBIC_CENTIMETER(MeasureUnit.CUBIC_CENTIMETER, LocaleData.MeasurementSystem.SI),
        VOLUME_UK_CUBIC_FOOT(MeasureUnit.CUBIC_FOOT, LocaleData.MeasurementSystem.UK),
        VOLUME_UK_CUBIC_INCH(MeasureUnit.CUBIC_INCH, LocaleData.MeasurementSystem.UK),
        VOLUME_SI_CUBIC_KILOMETER(MeasureUnit.CUBIC_KILOMETER, LocaleData.MeasurementSystem.SI),
        VOLUME_SI_CUBIC_METER(MeasureUnit.CUBIC_METER, LocaleData.MeasurementSystem.SI),
        VOLUME_UK_CUBIC_MILE(MeasureUnit.CUBIC_MILE, LocaleData.MeasurementSystem.UK),
        VOLUME_UK_CUBIC_YARD(MeasureUnit.CUBIC_YARD, LocaleData.MeasurementSystem.UK),
        VOLUME_UK_CUP(MeasureUnit.CUP, LocaleData.MeasurementSystem.UK),
        VOLUME_US_CUP(MeasureUnit.CUP, LocaleData.MeasurementSystem.US),
        VOLUME_SI_CUP_METRIC(MeasureUnit.CUP_METRIC, LocaleData.MeasurementSystem.SI),
        VOLUME_SI_DECILITER(MeasureUnit.DECILITER, LocaleData.MeasurementSystem.SI),
        VOLUME_US_FLUID_OUNCE(MeasureUnit.FLUID_OUNCE, LocaleData.MeasurementSystem.US),
        VOLUME_UK_FLUID_OUNCE_IMPERIAL(MeasureUnit.FLUID_OUNCE_IMPERIAL, LocaleData.MeasurementSystem.UK),
        VOLUME_US_GALLON(MeasureUnit.GALLON, LocaleData.MeasurementSystem.US),
        VOLUME_UK_GALLON_IMPERIAL(MeasureUnit.GALLON_IMPERIAL, LocaleData.MeasurementSystem.UK),
        VOLUME_SI_HECTOLITER(MeasureUnit.HECTOLITER, LocaleData.MeasurementSystem.SI),
        VOLUME_SI_LITER(MeasureUnit.LITER, LocaleData.MeasurementSystem.SI),
        VOLUME_SI_MEGALITER(MeasureUnit.MEGALITER, LocaleData.MeasurementSystem.SI),
        VOLUME_SI_MILLILITER(MeasureUnit.MILLILITER, LocaleData.MeasurementSystem.SI),
        VOLUME_UK_PINT(MeasureUnit.PINT, LocaleData.MeasurementSystem.UK),
        VOLUME_US_PINT(MeasureUnit.PINT, LocaleData.MeasurementSystem.US),
        VOLUME_SI_PINT_METRIC(MeasureUnit.PINT_METRIC, LocaleData.MeasurementSystem.UK),
        VOLUME_UK_QUART(MeasureUnit.QUART, LocaleData.MeasurementSystem.UK),
        VOLUME_US_QUART(MeasureUnit.QUART, LocaleData.MeasurementSystem.US),
        VOLUME_UK_TABLESPOON(MeasureUnit.TABLESPOON, LocaleData.MeasurementSystem.UK),
        VOLUME_US_TABLESPOON(MeasureUnit.TABLESPOON, LocaleData.MeasurementSystem.US),
        VOLUME_UK_TEASPOON(MeasureUnit.TEASPOON, LocaleData.MeasurementSystem.UK),
        VOLUME_US_TEASPOON(MeasureUnit.TEASPOON, LocaleData.MeasurementSystem.US);
        
        private LocaleData.MeasurementSystem measSys;
        private MeasureUnit unit;

        private Unit(MeasureUnit measureUnit, LocaleData.MeasurementSystem measurementSystem) {
            this.unit = measureUnit;
            this.measSys = measurementSystem;
        }

        public String getUnitType() {
            return this.unit.getType();
        }

        public String getUnitName() {
            return this.unit.getSubtype();
        }

        public MeasureUnit getMeasureUnit() {
            return this.unit;
        }

        public LocaleData.MeasurementSystem getMeasurementSystem() {
            return this.measSys;
        }
    }
}
