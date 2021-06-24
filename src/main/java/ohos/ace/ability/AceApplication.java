package ohos.ace.ability;

import com.huawei.ace.SystemPluginLoader;
import ohos.abilityshell.HarmonyApplication;

public class AceApplication extends HarmonyApplication {
    @Override // ohos.abilityshell.HarmonyApplication
    public void onCreate() {
        SystemPluginLoader.loadPluginJsCodeAndGroupName();
        super.onCreate();
    }
}
