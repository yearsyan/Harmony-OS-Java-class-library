package ohos.aafwk.ability;

import ohos.aafwk.content.Intent;
import ohos.aafwk.utils.log.Log;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

/* access modifiers changed from: package-private */
public interface ISupplyHost extends IRemoteBroker {
    public static final String ACQUIRE_TYPE = "form_acquire_form";
    public static final String DESCRIPTOR = "ohos.aafwk.ability.ISupplyHost";
    public static final String FORM_CONNECT_ID = "form_connect_id";
    public static final String FORM_SUPPLY_INFO = "form_supply_info";
    public static final String PROVIDER_FLAG = "provider_flag";
    public static final int TRANSACTION_EVENT_HANDLE = 2;
    public static final int TRANSACTION_FORM_ACQUIRED = 1;

    void onAcquire(ProviderFormInfo providerFormInfo, Intent intent) throws RemoteException;

    void onEventHandle(Intent intent) throws RemoteException;

    public static abstract class SupplyHostStub extends RemoteObject implements ISupplyHost {
        @Override // ohos.rpc.IRemoteBroker
        public IRemoteObject asObject() {
            return this;
        }

        SupplyHostStub() {
            super("SupplyHostStub");
        }

        static ISupplyHost asProxy(IRemoteObject iRemoteObject) {
            if (iRemoteObject == null) {
                return null;
            }
            return new SupplyHostProxy(iRemoteObject);
        }

        @Override // ohos.rpc.RemoteObject
        public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
            if (messageParcel == null || messageParcel2 == null) {
                return false;
            }
            String readInterfaceToken = messageParcel.readInterfaceToken();
            if (!ISupplyHost.DESCRIPTOR.equals(readInterfaceToken)) {
                Log.error("SupplyHostStub readToken failed: %{public}s", readInterfaceToken);
                return false;
            }
            if (i == 1) {
                Intent intent = new Intent();
                if (!messageParcel.readSequenceable(intent)) {
                    return false;
                }
                ProviderFormInfo providerFormInfo = null;
                if (intent.getBooleanParam(ISupplyHost.PROVIDER_FLAG, false)) {
                    providerFormInfo = ProviderFormInfo.createFromParcel(messageParcel);
                }
                onAcquire(providerFormInfo, intent);
            } else if (i != 2) {
                Log.warn("onRemoteRequest error, code is %{public}d", Integer.valueOf(i));
                return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
            } else {
                Intent intent2 = new Intent();
                if (!messageParcel.readSequenceable(intent2)) {
                    return false;
                }
                onEventHandle(intent2);
            }
            return true;
        }

        /* access modifiers changed from: private */
        public static class SupplyHostProxy implements ISupplyHost {
            private IRemoteObject remote;

            SupplyHostProxy(IRemoteObject iRemoteObject) {
                this.remote = iRemoteObject;
            }

            @Override // ohos.rpc.IRemoteBroker
            public IRemoteObject asObject() {
                return this.remote;
            }

            @Override // ohos.aafwk.ability.ISupplyHost
            public void onAcquire(ProviderFormInfo providerFormInfo, Intent intent) throws RemoteException {
                MessageParcel obtain = MessageParcel.obtain();
                MessageParcel obtain2 = MessageParcel.obtain();
                if (!obtain.writeInterfaceToken(ISupplyHost.DESCRIPTOR)) {
                    Log.error("onAcquire writeInterfaceToken fail: %{public}s", ISupplyHost.DESCRIPTOR);
                    return;
                }
                obtain.writeSequenceable(intent);
                if (intent.getBooleanParam(ISupplyHost.PROVIDER_FLAG, false)) {
                    obtain.writeSequenceable(providerFormInfo);
                }
                try {
                    this.remote.sendRequest(1, obtain, obtain2, new MessageOption(1));
                } finally {
                    obtain.reclaim();
                    obtain2.reclaim();
                }
            }

            @Override // ohos.aafwk.ability.ISupplyHost
            public void onEventHandle(Intent intent) throws RemoteException {
                MessageParcel obtain = MessageParcel.obtain();
                MessageParcel obtain2 = MessageParcel.obtain();
                if (!obtain.writeInterfaceToken(ISupplyHost.DESCRIPTOR)) {
                    Log.error("onEventHandle writeInterfaceToken fail: %{public}s", ISupplyHost.DESCRIPTOR);
                    return;
                }
                obtain.writeSequenceable(intent);
                try {
                    this.remote.sendRequest(2, obtain, obtain2, new MessageOption(1));
                } finally {
                    obtain.reclaim();
                    obtain2.reclaim();
                }
            }
        }
    }
}
