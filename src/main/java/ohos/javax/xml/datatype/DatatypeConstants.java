package ohos.javax.xml.datatype;

import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import ohos.javax.xml.XMLConstants;
import ohos.javax.xml.namespace.QName;

public final class DatatypeConstants {
    public static final int APRIL = 4;
    public static final int AUGUST = 8;
    public static final QName DATE = new QName("http://www.w3.org/2001/XMLSchema", SchemaSymbols.ATTVAL_DATE);
    public static final QName DATETIME = new QName("http://www.w3.org/2001/XMLSchema", SchemaSymbols.ATTVAL_DATETIME);
    public static final Field DAYS = new Field("DAYS", 2);
    public static final int DECEMBER = 12;
    public static final QName DURATION = new QName("http://www.w3.org/2001/XMLSchema", "duration");
    public static final QName DURATION_DAYTIME = new QName(XMLConstants.W3C_XPATH_DATATYPE_NS_URI, "dayTimeDuration");
    public static final QName DURATION_YEARMONTH = new QName(XMLConstants.W3C_XPATH_DATATYPE_NS_URI, "yearMonthDuration");
    public static final int EQUAL = 0;
    public static final int FEBRUARY = 2;
    public static final int FIELD_UNDEFINED = Integer.MIN_VALUE;
    public static final QName GDAY = new QName("http://www.w3.org/2001/XMLSchema", SchemaSymbols.ATTVAL_DAY);
    public static final QName GMONTH = new QName("http://www.w3.org/2001/XMLSchema", SchemaSymbols.ATTVAL_MONTH);
    public static final QName GMONTHDAY = new QName("http://www.w3.org/2001/XMLSchema", SchemaSymbols.ATTVAL_MONTHDAY);
    public static final int GREATER = 1;
    public static final QName GYEAR = new QName("http://www.w3.org/2001/XMLSchema", SchemaSymbols.ATTVAL_YEAR);
    public static final QName GYEARMONTH = new QName("http://www.w3.org/2001/XMLSchema", SchemaSymbols.ATTVAL_YEARMONTH);
    public static final Field HOURS = new Field("HOURS", 3);
    public static final int INDETERMINATE = 2;
    public static final int JANUARY = 1;
    public static final int JULY = 7;
    public static final int JUNE = 6;
    public static final int LESSER = -1;
    public static final int MARCH = 3;
    public static final int MAX_TIMEZONE_OFFSET = -840;
    public static final int MAY = 5;
    public static final Field MINUTES = new Field("MINUTES", 4);
    public static final int MIN_TIMEZONE_OFFSET = 840;
    public static final Field MONTHS = new Field("MONTHS", 1);
    public static final int NOVEMBER = 11;
    public static final int OCTOBER = 10;
    public static final Field SECONDS = new Field("SECONDS", 5);
    public static final int SEPTEMBER = 9;
    public static final QName TIME = new QName("http://www.w3.org/2001/XMLSchema", "time");
    public static final Field YEARS = new Field("YEARS", 0);

    private DatatypeConstants() {
    }

    public static final class Field {
        private final int id;
        private final String str;

        private Field(String str2, int i) {
            this.str = str2;
            this.id = i;
        }

        public String toString() {
            return this.str;
        }

        public int getId() {
            return this.id;
        }
    }
}
