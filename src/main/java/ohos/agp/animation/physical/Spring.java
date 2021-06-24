package ohos.agp.animation.physical;

public class Spring {
    private CalcSpring mCalc = new DefaultCalcSpring();
    private float mDamping = 30.0f;
    private float mEndValue = 1.0f;
    private float mMass = 1.0f;
    private float mStartValue = 0.0f;
    private float mStartVelocity = 0.0f;
    private float mStiffness = 228.0f;
    private float mTimeEstimateSpan = 0.001f;
    private float mValueAccuracy = 0.001f;
    private float mVelocityAccuracy = (this.mValueAccuracy * 62.5f);

    /* access modifiers changed from: package-private */
    public interface CalcSpring {
        float getValue(float f);

        float getVelocity(float f);
    }

    public Spring(float f, float f2) {
        this.mStiffness = f;
        this.mDamping = f2;
    }

    public boolean isAtEquilibrium(float f, float f2) {
        return ((double) Math.abs(f2)) < ((double) this.mVelocityAccuracy) && ((double) Math.abs(f)) < ((double) this.mValueAccuracy);
    }

    public Spring initialize() {
        this.mCalc = getCalc();
        return this;
    }

    public float getValue(long j) {
        return this.mCalc.getValue(((float) j) / 1000.0f) + this.mEndValue;
    }

    public float getVelocity(long j) {
        return this.mCalc.getVelocity(((float) j) / 1000.0f);
    }

    private CalcSpring getCalc() {
        float f = this.mStartValue - this.mEndValue;
        float f2 = this.mStartVelocity;
        float f3 = this.mDamping;
        float f4 = this.mMass;
        float f5 = f3 * f3;
        float f6 = 4.0f * f4 * this.mStiffness;
        float f7 = f5 - f6;
        int compare = Float.compare(f5, f6);
        if (compare == 0) {
            float f8 = (-f3) / (f4 * 2.0f);
            return new CalcCriticalDamping(f, f2 - (f8 * f), f8);
        } else if (compare > 0) {
            double d = (double) (-f3);
            double d2 = (double) f7;
            double d3 = (double) (f4 * 2.0f);
            float sqrt = (float) ((d - Math.sqrt(d2)) / d3);
            float sqrt2 = (float) ((d + Math.sqrt(d2)) / d3);
            float f9 = (f2 - (sqrt * f)) / (sqrt2 - sqrt);
            return new CalcOverDamping(f - f9, f9, sqrt, sqrt2);
        } else {
            float f10 = f4 * 2.0f;
            float sqrt3 = (float) (Math.sqrt((double) (f6 - f5)) / ((double) f10));
            float f11 = (-f3) / f10;
            return new CalcUnderdamped(f, (f2 - (f11 * f)) / sqrt3, sqrt3, f11);
        }
    }

    public float getEstimatedDuration() {
        return doEstimateTime(this.mCalc);
    }

    private float doEstimateTime(CalcSpring calcSpring) {
        float f = this.mTimeEstimateSpan;
        float value = calcSpring.getValue(f);
        float velocity = calcSpring.getVelocity(f);
        while (!isAtEquilibrium(value, velocity)) {
            f += ((float) 200) * this.mTimeEstimateSpan;
            value = calcSpring.getValue(f);
            velocity = calcSpring.getVelocity(f);
        }
        return getDuration(calcSpring, f - (((float) 200) * this.mTimeEstimateSpan), f) * 1000.0f;
    }

    private float getDuration(CalcSpring calcSpring, float f, float f2) {
        if (Float.compare(Math.abs(f2 - f), 0.005f) < 0) {
            return f;
        }
        float f3 = (f + f2) / 2.0f;
        if (isAtEquilibrium(calcSpring.getValue(f3), calcSpring.getVelocity(f3))) {
            return getDuration(calcSpring, f, f3);
        }
        return getDuration(calcSpring, f3, f2);
    }

    public float getStartValue() {
        return this.mStartValue;
    }

    public Spring setStartValue(float f) {
        this.mStartValue = f;
        return this;
    }

    public float getStartVelocity() {
        return this.mStartVelocity;
    }

    public Spring setStartVelocity(float f) {
        this.mStartVelocity = f;
        return this;
    }

    public float getEndValue() {
        return this.mEndValue;
    }

    public Spring setEndValue(float f) {
        this.mEndValue = f;
        return this;
    }

    public float getValueAccuracy() {
        return this.mValueAccuracy;
    }

    public Spring setValueAccuracy(float f) {
        this.mValueAccuracy = f;
        this.mVelocityAccuracy = this.mValueAccuracy * 62.5f;
        return this;
    }

    public float getStiffness() {
        return this.mStiffness;
    }

