package ohos.ace.plugin.featureability.proxy;

import com.huawei.ace.plugin.Result;
import com.huawei.ace.runtime.ALog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import ohos.ace.ability.AceAbility;
import ohos.ace.ability.AceInternalAbility;
import ohos.ace.ability.LocalParticleAbility;
import ohos.ace.plugin.featureability.dto.InterfaceElement;
import ohos.ace.plugin.featureability.dto.RequestHeader;

public class InterfaceProxyManager {
    private static final int DEFAULT_ABILITY_ID = Integer.MIN_VALUE;
    private static final byte[] LOCK = new byte[0];
    private static final String TAG = InterfaceProxyManager.class.getSimpleName();
    private static Map<Integer, InterfaceProxyManager> proxyManagerMap = new ConcurrentHashMap();
    private Map<InterfaceElement, InterfaceProxy<?>> interfaceProxyMap = new ConcurrentHashMap();

    public static InterfaceProxyManager getOrAddInterfaceProxyManager(Integer num) {
        int i;
        synchronized (LOCK) {
            if (num == null) {
                i = Integer.MIN_VALUE;
            } else {
                i = num.intValue();
            }
            if (proxyManagerMap.containsKey(Integer.valueOf(i))) {
                return proxyManagerMap.get(Integer.valueOf(i));
            }
            InterfaceProxyManager interfaceProxyManager = new InterfaceProxyManager();
            proxyManagerMap.put(Integer.valueOf(i), interfaceProxyManager);
            return interfaceProxyManager;
        }
    }

    public static void removeInterfaceProxyManager(Integer num) {
        int i;
        synchronized (LOCK) {
            if (num == null) {
                i = Integer.MIN_VALUE;
            } else {
                i = num.intValue();
            }
            proxyManagerMap.remove(Integer.valueOf(i));
        }
    }

    public void addInterfaceProxy(InterfaceElement interfaceElement, AceInternalAbility.AceInternalAbilityHandler aceInternalAbilityHandler) {
        if (interfaceElement == null) {
            ALog.w(TAG, "interface element is null, skip add action");
        } else if (this.interfaceProxyMap.containsKey(interfaceElement)) {
            String str = TAG;
            ALog.w(str, "interface proxy already exist, skip add action, proxyKey: " + interfaceElement.toString());
        } else {
            this.interfaceProxyMap.put(interfaceElement, new InternalInterfaceProxy(this, aceInternalAbilityHandler, interfaceElement));
            String str2 = TAG;
            ALog.i(str2, "add interface proxy, proxyKey: " + interfaceElement.toString());
        }
    }

    public void addInterfaceProxy(InterfaceElement interfaceElement, AceAbility aceAbility, LocalParticleAbility localParticleAbility) {
        InvokeInterfaceProxy invokeInterfaceProxy = new InvokeInterfaceProxy(this, aceAbility, localParticleAbility, interfaceElement);
        invokeInterfaceProxy.checkConnect();
        this.interfaceProxyMap.put(interfaceElement, invokeInterfaceProxy);
        String str = TAG;
        ALog.i(str, "add interface proxy, proxyKey: " + interfaceElement.toString());
    }

    public InterfaceProxy<?> removeInterfaceProxy(InterfaceElement interfaceElement) {
        if (interfaceElement == null) {
            ALog.w(TAG, "interface element is null, skip remove action");
            return null;
        }
        InterfaceProxy<?> remove = this.interfaceProxyMap.remove(interfaceElement);
        if (remove == null) {
            String str = TAG;
            ALog.w(str, "interface proxy is not exists, interfaceElement: " + interfaceElement.toString());
            return null;
        }
        remove.releaseConnect();
        String str2 = TAG;
        ALog.i(str2, "remove interface proxy, interfaceElement: " + interfaceElement.toString());
        return remove;
    }

    public InterfaceProxy<?> getInterfaceProxy(InterfaceElement interfaceElement) {
        if (this.interfaceProxyMap.containsKey(interfaceElement)) {
            return this.interfaceProxyMap.get(interfaceElement);
        }
        return null;
    }

    private InterfaceProxy<?> getOrAddInterfaceProxy(InterfaceElement interfaceElement, AceAbility aceAbility) {
        InterfaceProxy<?> interfaceProxy = getInterfaceProxy(interfaceElement);
        if (interfaceProxy == null) {
            synchronized (LOCK) {
                interfaceProxy = getInterfaceProxy(interfaceElement);
                if (interfaceProxy == null) {
                    InterfaceProxy<?> createInterfaceProxy = createInterfaceProxy(interfaceElement, aceAbility);
                    if (!(createInterfaceProxy == null || interfaceElement.getType() == 1)) {
                        this.interfaceProxyMap.put(interfaceElement, createInterfaceProxy);
                        String str = TAG;
                        ALog.i(str, "add interface proxy, interfaceElement: " + interfaceElement.toString());
                    }
                    interfaceProxy = createInterfaceProxy;
                }
            }
        }
        return interfaceProxy;
    }

    private InterfaceProxy<?> createInterfaceProxy(InterfaceElement interfaceElement, AceAbility aceAbility) {
        int type = interfaceElement.getType();
        if (type == 0 || type == 2) {
            return new RemoteInterfaceProxy(this, aceAbility, interfaceElement);
        }
        if (type == 3) {
            return new InvokeInterfaceProxy(this, aceAbility, interfaceElement);
        }
        if (type == 1) {
            ALog.w(TAG, "transmit local interface proxy appoint from shared to current ability if exists");
            InterfaceProxyManager interfaceProxyManager = proxyManagerMap.containsKey(Integer.MIN_VALUE) ? proxyManagerMap.get(Integer.MIN_VALUE) : null;
            if (interfaceProxyManager == null) {
                return null;
            }
            return interfaceProxyManager.getInterfaceProxy(interfaceElement);
        }
        String str = TAG;
        ALog.e(str, "not support interface type, interfaceElement: " + interfaceElement.toString());
        return null;
    }

    public void processRequest(AceAbility aceAbility, Result result, RequestHeader requestHeader, Object... objArr) {
        InterfaceProxy<?> orAddInterfaceProxy = getOrAddInterfaceProxy(requestHeader.getElement(), aceAbility);
        if (orAddInterfaceProxy == null) {
            String str = TAG;
            ALog.e(str, "request interface proxy is not exists, interfaceElement: " + requestHeader.getElement().toString());
            result.replyError(2004, "interface is not exists or register.");
            return;
        }
        orAddInterfaceProxy.processRequest(result, requestHeader, objArr);
    }
}
