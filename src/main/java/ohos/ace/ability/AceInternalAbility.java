package ohos.ace.ability;

import ohos.ace.plugin.featureability.dto.InterfaceElement;
import ohos.ace.plugin.featureability.proxy.InterfaceProxyManager;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class AceInternalAbility {
    private static final String TAG = "AceInternalAbility";
    private String abilityName;
    private String bundleName;

    public interface AceInternalAbilityHandler {
        boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException;
    }

    public AceInternalAbility(String str, String str2) {
        this.bundleName = str;
        this.abilityName = str2;
    }

    public AceInternalAbility(String str) {
        this(null, str);
    }

    public void setInternalAbilityHandler(AceInternalAbilityHandler aceInternalAbilityHandler) {
        setInternalAbilityHandler(aceInternalAbilityHandler, null);
    }

    public void setInternalAbilityHandler(AceInternalAbilityHandler aceInternalAbilityHandler, AceAbility aceAbility) {
        Integer num = null;
        if (aceInternalAbilityHandler == null) {
            if (aceAbility != null) {
                num = Integer.valueOf(aceAbility.getAbilityId());
            }
            InterfaceProxyManager.getOrAddInterfaceProxyManager(num).removeInterfaceProxy(new InterfaceElement(this.bundleName, this.abilityName));
            return;
        }
        if (aceAbility != null) {
            num = Integer.valueOf(aceAbility.getAbilityId());
        }
        InterfaceProxyManager.getOrAddInterfaceProxyManager(num).addInterfaceProxy(new InterfaceElement(this.bundleName, this.abilityName), aceInternalAbilityHandler);
    }
}
