package ohos.ace.plugin.featureability.proxy;

import com.huawei.ace.plugin.Callback;
import com.huawei.ace.plugin.Result;
import com.huawei.ace.runtime.ALog;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import ohos.ace.ability.AceAbility;
import ohos.ace.ability.LocalParticleAbility;
import ohos.ace.plugin.featureability.dto.InterfaceElement;
import ohos.ace.plugin.featureability.dto.RequestHeader;
import ohos.utils.fastjson.JSONException;
import ohos.utils.fastjson.JSONObject;

public class InvokeInterfaceProxy extends InterfaceProxy<LocalParticleAbility> {
    private static final String TAG = InvokeInterfaceProxy.class.getSimpleName();
    private boolean jsClientReady;
    private Map<String, Method> methodMap;

    public InvokeInterfaceProxy(InterfaceProxyManager interfaceProxyManager, AceAbility aceAbility, InterfaceElement interfaceElement) {
        this(interfaceProxyManager, aceAbility, null, interfaceElement);
    }

    public InvokeInterfaceProxy(InterfaceProxyManager interfaceProxyManager, AceAbility aceAbility, LocalParticleAbility localParticleAbility, InterfaceElement interfaceElement) {
        super(interfaceProxyManager, aceAbility, interfaceElement);
        this.jsClientReady = false;
        this.methodMap = new HashMap();
        this.interfaceInstance = localParticleAbility;
    }

