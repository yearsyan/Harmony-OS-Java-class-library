package ohos.agp.animation.timelinecurves;

import ohos.agp.animation.Animator;

public class CubicBezierCurve implements Animator.TimelineCurve {
    private static final int MAX_RESOLUTION = 4000;
    private static final float SEARCH_STEP = 2.5E-4f;
    private static final int THIRD_RDER = 3;
    private float mX1;
    private float mX2;
    private float mY1;
    private float mY2;

    private float getCubicBezierValue(float f, float f2, float f3) {
        float f4 = 1.0f - f;
        float f5 = 3.0f * f4;
        return (f4 * f5 * f * f2) + (f5 * f * f * f3) + (f * f * f);
    }

    public CubicBezierCurve(float f, float f2, float f3, float f4) {
        this.mX1 = f;
        this.mY1 = f2;
        this.mX2 = f3;
        this.mY2 = f4;
    }

    @Override // ohos.agp.animation.Animator.TimelineCurve
    public float getCurvedTime(float f) {
        return getCubicBezierValue(((float) binarySearch(f)) * SEARCH_STEP, this.mY1, this.mY2);
    }

    private int binarySearch(float f) {
        int i = 0;
        int i2 = 4000;
        while (i <= i2) {
            int i3 = (i + i2) / 2;
            float cubicBezierValue = getCubicBezierValue(((float) i3) * SEARCH_STEP, this.mX1, this.mX2);
            if (cubicBezierValue < f) {
                i = i3 + 1;
            } else if (cubicBezierValue <= f) {
                return i3;
            } else {
                i2 = i3 - 1;
            }
        }
        return i;
    }
}
