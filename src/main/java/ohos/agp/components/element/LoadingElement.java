package ohos.agp.components.element;

import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorGroup;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.animation.timelinecurves.CubicBezierCurve;
import ohos.agp.animation.util.ValueKeyFrame;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.MaskFilter;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.render.Texture;
import ohos.agp.render.ThreeDimView;
import ohos.agp.utils.Color;
import ohos.agp.utils.Matrix;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.annotation.SystemApi;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;

@SystemApi
public class LoadingElement extends PixelMapElement {
    private static final int ALPHA_MAX_VALUE = 255;
    private static final float ALPHA_MAX_VALUE_F = 255.0f;
    private static final String ANIMATION_PROP_ALPHA = "alpha";
    private static final String ANIMATION_PROP_DEGREES = "degrees";
    private static final String ANIMATION_PROP_OFFSET = "offset";
    private static final String ANIMATION_PROP_SCALE = "scale";
    private static final float CAMERA_LOCATION_Y = 1.0f;
    private static final int CENTER_COORDINATE_DIVISOR = 2;
    protected static final int COMET_TAIL_RANGE_DEGREES = 60;
    private static final int DEFAULT_BACKGROUND_RING_ALPHA = 135;
    private static final float DEFAULT_BACKGROUND_RING_BLUR_RADIUS_DIP = 2.0f;
    private static final float DEFAULT_BACKGROUND_RING_STROKE_WIDTH_DIP = 3.0f;
    protected static final int DEFAULT_BITMAP_SIZE = 300;
    protected static final int DEFAULT_COMET_COLOR = -932813210;
    private static final float DEFAULT_COMET_RADIUS_DIP = 3.0f;
    private static final float DEFAULT_COMET_TAIL_ALPHA_TRANSFER_FACTOR = 0.82f;
    private static final int DEFAULT_COMET_TAIL_COUNT = 20;
    private static final float DEFAULT_ORBIT_RADIUS_DIP = 17.0f;
    private static final int DEFAULT_RING_ALPHA = 200;
    private static final float DEFAULT_RING_BLUR_RADIUS_DIP = 0.2f;
    protected static final int DEFAULT_RING_COLOR = -932813210;
    private static final float DEFAULT_RING_RADIUS_DIP = 10.5f;
    private static final float DEFAULT_RING_STROKE_WIDTH_DIP = 1.9f;
    private static final int DEFAULT_SIZE_DIP = 40;
    private static final float ORBIT_ROTATION_DEGREES = 23.3f;
    private static final int RADIUS_TO_DIAMETER_MULTIPLICAND = 2;
    protected static final int ROTATION_DURATION = 1200;
    protected static final float ROTATION_START_DEGREES = 35.0f;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "LoadingAnim");
    private final StaggeredDimensionEvaluator mBackgroundRingStrokeWidthEvaluator;
    Comet mComet;
    private final StaggeredDimensionEvaluator mCometRadiusEvaluator;
    private Component mComponent;
    private float mDensity = 0.0f;
    boolean mIsRotating = false;
    float mOffsetY = 0.0f;
    LayeredRing mRing;
    private final StaggeredDimensionEvaluator mRingBlurRadiusEvaluator;
    private final StaggeredDimensionEvaluator mRingStrokeWidthEvaluator;
    private Animator mRotationValueAnimator;
    private Animator mVerticalTransitionAnimator;

    /* access modifiers changed from: package-private */
    public interface BlurEffect {
        void applyTo(Paint paint);
    }

    /* access modifiers changed from: private */
    public static float convertDpToPx(float f, float f2) {
        return f * f2;
    }

    /* access modifiers changed from: private */
    public static float getCenterCoordinate(float f) {
        return f / 2.0f;
    }

    public static LoadingElement create(Component component, float f, boolean z) {
        ParamsBundle paramsBundle;
        PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
        initializationOptions.pixelFormat = PixelFormat.ARGB_8888;
        initializationOptions.size = new Size(300, 300);
        initializationOptions.editable = true;
        PixelMap create = PixelMap.create(initializationOptions);
        create.writePixels(Color.TRANSPARENT.getValue());
        if (z) {
            paramsBundle = ParamsBundle.createParamsBundleForNightMode(f);
        } else {
            paramsBundle = ParamsBundle.createParamsBundle(f);
        }
        int i = -932813210;
        LayeredRing createRing = createRing(z ? Color.WHITE.getValue() : -932813210, paramsBundle);
        if (z) {
            i = Color.WHITE.getValue();
        }
        return new LoadingElement(create, component, createRing, createComet(i, paramsBundle), 1200, f);
    }

    LoadingElement(PixelMap pixelMap, Component component, LayeredRing layeredRing, Comet comet, int i, float f) {
        super(pixelMap);
        this.mRing = layeredRing;
        this.mComet = comet;
        this.mComponent = component;
        this.mDensity = f;
        setupVerticalTranslateAnimator(i);
        setupRotationAnimator(i);
        this.mRingStrokeWidthEvaluator = CustomDimensionEvaluatorFactory.createForRingStrokeWidth(f);
        this.mRingBlurRadiusEvaluator = CustomDimensionEvaluatorFactory.createForRingBlurRadius(f);
        this.mBackgroundRingStrokeWidthEvaluator = CustomDimensionEvaluatorFactory.createForBackgroundRingStrokeWidth(f);
        this.mCometRadiusEvaluator = CustomDimensionEvaluatorFactory.createForCometRadius(f);
        setBounds(getBounds());
    }

    public void start() {
        startRotation(true);
    }

    public void stop() {
        stopRotation();
    }

    @Override // ohos.agp.components.element.Element
    public void setBounds(Rect rect) {
        if (rect != null) {
            super.setBounds(rect);
            int min = Math.min(rect.getWidth(), rect.getHeight());
            updateNonLinearScaledDimensions(min);
            this.mRing.updateSize(min);
            this.mComet.updateSize(min);
        }
    }

    @Override // ohos.agp.components.element.Element
    public void drawToCanvas(Canvas canvas) {
        if (canvas != null) {
            canvas.save();
            canvas.translate(0.0f, this.mOffsetY);
            this.mRing.drawOnCanvas(canvas, getBounds());
            this.mComet.drawOnCanvas(canvas, getBounds(), ORBIT_ROTATION_DEGREES);
            canvas.restore();
        }
    }

    public boolean isRunning() {
        return this.mIsRotating;
    }

    public void setColor(int i) {
        this.mComet.updateColor(i);
        this.mRing.updateColor(i);
    }

    /* access modifiers changed from: protected */
    public void invalidateSelf() {
        Component component = this.mComponent;
        if (component != null) {
            component.invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void startRotation(boolean z) {
        if (!this.mIsRotating) {
            if (z) {
                this.mComet.mTail.maximiseTailRangeDegrees();
            }
            this.mRotationValueAnimator.start();
            this.mVerticalTransitionAnimator.start();
            this.mIsRotating = true;
        }
    }

    /* access modifiers changed from: protected */
    public void stopRotation() {
        if (this.mIsRotating) {
            this.mRotationValueAnimator.stop();
            this.mVerticalTransitionAnimator.stop();
            this.mIsRotating = false;
        }
    }

    static LayeredRing createRing(int i, ParamsBundle paramsBundle) {
        if (paramsBundle.mIsNightMode && paramsBundle.mBackgroundRingParams == null) {
            throw new IllegalArgumentException("create for night mode, but BackgroundRingParams is null");
        } else if (paramsBundle.mIsNightMode) {
            return createRingForNightMode(i, paramsBundle.mRingParams, paramsBundle.mBackgroundRingParams);
        } else {
            return createRing(i, paramsBundle.mRingParams);
        }
    }

    static LayeredRing createRing(int i, RingParams ringParams) {
        return new LayeredRing(ringParams.mInitSize, new PaintedRing(i, ringParams.mRadius, ringParams.mStrokeWidth, ringParams.mAlpha), false);
    }

    static LayeredRing createRingForNightMode(int i, RingParams ringParams, BackgroundRingParams backgroundRingParams) {
        PaintedRing paintedRing = new PaintedRing(i, ringParams.mRadius, backgroundRingParams.mStrokeWidth, backgroundRingParams.mAlpha);
        paintedRing.applyBlurEffect(BlurMaskFilterEffect.ofNormalType(backgroundRingParams.mBlurRadius));
        PaintedRing paintedRing2 = new PaintedRing(i, ringParams.mRadius, ringParams.mStrokeWidth);
        paintedRing2.applyBlurEffect(BlurMaskFilterEffect.ofNormalType(ringParams.mBlurRadius));
        return new LayeredRing(ringParams.mInitSize, paintedRing2, paintedRing, true);
    }

    static Comet createComet(int i, ParamsBundle paramsBundle) {
        return new Comet(i, paramsBundle.mCometParams.mInitSize, paramsBundle.mCometParams.mRadius, paramsBundle.mCometParams.mOrbitRadius, paramsBundle.mCometTailParams);
    }

    private void setupVerticalTranslateAnimator(int i) {
        this.mVerticalTransitionAnimator = AnimatorFactory.createVerticalTransition((long) i, this.mRing.mForegroundRing.mRingRadius * 2.0f, new AnimatorValue.ValueUpdateListener() {
            /* class ohos.agp.components.element.LoadingElement.AnonymousClass1 */

            @Override // ohos.agp.animation.AnimatorValue.ValueUpdateListener
            public void onUpdate(AnimatorValue animatorValue, float f) {
                LoadingElement.this.mOffsetY = ((float) animatorValue.estimateValue(LoadingElement.ANIMATION_PROP_OFFSET, f)) * LoadingElement.this.mRing.mSizeRatio;
                LoadingElement.this.invalidateSelf();
            }
        });
    }

    private void setupRotationAnimator(int i) {
        this.mRotationValueAnimator = AnimatorFactory.createRotationTransition(i, ROTATION_START_DEGREES, new AnimatorValue.ValueUpdateListener() {
            /* class ohos.agp.components.element.LoadingElement.AnonymousClass2 */

            @Override // ohos.agp.animation.AnimatorValue.ValueUpdateListener
            public void onUpdate(AnimatorValue animatorValue, float f) {
                LoadingElement.this.mComet.updateAnimatedProperties((float) animatorValue.estimateValue(LoadingElement.ANIMATION_PROP_DEGREES, f), (int) animatorValue.estimateValue("alpha", f), (float) animatorValue.estimateValue(LoadingElement.ANIMATION_PROP_SCALE, f));
                LoadingElement.this.invalidateSelf();
            }
        });
    }

    private void updateNonLinearScaledDimensions(int i) {
        float f = (float) i;
        this.mRing.mForegroundRing.updateStrokeWidth(this.mRingStrokeWidthEvaluator.evaluate(f));
        this.mComet.mCometRadius = this.mCometRadiusEvaluator.evaluate(f);
        Comet comet = this.mComet;
        comet.mDrawingCometRadius = comet.mCometRadius;
        if (this.mRing.mIsNightMode) {
            this.mRing.mForegroundRing.applyBlurEffect(BlurMaskFilterEffect.ofNormalType(this.mRingBlurRadiusEvaluator.evaluate(f)));
            if (this.mRing.mBackgroundRing != null) {
                this.mRing.mBackgroundRing.updateStrokeWidth(this.mBackgroundRingStrokeWidthEvaluator.evaluate(f));
            }
        }
    }

    public static class ParamsBundle {
        private final BackgroundRingParams mBackgroundRingParams;
        private final CometParams mCometParams;
        private final CometTailParams mCometTailParams;
        private final boolean mIsNightMode;
        private final RingParams mRingParams;

        ParamsBundle(RingParams ringParams, CometParams cometParams, CometTailParams cometTailParams, boolean z) {
            this(ringParams, cometParams, cometTailParams, null, z);
        }

        ParamsBundle(RingParams ringParams, CometParams cometParams, CometTailParams cometTailParams, BackgroundRingParams backgroundRingParams, boolean z) {
            this.mRingParams = ringParams;
            this.mCometParams = cometParams;
            this.mCometTailParams = cometTailParams;
            this.mIsNightMode = z;
            this.mBackgroundRingParams = backgroundRingParams;
        }

        static ParamsBundle createParamsBundle(float f) {
            float f2 = f * 40.0f;
            return new ParamsBundle(new RingParams(LoadingElement.DEFAULT_RING_RADIUS_DIP, LoadingElement.DEFAULT_RING_STROKE_WIDTH_DIP, LoadingElement.DEFAULT_RING_BLUR_RADIUS_DIP, 200, f2, f), new CometParams(3.0f, LoadingElement.DEFAULT_ORBIT_RADIUS_DIP, f2, f), new CometTailParams(20, 60.0f, LoadingElement.DEFAULT_COMET_TAIL_ALPHA_TRANSFER_FACTOR), false);
        }

        static ParamsBundle createParamsBundleForNightMode(float f) {
            float f2 = f * 40.0f;
            RingParams ringParams = new RingParams(LoadingElement.DEFAULT_RING_RADIUS_DIP, LoadingElement.DEFAULT_RING_STROKE_WIDTH_DIP, LoadingElement.DEFAULT_RING_BLUR_RADIUS_DIP, 200, f2, f);
            BackgroundRingParams backgroundRingParams = new BackgroundRingParams(3.0f, 2.0f, 135, f);
            return new ParamsBundle(ringParams, new CometParams(3.0f, LoadingElement.DEFAULT_ORBIT_RADIUS_DIP, f2, f), new CometTailParams(20, 60.0f, LoadingElement.DEFAULT_COMET_TAIL_ALPHA_TRANSFER_FACTOR), backgroundRingParams, true);
        }
    }

    public static class RingParams {
        private final int mAlpha;
        private final float mBlurRadius;
        private final float mInitSize;
        private final float mRadius;
        private final float mStrokeWidth;

        public RingParams(float f, float f2, float f3, int i, float f4, float f5) {
            this.mRadius = LoadingElement.convertDpToPx(f, f5);
            this.mStrokeWidth = LoadingElement.convertDpToPx(f2, f5);
            this.mBlurRadius = LoadingElement.convertDpToPx(f3, f5);
            this.mInitSize = f4;
            this.mAlpha = i;
        }
    }

    public static class BackgroundRingParams {
        private final int mAlpha;
        private final float mBlurRadius;
        private final float mStrokeWidth;

        public BackgroundRingParams(float f, float f2, int i, float f3) {
            this.mStrokeWidth = LoadingElement.convertDpToPx(f, f3);
            this.mBlurRadius = LoadingElement.convertDpToPx(f2, f3);
            this.mAlpha = i;
        }
    }

    public static class CometParams {
        private final float mInitSize;
        private final float mOrbitRadius;
        private final float mRadius;

        public CometParams(float f, float f2, float f3, float f4) {
            this.mRadius = LoadingElement.convertDpToPx(f, f4);
            this.mOrbitRadius = LoadingElement.convertDpToPx(f2, f4);
            this.mInitSize = f3;
        }
    }

    public static final class CometTailParams {
        private final float mAlphaTransferFactor;
        private final int mTailCount;
        private final float mTailLengthDegrees;

        public CometTailParams(int i, float f, float f2) {
            this.mTailCount = i;
            this.mTailLengthDegrees = f;
            this.mAlphaTransferFactor = f2;
        }
    }

    /* access modifiers changed from: package-private */
    public static class PaintedRing {
        private final int mAlpha;
        private final Paint mPaint;
        private final int mRingColor;
        private final float mRingRadius;
        private float mRingStrokeWidth;

        PaintedRing(int i, float f, float f2) {
            this(i, f, f2, 255);
        }

        PaintedRing(int i, float f, float f2, int i2) {
            this.mPaint = new Paint();
            this.mRingColor = i;
            this.mRingRadius = f;
            this.mRingStrokeWidth = f2;
            this.mAlpha = i2;
            setupRingPaint();
        }

        /* access modifiers changed from: package-private */
        public void updateRingColor(int i) {
            this.mPaint.setColor(new Color(i));
            this.mPaint.setAlpha(((float) this.mAlpha) / LoadingElement.ALPHA_MAX_VALUE_F);
        }

        /* access modifiers changed from: package-private */
        public void updateStrokeWidth(float f) {
            this.mRingStrokeWidth = f;
            this.mPaint.setStrokeWidth(this.mRingStrokeWidth);
        }

        /* access modifiers changed from: package-private */
        public void applyBlurEffect(BlurEffect blurEffect) {
            blurEffect.applyTo(this.mPaint);
        }

        /* access modifiers changed from: package-private */
        public void drawOnCanvas(Canvas canvas, float f, float f2) {
            canvas.drawCircle(f, f2, this.mRingRadius, this.mPaint);
        }

        private void setupRingPaint() {
            this.mPaint.setAntiAlias(true);
            this.mPaint.setStyle(Paint.Style.STROKE_STYLE);
            this.mPaint.setStrokeWidth(this.mRingStrokeWidth);
            this.mPaint.setColor(new Color(this.mRingColor));
            this.mPaint.setAlpha(((float) this.mAlpha) / LoadingElement.ALPHA_MAX_VALUE_F);
        }
    }

    /* access modifiers changed from: package-private */
    public static class LayeredRing {
        private final PaintedRing mBackgroundRing;
        private float mDrawingScale;
        private final PaintedRing mForegroundRing;
        private final float mInitSize;
        private PixelMap mInternalBitmap;
        private float mInternalBitmapSize;
        private Canvas mInternalCanvas;
        private boolean mIsNightMode;
        private PixelMapHolder mMapHolder;
        private final Paint mPaint;
        private float mSizeRatio;

        LayeredRing(float f, PaintedRing paintedRing, boolean z) {
            this(f, paintedRing, null, z);
        }

        LayeredRing(float f, PaintedRing paintedRing, PaintedRing paintedRing2, boolean z) {
            this.mPaint = new Paint();
            this.mIsNightMode = false;
            this.mDrawingScale = 1.0f;
            this.mSizeRatio = 1.0f;
            this.mInitSize = f;
            this.mForegroundRing = paintedRing;
            this.mBackgroundRing = paintedRing2;
            this.mIsNightMode = z;
            this.mPaint.setAntiAlias(true);
            initBitmap(300);
            drawInternalBitmap();
        }

        /* access modifiers changed from: package-private */
        public void updateSize(int i) {
            if (this.mInternalBitmap == null || ((float) i) > this.mInternalBitmapSize) {
                initBitmap(i);
            } else {
                clearBitmap();
            }
            this.mSizeRatio = ((float) i) / this.mInitSize;
            drawInternalBitmap();
        }

        /* access modifiers changed from: package-private */
        public void setScale(float f) {
            this.mDrawingScale = f;
        }

        /* access modifiers changed from: package-private */
        public void setAlpha(float f) {
            this.mPaint.setAlpha(f);
        }

        /* access modifiers changed from: package-private */
        public void drawOnCanvas(Canvas canvas, Rect rect) {
            canvas.save();
            float f = this.mDrawingScale;
            canvas.scale(f, f, (float) rect.getCenterX(), (float) rect.getCenterY());
            PixelMapHolder pixelMapHolder = this.mMapHolder;
            if (pixelMapHolder == null) {
                this.mMapHolder = new PixelMapHolder(this.mInternalBitmap);
            } else {
                pixelMapHolder.resetPixelMap(this.mInternalBitmap);
            }
            canvas.drawPixelMapHolder(this.mMapHolder, (float) rect.left, (float) rect.top, this.mPaint);
            canvas.restore();
        }

        /* access modifiers changed from: package-private */
        public void updateColor(int i) {
            this.mForegroundRing.updateRingColor(i);
            PaintedRing paintedRing = this.mBackgroundRing;
            if (paintedRing != null) {
                paintedRing.updateRingColor(i);
            }
            clearBitmap();
            drawInternalBitmap();
        }

        private void initBitmap(int i) {
            PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
            initializationOptions.pixelFormat = PixelFormat.ARGB_8888;
            initializationOptions.size = new Size(i, i);
            initializationOptions.editable = true;
            this.mInternalBitmap = PixelMap.create(initializationOptions);
            this.mInternalCanvas = new Canvas(new Texture(this.mInternalBitmap));
            this.mInternalBitmapSize = (float) i;
        }

        private void clearBitmap() {
            this.mInternalBitmap.writePixels(Color.TRANSPARENT.getValue());
        }

        private void drawInternalBitmap() {
            this.mInternalCanvas.save();
            Canvas canvas = this.mInternalCanvas;
            float f = this.mSizeRatio;
            canvas.scale(f, f);
            float centerCoordinate = LoadingElement.getCenterCoordinate(this.mInitSize);
            PaintedRing paintedRing = this.mBackgroundRing;
            if (paintedRing != null) {
                paintedRing.drawOnCanvas(this.mInternalCanvas, centerCoordinate, centerCoordinate);
            }
            this.mForegroundRing.drawOnCanvas(this.mInternalCanvas, centerCoordinate, centerCoordinate);
            this.mInternalCanvas.restore();
        }
    }

    /* access modifiers changed from: package-private */
    public static class Comet {
        private static final int INIT_ROTATE_DEGREES = -90;
        private final CometHeadCircle mCometHeadCircle;
        private final Paint mCometPaint = new Paint();
        private float mCometRadius;
        private float mDrawingCometRadius;
        private final float mInitSize;
        private final float mLocationOffsetY;
        private float mSizeRatio = 1.0f;
        private final Tail mTail;

        Comet(int i, float f, float f2, float f3, CometTailParams cometTailParams) {
            this.mInitSize = f;
            this.mCometPaint.setAntiAlias(true);
            this.mCometRadius = f2;
            this.mDrawingCometRadius = this.mCometRadius;
            Point point = new Point(f3, 0.0f);
            PointFRotationCalculator aroundAxisY = PointFRotationCalculator.aroundAxisY(-90.0f);
            aroundAxisY.setLocationY(1.0f);
            this.mLocationOffsetY = aroundAxisY.rotatePointF(point, 0.0f).getPointY();
            this.mCometHeadCircle = new CometHeadCircle(aroundAxisY, point, i);
            this.mTail = new Tail(point, aroundAxisY, cometTailParams, i);
        }

        /* access modifiers changed from: package-private */
        public void updateSize(int i) {
            this.mSizeRatio = ((float) i) / this.mInitSize;
        }

        /* access modifiers changed from: package-private */
        public void drawOnCanvas(Canvas canvas, Rect rect, float f) {
            canvas.saveLayer(new RectFloat(rect), this.mCometPaint);
            float f2 = this.mSizeRatio;
            canvas.scale(f2, f2);
            float centerCoordinate = LoadingElement.getCenterCoordinate(this.mInitSize);
            canvas.translate(centerCoordinate, centerCoordinate);
            canvas.rotate(-f, 0.0f, 0.0f);
            canvas.translate(0.0f, -this.mLocationOffsetY);
            this.mCometHeadCircle.drawCircle(canvas, this.mDrawingCometRadius);
            this.mTail.drawTail(canvas, this.mDrawingCometRadius);
            canvas.restore();
        }

        /* access modifiers changed from: package-private */
        public void updateAnimatedProperties(float f, int i, float f2) {
            rotateCometTo(f);
            this.mCometPaint.setAlpha(((float) i) / LoadingElement.ALPHA_MAX_VALUE_F);
            this.mDrawingCometRadius = this.mCometRadius * f2;
        }

        /* access modifiers changed from: package-private */
        public void rotateCometTo(float f) {
            this.mCometHeadCircle.rotateTo(f);
            this.mTail.rotateTo(f);
        }

        /* access modifiers changed from: package-private */
        public void updateTailRangeDegrees(float f) {
            this.mTail.updateTailRangeDegrees(f);
        }

        /* access modifiers changed from: package-private */
        public void updateColor(int i) {
            this.mCometHeadCircle.mCirclePaint.setColor(new Color(i));
            this.mTail.mTailPaint.setColor(new Color(i));
        }

        /* access modifiers changed from: package-private */
        public void setAlpha(float f) {
            this.mCometPaint.setAlpha(f);
        }

        /* access modifiers changed from: package-private */
        public static class CometHeadCircle {
            private final Paint mCirclePaint = new Paint();
            private final Point mCurrentCenter;
            private final Point mInitCenter;
            private final PointFRotationCalculator mRotationCalculator;

            CometHeadCircle(PointFRotationCalculator pointFRotationCalculator, Point point, int i) {
                this.mRotationCalculator = pointFRotationCalculator;
                this.mInitCenter = point;
                this.mCurrentCenter = new Point(point.getPointX(), point.getPointY());
                setupCirclePaint(i);
            }

            /* access modifiers changed from: package-private */
            public void rotateTo(float f) {
                this.mRotationCalculator.rotatePointF(this.mInitCenter, this.mCurrentCenter, f);
            }

            /* access modifiers changed from: package-private */
            public void drawCircle(Canvas canvas, float f) {
                canvas.drawCircle(this.mCurrentCenter.getPointX(), this.mCurrentCenter.getPointY(), f, this.mCirclePaint);
            }

            private void setupCirclePaint(int i) {
                this.mCirclePaint.setStyle(Paint.Style.FILL_STYLE);
                this.mCirclePaint.setColor(new Color(i));
                this.mCirclePaint.setAntiAlias(true);
            }
        }

        /* access modifiers changed from: package-private */
        public static class Tail {
            private int mCurrentTailCircleCount;
            private float mCurrentTailRangeDegrees;
            private final Point mInitCenter;
            private final float mMaxTailRangeDegrees;
            private final TailCircle[] mTailCircles;
            private final Paint mTailPaint = new Paint();
            private final int mTotalTailCount;

            private int calculateAlpha(int i, float f) {
                return (int) (((float) i) * f);
            }

            Tail(Point point, PointFRotationCalculator pointFRotationCalculator, CometTailParams cometTailParams, int i) {
                this.mInitCenter = point;
                this.mTotalTailCount = cometTailParams.mTailCount;
                this.mMaxTailRangeDegrees = cometTailParams.mTailLengthDegrees;
                this.mCurrentTailRangeDegrees = cometTailParams.mTailLengthDegrees;
                this.mTailCircles = new TailCircle[this.mTotalTailCount];
                float f = cometTailParams.mAlphaTransferFactor;
                int i2 = 0;
                while (true) {
                    TailCircle[] tailCircleArr = this.mTailCircles;
                    if (i2 < tailCircleArr.length) {
                        if (i2 == 0) {
                            tailCircleArr[i2] = new TailCircle(pointFRotationCalculator, calculateAlpha(255, f));
                        } else {
                            tailCircleArr[i2] = new TailCircle(pointFRotationCalculator, calculateAlpha(tailCircleArr[i2 - 1].mAlpha, f));
                        }
                        i2++;
                    } else {
                        setupTailPaint(i);
                        return;
                    }
                }
            }

            /* access modifiers changed from: package-private */
            public void updateTailRangeDegrees(float f) {
                this.mCurrentTailRangeDegrees = Math.min(f, this.mMaxTailRangeDegrees);
            }

            /* access modifiers changed from: package-private */
            public void maximiseTailRangeDegrees() {
                this.mCurrentTailRangeDegrees = this.mMaxTailRangeDegrees;
            }

            /* access modifiers changed from: package-private */
            public void rotateTo(float f) {
                float f2 = this.mMaxTailRangeDegrees;
                int i = 0;
                if (f2 <= 0.0f) {
                    this.mCurrentTailCircleCount = 0;
                    return;
                }
                this.mCurrentTailCircleCount = (int) ((this.mCurrentTailRangeDegrees / f2) * ((float) this.mTotalTailCount));
                while (true) {
                    int i2 = this.mCurrentTailCircleCount;
                    if (i < i2) {
                        int i3 = i + 1;
                        this.mTailCircles[i].evaluate(((float) i3) / ((float) i2), this.mInitCenter, f, this.mCurrentTailRangeDegrees);
                        i = i3;
                    } else {
                        return;
                    }
                }
            }

            /* access modifiers changed from: package-private */
            public void drawTail(Canvas canvas, float f) {
                for (int i = 0; i < this.mCurrentTailCircleCount; i++) {
                    this.mTailCircles[i].drawCircle(canvas, this.mTailPaint, f);
                }
            }

            private void setupTailPaint(int i) {
                this.mTailPaint.setColor(new Color(i));
                this.mTailPaint.setStyle(Paint.Style.FILL_STYLE);
                this.mTailPaint.setAntiAlias(true);
            }
        }

        /* access modifiers changed from: package-private */
        public static class TailCircle {
            private int mAlpha;
            private final Point mCenterPoint = new Point();
            private final PointFRotationCalculator mRotationCalculator;

            TailCircle(PointFRotationCalculator pointFRotationCalculator, int i) {
                this.mRotationCalculator = pointFRotationCalculator;
                this.mAlpha = i;
            }

            /* access modifiers changed from: package-private */
            public void drawCircle(Canvas canvas, Paint paint, float f) {
                int i = this.mAlpha;
                if (i != 0) {
                    paint.setAlpha(((float) i) / LoadingElement.ALPHA_MAX_VALUE_F);
                    canvas.drawCircle(this.mCenterPoint.getPointX(), this.mCenterPoint.getPointY(), f, paint);
                }
            }

            /* access modifiers changed from: package-private */
            public void evaluate(float f, Point point, float f2, float f3) {
                this.mRotationCalculator.rotatePointF(point, this.mCenterPoint, f2 + (f * (-f3)));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public static class BlurMaskFilterEffect extends MaskFilter implements BlurEffect {
        private final float blurRadius;
        private final MaskFilter.Blur blurStyle;

        BlurMaskFilterEffect(float f, MaskFilter.Blur blur) {
            super(f, blur);
            this.blurRadius = f;
            this.blurStyle = blur;
        }

        @Override // ohos.agp.components.element.LoadingElement.BlurEffect
        public void applyTo(Paint paint) {
            paint.setMaskFilter(null);
            paint.setMaskFilter(new MaskFilter(this.blurRadius, this.blurStyle));
        }

        static BlurMaskFilterEffect ofNormalType(float f) {
            HiLog.error(LoadingElement.TAG, "BlurMaskFilterEffect %{public}f", Float.valueOf(f));
            return new BlurMaskFilterEffect(f, MaskFilter.Blur.NORMAL);
        }
    }

    /* access modifiers changed from: package-private */
    public static class AnimatorFactory {
        private static final float ALPHA_KEY_FRAME_FRACTION_1 = 0.0f;
        private static final float ALPHA_KEY_FRAME_FRACTION_2 = 0.4f;
        private static final float ALPHA_KEY_FRAME_FRACTION_3 = 0.8f;
        private static final float ALPHA_KEY_FRAME_FRACTION_4 = 1.0f;
        private static final int ALPHA_KEY_FRAME_VALUE_1 = 255;
        private static final int ALPHA_KEY_FRAME_VALUE_2 = 51;
        private static final int ALPHA_KEY_FRAME_VALUE_3 = 255;
        private static final int ALPHA_KEY_FRAME_VALUE_4 = 255;
        private static final float ROTATION_CYCLE_DEGREES = 360.0f;
        private static final float SCALE_KEY_FRAME_FRACTION_1 = 0.0f;
        private static final float SCALE_KEY_FRAME_FRACTION_2 = 0.4f;
        private static final float SCALE_KEY_FRAME_FRACTION_3 = 0.9f;
        private static final float SCALE_KEY_FRAME_FRACTION_4 = 1.0f;
        private static final float SCALE_KEY_FRAME_VALUE_1 = 0.93f;
        private static final float SCALE_KEY_FRAME_VALUE_2 = 0.65f;
        private static final float SCALE_KEY_FRAME_VALUE_3 = 1.0f;
        private static final float SCALE_KEY_FRAME_VALUE_4 = 0.93f;
        private static final float VERTICAL_TRANSITION_CONTROL_POINT_X1 = 0.33f;
        private static final float VERTICAL_TRANSITION_CONTROL_POINT_X2 = 0.67f;
        private static final float VERTICAL_TRANSITION_CONTROL_POINT_Y1 = 0.0f;
        private static final float VERTICAL_TRANSITION_CONTROL_POINT_Y2 = 1.0f;
        private static final int VERTICAL_TRANSITION_CYCLE_DURATION_RATIO = 2;
        private static final float VERTICAL_TRANSITION_CYCLE_FRACTION_1 = 0.0f;
        private static final float VERTICAL_TRANSITION_CYCLE_FRACTION_2 = 1.0f;
        private static final float VERTICAL_TRANSITION_INIT_CONTROL_POINT_X1 = 0.0f;
        private static final float VERTICAL_TRANSITION_INIT_CONTROL_POINT_X2 = 0.67f;
        private static final float VERTICAL_TRANSITION_INIT_CONTROL_POINT_Y1 = 0.0f;
        private static final float VERTICAL_TRANSITION_INIT_CONTROL_POINT_Y2 = 1.0f;
        private static final int VERTICAL_TRANSITION_INIT_DURATION_RATIO = 4;
        private static final float VERTICAL_TRANSLATE_HEIGHT_FACTOR = 0.06f;

        AnimatorFactory() {
        }

        static Animator createVerticalTransition(long j, float f, AnimatorValue.ValueUpdateListener valueUpdateListener) {
            float f2 = f * VERTICAL_TRANSLATE_HEIGHT_FACTOR;
            AnimatorValue createForVerticalTransitionInit = createForVerticalTransitionInit(j, f2);
            createForVerticalTransitionInit.setValueUpdateListener(valueUpdateListener);
            AnimatorGroup createForVerticalTransition = createForVerticalTransition(j, f2, valueUpdateListener);
            AnimatorGroup animatorGroup = new AnimatorGroup();
            animatorGroup.runSerially(createForVerticalTransitionInit, createForVerticalTransition);
            return animatorGroup;
        }

        private static AnimatorValue createForVerticalTransitionInit(long j, float f) {
            AnimatorValue animatorValue = new AnimatorValue();
            animatorValue.newValueContainer(LoadingElement.ANIMATION_PROP_OFFSET).setFloat(0.0f, -f);
            animatorValue.setCurve(new CubicBezierCurve(0.0f, 0.0f, 0.67f, 1.0f));
            animatorValue.setDuration(j / 4);
            return animatorValue;
        }

        private static AnimatorGroup createForVerticalTransition(long j, float f, AnimatorValue.ValueUpdateListener valueUpdateListener) {
            AnimatorValue animatorValue = new AnimatorValue();
            float f2 = -f;
            animatorValue.newValueContainer(LoadingElement.ANIMATION_PROP_OFFSET).setKeyFrame(ValueKeyFrame.createFloatFrame(0.0f, f2), ValueKeyFrame.createFloatFrame(1.0f, f));
            long j2 = j / 2;
            animatorValue.setDuration(j2);
            animatorValue.setCurve(new CubicBezierCurve(VERTICAL_TRANSITION_CONTROL_POINT_X1, 0.0f, 0.67f, 1.0f));
            animatorValue.setValueUpdateListener(valueUpdateListener);
            AnimatorValue animatorValue2 = new AnimatorValue();
            animatorValue2.newValueContainer(LoadingElement.ANIMATION_PROP_OFFSET).setKeyFrame(ValueKeyFrame.createFloatFrame(0.0f, f), ValueKeyFrame.createFloatFrame(1.0f, f2));
            animatorValue2.setDuration(j2);
            animatorValue2.setCurve(new CubicBezierCurve(VERTICAL_TRANSITION_CONTROL_POINT_X1, 0.0f, 0.67f, 1.0f));
            animatorValue2.setValueUpdateListener(valueUpdateListener);
            AnimatorGroup animatorGroup = new AnimatorGroup();
            animatorGroup.runSerially(animatorValue, animatorValue2);
            animatorGroup.setLoopedCount(-1);
            return animatorGroup;
        }

        static AnimatorValue createRotationTransition(int i, float f, AnimatorValue.ValueUpdateListener valueUpdateListener) {
            AnimatorValue animatorValue = new AnimatorValue();
            animatorValue.newValueContainer(LoadingElement.ANIMATION_PROP_SCALE).setKeyFrame(ValueKeyFrame.createFloatFrame(0.0f, 0.93f), ValueKeyFrame.createFloatFrame(0.4f, SCALE_KEY_FRAME_VALUE_2), ValueKeyFrame.createFloatFrame(SCALE_KEY_FRAME_FRACTION_3, 1.0f), ValueKeyFrame.createFloatFrame(1.0f, 0.93f));
            animatorValue.newValueContainer("alpha").setKeyFrame(ValueKeyFrame.createFloatFrame(0.0f, LoadingElement.ALPHA_MAX_VALUE_F), ValueKeyFrame.createFloatFrame(0.4f, 51.0f), ValueKeyFrame.createFloatFrame(ALPHA_KEY_FRAME_FRACTION_3, LoadingElement.ALPHA_MAX_VALUE_F), ValueKeyFrame.createFloatFrame(1.0f, LoadingElement.ALPHA_MAX_VALUE_F));
            animatorValue.newValueContainer(LoadingElement.ANIMATION_PROP_DEGREES).setFloat(f, ROTATION_CYCLE_DEGREES + f);
            animatorValue.setDuration((long) i);
            animatorValue.setCurveType(8);
            animatorValue.setLoopedCount(-1);
            animatorValue.setValueUpdateListener(valueUpdateListener);
            return animatorValue;
        }
    }

    /* access modifiers changed from: package-private */
    public static abstract class PointFRotationCalculator {
        final ThreeDimView mCamera;
        private final Matrix mMatrix;

        /* access modifiers changed from: package-private */
        public abstract void rotate(float f);

        private PointFRotationCalculator(float f) {
            this.mCamera = new ThreeDimView();
            this.mMatrix = new Matrix();
            rotate(f);
            this.mCamera.save();
        }

        /* access modifiers changed from: package-private */
        public void setLocationX(float f) {
            ThreeDimView threeDimView = this.mCamera;
            threeDimView.setLocation(f, threeDimView.getLocationY(), this.mCamera.getLocationZ());
        }

        /* access modifiers changed from: package-private */
        public void setLocationY(float f) {
            ThreeDimView threeDimView = this.mCamera;
            threeDimView.setLocation(threeDimView.getLocationX(), f, this.mCamera.getLocationZ());
        }

        /* access modifiers changed from: package-private */
        public void setLocationZ(float f) {
            ThreeDimView threeDimView = this.mCamera;
            threeDimView.setLocation(threeDimView.getLocationX(), this.mCamera.getLocationY(), f);
        }

        /* access modifiers changed from: package-private */
        public Point rotatePointF(Point point, float f) {
            Point point2 = new Point();
            rotate(f);
            transform(point, point2);
            resetCamera();
            return point2;
        }

        /* access modifiers changed from: package-private */
        public void rotatePointF(Point point, Point point2, float f) {
            rotate(f);
            transform(point, point2);
            resetCamera();
        }

        private void transform(Point point, Point point2) {
            float[] pointFToFloatArray = pointFToFloatArray(point);
            this.mCamera.getMatrix(this.mMatrix);
            this.mMatrix.mapPoints(pointFToFloatArray);
            point2.modify(pointFToFloatArray[0], pointFToFloatArray[1]);
        }

        private static float[] pointFToFloatArray(Point point) {
            return new float[]{point.getPointX(), point.getPointY()};
        }

        private void resetCamera() {
            this.mCamera.restore();
            this.mCamera.save();
        }

        static PointFRotationCalculator aroundAxisY(float f) {
            return new PointFRotationCalculator(f) {
                /* class ohos.agp.components.element.LoadingElement.PointFRotationCalculator.AnonymousClass1 */

                /* access modifiers changed from: protected */
                @Override // ohos.agp.components.element.LoadingElement.PointFRotationCalculator
                public void rotate(float f) {
                    this.mCamera.rotateY(f);
                }
            };
        }
    }

    static class CustomDimensionEvaluatorFactory {
        private static final float[] BACKGROUND_RING_STROKE_WIDTH_DIMENSIONS = {3.0f, 3.0f, 2.0f};
        private static final float[] COMET_RADIUS_DIMENSIONS = {3.0f, 3.0f, 2.2f};
        private static final int END_POINT_INDEX = 2;
        private static final int MIDDLE_POINT_INDEX = 1;
        private static final float[] RING_BLUR_RADIUS_DIMENSIONS = {0.5f, LoadingElement.DEFAULT_RING_BLUR_RADIUS_DIP, 0.1f};
        private static final float[] RING_STROKE_WIDTH_DIMENSIONS = {2.8f, LoadingElement.DEFAULT_RING_STROKE_WIDTH_DIP, 1.2f};
        private static final float[] SIZE_DIMENSIONS = {16.0f, 40.0f, 76.0f};
        private static final int START_POINT_INDEX = 0;

        CustomDimensionEvaluatorFactory() {
        }

        static StaggeredDimensionEvaluator createForRingStrokeWidth(float f) {
            return create(RING_STROKE_WIDTH_DIMENSIONS, f);
        }

        static StaggeredDimensionEvaluator createForRingBlurRadius(float f) {
            return create(RING_BLUR_RADIUS_DIMENSIONS, f);
        }

        static StaggeredDimensionEvaluator createForBackgroundRingStrokeWidth(float f) {
            return create(BACKGROUND_RING_STROKE_WIDTH_DIMENSIONS, f);
        }

        static StaggeredDimensionEvaluator createForCometRadius(float f) {
            return create(COMET_RADIUS_DIMENSIONS, f);
        }

        static StaggeredDimensionEvaluator create(float[] fArr, float f) {
            return new StaggeredDimensionEvaluator(applyDensity(getStartPoint(fArr), f), applyDensity(getMiddlePoint(fArr), f), applyDensity(getEndPoint(fArr), f));
        }

        private static Point getStartPoint(float[] fArr) {
            return getPointAtIndex(0, fArr);
        }

        private static Point getMiddlePoint(float[] fArr) {
            return getPointAtIndex(1, fArr);
        }

        private static Point getEndPoint(float[] fArr) {
            return getPointAtIndex(2, fArr);
        }

        private static Point getPointAtIndex(int i, float[] fArr) {
            if (i >= 0) {
                float[] fArr2 = SIZE_DIMENSIONS;
                if (i < fArr2.length && i < fArr.length) {
                    return new Point(fArr2[i], fArr[i]);
                }
            }
            return new Point();
        }

        private static Point applyDensity(Point point, float f) {
            return new Point(point.getPointX() * f, point.getPointY() * f);
        }
    }

    /* access modifiers changed from: package-private */
    public static class StaggeredDimensionEvaluator {
        private final Point mEnd;
        private final Point mMiddle;
        private final Point mStart;

        private float evaluate(float f, float f2, float f3) {
            return f2 + (f * (f3 - f2));
        }

        StaggeredDimensionEvaluator(Point point, Point point2, Point point3) {
            ensureNonNegative(point.getPointX(), "start.getPointX");
            ensureNonNegative(point.getPointY(), "start.getPointY");
            ensureNonNegative(point2.getPointY(), "middle.getPointY");
            ensureNonNegative(point3.getPointY(), "end.getPointY");
            if (Float.compare(point.getPointX(), point2.getPointX()) >= 0) {
                throw new IllegalArgumentException("start.getPointX >= middle.getPointX");
            } else if (Float.compare(point2.getPointX(), point3.getPointX()) < 0) {
                this.mStart = point;
                this.mMiddle = point2;
                this.mEnd = point3;
            } else {
                throw new IllegalArgumentException("middle.getPointX >= end.getPointX");
            }
        }

        private static void ensureNonNegative(float f, String str) {
            if (f < 0.0f) {
                throw new IllegalArgumentException(str + " is negative");
            }
        }

        /* access modifiers changed from: package-private */
        public float evaluate(float f) {
            float pointX = this.mStart.getPointX();
            float pointY = this.mStart.getPointY();
            float pointX2 = this.mMiddle.getPointX();
            float pointY2 = this.mMiddle.getPointY();
            float pointX3 = this.mEnd.getPointX();
            float pointY3 = this.mEnd.getPointY();
            if (Float.compare(f, pointX) <= 0) {
                return pointY;
            }
            if (Float.compare(f, pointX3) >= 0) {
                return pointY3;
            }
            if (Float.compare(f, pointX) > 0 && Float.compare(f, pointX2) <= 0) {
                float f2 = f - pointX;
                if (Float.compare(pointX, pointX2) == 0) {
                    return pointX2;
                }
                return evaluate(f2 / (pointX2 - pointX), pointY, pointY2);
            } else if (Float.compare(pointX2, pointX3) == 0) {
                return pointX2;
            } else {
                return evaluate((f - pointX2) / (pointX3 - pointX2), pointY2, pointY3);
            }
        }
    }
}
