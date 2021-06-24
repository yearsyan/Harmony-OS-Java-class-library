package ohos.accessibility.ability;

public class GesturePathDefine {
    private static final int MAX_DURATION_TIME = 60000;
    private static final int MAX_STROKE_COUNT = 10;
    private int mDurationTime;
    private GesturePathPositionDefine mEndPosition;
    private GesturePathPositionDefine mStartPosition;

    public int getMaxStrokeDuration() {
        return 60000;
    }

    public int getMaxStrokes() {
        return 10;
    }

    public GesturePathPositionDefine getStartPosition() {
        return this.mStartPosition;
    }

    public void setStartPosition(GesturePathPositionDefine gesturePathPositionDefine) {
        this.mStartPosition = gesturePathPositionDefine;
    }

    public GesturePathPositionDefine getEndPosition() {
        return this.mEndPosition;
    }

    public void setEndPosition(GesturePathPositionDefine gesturePathPositionDefine) {
        this.mEndPosition = gesturePathPositionDefine;
    }

    public int getDurationTime() {
        return this.mDurationTime;
    }

    public void setDurationTime(int i) {
        this.mDurationTime = i;
    }
}
