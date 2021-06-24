package ohos.agp.animation.styledsolutions;

import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.animation.timelinecurves.SpringCurve;
import ohos.agp.components.Component;
import ohos.hiviewdfx.HiLogLabel;

public class GravitationalClick {
    private static final float DAMPING_DEFAULT = 1.03f;
    private static final float DAMPING_HEAVY = 28.0f;
    private static final float DAMPING_LIGHT = 38.0f;
    private static final float DAMPING_MIDDLE = 35.0f;
    private static final float DEFAULT_SCALE_HEAVY = 0.95f;
    private static final float DEFAULT_SCALE_LIGHT = 0.9f;
    private static final float DEFAULT_SCALE_MIDDLE = 0.95f;
    public static final int EFFECT_DEFALT = 0;
    public static final int EFFECT_HEAVY = 1;
    public static final int EFFECT_LIGHT = 3;
    public static final int EFFECT_MIDDLE = 2;
    private static final float STIFFNESS_DEFAULT = 1800.0f;
    private static final float STIFFNESS_HEAVY = 240.0f;
    private static final float STIFFNESS_LIGHT = 410.0f;
    private static final float STIFFNESS_MIDDLE = 350.0f;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "GravitationalClick");
    private static final float VALUE_THRESHOLD = 0.002f;
    private static final float VELOCITY_DEFAULT = 0.0f;
    private static final float VELOCITY_HEAVY = 0.0f;
    private static final float VELOCITY_LIGHT = 1.0f;
    private static final float VELOCITY_MIDDLE = 0.5f;

    public static AnimatorProperty getActionDownAnimation(Component component, int i) {
        float f = 0.95f;
        if (!(i == 1 || i == 2 || i != 3)) {
            f = DEFAULT_SCALE_LIGHT;
        }
        return getSpringAnimator(component, i, f);
    }

    public static AnimatorProperty getActionDownAnimation(Component component, int i, float f) {
        return getSpringAnimator(component, i, f);
    }

    public static AnimatorProperty getActionUpAnimation(Component component, int i) {
        return getSpringAnimator(component, i, 1.0f);
    }

    private static AnimatorProperty getSpringAnimator(Component component, int i, float f) {
        if (f > 1.0f) {
            f = 1.0f;
        } else if (f < 0.0f) {
            f = 0.0f;
        }
        SpringCurve springCurveByType = getSpringCurveByType(component.getScaleX(), f, i);
        long duration = springCurveByType.getDuration();
        AnimatorProperty createAnimatorProperty = component.createAnimatorProperty();
        createAnimatorProperty.scaleX(f);
        createAnimatorProperty.scaleY(f);
        createAnimatorProperty.setDuration(duration);
        createAnimatorProperty.setCurve(springCurveByType);
        return createAnimatorProperty;
    }

    private static SpringCurve getSpringCurveByType(float f, float f2, int i) {
        float abs = Math.abs(f - f2);
        if (Float.compare(abs, 0.0f) == 0) {
            abs = 0.050000012f;
        }
        if (i == 1) {
            return new SpringCurve(STIFFNESS_HEAVY, DAMPING_HEAVY, abs, 0.0f, VALUE_THRESHOLD);
        }
        if (i == 2) {
            return new SpringCurve(STIFFNESS_MIDDLE, DAMPING_MIDDLE, abs, 0.5f, VALUE_THRESHOLD);
        }
        if (i != 3) {
            return new SpringCurve(STIFFNESS_DEFAULT, DAMPING_DEFAULT, abs, 0.0f, VALUE_THRESHOLD);
        }
        return new SpringCurve(STIFFNESS_LIGHT, DAMPING_LIGHT, abs, 1.0f, VALUE_THRESHOLD);
    }
}
