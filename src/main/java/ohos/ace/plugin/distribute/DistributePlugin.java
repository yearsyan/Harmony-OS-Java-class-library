package ohos.ace.plugin.distribute;

import com.huawei.ace.plugin.Plugin;
import com.huawei.ace.plugin.Result;
import com.huawei.ace.runtime.ALog;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.ace.ability.AceAbility;
import ohos.ace.plugin.distribute.dto.DistributeReqParam;
import ohos.bundle.ElementName;
import ohos.utils.fastjson.JSONException;
import ohos.utils.fastjson.JSONObject;

public class DistributePlugin implements Plugin.RequestHandler {
    private static final String TAG = DistributePlugin.class.getSimpleName();
    private static int requestCode = 0;
    private static final Map<Integer, Result> startResultMap = new HashMap();
    private AceAbility aceAbility;

    public DistributePlugin(AceAbility aceAbility2) {
        this.aceAbility = aceAbility2;
    }

    public static void register(AceAbility aceAbility2) {
        if (!(aceAbility2 instanceof AceAbility)) {
            ALog.e(TAG, "context is not instance of AceAbility, register failed");
        } else {
            Plugin.registerPluginHandler(DistributeConstants.PLUGIN_GROUP_NAME, new DistributePlugin(aceAbility2), Integer.valueOf(aceAbility2.getAbilityId()));
        }
    }

    public static void deregister(AceAbility aceAbility2) {
        if (!(aceAbility2 instanceof AceAbility)) {
            ALog.e(TAG, "context is not instance of AceAbility, deregister failed");
        } else {
            Plugin.registerPluginHandler(DistributeConstants.PLUGIN_GROUP_NAME, null, Integer.valueOf(aceAbility2.getAbilityId()));
        }
    }

    public static Set<String> getPluginGroup() {
        HashSet hashSet = new HashSet();
        hashSet.add(DistributeConstants.PLUGIN_GROUP_NAME);
        return hashSet;
    }

    @Override // com.huawei.ace.plugin.Plugin.RequestHandler
    public void onRequest(Result result, Object obj, Object... objArr) {
        ALog.i(TAG, "onRequest");
        if (!(obj instanceof String)) {
            result.replyError(2002, "method name is not a String");
        } else if (!(this.aceAbility instanceof Ability)) {
            result.replyError(2001, "context is not instance of Ability");
        } else {
            String str = (String) obj;
            if (DistributeConstants.METHOD_CONTINUE_ABILITY.equals(str)) {
                continueAbility(result);
            } else if (objArr.length < 1) {
                result.replyError(2002, "params cont be null");
            } else {
                char c = 65535;
                int hashCode = str.hashCode();
                if (hashCode != -2077801098) {
                    if (hashCode != -1334954392) {
                        if (hashCode == 1553360062 && str.equals(DistributeConstants.METHOD_START_ABILITY_FOR_RESULT)) {
                            c = 1;
                        }
                    } else if (str.equals(DistributeConstants.METHOD_START_ABILITY)) {
                        c = 0;
                    }
                } else if (str.equals(DistributeConstants.METHOD_FINISH_WITH_RESULT)) {
                    c = 2;
                }
                if (c == 0) {
                    startAbility(result, objArr[0], false);
                } else if (c == 1) {
                    startAbility(result, objArr[0], true);
                } else if (c != 2) {
                    result.notExistFunction();
                } else {
                    finishAbilityWithResult(result, objArr[0], objArr.length > 1 ? objArr[1] : null);
                }
            }
        }
    }

    public static void onAbilityResultCallback(int i, int i2, Intent intent) {
        if (startResultMap.containsKey(Integer.valueOf(i))) {
            Result result = startResultMap.get(Integer.valueOf(i));
            if (result == null) {
                ALog.e(TAG, "resultHandler is null");
            } else if (intent == null || !intent.hasParameter("resultData")) {
                ALog.w(TAG, "ability result callback null or doesn't contain param resultData!");
                if (i2 == 0) {
                    result.success(null);
                } else {
                    result.error(i2, null);
                }
                startResultMap.remove(Integer.valueOf(i));
            } else {
                if (i2 == 0) {
                    result.success(intent.getStringParam("resultData"));
                } else if (i2 == -1) {
                    result.success(null);
                } else {
                    result.replyError(i2, intent.getStringParam("resultData"));
                }
                startResultMap.remove(Integer.valueOf(i));
            }
        } else {
            String str = TAG;
            ALog.e(str, "requestCode not found:" + i);
        }
    }

