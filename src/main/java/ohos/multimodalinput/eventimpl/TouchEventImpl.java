package ohos.multimodalinput.eventimpl;

import android.view.KeyEvent;
import android.view.MotionEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.MultimodalEvent;
import ohos.multimodalinput.event.TouchEvent;
import ohos.utils.Parcel;

/* access modifiers changed from: package-private */
public class TouchEventImpl extends TouchEvent implements HostEventAdapter {
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, 218114065, "TouchEventImpl");
    static final int SOURCE_TOUCHSCREEN = 4098;
    private MotionEvent motionEvent;

    @Override // ohos.multimodalinput.event.MultimodalEvent
    public String getDeviceId() {
        return "";
    }

    @Override // ohos.multimodalinput.event.TouchEvent
    public float getForcePrecision() {
        return 0.0f;
    }

    @Override // ohos.multimodalinput.eventimpl.HostEventAdapter
    public KeyEvent getHostKeyEvent() {
        return null;
    }

    @Override // ohos.multimodalinput.event.TouchEvent
    public float getMaxForce() {
        return 0.0f;
    }

    @Override // ohos.multimodalinput.event.TouchEvent
    public int getTapCount() {
        return 0;
    }

    @Override // ohos.utils.Sequenceable, ohos.multimodalinput.event.MultimodalEvent
    public boolean marshalling(Parcel parcel) {
        return false;
    }

    @Override // ohos.utils.Sequenceable, ohos.multimodalinput.event.MultimodalEvent
    public boolean unmarshalling(Parcel parcel) {
        return false;
    }

    TouchEventImpl(MotionEvent motionEvent2) {
        this.motionEvent = motionEvent2;
    }

    /* access modifiers changed from: protected */
    @Override // ohos.multimodalinput.event.TouchEvent
    public void setMultimodalEvent(MultimodalEvent multimodalEvent) {
        this.multimodalEvent = multimodalEvent;
    }

    @Override // ohos.multimodalinput.event.TouchEvent
    public int getAction() {
        MotionEvent motionEvent2 = this.motionEvent;
        if (motionEvent2 != null) {
            int actionMasked = motionEvent2.getActionMasked();
            switch (actionMasked) {
                case 0:
                    return 1;
                case 1:
                    return 2;
                case 2:
                    return 3;
                case 3:
                    return 6;
                case 4:
                case 8:
                default:
                    HiLog.error(LOG_LABEL, "unknown action: %{public}d", Integer.valueOf(actionMasked));
                    return 0;
                case 5:
                    return 4;
                case 6:
                    return 5;
                case 7:
                    return 8;
                case 9:
                    return 7;
                case 10:
                    return 9;
            }
        } else if (this.multimodalEvent instanceof StylusEventImpl) {
            return ((StylusEventImpl) this.multimodalEvent).getAction();
        } else {
            return 0;
        }
    }

    @Override // ohos.multimodalinput.event.TouchEvent
    public int getIndex() {
        MotionEvent motionEvent2 = this.motionEvent;
        if (motionEvent2 != null) {
            return motionEvent2.getActionIndex();
        }
        return 0;
    }

    @Override // ohos.multimodalinput.event.MultimodalEvent
    public int getSourceDevice() {
        MotionEvent motionEvent2 = this.motionEvent;
        if (motionEvent2 == null) {
            return -1;
        }
        int source = motionEvent2.getSource();
        if ((source & 4098) == 4098) {
            return 0;
        }
        HiLog.error(LOG_LABEL, "unknown source: %{public}d", Integer.valueOf(source));
        return -1;
    }

    @Override // ohos.multimodalinput.event.MultimodalEvent
    public int getInputDeviceId() {
        MotionEvent motionEvent2 = this.motionEvent;
        if (motionEvent2 != null) {
            return motionEvent2.getDeviceId();
        }
        return 0;
    }

    @Override // ohos.multimodalinput.event.MultimodalEvent
    public long getOccurredTime() {
        MotionEvent motionEvent2 = this.motionEvent;
        if (motionEvent2 != null) {
            return motionEvent2.getEventTime();
        }
        if (this.multimodalEvent instanceof StylusEventImpl) {
            return ((StylusEventImpl) this.multimodalEvent).getOccurredTime();
        }
        return 0;
    }

    @Override // ohos.multimodalinput.event.ManipulationEvent
    public long getStartTime() {
        MotionEvent motionEvent2 = this.motionEvent;
        if (motionEvent2 != null) {
            return motionEvent2.getDownTime();
        }
        if (this.multimodalEvent instanceof StylusEventImpl) {
            return ((StylusEventImpl) this.multimodalEvent).getStartTime();
        }
        return 0;
    }

    @Override // ohos.multimodalinput.event.ManipulationEvent
    public int getPhase() {
        int action = getAction();
        switch (action) {
            case 1:
                return 1;
            case 2:
                return 3;
            case 3:
            case 4:
            case 5:
                return 2;
            case 6:
                return 4;
            default:
                HiLog.error(LOG_LABEL, "unknown phase action: %{public}d", Integer.valueOf(action));
                return 0;
        }
    }

    @Override // ohos.multimodalinput.event.ManipulationEvent
    public MmiPoint getPointerPosition(int i) {
        float f;
        MotionEvent motionEvent2 = this.motionEvent;
        float f2 = 0.0f;
        if (motionEvent2 != null) {
            f2 = motionEvent2.getX(i);
            f = this.motionEvent.getY(i);
        } else {
            f = 0.0f;
        }
        return new MmiPoint(f2, f);
    }

    @Override // ohos.multimodalinput.event.ManipulationEvent
    public void setScreenOffset(float f, float f2) {
        MotionEvent motionEvent2 = this.motionEvent;
        if (motionEvent2 != null) {
            motionEvent2.offsetLocation(f, f2);
        }
    }

    @Override // ohos.multimodalinput.event.ManipulationEvent
    public MmiPoint getPointerScreenPosition(int i) {
        float f;
        MotionEvent motionEvent2 = this.motionEvent;
        float f2 = 0.0f;
        if (motionEvent2 != null) {
            f2 = motionEvent2.getRawX(i);
            f = this.motionEvent.getRawY(i);
        } else {
            f = 0.0f;
        }
        return new MmiPoint(f2, f);
    }

    @Override // ohos.multimodalinput.event.ManipulationEvent
    public int getPointerCount() {
        MotionEvent motionEvent2 = this.motionEvent;
        if (motionEvent2 == null) {
            return 0;
        }
        return motionEvent2.getPointerCount();
    }

    @Override // ohos.multimodalinput.event.ManipulationEvent
    public int getPointerId(int i) {
        MotionEvent motionEvent2 = this.motionEvent;
        if (motionEvent2 == null) {
            return 0;
        }
        return motionEvent2.getPointerId(i);
    }

    @Override // ohos.multimodalinput.event.ManipulationEvent
    public float getForce(int i) {
        MotionEvent motionEvent2 = this.motionEvent;
        if (motionEvent2 == null) {
            return 0.0f;
        }
        return motionEvent2.getPressure(i);
    }

    @Override // ohos.multimodalinput.event.ManipulationEvent
    public float getRadius(int i) {
        MotionEvent motionEvent2 = this.motionEvent;
        if (motionEvent2 == null) {
            return 0.0f;
        }
        return motionEvent2.getSize(i);
    }

    @Override // ohos.multimodalinput.eventimpl.HostEventAdapter
    public MotionEvent getHostMotionEvent() {
        return this.motionEvent;
    }
}
