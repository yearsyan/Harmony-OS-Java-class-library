package ohos.aafwk.ability.continuation;

import ohos.aafwk.utils.log.Log;
import ohos.aafwk.utils.log.LogLabel;
import ohos.app.Context;

public class ContinuationRegisterManagerProxy implements IContinuationRegisterManager {
    private static final LogLabel LABEL = LogLabel.create();
    private Context mApplicationContext;
    private Context mContext;
    private ContinuationConnector mContinuationConnector;

    public ContinuationRegisterManagerProxy(Context context) {
        this.mContext = context;
        Context context2 = this.mContext;
        if (context2 != null) {
            this.mApplicationContext = context2.getApplicationContext();
        }
    }

    @Override // ohos.aafwk.ability.continuation.IContinuationRegisterManager
    public void register(final String str, final ExtraParams extraParams, final IContinuationDeviceCallback iContinuationDeviceCallback, final RequestCallback requestCallback) {
        Log.debug(LABEL, "register called", new Object[0]);
        if (this.mContext == null || this.mApplicationContext == null) {
            Log.error(LABEL, "register context is null", new Object[0]);
            return;
        }
        sendRequest(this.mApplicationContext, new ContinuationRequest() {
            /* class ohos.aafwk.ability.continuation.ContinuationRegisterManagerProxy.AnonymousClass1 */

            @Override // ohos.aafwk.ability.continuation.ContinuationRequest
            public void execute() {
                if (ContinuationRegisterManagerProxy.this.mContinuationConnector == null) {
                    Log.error(ContinuationRegisterManagerProxy.LABEL, "register execute Connector is null", new Object[0]);
                    return;
                }
                int register = ContinuationRegisterManagerProxy.this.mContinuationConnector.register(ContinuationRegisterManagerProxy.this.mContext, str, extraParams, iContinuationDeviceCallback);
                RequestCallback requestCallback = requestCallback;
                if (requestCallback != null) {
                    requestCallback.onResult(register);
                }
            }
        });
    }

    @Override // ohos.aafwk.ability.continuation.IContinuationRegisterManager
    public void unregister(final int i, final RequestCallback requestCallback) {
        Log.debug(LABEL, "unregister called", new Object[0]);
        if (this.mApplicationContext == null) {
            Log.error(LABEL, "unregister context is null", new Object[0]);
            return;
        }
        sendRequest(this.mApplicationContext, new ContinuationRequest() {
            /* class ohos.aafwk.ability.continuation.ContinuationRegisterManagerProxy.AnonymousClass2 */

            @Override // ohos.aafwk.ability.continuation.ContinuationRequest
            public void execute() {
                int i = 0;
                if (ContinuationRegisterManagerProxy.this.mContinuationConnector == null) {
                    Log.error(ContinuationRegisterManagerProxy.LABEL, "unregister execute Connector is null", new Object[0]);
                    return;
                }
                boolean unregister = ContinuationRegisterManagerProxy.this.mContinuationConnector.unregister(i);
                RequestCallback requestCallback = requestCallback;
                if (requestCallback != null) {
                    if (!unregister) {
                        i = -1;
                    }
                    requestCallback.onResult(i);
                }
            }
        });
    }

    @Override // ohos.aafwk.ability.continuation.IContinuationRegisterManager
    public void updateConnectStatus(final int i, final String str, final int i2, final RequestCallback requestCallback) {
        Log.debug(LABEL, "updateConnectStatus called", new Object[0]);
        if (this.mApplicationContext == null) {
            Log.error(LABEL, "updateConnectStatus context is null", new Object[0]);
            return;
        }
        sendRequest(this.mApplicationContext, new ContinuationRequest() {
            /* class ohos.aafwk.ability.continuation.ContinuationRegisterManagerProxy.AnonymousClass3 */

            @Override // ohos.aafwk.ability.continuation.ContinuationRequest
            public void execute() {
                int i = 0;
                if (ContinuationRegisterManagerProxy.this.mContinuationConnector == null) {
                    Log.error(ContinuationRegisterManagerProxy.LABEL, "updateConnectStatus execute Connector is null", new Object[0]);
                    return;
                }
                boolean updateConnectStatus = ContinuationRegisterManagerProxy.this.mContinuationConnector.updateConnectStatus(i, str, i2);
                RequestCallback requestCallback = requestCallback;
                if (requestCallback != null) {
                    if (!updateConnectStatus) {
                        i = -1;
                    }
                    requestCallback.onResult(i);
                }
            }
        });
    }

    @Override // ohos.aafwk.ability.continuation.IContinuationRegisterManager
    public void showDeviceList(final int i, final ExtraParams extraParams, final RequestCallback requestCallback) {
        Log.debug(LABEL, "showDeviceList called", new Object[0]);
        if (this.mApplicationContext == null) {
            Log.error(LABEL, "showDeviceList context is null", new Object[0]);
            return;
        }
        sendRequest(this.mApplicationContext, new ContinuationRequest() {
            /* class ohos.aafwk.ability.continuation.ContinuationRegisterManagerProxy.AnonymousClass4 */

            @Override // ohos.aafwk.ability.continuation.ContinuationRequest
            public void execute() {
                int i = 0;
                if (ContinuationRegisterManagerProxy.this.mContinuationConnector == null) {
                    Log.error(ContinuationRegisterManagerProxy.LABEL, "showDeviceList execute Connector is null", new Object[0]);
                    return;
                }
                boolean showDeviceList = ContinuationRegisterManagerProxy.this.mContinuationConnector.showDeviceList(i, extraParams);
                RequestCallback requestCallback = requestCallback;
                if (requestCallback != null) {
                    if (!showDeviceList) {
                        i = -1;
                    }
                    requestCallback.onResult(i);
                }
            }
        });
    }

    @Override // ohos.aafwk.ability.continuation.IContinuationRegisterManager
    public void disconnect() {
        Log.debug(LABEL, "disconnect called", new Object[0]);
        ContinuationConnector continuationConnector = this.mContinuationConnector;
        if (continuationConnector != null && continuationConnector.isAbilityConnected()) {
            this.mContinuationConnector.unbindRemoteRegisterAbility();
        }
    }

    private void sendRequest(Context context, ContinuationRequest continuationRequest) {
        if (this.mContinuationConnector == null) {
            this.mContinuationConnector = ContinuationConnector.getInstance(context);
        }
        if (!this.mContinuationConnector.isAbilityConnected()) {
            this.mContinuationConnector.bindRemoteRegisterAbility(continuationRequest);
        } else {
            continuationRequest.execute();
        }
    }
}