    private void continueAbility(Result result) {
        this.aceAbility.getUITaskDispatcher().asyncDispatch(new Runnable(result) {
            /* class ohos.ace.plugin.distribute.$$Lambda$DistributePlugin$l3eOnKCX1Hqm7ET6HyqBBNoJ2k */
            private final /* synthetic */ Result f$1;

            {
                this.f$1 = r2;
            }

            public final void run() {
                DistributePlugin.this.lambda$continueAbility$0$DistributePlugin(this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$continueAbility$0$DistributePlugin(Result result) {
        try {
            this.aceAbility.continueAbility();
            result.success(null);
        } catch (IllegalStateException | UnsupportedOperationException e) {
            String str = TAG;
            ALog.e(str, "continue ability failed:" + e.getMessage());
            result.replyError(2013, "continue ability failed:" + e.getMessage());
        }
    }

    private void finishAbilityWithResult(Result result, Object obj, Object obj2) {
        if (!(obj instanceof Integer) || (obj2 != null && !(obj2 instanceof String))) {
            result.replyError(2002, "code is not int or result is not String");
            return;
        }
        try {
            Intent intent = new Intent();
            if (obj2 != null) {
                IntentParams intentParams = new IntentParams();
                intentParams.setParam("resultData", (String) obj2);
                intent.setParams(intentParams);
            }
            this.aceAbility.setResult(((Integer) obj).intValue(), intent);
            this.aceAbility.terminateAbility();
        } catch (IllegalArgumentException | IllegalStateException e) {
            String str = TAG;
            ALog.e(str, "Finish ability failed:" + e.getMessage());
            result.replyError(2014, "Finish ability failed:" + e.getMessage());
        }
    }

    private void startAbility(Result result, Object obj, boolean z) {
        DistributeReqParam parseRequestParam = parseRequestParam(obj);
        String validateRequestParam = validateRequestParam(parseRequestParam);
        if (validateRequestParam != null) {
            result.replyError(2002, validateRequestParam);
            return;
        }
        this.aceAbility.getUITaskDispatcher().asyncDispatch(new Runnable(z, getIntent(parseRequestParam), result) {
            /* class ohos.ace.plugin.distribute.$$Lambda$DistributePlugin$OSTw0dz5uCzdVIsbOWleKK9u7U */
            private final /* synthetic */ boolean f$1;
            private final /* synthetic */ Intent f$2;
            private final /* synthetic */ Result f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final void run() {
                DistributePlugin.this.lambda$startAbility$1$DistributePlugin(this.f$1, this.f$2, this.f$3);
            }
        });
    }

    public /* synthetic */ void lambda$startAbility$1$DistributePlugin(boolean z, Intent intent, Result result) {
        if (!z) {
            try {
                this.aceAbility.startAbility(intent);
                result.success(null);
            } catch (IllegalArgumentException | IllegalStateException e) {
                ALog.e(TAG, "Start ability failed:" + e.getMessage());
                result.replyError(2012, "Start ability failed:" + e.getMessage());
            }
        } else {
            intent.removeFlags(256);
            if (requestCode == 65535) {
                requestCode = 0;
            } else {
                requestCode++;
            }
            this.aceAbility.startAbilityForResult(intent, requestCode);
            startResultMap.put(Integer.valueOf(requestCode), result);
        }
    }

    private Intent getIntent(DistributeReqParam distributeReqParam) {
        Intent intent = new Intent();
        if (distributeReqParam.getDeviceType() == 0) {
            intent.addFlags(256);
        }
        intent.addFlags(distributeReqParam.getFlag());
        if (distributeReqParam.getData() != null) {
            intent.setParam(DistributeConstants.START_ABILITY_PARAMS_KEY, distributeReqParam.getData());
        }
        if (distributeReqParam.getUrl() != null) {
            intent.setParam("url", distributeReqParam.getUrl());
        }
        if (distributeReqParam.getBundleName() == null || distributeReqParam.getAbilityName() == null) {
            intent.getOperation().setAction(distributeReqParam.getAction());
            if (distributeReqParam.getEntities() != null) {
                for (String str : distributeReqParam.getEntities()) {
                    intent.getOperation().addEntity(str);
                }
            }
        } else {
            intent.setElement(new ElementName(distributeReqParam.getDeviceId(), distributeReqParam.getBundleName(), distributeReqParam.getAbilityName()));
        }
        return intent;
    }

    private DistributeReqParam parseRequestParam(Object obj) {
        if (!(obj instanceof String)) {
            return null;
        }
        try {
            return (DistributeReqParam) JSONObject.parseObject((String) obj, DistributeReqParam.class);
        } catch (JSONException unused) {
            ALog.e(TAG, "parse param to DistributeReqParam failed");
            return null;
        }
    }

    private String validateRequestParam(DistributeReqParam distributeReqParam) {
        if (distributeReqParam == null) {
            return "parse request param failed";
        }
        if ((distributeReqParam.getBundleName() == null || distributeReqParam.getAbilityName() == null) && distributeReqParam.getAction() == null) {
            return "bundleName and abilityName or action can't be all null";
        }
        return null;
    }
}
