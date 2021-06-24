package ohos.agp.components.webengine;

public interface ScaleController {
    int getTextScale();

    boolean isGestureScalable();

    boolean isScalable();

    boolean scaleDown();

    boolean scaleUp();

    void setGestureScalable(boolean z);

    void setScalable(boolean z);

    void setScale(int i);

    void setScaleChangeListener(ScaleChangeListener scaleChangeListener);

    void setTextScale(int i);
}
