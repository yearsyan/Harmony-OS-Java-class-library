package ohos.ace.plugin.featureability.proxy;

import ohos.ace.ability.AceInternalAbility;
import ohos.ace.plugin.featureability.dto.InterfaceElement;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class InternalInterfaceProxy extends InterfaceProxy<AceInternalAbility.AceInternalAbilityHandler> {
    private static final String TAG = InternalInterfaceProxy.class.getSimpleName();

    public InternalInterfaceProxy(InterfaceProxyManager interfaceProxyManager, AceInternalAbility.AceInternalAbilityHandler aceInternalAbilityHandler, InterfaceElement interfaceElement) {
        super(interfaceProxyManager, null, interfaceElement);
        this.interfaceInstance = aceInternalAbilityHandler;
    }

    @Override // ohos.ace.plugin.featureability.proxy.InterfaceProxy
    public boolean checkConnect() {
        return this.interfaceElement != null;
    }

    /* access modifiers changed from: protected */
    @Override // ohos.ace.plugin.featureability.proxy.InterfaceProxy
    public boolean writeReply(MessageParcel messageParcel, MessageParcel messageParcel2, IRemoteObject iRemoteObject) {
        return messageParcel2.writeRemoteObject(iRemoteObject);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.ace.plugin.featureability.proxy.InterfaceProxy
    public boolean sendRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        return ((AceInternalAbility.AceInternalAbilityHandler) this.interfaceInstance).onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
    }
}