    public Spring setStiffness(float f) {
        this.mStiffness = f;
        return this;
    }

    public float getDamping() {
        return this.mDamping;
    }

    public Spring setDamping(float f) {
        this.mDamping = f;
        return this;
    }

    public float getMass() {
        return this.mMass;
    }

    public Spring setMass(float f) {
        this.mMass = f;
        return this;
    }

    /* access modifiers changed from: package-private */
    public static class CalcCriticalDamping implements CalcSpring {
        float mC1;
        float mC2;
        float mR;

        CalcCriticalDamping(float f, float f2, float f3) {
            this.mC1 = f;
            this.mC2 = f2;
            this.mR = f3;
        }

        @Override // ohos.agp.animation.physical.Spring.CalcSpring
        public float getValue(float f) {
            return (float) (((double) (this.mC1 + (this.mC2 * f))) * Math.pow(2.718281828459045d, (double) (this.mR * f)));
        }

        @Override // ohos.agp.animation.physical.Spring.CalcSpring
        public float getVelocity(float f) {
            float pow = (float) Math.pow(2.718281828459045d, (double) (this.mR * f));
            float f2 = this.mR;
            float f3 = this.mC1;
            float f4 = this.mC2;
            return (f2 * (f3 + (f * f4)) * pow) + (f4 * pow);
        }
    }

    /* access modifiers changed from: package-private */
    public static class CalcOverDamping implements CalcSpring {
        float mC1;
        float mC2;
        float mR1;
        float mR2;

        CalcOverDamping(float f, float f2, float f3, float f4) {
            this.mC1 = f;
            this.mC2 = f2;
            this.mR1 = f3;
            this.mR2 = f4;
        }

        @Override // ohos.agp.animation.physical.Spring.CalcSpring
        public float getValue(float f) {
            return (this.mC1 * ((float) Math.pow(2.718281828459045d, (double) (this.mR1 * f)))) + (this.mC2 * ((float) Math.pow(2.718281828459045d, (double) (this.mR2 * f))));
        }

        @Override // ohos.agp.animation.physical.Spring.CalcSpring
        public float getVelocity(float f) {
            float f2 = this.mC1;
            float f3 = this.mR1;
            float pow = f2 * f3 * ((float) Math.pow(2.718281828459045d, (double) (f3 * f)));
            float f4 = this.mC2;
            float f5 = this.mR2;
            return pow + (f4 * f5 * ((float) Math.pow(2.718281828459045d, (double) (f5 * f))));
        }
    }

    /* access modifiers changed from: package-private */
    public static class CalcUnderdamped implements CalcSpring {
        float mC1;
        float mC2;
        float mR;
        float mW;

        CalcUnderdamped(float f, float f2, float f3, float f4) {
            this.mC1 = f;
            this.mC2 = f2;
            this.mW = f3;
            this.mR = f4;
        }

        @Override // ohos.agp.animation.physical.Spring.CalcSpring
        public float getValue(float f) {
            return ((float) Math.pow(2.718281828459045d, (double) (this.mR * f))) * ((this.mC1 * ((float) Math.cos((double) (this.mW * f)))) + (this.mC2 * ((float) Math.sin((double) (this.mW * f)))));
        }

        @Override // ohos.agp.animation.physical.Spring.CalcSpring
        public float getVelocity(float f) {
            float pow = (float) Math.pow(2.718281828459045d, (double) (this.mR * f));
            float cos = (float) Math.cos((double) (this.mW * f));
            float sin = (float) Math.sin((double) (this.mW * f));
            float f2 = this.mC2;
            float f3 = this.mW;
            float f4 = this.mC1;
            return ((((f2 * f3) * cos) - ((f3 * f4) * sin)) * pow) + (this.mR * pow * ((f2 * sin) + (f4 * cos)));
        }
    }

    static class DefaultCalcSpring implements CalcSpring {
        @Override // ohos.agp.animation.physical.Spring.CalcSpring
        public float getValue(float f) {
            return 0.0f;
        }

        @Override // ohos.agp.animation.physical.Spring.CalcSpring
        public float getVelocity(float f) {
            return 0.0f;
        }

        DefaultCalcSpring() {
        }
    }

    public String toString() {
        return "Spring{startValue=" + this.mStartValue + ", startVelocity=" + this.mStartVelocity + ", endValue=" + this.mEndValue + ", valueAccuracy=" + this.mValueAccuracy + ", stiffness=" + this.mStiffness + ", damping=" + this.mDamping + ", mass=" + this.mMass + ", timeEstimateSpan=" + this.mTimeEstimateSpan + ", calc=" + this.mCalc + ", velocityAccuracy=" + this.mVelocityAccuracy + '}';
    }
}
