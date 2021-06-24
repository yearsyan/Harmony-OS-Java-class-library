package ohos.aafwk.ability;

import ohos.agp.animation.AnimatorProperty;

public class AbilitySliceAnimator {
    private AnimatorType animatorType;
    private boolean defaultAnimator;
    private long delay;
    private long duration;
    private float fromX;
    private float fromY;
    private int repeatCount;
    private float toX;
    private float toY;

    /* access modifiers changed from: private */
    public enum AnimatorType {
        TRANSLATION,
        ROTATION
    }

    public AbilitySliceAnimator() {
        this.defaultAnimator = false;
        this.animatorType = AnimatorType.TRANSLATION;
        this.duration = 300;
        this.delay = 0;
        this.repeatCount = 0;
        this.defaultAnimator = true;
    }

    public AbilitySliceAnimator(float f, float f2, float f3, float f4) {
        this.defaultAnimator = false;
        this.animatorType = AnimatorType.TRANSLATION;
        this.duration = 300;
        this.delay = 0;
        this.repeatCount = 0;
        this.fromX = f;
        this.fromY = f2;
        this.toX = f3;
        this.toY = f4;
    }

    public AbilitySliceAnimator setDuration(long j) {
        this.duration = j;
        return this;
    }

    public AbilitySliceAnimator setDelay(long j) {
        this.delay = j;
        return this;
    }

    public AbilitySliceAnimator setRepeatCount(int i) {
        this.repeatCount = i;
        return this;
    }

    /* access modifiers changed from: package-private */
    public boolean isDefaultAnimator() {
        return this.defaultAnimator;
    }

    /* access modifiers changed from: package-private */
    public float getFromX() {
        return this.fromX;
    }

    /* access modifiers changed from: package-private */
    public float getFromY() {
        return this.fromY;
    }

    /* access modifiers changed from: package-private */
    public float getToX() {
        return this.toX;
    }

    /* access modifiers changed from: package-private */
    public float getToY() {
        return this.toY;
    }

    /* access modifiers changed from: package-private */
    public void constructDefaultAnimator(float f) {
        this.animatorType = AnimatorType.TRANSLATION;
        this.fromX = f;
        this.fromY = 0.0f;
        this.toX = 0.0f;
        this.toY = 0.0f;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.aafwk.ability.AbilitySliceAnimator$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$aafwk$ability$AbilitySliceAnimator$AnimatorType = new int[AnimatorType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            /*
                ohos.aafwk.ability.AbilitySliceAnimator$AnimatorType[] r0 = ohos.aafwk.ability.AbilitySliceAnimator.AnimatorType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.aafwk.ability.AbilitySliceAnimator.AnonymousClass1.$SwitchMap$ohos$aafwk$ability$AbilitySliceAnimator$AnimatorType = r0
                int[] r0 = ohos.aafwk.ability.AbilitySliceAnimator.AnonymousClass1.$SwitchMap$ohos$aafwk$ability$AbilitySliceAnimator$AnimatorType     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.aafwk.ability.AbilitySliceAnimator$AnimatorType r1 = ohos.aafwk.ability.AbilitySliceAnimator.AnimatorType.TRANSLATION     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.aafwk.ability.AbilitySliceAnimator.AnonymousClass1.$SwitchMap$ohos$aafwk$ability$AbilitySliceAnimator$AnimatorType     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.aafwk.ability.AbilitySliceAnimator$AnimatorType r1 = ohos.aafwk.ability.AbilitySliceAnimator.AnimatorType.ROTATION     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.AbilitySliceAnimator.AnonymousClass1.<clinit>():void");
        }
    }

    /* access modifiers changed from: package-private */
    public AnimatorProperty buildEnterAnimator(AnimatorProperty animatorProperty) {
        animatorProperty.setDuration(this.duration).setDelay(this.delay).setLoopedCount(this.repeatCount);
        if (AnonymousClass1.$SwitchMap$ohos$aafwk$ability$AbilitySliceAnimator$AnimatorType[this.animatorType.ordinal()] == 1) {
            animatorProperty.moveFromX(this.fromX).moveFromY(this.fromY).moveToX(this.toX).moveToY(this.toY);
        }
        return animatorProperty;
    }

    /* access modifiers changed from: package-private */
    public AnimatorProperty buildExitAnimator(AnimatorProperty animatorProperty) {
        animatorProperty.setDuration(this.duration).setDelay(this.delay).setLoopedCount(this.repeatCount);
        if (AnonymousClass1.$SwitchMap$ohos$aafwk$ability$AbilitySliceAnimator$AnimatorType[this.animatorType.ordinal()] == 1) {
            animatorProperty.moveFromX((this.toX * 2.0f) - this.fromX).moveFromY((this.toY * 2.0f) - this.fromY).moveToX(this.toX).moveToY(this.toY);
        }
        return animatorProperty;
    }
}