    @Override // ohos.ace.plugin.featureability.proxy.InterfaceProxy
    public boolean checkConnect() {
        if (!this.jsClientReady) {
            synchronized (this.lock) {
                if (!this.jsClientReady) {
                    if (createInstance()) {
                        if (parseInterfaceMethod()) {
                            createJsCient();
                            this.jsClientReady = true;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override // ohos.ace.plugin.featureability.proxy.InterfaceProxy
    public void releaseConnect() {
        String str = TAG;
        ALog.i(str, "release local particle ability instance, interfaceElement: " + this.interfaceElement.toString());
        releaseJsCient();
        this.methodMap.clear();
    }

    private boolean createInstance() {
        String str = TAG;
        ALog.i(str, "create local particle ability instance, interfaceElement: " + this.interfaceElement.toString());
        if (this.interfaceInstance != null) {
            return true;
        }
        if (this.aceAbility == null) {
            ALog.e(TAG, "the ability context is null, you should give a AceAbility instance when manual register");
            return false;
        }
        try {
            Class<?> loadClass = this.aceAbility.getClassloader().loadClass(this.interfaceElement.getName());
            if (LocalParticleAbility.class.isAssignableFrom(loadClass) && !createInstanceByMethod(loadClass)) {
                this.interfaceInstance = (LocalParticleAbility) loadClass.getConstructor(new Class[0]).newInstance(new Object[0]);
            }
        } catch (ClassNotFoundException e) {
            String str2 = TAG;
            ALog.e(str2, "get interface class exception, e: " + e.toString());
        } catch (NoSuchMethodException | SecurityException e2) {
            String str3 = TAG;
            ALog.e(str3, "get default constructor method exception, e: " + e2.toString());
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e3) {
            String str4 = TAG;
            ALog.e(str4, "construct interface proxy instance exception, e: " + e3.toString());
        }
        if (this.interfaceInstance != null) {
            return true;
        }
        return false;
    }

    private boolean createInstanceByMethod(Class<?> cls) {
        try {
            Method method = cls.getMethod("getInstance", new Class[0]);
            if (Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers()) && LocalParticleAbility.class.isAssignableFrom(method.getReturnType())) {
                this.interfaceInstance = (LocalParticleAbility) method.invoke(null, new Object[0]);
            }
        } catch (NoSuchMethodException | SecurityException unused) {
            ALog.w(TAG, "get method 'getInstance' exception");
        } catch (IllegalAccessException | InvocationTargetException unused2) {
            ALog.w(TAG, "invoke method 'getInstance' exception");
        }
        if (this.interfaceInstance != null) {
            return true;
        }
        return false;
    }

    private boolean parseInterfaceMethod() {
        if (!this.methodMap.isEmpty()) {
            return true;
        }
        Method[] methods = ((LocalParticleAbility) this.interfaceInstance).getClass().getMethods();
        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                this.methodMap.put(method.getName(), method);
            }
        }
        return !this.methodMap.isEmpty();
    }

    private void createJsCient() {
        StringBuffer stringBuffer = new StringBuffer("var jsInterface = global.createLocalParticleAbility(\"");
        stringBuffer.append(this.interfaceElement.getName());
        stringBuffer.append("\", true);");
        stringBuffer.append("if (jsInterface) {");
        for (String str : this.methodMap.keySet()) {
            stringBuffer.append("jsInterface.");
            stringBuffer.append(str);
            stringBuffer.append(" = function(...datas)");
            stringBuffer.append("{return jsInterface.__invoke(\"");
            stringBuffer.append(str);
            stringBuffer.append("\", ...datas)};");
        }
        stringBuffer.append("}");
        String str2 = TAG;
        ALog.d(str2, "create dynamic js interface client, interfaceElement: " + this.interfaceElement.toString());
        this.aceAbility.loadJsCode(stringBuffer.toString());
    }

    private void releaseJsCient() {
        if (this.jsClientReady) {
            StringBuffer stringBuffer = new StringBuffer("var jsInterface = global.createLocalParticleAbility(\"");
            stringBuffer.append(this.interfaceElement.getName());
            stringBuffer.append("\", true);");
            stringBuffer.append("if (jsInterface) {jsInterface.__release();}");
            String str = TAG;
            ALog.d(str, "release dynamic js interface client, interfaceElement: " + this.interfaceElement.toString());
            this.aceAbility.loadJsCode(stringBuffer.toString());
        }
    }

    private Method getMethod(Object obj) {
        if (!(obj instanceof String)) {
            return null;
        }
        return this.methodMap.get((String) obj);
    }

    private Object[] getParams(Method method, int i, Object... objArr) {
        int i2 = 0;
        try {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] objArr2 = new Object[parameterTypes.length];
            while (true) {
                if (i2 >= parameterTypes.length || objArr == null) {
                    break;
                } else if (i >= objArr.length) {
                    break;
                } else {
                    if (objArr[i] != null && !parameterTypes[i2].isPrimitive()) {
                        if (!parameterTypes[i2].isInstance(objArr[i])) {
                            if (objArr[i] instanceof Callback) {
                                objArr2[i2] = new CallbackImpl((Callback) objArr[i]);
                            } else if (parameterTypes[i2].equals(String.class)) {
                                objArr2[i2] = objArr[i].toString();
                            } else if (objArr[i] instanceof String) {
                                objArr2[i2] = JSONObject.parseObject((String) objArr[i], parameterTypes[i2]);
                            } else {
                                String str = TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("parameter index '");
                                int i3 = i2 + 1;
                                sb.append(i3);
                                sb.append("' type error");
                                ALog.e(str, sb.toString());
                                throw new IllegalArgumentException("parameter index '" + i3 + "' type error");
                            }
                            i2++;
                            i++;
                        }
                    }
                    objArr2[i2] = objArr[i];
                    i2++;
                    i++;
                }
            }
            return objArr2;
        } catch (JSONException e) {
            String str2 = TAG;
            ALog.e(str2, "parameter index '" + 0 + "' parse exception, e: " + e.toString());
            throw new IllegalArgumentException("parameter index '" + 0 + "' type error");
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.ace.plugin.featureability.proxy.InterfaceProxy
    public void process(Result result, RequestHeader requestHeader, Object... objArr) {
        Method method = getMethod(objArr.length > 0 ? objArr[0] : null);
        if (method == null) {
            ALog.e(TAG, "no such method in interface");
            result.replyError(104, "method not exists");
            return;
        }
        try {
            result.replySuccess(method.invoke(this.interfaceInstance, getParams(method, 1, objArr)));
        } catch (IllegalAccessException | InvocationTargetException e) {
            String str = TAG;
            ALog.e(str, "process method exception, e: " + e.toString());
            result.replyError(2006, "process method exception");
        } catch (IllegalArgumentException e2) {
            result.replyError(2002, e2.toString());
        }
    }

    public static class CallbackImpl implements LocalParticleAbility.Callback {
        private Callback callback;

        public CallbackImpl(Callback callback2) {
            this.callback = callback2;
        }

        @Override // ohos.ace.ability.LocalParticleAbility.Callback
        public void reply(Object obj) {
            this.callback.reply(obj);
        }
    }
}
