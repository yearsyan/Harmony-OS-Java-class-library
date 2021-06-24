package ohos.agp.animation.util;

public class ValueKeyFrame implements Comparable<ValueKeyFrame> {
    private double mKeyValue;
    private float mProgress;

    private ValueKeyFrame(float f, float f2) {
        this.mProgress = f;
        this.mKeyValue = (double) f2;
    }

    public static ValueKeyFrame createFloatFrame(float f, float f2) {
        return new ValueKeyFrame(f, f2);
    }

    public float getProgress() {
        return this.mProgress;
    }

    public double getKeyValue() {
        return this.mKeyValue;
    }

    public int compareTo(ValueKeyFrame valueKeyFrame) {
        if (Float.compare(this.mProgress, valueKeyFrame.mProgress) < 0) {
            return -1;
        }
        return equals(valueKeyFrame) ? 0 : 1;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ValueKeyFrame) || Float.compare(this.mProgress, ((ValueKeyFrame) obj).mProgress) != 0) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Float.hashCode(this.mProgress);
    }
}
