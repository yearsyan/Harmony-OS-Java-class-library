package com.huawei.ace.systemplugin.httpaccess;

import com.huawei.ace.plugin.ErrorCode;
import com.huawei.ace.plugin.Function;
import com.huawei.ace.plugin.ModuleGroup;
import com.huawei.ace.plugin.Result;
import com.huawei.ace.systemplugin.LogUtil;
import com.huawei.ace.systemplugin.httpaccess.data.RequestData;
import com.huawei.ace.systemplugin.httpaccess.data.ResponseData;
import java.util.HashSet;
import java.util.Set;
import ohos.ace.ability.AceAbility;
import ohos.app.Context;
import ohos.com.sun.org.apache.xalan.internal.templates.Constants;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import ohos.utils.fastjson.JSON;
import ohos.utils.fastjson.JSONException;
import ohos.utils.fastjson.JSONObject;
import ohos.utils.zson.ZSONObject;

public class HttpAccessPlugin implements ModuleGroup.ModuleGroupHandler, ErrorCode {
    private static final String COMMAND_DOWNLOAD = "download";
    private static final String COMMAND_FETCH = "fetch";
    private static final String COMMAND_ONDOWNLOADCOMPLETE = "onDownloadComplete";
    private static final String COMMAND_UPLOAD = "upload";
    private static final String MODULE_GROUP_NAME = "AceModuleGroup/HttpAccess";
    private static final String TAG = "HttpAccessPlugin";
    private static HttpAccessPlugin instance;
    private Context applicationContext;

    public static String getJsCode() {
        return "var fetch = {\n    fetch : async function fetch(param) {\n        var httpModuleGroup = null;\n        if (httpModuleGroup == null) {\n            httpModuleGroup = ModuleGroup.getGroup(\"AceModuleGroup/HttpAccess\");\n        }\n        return await catching(httpModuleGroup.callNative(\"fetch\", param), param);\n    }\n};\nvar request = {\n    onDownloadComplete : async function onDownloadComplete(param) {\n        var httpModuleGroup = null;\n        if (httpModuleGroup == null) {\n            httpModuleGroup = ModuleGroup.getGroup(\"AceModuleGroup/HttpAccess\");\n        }\n        return await catching(httpModuleGroup.callNative(\"onDownloadComplete\", param), param);\n    },\n    upload : async function upload(param) {\n        var httpModuleGroup = null;\n        if (httpModuleGroup == null) {\n            httpModuleGroup = ModuleGroup.getGroup(\"AceModuleGroup/HttpAccess\");\n        }\n        return await catching(httpModuleGroup.callNative(\"upload\", param), param);\n    },\n    download : async function download(param) {\n        var httpModuleGroup = null;\n        if (httpModuleGroup == null) {\n            httpModuleGroup = ModuleGroup.getGroup(\"AceModuleGroup/HttpAccess\");\n        }\n        return await catching(httpModuleGroup.callNative(\"download\", param), param);\n    }\n};\nglobal.systemplugin.fetch = fetch;\nglobal.systemplugin.request = request;";
    }

