package ohos.aafwk.ability.continuation;

public interface IContinuationRegisterManager {
    void disconnect();

    void register(String str, ExtraParams extraParams, IContinuationDeviceCallback iContinuationDeviceCallback, RequestCallback requestCallback);

    void showDeviceList(int i, ExtraParams extraParams, RequestCallback requestCallback);

    void unregister(int i, RequestCallback requestCallback);

    void updateConnectStatus(int i, String str, int i2, RequestCallback requestCallback);
}
