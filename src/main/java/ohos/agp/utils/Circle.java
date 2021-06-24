package ohos.agp.utils;

public class Circle {
    private static final int HASHCODE_MULTIPLIER = 31;
    private float radius;
    private float x;
    private float y;

    public Circle(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.radius = f3;
    }

    public boolean isEmpty() {
        return this.radius < 0.0f;
    }

    public void set(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.radius = f3;
    }

    public void setEmpty() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.radius = 0.0f;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass() || !(obj instanceof Circle)) {
            return false;
        }
        return ((Circle) obj).hashCode() == hashCode();
    }

    public int hashCode() {
        float f = this.x;
        int i = 0;
        int floatToIntBits = (f < 0.0f ? 0 : Float.floatToIntBits(f)) * 31;
        float f2 = this.y;
        int floatToIntBits2 = (floatToIntBits + (f2 < 0.0f ? 0 : Float.floatToIntBits(f2))) * 31;
        float f3 = this.radius;
        if (f3 >= 0.0f) {
            i = Float.floatToIntBits(f3);
        }
        return floatToIntBits2 + i;
    }

    public String toString() {
        return "Circle{x=" + this.x + ", y=" + this.y + ", radius=" + this.radius + '}';
    }

    public void set(Point point, float f) {
        this.x = point.getPointX();
        this.y = point.getPointY();
        this.radius = f;
    }

    public void set(Circle circle) {
        set(circle.x, circle.y, circle.radius);
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float f) {
        this.radius = f;
    }

    public Point getCenter() {
        return new Point(this.x, this.y);
    }

    public float getCenterX() {
        return this.x;
    }

    public float getCenterY() {
        return this.y;
    }

    public void setCenter(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public void setCenter(Point point) {
        setCenter(point.getPointX(), point.getPointY());
    }

    public void setCenterX(float f) {
        this.x = f;
    }

    public void setCenterY(float f) {
        this.y = f;
    }

    public boolean isInclude(float f, float f2) {
        return isInclude(f, f2, 0.0f);
    }

    public boolean isInclude(Point point) {
        if (point.position.length > 2) {
            return false;
        }
        return isInclude(point.getPointX(), point.getPointY(), 0.0f);
    }

    public boolean isInclude(float f, float f2, float f3) {
        if (this.radius >= f3 && ((float) getDistance(this.x, this.y, f, f2)) + f3 < this.radius) {
            return true;
        }
        return false;
    }

    public boolean isInclude(Circle circle) {
        return isInclude(circle.getCenterX(), circle.getCenterY(), circle.getRadius());
    }

    private double getDistance(float f, float f2, float f3, float f4) {
        return Math.sqrt(Math.pow((double) (f - f3), 2.0d) + Math.pow((double) (f2 - f4), 2.0d));
    }

    public boolean isIntersect(float f, float f2, float f3) {
        return ((double) (this.radius + f3)) > getDistance(this.x, this.y, f, f2);
    }

    public boolean isIntersect(Circle circle) {
        return isIntersect(circle.x, circle.y, circle.radius);
    }

    public void zoom(float f) {
        this.radius *= f;
    }

    public void stretch(float f) {
        this.radius += f;
    }

    public void shrink(float f) {
        this.radius -= f;
        float f2 = this.radius;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        this.radius = f2;
    }

    public void translate(float f, float f2) {
        this.x += f;
        this.y += f2;
    }

    public void translate(Point point) {
        if (point.position.length <= 2) {
            translate(point.getPointX(), point.getPointY());
        }
    }

    public double getArea() {
        float f = this.radius;
        return ((double) f) * 3.141592653589793d * ((double) f);
    }
}
