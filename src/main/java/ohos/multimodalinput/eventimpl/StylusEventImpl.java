package ohos.multimodalinput.eventimpl;

import android.view.KeyEvent;
import android.view.MotionEvent;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.StylusEvent;
import ohos.utils.Parcel;

/* access modifiers changed from: package-private */
public class StylusEventImpl extends StylusEvent implements HostEventAdapter {
    static final int SOURCE_STYLUS = 16384;
    private KeyEvent androidKeyEvent;
    private MotionEvent motionEvent;

    static boolean isStylusKeyCode(int i) {
        return i == 718;
    }

    @Override // ohos.multimodalinput.event.MultimodalEvent
    public String getDeviceId() {
        return "";
    }

    @Override // ohos.multimodalinput.event.MultimodalEvent
    public int getSourceDevice() {
        return 3;
    }

    @Override // ohos.utils.Sequenceable, ohos.multimodalinput.event.MultimodalEvent
    public boolean marshalling(Parcel parcel) {
        return false;
    }

    @Override // ohos.utils.Sequenceable, ohos.multimodalinput.event.MultimodalEvent
    public boolean unmarshalling(Parcel parcel) {
        return false;
    }

    StylusEventImpl(MotionEvent motionEvent2) {
        this.motionEvent = motionEvent2;
    }

    StylusEventImpl(KeyEvent keyEvent) {
        this.androidKeyEvent = keyEvent;
    }

    @Override // ohos.multimodalinput.event.StylusEvent
    public int getAction() {
        MotionEvent motionEvent2 = this.motionEvent;
        if (motionEvent2 != null) {
            int actionMasked = motionEvent2.getActionMasked();
            if (actionMasked == 0) {
                return 3;
            }
            if (actionMasked == 1) {
                return 5;
            }
            if (actionMasked != 2) {
                return 0;
            }
            return 4;
        }
        KeyEvent keyEvent = this.androidKeyEvent;
        if (keyEvent == null) {
            return 0;
        }
        int action = keyEvent.getAction();
        if (action != 0) {
            return action != 1 ? 0 : 2;
        }
        return 1;
    }

    @Override // ohos.multimodalinput.event.StylusEvent
    public int getButtons() {
        return this.androidKeyEvent != null ? 1 : 0;
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
        KeyEvent keyEvent = this.androidKeyEvent;
        if (keyEvent != null) {
            return keyEvent.getEventTime();
        }
        return 0;
    }

    @Override // ohos.multimodalinput.event.ManipulationEvent
    public long getStartTime() {
        MotionEvent motionEvent2 = this.motionEvent;
        if (motionEvent2 != null) {
            return motionEvent2.getDownTime();
        }
        KeyEvent keyEvent = this.androidKeyEvent;
        if (keyEvent != null) {
            return keyEvent.getDownTime();
        }
        return 0;
    }

    @Override // ohos.multimodalinput.event.ManipulationEvent
    public int getPhase() {
        int action = getAction();
        if (action == 3) {
            return 1;
        }
        if (action != 4) {
            return action != 5 ? 0 : 3;
        }
        return 2;
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
    public KeyEvent getHostKeyEvent() {
        return this.androidKeyEvent;
    }

    @Override // ohos.multimodalinput.eventimpl.HostEventAdapter
    public MotionEvent getHostMotionEvent() {
        return this.motionEvent;
    }
}
