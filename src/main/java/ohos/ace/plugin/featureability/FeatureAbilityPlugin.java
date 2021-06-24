package ohos.ace.plugin.featureability;

import com.huawei.ace.plugin.Plugin;
import com.huawei.ace.plugin.Result;
import com.huawei.ace.runtime.ALog;
import java.util.HashSet;
import java.util.Set;
import ohos.ace.ability.AceAbility;
import ohos.ace.plugin.distribute.DistributePlugin;
import ohos.ace.plugin.featureability.dto.RequestHeader;
import ohos.ace.plugin.featureability.proxy.InterfaceProxyManager;
import ohos.app.Context;
import ohos.utils.fastjson.JSONException;
import ohos.utils.fastjson.JSONObject;

public class FeatureAbilityPlugin implements Plugin.RequestHandler {
    private static final String TAG = FeatureAbilityPlugin.class.getSimpleName();
    private AceAbility aceAbility;
    private InterfaceProxyManager proxyManager;

    public FeatureAbilityPlugin(AceAbility aceAbility2) {
        this.aceAbility = aceAbility2;
        this.proxyManager = InterfaceProxyManager.getOrAddInterfaceProxyManager(Integer.valueOf(aceAbility2.getAbilityId()));
    }

    public static void register(Context context) {
        if (!(context instanceof AceAbility)) {
            ALog.e(TAG, "context is not instance of AceAbility, register failed");
            return;
        }
        AceAbility aceAbility2 = (AceAbility) context;
        Plugin.registerPluginHandler(FeatureAbilityConstants.PLUGIN_GROUP_NAME, new FeatureAbilityPlugin(aceAbility2), Integer.valueOf(aceAbility2.getAbilityId()));
        DistributePlugin.register(aceAbility2);
    }

    public static void deregister(Context context) {
        if (!(context instanceof AceAbility)) {
            ALog.e(TAG, "context is not instance of AceAbility, deregister failed");
            return;
        }
        AceAbility aceAbility2 = (AceAbility) context;
        Plugin.registerPluginHandler(FeatureAbilityConstants.PLUGIN_GROUP_NAME, null, Integer.valueOf(aceAbility2.getAbilityId()));
        DistributePlugin.deregister(aceAbility2);
        InterfaceProxyManager.removeInterfaceProxyManager(Integer.valueOf(aceAbility2.getAbilityId()));
    }

    public static Set<String> getPluginGroup() {
        HashSet hashSet = new HashSet();
        hashSet.add(FeatureAbilityConstants.PLUGIN_GROUP_NAME);
        hashSet.addAll(DistributePlugin.getPluginGroup());
        return hashSet;
    }

    @Override // com.huawei.ace.plugin.Plugin.RequestHandler
    public void onRequest(Result result, Object obj, Object... objArr) {
        ALog.i(TAG, "onRequest");
        RequestHeader parseRequestHeader = parseRequestHeader(obj);
        String validateRequestHeader = validateRequestHeader(parseRequestHeader);
        if (validateRequestHeader != null) {
            result.replyError(2002, validateRequestHeader);
            return;
        }
        InterfaceProxyManager interfaceProxyManager = this.proxyManager;
        if (interfaceProxyManager == null) {
            ALog.w(TAG, "No proxy manager exists due to plugin has been deregistered.");
        } else {
            interfaceProxyManager.processRequest(this.aceAbility, result, parseRequestHeader, objArr);
        }
    }

    private RequestHeader parseRequestHeader(Object obj) {
        if (!(obj instanceof String)) {
            return null;
        }
        try {
            return (RequestHeader) JSONObject.parseObject((String) obj, RequestHeader.class);
        } catch (JSONException unused) {
            ALog.e(TAG, "parse param to RequestHeader failed");
            return null;
        }
    }

    private String validateRequestHeader(RequestHeader requestHeader) {
        if (requestHeader == null) {
            return "parse request param failed";
        }
        if (requestHeader.getElement() == null || requestHeader.getElement().getName() == null) {
            return "interface name can't be null";
        }
        if (!requestHeader.isRequestLocal() && requestHeader.getElement().getBundleName() == null) {
            return "bundleName of remote interface can't be null";
        }
        if (requestHeader.getCode() == null) {
            return "interface method code can't be null";
        }
        return null;
    }
}
