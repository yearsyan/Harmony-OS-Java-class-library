package ohos.aafwk.ability.continuation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.utils.log.Log;
import ohos.aafwk.utils.log.LogLabel;
import ohos.abilityshell.utils.BinderConverter;
import ohos.app.Context;
import ohos.bundle.ElementName;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;

public class ContinuationConnector implements IAbilityConnection {
    private static final String CONTROLCENTER_PACKAGENAME = "com.huawei.controlcenter";
    private static final Object INSTANCE_LOCK = new Object();
    private static final LogLabel LABEL = LogLabel.create();
    private static final String LOCAL_DEVICE_ID = "";
    private static final Object PENDINGREQUEST_LOCK = new Object();
    private static final String REMOTE_CONTINUATION_SERVICENAME = "com.huawei.controlcenter.fatransfer.service.FeatureAbilityRegisterService";
    private static volatile ContinuationConnector sInstance;
    private Context mContext;
    private List<ContinuationRequest> mContinuationRequestList = new ArrayList(1);
    private AtomicBoolean mIsConnected = new AtomicBoolean(false);
    private IRemoteRegisterService mRemoteRegisterService;

    private ContinuationConnector(Context context) {
        this.mContext = context;
    }

    public static ContinuationConnector getInstance(Context context) {
        if (sInstance == null) {
            synchronized (INSTANCE_LOCK) {
                if (sInstance == null) {
                    sInstance = new ContinuationConnector(context);
                }
            }
        }
        return sInstance;
    }

    private void bindRemoteRegisterAbility() {
        if (this.mContext == null) {
            Log.error(LABEL, "bindRemoteRegisterAbility mContext is null", new Object[0]);
            return;
        }
        Intent intent = new Intent();
        intent.setElement(new ElementName("", CONTROLCENTER_PACKAGENAME, REMOTE_CONTINUATION_SERVICENAME));
        intent.addFlags(16);
        this.mContext.connectAbility(intent, this);
    }

    public void bindRemoteRegisterAbility(ContinuationRequest continuationRequest) {
        if (continuationRequest == null) {
            Log.error(LABEL, "bindRemoteRegisterAbility request is null", new Object[0]);
        } else if (this.mContext == null) {
            Log.error(LABEL, "bindRemoteRegisterAbility mContext is null", new Object[0]);
        } else if (isAbilityConnected()) {
            Log.info(LABEL, "bindRemoteRegisterAbility already connected", new Object[0]);
            continuationRequest.execute();
        } else {
            synchronized (PENDINGREQUEST_LOCK) {
                this.mContinuationRequestList.add(continuationRequest);
            }
            bindRemoteRegisterAbility();
        }
    }

    public void unbindRemoteRegisterAbility() {
        Context context = this.mContext;
        if (context == null) {
            Log.error(LABEL, "unbindRemoteRegisterAbility context is null", new Object[0]);
            return;
        }
        context.disconnectAbility(this);
        this.mIsConnected.set(false);
        this.mRemoteRegisterService = null;
    }

    @Override // ohos.aafwk.ability.IAbilityConnection
    public void onAbilityConnectDone(ElementName elementName, IRemoteObject iRemoteObject, int i) {
        Log.info(LABEL, "onAbilityConnectDone", new Object[0]);
        this.mRemoteRegisterService = new RemoteRegisterServiceProxy(iRemoteObject);
        this.mIsConnected.set(true);
        synchronized (PENDINGREQUEST_LOCK) {
            for (ContinuationRequest continuationRequest : this.mContinuationRequestList) {
                continuationRequest.execute();
            }
            this.mContinuationRequestList.clear();
        }
    }

    @Override // ohos.aafwk.ability.IAbilityConnection
    public void onAbilityDisconnectDone(ElementName elementName, int i) {
        Log.info(LABEL, "onAbilityDisconnectDone", new Object[0]);
        this.mRemoteRegisterService = null;
        this.mIsConnected.set(false);
        unbindRemoteRegisterAbility();
    }

    public boolean isAbilityConnected() {
        return this.mIsConnected.get();
    }

    public int register(Context context, String str, ExtraParams extraParams, IContinuationDeviceCallback iContinuationDeviceCallback) {
        if (this.mRemoteRegisterService == null) {
            Log.error(LABEL, "register mRemoteRegisterService is null", new Object[0]);
            return -1;
        } else if (context == null) {
            Log.error(LABEL, "register context is null", new Object[0]);
            return -1;
        } else {
            Object hostContext = context.getHostContext();
            if (!(hostContext instanceof android.content.Context)) {
                Log.error(LABEL, "register host context is null", new Object[0]);
                return -1;
            }
            Optional<IRemoteObject> remoteObjectFromContext = BinderConverter.getRemoteObjectFromContext((android.content.Context) hostContext);
            if (!remoteObjectFromContext.isPresent()) {
                Log.error(LABEL, "register abilityToken is null", new Object[0]);
                return -1;
            }
            try {
                return this.mRemoteRegisterService.register(str, remoteObjectFromContext.get(), extraParams, new ContinuationDeviceCallbackProxy(iContinuationDeviceCallback));
            } catch (RemoteException unused) {
                Log.error(LABEL, "exception in register", new Object[0]);
                return -1;
            }
        }
    }

    public boolean unregister(int i) {
        IRemoteRegisterService iRemoteRegisterService = this.mRemoteRegisterService;
        if (iRemoteRegisterService == null) {
            Log.error(LABEL, "unregister mRemoteRegisterService is null", new Object[0]);
            return false;
        }
        try {
            return iRemoteRegisterService.unregister(i);
        } catch (RemoteException unused) {
            Log.error(LABEL, "exception in unregister", new Object[0]);
            return false;
        }
    }

    public boolean updateConnectStatus(int i, String str, int i2) {
        IRemoteRegisterService iRemoteRegisterService = this.mRemoteRegisterService;
        if (iRemoteRegisterService == null) {
            Log.error(LABEL, "updateConnectStatus mRemoteRegisterService is null", new Object[0]);
            return false;
        }
        try {
            return iRemoteRegisterService.updateConnectStatus(i, str, i2);
        } catch (RemoteException unused) {
            Log.error(LABEL, "exception in updateConnectStatus", new Object[0]);
            return false;
        }
    }

    public boolean showDeviceList(int i, ExtraParams extraParams) {
        IRemoteRegisterService iRemoteRegisterService = this.mRemoteRegisterService;
        if (iRemoteRegisterService == null) {
            Log.error(LABEL, "showDeviceList mRemoteRegisterService is null", new Object[0]);
            return false;
        }
        try {
            return iRemoteRegisterService.showDeviceList(i, extraParams);
        } catch (RemoteException unused) {
            Log.error(LABEL, "exception in showDeviceList", new Object[0]);
            return false;
        }
    }
}
