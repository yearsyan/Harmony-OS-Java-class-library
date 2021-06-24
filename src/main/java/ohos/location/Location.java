package ohos.location;

import java.util.Locale;
import ohos.com.sun.org.apache.xpath.internal.XPath;
import ohos.utils.PacMap;
import ohos.utils.Parcel;

public class Location {
    private static final double END_ANGEL = 360.0d;
    private static final double MAX_LAT = 90.0d;
    private static final double MAX_LON = 180.0d;
    private static final double MIN_LAT = -90.0d;
    private static final double MIN_LON = -180.0d;
    private static final double START_ANGEL = 0.0d;
    private float accuracy = 0.0f;
    private PacMap additions;
    private double altitude = XPath.MATCH_SCORE_QNAME;
    private double direction = XPath.MATCH_SCORE_QNAME;
    private double latitude = XPath.MATCH_SCORE_QNAME;
    private double longitude = XPath.MATCH_SCORE_QNAME;
    private float speed = 0.0f;
    private long timeSinceBoot = 0;
    private long timeStamp = 0;

    public static float calculateDistance(double d, double d2, double d3, double d4) throws IllegalArgumentException {
        if (d < MIN_LAT || d > MAX_LAT) {
            throw new IllegalArgumentException();
        } else if (d2 < MIN_LON || d2 > MAX_LON) {
            throw new IllegalArgumentException();
        } else if (d3 < MIN_LAT || d3 > MAX_LAT) {
            throw new IllegalArgumentException();
        } else if (d4 < MIN_LON || d4 > MAX_LON) {
            throw new IllegalArgumentException();
        } else {
            double d5 = (d * 3.141592653589793d) / MAX_LON;
            double d6 = (d2 * 3.141592653589793d) / MAX_LON;
            double d7 = (d3 * 3.141592653589793d) / MAX_LON;
            return (float) (Math.asin(Math.sqrt(Math.pow(Math.sin((d5 - d7) / 2.0d), 2.0d) + (Math.cos(d5) * Math.cos(d7) * Math.pow(Math.sin((d6 - ((3.141592653589793d * d4) / MAX_LON)) / 2.0d), 2.0d)))) * 2.0d * 6378137.0d);
        }
    }

    public static float calculateBearing(double d, double d2, double d3, double d4) throws IllegalArgumentException {
        if (d < MIN_LAT || d > MAX_LAT) {
            throw new IllegalArgumentException();
        } else if (d2 < MIN_LON || d2 > MAX_LON) {
            throw new IllegalArgumentException();
        } else if (d3 < MIN_LAT || d3 > MAX_LAT) {
            throw new IllegalArgumentException();
        } else if (d4 < MIN_LON || d4 > MAX_LON) {
            throw new IllegalArgumentException();
        } else {
            double d5 = (d * 3.141592653589793d) / MAX_LON;
            double d6 = (d2 * 3.141592653589793d) / MAX_LON;
            double d7 = (d3 * 3.141592653589793d) / MAX_LON;
            double d8 = ((d4 * 3.141592653589793d) / MAX_LON) - d6;
            return (float) ((((Math.atan2(Math.sin(d8) * Math.cos(d7), (Math.cos(d5) * Math.sin(d7)) - ((Math.sin(d5) * Math.cos(d7)) * Math.cos(d8))) * MAX_LON) / 3.141592653589793d) + END_ANGEL) % END_ANGEL);
        }
    }

    public Location(Location location) throws IllegalArgumentException {
        if (location != null) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
            this.altitude = location.getAltitude();
            this.accuracy = location.getAccuracy();
            this.speed = location.getSpeed();
            this.timeStamp = location.getTimeStamp();
            this.direction = location.getDirection();
            this.timeSinceBoot = location.getTimeSinceBoot();
            this.additions = location.getAdditions();
            return;
        }
        throw new IllegalArgumentException();
    }

    public Location(double d, double d2) throws IllegalArgumentException {
        if (d < MIN_LAT || d > MAX_LAT || d2 < MIN_LON || d2 > MAX_LON) {
            throw new IllegalArgumentException();
        }
        this.latitude = d;
        this.longitude = d2;
    }

    public PacMap getAdditions() {
        return this.additions;
    }

    public void setAdditions(PacMap pacMap) {
        this.additions = pacMap;
    }

    public double getDirection() {
        return this.direction;
    }

    public void setDirection(double d) throws IllegalArgumentException {
        if (d < XPath.MATCH_SCORE_QNAME || d >= END_ANGEL) {
            throw new IllegalArgumentException();
        }
        this.direction = d;
    }

    public void setTimeSinceBoot(long j) {
        if (j > 0) {
            this.timeSinceBoot = j;
        } else {
            this.timeSinceBoot = 0;
        }
    }

    public long getTimeSinceBoot() {
        return this.timeSinceBoot;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double d) throws IllegalArgumentException {
        if (d < MIN_LAT || d > MAX_LAT) {
            throw new IllegalArgumentException();
        }
        this.latitude = d;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double d) throws IllegalArgumentException {
        if (d < MIN_LON || d > MAX_LON) {
            throw new IllegalArgumentException();
        }
        this.longitude = d;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public void setAltitude(double d) {
        this.altitude = d;
    }

    public float getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(float f) {
        if (f > 0.0f) {
            this.accuracy = f;
        } else {
            this.accuracy = 0.0f;
        }
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float f) {
        if (f < 0.0f) {
            this.speed = 0.0f;
        } else {
            this.speed = f;
        }
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long j) {
        if (j > 0) {
            this.timeStamp = j;
        } else {
            this.timeStamp = 0;
        }
    }

    public boolean unmarshalling(Parcel parcel) {
        if (parcel == null) {
            return false;
        }
        this.latitude = parcel.readDouble();
        this.longitude = parcel.readDouble();
        this.altitude = parcel.readDouble();
        this.accuracy = parcel.readFloat();
        this.speed = parcel.readFloat();
        this.direction = parcel.readDouble();
        this.timeStamp = parcel.readLong();
        this.timeSinceBoot = parcel.readLong();
        return true;
    }

    public boolean marshalling(Parcel parcel) {
        if (parcel == null) {
            return false;
        }
        parcel.writeDouble(this.latitude);
        parcel.writeDouble(this.longitude);
        parcel.writeDouble(this.altitude);
        parcel.writeFloat(this.accuracy);
        parcel.writeFloat(this.speed);
        parcel.writeDouble(this.direction);
        parcel.writeLong(this.timeStamp);
        parcel.writeLong(this.timeSinceBoot);
        return true;
    }

    public String toString() {
        return "Location: " + String.format(Locale.ENGLISH, "%.0f*****, %.0f*****", Double.valueOf(this.latitude), Double.valueOf(this.longitude)) + " alt=" + this.altitude + " acc=" + this.accuracy + " speed=" + this.speed + " direction=" + this.direction + " timeStamp=" + this.timeStamp + " timeSinceBoot=" + this.timeSinceBoot;
    }
}