    @Override // com.huawei.ace.plugin.ModuleGroup.ModuleGroupHandler
    public void onFunctionCall(Function function, final Result result) {
        RequestData requestData;
        LogUtil.debug(TAG, "enter httpaccess function");
        Object obj = function.arguments.get(0);
        try {
            requestData = (RequestData) ZSONObject.stringToClass(obj instanceof String ? (String) obj : "", RequestData.class);
        } catch (JSONException unused) {
            LogUtil.error(TAG, "parse json error");
            requestData = null;
        }
        if (requestData == null) {
            result.error(202, "request param is illegal");
            return;
        }
        if (COMMAND_FETCH.equals(function.name)) {
            LogUtil.debug(TAG, "enter fetch command");
            new HttpAccess().fetch(requestData, new HttpProbe() {
                /* class com.huawei.ace.systemplugin.httpaccess.HttpAccessPlugin.AnonymousClass1 */

                @Override // com.huawei.ace.systemplugin.httpaccess.HttpProbe
                public void onResponse(ResponseData responseData) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("code", (Object) Integer.valueOf(responseData.getCode()));
                    jSONObject.put("data", (Object) responseData.getData());
                    if (responseData.getCode() == 200) {
                        jSONObject.put("headers", (Object) JSON.toJSONString(responseData.getHeaders()));
                        result.success(jSONObject);
                        return;
                    }
                    LogUtil.error(HttpAccessPlugin.TAG, "fetch data error!");
                    result.error(200, jSONObject);
                }
            }, this.applicationContext);
        }
        if (COMMAND_DOWNLOAD.equals(function.name)) {
            LogUtil.debug(TAG, "enter download command");
            new HttpAccess().download(requestData, new HttpProbe() {
                /* class com.huawei.ace.systemplugin.httpaccess.HttpAccessPlugin.AnonymousClass2 */

                @Override // com.huawei.ace.systemplugin.httpaccess.HttpProbe
                public void onResponse(ResponseData responseData) {
                    if (responseData.getCode() == 8) {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put(SchemaSymbols.ATTVAL_TOKEN, (Object) Long.valueOf(responseData.getToken()));
                        result.success(jSONObject);
                        return;
                    }
                    LogUtil.error(HttpAccessPlugin.TAG, "download error!");
                    result.error(400, "Download error!");
                }
            }, this.applicationContext);
        }
        if (COMMAND_ONDOWNLOADCOMPLETE.equals(function.name)) {
            LogUtil.debug(TAG, "enter onDownloadComplete command");
            new HttpAccess().onDownloadComplete(requestData, new HttpProbe() {
                /* class com.huawei.ace.systemplugin.httpaccess.HttpAccessPlugin.AnonymousClass3 */

                @Override // com.huawei.ace.systemplugin.httpaccess.HttpProbe
                public void onResponse(ResponseData responseData) {
                    if (responseData.getCode() == 8) {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put(Constants.ELEMNAME_URL_STRING, (Object) responseData.getUri());
                        result.success(jSONObject);
                    } else if (responseData.getCode() == 401) {
                        LogUtil.error(HttpAccessPlugin.TAG, "download task does not exsist!");
                        result.error(401, "Download task does not exsist!");
                    } else {
                        LogUtil.error(HttpAccessPlugin.TAG, "download error!");
                        result.error(400, "Download error!");
                    }
                }
            }, this.applicationContext);
        }
        if (COMMAND_UPLOAD.equals(function.name)) {
            LogUtil.debug(TAG, "enter upload command");
            new HttpAccess().upload(requestData, new HttpProbe() {
                /* class com.huawei.ace.systemplugin.httpaccess.HttpAccessPlugin.AnonymousClass4 */

                @Override // com.huawei.ace.systemplugin.httpaccess.HttpProbe
                public void onResponse(ResponseData responseData) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("code", (Object) Integer.valueOf(responseData.getCode()));
                    jSONObject.put("data", (Object) responseData.getData());
                    if (responseData.getCode() == 200) {
                        jSONObject.put("headers", (Object) JSON.toJSONString(responseData.getHeaders()));
                        result.success(jSONObject);
                        return;
                    }
                    LogUtil.error(HttpAccessPlugin.TAG, "upload error!");
                    result.error(200, jSONObject);
                }
            }, this.applicationContext);
        }
    }

    public static void register(Context context) {
        instance = new HttpAccessPlugin();
        instance.onRegister(context);
        ModuleGroup.registerModuleGroup(MODULE_GROUP_NAME, instance, context instanceof AceAbility ? Integer.valueOf(((AceAbility) context).getAbilityId()) : null);
    }

    private void onRegister(Context context) {
        this.applicationContext = context;
    }

    public static void deregister(Context context) {
        ModuleGroup.registerModuleGroup(MODULE_GROUP_NAME, null, context instanceof AceAbility ? Integer.valueOf(((AceAbility) context).getAbilityId()) : null);
    }

    public static Set<String> getPluginGroup() {
        HashSet hashSet = new HashSet();
        hashSet.add(MODULE_GROUP_NAME);
        return hashSet;
    }
}
