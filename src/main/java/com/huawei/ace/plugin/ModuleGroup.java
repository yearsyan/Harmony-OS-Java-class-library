package com.huawei.ace.plugin;

import com.huawei.ace.plugin.Plugin;

public final class ModuleGroup {
    private final String name;

    public interface ModuleGroupHandler extends Plugin.RequestHandler {
        void onFunctionCall(Function function, Result result);
    }

    public ModuleGroup(String str) {
        this.name = str;
    }

    public void setModuleGroupHandler(ModuleGroupHandler moduleGroupHandler) {
        setModuleGroupHandler(moduleGroupHandler, null);
    }

    public void setModuleGroupHandler(ModuleGroupHandler moduleGroupHandler, Integer num) {
        Plugin.registerPluginHandler(this.name, moduleGroupHandler, num);
    }

    public static void registerModuleGroup(String str, ModuleGroupHandler moduleGroupHandler, Integer num) {
        Plugin.registerPluginHandler(str, moduleGroupHandler, num);
    }
}
