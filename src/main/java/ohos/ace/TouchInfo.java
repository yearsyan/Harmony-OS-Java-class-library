package ohos.ace;

public class TouchInfo {
    private float offsetX = 0.0f;
    private float offsetY = 0.0f;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;

    public TouchInfo(float f, float f2, float f3, float f4) {
        this.offsetX = f;
        this.offsetY = f2;
        this.scaleX = f3;
        this.scaleY = f4;
    }

    public float getScaleX() {
        return this.scaleX;
    }

    public void setScaleX(float f) {
        this.scaleX = f;
    }

    public float getScaleY() {
        return this.scaleY;
    }

    public void setScaleY(float f) {
        this.scaleY = f;
    }

    public float getOffsetX() {
        return this.offsetX;
    }

    public void setOffsetX(float f) {
        this.offsetX = f;
    }

    public float getOffsetY() {
        return this.offsetY;
    }

    public void setOffsetY(float f) {
        this.offsetY = f;
    }
}
