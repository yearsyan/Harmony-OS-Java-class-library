package ohos.agp.animation.styledsolutions;

import java.util.Optional;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.animation.timelinecurves.CubicBezierCurve;
import ohos.agp.animation.timelinecurves.SpringCurve;
import ohos.agp.components.Component;
import ohos.agp.components.PageSliderIndicator;
import ohos.agp.render.Canvas;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;

public class GravitationalPagerIndicatorAnimator implements Component.DrawTask {
    private static final int BACKGHEIGHT = 6;
    private static final float CONTROLPOINT1Y = 0.0f;
    private static final float CONTROLPOINT2X = 1.0f;
    private static final float CONTROLPOINT2Y = 1.0f;
    private static final float DAMPINGSCALE = 0.2f;
    private static final int DOMAIN_ID = 218107648;
    private static final float DRAG_THRESHOLD = 0.5f;
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, DOMAIN_ID, "SuperMedia");
    private static final float LEFTCIRVECONTROLPOINT1X = 1.0f;
    private static final String LOG_FORMAT = "%{public}s: %{public}s";
    private static final int OFFSET = 30;
    private static final int PAGESLIDER_STATE_NONE = 0;
    private static final int PAGESLIDER_STATE_SLIDER = 2;
    private static final int PAGESLIDER_STATE_TOUCH = 1;
    private static final float PRECISION = 1.0E-4f;
    private static final float RIGHTCURVECONTROLPOINT1X = 0.4f;
    private static final int SLIDER_DURATION = 400;
    private static final float SPRINGDAMPING = 0.47f;
    private static final int SPRINGDURATION = 300;
    private static final int SPRINGSTIFFNESS = 700;
    private static final int TOUCHDELAY = 300;
    private static final float TWO = 2.0f;
    private static final int VECTOR_NEGATIVE = -1;
    private static final int VECTOR_NONE = 0;
    private static final int VECTOR_POSITIVE = 1;
    private static final int ZOOMDURATION = 200;
    private ComponentAdapter mAdapter = new DefaultAdapter();
    private boolean mAutoSliderEnable = true;
    private AnimatorValue mCircleAnimator;
    private int mComponentHeight;
    private float mComponentOriginX;
    private int mComponentWidth;
    private GravitationalSelfDriveAnimator mDampingAnimator;
    private float mDampingThreshold;
    private float mDragValueEnd;
    private GravitationalPagerIndicatorDrawer mDraw = new GravitationalPagerIndicatorDrawer();
    private AnimatorValue mEnlargeAnimator;
    private float mEnlargeValueEnd;
    private AnimatorValue mFinishAnimator;
    private boolean mHadInit = false;
    private boolean mHasLarge;
    private int mIndcatorItemOffset;
    private int mIndicatorCount;
    private int mIndicatorHeight;
    private int mIndicatorSelected;
    private int mIsIncrease = 0;
    private Integer mItemIndexTemp = null;
    private int mItemPosOffsetPixelsDir = 0;
    private CubicBezierCurve mLeftInterpolator = new CubicBezierCurve(1.0f, 0.0f, 1.0f, 1.0f);
    private int mMoveDirection = 0;
    private Integer mOffsetTemp = null;
    private boolean mOnPageSlidng = false;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPageSlideState;
    private float mRadius = 10.0f;
    private HotZoneStatus mRegionStatus = HotZoneStatus.COMMON;
    private CubicBezierCurve mRightInterpolator = new CubicBezierCurve(RIGHTCURVECONTROLPOINT1X, 0.0f, 1.0f, 1.0f);
    private float mScale = 1.33f;
    private float mSelectedPosition;
    private GravitationalSelfDriveAnimator mSelfDriveAnimator;
    private AnimatorValue mShrinkAnimator;
    private float mSkrinValueEnd;
    private AnimatorValue mSpringAnimator;
    private float mTouchStartPos;
    private AnimatorValue mWaitAnimator;

    public interface ComponentAdapter {
        Optional<Component> getComponent();

        void getParamaterFromComponent();

        void getSelected();

        void invalidate();

        void setAnimator(GravitationalPagerIndicatorAnimator gravitationalPagerIndicatorAnimator);

        void setComponent(Component component);

        void setSelected(int i);
    }

    /* access modifiers changed from: private */
    public enum HotZoneStatus {
        COMMON,
        DOWN,
        VISIBLE,
        DONE
    }

    public GravitationalPagerIndicatorAnimator() {
        initAnimator();
    }

    public void setColor(Color color, Color color2) {
        this.mDraw.setNormalColor(color);
        this.mDraw.setFocusColor(color2);
    }

    public void setBackgroundColorStartAndEnd(Color color, Color color2) {
        this.mDraw.setStartBackGroundColor(color);
        this.mDraw.setEndBackGroundColor(color2);
    }

    public void setPageSlideState(int i) {
        if (i == 0) {
            if (isPageSliding()) {
                this.mSpringAnimator.start();
            }
            this.mItemPosOffsetPixelsDir = 0;
            this.mOffsetTemp = null;
            this.mMoveDirection = 0;
            this.mIsIncrease = 0;
            this.mOnPageSlidng = false;
            this.mAdapter.getSelected();
        } else if (i == 1) {
            this.mOnPageSlidng = true;
            this.mMoveDirection = 0;
            this.mHadInit = false;
        }
        this.mPageSlideState = i;
    }

    public void setComponent(Component component) {
        if (component instanceof PageSliderIndicator) {
            this.mAdapter = new IndicatorAdapter();
            this.mAdapter.setComponent(component);
        }
    }

    public void setScale(float f) {
        this.mScale = f;
    }

    public void setRadius(float f) {
        this.mRadius = f;
    }

    public void initSelectPosition() {
        initSelectPosition(this.mIndicatorSelected);
    }

    @Override // ohos.agp.components.Component.DrawTask
    public void onDraw(Component component, Canvas canvas) {
        this.mDraw.onDraw(canvas);
    }

    public void setAdapter(ComponentAdapter componentAdapter) {
        if (componentAdapter != null) {
            this.mAdapter = componentAdapter;
        }
    }

    public void autoSlider(int i, int i2) {
        if (!this.mAutoSliderEnable) {
            this.mAutoSliderEnable = true;
        } else if (i != i2 && !isPageSliding()) {
            this.mCircleAnimator.stop();
            this.mSpringAnimator.stop();
            initDraw(i, i2);
            this.mCircleAnimator.start();
        }
    }

    public void setUpdateValue(int i, float f, int i2, int i3) {
        int i4;
        if (isPageSliding()) {
            if (this.mRegionStatus == HotZoneStatus.DONE) {
                this.mWaitAnimator.cancel();
                this.mSelfDriveAnimator.stop();
                handleTouchUp();
            }
            this.mSpringAnimator.end();
            this.mMoveDirection = isIncrease(i, i2, i3);
            if (this.mHadInit && this.mMoveDirection != 0) {
                leftCircleAnimatorUpdate(f);
                rightCircleAnimatorUpdate(f);
            }
            this.mAdapter.getSelected();
            if ((!this.mHadInit || this.mIndicatorSelected != i) && (i4 = this.mMoveDirection) != 0) {
                this.mHadInit = true;
                if (i + i4 < 0) {
                    initDraw(this.mIndicatorCount - 1, i);
                } else if (i + i4 > this.mIndicatorCount - 1) {
                    initDraw(0, i);
                } else {
                    initDraw(i4 + i, i);
                }
            }
        }
    }

    public void handleTouch(TouchEvent touchEvent) {
        if (touchEvent == null) {
            HiLog.info(LABEL_LOG, LOG_FORMAT, "GravitationalPagerIndicatorAnimator:handleTouch", "TouchEvent is null");
            return;
        }
        if (touchEvent.getAction() == 1) {
            this.mAdapter.getSelected();
            this.mItemPosOffsetPixelsDir = 0;
            this.mOffsetTemp = null;
            this.mIsIncrease = 0;
            if (this.mRegionStatus == HotZoneStatus.COMMON) {
                this.mWaitAnimator.start();
            }
            if (this.mRegionStatus == HotZoneStatus.VISIBLE) {
                handleTouchDown();
            }
            setSelectedPosition(this.mDraw.calculateItemPosition(this.mIndicatorSelected));
            this.mTouchStartPos = touchEvent.getPointerPosition(touchEvent.getIndex()).getX();
        }
        if (this.mRegionStatus == HotZoneStatus.DONE && touchEvent.getAction() == 3) {
            this.mSelfDriveAnimator.setHadMove(true);
            handleTouchDownAndMove(touchEvent.getPointerPosition(touchEvent.getIndex()).getX());
        }
        if (touchEvent.getAction() == 2) {
            this.mSelfDriveAnimator.stop();
            if (this.mRegionStatus == HotZoneStatus.DOWN) {
                handleClike(touchEvent.getPointerPosition(touchEvent.getIndex()).getX());
            }
            if (this.mRegionStatus == HotZoneStatus.DONE || this.mRegionStatus == HotZoneStatus.VISIBLE) {
                handleTouchUp();
                finishAnimatorStart();
            }
            this.mWaitAnimator.cancel();
        }
    }

    public int getContentWidth() {
        this.mAdapter.getParamaterFromComponent();
        float f = this.mRadius;
        return (int) (((calculateBackGroundWidth() * 1.4000001f) + f + f + f) * this.mScale);
    }

    public int getContentHeight() {
        this.mAdapter.getParamaterFromComponent();
        return (int) ((((float) this.mIndicatorHeight) + this.mRadius) * this.mScale);
    }

    private void initSelectPosition(int i) {
        initDraw(i, i - 1);
        this.mDraw.setLeftsliderValue(1.0f);
        this.mDraw.setRightsliderValue(1.0f);
        this.mDraw.setSpringValue(1.0f);
        this.mAdapter.invalidate();
    }

    private void autoSliderNotBreak(int i, int i2) {
        if (i != i2 && !animatorIsRunning() && !isPageSliding()) {
            this.mSpringAnimator.stop();
            initDraw(i, i2);
            this.mCircleAnimator.start();
        }
    }

    private void initselfDriveAnimator(int i, int i2) {
        if (i != i2) {
            this.mSpringAnimator.stop();
            initDraw(i, i2);
            this.mSelfDriveAnimator.start();
        }
    }

    private void startEnlarge() {
        this.mShrinkAnimator.stop();
        if (!this.mHasLarge) {
            this.mEnlargeAnimator.start();
            this.mHasLarge = true;
        }
    }

    private void startShrink() {
        if (this.mHasLarge) {
            this.mEnlargeAnimator.stop();
            this.mShrinkAnimator.start();
            this.mHasLarge = false;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void dampingUpdate(float f) {
        this.mDampingAnimator.updateX(f);
    }

    private boolean isPageSliding() {
        return this.mOnPageSlidng;
    }

    private boolean animatorIsRunning() {
        if (!this.mCircleAnimator.isRunning() && !this.mSelfDriveAnimator.isRunning() && !this.mDampingAnimator.isRunning()) {
            return false;
        }
        return true;
    }

    private int isIncrease(int i, int i2, int i3) {
        if (this.mOffsetTemp == null) {
            this.mOffsetTemp = Integer.valueOf(i2);
            this.mItemIndexTemp = Integer.valueOf(i);
            return 0;
        }
        int intValue = i2 + ((i - this.mItemIndexTemp.intValue()) * i3);
        if (intValue > 0) {
            if (this.mItemPosOffsetPixelsDir == -1 && this.mPageSlideState != 2) {
                this.mHadInit = false;
            }
            this.mItemPosOffsetPixelsDir = 1;
        }
        if (intValue < 0) {
            if (this.mItemPosOffsetPixelsDir == 1 && this.mPageSlideState != 2) {
                this.mHadInit = false;
            }
            this.mItemPosOffsetPixelsDir = -1;
        }
        if (this.mOffsetTemp.intValue() < intValue) {
            if (this.mIsIncrease == 1) {
                this.mIsIncrease = 1;
                this.mOffsetTemp = Integer.valueOf(intValue);
                return 1;
            }
            this.mIsIncrease = 1;
            return 0;
        } else if (this.mOffsetTemp.intValue() <= intValue) {
            return this.mIsIncrease;
        } else {
            if (this.mIsIncrease == -1) {
                this.mIsIncrease = -1;
                this.mOffsetTemp = Integer.valueOf(intValue);
                return -1;
            }
            this.mIsIncrease = -1;
            return 0;
        }
    }

    private int moveIndicator(float f, int i, int i2) {
        int i3;
        if (animatorIsRunning()) {
            return i2;
        }
        int calculateIndexByPositioin = this.mDraw.calculateIndexByPositioin(f - this.mComponentOriginX);
        float f2 = (float) (calculateIndexByPositioin - (i2 / i));
        if (f2 > 0.0f) {
            i3 = f2 >= 1.0f ? i2 + i : i2;
            if (f2 < 1.0f) {
                i3 = calculateIndexByPositioin;
            }
        } else {
            i3 = i2;
        }
        if (f2 < 0.0f) {
            if (f2 > -1.0f) {
                i3 = calculateIndexByPositioin + 1;
            }
            if (f2 < -1.0f && calculateIndexByPositioin < i2 - 1) {
                i3 = i2 - i;
            }
        }
        if (i3 < this.mIndicatorCount && i3 >= 0) {
            autoSliderNotBreak(i3, i2);
            this.mAutoSliderEnable = false;
            this.mAdapter.setSelected(i3);
        }
        return i3;
    }

    private void startDamping(int i) {
        this.mSpringAnimator.end();
        this.mDraw.initParamater();
        if (i != 0) {
            this.mDraw.setDirection(i);
        }
        this.mDampingAnimator.start();
    }

    private void touchMoveUpdateAnimator(float f) {
        float f2;
        float f3;
        if (!this.mDampingAnimator.isRunning()) {
            if (this.mDraw.getSelectedItem() < this.mDraw.getTarget()) {
                f2 = f - this.mTouchStartPos;
            } else {
                f2 = this.mTouchStartPos - f;
            }
            this.mSelfDriveAnimator.updateX(f2 / (Math.abs(this.mDraw.getSliderLength()) + this.mDraw.getRadius()));
        } else if (Float.compare(this.mDampingThreshold, 0.0f) != 0) {
            if (this.mDraw.getDirection() == -1) {
                f3 = (this.mTouchStartPos - f) / this.mDampingThreshold;
            } else {
                f3 = (f - this.mTouchStartPos) / this.mDampingThreshold;
            }
            if (f3 < 1.0f) {
                dampingUpdate(f3);
            } else {
                dampingUpdate(0.9999f);
            }
        }
    }

    private int touchMove(float f, int i) {
        int i2;
        if (!animatorIsRunning()) {
            int i3 = (int) f;
            if (isIncrease(1, i3, 1) == 1) {
                i2 = i + 1;
                if (i2 >= this.mIndicatorCount) {
                    startDamping(1);
                    this.mTouchStartPos = f;
                }
            } else {
                i2 = i;
            }
            if (isIncrease(1, i3, 1) == -1 && i2 - 1 < 0) {
                startDamping(-1);
                this.mTouchStartPos = f;
            }
            if (i2 < this.mIndicatorCount && i2 >= 0 && i2 != i) {
                this.mTouchStartPos = f;
                initselfDriveAnimator(i2, i);
            }
        } else {
            touchMoveUpdateAnimator(f);
        }
        return this.mDraw.getTarget();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void handleTouchDown() {
        startEnlarge();
    }

    private void handleTouchUp() {
        this.mRegionStatus = HotZoneStatus.COMMON;
        startShrink();
    }

    private void finishAnimatorStart() {
        float f = this.mDragValueEnd;
        if (f <= 0.0f) {
            return;
        }
        if (f > 0.5f) {
            this.mFinishAnimator.setDuration((long) ((1.0f - f) * 400.0f));
            this.mFinishAnimator.start();
            this.mAutoSliderEnable = false;
            this.mAdapter.setSelected(this.mDraw.getTarget());
            return;
        }
        this.mFinishAnimator.setDuration((long) (f * 400.0f));
        this.mFinishAnimator.start();
    }

    private void setSelectedPosition(float f) {
        this.mSelectedPosition = f;
    }

    private int handleTouchDownAndMove(float f, int i) {
        return touchMove(f, i);
    }

    private int handleTouchDownAndMove(float f) {
        return handleTouchDownAndMove(f, this.mIndicatorSelected);
    }

    private int handleClike(float f, int i) {
        return moveIndicator(f, 1, i);
    }

    private int handleClike(float f) {
        return handleClike(f, this.mIndicatorSelected);
    }

    private void leftCircleAnimatorUpdate(float f) {
        this.mDraw.setLeftsliderValue(this.mLeftInterpolator.getCurvedTime(f));
        this.mAdapter.invalidate();
    }

    private void rightCircleAnimatorUpdate(float f) {
        this.mDraw.setRightsliderValue(this.mRightInterpolator.getCurvedTime(f));
        this.mAdapter.invalidate();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void circleAnimatorUpdate(float f) {
        leftCircleAnimatorUpdate(f);
        rightCircleAnimatorUpdate(f);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void enlargeAnimatorUpdate(float f) {
        float f2 = this.mSkrinValueEnd;
        if (f2 + f <= 1.0f) {
            this.mDraw.setScaleValue(f2 + f);
            this.mEnlargeValueEnd = f + this.mSkrinValueEnd;
            this.mAdapter.invalidate();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void shrinkAnimatorUpdate(float f) {
        float f2 = this.mEnlargeValueEnd;
        if (f2 - f >= 0.0f) {
            this.mDraw.setScaleValue(f2 - f);
            this.mAdapter.invalidate();
            this.mSkrinValueEnd = this.mEnlargeValueEnd - f;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void dampingAnimatorUpdate(float f) {
        float f2;
        if (this.mIndicatorCount == 1) {
            f2 = 0.0f;
        } else {
            f2 = ((calculateBackGroundWidth() * DAMPINGSCALE) / ((float) (this.mIndicatorCount - 1))) * f;
        }
        float f3 = 30.0f;
        int i = this.mIndcatorItemOffset;
        if (i != 0) {
            f3 = (float) i;
        }
        this.mDraw.setItemOffset(f3 + f2);
        this.mDraw.setDampValue(f);
        this.mAdapter.invalidate();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void springAnimatorUpdate(float f) {
        this.mDraw.setSpringValue(f);
        this.mAdapter.invalidate();
    }

    private void createSelfDriveAnimator() {
        this.mSelfDriveAnimator = new GravitationalSelfDriveAnimator(null);
        this.mSelfDriveAnimator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            /* class ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.AnonymousClass1 */

            @Override // ohos.agp.animation.AnimatorValue.ValueUpdateListener
            public void onUpdate(AnimatorValue animatorValue, float f) {
                if (!GravitationalPagerIndicatorAnimator.this.mFinishAnimator.isRunning()) {
                    GravitationalPagerIndicatorAnimator.this.mDragValueEnd = f;
                }
                GravitationalPagerIndicatorAnimator.this.circleAnimatorUpdate(f);
            }
        });
        this.mSelfDriveAnimator.setStateChangeListener(new Animator.StateChangedListener() {
            /* class ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.AnonymousClass2 */

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
            public void onCancel(Animator animator) {
                GravitationalPagerIndicatorAnimator.this.mDragValueEnd = 0.0f;
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onEnd(Animator animator) {
                GravitationalPagerIndicatorAnimator.this.mAutoSliderEnable = false;
                GravitationalPagerIndicatorAnimator.this.mAdapter.setSelected(GravitationalPagerIndicatorAnimator.this.mDraw.getTarget());
                GravitationalPagerIndicatorAnimator.this.mDragValueEnd = 0.0f;
                GravitationalPagerIndicatorAnimator.this.mSpringAnimator.start();
            }
        });
    }

    private void createWaitAnimator() {
        this.mWaitAnimator = new AnimatorValue();
        this.mWaitAnimator.setDuration(300);
        this.mWaitAnimator.setDelay(0);
        this.mWaitAnimator.setLoopedCount(0);
        this.mWaitAnimator.setStateChangedListener(new Animator.StateChangedListener() {
            /* class ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.AnonymousClass3 */

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onPause(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onResume(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onStop(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onStart(Animator animator) {
                GravitationalPagerIndicatorAnimator.this.mRegionStatus = HotZoneStatus.DOWN;
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onCancel(Animator animator) {
                GravitationalPagerIndicatorAnimator.this.mRegionStatus = HotZoneStatus.COMMON;
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onEnd(Animator animator) {
                GravitationalPagerIndicatorAnimator.this.mRegionStatus = HotZoneStatus.VISIBLE;
                GravitationalPagerIndicatorAnimator.this.handleTouchDown();
            }
        });
    }

    private void createmSpringAnimator() {
        this.mSpringAnimator = new AnimatorValue();
        this.mSpringAnimator.setDuration(300);
        this.mSpringAnimator.setDelay(0);
        this.mSpringAnimator.setLoopedCount(0);
        this.mSpringAnimator.setCurve(new SpringCurve(700.0f, SPRINGDAMPING));
        this.mSpringAnimator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            /* class ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.AnonymousClass4 */

            @Override // ohos.agp.animation.AnimatorValue.ValueUpdateListener
            public void onUpdate(AnimatorValue animatorValue, float f) {
                GravitationalPagerIndicatorAnimator.this.springAnimatorUpdate(f);
            }
        });
    }

    private void createCircleAniamtor() {
        this.mCircleAnimator = new AnimatorValue();
        this.mCircleAnimator.setDuration(400);
        this.mCircleAnimator.setDelay(0);
        this.mCircleAnimator.setLoopedCount(0);
        this.mCircleAnimator.setCurveType(8);
        this.mCircleAnimator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            /* class ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.AnonymousClass5 */

            @Override // ohos.agp.animation.AnimatorValue.ValueUpdateListener
            public void onUpdate(AnimatorValue animatorValue, float f) {
                GravitationalPagerIndicatorAnimator.this.circleAnimatorUpdate(f);
            }
        });
        this.mCircleAnimator.setStateChangedListener(new Animator.StateChangedListener() {
            /* class ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.AnonymousClass6 */

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
                GravitationalPagerIndicatorAnimator.this.mSpringAnimator.start();
            }
        });
    }

    private void createEnlargeAnimator() {
        this.mEnlargeAnimator = new AnimatorValue();
        this.mEnlargeAnimator.setDuration(200);
        this.mEnlargeAnimator.setDelay(0);
        this.mEnlargeAnimator.setLoopedCount(0);
        this.mEnlargeAnimator.setCurveType(8);
        this.mEnlargeAnimator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            /* class ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.AnonymousClass7 */

            @Override // ohos.agp.animation.AnimatorValue.ValueUpdateListener
            public void onUpdate(AnimatorValue animatorValue, float f) {
                GravitationalPagerIndicatorAnimator.this.enlargeAnimatorUpdate(f);
            }
        });
        this.mEnlargeAnimator.setStateChangedListener(new Animator.StateChangedListener() {
            /* class ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.AnonymousClass8 */

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
                GravitationalPagerIndicatorAnimator.this.mRegionStatus = HotZoneStatus.DONE;
            }
        });
    }

    private void createShrikAnimator() {
        this.mShrinkAnimator = new AnimatorValue();
        this.mShrinkAnimator.setDuration(200);
        this.mShrinkAnimator.setDelay(0);
        this.mShrinkAnimator.setLoopedCount(0);
        this.mShrinkAnimator.setCurveType(8);
        this.mShrinkAnimator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            /* class ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.AnonymousClass9 */

            @Override // ohos.agp.animation.AnimatorValue.ValueUpdateListener
            public void onUpdate(AnimatorValue animatorValue, float f) {
                GravitationalPagerIndicatorAnimator.this.shrinkAnimatorUpdate(f);
                if (GravitationalPagerIndicatorAnimator.this.mDraw.getDampValue() > 0.0f) {
                    GravitationalPagerIndicatorAnimator gravitationalPagerIndicatorAnimator = GravitationalPagerIndicatorAnimator.this;
                    gravitationalPagerIndicatorAnimator.dampingUpdate(gravitationalPagerIndicatorAnimator.mDraw.getDampValue() - f);
                }
            }
        });
    }

    private void createmDampingAnimator() {
        this.mDampingAnimator = new GravitationalSelfDriveAnimator(null);
        this.mDampingAnimator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            /* class ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.AnonymousClass10 */

            @Override // ohos.agp.animation.AnimatorValue.ValueUpdateListener
            public void onUpdate(AnimatorValue animatorValue, float f) {
                GravitationalPagerIndicatorAnimator.this.dampingAnimatorUpdate(f);
            }
        });
    }

    private void createFinishAniamtor() {
        this.mFinishAnimator = new AnimatorValue();
        this.mFinishAnimator.setDuration(400);
        this.mFinishAnimator.setDelay(0);
        this.mFinishAnimator.setLoopedCount(0);
        this.mFinishAnimator.setCurveType(8);
        this.mFinishAnimator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            /* class ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.AnonymousClass11 */

            @Override // ohos.agp.animation.AnimatorValue.ValueUpdateListener
            public void onUpdate(AnimatorValue animatorValue, float f) {
                GravitationalPagerIndicatorAnimator.this.finishAnimatorUpdate(f);
            }
        });
        this.mFinishAnimator.setStateChangedListener(new Animator.StateChangedListener() {
            /* class ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.AnonymousClass12 */

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
            public void onStop(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onStart(Animator animator) {
                GravitationalPagerIndicatorAnimator.this.mSelfDriveAnimator.start();
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onEnd(Animator animator) {
                GravitationalPagerIndicatorAnimator.this.mDragValueEnd = 0.0f;
                GravitationalPagerIndicatorAnimator.this.mSelfDriveAnimator.stop();
            }
        });
    }

    private void initAnimator() {
        createSelfDriveAnimator();
        createWaitAnimator();
        createmSpringAnimator();
        createCircleAniamtor();
        createEnlargeAnimator();
        createShrikAnimator();
        createmDampingAnimator();
        createFinishAniamtor();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void finishAnimatorUpdate(float f) {
        float f2 = this.mDragValueEnd;
        this.mSelfDriveAnimator.updateX((f * (f2 > 0.5f ? 1.0f - f2 : -f2)) + f2);
    }

    private void initDraw(int i, int i2) {
        this.mAdapter.getParamaterFromComponent();
        this.mDraw.setTargetItem(i);
        this.mDraw.setRadius(this.mRadius);
        this.mDraw.setItemCount(this.mIndicatorCount);
        if (this.mIndcatorItemOffset == 0) {
            this.mIndcatorItemOffset = 30;
        }
        this.mDraw.setItemOffset((float) this.mIndcatorItemOffset);
        this.mDraw.setOri(calculateOriItemPosition());
        this.mDraw.setSelectedItem(i2);
        this.mDraw.setScale(this.mScale);
        this.mDraw.setComponentHeight((float) this.mIndicatorHeight);
        this.mDraw.setComponentWidth(calculateBackGroundWidth());
        this.mDraw.setDampScale(DAMPINGSCALE);
        this.mDraw.initParamater();
        this.mDampingThreshold = (((float) this.mComponentWidth) - calculateBackGroundWidth()) / 2.0f;
        setSelectedPosition(this.mDraw.calculateItemPosition(i));
    }

    private Point calculateOriItemPosition() {
        Point point = new Point((((float) this.mComponentWidth) - calculateBackGroundWidth()) / 2.0f, ((float) this.mComponentHeight) / 2.0f);
        point.modify(point.getPointX() + (((float) (this.mPaddingLeft - this.mPaddingRight)) / 2.0f), point.getPointY() + (((float) (this.mPaddingTop - this.mPaddingBottom)) / 2.0f));
        return point;
    }

    private float calculateBackGroundWidth() {
        float f = this.mRadius;
        int i = this.mIndcatorItemOffset;
        if (i == 0) {
            i = 30;
        }
        int i2 = this.mIndicatorCount;
        return i2 > 0 ? ((((float) i2) + 2.0f) * 2.0f * f) + ((float) ((i2 - 1) * i)) : (((float) i2) + 2.0f) * 2.0f * f;
    }

    public static class GravitationalSelfDriveAnimator {
        private boolean hadMove;
        private boolean isRunning;
        private Animator.TimelineCurve mCurve;
        private Animator.StateChangedListener mStateChangeListener;
        private AnimatorValue.ValueUpdateListener mUpdateListener;

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void setStateChangeListener(Animator.StateChangedListener stateChangedListener) {
            this.mStateChangeListener = stateChangedListener;
        }

        private GravitationalSelfDriveAnimator(Animator.TimelineCurve timelineCurve) {
            this.hadMove = false;
            this.isRunning = false;
            setInterpolator(timelineCurve);
        }

        private void setInterpolator(Animator.TimelineCurve timelineCurve) {
            this.mCurve = timelineCurve;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void setValueUpdateListener(AnimatorValue.ValueUpdateListener valueUpdateListener) {
            this.mUpdateListener = valueUpdateListener;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void updateX(float f) {
            float f2;
            if (this.isRunning) {
                Animator.TimelineCurve timelineCurve = this.mCurve;
                if (timelineCurve == null) {
                    f2 = f;
                } else {
                    f2 = timelineCurve.getCurvedTime(f);
                }
                int i = (f > 1.0f ? 1 : (f == 1.0f ? 0 : -1));
                if (i > 0 || f < 0.0f) {
                    if (i > 0) {
                        Animator.StateChangedListener stateChangedListener = this.mStateChangeListener;
                        if (stateChangedListener != null) {
                            stateChangedListener.onEnd(null);
                        }
                        this.mUpdateListener.onUpdate(null, 1.0f);
                    }
                    if (f < 0.0f) {
                        Animator.StateChangedListener stateChangedListener2 = this.mStateChangeListener;
                        if (stateChangedListener2 != null) {
                            stateChangedListener2.onCancel(null);
                        }
                        this.mUpdateListener.onUpdate(null, 0.0f);
                    }
                    this.isRunning = false;
                    return;
                }
                this.mUpdateListener.onUpdate(null, f2);
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void start() {
            this.hadMove = true;
            this.isRunning = true;
        }

        private void end() {
            this.isRunning = false;
            Animator.StateChangedListener stateChangedListener = this.mStateChangeListener;
            if (stateChangedListener != null && this.hadMove) {
                this.hadMove = false;
                stateChangedListener.onEnd(null);
            }
            this.mUpdateListener.onUpdate(null, 1.0f);
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void stop() {
            this.isRunning = false;
        }

        private void cancel() {
            this.isRunning = false;
            if (this.hadMove) {
                this.hadMove = false;
            }
            this.mUpdateListener.onUpdate(null, 0.0f);
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void setHadMove(boolean z) {
            this.hadMove = z;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private boolean isRunning() {
            return this.isRunning;
        }
    }

    public class IndicatorAdapter implements ComponentAdapter {
        private PageSliderIndicator mIndicator;

        @Override // ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.ComponentAdapter
        public void setAnimator(GravitationalPagerIndicatorAnimator gravitationalPagerIndicatorAnimator) {
        }

        public IndicatorAdapter() {
        }

        @Override // ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.ComponentAdapter
        public void setComponent(Component component) {
            if (component == null) {
                HiLog.info(GravitationalPagerIndicatorAnimator.LABEL_LOG, GravitationalPagerIndicatorAnimator.LOG_FORMAT, "GravitationalPagerIndicatorAnimator::setIndicator:", "indicator is null!");
            } else if (component instanceof PageSliderIndicator) {
                this.mIndicator = (PageSliderIndicator) component;
                getParamaterFromComponent();
                this.mIndicator.setWidth(-2);
                this.mIndicator.setHeight(-2);
            }
        }

        @Override // ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.ComponentAdapter
        public void getParamaterFromComponent() {
            PageSliderIndicator pageSliderIndicator = this.mIndicator;
            if (pageSliderIndicator != null) {
                GravitationalPagerIndicatorAnimator.this.mIndicatorCount = pageSliderIndicator.getCount();
                GravitationalPagerIndicatorAnimator.this.mIndcatorItemOffset = this.mIndicator.getItemOffset();
                GravitationalPagerIndicatorAnimator.this.mComponentHeight = this.mIndicator.getHeight();
                GravitationalPagerIndicatorAnimator.this.mComponentWidth = this.mIndicator.getWidth();
                GravitationalPagerIndicatorAnimator.this.mIndicatorSelected = this.mIndicator.getSelected();
                GravitationalPagerIndicatorAnimator gravitationalPagerIndicatorAnimator = GravitationalPagerIndicatorAnimator.this;
                gravitationalPagerIndicatorAnimator.mIndicatorHeight = (int) (gravitationalPagerIndicatorAnimator.mRadius * 6.0f);
                GravitationalPagerIndicatorAnimator.this.mComponentOriginX = this.mIndicator.getContentPositionX();
                GravitationalPagerIndicatorAnimator.this.mPaddingBottom = this.mIndicator.getPaddingBottom();
                GravitationalPagerIndicatorAnimator.this.mPaddingLeft = this.mIndicator.getPaddingLeft();
                GravitationalPagerIndicatorAnimator.this.mPaddingRight = this.mIndicator.getPaddingRight();
                GravitationalPagerIndicatorAnimator.this.mPaddingTop = this.mIndicator.getPaddingTop();
            }
        }

        @Override // ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.ComponentAdapter
        public void setSelected(int i) {
            PageSliderIndicator pageSliderIndicator = this.mIndicator;
            if (pageSliderIndicator != null) {
                pageSliderIndicator.setSelected(i);
                GravitationalPagerIndicatorAnimator.this.mIndicatorSelected = this.mIndicator.getSelected();
            }
        }

        @Override // ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.ComponentAdapter
        public void getSelected() {
            PageSliderIndicator pageSliderIndicator = this.mIndicator;
            if (pageSliderIndicator != null) {
                GravitationalPagerIndicatorAnimator.this.mIndicatorSelected = pageSliderIndicator.getSelected();
            }
        }

        @Override // ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.ComponentAdapter
        public void invalidate() {
            PageSliderIndicator pageSliderIndicator = this.mIndicator;
            if (pageSliderIndicator != null) {
                pageSliderIndicator.invalidate();
            }
        }

        @Override // ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.ComponentAdapter
        public Optional<Component> getComponent() {
            return Optional.of(this.mIndicator);
        }
    }

    public static class DefaultAdapter implements ComponentAdapter {
        @Override // ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.ComponentAdapter
        public void getParamaterFromComponent() {
        }

        @Override // ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.ComponentAdapter
        public void getSelected() {
        }

        @Override // ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.ComponentAdapter
        public void invalidate() {
        }

        @Override // ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.ComponentAdapter
        public void setAnimator(GravitationalPagerIndicatorAnimator gravitationalPagerIndicatorAnimator) {
        }

        @Override // ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.ComponentAdapter
        public void setComponent(Component component) {
        }

        @Override // ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.ComponentAdapter
        public void setSelected(int i) {
        }

        @Override // ohos.agp.animation.styledsolutions.GravitationalPagerIndicatorAnimator.ComponentAdapter
        public Optional<Component> getComponent() {
            return Optional.empty();
        }
    }
}
