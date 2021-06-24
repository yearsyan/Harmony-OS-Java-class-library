package ohos.aafwk.ability.continuation;

public enum DeviceConnectState {
    FAILURE(-1),
    IDLE(0),
    CONNECTING(1),
    CONNECTED(2),
    DIS_CONNECTING(3);
    
    private final int mState;

    private DeviceConnectState(int i) {
        this.mState = i;
    }

    public int getState() {
        return this.mState;
    }

    public static DeviceConnectState getConnectState(int i) {
        DeviceConnectState[] values = values();
        for (DeviceConnectState deviceConnectState : values) {
            if (i == deviceConnectState.mState) {
                return deviceConnectState;
            }
        }
        return IDLE;
    }
}
