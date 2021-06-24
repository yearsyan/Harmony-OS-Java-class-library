package ohos.ai.cv.common;

import ohos.utils.fastjson.annotation.JSONField;

public class BoundingBox {
    @JSONField(name = "height")
    private int height;
    @JSONField(name = "left")
    private int left;
    @JSONField(name = "top")
    private int top;
    @JSONField(name = "width")
    private int width;

    public BoundingBox() {
        this(0, 0, 0, 0);
    }

    public BoundingBox(int i, int i2, int i3, int i4) {
        this.left = i;
        this.top = i2;
        this.width = i3;
        this.height = i4;
    }

    public int getLeft() {
        return this.left;
    }

    public void setLeft(int i) {
        this.left = i;
    }

    public int getTop() {
        return this.top;
    }

    public void setTop(int i) {
        this.top = i;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int i) {
        this.height = i;
    }
}
