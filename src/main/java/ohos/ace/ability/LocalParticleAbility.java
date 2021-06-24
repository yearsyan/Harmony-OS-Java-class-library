package ohos.ace.ability;

import ohos.ace.plugin.featureability.dto.InterfaceElement;
import ohos.ace.plugin.featureability.proxy.InterfaceProxyManager;

public interface LocalParticleAbility {

    public interface Callback {
        void reply(Object obj);
    }

    static default LocalParticleAbility getInstance() {
        return null;
    }

    default void register(AceAbility aceAbility) {
        if (aceAbility != null) {
            InterfaceProxyManager.getOrAddInterfaceProxyManager(Integer.valueOf(aceAbility.getAbilityId())).addInterfaceProxy(new InterfaceElement(null, getClass().getName(), 3), aceAbility, this);
        }
    }

    default void deregister(AceAbility aceAbility) {
        if (aceAbility != null) {
            InterfaceProxyManager.getOrAddInterfaceProxyManager(Integer.valueOf(aceAbility.getAbilityId())).removeInterfaceProxy(new InterfaceElement(null, getClass().getName(), 3));
        }
    }
}
