package ohos.agp.animation;

import ohos.agp.utils.MemoryCleanerRegistry;
import ohos.agp.utils.NativeMemoryCleanerHelper;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.solidxml.Node;

public abstract class Animator {
    public static final int INFINITE = -1;
    protected TimelineCurve mCustomizedCurve = null;
    protected LoopedListener mLoopedListener = null;
    protected long mNativeAnimatorPtr = 0;
    protected StateChangedListener mPauseListener = null;
    protected StateChangedListener mStartListener = null;

    public static class CurveType {
        public static final int ACCELERATE = 0;
        public static final int ACCELERATE_DECELERATE = 1;
        public static final int ANTICIPATE = 2;
        public static final int ANTICIPATE_OVERSHOOT = 3;
        public static final int BOUNCE = 4;
        public static final int CUBIC_BEZIER_ACCELERATION = 16;
        public static final int CUBIC_BEZIER_DECELERATION = 15;
        public static final int CUBIC_BEZIER_EXTREME_DECELERATION = 18;
        public static final int CUBIC_BEZIER_FRICTION = 17;
        public static final int CUBIC_BEZIER_RHYTHM = 20;
        public static final int CUBIC_BEZIER_SHARP = 19;
        public static final int CUBIC_BEZIER_SMOOTH = 21;
        public static final int CUBIC_BEZIER_STANDARD = 14;
        public static final int CUBIC_HERMITE = 5;
        public static final int CUSTOMIZED = -2;
        public static final int CYCLE = 6;
        public static final int DECELERATE = 7;
        public static final int INVALID = -1;
        public static final int LINEAR = 8;
        public static final int OVERSHOOT = 9;
        public static final int PHYSICAL_FLING = 13;
        public static final int PHYSICAL_SPRING = 12;
        public static final int SMOOTH_STEP = 10;
        public static final int SPRING = 11;

        public static boolean checkTypeValue(int i) {
            return i >= 0 && i <= 21;
        }
    }

    public interface LoopedListener {
        void onRepeat(Animator animator);
    }

    public interface StateChangedListener {
        void onCancel(Animator animator);

        void onEnd(Animator animator);

        void onPause(Animator animator);

        void onResume(Animator animator);

        void onStart(Animator animator);

        void onStop(Animator animator);
    }

    public interface TimelineCurve {
        float getCurvedTime(float f);
    }

    private native void nativeCancelAnimator(long j);

    private native void nativeEndAnimator(long j);

    private native int nativeGetCurveType(long j);

    private native long nativeGetDelay(long j);

    private native long nativeGetDuration(long j);

    private native int nativeGetRepeatCount(long j);

    private native boolean nativeIsPaused(long j);

    private native boolean nativeIsRunning(long j);

    private native void nativePauseAnimator(long j);

    private native void nativeResumeAnimator(long j);

    private native void nativeSetCurve(long j, TimelineCurve timelineCurve);

    private native void nativeSetCurveType(long j, int i);

    private native void nativeSetDelay(long j, long j2);

    private native void nativeSetDuration(long j, long j2);

    private native void nativeSetPauseListener(long j, StateChangedListener stateChangedListener);

    private native void nativeSetRepeatCount(long j, int i);

    private native void nativeSetRepeatListener(long j, LoopedListener loopedListener);

    private native void nativeSetStartListener(long j, StateChangedListener stateChangedListener);

    private native void nativeStartAnimator(long j);

    private native void nativeStopAnimator(long j);

    /* access modifiers changed from: protected */
    public void parse(Node node, ResourceManager resourceManager) {
    }

    public void release() {
    }

    public long getNativeAnimatorPtr() {
        return this.mNativeAnimatorPtr;
    }

    public void start() {
        nativeStartAnimator(this.mNativeAnimatorPtr);
    }

    public void stop() {
        nativeStopAnimator(this.mNativeAnimatorPtr);
    }

    public void cancel() {
        nativeCancelAnimator(this.mNativeAnimatorPtr);
    }

    public void end() {
        nativeEndAnimator(this.mNativeAnimatorPtr);
    }

    public void pause() {
        nativePauseAnimator(this.mNativeAnimatorPtr);
    }

    public void resume() {
        nativeResumeAnimator(this.mNativeAnimatorPtr);
    }

    public boolean isPaused() {
        return nativeIsPaused(this.mNativeAnimatorPtr);
    }

    public boolean isRunning() {
        return nativeIsRunning(this.mNativeAnimatorPtr);
    }

    public long getDuration() {
        return nativeGetDuration(this.mNativeAnimatorPtr);
    }

    public long getDelay() {
        return nativeGetDelay(this.mNativeAnimatorPtr);
    }

    public int getLoopedCount() {
        return nativeGetRepeatCount(this.mNativeAnimatorPtr);
    }

    public int getCurveType() {
        if (this.mCustomizedCurve == null) {
            return nativeGetCurveType(this.mNativeAnimatorPtr);
        }
        return -2;
    }

    /* access modifiers changed from: protected */
    public void setDurationInternal(long j) {
        if (j >= 0) {
            nativeSetDuration(this.mNativeAnimatorPtr, j);
        }
    }

    /* access modifiers changed from: protected */
    public void setDelayInternal(long j) {
        if (j >= 0) {
            nativeSetDelay(this.mNativeAnimatorPtr, j);
        }
    }

    /* access modifiers changed from: protected */
    public void setLoopedCountInternal(int i) {
        if (i >= 0 || i == -1) {
            nativeSetRepeatCount(this.mNativeAnimatorPtr, i);
        }
    }

    /* access modifiers changed from: protected */
    public void setCurveTypeInternal(int i) {
        if (CurveType.checkTypeValue(i)) {
            this.mCustomizedCurve = null;
            nativeSetCurveType(this.mNativeAnimatorPtr, i);
        }
    }

    /* access modifiers changed from: protected */
    public void setCurveInternal(TimelineCurve timelineCurve) {
        this.mCustomizedCurve = timelineCurve;
        nativeSetCurve(this.mNativeAnimatorPtr, this.mCustomizedCurve);
    }

    /* access modifiers changed from: protected */
    public void setPauseListenerInternal(StateChangedListener stateChangedListener) {
        this.mPauseListener = stateChangedListener;
        nativeSetPauseListener(this.mNativeAnimatorPtr, this.mPauseListener);
    }

    /* access modifiers changed from: protected */
    public void setLoopedListenerInternal(LoopedListener loopedListener) {
        this.mLoopedListener = loopedListener;
        nativeSetRepeatListener(this.mNativeAnimatorPtr, this.mLoopedListener);
    }

    /* access modifiers changed from: protected */
    public void setStartListenerInternal(StateChangedListener stateChangedListener) {
        this.mStartListener = stateChangedListener;
        nativeSetStartListener(this.mNativeAnimatorPtr, this.mStartListener);
    }

    /* access modifiers changed from: protected */
    public void initAnimator(long j) {
        MemoryCleanerRegistry.getInstance().registerWithNativeBind(this, new AnimatorCleaner(j), j);
    }

    protected static class AnimatorCleaner extends NativeMemoryCleanerHelper {
        private native void nativeAnimatorRelease(long j);

        public AnimatorCleaner(long j) {
            super(j);
        }

        /* access modifiers changed from: protected */
        @Override // ohos.agp.utils.NativeMemoryCleanerHelper
        public void releaseNativeMemory(long j) {
            if (j != 0) {
                nativeAnimatorRelease(j);
            }
        }
    }
}
