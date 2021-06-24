package ohos.agp.components.element;

import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.animation.timelinecurves.CubicBezierCurve;
import ohos.agp.components.Component;
import ohos.agp.components.element.LoadingElement;
import ohos.agp.utils.Color;
import ohos.annotation.SystemApi;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;
import ohos.utils.system.safwk.java.SystemAbilityDefinition;

@SystemApi
public class LoadingDraggingElement extends LoadingElement {
    private static final float ALPHA_END_VALUE = 1.0f;
    private static final float ALPHA_START_VALUE = 0.0f;
    private static final String ANIMATION_PROP_ROTATION = "rotation";
    private static final String ANIMATION_PROP_TAIL_GROWTH = "tailGrowth";
    private static final float DRAG_END_DEGREES = 15.0f;
    private static final float DRAG_START_DEGREES = -15.0f;
    private static final float MAX_ERROR = 1.0E-6f;
    private static final float ROTATION_SCALE_END_VALUE = 1.0f;
    private static final float ROTATION_SCALE_START_VALUE = 0.5f;
    private static final float START_FRACTION_VALUE = 0.0f;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "LoadingAnim");
    private static final float TAIL_GROWTH_DURATION_RATIO = 0.4f;
    private boolean mIsDraggingFinish = false;
    private boolean mIsDraggingStart = false;
    private AnimatorValue mRotationStartupAnimator;
    private AnimatorValue mTailGrowthAnimator;

    private float getAlphaForDragging(float f) {
        return (f * 1.0f) + 0.0f;
    }

    private float getCometDegreeForDragging(float f) {
        return (f * 30.0f) + DRAG_START_DEGREES;
    }

    private float getRingScaleForDragging(float f) {
        return (f * 0.5f) + 0.5f;
    }

    LoadingDraggingElement(PixelMap pixelMap, Component component, LoadingElement.LayeredRing layeredRing, LoadingElement.Comet comet, int i, float f) {
        super(pixelMap, component, layeredRing, comet, i, f);
        setupAnimators();
        this.mComet.updateTailRangeDegrees(0.0f);
        setDraggingFraction(0.0f);
    }

    public static LoadingDraggingElement create(Component component, float f, boolean z) {
        LoadingElement.ParamsBundle paramsBundle;
        PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
        initializationOptions.pixelFormat = PixelFormat.ARGB_8888;
        initializationOptions.size = new Size(300, 300);
        initializationOptions.editable = true;
        PixelMap create = PixelMap.create(initializationOptions);
        create.writePixels(Color.TRANSPARENT.getValue());
        if (z) {
            paramsBundle = LoadingElement.ParamsBundle.createParamsBundleForNightMode(f);
        } else {
            paramsBundle = LoadingElement.ParamsBundle.createParamsBundle(f);
        }
        return new LoadingDraggingElement(create, component, createRing(-932813210, paramsBundle), createComet(-932813210, paramsBundle), SystemAbilityDefinition.SUBSYS_DFX_SYS_ABILITY_ID_BEGIN, f);
    }

    public void setDraggingFraction(float f) {
        this.mIsDraggingFinish = false;
        this.mOffsetY = 0.0f;
        float alphaForDragging = getAlphaForDragging(f);
        this.mRing.setScale(getRingScaleForDragging(f));
        this.mRing.setAlpha(alphaForDragging);
        this.mComet.rotateCometTo(getCometDegreeForDragging(f));
        this.mComet.setAlpha(alphaForDragging);
        invalidateSelf();
        if (Float.compare(f, 1.0f) == 0) {
            this.mIsDraggingFinish = true;
        }
    }

    @Override // ohos.agp.components.element.LoadingElement
    public void start() {
        if (!isRunning() && this.mIsDraggingFinish) {
            this.mIsDraggingStart = true;
            this.mRotationStartupAnimator.start();
        }
    }

    @Override // ohos.agp.components.element.LoadingElement
    public void stop() {
        if (isRunning()) {
            this.mRotationStartupAnimator.cancel();
            this.mTailGrowthAnimator.cancel();
            this.mComet.updateTailRangeDegrees(0.0f);
            this.mIsDraggingStart = false;
            this.mIsDraggingFinish = false;
            super.stop();
        }
    }

    @Override // ohos.agp.components.element.LoadingElement
    public boolean isRunning() {
        return this.mIsRotating || this.mIsDraggingStart;
    }

    private void setupAnimators() {
        setupRotationStartAnimator();
        setupTailGrowthAnimator();
    }

    private void setupTailGrowthAnimator() {
        this.mTailGrowthAnimator = AnimatorFactory.createTailGrowthAnimator(60.0f, 480);
        this.mTailGrowthAnimator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            /* class ohos.agp.components.element.LoadingDraggingElement.AnonymousClass1 */

            @Override // ohos.agp.animation.AnimatorValue.ValueUpdateListener
            public void onUpdate(AnimatorValue animatorValue, float f) {
                LoadingDraggingElement.this.mComet.updateTailRangeDegrees((float) animatorValue.estimateValue(LoadingDraggingElement.ANIMATION_PROP_TAIL_GROWTH, f));
                LoadingDraggingElement.this.invalidateSelf();
            }
        });
    }

    private void setupRotationStartAnimator() {
        this.mRotationStartupAnimator = AnimatorFactory.createRotationStartupAnimator(DRAG_END_DEGREES, 35.0f);
        this.mRotationStartupAnimator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            /* class ohos.agp.components.element.LoadingDraggingElement.AnonymousClass2 */

            @Override // ohos.agp.animation.AnimatorValue.ValueUpdateListener
            public void onUpdate(AnimatorValue animatorValue, float f) {
                LoadingDraggingElement.this.mComet.rotateCometTo((float) animatorValue.estimateValue(LoadingDraggingElement.ANIMATION_PROP_ROTATION, f));
                LoadingDraggingElement.this.invalidateSelf();
            }
        });
        this.mRotationStartupAnimator.setStateChangedListener(new Animator.StateChangedListener() {
            /* class ohos.agp.components.element.LoadingDraggingElement.AnonymousClass3 */

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onCancel(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onPause(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onResume(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onStart(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onStop(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onEnd(Animator animator) {
                LoadingDraggingElement.this.mTailGrowthAnimator.start();
                LoadingDraggingElement.super.startRotation(false);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public static class AnimatorFactory {
        private static final float ROTATION_STARTUP_CONTROL_X1 = 0.6f;
        private static final float ROTATION_STARTUP_CONTROL_X2 = 1.0f;
        private static final float ROTATION_STARTUP_CONTROL_Y1 = 0.2f;
        private static final float ROTATION_STARTUP_CONTROL_Y2 = 1.0f;
        private static final int ROTATION_STARTUP_DURATION = 100;

        AnimatorFactory() {
        }

        static AnimatorValue createTailGrowthAnimator(float f, long j) {
            AnimatorValue animatorValue = new AnimatorValue();
            animatorValue.newValueContainer(LoadingDraggingElement.ANIMATION_PROP_TAIL_GROWTH).setFloat(0.0f, f);
            animatorValue.setDuration(j);
            animatorValue.setCurveType(8);
            return animatorValue;
        }

        static AnimatorValue createRotationStartupAnimator(float f, float f2) {
            AnimatorValue animatorValue = new AnimatorValue();
            animatorValue.newValueContainer(LoadingDraggingElement.ANIMATION_PROP_ROTATION).setFloat(f, f2);
            animatorValue.setCurve(new CubicBezierCurve(0.6f, ROTATION_STARTUP_CONTROL_Y1, 1.0f, 1.0f));
            animatorValue.setDuration(100);
            return animatorValue;
        }
    }
}
