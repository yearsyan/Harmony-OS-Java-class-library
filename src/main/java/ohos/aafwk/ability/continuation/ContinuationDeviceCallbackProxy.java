package ohos.aafwk.ability.continuation;

public class ContinuationDeviceCallbackProxy extends ConnectCallbackStub {
    private static final String DESCRIPTOR = "com.huawei.controlcenter.featureability.sdk.IConnectCallback";
    private IContinuationDeviceCallback callback;

    public ContinuationDeviceCallbackProxy(IContinuationDeviceCallback iContinuationDeviceCallback) {
        super(DESCRIPTOR);
        this.callback = iContinuationDeviceCallback;
    }

    @Override // ohos.aafwk.ability.continuation.IConnectCallback
    public void connect(String str, String str2) {
        IContinuationDeviceCallback iContinuationDeviceCallback = this.callback;
        if (iContinuationDeviceCallback != null) {
            iContinuationDeviceCallback.onDeviceConnectDone(str, str2);
        }
    }

    @Override // ohos.aafwk.ability.continuation.IConnectCallback
    public void disconnect(String str) {
        IContinuationDeviceCallback iContinuationDeviceCallback = this.callback;
        if (iContinuationDeviceCallback != null) {
            iContinuationDeviceCallback.onDeviceDisconnectDone(str);
        }
    }
}
