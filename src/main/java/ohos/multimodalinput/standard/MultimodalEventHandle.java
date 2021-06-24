package ohos.multimodalinput.standard;

import ohos.aafwk.ability.Ability;

public class MultimodalEventHandle {
    private MultimodalEventHandle() {
    }

    public static int registerStandardizedEventHandle(Ability ability, StandardizedEventHandle standardizedEventHandle) {
        return MultimodalStandardizedEventManager.getInstance().registerStandardizedEventHandle(ability, standardizedEventHandle);
    }

    public static int unregisterStandardizedEventHandle(Ability ability, StandardizedEventHandle standardizedEventHandle) {
        return MultimodalStandardizedEventManager.getInstance().unregisterStandardizedEventHandle(ability, standardizedEventHandle);
    }
}
