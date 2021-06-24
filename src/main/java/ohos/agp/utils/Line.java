package ohos.agp.utils;

import ohos.com.sun.org.apache.xpath.internal.XPath;

public class Line {
    private static final int HASHCODE_MULTIPLIER = 31;
    private Point endPoint;
    private Point startPoint;

    public Line(Point point, Point point2) {
        this.startPoint = point;
        this.endPoint = point2;
    }

    public boolean isEmpty() {
        return getDistance() <= XPath.MATCH_SCORE_QNAME;
    }

    public void set(Line line) {
        this.startPoint = line.startPoint;
        this.endPoint = line.endPoint;
    }

    public void set(float f, float f2, float f3, float f4) {
        this.startPoint.modify(f, f2);
        this.endPoint.modify(f3, f4);
    }

    public void set(Point point, Point point2) {
        this.startPoint = point;
        this.endPoint = point2;
    }

    public void setStartPointX(float f) {
        Point point = this.startPoint;
        point.modify(f, point.getPointY());
    }

    public void setStartPointY(float f) {
        Point point = this.startPoint;
        point.modify(point.getPointX(), f);
    }

    public void setEndPointX(float f) {
        Point point = this.endPoint;
        point.modify(f, point.getPointY());
    }

    public void setEndPointY(float f) {
        Point point = this.endPoint;
        point.modify(point.getPointX(), f);
    }

    public void setEmpty() {
        this.startPoint.modify(0.0f, 0.0f);
        this.endPoint.modify(0.0f, 0.0f);
    }

    public double getDistance() {
        return Math.sqrt(Math.pow((double) (this.startPoint.getPointX() - this.endPoint.getPointX()), 2.0d) + Math.pow((double) (this.startPoint.getPointY() - this.endPoint.getPointY()), 2.0d));
    }

    public Point getStartPoint() {
        return this.startPoint;
    }

    public Point getEndPoint() {
        return this.endPoint;
    }

    public float getStartPointX() {
        return this.startPoint.getPointX();
    }

    public float getStartPointY() {
        return this.startPoint.getPointY();
    }

    public float getEndPointX() {
        return this.endPoint.getPointX();
    }

    public float getEndPointY() {
        return this.endPoint.getPointY();
    }

    public void setStartPoint(Point point) {
        this.startPoint = point;
    }

    public void setEndPoint(Point point) {
        this.endPoint = point;
    }

    public void translate(float f, float f2) {
        Point point = this.startPoint;
        point.modify(point.getPointX() + f, this.startPoint.getPointY() + f2);
        Point point2 = this.endPoint;
        point2.modify(point2.getPointX() + f, this.endPoint.getPointY() + f2);
    }

    public void translate(Point point) {
        translate(point.getPointX(), point.getPointY());
    }

    public int hashCode() {
        int i = 0;
        int floatToIntBits = (((((this.startPoint.getPointX() < 0.0f ? 0 : Float.floatToIntBits(this.startPoint.getPointX())) * 31) + (this.startPoint.getPointY() < 0.0f ? 0 : Float.floatToIntBits(this.startPoint.getPointY()))) * 31) + (this.endPoint.getPointX() < 0.0f ? 0 : Float.floatToIntBits(this.endPoint.getPointX()))) * 31;
        if (this.endPoint.getPointY() >= 0.0f) {
            i = Float.floatToIntBits(this.endPoint.getPointY());
        }
        return floatToIntBits + i;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass() || !(obj instanceof Line)) {
            return false;
        }
        return ((Line) obj).hashCode() == hashCode();
    }

    public String toString() {
        return "Line{startPoint=" + this.startPoint + ", endPoint=" + this.endPoint + '}';
    }

    public boolean isVertical(Line line) {
        return Math.abs(((((this.endPoint.getPointY() - this.startPoint.getPointY()) / this.endPoint.getPointX()) - this.startPoint.getPointX()) * ((line.endPoint.getPointY() - line.startPoint.getPointY()) / (line.endPoint.getPointX() - line.startPoint.getPointX()))) + 1.0f) < 1.0E-6f;
    }
}
