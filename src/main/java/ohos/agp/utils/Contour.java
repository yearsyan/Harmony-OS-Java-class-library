package ohos.agp.utils;

import ohos.agp.render.Path;

public class Contour {
    public static final int MODE_EMPTY = 0;
    public static final int MODE_PATH = 2;
    public static final int MODE_RECT = 1;
    public int mMode = 0;
    public Path mPath;
    public float mRadius = Float.NEGATIVE_INFINITY;
    public final Rect mRect = new Rect();

    public void setContourEncompassRect(int i, int i2, int i3, int i4, float f) {
        if (i >= i3 || i2 >= i4) {
            setEmpty();
            return;
        }
        Path path = this.mPath;
        if (path != null && this.mMode == 2) {
            path.rewind();
        }
        this.mMode = 1;
        this.mRect.set(i, i2, i3, i4);
        this.mRadius = f;
    }

    public void translate(int i, int i2) {
        Path path;
        Rect rect;
        if (this.mMode == 1 && (rect = this.mRect) != null) {
            rect.translate(i, i2);
        } else if (this.mMode == 2 && (path = this.mPath) != null) {
            path.offset((float) i, (float) i2);
        }
    }

    public void setContourRect(Rect rect) {
        if (rect != null) {
            setContourRect(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    public void setContourRect(int i, int i2, int i3, int i4) {
        setContourEncompassRect(i, i2, i3, i4, 0.0f);
    }

    public void setConvexPath(Path path) {
        if (path == null || path.isEmpty()) {
            setEmpty();
            return;
        }
        if (this.mPath == null) {
            this.mPath = new Path();
        }
        this.mMode = 2;
        this.mPath.set(path);
        this.mRect.clear();
        this.mRadius = Float.NEGATIVE_INFINITY;
    }

    public boolean isEmpty() {
        return this.mMode == 0;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public boolean getRect(Rect rect) {
        if (this.mMode != 1) {
            return false;
        }
        rect.set(this.mRect.left, this.mRect.top, this.mRect.right, this.mRect.bottom);
        return true;
    }

    private void setEmpty() {
        Path path = this.mPath;
        if (path != null) {
            path.rewind();
        }
        this.mMode = 0;
        this.mRect.clear();
        this.mRadius = Float.NEGATIVE_INFINITY;
    }
}
