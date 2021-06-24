package ohos.aafwk.ability.continuation;

import ohos.aafwk.utils.log.Log;
import ohos.aafwk.utils.log.LogLabel;

public class ContinuationRegisterManager implements IContinuationRegisterManager {
    private static final LogLabel LABEL = LogLabel.create();
    private ContinuationRegisterManagerProxy mContinuationRegisterManagerProxy;

    public ContinuationRegisterManager(ContinuationRegisterManagerProxy continuationRegisterManagerProxy) {
        this.mContinuationRegisterManagerProxy = continuationRegisterManagerProxy;
    }

    @Override // ohos.aafwk.ability.continuation.IContinuationRegisterManager
    public void register(String str, ExtraParams extraParams, IContinuationDeviceCallback iContinuationDeviceCallback, RequestCallback requestCallback) {
        ContinuationRegisterManagerProxy continuationRegisterManagerProxy = this.mContinuationRegisterManagerProxy;
        if (continuationRegisterManagerProxy == null) {
            Log.error(LABEL, "register proxy is null", new Object[0]);
        } else {
            continuationRegisterManagerProxy.register(str, extraParams, iContinuationDeviceCallback, requestCallback);
        }
    }

    @Override // ohos.aafwk.ability.continuation.IContinuationRegisterManager
    public void unregister(int i, RequestCallback requestCallback) {
        ContinuationRegisterManagerProxy continuationRegisterManagerProxy = this.mContinuationRegisterManagerProxy;
        if (continuationRegisterManagerProxy == null) {
            Log.error(LABEL, "unregister proxy is null", new Object[0]);
        } else {
            continuationRegisterManagerProxy.unregister(i, requestCallback);
        }
    }

    @Override // ohos.aafwk.ability.continuation.IContinuationRegisterManager
    public void updateConnectStatus(int i, String str, int i2, RequestCallback requestCallback) {
        ContinuationRegisterManagerProxy continuationRegisterManagerProxy = this.mContinuationRegisterManagerProxy;
        if (continuationRegisterManagerProxy == null) {
            Log.error(LABEL, "updateConnectStatus proxy is null", new Object[0]);
        } else {
            continuationRegisterManagerProxy.updateConnectStatus(i, str, i2, requestCallback);
        }
    }

    @Override // ohos.aafwk.ability.continuation.IContinuationRegisterManager
    public void showDeviceList(int i, ExtraParams extraParams, RequestCallback requestCallback) {
        ContinuationRegisterManagerProxy continuationRegisterManagerProxy = this.mContinuationRegisterManagerProxy;
        if (continuationRegisterManagerProxy == null) {
            Log.error(LABEL, "showDeviceList proxy is null", new Object[0]);
        } else {
            continuationRegisterManagerProxy.showDeviceList(i, extraParams, requestCallback);
        }
    }

    @Override // ohos.aafwk.ability.continuation.IContinuationRegisterManager
    public void disconnect() {
        ContinuationRegisterManagerProxy continuationRegisterManagerProxy = this.mContinuationRegisterManagerProxy;
        if (continuationRegisterManagerProxy == null) {
            Log.error(LABEL, "disconnect proxy is null", new Object[0]);
        } else {
            continuationRegisterManagerProxy.disconnect();
        }
    }
}
