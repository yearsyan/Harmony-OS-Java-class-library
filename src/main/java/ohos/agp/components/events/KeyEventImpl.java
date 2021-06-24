package ohos.agp.components.events;

import ohos.multimodalinput.event.KeyEvent;

public final class KeyEventImpl extends KeyEvent {
    public static final int ACTION_DOWN = 0;
    public static final int ACTION_UP = 1;
    private static final String UNKNOWN_DEVICE_ID = "unknown_device";
    private final int mAction;
    private final String mDeviceId;
    private final int mInputDeviceId;
    private final int mKeyCode;
    private final long mKeyDownDuration;
    private final long mOccurredTime;
    private final int mSourceDevice;

    private KeyEventImpl(int i, int i2) {
        this(i, i2, 0, 0, UNKNOWN_DEVICE_ID, 0, 0);
    }

    private KeyEventImpl(int i, int i2, long j, int i3, String str, int i4, long j2) {
        this.mAction = i;
        this.mKeyCode = i2;
        this.mKeyDownDuration = j;
        this.mSourceDevice = i3;
        this.mDeviceId = str;
        this.mInputDeviceId = i4;
        this.mOccurredTime = j2;
    }

    public static KeyEvent obtain(int i, int i2) {
        return new KeyEventImpl(i, i2);
    }

    @Override // ohos.multimodalinput.event.KeyEvent
    public boolean isKeyDown() {
        return this.mAction == 0;
    }

    @Override // ohos.multimodalinput.event.KeyEvent
    public int getKeyCode() {
        return this.mKeyCode;
    }

    @Override // ohos.multimodalinput.event.KeyEvent
    public long getKeyDownDuration() {
        return this.mKeyDownDuration;
    }

    @Override // ohos.multimodalinput.event.MultimodalEvent
    public int getSourceDevice() {
        return this.mSourceDevice;
    }

    @Override // ohos.multimodalinput.event.MultimodalEvent
    public String getDeviceId() {
        return this.mDeviceId;
    }

    @Override // ohos.multimodalinput.event.MultimodalEvent
    public int getInputDeviceId() {
        return this.mInputDeviceId;
    }

    @Override // ohos.multimodalinput.event.MultimodalEvent
    public long getOccurredTime() {
        return this.mOccurredTime;
    }
}
