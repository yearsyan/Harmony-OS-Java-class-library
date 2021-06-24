package ohos.agp.animation.timelinecurves;

import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.animation.Animator;
import ohos.agp.animation.physical.Spring;
import ohos.hiviewdfx.HiLogLabel;

public class SpringCurve implements Animator.TimelineCurve {
    private static final int CURRENT_TIME = -1;
    private static final float ONE_SECOND = 1000.0f;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "SpringCurve");
    private long mDuration;
    private Spring mSpring;

    public SpringCurve() {
        this(228.0f, 30.0f);
    }

    public SpringCurve(float f, float f2) {
        this(f, f2, 1.0f);
    }

    public SpringCurve(float f, float f2, float f3) {
        this(f, f2, f3, 0.0f);
    }

    public SpringCurve(float f, float f2, float f3, float f4) {
        this(f, f2, f3, f4, 0.001f);
    }

    public SpringCurve(float f, float f2, float f3, float f4, float f5) {
        this.mSpring = null;
        this.mDuration = 1000;
        this.mSpring = new Spring(f, f2);
        this.mSpring.setEndValue(f3);
        this.mSpring.setStartVelocity(f4);
        this.mSpring.setValueAccuracy(f5);
        this.mSpring.initialize();
        this.mDuration = (long) this.mSpring.getEstimatedDuration();
    }

    public long getDuration() {
        return this.mDuration;
    }

    @Override // ohos.agp.animation.Animator.TimelineCurve
    public float getCurvedTime(float f) {
        if (f == 1.0f || this.mSpring.getEndValue() == 0.0f) {
            return 1.0f;
        }
        return this.mSpring.getValue((long) (f * ((float) this.mDuration))) / this.mSpring.getEndValue();
    }
}
