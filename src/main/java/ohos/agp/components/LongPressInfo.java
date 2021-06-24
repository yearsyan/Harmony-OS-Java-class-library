package ohos.agp.components;

import ohos.agp.utils.Point;

public class LongPressInfo {
    public final Component component;
    public final Point deltaPoint;
    public final Point globalPoint;
    public final Point localPoint;
    public final float xOffset;
    public final double xVelocity;
    public final float yOffset;
    public final double yVelocity;

    public LongPressInfo(Point point, Point point2, float f, float f2, double d, double d2, Point point3, Component component2) {
        this.globalPoint = point;
        this.localPoint = point2;
        this.xOffset = f;
        this.yOffset = f2;
        this.xVelocity = d;
        this.yVelocity = d2;
        this.deltaPoint = point3;
        this.component = component2;
    }
}
