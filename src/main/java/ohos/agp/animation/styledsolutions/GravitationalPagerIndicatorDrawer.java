package ohos.agp.animation.styledsolutions;

import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.utils.RectFloat;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class GravitationalPagerIndicatorDrawer {
    private static final int DOMAIN_ID = 218107648;
    private static final float GAMMA = 2.2f;
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, DOMAIN_ID, "SuperMedia");
    private static final String LOG_FORMAT = "%{public}s: %{public}s";
    private static final float MAX_INT_VALUE_OF_COLOR = 255.0f;
    private static final int MOVE_ALPHA = 24;
    private static final int MOVE_GREEN = 8;
    private static final int MOVE_RED = 16;
    private static final int NORMALITEMCOLOR = 419430400;
    private static final int TWO = 2;
    private float mBackgroundMaxAlpha = 0.3f;
    private float mComponentHeight;
    private float mComponentWidth;
    private float mComponentWidthTemp;
    private float mDampScale;
    private float mDampValue;
    private int mDirection;
    private Color mEndBackGroundColor;
    private Point mEnlagedOri;
    private Color mFocusColor;
    private float mGroundScale = 1.0f;
    private int mItemCount;
    private float mItemOffset;
    private float mItemOffsetTemp;
    private float mLeftsliderValue;
    private Color mNormalColor;
    private float mNormalItemSliderLength;
    private Point mOri;
    private Point mOritTemp;
    private float mRadius;
    private float mRadiusTemp;
    private float mRadiusoffset;
    private RectFloat mRect;
    private float mRightsliderValue;
    private float mScaleValue;
    private int mSelectedItem;
    private float mSliderLength;
    private Point mSliderStart;
    private float mSpringValue = 1.0f;
    private Color mStartBackGroundColor;
    private int mTargetItem;

    public void setFocusColor(Color color) {
        this.mFocusColor = color;
    }

    public void setNormalColor(Color color) {
        this.mNormalColor = color;
    }

    public void setStartBackGroundColor(Color color) {
        this.mStartBackGroundColor = color;
    }

    public void setEndBackGroundColor(Color color) {
        this.mEndBackGroundColor = color;
    }

    public void setDampScale(float f) {
        this.mDampScale = f;
    }

    public float getSliderLength() {
        return this.mSliderLength;
    }

    public void setLeftsliderValue(float f) {
        this.mLeftsliderValue = f;
    }

    public void setRightsliderValue(float f) {
        this.mRightsliderValue = f;
    }

    public void setSpringValue(float f) {
        this.mSpringValue = f;
    }

    public void setDirection(int i) {
        this.mDirection = i;
    }

    public int getDirection() {
        return this.mDirection;
    }

    public void setDampValue(float f) {
        this.mDampValue = f;
    }

    public float getDampValue() {
        return this.mDampValue;
    }

    public void setComponentHeight(float f) {
        this.mComponentHeight = f;
    }

    public void setComponentWidth(float f) {
        this.mComponentWidth = f;
        this.mComponentWidthTemp = f;
    }

    public void setTargetItem(int i) {
        this.mTargetItem = i;
    }

    public int getTarget() {
        return this.mTargetItem;
    }

    public void setSelectedItem(int i) {
        this.mSelectedItem = i;
    }

    public int getSelectedItem() {
        return this.mSelectedItem;
    }

    public void setItemOffset(float f) {
        if (f != 0.0f) {
            this.mItemOffset = f;
            this.mItemOffsetTemp = f;
        }
    }

    public float getItemOffset() {
        return this.mItemOffset;
    }

    public void setOri(Point point) {
        this.mOri = point;
        this.mOritTemp = new Point(point);
    }

    public void setItemCount(int i) {
        this.mItemCount = i;
    }

    public void setScale(float f) {
        this.mGroundScale = f;
    }

    public void setScaleValue(float f) {
        this.mScaleValue = f;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public void setRadius(float f) {
        this.mRadius = f;
        this.mRadiusTemp = f;
    }

    GravitationalPagerIndicatorDrawer() {
    }

    public void initParamater() {
        if (this.mOri == null) {
            HiLog.info(LABEL_LOG, LOG_FORMAT, "GravitationalPagerIndicatorDrawer::InitPatamter", "invalidate component fail !");
            return;
        }
        float f = this.mItemOffset;
        float f2 = this.mRadius;
        this.mSliderLength = ((float) (this.mTargetItem - this.mSelectedItem)) * (f + (2.0f * f2));
        if (this.mSliderLength > 0.0f) {
            this.mNormalItemSliderLength = -f2;
            this.mRadiusoffset = f2;
        }
        if (this.mSliderLength < 0.0f) {
            float f3 = this.mRadius;
            this.mNormalItemSliderLength = f3;
            this.mRadiusoffset = f3 * -1.0f;
        }
        this.mSliderStart = new Point(calculateItemPosition(this.mSelectedItem), this.mOri.getPointY());
    }

    public void onDraw(Canvas canvas) {
        if (this.mSliderStart == null) {
            HiLog.info(LABEL_LOG, LOG_FORMAT, "GravitationalPagerIndicatorDrawer::onDraw", "Start point is null , invalidate component fail !");
        } else if (canvas == null) {
            HiLog.info(LABEL_LOG, LOG_FORMAT, "GravitationalPagerIndicatorDrawer::onDraw", "canvas is null , invalidate component fail !");
        } else {
            enlargeIndicator();
            drawBackGround(canvas);
            drawNormalItems(canvas);
            Paint paint = new Paint();
            Color color = this.mFocusColor;
            if (color == null) {
                paint.setColor(Color.BLUE);
            } else {
                paint.setColor(color);
            }
            drawLeftCircle(canvas, paint);
            drawRect(canvas, paint);
            drawRightCircle(canvas, paint);
        }
    }

    public float calculateItemPosition(int i) {
        if (isOnSelectedRight(i)) {
            float pointX = this.mOri.getPointX();
            float f = this.mRadius;
            return pointX + (f * 2.0f) + (((float) i) * (this.mItemOffset + 0.0f + (f * 2.0f))) + (f * 2.0f);
        } else if (this.mSelectedItem == i) {
            float pointX2 = this.mOri.getPointX();
            float f2 = this.mRadius;
            return pointX2 + (f2 * 2.0f) + (((float) i) * (this.mItemOffset + 0.0f + (2.0f * f2))) + f2;
        } else {
            float pointX3 = this.mOri.getPointX();
            float f3 = this.mRadius;
            return pointX3 + (f3 * 2.0f) + (((float) i) * (this.mItemOffset + 0.0f + (f3 * 2.0f)));
        }
    }

    public int calculateIndexByPositioin(float f) {
        float calculateItemPosition = calculateItemPosition(this.mSelectedItem - 1);
        float calculateItemPosition2 = calculateItemPosition(this.mSelectedItem + 1);
        float calculateItemPosition3 = calculateItemPosition(this.mSelectedItem);
        if (f <= calculateItemPosition) {
            return calculateSelectFrontPosition(f);
        }
        if (f >= calculateItemPosition2) {
            return calculateSelectLaterPosiont(f);
        }
        if (f < calculateItemPosition3) {
            return this.mSelectedItem - 1;
        }
        return this.mSelectedItem;
    }

    private void enlargeIndicator() {
        float f = ((this.mGroundScale - 1.0f) * this.mScaleValue) + 1.0f;
        this.mRadius = this.mRadiusTemp * f;
        this.mItemOffset = this.mItemOffsetTemp * f;
        this.mComponentWidth = this.mComponentWidthTemp * f;
        this.mOri.modify(this.mOritTemp.getPointX() - (((f - 1.0f) * this.mComponentWidthTemp) / 2.0f), this.mOritTemp.getPointY());
        this.mEnlagedOri = this.mOri;
    }

    private void drawRect(Canvas canvas, Paint paint) {
        float f = this.mRightsliderValue;
        float f2 = this.mSliderLength;
        float f3 = (this.mLeftsliderValue * f2) - (this.mRadiusoffset * (this.mSpringValue - 1.0f));
        this.mRect = createRect((this.mSliderStart.getPointX() + f3) - this.mRadiusoffset, this.mSliderStart.getPointY() - this.mRadius, this.mSliderStart.getPointX() + (f * f2) + this.mRadiusoffset, this.mSliderStart.getPointY() + this.mRadius);
        canvas.drawRect(this.mRect, paint);
    }

    private void drawRightCircle(Canvas canvas, Paint paint) {
        canvas.drawCircle(this.mSliderStart.getPointX() + this.mRadiusoffset + (this.mRightsliderValue * this.mSliderLength), this.mSliderStart.getPointY(), this.mRadius, paint);
    }

    private void drawLeftCircle(Canvas canvas, Paint paint) {
        this.mSliderStart.modify(calculateItemPosition(this.mSelectedItem), this.mOri.getPointY());
        float f = this.mItemOffset;
        float f2 = this.mRadius;
        this.mSliderLength = ((float) (this.mTargetItem - this.mSelectedItem)) * (f + (2.0f * f2));
        if (this.mSliderLength > 0.0f) {
            this.mNormalItemSliderLength = -f2;
            this.mRadiusoffset = f2;
        }
        if (this.mSliderLength < 0.0f) {
            float f3 = this.mRadius;
            this.mNormalItemSliderLength = f3;
            this.mRadiusoffset = f3 * -1.0f;
        }
        canvas.drawCircle((this.mSliderStart.getPointX() - this.mRadiusoffset) + ((this.mLeftsliderValue * this.mSliderLength) - (this.mRadiusoffset * (this.mSpringValue - 1.0f))), this.mSliderStart.getPointY(), this.mRadius, paint);
    }

    private void drawNormalItems(Canvas canvas) {
        for (int i = 0; i < this.mItemCount; i++) {
            Paint paint = new Paint();
            Color color = this.mNormalColor;
            if (color == null) {
                paint.setColor(new Color(NORMALITEMCOLOR));
            } else {
                paint.setColor(color);
            }
            if (isSelectedOrTarget(i)) {
                canvas.drawCircle(calculateItemPosition(i) + (this.mRightsliderValue * this.mNormalItemSliderLength), this.mOri.getPointY(), this.mRadius, paint);
            } else if (betweenSelectedAndPosItem(i)) {
                float f = this.mRightsliderValue;
                float f2 = this.mNormalItemSliderLength;
                canvas.drawCircle(calculateItemPosition(i) + (f * (f2 + f2)), this.mOri.getPointY(), this.mRadius, paint);
            } else {
                canvas.drawCircle(calculateItemPosition(i), this.mOri.getPointY(), this.mRadius, paint);
            }
        }
    }

    private void drawBackGround(Canvas canvas) {
        RectFloat rectFloat;
        Paint paint = new Paint();
        if (this.mStartBackGroundColor == null) {
            this.mStartBackGroundColor = new Color(0);
        }
        if (this.mEndBackGroundColor == null) {
            this.mEndBackGroundColor = Color.GRAY;
        }
        paint.setColor(new Color(evaluate(this.mScaleValue, this.mStartBackGroundColor.getValue(), this.mEndBackGroundColor.getValue())));
        if (this.mDampValue != 0.0f) {
            float f = this.mDampScale;
            float f2 = this.mComponentHeight * f;
            float f3 = f * this.mComponentWidth;
            if (this.mDirection == -1) {
                this.mOri.modify(this.mEnlagedOri.getPointX() - (this.mDampValue * f3), this.mEnlagedOri.getPointY());
            }
            rectFloat = new RectFloat(this.mOri.getPointX() - this.mRadius, (this.mOri.getPointY() - (this.mComponentHeight / 2.0f)) + ((this.mDampValue * f2) / 2.0f), this.mOri.getPointX() + this.mRadius + this.mComponentWidth + (this.mDampValue * f3), (this.mOri.getPointY() + (this.mComponentHeight / 2.0f)) - ((f2 * this.mDampValue) / 2.0f));
        } else {
            rectFloat = new RectFloat(this.mOri.getPointX() - this.mRadius, this.mOri.getPointY() - (this.mComponentHeight / 2.0f), this.mOri.getPointX() + this.mRadius + this.mComponentWidth, this.mOri.getPointY() + (this.mComponentHeight / 2.0f));
        }
        float f4 = this.mComponentHeight;
        canvas.drawRoundRect(rectFloat, f4, f4, paint);
    }

    private RectFloat createRect(float f, float f2, float f3, float f4) {
        if (f < f3) {
            return new RectFloat(f, f2, f3, f4);
        }
        return new RectFloat(f3, f2, f, f4);
    }

    private boolean betweenSelectedAndPosItem(int i) {
        if (i < this.mTargetItem && i > this.mSelectedItem) {
            return true;
        }
        if (i <= this.mTargetItem || i >= this.mSelectedItem) {
            return false;
        }
        return true;
    }

    private boolean isSelectedOrTarget(int i) {
        return i == this.mTargetItem || i == this.mSelectedItem;
    }

    private boolean isOnSelectedRight(int i) {
        return i > this.mSelectedItem;
    }

    private int calculateSelectFrontPosition(float f) {
        if ((f - this.mOri.getPointX()) - (this.mRadius * 2.0f) < 0.0f) {
            return -1;
        }
        float pointX = f - this.mOri.getPointX();
        float f2 = this.mRadius;
        return (int) ((pointX - (f2 * 2.0f)) / (this.mItemOffset + (f2 * 2.0f)));
    }

    private int calculateSelectLaterPosiont(float f) {
        float pointX = f - this.mOri.getPointX();
        float f2 = this.mRadius;
        return (int) ((pointX - (4.0f * f2)) / (this.mItemOffset + (f2 * 2.0f)));
    }

    private int evaluate(float f, int i, int i2) {
        float f2 = ((float) ((i >> 24) & 255)) / MAX_INT_VALUE_OF_COLOR;
        float f3 = ((float) ((i >> 16) & 255)) / MAX_INT_VALUE_OF_COLOR;
        float f4 = ((float) ((i >> 8) & 255)) / MAX_INT_VALUE_OF_COLOR;
        float f5 = ((float) (i & 255)) / MAX_INT_VALUE_OF_COLOR;
        float f6 = ((float) ((i2 >> 24) & 255)) / MAX_INT_VALUE_OF_COLOR;
        float f7 = ((float) ((i2 >> 16) & 255)) / MAX_INT_VALUE_OF_COLOR;
        float f8 = ((float) ((i2 >> 8) & 255)) / MAX_INT_VALUE_OF_COLOR;
        float f9 = ((float) (i2 & 255)) / MAX_INT_VALUE_OF_COLOR;
        float pow = (float) Math.pow((double) f3, 2.200000047683716d);
        float pow2 = (float) Math.pow((double) f4, 2.200000047683716d);
        float pow3 = (float) Math.pow((double) f5, 2.200000047683716d);
        float pow4 = (float) Math.pow((double) f7, 2.200000047683716d);
        float pow5 = pow3 + (f * (((float) Math.pow((double) f9, 2.200000047683716d)) - pow3));
        return (Math.round((f2 + ((f6 - f2) * f)) * MAX_INT_VALUE_OF_COLOR) << 24) | (Math.round(((float) Math.pow((double) (pow + ((pow4 - pow) * f)), 0.4545454446934474d)) * MAX_INT_VALUE_OF_COLOR) << 16) | (Math.round(((float) Math.pow((double) (pow2 + ((((float) Math.pow((double) f8, 2.200000047683716d)) - pow2) * f)), 0.4545454446934474d)) * MAX_INT_VALUE_OF_COLOR) << 8) | Math.round(((float) Math.pow((double) pow5, 0.4545454446934474d)) * MAX_INT_VALUE_OF_COLOR);
    }
}
